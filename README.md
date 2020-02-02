# Processor Simulation on Logism

#+About The Project

In this project we are expected to design a CPU which supports a set of instructions, 16 registers with 18 bit data width. Processor will have 18 bit address width also. Instructions are AND,OR, ADD, LD, ST, ANDI, ORI, ADDI, XOR, XORI, JUMP, BEQ, BGT, BLT, BGE, BLE. CPU consists of 7 main components. These are Register File, Instruction Memory, Data memory, Control unit, Alu, Branch Operator. Instruction memory is a read-only for instruction data set. Data memory is read-write for load and store operations. Memory has 10 bit address width. Alu is responsible for XOR,OR,ADD,AND and immediate types of these instructions. Branch operator is a part that we decode the branch opcode then decide by NZP values to generate an output whether condition Is true. We also wrote an assembler to test and debug our CPU design.
