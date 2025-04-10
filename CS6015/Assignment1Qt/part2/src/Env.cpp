//
// Created by Christopher Lawton on 4/4/25.
//

#include "Env.h"

PTR(Val) EmptyEnv::lookup(std::string find_name) {
  throw std::runtime_error("free variable: " + find_name);
};

PTR(Val) ExtendedEnv::lookup(std::string find_name) {
  return find_name == name ? val : rest->lookup(find_name);
}

PTR(Env) Env::empty = NEW(EmptyEnv)();
