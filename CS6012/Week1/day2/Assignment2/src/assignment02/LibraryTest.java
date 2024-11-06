package assignment02;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testng.internal.collections.Pair;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

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

        var lib = new Library<String>();
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
        assertEquals( (booksCheckedOut.get(0)).getHolder(), "Jane Doe");
        assertEquals(booksCheckedOut.get(0).getDueDate(),new GregorianCalendar(2008, 1, 1));
        res = lib.checkin(9780330351690L);
        assertTrue(res);
        res = lib.checkin("Jane Doe");
        assertFalse(res);
    }

    @Test
    public void testLargeLibrary(){
        // test a medium library
        var lib = new Library<String>();
        lib.addAll("Mushroom_Publishing.txt");

        ArrayList<LibraryBook<String>> allBooks = lib.getAllbooks();
        assertEquals(23, allBooks.size());

        long firstISBN = 9781843190004L;
        assertEquals(firstISBN, allBooks.getFirst().getIsbn());
        assertNull(lib.lookup(firstISBN));

        long lastISBN = 9781843193319L;
        assertEquals(lastISBN, allBooks.getLast().getIsbn());
        assertNull(lib.lookup(lastISBN));

        for (LibraryBook<String> book : allBooks) {
            assertFalse(book.isCheckoutOut());
            assertNull(book.getHolder());
            assertNull(book.getDueDate());
        }

        // Test checkout process
        String testHolder = "Test Holder";

        ArrayList<LibraryBook<String>> booksCheckedOut = lib.lookup(testHolder);
        assertTrue(booksCheckedOut.isEmpty());

        assertTrue(lib.checkout(firstISBN, testHolder, 1, 20, 2008));
        assertTrue(lib.checkout(lastISBN, testHolder, 2, 25, 2010));
        // Attempt to recheckout
        assertFalse(lib.checkout(lastISBN, testHolder, 2, 25, 2010));

        assertEquals(23, lib.getAllbooks().size()); // Assert number of books has not changed during other operations

        // Test correct books were checkout out
        booksCheckedOut = lib.lookup(testHolder);
        assertEquals(booksCheckedOut.size(), 2);
        assertEquals(booksCheckedOut.getFirst(), new LibraryBook<String>(firstISBN, "Moyra Caldecott", "Weapons of the Wolfhound"));
        assertEquals(booksCheckedOut.getFirst().getIsbn(), firstISBN);
        assertEquals(booksCheckedOut.getFirst().getHolder(), testHolder);
        assertEquals(booksCheckedOut.getLast(), new LibraryBook<String>(lastISBN, "Alan Burt Akers", "Transit to Scorpio"));
        assertEquals(booksCheckedOut.getLast().getIsbn(), lastISBN);
        assertEquals(booksCheckedOut.getLast().getHolder(), testHolder);

        // Ensure only the above books were checked out
        int nCheckedOut = 0;
        for (LibraryBook<String> book : allBooks) {
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
        for (LibraryBook<String> book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(1, numCheckedOut);
        assertEquals(23, nBooks);
        assertNull(lib.lookup(firstISBN));
        ArrayList<LibraryBook<String>> holdersBooks = lib.lookup(testHolder);
        assertEquals(1, holdersBooks.size());
        // Ensure only the lastISBN book remains
        assertEquals(lastISBN, holdersBooks.getFirst().getIsbn());
        assertEquals(holdersBooks.getFirst(), new LibraryBook<String>(lastISBN, "Alan Burt Akers", "Transit to Scorpio"));

        // Checking last ISBN
        assertTrue(lib.checkin(lastISBN));
        numCheckedOut = 0;
        nBooks = 0;
        for (LibraryBook<String> book : lib.getAllbooks()) {
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
        for (LibraryBook<String> book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(4, numCheckedOut);
        assertEquals(23, nBooks);
        holdersBooks = lib.lookup(testHolder);
        assertEquals(4, holdersBooks.size());
        assertEquals(holdersBooks.getFirst(), new LibraryBook<String>(firstISBN, "Moyra Caldecott", "Weapons of the Wolfhound"));
        assertEquals(holdersBooks.get(1), new LibraryBook<String>(9781843190110L, "David Meade Betts", "Breaking the Gaze"));
        assertEquals(holdersBooks.get(2), new LibraryBook<String>(9781843190516L, "Daniel Wyatt", "The Fuehrermaster"));
        assertEquals(holdersBooks.getLast(), new LibraryBook<String>(lastISBN, "Alan Burt Akers", "Transit to Scorpio"));

        // Checkin by holder
        assertTrue(lib.checkin(testHolder));
        numCheckedOut = 0;
        nBooks = 0;
        for (LibraryBook<String> book : lib.getAllbooks()) {
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
        for (LibraryBook<String> book : lib.getAllbooks()) {
            if (book.isCheckoutOut()) {
                numCheckedOut++;
            }
            nBooks++;
        }
        assertEquals(0, numCheckedOut);
        assertEquals(23, nBooks);
    }

    @Test
    public void stringLibraryTest() {
        // test a library that uses names (String) to id patrons
        Library<String> lib = new Library<>();
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        String patron1 = "Jane Doe";

        assertTrue(lib.checkout(9780330351690L, patron1, 1, 1, 2008));
        assertTrue(lib.checkout(9780374292799L, patron1, 1, 1, 2008));

        var booksCheckedOut1 = lib.lookup(patron1);
        assertEquals(booksCheckedOut1.size(), 2);
        assertTrue(booksCheckedOut1.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
        assertTrue(booksCheckedOut1.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
        assertEquals(booksCheckedOut1.get(0).getHolder(), patron1);
        assertEquals(booksCheckedOut1.get(0).getDueDate(), new GregorianCalendar(2008, 1, 1));
        assertEquals(booksCheckedOut1.get(1).getHolder(),patron1);
        assertEquals(booksCheckedOut1.get(1).getDueDate(),new GregorianCalendar(2008, 1, 1));

        assertTrue(lib.checkin(patron1));
    }

    @Test
    public void phoneNumberTest(){
        // test a library that uses phone numbers (PhoneNumber) to id patrons
        var lib = new Library<PhoneNumber>();
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        PhoneNumber patron2 = new PhoneNumber("801.555.1234");

        assertTrue(lib.checkout(9780330351690L, patron2, 1, 1, 2008));
        assertTrue(lib.checkout(9780374292799L, patron2, 1, 1, 2008));

        ArrayList<LibraryBook<PhoneNumber>> booksCheckedOut2 = lib.lookup(patron2);

        assertEquals(booksCheckedOut2.size(), 2);
        assertTrue(booksCheckedOut2.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
        assertTrue(booksCheckedOut2.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
        assertEquals(booksCheckedOut2.get(0).getHolder(),patron2);
        assertEquals(booksCheckedOut2.get(0).getDueDate(),new GregorianCalendar(2008, 1, 1));
        assertEquals(booksCheckedOut2.get(1).getHolder(), patron2);
        assertEquals(booksCheckedOut2.get(1).getDueDate(), new GregorianCalendar(2008, 1, 1));

        assertTrue(lib.checkin(patron2));
    }

    private static Stream<Arguments> holderTypes() {
        return Stream.of(
                Arguments.of(String.class, "Test holder"),
                Arguments.of(Integer.class, 1),
                Arguments.of(Double.class, 1.5),
                Arguments.of(Pair.class, new Pair<>("test", "holder"))
        );
    }

    @ParameterizedTest
    @MethodSource("holderTypes")
    public <T> void testDifferentHolderTypes(Class<T> cls, T holder){
        Library<T> lib = new Library<>();
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        assertTrue(lib.checkout(9780374292799L, holder, 1, 1, 2008));
        ArrayList<LibraryBook<T>> checkedOut = lib.lookup(holder);
        assertEquals(checkedOut.size(), 1);
        assertEquals(checkedOut.getFirst().getHolder(), cls.cast(holder)); // Ensure holder type remains the same
        assertTrue(lib.checkin(holder));
        checkedOut = lib.lookup(holder);
        assertEquals(checkedOut.size(), 0);
    }


    @Test
    public void getInventoryListReturnsSortedCopy() {
        Library<String> lib = new Library<>();
        // Added out of order of ISBN
        lib.add(0000000000001L, "A1", "B1");
        lib.add(0000000000003L, "A3", "B3");
        lib.add(0000000000002L, "A2", "B2");

        ArrayList<LibraryBook<String>> sortedBooks = lib.getInventoryList();
        assertEquals(3, sortedBooks.size());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B1"), sortedBooks.getFirst());
        assertEquals(new LibraryBook<String>(0000000000002L, "A2", "B2"), sortedBooks.get(1));
        assertEquals(new LibraryBook<String>(0000000000003L, "A3", "B3"), sortedBooks.getLast());
    }

    @Test
    public void getInventoryListAllowsSameISBN() {
        Library<String> lib = new Library<>();
        // Added out of order of ISBN
        lib.add(0000000000001L, "A1", "B1");
        lib.add(0000000000001L, "A1", "B1");

        ArrayList<LibraryBook<String>> sortedBooks = lib.getInventoryList();
        assertEquals(2, sortedBooks.size());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B1"), sortedBooks.getFirst());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B1"), sortedBooks.getLast());
    }

    @Test
    public void getOrderByAuthorWithUniqueAuthors() {
        Library<String> lib = new Library<>();
        // Added out of order of ISBN
        // Keep ISBNs and titles the same to ensure sorting is done by author name
        lib.add(0000000000001L, "A1", "B");
        lib.add(0000000000001L, "A3", "B");
        lib.add(0000000000001L, "A2", "B");

        ArrayList<LibraryBook<String>> sortedBooks = lib.getOrderedByAuthor();
        assertEquals(3, sortedBooks.size());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B"), sortedBooks.getFirst());
        assertEquals(new LibraryBook<String>(0000000000001L, "A2", "B"), sortedBooks.get(1));
        assertEquals(new LibraryBook<String>(0000000000001L, "A3", "B"), sortedBooks.getLast());
    }

    @Test
    public void getOrderByAuthorWithTieBreaker() {
        Library<String> lib = new Library<>();
        // Added out of order of ISBN
        // Keep ISBNs and titles the same to ensure sorting is done by author name
        lib.add(0000000000001L, "A1", "B");
        lib.add(0000000000001L, "A1", "A");
        lib.add(0000000000001L, "A2", "C");

        ArrayList<LibraryBook<String>> sortedBooks = lib.getOrderedByAuthor();
        assertEquals(3, sortedBooks.size());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "A"), sortedBooks.getFirst());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B"), sortedBooks.get(1));
        assertEquals(new LibraryBook<String>(0000000000001L, "A2", "C"), sortedBooks.getLast());
    }

    @Test
    public void getOrderByAuthorWithSameBook() {
        Library<String> lib = new Library<>();
        // Added out of order of ISBN
        // Keep ISBNs and titles the same to ensure sorting is done by author name
        lib.add(0000000000001L, "A1", "B");
        lib.add(0000000000001L, "A1", "B");
        lib.add(0000000000001L, "A2", "B");

        ArrayList<LibraryBook<String>> sortedBooks = lib.getOrderedByAuthor();
        assertEquals(3, sortedBooks.size());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B"), sortedBooks.getFirst());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B"), sortedBooks.get(1));
        assertEquals(new LibraryBook<String>(0000000000001L, "A2", "B"), sortedBooks.getLast());
    }

    @Test
    public void getOrderByDueDate() {
        Library<String> lib = new Library<>();
        // Added out of order of ISBN
        lib.add(0000000000001L, "A1", "B1");
        lib.add(0000000000003L, "A3", "B3");
        lib.add(0000000000002L, "A2", "B2");
        lib.add(0000000000000L, "A$", "B4"); // Add book that will not be checked out

        assertTrue(lib.checkout(0000000000001L, "Holder name", 1, 1, 2008));
        assertTrue(lib.checkout(0000000000003L, "Holder name", 1, 1, 2007));
        assertTrue(lib.checkout(0000000000002L, "Holder name", 1, 1, 2006));

        // No books should be overdue
        ArrayList<LibraryBook<String>> overDue = lib.getOverdueList(1, 1, 2009);
        assertEquals(0, overDue.size());

        // ALL books should be overdue
        overDue = lib.getOverdueList(1, 1, 2000);
        assertEquals(3, overDue.size());

        // One book should be overdue
        overDue = lib.getOverdueList(2, 1, 2007);
        assertEquals(1, overDue.size());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B1"), overDue.getFirst());

        // two books should be overdue
        overDue = lib.getOverdueList(2, 1, 2006);
        assertEquals(2, overDue.size());
        assertEquals(new LibraryBook<String>(0000000000003L, "A3", "B3"), overDue.getFirst());
        assertEquals(new LibraryBook<String>(0000000000001L, "A1", "B1"), overDue.get(1));
    }
}