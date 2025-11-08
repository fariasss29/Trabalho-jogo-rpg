public class Batalha {

    public static void batalhar (Personagem jogador, Personagem inimigo){
        System.out.println("\nA batalha Se inicia...\n");
        System.out.println(jogador.getNome() + "\nVS\n" + inimigo.getNome() + "\n");

        int turno = 1;

        while(jogador.estaVivo() && inimigo.estaVivo()){
            System.out.println("Turno: " + turno);

            if (jogador.estaVivo()){
                jogador.atacar(inimigo);
            }
            if (!inimigo.estaVivo()){
                System.out.println("Vitória!!! " + inimigo.getNome() + " Foi derrotado!!");
                if (inimigo.getNivel() >= 1 && inimigo.getNivel() <= 3){
                    jogador.nivel += 1;
                    jogador.ataque += 10;
                    jogador.vidaMaxima += 30;
                    jogador.defesa += 5;
                }
                if (inimigo.getNivel() >= 4 && inimigo.getNivel() <= 8){
                    jogador.nivel += 2;
                    jogador.ataque += 20;
                    jogador. vidaMaxima += 60;
                    jogador.defesa += 10;
                }
                if (inimigo.getNivel() >= 8){
                    jogador.nivel += 3;
                    jogador.ataque += 30;
                    jogador.vidaMaxima += 90;
                    jogador.defesa += 15;
                }
                break;
            }

            if (inimigo.estaVivo()){
                inimigo.atacar(jogador);
            }

            if (!jogador.estaVivo()){
                break;
            }

            turno ++;
        }

        System.out.println("FIM DA BATALHA");

    }
}
