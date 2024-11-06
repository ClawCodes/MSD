package assignment02;

import java.util.GregorianCalendar;

/**
 * Representation of a library book
 * @param <HolderType> - The data type of the values uniquely identifying the holder of the book
 */
public class LibraryBook<HolderType> extends Book {
    HolderType holder = null;
    GregorianCalendar dueDate = null;

    /**
     *
     * @param isbn
     * @param author
     * @param title
     */
    public LibraryBook(long isbn, String author, String title) {
        super(isbn, author, title);
    }

    public void setDueDate(int year, int month, int day){
        dueDate = new GregorianCalendar(year, month, day);
    }

    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public void setHolder(HolderType holder){
        this.holder = holder;
    }

    public HolderType getHolder(){
        return holder;
    }

    public void checkOut(HolderType holder, int year, int month, int day){
        setDueDate(year, month, day);
        setHolder(holder);
    }

    public void checkIn(){
        holder = null;
        dueDate = null;
    }

    public boolean isCheckedOut(){
        return holder != null;
    }

}
