package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GeneralUsage {
	private static final String[] FIRST_NAMES = { "Jorge", "Joao", "Henrique", "Gustavo", "Guilherme", "Ornofre", "Ian",
			"Gabriel", "Cleiton", "Sergio", "Lucas", "Pedro", "Matheus", "Rafael", "Felipe", "Bruno", "Tiago",
			"Leonardo", "Daniel", "Carlos", "Vinicius", "Andre", "Marcelo", "Victor", "Ricardo", "Eduardo", "Fernando",
			"Roberto", "Alexandre", "Diego", "Antonio", "Murilo", "Leandro", "Rodrigo", "Marcos", "Wesley", "Fabio",
			"Douglas", "Igor", "Samuel", "Jose", "William", "Nathan", "Thiago", "Everton", "Kleber", "Elton", "Luis",
			"Alex", "Cristiano", "Francisco", "Marlon", "Brayan", "Alan", "Geovanni", "Julio", "Jair", "Erick", "Edson",
			"Ronaldo", "Caio", "Otavio", "Vitor", "Afonso", "Angelo", "Matias", "Artur", "Bernardo", "Davi", "Enzo",
			"Frederico", "Heitor", "Joaquim", "Lorenzo", "Miguel", "Noah", "Benjamin", "Vicente", "Nicolas", "Tomás",
			"Alberto", "Augusto", "Sandro", "Raul", "Renato", "Sandro", "Sandro", "Rafael", "Sebastião", "Teodoro",
			"Valentim", "Walter", "Xavier", "Yuri", "Zeca", "Cesar", "Enrico", "Geraldo", "Gilberto", "Jean" };

	private static final String[] LAST_NAMES = { "Silva", "Santos", "Oliveira", "Souza", "Pereira", "Costa",
			"Rodrigues", "Almeida", "Nascimento", "Lima", "Araújo", "Fernandes", "Carvalho", "Gomes", "Martins",
			"Rocha", "Ribeiro", "Alves", "Monteiro", "Mendes", "Barros", "Freitas", "Barbosa", "Pinto", "Correia",
			"Moreira", "Cardoso", "Teixeira", "Cavalcanti", "Dias", "Castro", "Campos", "Moura", "Peixoto", "Andrade",
			"Leal", "Vieira", "Santana", "Machado", "Duarte", "Ramos", "Freire", "Amaral", "Tavares", "Matos",
			"Azevedo", "Braga", "Cunha", "Farias", "Lopes", "Macedo", "Nogueira", "Reis", "Xavier", "Branco", "Fonseca",
			"Pacheco", "Neves", "Borges", "Siqueira", "Moraes", "Mello", "Guimaraes", "Figueiredo", "Sales", "Viana",
			"Monteiro", "Ferreira", "Vargas", "Vasconcelos", "Aguiar", "Soares", "Batista", "Parreira", "Campos",
			"Assis", "Domingues", "Aragao", "Bezerra", "Bittencourt", "Carmo", "Chaves", "Coelho", "Diniz", "Espindola",
			"Esteves", "Falcao", "Farias", "Franco", "Gama", "Garcia", "Henriques", "Lacerda", "Lourenco", "Magalhaes",
			"Medeiros", "Meireles", "Meneses", "Mesquita", "Miranda" };

	private static final String[] PROFISSOES = { "Dentista", "Assistente Odontologico", "Recepcionista", "Gerente",
			"Faxineiro" };

	private static final Random RANDOM = new Random();

	public static LocalDate converterParaHorario(String hora, String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return LocalDate.parse(data + " " + hora, formatter);
	}

	private static int calcularDigito(int[] cpf, int tamanho) {
		int soma = 0;
		int multiplicador = 2;

		for (int i = tamanho - 1; i >= 0; i--) {
			soma += cpf[i] * multiplicador;
			multiplicador = (multiplicador == 9) ? 2 : multiplicador + 1;
		}

		int resto = soma % 11;
		return (resto < 2) ? 0 : 11 - resto;
	}

	public static String gerarCPF() {
		int[] cpf_numeros = new int[9];
		Random random = new Random();

		for (int i = 0; i < 9; i++) {
			cpf_numeros[i] = random.nextInt(10);
		}

		int digito1 = calcularDigito(cpf_numeros, 9);
		cpf_numeros = Arrays.copyOf(cpf_numeros, 10);
		cpf_numeros[9] = digito1;
		int digito2 = calcularDigito(cpf_numeros, 10);

		return String.format("%d%d%d.%d%d%d.%d%d%d-%d%d", cpf_numeros[0], cpf_numeros[1], cpf_numeros[2],
				cpf_numeros[3], cpf_numeros[4], cpf_numeros[5], cpf_numeros[6], cpf_numeros[7], cpf_numeros[8], digito1,
				digito2);
	}

	public static String gerarTelefone() {
		int ddd = 11 + RANDOM.nextInt(89);
		int num = RANDOM.nextInt(100000000);

		return String.format("%02d 9%04d-%04d", ddd, num / 10000, num % 10000);
	}

	public static String gerarDataNascimento(boolean dentista) {
		LocalDate hoje = LocalDate.now();
		int anoMax = dentista ? hoje.getYear() - 18 : hoje.getYear();
		int anoMin = dentista ? anoMax - 65 : anoMax - 100;

		int ano = RANDOM.nextInt(anoMax - anoMin + 1) + anoMin;
		int mes = RANDOM.nextInt(12) + 1;
		int dia;

		switch (mes) {
		case 2:
			dia = RANDOM.nextInt((ano % 4 == 0 ? 29 : 28)) + 1;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			dia = RANDOM.nextInt(30) + 1;
			break;
		default:
			dia = RANDOM.nextInt(31) + 1;
		}

		return String.format("%02d/%02d/%04d", dia, mes, ano);
	}

	public static String gerarEndereco() {
		String[] tiposEndereco = { "Rua", "Avenida", "Praça", "Travessa", "Alameda", "Largo", "Estrada", "Rodovia",
				"Boulevard", "Caminho" };

		String[] complementosEndereco = { "Casa", "Apartamento", "Sala", "Cobertura", "Andar", "Bloco", "Prédio",
				"Lote", "Chácara", "Sítio" };

		String[] cidades = { "São Paulo", "Rio de Janeiro", "Belo Horizonte", "Salvador", "Porto Alegre", "Curitiba",
				"Brasília", "Fortaleza", "Recife", "Manaus" };

		String[] estados = { "SP", "RJ", "MG", "BA", "RS", "PR", "DF", "CE", "PE", "AM" };

		int tipoIndex = RANDOM.nextInt(tiposEndereco.length);
		int complementoIndex = RANDOM.nextInt(complementosEndereco.length);
		int cidadeIndex = RANDOM.nextInt(cidades.length);
		int estadoIndex = RANDOM.nextInt(estados.length);

		int numero = RANDOM.nextInt(9999) + 1;
		int cep = RANDOM.nextInt(90000) + 10000;

		return String.format("%s %d, %s, %s, %s, %s, %05d", tiposEndereco[tipoIndex], numero,
				complementosEndereco[complementoIndex], cidades[cidadeIndex], estados[estadoIndex], "Brasil", cep);
	}

	public static String gerarNome() {
		String primeiroNome = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
		String sobrenome = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];

		return primeiroNome + " " + sobrenome;
	}

	public static String gerarProfissao() {
		int[] pesos = { 30, 90, 95, 96, 100 };

		int valorAleatorio = RANDOM.nextInt(100) + 1;

		for (int i = 0; i < pesos.length; i++) {
			if (valorAleatorio <= pesos[i]) {
				return PROFISSOES[i];
			}
		}

		return null;
	}

	public static Date obterDataInicial() {
		Calendar cal = Calendar.getInstance();
		// Define a data inicial como o início do dia de hoje
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date obterDataFinal() {
		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.MONTH, 1); // Adiciona um mês à data atual
		calendario.set(Calendar.HOUR_OF_DAY, 23);
		calendario.set(Calendar.MINUTE, 59);
		calendario.set(Calendar.SECOND, 59);
		return calendario.getTime();
	}

	public static void ClearConsole() {
		for (int i = 0; i < 20; i++) {
			System.out.println();
		}
	}
}