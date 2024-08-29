//
//  card.cpp
//  Poker
//
//  Created by Christopher Lawton on 8/28/24.
//

#include <stdio.h>
#include "card.h"


bool _rank_sorter(Card const& left, Card const& right){
    return left.rank < right.rank;
};

std::vector<Card> sort_cards(std::vector<Card> cards){
    std::sort(cards.begin(), cards.end(), &_rank_sorter);
    return cards;
}
