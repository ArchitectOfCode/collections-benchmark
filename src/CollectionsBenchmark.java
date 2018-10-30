import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

class Book {
    String author;
    String title;

    public Book(String author, String title) {
        this.author = author;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return author.equals(book.author) &&
                title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title);
    }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + author;
    }
}

class BookShop {
    String shopName;
    String countryCode;

    public BookShop(String shopName, String countryCode) {
        this.shopName = shopName;
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return shopName + " (" + countryCode + ")";
    }
}

public class CollectionsBenchmark {
    public static void main(String[] args) {
        LinkedList<Book> booksList = new LinkedList<Book>();
        int noOfBooks = 1_000_000;
        String author = "";
        String title = "";
        Book book = new Book(author, title);
        long begin;
        long end;
        Random random = new Random();       // this will be used to place each book in randomly chosen shop

        System.out.println("Initializing LinkedList with " + noOfBooks + " books...");
        begin = System.currentTimeMillis();
        for(int i = 0; i < noOfBooks; i++) {
            //booksList.add(new Book("Author " + (i + 1), title = "Title " + (i + 1)));
            booksList.add(new Book("Author " + (i + 1), "Title " + (i + 1)));
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList with " + noOfBooks + " books created in " + (end - begin) + " ms.\n");

        begin = System.nanoTime();
        book = booksList.getFirst();
        end = System.nanoTime();
        System.out.println("Reading the first book (" + book.toString() + ") in LinkedList with " + noOfBooks + " books took " + (end - begin) + " ns");

        begin = System.nanoTime();
        book = booksList.getLast();
        end = System.nanoTime();
        System.out.println("Reading the last book (" + book.toString() + ") in LinkedList with " + noOfBooks + " books took " + (end - begin) + " ns\n");

        System.out.println("Removing the first object from LinkedList [" + booksList.size() + " books]...");
        begin = System.nanoTime();
        booksList.remove(0);
        end = System.nanoTime();
        System.out.println("First object from LinkedList removed in " + (end - begin) + " ns.\n");

        System.out.println("Removing the last object from LinkedList [" + booksList.size() + " books]...");
        begin = System.nanoTime();
        booksList.remove(booksList.size() - 1);
        end = System.nanoTime();
        System.out.println("Last object from LinkedList removed in " + (end - begin) + " ns.\n");


        System.out.println("Creating list of 11 shops (objects)...\n");
        BookShop[] bookShops = new BookShop[11];

        bookShops[0] = new BookShop("Amazon Niemcy", "DE");
        bookShops[1] = new BookShop("Amazon Polska", "PL");
        bookShops[2] = new BookShop("Amazon Wielka Brytania", "UK");
        bookShops[3] = new BookShop("Amazon Holandia", "NL");
        bookShops[4] = new BookShop("Amazon Czechy", "CZ");
        bookShops[5] = new BookShop("Amazon Finlandia", "FL");
        bookShops[6] = new BookShop("Amazon Szwajcaria", "CH");
        bookShops[7] = new BookShop("Amazon Wybrzeże Kości Słoniowej", "CI");
        bookShops[8] = new BookShop("Sklep Mietka", "PL");
        bookShops[9] = new BookShop("Helion", "PL");
        bookShops[10] = new BookShop("Merlin", "PL");

        System.out.println("Adding missing books to the list to keep initial number of books...");
        while(booksList.size() < noOfBooks) {

            booksList.add(new Book("Unknown author " + (System.nanoTime() + random.nextInt(100)), "Unknown title"));
        }

        HashMap<Book, BookShop> booksHashMap = new HashMap<Book, BookShop>();
        System.out.println("Assigning " + booksList.size() + " books to randomy chosen shops in HashMap...");
        begin = System.currentTimeMillis();

        // Unfortunately, assigning big number of books to randomly chosen shops taking too much time
        for(int i = 0; i < noOfBooks; i++) {
            booksHashMap.put(booksList.get(i), bookShops[random.nextInt(10)]);
            if(i != 0 && i%100_000==0) System.out.println(i + " books assigned randomly to shops.");
        }

        end = System.currentTimeMillis();
        System.out.println("HashMap with " + booksHashMap.size() + " books created in " + (end - begin) + " ms.\n");


        BookShop bookShop;
        book = booksList.getFirst();
        System.out.println("First book added to HashMap was: " + book.toString());
        begin = System.nanoTime();
        bookShop = booksHashMap.get(book);
        end = System.nanoTime();
        System.out.println(book.toString() + " is available in " + bookShop + " (found in " + (end - begin) + " ns)\n");

        book = booksList.get(noOfBooks / 2);
        System.out.println("Book added to HashMap in the middle was: " + book.toString());
        begin = System.nanoTime();
        bookShop = booksHashMap.get(book);
        end = System.nanoTime();
        System.out.println(book.toString() + " is available in " + bookShop + " (found in " + (end - begin) + " ns)\n");

        book = booksList.getLast();
        System.out.println("Book added to HashMap as the last was: " + book.toString());
        begin = System.nanoTime();
        bookShop = booksHashMap.get(book);
        end = System.nanoTime();
        System.out.println(book.toString() + " is available in " + bookShop + " (found in " + (end - begin) + " ns)\n");


        book.setAuthor("Andrzej Duda");
        book.setTitle("Obietnice wyborcze dla tłuszczy");
        bookShop = bookShops[8];
        System.out.println("Adding " + book.toString() + " to the HashMap [" + booksHashMap.size() + " books]...");
        begin = System.nanoTime();
        booksHashMap.put(book, bookShop);
        end = System.nanoTime();
        System.out.println(book.toString() + " has been added to HashMap [" + booksHashMap.size() + " books] in " + (end - begin) + " ns.\n");

        String msg = booksHashMap.get(booksList.get(booksList.size() / 2)) != null ? "found" : "not found";
        System.out.println(booksList.get(booksList.size() / 2).toString() + " has been " + msg + " in HashMap collection.\n");

        System.out.println("Removing " + booksList.get(booksList.size() / 2).toString() + " from HashMap with [" + booksHashMap.size() + " books]...");
        begin = System.nanoTime();
        booksHashMap.remove(booksList.get(booksList.size() / 2));
        end = System.nanoTime();
        System.out.println(booksList.get(booksList.size() / 2).toString() + " has been removed from HashMap in " + (end - begin) + " ns.\n");

        msg = booksHashMap.get(booksList.get(booksList.size() / 2)) != null ? "found" : "not found";
        System.out.println(booksList.get(booksList.size() / 2).toString() + " has been " + msg + " in HashMap collection.");
    }
}

/*
Część 1 (LinkedList)
Stwórz klasę reprezentującą książkę o nazwie Book.
Klasa powinna mieć dwa pola: author oraz title.
Pamiętaj o implementacji metod hashCode() oraz equals(Object o).
Będziemy jej używali jako obiektów kolekcji LinkedList w tej części zadania,
oraz jako obiektów kolekcji HashMap w drugiej części zadania.

Stwórz program, który zmierzy czas operacji wyszukiwania i usunięcia obiektu
na początku (z użyciem metody remove(Object o)), wyszukiwania i usunięcia obiektu
na końcu (z użyciem metody remove(Object o)), wstawiania na początku
oraz wstawiania na końcu listy LinkedList liczącej kilka milionów obiektów.

Część 2 (HashMap)
Stwórz program, który zmierzy czas operacji wyszukiwania po kluczu,
a także czas dodawania i usuwania obiektu z mapy HashMap liczącej kilka milionów obiektów
 */