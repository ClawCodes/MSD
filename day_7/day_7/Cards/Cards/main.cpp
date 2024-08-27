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


enum DeckType{
    STRIPPED,
    STANDARD
};

struct Deck{
    DeckType type;
    std::vector<Card> cards;
    
    std::string getTypeName(){
        if (type == STANDARD)
            return "standard";
        else
            return "stripped";
    }
    void printDeck(){
        std::string cardRepr;
        if (cards.size() == 0)
            cardRepr = "NO CARDS IN DECK";
        else if (cards.size() == 1)
            cardRepr = cards[0].getCardRepr();
        else
            cardRepr = cards[0].getCardRepr() + "..." + cards[cards.size() - 1].getCardRepr();
        std::cout << "Deck{Type: " << getTypeName() << ", Count: " << cards.size() << ", Cards: " << cardRepr << std::endl;
    }
};

Deck getDeck(std::vector<CardRank> excludeRanks = {}){
    std::vector<CardRank> includedRanks;
    for (CardRank rank : allRanks){
        auto itfound = std::find(excludeRanks.begin(), excludeRanks.end(), rank);
        // Only push rank to includedRanks if the rank is not found in excludeRanks
        if (itfound == excludeRanks.end()){
            includedRanks.push_back(rank);
        }
    };
    std::vector<Card> cards;
    for (std::string suit : allSuits){
        for (CardRank rank: includedRanks)
            cards.push_back(Card{rank, suit});
    }
    
    DeckType deckType;
    if (excludeRanks.size() == 0)
        deckType = STANDARD;
    else
        deckType = STRIPPED;
    
    return Deck{deckType, cards};
};


int main(int argc, const char * argv[]) {
    
    // Create and print the standrd 52 card deck
    Deck standardDeck = getDeck();
    standardDeck.printDeck();
    
    // Create and print a stripped deck that start at hearts of rank 2 and ends at Queen of rank 12
    std::vector<CardRank> ignoreRanks = {ACE, KING};
    Deck strippedDeck = getDeck(ignoreRanks);
    strippedDeck.printDeck();
    
    return 0;
}
