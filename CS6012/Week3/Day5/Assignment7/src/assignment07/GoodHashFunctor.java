package assignment07;

public class GoodHashFunctor implements HashFunctor {
    /**
     * Get the hash of a string using the djb2 algorithm.
     * See: <a href="http://www.cse.yorku.ca/~oz/hash.html">...</a>
     *
     * @param item item to get hash of
     * @return hash of item
     */
    @Override
    public int hash(String item) {
        int hash = 5381;
        for (char ch : item.toCharArray()) {
            hash = ((hash << 5) + hash) + (int) ch;
        }

        return hash;
    }
}
