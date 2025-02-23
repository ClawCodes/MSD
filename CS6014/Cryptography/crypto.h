//
// Created by Christopher Lawton on 2/22/25.
//

#ifndef CRYPTO_H
#define CRYPTO_H

#include <array>
#include <iostream>
#include <string>

std::array<uint8_t, 8> generateKey(std::string password);

class SubTables {
  std::array<std::array<uint8_t, 256>, 8> tables_;

  static void shuffle(std::array<uint8_t, 256> &arr);

 public:
  SubTables();

  uint8_t get(int table, int index) const;

  int getIndex(int table, int value) const;
};

void LshiftState(std::array<uint8_t, 8> &state);

void RshiftState(std::array<uint8_t, 8> &state);

std::array<uint8_t, 8> encrypt(std::array<uint8_t, 8> plaintext,
                               std::array<uint8_t, 8> key,
                               const SubTables &tables);

std::array<uint8_t, 8> decrypt(std::array<uint8_t, 8> ciphertext,
                               std::array<uint8_t, 8> key,
                               const SubTables &tables);

#endif  // CRYPTO_H
