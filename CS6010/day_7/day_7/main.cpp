//
//  main.cpp
//  day_7
//
//  Created by Christopher Lawton on 8/27/24.
//
// Scratch program for day 7 lecture examples

#include <iostream>
#include <string>
#include <numeric>

struct Student{
    std::string name;
    int idNumber;
    std::vector<double> grades;
    double gpa;
};

// function that calculates the average grade for a student.
double calculateAvg(Student student){
    std::vector<double> grades = student.grades;
    int sum = 0;
    for (int i = 0; i < grades.size(); i++){
        sum += grades[i];
    }
    return sum / grades.size();
}

int main(int argc, const char * argv[]) {
    // Create student struct
    
    Student student1;
    student1.name = "Ahmad";
    student1.gpa = 70.1;
    student1.idNumber = 233232;
    student1.grades.push_back(50);
    student1.grades.push_back(60);
    
    // Vector of students
    std::vector<Student> students;
    students.push_back(student1);
    
    std::cout << "The student average = " << calculateAvg(student1) << std::endl;
    
    return 0;
}
