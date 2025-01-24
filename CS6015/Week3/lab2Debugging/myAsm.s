extern puts

section .text ; code goes here, not data!

global sayHello ; "sayHello" is a symbol that the linker needs to know about
global myPuts
global myGTOD

sayHello:
	mov rax, 1 ; Ensure syscall in puts writes to stdout
	mov rdi, helloString ; Move string into first parameter of puts function 
	call puts
	ret

myPuts:
	mov rax, 1
	mov rdx, rsi
	mov rsi, rdi
	mov rdi, 1
	syscall
	ret

myGTOD:

	; Prologue
	push rbp
	mov rbp, rsp
	sub rsp, 16 ; move stack to make room for two longs (seconds and microseconds)
	
	mov rax, 96 ; Denote future sys call as gettimeofday 
	mov rdi, rsp ; Fill rdi with stack address where time values will be stored
	mov rsi, 0 ; Pass 0 for timezone
	syscall 
	mov rax, [rsp] ; Move seconds in stack to rax for struct ABI
	mov rdx, [rsp+8] ; Move microseconds in stack to rdx for struct ABI

	; Epilogue
	mov rsp, rbp ; Move stack point back up 
	pop rbp ; Reset base pointer to where it started in frame
	ret

section .rodata

helloString: db "hello",0 ; Must include 0 for null terminator

