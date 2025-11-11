import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Item implements Comparable<Item> {
    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;

    private static final Random RANDOM = new Random();

    public Item(String nome, String descricao, String efeito, int quantidade) {
        setNome(nome);
        setDescricao(descricao);
        setEfeito(efeito);
        setQuantidade(quantidade);
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }


    public static final List<Item> LISTA_ITENS_LIXO = new ArrayList<>();
    public static final List<Item> LISTA_ITENS_COMUNS = new ArrayList<>();
    public static final List<Item> LISTA_ITENS_RAROS = new ArrayList<>();
    public static final List<Item> LISTA_ITENS_LENDARIOS = new ArrayList<>();

    static {
        // --- ITENS LIXO
        LISTA_ITENS_LIXO.add(new Item("Trapo", "Apenas um trapo velho", "LIXO:0", 1));
        LISTA_ITENS_LIXO.add(new Item("Pedra Quebrada", "Inútil e pesado", "LIXO:0", 1));

        // --- ITENS COMUNS / MÉDIOS
        LISTA_ITENS_COMUNS.add(new Item("Poção Fraca", "Restaura 10 HP", "CURA:10", 1));
        LISTA_ITENS_COMUNS.add(new Item("Poção de Cura", "Restaura 30 HP", "CURA:30", 1));
        LISTA_ITENS_COMUNS.add(new Item("Poção de Cura Grande", "Restaura 60 HP", "CURA:60", 1));
        LISTA_ITENS_COMUNS.add(new Item("Escudo Velho", "Aumente DEF em 3", "DEF_UP:3", 1));
        LISTA_ITENS_COMUNS.add(new Item("Elixir de Energia", "Restaura 15 Mana/Fúria", "RECURSO:15", 1));
        LISTA_ITENS_COMUNS.add(new Item("Elixir de Ataque", "Aumente ATK em 5", "ATK_UP:5", 1));
        LISTA_ITENS_COMUNS.add(new Item("Pedra de Afiar", "Aumenta ATK em 3", "ATK_UP:3", 1));

        // --- ITENS RAROS
        LISTA_ITENS_RAROS.add(new Item("Poção Épica", "Restaura 100 HP", "CURA:100", 1));
        LISTA_ITENS_RAROS.add(new Item("Amuleto da Vida", "Aumenta HP Máximo em 25", "HP_UP:25", 1));
        LISTA_ITENS_RAROS.add(new Item("Elixir do Poder", "Aumenta ATK em 8", "ATK_UP:8", 1));
        LISTA_ITENS_RAROS.add(new Item("Armadura Grega Antiga", "Aumenta DEF em 8", "DEF_UP:8", 1));
        LISTA_ITENS_RAROS.add(new Item("Arma Universal", "Aumenta ATK em 15", "ATK_UP:15", 1));

        // --- ITENS LENDARIOS
        LISTA_ITENS_LENDARIOS.add(new Item("Armadura Rúnica", "Aumenta DEF em 10", "DEF_UP:10", 1));
        LISTA_ITENS_LENDARIOS.add(new Item("Arma Celestial", "Aumenta ATK em 20", "ATK_UP:20", 1));

    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do item não pode ser vazio");
        }
        this.nome = nome.trim();
    }

    public void setDescricao(String descricao) {
        this.descricao = (descricao != null) ? descricao : "Sem descrição";
    }

    public void setEfeito(String efeito) {
        this.efeito = (efeito != null) ? efeito : "";
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    public void adicionarQuantidade(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
        this.quantidade += valor;
    }

    public boolean removerQuantidade(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
        if (this.quantidade >= valor) {
            this.quantidade -= valor;
            return true;
        }
        return false;
    }

    public boolean usarUmaUnidade() {
        return removerQuantidade(1);
    }

    public boolean estaDisponivel() {
        return this.quantidade > 0;
    }

    public String getTipoEfeito() {
        if (efeito == null || efeito.isEmpty()) {
            return "";
        }
        String[] partes = efeito.split(":");
        return partes.length > 0 ? partes[0] : "";
    }

    public int getValorEfeito() {
        if (efeito == null || efeito.isEmpty()) {
            return 0;
        }
        String[] partes = efeito.split(":");
        try {
            return partes.length > 1 ? Integer.parseInt(partes[1]) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(nome.toLowerCase(), item.nome.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase());
    }

    @Override
    public int compareTo(Item outroItem) {
        return this.nome.compareToIgnoreCase(outroItem.nome);
    }

    @Override
    public String toString() {
        String emoji = getEmojiPorTipo();
        return String.format("%s %s (x%d) - %s",
                emoji, nome, quantidade, descricao);
    }

    private String getEmojiPorTipo() {
        String tipo = getTipoEfeito().toLowerCase();
        switch (tipo) {
            case "cura":
                return "❤️";
            case "atk_up":
                return "⚔️";
            case "def_up":
                return "🛡️";
            case "level_up":
                return "⭐";
            default:
                return "📦";
        }
    }

    public Item copiar() {
        return new Item(this.nome, this.descricao, this.efeito, this.quantidade);
    }

    public Item comQuantidade(int novaQuantidade) {
        return new Item(this.nome, this.descricao, this.efeito, novaQuantidade);
    }
}