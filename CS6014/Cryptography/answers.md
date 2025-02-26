# Question 1

Since the block size is 8-bits, this means we will observe a one character to one character mapping from plain text to cipher text.
If the plaintext is comprehensive (i.e. the 256 8-bit possible values are fully represented in the plaintext),
an attacker could compute the direct character-to-character mappings by noting which plaintext character results in which ciphertext character.
Once the attacker has a mapping of ciphertext to plaintext characters, then decrypting a separate ciphertext can be done by looking up each plaintext character in the mapping.
If the plaintext is not comprehensive (i.e. only represents a subset of the possible 256 8-bit values), then the attacker can analyze the patterns of the observed plaintext to ciphertext mappings an infer what the remaining mappings are.
For example, if an attacker observes each plaintext character is mapped to the character 10 steps ahead in an ASCII table, then the attacker can apply this 10 character offset rule to unobserved plaintext characters.

# Question 2

### Part A
An eavesdropper can discern the general structure and repeated patterns in the message.
This is because each block that shares the same value will result in the same ciphertext.
This does not obfuscate patterns in the plaintext as the same pattern will exist in the ciphertext, but represented by different values.

### Part B
The attacker can alter the ciphertext by intercepting the message before Bob receives it, by performing operations on the message which still preserve the patterns but change the values.
For example, subtracting an encrypted image from a completely white image, will invert the image.
When Bob decrypts the altered message, bob will see a different message than what Alice sent him and the patterns will still be preserved.

### Part C
You can change the mode of operation.
The mode outlined in the question is using the electronic code book mode which has an insufficient amount of diffusion thus resulting in preserved patterns in the ciphertext.
Instead, one could use a mode like counter mode which relies on a unique nonce for each block, this result in different ciphertexts even when the plaintexts are the same.
Thus, the preserved patterns an attacker could see in the ciphertexts, would no longer be observable.

# Programming Part 1

* "Verify that you can encrypt and decrypt messages using your program" - see "Encrypt-Decrypt" test case
* "Demonstate that trying to decrypt a message using the wrong password (and therefore the wrong key) does not recover the plaintext message" - see "decrypt-wrong-password" test case 
* "Try modifying 1 bit of the ciphertext and then decrypting with the correct passwords. What do you see?"
  * The entire decrypted cipher is jumbled

# Programming Part 2

See `main` in `rc4.cpp`

