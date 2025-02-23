//
// Created by Christopher Lawton on 2/22/25.
//

#define CATCH_CONFIG_MAIN
#include <catch2/catch_all.hpp>

#include "crypto.h"

TEST_CASE("LshiftState") {
  std::array<uint8_t, 8> s1 = {0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80};
  LshiftState(s1);
  CHECK(s1[0] == 1);
  CHECK(s1[1] == 1);
  CHECK(s1[2] == 1);
  CHECK(s1[3] == 1);
  CHECK(s1[4] == 1);
  CHECK(s1[5] == 1);
  CHECK(s1[6] == 1);
  CHECK(s1[7] == 1);
}

TEST_CASE("RshiftState") {
  std::array<uint8_t, 8> s1 = {1, 1, 1, 1, 1, 1, 1, 1};
  RshiftState(s1);
  CHECK(s1[0] == 0x80);
  CHECK(s1[1] == 0x80);
  CHECK(s1[2] == 0x80);
  CHECK(s1[3] == 0x80);
  CHECK(s1[4] == 0x80);
  CHECK(s1[5] == 0x80);
  CHECK(s1[6] == 0x80);
  CHECK(s1[7] == 0x80);
}
