import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

public class GameManager {
    private Scanner scanner;
    private Random random;
    private List<Inimigo> inimigosDisponiveis;
    private Personagem heroi;
    private boolean jogando;
    private int eventosEspeciaisAtivados;
    private boolean bossDerrotado;

    public GameManager() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.inimigosDisponiveis = new ArrayList<>();
        this.jogando = true;
        this.eventosEspeciaisAtivados = 0;
        this.bossDerrotado = false;
        inicializarInimigos();
    }

    private static final Random RANDOM = new Random();

    public void iniciarJogo() {
        System.out.println("=========================================");
        System.out.println("     🏰 RPG DE TEXTO - JORNADA ÉPICA");
        System.out.println("=========================================");

        String nomeHeroi = exibirIntroducao();
        this.heroi = escolherClasse(nomeHeroi);

        System.out.println("🎒 Equipando itens iniciais...");
        pausar(1000);
        heroi.getInventario().adicionarItem(new Item("Poção de Cura", "Restaura 30 HP", "CURA:30", 2));
        pausar(500);
        heroi.getInventario().adicionarItem(new Item("Elixir de Ataque", "Aumente ATK em 5", "ATK_UP:5", 1));
        pausar(500);

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Sua jornada começa agora, " + heroi.getNome() + "!");
        System.out.println("=".repeat(50));

        aguardarEnter();

        while (jogando && heroi.estaVivo()) {
            exibirMenuPrincipal();
            String escolha = scanner.nextLine();
            System.out.println();
            processarEscolhaMenu(escolha);
        }

        finalizarJogo();
    }

    private void inicializarInimigos() {
        inimigosDisponiveis.add(Inimigo.criarGoblin());
        inimigosDisponiveis.add(Inimigo.criarLobo());
        inimigosDisponiveis.add(Inimigo.criarOrc());
        inimigosDisponiveis.add(Inimigo.criarEsqueleto());
        inimigosDisponiveis.add(Inimigo.criarAranhaGigante());
        inimigosDisponiveis.add(Inimigo.criarBruxa());
    }

    private String exibirIntroducao() {
        System.out.println("\n(Pressione Enter para iniciar a história...)");
        scanner.nextLine();
        System.out.println("📜 Narrador: À algum tempo atrás, no reino de Sendeor, onde líderes tiranos...");
        imprimirComPausa("...deixavam seus súditos em situações miseráveis...", 2000);
        imprimirComPausa("...nascia um bebê que, futuramente, se tornaria o herói da nação.", 2000);
        System.out.println("\n📜 Narrador: E seu nome? Seu nome era...");
        pausar(750);
        System.out.print("💬 Digite o nome do seu herói: ");

        String nome = scanner.nextLine();
        if (nome.trim().isEmpty()) {
            nome = "Herói Sem Nome";
        }

        System.out.println("\n📜 Narrador: ...seu nome era " + nome + ", destinado(a) a salvar os cidadãos de sua terra natal.");
        aguardarEnter();
        return nome.trim();
    }

    private Personagem escolherClasse(String nome) {
        System.out.println("📜 Narrador: Com muitos anos de treinamento e dedicação...");
        imprimirComPausa(nome + " se especializou e decidiu ser um...", 2000);
        System.out.print("💬 Escolha a classe de seu herói: \n\n");
        System.out.println("1. ⚔️  GUERREIRO");
        System.out.println("   - Vida: 80, Ataque: 12, Defesa: 8");
        System.out.println("   - Habilidade: Fúria Descontrolada (2D8)");
        System.out.println("2. 🏹 ARQUEIRO");
        System.out.println("   - Vida: 60, Ataque: 10, Defesa: 5");
        System.out.println("   - Habilidade: Tiro Certeiro (2D8+D4)");
        System.out.println("3. 🔮 MAGO");
        System.out.println("   - Vida: 50, Ataque: 8, Defesa: 3");
        System.out.println("   - Habilidade: Bola de Fogo (3D6)");
        System.out.print("\n🎯 Escolha (1-3): ");

        String escolha = scanner.nextLine();
        Personagem heroi;

        switch (escolha) {
            case "1":
                System.out.println("\n📜 Narrador: Guerreiro!!!");
                imprimirComPausa("Treinado na fortaleza de Pedra Alta, " + nome + " usa sua força bruta", 2000);
                imprimirComPausa("e sua espada para proteger os inocentes.", 1500);
                heroi = new Guerreiro(nome, 80, 12, 8, 1);
                break;
            case "2":
                System.out.println("\n📜 Narrador: Arqueiro!!!");
                imprimirComPausa("Vindo das florestas densas de Sylan, " + nome + " usa sua precisão", 2000);
                imprimirComPausa("e agilidade para abater inimigos à distância.", 1500);
                heroi = new Arqueiro(nome, 60, 10, 5, 1);
                break;
            case "3":
                System.out.println("\n📜 Narrador: Mago!!!");
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

        // Evento especial baseado na classe escolhida
        eventoEspecialInicial(heroi);
        return heroi;
    }

    private void eventoEspecialInicial(Personagem heroi) {
        System.out.println("\n🌟 EVENTO INICIAL ESPECIAL!");
        if (heroi instanceof Guerreiro) {
            System.out.println("⚔️  Você encontra uma espada lendária da fortaleza!");
            heroi.aumentarAtaque(3);
            heroi.getInventario().adicionarItem(new Item("Espada Lendária", "Aumenta ATK +3", "ATK_UP:3", 1));
        } else if (heroi instanceof Arqueiro) {
            System.out.println("🏹 Você recebe um arco élfico das florestas!");
            heroi.aumentarAtaque(2);
            heroi.getInventario().adicionarItem(new Item("Arco Élfico", "Aumenta PRC +2", "ATK_UP:2", 1));
        } else if (heroi instanceof Mago) {
            System.out.println("🔮 Você desbloqueia um grimório arcano antigo!");
            heroi.aumentarAtaque(2);
            heroi.getInventario().adicionarItem(new Item("Grimório Arcano", "Aumenta PDM +2", "ATK_UP:2", 1));
        }
        aguardarEnter();
    }

    private void exibirMenuPrincipal() {
        System.out.println("=".repeat(50));
        System.out.println("                 🎮 MENU PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("📊 " + heroi.getNome() + " | Nível " + heroi.getNivel());
        System.out.println("❤️  Vida: " + heroi.getPontosVida() + "/" + heroi.getVidaMaxima());
        System.out.println("⚔️  Ataque: " + heroi.getAtaque() + " | 🛡️ Defesa: " + heroi.getDefesa());


        if (heroi instanceof Guerreiro) {
            Guerreiro g = (Guerreiro) heroi;
            System.out.println("🔥 Fúria: " + g.getCargaFuria() + "/100");
        } else if (heroi instanceof Mago) {
            Mago m = (Mago) heroi;
            System.out.println("🔵 Mana: " + m.getMana() + "/100");
        } else if (heroi instanceof Arqueiro) {
            Arqueiro a = (Arqueiro) heroi;
            System.out.println("🎯 Precisão: " + a.getPrecisao() + "%");
        }

        System.out.println("-".repeat(50));
        System.out.println("1. 🗺️  Explorar Locais");
        System.out.println("2. 🎒 Inventário (Usar Itens)");
        System.out.println("3. 📋 Status Detalhado");

        if (bossDerrotado) {
            System.out.println("4. 🏆 Ver Final");
        } else if (heroi.getNivel() >= 5) {
            System.out.println("4. 🏰 Desafiar Boss Final");
        } else {
            System.out.println("4. 🏰 Boss Final (Nível 5 necessário)");
        }

        System.out.println("5. 🚪 Sair do Jogo");
        System.out.print("🎯 Escolha uma opção: ");
    }

    private void processarEscolhaMenu(String escolha) {
        switch (escolha) {
            case "1":
                menuExplorar();
                break;
            case "2":
                menuInventario();
                break;
            case "3":
                exibirStatusDetalhado();
                break;
            case "4":
                if (bossDerrotado) {
                    exibirFinal();
                } else if (heroi.getNivel() >= 5) {
                    enfrentarBossFinal();
                } else {
                    System.out.println("❌ Você precisa estar no nível 5 ou superior para desafiar o Boss Final!");
                    System.out.println("⭐ Seu nível atual: " + heroi.getNivel());
                }
                break;
            case "5":
                System.out.println("🏃 O herói decide encerrar sua jornada por hoje...");
                jogando = false;
                break;
            default:
                System.out.println("❌ Comando inválido. Tente novamente.");
        }
    }

    private void menuExplorar() {
        boolean explorando = true;
        while (explorando && heroi.estaVivo()) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("🗺️ PARA ONDE VOCÊ QUER IR?");
            System.out.println("-".repeat(50));
            System.out.println("1. 🌳 Floresta Sombria (Inimigos: Lobos, Aranhas)");
            System.out.println("2. 🏔️ Montanhas Rochosas (Inimigos: Goblins, Orcs)");
            System.out.println("3. 💀 Cripta Abandonada (Inimigos: Esqueletos, Bruxas)");

            if (heroi.getNivel() >= 3) {
                System.out.println("4. 🏚️ Vila Abandonada (Evento Especial - Nível 3+)");
            } else {
                System.out.println("4. 🏚️ Vila Abandonada (Nível 3 necessário)");
            }

            if (heroi.getNivel() >= 5 && !bossDerrotado) {
                System.out.println("5. 🏰 Castelo Amaldiçoado (BOSS FINAL - Nível 5+)");
            } else if (bossDerrotado) {
                System.out.println("5. 🏰 Castelo Amaldiçoado (Já Conquistado)");
            } else {
                System.out.println("5. 🏰 Castelo Amaldiçoado (Nível 5 necessário)");
            }

            System.out.println("0. 🏘️ Voltar para a área segura (Menu Principal)");
            System.out.print("🎯 Escolha seu destino: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    explorarLocal("Floresta Sombria", "floresta");
                    explorando = false;
                    break;
                case "2":
                    explorarLocal("Montanhas Rochosas", "montanha");
                    explorando = false;
                    break;
                case "3":
                    explorarLocal("Cripta Abandonada", "cripta");
                    explorando = false;
                    break;
                case "4":
                    if (heroi.getNivel() >= 3) {
                        explorarVilaAbandonada();
                        explorando = false;
                    } else {
                        System.out.println("❌ Você precisa estar no nível 3 ou superior para explorar a Vila Abandonada!");
                    }
                    break;
                case "5":
                    if (heroi.getNivel() >= 5 && !bossDerrotado) {
                        enfrentarBossFinal();
                        explorando = false;
                    } else if (bossDerrotado) {
                        System.out.println("🏰 Você já derrotou o Boss Final! O castelo agora está seguro.");
                    } else {
                        System.out.println("❌ Você precisa estar no nível 5 ou superior para desafiar o Boss Final!");
                    }
                    break;
                case "0":
                    explorando = false;
                    break;
                default:
                    System.out.println("❌ Localização inválida. Tente novamente.");
            }
        }
    }

    private void explorarLocal(String nomeLocal, String tipoLocal) {
        System.out.println();
        imprimirComPausa("🌄 Você entra na " + nomeLocal + "...", 1500);

        Item itemEncontrado = null;

        // 25% chance de encontrar evento de decisão
        if (random.nextDouble() < 0.25) {

            if (random.nextDouble() < 0.50){
                decisaoExploracao(tipoLocal);

            }else {
                imprimirComPausa("Você segue o caminho tranquilamente...", 1500);
                imprimirComPausa("Até que...", 1500);
                imprimirComPausa("Você avista algo no chão", 1500);

                imprimirComPausa("💬 Deseja pegar o item?: (S/N)", 1500);
                String decisao = scanner.nextLine().trim();

                if (decisao.equalsIgnoreCase("S")) {
                    if (random.nextDouble() < 0.50) {
                        itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_COMUNS);
                    } else {
                        itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_LIXO);
                    }
                    if (itemEncontrado != null) {
                        heroi.getInventario().adicionarItem(itemEncontrado);
                        System.out.println("🎁 Você encontrou: " + itemEncontrado.getNome() + " e adicionou ao inventário.");
                    }else{
                        System.out.println("O objeto se desfez ao tocá-lo...");
                    }
                }else{
                    System.out.println("Você ignora o objeto e segue em frente.");
                }
            }
            aguardarEnter();
        }
        else if (random.nextDouble() < 0.15) {
            eventoEspecial();
        }

        else if (random.nextDouble() < 0.10) {
            imprimirComPausa("Você encontra algo brilhando no chão...", 1500);
            encontrarItemRaro();
            aguardarEnter();
            return;

        }else {
            encontroInimigo(tipoLocal);
        }
    }

    private void encontroInimigo(String tipoLocal) {

        imprimirComPausa("O ar fica pesado. Você ouve um barulho...", 2000);
        imprimirComPausa("...", 1000);
        imprimirComPausa("...", 1000);
        imprimirComPausa("DE REPENTE!", 700);

        Inimigo inimigo = sortearInimigo(tipoLocal);
        System.out.println("👹 UM " + inimigo.getNome().toUpperCase() + " SALTA EM SUA DIREÇÃO!");

        aguardarEnter();

        boolean heroiVenceu = Batalha.batalhar(heroi, inimigo, scanner);

        if (heroiVenceu) {
            // Chance de encontrar um item extra após a batalha
            if (random.nextDouble() < 0.3) {
                System.out.println("\n🗺️ Após a batalha, você explora a área...");
                pausar(1000);
                encontrarItem();
            }

            // Chance de evento especial pós-batalha
            if (random.nextDouble() < 0.15) {
                eventoPosBatalha();
            }
        } else if (heroi.estaMorto()) {
            return;
        }
        aguardarEnter();
    }

    private void decisaoExploracao(String tipoLocal) {
        System.out.println("\n🗺️ O caminho se bifurca:");
        System.out.println("1. Caminho da Esquerda: Parece silencioso.");
        System.out.println("2. Caminho da Direita: Você sente um mal olhar.");
        System.out.print("🎯 Escolha o caminho (1 ou 2) ou digite 'V' para voltar: ");

        String escolha = scanner.nextLine().trim();

        switch (escolha) {
            case "1":
                // Caminho 1: 50% Armadilha, 50% Item
                System.out.println("Você escolheu o caminho silencioso...");
                if (random.nextDouble() < 0.5) {
                    eventoArmadilha(); // Implementa a lógica de Armadilha (D20)
                } else {
                    System.out.println("Você anda por um tempo e...");
                    pausar(1000);
                    System.out.println("Encontra um báu perdido...");

                    Item itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_COMUNS);

                    heroi.getInventario().adicionarItem(itemEncontrado);

                    System.out.println("🎁 Você abriu o baú! " + itemEncontrado.getNome() + " foi adicionado ao seu inventário");
                    aguardarEnter();
                }
                break;

            case "2":
                // Caminho 2: Inimigo Garantido (com chance de ser mais forte)
                System.out.println("Você escolheu ser corajoso...");
                encontroInimigo(tipoLocal);
                break;

            case "V":
                System.out.println("Você hesita e volta ao menu de locais.");
                break;

            default:
                System.out.println("Escolha inválida. O herói perde tempo valioso.");
        }
    }

    private void eventoArmadilha() {
        System.out.println("\n⚠️  Você pisa em uma laje escondida!");
        imprimirComPausa("Uma armadilha de espinhos se ativa...", 1500);

        int danoFixo = 20;

        int danoRecebido = danoFixo;

        heroi.receberDano(danoRecebido);
        System.out.println("❤️ HP atual: " + heroi.getPontosVida() + "/" + heroi.getVidaMaxima());

    }

    private static Item sortearItemDaLista(List<Item> lista) {
        if (lista.isEmpty()) {
            return new Item("Nulo", "Item Padrão", "LIXO:0", 1).copiar();
        }

        int indice = RANDOM.nextInt(lista.size());
        Item itemBase = lista.get(indice);

        return itemBase.copiar();
    }

    private void explorarVilaAbandonada() {
        System.out.println();
        imprimirComPausa("🏚️ Você chega a uma vila abandonada...", 1500);
        imprimirComPausa("As casas estão em ruínas e o silêncio é assustador...", 2000);

        // Evento especial único para a vila abandonada
        eventosEspeciaisAtivados++;

        if (eventosEspeciaisAtivados == 1) {
            System.out.println("📜 Você encontra um diário antigo...");
            imprimirComPausa("...que conta a história de como a vila foi destruída pelo Dragão Negro.", 2000);
            System.out.println("🌟 Você ganha conhecimento sobre o Boss Final!");
            heroi.aumentarAtaque(2);
            System.out.println("⚔️ Seu ataque aumenta em +2 devido ao conhecimento adquirido!");
        } else if (eventosEspeciaisAtivados == 2) {
            System.out.println("💎 Você encontra um baú escondido com equipamentos lendários!!!");

            Item itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_LENDARIOS);

            if (itemEncontrado != null) {
                heroi.getInventario().adicionarItem(itemEncontrado);
                System.out.println("🎁 Você ganhou: " + itemEncontrado.getNome() + "!");
            }
        } else {
            System.out.println("⚔️ Você treina nas ruínas da vila, melhorando suas habilidades!");
            heroi.aumentarAtaque(3);
            heroi.aumentarDefesa(3);
            System.out.println("⚔️ Ataque +3, 🛡️ Defesa +3!");
        }

        aguardarEnter();
    }

    private Inimigo sortearInimigo(String tipoLocal) {
        List<Inimigo> poolDeInimigos;

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
            poolDeInimigos = inimigosDisponiveis;
        }

        if (poolDeInimigos.isEmpty()) {
            poolDeInimigos = inimigosDisponiveis;
        }

        int index = random.nextInt(poolDeInimigos.size());
        Inimigo base = poolDeInimigos.get(index);
        Inimigo inimigo = new Inimigo(base);

        // Variação aleatória nos atributos baseada no nível do herói
        double fatorDificuldade = 1.0 + (heroi.getNivel() * 0.1);
        int variacaoVida = (int)(base.getVidaMaxima() * (random.nextDouble() * 0.3));
        int variacaoAtaque = (int)(base.getAtaque() * (random.nextDouble() * 0.2));

        inimigo.setVidaMaxima((int)(base.getVidaMaxima() * fatorDificuldade) + variacaoVida);
        inimigo.setPontosVida(inimigo.getVidaMaxima());
        inimigo.setAtaque((int)(base.getAtaque() * fatorDificuldade) + variacaoAtaque);
        inimigo.setNivel(heroi.getNivel()); // Inimigo escala com o herói

        return inimigo;
    }

    private void enfrentarBossFinal() {
        System.out.println("\n🏰 VOCÊ SE APROXIMA DO CASTELO AMALDIÇOADO!");
        imprimirComPausa("O ar fica gelado...", 2000);
        imprimirComPausa("Uma presença maligna paira sobre você...", 2000);
        imprimirComPausa("VOCÊ ENCONTROU O BOSS FINAL!", 1000);

        Inimigo boss = criarBossFinal();
        System.out.println("🐉 O DRAGÃO NEGRO APARECE DIANTE DE VOCÊ!");

        aguardarEnter();

        boolean vitoria = Batalha.batalhar(heroi, boss, scanner);

        if (vitoria) {
            bossDerrotado = true;
            System.out.println("\n🎉 PARABÉNS! VOCÊ DERROTOU O DRAGÃO NEGRO!");
            System.out.println("🏆 VOCÊ SALVOU O REINO DE SENDEOR!");

            // Recompensas épicas
            System.out.println("\n💎 RECOMPENSAS ÉPICAS:");
            System.out.println("⭐ +10 de Ataque Permanente!");
            System.out.println("⭐ +8 de Defesa Permanente!");
            System.out.println("⭐ +50 de Vida Máxima!");
            System.out.println("💰 1000 moedas de ouro!");

            heroi.aumentarAtaque(10);
            heroi.aumentarDefesa(8);
            heroi.setVidaMaxima(heroi.getVidaMaxima() + 50);
            heroi.curar(heroi.getVidaMaxima());

            heroi.getInventario().adicionarItem(new Item("Coração do Dragão", "Item lendário", "ATK_UP:20", 1));
            heroi.getInventario().adicionarItem(new Item("Escama Dragônica", "Defesa lendária", "DEF_UP:15", 1));
            heroi.getInventario().adicionarItem(new Item("Troféu do Herói", "Prova de sua vitória", "CURA:100", 1));

            aguardarEnter();
            exibirFinal();
        } else {
            System.out.println("\n💀 O Dragão Negro foi implacável...");
            System.out.println("🔄 Tente novamente quando estiver mais forte!");
        }
    }

    private Inimigo criarBossFinal() {
        Inimigo boss = new Inimigo("Dragão Negro", 250 + (heroi.getNivel() * 20), 30 + (heroi.getNivel() * 3), 20 + (heroi.getNivel() * 2), heroi.getNivel() + 2, "dragão");

        // Habilidades especiais do boss
        System.out.println("\nDe acordo com o pergaminho você descobre as habilidades do Dragão...");
        pausar(1000);
        System.out.println("\nEle equipa...");
        boss.getInventario().adicionarItem(new Item("Sopro de Fogo", "Ataque devastador", "ATK_UP:25", 1));
        boss.getInventario().adicionarItem(new Item("Escamas Impenetráveis", "Defesa máxima", "DEF_UP:20", 1));
        boss.getInventario().adicionarItem(new Item("Poção de Cura Épica", "Cura completa", "CURA:200", 3));

        return boss;
    }

    private void eventoEspecial() {
        int evento = random.nextInt(4);
        switch (evento) {
            case 0:
                System.out.println("🎁 Você encontrou um baú perdido!");

                Item itemEncontrado;

                if (random.nextDouble() < 0.70) {
                    itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_COMUNS);
                } else {
                    itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_RAROS);
                }

                if (itemEncontrado != null) {

                    heroi.getInventario().adicionarItem(itemEncontrado);

                    System.out.println("✨ Você ganhou: " + itemEncontrado.getNome() + "!");

                } else {
                    System.out.println("O baú estava estranhamente vazio...");
                }

                break;
            case 1:
                System.out.println("💫 Você encontra uma fonte mística e bebe sua água...");
                int cura = 30 + heroi.getNivel() * 5;
                heroi.curar(cura);
                System.out.println("❤️ Recuperou " + cura + " pontos de vida!");
                break;
            case 2:
                System.out.println("📜 Você encontra um pergaminho antigo com conhecimentos de batalha.");
                int aumentoAtaque = 2 + heroi.getNivel();
                heroi.aumentarAtaque(aumentoAtaque);
                System.out.println("🌟 Seu ataque aumenta permanentemente em " + aumentoAtaque + "!");
                break;
            case 3:
                System.out.println("🛡️ Você encontra um escudo abandonado em ótimo estado.");
                int aumentoDefesa = 2 + heroi.getNivel();
                heroi.aumentarDefesa(aumentoDefesa);
                System.out.println("🌟 Sua defesa aumenta permanentemente em " + aumentoDefesa + "!");
                break;
        }
    }

    private void eventoPosBatalha() {
        System.out.println("\n🌟 EVENTO PÓS-BATALHA!");
        int evento = random.nextInt(3);
        switch (evento) {
            case 0:
                System.out.println("🌿 Você encontra ervas medicinais e faz uma poção!");
                heroi.curar(20);
                break;
            case 1:
                System.out.println("💡 Você reflete sobre a batalha e aprende novas técnicas!");
                heroi.aumentarAtaque(1);
                System.out.println("⚔️ Ataque +1!");
                break;
            case 2:
                System.out.println("🏞️ Você descansa em um local seguro e recupera energias!");
                if (heroi instanceof Mago) {
                    ((Mago) heroi).setMana(100);
                    System.out.println("🔵 Mana totalmente recuperada!");
                } else if (heroi instanceof Arqueiro) {
                    ((Arqueiro) heroi).setPrecisao(100);
                    System.out.println("🎯 Precisão totalmente recuperada!");
                } else if (heroi instanceof Guerreiro) {
                    ((Guerreiro) heroi).resetarFuria();
                    System.out.println("🔥 Fúria resetada!");
                }
                break;
        }
    }

    private void encontrarItem() {

        Item itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_COMUNS);

        if (itemEncontrado != null) {

            heroi.getInventario().adicionarItem(itemEncontrado);
            System.out.println("🎁 Você encontrou: " + itemEncontrado.getNome() + "!");

        } else {
            System.out.println("O baú estava estranhamente vazio...");
        }
    }

    private void encontrarItemRaro() {

        Item itemEncontrado = sortearItemDaLista(Item.LISTA_ITENS_RAROS);

        if (itemEncontrado != null) {

            heroi.getInventario().adicionarItem(itemEncontrado);
            System.out.println("🎁 Você encontrou: " + itemEncontrado.getNome() + "!");

        } else {
            System.out.println("O baú estava estranhamente vazio...");
        }

    }

    private void menuInventario() {
        if (this.heroi.getInventario().estaVazio()) {
            System.out.println("📭 Inventário vazio. Nada para gerenciar.");
            aguardarEnter();
            return;
        }
        while (true) {

            System.out.println(this.heroi.getInventario().listarItens()); // Mostra o inventário atual
            System.out.println("1. ✨ Usar Item (Poções, Elixires)");
            System.out.println("2. 🗑️ Descartar Item");
            System.out.println("0. 🔙 Voltar ao Menu Principal");
            System.out.print("🎯 Escolha: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    usarItem();
                    return;
                case "2":
                    menuDescartarItens();
                    return;
                case "0":
                    return; // Sai do menu de inventário
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
                    aguardarEnter();
            }
        }
    }

    private void usarItem() {
        String listaItens = heroi.getInventario().listarItens();
        System.out.println(listaItens);

        System.out.print("💬 Digite o número do item que deseja usar (ou 0 para voltar): ");

        if (!scanner.hasNextInt()) {
            System.out.println("❌ Entrada inválida. Digite um número.");
            scanner.nextLine();
            return;
        }

        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            return; // Volta ao menu principal
        }

        Item itemParaUso = heroi.getInventario().buscarItemPorIndice(escolha);

        if (itemParaUso == null) {
            System.out.println("❌ Número de item inválido.");
            return;
        }

        Item itemConsumido = itemParaUso.copiar();

        if (heroi.getInventario().removerUmaUnidade(itemParaUso.getNome())) {
            aplicarEfeitoItem(heroi, itemConsumido);
            System.out.println("✨ " + itemConsumido.getNome() + " usado com sucesso!");
            System.out.println("❤️ Status atual: " + heroi.getPontosVida() + "/" + heroi.getVidaMaxima() + " HP");
        } else {
            System.out.println("❌ Erro ao usar o item. Talvez a quantidade seja zero.");
        }
    }

    private void menuDescartarItens() {

        System.out.println(this.heroi.getInventario().listarItens());

        if (this.heroi.getInventario().estaVazio()) {
            aguardarEnter();
            return;
        }

        System.out.print("💬 Digite o número do item que deseja descartar (ou 0 para voltar): ");

        // 1. Tenta ler um número (e trata exceção se for uma String)
        if (!scanner.hasNextInt()) {
            System.out.println("❌ Entrada inválida. Por favor, digite um número.");
            scanner.nextLine(); // Consome a linha inválida
            aguardarEnter();
            return;
        }

        int indiceEscolha = scanner.nextInt();
        scanner.nextLine(); // Consome o resto da linha

        if (indiceEscolha == 0) return;

        // 2. Busca o item pelo ÍNDICE (Corrigindo o fluxo)
        Item itemParaDescarte = this.heroi.getInventario().buscarItemPorIndice(indiceEscolha);

        if (itemParaDescarte == null) {
            System.out.println("❌ Número de item inválido ou item não encontrado.");
            aguardarEnter();
            return;
        }

        // O nome do item é puxado do objeto encontrado
        String nomeItem = itemParaDescarte.getNome();

        // 3. Pedir a Quantidade
        // O restante da lógica de quantidade e remoção pode ser reutilizada
        try {
            System.out.print(String.format("💬 Quantas unidades de '%s' deseja descartar? (Disponível: %d): ",
                    nomeItem, itemParaDescarte.getQuantidade()));

            int quantidade = Integer.parseInt(scanner.nextLine());

            if (quantidade <= 0) {
                System.out.println("❌ A quantidade deve ser maior que zero.");
            } else if (quantidade > itemParaDescarte.getQuantidade()) {
                System.out.println("❌ Você não tem essa quantidade de itens.");
            } else {
                // 4. Delegar o Descarte (Cria o Item temporário para o removerItem)
                Item itemTemporarioParaRemocao = new Item(
                        nomeItem,
                        itemParaDescarte.getDescricao(),
                        itemParaDescarte.getEfeito(),
                        quantidade
                );

                if (this.heroi.getInventario().removerItem(itemTemporarioParaRemocao)) {
                    System.out.println(String.format("🗑️ %d unidade(s) de '%s' descartada(s) com sucesso!", quantidade, nomeItem));
                } else {
                    System.out.println("❌ Falha interna ao descartar o item.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida. Por favor, digite um número.");
        }

        aguardarEnter();
    }

    private void aplicarEfeitoItem(Personagem heroi, Item item) {
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
            case "HP_UP":
                heroi.setVidaMaxima(heroi.getVidaMaxima() + valorEfeito);
                heroi.curar(valorEfeito);
                System.out.println("❤️ Vida máxima aumentada em +" + valorEfeito + "!");
                break;
            default:
                System.out.println("❌ Efeito do item não reconhecido: " + tipoEfeito);
        }
    }

    private void exibirStatusDetalhado() {
        System.out.println("📋 STATUS DETALHADO 📋");
        System.out.println("=".repeat(50));
        System.out.println(heroi.toString());

        // Informações específicas da classe
        if (heroi instanceof Guerreiro) {
            Guerreiro g = (Guerreiro) heroi;
            System.out.println("🔥 Carga de Fúria: " + g.getCargaFuria() + "/100");
            System.out.println("💥 Habilidade Especial: Fúria Descontrolada (2D8)");
            System.out.println("🛡️ Habilidade Defensiva: Posição Defensiva");
        } else if (heroi instanceof Mago) {
            Mago m = (Mago) heroi;
            System.out.println("🔵 Mana: " + m.getMana() + "/100");
            System.out.println("💥 Habilidade Especial: Bola de Fogo (3D6)");
            System.out.println("🛡️ Habilidade Defensiva: Barreira Arcana");
        } else if (heroi instanceof Arqueiro) {
            Arqueiro a = (Arqueiro) heroi;
            System.out.println("🎯 Precisão: " + a.getPrecisao() + "%");
            System.out.println("💥 Habilidade Especial: Tiro Certeiro (2D8+D4)");
            System.out.println("🛡️ Habilidade Defensiva: Foco Aprimorado");
        }

        System.out.println("=".repeat(50));
        System.out.println("\n🎒 ITENS NO INVENTÁRIO:");
        System.out.println(heroi.getInventario().listarItens());

        // Estatísticas de progresso
        System.out.println("📊 ESTATÍSTICAS DE PROGRESSO:");
        System.out.println("⭐ Nível: " + heroi.getNivel());
        System.out.println("🏆 Boss Final: " + (bossDerrotado ? "✅ Derrotado" : "❌ Pendente"));
        System.out.println("🔮 Eventos Especiais: " + eventosEspeciaisAtivados);

        aguardarEnter();
    }

    private void salvarProgresso() {
        System.out.println("\n💾 SALVANDO PROGRESSO...");
        pausar(2000);

        // Usa construtor de cópia para criar um save state
        Personagem saveHeroi;
        if (heroi instanceof Guerreiro) {
            saveHeroi = new Guerreiro((Guerreiro) heroi);
        } else if (heroi instanceof Mago) {
            saveHeroi = new Mago((Mago) heroi);
        } else {
            saveHeroi = new Arqueiro((Arqueiro) heroi);
        }

        System.out.println("✅ Progresso salvo com sucesso!");
        System.out.println("📊 Nível atual: " + saveHeroi.getNivel());
        System.out.println("❤️ Vida: " + saveHeroi.getPontosVida() + "/" + saveHeroi.getVidaMaxima());
        System.out.println("🎒 Itens no inventário: " + saveHeroi.getInventario().getTamanho());
        System.out.println("🏆 Boss Final: " + (bossDerrotado ? "Derrotado" : "Pendente"));

        aguardarEnter();
    }

    private void exibirFinal() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    🏆 FIM DE JOGO");
        System.out.println("=".repeat(60));

        System.out.println("📜 Narrador: E assim, " + heroi.getNome() + " cumpriu seu destino...");
        pausar(3000);

        System.out.println("✨ O Dragão Negro foi derrotado e o reino de Sendeor está salvo!");
        pausar(3000);

        System.out.println("🎭 O povo celebra seu herói, " + heroi.getNome() + ", o " +
                heroi.getClass().getSimpleName() + " lendário!");
        pausar(3000);

        System.out.println("\n📊 SUA JORNADA EM NÚMEROS:");
        System.out.println("⭐ Nível Final: " + heroi.getNivel());
        System.out.println("❤️ Vida Máxima: " + heroi.getVidaMaxima());
        System.out.println("⚔️ Ataque Final: " + heroi.getAtaque());
        System.out.println("🛡️ Defesa Final: " + heroi.getDefesa());
        System.out.println("🎒 Itens Coletados: " + heroi.getInventario().getTamanho());
        System.out.println("🔮 Eventos Especiais: " + eventosEspeciaisAtivados);

        System.out.println("\n🎉 PARABÉNS POR COMPLETAR RPG DE TEXTO - JORNADA ÉPICA!");
        System.out.println("=".repeat(60));

        jogando = false;
        aguardarEnter();
    }

    private void finalizarJogo() {
        if (!heroi.estaVivo()) {
            System.out.println("\n💀 FIM DE JOGO! Seu herói não resistiu aos perigos...");
            System.out.println("📊 Estatísticas Finais:");
            System.out.println("⭐ Nível alcançado: " + heroi.getNivel());
            System.out.println("❤️ Vida Máxima: " + heroi.getVidaMaxima());
            System.out.println("⚔️ Ataque: " + heroi.getAtaque());
            System.out.println("🛡️ Defesa: " + heroi.getDefesa());
            System.out.println("🏆 Boss Final: " + (bossDerrotado ? "✅ Derrotado" : "❌ Não derrotado"));
        } else if (bossDerrotado) {
            // Já exibiu o final épico
        } else {
            System.out.println("\n✨ Obrigado por jogar! Sua jornada termina aqui...");
            System.out.println("📊 Estatísticas Atuais:");
            System.out.println("⭐ Nível: " + heroi.getNivel());
            System.out.println("❤️ Vida: " + heroi.getPontosVida() + "/" + heroi.getVidaMaxima());
            System.out.println("🎒 Itens no inventário: " + heroi.getInventario().getTamanho());
        }

        System.out.println("\n👋 Até a próxima aventura!");
        scanner.close();
    }

    // Métodos auxiliares
    private void pausar(int milissegundos) {
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void imprimirComPausa(String texto, int milissegundos) {
        System.out.println(texto);
        pausar(milissegundos);
    }

    private void aguardarEnter() {
        System.out.println("\n(Pressione Enter para continuar...)");
        scanner.nextLine();
    }
}