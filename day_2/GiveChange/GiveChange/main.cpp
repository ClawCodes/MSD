//
//  main.cpp
//  GiveChange
//
//  Created by Christopher Lawton on 8/20/24.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    std::cout << "Enter item price in cents: ";
    int itemPrice;
    std::cin >> itemPrice;
    
    std::cout << "Enter amount paid in cents: ";
    int amountPaid;
    std::cin >> amountPaid;
    
    int change = amountPaid - itemPrice;
    std::cout << "Change = " << change  << " cents\n";
    
    int remaining;
    
    int numQuarters = change / 25;
    remaining = change % 25;
    
    int numDimes = remaining / 10;
    remaining = remaining % 10;
    
    int numNickels = remaining / 5;
    remaining = remaining % 5;
    
    std::cout << "Quarters: " << numQuarters << "\n";
    std::cout << "Dimes: " << numDimes << "\n";
    std::cout << "Nickels: " << numNickels << "\n";
    std::cout << "Pennies: " << remaining << "\n";
    
    return 0;
}
