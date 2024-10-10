package entities;

import java.io.*;

public class TabelaHashDisco {
    private static final String ARQUIVO_TABELA = "tabela_hash.dat";
    private static final String ARQUIVO_LISTAS = "listas_encadeadas.dat";
    private int tamanho;

    // Construtor que inicializa a tabela hash
    public TabelaHashDisco(int tamanho) {
        this.tamanho = tamanho;
        inicializarArquivos();
    }

    // Inicializa os arquivos da tabela hash e das listas encadeadas
    private void inicializarArquivos() {
        try (RandomAccessFile tabelaFile = new RandomAccessFile(ARQUIVO_TABELA, "rw");
             RandomAccessFile listasFile = new RandomAccessFile(ARQUIVO_LISTAS, "rw")) {
            // Preenche a tabela com -1 (vazia)
            for (int i = 0; i < tamanho; i++) {
                tabelaFile.writeInt(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mapearClientesParaHash() {
        System.out.println("MAPEANDO...");

        // Apaga os arquivos antigos da tabela e listas encadeadas
        File tabelaFile = new File(ARQUIVO_TABELA);
        File listasFile = new File(ARQUIVO_LISTAS);
        
        if (tabelaFile.exists()) {
            tabelaFile.delete();
        }
        
        if (listasFile.exists()) {
            listasFile.delete();
        }

        // Recria os arquivos vazios
        inicializarArquivos(); // Isso vai recriar os arquivos da tabela hash e listas encadeadas

        // Mapeia os clientes no novo arquivo
        try (DataInputStream dis = new DataInputStream(new FileInputStream("clientes.dat"))) {
            int count = 0;
            while (dis.available() > 0) {
                Cliente cliente = BaseDeDados.lerRegistroCliente(dis);
                cliente.setPosicao(count);
                inserir(cliente);  // Insere os clientes no hash
                count++;
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de clientes: " + e.getMessage());
        }
    }


    // Função hash que retorna a posição na tabela
    private int hash(int id) {
        return id % tamanho;
    }

    // Insere um cliente na tabela hash
    public void inserir(Cliente cliente) {
        int posicao = hash(cliente.getId());        

        try (RandomAccessFile tabelaFile = new RandomAccessFile(ARQUIVO_TABELA, "rw");
             RandomAccessFile listasFile = new RandomAccessFile(ARQUIVO_LISTAS, "rw")) {

            // Lê o primeiro índice para verificar se já existe um cliente nesse índice
            tabelaFile.seek(posicao * 4);
            int primeiroIndice = tabelaFile.readInt();           

            // Serializa o cliente em um array de bytes
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteOut);
            oos.writeObject(cliente);
            oos.flush();
            byte[] bytesCliente = byteOut.toByteArray();

            // Escreve o cliente no arquivo de listas encadeadas
            long novaPosicao = listasFile.length();
            listasFile.seek(novaPosicao);
            listasFile.writeInt(bytesCliente.length); // Tamanho dos dados do cliente
            listasFile.write(bytesCliente); // Dados do cliente
            listasFile.writeInt(-1); // Próximo índice (inicialmente -1)

            // Atualiza a tabela hash
            if (primeiroIndice == -1) {
                tabelaFile.seek(posicao * 4);
                tabelaFile.writeInt((int) novaPosicao); // Insere o novo índice na tabela              
            } else {
                // Navega até o final da lista encadeada
                long ultimaPosicao = primeiroIndice;
                while (true) {
                    listasFile.seek(ultimaPosicao);
                    int tamanho = listasFile.readInt();
                    listasFile.skipBytes(tamanho);
                    int proximoIndice = listasFile.readInt();

                    if (proximoIndice == -1) {
                        listasFile.seek(ultimaPosicao + 4 + tamanho);
                        listasFile.writeInt((int) novaPosicao); // Atualiza o próximo índice                        
                        break;
                    }
                    ultimaPosicao = proximoIndice;
                }
            }

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cliente buscar(int id) {
        int posicao = hash(id);  // Calcula a posição hash a partir do ID
        
        try (RandomAccessFile tabelaFile = new RandomAccessFile(ARQUIVO_TABELA, "r");
             RandomAccessFile listasFile = new RandomAccessFile(ARQUIVO_LISTAS, "r")) {

            // Pega o índice na tabela hash
            tabelaFile.seek(posicao * 4);
            int primeiroIndice = tabelaFile.readInt();

            // Se o índice for -1, não há cliente com esse ID
            if (primeiroIndice == -1) {
                System.out.println("Cliente com ID " + id + " não encontrado.");
                return null;
            }

            // Percorre a lista encadeada para encontrar o cliente
            while (primeiroIndice != -1) {
                listasFile.seek(primeiroIndice);
                int tamanhoCliente = listasFile.readInt();
                byte[] bytesCliente = new byte[tamanhoCliente];
                listasFile.readFully(bytesCliente);

                // Desserializa o cliente
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytesCliente))) {
                    Cliente cliente = (Cliente) ois.readObject();                    
                    // Verifica se o ID do cliente corresponde ao ID procurado
                    if (cliente.getId() == id) {                    	                        
                        return cliente;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // Move para o próximo cliente na lista encadeada (se houver)
                primeiroIndice = listasFile.readInt();
            }

            // Se chegar ao final da lista sem encontrar o cliente
            System.out.println("Cliente com ID " + id + " não encontrado.");
            return null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean remover(int id) {
        int posicao = hash(id);  // Calcula a posição hash a partir do ID
        
        try (RandomAccessFile tabelaFile = new RandomAccessFile(ARQUIVO_TABELA, "rw");
             RandomAccessFile listasFile = new RandomAccessFile(ARQUIVO_LISTAS, "rw")) {

            // Pega o índice na tabela hash
            tabelaFile.seek(posicao * 4);
            int primeiroIndice = tabelaFile.readInt();

            // Se o índice for -1, não há cliente com esse ID
            if (primeiroIndice == -1) {
                System.out.println("Cliente com ID " + id + " não encontrado.");
                return false;
            }

            long posicaoAnterior = -1;  // Guardará a posição do cliente anterior na lista encadeada
            long posicaoAtual = primeiroIndice;

            // Percorre a lista encadeada para encontrar o cliente
            while (posicaoAtual != -1) {
                listasFile.seek(posicaoAtual);
                int tamanhoCliente = listasFile.readInt();
                byte[] bytesCliente = new byte[tamanhoCliente];
                listasFile.readFully(bytesCliente);

                // Desserializa o cliente
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytesCliente))) {
                    Cliente cliente = (Cliente) ois.readObject();
                    
                    // Verifica se o ID do cliente corresponde ao ID procurado
                    if (cliente.getId() == id) {
                        int proximoIndice = listasFile.readInt();  // Lê o próximo índice
                        
                        // Se for o primeiro cliente da lista, atualize a tabela hash
                        if (posicaoAnterior == -1) {
                            tabelaFile.seek(posicao * 4);
                            tabelaFile.writeInt(proximoIndice);  // Atualiza a tabela para apontar para o próximo cliente
                        } else {
                            // Atualiza o próximo do cliente anterior para pular o cliente removido
                            listasFile.seek(posicaoAnterior + 4 + tamanhoCliente);
                            listasFile.writeInt(proximoIndice);
                        }

                        System.out.println("Cliente com ID " + id + " removido.");
                        return true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // Atualiza a posição anterior e vai para o próximo cliente na lista encadeada
                posicaoAnterior = posicaoAtual;
                posicaoAtual = listasFile.readInt();
            }

            // Se chegar ao final da lista sem encontrar o cliente
            System.out.println("Cliente com ID " + id + " não encontrado.");
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

 
    public void exibirTabelaHash() {
        try (RandomAccessFile tabelaFile = new RandomAccessFile(ARQUIVO_TABELA, "r");
             RandomAccessFile listasFile = new RandomAccessFile(ARQUIVO_LISTAS, "r")) {

            System.out.println("Tabela Hash:");
            for (int i = 0; i < tamanho; i++) {
                tabelaFile.seek(i * 4);
                int primeiroIndice = tabelaFile.readInt();
                System.out.print("Índice " + i + ": ");

                if (primeiroIndice == -1) {
                    System.out.println("vazio");
                } else {
                    // Percorre a lista encadeada
                    while (primeiroIndice != -1) {
                        listasFile.seek(primeiroIndice);
                        int tamanhoCliente = listasFile.readInt();
                        byte[] bytesCliente = new byte[tamanhoCliente];
                        listasFile.readFully(bytesCliente);

                        // Desserializa o cliente
                        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytesCliente))) {
                            Cliente cliente = (Cliente) ois.readObject();
                            
                            if (cliente != null) {
                                System.out.print("[" + cliente.getId() + ", " + cliente.getNomeSemEspaco() + " Pos[" + cliente.getPosicao() + "]" + "] -> ");
                            } else {
                                System.out.print("[null, null] -> ");
                            }
                        } catch (Exception e) {
                            System.err.println("Erro na desserialização: " + e.getMessage());
                            e.printStackTrace();
                        }
                        
                        primeiroIndice = listasFile.readInt(); // Lê o próximo índice
                    }
                    System.out.println("null");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
}
