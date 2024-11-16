package assignment05;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class WebBrowserTest {
    private final URL googleDotCom = new URL("https://www.google.com");
    private final URL utahEdu = new URL("https://www.utah.edu");
    private final URL gitHub = new URL("https://www.github.com");
    private final SinglyLinkedList<URL> history = new SinglyLinkedList<>();

    WebBrowserTest() throws MalformedURLException {
        history.insertFirst(googleDotCom); // most recent
        history.insert(1, utahEdu); // second most recent
        history.insert(2, gitHub); // third most recent
    }

    @Test
    void testDefaultConstructor() {
        WebBrowser webBrowser = new WebBrowser();
        assertNotNull(webBrowser);
        assertTrue(webBrowser.history_.isEmpty());
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertNull(webBrowser.current_);
    }

    @Test
    void testNonDefaultConstructorWithEmptyHistory() {
        WebBrowser webBrowser = new WebBrowser(new SinglyLinkedList<>());
        assertNotNull(webBrowser);
        assertTrue(webBrowser.history_.isEmpty());
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertNull(webBrowser.current_);
    }

    @Test
    void testNonDefaultConstructorWithSinglePageHistory() throws MalformedURLException {
        SinglyLinkedList<URL> history = new SinglyLinkedList<>();
        history.insertFirst(googleDotCom);

        WebBrowser webBrowser = new WebBrowser(history);
        assertTrue(webBrowser.history_.isEmpty());
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(googleDotCom, webBrowser.current_);
    }

    @Test
    void testNonDefaultConstructorWithMultiPageHistory() throws MalformedURLException {
        WebBrowser webBrowser = new WebBrowser(history);
        assertFalse(webBrowser.history_.isEmpty());
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(googleDotCom, webBrowser.current_);
        assertEquals(utahEdu, webBrowser.history_.pop());
        assertEquals(gitHub, webBrowser.history_.pop());
    }

    @Test
    void testVisitWithEmptyBrowser(){
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.visit(googleDotCom);

        assertTrue(webBrowser.history_.isEmpty());
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(googleDotCom, webBrowser.current_);
    }

    @Test
    void testVisitWithSinglePageHistory(){
        SinglyLinkedList<URL> history = new SinglyLinkedList<>();
        history.insertFirst(googleDotCom);
        WebBrowser webBrowser = new WebBrowser(history);
        assertTrue(webBrowser.history_.isEmpty());
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(googleDotCom, webBrowser.current_);

        webBrowser.visit(utahEdu);
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(utahEdu, webBrowser.current_);
        assertEquals(1, webBrowser.history_.size());
        assertEquals(googleDotCom, webBrowser.history_.pop());
        assertEquals(0, webBrowser.history_.size());
    }

    @Test
    void backWithEmptyBrowserThrows(){
        WebBrowser webBrowser = new WebBrowser();

        assertThrows(NoSuchElementException.class, webBrowser::back);
    }

    @Test
    void backWithSingleVisitThrows(){
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.visit(googleDotCom);
        assertThrows(NoSuchElementException.class, webBrowser::back);
    }

    @Test
    void backWithSufficientHistroyReturnsURLs(){
        WebBrowser webBrowser = new WebBrowser(history);

        assertEquals(utahEdu, webBrowser.back());
        assertEquals(1, webBrowser.fwdHistory_.size());
        assertEquals(googleDotCom, webBrowser.fwdHistory_.peek());
        assertEquals(utahEdu, webBrowser.current_);
        assertEquals(1, webBrowser.history_.size());
        assertEquals(gitHub, webBrowser.history_.peek());

        assertEquals(gitHub, webBrowser.back());
        assertEquals(2, webBrowser.fwdHistory_.size());
        assertEquals(utahEdu, webBrowser.fwdHistory_.pop());
        assertEquals(googleDotCom, webBrowser.fwdHistory_.pop());
        assertEquals(gitHub, webBrowser.current_);
        assertTrue(webBrowser.history_.isEmpty());

        // no more history
        assertThrows(NoSuchElementException.class, webBrowser::back);
    }

    @Test
    void forwardWithEmptyBrowserThrows(){
        WebBrowser webBrowser = new WebBrowser();
        assertThrows(NoSuchElementException.class, webBrowser::forward);
    }

    @Test
    void forwardWithSingleVisitThrows(){
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.visit(googleDotCom);
        assertThrows(NoSuchElementException.class, webBrowser::forward);
    }

    @Test
    void forwardWithSufficientHistroyReturnsURLs(){
        WebBrowser webBrowser = new WebBrowser(history);
        // State 1
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(2, webBrowser.history_.size());
        assertEquals(utahEdu, webBrowser.history_.peek());
        assertEquals(googleDotCom, webBrowser.current_);

        assertEquals(utahEdu, webBrowser.back());
        // State 2
        assertEquals(1, webBrowser.fwdHistory_.size());
        assertEquals(googleDotCom, webBrowser.fwdHistory_.peek());
        assertEquals(1, webBrowser.history_.size());
        assertEquals(gitHub, webBrowser.history_.peek());
        assertEquals(utahEdu, webBrowser.current_);

        assertEquals(gitHub, webBrowser.back());
        // State 3
        assertEquals(2, webBrowser.fwdHistory_.size());
        assertEquals(utahEdu, webBrowser.fwdHistory_.peek());
        assertTrue(webBrowser.history_.isEmpty());
        assertEquals(gitHub, webBrowser.current_);

        assertEquals(utahEdu, webBrowser.forward());
        // Back to state 2
        assertEquals(1, webBrowser.fwdHistory_.size());
        assertEquals(googleDotCom, webBrowser.fwdHistory_.peek());
        assertEquals(1, webBrowser.history_.size());
        assertEquals(gitHub, webBrowser.history_.peek());
        assertEquals(utahEdu, webBrowser.current_);

        assertEquals(googleDotCom, webBrowser.forward());
        // Back to state 1
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(2, webBrowser.history_.size());
        assertEquals(utahEdu, webBrowser.history_.peek());
        assertEquals(googleDotCom, webBrowser.current_);
    }

    @Test
    void visitClearsForwardHistroy() throws MalformedURLException {
        WebBrowser webBrowser = new WebBrowser(history);
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(2, webBrowser.history_.size());
        assertEquals(utahEdu, webBrowser.history_.peek());
        assertEquals(googleDotCom, webBrowser.current_);

        assertEquals(utahEdu, webBrowser.back());
        assertEquals(1, webBrowser.fwdHistory_.size());
        assertEquals(googleDotCom, webBrowser.fwdHistory_.peek());
        assertEquals(1, webBrowser.history_.size());
        assertEquals(gitHub, webBrowser.history_.peek());
        assertEquals(utahEdu, webBrowser.current_);

        URL JavaDotCom = new URL("https://www.java.com");
        webBrowser.visit(JavaDotCom);
        assertTrue(webBrowser.fwdHistory_.isEmpty());
        assertEquals(2, webBrowser.history_.size());
        assertEquals(utahEdu, webBrowser.history_.peek());
        assertEquals(JavaDotCom, webBrowser.current_);
    }

    // TODO: ADD MORE TESTS
    @Test
    void historyWithNoFwdHistory(){
        WebBrowser webBrowser = new WebBrowser(history);

        SinglyLinkedList<URL> actual = webBrowser.history();
        assertEquals(3, actual.size());
        assertEquals(googleDotCom, actual.getFirst());
        assertEquals(utahEdu, actual.get(1));
        assertEquals(gitHub, actual.get(2));
    }
}