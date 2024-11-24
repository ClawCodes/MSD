package assignment07;

public class MediocreHashFunctor implements HashFunctor {
    @Override
    public int hash(String item) {
        String hashNum = "";

        for (char c : item.toCharArray()) {
            hashNum += (int)c;
        }
        return Integer.parseInt(hashNum.substring(0, 6));
    }
}
