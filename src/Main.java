import java.util.Scanner;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("---------------------------------");
        System.out.println("  BEM-VINDO AO RPG DE TEXTO EM JAVA");
        System.out.println("---------------------------------");

        // Nome, Vida Maxíma, ATK, DEF, Nível
        Guerreiro heroi = new Guerreiro("Sir Arthur", 60, 15, 5, 1);

        Item pocao = new Item("Poção de Cura", "Restaura 20 HP", "CURA:20", 3);
        heroi.getInventario().adicionarItem(pocao);

        // Nome, HP, ATK, DEF, Nível
        Inimigo goblin = new Inimigo("Goblin Ranzinza", 30, 8, 3, 1);

        boolean jogando = true;
        while (jogando) {

            System.out.println("\n---------------------------------");
            System.out.println(" STATUS ATUAL: " + heroi);
            System.out.println("---------------------------------");
            System.out.println("O que deseja fazer?");
            System.out.println("1. Explorar (Buscar Inimigo)");
            System.out.println("2. Inventário (Usar Item)");
            System.out.println("3. Fugir (Encerrar Jogo)"); // Por simplicidade, vamos usar Fugir para Sair
            System.out.print("Escolha: ");

            String escolha = scanner.nextLine();


            switch (escolha) {
                case "1":

                    Inimigo inimigoAtual = new Inimigo(goblin); // Clonando para criar um novo inimigo
                    Batalha.batalhar(heroi, inimigoAtual);

                    // Verifica se o herói sobreviveu para continuar explorando
                    if (!heroi.estaVivo()) {
                        System.out.println("FIM DE JOGO! Seu herói não resistiu...");
                        jogando = false;
                    }
                    break;
                case "2":
                    usarItem(heroi, scanner);
                    break;
                case "3":
                    System.out.println("O herói foge e encerra a jornada.");
                    jogando = false;
                    break;
                default:
                    System.out.println("Comando inválido. Tente novamente.");
            }
        }
        scanner.close();
    }


    public static void explorar(Personagem heroi, Inimigo goblinModelo) {
        // Simulação de decisão na história
        if (Math.random() < 0.3) {
            System.out.println("Você encontra um desvio, mas decide ignorá-lo.");
            return;
        }

        // Simulação de Encontro com Inimigo (Clonagem é usada aqui)
        Inimigo inimigoAtual = new Inimigo(goblinModelo);
        inimigoAtual.setPontosVida(inimigoAtual.getPontosVida() + (int)(Math.random() * 10)); // Varia um pouco o HP

        System.out.println("\n>> Você encontrou um(a) " + inimigoAtual.getNome() + "!");
        Batalha.batalhar(heroi, inimigoAtual);

        // Verifica se o herói morreu
        if (!heroi.estaVivo()) {
            System.out.println("GAME OVER! Seu herói foi derrotado em combate.");
        } else {
            // Lógica de Recompensa (Saque simulado, se fosse implementado)
            System.out.println("Você venceu! Seu HP restante: " + heroi.getPontosVida());
        }
    }

    public static void usarItem(Personagem heroi, Scanner scanner) {
        System.out.println("\n" + heroi.getInventario().listarItens());

        if (heroi.getInventario().getItens().isEmpty()) return;

        System.out.print("Digite o nome do item para usar (ou 'voltar'): ");
        String nomeItem = scanner.nextLine();

        if (nomeItem.equalsIgnoreCase("voltar")) return;

        // Criamos um item temporário para usar o equals() e remover o item correto
        Item itemUsar = new Item(nomeItem, "", "", 1);

        if (heroi.getInventario().removerItem(itemUsar)) {
            // Aplica o efeito (Lógica baseada no nome do item)
            if (nomeItem.equalsIgnoreCase("Poção de Cura")) {
                int cura = 50;
                heroi.curar(cura); // Usando o método curar que adicionamos em Personagem
                System.out.println(heroi.getNome() + " usou Poção de Cura! HP restaurado em " + cura + ".");
            } else {
                System.out.println("Item [" + nomeItem + "] usado com sucesso, mas sem efeito de jogo implementado.");
            }
        } else {
            System.out.println("Item não encontrado ou quantidade insuficiente (certifique-se de digitar o nome completo).");
        }
    }
}