//
//  main.cpp
//  day_21
//
//  Created by Christopher Lawton on 9/17/24.
//
// Scratch file for iterators and Standard Template Library (STL)

#include <iostream>
#include <vector>

class Student{
private:
    int grade;
    std::string name;
    
public:
    Student(const int grade, const std::string name){
        this->grade = grade;
        this->name = name;
    }
    int getGrade(){return grade;};
    std::string getName(){return name;};
    
    bool operator<(const Student& rhs) const {
        return this->grade < rhs.grade;
    }
};

void printNumbers(int x){
    std::cout << x << std::endl;
}


int main(int argc, const char * argv[]) {
    std::vector<int> numbers = {2,3,5,6,1,4};
    
    std::sort(numbers.begin(), numbers.end());
    
    for(int num: numbers)
        std::cout << num << std::endl;
    
    
    auto index = std::find(numbers.begin(), numbers.end(), 5);
    std::cout << *index << std::endl;
    
    std::cout<< std::distance(numbers.begin(), index) << std::endl;
    
    
    std::vector<Student> students = {{90, "Ahmad"}, {80, "John"}, {70, "Ben"}};
    
    
    // To sort on a custom type you need to include a equality operator overload otherwise there is no way to know how the type can be sorted
    std::sort(students.begin(), students.end());
    
    // Go through every element in the specified range and perform the function on each element
    std::for_each(numbers.begin(), numbers.end(), printNumbers);
    // OR with anonymous function (lambdas)
    // Square brackets are called the "capture list"
    std::for_each(numbers.begin(), numbers.end(), [](int x){std::cout << x << std::endl;});
    
    // You can assign lambda functions. Auto is required becuase the function has not name.
    auto newFunction = [](int x){std::cout<<x<<std::endl;};
    
    // Update in place with a reference
    auto inPlaceFunction = [](int& x){x = x*x;};
    
    // Transform lets you perform operations on one container and construct a new container
    std::vector<int> numbersPower(numbers.size());
    std::transform(numbers.begin(), numbers.end(), numbersPower, [](int x){return x*x;});
    
    std::for_each(numbersPower.begin(), numbersPower.end(), [](int x){std::cout << x << std::endl;});
    
    return 0;
}
