public class Mago extends Personagem {

    public Mago(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
        // O inventário é criado automaticamente pelo construtor da superclasse
    }

    public Mago(Mago outroMago) {
        super(outroMago); // Construtor de Cópia
    }

    @Override
    public int atacar(Personagem alvo) {
        // Usando a classe Dado sugerida anteriormente
        int dado = Dado.rolarD10(); // Rola D10

        // CORREÇÃO: usar getters em vez de acesso direto aos atributos
        int danoBruto = this.getAtaque() + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        // CORREÇÃO: usar getter para nome
        System.out.println(this.getNome() + " 🔮 lançou uma magia arcana em " +
                alvo.getNome() + " causando " + danoReal + " de dano (D10).");

        return danoReal;
    }

    // MÉTODO ADICIONAL ESPECÍFICO DO MAGO
    public int atacarComBolaDeFogo(Personagem alvo) {
        System.out.println("🔥 " + this.getNome() + " conjura Bola de Fogo!");

        int dadoDano = Dado.rolarD6() + Dado.rolarD6(); // 2D6
        int danoBase = this.getAtaque() + dadoDano;
        int danoReal = Math.max(3, danoBase - alvo.getDefesa());

        alvo.receberDano(danoReal);
        System.out.println("💥 Explosão flamejante causa " + danoReal + " de dano em " + alvo.getNome() + "!");

        return danoReal;
    }

    // MÉTODO DE CURA PARA MAGO (poderia ser uma magia de cura)
    public void meditar() {
        int cura = Dado.rolarD4() + this.getNivel();
        int vidaAntes = this.getPontosVida();

        this.curar(cura);

        int vidaRecuperada = this.getPontosVida() - vidaAntes;
        if (vidaRecuperada > 0) {
            System.out.println("🧘 " + this.getNome() + " medita e recupera " + vidaRecuperada + " pontos de vida.");
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\n🎯 Habilidades: Magia Arcana (D10), Bola de Fogo (2D6)";
    }
}