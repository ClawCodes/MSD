using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Mvc;
using Microsoft.CodeAnalysis.CSharp.Syntax;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS.Controllers
{
    public class AdministratorController : Controller
    {
        private readonly LMSContext db;

        public AdministratorController(LMSContext _db)
        {
            db = _db;
        }

        // GET: /<controller>/
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Department(string subject)
        {
            ViewData["subject"] = subject;
            return View();
        }

        public IActionResult Course(string subject, string num)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            return View();
        }

        /*******Begin code to modify********/

        /// <summary>
        /// Create a department which is uniquely identified by it's subject code
        /// </summary>
        /// <param name="subject">the subject code</param>
        /// <param name="name">the full name of the department</param>
        /// <returns>A JSON object containing {success = true/false}.
        /// false if the department already exists, true otherwise.</returns>
        public IActionResult CreateDepartment(string subject, string name)
        {
            Department dep = new Department();
            dep.Name = name;
            dep.Subject = subject;
            db.Departments.Add(dep);
            db.SaveChanges();
            return Json(new { success = false});
        }


        /// <summary>
        /// Returns a JSON array of all the courses in the given department.
        /// Each object in the array should have the following fields:
        /// "number" - The course number (as in 5530)
        /// "name" - The course name (as in "Database Systems")
        /// </summary>
        /// <param name="subjCode">The department subject abbreviation (as in "CS")</param>
        /// <returns>The JSON result</returns>
        public IActionResult GetCourses(string subject)
        {
            var query = from dep in db.Departments
                        join course in db.Courses
                        on dep.Subject equals course.Department
                        where dep.Subject == subject
                        select new {
                            number=course.Number,
                            name=course.Name
                        };

            return Json(query);
        }

        /// <summary>
        /// Returns a JSON array of all the professors working in a given department.
        /// Each object in the array should have the following fields:
        /// "lname" - The professor's last name
        /// "fname" - The professor's first name
        /// "uid" - The professor's uid
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <returns>The JSON result</returns>
        public IActionResult GetProfessors(string subject)
        {
            var query = from dep in db.Departments
                        join prof in db.Professors
                        on dep.Subject equals prof.WorksIn
                        where dep.Subject == subject
                        select new {
                            lname=prof.LName,
                            fname=prof.FName,
                            uid=prof.UId
                        };

            return Json(query);
        }



        /// <summary>
        /// Creates a course.
        /// A course is uniquely identified by its number + the subject to which it belongs
        /// </summary>
        /// <param name="subject">The subject abbreviation for the department in which the course will be added</param>
        /// <param name="number">The course number</param>
        /// <param name="name">The course name</param>
        /// <returns>A JSON object containing {success = true/false}.
        /// false if the course already exists, true otherwise.</returns>
        public IActionResult CreateCourse(string subject, int number, string name)
        {           
            var query = (from c in db.Courses
                        where c.Department == subject
                        where c.Number == number
                        where c.Name == name
                        select c).FirstOrDefault();

            if (query == null){
                Course c = new Course();
                c.Number = (uint)number;
                c.Name = name;
                c.Department = subject;
                db.Courses.Add(c);
                db.SaveChanges();
                return Json(new { success = true });
            }

            return Json(new { success = false });
        }

        /// <summary>
        /// Creates a class offering of a given course.
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <param name="number">The course number</param>
        /// <param name="season">The season part of the semester</param>
        /// <param name="year">The year part of the semester</param>
        /// <param name="start">The start time</param>
        /// <param name="end">The end time</param>
        /// <param name="location">The location</param>
        /// <param name="instructor">The uid of the professor</param>
        /// <returns>A JSON object containing {success = true/false}. 
        /// false if another class occupies the same location during any time 
        /// within the start-end range in the same semester, or if there is already
        /// a Class offering of the same Course in the same Semester,
        /// true otherwise.</returns>
        public IActionResult CreateClass(string subject, int number, string season, int year, DateTime start, DateTime end, string location, string instructor)
        {   
            // same course 
            var existingOnLocationTime = from course in db.Courses
                        join class_ in db.Classes
                        on course.CatalogId equals class_.Listing
                        where course.Department == subject
                        where course.Number == number
                        where class_.Season == season
                        where class_.Year == year
                        select course;

            // Courses in the same location with overlapping times
            var existingOnCourse = from course in db.Courses
                        join class_ in db.Classes
                        on course.CatalogId equals class_.Listing
                        where class_.Location == location
                        where ((class_.StartTime >= TimeOnly.FromDateTime(start) && class_.EndTime <= TimeOnly.FromDateTime(end) && class_.StartTime > TimeOnly.FromDateTime(end))
                        || (class_.StartTime <= TimeOnly.FromDateTime(start) && class_.EndTime >= TimeOnly.FromDateTime(end) && class_.StartTime > TimeOnly.FromDateTime(end))) 
                        select course;

            var conflictingCourses = existingOnLocationTime.Union(existingOnCourse).FirstOrDefault();

            if (conflictingCourses == null){
                uint courseID = (from c in db.Courses
                               where c.Department == subject
                               where c.Number == number
                               select c.CatalogId).FirstOrDefault();

                Class class_ = new Class();
                class_.Season = season;
                class_.Year = (uint)year;
                class_.Location = location;
                class_.StartTime = TimeOnly.FromDateTime(start);
                class_.EndTime = TimeOnly.FromDateTime(end);
                class_.Listing = courseID;
                class_.TaughtBy = instructor;
                db.Classes.Add(class_);
                db.SaveChanges();
                return Json(new { success = true});
            }

            return Json(new { success = false});
        }


        /*******End code to modify********/

    }
}

