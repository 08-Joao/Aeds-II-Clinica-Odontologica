#ifndef CONTROLLER_H_INCLUDED
#define CONTROLLER_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <time.h>


#define CPF_LENGTH 15  // Comprimento total do CPF, incluindo pontos e hífen
#define BLOCK_SIZE 4096
#define MAX_FILES 100

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
    char profissao[40];
    char telefone[16];
    char data_nascimento[11];
    char endereco[100];
} TProfissional;

typedef struct agendado {
    int id_profissional;
    int id_cliente;
} listaAgendados;

typedef struct Horario {
    time_t horario;
    listaAgendados listaHorario[10]; // Define que podemos ter 10 consultas agendadas em um mesmo horário, contudo, com 10 profissionais e clientes diferentes
    bool status;
} THorario;

void editar();
// Funções gerais
time_t converterParaHorario(const char *hora, const char *data);

void embaralha(int *vet, int max, int trocas);

void criaBaseOrdenada(FILE *base, int tamanho);

void imprimirBase(FILE* base_de_dados, char* tipoDeDado);

/* Criação de Pessoas */

TCliente *criaCliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco);

TProfissional* criaProfissional(int id, char* nome, char* cpf,char* especialidade, char* telefone, char* data_nascimento, char* endereco);

void salva_cliente(TCliente *cliente, FILE *base_clientes);

void salva_profissional(TProfissional *profissional, FILE *base_profissionais);

void salva_horario(THorario* horario, FILE* base_horario);

/* Adição e Remoção de Pessoas */

void adicionaPessoa(FILE* base_de_dados, char* tipoPessoa);

void removerPessoa(FILE* base_de_dados, char* tipoPessoa);

/* Verificar Ordenação de Bases */

bool verificaOrdenacaoPessoa(FILE *base_de_dados,char* tipoPessoa);

bool verificaOrdenacaoHorario(FILE* base_de_dados);

/* Agendamento de Consultas */

void agendarConsulta(FILE* base_clientes,FILE* base_profissionais,FILE* base_horarios);

/* Mostrar Informações */

TCliente *le_cliente(FILE *base_clientes);

TProfissional *le_profissional(FILE *base_profissional);

THorario *le_horario(FILE* base_horario);

void imprime_cliente(TCliente* cliente);

void imprime_profissional(TProfissional* profissional);

void imprime_horario(THorario* horario);

/* Busca de Pessoas */

TCliente *busca_cliente(FILE *base_clientes, int tam, int id);

TCliente *busca_binaria_cliente(FILE *base_clientes, int tam, int id);

TProfissional* busca_profissional(FILE* base_de_dados, int tam, int id);

TProfissional* busca_binaria_profissional(FILE* base_de_dados, int tam, int id);

THorario* busca_horario(FILE* base_de_dados, int tam, time_t horario);

THorario* busca_binaria_horario(FILE* base_de_dados, int tam, time_t horario);

/*Criação de Bases */
void criarBaseDesordenada(FILE *base_clientes, int tam, int qtdTrocas, char *tipoPessoa);

/* Área dos Horarios */

void criarBaseHorarios(FILE *base_de_dados, int qtdTrocas);

/* Ordenação */

void ordenarBase(FILE *base_de_dados, const char *tipo);

#endif // CONTROLLER_H_INCLUDED
