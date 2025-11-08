import java.lang.Math;

public class Inimigo extends Personagem {

    private String tipo; // Novo atributo para variedade de inimigos
    private int experienciaFornecida; // Experiência que o inimigo fornece ao ser derrotado

    public Inimigo(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
        this.tipo = "Comum";
        this.experienciaFornecida = calcularExperiencia();
        adicionarItensIniciais();
    }

    public Inimigo(String nome, int vidaMaxima, int ataque, int defesa, int nivel, String tipo) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
        this.tipo = tipo;
        this.experienciaFornecida = calcularExperiencia();
        adicionarItensIniciais();
    }

    public Inimigo(Inimigo outroInimigo) {
        super(outroInimigo);
        this.tipo = outroInimigo.tipo;
        this.experienciaFornecida = outroInimigo.experienciaFornecida;
    }

    // Getters
    public String getTipo() { return tipo; }
    public int getExperienciaFornecida() { return experienciaFornecida; }

    @Override
    public int atacar(Personagem alvo) {
        // CORREÇÃO: usar getters em vez de atributos diretos
        int dado = Dado.rolarD8(); // Rola D8

        int danoBruto = this.getAtaque() + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        // CORREÇÃO: usar getters
        System.out.println("👹 " + this.getNome() + " atacou " + alvo.getNome() +
                " causando " + danoReal + " de dano (D8).");

        return danoReal;
    }

    // Método de ataque especial baseado no tipo de inimigo
    public int ataqueEspecial(Personagem alvo) {
        System.out.println("💥 " + this.getNome() + " usa ataque especial!");

        int danoBase;
        String mensagemEspecial = "";

        switch (this.tipo.toLowerCase()) {
            case "bruxa":
                danoBase = this.getAtaque() + Dado.rolarD6() + Dado.rolarD6(); // 2D6
                mensagemEspecial = "Magia Sombria";
                break;
            case "orc":
                danoBase = this.getAtaque() + Dado.rolarD10(); // D10
                mensagemEspecial = "Golpe Brutal";
                break;
            case "lobo":
                danoBase = this.getAtaque() + Dado.rolarD6() + 3; // D6 + 3
                mensagemEspecial = "Mordida Afiada";
                break;
            default:
                danoBase = this.getAtaque() + Dado.rolarD8() + 2; // D8 + 2
                mensagemEspecial = "Ataque Feroz";
        }

        int danoReal = Math.max(2, danoBase - alvo.getDefesa());
        alvo.receberDano(danoReal);

        System.out.println("🔥 " + mensagemEspecial + " causa " + danoReal + " de dano em " + alvo.getNome() + "!");

        return danoReal;
    }

    // Chance de usar ataque especial (30% base)
    public boolean deveUsarAtaqueEspecial() {
        return Math.random() < 0.3;
    }

    // Método unificado de ataque que decide entre normal e especial
    public int atacarDecidido(Personagem alvo) {
        if (deveUsarAtaqueEspecial()) {
            return ataqueEspecial(alvo);
        } else {
            return atacar(alvo);
        }
    }

    // Calcular experiência baseada nos atributos
    private int calcularExperiencia() {
        int base = this.getNivel() * 10;
        int bonus = (this.getAtaque() + this.getDefesa() + this.getVidaMaxima() / 10);
        return base + bonus;
    }

    // Adicionar itens iniciais baseados no tipo de inimigo
    private void adicionarItensIniciais() {
        switch (this.tipo.toLowerCase()) {
            case "bruxa":
                getInventario().adicionarItem(new Item("Poção Misteriosa", "Efeito desconhecido", "ATK_UP:5", 1));
                break;
            case "orc":
                getInventario().adicionarItem(new Item("Pele Resistente", "Pele dura de orc", "DEF_UP:3", 1));
                break;
            case "lobo":
                if (Math.random() < 0.5) {
                    getInventario().adicionarItem(new Item("Dente de Lobo", "Afiado e perigoso", "ATK_UP:2", 1));
                }
                break;
            default:
                // Inimigos comuns têm chance de ter poção de cura
                if (Math.random() < 0.3) {
                    getInventario().adicionarItem(new Item("Poção de Cura", "Restaura 20 HP", "CURA:20", 1));
                }
        }
    }

    // Método para o inimigo usar itens quando está com vida baixa
    public boolean usarItemSePrecisar() {
        if (this.getPercentualVida() < 30 && !getInventario().estaVazio()) {
            // Procura por poções de cura
            var pocaoCura = getInventario().buscarItemPorNome("Poção de Cura");
            if (pocaoCura.isPresent() && pocaoCura.get().estaDisponivel()) {
                System.out.println("💊 " + this.getNome() + " usa uma Poção de Cura!");
                getInventario().removerUmaUnidade("Poção de Cura");
                this.curar(30); // Cura fixa para inimigos
                return true;
            }
        }
        return false;
    }

    // Sobrescrever toString para informações melhores
    @Override
    public String toString() {
        String emoji = "👹";
        if (tipo.equalsIgnoreCase("bruxa")) emoji = "🧙";
        else if (tipo.equalsIgnoreCase("orc")) emoji = "👹";
        else if (tipo.equalsIgnoreCase("lobo")) emoji = "🐺";

        return String.format(
                "%s %s (%s) | Nível %d\n" +
                        "❤️ %d/%d HP | ⚔️ %d ATK | 🛡️ %d DEF\n" +
                        "⭐ Experiência: %d",
                emoji, getNome(), tipo, getNivel(),
                getPontosVida(), getVidaMaxima(), getAtaque(), getDefesa(),
                experienciaFornecida
        );
    }

    // Factory method para criar inimigos comuns
    public static Inimigo criarGoblin() {
        return new Inimigo("Goblin", 30, 8, 3, 1, "goblin");
    }

    public static Inimigo criarOrc() {
        return new Inimigo("Orc Guerreiro", 50, 12, 5, 2, "orc");
    }

    public static Inimigo criarBruxa() {
        return new Inimigo("Bruxa Sombria", 40, 15, 4, 3, "bruxa");
    }

    public static Inimigo criarLobo() {
        return new Inimigo("Lobo Selvagem", 25, 10, 2, 1, "lobo");
    }

    public static Inimigo criarEsqueleto() {
        return new Inimigo("Esqueleto Armado", 35, 11, 4, 2, "esqueleto");
    }

    public static Inimigo criarAranhaGigante() {
        return new Inimigo("Aranha Gigante", 45, 9, 6, 3, "aranha");
    }
}