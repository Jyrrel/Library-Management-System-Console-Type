Josh Gaborne
import java.util.*;

class Book {
    String title;
    boolean isAvailable;
    Date dueDate;

    public Book(String title) {
        this.title = title;
        this.isAvailable = true;
        this.dueDate = null;
    }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void addBook(String title) {
        books.add(new Book(title));
    }

    public void borrowBook(String title) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title) && book.isAvailable) {
                book.isAvailable = false;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 7); // Due in 7 days
                book.dueDate = calendar.getTime();
                System.out.println("You borrowed: " + book.title);
                System.out.println("Due date: " + book.dueDate);
                return;
            }
        }
        System.out.println("Book not available or not found.");
    }

    public void returnBook(String title) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title) && !book.isAvailable) {
                book.isAvailable = true;
                book.dueDate = null;
                System.out.println("You returned: " + book.title);
                return;
            }
        }
        System.out.println("Book not found or not borrowed.");
    }

    public void searchBook(String title) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                System.out.println("Book found: " + book.title);
                System.out.println("Available: " + (book.isAvailable ? "Yes" : "No"));
                if (!book.isAvailable) {
                    System.out.println("Due Date: " + book.dueDate);
                }
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void showBooks() {
        System.out.println("Available Books:");
        for (Book book : books) {
            System.out.println("- " + book.title + " (Available: " + (book.isAvailable ? "Yes" : "No") + ")");
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook("Java Programming");
        library.addBook("Data Structures");
        library.addBook("Database Systems");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nLibrary Menu:");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Search Book");
            System.out.println("4. Show All Books");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter book title to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    library.borrowBook(borrowTitle);
                    break;
                case 2:
                    System.out.print("Enter book title to return: ");
                    String returnTitle = scanner.nextLine();
                    library.returnBook(returnTitle);
                    break;
                case 3:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.nextLine();
                    library.searchBook(searchTitle);
                    break;
                case 4:
                    library.showBooks();
                    break;
                case 5:
                    System.out.println("Exiting Library System...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
