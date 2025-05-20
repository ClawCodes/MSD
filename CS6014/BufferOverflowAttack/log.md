After reviewing the source code, it appears the `read` call under `login` allows for the attack to occur.
40 bytes is allocated on the stack to store the password, however, the read function is called to allow reading up to 1000 bytes.

After skimming the otool man page I decided to use the `-tV` option.

Given the source code, the exploitation will have to be done by writing the correct number and sequence of characters in the password.txt file.
The `_login` function makes 56 bytes of room in the stack and the buffer take the top 40 bytes of that expansion.
To overflow, I would then need to write 40 bytes of anything followed by the return address of the `success` instruction.
After running the program in debug with lldb I found the address to the `callq _success` instruction is `0x100000ebb`.
This was found by setting a breakpoint in main and running `disassem`.

Running the following will add 40 bytes and then overflow the buffer with the `success` address
```shell
python3 -c  'import sys; sys.stdout.buffer.write( b"a"*40 + b"\xbb\x0e\x00\x00")' > password.txt
```

Rerunning the executable successfully launched the shell!