package entities;

import java.io.Serializable;

import utils.TamanhoCampo;

public abstract class Pessoa implements Serializable, InformationOutput {
	private static final long serialVersionUID = 1L;
	
    private static final int TAMANHO_ID = TamanhoCampo.ID.valor;
    private static final int TAMANHO_NOME = TamanhoCampo.NOME.valor;;
    private static final int TAMANHO_CPF = TamanhoCampo.CPF.valor;
    private static final int TAMANHO_TELEFONE = TamanhoCampo.TELEFONE.valor;
    private static final int TAMANHO_DATA_NASCIMENTO = TamanhoCampo.DATA_NASCIMENTO.valor;
    private static final int TAMANHO_ENDERECO = TamanhoCampo.ENDERECO.valor;
    
    private Integer id;
    private String nome; 
    private String cpf; 
    private String telefone; 
    private String dataNascimento; 
    private String endereco; 

    public Pessoa() {
    }

    public Pessoa(int id, String nome, String cpf, String telefone, String dataNascimento, String endereco) {
        this.id = id;
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setDataNascimento(dataNascimento);
        setEndereco(endereco);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeSemEspaco() {
        String novoNome = this.nome;  // Supondo que o atributo nome já exista na classe
        
        // Remover espaços apenas do final da string
        novoNome = novoNome.replaceAll("\\s+$", "");
        
        return novoNome;
    }

    
    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = formatarCampo(nome, TAMANHO_NOME);
    }

    public void setCpf(String cpf) {
        this.cpf = formatarCampo(cpf, TAMANHO_CPF);
    }

    public void setTelefone(String telefone) {
        this.telefone = formatarCampo(telefone, TAMANHO_TELEFONE);
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = formatarCampo(dataNascimento, TAMANHO_DATA_NASCIMENTO);
    }

    public void setEndereco(String endereco) {
        this.endereco = formatarCampo(endereco, TAMANHO_ENDERECO);
    }

    protected String formatarCampo(String valor, int tamanho) {
        if (valor == null) {
            valor = "";
        }
        if (valor.length() > tamanho) {
            return valor.substring(0, tamanho);
        } else {
            return String.format("%-" + tamanho + "s", valor);
        }
    }
}