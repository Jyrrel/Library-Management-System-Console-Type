# Library Management System Console
## Overview
This is a simple Library Management System Console built in Java. The program allows users to borrow, return, search, and view books in the library. It uses a command-line interface for interaction and stores information about books, their availability, and due dates.

## Features

### View Available Books:
 * Displays a list of all books currently available in the library along with their details and available copies.

### Issue Books:
* Students can borrow up to 3 books at a time.
* Borrowing is limited to a maximum of 5 days per book.
* Validates name and student ID format (XX-XXXX-XXXXXX).
* Checks book availability and prevents borrowing more copies than available.

### Return Books:
* Students can return borrowed books and specify how many copies they are returning.
* Updates the book’s availability accordingly.
* Prevents returning more copies than borrowed.

###  Add New Books: 
*  Allows users to add new books by providing a title, author, category, and number of copies.

 ### View All Borrowed Books:
* Displays all books currently borrowed by all students.
* Highlights overdue books with a clear “OVERDUE!” warning.

### Student Borrow History:
* Each student can view their borrowed books along with borrow date and due date.

 ###  Interactive Console Menu: 
* A menu-driven interface for easy interaction with the system via terminal.

## Prerequisites
1. Download and Install JDK: Visit the [Download JDK here!](https://www.oracle.com/java/technologies/downloads/#java11?er=221886) to download the JDK for your operating system (Windows, macOS, or Linux).

2. Set up Java Environment: After installing the JDK, make sure you add the JDK's bin directory to your system's PATH environment variable.

```bash
java- version
javac -version
```
You should see the installed Java version details if the setup was successful.

# How to Run the Library Management System Console
Step-by-Step Instructions:
1. Clone the Repository or Copy the Code:
Clone the repository [https://github.com/Jyrrel/Library-Management-System-Console-Type.git](https://github.com/Jyrrel/Library-Management-System-Console-Type.git) or copy the LibraryManagementSystem.java file to your local machine.

2. Navigate to the Project Directory:
Open your terminal (macOS/Linux) or command prompt (Windows) and navigate to the directory where the LibraryManagementSystem.java file is located.

3. Compile the Java File:
In the terminal/command prompt, run the following command to compile the program:
```bash
javac LibraryManagementSystem.java
```
This will compile the LibraryManagementSystem.java file into a LibraryManagementSystem.class bytecode file.

4. Run the Program:
After successful compilation, you can run the program using the command:
```bash
java LibraryManagementSystem
```
The program will display a menu allowing you to borrow, return, search, and view books in the library.

Example of Menu and Output:
<div align="center"> <img width="70%" src="Screenshot 2025-03-28 014447.png"><br><br> </div>

# Exiting the Program:
To exit, simply choose option 5 from the menu, and the program will display "Exiting Library System..." and terminate.

# Contributing
Feel free to contribute to the project by opening issues or submitting pull requests.
