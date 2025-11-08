import java.lang.Math;


public class Inimigo extends Personagem {

    public Inimigo(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {

        super(nome, vidaMaxima, ataque, defesa, nivel, new Inventario());
    }
    public Inimigo(Inimigo outroInimigo) {
        super(outroInimigo);
    }

    @Override
    public int atacar(Personagem alvo){

        int dado = (int) (Math.random()*8) + 1;

        int danoBruto = this.ataque + dado - alvo.defesa;

        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        System.out.println(this.nome + "(Inimigo) rolou D" + dado + " e causou " + danoReal + " de dano em " + alvo.nome + ".");

        return danoReal;
    }
}
