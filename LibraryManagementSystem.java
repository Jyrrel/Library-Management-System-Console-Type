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

// BorrowedBook class to track borrowed books
class BorrowedBook {
    Book book;
    int numCopies;
    LocalDate borrowDate;
    LocalDate returnDate;
    String studentName;
    String studentId;

    BorrowedBook(Book book, int numCopies, LocalDate borrowDate, int days, String studentName, String studentId) {
        this.book = book;
        this.numCopies = numCopies;
        this.borrowDate = borrowDate;
        this.returnDate = borrowDate.plus(days, ChronoUnit.DAYS);
        this.studentName = studentName;
        this.studentId = studentId;
    }

    boolean isOverdue() {
        return LocalDate.now().isAfter(returnDate);
    }

    @Override
    public String toString() {
        return "Student: " + studentName + " (ID: " + studentId + "), Book: " + book.title + ", Borrowed on: " + borrowDate + ", Copies: " + numCopies + ", Due date: " + returnDate + (isOverdue() ? " - OVERDUE!" : "");
    }
}

// User class to track borrowed books
class User {
    String name;
    String studentId;
    ArrayList<BorrowedBook> borrowedBooks;

    User(String name, String studentId) {
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

public class LibraryManagementSystem {
    private static final ArrayList<Book> books = new ArrayList<>();
    private static final HashMap<String, User> users = new HashMap<>();
    private static final ArrayList<BorrowedBook> allBorrowedBooks = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Predefined books
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
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (choice) {
                case 1 -> viewAvailableBooks();
                case 2 -> issueBook();
                case 3 -> returnBook();
                case 4 -> addNewBook();
                case 5 -> viewAllBorrowedBooks();
                case 6 -> {
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
        System.out.println("\nAvailable Books:");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.isAvailable()) {
                System.out.println((i + 1) + ". " + book);
            }
        }
    }

    private static void issueBook() {
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();
        users.putIfAbsent(studentId, new User(userName, studentId));
        User user = users.get(studentId);

        System.out.print("How many books would you like to borrow (max 3 copies per book)? ");
        int numBooks = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        // Issue books
        for (int i = 0; i < numBooks; i++) {
            System.out.println("Select a book to borrow:");
            viewAvailableBooks();
            System.out.print("Enter book number: ");
            int bookChoice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            if (bookChoice < 1 || bookChoice > books.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            Book selectedBook = books.get(bookChoice - 1);
            if (selectedBook.isAvailable()) {
                System.out.print("Enter the number of copies to borrow (max 3): ");
                int numCopies = scanner.nextInt();
                scanner.nextLine(); // consume the newline character

                // Validate number of copies
                if (numCopies < 1 || numCopies > 3) {
                    System.out.println("Invalid number of copies. Please choose between 1 and 3.");
                    return;
                }

                if (numCopies > (selectedBook.copies - selectedBook.issuedCopies)) {
                    System.out.println("Not enough copies available.");
                    return;
                }

                selectedBook.issueBook(numCopies);
                BorrowedBook borrowedBook = new BorrowedBook(selectedBook, numCopies, LocalDate.now(), 7, userName, studentId); // Include student ID
                user.borrowedBooks.add(borrowedBook);
                allBorrowedBooks.add(borrowedBook);  // Add to global list of borrowed books
                System.out.println("Book issued successfully! Due date: " + borrowedBook.returnDate);
            } else {
                System.out.println("The selected book is not available.");
            }
        }

        // List borrowed books after issuing
        user.listBorrowedBooks();
    }

    private static void returnBook() {
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();
        User user = users.get(studentId);
        if (user == null || user.borrowedBooks.isEmpty()) {
            System.out.println("No books found under your student ID.");
            return;
        }

        System.out.println("Books you have borrowed:");
        user.listBorrowedBooks();

        System.out.print("Enter the number of the book to return: ");
        int returnChoice = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        if (returnChoice < 1 || returnChoice > user.borrowedBooks.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        BorrowedBook returnedBook = user.borrowedBooks.get(returnChoice - 1);
        System.out.print("Enter the number of copies to return: ");
        int returnCopies = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        if (returnCopies < 1 || returnCopies > returnedBook.numCopies) {
            System.out.println("Invalid number of copies to return.");
            return;
        }

        returnedBook.book.returnBook(returnCopies);
        user.borrowedBooks.remove(returnChoice - 1);
        allBorrowedBooks.remove(returnChoice - 1);  // Remove from global list
        System.out.println("Book returned successfully!");
    }

    private static void addNewBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();

        System.out.println("Select book category:");
        for (int i = 0; i < Category.values().length; i++) {
            System.out.println((i + 1) + ". " + Category.values()[i]);
        }
        System.out.print("Enter category number: ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        if (categoryChoice < 1 || categoryChoice > Category.values().length) {
            System.out.println("Invalid category choice.");
            return;
        }

        Category category = Category.values()[categoryChoice - 1];

        System.out.print("Enter the number of copies: ");
        int copies = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        Book newBook = new Book(title, author, category, copies);
        books.add(newBook);
        System.out.println("New book added successfully!");
    }

    private static void viewAllBorrowedBooks() {
        if (allBorrowedBooks.isEmpty()) {
            System.out.println("No books have been borrowed yet.");
        } else {
            System.out.println("All Borrowed Books:");
            for (int i = 0; i < allBorrowedBooks.size(); i++) {
                System.out.println((i + 1) + ". " + allBorrowedBooks.get(i));
            }
        }
    }
}
