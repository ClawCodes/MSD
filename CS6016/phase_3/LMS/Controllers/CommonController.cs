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
            var classOfferings = from c in db.Classes
                                 join p in db.Professors
                                 on c.TaughtBy equals p.UId
                                 select new
                                 {
                                     season = c.Season,
                                     year = c.Year,
                                     location = c.Location,
                                     start = c.StartTime,
                                     end = c.EndTime,
                                     fname = p.FName,
                                     lname = p.LName
                                 };
            return Json(classOfferings);
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
            var assignmentContent = (from a in db.Assignments
                             join assCat in db.AssignmentCategories
                             on a.Category equals assCat.CategoryId
                             into fullAss

                             from fa in fullAss.DefaultIfEmpty()
                             join c in db.Classes
                             on fa.InClass equals c.ClassId
                             into assClass

                             from ac in assClass.DefaultIfEmpty()
                             join cc in db.Courses
                             on ac.Listing equals cc.CatalogId
                             into join3

                             from j3 in join3.DefaultIfEmpty()
                             join d in db.Departments
                             on j3.Department equals d.Subject
                             into join4

                             from j4 in join4.DefaultIfEmpty()
                             where subject == j4.Subject 
                             && num == j3.Number
                             && season == ac.Season
                             && year == ac.Year
                             && category == fa.Name
                             && asgname == a.Name
                             select new
                             {
                                 content = a.Contents
                             }).ToList();

            if (assignmentContent.Count > 1)
                throw new Exception("Cannot return more than one assignment's content");

            string contents = assignmentContent.ToString();
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
            var submission = (from a in db.Assignments
                              join assCat in db.AssignmentCategories
                              on a.Category equals assCat.CategoryId
                              into fullAss

                              from fa in fullAss.DefaultIfEmpty()
                              join c in db.Classes
                              on fa.InClass equals c.ClassId
                              into assClass

                              from ac in assClass.DefaultIfEmpty()
                              join cc in db.Courses
                              on ac.Listing equals cc.CatalogId
                              into join3

                              from j3 in join3.DefaultIfEmpty()
                              join d in db.Departments
                              on j3.Department equals d.Subject
                              into join4

                              from j4 in join4.DefaultIfEmpty()
                              join sub in db.Submissions
                              on a.AssignmentId equals sub.Assignment
                              into join5

                              from j5 in join5.DefaultIfEmpty()
                              join stu in db.Students
                              // TODO on
                              where subject == j4.Subject
                             && num == j3.Number
                             && season == ac.Season
                             && year == ac.Year
                             && category == fa.Name
                             && asgname == a.Name
                             // TODO uid match
                            //  && uid == 
                              select new
                              {
                                  submission = j5.SubmissionContents
                              }).ToList()

            string submissionText = "";

            if (submission.Count > 1)
                throw new Exception("Cannot return more than one assignment's submissions");
            if (submission.Count == 0)
                return Content(submissionText);

            submissionText = submission.ToString();
            return Content(submissionText);
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
            return Json(new { success = false });
        }


        /*******End code to modify********/
    }
}

