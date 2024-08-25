package entities;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public abstract class Pessoa implements InformationOutput {
    
    private static final int TAMANHO_NOME = 100;
    private static final int TAMANHO_CPF = 15;
    private static final int TAMANHO_TELEFONE = 14; // 14 caracteres com espaço e hífen
    private static final int TAMANHO_DATA_NASCIMENTO = 10;
    private static final int TAMANHO_ENDERECO = 100;

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
