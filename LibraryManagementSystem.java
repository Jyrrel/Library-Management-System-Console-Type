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
            
            int choice = getValidOptionInput(1, 6);  // Added validation for this input

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

    private static int getValidOptionInput(int min, int max) {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid option. Please try again and choose a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("You've entered invalid characters. Please try again and choose a number between " + min + " and " + max + ".");
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
        students.putIfAbsent(studentId, new Student(userName, studentId));
        Student student = students.get(studentId);

        int totalBooksToBorrow;
        do {
            System.out.print("How many books would you like to borrow (max 3 copies per book)? ");
            totalBooksToBorrow = getValidOptionInput(1, 3);  // Validate input for book count

        } while (totalBooksToBorrow <= 0 || totalBooksToBorrow > 3);

        // Issue books
        for (int i = 0; i < totalBooksToBorrow; i++) {
            int bookChoice = -1;
            while (bookChoice < 1 || bookChoice > books.size()) {
                System.out.println("Select a book to borrow:");
                viewAvailableBooks();
                System.out.print("Enter book number: ");
                bookChoice = getValidOptionInput(1, books.size());  // Validate book selection input
            }

            Book selectedBook = books.get(bookChoice - 1);
            if (selectedBook.isAvailable()) {
                int numCopies;
                while (true) {
                    System.out.print("Enter the number of copies to borrow (max 3): ");
                    try {
                        numCopies = Integer.parseInt(scanner.nextLine());
                        if (numCopies < 1 || numCopies > 3) {
                            System.out.println("Invalid number of copies. Please choose between 1 and 3.");
                        } else if (numCopies > (selectedBook.copies - selectedBook.issuedCopies)) {
                            System.out.println("Not enough copies available.");
                        } else {
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("You've entered invalid characters. Please try again and choose a number between 1 and 3.");
                    }
                }

                int borrowDays;
                while (true) {
                    System.out.print("Enter the number of days to borrow (max 5 days): ");
                    borrowDays = getValidOptionInput(1, 5);  // Validate borrow days input
                    if (borrowDays > 0 && borrowDays <= 5) {
                        break;
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

        // List borrowed books after issuing
        student.listBorrowedBooks();
    }

    private static void returnBook() {
        System.out.print("Enter your student ID: ");
        String studentId = scanner.nextLine();
        Student student = students.get(studentId);
        if (student == null || student.borrowedBooks.isEmpty()) {
            System.out.println("No books found under your student ID.");
            return;
        }

        System.out.println("Books you have borrowed:");
        student.listBorrowedBooks();

        System.out.print("Enter the number of the book to return: ");
        int returnChoice = getValidOptionInput(1, student.borrowedBooks.size());  // Validate return choice input

        BorrowedBook returnedBook = student.borrowedBooks.get(returnChoice - 1);
        int returnCopies;
        while (true) {
            System.out.print("Enter the number of copies to return: ");
            returnCopies = getValidOptionInput(1, returnedBook.numCopies);  // Validate return copies input
            if (returnCopies <= returnedBook.numCopies) {
                break;
            }
        }

        returnedBook.book.returnBook(returnCopies);
        student.borrowedBooks.remove(returnChoice - 1);
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
        int categoryChoice = getValidOptionInput(1, Category.values().length);  // Validate category choice input

        Category category = Category.values()[categoryChoice - 1];

        System.out.print("Enter the number of copies: ");
        int copies = getValidOptionInput(1, Integer.MAX_VALUE);  // Validate number of copies input

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
