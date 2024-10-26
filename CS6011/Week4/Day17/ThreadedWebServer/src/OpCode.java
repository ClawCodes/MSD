public enum OpCode{
    CONTINUATION(0),
    TEXT(1),
    BINARY(2),
    CLOSE(8)
    ;

    private final int value;

    OpCode(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
