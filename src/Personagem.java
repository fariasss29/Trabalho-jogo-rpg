public abstract class Personagem {
    protected String nome;
    protected int pontosVida;
    protected int vidaMaxima;
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected Inventario inventario;

    public Personagem(String nome, int vidaMaxima, int ataque, int defesa, int nivel, Inventario inventario) {
        this.nome = nome;
        this.pontosVida = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.inventario = inventario;
    }

    public String getNome() {
        return nome;
    }

    public int getPontosVida() {
        return this.pontosVida;
    }
    public int getVidaMaxima() {return this.vidaMaxima;}
    public int getAtaque(){
        return this.ataque;
    }
    public int getDefesa(){
        return this.defesa;
    }
    public int getNivel(){
        return this.nivel;
    }
    public Inventario getInventario(){
        return this.inventario;
    }

    public Personagem(Personagem outro) {
        this.nome = outro.nome;
        this.pontosVida = outro.pontosVida;
        this.vidaMaxima = outro.vidaMaxima;
        this.ataque = outro.ataque;
        this.defesa = outro.defesa;
        this.nivel = outro.nivel;

        this.inventario = new Inventario(outro.inventario);
    }

    public void setPontosVida(int novoHP) {
        this.pontosVida = novoHP;
    }

    public void receberDano(int dano) {

        if (dano > 0) {
            this.pontosVida -= dano;

            // Garante que a vida não fique negativa
            if (this.pontosVida < 0) {
                this.pontosVida = 0;
            }
        }

    }

    public boolean estaVivo() {
        // Retorna 'true' se a vida for maior que zero.
        return this.pontosVida > 0;
    }

    public void aumentarNivel(int niveisGanhos) {
        this.nivel += niveisGanhos;
    }


    public void aumentarAtaque(int valor) {
        this.ataque += valor;
    }


    public void aumentarDefesa(int valor) {
        this.defesa += valor;
    }


    public void curar(int valor) {
        this.pontosVida += valor;

        if (this.pontosVida > this.vidaMaxima) {
            this.pontosVida = this.vidaMaxima;
        }

    }

    @Override
    public String toString(){
        return "["+ this.nome + "] \n" +
                "| Nível: " + this.nivel + "\n"+
                "| HP Max: " + this.vidaMaxima + "/ HP Atual: " + this.pontosVida +"\n" +
                "| ATK: " + this.ataque + "\n" +
                "| DEF: " + this.defesa;
    }
    public abstract int atacar(Personagem alvo);


}