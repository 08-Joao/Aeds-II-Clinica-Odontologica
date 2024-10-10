package utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import entities.BaseDeDados;
import entities.TabelaHashDisco;

public class OrdenacaoPessoas {
	private static final int TAMANHO_ID = TamanhoCampo.ID.valor;
    private static final int TAMANHO_NOME = TamanhoCampo.NOME.valor;;
    private static final int TAMANHO_CPF = TamanhoCampo.CPF.valor;
    private static final int TAMANHO_TELEFONE = TamanhoCampo.TELEFONE.valor;
    private static final int TAMANHO_DATA_NASCIMENTO = TamanhoCampo.DATA_NASCIMENTO.valor;
    private static final int TAMANHO_ENDERECO = TamanhoCampo.ENDERECO.valor;
    private static final int TAMANHO_PROFISSAO = TamanhoCampo.PROFISSAO.valor;

    private static final int TAMANHO_REGISTRO_CLIENTE = TamanhoCampo.REGISTRO_CLIENTE.valor;
    private static final int TAMANHO_REGISTRO_PROFISSIONAL = TamanhoCampo.REGISTRO_PROFISSIONAL.valor;

    private static final int TAMANHO_HORARIO = TamanhoCampo.HORARIO.valor;
    private static final int TAMANHO_LISTA_AGENDADOS = TamanhoCampo.LISTA_AGENDADOS.valor; 
    private static final int TAMANHO_TOTAL_AGENDAMENTOS = TamanhoCampo.TOTAL_AGENDAMENTOS.valor; 
    private static final int TAMANHO_REGISTRO_HORARIO = TamanhoCampo.REGISTRO_HORARIO.valor;

  
    private static final String CAMINHO_CLIENTES = CaminhoArquivo.CLIENTES.caminho;
    private static final String CAMINHO_PROFISSIONAIS = CaminhoArquivo.PROFISSIONAIS.caminho;
    private static final String CAMINHO_HORARIOS = CaminhoArquivo.HORARIOS.caminho;
	
	
	private static final int MAX_REGISTROS_MEMORIA = 200;

	public static void ordenarClientes() throws IOException {
		ordenarDisco(CAMINHO_CLIENTES, TAMANHO_REGISTRO_CLIENTE, TAMANHO_ID);		
	}

	public static void ordenarProfissionais() throws IOException {
		ordenarDisco(CAMINHO_PROFISSIONAIS, TAMANHO_REGISTRO_PROFISSIONAL, TAMANHO_ID);
		BaseDeDados.mapearHash("Profissional");
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
				raf.seek(0); // Voltar ao início do arquivo original
				rafTemp.seek(0); // Voltar ao início do arquivo temporário
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

	private static void quickSortDisco(RandomAccessFile raf, int esquerda, int direita, int tamanhoRegistro,
			int tamanhoCampoId) throws IOException {
		if (esquerda < direita) {
			int idPivo = obterId(raf, (esquerda + direita) / 2, tamanhoRegistro, tamanhoCampoId);
			int i = esquerda;
			int j = direita;

			while (i <= j) {
				while (obterId(raf, i, tamanhoRegistro, tamanhoCampoId) < idPivo && i < direita)
					i++;
				while (obterId(raf, j, tamanhoRegistro, tamanhoCampoId) > idPivo && j > esquerda)
					j--;

				if (i <= j) {
					trocarTodosCampos(raf, i, j, tamanhoRegistro);
					i++;
					j--;
				}
			}

			if (esquerda < j)
				quickSortDisco(raf, esquerda, j, tamanhoRegistro, tamanhoCampoId);
			if (i < direita)
				quickSortDisco(raf, i, direita, tamanhoRegistro, tamanhoCampoId);
		}
	}

	private static void trocarTodosCampos(RandomAccessFile raf, long i, long j, int tamanhoRegistro)
			throws IOException {
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

	private static int obterId(RandomAccessFile raf, long registro, int tamanhoRegistro, int tamanhoCampoId)
			throws IOException {
		raf.seek(registro * tamanhoRegistro); // Posiciona-se no início do registro
		return raf.readInt(); // Lê o inteiro diretamente
	}

	// ------------------------------ ATIVIDADE AVALIATIVA II
	// ------------------------------

	public static void ordenarClientesNatural() throws IOException {
		String[] arquivosRuns = ordenacaoNatural("clientes.dat", TAMANHO_REGISTRO_CLIENTE, TAMANHO_ID);
		intercalarRuns("clientes_ordenados.dat", arquivosRuns, TAMANHO_REGISTRO_CLIENTE, TAMANHO_ID); // Arquivo de
																										// saída final
		substituirArquivo("clientes.dat", "clientes_ordenados.dat");		
	}

	public static void ordenarProfissionaisNatural() throws IOException {
		String[] arquivosRuns = ordenacaoNatural("profissionais.dat", TAMANHO_REGISTRO_PROFISSIONAL, TAMANHO_ID);
		intercalarRuns("profissionais_ordenados.dat", arquivosRuns, TAMANHO_REGISTRO_PROFISSIONAL, TAMANHO_ID); // Arquivo
																										// final
		substituirArquivo("profissionais.dat", "profissionais_ordenados.dat");
		BaseDeDados.mapearHash("Profissional");
	}

	// Método de ordenação natural
	private static String[] ordenacaoNatural(String arquivo, int tamanhoRegistro, int tamanhoCampoId)
			throws IOException {
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
		int ultimoId = Integer.MIN_VALUE;

		// Opcional: Limite de registros em memória
		int registrosEmMemoria = 0;

		while (raf.getFilePointer() < raf.length()) {
			long posicaoAtual = raf.getFilePointer();
			raf.read(buffer);
			int idAtual = obterIdFromBytes(buffer);

			if (idAtual >= ultimoId && registrosEmMemoria < MAX_REGISTROS_MEMORIA) {
				tempRun.write(buffer);
				ultimoId = idAtual;
				registrosEmMemoria++;
			} else {
				raf.seek(posicaoAtual); // Volta o ponteiro para a posição anterior
				break; // Fim do run natural
			}
		}
	}

	// Método para obter o ID a partir dos bytes
	private static int obterIdFromBytes(byte[] buffer) {
		return ((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16) | ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF);
	}

	public static void intercalarRunsOtima(String arquivoSaida, String[] arquivosRuns, int tamanhoRegistro,
			int tamanhoCampoId) throws IOException {
		// Define a quantidade máxima de runs que podem ser intercalados ao mesmo tempo
		int maxRuns = Math.min(arquivosRuns.length, MAX_REGISTROS_MEMORIA / tamanhoRegistro);

		while (arquivosRuns.length > 1) {
			List<String> novosRuns = new ArrayList<>();
			int totalRuns = arquivosRuns.length;
			int i = 0;

			// Processa o máximo de runs possível de uma vez
			while (i < totalRuns) {
				int fim = Math.min(i + maxRuns, totalRuns);
				String[] subsetRuns = Arrays.copyOfRange(arquivosRuns, i, fim);
				String novoRun = "temp_intercalado_" + i + ".dat";
				intercalarRuns(novoRun, subsetRuns, tamanhoRegistro, tamanhoCampoId);
				novosRuns.add(novoRun);
				i += maxRuns;
			}

			// Atualiza a lista de runs para a próxima iteração
			arquivosRuns = novosRuns.toArray(new String[0]);
		}

		// O último arquivo intercalado é o arquivo de saída final
		substituirArquivo(arquivoSaida, arquivosRuns[0]);
	}

	// Método de intercalação de runs (como o anterior, adaptado)
	public static void intercalarRuns(String arquivoSaida, String[] arquivosRuns, int tamanhoRegistro,
			int tamanhoCampoId) throws IOException {
		Run[] runs = new Run[arquivosRuns.length];

		try (RandomAccessFile output = new RandomAccessFile(arquivoSaida, "rw")) {
			PriorityQueue<RunRecord> pq = new PriorityQueue<>((r1, r2) -> Integer.compare(r1.id, r2.id));

			// Abre todos os arquivos dos runs e insere o primeiro registro de cada run na
			// fila
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

				// Se o run de onde veio o menor registro ainda tiver mais registros, insere o
				// próximo
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
			int id = obterIdFromBytes(buffer); // Assume que o campo ID tem 4 bytes
			return new RunRecord(id, buffer, this);
		}

		void close() throws IOException {
			raf.close();
		}
	}

	// Classe auxiliar para representar um registro em um Run
	static class RunRecord implements Comparable<RunRecord> {
		int id;
		byte[] registro;
		Run run;

		RunRecord(int id, byte[] registro, Run run) {
			this.id = id;
			this.registro = registro;
			this.run = run;
		}

		@Override
		public int compareTo(RunRecord other) {
			return Integer.compare(this.id, other.id);
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