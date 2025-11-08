import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors; // Importante para filtrar listas

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Inimigo> inimigosDisponiveis = new ArrayList<>();
    private static Random random = new Random();

    // ##################################################################
    // ###           NOVOS MÉTODOS DE PAUSA E RITMO           ###
    // ##################################################################

    /**
     * NOVO MÉTODO: Pausa a execução por um tempo (em milissegundos).
     * Isso cria o efeito "jogando e pausando" que você queria.
     */
    private static void pausar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            // Em caso de interrupção, restaura o status de interrupção
            Thread.currentThread().interrupt();
        }
    }

    /**
     * NOVO MÉTODO: Imprime um texto e, EM SEGUIDA, pausa.
     */
    private static void imprimirComPausa(String texto, int milissegundos) {
        System.out.println(texto);
        pausar(milissegundos);
    }

    /**
     * NOVO MÉTODO: Centraliza a lógica de "Pressione Enter".
     */
    private static void aguardarEnter() {
        System.out.println("\n(Pressione Enter para continuar...)");
        scanner.nextLine();
    }


    // ##################################################################
    // ###                LÓGICA PRINCIPAL DO JOGO              ###
    // ##################################################################

    public static void main(String[] args) {
        inicializarInimigos(); // Carrega o "banco de dados" de inimigos

        System.out.println("=========================================");
        System.out.println("     🏰 RPG DE TEXTO - JORNADA ÉPICA");
        System.out.println("=========================================");

        String nomeHeroi = exibirIntroducao(scanner);
        Personagem heroi = escolherClasse(nomeHeroi, scanner);

        System.out.println("\n🎒 Equipando itens iniciais...");
        pausar(1000); // Pausa
        heroi.getInventario().adicionarItem(new Item("Poção de Cura", "Restaura 30 HP", "CURA:30", 2));
        pausar(500); // Pausa
        heroi.getInventario().adicionarItem(new Item("Elixir de Força", "Aumenta ataque em 5", "ATK_UP:5", 1));
        pausar(500); // Pausa

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Sua jornada começa agora, " + heroi.getNome() + "!");
        System.out.println("=".repeat(50));

        aguardarEnter();

        boolean jogando = true;
        while (jogando && heroi.estaVivo()) {
            exibirMenuPrincipal(heroi);

            String escolha = scanner.nextLine();
            System.out.println();

            switch (escolha) {
                case "1":
                    // ### MUDANÇA: Agora chama o NOVO menu de exploração ###
                    menuExplorar(heroi);
                    break;
                case "2":
                    usarItem(heroi);
                    break;
                case "3":
                    exibirStatusDetalhado(heroi);
                    break;
                case "4":
                    System.out.println("🏃 O herói decide encerrar sua jornada por hoje...");
                    jogando = false;
                    break;
                default:
                    System.out.println("❌ Comando inválido. Tente novamente.");
            }

            // Pausa entre as ações (removido, pois a pausa agora está no fim da exploração)
            // if (jogando && heroi.estaVivo()) {
            //     aguardarEnter();
            // }
        }

        // ... (resto do seu código main, mensagens finais, etc.)
        if (!heroi.estaVivo()) {
            System.out.println("\n💀 FIM DE JOGO! Seu herói não resistiu aos perigos...");
            System.out.println("🏆 Seu herói alcançou o nível " + heroi.getNivel() + "!");
        } else {
            System.out.println("\n✨ Obrigado por jogar! Sua jornada termina aqui...");
        }

        scanner.close();
    }

    /**
     * MÉTODO MODIFICADO: Apenas carrega a lista mestre de inimigos.
     */
    private static void inicializarInimigos() {
        inimigosDisponiveis.add(Inimigo.criarGoblin());     // tipo: "goblin"
        inimigosDisponiveis.add(Inimigo.criarLobo());       // tipo: "lobo"
        inimigosDisponiveis.add(Inimigo.criarOrc());        // tipo: "orc"
        inimigosDisponiveis.add(Inimigo.criarEsqueleto());  // tipo: "esqueleto"
        inimigosDisponiveis.add(Inimigo.criarAranhaGigante()); // tipo: "aranha"
        inimigosDisponiveis.add(Inimigo.criarBruxa());      // tipo: "bruxa"
    }

    /**
     * MÉTODO MODIFICADO: A opção 1 foi atualizada.
     */
    private static void exibirMenuPrincipal(Personagem heroi) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("                 🎮 MENU PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("📊 " + heroi.getNome() + " | Nível " + heroi.getNivel());
        System.out.println("❤️  Vida: " + heroi.getPontosVida() + "/" + heroi.getVidaMaxima());
        System.out.println("⚔️  Ataque: " + heroi.getAtaque() + " | 🛡️ Defesa: " + heroi.getDefesa());
        System.out.println("-".repeat(50));
        System.out.println("1. 🗺️  Explorar Locais"); // <-- MUDOU AQUI
        System.out.println("2. 🎒 Inventário (Usar Itens)");
        System.out.println("3. 📋 Status Detalhado");
        System.out.println("4. 🚪 Sair do Jogo");
        System.out.print("🎯 Escolha uma opção: ");
    }

    // ##################################################################
    // ###      NOVA LÓGICA DE EXPLORAÇÃO (SUA IDEIA 1)       ###
    // ##################################################################

    /**
     * NOVO MÉTODO: Mostra o menu de locais para explorar.
     */
    private static void menuExplorar(Personagem heroi) {
        boolean explorando = true;
        while (explorando && heroi.estaVivo()) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("🗺️ PARA ONDE VOCÊ QUER IR?");
            System.out.println("-".repeat(50));
            System.out.println("1. 🌳 Floresta Sombria (Inimigos: Lobos, Aranhas)");
            System.out.println("2. 🏔️ Montanhas Rochosas (Inimigos: Goblins, Orcs)");
            System.out.println("3. 💀 Cripta Abandonada (Inimigos: Esqueletos, Bruxas)");
            System.out.println("0. 🏘️ Voltar para a área segura (Menu Principal)");
            System.out.print("🎯 Escolha seu destino: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    explorarLocal(heroi, "Floresta Sombria", "floresta");
                    explorando = false; // Sai do menu de exploração após um evento
                    break;
                case "2":
                    explorarLocal(heroi, "Montanhas Rochosas", "montanha");
                    explorando = false;
                    break;
                case "3":
                    explorarLocal(heroi, "Cripta Abandonada", "cripta");
                    explorando = false;
                    break;
                case "0":
                    explorando = false;
                    break;
                default:
                    System.out.println("❌ Localização inválida. Tente novamente.");
            }
        }
    }

    /**
     * MÉTODO MODIFICADO: Filtra inimigos baseado no local.
     */
    private static Inimigo sortearInimigo(String tipoLocal) {
        List<Inimigo> poolDeInimigos;

        // Filtra a lista mestre baseado no tipo de local
        if (tipoLocal.equals("floresta")) {
            poolDeInimigos = inimigosDisponiveis.stream()
                    .filter(i -> i.getTipo().equals("lobo") || i.getTipo().equals("aranha"))
                    .collect(Collectors.toList());
        } else if (tipoLocal.equals("montanha")) {
            poolDeInimigos = inimigosDisponiveis.stream()
                    .filter(i -> i.getTipo().equals("goblin") || i.getTipo().equals("orc"))
                    .collect(Collectors.toList());
        } else if (tipoLocal.equals("cripta")) {
            poolDeInimigos = inimigosDisponiveis.stream()
                    .filter(i -> i.getTipo().equals("esqueleto") || i.getTipo().equals("bruxa"))
                    .collect(Collectors.toList());
        } else {
            // Caso padrão: pega qualquer inimigo
            poolDeInimigos = inimigosDisponiveis;
        }

        // Se o pool filtrado estiver vazio, usa a lista principal para evitar erros
        if (poolDeInimigos.isEmpty()) {
            poolDeInimigos = inimigosDisponiveis;
        }

        // Sorteia da lista filtrada
        int index = random.nextInt(poolDeInimigos.size());
        Inimigo base = poolDeInimigos.get(index);
        Inimigo inimigo = new Inimigo(base); // Usa o construtor de cópia

        // Variação aleatória nos atributos (±20%)
        int variacaoVida = (int)(base.getVidaMaxima() * (random.nextDouble() * 0.4 - 0.2));
        int variacaoAtaque = (int)(base.getAtaque() * (random.nextDouble() * 0.4 - 0.2));

        inimigo.setVidaMaxima(base.getVidaMaxima() + variacaoVida);
        inimigo.setPontosVida(inimigo.getVidaMaxima());
        inimigo.setAtaque(base.getAtaque() + variacaoAtaque);

        return inimigo;
    }


    // ##################################################################
    // ###      NOVA LÓGICA DE RITMO (SUA IDEIA 2)          ###
    // ##################################################################

    /**
     * MÉTODO ANTIGO "explorar" RENOMEADO E MELHORADO.
     * Agora usa as pausas para criar drama.
     */
    private static void explorarLocal(Personagem heroi, String nomeLocal, String tipoLocal) {
        System.out.println();
        imprimirComPausa("🌄 Você entra na " + nomeLocal + "...", 1500);

        // 30% chance de encontrar algo especial
        if (random.nextDouble() < 0.3) {
            imprimirComPausa("O caminho parece quieto...", 1500);
            eventoEspecial(heroi);
            aguardarEnter(); // Pausa após o evento
            return;
        }

        // 70% chance de encontrar inimigo
        imprimirComPausa("O ar fica pesado. Você ouve um barulho...", 2000);
        imprimirComPausa("...", 1000);
        imprimirComPausa("...", 1000);
        imprimirComPausa("DE REPENTE!", 700);

        // Encontro com inimigo
        Inimigo inimigo = sortearInimigo(tipoLocal); // Sorteia inimigo do local
        System.out.println("👹 UM " + inimigo.getNome().toUpperCase() + " SALTA EM SUA DIREÇÃO!");

        aguardarEnter(); // Dá tempo para o jogador ler antes da batalha começar

        boolean heroiVenceu = Batalha.batalhar(heroi, inimigo, scanner);

        if (heroiVenceu) {
            // Chance de encontrar um item extra *além* do loot do inimigo
            if (random.nextDouble() < 0.2) {
                System.out.println("\n🗺️ Após a batalha, você explora a área...");
                pausar(1000);
                encontrarItem(heroi);
            }
        } else if (heroi.estaMorto()) {
            // Se o herói morreu, a batalha já imprimiu a mensagem.
            // O loop principal no main() vai parar.
            return;
        } else {
            // Herói fugiu
            imprimirComPausa("Você se reagrupa e volta ao menu de locais...", 1500);
            return; // Retorna ao menuExplorar
        }

        // Pausa final antes de voltar ao menu principal
        aguardarEnter();
    }


    // ##################################################################
    // ###                MÉTODOS AUXILIARES (SEM MUDANÇAS)     ###
    // ##################################################################

    private static String exibirIntroducao(Scanner scanner) {
        System.out.println("\n(Pressione Enter para iniciar a história...)");
        scanner.nextLine();
        System.out.println("📜 Narrador: À algum tempo atrás, no reino de Sendeor, onde líderes tiranos...");
        imprimirComPausa("...deixavam seus súditos em situações miseráveis...", 2000);
        imprimirComPausa("...nascia um bebê que, futuramente, se tornaria o herói da nação.", 2000);
        System.out.println("\n📜 Narrador: E seu nome? Seu nome era...");
        pausar(1000);
        System.out.print("💬 Digite o nome do seu herói: ");

        String nome = scanner.nextLine();
        if (nome.trim().isEmpty()) {
            nome = "Herói Sem Nome";
        }

        System.out.println("\n📜 Narrador: ...seu nome era " + nome + ", destinado(a) a salvar os cidadãos de sua terra natal.");
        aguardarEnter();
        return nome.trim();
    }

    private static Personagem escolherClasse(String nome, Scanner scanner) {
        System.out.println("\n🎭 ESCOLHA SUA CLASSE, " + nome + ":");
        System.out.println("1. ⚔️  GUERREIRO");
        System.out.println("   - Vida: 80, Ataque: 12, Defesa: 8");
        System.out.println("2. 🏹 ARQUEIRO");
        System.out.println("   - Vida: 60, Ataque: 10, Defesa: 5");
        System.out.println("3. 🔮 MAGO");
        System.out.println("   - Vida: 50, Ataque: 8, Defesa: 3");
        System.out.print("\n🎯 Escolha (1-3): ");

        String escolha = scanner.nextLine();
        Personagem heroi;

        switch (escolha) {
            case "1":
                System.out.println("\n📜 Narrador: Um Guerreiro!");
                imprimirComPausa("Treinado na fortaleza de Pedra Alta, " + nome + " usa sua força bruta", 2000);
                imprimirComPausa("e sua espada para proteger os inocentes.", 1500);
                heroi = new Guerreiro(nome, 80, 12, 8, 1);
                break;
            case "2":
                System.out.println("\n📜 Narrador: Um Arqueiro!");
                imprimirComPausa("Vindo das florestas densas de Sylan, " + nome + " usa sua precisão", 2000);
                imprimirComPausa("e agilidade para abater inimigos à distância.", 1500);
                heroi = new Arqueiro(nome, 60, 10, 5, 1);
                break;
            case "3":
                System.out.println("\n📜 Narrador: Um Mago!");
                imprimirComPausa("Estudante da Torre de Marfim, " + nome + " manipula as energias arcanas", 2000);
                imprimirComPausa("para destruir seus oponentes com magias poderosas.", 1500);
                heroi = new Mago(nome, 50, 8, 3, 1);
                break;
            default:
                System.out.println("\n❌ Escolha inválida! O destino escolhe por você...");
                pausar(1000);
                System.out.println("📜 Narrador: Um Guerreiro!");
                imprimirComPausa("Treinado na fortaleza de Pedra Alta, " + nome + " usa sua força bruta", 2000);
                imprimirComPausa("e sua espada para proteger os inocentes.", 1500);
                heroi = new Guerreiro(nome, 80, 12, 8, 1);
                break;
        }
        return heroi;
    }

    private static void eventoEspecial(Personagem heroi) {
        int evento = random.nextInt(3);
        switch (evento) {
            case 0:
                System.out.println("🎁 Você encontrou um baú perdido!");
                Item pocao = new Item("Poção de Cura", "Restaura 30 HP", "CURA:30", 1);
                heroi.getInventario().adicionarItem(pocao);
                break;
            case 1:
                System.out.println("💫 Você encontra uma fonte mística e bebe sua água...");
                heroi.curar(25);
                break;
            case 2:
                System.out.println("📜 Você encontra um pergaminho antigo com conhecimentos de batalha.");
                System.out.println("🌟 Seu ataque aumenta permanentemente em 2!");
                heroi.aumentarAtaque(2);
                break;
        }
    }

    private static void encontrarItem(Personagem heroi) {
        Item[] itensComuns = {
                new Item("Poção de Cura", "Restaura 30 HP", "CURA:30", 1),
                new Item("Elixir de Energia", "Restaura 15 HP", "CURA:15", 1),
                new Item("Pedra de Afiar", "+3 Ataque por 1 batalha", "ATK_UP:3", 1)
        };
        Item itemEncontrado = itensComuns[random.nextInt(itensComuns.length)];
        heroi.getInventario().adicionarItem(itemEncontrado);
    }

    private static void usarItem(Personagem heroi) {
        System.out.println("🎒 INVENTÁRIO (Menu Principal):");
        String listaItens = heroi.getInventario().listarItens();
        System.out.println(listaItens);

        if (heroi.getInventario().estaVazio()) {
            return;
        }
        System.out.print("💬 Digite o nome do item para usar (ou 'voltar'): ");
        String nomeItem = scanner.nextLine();
        if (nomeItem.equalsIgnoreCase("voltar")) {
            return;
        }
        var itemOptional = heroi.getInventario().buscarItemPorNome(nomeItem);
        if (itemOptional.isEmpty()) {
            System.out.println("❌ Item não encontrado no inventário.");
            return;
        }
        Item itemReal = itemOptional.get();
        if (heroi.getInventario().removerUmaUnidade(itemReal.getNome())) {
            aplicarEfeitoItem(heroi, itemReal);
        } else {
            System.out.println("❌ Erro ao usar o item.");
        }
        aguardarEnter(); // Pausa após usar o item
    }

    private static void aplicarEfeitoItem(Personagem heroi, Item item) {
        String tipoEfeito = item.getTipoEfeito();
        int valorEfeito = item.getValorEfeito();
        System.out.println("✨ " + heroi.getNome() + " usa " + item.getNome() + "!");
        pausar(1000);

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

    private static void exibirStatusDetalhado(Personagem heroi) {
        System.out.println("📋 STATUS DETALHADO 📋");
        System.out.println("=".repeat(30));
        System.out.println(heroi.toString());
        System.out.println("=".repeat(30));
        System.out.println("\n🎒 ITENS NO INVENTÁRIO:");
        System.out.println(heroi.getInventario().listarItens());
        aguardarEnter(); // Pausa após ver o status
    }
}