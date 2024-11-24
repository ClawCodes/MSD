package assignment07;

import java.util.Locale;

public class BadHashFunctor implements HashFunctor {
    @Override
    public int hash(String item) {;
        return item.toLowerCase(Locale.ROOT).charAt(0);
    }
}
