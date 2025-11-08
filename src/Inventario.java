import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario implements Cloneable {

    // Usamos List para armazenar itens (facilita o acesso por índice)
    private List<Item> itens;

    // --- 1. Construtor Padrão ---
    public Inventario() {
        this.itens = new ArrayList<>();
    }

    public List<Item> getItens() {
        return this.itens;
    }

    // --- 2. Construtor de Cópia (para criar um clone do inventário) ---
    public Inventario(Inventario outroInventario) {
        this.itens = new ArrayList<>();
        // Copia CADA item individualmente (Deep Copy)
        for (Item item : outroInventario.itens) {
            // Cria um novo objeto Item a partir dos dados do item original
            this.itens.add(new Item(item.getNome(), item.getDescricao(), item.getEfeito(), item.getQuantidade()));
        }
    }

    // --- 3. Métodos de Manipulação de Item ---

    /**
     * Adiciona um item ao inventário. Se já existir (pelo nome), soma a quantidade.
     */
    public void adicionarItem(Item novoItem) {
        // O indexOf usa o Item.equals()
        int index = itens.indexOf(novoItem);

        if (index != -1) {
            // Item já existe: soma a quantidade
            itens.get(index).adicionarQuantidade(novoItem.getQuantidade());
        } else {
            // Item novo: adiciona à lista
            // É importante adicionar uma CÓPIA do Item se você for manipulá-lo fora daqui!
            itens.add(novoItem);
        }
    }

    /**
     * Remove ou diminui a quantidade de um item.
     * Retorna true se a remoção ou decremento foi bem-sucedido.
     */
    public boolean removerItem(Item itemParaRemover) {
        int index = itens.indexOf(itemParaRemover);
        if (index == -1) {
            return false; // Item não encontrado
        }

        Item itemExistente = itens.get(index);
        int quantidadeParaRemover = itemParaRemover.getQuantidade();

        if (itemExistente.getQuantidade() > quantidadeParaRemover) {
            // Diminui a quantidade
            itemExistente.adicionarQuantidade(-quantidadeParaRemover);
            return true;
        } else if (itemExistente.getQuantidade() == quantidadeParaRemover) {
            // Remove o item
            itens.remove(index);
            return true;
        }

        // Quantidade insuficiente
        return false;
    }

    /**
     * Lista todos os itens ordenados pelo nome (usando Item.compareTo()).
     */
    public String listarItens() {
        if (itens.isEmpty()) {
            return "Inventário vazio.";
        }

        Collections.sort(itens);

        StringBuilder lista = new StringBuilder("Inventário Ordenado:\n");
        for (Item item : itens) {
            lista.append("- ").append(item).append("\n");
        }
        return lista.toString();
    }

    // --- 4. Método clone() (para saque de inimigos) ---
    @Override
    public Inventario clone() {
        try {
            // 1. Cria um clone superficial do objeto Inventario
            Inventario clone = (Inventario) super.clone();

            // 2. Garante a cópia profunda (Deep Copy) da lista de itens
            clone.itens = new ArrayList<>();
            for (Item item : this.itens) {
                // Usa o construtor de cópia do Item para garantir que cada item seja uma nova instância
                clone.itens.add(new Item(item.getNome(), item.getDescricao(), item.getEfeito(), item.getQuantidade()));
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            // Se algo der errado (o que não deve acontecer, pois implementamos Cloneable)
            throw new RuntimeException("Falha na clonagem do Inventário.", e);
        }
    }
}