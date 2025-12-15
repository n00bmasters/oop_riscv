.section .text
.globl main

main:
    li x1,  0x11
    li x2,  0x22
    li x3,  0x33
    li x4,  0x44
    li x5,  0x55
    li x6,  0x66
    li a0, 0 # exit code
    li a7, 93 # SYS_exit on RISC-V Linux
    ecall

