package utils;

public enum TamanhoCampo {
    ID(4),
    NOME(100),
    CPF(15),
    TELEFONE(14),
    DATA_NASCIMENTO(10),
    ENDERECO(100),
    PROFISSAO(50),
    TAMANHO_HASHTABLE(10),
    POSICAO(4),
    
    
    REGISTRO_CLIENTE(ID.valor + NOME.valor + CPF.valor + TELEFONE.valor + DATA_NASCIMENTO.valor + ENDERECO.valor + POSICAO.valor),
    REGISTRO_PROFISSIONAL(ID.valor + NOME.valor + CPF.valor + TELEFONE.valor + DATA_NASCIMENTO.valor + ENDERECO.valor + PROFISSAO.valor),

    HORARIO(8),
    LISTA_AGENDADOS(8),
    TOTAL_AGENDAMENTOS(LISTA_AGENDADOS.valor * 10),
    REGISTRO_HORARIO(HORARIO.valor + TOTAL_AGENDAMENTOS.valor);

	
    public final int valor;

    TamanhoCampo(int valor) {
        this.valor = valor;
    }
}
