package assignment02;

import java.util.GregorianCalendar;

public class LibraryBook extends Book {
    String holder = null;
    GregorianCalendar dueDate = null;

    public LibraryBook(long isbn, String author, String title) {
        super(isbn, author, title);
    }

    public void setDueDate(int year, int month, int day){
        dueDate = new GregorianCalendar(year, month, day);
    }

    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public void setHolder(String holder){
        this.holder = holder;
    }

    public String getHolder(){
        return holder;
    }

    public void checkOut(String holder, int year, int month, int day){
        setDueDate(year, month, day);
        setHolder(holder);
    }

    public void checkIn(){
        holder = null;
        dueDate = null;
    }

    public boolean isCheckoutOut(){
        return holder != null;
    }

}
