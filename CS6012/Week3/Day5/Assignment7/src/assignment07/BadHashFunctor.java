package assignment07;

import java.util.Locale;

public class BadHashFunctor implements HashFunctor {
    /**
     * @param item input string to get hash of
     * @return the ASCII value of the first character
     */
    @Override
    public int hash(String item) {;
        return item.toLowerCase(Locale.ROOT).charAt(0);
    }
}
