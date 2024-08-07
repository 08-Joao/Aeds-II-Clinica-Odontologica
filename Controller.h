#ifndef CONTROLLER_H_INCLUDED
#define CONTROLLER_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define CPF_LENGTH 15  // Comprimento total do CPF, incluindo pontos e hífen
typedef struct Cliente {
    int id;
    char nome[100];
    char cpf[CPF_LENGTH];
    char telefone[16];
    char data_nascimento[11];
    char endereco[100];
} TCliente;

typedef struct Profissional {
    int id;
    char nome[100];
    char cpf[CPF_LENGTH];
    char especialidade[40];
    char telefone[16];
    char data_nascimento[11];
    char endereco[100];
} TProfissional;

typedef struct Horario {
    int id;
    int id_cliente;
    int id_profissional;
    char data[11];
    char hora[6];
    bool status;
} THorario;


// Funções gerais
void embaralha(int *vet, int max, int trocas);
void criaBaseOrdenada(FILE *base, int tamanho);
void imprimirBase_cliente(FILE *base_clientes);

/* Criação de PEssoas */
TCliente *criaCliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco);

TProfissional* criaProfissional(int id, char* nome, char* cpf,char* especialidade, char* telefone, char* data_nascimento, char* endereco){

void salva_cliente(TCliente *cliente, FILE *base_clientes);
/*
void salva_profissional(TProfissional *profissional, FILE *base_profissionais);
*/


/* Mostrar Informações */

TCliente *le_cliente(FILE *base_clientes);

void imprime_cliente(TCliente* cliente);

/* Busca de Pessoas */

TCliente *busca_cliente(FILE *base_clientes, int tam, int id);

TCliente *busca_binaria_cliente(FILE *base_clientes, int tam, int id);

int tamanho_registro_cliente();

int qtdRegistros_cliente(FILE *base_clientes);

void criarBaseDesordenada(FILE *base_clientes, int tam, int qtdTrocas, char *tipoPessoa);


#endif // CONTROLLER_H_INCLUDED
