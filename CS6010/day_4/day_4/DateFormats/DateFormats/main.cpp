//
//  main.cpp
//  DateFormats
//
//  Created by Alexia Pappas on 8/22/24.
//  Worked with Chris Lawton

#include <iostream>
using namespace std;
#include <string>

int main(int argc, const char * argv[]) {
   
    int month, day, year;
    string calendarDate, calendarMonth;
    
    cout << "Enter a date: \n";
    cin >> calendarDate;
    
    month = stoi(calendarDate.substr(0,2));
    day = stoi(calendarDate.substr(3,2));
    year = stoi(calendarDate.substr(6,4));
    
    if(!(month > 0 && month < 13) || !(day > 0 && day < 32) || !(year > 999 && year < 10000)){
        cout << "Invalid date";
    }
 
    if(month == 1){
        calendarMonth = "January";
    } else if (month == 2){
        calendarMonth = "February";
    } else if (month == 3){
        calendarMonth = "March";
    } else if (month == 4){
        calendarMonth = "April";
    } else if (month == 5){
        calendarMonth = "May";
    } else if (month == 6){
        calendarMonth = "June";
    } else if (month == 7){
        calendarMonth = "July";
    } else if (month == 8){
        calendarMonth = "August";
    } else if (month == 9){
        calendarMonth = "September";
    } else if (month == 10){
        calendarMonth = "October";
    } else if (month == 11){
        calendarMonth = "November";
    } else {
        calendarMonth = "December";
    }
    
    cout << calendarMonth + " " << day << ", " << year << endl;
    
    return 0;
}
