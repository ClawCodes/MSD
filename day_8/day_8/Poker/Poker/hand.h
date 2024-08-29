//
//  hand.h
//  Poker
//
//  Created by Christopher Lawton on 8/28/24.
//

#ifndef hand_h
#define hand_h

#include <algorithm>
#include <string>
#include <cassert>
#include <map>
#include "deck.h"

struct Hand{
//    Card cards[5];
    std::vector<Card> cards;
    
    /// <#Description#> Determine if the hand is a flush
    bool isFlush(){
        std::string suit = cards[0].suit;
        for(int i = 1; i < cards.size(); i++){
            if (suit != cards[i].suit)
                return false;
        }
        return true;
    }
    /// <#Description#> Determine if the hand is a stright
    bool isStraight(){
        // First sort cards by rank in ascending order
        std::vector<Card> sortedCards = sort_cards(cards);
        
        // If an ace exists and the following card is not two
        // then we can consider the ace high
        if (sortedCards[0].rank == ACE && sortedCards[1].rank != 2){
            CardRank* aceRank = &sortedCards[0].rank;
            *aceRank = ACEHIGH;
            sortedCards = sort_cards(sortedCards);
        }
        
        CardRank previous = sortedCards[0].rank;
        for (int i = 1; i < sortedCards.size(); i++){
            if (previous + 1 != sortedCards[i].rank)
                return false;
            else
                previous = sortedCards[i].rank;
        }
        return true;
    }
    /// <#Description#> Determine if the hand is a stright flush
    bool isStraightFlush(){
        return isFlush() && isStraight();
    }
    /// <#Description#> Determine if the hand is a royal flush
    bool isRoyalFlush(){
        std::vector<Card> sortedCards = sort_cards(cards);
        return sortedCards[0].rank == ACE && sortedCards[1].rank == TEN && isStraightFlush();
    }
    bool isFullHouse(){
        std::map<std::string, int> suitCounter;
        for (Card card : cards){
            std::string suit = card.suit;
            if (suitCounter.count(suit))
                suitCounter[suit] += 1;
            // If we already have 2 suits captured and found a new one then it's not a fullhouse
            else if (suitCounter.size() == 2)
                return false;
            else
                suitCounter[suit] = 1;
        }
        int firstSuitCount = suitCounter.begin()->second;
        // At this point there are only two suits
        return firstSuitCount == 2 || firstSuitCount == 3;
    }
};

Hand dealHand(Deck &deck);


// Function to run assertions against member functions of the Hand struct
void testHand(){
    // Assert hand is flush
    Hand flush = Hand{
        {
            Card{ACE, clubs},
            Card{KING, clubs},
            Card{QUEEN, clubs},
            Card{JACK, clubs},
            Card{TEN, clubs}
        }
    };
    assert(flush.isFlush());
    
    // Assert hand is not flush
    Hand notFlush = Hand{
        {
            Card{ACE, clubs},
            Card{KING, hearts},
            Card{QUEEN, clubs},
            Card{JACK, clubs},
            Card{TEN, clubs}
        }
    };
    assert(!(notFlush.isFlush()));

    // Assert hand is stright
    Hand stright = Hand{
        {
            Card{TWO, hearts},
            Card{ACE, clubs},
            Card{FOUR, diamonds},
            Card{THREE, hearts},
            Card{FIVE, hearts}
        }
    };
    
    assert(stright.isStraight());
    
    // Assert hand is not stright
    Hand notStright = Hand{
        {
            Card{KING, hearts},
            Card{ACE, clubs},
            Card{FOUR, diamonds},
            Card{THREE, hearts},
            Card{FIVE, hearts}
        }
    };
    
    assert(!(notStright.isStraight()));
    
    // Assert hand is stright with Ace high
    Hand strightAceHigh = Hand{
        {
            Card{TEN, hearts},
            Card{ACE, clubs},
            Card{JACK, diamonds},
            Card{KING, hearts},
            Card{QUEEN, hearts}
        }
    };
    
    assert(stright.isStraight());
    
    // Assert hand is stright flush
    Hand strightFlush = Hand{
        {
            Card{TWO, hearts},
            Card{ACE, hearts},
            Card{FOUR, hearts},
            Card{THREE, hearts},
            Card{FIVE, hearts}
        }
    };
    
    assert(strightFlush.isStraightFlush());
    
    // Assert hand is NOT stright flush
    Hand notStrightFlush = Hand{
        {
            Card{TWO, hearts},
            Card{ACE, hearts},
            Card{FOUR, hearts},
            Card{THREE, clubs},
            Card{FIVE, hearts}
        }
    };
    
    assert(!(notStrightFlush.isStraightFlush()));
    
    // Assert hand is a royal flush
    Hand royalFlush = Hand{
        {
            Card{TEN, hearts},
            Card{JACK, hearts},
            Card{QUEEN, hearts},
            Card{KING, hearts},
            Card{ACE, hearts},
        }
    };
    
    assert(royalFlush.isRoyalFlush());
    
    // Assert hand is NOT a royal flush
    Hand notRoyalFlush = Hand{
        {
            Card{JACK, clubs},
            Card{JACK, hearts},
            Card{QUEEN, hearts},
            Card{KING, hearts},
            Card{ACE, hearts},
        }
    };
    
    assert(royalFlush.isRoyalFlush());
    
    // Assert hand is a full house
    Hand fullHouse = Hand{
        {
            Card{TEN, hearts},
            Card{JACK, hearts},
            Card{QUEEN, hearts},
            Card{KING, clubs},
            Card{THREE, clubs}
        }
    };
    
    assert(fullHouse.isFullHouse());
    
    // Assert hand is a full house
    Hand notFullHouse = Hand{
        {
            Card{TEN, hearts},
            Card{JACK, hearts},
            Card{QUEEN, diamonds},
            Card{KING, clubs},
            Card{THREE, clubs}
        }
    };
    
    assert(!(notFullHouse.isFullHouse()));
}

#endif /* hand_h */
