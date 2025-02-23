//
// Created by Christopher Lawton on 2/22/25.
//

#ifndef RC4_H
#define RC4_H

#include <array>
#include <string>

class RC4 {
  std::array<int, 256> state_;
  int i_ = 0;
  int j_ = 0;

 public:
  RC4(std::string key);

  int nextByte();
};

#endif  // RC4_H
