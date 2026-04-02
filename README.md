# Java Text Editor (Undo/Redo Implementation)

A specialized Java application that simulates a text editor's core engine, focusing on **action history management** through Undo and Redo operations.

## Technical Highlights

This project demonstrates a deep understanding of memory efficiency and linear data structures:

1. **Stack-Based History Management:**
   - Utilizes two `java.util.Stack` objects (`undoStack` and `redoStack`) to manage the application state.
   - Implements the **Command Pattern** logic where every user action (Insert, Delete, Replace) is encapsulated as an `Action` object.

2. **Efficient String Manipulation:**
   - Uses `StringBuilder` instead of standard `String` for the primary text buffer to ensure $O(n)$ performance for modifications, avoiding the overhead of string immutability in Java.

3. **Atomic Action Tracking:**
   - The `Action` class records the exact state changes (type, position, old data, new data), allowing for precise state reconstruction during undo/redo cycles.



## Features

- **Insert:** Add text at any valid index.
- **Delete:** Remove a specific range of characters while preserving them in history.
- **Replace:** Swap text segments with full recovery of the original content.
- **Undo/Redo:** Unlimited history navigation (until the redo chain is broken by a new action).
- **File-Driven Execution:** `CommandReader` parses an external `actions.txt` file to execute a batch of complex editing sequences.

## Project Structure

- `TextEditor.java`: The core engine containing the logic for text modification and history stacks.
- `Action.java`: A data transfer object (DTO) representing an atomic editing operation.
- `CommandReader.java`: A robust file parser with comprehensive error handling (IO and Format exceptions).
- `Main.java`: The entry point that orchestrates the editor and the file reader.

## How to Run

1. Ensure you have an `actions.txt` file in the project root directory.
2. Example `actions.txt` content:
   ```text
   insert Hello 0
   insert  World 5
   replace Java 6 5
   undo
   redo
