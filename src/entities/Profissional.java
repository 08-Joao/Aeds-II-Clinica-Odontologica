package entities;

public class Profissional extends Pessoa implements InformationOutput {
   
    private static final int TAMANHO_PROFISSAO = 50;

    
    private String profissao; // 50 caracteres

    public Profissional() {
        super();
    }

    public Profissional(String nome, String cpf, String telefone, String dataNascimento, String endereco, String profissao) {
        super();
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setDataNascimento(dataNascimento);
        setEndereco(endereco);
        setProfissao(profissao);
    }

    public Profissional(int id, String nome, String cpf, String telefone, String dataNascimento, String endereco, String profissao) {
        super(id, nome, cpf, telefone, dataNascimento, endereco);
        setProfissao(profissao);
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
    	this.profissao = formatarCampo(profissao,TAMANHO_PROFISSAO);
    }

    @Override	
    public void imprimeInformacao() {
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("Profissional ID: " + getId());
        System.out.println("Nome: " + getNome());
        System.out.println("CPF: " + getCpf());
        System.out.println("Profissão: " + getProfissao());
        System.out.println("Telefone: " + getTelefone());
        System.out.println("Data de Nascimento: " + getDataNascimento());
        System.out.println("Endereço: " + getEndereco());
    }
}
