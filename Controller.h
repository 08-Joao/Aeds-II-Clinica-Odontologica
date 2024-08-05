#ifndef CONTROLLER_H_INCLUDED
#define CONTROLLER_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct Cliente {
    int id;
    char nome[50];
    char cpf[15];
    char telefone[9];
    char data_nascimento[11];
    char endereco[50];
} TCliente;

typedef struct Profissional {
    int id;
    char nome[50];
    char especialidade[30];
    char telefone[9];
    char endereco[50];
} TProfissional;

typedef struct Horario{
    int id;
    int id_cliente;
    int id_profissional;
    char data[10];
    char hora[5];
    bool status;
} THorario;

//Cria cliente
TCliente *cliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco);

// Salva cliente no arquivo out, na posicao atual do cursor
void salva_Cliente(TCliente *cliente, FILE *out);

// Le um cliente do arquivo na posicao atual do cursor
// Retorna um ponteiro para cliente lido do arquivo
TCliente *le(FILE *in);

// Imprime cliente
void imprimeCliente(TCliente *cliente);

TCliente *busca_cliente(FILE *in, int tam, int cod);

TCliente *busca_binaria_cliente(FILE *in, int tam, int cod)

// Retorna tamanho da struct em bytes
int tamanho_registro();

// retorna a quantidade de registros no arquivo
int qtdRegistros(FILE *arq);

//embaralha base de dados
void embaralha(int *vet,int MAX,int trocas);

// Cria a base de dados desordenada pelo id
void criarBaseDesordenada(FILE *out, int tam, int qtdTrocas);

// Imprime a base de dados
void imprimirBase(FILE *out);



#endif // CONTROLLER_H_INCLUDED
