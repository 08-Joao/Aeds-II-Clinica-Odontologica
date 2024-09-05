package entities;

// Importa classes necessárias para manipulação de arquivos, datas e listas
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import Utils.GeneralUsage;

public class BaseDeDados {

	// Tamanhos dos campos para garantir que os registros tenham tamanhos fixos
	private static final int TAMANHO_ID = 4; // int
	private static final int TAMANHO_NOME = 100;
	private static final int TAMANHO_CPF = 15;
	private static final int TAMANHO_TELEFONE = 14;
	private static final int TAMANHO_DATA_NASCIMENTO = 10;
	private static final int TAMANHO_ENDERECO = 100;
	private static final int TAMANHO_PROFISSAO = 50;

	 // Define o tamanho total dos registros de clientes e profissionais
	private static final int TAMANHO_REGISTRO_CLIENTE = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE
			+ TAMANHO_DATA_NASCIMENTO + TAMANHO_ENDERECO;
	private static final int TAMANHO_REGISTRO_PROFISSIONAL = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE
			+ TAMANHO_DATA_NASCIMENTO + TAMANHO_ENDERECO + TAMANHO_PROFISSAO;

	// Define o tamanho de registros de horários e lista de agendamentos
	private static final int TAMANHO_HORARIO = 8;
	private static final int TAMANHO_LISTA_AGENDADOS = 8;
	private static final int TAMANHO_TOTAL_AGENDAMENTOS = TAMANHO_LISTA_AGENDADOS * 10;
	private static final int TAMANHO_REGISTRO_HORARIO = TAMANHO_HORARIO + TAMANHO_TOTAL_AGENDAMENTOS;

	// Caminhos dos arquivos para armazenar dados de clientes, profissionais e horários
	private static final String CAMINHO_CLIENTES = "clientes.dat";
	private static final String CAMINHO_PROFISSIONAIS = "profissionais.dat";
	private static final String CAMINHO_HORARIOS = "horarios.dat";

	// Cria bases de dados desordenadas para clientes ou profissionais
	public static void criarBasesDesordenada(String type, int size) {
		List<Cliente> clientes = new ArrayList<>();
		List<Profissional> profissionais = new ArrayList<>();
		Random random = new Random();

		// Se o tipo for "Cliente", gera registros desordenados de clientes
		if (type.compareToIgnoreCase("Cliente") == 0) {
			for (int i = 1; i <= size; i++) {
				Cliente cliente = new Cliente(i, GeneralUsage.gerarNome(), GeneralUsage.gerarCPF(),
						GeneralUsage.gerarTelefone(), GeneralUsage.gerarDataNascimento(false),
						GeneralUsage.gerarEndereco()); // Cria um cliente
				clientes.add(cliente);
			}

			Collections.shuffle(clientes); // Embaralha a lista de clientes

			try (DataOutputStream dosClientes = new DataOutputStream(new FileOutputStream(CAMINHO_CLIENTES))) {
				for (Cliente cliente : clientes) {
					escreverRegistroCliente(dosClientes, cliente);
				}
			} catch (IOException e) {
				System.err.println("Erro ao manipular o arquivo de clientes: " + e.getMessage());
			}
		// Se o tipo for "Profissional", gera registros desordenados de profissionais
		} else if (type.compareToIgnoreCase("Profissional") == 0) {
			for (int i = 1; i <= size; i++) {
				Profissional profissional = new Profissional(i, GeneralUsage.gerarNome(), GeneralUsage.gerarCPF(),
						GeneralUsage.gerarTelefone(), GeneralUsage.gerarDataNascimento(true),
						GeneralUsage.gerarEndereco(), GeneralUsage.gerarProfissao()); // Cria um profissional
				profissionais.add(profissional);
			}

			Collections.shuffle(profissionais); // Embaralha a lista de profissionais

			try (DataOutputStream dosProfissionais = new DataOutputStream(
					new FileOutputStream(CAMINHO_PROFISSIONAIS))) {
				for (Profissional profissional : profissionais) {
					escreverRegistroProfissional(dosProfissionais, profissional);
				}
			} catch (IOException e) {
				System.err.println("Erro ao manipular o arquivo de profissionais: " + e.getMessage());
			}
		}
	}

	// Cria uma base de horários desordenada
	public static void criarBaseHorariosDesordenada() throws FileNotFoundException, IOException {
		List<Horario> horarios = new ArrayList<>();
		Date dataInicial = GeneralUsage.obterDataInicial();
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(dataInicial);

		// Preenche a lista de horários com intervalos de 30 minutos em dias úteis
		while (calendario.getTime().before(GeneralUsage.obterDataFinal())) {
			if (calendario.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& calendario.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				Calendar horaAtual = (Calendar) calendario.clone();
				horaAtual.set(Calendar.HOUR_OF_DAY, 8);
				horaAtual.set(Calendar.MINUTE, 0);

				while (horaAtual.get(Calendar.HOUR_OF_DAY) < 18) {
					// Cria um novo objeto Horario para cada intervalo de 30 minutos
					Horario horarioObj = new Horario(horaAtual.getTime());

					// Verifica se o horário já foi adicionado
					if (!horarios.contains(horarioObj)) {
						horarios.add(horarioObj);
					}

					horaAtual.add(Calendar.MINUTE, 30); // Incrementa 30 minutos
				}
			}
			calendario.add(Calendar.DAY_OF_MONTH, 1); // Move para o próximo dia
		}

		Collections.shuffle(horarios); // Embaralha a lista de horários

		try (DataOutputStream dosHorarios = new DataOutputStream(new FileOutputStream(CAMINHO_HORARIOS))) {
			for (Horario horario : horarios) {
				escreverRegistroHorario(dosHorarios, horario);
			}
		} catch (IOException e) {
			System.err.println("Erro ao criar base de horários: " + e.getMessage());
		}
	}

	// Função para escrever um cliente em um arquivo com tamanho fixo
	public static void escreverRegistroCliente(DataOutputStream dos, Cliente cliente) throws IOException {
		dos.writeInt(cliente.getId());
		dos.write(cliente.getNome().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_NOME, cliente.getNome().length()));
		dos.write(cliente.getCpf().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_CPF, cliente.getCpf().length()));
		dos.write(cliente.getTelefone().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_TELEFONE, cliente.getTelefone().length()));
		dos.write(cliente.getDataNascimento().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_DATA_NASCIMENTO, cliente.getDataNascimento().length()));
		dos.write(cliente.getEndereco().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_ENDERECO, cliente.getEndereco().length()));

		int bytesEscritos = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE + TAMANHO_DATA_NASCIMENTO
				+ TAMANHO_ENDERECO;
		for (int i = bytesEscritos; i < TAMANHO_REGISTRO_CLIENTE; i++) {
			dos.writeByte(' ');
		}
	}

	// Função para escrever um profissional em um arquivo com tamanho fixo
	public static void escreverRegistroProfissional(DataOutputStream dos, Profissional profissional)
			throws IOException {
		dos.writeInt(profissional.getId());
		dos.write(profissional.getNome().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_NOME, profissional.getNome().length()));
		dos.write(profissional.getCpf().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_CPF, profissional.getCpf().length()));
		dos.write(profissional.getTelefone().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_TELEFONE, profissional.getTelefone().length()));
		dos.write(profissional.getDataNascimento().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_DATA_NASCIMENTO, profissional.getDataNascimento().length()));
		dos.write(profissional.getEndereco().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_ENDERECO, profissional.getEndereco().length()));
		dos.write(profissional.getProfissao().getBytes(StandardCharsets.UTF_8), 0,
				Math.min(TAMANHO_PROFISSAO, profissional.getProfissao().length()));

		int bytesEscritos = TAMANHO_ID + TAMANHO_NOME + TAMANHO_CPF + TAMANHO_TELEFONE + TAMANHO_DATA_NASCIMENTO
				+ TAMANHO_ENDERECO + TAMANHO_PROFISSAO;
		for (int i = bytesEscritos; i < TAMANHO_REGISTRO_PROFISSIONAL; i++) {
			dos.writeByte(' ');
		}
	}

	private static Cliente lerRegistroCliente(DataInputStream dis) throws IOException {
		int id = dis.readInt();
		String nome = lerCampo(dis, TAMANHO_NOME);
		String cpf = lerCampo(dis, TAMANHO_CPF);
		String telefone = lerCampo(dis, TAMANHO_TELEFONE);
		String dataNascimento = lerCampo(dis, TAMANHO_DATA_NASCIMENTO);
		String endereco = lerCampo(dis, TAMANHO_ENDERECO);
		return new Cliente(id, nome, cpf, telefone, dataNascimento, endereco);
	}

	private static Profissional lerRegistroProfissional(DataInputStream dis) throws IOException {
		int id = dis.readInt();
		String nome = lerCampo(dis, TAMANHO_NOME);
		String cpf = lerCampo(dis, TAMANHO_CPF);
		String telefone = lerCampo(dis, TAMANHO_TELEFONE);
		String dataNascimento = lerCampo(dis, TAMANHO_DATA_NASCIMENTO);
		String endereco = lerCampo(dis, TAMANHO_ENDERECO);
		String profissao = lerCampo(dis, TAMANHO_PROFISSAO);
		return new Profissional(id, nome, cpf, telefone, dataNascimento, endereco, profissao);
	}

	public static void imprimirBaseDeDados(String type) {

		if (type.compareToIgnoreCase("Cliente") == 0) {
			System.out.println("\n\n\nImprimindo Base dos Clientes:");
			try (DataInputStream disClientes = new DataInputStream(new FileInputStream(CAMINHO_CLIENTES))) {
				while (disClientes.available() > 0) {
					Cliente cliente = lerRegistroCliente(disClientes);
					cliente.imprimeInformacao();
				}
			} catch (IOException e) {
				System.err.println("Erro ao manipular o arquivo de clientes: " + e.getMessage());
			}

		} else if (type.compareToIgnoreCase("Profissional") == 0) {
			System.out.println("\n\n\nImprimindo Base dos Profissionais:");
			try (DataInputStream disProfissionais = new DataInputStream(new FileInputStream(CAMINHO_PROFISSIONAIS))) {
				while (disProfissionais.available() > 0) {
					Profissional profissional = lerRegistroProfissional(disProfissionais);
					profissional.imprimeInformacao();
				}
			} catch (IOException e) {
				System.err.println("Erro ao manipular o arquivo de profissionais: " + e.getMessage());
			}
		}
	}

	public static void escreverRegistroHorario(DataOutputStream dos, Horario horario) throws IOException {
		dos.writeLong(horario.getHorario().getTime());
		List<ListaAgendados> agendamentos = horario.getAgendamentos();
		for (ListaAgendados agendamento : agendamentos) {
			dos.writeInt(agendamento.getId_cliente());
			dos.writeInt(agendamento.getId_profissional());
		}
	}

	private static Horario lerRegistroHorario(DataInputStream dis) throws IOException {
		long horarioMillis = dis.readLong();
		Date horario = new Date(horarioMillis);
		Horario horarioObj = new Horario(horario);

		// Inicializa a lista de agendamentos com tamanho fixo
		List<ListaAgendados> agendamentos = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			int idCliente = dis.readInt();
			int idProfissional = dis.readInt();
			ListaAgendados agendamento = new ListaAgendados(idCliente, idProfissional);
			agendamentos.add(agendamento);
		}
		horarioObj.setAgendamentos(agendamentos);

		return horarioObj;
	}

	public static void imprimirBaseDeHorarios() {
		System.out.println("\n\n\nImprimindo Base de Horários:");
		try (DataInputStream disHorarios = new DataInputStream(new FileInputStream(CAMINHO_HORARIOS))) {
			while (disHorarios.available() > 0) {
				Horario horario = lerRegistroHorario(disHorarios);
				horario.imprimeInformacao();
			}
		} catch (IOException e) {
			System.err.println("Erro ao manipular o arquivo de horários:" + e.getMessage());
		}
	}

	private static String lerCampo(DataInputStream dis, int tamanhoCampo) throws IOException {
		byte[] buffer = new byte[tamanhoCampo];
		dis.readFully(buffer);
		return new String(buffer, StandardCharsets.UTF_8).trim();
	}
}
