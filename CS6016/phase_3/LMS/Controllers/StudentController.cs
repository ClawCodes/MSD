using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore.Migrations.Operations;
using Newtonsoft.Json.Linq;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS.Controllers
{
    [Authorize(Roles = "Student")]
    public class StudentController : Controller
    {
        private LMSContext db;
        public StudentController(LMSContext _db)
        {
            db = _db;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Catalog()
        {
            return View();
        }

        public IActionResult Class(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Assignment(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }


        public IActionResult ClassListings(string subject, string num)
        {
            System.Diagnostics.Debug.WriteLine(subject + num);
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            return View();
        }


        /*******Begin code to modify********/

        /// <summary>
        /// Returns a JSON array of the classes the given student is enrolled in.
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester
        /// "year" - The year part of the semester
        /// "grade" - The grade earned in the class, or "--" if one hasn't been assigned
        /// </summary>
        /// <param name="uid">The uid of the student</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var studentClasses = from e in db.Enrolleds
                                 join cls in db.Classes on e.Class equals cls.ClassId
                                 join course in db.Courses on cls.Listing equals course.CatalogId
                                 where e.Student == uid
                                 select new
                                 {
                                     subject = course.Department,
                                     number = course.Number,
                                     name = course.Name,
                                     season = cls.Season,
                                     year = cls.Year,
                                     grade = e.Grade ?? "--"
                                 };

            return Json(studentClasses.ToArray());
        }

        /// <summary>
        /// Returns a JSON array of all the assignments in the given class that the given student is enrolled in.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The category name that the assignment belongs to
        /// "due" - The due Date/Time
        /// "score" - The score earned by the student, or null if the student has not submitted to this assignment.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="uid"></param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInClass(string subject, int num, string season, int year, string uid)
        {
            var assignments = from course in db.Courses
                              join cls in db.Classes on course.CatalogId equals cls.Listing
                              where course.Department == subject
                                  && course.Number == num
                                  && cls.Season == season
                                  && cls.Year == year
                              join enroll in db.Enrolleds on cls.ClassId equals enroll.Class
                              where enroll.Student == uid
                              join cat in db.AssignmentCategories on cls.ClassId equals cat.InClass
                              join assign in db.Assignments on cat.CategoryId equals assign.Category
                              join sub in db.Submissions
                                  .Where(s => s.Student == uid)
                                  on assign.AssignmentId equals sub.Assignment into subs
                              from maybeSub in subs.DefaultIfEmpty()
                              select new
                              {
                                  aname = assign.Name,
                                  cname = cat.Name,
                                  due = assign.Due,
                                  score = maybeSub == null ? (decimal?)null : maybeSub.Score
                              };

            return Json(assignments.ToArray());
        }



        /// <summary>
        /// Adds a submission to the given assignment for the given student
        /// The submission should use the current time as its DateTime
        /// You can get the current time with DateTime.Now
        /// The score of the submission should start as 0 until a Professor grades it
        /// If a Student submits to an assignment again, it should replace the submission contents
        /// and the submission time (the score should remain the same).
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="uid">The student submitting the assignment</param>
        /// <param name="contents">The text contents of the student's submission</param>
        /// <returns>A JSON object containing {success = true/false}</returns>
        public IActionResult SubmitAssignmentText(string subject, int num, string season, int year,
        string category, string asgname, string uid, string contents)
        {
            var course = (from c in db.Courses
                          where c.Department == subject && c.Number == num
                          select c).FirstOrDefault();

            if (course == null)
                return Json(new { success = false });

            var assClass = (from c in db.Classes
                            where c.Listing == course.CatalogId
                             && c.Season == season
                             && c.Year == year
                            select c).FirstOrDefault();

            if (assClass == null)
                return Json(new { success = false });

            var categoryObj = (from ac in db.AssignmentCategories
                               where ac.InClass == assClass.ClassId
                               && ac.Name == category
                               select assClass).FirstOrDefault();

            if (categoryObj == null)
                return Json(new { success = false });


            var assignment = (from a in db.Assignments
                              where a.Name == asgname
                              select a).FirstOrDefault();

            if (assignment == null)
                return Json(new { success = false });


            var existingSubmission = (from s in db.Submissions
                                      where s.Assignment == assignment.AssignmentId &&
                                              s.Student == uid
                                      select s).FirstOrDefault();

            if (existingSubmission != null)
            {

                existingSubmission.SubmissionContents = contents;
                existingSubmission.Time = DateTime.Now;
            }
            else
            {

                var newSubmission = new Submission
                {
                    Assignment = assignment.AssignmentId,
                    Student = uid,
                    Time = DateTime.Now,
                    SubmissionContents = contents,
                    Score = 0
                };
                db.Submissions.Add(newSubmission);
            }

            db.SaveChanges();

            return Json(new { success = true });
        }


        /// <summary>
        /// Enrolls a student in a class.
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester</param>
        /// <param name="year">The year part of the semester</param>
        /// <param name="uid">The uid of the student</param>
        /// <returns>A JSON object containing {success = {true/false}. 
        /// false if the student is already enrolled in the class, true otherwise.</returns>
        public IActionResult Enroll(string subject, int num, string season, int year, string uid)
        {
            var alreadyEnrolled = (from e in db.Enrolleds
                                   join cls in db.Classes on e.Class equals cls.ClassId
                                   join crs in db.Courses on cls.Listing equals crs.CatalogId
                                   where e.Student == uid
                                   && crs.Department.ToUpper() == subject.ToUpper()
                                   && crs.Number == num
                                   && cls.Season.ToUpper() == season.ToUpper()
                                   && cls.Year == year
                                   select e).Any();

            if (alreadyEnrolled)
            {
                return Json(new { success = false });
            }

            var course = db.Courses.FirstOrDefault(c =>
                c.Department.ToUpper() == subject.ToUpper()
                && c.Number == num);

            if (course == null)
                return Json(new { success = false });

            // Find the class
            var classObj = db.Classes.FirstOrDefault(c =>
                c.Listing == course.CatalogId
                && c.Season.ToUpper() == season.ToUpper()
                && c.Year == year);

            if (classObj == null)
                return Json(new { success = false });


            var newEnrollment = new Enrolled
            {
                Class = classObj.ClassId,
                Student = uid,
                Grade = "--"
            };

            db.Enrolleds.Add(newEnrollment);
            db.SaveChanges();

            return Json(new { success = true });
        }




        /// <summary>
        /// Calculates a student's GPA
        /// A student's GPA is determined by the grade-point representation of the average grade in all their classes.
        /// Assume all classes are 4 credit hours.
        /// If a student does not have a grade in a class ("--"), that class is not counted in the average.
        /// If a student is not enrolled in any classes, they have a GPA of 0.0.
        /// Otherwise, the point-value of a letter grade is determined by the table on this page:
        /// https://advising.utah.edu/academic-standards/gpa-calculator-new.php
        /// </summary>
        /// <param name="uid">The uid of the student</param>
        /// <returns>A JSON object containing a single field called "gpa" with the number value</returns>
        public IActionResult GetGPA(string uid)
        {
            var gradeScale = new Dictionary<string, double>
            {
                ["A"] = 4.0,
                ["A-"] = 3.7,
                ["B+"] = 3.3,
                ["B"] = 3.0,
                ["B-"] = 2.7,
                ["C+"] = 2.3,
                ["C"] = 2.0,
                ["C-"] = 1.7,
                ["D+"] = 1.3,
                ["D"] = 1.0,
                ["D-"] = 0.7,
                ["E"] = 0.0
            };

            var grades = from e in db.Enrolleds
                         where e.Student == uid
                         && e.Grade != null
                         && e.Grade != "--"
                         select e.Grade;

            int count = 0;
            double totalPoints = 0.0;

            foreach (var grade in grades)
            {
                if (gradeScale.ContainsKey(grade))
                {
                    totalPoints += gradeScale[grade];
                    count++;
                }
            }

            double gpa = (count > 0) ? totalPoints / count : 0.0;

            return Json(new { gpa = gpa });
        }
        /*******End code to modify********/

    }
}

