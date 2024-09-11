//
//  Fraction.cpp
//  FractionClass
//
//  Created by Christopher Lawton on 9/11/24.
//

#include "Fraction.hpp"

#include <iostream>

Fraction::Fraction(){
    numerator = 0;
    denominator = 1;
};

Fraction::Fraction(long numerator, long denominator){
    if ((numerator < 0 && denominator < 0) || (numerator >= 0 && denominator < 0)){
        numerator = numerator * -1;
        denominator = denominator * -1;
    }
    this->numerator = numerator;
    this->denominator = denominator;
    reduce();
};

Fraction Fraction::plus(Fraction rhs){
    long newDenom = denominator * rhs.denominator;
    long lhsNumer = numerator * rhs.denominator;
    long rhsNumer = rhs.numerator * denominator;
    
    return Fraction(lhsNumer + rhsNumer, newDenom);
};

Fraction Fraction::minus(Fraction rhs){
    long newDenom = denominator * rhs.denominator;
    long lhsNumer = numerator * rhs.denominator;
    long rhsNumer = rhs.numerator * denominator;
    
    return Fraction(lhsNumer - rhsNumer, newDenom);
};

Fraction Fraction::times(Fraction rhs){
    return Fraction(numerator * rhs.numerator, denominator * rhs.denominator);
};

Fraction Fraction::dividedBy(Fraction rhs){
    return times(reciprocal());
};

Fraction Fraction::reciprocal(){
    return Fraction(denominator, numerator);
};

std::string Fraction::toString(){
    return std::to_string(numerator) + "/" + std::to_string(denominator);
};

double Fraction::toDouble(){
    return numerator / double(denominator);
};

void Fraction::reduce(){
    long gcd = GCD();
    numerator = numerator / gcd;
    denominator = denominator / gcd;
};

long Fraction::GCD(){
    long gcd = abs(numerator);
    long remainder = abs(denominator);
    while(remainder != 0) {
      long temp = remainder;
      remainder = gcd % remainder;
      gcd = temp;
    }
    return gcd;
};

// Operators
Fraction& Fraction::operator+=(const Fraction& rhs){
    *this = plus(rhs);
    return *this;
}

Fraction& Fraction::operator-=(const Fraction& rhs){
    *this = minus(rhs);
    return *this;
}

Fraction& Fraction::operator/=(const Fraction& rhs){
    *this = dividedBy(rhs);
    return *this;
}

Fraction& Fraction::operator*=(const Fraction& rhs){
    *this = times(rhs);
    return *this;
}

bool Fraction::operator<(Fraction rhs){
    return toDouble() < rhs.toDouble();
}

bool Fraction::operator==(Fraction rhs){
    return (denominator == rhs.denominator) && (numerator == rhs.numerator);
}

Fraction operator+(Fraction& lhs, Fraction rhs){
    lhs += rhs;
    return lhs;
}

Fraction operator-(Fraction& lhs, Fraction rhs){
    lhs -= rhs;
    return lhs;
}

Fraction operator/(Fraction& lhs, Fraction rhs){
    lhs /= rhs;
    return lhs;
}

Fraction operator*(Fraction& lhs, Fraction rhs){
    lhs *= rhs;
    return lhs;
}

bool operator!=(Fraction& lhs, Fraction& rhs){
    return ~(lhs == rhs);
}

bool operator>(Fraction& lhs, Fraction& rhs){
    return !(lhs < rhs);
}

bool operator<=(Fraction& lhs, Fraction& rhs){
    return !(lhs > rhs);
}

bool operator>=(Fraction& lhs, Fraction& rhs){
    return !(lhs < rhs);
}
