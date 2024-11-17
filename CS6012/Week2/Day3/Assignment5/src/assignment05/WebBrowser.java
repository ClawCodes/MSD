package assignment05;

import java.net.URL;
import java.util.NoSuchElementException;

public class WebBrowser {
    protected LinkedListStack<URL> history_ = new LinkedListStack<>();
    protected LinkedListStack<URL> fwdHistory_ = new LinkedListStack<>();
    protected URL current_ = null;

    public WebBrowser() {}

    public WebBrowser(SinglyLinkedList<URL> history) {
            if (!history.isEmpty()) {
                for (URL url : history.reverse()) {
                    history_.push(url);
                }
                current_ = history_.pop();
        }
    }

    public void visit(URL webpage){
        if (current_ == null){
            current_ = webpage;
        } else {
            history_.push(current_);
            current_ = webpage;
            fwdHistory_.clear();
        }
    }

    public URL back() throws NoSuchElementException {
        if (history_.isEmpty()){
            throw new NoSuchElementException("There is no history to return to.");
        } else {
            fwdHistory_.push(current_);
            current_ = history_.pop();
            return current_;
        }
    }

    public URL forward() throws NoSuchElementException {
        if (fwdHistory_.isEmpty()){
            throw new NoSuchElementException("There is no history to return to.");
        } else {
            history_.push(current_);
            current_ = fwdHistory_.pop();
            return current_;
        }
    }

    public SinglyLinkedList<URL> history(){
        SinglyLinkedList<URL> orderedHistory = new SinglyLinkedList<>();
        orderedHistory.insertFirst(current_);
        while(!history_.isEmpty()){
            orderedHistory.insertFirst(history_.pop());
        }
        orderedHistory = orderedHistory.reverse();
        WebBrowser tmp = new WebBrowser(orderedHistory);
        history_ = tmp.history_;
        return orderedHistory;
    }
}
