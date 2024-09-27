import java.lang.Math;

public class Fraction {
    public long numerator;
    public long denominator;

    public Fraction(){
        numerator = 0;
        denominator = 1;
    }
    public Fraction(long numerator, long denominator) {
        if (denominator == 0){
            throw new ArithmeticException("A fraction cannot have a denominator of 0 as you cannot divide by zero");
        }
        if ((numerator < 0 && denominator < 0) || (numerator >= 0 && denominator < 0)){
            numerator = numerator * -1;
            denominator = denominator * -1;
        }

        this.numerator = numerator;
        this.denominator = denominator;
        this.reduce();
    }

    public Fraction plus(Fraction rhs){
        long newDenom = denominator * rhs.denominator;
        long lhsNumer = numerator * rhs.denominator;
        long rhsNumer = rhs.numerator * denominator;

        return new Fraction(lhsNumer + rhsNumer, newDenom);
    };

    public Fraction minus(Fraction rhs){
        long newDenom = denominator * rhs.denominator;
        long lhsNumer = numerator * rhs.denominator;
        long rhsNumer = rhs.numerator * denominator;

        return new Fraction(lhsNumer - rhsNumer, newDenom);
    };

    public Fraction times(Fraction rhs){
        return new Fraction(numerator * rhs.numerator, denominator * rhs.denominator);
    };

    public Fraction dividedBy(Fraction rhs){
        return times(rhs.reciprocal());
    };

    public Fraction reciprocal(){
        return new Fraction(denominator, numerator);
    };

    public String toString(){
        return numerator + "/" + denominator;
    };

    public double toDouble(){
        return numerator / (double)denominator;
    };

    public void reduce(){
        long gcd = GCD();
        numerator = numerator / gcd;
        denominator = denominator / gcd;
    };

    private long GCD(){
        long gcd = Math.abs(numerator);
        long remainder = Math.abs(denominator);
        while(remainder != 0) {
            long temp = remainder;
            remainder = gcd % remainder;
            gcd = temp;
        }
        return gcd;
    };
}
