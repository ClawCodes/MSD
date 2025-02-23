//
// Created by Christopher Lawton on 2/22/25.
//

#include "crypto.h"

std::array<uint8_t, 8> generateKey(std::string password) {
  std::array<uint8_t, 8> key = {};  // Initialize key with 0s
  for (uint8_t i = 0; i < password.length(); i++) {
    key[i % 8] = key[i % 8] xor password[i];
  }
  return key;
}

void SubTables::shuffle(std::array<uint8_t, 256> &arr) {
  srand(time(NULL));
  for (int i = arr.size() - 1; i >= 0; i--) {
    int j = rand() % (i + 1);
    std::swap(arr[i], arr[j]);
  }
}

SubTables::SubTables() {
  std::array<uint8_t, 256> initArray = {};
  for (int i = 0; i < 256; i++) {
    initArray[i] = static_cast<uint8_t>(i);
  }
  shuffle(initArray);
  tables_[0] = initArray;
  for (int i = 1; i < 8; i++) {
    std::array<uint8_t, 256> newArr = tables_[i - 1];
    shuffle(newArr);
    tables_[i] = newArr;
  }
}

uint8_t SubTables::get(const int table, const int index) const {
  return tables_[table][index];
};

int SubTables::getIndex(const int table, const int value) const {
  for (int i = 0; i < 256; i++) {
    if (tables_[table][i] == value) {
      return i;
    }
  }
  throw std::runtime_error("Value not found");
}

void LshiftState(std::array<uint8_t, 8> &state) {
  uint8_t zerothOverflow = (state[0] & 0x80) >> 7;
  state[0] = state[0] << 1;
  for (int i = 1; i < state.size(); i++) {
    uint8_t overflow = (state[i] & 0x80) >> 7;
    state[i] = state[i] << 1;
    state[i - 1] = state[i - 1] | overflow;
  }
  state[state.size() - 1] = state[state.size() - 1] | zerothOverflow;
}

void RshiftState(std::array<uint8_t, 8> &state) {
  uint8_t lastOverflow = (state[state.size() - 1] & 0x01) << 7;
  state[state.size() - 1] = state[state.size() - 1] >> 1;
  for (int i = state.size() - 2; i >= 0; i--) {
    uint8_t overflow = (state[i] & 0x01) << 7;
    state[i] = state[i] >> 1;
    state[i - 1] = state[i - 1] | overflow;
  }
  state[0] = state[0] | lastOverflow;
}

std::array<uint8_t, 8> encrypt(std::array<uint8_t, 8> plaintext,
                               std::array<uint8_t, 8> key,
                               const SubTables &tables) {
  std::array<uint8_t, 8> state = plaintext;
  for (int round = 0; round < 16; round++) {
    for (int i = 0; i < state.size(); ++i) {
      state[i] = state[i] ^ key[i];
      state[i] = tables.get(i, state[i]);
    }
    LshiftState(state);
  }
  return state;
}

std::array<uint8_t, 8> decrypt(std::array<uint8_t, 8> ciphertext,
                               std::array<uint8_t, 8> key,
                               const SubTables &tables) {
  std::array<uint8_t, 8> state = ciphertext;
  for (int round = 0; round < 16; round++) {
    RshiftState(state);
    for (int i = 0; i < state.size(); ++i) {
      state[i] = tables.getIndex(i, state[i]) ^ key[i];
    }
  }
  return state;
}