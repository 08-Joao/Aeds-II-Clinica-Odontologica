package Utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class OrdenacaoPessoas {
	// Tamanhos dos campos para garantir que os registros tenham tamanhos fixos
    private static final int TAMANHO_ID = 4; // int
    private static final int TAMANHO_NOME = 100;
    private static final int TAMANHO_CPF = 15;
    private static final int TAMANHO_TELEFONE = 14;
    private static final int TAMANHO_DATA_NASCIMENTO = 10;
    private static final int TAMANHO_ENDERECO = 100;
    private static final int TAMANHO_PROFISSAO = 50;

    // Tamanho total dos registros
    private static final int TAMANHO_REGISTRO_CLIENTE = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE + TAMANHO_DATA_NASCIMENTO + TAMANHO_ENDERECO;
    private static final int TAMANHO_REGISTRO_PROFISSIONAL = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE + TAMANHO_DATA_NASCIMENTO + TAMANHO_ENDERECO + TAMANHO_PROFISSAO;

    private static final int TAMANHO_HORARIO = 8; // long (para armazenar o horário em milissegundos)
    private static final int TAMANHO_REGISTRO_HORARIO = TAMANHO_HORARIO;


    private static final String CAMINHO_CLIENTES = "clientes.dat";
    private static final String CAMINHO_PROFISSIONAIS = "profissionais.dat";
    private static final String CAMINHO_HORARIOS = "horarios.dat";

    public static void ordenarClientes() throws IOException {
        ordenarDisco(CAMINHO_CLIENTES, TAMANHO_REGISTRO_CLIENTE, TAMANHO_ID);
    }

    public static void ordenarProfissionais() throws IOException {
        ordenarDisco(CAMINHO_PROFISSIONAIS, TAMANHO_REGISTRO_PROFISSIONAL, TAMANHO_ID);
    }


    private static void ordenarDisco(String arquivo, int tamanhoRegistro, int tamanhoCampoId) throws IOException {
        String arquivoTemp = "temp.dat";

        // Usando o mesmo arquivo para leitura e escrita
        try (RandomAccessFile raf = new RandomAccessFile(arquivo, "rwd")) {
            // Criar um arquivo temporário para armazenar dados durante a ordenação
            try (RandomAccessFile rafTemp = new RandomAccessFile(arquivoTemp, "rw")) {
                // Copiar dados do arquivo original para o arquivo temporário
                byte[] buffer = new byte[tamanhoRegistro];
                while (raf.read(buffer) != -1) {
                    rafTemp.write(buffer);
                }

                // Ordenar o arquivo temporário
                System.out.println("Ordenando arquivo de disco.");
                int quantidade = (int) (rafTemp.length() / tamanhoRegistro);
                quickSortDisco(rafTemp, 0, quantidade - 1, tamanhoRegistro, tamanhoCampoId);

                // Copiar dados do arquivo temporário para o arquivo original
                raf.seek(0);  // Voltar ao início do arquivo original
                rafTemp.seek(0);  // Voltar ao início do arquivo temporário
                while (rafTemp.read(buffer) != -1) {
                    raf.write(buffer);
                }
            }

            // Opcional: Excluir o arquivo temporário após a cópia
            File tempFile = new File(arquivoTemp);
            if (tempFile.delete()) {
                System.out.println("Arquivo temporário excluído.");
            } else {
                System.out.println("Não foi possível excluir o arquivo temporário.");
            }
        }
    }


    private static void quickSortDisco(RandomAccessFile raf, int esquerda, int direita, int tamanhoRegistro, int tamanhoCampoId) throws IOException {
        if (esquerda < direita) {
            int idPivo = obterId(raf, (esquerda + direita) / 2, tamanhoRegistro, tamanhoCampoId);
            int i = esquerda;
            int j = direita;

            while (i <= j) {
                while (obterId(raf, i, tamanhoRegistro, tamanhoCampoId) < idPivo && i < direita) i++;
                while (obterId(raf, j, tamanhoRegistro, tamanhoCampoId) > idPivo && j > esquerda) j--;

                if (i <= j) {
                    trocarTodosCampos(raf, i, j, tamanhoRegistro);
                    i++;
                    j--;
                }
            }

            if (esquerda < j) quickSortDisco(raf, esquerda, j, tamanhoRegistro, tamanhoCampoId);
            if (i < direita) quickSortDisco(raf, i, direita, tamanhoRegistro, tamanhoCampoId);
        }
    }

    private static void trocarTodosCampos(RandomAccessFile raf, long i, long j, int tamanhoRegistro) throws IOException {
        byte[] registroI = new byte[tamanhoRegistro];
        byte[] registroJ = new byte[tamanhoRegistro];

        // Ler o registro na posição i
        raf.seek(i * tamanhoRegistro);
        raf.readFully(registroI);

        // Ler o registro na posição j
        raf.seek(j * tamanhoRegistro);
        raf.readFully(registroJ);

        // Escrever o registro na posição j
        raf.seek(j * tamanhoRegistro);
        raf.write(registroI);

        // Escrever o registro na posição i
        raf.seek(i * tamanhoRegistro);
        raf.write(registroJ);
    }

    private static int obterId(RandomAccessFile raf, long registro, int tamanhoRegistro, int tamanhoCampoId) throws IOException {
        raf.seek(registro * tamanhoRegistro); // Posiciona-se no início do registro
        return raf.readInt(); // Lê o inteiro diretamente
    }
    
}
