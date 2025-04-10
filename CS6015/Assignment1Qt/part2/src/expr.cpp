//
// Created by Christopher Lawton on 1/18/25.
//

#include "expr.h"

#include <sstream>

#include "Env.h"
#include "val.h"

std::string Expr::toString() {
  std::stringstream st("");
  this->printExp(st);
  return st.str();
}

std::string Expr::toPrettyString() {
  std::stringstream st("");
  this->prettyPrint(st);
  return st.str();
}

void Expr::prettyPrintAt(std::ostream &out, precedence_t prec,
                         std::streampos &lastNewLine) {
  printExp(out);
};

void Expr::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
};

Operator::Operator(PTR(Expr) lhs, PTR(Expr) rhs, std::string op) {
  this->lhs = lhs;
  this->rhs = rhs;
  this->op = op;
}

bool Operator::equals(PTR(Expr) expr) {
  PTR(Operator) opExpr = CAST(Operator)(expr);
  if (opExpr == nullptr) {
    return false;
  }
  return this->lhs->equals(opExpr->lhs) && this->rhs->equals(opExpr->rhs);
}

bool Operator::hasVariable() {
  return this->lhs->hasVariable() || this->rhs->hasVariable();
}

void Operator::printExp(std::ostream &ot) {
  ot << "(";
  lhs->printExp(ot);
  ot << op;
  rhs->printExp(ot);
  ot << ")";
}

void Operator::printOp(std::ostream &out) const {
  out << " " << this->op << " ";
}

PTR(Val) Num::interp(PTR(Env) env) { return NEW(NumVal)(this->val); }

bool Num::hasVariable() { return false; }

void Num::printExp(std::ostream &ot) { ot << std::to_string(val); }

bool VarExpr::hasVariable() { return true; }

PTR(Val) VarExpr::interp(PTR(Env) env) { return env->lookup(this->val); }

void VarExpr::printExp(std::ostream &ot) { ot << val; }

PTR(Val) Add::interp(PTR(Env) env) {
  return lhs->interp(env)->add_to(rhs->interp(env));
}

void Add::prettyPrintAt(std::ostream &out, precedence_t prec,
                        std::streampos &lastNewLine) {
  const bool group = prec >= prec_add;
  if (group) {
    out << "(";
  }
  this->lhs->prettyPrintAt(out, prec_add, lastNewLine);
  printOp(out);
  this->rhs->prettyPrintAt(out, prec_none, lastNewLine);
  if (group) {
    out << ")";
  }
}

void Add::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

PTR(Val) Mult::interp(PTR(Env) env) {
  return lhs->interp(env)->mult_with(rhs->interp(env));
}

void Mult::prettyPrintAt(std::ostream &out, precedence_t prec,
                         std::streampos &lastNewLine) {
  const bool group = prec >= prec_mult;
  if (group) {
    out << "(";
  }
  this->lhs->prettyPrintAt(out, prec_mult, lastNewLine);
  printOp(out);
  this->rhs->prettyPrintAt(out, prec_add, lastNewLine);
  if (group) {
    out << ")";
  }
}

void Mult::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

bool Let::equals(PTR(Expr) expr) {
  PTR(Let) letExpr = CAST(Let)(expr);
  if (letExpr == nullptr) {
    return false;
  }
  return lhs == letExpr->lhs && rhs->equals(letExpr->rhs) &&
         body->equals(letExpr->body);
}

PTR(Val) Let::interp(PTR(Env) env) {
  PTR(Val) rhsVal = rhs->interp(env);
  PTR(Env) newEnv = NEW(ExtendedEnv)(lhs, rhsVal, env);
  return body->interp(newEnv);
}

bool Let::hasVariable() { return rhs->hasVariable() || body->hasVariable(); }

void Let::printExp(std::ostream &ot) {
  ot << "(_let " + lhs << '=';
  rhs->printExp(ot);
  ot << " _in ";
  body->printExp(ot);
  ot << ")";
}

void Let::prettyPrintAt(std::ostream &out, precedence_t prec,
                        std::streampos &lastNewLine) {
  if (prec > prec_none) {
    out << "(";
  }
  int startPos = out.tellp();  // Keep track of when _let starts for future _in
  out << "_let " << lhs << " = ";
  rhs->prettyPrintAt(out, prec_none, lastNewLine);
  out << "\n";

  std::ostream::pos_type newLinePos = out.tellp();
  int spacesNeeded = startPos - lastNewLine;

  for (int i = 0; i < spacesNeeded; i++) {
    out << " ";
  }
  out << "_in ";
  body->prettyPrintAt(out, prec_none, newLinePos);
  if (prec > prec_none) {
    out << ")";
  }
}

void Let::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

bool BoolExpr::equals(PTR(Expr) expr) {
  PTR(BoolExpr) boolExpr = CAST(BoolExpr)(expr);
  if (boolExpr == nullptr) {
    return false;
  }
  return val == boolExpr->val;
}

PTR(Val) BoolExpr::interp(PTR(Env) env) { return NEW(BoolVal)(val); }

bool BoolExpr::hasVariable() { return false; }

void BoolExpr::printExp(std::ostream &ot) {
  ot << interp(Env::empty)->to_string();
}

void BoolExpr::prettyPrintAt(std::ostream &out, precedence_t prec,
                             std::streampos &lastNewLine) {
  printExp(out);
}

void BoolExpr::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

bool IfExpr::equals(PTR(Expr) expr) {
  PTR(IfExpr) ifExpr = CAST(IfExpr)(expr);
  if (ifExpr == nullptr) {
    return false;
  }
  return condition_->equals(ifExpr->condition_) &&
         then_->equals(ifExpr->then_) && else_->equals(ifExpr->else_);
}

PTR(Val) IfExpr::interp(PTR(Env) env) {
  if (condition_->interp(env)->is_true()) {
    return then_->interp(env);
  }
  return else_->interp(env);
}

bool IfExpr::hasVariable() {
  return condition_->hasVariable() || then_->hasVariable() ||
         else_->hasVariable();
}

void IfExpr::printExp(std::ostream &ot) {
  ot << "(_if ";
  condition_->printExp(ot);
  ot << " _then ";
  then_->printExp(ot);
  ot << " _else ";
  else_->printExp(ot);
  ot << ")";
}

void IfExpr::prettyPrintAt(std::ostream &out, precedence_t prec,
                           std::streampos &lastNewLine) {
  if (prec > prec_none) {
    out << "(";
  }

  std::streampos ifPosition = out.tellp();  // marking the position of _if
  out << "_if ";

  condition_->prettyPrintAt(out, prec_none, ifPosition);
  out << "\n";

  std::streampos currentPos = out.tellp();
  int spaceNeeded = ifPosition - lastNewLine;
  for (int i = 0; i < spaceNeeded; i++) {
    out << " ";
  }

  out << "_then ";
  then_->prettyPrintAt(out, prec_none, currentPos);
  out << "\n";

  for (int i = 0; i < spaceNeeded; i++) {
    out << " ";
  }

  out << "_else ";
  else_->prettyPrintAt(out, prec_none, currentPos);

  if (prec > prec_none) {
    out << ")";
  }
}

void IfExpr::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

PTR(Val) EqExpr::interp(PTR(Env) env) {
  return NEW(BoolVal)(lhs->interp(env)->equals(rhs->interp(env)));
}

void EqExpr::prettyPrintAt(std::ostream &out, precedence_t prec,
                           std::streampos &lastNewLine) {
  const bool group = prec >= prec_none;
  if (group) {
    out << "(";
  }
  this->lhs->prettyPrintAt(out, prec_none, lastNewLine);
  printOp(out);
  this->rhs->prettyPrintAt(out, prec_none, lastNewLine);
  if (group) {
    out << ")";
  }
}

void EqExpr::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

bool FunExpr::equals(PTR(Expr) expr) {
  PTR(FunExpr) funExpr = CAST(FunExpr)(expr);
  if (funExpr == nullptr) {
    return false;
  }
  return formal_arg_ == funExpr->formal_arg_ && body_->equals(funExpr->body_);
}

PTR(Val) FunExpr::interp(PTR(Env) env) {
  return NEW(FunVal)(formal_arg_, body_, env);
}

bool FunExpr::hasVariable() { return false; }

void FunExpr::printExp(std::ostream &ot) {
  ot << "(_fun (" << formal_arg_ << ") ";
  body_->printExp(ot);
  ot << ")";
}

void FunExpr::prettyPrintAt(std::ostream &out, precedence_t prec,
                            std::streampos &lastNewLine) {
  if (prec > prec_none) {
    out << "(";
  }
  int startPos = out.tellp();  // Keep track of when _let starts for future _in
  out << "_fun (" << formal_arg_ << ")" << std::endl;

  std::ostream::pos_type newLinePos = out.tellp();
  int spacesNeeded = startPos - lastNewLine + 2;

  for (int i = 0; i < spacesNeeded; i++) {
    out << " ";
  }

  body_->prettyPrintAt(out, prec_none, newLinePos);
  if (prec > prec_none) {
    out << ")";
  }
}

void FunExpr::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}

bool CallExpr::equals(PTR(Expr) expr) {
  PTR(CallExpr) callExpr = CAST(CallExpr)(expr);
  if (callExpr == nullptr) {
    return false;
  }
  return to_be_called->equals(callExpr->to_be_called) &&
         actual_arg->equals(callExpr->actual_arg);
}

PTR(Val) CallExpr::interp(PTR(Env) env) {
  return to_be_called->interp(env)->call(actual_arg->interp(env));
}

bool CallExpr::hasVariable() { return false; }

void CallExpr::printExp(std::ostream &ot) {
  to_be_called->printExp(ot);
  ot << "(";
  actual_arg->printExp(ot);
  ot << ")";
}

void CallExpr::prettyPrintAt(std::ostream &out, precedence_t prec,
                             std::streampos &lastNewLine) {
  to_be_called->prettyPrintAt(out, prec, lastNewLine);
  out << "(";
  actual_arg->prettyPrintAt(out, prec, lastNewLine);
  out << ")";
}

void CallExpr::prettyPrint(std::ostream &out) {
  std::streampos pos = out.tellp();
  prettyPrintAt(out, prec_none, pos);
}