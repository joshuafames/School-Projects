# Project Title: Sudoku Solver
## Description:
This repository contains a C++ program that solves Sudoku puzzles using a combination of logical deduction and brute-force techniques. The program reads a Sudoku puzzle from an input file (Input.txt), processes the puzzle, and outputs the solved puzzle to the terminal.

## Input:
- File Name: Input.txt
- Format: The file should contain a 9x9 Sudoku grid where each number is separated by a space. The number 0 represents an empty cell in the grid.

Example Input.txt:
```
8 0 0 0 0 0 0 0 0 
0 0 3 6 0 0 0 0 0 
0 7 0 0 9 0 2 0 0 
0 5 0 0 0 7 0 0 0 
0 0 0 0 4 5 7 0 0 
0 0 0 1 0 0 0 3 0 
0 0 1 0 0 0 0 6 8 
0 0 8 5 0 0 0 1 0 
0 9 0 0 0 0 4 0 0 
```
**Output:**
The program prints the solved Sudoku puzzle to the terminal in the same 9x9 format.

## How to Run:
1. Compile the Program: Use the following command to compile the SudokuSolver.cpp file: ``` g++ -o SudokuSolver SudokuSolver.cpp ```
2. Run the Program: Execute the compiled program by running: ``` ./SudokuSolver ```
3. Ensure the Input.txt file is in the same directory as the compiled program. The program will read the Sudoku puzzle from this file and output the solved puzzle to the terminal.

## Algorithm Explanation:
The Sudoku solver uses a combination of logical deduction techniques and a brute-force backtracking approach to solve the puzzle.

1. Logical Deduction:

- **Bitset Representation:** The program utilizes bitsets to represent possible values for each cell in the Sudoku grid. This reduces the search space by eliminating impossible values based on Sudoku rules.
- **Constraint Propagation:** As the solver fills in certain cells, it updates the possible values for the remaining cells, further narrowing down the options.

2. Brute-force Backtracking:
- **Recursive Backtracking:** After the initial logical deductions, the program employs a recursive backtracking approach to try out different possibilities for the remaining empty cells.
- **Validation:** For each attempt, the program checks if the current number placement is valid according to Sudoku rules. If an invalid placement is found, the solver backtracks and tries a different number.

3. Efficiency Considerations:
- The program first applies logical deductions to reduce the number of possibilities before resorting to brute-force methods, which enhances efficiency.
