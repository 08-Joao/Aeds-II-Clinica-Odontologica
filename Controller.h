#ifndef CONTROLLER_H_INCLUDED
#define CONTROLLER_H_INCLUDED

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>


typedef struct Cliente {
    int id;
    char nome[50];
    char cpf[15];
    char telefone[16];
    char data_nascimento[11];
    char endereco[100];
} TCliente;

typedef struct Profissional {
    int id;
    char nome[50];
    char cpf[15];
    char especialidade[40];
    char telefone[16];
    char data_nascimento[11];
    char endereco[100];
} TProfissional;

typedef struct Horario{
    int id;
    int id_cliente;
    int id_profissional;
    char data[11];
    char hora[6];
    bool status;
} THorario;

#define CPF_LENGTH 14  // Comprimento total do CPF, incluindo pontos e hífen


//AREA GERAL

//embaralha base de dados

int calcularDigito(int cpf[],int tamanho);

char* gerarCPF();

char* gerarTelefone();

char* gerarDataNascimento(int dentista);

char *gerarNome();

char* gerarEndereco();

char* gerarEspecialidade();

void embaralha(int *vet,int MAX,int trocas);

//------------------------AREA CLIENTE----------------------------------------------------------------
//Cria cliente
TCliente *criaCliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco);

// Salva cliente no arquivo base_clientes, na posicao atual do cursor
void salva_Cliente(TCliente *cliente, FILE *base_clientes);

// Le um cliente do arquivo na posicao atual do cursor, retorna um ponteiro para cliente lido do arquivo
TCliente *le_cliente(FILE *base_clientes);

// Imprime cliente
void imprime_cliente(TCliente *cliente);

//Busca sequencial de cliente
TCliente *busca_cliente(FILE *base_clientes, int tam, int id);

//Busca binaria de cliente
TCliente *busca_binaria_cliente(FILE *base_clientes, int tam, int id);

// Retorna tamanho da struct de clientes em bytes
int tamanho_registro_cliente();

// retorna a quantidade de registros no arquivo
int qtdRegistros_cliente(FILE *base_clientes);

// Cria a base de dados desordenada pelo id
void criarBaseDesordenada(FILE *base_clientes, int tam, int qtdTrocas,char *tipoPessoa);

// Imprime a base de dados
void imprimirBase_cliente(FILE *base_clientes);

//------------------------AREA PROFISSIONAIS----------------------------------------------------------------

//Cria profissional
TProfissional *criaProfissional(int id, char *nome, char *cpf, char *especialidade, char *telefone, char *data_nascimento, char *endereco);

// Salva profissional no arquivo base_profissionais, na posicao atual do cursor
void salva_Profissional(TProfissional *profissional, FILE *base_profissionais);

// Le um profissional do arquivo na posicao atual do cursor, retorna um ponteiro para profissional lido do arquivo
TProfissional *le_profissional(FILE *base_profissionais);

// Imprime profissional
void imprime_profissional(TProfissional *profissional);

// Imprime a base de dados de profissionais
void imprimirBase_profissinal(FILE *base_profissionais);

//Busca sequencial de profissional
TProfissional *busca_profissional(FILE *base_profissionais, int tam, int id);

//Busca binaria de cliente
TProfissional *busca_binaria_profissional(FILE *base_profissionais, int tam, int id);

// Retorna tamanho da struct de profissionais em bytes
int tamanho_registro_profissional();

// retorna a quantidade de registros no arquivo
int qtdRegistros_profissional(FILE *base_profissionais);

// Cria a base de dados desordenada pelo id
void criarBaseDesordenada_profissional(FILE *base_profissionais, int tam, int qtdTrocas);



//------------------------AREA HORARIOS----------------------------------------------------------------

//Cria horarios
THorario *criarHorario(int id, int id_cliente, int id_profissional, char *data, char *hora, bool status);

// Salva profissional no arquivo base_horarios, na posicao atual do cursor
void salva_Horario(THorario *horario, FILE *base_horarios);

// Le um horario do arquivo na posicao atual do cursor, retorna um ponteiro para o horario lido do arquivo
THorario *le_horario(FILE *base_horarios);

// Imprime horario
void imprime_horario(THorario *horario);

// Imprime a base de dados de horarios
void imprimirBase_horario(FILE *base_horarios);

//Busca sequencial de horario
THorario *busca_horario(FILE *base_horarios, int tam, int id);

//Busca binaria de horario
THorario *busca_binaria_horario(FILE *base_horario, int tam, int id);

// Retorna tamanho da struct de horario em bytes
int tamanho_registro_horario();

// retorna a quantidade de registros no arquivo
int qtdRegistros_horario(FILE *base_horarios);

// Cria a base de dados desordenada pelo id
void criarBaseDesordenada_horario(FILE *base_horario, int tam, int qtdTrocas);



#endif // CONTROLLER_H_INCLUDED
