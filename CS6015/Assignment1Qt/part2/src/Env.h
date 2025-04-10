//
// Created by Christopher Lawton on 4/4/25.
//

#ifndef ENV_H
#define ENV_H

#include "pointer.h"
class Val;
#include "val.h"

CLASS(Env) {
 public:
  static PTR(Env) empty;
  virtual PTR(Val) lookup(std::string find_name) = 0;
};

class EmptyEnv : public Env {
  PTR(Val) lookup(std::string find_name) override;
};

class ExtendedEnv : public Env {
  std::string name;
  PTR(Val) val;
  PTR(Env) rest;

 public:
  ExtendedEnv(std::string find_name, PTR(Val) val, PTR(Env) rest)
      : name(find_name), val(val), rest(rest){};

  PTR(Val) lookup(std::string find_name) override;
};

#endif  // ENV_H
