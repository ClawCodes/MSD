//
//  main.cpp
//  FunctionPractice
//
//  Created by Christopher Lawton on 8/23/24.
//

#include <iostream>
#include <cmath>

using namespace std;

float calculateHypotenuse(float length1, float length2){
    return sqrt(pow(length1, 2) + pow(length2, 2));
}

void calculateVelocity(double speed, double angle){
    double x = speed*cos(angle);
    double y = speed*sin(angle);
    
    cout << "Your x velocity is: " << x << endl;
    cout << "Your y velocity is: " << y << endl;
}

void exampleTime()
{
    std::time_t result = std::time(nullptr);
    std::cout << std::asctime(std::localtime(&result))
              << result << " seconds since the Epoch\n";
}

bool isCapitalized(string inputstr){
    if (inputstr[0] == tolower(inputstr[0])){
        return false;
    } else {
        return true;
    }
}

string boolToString(bool inputBool){
    if (inputBool == true){
        return "true";
    } else {
        return "false";
    }
}


int main(int argc, const char * argv[]) {
    
    // Section a: find hypotenuse length
    float length1, length2;
    cout << "Enter the side lengths of a right triangle " << endl;
    cin >> length1 >> length2;
    
    cout << "Your right triangle's hypotenuse length is: " << calculateHypotenuse(length1, length2) << endl;
    
    // Seciton b: Find velocity
    double speed, angle;
    cout << "Enter your speed" << endl;
    cin >> speed;
    cout << "Enter you angle" << endl;
    cin >> angle;
    
    calculateVelocity(speed, angle);
    
    // Section C:
    exampleTime();
    
    // Capitalize check
    string userInput;
    cout << "Please enter a word to see if the first letter is capitalized" << endl;
    cin >> userInput;
    
    string inputIsCapitalized = boolToString(isCapitalized(userInput));
    if (inputIsCapitalized == "true") {
        cout << "Your string starts with a capital letter" << endl;
    } else {
        cout << "Your string does NOT start with a capital letter" << endl;
    }
    
    return 0;
}
