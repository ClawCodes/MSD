//
// Created by Christopher Lawton on 2/22/25.
//

#include <array>
#include <iostream>
#include <string>

// Notes:
// 64-bit block size and 64-bit key -> store 64-bit values as an array of
// unsigned bytes Parts: 1) compute secret key based on password of any length
// 2) compute a set of substitution tables that will be used during
// encryption/decryption 3) the actual encryption algorithm

std::array<uint8_t, 8> generateKey(std::string password) {
  std::array<uint8_t, 8> key = {};  // Initialize key with 0s
  for (uint8_t i = 0; i < password.length(); i++) {
    key[i % 8] = key[i % 8] xor password[i];
  }
  return key;
}

void shuffle(std::array<uint8_t, 256> &arr) {
  srand(time(NULL));
  for (int i = arr.size() - 1; i >= 0; i--) {
    int j = rand() % (i + 1);
    std::swap(arr[i], arr[j]);
  }
}

std::array<std::array<uint8_t, 256>, 8> generateTables() {
  std::array<uint8_t, 256> initArray = {};
  for (int i = 0; i < 256; i++) {
    initArray[i] = (uint8_t)i;
  }
  shuffle(initArray);
  std::array<std::array<uint8_t, 256>, 8> tables;
  tables[0] = initArray;
  for (int i = 1; i < 8; i++) {
    std::array<uint8_t, 256> newArr = tables[i - 1];
    shuffle(newArr);
    tables[i] = newArr;
  }
  return tables;
}

int main() {
  std::array<uint8_t, 8> key = generateKey("myAmazingInsecurePassword!");
  std::array<std::array<uint8_t, 256>, 8> tables = generateTables();

  return 0;
}
