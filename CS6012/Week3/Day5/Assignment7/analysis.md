# Analysis

## Question 1

The `BadHashFunctor` functor simply returns the ASCII value of the first character in the string.
This will perform badly as uniqueness is limited to 256 ASCII characters, thus the underlying array would only require 256 slots.
Additionally, some characters would have frequent collisions due to their high usage in language, such as "s" since there are more english words beginning with the letter "s" than with any other letter.

## Question 2

The `MediocreHashFunctor` concatenates the individual ASCII values of each character in the input and converts up to the first 5 digits in the concatenated value to an integer.
For example:, `Hello` ASCII values are 72101108108111
`H` - 72
`e` - 101
'l' - 108
`o` - 111
The string hello receives the hash `72101`.

This functor should show reduced collisions when compared to `BadHashFunctor`. 
This is because we now use combinations of ASCII values instead of single ASCII values.
This brings our possible hash count beyond the previous 256 slots.
Collisions will only result when a string shares the same starting combination of characters.
For example, `hello` would collide with `hello world!`.
But unlike the `BadHashFunctor`; `h` and `he` will not share the same hash.

## Question 3

The `GoodHashFunctor` implements the djb2 algorithm.
This algorithm can produce a very large number of unique values and will essentially be globally unique thus collisions will only arise if the array backing the hash table is smaller than the number of possible values that will be added.