import java.util.Objects;

public class Item implements Comparable<Item> {

    private String nome;
    private String descricao;
    private String efeito; // Ex.: "CURA:20", "ATK_UP:5"
    private int quantidade;

    // Construtor Padrão
    public Item(String nome, String descricao, String efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }

    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }

    // Métodos para Manipular Quantidade
    public void adicionarQuantidade(int valor) { this.quantidade += valor; }

    // --- MÉTODOS OBRIGATÓRIOS (equals, hashCode, compareTo) ---

    // 1. Sobrescreve equals para comparar pelo NOME
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        // Dois itens são iguais se têm o mesmo nome
        return Objects.equals(nome, item.nome);
    }

    // 2. Sobrescreve hashCode (consistente com equals)
    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    // 3. Implementa Comparable para ordenar por NOME
    @Override
    public int compareTo(Item outroItem) {
        return this.nome.compareTo(outroItem.nome);
    }

    // 4. toString para exibição
    @Override
    public String toString() {
        return nome + " (x" + quantidade + ") - " + descricao;
    }
}
