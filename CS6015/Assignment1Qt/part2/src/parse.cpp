#include "parse.h"

#include <format>
#include <sstream>
#include <vector>

static void consume(std::istream &in, int expect);

void skip_whitespace(std::istream &in);

PTR(Expr) parse_num(std::istream &inn);

PTR(Expr) parse_addend(std::istream &in);

PTR(Expr) parse_multicand(std::istream &in);

PTR(Expr) parse_comparg(std::istream &in);

bool contains(const std::vector<char> &vec, char value) {
  return std::find(vec.begin(), vec.end(), value) != vec.end();
}

std::string parse_string(std::istream &in) {
  std::string s;
  char c = in.peek();
  while (std::isalpha(c)) {
    s += c;
    consume(in, c);
    c = in.peek();
  }
  return s;
}

std::string parse_keyword(std::istream &in) {
  char c = in.peek();
  if (c != '_') {
    throw std::runtime_error(
        std::format("Expected keyword to begin with '_', but found '{}'", c));
  }
  consume(in, c);
  return parse_string(in);
}

PTR(VarExpr) parse_var(std::istream &in) {
  return NEW(VarExpr)(parse_string(in));
}

PTR(Expr) parse_expr(std::istream &in) {
  PTR(Expr) e;
  e = parse_comparg(in);
  skip_whitespace(in);
  int c = in.peek();
  if (c == '=') {
    consume(in, c);
    c = in.peek();
    if (c != '=') {
      throw std::runtime_error("Malformed comparison statement");
    }
    consume(in, c);
    PTR(Expr) rhs = parse_expr(in);
    return NEW(EqExpr)(e, rhs);
  }
  return e;
}

PTR(Expr) parse_comparg(std::istream &in) {
  PTR(Expr) e;
  e = parse_addend(in);
  skip_whitespace(in);
  int c = in.peek();
  if (c == '+') {
    consume(in, '+');
    PTR(Expr) rhs = parse_comparg(in);
    return NEW(Add)(e, rhs);
  }
  return e;
}

PTR(Expr) expr_from_stream(std::istream &in) {
  PTR(Expr) expr = parse_expr(in);
  if (!in.eof()) {
    throw std::runtime_error("Unexpected end of input");
  }
  return expr;
}

PTR(Expr) expr_from_string(std::string str) {
  std::stringstream ss(str);
  return expr_from_stream(ss);
}

PTR(Expr) parse_addend(std::istream &in) {
  PTR(Expr) e;
  e = parse_multicand(in);
  skip_whitespace(in);
  int c = in.peek();
  if (c == '*') {
    consume(in, '*');
    PTR(Expr) rhs = parse_addend(in);
    return NEW(Mult)(e, rhs);
  }
  return e;
}

PTR(Let) parse_let(std::istream &in) {
  skip_whitespace(in);
  std::string lhs = parse_string(in);
  skip_whitespace(in);
  char c = in.peek();
  if (c != '=') {
    throw std::runtime_error("Expected '=' after let variable");
  }
  consume(in, c);
  PTR(Expr) rhs = parse_expr(in);
  skip_whitespace(in);
  std::string in_keyword = parse_keyword(in);
  if (in_keyword != "in") {
    throw std::runtime_error(
        "Expected _in to proceed the variable assignment in _let expression");
  }
  PTR(Expr) body = parse_expr(in);
  return NEW(Let)(lhs, rhs, body);
}

PTR(IfExpr) parse_if(std::istream &in) {
  skip_whitespace(in);

  PTR(Expr) if_ = parse_expr(in);

  std::string kw_then = parse_keyword(in);
  if (kw_then != "then") {
    throw std::runtime_error("Malformed if expression, expected '_then'");
  }

  skip_whitespace(in);

  PTR(Expr) then = parse_expr(in);

  skip_whitespace(in);
  std::string kw_else = parse_keyword(in);
  if (kw_else != "else") {
    throw std::runtime_error("Malformed if expression, expected '_else");
  }

  PTR(Expr) else_ = parse_expr(in);
  return NEW(IfExpr)(if_, then, else_);
}

PTR(FunExpr) parse_fun(std::istream &in) {
  skip_whitespace(in);
  char c = in.peek();
  if (c != '(') {
    throw std::runtime_error("Expected '(' after function declaration");
  }
  consume(in, c);
  skip_whitespace(in);
  std::string formal_arg = parse_string(in);
  skip_whitespace(in);
  c = in.peek();
  if (c != ')') {
    throw std::runtime_error(
        "Expected ')' after function argument declaration");
  }
  consume(in, c);
  PTR(Expr) body = parse_expr(in);
  return NEW(FunExpr)(formal_arg, body);
}

PTR(Expr) parse_inner(std::istream &in) {
  skip_whitespace(in);

  int c = in.peek();
  if (c == '-' || isdigit(c)) {
    return parse_num(in);
  }
  if (c == '(') {
    consume(in, '(');
    PTR(Expr) e = parse_expr(in);
    skip_whitespace(in);
    c = in.get();
    if (c != ')') throw std::runtime_error("missing close parenthesis");
    return e;
  }
  if (c == '_') {
    std::string s = parse_keyword(in);
    if (s == "let") return parse_let(in);
    if (s == "true") return NEW(BoolExpr)(true);
    if (s == "false") return NEW(BoolExpr)(false);
    if (s == "if") return parse_if(in);
    if (s == "fun") return parse_fun(in);
    throw std::runtime_error("Invalid keyword: '_" + s + "'");
  }
  if (std::isalpha(c)) {
    return parse_var(in);
  }
  consume(in, c);
  throw std::runtime_error("invalid input");
}

PTR(Expr) parse_multicand(std::istream &in) {
  PTR(Expr) expr = parse_inner(in);
  while (in.peek() == '(') {
    consume(in, '(');
    PTR(Expr) actual_arg = parse_expr(in);
    consume(in, ')');
    expr = NEW(CallExpr)(expr, actual_arg);
  }
  return expr;
}

PTR(Expr) parse_num(std::istream &inn) {
  int n = 0;
  bool negative = false;

  if (inn.peek() == '-') {
    negative = true;
    consume(inn, '-');
  }

  int c = inn.peek();
  if (negative && !isdigit(c)) {
    throw std::runtime_error("Non-numeric value proceeding negative sign");
  }
  while (isdigit(c)) {
    n = n * 10 + (c - '0');
    consume(inn, c);
    c = inn.peek();
  }

  if (negative) n = -n;

  return NEW(Num)(n);
}

static void consume(std::istream &in, int expect) {
  int c = in.get();
  if (c != expect) {
    throw std::runtime_error("consume mismatch");
  }
}

void skip_whitespace(std::istream &in) {
  while (true) {
    int c = in.peek();
    if (!isspace(c)) break;
    consume(in, c);
  }
}
