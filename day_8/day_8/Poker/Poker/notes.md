# Notes for the homework

Requirements:
* Create method to shuffle a deck:
    * As you iterate through a deck select a random integer in the index range of the deck array and swap with another index
    * Perform this operation in place (use reference)
    * You can use std::rand() to perform the random sampling
* Add functionality to produce a random hand of 5 cards
* Add functionality to analyze a hand and determine if it is one of the following:
    * Flush, Straight, Straight flush, Royal flush, Full house
    * Note: The instructions want specific function names for these
    * Potentially create some example hands 

MVP:
* Update the Deck struct to include a memeber function to shuffle the deck
* Create a Hand struct which:
    * Contains the attibute hand that accepts a deck and produced an array of 5 cards
    * Contains member functions to analyze the hand

