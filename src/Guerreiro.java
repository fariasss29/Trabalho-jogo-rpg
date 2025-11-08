import java.lang.Math;

public class Guerreiro extends Personagem {

    public Guerreiro(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel, new Inventario());
    }
    public Guerreiro(Guerreiro outroGuerreiro) {
        super(outroGuerreiro);

    }

    @Override
    public int atacar(Personagem alvo){

        int dado = (int) (Math.random()*6) + 1;

        int danoBruto = this.ataque + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        System.out.println(this.nome + "(Guerreiro) rolou D" + dado + " e causou " + danoReal + " de dano em " + alvo.nome + ".");

        return danoReal;
    }
}
