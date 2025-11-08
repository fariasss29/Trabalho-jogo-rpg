import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class Batalha {

    /**
     * Ponto de entrada principal para a batalha.
     * Modificado para receber o Scanner e o tipo específico 'Inimigo'.
     * Retorna 'true' se o herói venceu, 'false' se fugiu ou foi derrotado.
     */
    public static boolean batalhar(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("⚔️ BATALHA INICIADA ⚔️");
        System.out.println("=".repeat(40));
        System.out.println(heroi.toString()); // Mostra status do herói
        System.out.println("\n" + " ".repeat(18) + "VS\n");
        System.out.println(inimigo.toString()); // Mostra status do inimigo
        System.out.println("=".repeat(40) + "\n");

        int turno = 1;

        while (heroi.estaVivo() && inimigo.estaVivo()) {
            System.out.println("--- TURNO " + turno + " ---");

            // 1. Turno do Herói
            boolean heroiFugiu = !turnoHeroi(heroi, inimigo, scanner);
            if (heroiFugiu) {
                System.out.println("🏃 " + heroi.getNome() + " fugiu da batalha!");
                return false; // Batalha encerrada (fuga)
            }

            // 2. Verifica se o inimigo morreu
            if (inimigo.estaMorto()) {
                System.out.println("\n🏆 VITÓRIA! " + inimigo.getNome() + " foi derrotado!");
                // ATUALIZAÇÃO: Passa o scanner para o método de recompensa
                concederRecompensa(heroi, inimigo, scanner);
                return true; // Batalha encerrada (vitória)
            }

            // 3. Turno do Inimigo
            System.out.println("\n--- Vez do Inimigo ---");
            turnoInimigo(inimigo, heroi);

            // 4. Verifica se o herói morreu
            if (heroi.estaMorto()) {
                System.out.println("\n💀 DERROTA! " + heroi.getNome() + " foi vencido em combate...");
                return false; // Batalha encerrada (derrota)
            }

            // Pausa para leitura
            System.out.println("\n(Pressione Enter para o próximo turno...)");
            scanner.nextLine();

            System.out.println("\n" + "-".repeat(40));
            System.out.println("📊 STATUS PÓS-TURNO:");
            System.out.println(String.format("❤️ %s: %d/%d HP", heroi.getNome(), heroi.getPontosVida(), heroi.getVidaMaxima()));
            System.out.println(String.format("❤️ %s: %d/%d HP", inimigo.getNome(), inimigo.getPontosVida(), inimigo.getVidaMaxima()));
            System.out.println("-".repeat(40) + "\n");

            turno++;
        }

        return false; // Caso algo inesperado ocorra
    }

    /**
     * Gerencia as ações do herói.
     * Retorna 'true' se o herói agiu, 'false' se ele fugiu.
     */
    private static boolean turnoHeroi(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        while (true) {
            System.out.println("--- Vez de " + heroi.getNome() + " ---");
            System.out.println("1. ⚔️ Atacar (Básico)");
            System.out.println("2. ✨ Habilidade Especial");
            System.out.println("3. 🎒 Usar Item");
            System.out.println("4. 🏃 Fugir");
            System.out.print("🎯 Escolha sua ação: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    heroi.atacar(inimigo); // Usa o ataque básico polimórfico
                    return true;

                case "2":
                    // Lógica de habilidades especiais (Padrão de Excelência)
                    if (usarHabilidadeEspecial(heroi, inimigo, scanner)) {
                        return true; // Turno foi usado
                    }
                    // Se o jogador voltou do menu de habilidades, o loop continua
                    break;

                case "3":
                    // Lógica de item (Padrão de Excelência)
                    if (usarItemBatalha(heroi, scanner)) {
                        return true; // Turno foi usado
                    }
                    // Se o jogador não usou item, o loop continua
                    break;

                case "4":
                    return false; // Sinaliza fuga

                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Gerencia o turno do Inimigo, usando a IA da classe Inimigo.
     */
    private static void turnoInimigo(Inimigo inimigo, Personagem heroi) {
        // 1. Tenta usar item (IA da classe Inimigo)
        if (inimigo.usarItemSePrecisar()) {
            return; // Inimigo usou o turno para se curar
        }

        // 2. Se não se curou, ataca (IA da classe Inimigo)
        inimigo.atacarDecidido(heroi);
    }

    // ##################################################################
    // ### MUDANÇA PRINCIPAL (1/2): MÉTODO DE RECOMPENSA ATUALIZADO ###
    // ##################################################################

    /**
     * Concede recompensas ao herói baseado no inimigo derrotado.
     * ATUALIZAÇÃO: Agora concede PONTOS DE ATRIBUTO em vez de stats fixos.
     */
    private static void concederRecompensa(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🎉 RECOMPENSAS DA BATALHA 🎉");
        System.out.println("=".repeat(40));

        // 1. Ganho de Experiência (conforme definido na classe Inimigo)
        int expGanha = inimigo.getExperienciaFornecida();
        System.out.println(String.format("⭐ Você ganhou %d pontos de experiência!", expGanha));

        // 2. Level Up
        heroi.aumentarNivel(1);
        System.out.println("🌟 LEVEL UP! " + heroi.getNome() + " alcançou o Nível " + heroi.getNivel() + "!");

        // 3. Cura Pós-Batalha
        heroi.curar(heroi.getVidaMaxima()); // Cura completa após a batalha
        System.out.println("✨ Sua vida foi totalmente restaurada!");

        // 4. Loot Drop (Padrão de Excelência: pega itens do inventário do inimigo)
        Inventario loot = inimigo.getInventario();
        if (!loot.estaVazio()) {
            System.out.println("\n🎁 Itens largados pelo inimigo:");
            for (Item item : loot.getItens()) {
                System.out.println("  • " + item.getNome() + " (x" + item.getQuantidade() + ")");
                heroi.getInventario().adicionarItem(item); // Adiciona ao inventário do herói
            }
        }

        // 5. NOVA LÓGICA: Conceder Pontos de Atributo
        // Em vez de dar stats fixos, chamamos o novo método de distribuição.
        int pontosGanhos = 5; // Como você sugeriu!
        System.out.println("\n✨ Você ganhou " + pontosGanhos + " Pontos de Atributo para distribuir!");

        // Pausa para o jogador ler
        System.out.println("\n(Pressione Enter para distribuir seus pontos...)");
        scanner.nextLine();

        distribuirPontosDeAtributo(heroi, pontosGanhos, scanner);
    }

    // ##################################################################
    // ### MUDANÇA PRINCIPAL (2/2): NOVO MÉTODO DE DISTRIBUIÇÃO ###
    // ##################################################################

    /**
     * Novo método para permitir ao jogador distribuir pontos de atributo.
     */
    private static void distribuirPontosDeAtributo(Personagem heroi, int pontos, Scanner scanner) {
        int pontosRestantes = pontos;

        while (pontosRestantes > 0) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("✨ Você tem " + pontosRestantes + " PONTO(S) DE ATRIBUTO para distribuir!");
            System.out.println("=".repeat(40));
            System.out.println("📊 STATUS ATUAIS:");
            System.out.println(String.format("  ❤️ Vida Máxima: %d", heroi.getVidaMaxima()));
            System.out.println(String.format("  ⚔️ Ataque: %d", heroi.getAtaque()));
            System.out.println(String.format("  🛡️ Defesa: %d", heroi.getDefesa()));
            System.out.println("-".repeat(40));
            System.out.println("Onde você quer gastar 1 ponto?");
            System.out.println("1. +5 Vida Máxima");
            System.out.println("2. +1 Ataque");
            System.out.println("3. +1 Defesa");
            System.out.print("🎯 Escolha (1-3): ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    heroi.setVidaMaxima(heroi.getVidaMaxima() + 5);
                    System.out.println("\n❤️ Vida Máxima aumentada para " + heroi.getVidaMaxima() + "!");
                    pontosRestantes--;
                    break;
                case "2":
                    heroi.aumentarAtaque(1);
                    System.out.println("\n⚔️ Ataque aumentado para " + heroi.getAtaque() + "!");
                    pontosRestantes--;
                    break;
                case "3":
                    heroi.aumentarDefesa(1);
                    System.out.println("\n🛡️ Defesa aumentada para " + heroi.getDefesa() + "!");
                    pontosRestantes--;
                    break;
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }

            if (pontosRestantes > 0) {
                System.out.println("(Pressione Enter para continuar...)");
                scanner.nextLine();
            }
        }

        System.out.println("\n✅ Todos os pontos foram distribuídos!");
        System.out.println("📊 STATUS FINAIS APÓS DISTRIBUIÇÃO:");
        // Mostra o status final completo
        System.out.println(heroi.toString());
    }


    /**
     * Menu de habilidades especiais.
     * Retorna 'true' se uma habilidade foi usada, 'false' se o jogador voltou.
     */
    private static boolean usarHabilidadeEspecial(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        // Verifica o tipo de herói e mostra o menu apropriado
        if (heroi instanceof Guerreiro) {
            System.out.println("Habilidades de Guerreiro:");
            System.out.println("1. Ataque Poderoso (2D6)");
            System.out.println("2. Fortalecer Defesa (+D4 DEF)");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            Guerreiro g = (Guerreiro) heroi; // Cast para Guerreiro
            if (escolha.equals("1")) {
                g.ataquePoderoso(inimigo);
                return true;
            } else if (escolha.equals("2")) {
                g.fortalecerDefesa();
                return true;
            }
            return false; // Voltou

        } else if (heroi instanceof Mago) {
            System.out.println("Magias de Mago:");
            System.out.println("1. Bola de Fogo (2D6)");
            System.out.println("2. Meditar (+D4 HP)");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            Mago m = (Mago) heroi; // Cast para Mago
            if (escolha.equals("1")) {
                m.atacarComBolaDeFogo(inimigo);
                return true;
            } else if (escolha.equals("2")) {
                m.meditar();
                return true;
            }
            return false; // Voltou

        } else if (heroi instanceof Arqueiro) {
            // ATUALIZADO: Habilidades do Arqueiro agora funcionam
            System.out.println("Habilidades de Arqueiro:");
            System.out.println("1. Tiro Certeiro (2D6)");
            System.out.println("2. Foco Aprimorado (+D4 ATK)");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String escolha = scanner.nextLine();

            Arqueiro a = (Arqueiro) heroi; // Cast para Arqueiro
            if (escolha.equals("1")) {
                a.tiroCerteiro(inimigo);
                return true;
            } else if (escolha.equals("2")) {
                a.focoAprimorado();
                return true;
            }
            return false; // Voltou
        }

        System.out.println("❌ Seu personagem não possui habilidades especiais.");
        return false;
    }

    /**
     * Gerencia o uso de itens durante a batalha.
     * Retorna 'true' se um item foi usado, 'false' se o jogador voltou.
     */
    private static boolean usarItemBatalha(Personagem heroi, Scanner scanner) {
        System.out.println("\n🎒 INVENTÁRIO DE BATALHA:");
        System.out.println(heroi.getInventario().listarItens());

        if (heroi.getInventario().estaVazio()) {
            System.out.println("📭 O inventário está vazio.");
            return false;
        }

        System.out.print("💬 Digite o nome do item para usar (ou 'voltar'): ");
        String nomeItem = scanner.nextLine();

        if (nomeItem.equalsIgnoreCase("voltar")) {
            return false;
        }

        // Padrão de Excelência: Usar Optional
        Optional<Item> itemOptional = heroi.getInventario().buscarItemPorNome(nomeItem);

        if (itemOptional.isEmpty()) {
            System.out.println("❌ Item não encontrado no inventário.");
            return false;
        }

        Item item = itemOptional.get();

        // Tenta remover 1 unidade do item
        if (heroi.getInventario().removerUmaUnidade(item.getNome())) {
            // Aplica o efeito (lógica similar à da classe Main)
            aplicarEfeitoItem(heroi, item);
            return true; // Item foi usado, turno encerrado
        } else {
            System.out.println("❌ Erro ao usar o item (possivelmente bug?).");
            return false;
        }
    }

    /**
     * Aplica o efeito de um item no herói (método auxiliar).
     * Padrão de Excelência: Usa getTipoEfeito() e getValorEfeito() da classe Item.
     */
    private static void aplicarEfeitoItem(Personagem heroi, Item item) {
        String tipoEfeito = item.getTipoEfeito();
        int valorEfeito = item.getValorEfeito();

        System.out.println("✨ " + heroi.getNome() + " usa " + item.getNome() + "!");

        switch (tipoEfeito) {
            case "CURA":
                heroi.curar(valorEfeito);
                break;
            case "ATK_UP":
                heroi.aumentarAtaque(valorEfeito);
                System.out.println("🔥 Ataque aumentado em +" + valorEfeito + "!");
                break;
            case "DEF_UP":
                heroi.aumentarDefesa(valorEfeito);
                System.out.println("🛡️ Defesa aumentada em +" + valorEfeito + "!");
                break;
            default:
                System.out.println("❌ Efeito do item não reconhecido: " + tipoEfeito);
        }
    }
}