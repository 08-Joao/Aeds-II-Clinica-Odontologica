package utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entities.BaseDeDados;
import entities.Cliente;
import entities.Horario;
import entities.ListaAgendados;
import entities.Profissional;

public class Busca {
	
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

    
    
    public static Cliente sequencialCliente(int id) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream("clientes.dat"))) {
            while (dis.available() > 0) {
                Cliente cliente = lerRegistroCliente(dis);
                if (cliente.getId() == id) {
                    return cliente;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
        return null;
    }

    public static Profissional sequencialProfissional(int id) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream("profissionais.dat"))) {
            while (dis.available() > 0) {
                Profissional profissional = lerRegistroProfissional(dis);
                if (profissional.getId() == id) {
                    return profissional;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de profissionais: " + e.getMessage());
        }
        return null;
    }
    
    
    public static Cliente binariaCliente(int id) {
        try (RandomAccessFile raf = new RandomAccessFile(CAMINHO_CLIENTES, "r")) {
            long inicio = 0;
            long fim = raf.length() / TAMANHO_REGISTRO_CLIENTE - 1;
            
            while (inicio <= fim) {
                long meio = (inicio + fim) / 2;
                raf.seek(meio * TAMANHO_REGISTRO_CLIENTE);
                
                Cliente cliente = lerRegistroCliente(raf);
                if (cliente.getId() == id) {
                    return cliente;
                } else if (cliente.getId() < id) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
        return null;
    }

    public static Cliente hashCliente(int id) { 
    	try (RandomAccessFile raf = new RandomAccessFile(CAMINHO_CLIENTES, "r")) {
    		Integer clientePos = BaseDeDados.getPosHash("Cliente",id);
    		if(clientePos == null) {
    			System.out.println("Cliente não existe na base de dados.");
    			return null;
    		}
    		raf.seek((clientePos - 1) * TAMANHO_REGISTRO_CLIENTE);
    		
    		Cliente cliente = lerRegistroCliente(raf);
    		
    		if(cliente.getId() == id) {
    			return cliente;
    		}
    	}catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
    	return null;
    }
    
    public static Profissional binariaProfissional(int id) {
        try (RandomAccessFile raf = new RandomAccessFile(CAMINHO_PROFISSIONAIS, "r")) {
            long inicio = 0;
            long fim = raf.length() / TAMANHO_REGISTRO_PROFISSIONAL - 1;
            
            while (inicio <= fim) {
                long meio = (inicio + fim) / 2;
                raf.seek(meio * TAMANHO_REGISTRO_PROFISSIONAL);
                
                Profissional profissional = lerRegistroProfissional(raf);
                if (profissional.getId() == id) {
                    return profissional;
                } else if (profissional.getId() < id) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
				}
			}
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de profissionais: " + e.getMessage());
        }
        return null;
    }
    
    public static Profissional hashProfissional(int id) { 
    	try (RandomAccessFile raf = new RandomAccessFile(CAMINHO_PROFISSIONAIS, "r")) {
    		Integer profissionalPos = BaseDeDados.getPosHash("Profissional",id);
    		
    		if(profissionalPos == null) {
    			System.out.println("Profissional não existe na base de dados.");
    			return null;
    		}    		
    		raf.seek((profissionalPos - 1) * TAMANHO_REGISTRO_PROFISSIONAL);
    		
    		Profissional profissional = lerRegistroProfissional(raf);
    		
    		if(profissional.getId() == id) {    			
    			return profissional;
    		}
    	}catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
    	return null;
    }
    
    
    public static Horario sequencialHorario(Date horario) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(CAMINHO_HORARIOS))) {
            while (dis.available() > 0) {
                long horarioLido = dis.readLong();
                if (horarioLido == horario.getTime()) {
                    Horario horarioEncontrado = new Horario(new Date(horarioLido));
                    List<ListaAgendados> pessoasAgendadas = new ArrayList<>();
                    
                    for (int i = 0; i < 10; i++) {
                        int idCliente = dis.readInt();
                        int idProfissional = dis.readInt();
                        ListaAgendados agendado = new ListaAgendados();
                        agendado.setId_cliente(idCliente);
                        agendado.setId_profissional(idProfissional);
                        pessoasAgendadas.add(agendado);
                    }
                    
                    horarioEncontrado.setAgendamentos(pessoasAgendadas);
                    return horarioEncontrado;
                } else {
                    // Pular as pessoas agendadas para esse horário
                    dis.skipBytes(TAMANHO_TOTAL_AGENDAMENTOS);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de horários: " + e.getMessage());
        }
        return null;
    }

    
    public static Horario binariaHorario(Date horario) {
        try (RandomAccessFile raf = new RandomAccessFile(CAMINHO_HORARIOS, "r")) {
            long inicio = 0;
            long fim = raf.length() / TAMANHO_REGISTRO_HORARIO - 1;

            while (inicio <= fim) {
                long meio = (inicio + fim) / 2;
                raf.seek(meio * TAMANHO_REGISTRO_HORARIO);
                
                long horarioLido = raf.readLong();
                if (horarioLido == horario.getTime()) {
                    Horario horarioEncontrado = new Horario(new Date(horarioLido));
                    List<ListaAgendados> pessoasAgendadas = new ArrayList<>();
                    
                    raf.seek(raf.getFilePointer()); // Pular para a parte das pessoas agendadas
                    for (int i = 0; i < 10; i++) {
                        int idCliente = raf.readInt();
                        int idProfissional = raf.readInt();
                        ListaAgendados agendado = new ListaAgendados();
                        agendado.setId_cliente(idCliente);
                        agendado.setId_profissional(idProfissional);
                        pessoasAgendadas.add(agendado);
                    }
                    
                    horarioEncontrado.setAgendamentos(pessoasAgendadas);
                    return horarioEncontrado;
                } else if (horarioLido < horario.getTime()) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de horários: " + e.getMessage());
        }
        return null;
    }

    public static Horario hashHorario(Date horario) { 
    	try (RandomAccessFile raf = new RandomAccessFile(CAMINHO_HORARIOS, "r")) {
    		Integer horarioPos = BaseDeDados.getPosHorarioHash(horario);
    		
    		if(horarioPos == null) {
    			System.out.println("Horario não existe na base de dados.");
    			return null;
    		}  
    		raf.seek((horarioPos - 1) * TAMANHO_REGISTRO_HORARIO);
    		
    		long horarioLido = raf.readLong();
            if (horarioLido == horario.getTime()) {
                Horario horarioEncontrado = new Horario(new Date(horarioLido));
                List<ListaAgendados> pessoasAgendadas = new ArrayList<>();
                
                raf.seek(raf.getFilePointer()); // Pular para a parte das pessoas agendadas
                for (int i = 0; i < 10; i++) {
                    int idCliente = raf.readInt();
                    int idProfissional = raf.readInt();
                    ListaAgendados agendado = new ListaAgendados();
                    agendado.setId_cliente(idCliente);
                    agendado.setId_profissional(idProfissional);
                    pessoasAgendadas.add(agendado);
                }
                
                horarioEncontrado.setAgendamentos(pessoasAgendadas);
                return horarioEncontrado;
            }
    	}catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
    	return null;
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

    private static Cliente lerRegistroCliente(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        String nome = lerCampo(raf, TAMANHO_NOME);
        String cpf = lerCampo(raf, TAMANHO_CPF);
        String telefone = lerCampo(raf, TAMANHO_TELEFONE);
        String dataNascimento = lerCampo(raf, TAMANHO_DATA_NASCIMENTO);
        String endereco = lerCampo(raf, TAMANHO_ENDERECO);
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

    private static Profissional lerRegistroProfissional(RandomAccessFile raf) throws IOException {
        int id = raf.readInt();
        String nome = lerCampo(raf, TAMANHO_NOME);
        String cpf = lerCampo(raf, TAMANHO_CPF);
        String telefone = lerCampo(raf, TAMANHO_TELEFONE);
        String dataNascimento = lerCampo(raf, TAMANHO_DATA_NASCIMENTO);
        String endereco = lerCampo(raf, TAMANHO_ENDERECO);
        String profissao = lerCampo(raf, TAMANHO_PROFISSAO);
        return new Profissional(id, nome, cpf, telefone, dataNascimento, endereco, profissao);
    }
    
    private static String lerCampo(DataInputStream dis, int tamanhoCampo) throws IOException {
    	byte[] buffer = new byte[tamanhoCampo];
    	dis.readFully(buffer);
    	return new String(buffer, StandardCharsets.UTF_8).trim();
    }
    
    private static String lerCampo(RandomAccessFile raf, int tamanhoCampo) throws IOException {
        byte[] buffer = new byte[tamanhoCampo];
        raf.readFully(buffer);
        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

}