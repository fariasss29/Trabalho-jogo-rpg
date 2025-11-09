import java.util.Objects;

public class Item implements Comparable<Item> {
    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;

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