public abstract class Personagem {
    private String nome;
    private int pontosVida;
    private int vidaMaxima;
    private int ataque;
    private int defesa;
    private int nivel;
    private Inventario inventario;

    public Personagem(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        this(nome, vidaMaxima, ataque, defesa, nivel, new Inventario());
    }

    public Personagem(String nome, int vidaMaxima, int ataque, int defesa, int nivel, Inventario inventario) {
        setNome(nome);
        setVidaMaxima(vidaMaxima);
        this.pontosVida = vidaMaxima;
        setAtaque(ataque);
        setDefesa(defesa);
        setNivel(nivel);
        this.inventario = (inventario != null) ? inventario : new Inventario();
    }

    public Personagem(Personagem outro) {
        this(outro.nome, outro.vidaMaxima, outro.ataque, outro.defesa, outro.nivel,
                (outro.inventario != null) ? new Inventario(outro.inventario) : new Inventario());
        this.pontosVida = outro.pontosVida;
    }

    // GETTERS
    public String getNome() { return nome; }
    public int getPontosVida() { return this.pontosVida; }
    public int getVidaMaxima() { return this.vidaMaxima; }
    public int getAtaque() { return this.ataque; }
    public int getDefesa() { return this.defesa; }
    public int getNivel() { return this.nivel; }
    public Inventario getInventario() { return this.inventario; }

    // SETTERS COM VALIDAÇÃO
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            this.nome = "Herói Sem Nome";
        } else {
            this.nome = nome.trim();
        }
    }

    public void setPontosVida(int pontosVida) {
        this.pontosVida = Math.max(0, Math.min(pontosVida, this.vidaMaxima));
    }

    public void setVidaMaxima(int vidaMaxima) {
        if (vidaMaxima <= 0) {
            throw new IllegalArgumentException("Vida máxima deve ser positiva");
        }
        this.vidaMaxima = vidaMaxima;
        if (this.pontosVida > vidaMaxima) {
            this.pontosVida = vidaMaxima;
        }
    }

    public void setAtaque(int ataque) {
        this.ataque = Math.max(0, ataque);
    }

    public void setDefesa(int defesa) {
        this.defesa = Math.max(0, defesa);
    }

    public void setNivel(int nivel) {
        if (nivel <= 0) {
            throw new IllegalArgumentException("Nível deve ser positivo");
        }
        this.nivel = nivel;
    }

    // MÉTODOS DE AÇÃO
    public void receberDano(int dano) {
        if (dano < 0) {
            throw new IllegalArgumentException("Dano não pode ser negativo");
        }

        this.pontosVida -= dano;
        if (this.pontosVida < 0) {
            this.pontosVida = 0;
        }

        System.out.println("💥 " + this.nome + " recebeu " + dano + " de dano! " +
                this.pontosVida + "/" + this.vidaMaxima + " HP");
    }

    public boolean estaVivo() {
        return this.pontosVida > 0;
    }

    public boolean estaMorto() {
        return !estaVivo();
    }

    public void aumentarNivel(int niveisGanhos) {
        if (niveisGanhos <= 0) {
            throw new IllegalArgumentException("Níveis ganhos devem ser positivos");
        }
        this.nivel += niveisGanhos;
    }

    public void aumentarAtaque(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor do aumento não pode ser negativo");
        }
        this.ataque += valor;
    }

    public void aumentarDefesa(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor do aumento não pode ser negativo");
        }
        this.defesa += valor;
    }

    public void curar(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor de cura não pode ser negativo");
        }

        int vidaAntes = this.pontosVida;
        this.pontosVida += valor;

        if (this.pontosVida > this.vidaMaxima) {
            this.pontosVida = this.vidaMaxima;
        }

        int vidaCurada = this.pontosVida - vidaAntes;
        if (vidaCurada > 0) {
            System.out.println("✨ " + this.nome + " recuperou " + vidaCurada + " HP! " +
                    this.pontosVida + "/" + this.vidaMaxima + " HP");
        }
    }

    // MÉTODOS UTILITÁRIOS
    public double getPercentualVida() {
        return (double) this.pontosVida / this.vidaMaxima * 100;
    }

    public boolean precisaCurar() {
        return this.pontosVida < this.vidaMaxima * 0.5;
    }

    public boolean estaComVidaBaixa() {
        return this.pontosVida < this.vidaMaxima * 0.25;
    }

    // MÉTODOS ABSTRATOS PARA POLIMORFISMO EXPLÍCITO
    public abstract int atacar(Personagem alvo);
    public abstract int usarHabilidadeEspecial(Personagem alvo);
    public abstract void usarHabilidadeDefensiva();
    public abstract String getDescricaoHabilidades();

    // TO STRING MELHORADO
    @Override
    public String toString() {
        String estadoVida = estaVivo() ?
                String.format("❤️ %d/%d", pontosVida, vidaMaxima) : "💀 MORTO";

        String classe = this.getClass().getSimpleName();
        String emojiClasse = "";

        if (this instanceof Guerreiro) emojiClasse = "⚔️ ";
        else if (this instanceof Arqueiro) emojiClasse = "🏹";
        else if (this instanceof Mago) emojiClasse = "🔮";
        else emojiClasse = "👤";

        return String.format(
                "%s %s %s\n" +
                        "⭐ Nível: %d\n" +
                        "%s HP\n" +
                        "⚔️  Ataque: %d\n" +
                        "🛡️  Defesa: %d\n" +
                        "🎯 %s",
                emojiClasse, classe, nome, nivel, estadoVida, ataque, defesa,
                getDescricaoHabilidades()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Personagem that = (Personagem) obj;
        return nome.equals(that.nome) && nivel == that.nivel;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nome, nivel);
    }
}