//
//  main.cpp
//  GiveChange
//
//  Created by Christopher Lawton on 8/20/24.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    int itemPrice, amountPaid, change;
    std::cout << "Enter item price in cents: ";
    std::cin >> itemPrice;
    
    std::cout << "Enter amount paid in cents: ";
    std::cin >> amountPaid;
    
    if (itemPrice <= 0 || amountPaid <= 0){
        std::cout << "ERROR: You cannot enter a negative number for the item price or the amount you paid.\n";
        return 1;
    }
    
    if (amountPaid < itemPrice){
        std::cout << "ERROR: You must provide enough money to pay for the item. The price is " << itemPrice << " and you paid " << amountPaid << "\n";
        return 1;
    }
    
    change = amountPaid - itemPrice;
    std::cout << "Change = " << change  << " cents\n";
    
    int remaining, numQuarters, numDimes, numNickels, numAvailable = 2;
    
    numQuarters = change / 25;
    if (numQuarters > numAvailable) {
        numQuarters = numAvailable;
    }
    remaining = change - (numQuarters * 25);
    
    numDimes = remaining / 10;
    if (numDimes > numAvailable) {
        numDimes = numAvailable;
    }
    remaining = remaining - (numDimes * 10);
    
    numNickels = remaining / 5;
    if (numNickels > numAvailable) {
        numNickels = numAvailable;
    }
    remaining = remaining - (numDimes * 5);
    
    if (remaining > 2){
        std::cout << "Sorry, the machine does not have enough coins to provide you with change. Returning your money.";
        return 1;
    }
    
    std::cout << "Quarters: " << numQuarters << "\n";
    std::cout << "Dimes: " << numDimes << "\n";
    std::cout << "Nickels: " << numNickels << "\n";
    std::cout << "Pennies: " << remaining << "\n";
    
    return 0;
}
