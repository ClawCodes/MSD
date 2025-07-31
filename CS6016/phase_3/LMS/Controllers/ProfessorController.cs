using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Build.Experimental.ProjectCache;
using Microsoft.EntityFrameworkCore.Storage.Json;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS_CustomIdentity.Controllers
{
    [Authorize(Roles = "Professor")]
    public class ProfessorController : Controller
    {

        private readonly LMSContext db;

        public ProfessorController(LMSContext _db)
        {
            db = _db;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Students(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
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

        public IActionResult Categories(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult CatAssignments(string subject, string num, string season, string year, string cat)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
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

        public IActionResult Submissions(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Grade(string subject, string num, string season, string year, string cat, string aname, string uid)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            ViewData["uid"] = uid;
            return View();
        }

        /*******Begin code to modify********/


        /// <summary>
        /// Returns a JSON array of all the students in a class.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "dob" - date of birth
        /// "grade" - the student's grade in this class
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetStudentsInClass(string subject, int num, string season, int year)
        {
            var query = from course in db.Courses
                        join class_ in db.Classes
                        on course.CatalogId equals class_.Listing
                        join enroll in db.Enrolleds
                        on class_.ClassId equals enroll.Class
                        join stu in db.Students
                        on enroll.Student equals stu.UId
                        where course.Department == subject
                        where course.Number == num
                        where class_.Season == season
                        where class_.Year == year
                        select new
                        {
                            fname = stu.FName,
                            lname = stu.LName,
                            uid = stu.UId,
                            dob = stu.Dob,
                            grade = enroll.Grade
                        };

            return Json(query);
        }



        /// <summary>
        /// Returns a JSON array with all the assignments in an assignment category for a class.
        /// If the "category" parameter is null, return all assignments in the class.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The assignment category name.
        /// "due" - The due DateTime
        /// "submissions" - The number of submissions to the assignment
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class, 
        /// or null to return assignments from all categories</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInCategory(string subject, int num, string season, int year, string category)
        {
            // Get submission count by assignment
            var subGroups = (from sub in db.Submissions
                            group sub by sub.Assignment into g
                            select new {
                                assignId = g.Key,
                                count = g.Count()
                            }).ToList();

            // Get all assignments in a course
            var courseAssigns = (from course in db.Courses
                                join class_ in db.Classes
                                on course.CatalogId equals class_.Listing
                                join cat in db.AssignmentCategories
                                on class_.ClassId equals cat.InClass
                                join assign in db.Assignments
                                on cat.CategoryId equals assign.Category
                                where course.Department == subject
                                where course.Number == num
                                where class_.Season == season
                                where class_.Year == year
                                select new {
                                    assign.AssignmentId,
                                    assign.Name,
                                    CatName = cat.Name,
                                    assign.Due
                                }).ToList();

            // Join submission counts to the respective assignments
            var query = from c in courseAssigns
                        join subg in subGroups
                        on c.AssignmentId equals subg.assignId into gc
                        from g in gc.DefaultIfEmpty()
                        select new {
                            aname = c.Name,
                            cname = c.CatName,
                            due = c.Due,
                            submissions = g == null ? 0 : g.count
                        };

            // Filter by category if provided
            if (category != null){

                query = from q in query
                        where q.cname == category
                        select q;
            }

            return Json(query);
        }


        /// <summary>
        /// Returns a JSON array of the assignment categories for a certain class.
        /// Each object in the array should have the folling fields:
        /// "name" - The category name
        /// "weight" - The category weight
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentCategories(string subject, int num, string season, int year)
        {

            var query = from course in db.Courses
                        join class_ in db.Classes
                        on course.CatalogId equals class_.Listing
                        join cat in db.AssignmentCategories
                        on class_.ClassId equals cat.InClass
                        where course.Department == subject
                        where course.Number == num
                        where class_.Season == season
                        where class_.Year == year
                        select new
                        {
                            name = cat.Name,
                            weight = cat.Weight
                        };

            return Json(query);
        }

        /// <summary>
        /// Creates a new assignment category for the specified class.
        /// If a category of the given class with the given name already exists, return success = false.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The new category name</param>
        /// <param name="catweight">The new category weight</param>
        /// <returns>A JSON object containing {success = true/false} </returns>
        public IActionResult CreateAssignmentCategory(string subject, int num, string season, int year, string category, int catweight)
        {
            // Get class ID to add into assignment cats
            uint? classID = (from course in db.Courses
                             join clazz in db.Classes
                             on course.CatalogId equals clazz.Listing
                             where course.Department == subject
                             where course.Number == num
                             where clazz.Season == season
                             where clazz.Year == year
                             select clazz.ClassId).FirstOrDefault();

            if (classID == null || classID == 0)
            {
                return Json(new { success = false });
            }

            // Check if a category with the same name already exists for the class
            bool exists = db.AssignmentCategories
                .Any(c => c.InClass == classID && c.Name == category);

            if (exists)
            {
                return Json(new { success = false });
            }

            AssignmentCategory cat = new AssignmentCategory
            {
                Name = category,
                Weight = (uint)catweight,
                InClass = (uint)classID
            };

            db.AssignmentCategories.Add(cat);
            db.SaveChanges();

            return Json(new { success = true });
        }


        /// <summary>
        /// Creates a new assignment for the given class and category.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="asgpoints">The max point value for the new assignment</param>
        /// <param name="asgdue">The due DateTime for the new assignment</param>
        /// <param name="asgcontents">The contents of the new assignment</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult CreateAssignment(string subject, int num, string season, int year, string category, string asgname, int asgpoints, DateTime asgdue, string asgcontents)
        {
            uint? categoryID = (from course in db.Courses
                                join class_ in db.Classes
                                on course.CatalogId equals class_.Listing
                                join cat in db.AssignmentCategories
                                on class_.ClassId equals cat.InClass
                                where course.Department == subject
                                where course.Number == num
                                where class_.Season == season
                                where class_.Year == year
                                where cat.Name == category
                                select (uint?)cat.CategoryId).FirstOrDefault();


            if (categoryID == 0)
            {
                return Json(new { success = false });
            }

            Assignment a = new Assignment();
            a.Name = asgname;
            a.Contents = asgcontents;
            a.Due = asgdue;
            a.MaxPoints = (uint)asgpoints;
            a.Category = (uint)categoryID;
            db.Assignments.Add(a);

            var students = (from course in db.Courses
                           join clazz in db.Classes
                               on course.CatalogId equals clazz.Listing
                           join enrollment in db.Enrolleds
                               on clazz.ClassId equals enrollment.Class
                           join student in db.Students
                               on enrollment.Student equals student.UId
                           where course.Number == num
                           select new
                           {
                               student.UId,
                               clazz.ClassId,
                           }).ToList();


            foreach (var student in students)
                CalculateStudentGrade(student.UId, student.ClassId);

            db.SaveChanges();

            return Json(new { success = true });
        }


        /// <summary>
        /// Gets a JSON array of all the submissions to a certain assignment.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "time" - DateTime of the submission
        /// "score" - The score given to the submission
        /// 
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetSubmissionsToAssignment(string subject, int num, string season, int year, string category, string asgname)
        {
            var query = from course in db.Courses
                        join class_ in db.Classes
                        on course.CatalogId equals class_.Listing
                        join cat in db.AssignmentCategories
                        on class_.ClassId equals cat.InClass
                        join assign in db.Assignments
                        on cat.CategoryId equals assign.Category
                        join sub in db.Submissions
                        on assign.AssignmentId equals sub.Assignment
                        join stu in db.Students
                        on sub.Student equals stu.UId
                        where course.Department == subject
                        where course.Number == num
                        where class_.Season == season
                        where class_.Year == year
                        where assign.Name == asgname
                        select new
                        {
                            fname = stu.FName,
                            lname = stu.LName,
                            uid = stu.UId,
                            time = sub.Time,
                            score = sub.Score
                        };

            return Json(query);
        }


        /// <summary>
        /// Set the score of an assignment submission
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <param name="uid">The uid of the student who's submission is being graded</param>
        /// <param name="score">The new score for the submission</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult GradeSubmission(string subject, int num, string season, int year, string category, string asgname, string uid, int score)
        {
            var submission = (from course in db.Courses
                              join class_ in db.Classes on course.CatalogId equals class_.Listing
                              join cat in db.AssignmentCategories on class_.ClassId equals cat.InClass
                              join asg in db.Assignments on cat.CategoryId equals asg.Category
                              join sub in db.Submissions on asg.AssignmentId equals sub.Assignment
                              where course.Department == subject
                                    && course.Number == num
                                    && class_.Season == season
                                    && class_.Year == year
                                    && cat.Name == category
                                    && asg.Name == asgname
                                    && sub.Student == uid
                              select sub).FirstOrDefault();

            if (submission != null)
            {
                submission.Score = (uint)score;
                db.SaveChanges();

                var classId = (from course in db.Courses
                               join class_ in db.Classes on course.CatalogId equals class_.Listing
                               where course.Department == subject
                                     && course.Number == num
                                     && class_.Season == season
                                     && class_.Year == year
                               select class_.ClassId).FirstOrDefault();

                CalculateStudentGrade(uid, classId);

                return Json(new { success = true });
            }

            return Json(new { success = false });
        }


        private void CalculateStudentGrade(string uid, uint? classId)
        {
            var enrollment = db.Enrolleds
                .FirstOrDefault(e => e.Student == uid && e.Class == classId);

            if (enrollment == null)
                return;

            var categories = db.AssignmentCategories
                .Where(c => c.InClass == classId)
                .ToList();

            double totalWeightedScore = 0.0;
            double totalWeight = 0.0;

            foreach (var category in categories)
            {
                var assignments = db.Assignments
                    .Where(a => a.Category == category.CategoryId)
                    .ToList();

                if (assignments.Count == 0)
                    continue;

                double earnedPoints = 0.0;
                double maxPoints = 0.0;

                foreach (var assignment in assignments)
                {
                    maxPoints += assignment.MaxPoints;

                    var submission = db.Submissions
                        .FirstOrDefault(s => s.Assignment == assignment.AssignmentId && s.Student == uid);

                    earnedPoints += submission != null ? submission.Score : 0;
                }

                double categoryPercentage = earnedPoints / maxPoints;
                totalWeightedScore += categoryPercentage * category.Weight;
                totalWeight += category.Weight;
            }

            string letterGrade = "--";

            if (totalWeight > 0)
            {
                double scaledScore = totalWeightedScore * (100.0 / totalWeight);

                if (scaledScore >= 93) letterGrade = "A";
                else if (scaledScore >= 90) letterGrade = "A-";
                else if (scaledScore >= 87) letterGrade = "B+";
                else if (scaledScore >= 83) letterGrade = "B";
                else if (scaledScore >= 80) letterGrade = "B-";
                else if (scaledScore >= 77) letterGrade = "C+";
                else if (scaledScore >= 73) letterGrade = "C";
                else if (scaledScore >= 70) letterGrade = "C-";
                else if (scaledScore >= 67) letterGrade = "D+";
                else if (scaledScore >= 63) letterGrade = "D";
                else if (scaledScore >= 60) letterGrade = "D-";
                else letterGrade = "E";
            }

            enrollment.Grade = letterGrade;
            db.SaveChanges();
        }


        /// <summary>
        /// Returns a JSON array of the classes taught by the specified professor
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester in which the class is taught
        /// "year" - The year part of the semester in which the class is taught
        /// </summary>
        /// <param name="uid">The professor's uid</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var query = from prof in db.Professors
                        join class_ in db.Classes
                        on prof.UId equals class_.TaughtBy
                        join course in db.Courses
                        on class_.Listing equals course.CatalogId
                        where prof.UId == uid
                        select new
                        {
                            subject = course.Department,
                            number = course.Number,
                            name = course.Name,
                            season = class_.Season,
                            year = class_.Year
                        };

            return Json(query);
        }
        /*******End code to modify********/
    }
}

