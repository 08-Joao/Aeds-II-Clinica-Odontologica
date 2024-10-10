package entities;

import java.io.Serializable;

public class Cliente extends Pessoa implements InformationOutput, Serializable {
   //Classe de Cliente que herda da classe Pessoa

	private static final long serialVersionUID = 1L;
	Integer posicao;
	
	
	public Cliente() {
        super();
    }

    public Cliente(String nome, String cpf, String telefone, String dataNascimento, String endereco, String posicao) {
        super();
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setDataNascimento(dataNascimento);
        setEndereco(endereco);
        setPosicao(posicao);
    }

    public Cliente(int id, String nome, String cpf, String telefone, String dataNascimento, String endereco,Integer posicao) {
        super(id, nome, cpf, telefone, dataNascimento, endereco);
        setPosicao(posicao);
    }

    public Cliente(int id, String nome, String cpf, String telefone, String dataNascimento, String endereco,String posicao) {
        super(id, nome, cpf, telefone, dataNascimento, endereco);
        setPosicao(posicao);
    }
    
    public Integer getPosicao() {
    	return this.posicao;
    }
    
    public void setPosicao(int posicao) {
    	this.posicao = posicao;    
    }
    
    public void setPosicao(String posicao) {
        try {
            this.posicao = Integer.valueOf(posicao);
        } catch (NumberFormatException e) {           
            this.posicao = -1;  // Ou qualquer valor padrão que faça sentido
        }
    }


    @Override
    public void imprimeInformacao() {
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("Cliente ID: " + getId());
        System.out.println("Nome: " + getNome());
        System.out.println("CPF: " + getCpf());
        System.out.println("Telefone: " + getTelefone());
        System.out.println("Data de Nascimento: " + getDataNascimento());
        System.out.println("Endereço: " + getEndereco());
    }
}

