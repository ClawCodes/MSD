using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text.Json;
using System.Text.Json.Nodes;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Mvc;
using Microsoft.CodeAnalysis.CSharp.Syntax;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Storage.Json;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS.Controllers
{
    public class CommonController : Controller
    {
        private readonly LMSContext db;

        public CommonController(LMSContext _db)
        {
            db = _db;
        }

        /*******Begin code to modify********/
        /// <summary>
        /// Retreive a JSON array of all departments from the database.
        /// Each object in the array should have a field called "name" and "subject",
        /// where "name" is the department name and "subject" is the subject abbreviation.
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetDepartments()
        {
            var departments = from d in db.Departments
                              select new
                              {
                                  name = d.Name,
                                  subject = d.Subject
                              };

            return Json(departments);
        }




        /// <summary>
        /// Returns a JSON array representing the course catalog.
        /// Each object in the array should have the following fields:
        /// "subject": The subject abbreviation, (e.g. "CS")
        /// "dname": The department name, as in "Computer Science"
        /// "courses": An array of JSON objects representing the courses in the department.
        ///            Each field in this inner-array should have the following fields:
        ///            "number": The course number (e.g. 5530)
        ///            "cname": The course name (e.g. "Database Systems")
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetCatalog()
        {
            var courseCatalogue = from d in db.Departments
                                  select new
                                  {
                                      subject = d.Subject,
                                      dname = d.Name,
                                      courses = (from c in db.Courses
                                                 where c.Department == d.Subject
                                                 select new
                                                 {
                                                     number = c.Number,
                                                     cname = c.Name
                                                 }).ToList()
                                  };
            return Json(courseCatalogue);
        }

        /// <summary>
        /// Returns a JSON array of all class offerings of a specific course.
        /// Each object in the array should have the following fields:
        /// "season": the season part of the semester, such as "Fall"
        /// "year": the year part of the semester
        /// "location": the location of the class
        /// "start": the start time in format "hh:mm:ss"
        /// "end": the end time in format "hh:mm:ss"
        /// "fname": the first name of the professor
        /// "lname": the last name of the professor
        /// </summary>
        /// <param name="subject">The subject abbreviation, as in "CS"</param>
        /// <param name="number">The course number, as in 5530</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetClassOfferings(string subject, int number)
        {
            var classOfferings = from cls in db.Classes
                                 join course in db.Courses
                                 on cls.Listing equals course.CatalogId
                                 where course.Department == subject && course.Number == number
                                 join prof in db.Professors
                                 on cls.TaughtBy equals prof.UId
                                 select new
                                 {
                                     season = cls.Season,
                                     year = cls.Year,
                                     location = cls.Location,
                                     start = cls.StartTime.ToString(@"hh\:mm\:ss"),
                                     end = cls.EndTime.ToString(@"hh\:mm\:ss"),
                                     fname = prof.FName,
                                     lname = prof.LName
                                 };

            return Json(classOfferings.ToList());
        }

        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <returns>The assignment contents</returns>
        public IActionResult GetAssignmentContents(string subject, int num, string season, int year, string category, string asgname)
        {
            var assignmentContents = (from a in db.Assignments
                                      join assCat in db.AssignmentCategories
                                      on a.Category equals assCat.CategoryId
                                      join c in db.Classes
                                      on assCat.InClass equals c.ClassId
                                      join cc in db.Courses
                                      on c.Listing equals cc.CatalogId
                                      join d in db.Departments
                                      on cc.Department equals d.Subject
                                      where subject == d.Subject
                                          && num == cc.Number
                                          && season == c.Season
                                          && year == c.Year
                                          && category == assCat.Name
                                          && asgname == a.Name
                                      select a.Contents).ToList();

            if (assignmentContents.Count > 1)
                throw new Exception("Cannot return more than one assignment's content");

            string contents = assignmentContents.FirstOrDefault() ?? "";
            return Content(contents);
        }



        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment submission.
        /// Returns the empty string ("") if there is no submission.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <param name="uid">The uid of the student who submitted it</param>
        /// <returns>The submission text</returns>
        public IActionResult GetSubmissionText(string subject, int num, string season, int year, string category, string asgname, string uid)
        {
            var submission = (from course in db.Courses
                              where course.Department == subject && course.Number == num
                              join cls in db.Classes
                              on course.CatalogId equals cls.Listing
                              where cls.Season == season && cls.Year == year
                              join cat in db.AssignmentCategories
                              on cls.ClassId equals cat.InClass
                              where cat.Name == category
                              join asg in db.Assignments
                              on cat.CategoryId equals asg.Category
                              where asg.Name == asgname
                              join sub in db.Submissions
                              on asg.AssignmentId equals sub.Assignment
                              where sub.Student == uid
                              select sub.SubmissionContents).FirstOrDefault();

            // If no submission was found, return an empty string
            return Content(submission ?? "");
        }



        /// <summary>
        /// Gets information about a user as a single JSON object.
        /// The object should have the following fields:
        /// "fname": the user's first name
        /// "lname": the user's last name
        /// "uid": the user's uid
        /// "department": (professors and students only) the name (such as "Computer Science") of the department for the user. 
        ///               If the user is a Professor, this is the department they work in.
        ///               If the user is a Student, this is the department they major in.    
        ///               If the user is an Administrator, this field is not present in the returned JSON
        /// </summary>
        /// <param name="uid">The ID of the user</param>
        /// <returns>
        /// The user JSON object 
        /// or an object containing {success: false} if the user doesn't exist
        /// </returns>
        public IActionResult GetUser(string uid)
        {
            var student = (from s in db.Students
                           where s.UId == uid
                           select new
                           {
                               fname = s.FName,
                               lname = s.LName,
                               uid = s.UId,
                               department = s.Major
                           }).FirstOrDefault();

            if (student != null)
                return Json(student);

            var professor = (from p in db.Professors
                             where p.UId == uid
                             select new
                             {
                                 fname = p.FName,
                                 lname = p.LName,
                                 uid = p.UId,
                                 department = p.WorksIn
                             }).FirstOrDefault();

            if (professor != null)
                return Json(professor);

            var admin = (from a in db.Administrators
                         where a.UId == uid
                         select new
                         {
                             fname = a.FName,
                             lname = a.LName,
                             uid = a.UId
                         }).FirstOrDefault();

            if (admin != null)
                return Json(admin);

            return Json(new { success = false });
        }


        /*******End code to modify********/
    }
}

