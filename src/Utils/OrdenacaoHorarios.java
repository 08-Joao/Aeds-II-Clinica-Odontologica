package Utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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
    
    
    
    
    
    
    
    
    
    
 // Método para ordenar a base de horários no arquivo
    public static void ordenarBaseHorariosNatural() throws IOException {
        String[] arquivosRuns = ordenacaoNatural(CAMINHO_HORARIOS, TAMANHO_REGISTRO_HORARIO, TAMANHO_HORARIO); 
        intercalarRuns("horarios_ordenados.dat", arquivosRuns, TAMANHO_REGISTRO_HORARIO, TAMANHO_HORARIO);
        substituirArquivo(CAMINHO_HORARIOS, "horarios_ordenados.dat");
    }

    // Método de ordenação natural
    private static String[] ordenacaoNatural(String arquivo, int tamanhoRegistro, int tamanhoCampoId) throws IOException {
        String tempPrefix = "temp_run_";
        RandomAccessFile raf = new RandomAccessFile(arquivo, "r");

        List<String> runFiles = new ArrayList<>();

        // Passo 1: Identificação e criação de runs naturais
        int runCounter = 0;
        while (raf.getFilePointer() < raf.length()) {
            String tempFileName = tempPrefix + runCounter++ + ".dat";
            try (RandomAccessFile tempRun = new RandomAccessFile(tempFileName, "rw")) {
                criarRunNatural(raf, tempRun, tamanhoRegistro, tamanhoCampoId);
                runFiles.add(tempFileName); // Armazena o nome do arquivo temporário
            }
        }

        raf.close();

        // Converte a lista de nomes de arquivos para array
        String[] arquivosRuns = runFiles.toArray(new String[0]);

        System.out.println("Ordenação natural concluída.");

        return arquivosRuns; // Retorna os nomes dos arquivos temporários para intercalação
    }

    // Método para criar um run natural
    private static void criarRunNatural(RandomAccessFile raf, RandomAccessFile tempRun, int tamanhoRegistro,
            int tamanhoCampoId) throws IOException {
        byte[] buffer = new byte[tamanhoRegistro];
        long ultimoHorario = Long.MIN_VALUE;

        while (raf.getFilePointer() < raf.length()) {
            long posicaoAtual = raf.getFilePointer();
            raf.read(buffer);
            long horarioAtual = obterHorarioFromBytes(buffer);

            if (horarioAtual >= ultimoHorario) {
                tempRun.write(buffer);
                ultimoHorario = horarioAtual;
            } else {
                raf.seek(posicaoAtual); // Volta o ponteiro para a posição anterior
                break; // Fim do run natural
            }
        }
    }

    // Método para obter o horário a partir dos bytes
    private static long obterHorarioFromBytes(byte[] buffer) {
        return ((long) (buffer[0] & 0xFF) << 56) |
               ((long) (buffer[1] & 0xFF) << 48) |
               ((long) (buffer[2] & 0xFF) << 40) |
               ((long) (buffer[3] & 0xFF) << 32) |
               ((long) (buffer[4] & 0xFF) << 24) |
               ((long) (buffer[5] & 0xFF) << 16) |
               ((long) (buffer[6] & 0xFF) << 8)  |
               ((long) (buffer[7] & 0xFF));
    }

    // Método de intercalação de runs
    public static void intercalarRuns(String arquivoSaida, String[] arquivosRuns, int tamanhoRegistro,
            int tamanhoCampoId) throws IOException {
        // Declara o array 'runs' no escopo do método
        Run[] runs = new Run[arquivosRuns.length];

        try (RandomAccessFile output = new RandomAccessFile(arquivoSaida, "rw")) {

            // Fila de prioridade para armazenar o menor registro de cada run
            PriorityQueue<RunRecord> pq = new PriorityQueue<>((r1, r2) -> Long.compare(r1.horario, r2.horario));

            // Abre todos os arquivos dos runs e insere o primeiro registro de cada run na fila
            for (int i = 0; i < arquivosRuns.length; i++) {
                runs[i] = new Run(arquivosRuns[i], tamanhoRegistro);
                if (runs[i].hasNext()) {
                    pq.add(runs[i].next());
                }
            }

            // Processa a fila de prioridade até que todos os runs tenham sido processados
            while (!pq.isEmpty()) {
                RunRecord menorRegistro = pq.poll();
                output.write(menorRegistro.registro); // Escreve o menor registro no arquivo de saída

                // Se o run de onde veio o menor registro ainda tiver mais registros, insere o próximo
                if (menorRegistro.run.hasNext()) {
                    pq.add(menorRegistro.run.next());
                }
            }
        }

        // Limpa os arquivos temporários após a intercalação
        for (Run run : runs) {
            run.close();
            File tempFile = new File(run.fileName);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

        System.out.println("Intercalação concluída.");
    }

    // Classe auxiliar para representar um Run (subsequência)
    static class Run {
        String fileName;
        RandomAccessFile raf;
        int tamanhoRegistro;

        Run(String fileName, int tamanhoRegistro) throws IOException {
            this.fileName = fileName;
            this.raf = new RandomAccessFile(fileName, "r");
            this.tamanhoRegistro = tamanhoRegistro;
        }

        boolean hasNext() throws IOException {
            return raf.getFilePointer() < raf.length();
        }

        RunRecord next() throws IOException {
            byte[] buffer = new byte[tamanhoRegistro];
            raf.read(buffer);
            long horario = obterHorarioFromBytes(buffer);
            return new RunRecord(horario, buffer, this);
        }

        void close() throws IOException {
            raf.close();
        }
    }

    // Classe auxiliar para representar um registro em um Run
    static class RunRecord implements Comparable<RunRecord> {
        long horario;
        byte[] registro;
        Run run;

        RunRecord(long horario, byte[] registro, Run run) {
            this.horario = horario;
            this.registro = registro;
            this.run = run;
        }

        @Override
        public int compareTo(RunRecord other) {
            return Long.compare(this.horario, other.horario);
        }
    }

    public static void substituirArquivo(String arquivoOriginal, String arquivoOrdenado) throws IOException {
        File arquivoOriginalFile = new File(arquivoOriginal);
        File arquivoOrdenadoFile = new File(arquivoOrdenado);

        if (!arquivoOrdenadoFile.exists()) {
            throw new IOException("Arquivo ordenado não encontrado: " + arquivoOrdenado);
        }

        // Apagar o arquivo original
        if (arquivoOriginalFile.exists()) {
            if (!arquivoOriginalFile.delete()) {
                throw new IOException("Não foi possível excluir o arquivo original: " + arquivoOriginal);
            }
        }

        // Renomear o arquivo ordenado para o nome do arquivo original
        if (!arquivoOrdenadoFile.renameTo(arquivoOriginalFile)) {
            throw new IOException("Não foi possível renomear o arquivo ordenado para: " + arquivoOriginal);
        }

        System.out.println("Arquivo original substituído com sucesso.");
    }
}
