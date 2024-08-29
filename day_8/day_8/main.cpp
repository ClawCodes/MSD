//
//  main.cpp
//  Cards
//
//  Created by Christopher Lawton on 8/27/24.
//
// No partner worked on this with me (I was out of the classroom that day)
//
// RESULTS:
//Counts of hand types after 1000000 shuffles and deals:
//    Flush = 21545
//    Full house = 9523
//    Royal flush = 0
//    Straight = 2569
//    Straight flush = 6

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <map>
#include "hand.h"


int main(int argc, const char * argv[]) {
    testHand();
    std::map<std::string, int> handCounter;
    const std::string flush = "Flush";
    const std::string straight = "Straight";
    const std::string straightFlush = "Straight flush";
    const std::string royalFlush = "Royal flush";
    const std::string fullHouse = "Full house";
    
    handCounter[flush] = 0;
    handCounter[straight] = 0;
    handCounter[straightFlush] = 0;
    handCounter[royalFlush] = 0;
    handCounter[fullHouse] = 0;
    
    Deck deck = createDeck();
    
    int itercount = 0;
    int numHands = 1000000;
    while(itercount <= numHands){
        shuffleDeck(deck);
        Hand hand = dealHand(deck);
        
        // Check hands in order of highest to lowest score
        if (hand.isRoyalFlush())
            handCounter[royalFlush] += 1;
        else if (hand.isStraightFlush())
            handCounter[straightFlush] += 1;
        else if (hand.isFullHouse())
            handCounter[fullHouse] += 1;
        else if (hand.isFlush())
            handCounter[flush] += 1;
        else if (hand.isStraight())
            handCounter[straight] += 1;
        
        itercount += 1;
    }
    
    std::cout << "Counts of hand types after " << numHands << " shuffles and deals: " << std::endl;
    
    for (auto const& [key, value] : handCounter){
        std::cout << "\t" << key << " = " << value << std::endl;
    }
    
    return 0;
}
