//
// Created by Christopher Lawton on 1/18/25.
//

#ifndef EXPR_H
#define EXPR_H

#include <stdexcept>
#include <string>

#include "Env.h"
#include "pointer.h"

// Forward declaration for val::Val type
class Val;

typedef enum {
  prec_none,  // = 0
  prec_add,   // = 1
  prec_mult   // = 2
} precedence_t;

/**
 * Abstract base class for expressions
 *
 * Inherit this class to enforce definition of methods required for an
 * expression.
 */
CLASS(Expr) {
 public:
  /**
   * Abstract method to determine if an expression is equivalent to another
   * expression instance.
   * @param expr expression to compare to
   * @return true is expr is equivalent to this, otherwise false
   */
  virtual bool equals(PTR(Expr) expr) = 0;

  /**
   * Abstract method to interpret an expression.
   * For example, if the expression denotes addition, such as 1 + 2.
   * Then interp should return 3.
   * @return The interpretation of the expression
   */
  virtual PTR(Val) interp(PTR(Env) env) = 0;

  /**
   * Abstract method determine if the expression contains a variable expression.
   * This should handle nested expressions.
   * @return true if the expression contains a variable, otherwise false
   */
  virtual bool hasVariable() = 0;

  /**
   * Write the expression to an output stream
   * @param out the output stream to write to
   */
  virtual void printExp(std::ostream & out) = 0;

  /**
   * Write the expression to an output stream such that it adheres to right
   * operator associativity rules
   * @param out output stream to write to
   */
  virtual void prettyPrint(std::ostream & out);

  /**
   * An abstract recursive method which passed precedence to child expression,
   * enforcing right operator associativity rules
   * @param out
   * @param prec
   */
  virtual void prettyPrintAt(std::ostream & out, precedence_t prec,
                             std::streampos & lastNewLine);

  /**
   * Generate the string representation of the expression
   * @return expression string representation
   */
  std::string toString();

  /**
   * Generate a well formatted string representation of the expression that
   * adheres to right operator associativity rules
   * @return
   */
  std::string toPrettyString();
};

/**
 * Base class for an expression that is a singular value
 *
 * Inherit this class for common code shared between expressions with singular
 * values. For example, expressions only containing an integer or a string. Bind
 * your subclass with the following pattern: class YourClassName : public
 * SingleValue<your type to bind to> (eg: class Number :public SingleValue<int>)
 * @tparam T type to bind to
 */
template <typename T>
class SingleValue : public Expr {
 protected:
  T val;  /// value to store in expression

 public:
  SingleValue(T val) { this->val = val; }

  /**
   * Determine if an expression is equivalent to another expression instance.
   *
   * This method will compare expression class type and the value of the val
   * member variable
   * @param expr
   * @return true if expr is equivalent to this, otherwise false
   */
  bool equals(PTR(Expr) expr) override {
    PTR(SingleValue) num = CAST(SingleValue)(expr);
    if (num == nullptr) {
      return false;
    }
    return this->val == num->val;
  }

  /**
   * Throws error if not overridden in child class.
   * By default, no interpretation can be made.
   * Interpretation will depend on the type the SingleValue is bound to.
   */
  PTR(Val) interp(PTR(Env) env) override {
    throw std::runtime_error("There is no interpretation of this type.");
  }
};

/**
 * Integer value expression
 *
 * Expression containing a single value which is an integer type
 */
class Num : public SingleValue<int> {
 public:
  /**
   * Constructor. Pass integer value to create Num expression instance.
   * @param val value to store
   */
  explicit Num(int val) : SingleValue<int>(val){};
  /**
   * Interpretation of the Num expression.
   * Since this is a singular value expression,
   * the interpretation is to return the integer value the class was constructed
   * with.
   * @return value of member variable, val.
   */
  PTR(Val) interp(PTR(Env) env) override;

  /**
   * Determine if this expression contains a VarExpr instance.
   * Since Num instances cannot contain nested expressions, this will always
   * return false.
   * @return false
   */
  bool hasVariable() override;

  /**
   * Write the val member variable to an ostream.
   * @param ot std::ostream to write member variable to
   */
  void printExp(std::ostream &ot) override;
};

/**
 * Variable expression
 *
 * Expression containing a single value which is a string type.
 * This expression is substitutable in with other expressions.
 */
class VarExpr : public SingleValue<std::string> {
 public:
  /**
   * Constructor. Pass string value to create VarExpr expression instance.
   * @param val value to store (e.g. "x")
   */
  explicit VarExpr(std::string val) : SingleValue<std::string>(val){};

  /**
   * Determine if the expression contains a VarExpr instance.
   * Since this is a VarExpr class, this will always return true.
   * @return true
   */
  bool hasVariable() override;

  PTR(Val) interp(PTR(Env) env) override;

  /**
   * Write the val member variable to an ostream.
   * @param ot std::ostream to write member variable to
   */
  void printExp(std::ostream &ot) override;
};

/**
 * Base expression that performs an operation between two expressions
 *
 * A class which contains common code for an operator expression.
 * Inherit this class to create a functional operator expression.
 * Each child class should represent a particular operation between expressions
 * such as addition or multiplication.
 * Example:
 * class Add : public Operator {
 *   public:
 *    Add(PTR(Expr)lhs, PTR(Expr)rhs) : Operator(lhs, rhs) { this->op = '+'; };
 *    }
 */
class Operator : public Expr {
 protected:
  PTR(Expr) lhs;   /// expression on left hand side of operator
  PTR(Expr) rhs;   /// expression of right hand side of operator
  std::string op;  /// string of operator (e.g. '*', '+', '==', etc.)

 public:
  /**
   * Base constructor of an Operator Expression
   * @param lhs expression of left hand side of operator
   * @param rhs expression of right hand side of operator
   * @param op the symbol of the operator (e.g. '+' for addition, '*' for
   * multiplication)
   */
  Operator(PTR(Expr) lhs, PTR(Expr) rhs, std::string op);

  /**
   * Determine if an expression is equivalent to this expression.
   * This will recursively call nested expressions, thus resulting in a deep
   * comparison.
   * @param expr instance of Expr class to compare to
   * @return true if equivalent, otherwise false
   */
  bool equals(PTR(Expr) expr) override;

  /**
   * Determine if operator instance contains a VarExpr instance.
   * This method will check recursively in nested expressions.
   * @return true if the lhs or rhs contains a VarExpr instance
   */
  bool hasVariable() override;

  /**
   * Write the expression to an output stream in the format of
   * "(<lhs><op><rhs>)"
   * @param ot std::ostream to write to
   */
  void printExp(std::ostream &ot) override;

  /**
   * Write the operator to an output stream.
   * The operator will be padded by spaces (i.e. " <op> ")
   * @param out std::ostream to write to
   */
  void printOp(std::ostream &out) const;
};

/**
 * Addition operator expression
 *
 * Operator expression which can perform addition between two other expressions.
 * Initialize with two other expressions.
 * @code
 * Add myAddition = new Add(new Num(1), new Num(2));
 * myAddition.interp(Env::empty)
 * >>> 3
 * @endcode
 */
class Add : public Operator {
 public:
  /**
   * Constructor
   * @param lhs Expression instance on left hand side of operator
   * @param rhs Expression instance on right hand side of operator
   */
  Add(PTR(Expr) lhs, PTR(Expr) rhs) : Operator(lhs, rhs, "+"){};
  /**
   * Interpret the expression's result.
   * This method will interpret nested expressions as well.
   * For example:
   * @code
   * Add add = new Add(new Num(1), new Add(new Num(3), new Num(4)));
   * add.interp(Env::empty);
   * >>> 8
   * @endcode
   * @return
   */
  PTR(Val) interp(PTR(Env) env) override;

  /**
   * Write expression to an output stream and determine the precedence to pass
   * to the expression's lhs and lhs expressions. The precedence will determine
   * where to include parenthesis for right operator associativity
   * @param out the output stream to write to
   * @param prec the precedence of the parent expression
   */
  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  /**
   * Write the expression to an output stream using right operator associativity
   * Example:
   * @code
   * Add addOne = new Add(new Add(new Num(3), new Num(5)), new Num(1));
   * Add addTwo = new Add(new Num(1), new Add(new Num(3), new Num(5)));
   * std::stringstream st("");
   * addOne.prettyPrint(st)
   * st.str()
   * > (3 + 5) + 1
   * addTwo.prettyPrint(st)
   * st.str()
   * > 3 + 5 + 1
   * @endcode
   * @param out the output stream to write to
   */
  void prettyPrint(std::ostream &out) override;
};

/**
 * Multiplication operator expression
 *
 * Operator expression which can perform multiplication between two other
 * expressions. Initialize with two other expressions. Example: Mult myMult =
 * new Mult(new Num(1), new Num(2)); myMult.interp(Env:empty) > 2
 */
class Mult : public Operator {
 public:
  /**
   * Constructor
   * @param lhs Expression instance on left hand side of operator
   * @param rhs Expression instance on right hand side of operator
   */
  Mult(PTR(Expr) lhs, PTR(Expr) rhs) : Operator(lhs, rhs, "*"){};

  /**
   * Interpret the expression's result.
   * This method will interpret nested expressions as well.
   * For example:
   * @code
   * Mult add = new Mult(new Num(1), new Mult(new Num(3), new Num(4)));
   * add.interp(Env::empty);
   * >>> 12
   * @endcode
   * @return
   */
  PTR(Val) interp(PTR(Env) env) override;

  /**
   * Write expression to an output stream and determine the precedence to pass
   * to the expression's lhs and lhs expressions. The precedence will determine
   * where to include parenthesis for right operator associativity
   * @param out the output stream to write to
   * @param prec the precedence of the parent expression
   */
  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  /**
   * Write the expression to an output stream using right operator associativity
   * Example:
   * @code
   * Mult multOne = new Mult(new Mult(new Num(3), new Num(5)), new Num(1));
   * Mult multTwo = new Mult(new Num(1), new Mult(new Num(3), new Num(5)));
   * std::stringstream st("");
   * multOne.prettyPrint(st)
   * st.str()
   * > (3 * 5) * 1
   * multTwo.prettyPrint(st)
   * st.str()
   * > 3 * 5 * 1
   * @endcode
   * @param out the output stream to write to
   */
  void prettyPrint(std::ostream &out) override;
};

class Let : public Expr {
  std::string lhs;
  PTR(Expr) rhs;
  PTR(Expr) body;

 public:
  Let(std::string lhs, PTR(Expr) rhs, PTR(Expr) body)
      : lhs(lhs), rhs(rhs), body(body){};

  bool equals(PTR(Expr) expr) override;

  PTR(Val) interp(PTR(Env) env) override;

  bool hasVariable() override;

  void printExp(std::ostream &ot) override;

  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  void prettyPrint(std::ostream &out) override;
};

// BoolExpr
class BoolExpr : public SingleValue<bool> {
 public:
  explicit BoolExpr(bool val) : SingleValue<bool>(val){};

  bool equals(PTR(Expr) expr) override;

  PTR(Val) interp(PTR(Env) env) override;

  bool hasVariable() override;

  void printExp(std::ostream &ot) override;

  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  void prettyPrint(std::ostream &out) override;
};

// IfExpr
class IfExpr : public Expr {
  PTR(Expr) condition_;
  PTR(Expr) then_;
  PTR(Expr) else_;

 public:
  IfExpr(PTR(Expr) condition, PTR(Expr) then, PTR(Expr) else_)
      : condition_(condition), then_(then), else_(else_){};

  bool equals(PTR(Expr) expr) override;

  PTR(Val) interp(PTR(Env) env) override;

  bool hasVariable() override;

  void printExp(std::ostream &ot) override;

  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  void prettyPrint(std::ostream &out) override;
};

class EqExpr : public Operator {
 public:
  EqExpr(PTR(Expr) lhs, PTR(Expr) rhs) : Operator(lhs, rhs, "=="){};

  PTR(Val) interp(PTR(Env) env) override;

  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  void prettyPrint(std::ostream &out) override;
};

class FunExpr : public Expr {
  std::string formal_arg_;
  PTR(Expr) body_;

 public:
  FunExpr(std::string &formal_arg, PTR(Expr) body)
      : Expr(), formal_arg_(formal_arg), body_(body){};

  bool equals(PTR(Expr) expr) override;

  PTR(Val) interp(PTR(Env) env) override;

  bool hasVariable() override;

  void printExp(std::ostream &ot) override;

  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  void prettyPrint(std::ostream &out) override;
};

class CallExpr : public Expr {
  PTR(Expr) to_be_called;
  PTR(Expr) actual_arg;

 public:
  CallExpr(PTR(Expr) to_be_called, PTR(Expr) actual_arg)
      : Expr(), to_be_called(to_be_called), actual_arg(actual_arg) {}
  bool equals(PTR(Expr) expr) override;

  PTR(Val) interp(PTR(Env) env) override;

  bool hasVariable() override;

  void printExp(std::ostream &out) override;

  void prettyPrintAt(std::ostream &out, precedence_t prec,
                     std::streampos &lastNewLine) override;

  void prettyPrint(std::ostream &out) override;
};

#endif  // EXPR_H
