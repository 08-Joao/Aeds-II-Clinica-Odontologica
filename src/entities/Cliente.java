package entities;

public class Cliente extends Pessoa implements InformationOutput {
   

    public Cliente() {
        super();
    }

    public Cliente(String nome, String cpf, String telefone, String dataNascimento, String endereco) {
        super();
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setDataNascimento(dataNascimento);
        setEndereco(endereco);
        
    }

    public Cliente(int id, String nome, String cpf, String telefone, String dataNascimento, String endereco) {
        super(id, nome, cpf, telefone, dataNascimento, endereco);
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
