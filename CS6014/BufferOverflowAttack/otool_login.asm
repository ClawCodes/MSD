login.o:
(__TEXT,__text) section
_check_secret:
0000000000000000	subq	$0x38, %rsp
0000000000000004	movq	%rdi, 0x28(%rsp)
0000000000000009	movl	%esi, 0x24(%rsp)
000000000000000d	cmpl	$-0x1, 0x24(%rsp)
0000000000000012	jne	0x33
0000000000000018	leaq	0x19b(%rip), %rdi               ## literal pool for: "problem reading password.txt\n"
000000000000001f	movb	$0x0, %al
0000000000000021	callq	_printf
0000000000000026	movl	$_check_secret, 0x34(%rsp)
000000000000002e	jmp	0x95
0000000000000033	leaq	0x19e(%rip), %rax               ## literal pool for: "superSecretPassword"
000000000000003a	movq	%rax, 0x18(%rsp)
000000000000003f	movslq	0x24(%rsp), %rax
0000000000000044	movq	%rax, 0x8(%rsp)
0000000000000049	movq	0x18(%rsp), %rdi
000000000000004e	callq	_strlen
0000000000000053	movq	0x8(%rsp), %rcx
0000000000000058	movq	%rax, %rdx
000000000000005b	xorl	%eax, %eax
000000000000005d	cmpq	%rdx, %rcx
0000000000000060	movb	%al, 0x17(%rsp)
0000000000000064	jne	0x88
000000000000006a	movq	0x28(%rsp), %rdi
000000000000006f	movq	0x18(%rsp), %rsi
0000000000000074	movslq	0x24(%rsp), %rdx
0000000000000079	callq	_memcmp
000000000000007e	cmpl	$0x0, %eax
0000000000000081	sete	%al
0000000000000084	movb	%al, 0x17(%rsp)
0000000000000088	movb	0x17(%rsp), %al
000000000000008c	andb	$0x1, %al
000000000000008e	movzbl	%al, %eax
0000000000000091	movl	%eax, 0x34(%rsp)
0000000000000095	movl	0x34(%rsp), %eax
0000000000000099	addq	$0x38, %rsp
000000000000009d	retq
000000000000009e	nop
_check_secret1:
00000000000000a0	subq	$0x18, %rsp
00000000000000a4	movq	%rdi, 0x10(%rsp)
00000000000000a9	movl	%esi, 0xc(%rsp)
00000000000000ad	movq	0x10(%rsp), %rdi
00000000000000b2	movl	0xc(%rsp), %esi
00000000000000b6	callq	_check_secret
00000000000000bb	addq	$0x18, %rsp
00000000000000bf	retq
_success:
00000000000000c0	subq	$0x18, %rsp
00000000000000c4	movq	_sh(%rip), %rax
00000000000000cb	movq	%rax, _check_secret(%rsp)
00000000000000cf	movq	$_check_secret, 0x8(%rsp)
00000000000000d8	leaq	0x10d(%rip), %rdi               ## literal pool for: "successful login!\n"
00000000000000df	callq	_puts
00000000000000e4	movq	_sh(%rip), %rdi
00000000000000eb	movq	%rsp, %rsi
00000000000000ee	movq	_environ(%rip), %rax
00000000000000f5	movq	_check_secret(%rax), %rdx
00000000000000f8	callq	_execve
00000000000000fd	addq	$0x18, %rsp
0000000000000101	retq
0000000000000102	nopw	%cs:_check_secret(%rax,%rax)
_failure:
0000000000000110	pushq	%rax
0000000000000111	leaq	0xe7(%rip), %rdi                ## literal pool for: "wrong password\n"
0000000000000118	callq	_puts
000000000000011d	popq	%rax
000000000000011e	retq
000000000000011f	nop
_login:
0000000000000120	subq	$0x38, %rsp
0000000000000124	leaq	0xe4(%rip), %rdi                ## literal pool for: "password.txt"
000000000000012b	xorl	%esi, %esi
000000000000012d	movb	$0x0, %al
000000000000012f	callq	_open
0000000000000134	movl	%eax, 0xc(%rsp)
0000000000000138	leaq	0xdd(%rip), %rdi                ## literal pool for: "enter your password:\n"
000000000000013f	movb	$0x0, %al
0000000000000141	callq	_printf
0000000000000146	movl	0xc(%rsp), %edi
000000000000014a	leaq	0x10(%rsp), %rsi
000000000000014f	movl	$0x3e8, %edx                    ## imm = 0x3E8
0000000000000154	callq	_read
0000000000000159	movl	%eax, 0x8(%rsp)
000000000000015d	movl	0xc(%rsp), %edi
0000000000000161	callq	_close
0000000000000166	leaq	0x10(%rsp), %rdi
000000000000016b	movl	0x8(%rsp), %esi
000000000000016f	callq	_check_secret1
0000000000000174	addq	$0x38, %rsp
0000000000000178	retq
0000000000000179	nopl	_check_secret(%rax)
_main:
0000000000000180	pushq	%rax
0000000000000181	movl	$_check_secret, 0x4(%rsp)
0000000000000189	callq	_login
000000000000018e	movl	%eax, _check_secret(%rsp)
0000000000000191	cmpl	$0x0, _check_secret(%rsp)
0000000000000195	je	0x1a5
000000000000019b	callq	_success
00000000000001a0	jmp	0x1aa
00000000000001a5	callq	_failure
00000000000001aa	leaq	0x81(%rip), %rdi                ## literal pool for: "exiting in main\n"
00000000000001b1	callq	_puts
00000000000001b6	xorl	%eax, %eax
00000000000001b8	popq	%rcx
00000000000001b9	retq
login.o:
(__TEXT,__text) section
_check_secret:
0000000000000000	subq	$0x38, %rsp
0000000000000004	movq	%rdi, 0x28(%rsp)
0000000000000009	movl	%esi, 0x24(%rsp)
000000000000000d	cmpl	$-0x1, 0x24(%rsp)
0000000000000012	jne	0x33
0000000000000018	leaq	0x19b(%rip), %rdi               ## literal pool for: "problem reading password.txt\n"
000000000000001f	movb	$0x0, %al
0000000000000021	callq	_printf
0000000000000026	movl	$_check_secret, 0x34(%rsp)
000000000000002e	jmp	0x95
0000000000000033	leaq	0x19e(%rip), %rax               ## literal pool for: "superSecretPassword"
000000000000003a	movq	%rax, 0x18(%rsp)
000000000000003f	movslq	0x24(%rsp), %rax
0000000000000044	movq	%rax, 0x8(%rsp)
0000000000000049	movq	0x18(%rsp), %rdi
000000000000004e	callq	_strlen
0000000000000053	movq	0x8(%rsp), %rcx
0000000000000058	movq	%rax, %rdx
000000000000005b	xorl	%eax, %eax
000000000000005d	cmpq	%rdx, %rcx
0000000000000060	movb	%al, 0x17(%rsp)
0000000000000064	jne	0x88
000000000000006a	movq	0x28(%rsp), %rdi
000000000000006f	movq	0x18(%rsp), %rsi
0000000000000074	movslq	0x24(%rsp), %rdx
0000000000000079	callq	_memcmp
000000000000007e	cmpl	$0x0, %eax
0000000000000081	sete	%al
0000000000000084	movb	%al, 0x17(%rsp)
0000000000000088	movb	0x17(%rsp), %al
000000000000008c	andb	$0x1, %al
000000000000008e	movzbl	%al, %eax
0000000000000091	movl	%eax, 0x34(%rsp)
0000000000000095	movl	0x34(%rsp), %eax
0000000000000099	addq	$0x38, %rsp
000000000000009d	retq
000000000000009e	nop
_check_secret1:
00000000000000a0	subq	$0x18, %rsp
00000000000000a4	movq	%rdi, 0x10(%rsp)
00000000000000a9	movl	%esi, 0xc(%rsp)
00000000000000ad	movq	0x10(%rsp), %rdi
00000000000000b2	movl	0xc(%rsp), %esi
00000000000000b6	callq	_check_secret
00000000000000bb	addq	$0x18, %rsp
00000000000000bf	retq
_success:
00000000000000c0	subq	$0x18, %rsp
00000000000000c4	movq	_sh(%rip), %rax
00000000000000cb	movq	%rax, _check_secret(%rsp)
00000000000000cf	movq	$_check_secret, 0x8(%rsp)
00000000000000d8	leaq	0x10d(%rip), %rdi               ## literal pool for: "successful login!\n"
00000000000000df	callq	_puts
00000000000000e4	movq	_sh(%rip), %rdi
00000000000000eb	movq	%rsp, %rsi
00000000000000ee	movq	_environ(%rip), %rax
00000000000000f5	movq	_check_secret(%rax), %rdx
00000000000000f8	callq	_execve
00000000000000fd	addq	$0x18, %rsp
0000000000000101	retq
0000000000000102	nopw	%cs:_check_secret(%rax,%rax)
_failure:
0000000000000110	pushq	%rax
0000000000000111	leaq	0xe7(%rip), %rdi                ## literal pool for: "wrong password\n"
0000000000000118	callq	_puts
000000000000011d	popq	%rax
000000000000011e	retq
000000000000011f	nop
_login:
0000000000000120	subq	$0x38, %rsp
0000000000000124	leaq	0xe4(%rip), %rdi                ## literal pool for: "password.txt"
000000000000012b	xorl	%esi, %esi
000000000000012d	movb	$0x0, %al
000000000000012f	callq	_open
0000000000000134	movl	%eax, 0xc(%rsp)
0000000000000138	leaq	0xdd(%rip), %rdi                ## literal pool for: "enter your password:\n"
000000000000013f	movb	$0x0, %al
0000000000000141	callq	_printf
0000000000000146	movl	0xc(%rsp), %edi
000000000000014a	leaq	0x10(%rsp), %rsi
000000000000014f	movl	$0x3e8, %edx                    ## imm = 0x3E8
0000000000000154	callq	_read
0000000000000159	movl	%eax, 0x8(%rsp)
000000000000015d	movl	0xc(%rsp), %edi
0000000000000161	callq	_close
0000000000000166	leaq	0x10(%rsp), %rdi
000000000000016b	movl	0x8(%rsp), %esi
000000000000016f	callq	_check_secret1
0000000000000174	addq	$0x38, %rsp
0000000000000178	retq
0000000000000179	nopl	_check_secret(%rax)
_main:
0000000000000180	pushq	%rax
0000000000000181	movl	$_check_secret, 0x4(%rsp)
0000000000000189	callq	_login
000000000000018e	movl	%eax, _check_secret(%rsp)
0000000000000191	cmpl	$0x0, _check_secret(%rsp)
0000000000000195	je	0x1a5
000000000000019b	callq	_success
00000000000001a0	jmp	0x1aa
00000000000001a5	callq	_failure
00000000000001aa	leaq	0x81(%rip), %rdi                ## literal pool for: "exiting in main\n"
00000000000001b1	callq	_puts
00000000000001b6	xorl	%eax, %eax
00000000000001b8	popq	%rcx
00000000000001b9	retq
