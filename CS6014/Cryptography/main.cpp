//
// Created by Christopher Lawton on 2/22/25.
//

#include "crypto.h"

int main() {
  std::array<uint8_t, 8> key = generateKey("myAmazingInsecurePassword!");
  SubTables tables = SubTables();
  std::array<uint8_t, 8> plaintext = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
  std::array<uint8_t, 8> ciphertext = encrypt(plaintext, key, tables);
  std::cout << "ENCRYPT:\n";
  for (int i = 0; i < 8; i++) {
    std::cout << plaintext[i] << "->" << ciphertext[i] << std::endl;
  }
  std::array<uint8_t, 8> decryptedText = decrypt(ciphertext, key, tables);
  std::cout << "DECRYPT:\n";
  for (int i = 0; i < 8; i++) {
    std::cout << ciphertext[i] << "->" << decryptedText[i] << std::endl;
  }
  return 0;
}
