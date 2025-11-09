public class Arqueiro extends Personagem {
    private int precisao; // Novo recurso único para o Arqueiro

    public Arqueiro(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
        this.precisao = 100; // Precisão inicial
    }

    public Arqueiro(Arqueiro outroArqueiro) {
        super(outroArqueiro);
        this.precisao = outroArqueiro.precisao;
    }

    @Override
    public int atacar(Personagem alvo) {
        int dado = Dado.rolarD8();

        // Chance de acerto crítico baseada na precisão
        boolean critico = Dado.rolarD20() > (20 - precisao / 10);
        int multiplicador = critico ? 2 : 1;

        int danoBruto = (this.getAtaque() + dado) * multiplicador - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        alvo.receberDano(danoReal);

        if (critico) {
            System.out.println("🎯 " + this.getNome() + " ACERTO CRÍTICO! Dispara uma flecha em " +
                    alvo.getNome() + " causando " + danoReal + " de dano (D8)!");
        } else {
            System.out.println("🏹 " + this.getNome() + " dispara uma flecha em " +
                    alvo.getNome() + " causando " + danoReal + " de dano (D8).");
        }
        System.out.println("🎯 Precisão: " + precisao + "%");

        return danoReal;
    }

    @Override
    public int usarHabilidadeEspecial(Personagem alvo) {
        if (precisao < 30) {
            System.out.println("❌ Precisão muito baixa! Necessário 30%, atual: " + precisao + "%");
            return 0;
        }

        System.out.println("🎯 " + this.getNome() + " usa TIRO CERTO!");

        // Reduz precisão temporariamente
        int precisaoAntiga = precisao;
        precisao = Math.max(10, precisao - 20);

        int dadoDano = Dado.rolarD8() + Dado.rolarD8() + Dado.rolarD4(); // 2D8 + D4
        int danoBase = this.getAtaque() * 2 + dadoDano;
        int danoReal = Math.max(5, danoBase - alvo.getDefesa());

        // Tiro certeiro ignora parte da defesa
        danoReal += alvo.getDefesa() / 2;

        alvo.receberDano(danoReal);
        System.out.println("💥 Flecha perfurante causa " + danoReal + " de dano em " + alvo.getNome() + "!");
        System.out.println("🎯 Precisão reduzida para: " + precisao + "%");

        return danoReal;
    }

    @Override
    public void usarHabilidadeDefensiva() {
        System.out.println("👁️ " + this.getNome() + " usa Foco Aprimorado!");

        int bonusAtaque = Dado.rolarD6() + this.getNivel();
        this.aumentarAtaque(bonusAtaque);

        // Recupera precisão
        int precisaoRecuperada = 15 + this.getNivel() * 2;
        precisao = Math.min(100, precisao + precisaoRecuperada);

        System.out.println("✨ Ataque aumentado em +" + bonusAtaque +
                " e precisão recuperada em +" + precisaoRecuperada + "%");
        System.out.println("⚔️ Ataque atual: " + this.getAtaque());
        System.out.println("🎯 Precisão atual: " + precisao + "%");
    }

    public void recuperarPrecisao() {
        int recuperacao = 10 + this.getNivel() * 3;
        precisao = Math.min(100, precisao + recuperacao);
        System.out.println("🎯 " + this.getNome() + " ajusta sua mira. Precisão +" + recuperacao + "%");
    }

    @Override
    public String getDescricaoHabilidades() {
        return "Habilidades: Flecha (D8), Tiro Certeiro (2D8+D4), Foco Aprimorado";
    }

    public int getPrecisao() {
        return precisao;
    }

    public void setPrecisao(int precisao) {
        this.precisao = Math.max(0, Math.min(precisao, 100));
    }

    // ######################################################
    // ### HABILIDADES ADICIONADAS CONFORME SOLICITADO    ###
    // ######################################################

    public int chuvaDeFlechas(Personagem alvo) {
        if (precisao < 30) {
            System.out.println("❌ Precisão insuficiente! Necessário 30%, atual: " + precisao + "%");
            return 0;
        }

        System.out.println("🌧️ " + this.getNome() + " dispara CHUVA DE FLECHAS!");
        precisao -= 30;

        int dadoDano = Dado.rolarD6() + Dado.rolarD6() + Dado.rolarD6(); // 3D6
        int danoBase = this.getAtaque() + dadoDano;
        int danoReal = Math.max(4, danoBase - alvo.getDefesa());

        alvo.receberDano(danoReal);
        System.out.println("💥 A chuva de flechas causa " + danoReal + " de dano em " + alvo.getNome() + "!");
        System.out.println("🎯 Precisão restante: " + precisao + "%");

        return danoReal;
    }

    public int disparoRapido(Personagem alvo) {
        if (precisao < 15) {
            System.out.println("❌ Precisão insuficiente! Necessário 15%, atual: " + precisao + "%");
            return 0;
        }

        System.out.println("⚡ " + this.getNome() + " usa DISPARO RÁPIDO!");
        precisao -= 15;

        // Dois ataques rápidos
        int danoTotal = 0;

        for (int i = 1; i <= 2; i++) {
            int dadoDano = Dado.rolarD4() + Dado.rolarD4(); // 2D4 por ataque
            int danoBase = this.getAtaque() + dadoDano;
            int danoReal = Math.max(2, danoBase - alvo.getDefesa());

            danoTotal += danoReal;
            System.out.println("🏹 Flecha " + i + " causa " + danoReal + " de dano!");
        }

        alvo.receberDano(danoTotal);
        System.out.println("💥 Disparo rápido causa " + danoTotal + " de dano total em " + alvo.getNome() + "!");
        System.out.println("🎯 Precisão restante: " + precisao + "%");

        return danoTotal;
    }
}