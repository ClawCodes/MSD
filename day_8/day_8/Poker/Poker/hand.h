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
#include "card.h"

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
        CardRank smallestRank = sort_cards(cards)[0].rank;
        return smallestRank == TEN && isStraightFlush();
    }
    bool isFullHouse(){
        
        return true;
    }
};


/// <#Description#> Function to run assertions against member functions of the Hand struct
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
        Card{THREE, hearts}
        }
    };
    
    assert(stright.isStraight());
    
    // Assert hand is not stright
    Hand notStright = Hand{
        {
        Card{KING, hearts},
        Card{ACE, clubs},
        Card{FOUR, diamonds},
        Card{THREE, hearts}
        }
    };
    
    assert(!(notStright.isStraight()));
    
    // Assert hand is stright flush
    Hand strightFlush = Hand{
        {
        Card{TWO, hearts},
        Card{ACE, hearts},
        Card{FOUR, hearts},
        Card{THREE, hearts}
        }
    };
    
    assert(strightFlush.isStraightFlush());
    
    // Assert hand is NOT stright flush
    Hand notStrightFlush = Hand{
        {
        Card{TWO, hearts},
        Card{ACE, hearts},
        Card{FOUR, hearts},
        Card{THREE, clubs}
        }
    };
    
    assert(!(notStrightFlush.isStraightFlush()));
    
    // Assert hand is a royal flush
    Hand royalFlush = Hand{
        {
        Card{TEN, hearts},
        Card{JACK, hearts},
        Card{QUEEN, hearts},
        Card{KING, hearts}
        }
    };
    
    assert(royalFlush.isRoyalFlush());
    
    // Assert hand is NOT a royal flush
    Hand notRoyalFlush = Hand{
        {
        Card{JACK, clubs},
        Card{JACK, hearts},
        Card{QUEEN, hearts},
        Card{KING, hearts}
        }
    };
    
    assert(royalFlush.isRoyalFlush());
}

#endif /* hand_h */
