import java.util.Random;

public class Dado {

    // 1. Cria uma instância de Random que será usada por todos os métodos
    // É mais eficiente criar um 'gerador' estático do que usar Math.random() repetidamente
    private static final Random gerador = new Random();

    /**
     * Rola um dado com um número de faces.
     * Usa gerador.nextInt(bound) que retorna um valor entre 0 e (bound-1).
     */
    public static int rolar(int faces) {
        // 2. Validação (Padrão de Excelência)
        if (faces <= 0) {
            throw new IllegalArgumentException("O número de faces deve ser positivo.");
        }

        // gerador.nextInt(faces) retorna de 0 a (faces-1)
        // Por isso somamos 1 para ficar de 1 a (faces)
        return gerador.nextInt(faces) + 1;
    }

    // Os métodos abaixo continuam funcionando perfeitamente
    public static int rolarD4() { return rolar(4); }
    public static int rolarD6() { return rolar(6); }
    public static int rolarD8() { return rolar(8); }
    public static int rolarD10() { return rolar(10); }
    public static int rolarD20() { return rolar(20); }
}