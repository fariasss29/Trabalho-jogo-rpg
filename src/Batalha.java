import java.util.Scanner;
import java.util.Optional;

public class Batalha {

    public static boolean batalhar(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("⚔️ BATALHA INICIADA ⚔️");
        System.out.println("=".repeat(40));
        System.out.println(heroi.toString());
        System.out.println("\n" + " ".repeat(18) + "VS\n");
        System.out.println(inimigo.toString());
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

    private static boolean turnoHeroi(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        while (true) {
            System.out.println("--- Vez de " + heroi.getNome() + " ---");
            System.out.println("1. ⚔️ Atacar (Básico)");
            System.out.println("2. ✨ Habilidade Especial");
            System.out.println("3. 🛡️ Habilidade Defensiva");
            System.out.println("4. 🎒 Usar Item");
            System.out.println("5. 🏃 Fugir");
            System.out.print("🎯 Escolha sua ação: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    heroi.atacar(inimigo);
                    return true;
                case "2":
                    // Menu de habilidades especiais - se retornar true, usou uma habilidade (termina turno), se false, voltou (continua)
                    if (menuHabilidadesEspeciais(heroi, inimigo, scanner)) {
                        return true;
                    }
                    break;
                case "3":
                    heroi.usarHabilidadeDefensiva();
                    return true;
                case "4":
                    if (usarItemBatalha(heroi, scanner)) {
                        return true;
                    }
                    break;
                case "5":
                    return false; // Sinaliza fuga
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }

    private static void turnoInimigo(Inimigo inimigo, Personagem heroi) {
        // 1. Tenta usar item (IA da classe Inimigo)
        if (inimigo.usarItemSePrecisar()) {
            return; // Inimigo usou o turno para se curar
        }

        // 2. Se não se curou, ataca (IA da classe Inimigo)
        inimigo.atacarDecidido(heroi);
    }

    private static void concederRecompensa(Personagem heroi, Inimigo inimigo, Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🎉 RECOMPENSAS DA BATALHA 🎉");
        System.out.println("=".repeat(40));

        int expGanha = inimigo.getExperienciaFornecida();
        System.out.println(String.format("⭐ Você ganhou %d pontos de experiência!", expGanha));

        heroi.aumentarNivel(1);
        System.out.println("🌟 LEVEL UP! " + heroi.getNome() + " alcançou o Nível " + heroi.getNivel() + "!");

        heroi.curar(heroi.getVidaMaxima());
        System.out.println("✨ Sua vida foi totalmente restaurada!");

        Inventario loot = inimigo.getInventario();
        if (!loot.estaVazio()) {
            System.out.println("\n🎁 Itens largados pelo inimigo:");
            for (Item item : loot.getItens()) {
                System.out.println("  • " + item.getNome() + " (x" + item.getQuantidade() + ")");
                heroi.getInventario().adicionarItem(item);
            }
        }

        int pontosGanhos = 5;
        System.out.println("\n✨ Você ganhou " + pontosGanhos + " Pontos de Atributo para distribuir!");

        System.out.println("\n(Pressione Enter para distribuir seus pontos...)");
        scanner.nextLine();

        distribuirPontosDeAtributo(heroi, pontosGanhos, scanner);
    }

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
        System.out.println(heroi.toString());
    }

    // 🎯 MÉTODO PRINCIPAL DO MENU DE HABILIDADES ESPECIAIS
    private static boolean menuHabilidadesEspeciais(Personagem heroi, Personagem inimigo, Scanner scanner) {
        if (heroi instanceof Guerreiro) {
            return menuHabilidadesGuerreiro((Guerreiro) heroi, inimigo, scanner);
        } else if (heroi instanceof Mago) {
            return menuHabilidadesMago((Mago) heroi, inimigo, scanner);
        } else if (heroi instanceof Arqueiro) {
            return menuHabilidadesArqueiro((Arqueiro) heroi, inimigo, scanner);
        }

        System.out.println("❌ Seu personagem não possui habilidades especiais.");
        return false;
    }

    // ⚔️ MENU DE HABILIDADES DO GUERREIRO
    private static boolean menuHabilidadesGuerreiro(Guerreiro guerreiro, Personagem inimigo, Scanner scanner) {
        while (true) {
            System.out.println("\n" + "═".repeat(50));
            System.out.println("⚔️  HABILIDADES DO GUERREIRO");
            System.out.println("═".repeat(50));
            System.out.println("🔥 Fúria Disponível: " + guerreiro.getCargaFuria() + "/100");
            System.out.println("❤️  Vida: " + guerreiro.getPontosVida() + "/" + guerreiro.getVidaMaxima());
            System.out.println("═".repeat(50));

            System.out.println("1. Golpe Esmagador - 30 Fúria");
            System.out.println("   ⚔️  Causa 2D8 + ATQ - DEF");
            System.out.println("   💥 Dano pesado com chance de atordoar");
            System.out.println("   " + (guerreiro.getCargaFuria() >= 30 ? "✅ Disponível" : "❌ Fúria insuficiente"));

            System.out.println("2. Fúria Descontrolada - 50 Fúria");
            System.out.println("   🔥 Causa 2D8 + ATQ×2 - DEF");
            System.out.println("   💀 Dano massivo, ignora parte da defesa");
            System.out.println("   " + (guerreiro.getCargaFuria() >= 50 ? "✅ Disponível" : "❌ Fúria insuficiente"));

            System.out.println("3. Posição Defensiva - 20 Fúria");
            System.out.println("   🛡️ +D6 DEF e cura DEF/2 de HP");
            System.out.println("   ✨ Defesa e cura em uma ação");
            System.out.println("   " + (guerreiro.getCargaFuria() >= 20 ? "✅ Disponível" : "❌ Fúria insuficiente"));

            System.out.println("4. Grito de Guerra - 25 Fúria");
            System.out.println("   📢 +3 ATQ permanente e recupera 15 HP");
            System.out.println("   💪 Buff ofensivo com cura");
            System.out.println("   " + (guerreiro.getCargaFuria() >= 25 ? "✅ Disponível" : "❌ Fúria insuficiente"));

            System.out.println("0. Voltar ao menu de ações");
            System.out.println("═".repeat(50));
            System.out.print("🎯 Escolha uma habilidade: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    if (guerreiro.getCargaFuria() >= 30) {
                        guerreiro.golpeEsmagador(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Fúria insuficiente! Necessário: 30, Disponível: " + guerreiro.getCargaFuria());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "2":
                    if (guerreiro.getCargaFuria() >= 50) {
                        guerreiro.usarHabilidadeEspecial(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Fúria insuficiente! Necessário: 50, Disponível: " + guerreiro.getCargaFuria());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "3":
                    if (guerreiro.getCargaFuria() >= 20) {
                        guerreiro.usarHabilidadeDefensiva();
                        return true;
                    } else {
                        System.out.println("❌ Fúria insuficiente! Necessário: 20, Disponível: " + guerreiro.getCargaFuria());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "4":
                    if (guerreiro.getCargaFuria() >= 25) {
                        guerreiro.gritoDeGuerra();
                        return true;
                    } else {
                        System.out.println("❌ Fúria insuficiente! Necessário: 25, Disponível: " + guerreiro.getCargaFuria());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "0":
                    return false; // Volta ao menu de ações
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
            }
        }
    }

    // 🔮 MENU DE HABILIDADES DO MAGO
    private static boolean menuHabilidadesMago(Mago mago, Personagem inimigo, Scanner scanner) {
        while (true) {
            System.out.println("\n" + "═".repeat(50));
            System.out.println("🔮 MAGIAS DO MAGO");
            System.out.println("═".repeat(50));
            System.out.println("🔵 Mana Disponível: " + mago.getMana() + "/100");
            System.out.println("❤️  Vida: " + mago.getPontosVida() + "/" + mago.getVidaMaxima());
            System.out.println("═".repeat(50));

            System.out.println("1. Bola de Fogo - 30 Mana");
            System.out.println("   🔥 Causa 3D6 + ATQ - DEF");
            System.out.println("   💥 Dano em área, chance de queimar");
            System.out.println("   " + (mago.getMana() >= 30 ? "✅ Disponível" : "❌ Mana insuficiente"));

            System.out.println("2. Raio Arcano - 25 Mana");
            System.out.println("   ⚡ Causa 2D8 + ATQ - DEF/2");
            System.out.println("   ✨ Ignora metade da defesa");
            System.out.println("   " + (mago.getMana() >= 25 ? "✅ Disponível" : "❌ Mana insuficiente"));

            System.out.println("3. Barreira Arcana - 15 Mana");
            System.out.println("   🛡️ Cura D8 + NVL×2 de HP");
            System.out.println("   💫 Cura baseada no nível");
            System.out.println("   " + (mago.getMana() >= 15 ? "✅ Disponível" : "❌ Mana insuficiente"));

            System.out.println("4. Meditação Profunda - 0 Mana");
            System.out.println("   🧘 Recupera 25 + NVL×5 de Mana");
            System.out.println("   🔄 Recarga de recursos");
            System.out.println("   ✅ Sempre disponível");

            System.out.println("0. Voltar ao menu de ações");
            System.out.println("═".repeat(50));
            System.out.print("🎯 Escolha uma magia: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    if (mago.getMana() >= 30) {
                        mago.usarHabilidadeEspecial(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Mana insuficiente! Necessário: 30, Disponível: " + mago.getMana());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "2":
                    if (mago.getMana() >= 25) {
                        mago.raioArcano(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Mana insuficiente! Necessário: 25, Disponível: " + mago.getMana());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "3":
                    if (mago.getMana() >= 15) {
                        mago.usarHabilidadeDefensiva();
                        return true;
                    } else {
                        System.out.println("❌ Mana insuficiente! Necessário: 15, Disponível: " + mago.getMana());
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "4":
                    mago.meditar();
                    return true;
                case "0":
                    return false; // Volta ao menu de ações
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
            }
        }
    }

    // 🏹 MENU DE HABILIDADES DO ARQUEIRO
    private static boolean menuHabilidadesArqueiro(Arqueiro arqueiro, Personagem inimigo, Scanner scanner) {
        while (true) {
            System.out.println("\n" + "═".repeat(50));
            System.out.println("🏹 HABILIDADES DO ARQUEIRO");
            System.out.println("═".repeat(50));
            System.out.println("🎯 Precisão Disponível: " + arqueiro.getPrecisao() + "%");
            System.out.println("❤️  Vida: " + arqueiro.getPontosVida() + "/" + arqueiro.getVidaMaxima());
            System.out.println("═".repeat(50));

            System.out.println("1. Tiro Certeiro - 20% Precisão");
            System.out.println("   🎯 Causa 2D8 + D4 + ATQ - DEF/2");
            System.out.println("   💫 Ignora metade da defesa");
            System.out.println("   " + (arqueiro.getPrecisao() >= 20 ? "✅ Disponível" : "❌ Precisão insuficiente"));

            System.out.println("2. Chuva de Flechas - 30% Precisão");
            System.out.println("   🌧️ Causa 3D6 + ATQ - DEF");
            System.out.println("   🔥 Dano múltiplo, difícil de esquivar");
            System.out.println("   " + (arqueiro.getPrecisao() >= 30 ? "✅ Disponível" : "❌ Precisão insuficiente"));

            System.out.println("3. Foco Aprimorado - 10% Precisão");
            System.out.println("   👁️ +D6 ATQ e +15% Precisão");
            System.out.println("   💪 Buff ofensivo duradouro");
            System.out.println("   " + (arqueiro.getPrecisao() >= 10 ? "✅ Disponível" : "❌ Precisão insuficiente"));

            System.out.println("4. Disparo Rápido - 15% Precisão");
            System.out.println("   🏹 Causa 2D4 + ATQ - DEF, ataca duas vezes");
            System.out.println("   ⚡ Ataque duplo rápido");
            System.out.println("   " + (arqueiro.getPrecisao() >= 15 ? "✅ Disponível" : "❌ Precisão insuficiente"));

            System.out.println("0. Voltar ao menu de ações");
            System.out.println("═".repeat(50));
            System.out.print("🎯 Escolha uma habilidade: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    if (arqueiro.getPrecisao() >= 20) {
                        arqueiro.usarHabilidadeEspecial(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Precisão insuficiente! Necessário: 20%, Disponível: " + arqueiro.getPrecisao() + "%");
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "2":
                    if (arqueiro.getPrecisao() >= 30) {
                        arqueiro.chuvaDeFlechas(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Precisão insuficiente! Necessário: 30%, Disponível: " + arqueiro.getPrecisao() + "%");
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "3":
                    if (arqueiro.getPrecisao() >= 10) {
                        arqueiro.usarHabilidadeDefensiva();
                        return true;
                    } else {
                        System.out.println("❌ Precisão insuficiente! Necessário: 10%, Disponível: " + arqueiro.getPrecisao() + "%");
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "4":
                    if (arqueiro.getPrecisao() >= 15) {
                        arqueiro.disparoRapido(inimigo);
                        return true;
                    } else {
                        System.out.println("❌ Precisão insuficiente! Necessário: 15%, Disponível: " + arqueiro.getPrecisao() + "%");
                        System.out.println("Pressione Enter para continuar...");
                        scanner.nextLine();
                    }
                    break;
                case "0":
                    return false; // Volta ao menu de ações
                default:
                    System.out.println("❌ Opção inválida. Tente novamente.");
                    System.out.println("Pressione Enter para continuar...");
                    scanner.nextLine();
            }
        }
    }

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

        Optional<Item> itemOptional = heroi.getInventario().buscarItemPorNome(nomeItem);

        if (itemOptional.isEmpty()) {
            System.out.println("❌ Item não encontrado no inventário.");
            return false;
        }

        Item item = itemOptional.get();

        if (heroi.getInventario().removerUmaUnidade(item.getNome())) {
            aplicarEfeitoItem(heroi, item);
            return true;
        } else {
            System.out.println("❌ Erro ao usar o item.");
            return false;
        }
    }

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