public class Arqueiro extends Personagem {

    /**
     * Construtor principal.
     * Chama o construtor de 5 argumentos da superclasse, que já cuida
     * da inicialização do inventário por padrão.
     */
    public Arqueiro(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
    }

    /**
     * Construtor de Cópia.
     */
    public Arqueiro(Arqueiro outroArqueiro) {
        super(outroArqueiro);
    }

    /**
     * Ataque básico do Arqueiro.
     * PADRÃO DE EXCELÊNCIA:
     * 1. Usa a classe 'Dado' (D6, conforme definido no seu Main.java).
     * 2. Usa 'getters' para atributos encapsulados (getAtaque, getDefesa, getNome).
     */
    @Override
    public int atacar(Personagem alvo) {
        // Usa D6, conforme a descrição da classe no seu Main.java ("Dano: D6 (Versátil)")
        int dado = Dado.rolarD6();

        // Usa getters para acessar atributos (padrão de encapsulamento)
        int danoBruto = this.getAtaque() + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        // Usa getters para os nomes e adiciona emoji para consistência
        System.out.println("🏹 " + this.getNome() + " dispara uma flecha em " +
                alvo.getNome() + " causando " + danoReal + " de dano (D6).");

        return danoReal;
    }

    /**
     * MÉTODO ADICIONAL: Ataque especial "Tiro Certeiro".
     * Simula um tiro de alta precisão que causa mais dano (2D6).
     */
    public int tiroCerteiro(Personagem alvo) {
        System.out.println("🎯 " + this.getNome() + " se concentra e usa Tiro Certeiro!");

        int dadoDano = Dado.rolarD6() + Dado.rolarD6(); // 2D6
        int danoBase = this.getAtaque() + dadoDano;
        int danoReal = Math.max(3, danoBase - alvo.getDefesa());

        alvo.receberDano(danoReal);
        System.out.println("💥 A flecha perfurante causa " + danoReal + " de dano em " + alvo.getNome() + "!");

        return danoReal;
    }

    /**
     * MÉTODO ADICIONAL: Habilidade de Foco.
     * Aumenta o ataque permanentemente.
     */
    public void focoAprimorado() {
        int bonusAtaque = Dado.rolarD4(); // Aumenta o ataque em 1-4
        this.aumentarAtaque(bonusAtaque); // Usa o método seguro da superclasse

        System.out.println("👁️ " + this.getNome() + " usa Foco Aprimorado, aumentando seu ataque em +" + bonusAtaque + "!");
        System.out.println("⚔️ Ataque atual: " + this.getAtaque());
    }

    /**
     * MÉTODO ADICIONAL: Sobrescreve toString para incluir habilidades.
     * Segue o padrão da classe Mago e Guerreiro.
     */
    @Override
    public String toString() {
        return super.toString() + "\n🎯 Habilidades: Ataque (D6), Tiro Certeiro (2D6)";
    }
}