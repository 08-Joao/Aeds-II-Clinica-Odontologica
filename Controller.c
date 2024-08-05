#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "Controller.h"


// Cria cliente
TCliente *cliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco) {
    TCliente *cliente = (TCliente *) malloc(sizeof(TCliente));
    //inicializa espaco de memoria com ZEROS
    if (cliente) memset(cliente, 0, sizeof(TCliente));
    //copia valores para os campos de cliente
    cliente->id = id;
    strcpy(cliente->nome, nome);
    strcpy(cliente->cpf, cpf);
    strcpy(cliente->telefone, telefone);
    strcpy(cliente->data_nascimento, data_nascimento);
    strcpy(cliente->endereco, endereco);

    return cliente;
}

