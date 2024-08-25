package Utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;

import entities.Cliente;
import entities.Horario;
import entities.ListaAgendados;
import entities.Profissional;

public class Consultas {

	private static final int TAMANHO_HORARIO = 8;
	private static final int TAMANHO_LISTA_AGENDADOS = 8;
	private static final int TAMANHO_TOTAL_AGENDAMENTOS = TAMANHO_LISTA_AGENDADOS * 10;
	private static final int TAMANHO_REGISTRO_HORARIO = TAMANHO_HORARIO + TAMANHO_TOTAL_AGENDAMENTOS;

	private static final String CAMINHO_CLIENTES = "clientes.dat";
	private static final String CAMINHO_PROFISSIONAIS = "profissionais.dat";
	private static final String CAMINHO_HORARIOS = "horarios.dat";

	private static final String ARQUIVO_TEMPORARIO = "horarios_temp.dat";

	public static void agendarConsulta(Date horario, int idCliente, int idProfissional)
			throws FileNotFoundException, IOException {
		Horario horarioExistente = Busca.binariaHorario(horario);
		Profissional profissional = Busca.binariaProfissional(idProfissional);
		Cliente cliente = Busca.binariaCliente(idCliente);

		if (profissional == null || cliente == null) {
			System.out.println("Cliente ou Profissional inválidos.");
			return;
		}

		if (!profissional.getProfissao().trim().equalsIgnoreCase("Dentista")) {
			System.out.println("Profissional selecionado não é um dentista.");
			System.out.println(profissional.getProfissao().trim());
			return;
		}

		if (horarioExistente != null) {
			ListaAgendados novoAgendamento = new ListaAgendados();
			novoAgendamento.setId_cliente(idCliente);
			novoAgendamento.setId_profissional(idProfissional);

			boolean clienteJaAgendado = false;
			boolean profissionalJaAgendado = false;
			boolean espacoDisponivel = false;
			int posicaoEspacoDisponivel = -1;

			for (int i = 0; i < horarioExistente.getAgendamentos().size(); i++) {
				ListaAgendados agendamento = horarioExistente.getAgendamentos().get(i);

				if (agendamento.getId_cliente() == 0 && agendamento.getId_profissional() == 0) {
					espacoDisponivel = true;
					posicaoEspacoDisponivel = i;
				}
				if (agendamento.getId_cliente() == idCliente && agendamento.getId_profissional() == idProfissional) {
					System.out.println("Cliente e Profissional já agendados para esse horário");
					return;
				} else if (agendamento.getId_cliente() == idCliente) {
					clienteJaAgendado = true;
				} else if (agendamento.getId_profissional() == idProfissional) {
					profissionalJaAgendado = true;
				}
			}

			if (clienteJaAgendado) {
				System.out.println("Cliente já agendado para esse horário com outro profissional");
				return;
			}

			if (profissionalJaAgendado) {
				System.out.println("Profissional já agendado para esse horário com outro cliente");
				return;
			}

			if (espacoDisponivel) {
				horarioExistente.getAgendamentos().set(posicaoEspacoDisponivel, novoAgendamento);
			} else {
				horarioExistente.getAgendamentos().add(novoAgendamento);
			}

			System.out.println("Agendamento realizado com sucesso!");
			horarioExistente.imprimeInformacao();
			salvarHorario(horarioExistente);

		} else {
			System.out.println("Horário Inválido.");
		}
	}

	public static void removerConsulta(Date horario, Integer idCliente, Integer idProfissional)
			throws FileNotFoundException, IOException {
		Horario tempHorario = Busca.binariaHorario(horario);

		if (tempHorario != null) {
			ListaAgendados novoAgendamento = new ListaAgendados();
			novoAgendamento.setId_cliente(0);
			novoAgendamento.setId_profissional(0);

			for (int i = 0; i < tempHorario.getAgendamentos().size(); i++) {
				if (idCliente != 0) {
					if (tempHorario.getAgendamentos().get(i).getId_cliente() == idCliente) {
						tempHorario.getAgendamentos().set(i, novoAgendamento);
						break;
					}
				} else if (idProfissional != 0) {
					if (tempHorario.getAgendamentos().get(i).getId_profissional() == idProfissional) {
						tempHorario.getAgendamentos().set(i, novoAgendamento);
						break;
					}
				}

			}

			salvarHorario(tempHorario);
		} else {
			System.out.println("Horário Inválido.");
		}

	}

	public static void removerConsultas(Integer idCliente, Integer idProfissional) throws IOException {


		try (DataInputStream dis = new DataInputStream(new FileInputStream(CAMINHO_HORARIOS))) {
			while (dis.available() > 0) {
				long horarioLido = dis.readLong();
				Date horario = new Date(horarioLido);

				if (idCliente != 0) {
					removerConsulta(horario, idCliente, 0);
				} else if (idProfissional != 0) {
					removerConsulta(horario, 0, idProfissional);
				}

				dis.skipBytes(TAMANHO_TOTAL_AGENDAMENTOS);
			}
		}
	}

	private static void salvarHorario(Horario horario) throws FileNotFoundException, IOException {
		String arquivoTemp = "temporario.dat";

		try (RandomAccessFile rafOriginal = new RandomAccessFile(CAMINHO_HORARIOS, "r");
				RandomAccessFile rafTemp = new RandomAccessFile(arquivoTemp, "rw")) {

			byte[] buffer = new byte[TAMANHO_REGISTRO_HORARIO];

			while (rafOriginal.read(buffer) != -1) {
				Horario tempHorario = lerHorarioDoBuffer(buffer);

				if (tempHorario.getHorario().equals(horario.getHorario())) {
					escreverHorarioNoBuffer(horario, buffer);
					rafTemp.write(buffer);
				} else {
					//
					rafTemp.write(buffer);
				}
			}
		}

		try (RandomAccessFile rafOriginal = new RandomAccessFile(CAMINHO_HORARIOS, "rw");
				RandomAccessFile rafTemp = new RandomAccessFile(arquivoTemp, "r")) {

			byte[] buffer = new byte[TAMANHO_REGISTRO_HORARIO];
			while (rafTemp.read(buffer) != -1) {
				rafOriginal.write(buffer);
			}
		}

		File tempFile = new File(arquivoTemp);
		if (!tempFile.delete()) {
			System.out.println("Não foi possível excluir o arquivo temporário.");
		}
	}

	private static Horario lerHorarioDoBuffer(byte[] buffer) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		long horarioMillis = byteBuffer.getLong();
		Horario horario = new Horario(new Date(horarioMillis));

		for (int i = 0; i < 10; i++) {
			int idCliente = byteBuffer.getInt();
			int idProfissional = byteBuffer.getInt();
			ListaAgendados agendamento = new ListaAgendados();
			agendamento.setId_cliente(idCliente);
			agendamento.setId_profissional(idProfissional);
			horario.getAgendamentos().add(agendamento);
		}

		return horario;
	}

	private static void escreverHorarioNoBuffer(Horario horario, byte[] buffer) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		byteBuffer.putLong(horario.getHorario().getTime());

		int maxAgendamentos = 10;

		for (int i = 0; i < maxAgendamentos; i++) {
			if (i < horario.getAgendamentos().size()) {
				ListaAgendados agendamento = horario.getAgendamentos().get(i);
				byteBuffer.putInt(agendamento.getId_cliente());
				byteBuffer.putInt(agendamento.getId_profissional());
			} else {
				byteBuffer.putInt(0);
				byteBuffer.putInt(0);
			}
		}
	}

}
