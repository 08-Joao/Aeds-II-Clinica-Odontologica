//Feito por: 
//	Joao Victor Vieira Amora de Figueiredo 23.1.8019
//	Henrique Angelo Duarte Alves 23.1.8028
//Turma de Sistemas de Informação

package aplication;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Utils.Busca;
import Utils.Consultas;
import Utils.GeneralUsage;
import Utils.Gerenciamento;
import Utils.OrdenacaoHorarios;
import Utils.OrdenacaoPessoas;
import entities.BaseDeDados;
import entities.Cliente;
import entities.Horario;
import entities.Profissional;

public class Main {

    public static void main(String[] args) {
        try {
            // Definindo os caminhos dos arquivos de dados (clientes, profissionais, horários)
            String caminhoClientes = "clientes.dat";
            String caminhoProfissionais = "profissionais.dat";
            String caminhoHorarios = "horarios.dat";

            // Inicializando objetos File para os arquivos
            File arquivoClientes = new File(caminhoClientes);
            File arquivoProfissionais = new File(caminhoProfissionais);
            File arquivoHorario = new File(caminhoHorarios);

            // Flags que indicam se as bases já foram ordenadas
            Boolean ordenadoCliente = false;
            Boolean ordenadoProfissional = false;
            Boolean ordenadoHorario = false;

            // Verifica se os arquivos de dados existem, caso contrário, cria bases de dados desordenadas
            if (!arquivoClientes.exists()) {
                BaseDeDados.criarBasesDesordenada("Cliente", 100000);
            }

            if (!arquivoProfissionais.exists()) {
                BaseDeDados.criarBasesDesordenada("Profissional", 500);
            }

            if (!arquivoHorario.exists()) {
                BaseDeDados.criarBaseHorariosDesordenada();
            }

            Scanner sc = new Scanner(System.in); // Inicializa o Scanner para receber entradas do usuário
            int choice = 1000;

            // Loop do menu principal
            do {
                GeneralUsage.ClearConsole(); // Limpa a tela e exibe o menu principal
                System.out.println("--------------------------- MENU ---------------------------");
                System.out.println("1. Ordenar Base");
                System.out.println("2. Fazer Busca");
                System.out.println("3. Adicionar Pessoa");
                System.out.println("4. Remover Pessoa");
                System.out.println("5. Agendar Consulta");
                System.out.println("6. Desmarcar Consulta");
                System.out.println("7. Imprimir Base");
                System.out.println("0. Sair");
                System.out.println("-------------------------- SAIDA --------------------------");
                choice = sc.nextInt(); // Lê a escolha do usuário

                // Estrutura switch para lidar com a escolha do menu
                switch (choice) {
                    case 1:
                        int choice01 = 3;
                        do {
                            GeneralUsage.ClearConsole();
                            System.out.println("----------------- Tipo de Ordenação -----------------");
                            System.out.println("1. Ordenação Normal");
                            System.out.println("2. Ordenação Natural");
                            System.out.println("3. Voltar");
                            System.out.println("----------------- Tipo de Ordenação -----------------");
                            choice01 = sc.nextInt();
                            
                            int choice1 = 5;
                            if(choice01 == 3) {
                            	break;
                            }
                            do {
                                // Menu de ordenação
                                GeneralUsage.ClearConsole();
                                System.out.println("----------------- Ordenação -----------------");
                                System.out.println("Qual base deseja ordenar?");
                                System.out.println("1. Base dos Clientes");
                                System.out.println("2. Base dos Profissionais");
                                System.out.println("3. Base dos Horários");
                                System.out.println("4. Todas as Bases");
                                System.out.println("5. Voltar");
                                System.out.println("----------------- Ordenação -----------------");
                                choice1 = sc.nextInt();

                                // Realiza a ordenação com base na escolha do usuário
                                switch (choice1) {
                                    case 1: // Ordenação das bases
                                        System.out.println("Ordenando base de clientes...");
                                        switch(choice01) {
                                            case 1:
                                                OrdenacaoPessoas.ordenarClientes();
                                                break;
                                            case 2:
                                                OrdenacaoPessoas.ordenarClientesNatural();
                                                break;
                                            default:
                                                System.out.println("Escolha inválida...");
                                                break;
                                        }
                                        ordenadoCliente = true;
                                        break;
                                    case 2:
                                        System.out.println("Ordenando base de profissionais...");
                                        switch(choice01) {
                                            case 1:
                                                OrdenacaoPessoas.ordenarProfissionais();
                                                break;
                                            case 2:
                                                OrdenacaoPessoas.ordenarProfissionaisNatural();
                                                break;
                                            default:
                                                System.out.println("Escolha inválida...");
                                                break;
                                        }
                                        ordenadoProfissional = true;
                                        break;
                                    case 3:
                                        System.out.println("Ordenando base de horários...");
                                        switch(choice01) {
                                            case 1:
                                                OrdenacaoHorarios.ordenarBaseHorarios();
                                                break;
                                            case 2:
                                                OrdenacaoHorarios.ordenarBaseHorariosNatural();
                                                break;
                                            default:
                                                System.out.println("Escolha inválida...");
                                                break;
                                        }
                                        ordenadoHorario = true;
                                        break;
                                    case 4:
                                        // Ordena todas as bases
                                        System.out.println("Ordenando todas as bases de dados...");
                                        switch(choice01) {
                                            case 1:
                                                OrdenacaoPessoas.ordenarClientes();
                                                OrdenacaoPessoas.ordenarProfissionais();
                                                OrdenacaoHorarios.ordenarBaseHorarios();
                                                break;
                                            case 2:
                                                OrdenacaoPessoas.ordenarClientesNatural();
                                                OrdenacaoPessoas.ordenarProfissionaisNatural();
                                                OrdenacaoHorarios.ordenarBaseHorariosNatural();
                                                break;
                                            default:
                                                System.out.println("Escolha inválida...");
                                                break;
                                        }
                                        // Atualiza as flags indicando que as bases estão ordenadas
                                        ordenadoCliente = true;
                                        ordenadoProfissional = true;
                                        ordenadoHorario = true;
                                        break;
                                    case 5:
                                        System.out.println("Retornando ao menu inicial.");
                                        break;
                                    default:
                                        System.out.println("Escolha inválida...");
                                        break;
                                }
                            } while (choice1 != 5);
                        } while(choice01 != 3);
                        
                        break;
                    case 2: // Busca nas bases
                        int choice2 = 4;
                        do {
                            GeneralUsage.ClearConsole();
                            System.out.println("----------------- Busca -----------------");
                            System.out.println("1. Busca Sequencial");
                            System.out.println("2. Busca Binaria");
                            System.out.println("3. Voltar");
                            System.out.println("----------------- Busca -----------------");
                            choice2 = sc.nextInt();

                            if (choice2 == 1 || choice2 == 2) {
                                int choice2_2 = 4;

                                do {
                                    GeneralUsage.ClearConsole();
                                    System.out.println("----------------- Busca -----------------");
                                    System.out.println("1. Buscar um Cliente");
                                    System.out.println("2. Buscar um Profissional");
                                    System.out.println("3. Buscar um Horário");
                                    System.out.println("4. Voltar");
                                    System.out.println("----------------- Busca -----------------");
                                    choice2_2 = sc.nextInt();

                                    switch (choice2_2) {
                                        case 1: // Busca de cliente
                                            System.out.println("Informe o ID do Cliente a ser procurado: ");
                                            int client_id = sc.nextInt();
                                            Cliente tempClie;

                                            if (choice2 == 1) {
                                                tempClie = Busca.sequencialCliente(client_id);
                                            } else {
                                                if (!ordenadoCliente) {
                                                    OrdenacaoPessoas.ordenarClientes();
                                                }
                                                tempClie = Busca.binariaCliente(client_id);
                                            }

                                            if (tempClie != null) {
                                                GeneralUsage.ClearConsole();
                                                System.out.println("Cliente encontrado: ");
                                                tempClie.imprimeInformacao();
                                            } else {
                                                GeneralUsage.ClearConsole();
                                                System.out.println("Cliente não encontrado.");
                                            }

                                            break;

                                        case 2: // Busca de profissional
                                            System.out.println("Informe o ID do Profissional a ser procurado: ");
                                            int prof_id = sc.nextInt();
                                            Profissional tempProf;

                                            if (choice2 == 1) {
                                                tempProf = Busca.sequencialProfissional(prof_id);
                                            } else {
                                                if (!ordenadoProfissional) {
                                                    OrdenacaoPessoas.ordenarProfissionais();
                                                }
                                                tempProf = Busca.binariaProfissional(prof_id);
                                            }

                                            if (tempProf != null) {
                                                GeneralUsage.ClearConsole();
                                                System.out.println("Profissional encontrado: ");
                                                tempProf.imprimeInformacao();
                                            } else {
                                                GeneralUsage.ClearConsole();
                                                System.out.println("Profissional não encontrado.");
                                            }

                                            break;

                                        case 3: // Busca de horário
                                            sc.nextLine(); // Limpa o buffer do scanner
                                            System.out.println("Informe a hora no formato dd/MM/yyyy HH:mm;");
                                            String data = sc.nextLine();

                                            try {
                                                // Converte a string para um objeto Date
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                                Date horario = sdf.parse(data);
                                                System.out.println("Horario Pesquisado: " + horario);

                                                Horario tempHorario;

                                                if (choice2 == 1) {
                                                    tempHorario = Busca.sequencialHorario(horario);
                                                } else {
                                                    if (!ordenadoHorario) {
                                                        OrdenacaoHorarios.ordenarBaseHorarios();
                                                    }
                                                    tempHorario = Busca.binariaHorario(horario);
                                                }

                                                if (tempHorario != null) {
                                                    System.out.println("Horário encontrado: ");
                                                    tempHorario.imprimeInformacao();
                                                } else {
                                                    System.out.println("Horário não encontrado.");
                                                }

                                            } catch (ParseException e) {
                                                System.out.println("Formato de data inválido. Use dd/MM/yyyy HH:mm");
                                            }

                                            break;

                                        case 4:
                                            System.out.println("Voltando ao menu de busca.");
                                            break;

                                        default:
                                            System.out.println("Escolha inválida.");
                                            break;
                                    }
                                } while (choice2_2 != 4);
                            } else if (choice2 != 3) {
                                System.out.println("Entrada inválida.");
                            }

                        } while (choice2 != 3);

                        break;

                    case 3: // Adicionar pessoa
                    	int choice3 = 3;
    					do {
    						GeneralUsage.ClearConsole();
    						System.out.println("----------------- Adicionar Pessoa -----------------");
    						System.out.println("1. Adicionar Cliente");
    						System.out.println("2. Adicionar Profissional");
    						System.out.println("3. Voltar");
    						System.out.println("----------------- Adicionar Pessoa -----------------");

    						choice3 = sc.nextInt();
    						switch (choice3) {
    						case 1:
    							// Adiciona um novo cliente
    							Gerenciamento.adicionarCliente();
    							break;
    						case 2:
    							// Adiciona um novo profissional
    							Gerenciamento.adicionarProfissional();
    							break;
    						case 3:
    							System.out.println("Retornando ao menu inicial.");
    							break;
    						default:
    							System.out.println("Escolha inválida...");
    						}
    					} while (choice3 != 3);
                        break;

                    case 4: // Remover pessoa
                    	int choice4 = 3;
    					do {
    						GeneralUsage.ClearConsole();
    						System.out.println("----------------- Remover Pessoa -----------------");
    						System.out.println("1. Remover Cliente");
    						System.out.println("2. Remover Profissional");
    						System.out.println("3. Voltar");
    						System.out.println("----------------- Remover Pessoa -----------------");
    						choice4 = sc.nextInt();

    						switch (choice4) {
    						case 1:
    							// Remove um cliente
    							System.out.println("Informe o ID do Cliente a ser removido; ");
    							int rem_clie = sc.nextInt();

    							Gerenciamento.removerCliente(rem_clie);
    							break;
    						case 2:
    							// Remove um profissional
    							System.out.println("Informe o ID do Profissional a ser removido; ");
    							int rem_prof = sc.nextInt();

    							Gerenciamento.removerProfissional(rem_prof);
    							break;
    						case 3:
    							System.out.println("Retornando ao menu inicial.");
    							break;
    						default:
    							System.out.println("Escolha inválida...");
    						}
    					} while (choice4 != 3);
                        break;

                    case 5: // Agendar consulta
                    	GeneralUsage.ClearConsole();
    					System.out.println("Informe a hora no formato dd/MM/yyyy HH:mm;");
    					String data = sc.nextLine();
    					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    					Date horario = sdf.parse(data);

    					System.out.println("Informe o ID do Cliente a consultar; ");
    					Integer id_clie = sc.nextInt();

    					System.out.println("Informe o ID do Profissional a realizar a consulta; ");
    					Integer id_prof = sc.nextInt();

    					Consultas.agendarConsulta(horario, id_clie, id_prof);
                        break;

                    case 6: // Desmarcar consulta
                    	GeneralUsage.ClearConsole();

    					System.out.println("Informe a hora no formato dd/MM/yyyy HH:mm;");
    					String data2 = sc.nextLine();
    					SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    					Date horario2 = sdf2.parse(data2);

    					System.out.println("Informe o ID do Cliente a desmarcar a consultar; ");
    					Integer id_clie2 = sc.nextInt();

    					Consultas.removerConsulta(horario2, id_clie2, 0);
                        break;

                    case 7: // Imprimir base
                    	int choice7 = 4;
    					GeneralUsage.ClearConsole();
    					do {

    						System.out.println("----------------- Impressão -----------------");
    						System.out.println("Qual base deseja imprimir?");
    						System.out.println("1. Base dos Clientes");
    						System.out.println("2. Base dos Profissionais");
    						System.out.println("3. Base dos Horários");
    						System.out.println("4. Voltar");
    						System.out.println("----------------- Impressão -----------------");
    						choice7 = sc.nextInt();
    						switch (choice7) {
    						case 1:
    							GeneralUsage.ClearConsole();
    							System.out.println("Imprimindo base de clientes...");
    							BaseDeDados.imprimirBaseDeDados("Cliente");
    							break;
    						case 2:
    							GeneralUsage.ClearConsole();
    							System.out.println("Imprimindo base de profissionais...");
    							BaseDeDados.imprimirBaseDeDados("Profissional");
    							break;
    						case 3:
    							GeneralUsage.ClearConsole();
    							System.out.println("Imprimindo base de horarios...");
    							BaseDeDados.imprimirBaseDeHorarios();
    							break;
    						case 4:
    							System.out.println("Retornando ao menu principal...");
    							break;
    						default:
    							System.out.println("Entrada inválida.");
    						}
    					} while (choice7 != 4);
                        break;

                    case 0:
                        System.out.println("Saindo do programa...");
                        break;

                    default:
                        System.out.println("Escolha inválida...");
                        break;
                }
            } while (choice != 0);

            sc.close(); // Fecha o Scanner ao final
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

