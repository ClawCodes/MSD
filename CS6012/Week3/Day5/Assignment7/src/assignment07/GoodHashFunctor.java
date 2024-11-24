package assignment07;

public class GoodHashFunctor implements HashFunctor {
    @Override
    public int hash(String item) {
        int hash = 5381;
        for (char ch : item.toCharArray()) {
            hash = ((hash << 5) + hash) + (int) ch;
        }

        return hash;
    }
}
