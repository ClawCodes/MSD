package assignment02;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.Comparator;

/**
 * Class representation of a library (a collection of library books).
 *
 */
public class Library<Type> {

  private ArrayList<LibraryBook<Type>> library;

  public Library() {
    library = new ArrayList<>();
  }

  /**
   * Add the specified book to the library, assume no duplicates.
   *
   * @param isbn
   *          -- ISBN of the book to be added
   * @param author
   *          -- author of the book to be added
   * @param title
   *          -- title of the book to be added
   */
  public void add(long isbn, String author, String title) {
    library.add((LibraryBook<Type>) new LibraryBook(isbn, author, title));
  }

  /**
   * Add the list of library books to the library, assume no duplicates.
   *
   * @param list
   *          -- list of library books to be added
   */
  public void addAll(ArrayList<LibraryBook<Type>> list) {
    library.addAll(list);
  }

  /**
   * Add books specified by the input file. One book per line with ISBN, author,
   * and title separated by tabs.
   *
   * If file does not exist or format is violated, do nothing.
   *
   * @param filename
   */
  public void addAll(String filename) {
    ArrayList<LibraryBook<Type>> toBeAdded = new ArrayList<>();

    try (Scanner fileIn = new Scanner(new File(filename))) {

      int lineNum = 1;

      while (fileIn.hasNextLine()) {
        String line = fileIn.nextLine();

        try (Scanner lineIn = new Scanner(line)) {
          lineIn.useDelimiter("\\t");

          if (!lineIn.hasNextLong()) {
            throw new ParseException("ISBN", lineNum);
          }
          long isbn = lineIn.nextLong();

          if (!lineIn.hasNext()) {
            throw new ParseException("Author", lineNum);
          }
          String author = lineIn.next();

          if (!lineIn.hasNext()) {
            throw new ParseException("Title", lineNum);
          }
          String title = lineIn.next();
          toBeAdded.add(new LibraryBook<>(isbn, author, title));
        }
        lineNum++;
      }
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage() + " Nothing added to the library.");
      return;
    } catch (ParseException e) {
      System.err.println(e.getLocalizedMessage() + " formatted incorrectly at line " + e.getErrorOffset()
              + ". Nothing added to the library.");
      return;
    }

    library.addAll(toBeAdded);
  }

  /**
   * Returns the holder of the library book with the specified ISBN.
   *
   * If no book with the specified ISBN is in the library, returns null.
   *
   * @param isbn
   *          -- ISBN of the book to be looked up
   */
  public Type lookup(long isbn) {
    for (Object item : library) {
      LibraryBook<Type> book = (LibraryBook<Type>) item;
      if (book.getIsbn() == isbn) {
        return book.getHolder(); // can return null when book is not checked out
      }
    }
    return null;
  }

  /**
   * Returns the list of library books checked out to the specified holder.
   *
   * If the specified holder has no books checked out, returns an empty list.
   *
   * @param holder
   *          -- holder whose checked out books are returned
   */
  public ArrayList<LibraryBook<Type>> lookup(Type holder) {
    ArrayList<LibraryBook<Type>> checkedOut = new ArrayList<>();
    for (Object item : library) {
      LibraryBook<Type> book = (LibraryBook<Type>) item;
      if (book.isCheckedOut() && book.getHolder().equals(holder)) {
        checkedOut.add(book);
      }
    }
    return checkedOut;
  }

  /**
   * Sets the holder and due date of the library book with the specified ISBN.
   *
   * If no book with the specified ISBN is in the library, returns false.
   *
   * If the book with the specified ISBN is already checked out, returns false.
   *
   * Otherwise, returns true.
   *
   * @param isbn
   *          -- ISBN of the library book to be checked out
   * @param holder
   *          -- new holder of the library book
   * @param month
   *          -- month of the new due date of the library book
   * @param day
   *          -- day of the new due date of the library book
   * @param year
   *          -- year of the new due date of the library book
   *
   */
  public boolean checkout(long isbn, Type holder, int month, int day, int year) {
    for (Object item : library) {
      LibraryBook<Type> book = (LibraryBook<Type>) item;
      if (book.getIsbn() == isbn) {
        if (book.isCheckedOut()) {
          return false;
        } else {
          book.checkOut(holder, year, month, day);
          return true;
        }
      }
    }
    return false; // isbn not found
  }

  /**
   * Unsets the holder and due date of the library book.
   *
   * If no book with the specified ISBN is in the library, returns false.
   *
   * If the book with the specified ISBN is already checked in, returns false.
   *
   * Otherwise, returns true.
   *
   * @param isbn
   *          -- ISBN of the library book to be checked in
   */
  public boolean checkin(long isbn) {
    for (Object item : library) {
      LibraryBook<Type> book = (LibraryBook<Type>) item;
      if (book.getIsbn() == isbn) {
        if (book.isCheckedOut()) {
          book.checkIn();
          return true;
        } else {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * Unsets the holder and due date for all library books checked out be the
   * specified holder.
   *
   * If no books with the specified holder are in the library, returns false;
   *
   * Otherwise, returns true.
   *
   * @param holder
   *          -- holder of the library books to be checked in
   */
  public boolean checkin(Type holder) {
    boolean hasBooks = false;
    for (Object item : library) {
      LibraryBook<Type> book = (LibraryBook<Type>) item;
      if (book.isCheckedOut() && book.getHolder().equals(holder)) {
        book.checkIn();
        hasBooks = true;
      }
    }
    return hasBooks;
  }

  public ArrayList<LibraryBook<Type>> getAllbooks(){
    return library;
  }

  /**
   * Get a copy of the existing library member variable
   * @return copy of library
   */
  private ArrayList<LibraryBook<Type>> getCopy(){
    ArrayList<LibraryBook<Type>> libraryCopy = new ArrayList<>();
    libraryCopy.addAll(library);
    return libraryCopy;
  }

  /**
   * Returns the list of library books, sorted by ISBN (smallest ISBN
   first).
   */
  public ArrayList<LibraryBook<Type>> getInventoryList() {
    ArrayList<LibraryBook<Type>> libraryCopy = getCopy();
    sort(libraryCopy, new OrderByIsbn());
    return libraryCopy;
  }

  /**
   * Returns the list of library books, sorted by author
   */
  public ArrayList<LibraryBook<Type>> getOrderedByAuthor() {
    ArrayList<LibraryBook<Type>> libraryCopy = getCopy();
    sort(libraryCopy, new OrderByAuthor());
    return libraryCopy;
  }

  /**
   * Returns the list of library books whose due date is older than the
   input
   * date. The list is sorted by date (oldest first).
   *
   * If no library books are overdue, returns an empty list.
   */
  public ArrayList<LibraryBook<Type>> getOverdueList(int month, int day, int year) {
    GregorianCalendar deadline = new GregorianCalendar(year, month, day);
    ArrayList<LibraryBook<Type>> pastDue = new ArrayList<>();
    for (LibraryBook<Type> book : library) {
      if (book.isCheckedOut() && book.getDueDate().compareTo(deadline) < 0) {
        pastDue.add(book);
      }
    }
    sort(pastDue, new OrderByDueDate());
    return pastDue;
  }

  /**
   * Performs a SELECTION SORT on the input ArrayList.
   * 1. Find the smallest item in the list.
   * 2. Swap the smallest item with the first item in the list.
   * 3. Now let the list be the remaining unsorted portion
   * (second item to Nth item) and repeat steps 1, 2, and 3.
   */
  private static <ListType> void sort(ArrayList<ListType> list, Comparator<ListType> c) {
    for (int i = 0; i < list.size() - 1; i++) {
      int j, minIndex;
      for (j = i + 1, minIndex = i; j < list.size(); j++)
        if (c.compare(list.get(j), list.get(minIndex)) < 0)
          minIndex = j;
      ListType temp = list.get(i);
      list.set(i, list.get(minIndex));
      list.set(minIndex, temp);
    }
  }

  /**
   * Comparator that defines an ordering among library books using the
   ISBN.
   */
  protected class OrderByIsbn implements Comparator<LibraryBook<Type>> {
    /**
     * Returns a negative value if lhs is smaller than rhs. Returns a positive
     * value if lhs is larger than rhs. Returns 0 if lhs 	and rhs are equal.
     */
    public int compare(LibraryBook<Type> lhs, LibraryBook<Type> rhs) {
      return (int) (lhs.getIsbn() - rhs.getIsbn());
    }
  }

  /**
   * Comparator that defines an ordering among library books using the
   author, and book title as a tie-breaker.
   */
  protected class OrderByAuthor implements Comparator<LibraryBook<Type>> {
    @Override
    public int compare(LibraryBook<Type> o1, LibraryBook<Type> o2) {
      int nameCompare = o1.getAuthor().compareTo(o2.getAuthor());
      if (nameCompare == 0) {
        return o1.getTitle().compareTo(o2.getTitle());
      }
      return nameCompare;
    }
  }

  /**
   * Comparator that defines an ordering among library books using the
   due date.
   */
  protected class OrderByDueDate implements Comparator<LibraryBook<Type>> {
    @Override
    public int compare(LibraryBook<Type> o1, LibraryBook<Type> o2) {
      return o1.getDueDate().compareTo(o2.getDueDate());
    }
  }
}
