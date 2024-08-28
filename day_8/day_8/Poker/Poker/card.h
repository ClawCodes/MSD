//
//  card.h
//  Cards
//
//  Created by Christopher Lawton on 8/27/24.
//

#ifndef card_h
#define card_h

#include <iostream>
#include <vector>

// Enumeration of card ranks to enforce a limited set of allowed ranks
// ORDER MATTERS
enum CardRank {
    ACE = 1,
    TWO = 2,
    THREE = 3,
    FOUR = 4,
    FIVE = 5,
    SIX = 6,
    SEVEN = 7,
    EIGHT = 8,
    NINE = 9,
    TEN = 10,
    JACK = 11,
    QUEEN = 12,
    KING = 13
};

// Vector of ranks in standard sorted order for convenient iteration
const std::vector<CardRank> allRanks = {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING
};

// Create constants for reusable suit names
const std::string hearts = "Hearts";
const std::string spades = "Diamonds";
const std::string diamonds = "Diamonds";
const std::string clubs = "Clubs";

// Vector of suit names in standard sorted order for convenient iteration
const std::vector<std::string> allSuits = {hearts, clubs, diamonds, spades};

// Representation of an individual card
struct Card{
    CardRank rank;
    std::string suit;
    
    /// <#Description#> Member function to get card representation as string containing Suit, Name (human readable), and rank.
    /// returns - string of representation
    std::string getCardRepr(){
        std::string rankName;
        if (rank == ACE)
            rankName = "Ace";
        else if (rank == KING)
            rankName = "King";
        else if (rank == QUEEN)
            rankName = "Queen";
        else if (rank == JACK)
            rankName = "Jack";
        else
            rankName = "pip";
        
        return "Card{Suit: " + suit + ", Name: " + rankName + ", Rank: " + std::to_string(rank) + "}";
    }
    /// <#Description#> member function to print the representation of the Card
    void printCard(){
        std::cout << getCardRepr() << std::endl;
    }
};

#endif /* card_h */
