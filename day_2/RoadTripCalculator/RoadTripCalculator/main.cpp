//
//  main.cpp
//  RoadTripCalculator
//
//  Created by Christopher Lawton on 8/20/24.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    int drivingDistance;
    int mpg;
    float costPerGallon;
    std::cout << "Enter driving distance, MPG, and cost per gallon\n";
    std::cin >> drivingDistance >> mpg >> costPerGallon;
    float gallonsUsed = drivingDistance / mpg;
    float tripCost = costPerGallon * gallonsUsed;
    std::cout << "Your total trip cost is: " << tripCost << "\n";
    return 0;
}
