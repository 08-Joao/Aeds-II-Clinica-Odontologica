package Utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import entities.Horario;

public class OrdenacaoHorarios {
	// Tamanhos dos campos para garantir que os registros tenham tamanhos fixos
    private static final int TAMANHO_ID = 4; // int
    private static final int TAMANHO_NOME = 100;
    private static final int TAMANHO_CPF = 15;
    private static final int TAMANHO_TELEFONE = 14;
    private static final int TAMANHO_DATA_NASCIMENTO = 10;
    private static final int TAMANHO_ENDERECO = 100;
    private static final int TAMANHO_PROFISSAO = 50;

    private static final int TAMANHO_REGISTRO_CLIENTE = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE + TAMANHO_DATA_NASCIMENTO + TAMANHO_ENDERECO;
    private static final int TAMANHO_REGISTRO_PROFISSIONAL = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE + TAMANHO_DATA_NASCIMENTO + TAMANHO_ENDERECO + TAMANHO_PROFISSAO;

    private static final int TAMANHO_HORARIO = 8; 
    private static final int TAMANHO_LISTA_AGENDADOS = 8; 
    private static final int TAMANHO_TOTAL_AGENDAMENTOS = TAMANHO_LISTA_AGENDADOS * 10;
    private static final int TAMANHO_REGISTRO_HORARIO = TAMANHO_HORARIO + TAMANHO_TOTAL_AGENDAMENTOS; 



    private static final String CAMINHO_CLIENTES = "clientes.dat";
    private static final String CAMINHO_PROFISSIONAIS = "profissionais.dat";
    private static final String CAMINHO_HORARIOS = "horarios.dat";

    
    

    
    // Método para ordenar a base de horários no arquivo
    public static void ordenarBaseHorarios() throws IOException {
        String arquivoTemp = "temp.dat";
        
        // Passo 1: Copiar o conteúdo original para um arquivo temporário
        try (RandomAccessFile rafOriginal = new RandomAccessFile(CAMINHO_HORARIOS, "r");
             RandomAccessFile rafTemp = new RandomAccessFile(arquivoTemp, "rw")) {

            byte[] buffer = new byte[TAMANHO_REGISTRO_HORARIO];
            while (rafOriginal.read(buffer) != -1) {
                rafTemp.write(buffer);
            }
        }

        // Passo 2: Ordenar o arquivo temporário
        try (RandomAccessFile rafTemp = new RandomAccessFile(arquivoTemp, "rw")) {
            long numRegistros = rafTemp.length() / TAMANHO_REGISTRO_HORARIO;
            quickSortDiscoHorarios(rafTemp, 0, numRegistros - 1);
        }

        // Passo 3: Substituir o arquivo original pelo arquivo temporário ordenado
        try (RandomAccessFile rafOriginal = new RandomAccessFile(CAMINHO_HORARIOS, "rw");
             RandomAccessFile rafTemp = new RandomAccessFile(arquivoTemp, "r")) {

            byte[] buffer = new byte[TAMANHO_REGISTRO_HORARIO];
            while (rafTemp.read(buffer) != -1) {
                rafOriginal.write(buffer);
            }
        }

        // Excluir o arquivo temporário
        File tempFile = new File(arquivoTemp);
        if (!tempFile.delete()) {
            System.out.println("Não foi possível excluir o arquivo temporário.");
        }
    }

     // Função recursiva de QuickSort para ordenar os registros diretamente no disco
    private static void quickSortDiscoHorarios(RandomAccessFile raf, long esquerda, long direita) throws IOException {
        if (esquerda < direita) {
            long pivo = obterHorario(raf, (esquerda + direita) / 2);
            long i = esquerda;
            long j = direita;

            while (i <= j) {
                while (obterHorario(raf, i) < pivo) i++;
                while (obterHorario(raf, j) > pivo) j--;

                if (i <= j) {
                    trocarRegistrosHorarios(raf, i, j);
                    i++;
                    j--;
                }
            }

            if (esquerda < j) quickSortDiscoHorarios(raf, esquerda, j);
            if (i < direita) quickSortDiscoHorarios(raf, i, direita);
        }
    }

    // Função para obter o horário de um registro com base na posição
    private static long obterHorario(RandomAccessFile raf, long posicao) throws IOException {
        raf.seek(posicao * TAMANHO_REGISTRO_HORARIO);
        return raf.readLong();
    }

    // Função para trocar dois registros no arquivo, usada pelo QuickSort
    private static void trocarRegistrosHorarios(RandomAccessFile raf, long i, long j) throws IOException {
        byte[] bufferI = new byte[TAMANHO_REGISTRO_HORARIO];
        byte[] bufferJ = new byte[TAMANHO_REGISTRO_HORARIO];

        raf.seek(i * TAMANHO_REGISTRO_HORARIO);
        raf.readFully(bufferI);
        raf.seek(j * TAMANHO_REGISTRO_HORARIO);
        raf.readFully(bufferJ);

        raf.seek(i * TAMANHO_REGISTRO_HORARIO);
        raf.write(bufferJ);
        raf.seek(j * TAMANHO_REGISTRO_HORARIO);
        raf.write(bufferI);
    }
}
