
login.o:	file format mach-o 64-bit x86-64

Disassembly of section __TEXT,__text:

0000000000000000 <_check_secret>:
       0: 48 83 ec 38                  	sub	rsp, 56
       4: 48 89 7c 24 28               	mov	qword ptr [rsp + 40], rdi
       9: 89 74 24 24                  	mov	dword ptr [rsp + 36], esi
       d: 83 7c 24 24 ff               	cmp	dword ptr [rsp + 36], -1
      12: 0f 85 1b 00 00 00            	jne	0x33 <_check_secret+0x33>
      18: 48 8d 3d 9b 01 00 00         	lea	rdi, [rip + 411]        ## 0x1ba <_main+0x3a>
      1f: b0 00                        	mov	al, 0
      21: e8 00 00 00 00               	call	0x26 <_check_secret+0x26>
      26: c7 44 24 34 00 00 00 00      	mov	dword ptr [rsp + 52], 0
      2e: e9 62 00 00 00               	jmp	0x95 <_check_secret+0x95>
      33: 48 8d 05 9e 01 00 00         	lea	rax, [rip + 414]        ## 0x1d8 <_main+0x58>
      3a: 48 89 44 24 18               	mov	qword ptr [rsp + 24], rax
      3f: 48 63 44 24 24               	movsxd	rax, dword ptr [rsp + 36]
      44: 48 89 44 24 08               	mov	qword ptr [rsp + 8], rax
      49: 48 8b 7c 24 18               	mov	rdi, qword ptr [rsp + 24]
      4e: e8 00 00 00 00               	call	0x53 <_check_secret+0x53>
      53: 48 8b 4c 24 08               	mov	rcx, qword ptr [rsp + 8]
      58: 48 89 c2                     	mov	rdx, rax
      5b: 31 c0                        	xor	eax, eax
      5d: 48 39 d1                     	cmp	rcx, rdx
      60: 88 44 24 17                  	mov	byte ptr [rsp + 23], al
      64: 0f 85 1e 00 00 00            	jne	0x88 <_check_secret+0x88>
      6a: 48 8b 7c 24 28               	mov	rdi, qword ptr [rsp + 40]
      6f: 48 8b 74 24 18               	mov	rsi, qword ptr [rsp + 24]
      74: 48 63 54 24 24               	movsxd	rdx, dword ptr [rsp + 36]
      79: e8 00 00 00 00               	call	0x7e <_check_secret+0x7e>
      7e: 83 f8 00                     	cmp	eax, 0
      81: 0f 94 c0                     	sete	al
      84: 88 44 24 17                  	mov	byte ptr [rsp + 23], al
      88: 8a 44 24 17                  	mov	al, byte ptr [rsp + 23]
      8c: 24 01                        	and	al, 1
      8e: 0f b6 c0                     	movzx	eax, al
      91: 89 44 24 34                  	mov	dword ptr [rsp + 52], eax
      95: 8b 44 24 34                  	mov	eax, dword ptr [rsp + 52]
      99: 48 83 c4 38                  	add	rsp, 56
      9d: c3                           	ret
      9e: 66 90                        	nop

00000000000000a0 <_check_secret1>:
      a0: 48 83 ec 18                  	sub	rsp, 24
      a4: 48 89 7c 24 10               	mov	qword ptr [rsp + 16], rdi
      a9: 89 74 24 0c                  	mov	dword ptr [rsp + 12], esi
      ad: 48 8b 7c 24 10               	mov	rdi, qword ptr [rsp + 16]
      b2: 8b 74 24 0c                  	mov	esi, dword ptr [rsp + 12]
      b6: e8 00 00 00 00               	call	0xbb <_check_secret1+0x1b>
      bb: 48 83 c4 18                  	add	rsp, 24
      bf: c3                           	ret

00000000000000c0 <_success>:
      c0: 48 83 ec 18                  	sub	rsp, 24
      c4: 48 8b 05 00 00 00 00         	mov	rax, qword ptr [rip]    ## 0xcb <_success+0xb>
      cb: 48 89 04 24                  	mov	qword ptr [rsp], rax
      cf: 48 c7 44 24 08 00 00 00 00   	mov	qword ptr [rsp + 8], 0
      d8: 48 8d 3d 0d 01 00 00         	lea	rdi, [rip + 269]        ## 0x1ec <_main+0x6c>
      df: e8 00 00 00 00               	call	0xe4 <_success+0x24>
      e4: 48 8b 3d 00 00 00 00         	mov	rdi, qword ptr [rip]    ## 0xeb <_success+0x2b>
      eb: 48 89 e6                     	mov	rsi, rsp
      ee: 48 8b 05 00 00 00 00         	mov	rax, qword ptr [rip]    ## 0xf5 <_success+0x35>
      f5: 48 8b 10                     	mov	rdx, qword ptr [rax]
      f8: e8 00 00 00 00               	call	0xfd <_success+0x3d>
      fd: 48 83 c4 18                  	add	rsp, 24
     101: c3                           	ret
     102: 66 66 66 66 66 2e 0f 1f 84 00 00 00 00 00    	nop	word ptr cs:[rax + rax]

0000000000000110 <_failure>:
     110: 50                           	push	rax
     111: 48 8d 3d e7 00 00 00         	lea	rdi, [rip + 231]        ## 0x1ff <_main+0x7f>
     118: e8 00 00 00 00               	call	0x11d <_failure+0xd>
     11d: 58                           	pop	rax
     11e: c3                           	ret
     11f: 90                           	nop

0000000000000120 <_login>:
     120: 48 83 ec 38                  	sub	rsp, 56
     124: 48 8d 3d e4 00 00 00         	lea	rdi, [rip + 228]        ## 0x20f <_main+0x8f>
     12b: 31 f6                        	xor	esi, esi
     12d: b0 00                        	mov	al, 0
     12f: e8 00 00 00 00               	call	0x134 <_login+0x14>
     134: 89 44 24 0c                  	mov	dword ptr [rsp + 12], eax
     138: 48 8d 3d dd 00 00 00         	lea	rdi, [rip + 221]        ## 0x21c <_main+0x9c>
     13f: b0 00                        	mov	al, 0
     141: e8 00 00 00 00               	call	0x146 <_login+0x26>
     146: 8b 7c 24 0c                  	mov	edi, dword ptr [rsp + 12]
     14a: 48 8d 74 24 10               	lea	rsi, [rsp + 16]
     14f: ba e8 03 00 00               	mov	edx, 1000
     154: e8 00 00 00 00               	call	0x159 <_login+0x39>
     159: 89 44 24 08                  	mov	dword ptr [rsp + 8], eax
     15d: 8b 7c 24 0c                  	mov	edi, dword ptr [rsp + 12]
     161: e8 00 00 00 00               	call	0x166 <_login+0x46>
     166: 48 8d 7c 24 10               	lea	rdi, [rsp + 16]
     16b: 8b 74 24 08                  	mov	esi, dword ptr [rsp + 8]
     16f: e8 00 00 00 00               	call	0x174 <_login+0x54>
     174: 48 83 c4 38                  	add	rsp, 56
     178: c3                           	ret
     179: 0f 1f 80 00 00 00 00         	nop	dword ptr [rax]

0000000000000180 <_main>:
     180: 50                           	push	rax
     181: c7 44 24 04 00 00 00 00      	mov	dword ptr [rsp + 4], 0
     189: e8 00 00 00 00               	call	0x18e <_main+0xe>
     18e: 89 04 24                     	mov	dword ptr [rsp], eax
     191: 83 3c 24 00                  	cmp	dword ptr [rsp], 0
     195: 0f 84 0a 00 00 00            	je	0x1a5 <_main+0x25>
     19b: e8 00 00 00 00               	call	0x1a0 <_main+0x20>
     1a0: e9 05 00 00 00               	jmp	0x1aa <_main+0x2a>
     1a5: e8 00 00 00 00               	call	0x1aa <_main+0x2a>
     1aa: 48 8d 3d 81 00 00 00         	lea	rdi, [rip + 129]        ## 0x232 <_main+0xb2>
     1b1: e8 00 00 00 00               	call	0x1b6 <_main+0x36>
     1b6: 31 c0                        	xor	eax, eax
     1b8: 59                           	pop	rcx
     1b9: c3                           	ret
