import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Enum for Book Categories
enum Category {
    GENERAL_WORKS, PHILOSOPHY, RELIGION, SOCIAL_SCIENCES, LANGUAGE,
    SCIENCE, TECHNOLOGY, ARTS, LITERATURE, HISTORY_GEOGRAPHY
}

// Book class to store book details
class Book {
    private final String title;
    private final String author;
    private final Category category;
    private final int copies;
    private int issuedCopies;

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
        if (numCopies > 0 && isAvailable() && numCopies <= (copies - issuedCopies)) {
            issuedCopies += numCopies;
        }
    }

    void returnBook(int numCopies) {
        if (numCopies > 0 && issuedCopies >= numCopies) {
            issuedCopies -= numCopies;
        }
    }

    int getAvailableCopies() {
        return copies - issuedCopies;
    }

    String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Category: " + category + ", Available Copies: " + getAvailableCopies();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;
        Book book = (Book) obj;
        return title.equals(book.title) && author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }
}

// Student class to store student details and borrowed books
class Student {
    final String name;
    final String studentId;
    final ArrayList<BorrowedBook> borrowedBooks;

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
                System.out.println((i + 1) + ". " + borrowedBooks.get(i));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student student = (Student) obj;
        return studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}

// BorrowedBook class to track borrowed books
class BorrowedBook {
    final Book book;
    final int numCopies;
    final LocalDate borrowDate;
    final LocalDate returnDate;
    final Student student;

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
        return "Student: " + student.name + " (ID: " + student.studentId + "), Book: " + book.getTitle()
                + ", Borrowed on: " + borrowDate + ", Copies: " + numCopies + ", Due date: " + returnDate
                + (isOverdue() ? " - OVERDUE!" : "");
    }
}

public class LibraryManagementSystem {
    private static final ArrayList<Book> books = new ArrayList<>();
    private static final HashMap<String, Student> students = new HashMap<>();
    private static final ArrayList<BorrowedBook> allBorrowedBooks = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final int FINE_PER_DAY = 10;

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
            System.out.println("6. View All Students");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> viewAvailableBooks();
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> addNewBook();
                case 5 -> viewAllBorrowedBooks();
                case 6 -> viewAllStudents();
                case 7 -> {
                    System.out.println("Exiting Library Management System...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
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
                totalAvailableBooks += book.getAvailableCopies();
            }
        }
        System.out.println("Total Available Books: " + totalAvailableBooks);
    }

    private static void returnBook() {
        String studentId = getLineInput("Enter your student ID: ");
        Student student = students.get(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        student.listBorrowedBooks();
        if (student.borrowedBooks.isEmpty()) return;

        System.out.print("Enter the number of the book you want to return: ");
        int bookIndex = getIntInput();
        if (bookIndex < 1 || bookIndex > student.borrowedBooks.size()) {
            System.out.println("Invalid book number.");
            return;
        }

        BorrowedBook borrowedBook = student.borrowedBooks.get(bookIndex - 1);
        Book book = borrowedBook.book;

        System.out.print("Enter the number of copies to return: ");
        int copiesToReturn = getIntInput();
        if (copiesToReturn <= 0 || copiesToReturn > borrowedBook.numCopies) {
            System.out.println("Invalid number of copies.");
            return;
        }

        book.returnBook(copiesToReturn);
        student.borrowedBooks.remove(borrowedBook);

        long overdueDays = ChronoUnit.DAYS.between(borrowedBook.returnDate, LocalDate.now());
        if (overdueDays > 0) {
            int fine = (int) overdueDays * FINE_PER_DAY;
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
        String title = getLineInput("Enter the book title: ");
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }

        String author = getLineInput("Enter the author: ");
        if (author.isEmpty()) {
            System.out.println("Author cannot be empty.");
            return;
        }

        System.out.println("Enter the book category:");
        for (int i = 0; i < Category.values().length; i++) {
            System.out.println((i + 1) + ". " + Category.values()[i]);
        }

        int categoryChoice = getIntInput();
        if (categoryChoice < 1 || categoryChoice > Category.values().length) {
            System.out.println("Invalid category.");
            return;
        }

        Category category = Category.values()[categoryChoice - 1];
        System.out.print("Enter the number of copies: ");
        int copies = getIntInput();

        if (copies <= 0) {
            System.out.println("Number of copies must be positive.");
            return;
        }

        books.add(new Book(title, author, category, copies));
        System.out.println("New book added successfully.");
    }

    private static void viewAllBorrowedBooks() {
        if (allBorrowedBooks.isEmpty()) {
            System.out.println("No books have been borrowed yet.");
            return;
        }

        System.out.println("\nAll Borrowed Books:");
        int totalFine = 0;
        for (BorrowedBook borrowedBook : allBorrowedBooks) {
            System.out.println(borrowedBook);
            if (borrowedBook.isOverdue()) {
                long overdueDays = ChronoUnit.DAYS.between(borrowedBook.returnDate, LocalDate.now());
                totalFine += overdueDays * FINE_PER_DAY;
            }
        }
        System.out.println("Total Potential Fines: ₱" + totalFine);
    }

    private static void issueBook() {
        if (books.isEmpty()) {
            System.out.println("No books available to issue.");
            return;
        }

        String name = getLineInput("Enter your name: ");
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        String studentId = getLineInput("Enter your student ID: ");
        if (studentId.isEmpty()) {
            System.out.println("Student ID cannot be empty.");
            return;
        }

        final Student student = students.getOrDefault(studentId, new Student(name, studentId));
        students.putIfAbsent(studentId, student);

        viewAvailableBooks();
        System.out.print("Enter the number of the book to borrow: ");
        int bookIndex = getIntInput();

        if (bookIndex < 1 || bookIndex > books.size()) {
            System.out.println("Invalid book selection.");
            return;
        }

        final Book selectedBook = books.get(bookIndex - 1);
        if (!selectedBook.isAvailable()) {
            System.out.println("This book is currently not available.");
            return;
        }

        System.out.print("Enter number of copies to borrow: ");
        int numCopies = getIntInput();
        if (numCopies <= 0 || numCopies > selectedBook.getAvailableCopies()) {
            System.out.println("Invalid number of copies.");
            return;
        }

        System.out.print("Enter number of days to borrow: ");
        int days = getIntInput();
        if (days <= 0) {
            System.out.println("Borrow duration must be positive.");
            return;
        }

        selectedBook.issueBook(numCopies);
        final BorrowedBook borrowedBook = new BorrowedBook(selectedBook, numCopies, LocalDate.now(), days, student);
        student.borrowedBooks.add(borrowedBook);
        allBorrowedBooks.add(borrowedBook);

        System.out.println("Book issued successfully!");
    }

    private static int getIntInput() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                return input;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static String getLineInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
