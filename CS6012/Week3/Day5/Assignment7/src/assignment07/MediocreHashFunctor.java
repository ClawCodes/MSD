package assignment07;

public class MediocreHashFunctor implements HashFunctor {
    /**
     * Get the hash of an input by concatenating the ASCII values of
     * each character and returning the first 5 digits
     * Example:
     * String item = "Hello"
     *  // "Hello" as concatenated ASCII - 72101108108111
     * hash(item)
     * >>> 72101
     *
     * @param item String value to get the hash of
     * @return Hash of the item
     */
    @Override
    public int hash(String item) {
        String hashNum = "";

        for (char c : item.toCharArray()) {
            hashNum += (int)c;
        }

        if (hashNum.length() < 6) {
            return Integer.parseInt(hashNum);
        } else {
            return Integer.parseInt(hashNum.substring(0, 6));
        }
    }
}
