public class Mago extends Personagem {
    public Mago(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel, new Inventario());
    }
    public Mago(Mago outroMago) { super(outroMago); } // Construtor de Cópia

    @Override
    public int atacar(Personagem alvo) {

        int dado = (int) (Math.random() * 10) + 1;// Rola D10

        int danoBruto = this.ataque + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);
        System.out.println(this.nome + " (Mago) lançou magia em " + alvo.getNome() + " causando " + danoReal + " de dano (D10).");

        return danoReal;
    }
}
