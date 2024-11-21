package assignment06;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

class SpellCheckerTest {
    @Test
    void testSpellCheckerSingleWordIsCorrect() throws IOException {
        SpellChecker spellChecker = new SpellChecker();
        spellChecker.addToDictionary("test");

        String word = "test";
        File tmpFile = Files.createTempFile(word, ".txt").toFile();
        Files.write(tmpFile.toPath(), word.getBytes());

        assertEquals(new ArrayList<>(), spellChecker.spellCheck(tmpFile));
    }

    @Test
    void testSpellCheckerMultiWordDictionary() throws IOException {
        SpellChecker spellChecker = new SpellChecker();
        spellChecker.addToDictionary("test");
        spellChecker.addToDictionary("with");
        spellChecker.addToDictionary("additional");
        spellChecker.addToDictionary("words");

        String word = "test";
        File tmpFile = Files.createTempFile(word, ".txt").toFile();
        Files.write(tmpFile.toPath(), word.getBytes());

        assertEquals(new ArrayList<>(), spellChecker.spellCheck(tmpFile));
    }


    @Test
    void testSpellCheckerFromArrayOfStrings() throws IOException {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("hello", "world"));
        SpellChecker spellChecker = new SpellChecker(words);

        String word = "hello";
        File tmpFile = Files.createTempFile(word, ".txt").toFile();
        Files.write(tmpFile.toPath(), word.getBytes());

        assertEquals(new ArrayList<>(), spellChecker.spellCheck(tmpFile));
    }

    @Test
    void testSingleMisspelledWord() throws IOException {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("hello", "world"));
        SpellChecker spellChecker = new SpellChecker(words);

        File tmpFile = Files.createTempFile("hello", ".txt").toFile();
        Files.write(tmpFile.toPath(), "wrong".getBytes());

        assertEquals(new ArrayList<>(Arrays.asList("wrong")), spellChecker.spellCheck(tmpFile));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "Test", "TEST", "tesT", "tEst"})
    void testSpellcheckCapitalization(String input) throws IOException {
        ArrayList<String> words = new ArrayList<>(Arrays.asList("test", "capital", "letters"));
        SpellChecker spellChecker = new SpellChecker(words);

        File tmpFile = Files.createTempFile("test", ".txt").toFile();
        Files.write(tmpFile.toPath(), input.getBytes());

        assertEquals(new ArrayList<>(), spellChecker.spellCheck(tmpFile));
    }

    @Test
    void testCheckFailsAfterRemove() throws IOException {
        SpellChecker spellChecker = new SpellChecker();
        spellChecker.addToDictionary("test");

        String word = "test";
        File tmpFile = Files.createTempFile(word, ".txt").toFile();
        Files.write(tmpFile.toPath(), word.getBytes());

        assertEquals(new ArrayList<>(), spellChecker.spellCheck(tmpFile));

        spellChecker.removeFromDictionary(word);
        assertEquals(new ArrayList<>(Arrays.asList(word)), spellChecker.spellCheck(tmpFile));
    }
}