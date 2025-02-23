//
// Created by Christopher Lawton on 2/22/25.
//

#include "rc4.h"

#include <cassert>
#include <format>
#include <iostream>
#include <sstream>

#include "vector"

RC4::RC4(std::string key) {
  for (int i = 0; i < 256; i++) {
    state_[i] = i;
  }
  int j = 0;
  for (int i = 0; i < 256; i++) {
    j = (j + state_[i] + key[i % (key.size() - 1)]) % 256;
    std::swap(state_[i], state_[j]);
  }
}

int RC4::nextByte() {
  i_ = (i_ + 1) % 256;
  j_ = (j_ + state_[i_]) % 256;
  std::swap(state_[i_], state_[j_]);
  return state_[(state_[i_] + state_[j_]) % 256];
}

std::vector<uint8_t> generateKey(RC4 &rng, const std::string &plaintext) {
  std::vector<uint8_t> key;
  for (int i = 0; i < plaintext.size(); i++) {
    key.push_back(rng.nextByte());
  }
  return key;
}

std::string encrypt(std::string plaintext, std::vector<uint8_t> key) {
  std::stringstream ss;
  for (int i = 0; i < plaintext.size(); i++) {
    ss << (char)(plaintext[i] ^ key[i]);
  }
  return ss.str();
}

int main() {
  std::string plaintext = "Hello world!";
  RC4 rng("0987ceq34b2nd");
  std::vector<uint8_t> key = generateKey(rng, plaintext);
  for (uint8_t i = 0; i < key.size(); i++) {
    std::cout << key[i];
  }
  std::string ciphertext = encrypt(plaintext, key);
  std::string decryptedText = encrypt(ciphertext, key);
  std::cout << "\nPlaintext -> Ciphertext -> Decrypted text\n";
  for (int i = 0; i < plaintext.size(); i++) {
    std::cout << plaintext[i] << "->" << ciphertext[i] << "->"
              << decryptedText[i] << std::endl;
  }
  std::cout << "\nDecrypt with different key:\n";
  std::cout << "Plaintext -> Ciphertext -> Decrypted text\n";
  std::vector<uint8_t> key2 = generateKey(rng, plaintext);
  assert(key != key2);  // Ensure keys are different for next decrypt
  for (uint8_t i = 0; i < key2.size(); i++) {
    std::cout << key2[i];
  }
  std::string decryptedText2 = encrypt(ciphertext, key2);
  for (int i = 0; i < plaintext.size(); i++) {
    std::cout << plaintext[i] << "->" << ciphertext[i] << "->"
              << decryptedText2[i] << std::endl;
  }

  std::cout << "\nEncrypt with the same key:\n";
  std::string plaintext2 = "Hello, Mars!";
  std::string ciphertext2 = encrypt(plaintext2, key);
  std::cout << std::format("\nencrypt('{}') ^ encrypt('{}')\n", plaintext,
                           plaintext2)
            << std::endl;
  for (int i = 0; i < plaintext2.size(); i++) {
    char xored = ciphertext[i] ^ ciphertext2[i];
    std::cout << ciphertext[i] << " ^ " << ciphertext2[i] << " = " << xored
              << std::endl;
  }

  // TODO: finish bit flipping attack
  std::cout << "\nBit flip attack\n";
  std::string plaintext3 = "Your salary is $1000";
  std::string ciphertext3 = encrypt(plaintext3, key);

  return 0;
}
