package assignment02;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    @Test
    public void testEmpty() {
        Library lib = new Library();
        assertNull(lib.lookup(978037429279L));

        ArrayList<LibraryBook> booksCheckedOut = lib.lookup("Jane Doe");
        assertEquals(booksCheckedOut.size(), 0);

        assertFalse(lib.checkout(978037429279L, "Jane Doe", 1, 1, 2008));
        assertFalse(lib.checkin(978037429279L));
        assertFalse(lib.checkin("Jane Doe"));
    }

    @Test
    public void testNonEmpty() {

        var lib = new Library();
        // test a small library
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        assertNull(lib.lookup(9780330351690L)); //not checked out
        var res = lib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008);
        assertTrue(res);
        var booksCheckedOut = lib.lookup("Jane Doe");
        assertEquals(booksCheckedOut.size(), 1);
        assertEquals(booksCheckedOut.get(0),new Book(9780330351690L, "Jon Krakauer", "Into the Wild"));
        assertEquals(booksCheckedOut.get(0).getHolder(), "Jane Doe");
        assertEquals(booksCheckedOut.get(0).getDueDate(),new GregorianCalendar(2008, 1, 1));
        res = lib.checkin(9780330351690L);
        assertTrue(res);
        res = lib.checkin("Jane Doe");
        assertFalse(res);
    }

    @Test
    public void testLargeLibrary(){
        // test a medium library
        var lib = new Library();
        lib.addAll("Mushroom_Publishing.txt");

        ArrayList<LibraryBook> allBooks = lib.getAllbooks();
        assertEquals(23, allBooks.size());

        long firstISBN = 9781843190004L;
        assertEquals(firstISBN, allBooks.getFirst().getIsbn());
        assertNull(lib.lookup(firstISBN));

        long lastISBN = 9781843193319L;
        assertEquals(lastISBN, allBooks.getLast().getIsbn());
        assertNull(lib.lookup(lastISBN));

        for (LibraryBook book : allBooks) {
            assertFalse(book.isCheckoutOut());
            assertNull(book.getHolder());
            assertNull(book.getDueDate());
        }

        // Test checkout process
        String testHolder = "Test Holder";

        ArrayList<LibraryBook> booksCheckedOut = lib.lookup(testHolder);
        assertTrue(booksCheckedOut.isEmpty());

        assertTrue(lib.checkout(firstISBN, testHolder, 1, 20, 2008));
        assertTrue(lib.checkout(lastISBN, testHolder, 2, 25, 2010));
        // Attempt to recheckout
        assertFalse(lib.checkout(lastISBN, testHolder, 2, 25, 2010));

        assertEquals(23, lib.getAllbooks().size()); // Assert number of books has not changed during other operations

        // Test correct books were checkout out
        booksCheckedOut = lib.lookup(testHolder);
        assertEquals(booksCheckedOut.size(), 2);
        assertEquals(booksCheckedOut.getFirst(), new LibraryBook(firstISBN, "Moyra Caldecott", "Weapons of the Wolfhound"));
        assertEquals(booksCheckedOut.getFirst().getIsbn(), firstISBN);
        assertEquals(booksCheckedOut.getFirst().getHolder(), testHolder);
        assertEquals(booksCheckedOut.getLast(), new LibraryBook(lastISBN, "Alan Burt Akers", "Transit to Scorpio"));
        assertEquals(booksCheckedOut.getLast().getIsbn(), lastISBN);
        assertEquals(booksCheckedOut.getLast().getHolder(), testHolder);

        // Ensure only the above books were checked out
        int nCheckedOut = 0;
        for (LibraryBook book : allBooks) {
            if (book.isCheckoutOut()) {
                nCheckedOut++;
            }
        }
        assertEquals(2, nCheckedOut);

        // Test checkin with ISBN
        assertTrue(lib.checkin(firstISBN));
        // Ensure only one book was checked out and one remains checked in
        int numCheckedOut = 0;
        int nBooks = 0;
        for (LibraryBook book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(1, numCheckedOut);
        assertEquals(23, nBooks);
        assertNull(lib.lookup(firstISBN));
        var holdersBooks = lib.lookup(testHolder);
        assertEquals(1, holdersBooks.size());
        // Ensure only the lastISBN book remains
        assertEquals(lastISBN, holdersBooks.get(0).getIsbn());
        assertEquals(holdersBooks.getFirst(), new LibraryBook(lastISBN, "Alan Burt Akers", "Transit to Scorpio"));

        // Checking last ISBN
        assertTrue(lib.checkin(lastISBN));
        numCheckedOut = 0;
        nBooks = 0;
        for (LibraryBook book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(0, numCheckedOut);
        assertEquals(23, nBooks);
        assertNull(lib.lookup(lastISBN));
        holdersBooks = lib.lookup(testHolder);
        assertTrue(holdersBooks.isEmpty());

        // Checkout to test checkin with holder name
        assertTrue(lib.checkout(firstISBN, testHolder, 1, 20, 2008));
        assertTrue(lib.checkout(9781843190110L, testHolder, 1, 20, 2008));
        assertTrue(lib.checkout(9781843190516L, testHolder, 1, 20, 2008));
        assertTrue(lib.checkout(lastISBN, testHolder, 1, 20, 2008));

        numCheckedOut = 0;
        nBooks = 0;
        for (LibraryBook book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(4, numCheckedOut);
        assertEquals(23, nBooks);
        // Ensure correct book was checked out
        holdersBooks = lib.lookup(testHolder);
        assertEquals(4, holdersBooks.size());
        // Ensure only the lastISBN book remains
        assertEquals(firstISBN, holdersBooks.getFirst().getIsbn());
        assertEquals(holdersBooks.getFirst(), new LibraryBook(firstISBN, "Moyra Caldecott", "Weapons of the Wolfhound"));
        assertEquals(holdersBooks.get(1), new LibraryBook(9781843190110L, "David Meade Betts", "Breaking the Gaze"));
        assertEquals(holdersBooks.get(2), new LibraryBook(9781843190516L, "Daniel Wyatt", "The Fuehrermaster"));

        // Checkin by holder
        assertTrue(lib.checkin(testHolder));
        numCheckedOut = 0;
        nBooks = 0;
        for (LibraryBook book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(0, numCheckedOut);
        assertEquals(23, nBooks);
        holdersBooks = lib.lookup(testHolder);
        assertTrue(holdersBooks.isEmpty());

        // attempt recheckin
        assertFalse(lib.checkin(testHolder));

        // Ensure library remains intact
        numCheckedOut = 0;
        nBooks = 0;
        for (LibraryBook book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(0, numCheckedOut);
        assertEquals(23, nBooks);
    }

}