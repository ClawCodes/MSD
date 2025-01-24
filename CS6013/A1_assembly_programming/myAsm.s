extern puts

section .text ; code goes here, not data!

global sayHello ; "sayHello" is a symbol that the linker needs to know about
global myPuts

sayHello:
	mov rax, 1 ; Ensure syscall in puts writes to stdout
	mov rdi, helloString ; Move string into first parameter of puts function 
	call puts
	ret

myPuts:
	mov rax, 1;
	mov rdx, rsi;
	mov rsi, rdi;
	mov rdi, 1;
	syscall
	ret

myGTOD:
	sub rsp, 16
	; mov [rsp], 0 
	mov rdi, rsp ; Fill rdi with stack address to fill with timeval stuct values 
	; mov [rsp+8], 0 ; Null out memory at tz struct location
	mov rsi, 0;
	syscall 
	mov rax, [rsp]
	mov rdx, [rsp+8]
	add rsp, 16
	ret

section .rodata

helloString: db "hello",0 ; Must include 0 for null terminator

