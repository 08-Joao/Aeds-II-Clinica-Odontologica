package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import entities.BaseDeDados;
import entities.Cliente;
import entities.Profissional;
import entities.TabelaHashDisco;


//Classe responsável por gerenciar operações relacionadas a clientes e profissionais

public class Gerenciamento {

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

    
    
	public static void adicionarCliente(TabelaHashDisco tabelaHash) throws IOException {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Digite o nome do cliente:");
		String nome = scanner.nextLine();
		System.out.println("Digite o CPF do cliente:");
		String cpf = scanner.nextLine();
		System.out.println("Digite o telefone do cliente:");
		String telefone = scanner.nextLine();
		System.out.println("Digite a data de nascimento do cliente (dd/MM/yyyy):");
		String dataNascimento = scanner.nextLine();
		System.out.println("Digite o endereço do cliente:");
		String endereco = scanner.nextLine();

		try (RandomAccessFile arquivo = new RandomAccessFile("clientes.dat", "rw")) {
			Long posicaoAtual = arquivo.length();
			Integer id = (int) ((posicaoAtual / TAMANHO_REGISTRO_CLIENTE) + 1);
			Cliente cliente = new Cliente(id, nome, cpf, telefone, dataNascimento, endereco,id);

			arquivo.seek(posicaoAtual);
			arquivo.writeInt(cliente.getId());
			arquivo.writeBytes(cliente.getNome());
			arquivo.writeBytes(cliente.getCpf());
			arquivo.writeBytes(cliente.getTelefone());
			arquivo.writeBytes(cliente.getDataNascimento());
			arquivo.writeBytes(cliente.getEndereco());
			
			tabelaHash.inserir(cliente);
		}
	}

	public static void adicionarProfissional() throws IOException {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Digite o nome do profissional:");
		String nome = scanner.nextLine();
		System.out.println("Digite o CPF do profissional:");
		String cpf = scanner.nextLine();
		System.out.println("Digite o telefone do profissional:");
		String telefone = scanner.nextLine();
		System.out.println("Digite a data de nascimento do profissional (dd/MM/yyyy):");
		String dataNascimento = scanner.nextLine();
		System.out.println("Digite o endereço do profissional:");
		String endereco = scanner.nextLine();
		System.out.println("Digite a profissão do profissional:");
		String profissao = scanner.nextLine();

		try (RandomAccessFile arquivo = new RandomAccessFile("profissionais.dat", "rw")) {
			Long posicaoAtual = arquivo.length();
			Integer id = (int) ((posicaoAtual / TAMANHO_REGISTRO_PROFISSIONAL) + 1);
			Profissional profissional = new Profissional(id, nome, cpf, telefone, dataNascimento, endereco, profissao);

			arquivo.seek(posicaoAtual);
			arquivo.writeInt(profissional.getId());
			arquivo.writeBytes(profissional.getNome());
			arquivo.writeBytes(profissional.getCpf());
			arquivo.writeBytes(profissional.getTelefone());
			arquivo.writeBytes(profissional.getDataNascimento());
			arquivo.writeBytes(profissional.getEndereco());
			arquivo.writeBytes(profissional.getProfissao());
			
			BaseDeDados.hashAdd("Profissional", id);
		}
	}

	public static void removerCliente(Integer id, TabelaHashDisco tabelaHash) throws FileNotFoundException, IOException {
		File arquivoOriginal = new File("clientes.dat");
		File arquivoTemp = new File("clientes.tmp.dat");

		try (RandomAccessFile arquivo = new RandomAccessFile(arquivoOriginal, "r");
				RandomAccessFile arquivoTempRa = new RandomAccessFile(arquivoTemp, "rw")) {

			while (arquivo.getFilePointer() < arquivo.length()) {
				int id1 = arquivo.readInt();
				String nome = lerString(arquivo, TAMANHO_NOME);
				String cpf = lerString(arquivo, TAMANHO_CPF);
				String telefone = lerString(arquivo, TAMANHO_TELEFONE);
				String dataNascimento = lerString(arquivo, TAMANHO_DATA_NASCIMENTO);
				String endereco = lerString(arquivo, TAMANHO_ENDERECO);

				// Se o ID não corresponder ao ID a ser removido, gravamos no arquivo temporário
				if (!id.equals(id1)) {
					arquivoTempRa.writeInt(id1);
					escreverCampo(arquivoTempRa, nome, TAMANHO_NOME);
					escreverCampo(arquivoTempRa, cpf, TAMANHO_CPF);
					escreverCampo(arquivoTempRa, telefone, TAMANHO_TELEFONE);
					escreverCampo(arquivoTempRa, dataNascimento, TAMANHO_DATA_NASCIMENTO);
					escreverCampo(arquivoTempRa, endereco, TAMANHO_ENDERECO);
				}
			}
			
			
		}

		if (!arquivoOriginal.delete()) {
			System.out.println("\nNão foi possível excluir o arquivo original.");
			return;
		}

		if (!arquivoTemp.renameTo(arquivoOriginal)) {
			System.out.println("\nNão foi possível renomear o arquivo temporário.");
		} else {
			System.out.println("\nRegistro excluído com sucesso!");
			tabelaHash.mapearClientesParaHash();
			Consultas.removerConsultas(id, 0);
		}
	}

	public static void removerProfissional(Integer id) throws FileNotFoundException, IOException {
		File arquivoOriginal = new File("profissionais.dat");
		File arquivoTemp = new File("profissionais.tmp.dat");

		try (RandomAccessFile arquivo = new RandomAccessFile(arquivoOriginal, "r");
				RandomAccessFile arquivoTempRa = new RandomAccessFile(arquivoTemp, "rw")) {

			while (arquivo.getFilePointer() < arquivo.length()) {
				int id1 = arquivo.readInt();
				String nome = lerString(arquivo, TAMANHO_NOME);
				String cpf = lerString(arquivo, TAMANHO_CPF);
				String telefone = lerString(arquivo, TAMANHO_TELEFONE);
				String dataNascimento = lerString(arquivo, TAMANHO_DATA_NASCIMENTO);
				String endereco = lerString(arquivo, TAMANHO_ENDERECO);
				String profissao = lerString(arquivo, TAMANHO_PROFISSAO);

				// Se o ID não corresponder ao ID a ser removido, gravamos no arquivo temporário
				if (!id.equals(id1)) {
					arquivoTempRa.writeInt(id1);
					escreverCampo(arquivoTempRa, nome, TAMANHO_NOME);
					escreverCampo(arquivoTempRa, cpf, TAMANHO_CPF);
					escreverCampo(arquivoTempRa, telefone, TAMANHO_TELEFONE);
					escreverCampo(arquivoTempRa, dataNascimento, TAMANHO_DATA_NASCIMENTO);
					escreverCampo(arquivoTempRa, endereco, TAMANHO_ENDERECO);
					escreverCampo(arquivoTempRa, profissao, TAMANHO_PROFISSAO);
				}
			}
		}

		if (!arquivoOriginal.delete()) {
			System.out.println("\nNão foi possível excluir o arquivo original.");
			return;
		}

		if (!arquivoTemp.renameTo(arquivoOriginal)) {
			System.out.println("\nNão foi possível renomear o arquivo temporário.");
		} else {
			System.out.println("\nRegistro excluído com sucesso!");
			BaseDeDados.hashRemove("Cliente", id);
			Consultas.removerConsultas(0, id);
		}
	}

	private static String lerString(RandomAccessFile raf, int tamanho) throws IOException {
		byte[] bytes = new byte[tamanho];
		raf.readFully(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

	private static void escreverCampo(RandomAccessFile dos, String campo, int tamanho) throws IOException {
		byte[] bytes = campo.getBytes(StandardCharsets.UTF_8);
		dos.write(bytes, 0, Math.min(tamanho, bytes.length));
		// Preencher o restante com espaços para garantir o tamanho fixo
		for (int i = bytes.length; i < tamanho; i++) {
			dos.writeByte(' ');
		}
	}
}