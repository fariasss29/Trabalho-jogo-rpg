import java.util.Random;

public class Dado {
    private static final Random gerador = new Random();

    public static int rolar(int faces) {
        if (faces <= 0) {
            throw new IllegalArgumentException("O número de faces deve ser positivo.");
        }
        return gerador.nextInt(faces) + 1;
    }

    public static int rolarD4() { return rolar(4); }
    public static int rolarD6() { return rolar(6); }
    public static int rolarD8() { return rolar(8); }
    public static int rolarD10() { return rolar(10); }
    public static int rolarD20() { return rolar(20); }
}