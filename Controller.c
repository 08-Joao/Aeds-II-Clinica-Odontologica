#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "Controller.h"

//embaralha base de dados FUNCIONA PARA TODOS
void embaralha(int *vet,int max,int trocas) {
    srand(time(NULL));
    for (int i = 0; i <= trocas; i++) {
        int j = rand() % (max-1);
        int k = rand() % (max-1);
        int tmp = vet[j];
        vet[j] = vet[k];
        vet[k] = tmp;
    }
}

//------------------------AREA CLIENTE----------------------------------------------------------------

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

// Salva cliente arquivo base_clientes, na posicao atual do cursor
void salva_cliente(TCliente *cliente, FILE *base_clientes) {
    fwrite(&cliente->id, sizeof(int), 1, base_clientes);
    //cliente->nome ao inves de &cliente->nome, pois string ja eh um ponteiro
    fwrite(cliente->nome, sizeof(char), sizeof(cliente->nome), base_clientes);
    fwrite(cliente->cpf, sizeof(char), sizeof(cliente->cpf), base_clientes);
    fwrite(cliente->telefone, sizeof(char), sizeof(cliente->telefone), base_clientes);
    fwrite(cliente->data_nascimento, sizeof(char), sizeof(cliente->data_nascimento), base_clientes);
    fwrite(cliente->endereco, sizeof(char), sizeof(cliente->endereco), base_clientes);   
}

// Le um cliente do arquivo  na posicao atual do cursor, retorna um ponteiro para cliente lido do arquivo
TCliente *le_cliente(FILE *base_clientes) {
    TCliente *cliente = (TCliente *) malloc(sizeof(TCliente));
    if (0 >= fread(&cliente->id, sizeof(int), 1, base_clientes)) {
        free(cliente);
        return NULL;
    }
    fread(cliente->nome, sizeof(char), sizeof(cliente->nome), base_clientes);
    fread(cliente->cpf, sizeof(char), sizeof(cliente->cpf), base_clientes);
    fread(cliente->telefone, sizeof(char), sizeof(cliente->telefone), base_clientes);
    fread(cliente->data_nascimento, sizeof(char), sizeof(cliente->data_nascimento), base_clientes);
    fread(cliente->endereco, sizeof(char), sizeof(cliente->endereco), base_clientes);
    return cliente;
}

// Imprime cliente
void imprime_cliente(TCliente *cliente) {
    printf("**********************************************");
    printf("\nCliente de codigo ");
    printf("%d", cliente->id);
    printf("\nNome: ");
    printf("%s", cliente->nome);
    printf("\nCPF: ");
    printf("%s", cliente->cpf);
    printf("\nTelefone: ");
    printf("%s", cliente->telefone);
    printf("\nData de Nascimento: ");
    printf("%s", cliente->data_nascimento);
    printf("\nEndereco: ");
    printf("%s", cliente->endereco);
    printf("\n**********************************************\n");
}

//Imprime toda a base de dados de cliente
void imprimirBase_cliente(FILE *base_clietes){
printf("\nImprimindo a base de clientes...\n");
    rewind(base_clientes);
    TCliente *cliente;

    while ((cliente = le_cliente(base_clietes)) != NULL)
        imprime_cliente(base_clietes);

    free(cliente);

}

//Obtem o tamanho de um cliente
int tamanho_registro_cliente() {
    return sizeof(int)  //id
           + sizeof(char) * 50 //nome
           + sizeof(char) * 15 //cpf
           + sizeof(char) * 9 //telefone
           + sizeof(char) * 11 //data_nascimento
           + sizeof(char) * 50; //endereco
}

// retorna a quantidade de registros de clientes no arquivo
int qtdRegistros_cliente(FILE *arq) {
    fseek(arq, 0, SEEK_END);
    int tam = trunc(ftell(arq) / tamanho_registro_cliente());
    return tam;
}

// Cria a base de dados desordenada pelo codigo do cliente
void criarBaseDesordenada_cliente(FILE *base_clientes, int tam, int qtdTrocas){

    int vet[tam];
    TCliente *cliente;

    for(int i=0;i<tam;i++)
        vet[i] = i+1;

    embaralha(vet,tam,qtdTrocas);

    printf("\nGerando a base de clientes...\n");

    for (int i=0;i<tam;i++){
        cliente = cliente(vet[i], "A", "000.000.000-00", "999999999" ,"01/01/1980", "Rua das casas");
        salva_cliente(cliente, base_clientes);
    }

    free(cliente);

}

//Busca sequencial de cliente
TCliente *busca_cliente(FILE *base_clientes, int tam, int id){
    int comparacoes = 0;
    for(int i = 0; i < tam; i++){
        fseek(base_clientes,tamanho_registro_cliente()*i,SEEK_SET);
        TCliente *cliente = le_cliente(base_clientes);
        comparacoes +=1;
        if(cliente->id == id){
            printf("A quantidade de buscas foi de %d comparacoes\n", comparacoes);
            return f;
        }
    }
    return NULL;
}

//Busca Binaria Cliente
TFunc *busca_binaria_cliente(FILE *base_clientes, int tam, int cod){
    int comparacoes = 0;
    int inicio = 0;
    int fim = tam - 1;
    int meio;

    while (inicio <= fim) {
        meio =(inicio + fim) /2;

        fseek(base_clientes, tamanho_registro_cliente()*meio, SEEK_SET);
        TCliente *cliente = le_cliente(base_clientes);
        comparacoes ++;
        meio = (inicio + fim) / 2;

        if (cliente->id == id){
            printf("A quantidade de buscas foi de %d comparacoes\n", comparacoes);
            return cliente;
        } else if (cliente->id < id){
            inicio = meio + 1;
        }
        else{
            fim = meio - 1;
        }
        free(cliente);
    }
    // Se não encontrou a chave
    return NULL;
}

//------------------------AREA PROFISSIONAIS----------------------------------------------------------------

// Cria profissional
TProfissional *profissional(int id, char *nome, char *cpf, char *especialidade, char *telefone, char *data_nascimento, char *endereco) {
    TProfissional *profissional = (TProfissional *) malloc(sizeof(TProfissional));
    //inicializa espaco de memoria com ZEROS
    if (profissional) memset(profissional, 0, sizeof(TProfissional));
    //copia valores para os campos de profissional
    profissional->id = id;
    strcpy(profissional->nome, nome);
    strcpy(profissional->cpf, cpf);
    strcpy(profissional->especialidade, especialidade);
    strcpy(profissional->telefone, telefone);
    strcpy(profissional->data_nascimento, data_nascimento);
    strcpy(profissional->endereco, endereco);

    return profissional;
}

// Salva profissional arquivo base_profissionais, na posicao atual do cursor
void salva_profissional(TProfissional *profissional, FILE *base_profissionais) {
    fwrite(&profissional->id, sizeof(int), 1, base_profissionais);
    fwrite(profissional->nome, sizeof(char), sizeof(profissional->nome), base_profissionais);
    fwrite(profissional->cpf, sizeof(char), sizeof(profissional->cpf), base_profissionais);
    fwrite(profissional->especialidade, sizeof(char), sizeof(profissional->especialidade), base_profissionais);
    fwrite(profissional->telefone, sizeof(char), sizeof(profissional->telefone), base_profissionais);
    fwrite(profissional->data_nascimento, sizeof(char), sizeof(profissional->data_nascimento), base_profissionais);
    fwrite(profissional->endereco, sizeof(char), sizeof(profissional->endereco), base_profissionais);   
}

// Le um profissional do arquivo  na posicao atual do cursor, retorna um ponteiro para profissional lido do arquivo
TProfissional *le_profissional(FILE *base_profissionais) {
    TProfissional *profissional = (TCliente *) malloc(sizeof(TCliente));
    if (0 >= fread(&profissional->id, sizeof(int), 1, base_profissionais)) {
        free(profissional);
        return NULL;
    }
    fread(profissional->nome, sizeof(char), sizeof(profissional->nome), base_profissionais);
    fread(profissional->cpf, sizeof(char), sizeof(profissional->cpf), base_profissionais);
    fread(profissional->especialidade, sizeof(char), sizeof(profissional->especialidade), base_profissionais);
    fread(profissional->telefone, sizeof(char), sizeof(profissional->telefone), base_profissionais);
    fread(profissional->data_nascimento, sizeof(char), sizeof(profissional->data_nascimento), base_profissionais);
    fread(profissional->endereco, sizeof(char), sizeof(profissional->endereco), base_profissionais);
    return profissional;
}

// Imprime profissional
void imprime_profissional(TProfissional *profissional) {
    printf("**********************************************");
    printf("\nProficional de codigo ");
    printf("%d", profissional->id);
    printf("\nNome: ");
    printf("%s", profissional->nome);
    printf("\nEspecialidade: ");
    printf("%s", profissional->especialidade)
    printf("\nCPF: ");
    printf("%s", profissional->cpf);
    printf("\nTelefone: ");
    printf("%s", profissional->telefone);
    printf("\nData de Nascimento: ");
    printf("%s", profissional->data_nascimento);
    printf("\nEndereco: ");
    printf("%s", profissional->endereco);
    printf("\n**********************************************\n");
}

//Imprime toda a base de dados de profissional
void imprimirBase_profissional(FILE *base_profissionais){
printf("\nImprimindo a base de profissionais...\n");
    rewind(base_profissionais);
    TProfissional *profissional;

    while ((profissional = le_profissional(base_profissionais)) != NULL)
        imprime_profissional(base_profissionais);

    free(profissional);

}

//Obtem o tamanho de um profissional
int tamanho_registro_profissional() {
    return sizeof(int)  //id
           + sizeof(char) * 50 //nome
           + sizeof(char) * 15 //cpf
           + sizeof(char) * 30 //especialidade
           + sizeof(char) * 9 //telefone
           + sizeof(char) * 11 //data_nascimento
           + sizeof(char) * 50; //endereco
}

// retorna a quantidade de registros de profissionais no arquivo
int qtdRegistros_profissional(FILE *base_profissionais) {
    fseek(base_profissionais, 0, SEEK_END);
    int tam = trunc(ftell(base_profissionais) / tamanho_registro_profissional());
    return tam;
}

// Cria a base de dados desordenada pelo codigo do profissional
void criarBaseDesordenada_profissional(FILE *base_profissionais, int tam, int qtdTrocas){

    int vet[tam];
    TProfissional *profissional;

    for(int i=0;i<tam;i++)
        vet[i] = i+1;

    embaralha(vet,tam,qtdTrocas);

    printf("\nGerando a base de profissionais...\n");

    for (int i=0;i<tam;i++){
        profissional = profissional(vet[i], "P", "000.000.000-00", "Dentista", "999999999" ,"01/01/1980", "Rua dos predios");
        salva_profissional(profissional, base_profissionais);
    }

    free(profissional);

}

//Busca sequencial de profissional
TProfissional *busca_profissional(FILE *base_proficionais, int tam, int id){
    int comparacoes = 0;
    for(int i = 0; i < tam; i++){
        fseek(base_proficionais,tamanho_registro_profissional()*i,SEEK_SET);
        TProfissional *profissional = le_profissional(base_proficionais);
        comparacoes +=1;
        if(profissional->id == id){
            printf("A quantidade de buscas foi de %d comparacoes\n", comparacoes);
            return profissional;
        }
    }
    return NULL;
}

//Busca Binaria Profissional
TFunc *busca_binaria_profissional(FILE *base_profissionais, int tam, int cod){
    int comparacoes = 0;
    int inicio = 0;
    int fim = tam - 1;
    int meio;

    while (inicio <= fim) {
        meio =(inicio + fim) /2;

        fseek(base_profissionais, tamanho_registro_profissional()*meio, SEEK_SET);
        TProfissional *profissional = le_profissional(base_profissionais);
        comparacoes ++;
        meio = (inicio + fim) / 2;

        if (profissional->id == id){
            printf("A quantidade de buscas foi de %d comparacoes\n", comparacoes);
            return profissional;
        } else if (profissional->id < id){
            inicio = meio + 1;
        }
        else{
            fim = meio - 1;
        }
        free(profissional);
    }
    // Se não encontrou a chave
    return NULL;
}


//------------------------AREA HORARIOS----------------------------------------------------------------

// Cria horario
THorario *horario(int id, int id_cliente, in id_profissional, char *data, char *hora, bool status) {
    THorario *horario = (THorario *) malloc(sizeof(THorario));
    //inicializa espaco de memoria com ZEROS
    if (horario) memset(horario, 0, sizeof(THorario));
    //copia valores para os campos de horario
    horario->id = id;
    horario->id_cliente = id_cliente;
    horario->id_profissional = id_profissional;
    strcpy(horario->data, data);
    strcpy(horario->hora, hora);
    horario->status = status;

    return horario;
}

// Salva horario arquivo base_horarios, na posicao atual do cursor
void salva_horario(THorario *horario, FILE *base_horarios) {
    fwrite(&horario->id, sizeof(int), 1, base_horarios);
    fwrite(&horario->id_cliente, sizeof(int), 1, base_horarios);
    fwrite(&horario->id_profissional, sizeof(int), 1, base_horarios);
    fwrite(horario->data, sizeof(char), sizeof(horario->data), base_horarios);
    fwrite(horario->hora, sizeof(char), sizeof(horario->hora), base_horarios);
    fwrite(horario->status, sizeof(bool), 1, base_horarios) 
}

// Le um horario do arquivo  na posicao atual do cursor, retorna um ponteiro para horario lido do arquivo
THorario *le_horario(FILE *base_horarios) {
    THorario *horario = (THorario *horario) malloc(sizeof(THorario));
    if (0 >= fread(&horario->id, sizeof(int), 1, base_horarios)) {
        free(horario);
        return NULL;
    }
    fread(horario->id, sizeof(int), sizeof(horario->id), base_horarios);
    fread(horario->id_cliente, sizeof(int), sizeof(horario->id_cliente), base_horarios);
    fread(horario->id_profissional, sizeof(int), sizeof(horario->id_profissional), base_horarios);
    fread(horario->data, sizeof(char), sizeof(horario->data), base_horarios);
    fread(horario->hora, sizeof(char), sizeof(horario->hora), base_horarios);
    fread(horario->status, sizeof(bool), sizeof(horario->status), base_horarios);
    return horario;
}

// Imprime horario
void imprime_horario(THorario *horario) {
    printf("**********************************************");
    printf("\nHorario de codigo ");
    printf("%d", horario->id);
    printf("\nId do Cliente: %d", horario->id_cliente);
    printf("\nId do Profissional: %d", horario->id_profissional);
    printf("\nData: %s", horario->data);
    printf("\nHorario: %s", horario->hora);
    printf("\nDiponibilidade: ");
    printf(horario->status);
}

//Imprime toda a base de dados de horario
void imprimirBase_horario(FILE *base_horarios){
printf("\nImprimindo a base de horarios...\n");
    rewind(base_horarios);
    THorario *horario;

    while ((horario = le_horario(base_horarios)) != NULL)
        imprime_horario(base_horarios);

    free(horario);

}

//Obtem o tamanho de um horario
int tamanho_registro_horario() {
    return sizeof(int)  //id
           + sizeof(int) //id_cliente
           + sizeof(int) //id_profissional
           + sizeof(char) * 10 //data
           + sizeof(char) * 5 //hora
           + sizeof(bool); //status
}

// retorna a quantidade de registros de horarios no arquivo
int qtdRegistros_horario(FILE *base_horarios) {
    fseek(base_horarios, 0, SEEK_END);
    int tam = trunc(ftell(base_horarios) / tamanho_registro_horario());
    return tam;
}

// Cria a base de dados desordenada pelo codigo do horario
void criarBaseDesordenada_horario(FILE *base_horarios, int tam, int qtdTrocas){

    int vet[tam];
    THorario *horario;

    for(int i=0;i<tam;i++)
        vet[i] = i+1;

    embaralha(vet,tam,qtdTrocas);

    printf("\nGerando a base de horarios...\n");

    for (int i=0;i<tam;i++){
        horario = horario(vet[i], 0, 1, "06/08/2024", "15:45", true);
        salva_horario(horario, base_horarios);
    }

    free(horario);

}

//Busca sequencial de horario
THorario *busca_horario(FILE *base_horarios, int tam, int id){
    int comparacoes = 0;
    for(int i = 0; i < tam; i++){
        fseek(base_horarios,tamanho_registro_horario()*i,SEEK_SET);
        THorario *horario = le_horario(base_horarios);
        comparacoes +=1;
        if(horario->id == id){
            printf("A quantidade de buscas foi de %d comparacoes\n", comparacoes);
            return horario;
        }
    }
    return NULL;
}

//Busca Binaria horario
TFunc *busca_binaria_horario(FILE *base_horarios, int tam, int cod){
    int comparacoes = 0;
    int inicio = 0;
    int fim = tam - 1;
    int meio;

    while (inicio <= fim) {
        meio =(inicio + fim) /2;

        fseek(base_horarios, tamanho_registro_horario()*meio, SEEK_SET);
        THorario *horario = le_horario(base_horarios);
        comparacoes ++;
        meio = (inicio + fim) / 2;

        if (horario->id == id){
            printf("A quantidade de buscas foi de %d comparacoes\n", comparacoes);
            return horario;
        } else if (horario->id < id){
            inicio = meio + 1;
        }
        else{
            fim = meio - 1;
        }
        free(horario);
    }
    // Se não encontrou a chave
    return NULL;
}