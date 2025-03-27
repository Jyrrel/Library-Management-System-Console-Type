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

    private static void issueBook() {
        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();
        while (!userName.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid input. Name should only contain letters.");
            System.out.print("Enter your name: ");
            userName = scanner.nextLine();
        }

        // Validate student ID to ensure only the specified format (XX-XXXX-XXXXXX) is entered
        String studentId = "";
        while (true) {
            System.out.print("Enter your student ID (e.g., 02-2425-016386): ");
            studentId = scanner.nextLine();
            if (studentId.matches("\\d{2}-\\d{4}-\\d{6}")) { // Pattern matches the format XX-XXXX-XXXXXX
                break;
            } else {
                System.out.println("Invalid student ID format. Please use the format XX-XXXX-XXXXXX.");
            }
        }

        students.putIfAbsent(studentId, new Student(userName, studentId));
        Student student = students.get(studentId);

        // Calculate the total available copies of all books in the library
        int totalAvailableCopies = 0;
        for (Book book : books) {
            totalAvailableCopies += (book.copies - book.issuedCopies);
        }

        // Issue books
        while (true) {
            System.out.print("Enter the total number of books you want to borrow: ");
            int totalBooksToBorrow = 0;
            try {
                totalBooksToBorrow = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            if (totalBooksToBorrow <= 0) {
                System.out.println("You cannot borrow zero or negative copies. Please try again.");
            } else if (totalBooksToBorrow > totalAvailableCopies) {
                System.out.println("Oops! You've entered an amount that exceeds the total available books in the library. There are only " + totalAvailableCopies + " books available.");
            } else {
                int totalCopiesRequested = 0;

                for (int i = 0; i < totalBooksToBorrow; i++) {
                    int bookChoice = -1;
                    while (bookChoice < 1 || bookChoice > books.size()) {
                        System.out.println("Select a book to borrow:");
                        viewAvailableBooks();
                        System.out.print("Enter book number: ");
                        bookChoice = scanner.nextInt();
                        scanner.nextLine(); // consume the newline character

                        if (bookChoice < 1 || bookChoice > books.size()) {
                            System.out.println("Invalid choice. Please try again.");
                        }
                    }

                    Book selectedBook = books.get(bookChoice - 1);
                    if (selectedBook.isAvailable()) {
                        int numCopies;
                        while (true) {
                            System.out.print("Enter the number of copies to borrow: ");
                            try {
                                numCopies = Integer.parseInt(scanner.nextLine());

                                // Validate number of copies
                                if (numCopies < 1) {
                                    System.out.println("You cannot borrow less than 1 copy. Please try again.");
                                } else if (numCopies > (selectedBook.copies - selectedBook.issuedCopies)) {
                                    System.out.println("You have entered more copies than available. There are only " + (selectedBook.copies - selectedBook.issuedCopies) + " copies available. Please enter a valid number.");
                                } else {
                                    totalCopiesRequested += numCopies;
                                    break; // exit the loop if valid input is entered
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid number.");
                            }
                        }

                        // Allow the user to input the number of days to borrow the book (max 5 days)
                        int borrowDays;
                        while (true) {
                            System.out.print("Enter the number of days to borrow (max 5 days): ");
                            try {
                                borrowDays = Integer.parseInt(scanner.nextLine());

                                if (borrowDays < 1 || borrowDays > 5) {
                                    System.out.println("You can only borrow for up to 5 days. Please enter a valid number.");
                                } else {
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("You've entered invalid characters. Please try again and choose a number between 1 and 5.");
                            }
                        }

                        selectedBook.issueBook(numCopies);
                        BorrowedBook borrowedBook = new BorrowedBook(selectedBook, numCopies, LocalDate.now(), borrowDays, student);
                        student.borrowedBooks.add(borrowedBook);
                        allBorrowedBooks.add(borrowedBook);  // Add to global list of borrowed books
                        System.out.println("Book issued successfully! Due date: " + borrowedBook.returnDate);
                    } else {
                        System.out.println("The selected book is not available.");
                    }
                }

                break; // Exit the loop when all books are issued
            }
        }

        // List borrowed books after issuing
        student.listBorrowedBooks();
    }

    private static void returnBook() {
        // Validate student ID to ensure only the specified format (XX-XXXX-XXXXXX) is entered
        String studentId = "";
        while (true) {
            System.out.print("Enter your student ID (e.g., 02-2425-016386): ");
            studentId = scanner.nextLine();
            if (studentId.matches("\\d{2}-\\d{4}-\\d{6}")) { // Pattern matches the format XX-XXXX-XXXXXX
                break;
            } else {
                System.out.println("Invalid student ID format. Please use the format XX-XXXX-XXXXXX.");
            }
        }

        Student student = students.get(studentId);
        if (student == null || student.borrowedBooks.isEmpty()) {
            System.out.println("No books found under your student ID.");
            return;
        }

        System.out.println("Books you have borrowed:");
        student.listBorrowedBooks();

        System.out.print("Enter the number of the book to return: ");
        int returnChoice = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        if (returnChoice < 1 || returnChoice > student.borrowedBooks.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        BorrowedBook returnedBook = student.borrowedBooks.get(returnChoice - 1);
        int returnCopies;
        while (true) {
            System.out.print("Enter the number of copies to return: ");
            returnCopies = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            if (returnCopies < 1 || returnCopies > returnedBook.numCopies) {
                System.out.println("Invalid number of copies to return.");
            } else if (returnCopies > returnedBook.numCopies) {
                System.out.println("You cannot return more copies than you have borrowed.");
            } else {
                break; // exit the loop if valid input is entered
            }
        }

        returnedBook.book.returnBook(returnCopies);
        student.borrowedBooks.remove(returnChoice - 1);
        allBorrowedBooks.remove(returnChoice - 1);  // Remove from global list
        System.out.println("Book returned successfully!");
    }

    private static void addNewBook() {
        System.out.print("Enter the book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter the author: ");
        String author = scanner.nextLine();
        System.out.println("Select category:");
        for (Category category : Category.values()) {
            System.out.println(category.ordinal() + 1 + ". " + category);
        }
        System.out.print("Enter category number: ");
        int categoryChoice = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        Category category = Category.values()[categoryChoice - 1];
        System.out.print("Enter the number of copies: ");
        int copies = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        books.add(new Book(title, author, category, copies));
        System.out.println("Book added successfully!");
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
}
