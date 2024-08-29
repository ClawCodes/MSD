//
//  main.cpp
//  Cards
//
//  Created by Christopher Lawton on 8/27/24.
//

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cassert>
#include <map>
#include "deck.h"
#include "hand.h"


int main(int argc, const char * argv[]) {
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
    int numHands = 100;
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
        std::cout << key << " = " << value << std::endl;
    }
    
    return 0;
}
