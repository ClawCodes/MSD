//
// Created by Christopher Lawton on 3/2/25.
//

#include "val.h"

#include <sstream>
#include <stdexcept>

#include "expr.h"

// NumVal
PTR(NumVal) NumVal::cast(PTR(Val) val) {
  PTR(NumVal) casted_val = CAST(NumVal)(val);
  if (!casted_val) throw std::runtime_error("Cannot cast to NumVal");
  return casted_val;
}

PTR(Val) NumVal::add_to(PTR(Val) other) {
  return NEW(NumVal)(static_cast<unsigned>(val_) +
                     static_cast<unsigned>(cast(other)->val_));
}

PTR(Expr) NumVal::to_expr() { return NEW(Num)(val_); }

PTR(Val) NumVal::mult_with(PTR(Val) other) {
  return NEW(NumVal)(val_ * cast(other)->val_);
}

bool NumVal::is_true() { throw std::runtime_error("NumVal is not a boolean"); }

// BoolVal
PTR(BoolVal) BoolVal::cast(PTR(Val) val) {
  PTR(BoolVal) casted_val = CAST(BoolVal)(val);
  if (!casted_val) throw std::runtime_error("Cannot cast to BoolVal");
  return casted_val;
}

PTR(Val) BoolVal::add_to(PTR(Val) other) {
  throw NEW(std::runtime_error)("Cannot add to BoolVal");
}

PTR(Expr) BoolVal::to_expr() { return NEW(BoolExpr)(val_); }

PTR(Val) BoolVal::mult_with(PTR(Val) other) {
  throw NEW(std::runtime_error)("Cannot mult with BoolVal");
}

bool BoolVal::is_true() { return val_; }

std::string BoolVal::to_string() { return val_ ? "_true" : "_false"; }

PTR(Val) FunVal::add_to(PTR(Val) other) {
  throw NEW(std::runtime_error)("Cannot add to a FunVal instance");
}

PTR(Val) FunVal::mult_with(PTR(Val) other) {
  throw NEW(std::runtime_error)("Cannot mult a FunVal instance");
}

bool FunVal::equals(PTR(Val) other) {
  PTR(FunVal) val = CAST(FunVal)(other);
  if (!val) {
    return false;
  }
  return formal_arg_ == val->formal_arg_ && body_->equals(val->body_);
}

std::string FunVal::to_string() {
  return "_fun (" + formal_arg_ + ") " + body_->toString();
}

PTR(Expr) FunVal::to_expr() { return NEW(FunExpr)(formal_arg_, body_); }

bool FunVal::is_true() { return false; }

PTR(Val) FunVal::call(PTR(Val) actual_arg) {
  return body_->interp(NEW(ExtendedEnv)(formal_arg_, actual_arg, env_));
}
