//
//  Fraction.hpp
//  FractionClass
//
//  Created by Christopher Lawton on 9/11/24.
//

#ifndef Fraction_hpp
#define Fraction_hpp

#include <string>


class Fraction{
private:
    long numerator;
    long denominator;
    
public:
    Fraction();
    Fraction(long numerator, long denominator);
    
    Fraction plus(Fraction rhs);
    
    Fraction minus(Fraction rhs);
    
    Fraction times(Fraction rhs);
    
    Fraction dividedBy(Fraction rhs);
    
    Fraction reciprocal();
    
    std::string toString();
    
    double toDouble();
    
    void reduce();
    
    long GCD();
    
    // operators
    
    Fraction& operator+=(const Fraction& rhs);
    
    Fraction& operator-=(const Fraction& rhs);
    
    Fraction& operator/=(const Fraction& rhs);
    
    Fraction& operator*=(const Fraction& rhs);
    
    bool operator<(Fraction rhs);
    
    bool operator==(Fraction rhs);
    
};

Fraction operator+(Fraction lhs, Fraction rhs);

Fraction operator-(Fraction lhs, Fraction rhs);

Fraction operator/(Fraction lhs, Fraction rhs);

Fraction operator*(Fraction lhs, Fraction rhs);

bool operator!=(Fraction lhs, Fraction rhs);

bool operator>(Fraction lhs, Fraction rhs);

bool operator<=(Fraction lhs, Fraction rhs);

bool operator>=(Fraction lhs, Fraction rhs);

#endif /* Fraction_hpp */
