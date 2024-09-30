package Utils;

import java.util.LinkedList;

import utils.EntradaHash;

public class TabelaHash {
    private static final int TAMANHO_TABELA = 10; // Tamanho da tabela hash
    private LinkedList<EntradaHash>[] tabela;

    @SuppressWarnings("unchecked")
    public TabelaHash() {
        tabela = new LinkedList[TAMANHO_TABELA];
        for (int i = 0; i < TAMANHO_TABELA; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    private int calcularHash(int id) {
        return id % TAMANHO_TABELA;
    }

    public void inserir(int id, long ponteiro) {
        int indice = calcularHash(id);
        tabela[indice].add(new EntradaHash(id, ponteiro));
    }

    public long buscar(int id) {
        int indice = calcularHash(id);
        for (EntradaHash entrada : tabela[indice]) {
            if (entrada.getId() == id) {
                return entrada.getPonteiro();
            }
        }
        return -1; // Não encontrado
    }

    public void remover(int id) {
        int indice = calcularHash(id);
        tabela[indice].removeIf(entrada -> entrada.getId() == id);
    }
}
