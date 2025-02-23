//
// Created by Christopher Lawton on 2/22/25.
//

#include <cassert>

#include "crypto.h"

TEST_CASE("LshiftState") {
  // Test LshiftState
  std::array<uint8_t, 8> s1 = {0x80, 1, 1, 1, 1, 1, 1, 1};
  LshiftState(s1);
  CHECK(s1[0] == 1);
  // assert(s1[1] == 3);
  // assert(s1[2] == 3);
  // assert(s1[3] == 3);
  // assert(s1[4] == 3);
  // assert(s1[5] == 3);
  // assert(s1[6] == 3);
  // assert(s1[7] == 3);
}
