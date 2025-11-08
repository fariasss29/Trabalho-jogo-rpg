// import java.lang.Math; // Não é mais necessário

public class Guerreiro extends Personagem {

    /**
     * Construtor principal.
     * Chama o construtor de 5 argumentos da superclasse, que já cuida
     * da inicialização do inventário por padrão.
     */
    public Guerreiro(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
    }

    /**
     * Construtor de Cópia.
     */
    public Guerreiro(Guerreiro outroGuerreiro) {
        super(outroGuerreiro);
    }

    /**
     * Ataque básico do Guerreiro.
     * PADRÃO DE EXCELÊNCIA:
     * 1. Usa a classe 'Dado' (D8, conforme definido no seu Main.java).
     * 2. Usa 'getters' para atributos encapsulados (getAtaque, getDefesa, getNome).
     */
    @Override
    public int atacar(Personagem alvo) {
        // Usa D8, conforme a descrição da classe no seu Main.java
        int dado = Dado.rolarD8();

        // Usa getters para acessar atributos (padrão de encapsulamento)
        int danoBruto = this.getAtaque() + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        // Usa getters para os nomes e adiciona emoji para consistência
        System.out.println("⚔️ " + this.getNome() + " ataca " +
                alvo.getNome() + " com sua espada, causando " + danoReal + " de dano (D8).");

        return danoReal;
    }

    /**
     * MÉTODO ADICIONAL: Ataque especial para o Guerreiro.
     * Causa mais dano, simulando 2 dados D6.
     */
    public int ataquePoderoso(Personagem alvo) {
        System.out.println("💥 " + this.getNome() + " usa Ataque Poderoso!");

        int dadoDano = Dado.rolarD6() + Dado.rolarD6(); // 2D6
        int danoBase = this.getAtaque() + dadoDano;
        int danoReal = Math.max(3, danoBase - alvo.getDefesa());

        alvo.receberDano(danoReal);
        System.out.println("🔥 O golpe esmagador causa " + danoReal + " de dano em " + alvo.getNome() + "!");

        return danoReal;
    }

    /**
     * MÉTODO ADICIONAL: Habilidade de defesa do Guerreiro.
     * Aumenta a defesa permanentemente (ou poderia ser temporário em uma Batalha).
     */
    public void fortalecerDefesa() {
        int bonusDefesa = Dado.rolarD4(); // Aumenta a defesa em 1-4
        this.aumentarDefesa(bonusDefesa); // Usa o método seguro da superclasse

        System.out.println("🛡️ " + this.getNome() + " se concentra e fortalece sua defesa em +" + bonusDefesa + "!");
        System.out.println("🛡️ Defesa atual: " + this.getDefesa());
    }

    /**
     * MÉTODO ADICIONAL: Sobrescreve toString para incluir habilidades.
     * Segue o padrão da classe Mago.
     */
    @Override
    public String toString() {
        return super.toString() + "\n🎯 Habilidades: Ataque (D8), Ataque Poderoso (2D6)";
    }
}