public class Arqueiro extends Personagem {
    public Arqueiro(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel, new Inventario());
    }
    public Arqueiro(Arqueiro outroArqueiro) { super(outroArqueiro); } // Construtor de Cópia

    @Override
    public int atacar(Personagem alvo) {

        int dado = (int) (Math.random() * 4) + 1; // Rola D4

        int danoBruto = this.ataque + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        System.out.println(this.nome + " (Arqueiro) disparou flecha em " + alvo.getNome() + " causando " + danoReal + " de dano (D4).");
        return danoReal;
    }
}