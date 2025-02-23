//
// Created by Christopher Lawton on 2/22/25.
//

#include "crypto.h"

int main() {
  std::array<uint8_t, 8> key = generateKey("myAmazingInsecurePassword!");
  SubTables tables = SubTables();
  std::array<uint8_t, 8> plaintext = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
  std::array<uint8_t, 8> ciphertext = encrypt(plaintext, key, tables);
  std::array<uint8_t, 8> decryptedText = decrypt(ciphertext, key, tables);
  std::cout << "Plaintext -> Ciphertext -> Decrypted text\n";
  for (int i = 0; i < 8; i++) {
    std::cout << plaintext[i] << "->" << ciphertext[i] << "->"
              << decryptedText[i] << std::endl;
  }

  std::cout << "\nEnc/Dec with bitflip\n";
  ciphertext[2] ^=
      1;  // Flip the first bit of the second byte in the cipher text
  std::array<uint8_t, 8> decryptedText2 = decrypt(ciphertext, key, tables);
  std::cout << "Plaintext -> Ciphertext -> Decrypted text\n";
  for (int i = 0; i < 8; i++) {
    std::cout << plaintext[i] << "->" << ciphertext[i] << "->"
              << decryptedText2[i] << std::endl;
  }
  return 0;
}
