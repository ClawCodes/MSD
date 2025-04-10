//
// Created by Christopher Lawton on 3/2/25.
//

#ifndef VAL_H
#define VAL_H

#include <iostream>

#include "pointer.h"

class Env;

#include "Env.h"

class Expr;

CLASS(Val) {
  // Non-template base class to enable ValType polymorphism
 public:
  virtual PTR(Val) add_to(PTR(Val) other) = 0;

  virtual PTR(Val) mult_with(PTR(Val) other) = 0;

  virtual bool equals(PTR(Val) other) = 0;

  virtual std::string to_string() = 0;

  virtual PTR(Expr) to_expr() = 0;

  virtual bool is_true() = 0;

  virtual PTR(Val) call(PTR(Val) actual_arg) = 0;
};

template <typename T>
class ValType : public Val {
 protected:
  T val_;

 public:
  explicit ValType(T val) : val_(val) {}

  std::string to_string() override { return std::to_string(val_); }

  bool equals(PTR(Val) other) override {
    PTR(ValType) otherVal = CAST(ValType<T>)(other);
    if (otherVal == nullptr) {
      return false;
    }
    return val_ == otherVal->val_;
  }

  PTR(Val) call(PTR(Val) actual_arg) override {
    throw std::runtime_error("ValType is not callable.");
  }
};

class NumVal : public ValType<int> {
 public:
  static PTR(NumVal) cast(PTR(Val) val);

  explicit NumVal(const int val) : ValType(val) {}

  PTR(Val) add_to(PTR(Val) other_val) override;

  PTR(Expr) to_expr() override;

  PTR(Val) mult_with(PTR(Val) other) override;

  bool is_true() override;
};

class BoolVal : public ValType<bool> {
 public:
  static PTR(BoolVal) cast(PTR(Val) val);

  explicit BoolVal(const bool val) : ValType(val) {}

  PTR(Val) add_to(PTR(Val) other_val) override;

  PTR(Expr) to_expr() override;

  PTR(Val) mult_with(PTR(Val) other) override;

  bool is_true() override;

  std::string to_string() override;
};

class FunVal : public Val {
  std::string formal_arg_;
  PTR(Expr) body_;
  PTR(Env) env_;

 public:
  FunVal(const std::string &formal_arg, PTR(Expr) body, PTR(Env) env)
      : formal_arg_(formal_arg), body_(body), env_(env){};
  PTR(Val) add_to(PTR(Val) other) override;

  PTR(Val) mult_with(PTR(Val) other) override;

  bool equals(PTR(Val) other) override;

  std::string to_string() override;

  PTR(Expr) to_expr() override;

  bool is_true() override;

  PTR(Val) call(PTR(Val) actual_arg) override;
};

#endif  // VAL_H
