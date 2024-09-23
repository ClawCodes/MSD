/*
 * Author: Daniel Kopta and <add your names here>
 * July 2017

 * Testing program for your Fraction class.
 * These tests are not very thorough, and you will need to add your own.
*/

// Include the student's Fraction class
#include "FractionTest.hpp"

// Standard includes
#include <iostream>
#include <string>
// Also include math so we can use the pow and abs functions (see below)
#include <cmath>
#include <cassert>

using std::cout;
using std::cin;
using std::endl;
using std::string;

bool CompareDoubles( double d1, double d );


/* Helper function for writing tests that compare strings.
 * Compares expected to result, and prints an error if they don't match.
 */
void Test( const string & message, const string & expected, const string & result )
{
  cout << "Test: " << message << endl;
  if(expected != result) {
    cout << "\tFAILED, expected: \"" << expected << "\", got: \"" << result << "\"" << endl;
  }
  else {
    cout << "\tPASSED" << endl;
  }
}

/* Helper function for writing tests that compare doubles (overloaded version of the above)
 * Compares expected to result, and prints an error if they don't match.
 */
void Test( const string & message, double expected, double result )
{
  cout << "Test: " << message << endl;
  if( !CompareDoubles( expected, result ) ) {
    cout << "\tFAILED, expected: " << expected << ", got: " << result << endl;
  }
  else {
    cout << "\tPASSED" << endl;
  }
}


/*
 * Helper function
 * Returns whether or not two doubles are "equivalent",
 * within a certain error bound.
 *
 * As we know, floating point numbers are incapable of
 * precisely representing certain values, so to compare
 * equality, we must tolerate some absolute difference.
 */
bool CompareDoubles( double d1, double d2 )
{
  return std::abs( d1 - d2 ) < 1e-6;
}

/*
 * Basic constructor and toString tests
 */
void TestConstructors()
{
  std::string result = "";
  Fraction f1;
  result = f1.toString();
  Test( "Default constructor", "0/1", result );
  
  Fraction f2(1, 2);
  result = f2.toString();
  Test( "Basic constructor", "1/2", result );

  Fraction f3(1, -2);
  result = f3.toString();
  Test("Basic constructor - Negative denominator results in negative numerator", "-1/2", result );
    
  Fraction f4(-1, -2);
  result = f4.toString();
  Test("Basic constructor - Negative denominator and numerator results in positive fraction", "1/2", result );
    
 Fraction f5(2, 4);
 result = f5.toString();
 Test("Basic constructor - Positive fraction is reduced", "1/2", result );
    
 Fraction f6(-2, 4);
 result = f6.toString();
 Test("Basic constructor - Negative fraction is reduced", "-1/2", result );
}


/*
 * Negative fraction tests
 */
void TestNegative()
{
  std::string result = "";
  Fraction f1(-1, 2);
  result = f1.toString();
  Test( "Negative numerator", "-1/2", result );

  Fraction f2(1, -2);
  result = f2.toString();
  Test( "Negative denominator", "-1/2", result );

  Fraction f3(-1, -2);
  result = f3.toString();
  Test( "Negative numerator and denominator", "1/2", result );

  Fraction f4(4, -16);
  result = f4.toString();
  Test( "Negative denominator and reduce", "-1/4", result );

  Fraction f5(0, -1);
  result = f5.toString();
  Test( "Zero numerator and negative denominator results in 0 numerator", "0/1", result );
    
  Fraction f6(-0, -0);
  result = f6.toString();
  Test( "Allow negative zeros to be stripped of negative", "0/0", result );
}


/*
 * Reduced fraction tests
 */
void TestReduced()
{
  std::string result = "";
  Fraction f1(2, 4);
  result = f1.toString();
  Test( "Reduce fraction (2/4)", "1/2", result );

  Fraction f2(-2, 4);
  result = f2.toString();
  Test( "Reduce fraction with negative (-2/4)", "-1/2", result );
    
  Fraction f3(1, 2);
  result = f3.toString();
  Test( "Reduce already reduced fraction (1/2)", "1/2", result );
    
  Fraction f4(12, 12);
  result = f4.toString();
  Test( "Reduce whole number fraction (12/12)", "1/1", result );
    
  Fraction f5(0, 0);
  result = f5.toString();
  Test( "Reduce zero remains (0/0)", "0/0", result );
    
  Fraction f6(16, 8);
  result = f6.toString();
  Test( "Reduce greater numerator (16/8)", "2/1", result );
}


/*
 * Reciprocal tests
 */
void TestReciprocal()
{
  std::string result = "";
  Fraction f1(1, 3);
  f1 = f1.reciprocal();
  result = f1.toString();
  Test( "Reciprocal of simple", "3/1", result );

  Fraction f2(-1, 2);
  f2 = f2.reciprocal();
  result = f2.toString();
  Test( "Reciprocal of negative", "-2/1", result );

  Fraction f3(6, 2);
  f3 = f3.reciprocal();
  result = f3.toString();
  Test( "Reciprocal of non-reduced", "1/3", result );

  Fraction f4;
  f4 = f4.reciprocal();
  result = f4.toString();
  Test( "Reciprocal of default remains the same", "1/0", result );
    
  Fraction f5(-1, -2);
  f5 = f5.reciprocal();
  result = f5.toString();
  Test( "Reciprocal of double negative", "2/1", result );
}

/*
 * Fraction addition tests
 */
void TestPlus()
{
  std::string result = "";
  Fraction f1(4, 6);
  Fraction f2(3, 4);
  
  // Should result in 17/12
  Fraction f3 = f1.plus(f2);
  result = f3.toString();
  Test( "Addition of non-reduced", "17/12", result );

  result = Fraction(-1,2).plus(Fraction(1,2)).toString();
  Test( "Addition positive and negative fraction resulting in zero numerator", "0/1", result );
    
  result = Fraction(-1,2).plus(Fraction(1,3)).toString();
  Test( "Addition positive and negative fraction resulting in non-zero numerator", "-1/6", result );
    
  result = Fraction(0,2).plus(Fraction(0,3)).toString();
  Test( "Addition of zeroed numerators results in default", "0/1", result );
    
  result = Fraction(1,2).plus(Fraction(1,3)).toString();
  Test( "Addition of positive fractions with different numerators", "5/6", result );
    
  result = Fraction(-1,2).plus(Fraction(-1,3)).toString();
  Test( "Addition of negative fractions with different numerators", "-5/6", result );
    
  result = Fraction(1,2).plus(Fraction(0,0)).toString();
  Test( "Addition of fraction and undefined (0/0) results in undefined", "0/0", result );
    
  result = Fraction(1,2).plus(Fraction(0,1)).toString();
  Test( "Addition of fraction with zero (0/1) results in original fraction", "1/2", result );
}

void TestAdditionOp(){
    std::string result = "";
    
    result = (Fraction(-1,2) + Fraction(1,2)).toString();
    Test( "Operator Addition of positive and negative fraction resulting in zero numerator", "0/1", result );
      
    result = (Fraction(-1,2) + Fraction(1,3)).toString();
    Test( "Operator Addition of positive and negative fraction resulting in non-zero numerator", "-1/6", result );
      
    result = (Fraction(0,2) + Fraction(0,3)).toString();
    Test( "Operator Addition of zeroed numerators results in default", "0/1", result );
      
    result = (Fraction(1,2) + Fraction(1,3)).toString();
    Test( "Operator Addition of positive fractions with different numerators", "5/6", result );
      
    result = (Fraction(-1,2) + Fraction(-1,3)).toString();
    Test( "Operator Addition of negative fractions with different numerators", "-5/6", result );
      
    result = (Fraction(1,2) + Fraction(0,0)).toString();
    Test( "Operator Addition of fraction and undefined (0/0) results in undefined", "0/0", result );
      
    result = (Fraction(1,2) + Fraction(0,1)).toString();
    Test( "Operator Addition of fraction with zero (0/1) results in original fraction", "1/2", result );
}

void TestAdditionEqualsOp(){
    Fraction result;
    
    result = Fraction(-1,2);
    result += Fraction(1,2);
    Test( "Operator Addition Equals of positive and negative fraction resulting in zero numerator", "0/1", result.toString() );
      
    result = Fraction(-1,2);
    result += Fraction(1,3);
    Test( "Operator Addition Equals of positive and negative fraction resulting in non-zero numerator", "-1/6", result.toString() );
      
    result = Fraction(0,2);
    result += Fraction(0,3);
    Test( "Operator Addition Equals of zeroed numerators results in default", "0/1", result.toString() );
      
    result = Fraction(1,2);
    result += Fraction(1,3);
    Test( "Operator Addition Equals of positive fractions with different numerators", "5/6", result.toString() );
      
    result = Fraction(-1,2);
    result += Fraction(-1,3);
    Test( "Operator Addition Equals of negative fractions with different numerators", "-5/6", result.toString() );
      
    result = Fraction(1,2);
    result += Fraction(0,0);
    Test( "Operator Addition Equals of fraction and undefined (0/0) results in undefined", "0/0", result.toString() );
      
    result = Fraction(1,2);
    result += Fraction(0,1);
    Test( "Operator Addition Equals of fraction with zero (0/1) results in original fraction", "1/2", result.toString() );
}

void TestMinus()
{
  std::string result = "";
    
  result = Fraction(-1,2).minus(Fraction(-1,2)).toString();
  Test( "Subtraction of fractions resulting in zero numerator", "0/1", result );
    
  result = Fraction(-1,2).minus(Fraction(1,3)).toString();
  Test( "Subtraction of positive and negative fraction resulting in non-zero numerator", "-5/6", result );
    
  result = Fraction(0,2).minus(Fraction(0,3)).toString();
  Test( "Subtraction of zeroed numerators results in default", "0/1", result );
    
  result = Fraction(1,2).minus(Fraction(1,3)).toString();
  Test( "Subtraction of positive fractions with different numerators", "1/6", result );
    
  result = Fraction(-1,2).minus(Fraction(-1,3)).toString();
  Test( "Subtraction of negative fractions with different numerators", "-1/6", result );
    
  result = Fraction(1,2).minus(Fraction(0,0)).toString();
  Test( "Subtraction of fraction and undefined (0/0) results in undefined", "0/0", result );
    
  result = Fraction(1,2).minus(Fraction(0,1)).toString();
  Test( "Subtraction of fraction with zero (0/1) results in original fraction", "1/2", result );
}

void TestSubtractionOp()
{
  std::string result = "";
    
  result = (Fraction(-1,2) - Fraction(-1,2)).toString();
  Test( "Operator subtraction of fractions resulting in zero numerator", "0/1", result );
    
  result = (Fraction(-1,2) - Fraction(1,3)).toString();
  Test( "Operator subtraction of positive and negative fraction resulting in non-zero numerator", "-5/6", result );
    
  result = (Fraction(0,2) - Fraction(0,3)).toString();
  Test( "Operator subtraction of zeroed numerators results in default", "0/1", result );
    
  result = (Fraction(1,2) - Fraction(1,3)).toString();
  Test( "Operator subtraction of positive fractions with different numerators", "1/6", result );
    
  result = (Fraction(-1,2) - Fraction(-1,3)).toString();
  Test( "Operator subtraction of negative fractions with different numerators", "-1/6", result );
    
  result = (Fraction(1,2) - Fraction(0,0)).toString();
  Test( "Operator subtraction of fraction and undefined (0/0) results in undefined", "0/0", result );
    
  result = (Fraction(1,2) - Fraction(0,1)).toString();
  Test( "Operator subtraction of fraction with zero (0/1) results in original fraction", "1/2", result );
}

void TestSubtractionEqualsOp()
{
  Fraction result;
    
  result = Fraction(-1,2);
  result -= Fraction(-1,2);
  Test( "Operator subtraction equals of fractions resulting in zero numerator", "0/1", result.toString() );
    
  result = Fraction(-1,2);
  result -= Fraction(1,3);
  Test( "Operator subtraction equals of positive and negative fraction resulting in non-zero numerator", "-5/6", result.toString() );
    
  result = Fraction(0,2);
  result -= Fraction(0,3);
  Test( "Operator subtraction equals of zeroed numerators results in default", "0/1", result.toString() );
    
  result = Fraction(1,2);
  result -= Fraction(1,3);
  Test( "Operator subtraction equals of positive fractions with different numerators", "1/6", result.toString() );
    
  result = Fraction(-1,2);
  result -= Fraction(-1,3);
  Test( "Operator subtraction equals of negative fractions with different numerators", "-1/6", result.toString() );
    
  result = Fraction(1,2);
  result -= Fraction(0,0);
  Test( "Operator subtraction equals of fraction and undefined (0/0) results in undefined", "0/0", result.toString() );
    
  result = Fraction(1,2);
  result -= Fraction(0,1);
  Test( "Operator subtraction equals of fraction with zero (0/1) results in original fraction", "1/2", result.toString() );
}

void TestTimes()
{
  std::string result = "";
    
  result = Fraction(1,2).times(Fraction(1,2)).toString();
  Test( "Multiplication of positive fractions result in smaller positive", "1/4", result );
    
  result = Fraction(-1,2).times(Fraction(1,3)).toString();
  Test( "Multiplication of positive and negative fractions result in negative fraction", "-1/6", result );

  result = Fraction(0,2).times(Fraction(0,3)).toString();
  Test( "Multiplication of zeroed numerators results in default", "0/1", result );

  result = Fraction(1,2).times(Fraction(1,3)).toString();
  Test( "Multiplication of positive fractions with different numerators", "1/6", result );

  result = Fraction(-1,2).times(Fraction(-1,3)).toString();
  Test( "Multiplication of negative fractions with different numerators", "1/6", result );

  result = Fraction(1,2).times(Fraction(0,0)).toString();
  Test( "Multiplication of fraction and undefined (0/0) results in undefined", "0/0", result );

  result = Fraction(1,2).times(Fraction(0,1)).toString();
  Test( "Multiplication of fraction with zero (0/1) results in zero", "0/1", result );
}

void TestMultiplicationOp(){
    std::string result = "";
      
    result = (Fraction(1,2) * Fraction(1,2)).toString();
    Test( "Operator Multiplication of positive fractions result in smaller positive", "1/4", result );
      
    result = (Fraction(-1,2) * Fraction(1,3)).toString();
    Test( "Operator Multiplication of positive and negative fractions result in negative fraction", "-1/6", result );

    result = (Fraction(0,2) * Fraction(0,3)).toString();
    Test( "Operator Multiplication of zeroed numerators results in default", "0/1", result );

    result = (Fraction(1,2) * Fraction(1,3)).toString();
    Test( "Operator Multiplication of positive fractions with different numerators", "1/6", result );

    result = (Fraction(-1,2) * Fraction(-1,3)).toString();
    Test( "Operator Multiplication of negative fractions with different numerators", "1/6", result );

    result = (Fraction(1,2) * Fraction(0,0)).toString();
    Test( "Operator Multiplication of fraction and undefined (0/0) results in undefined", "0/0", result );

    result = (Fraction(1,2) * Fraction(0,1)).toString();
    Test( "Operator Multiplication of fraction with zero (0/1) results in zero", "0/1", result );
}

void TestMultiplicationEqualsOp(){
    Fraction result;
      
    result = Fraction(1,2);
    result *= Fraction(1,2);
    Test( "Operator Multiplication Equals of positive fractions result in smaller positive", "1/4", result.toString() );
      
    result = Fraction(-1,2);
    result *= Fraction(1,3);
    Test( "Operator Multiplication Equals of positive and negative fractions result in negative fraction", "-1/6", result.toString() );

    result = Fraction(0,2);
    result *= Fraction(0,3);
    Test( "Operator Multiplication Equals of zeroed numerators results in default", "0/1", result.toString() );

    result = Fraction(1,2);
    result *= Fraction(1,3);
    Test( "Operator Multiplication Equals of positive fractions with different numerators", "1/6", result.toString() );

    result = Fraction(-1,2);
    result *= Fraction(-1,3);
    Test( "Operator Multiplication Equals of negative fractions with different numerators", "1/6", result.toString() );

    result = Fraction(1,2);
    result *= Fraction(0,0);
    Test( "Operator Multiplication Equals of fraction and undefined (0/0) results in undefined", "0/0", result.toString() );

    result = Fraction(1,2);
    result *= Fraction(0,1);
    Test( "Operator Multiplication Equals of fraction with zero (0/1) results in zero", "0/1", result.toString() );
}

void TestdividedBy()
{
  std::string result = "";
    
  result = Fraction(1,2).dividedBy(Fraction(1,2)).toString();
  Test( "Division of positive fractions result in larger positive", "1/1", result );
    
  result = Fraction(-1,2).dividedBy(Fraction(1,3)).toString();
  Test( "Division of positive and negative fractions result in negative fraction", "-3/2", result );

  result = Fraction(0,2).dividedBy(Fraction(0,3)).toString();
  Test( "Division of zeroed numerators results in undefined", "0/0", result );

  result = Fraction(1,2).dividedBy(Fraction(1,3)).toString();
  Test( "Division of positive fractions with different numerators", "3/2", result );

  result = Fraction(-1,2).dividedBy(Fraction(-1,3)).toString();
  Test( "Division of negative fractions with different numerators", "3/2", result );

  result = Fraction(1,2).dividedBy(Fraction(0,0)).toString();
  Test( "Division of fraction and undefined (0/0) results in undefined", "0/0", result );

  result = Fraction(1,2).dividedBy(Fraction(0,1)).toString();
  Test( "Division of fraction with zero (0/1) results in lhs.numerator/0", "1/0", result );
}

void TestDivisionOp()
{
  std::string result = "";
    
  result = (Fraction(1,2) / Fraction(1,2)).toString();
  Test( "Operator Division of positive fractions result in larger positive", "1/1", result );
    
  result = (Fraction(-1,2) / Fraction(1,3)).toString();
  Test( "Operator Division of positive and negative fractions result in negative fraction", "-3/2", result );

  result = (Fraction(0,2) / Fraction(0,3)).toString();
  Test( "Operator Division of zeroed numerators results in undefined", "0/0", result );

  result = (Fraction(1,2) / Fraction(1,3)).toString();
  Test( "Operator Division of positive fractions with different numerators", "3/2", result );

  result = (Fraction(-1,2) / Fraction(-1,3)).toString();
  Test( "Operator Division of negative fractions with different numerators", "3/2", result );

  result = (Fraction(1,2) / Fraction(0,0)).toString();
  Test( "Operator Division of fraction and undefined (0/0) results in undefined", "0/0", result );

  result = (Fraction(1,2) / Fraction(0,1)).toString();
  Test( "Operator Division of fraction with zero (0/1) results in lhs.numerator/0", "1/0", result );
}

void TestDivisionEqualsOp()
{
  Fraction result;
    
  result = Fraction(1,2);
  result /= Fraction(1,2);
  Test( "Operator Division Equals of positive fractions result in larger positive", "1/1", result.toString() );
    
  result = Fraction(-1,2);
  result /= Fraction(1,3);
  Test( "Operator Division Equals of positive and negative fractions result in negative fraction", "-3/2", result.toString() );

  result = Fraction(0,2);
  result /= Fraction(0,3);
  Test( "Operator Division Equals of zeroed numerators results in undefined", "0/0", result.toString() );

  result = Fraction(1,2);
  result /= Fraction(1,3);
  Test( "Operator Division Equals of positive fractions with different numerators", "3/2", result.toString() );

  result = Fraction(-1,2);
  result /= Fraction(-1,3);
  Test( "Operator Division Equals of negative fractions with different numerators", "3/2", result.toString() );

  result = Fraction(1,2);
  result /= Fraction(0,0);
  Test( "Operator Division Equals of fraction and undefined (0/0) results in undefined", "0/0", result.toString() );

  result = Fraction(1,2);
  result /= Fraction(0,1);
  Test( "Operator Division Equals of fraction with zero (0/1) results in lhs.numerator/0", "1/0", result.toString() );
}

// TEST COMPARISON OPERATORS
void TestEqualsComparisonOp(){
    Test("1/2 == 1/2", true, Fraction(1,2) == Fraction(1,2));
    Test("-1/2 == -1/2", true, Fraction(-1,2) == Fraction(-1,2));
    Test("-1/-2 == -1/-2", true, Fraction(-1,-2) == Fraction(-1,-2));
    Test("1/-2 == 1/-2", true, Fraction(1,-2) == Fraction(1,-2));
    Test("1/2 == 1/4 evaluates to false", false, Fraction(1,2) == Fraction(1,4));
    Test("0/0 == 0/0 (consider undefined == undefined)", true, Fraction(0,0) == Fraction(0,0));
}

void TestNotEqualsComparisonOp(){
    Test("1/2 != 1/2 evaluates to false", false, Fraction(1,2) != Fraction(1,2));
    Test("-1/2 != -1/2 evaluates to false", false, Fraction(-1,2) != Fraction(-1,2));
    Test("-1/-2 != -1/-2 evaluates to false", false, Fraction(-1,-2) != Fraction(-1,-2));
    Test("1/-2 != 1/-2 evaluates to false", false, Fraction(1,-2) != Fraction(1,-2));
    Test("1/2 != 1/4 evaluates to true", true, Fraction(1,2) != Fraction(1,4));
    Test("0/0 != 0/0 evaluates to false", false, Fraction(0,0) != Fraction(0,0));
    Test("1/4 != 1/2 evaluates to true", true, Fraction(1,4) != Fraction(1,2));
    Test("-1/4 != -1/2 evaluates to true", true, Fraction(-1,4) != Fraction(-1,2));
    Test("-1/-4 != -1/-2 evaluates to true", true, Fraction(-1,-4) != Fraction(-1,-2));
}

void TestLessThanOp(){
    Test("1/2 < 1/2 evaluates to false", false, Fraction(1,2) < Fraction(1,2));
    Test("-1/2 < -1/2 evaluates to false", false, Fraction(-1,2) < Fraction(-1,2));
    Test("-1/-2 < -1/-2 evaluates to false", false, Fraction(-1,-2) < Fraction(-1,-2));
    Test("1/-2 < 1/-2 evaluates to false", false, Fraction(1,-2) < Fraction(1,-2));
    Test("0/0 < 0/0 evaluates to false", false, Fraction(0,0) < Fraction(0,0));
    Test("1/2 < 1/4 evaluates to false", false, Fraction(1,2) < Fraction(1,4));
    Test("1/4 < 1/2 evaluates to true", true, Fraction(1,4) < Fraction(1,2));
    Test("-1/2 < 1/2 evaluates to true", true, Fraction(-1,2) < Fraction(1,2));
    Test("-1/-4 < -1/-2 evaluates to true", true, Fraction(-1,-4) < Fraction(-1,-2));
    Test("1/-4 < 1/-2 evaluates to false", false, Fraction(1,-4) < Fraction(1,-2));
}

void TestGreaterThanOp(){
    Test("1/2 > 1/2 evaluates to false", false, Fraction(1,2) > Fraction(1,2));
    Test("-1/2 > -1/2 evaluates to false", false, Fraction(-1,2) > Fraction(-1,2));
    Test("-1/-2 > -1/-2 evaluates to false", false, Fraction(-1,-2) > Fraction(-1,-2));
    Test("1/-2 > 1/-2 evaluates to false", false, Fraction(1,-2) > Fraction(1,-2));
    Test("0/0 > 0/0 evaluates to false", false, Fraction(0,0) > Fraction(0,0));
    Test("1/2 > 1/4 evaluates to true", true, Fraction(1,2) > Fraction(1,4));
    Test("1/4 > 1/2 evaluates to false", false, Fraction(1,4) > Fraction(1,2));
    Test("-1/2 > 1/2 evaluates to false", false, Fraction(-1,2) > Fraction(1,2));
    Test("-1/-4 > -1/-2 evaluates to false", false, Fraction(-1,-4) > Fraction(-1,-2));
    Test("1/-4 > 1/-2 evaluates to true", true, Fraction(1,-4) > Fraction(1,-2));
}

void TestGreaterThanOrEqualToOp(){
    Test("1/2 >= 1/2 evaluates to true", true, Fraction(1,2) >= Fraction(1,2));
    Test("-1/2 >= -1/2 evaluates to true", true, Fraction(-1,2) >= Fraction(-1,2));
    Test("-1/-2 >= -1/-2 evaluates to true", true, Fraction(-1,-2) >= Fraction(-1,-2));
    Test("1/-2 >= 1/-2 evaluates to true", true, Fraction(1,-2) >= Fraction(1,-2));
    Test("0/0 >= 0/0 evaluates to true", true, Fraction(0,0) >= Fraction(0,0));
    Test("1/2 >= 1/4 evaluates to true", true, Fraction(1,2) >= Fraction(1,4));
    Test("1/4 >= 1/2 evaluates to false", false, Fraction(1,4) >= Fraction(1,2));
    Test("-1/2 >= 1/2 evaluates to false", false, Fraction(-1,2) >= Fraction(1,2));
    Test("-1/-4 >= -1/-2 evaluates to false", false, Fraction(-1,-4) >= Fraction(-1,-2));
    Test("1/-4 >= 1/-2 evaluates to true", true, Fraction(1,-4) >= Fraction(1,-2));
}


void TestLessThanOREqualToOp(){
    Test("1/2 <= 1/2 evaluates to true", true, Fraction(1,2) <= Fraction(1,2));
    Test("-1/2 <= -1/2 evaluates to true", true, Fraction(-1,2) <= Fraction(-1,2));
    Test("-1/-2 <= -1/-2 evaluates to true", true, Fraction(-1,-2) <= Fraction(-1,-2));
    Test("1/-2 <= 1/-2 evaluates to true", true, Fraction(1,-2) <= Fraction(1,-2));
    Test("0/0 <= 0/0 evaluates to true", true, Fraction(0,0) <= Fraction(0,0));
    Test("1/2 <= 1/4 evaluates to false", false, Fraction(1,2) <= Fraction(1,4));
    Test("1/4 <= 1/2 evaluates to true", true, Fraction(1,4) <= Fraction(1,2));
    Test("-1/2 <= 1/2 evaluates to true", true, Fraction(-1,2) <= Fraction(1,2));
    Test("-1/-4 <= -1/-2 evaluates to true", true, Fraction(-1,-4) <= Fraction(-1,-2));
    Test("1/-4 <= 1/-2 evaluates to false", false, Fraction(1,-4) <= Fraction(1,-2));
}


void TestToDouble()
{
  Fraction f1(1, 2);
  double result = f1.toDouble();
  Test( "toDouble (1/2)", 0.5, result );

  Fraction f2(1, 3);
  result = f2.toDouble();
  Test( "toDouble (1/3)", 1.0/3.0, result );
}

/*
 * Approximates pi using a summation of fractions.
 */
double ComputePi()
{
  Fraction sum;

  // We will only compute the first 4 terms
  // Note that even with long (64-bit) numbers,
  // the intermediate numbers required for fraction addition
  // become too large to represent if we go above k=4.
  for( long k = 0; k < 4; k++ )
  {
    Fraction multiplier( 1, pow(16, k) );
    Fraction firstTerm(  4, 8*k + 1 );
    Fraction secondTerm( 2, 8*k + 4 );
    Fraction thirdTerm(  1, 8*k + 5 );
    Fraction fourthTerm( 1, 8*k + 6 );

    Fraction temp = firstTerm.minus( secondTerm );
    temp = temp.minus( thirdTerm );
    temp = temp.minus( fourthTerm );
 
    Fraction product = multiplier.times( temp );

    sum = sum.plus( product );
  }
  
  return sum.toDouble();
}

namespace TestEntry {
    int main()
    {
        // Break up the tests into categories for better readability.
        TestConstructors();
        TestNegative();
        TestReduced();
        TestReciprocal();
        TestToDouble();
        TestPlus();
        
        TestAdditionOp();
        TestAdditionEqualsOp();
        
        TestMinus();
        TestSubtractionOp();
        TestSubtractionEqualsOp();
        
        TestTimes();
        TestMultiplicationOp();
        TestMultiplicationEqualsOp();
        
        TestdividedBy();
        TestDivisionOp();
        TestDivisionEqualsOp();
        
        TestEqualsComparisonOp();
        TestNotEqualsComparisonOp();
        TestLessThanOp();
        TestGreaterThanOp();
        TestGreaterThanOrEqualToOp();
        TestLessThanOREqualToOp();
        
        Test("Approximating pi", 3.141592, ComputePi());
        
        return 0;
    }
}
