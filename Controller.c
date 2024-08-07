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
    srand(time(NULL) ^ (clock() << 16));
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
    srand(time(NULL) ^ (clock() << 16));

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
    srand(time(NULL) ^ (clock() << 16));

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
    srand(time(NULL) ^ (clock() << 16));

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
    srand(time(NULL) ^ (clock() << 16));

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
    srand(time(NULL) ^ (clock() << 16));
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
    srand(time(NULL) ^ (clock() << 16));
    for (int i = 0; i <= trocas; i++) {
        int j = rand() % (max-1);
        int k = rand() % (max-1);
        int tmp = vet[j];
        vet[j] = vet[k];
        vet[k] = tmp;
    }
}


/* Criação de Pessoas */
// Cria cliente
TCliente* criaCliente(int id, char* nome, char* cpf, char* telefone, char* data_nascimento, char* endereco) {
    TCliente* cliente = (TCliente*)malloc(sizeof(TCliente));
    if (cliente == NULL) {
        return NULL;
    }

    cliente->id = id;

    strncpy(cliente->nome, nome, sizeof(cliente->nome) - 1);
    cliente->nome[sizeof(cliente->nome) - 1] = '\0';

    strncpy(cliente->cpf, cpf, sizeof(cliente->cpf) - 1);
    cliente->cpf[sizeof(cliente->cpf) - 1] = '\0';

    strncpy(cliente->telefone, telefone, sizeof(cliente->telefone) - 1);
    cliente->telefone[sizeof(cliente->telefone) - 1] = '\0';

    strncpy(cliente->data_nascimento, data_nascimento, sizeof(cliente->data_nascimento) - 1);
    cliente->data_nascimento[sizeof(cliente->data_nascimento) - 1] = '\0';

    strncpy(cliente->endereco, endereco, sizeof(cliente->endereco) - 1);
    cliente->endereco[sizeof(cliente->endereco) - 1] = '\0';

    return cliente;
}

TProfissional* criaProfissional(int id, char* nome, char* cpf, char* especialidade, char* telefone, char* data_nascimento, char* endereco) {
    TProfissional* profissional = (TProfissional*)malloc(sizeof(TProfissional));
    if (profissional == NULL) {
        return NULL;
    }

    profissional->id = id;

    strncpy(profissional->nome, nome, sizeof(profissional->nome) - 1);
    profissional->nome[sizeof(profissional->nome) - 1] = '\0';

    strncpy(profissional->cpf, cpf, sizeof(profissional->cpf) - 1);
    profissional->cpf[sizeof(profissional->cpf) - 1] = '\0';

    strncpy(profissional->especialidade, especialidade, sizeof(profissional->especialidade) - 1);
    profissional->especialidade[sizeof(profissional->especialidade) - 1] = '\0';

    strncpy(profissional->telefone, telefone, sizeof(profissional->telefone) - 1);
    profissional->telefone[sizeof(profissional->telefone) - 1] = '\0';

    strncpy(profissional->data_nascimento, data_nascimento, sizeof(profissional->data_nascimento) - 1);
    profissional->data_nascimento[sizeof(profissional->data_nascimento) - 1] = '\0';

    strncpy(profissional->endereco, endereco, sizeof(profissional->endereco) - 1);
    profissional->endereco[sizeof(profissional->endereco) - 1] = '\0';

    return profissional;
}



// Salva cliente arquivo base_clientes, na posicao atual do cursor
void salva_cliente(TCliente* cliente, FILE* base_clientes) {
    if (base_clientes != NULL) {
        fwrite(cliente, sizeof(TCliente), 1, base_clientes);
    }
}

/*
void salva_profissional(TProfissional *profissional, FILE *base_profissionais){
    if (base_profissionais != NULL) {
        fwrite(profissional, sizeof(TProfissional), 1, base_profissionais);
    }
}

*/

// Le um cliente do arquivo  na posicao atual do cursor, retorna um ponteiro para cliente lido do arquivo
TCliente* le_cliente(FILE* base_clientes) {
    TCliente* cliente = (TCliente*)malloc(sizeof(TCliente));
    if (cliente == NULL) {
        return NULL;
    }

    if (fread(cliente, sizeof(TCliente), 1, base_clientes) != 1) {
        free(cliente);
        return NULL;
    }

    return cliente;
}

// Imprime cliente
void imprime_cliente(TCliente* cliente) {
    if (cliente != NULL) {
        printf("\n-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n");
        printf("ID: %d\nNome: %s\nCPF: %s\nTelefone: %s\nData de Nascimento: %s\nEndereco: %s\n", cliente->id, cliente->nome, cliente->cpf, cliente->telefone, cliente->data_nascimento, cliente->endereco);
    }
}


//Imprime toda a base de dados de cliente
void imprimirBase_cliente(FILE* base_clientes) {
    TCliente* cliente;
    fseek(base_clientes, 0, SEEK_SET);

    while ((cliente = le_cliente(base_clientes)) != NULL) {
        imprime_cliente(cliente);
        free(cliente);
    }
}


//Obtem o tamanho de um cliente
int tamanho_registro_cliente() {
    return sizeof(TCliente);
}

int qtdRegistros_cliente(FILE* base_clientes) {
    fseek(base_clientes, 0, SEEK_END);
    return ftell(base_clientes) / tamanho_registro_cliente();
}

// Cria a base de dados desordenada pelo codigo do cliente



//Busca sequencial de cliente
TCliente* busca_cliente(FILE* base_clientes, int tam, int id) {
    TCliente* cliente;
    fseek(base_clientes, 0, SEEK_SET);

    for (int i = 0; i < tam; i++) {
        cliente = le_cliente(base_clientes);
        if (cliente->id == id) {
            return cliente;
        }
        free(cliente);
    }
    return NULL;
}

//Busca Binaria Cliente
TCliente* busca_binaria_cliente(FILE* base_clientes, int tam, int id) {
    int inicio = 0, fim = tam - 1, meio;
    TCliente* cliente = (TCliente*)malloc(sizeof(TCliente));
    if (cliente == NULL) {
        return NULL;
    }

    fseek(base_clientes, 0, SEEK_SET);

    while (inicio <= fim) {
        meio = (inicio + fim) / 2;
        fseek(base_clientes, meio * sizeof(TCliente), SEEK_SET);
        cliente = le_cliente(base_clientes);

        if (cliente->id == id) {
            return cliente;
        } else if (cliente->id < id) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }

        free(cliente);
    }

    return NULL;
}



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
        /*int vet[tam];
        TProfissional *profissional;

        for(int i=0;i<tam;i++)
            vet[i] = i+1;

        embaralha(vet,tam,qtdTrocas);

        printf("\nGerando a base de profissionais...\n");

        for (int i=0;i<tam;i++){
            profissional = criaProfissional(vet[i], gerarNome(), gerarCPF(), gerarEspecialidade(), gerarTelefone() ,gerarDataNascimento(true), gerarEndereco());
            salva_profissional(profissional, base_de_dados);
        }

        free(profissional);*/
    }


}


void criaBaseOrdenada(FILE* base_clientes, int tamanho) {
    for (int i = 0; i < tamanho; i++) {
        char* nome = gerarNome();
        TCliente* cliente = criaCliente(i + 1, nome, gerarCPF(), gerarTelefone(), gerarDataNascimento(false), gerarEndereco());
        salva_cliente(cliente, base_clientes);
        free(cliente);
    }
}

