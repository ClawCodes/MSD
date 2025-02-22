# Question 1

Since the block size is 8-bits, this means we will observe a one character to one character mapping from plain text to cipher text.
If the plaintext is comprehensive (i.e. the 256 8-bit possible values are fully represented in the plaintext),
an attacker could compute the direct character-to-character mappings by noting which plaintext character results in which ciphertext character.
Once the attacker has a mapping of ciphertext to plaintext characters, then decrypting a separate ciphertext can be done by looking up each plaintext character in the mapping.
If the plaintext is not comprehensive (i.e. only represents a subset of the possible 256 8-bit values), then the attacker can analyze the patterns of the observed plaintext to ciphertext mappings an infer what the remaining mappings are.
For example, if an attacker observes each plaintext character is mapped to the character 10 steps ahead in an ASCII table, then the attacker can apply this 10 character offset rule to unobserved plaintext characters.



