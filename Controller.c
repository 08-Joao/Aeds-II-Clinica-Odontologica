#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <time.h>
#include "Controller.h"
#include <string.h>
#include <math.h>

char *first_names[100] = {
    "Jorge", "Joao", "Henrique", "Gustavo", "Guilherme", "Ornofre", "Ian", "Gabriel", "Cleiton", "Sergio",
    "Lucas", "Pedro", "Matheus", "Rafael", "Felipe", "Bruno", "Tiago", "Leonardo", "Daniel", "Carlos",
    "Vinicius", "Andre", "Marcelo", "Victor", "Ricardo", "Eduardo", "Fernando", "Roberto", "Alexandre", "Diego",
    "Antonio", "Murilo", "Leandro", "Rodrigo", "Marcos", "Wesley", "Fabio", "Douglas", "Igor", "Samuel",
    "Jose", "William", "Nathan", "Thiago", "Everton", "Kleber", "Elton", "Luis", "Alex", "Cristiano",
    "Francisco", "Marlon", "Brayan", "Alan", "Geovanni", "Julio", "Jair", "Erick", "Edson", "Ronaldo",
    "Caio", "Otavio", "Vitor", "Afonso", "Angelo", "Matias", "Artur", "Bernardo", "Davi", "Enzo",
    "Frederico", "Heitor", "Joaquim", "Lorenzo", "Miguel", "Noah", "Benjamin", "Vicente", "Nicolas", "Tomás",
    "Alberto", "Augusto", "Sandro", "Raul", "Renato", "Sandro", "Sandro", "Rafael", "Sebastião", "Teodoro",
    "Valentim", "Walter", "Xavier", "Yuri", "Zeca", "Cesar", "Enrico", "Geraldo", "Gilberto", "Jean"
};


char *last_names[100] = {
   "Silva", "Santos", "Oliveira", "Souza", "Pereira", "Costa", "Rodrigues", "Almeida", "Nascimento", "Lima",
    "Araújo", "Fernandes", "Carvalho", "Gomes", "Martins", "Rocha", "Ribeiro", "Alves", "Monteiro", "Mendes",
    "Barros", "Freitas", "Barbosa", "Pinto", "Correia", "Moreira", "Cardoso", "Teixeira", "Cavalcanti", "Dias",
    "Castro", "Campos", "Moura", "Peixoto", "Andrade", "Leal", "Vieira", "Santana", "Machado", "Duarte",
    "Ramos", "Freire", "Amaral", "Tavares", "Matos", "Azevedo", "Braga", "Cunha", "Farias", "Lopes",
    "Macedo", "Nogueira", "Reis", "Xavier", "Branco", "Fonseca", "Pacheco", "Neves", "Borges", "Siqueira",
    "Moraes", "Mello", "Guimaraes", "Figueiredo", "Sales", "Viana", "Monteiro", "Ferreira", "Vargas", "Vasconcelos",
    "Aguiar", "Soares", "Batista", "Parreira", "Campos", "Assis", "Domingues", "Aragao", "Bezerra", "Bittencourt",
    "Carmo", "Chaves", "Coelho", "Diniz", "Espindola", "Esteves", "Falcao", "Farias", "Franco", "Gama",
    "Garcia", "Henriques", "Lacerda", "Lourenco", "Magalhaes", "Medeiros", "Meireles", "Meneses", "Mesquita", "Miranda"
};

char *especialidades[5] = {
    "Dentista",
    "Assistente Odontologico",
    "Recepcionista",
    "Gerente",
    "Faxineiro"
};


// Função para calcular o dígito verificador


char* gerarEspecialidade() {
    // Array de especialidades
    const char* especialidades[] = {
        "Dentista",            // 30%
        "Assistente Odontologico", // 60%
        "Recepcionista",       // 5%
        "Gerente",             // 1%
        "Faxineiro"            // 4%
    };

    // Pesos acumulados em porcentagens
    int pesos[] = {30, 90, 95, 96, 100}; // 30%, 60%, 5%, 1%, 4%

    // Gera um número aleatório de 1 a 100
    int valorAleatorio = rand() % 100 + 1;

    // Determina a especialidade com base no valor aleatório e pesos
    for (int i = 0; i < sizeof(pesos) / sizeof(pesos[0]); i++) {
        if (valorAleatorio <= pesos[i]) {
            return (char*)especialidades[i];
        }
    }

    return NULL; // Caso não encontre (não deve acontecer)
}

int calcularDigito(int cpf[], int tamanho) {
    int soma = 0;
    int multiplicador = 2;

    for (int i = tamanho - 1; i >= 0; i--) {
        soma += cpf[i] * multiplicador;
        multiplicador = (multiplicador == 9) ? 2 : multiplicador + 1;
    }

    int resto = soma % 11;
    return (resto < 2) ? 0 : 11 - resto;
}

char* gerarCPF() {
    int cpf_numeros[9];
    static char cpf[CPF_LENGTH + 1];

    // Inicializa o gerador de números aleatórios
    srand((unsigned int)time(NULL));

    // Gera os 9 primeiros dígitos aleatórios
    for (int i = 0; i < 9; i++) {
        cpf_numeros[i] = rand() % 10;
    }

    // Calcula os dois dígitos verificadores
    int digito1 = calcularDigito(cpf_numeros, 9);
    int digito2 = calcularDigito(cpf_numeros, 9 + 1);

    // Formata o CPF no formato padrão XXX.XXX.XXX-YY
    snprintf(cpf, CPF_LENGTH + 1, "%d%d%d.%d%d%d.%d%d%d-%d%d",
             cpf_numeros[0], cpf_numeros[1], cpf_numeros[2],
             cpf_numeros[3], cpf_numeros[4], cpf_numeros[5],
             cpf_numeros[6], cpf_numeros[7], cpf_numeros[8],
             digito1, digito2);

    return cpf;
}

char* gerarTelefone() {
    // Aloca memória para o número de telefone
    char *telefone = (char *)malloc(16 * sizeof(char));
    if (telefone == NULL) {
        return NULL; // Falha na alocação de memória
    }

    // Inicializa o gerador de números aleatórios
    srand((unsigned int)time(NULL));

    // Gera um DDD aleatório entre 11 e 99
    int ddd = 11 + rand() % 89; // Gera um número entre 11 e 99

    // Gera os 8 dígitos do número de telefone (excluindo o 9 adicional)
    int num = rand() % 100000000; // Gera um número de 0 a 99999999

    // Formata o número de telefone no formato (DDD) 9XXXX-XXXX
    snprintf(telefone, 16, "(%02d) 9%04d-%04d", ddd, num / 10000, num % 10000);

    return telefone;
}

// Função para gerar uma data de nascimento aleatória
char* gerarDataNascimento(int dentista) {
    // Aloca memória para a data de nascimento
    char *data = (char *)malloc(11 * sizeof(char));  // 10 caracteres + 1 para '\0'
    if (data == NULL) {
        return NULL; // Falha na alocação de memória
    }

    // Inicializa o gerador de números aleatórios
    srand((unsigned int)time(NULL));

    // Obtém a data atual
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);

    int anoAtual = tm.tm_year + 1900;
    int mesAtual = tm.tm_mon + 1;
    int diaAtual = tm.tm_mday;

    int anoMin, anoMax;

    if (dentista) {
        // Se dentista, deve ter mais de 18 anos
        anoMax = anoAtual - 18;
        anoMin = anoMax - 65; // Limite máximo de idade (110 anos)
    } else {
        // Se não dentista, pode ter até 110 anos
        anoMax = anoAtual;
        anoMin = anoMax - 100;
    }

    // Gera o ano, mês e dia aleatórios
    int ano = anoMin + rand() % (anoMax - anoMin + 1);
    int mes = 1 + rand() % 12;
    int dia;

    // Ajusta o dia baseado no mês e ano
    if (mes == 2) {
        dia = 1 + rand() % (ano % 4 == 0 ? 29 : 28); // Fevereiro com 28 ou 29 dias
    } else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
        dia = 1 + rand() % 30; // Meses com 30 dias
    } else {
        dia = 1 + rand() % 31; // Meses com 31 dias
    }

    // Formata a data no formato DD/MM/AAAA
    snprintf(data, 11, "%02d/%02d/%04d", dia, mes, ano);

    return data;
}

char* gerarEndereco() {
    // Definições de diferentes partes do endereço
    const char* tiposEndereco[] = {
        "Rua", "Avenida", "Praça", "Travessa", "Alameda", "Largo", "Estrada", "Rodovia", "Boulevard", "Caminho"
    };

    const char* complementosEndereco[] = {
        "Casa", "Apartamento", "Sala", "Cobertura", "Andar", "Bloco", "Prédio", "Lote", "Chácara", "Sítio"
    };

    const char* cidades[] = {
        "São Paulo", "Rio de Janeiro", "Belo Horizonte", "Salvador", "Porto Alegre", "Curitiba", "Brasília", "Fortaleza", "Recife", "Manaus"
    };

    const char* estados[] = {
        "SP", "RJ", "MG", "BA", "RS", "PR", "DF", "CE", "PE", "AM"
    };

    // Inicializa o gerador de números aleatórios
    srand((unsigned int)time(NULL));

    // Gera índices aleatórios para selecionar partes do endereço
    int tipoIndex = rand() % (sizeof(tiposEndereco) / sizeof(tiposEndereco[0]));
    int complementoIndex = rand() % (sizeof(complementosEndereco) / sizeof(complementosEndereco[0]));
    int cidadeIndex = rand() % (sizeof(cidades) / sizeof(cidades[0]));
    int estadoIndex = rand() % (sizeof(estados) / sizeof(estados[0]));

    // Gera número e CEP aleatórios
    int numero = rand() % 9999 + 1; // Número de 1 a 9999
    int cep = rand() % 90000 + 10000; // CEP de 10000 a 99999

    // Aloca memória para o endereço
    char* endereco = (char*)malloc(150 * sizeof(char)); // Tamanho suficiente para o endereço
    if (endereco == NULL) {
        return NULL; // Falha na alocação de memória
    }

    // Formata o endereço
    snprintf(endereco, 150, "%s %d, %s, %s, %s, %s, %05d",
             tiposEndereco[tipoIndex], numero, complementosEndereco[complementoIndex],
             cidades[cidadeIndex], estados[estadoIndex], "Brasil", cep);

    return endereco;
}


char* gerarNome() {
    // Gera um índice aleatório entre 0 e 99
    char *nome_completo = (char *) malloc(sizeof(char) * 50);

    if(nome_completo == NULL){
      return NULL;
    }

    strcpy(nome_completo,first_names[rand() % 100]);
    strcat(nome_completo," ");
    strcat(nome_completo,last_names[rand() % 100]);

    return nome_completo;
}

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
TCliente *criaCliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco) {
    TCliente *cliente = (TCliente *) malloc(sizeof(TCliente));
    if (!cliente) return NULL; // Verificar se a alocação foi bem-sucedida
    memset(cliente, 0, sizeof(TCliente));
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
    fwrite(cliente->nome, sizeof(char), strlen(cliente->nome) + 1, base_clientes);
    fwrite(cliente->cpf, sizeof(char), strlen(cliente->cpf) + 1, base_clientes);
    fwrite(cliente->telefone, sizeof(char), strlen(cliente->telefone) + 1, base_clientes);
    fwrite(cliente->data_nascimento, sizeof(char), strlen(cliente->data_nascimento) + 1, base_clientes);
    fwrite(cliente->endereco, sizeof(char), strlen(cliente->endereco) + 1, base_clientes);

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
void imprimirBase_cliente(FILE *base_clientes) {
    printf("\nImprimindo a base de clientes...\n");
    rewind(base_clientes);
    TCliente *cliente;

    while ((cliente = le_cliente(base_clientes)) != NULL) {
        imprime_cliente(cliente);
        free(cliente); // Liberar memória após imprimir
    }
}


//Obtem o tamanho de um cliente
int tamanho_registro_cliente() {
    return sizeof(int)  //id
           + sizeof(char) * 50 //nome
           + sizeof(char) * 15 //cpf
           + sizeof(char) * 16 //telefone
           + sizeof(char) * 11 //data_nascimento
           + sizeof(char) * 100; //endereco
}

// retorna a quantidade de registros de clientes no arquivo
int qtdRegistros_cliente(FILE *arq) {
    fseek(arq, 0, SEEK_END);
    int tam = trunc(ftell(arq) / tamanho_registro_cliente());
    return tam;
}

// Cria a base de dados desordenada pelo codigo do cliente

/*
void criarBaseDesordenada_cliente(FILE *base_clientes, int tam, int qtdTrocas){

    int vet[tam];
    TCliente *cliente;

    for(int i=0;i<tam;i++)
        vet[i] = i+1;

    embaralha(vet,tam,qtdTrocas);

    printf("\nGerando a base de clientes...\n");

    for (int i=0;i<tam;i++){
        cliente = criaCliente(vet[i],gerarNome(), gerarCPF(), gerarTelefone() ,gerarDataNascimento(false), gerarEndereco());
        salva_cliente(cliente, base_clientes);
    }

    free(cliente);

}
*/

void criarBaseDesordenada(FILE *base_de_dados, int tam, int qtdTrocas,char *tipoPessoa){
    if(strcmp(tipoPessoa,"Cliente") == 0){
        int vet[tam];
        TCliente *cliente;

        for(int i=0;i<tam;i++)
            vet[i] = i+1;

        embaralha(vet,tam,qtdTrocas);

        printf("\nGerando a base de clientes...\n");

        for (int i=0;i<tam;i++){
            cliente = criaCliente(vet[i],gerarNome(), gerarCPF(), gerarTelefone() ,gerarDataNascimento(false), gerarEndereco());
            salva_cliente(cliente, base_de_dados);
        }

        free(cliente);
    }else if(strcmp(tipoPessoa,"Profissional") == 0){
        int vet[tam];
        TProfissional *profissional;

        for(int i=0;i<tam;i++)
            vet[i] = i+1;

        embaralha(vet,tam,qtdTrocas);

        printf("\nGerando a base de profissionais...\n");

        for (int i=0;i<tam;i++){
            profissional = criaProfissional(vet[i], gerarNome(), gerarCPF(), gerarEspecialidade(), gerarTelefone() ,gerarDataNascimento(true), gerarEndereco());
            salva_profissional(profissional, base_de_dados);
        }

        free(profissional);
    }


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
            return cliente;
        }
    }
    return NULL;
}

//Busca Binaria Cliente
TCliente *busca_binaria_cliente(FILE *base_clientes, int tam, int id){
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
TProfissional *criaProfissional(int id, char *nome, char *cpf, char *especialidade, char *telefone, char *data_nascimento, char *endereco) {
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
    printf("%s", profissional->especialidade);
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
           + sizeof(char) * 40 //especialidade
           + sizeof(char) * 16 //telefone
           + sizeof(char) * 11 //data_nascimento
           + sizeof(char) * 100; //endereco
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
        profissional = criaProfissional(vet[i], gerarNome(), gerarCPF(), gerarEspecialidade(), gerarTelefone() ,gerarDataNascimento(true), gerarEndereco());
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
TProfissional *busca_binaria_profissional(FILE *base_profissionais, int tam, int id){
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
THorario *criarHorario(int id, int id_cliente, int id_profissional, char *data, char *hora, bool status) {
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
    fwrite(&horario->status, sizeof(bool), 1, base_horarios);
}

// Le um horario do arquivo  na posicao atual do cursor, retorna um ponteiro para horario lido do arquivo
THorario *le_horario(FILE *base_horarios) {
    THorario *horario = (THorario *) malloc(sizeof(THorario));
    if (horario == NULL) {
        return NULL;
    }

    if (fread(&horario->id, sizeof(int), 1, base_horarios) != 1) {
        free(horario);
        return NULL;
    }

    fread(&horario->id_cliente, sizeof(int), 1, base_horarios);
    fread(&horario->id_profissional, sizeof(int), 1, base_horarios);
    fread(horario->data, sizeof(char), sizeof(horario->data), base_horarios);
    fread(horario->hora, sizeof(char), sizeof(horario->hora), base_horarios);
    fread(&horario->status, sizeof(bool), 1, base_horarios);

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
    printf("\nDiponibilidade: %d",horario->status);
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
        horario = criarHorario(vet[i], 0, 1, "06/08/2024", "15:45", true);
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
THorario *busca_binaria_horario(FILE *base_horarios, int tam, int id){
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
