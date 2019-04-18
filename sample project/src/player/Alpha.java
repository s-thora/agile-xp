package player;

public enum Alpha {
    A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7);

    private final int value;

    Alpha(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
