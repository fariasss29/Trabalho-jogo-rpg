public class Mago extends Personagem {
    private int mana; // Novo recurso único para o Mago

    public Mago(String nome, int vidaMaxima, int ataque, int defesa, int nivel) {
        super(nome, vidaMaxima, ataque, defesa, nivel);
        this.mana = 100; // Mana inicial
    }

    public Mago(Mago outroMago) {
        super(outroMago);
        this.mana = outroMago.mana;
    }

    @Override
    public int atacar(Personagem alvo) {
        int dado = Dado.rolarD6();
        int danoBruto = this.getAtaque() + dado - alvo.getDefesa();
        int danoReal = Math.max(1, danoBruto);

        // Regenera mana ao atacar
        mana = Math.min(100, mana + 5);

        alvo.receberDano(danoReal);
        System.out.println("🔮 " + this.getNome() + " lança um feitiço arcano em " +
                alvo.getNome() + " causando " + danoReal + " de dano (D6).");
        System.out.println("🔵 Mana: " + mana + "/100");

        return danoReal;
    }

    @Override
    public int usarHabilidadeEspecial(Personagem alvo) {
        if (mana < 30) {
            System.out.println("❌ Mana insuficiente! Necessário 30, atual: " + mana);
            return 0;
        }

        System.out.println("🔥 " + this.getNome() + " conjura BOLA DE FOGO!");
        mana -= 30;

        int dadoDano = Dado.rolarD6() + Dado.rolarD6() + Dado.rolarD6(); // 3D6
        int danoBase = this.getAtaque() + dadoDano;
        int danoReal = Math.max(3, danoBase - alvo.getDefesa());

        // Chance de queimar o inimigo (dano adicional no próximo turno)
        if (Dado.rolarD20() > 15) {
            System.out.println("💥 O inimigo está em chamas! Sofrerá dano adicional no próximo turno.");
        }

        alvo.receberDano(danoReal);
        System.out.println("💥 Explosão flamejante causa " + danoReal + " de dano em " + alvo.getNome() + "!");
        System.out.println("🔵 Mana restante: " + mana + "/100");

        return danoReal;
    }

    @Override
    public void usarHabilidadeDefensiva() {
        System.out.println("🧙 " + this.getNome() + " invoca Barreira Arcana!");

        int escudo = Dado.rolarD8() + this.getNivel() * 2;
        this.curar(escudo / 2); // Cura metade do escudo como HP

        System.out.println("✨ Barreira Arcana absorve " + escudo + " de dano e cura " + (escudo / 2) + " HP!");

        // Custa menos mana que ataque especial
        mana = Math.max(0, mana - 15);
        System.out.println("🔵 Mana restante: " + mana + "/100");
    }

    public void meditar() {
        int manaRecuperada = 25 + this.getNivel() * 5;
        mana = Math.min(100, mana + manaRecuperada);

        System.out.println("🧘 " + this.getNome() + " medita e recupera " + manaRecuperada + " de mana.");
        System.out.println("🔵 Mana atual: " + mana + "/100");
    }

    @Override
    public String getDescricaoHabilidades() {
        return "Habilidades: Feitiço Arcano (D6), Bola de Fogo (3D6), Barreira Arcana";
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, 100));
    }

    // ######################################################
    // ### HABILIDADES ADICIONADAS CONFORME SOLICITADO    ###
    // ######################################################

    public int raioArcano(Personagem alvo) {
        if (mana < 25) {
            System.out.println("❌ Mana insuficiente! Necessário 25, atual: " + mana);
            return 0;
        }

        System.out.println("⚡ " + this.getNome() + " conjura RAIO ARCANO!");
        mana -= 25;

        int dadoDano = Dado.rolarD8() + Dado.rolarD8(); // 2D8
        int danoBase = this.getAtaque() + dadoDano;

        // Raio arcano ignora metade da defesa
        int danoReal = Math.max(3, danoBase - (alvo.getDefesa() / 2));

        alvo.receberDano(danoReal);
        System.out.println("💥 O raio arcano causa " + danoReal + " de dano em " + alvo.getNome() + "!");
        System.out.println("🔵 Mana restante: " + mana + "/100");

        return danoReal;
    }
}