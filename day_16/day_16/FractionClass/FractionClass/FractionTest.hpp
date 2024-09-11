//
//  FractionTest.hpp
//  FractionClass
//
//  Created by Christopher Lawton on 9/11/24.
//

#ifndef FractionTest_hpp
#define FractionTest_hpp

// Standard includes
#include <iostream>
#include <string>
// Also include math so we can use the pow and abs functions (see below)
#include <cmath>

#include "Fraction.hpp"

using std::cout;
using std::cin;
using std::endl;
using std::string;

void Test( const string & message, const string & expected, const string & result );

void Test( const string & message, double expected, double result );

bool CompareDoubles( double d1, double d2 );

void TestConstructors();

void TestNegative();

void TestReduced();

void TestReciprocal();

void TestPlus();

void TestAdditionOpperator();

void TestToDouble();

double ComputePi();

void TestEqualsComparisonOp();

namespace TestEntry{
int main();
}


#endif /* FractionTest_hpp */