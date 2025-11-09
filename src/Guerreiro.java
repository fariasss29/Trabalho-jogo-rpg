public class Guerreiro extends Personagem {
    private int cargaFuria; // Novo recurso único para o Guerreiro

    public Guerreiro(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
        this.cargaFuria = 0;
    }

    public Guerreiro(Guerreiro outroGuerreiro) {
        super(outroGuerreiro);
        this.cargaFuria = outroGuerreiro.cargaFuria;
    }

    @Override
    public int atacar(Personagem alvo) {
        int dado = Dado.rolarD8();
        int danoBruto = this.getAtaque() + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        // Acumula fúria ao atacar
        cargaFuria = Math.min(100, cargaFuria + 10);

        alvo.receberDano(danoReal);
        System.out.println("⚔️ " + this.getNome() + " ataca " +
                alvo.getNome() + " com sua espada, causando " + danoReal + " de dano (D8).");
        System.out.println("🔥 Fúria: " + cargaFuria + "/100");

        return danoReal;
    }

    @Override
    public int usarHabilidadeEspecial(Personagem alvo) {
        if (cargaFuria < 50) {
            System.out.println("❌ Fúria insuficiente! Necessário 50, atual: " + cargaFuria);
            return 0;
        }

        System.out.println("💥 " + this.getNome() + " libera sua FÚRIA DESCONTROLADA!");
        cargaFuria -= 50;

        int dadoDano = Dado.rolarD8() + Dado.rolarD8(); // 2D8
        int danoBase = this.getAtaque() * 2 + dadoDano; // Dano dobrado
        int danoReal = Math.max(5, danoBase - alvo.getDefesa());

        alvo.receberDano(danoReal);
        System.out.println("🔥 Fúria Descontrolada causa " + danoReal + " de dano em " + alvo.getNome() + "!");
        System.out.println("🔥 Fúria restante: " + cargaFuria + "/100");

        return danoReal;
    }

    @Override
    public void usarHabilidadeDefensiva() {
        System.out.println("🛡️ " + this.getNome() + " assume Posição Defensiva!");

        int bonusDefesa = Dado.rolarD6() + this.getNivel();
        this.aumentarDefesa(bonusDefesa);

        // Cura baseada na defesa
        int cura = this.getDefesa() / 2;
        this.curar(cura);

        System.out.println("✨ Defesa aumentada em +" + bonusDefesa + " e recuperou " + cura + " HP!");
        System.out.println("🛡️ Defesa atual: " + this.getDefesa());
    }

    @Override
    public String getDescricaoHabilidades() {
        return "Habilidades: Ataque (D8), Fúria Descontrolada (2D8), Posição Defensiva";
    }

    public int getCargaFuria() {
        return cargaFuria;
    }

    public void resetarFuria() {
        this.cargaFuria = 0;
    }

    // ######################################################
    // ### HABILIDADES ADICIONADAS CONFORME SOLICITADO    ###
    // ######################################################

    public int golpeEsmagador(Personagem alvo) {
        if (cargaFuria < 30) {
            System.out.println("❌ Fúria insuficiente! Necessário 30, atual: " + cargaFuria);
            return 0;
        }

        System.out.println("💥 " + this.getNome() + " usa GOLPE ESMAGADOR!");
        cargaFuria -= 30;

        int dadoDano = Dado.rolarD8() + Dado.rolarD8(); // 2D8
        int danoBase = this.getAtaque() + dadoDano;
        int danoReal = Math.max(5, danoBase - alvo.getDefesa());

        // Chance de atordoar (20%)
        if (Dado.rolarD20() > 16) {
            System.out.println("😵 O inimigo ficou atordoado pelo impacto!");
            danoReal += 5; // Dano extra
        }

        alvo.receberDano(danoReal);
        System.out.println("💥 Impacto esmagador causa " + danoReal + " de dano em " + alvo.getNome() + "!");
        System.out.println("🔥 Fúria restante: " + cargaFuria + "/100");

        return danoReal;
    }

    public void gritoDeGuerra() {
        if (cargaFuria < 25) {
            System.out.println("❌ Fúria insuficiente! Necessário 25, atual: " + cargaFuria);
            return;
        }

        System.out.println("📢 " + this.getNome() + " solta um GRITO DE GUERRA!");
        cargaFuria -= 25;

        this.aumentarAtaque(3);
        this.curar(15);

        System.out.println("⚔️ Ataque aumentado em +3 permanentemente!");
        System.out.println("❤️ Recuperou 15 pontos de vida!");
        System.out.println("🔥 Fúria restante: " + cargaFuria + "/100");
    }
}