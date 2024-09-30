package utils;

public class EntradaHash {
    private int id;
    private long ponteiro; // Posição no arquivo

    public EntradaHash(int id, long ponteiro) {
        this.id = id;
        this.ponteiro = ponteiro;
    }

    public int getId() {
        return id;
    }

    public long getPonteiro() {
        return ponteiro;
    }
}
