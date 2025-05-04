import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Enum for Book Categories
enum Category {
    GENERAL_WORKS, PHILOSOPHY, RELIGION, SOCIAL_SCIENCES, LANGUAGE, SCIENCE, TECHNOLOGY, ARTS, LITERATURE, HISTORY_GEOGRAPHY
}

// Book class to store book details
class Book {
    String title;
    String author;
    Category category;
    int copies;
    int issuedCopies;

    Book(String title, String author, Category category, int copies) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.copies = copies;
        this.issuedCopies = 0;
    }

    boolean isAvailable() {
        return (copies - issuedCopies) > 0;
    }

    void issueBook(int numCopies) {
        if (isAvailable() && numCopies <= (copies - issuedCopies)) {
            issuedCopies += numCopies;
        }
    }

    void returnBook(int numCopies) {
        if (issuedCopies >= numCopies) {
            issuedCopies -= numCopies;
        }
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Category: " + category + ", Available Copies: " + (copies - issuedCopies);
    }
}

// Student class to store student details and borrowed books
class Student {
    String name;
    String studentId;
    ArrayList<BorrowedBook> borrowedBooks;

    Student(String name, String studentId) {
        this.name = name;
        this.studentId = studentId;
        this.borrowedBooks = new ArrayList<>();
    }

    void listBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books borrowed.");
        } else {
            System.out.println("Books borrowed by " + name + ":");
            for (int i = 0; i < borrowedBooks.size(); i++) {
                BorrowedBook borrowedBook = borrowedBooks.get(i);
                System.out.println((i + 1) + ". " + borrowedBook);
            }
        }
    }
}

// BorrowedBook class to track borrowed books
class BorrowedBook {
    Book book;
    int numCopies;
    LocalDate borrowDate;
    LocalDate returnDate;
    Student student;

    BorrowedBook(Book book, int numCopies, LocalDate borrowDate, int days, Student student) {
        this.book = book;
        this.numCopies = numCopies;
        this.borrowDate = borrowDate;
        this.returnDate = borrowDate.plus(days, ChronoUnit.DAYS);
        this.student = student;
    }

    boolean isOverdue() {
        return LocalDate.now().isAfter(returnDate);
    }

    @Override
    public String toString() {
        return "Student: " + student.name + " (ID: " + student.studentId + "), Book: " + book.title + ", Borrowed on: " + borrowDate + ", Copies: " + numCopies + ", Due date: " + returnDate + (isOverdue() ? " - OVERDUE!" : "");
    }
}

public class LibraryManagementSystem {
    private static final ArrayList<Book> books = new ArrayList<>();
    private static final HashMap<String, Student> students = new HashMap<>();
    private static final ArrayList<BorrowedBook> allBorrowedBooks = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        books.add(new Book("The Catcher in the Rye", "J.D. Salinger", Category.LITERATURE, 5));
        books.add(new Book("1984", "George Orwell", Category.LITERATURE, 4));
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", Category.LITERATURE, 3));
        books.add(new Book("Introduction to Algorithms", "Thomas H. Cormen", Category.SCIENCE, 6));
        books.add(new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", Category.HISTORY_GEOGRAPHY, 2));

        while (true) {
            System.out.println("\n--- Library Management System ---");
            System.out.println("1. View Available Books");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Add New Book");
            System.out.println("5. View All Borrowed Books");
            System.out.println("6. Exit");
            System.out.println("7. View All Students");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: viewAvailableBooks(); break;
                case 2: issueBook(); break;
                case 3: returnBook(); break;
                case 4: addNewBook(); break;
                case 5: viewAllBorrowedBooks(); break;
                case 6: System.out.println("Exiting Library Management System..."); return;
                case 7: viewAllStudents(); break;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewAvailableBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        int totalAvailableBooks = 0;
        System.out.println("\nAvailable Books:");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.isAvailable()) {
                System.out.println((i + 1) + ". " + book);
                totalAvailableBooks += (book.copies - book.issuedCopies);
            }
        }
        System.out.println("Total Available Books: " + totalAvailableBooks);
    }

    private static void returnBook() {
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();

        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        student.listBorrowedBooks();

        if (student.borrowedBooks.isEmpty()) {
            System.out.println("You have no books to return.");
            return;
        }

        System.out.print("Enter the number of the book you want to return: ");
        int bookIndex = scanner.nextInt();
        scanner.nextLine();

        if (bookIndex < 1 || bookIndex > student.borrowedBooks.size()) {
            System.out.println("Invalid book number. Please try again.");
            return;
        }

        BorrowedBook borrowedBook = student.borrowedBooks.get(bookIndex - 1);
        Book book = borrowedBook.book;

        System.out.print("Enter the number of copies to return: ");
        int copiesToReturn = scanner.nextInt();
        scanner.nextLine();

        if (copiesToReturn <= 0 || copiesToReturn > borrowedBook.numCopies) {
            System.out.println("Invalid number of copies. Please try again.");
            return;
        }

        book.returnBook(copiesToReturn);
        student.borrowedBooks.remove(borrowedBook);

        long overdueDays = ChronoUnit.DAYS.between(borrowedBook.returnDate, LocalDate.now());
        if (overdueDays > 0) {
            int fine = (int) overdueDays * 10;
            System.out.println("This book is overdue by " + overdueDays + " day(s).");
            System.out.println("Please pay a fine of ₱" + fine + ".");
        }

        System.out.println("Book returned successfully.");
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        for (Student student : students.values()) {
            System.out.println("Student: " + student.name + " (ID: " + student.studentId + ")");
            student.listBorrowedBooks();
            System.out.println();
        }
    }

    private static void addNewBook() {
        System.out.print("Enter the book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter the author: ");
        String author = scanner.nextLine();
        System.out.print("Enter the book category (1. GENERAL_WORKS, 2. PHILOSOPHY, 3. RELIGION, 4. SOCIAL_SCIENCES, 5. LANGUAGE, 6. SCIENCE, 7. TECHNOLOGY, 8. ARTS, 9. LITERATURE, 10. HISTORY_GEOGRAPHY): ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine();
        Category category = Category.values()[categoryChoice - 1];
        System.out.print("Enter the number of copies: ");
        int copies = scanner.nextInt();
        scanner.nextLine();

        books.add(new Book(title, author, category, copies));
        System.out.println("New book added successfully.");
    }

    private static void viewAllBorrowedBooks() {
        if (allBorrowedBooks.isEmpty()) {
            System.out.println("No books have been borrowed yet.");
        } else {
            System.out.println("\nAll Borrowed Books:");
            for (BorrowedBook borrowedBook : allBorrowedBooks) {
                System.out.println(borrowedBook);
            }
        }
    }

    private static void issueBook() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();

        Student student = students.get(studentId);
        if (student == null) {
            student = new Student(name, studentId);
            students.put(studentId, student);
        }

        viewAvailableBooks();
        System.out.print("Enter the number of the book to borrow: ");
        int bookIndex = scanner.nextInt();
        scanner.nextLine();

        if (bookIndex < 1 || bookIndex > books.size()) {
            System.out.println("Invalid book selection.");
            return;
        }

        Book selectedBook = books.get(bookIndex - 1);

        if (!selectedBook.isAvailable()) {
            System.out.println("This book is currently not available.");
            return;
        }

        System.out.print("Enter number of copies to borrow: ");
        int numCopies = scanner.nextInt();
        scanner.nextLine();

        if (numCopies <= 0 || numCopies > (selectedBook.copies - selectedBook.issuedCopies)) {
            System.out.println("Invalid number of copies.");
            return;
        }

        System.out.print("Enter number of days to borrow: ");
        int days = scanner.nextInt();
        scanner.nextLine();

        selectedBook.issueBook(numCopies);
        BorrowedBook borrowedBook = new BorrowedBook(selectedBook, numCopies, LocalDate.now(), days, student);
        student.borrowedBooks.add(borrowedBook);
        allBorrowedBooks.add(borrowedBook);

        System.out.println("Book issued successfully!");
    }
}
