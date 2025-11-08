import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Inventario implements Cloneable {

    private List<Item> itens;

    // --- 1. Construtores ---
    public Inventario() {
        this.itens = new ArrayList<>();
    }

    public Inventario(Inventario outroInventario) {
        this();
        if (outroInventario != null) {
            for (Item item : outroInventario.itens) {
                this.itens.add(item.copiar()); // Usa o método copiar() da classe Item
            }
        }
    }

    // --- 2. Getters com encapsulamento melhorado ---
    public List<Item> getItens() {
        return new ArrayList<>(this.itens); // Retorna cópia para evitar modificação externa
    }

    public int getTamanho() {
        return this.itens.size();
    }

    public boolean estaVazio() {
        return this.itens.isEmpty();
    }

    // --- 3. Métodos de Manipulação de Item Aprimorados ---

    /**
     * Adiciona um item ao inventário. Se já existir (pelo nome), soma a quantidade.
     */
    public void adicionarItem(Item novoItem) {
        if (novoItem == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }

        if (!novoItem.estaDisponivel()) {
            System.out.println("⚠️  Item com quantidade zero não será adicionado: " + novoItem.getNome());
            return;
        }

        Optional<Item> itemExistente = buscarItemPorNome(novoItem.getNome());

        if (itemExistente.isPresent()) {
            // Item já existe: soma a quantidade
            itemExistente.get().adicionarQuantidade(novoItem.getQuantidade());
            System.out.println("📥 " + novoItem.getNome() + " adicionado. Quantidade: " + itemExistente.get().getQuantidade());
        } else {
            // Item novo: adiciona uma cópia à lista
            this.itens.add(novoItem.copiar());
            System.out.println("🆕 " + novoItem.getNome() + " adicionado ao inventário.");
        }
    }

    /**
     * Remove ou diminui a quantidade de um item.
     * Retorna true se a remoção ou decremento foi bem-sucedido.
     */
    public boolean removerItem(Item itemParaRemover) {
        if (itemParaRemover == null) {
            return false;
        }

        Optional<Item> itemExistente = buscarItemPorNome(itemParaRemover.getNome());

        if (!itemExistente.isPresent()) {
            return false; // Item não encontrado
        }

        Item item = itemExistente.get();
        int quantidadeParaRemover = itemParaRemover.getQuantidade();

        // Usa o método removerQuantidade da classe Item
        if (item.removerQuantidade(quantidadeParaRemover)) {
            // Se a quantidade chegou a zero, remove o item da lista
            if (!item.estaDisponivel()) {
                itens.remove(item);
            }
            System.out.println("📤 " + quantidadeParaRemover + " unidade(s) de " + item.getNome() + " removida(s).");
            return true;
        }

        return false; // Quantidade insuficiente
    }

    /**
     * Remove uma unidade do item pelo nome.
     */
    public boolean removerUmaUnidade(String nomeItem) {
        Optional<Item> item = buscarItemPorNome(nomeItem);
        if (item.isPresent()) {
            return removerItem(new Item(nomeItem, "", "", 1));
        }
        return false;
    }

    // --- 4. Métodos de Busca ---

    /**
     * Busca um item pelo nome (case insensitive).
     */
    public Optional<Item> buscarItemPorNome(String nome) {
        return itens.stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    /**
     * Verifica se o inventário contém um item pelo nome.
     */
    public boolean contemItem(String nome) {
        return buscarItemPorNome(nome).isPresent();
    }

    /**
     * Verifica se o inventário contém um item pelo nome e quantidade mínima.
     */
    public boolean contemItem(String nome, int quantidadeMinima) {
        Optional<Item> item = buscarItemPorNome(nome);
        return item.isPresent() && item.get().getQuantidade() >= quantidadeMinima;
    }

    /**
     * Obtém a quantidade total de um item no inventário.
     */
    public int getQuantidadeItem(String nome) {
        return buscarItemPorNome(nome)
                .map(Item::getQuantidade)
                .orElse(0);
    }

    // --- 5. Listagem e Relatórios ---

    /**
     * Lista todos os itens ordenados pelo nome.
     */
    public String listarItens() {
        if (itens.isEmpty()) {
            return "📭 Inventário vazio.";
        }

        Collections.sort(itens);

        StringBuilder lista = new StringBuilder();
        lista.append("🎒 INVENTÁRIO (").append(getTamanho()).append(" tipos de itens):\n");
        lista.append("═".repeat(40)).append("\n");

        int totalItens = 0;
        for (Item item : itens) {
            lista.append("• ").append(item).append("\n");
            totalItens += item.getQuantidade();
        }

        lista.append("═".repeat(40)).append("\n");
        lista.append("📊 Total de itens: ").append(totalItens).append("\n");

        return lista.toString();
    }

    /**
     * Lista apenas itens de um determinado tipo (ex: "CURA", "ATK_UP").
     */
    public String listarItensPorTipo(String tipoEfeito) {
        List<Item> itensFiltrados = itens.stream()
                .filter(item -> tipoEfeito.equalsIgnoreCase(item.getTipoEfeito()))
                .toList();

        if (itensFiltrados.isEmpty()) {
            return "❌ Nenhum item do tipo '" + tipoEfeito + "' encontrado.";
        }

        StringBuilder lista = new StringBuilder();
        lista.append("🎒 Itens do tipo ").append(tipoEfeito).append(":\n");
        for (Item item : itensFiltrados) {
            lista.append("• ").append(item).append("\n");
        }
        return lista.toString();
    }

    /**
     * Retorna um relatório resumido do inventário.
     */
    public String getRelatorio() {
        int totalTipos = itens.size();
        int totalUnidades = itens.stream().mapToInt(Item::getQuantidade).sum();

        long pocaoCura = itens.stream().filter(item -> "CURA".equals(item.getTipoEfeito())).count();
        long buffAtaque = itens.stream().filter(item -> "ATK_UP".equals(item.getTipoEfeito())).count();
        long buffDefesa = itens.stream().filter(item -> "DEF_UP".equals(item.getTipoEfeito())).count();

        return String.format(
                "📊 RELATÓRIO DO INVENTÁRIO:\n" +
                        "├─ Tipos de itens: %d\n" +
                        "├─ Unidades totais: %d\n" +
                        "├─ Poções de cura: %d\n" +
                        "├─ Itens de ataque: %d\n" +
                        "└─ Itens de defesa: %d",
                totalTipos, totalUnidades, pocaoCura, buffAtaque, buffDefesa
        );
    }

    // --- 6. Métodos de Limpeza e Utilidade ---

    /**
     * Remove todos os itens com quantidade zero.
     */
    public void limparItensVazios() {
        itens.removeIf(item -> !item.estaDisponivel());
    }

    /**
     * Remove todos os itens do inventário.
     */
    public void limparInventario() {
        itens.clear();
        System.out.println("🗑️  Inventário limpo.");
    }

    /**
     * Transfere todos os itens para outro inventário.
     */
    public void transferirPara(Inventario outroInventario) {
        if (outroInventario == null) {
            throw new IllegalArgumentException("Inventário de destino não pode ser nulo");
        }

        for (Item item : this.itens) {
            outroInventario.adicionarItem(item.copiar());
        }
        this.limparInventario();
    }

    // --- 7. Método clone() Aprimorado ---
    @Override
    public Inventario clone() {
        try {
            Inventario clone = (Inventario) super.clone();
            clone.itens = new ArrayList<>();

            for (Item item : this.itens) {
                clone.itens.add(item.copiar()); // Usa o método copiar da classe Item
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("❌ Falha na clonagem do Inventário.", e);
        }
    }

    // --- 8. Métodos equals e hashCode ---
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Inventario that = (Inventario) obj;
        return this.itens.equals(that.itens);
    }

    @Override
    public int hashCode() {
        return itens.hashCode();
    }

    @Override
    public String toString() {
        return listarItens();
    }
}