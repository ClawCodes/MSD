; NAME: Christopher Lawton
; CLASS: CS6013
; DATE: Jan 27th, 2025

section .text

global main

print_int:
    ; Prologue
    push rbp
    mov rbp, rsp

    mov rax, rdi       ; Place the input number into rax
    xor rbx, rbx       ; Clear rbx - will track number of bytes added to the stack
    sub rsp, 1         ; Increase stack by one byte for newline
    mov byte [rsp], 10 ; Add new line to to stack
    inc rbx            ; increment rbx for stack byte count

divide:
    xor rdx, rdx ; Clear rdx for division
    mov rcx, 10  ; Initialize 10 as the divisor
    div rcx      ; rax = rax / rcx, remainder in rdx

    add rdx, 48  ; Convert remainder to ASCII

    sub rsp, 1    ; Increment one byte on stack for digit
    mov [rsp], dl ; Place the digit (in rdx) on stack
    inc rbx       ; Increment the byte counter

    cmp rax, 0   ; Check if all digits are processed
    jne divide   ; Continue dividing if rax is not 0

    mov rax, 1   ; sys_write
    mov rdi, 1   ; File descriptor (stdout)
    mov rsi, rsp ; Pointer to the top of the stack
    mov rdx, rbx ; Number of bytes to write
    syscall

    ; Epilogue
    mov rsp, rbp
    pop rbp
    ret

main:
    mov rdi, 1234 ; Input number
    call print_int
