//
// Created by Christopher Lawton on 2/11/25.
//

#ifndef PARSE_H
#define PARSE_H
#include <iostream>

#include "expr.h"
#include "pointer.h"

PTR(Expr) expr_from_stream(std::istream &in);
PTR(Expr) expr_from_string(std::string str);

#endif  // PARSE_H
