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

char *profissoes[5] = {
    "Dentista",
    "Assistente Odontologico",
    "Recepcionista",
    "Gerente",
    "Faxineiro"
};


time_t converterParaHorario(const char *hora, const char *data) {
    struct tm tm = {0};

    // Parse da string de data no formato "dd/mm/yyyy"
    sscanf(data, "%d/%d/%d", &tm.tm_mday, &tm.tm_mon, &tm.tm_year);

    // Parse da string de hora no formato "hh:mm"
    sscanf(hora, "%d:%d", &tm.tm_hour, &tm.tm_min);

    // Ajuste de tm.tm_year e tm.tm_mon
    tm.tm_year -= 1900; // tm_year é o número de anos desde 1900
    tm.tm_mon -= 1;     // tm_mon varia de 0 (Janeiro) a 11 (Dezembro)

    // Convertendo struct tm para time_t (segundos desde 1 de janeiro de 1970)
    time_t tempo = mktime(&tm);

    return tempo;
}


char* gerarProfissao() {
    srand(time(NULL) ^ (clock() << 16));
    // Array de profissaos
    const char* profissoes[] = {
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

    // Determina a profissao com base no valor aleatório e pesos
    for (int i = 0; i < sizeof(pesos) / sizeof(pesos[0]); i++) {
        if (valorAleatorio <= pesos[i]) {
            return (char*)profissoes[i];
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


/* Calcular Tamanho da Base*/
size_t calculaTamanhoBase(FILE *base_de_dados, size_t tamanhoRegistro) {
    fseek(base_de_dados, 0, SEEK_END);  // Move o ponteiro para o final do arquivo
    long tamanhoArquivo = ftell(base_de_dados);  // Obtém o tamanho do arquivo em bytes
    fseek(base_de_dados, 0, SEEK_SET);  // Retorna o ponteiro para o início do arquivo

    return tamanhoArquivo / tamanhoRegistro;  // Calcula o número de registros
}

/* Criação de Pessoas */
TCliente *criaCliente(int id, char *nome, char *cpf, char *telefone, char *data_nascimento, char *endereco) {
    TCliente *cliente = (TCliente *)malloc(sizeof(TCliente));
    if (cliente != NULL) {
        cliente->id = id;
        strncpy(cliente->nome, nome, sizeof(cliente->nome) - 1);
        strncpy(cliente->cpf, cpf, sizeof(cliente->cpf) - 1);
        strncpy(cliente->telefone, telefone, sizeof(cliente->telefone) - 1);
        strncpy(cliente->data_nascimento, data_nascimento, sizeof(cliente->data_nascimento) - 1);
        strncpy(cliente->endereco, endereco, sizeof(cliente->endereco) - 1);
    }
    return cliente;
}

TProfissional* criaProfissional(int id, char* nome, char* cpf, char* profissao, char* telefone, char* data_nascimento, char* endereco) {
    TProfissional *profissional = (TProfissional *)malloc(sizeof(TProfissional));
    if (profissional != NULL) {
        profissional->id = id;
        strncpy(profissional->nome, nome, sizeof(profissional->nome) - 1);
        strncpy(profissional->cpf, cpf, sizeof(profissional->cpf) - 1);
        strncpy(profissional->profissao, profissao, sizeof(profissional->profissao) - 1);
        strncpy(profissional->telefone, telefone, sizeof(profissional->telefone) - 1);
        strncpy(profissional->data_nascimento, data_nascimento, sizeof(profissional->data_nascimento) - 1);
        strncpy(profissional->endereco, endereco, sizeof(profissional->endereco) - 1);
    }
    return profissional;
}


void salva_cliente(TCliente *cliente, FILE *base_de_dados) {


    if (cliente == NULL || base_de_dados == NULL) {
        printf("Cliente ou base de dados inválido.\n");
        return;
    }

    // Localiza a posição correta no arquivo e grava o cliente
    fseek(base_de_dados, (cliente->id - 1) * sizeof(TCliente), SEEK_SET);
    fwrite(cliente, sizeof(TCliente), 1, base_de_dados);
    fflush(base_de_dados);
}

void salva_profissional(TProfissional *profissional, FILE *base_de_dados) {


    if (profissional == NULL || base_de_dados == NULL) {
        printf("Profissional ou base de dados inválido.\n");
        return;
    }

    // Localiza a posição correta no arquivo e grava o profissional
    fseek(base_de_dados, (profissional->id - 1) * sizeof(TProfissional), SEEK_SET);
    fwrite(profissional, sizeof(TProfissional), 1, base_de_dados);
    fflush(base_de_dados);
}

void salva_horario(THorario *horario, FILE *base_de_dados) {

    if (horario == NULL || base_de_dados == NULL) {
        printf("Horário ou base de dados inválido.\n");
        return;
    }

    // Busca a posição correta para o horário no arquivo e grava o horário
    THorario temp_horario;
    fseek(base_de_dados, 0, SEEK_SET);

    while (fread(&temp_horario, sizeof(THorario), 1, base_de_dados) == 1) {
        if (temp_horario.horario == horario->horario) {
            fseek(base_de_dados, -sizeof(THorario), SEEK_CUR);
            fwrite(horario, sizeof(THorario), 1, base_de_dados);
            fflush(base_de_dados);
            return;
        }
    }

    printf("Horário não encontrado na base de dados.\n");
}

/* Le Informações do Arquivo */
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


TProfissional * le_profissional(FILE* base_profissional) {
    TProfissional* profissional= (TProfissional*)malloc(sizeof(TProfissional));
    if (profissional == NULL) {
        return NULL;
    }

    if (fread(profissional, sizeof(TProfissional), 1, base_profissional) != 1) {
        free(profissional);
        return NULL;
    }

    return profissional;
}

THorario *le_horario(FILE* base_horario) {
    THorario* horario= (THorario*)malloc(sizeof(THorario));
    if (horario == NULL) {
        return NULL;
    }

    if (fread(horario, sizeof(THorario), 1, base_horario) != 1) {
        free(horario);
        return NULL;
    }

    return horario;
}

/* Adição e Remoção de Pessoas*/

void adicionaPessoa(FILE* base_de_dados, char* tipoPessoa) {
    char nome[100];
    char cpf[CPF_LENGTH];
    char profissao[40];
    char telefone[16];
    char data_nascimento[11];
    char endereco[100];

    printf("\nNome: ");
    fgets(nome, sizeof(nome), stdin);
    nome[strcspn(nome, "\n")] = '\0';

    printf("\nCPF (xxx.xxx.xxx-xx): ");
    fgets(cpf, sizeof(cpf), stdin);
    cpf[strcspn(cpf, "\n")] = '\0';

    getchar();
    printf("\nTelefone (xx) xxxxx-xxxx: ");
    fgets(telefone, sizeof(telefone), stdin);
    telefone[strcspn(telefone, "\n")] = '\0';

    getchar();
    printf("\nData de Nascimento: ");
    fgets(data_nascimento, sizeof(data_nascimento), stdin);
    data_nascimento[strcspn(data_nascimento, "\n")] = '\0';

    getchar();
    printf("\nEndereco: ");
    fgets(endereco, sizeof(endereco), stdin);
    endereco[strcspn(endereco, "\n")] = '\0';

    if (strcmp(tipoPessoa, "Cliente") == 0) {
        int id = calculaTamanhoBase(base_de_dados, sizeof(TCliente)) + 1;
        TCliente* cliente = criaCliente(id, nome, cpf, telefone, data_nascimento, endereco);
        salva_cliente(cliente, base_de_dados);
        free(cliente);
    } else if (strcmp(tipoPessoa, "Profissional") == 0) {
        printf("\nProfissao: ");
        fgets(profissao, sizeof(profissao), stdin);
        profissao[strcspn(profissao, "\n")] = '\0';

        int id = calculaTamanhoBase(base_de_dados, sizeof(TProfissional)) + 1;
        TProfissional* profissional = criaProfissional(id, nome, cpf, profissao, telefone, data_nascimento, endereco);
        salva_profissional(profissional, base_de_dados);
        free(profissional);
    } else {
        printf("Tipo de pessoa inválido.\n");
    }
}


/* Mostra Informações */
void imprime_cliente(TCliente* cliente) {
    if (cliente != NULL) {
        printf("\n-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n");
        printf("ID: %d\nNome: %s\nCPF: %s\nTelefone: %s\nData de Nascimento: %s\nEndereco: %s\n", cliente->id, cliente->nome, cliente->cpf, cliente->telefone, cliente->data_nascimento, cliente->endereco);
    }
}

void imprime_profissional(TProfissional* profissional) {
    if (profissional != NULL) {
        printf("\n-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n");
        printf("ID: %d\nNome: %s\nCPF: %s\nProfissao: %s\nTelefone: %s\nData de Nascimento: %s\nEndereco: %s\n", profissional->id, profissional->nome, profissional->cpf,profissional->profissao, profissional->telefone, profissional->data_nascimento, profissional->endereco);
    }
}

void imprime_horario(THorario* horario) {
    if (horario != NULL) {
        printf("\n-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-\n");
        printf("Horario: %s", ctime(&horario->horario));
        printf("Status: %s\n", horario->status ? "Disponível" : "Indisponível");

        printf("Lista de Agendamentos:\n");
        for (int i = 0; i < 10; i++) {
            if (horario->listaHorario[i].id_profissional != -1 && horario->listaHorario[i].id_cliente != -1) {
                printf("  Profissional ID: %d, Cliente ID: %d\n", horario->listaHorario[i].id_profissional, horario->listaHorario[i].id_cliente);
            }
        }
    }
}


//Imprime toda a base de dados de cliente
void imprimirBase(FILE* base_de_dados, char* tipoDeDado) {
    fseek(base_de_dados, 0, SEEK_SET);

    if (strcmp(tipoDeDado, "Cliente") == 0) {
        TCliente* cliente;

        while ((cliente = le_cliente(base_de_dados)) != NULL) {
            imprime_cliente(cliente);
            free(cliente);
        }
    } else if (strcmp(tipoDeDado, "Profissional") == 0) {
        TProfissional* profissional;

        while ((profissional = le_profissional(base_de_dados)) != NULL) {
            imprime_profissional(profissional);
            free(profissional);
        }
    } else if (strcmp(tipoDeDado, "Horario") == 0) {
        THorario* horario;

        while ((horario = le_horario(base_de_dados)) != NULL) {
            imprime_horario(horario);
            free(horario);
        }
    }
}

/* Sistema de Buscas */

TCliente* busca_cliente(FILE* base_de_dados, int tam, int id) {
    TCliente* cliente;
    fseek(base_de_dados, 0, SEEK_SET);

    for (int i = 0; i < tam; i++) {
        cliente = le_cliente(base_de_dados);
        if (cliente->id == id) {
            return cliente;
        }
        free(cliente);
    }
    return NULL;
}

TCliente* busca_binaria_cliente(FILE* base_de_dados, int tam, int id) {
    int inicio = 0, fim = tam - 1, meio;
    TCliente* cliente = (TCliente*)malloc(sizeof(TCliente));
    if (cliente == NULL) {
        return NULL;
    }

    fseek(base_de_dados, 0, SEEK_SET);

    while (inicio <= fim) {
        meio = (inicio + fim) / 2;
        fseek(base_de_dados, meio * sizeof(TCliente), SEEK_SET);
        cliente = le_cliente(base_de_dados);

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



TProfissional* busca_profissional(FILE* base_de_dados, int tam, int id) {
    TProfissional* profissional;
    fseek(base_de_dados, 0, SEEK_SET);

    for (int i = 0; i < tam; i++) {
        profissional = le_profissional(base_de_dados);
        if (profissional->id == id) {
            return profissional;
        }
        free(profissional);
    }
    return NULL;
}

TProfissional* busca_binaria_profissional(FILE* base_de_dados, int tam, int id) {
    int inicio = 0, fim = tam - 1, meio;
    TProfissional* profissional = (TProfissional*)malloc(sizeof(TProfissional));
    if (profissional == NULL) {
        return NULL;
    }

    fseek(base_de_dados, 0, SEEK_SET);

    while (inicio <= fim) {
        meio = (inicio + fim) / 2;
        fseek(base_de_dados, meio * sizeof(TProfissional), SEEK_SET);
        profissional = le_profissional(base_de_dados);

        if (profissional->id == id) {
            return profissional;
        } else if (profissional->id < id) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }

        free(profissional);
    }

    return NULL;
}

THorario* busca_horario(FILE* base_de_dados, int tam, time_t horario) {
    THorario* horario_atual;
    fseek(base_de_dados, 0, SEEK_SET);

    for (int i = 0; i < tam; i++) {
        horario_atual = le_horario(base_de_dados);
        if (horario_atual->horario == horario) {
            return horario_atual;
        }
        free(horario_atual);
    }
    return NULL;
}

THorario* busca_binaria_horario(FILE* base_de_dados, int tam, time_t horario) {
    int inicio = 0, fim = tam - 1, meio;
    THorario* horario_atual = (THorario*)malloc(sizeof(THorario));
    if (horario_atual == NULL) {
        return NULL;
    }

    fseek(base_de_dados, 0, SEEK_SET);

    while (inicio <= fim) {
        meio = (inicio + fim) / 2;
        fseek(base_de_dados, meio * sizeof(THorario), SEEK_SET);
        horario_atual = le_horario(base_de_dados);

        if (horario_atual->horario == horario) {
            return horario_atual;
        } else if (difftime(horario_atual->horario, horario) < 0) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }

        free(horario_atual);
    }

    return NULL;
}


/* Verificar ordenao das Bases */

bool verificaOrdenacaoPessoa(FILE *base_de_dados,char* tipoPessoa){
  if(strcmp(tipoPessoa,"Cliente") == 0){
    TCliente* cliente;
    fseek(base_de_dados, 0, SEEK_SET);

    for (int i = 0; i < 1000; i++) {
        cliente = le_cliente(base_de_dados);

        if(cliente == NULL){
            return true;
        }

        if (cliente->id != i + 1) {
            return false;
        }

        free(cliente);

    }

    return true;


  }else if((strcmp(tipoPessoa,"Profissional") == 0)){
    TProfissional* profissional;
    fseek(base_de_dados, 0, SEEK_SET);

    for (int i = 0; i < 1000; i++) {
        profissional = le_profissional(base_de_dados);

        if(profissional == NULL){
            return true;
        }

        if (profissional->id != i + 1) {
            return false;
        }

        free(profissional);

    }

    return true;
  }
}

bool verificaOrdenacaoHorario(FILE* base_de_dados){
    THorario* tempHorario;
    fseek(base_de_dados,0,SEEK_SET);

    time_t lastTime = NULL;

    for(int i = 0;i < 1000;i++){
        tempHorario = le_horario(base_de_dados);

        if(tempHorario == NULL){
            return true;
        }

        if(lastTime == NULL){
            lastTime = tempHorario->horario;
        }else{
            if(lastTime > tempHorario->horario){
                return false;
            }else{
                lastTime = tempHorario->horario;
            }
        }
    }

    free(tempHorario);
    return true;
}


/* Cria Base de Dados */
void criarBaseDesordenada(FILE *base_de_dados, int tam, int qtdTrocas,char *tipoPessoa){
    if(strcmp(tipoPessoa,"Cliente") == 0){
        int vet[tam];
        TCliente *cliente;

        for(int i=0;i<tam;i++)
            vet[i] = i+1;

        embaralha(vet,tam,qtdTrocas);

        printf("\nGerando a base desordenada de clientes...\n");

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

        printf("\nGerando a base desordenada de profissionais...\n");

        for (int i=0;i<tam;i++){
            profissional = criaProfissional(vet[i], gerarNome(), gerarCPF(), gerarProfissao(), gerarTelefone() ,gerarDataNascimento(true), gerarEndereco());
            salva_profissional(profissional, base_de_dados);
        }

        free(profissional);
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


/* Criar Base */

void criarBaseHorarios(FILE *base_de_dados, int qtdTrocas) {
    THorario horarioBase;
    struct tm tm;
    memset(&tm, 0, sizeof(struct tm));

    // Inicializando a data para 13/08/2024
    tm.tm_year = 2024 - 1900;
    tm.tm_mon = 7;  // Agosto (0-indexed)
    tm.tm_mday = 13;
    tm.tm_hour = 8; // Início às 08:00
    tm.tm_min = 0;
    tm.tm_sec = 0;

    // Variável para controlar 1 mês de datas
    time_t dataInicial = mktime(&tm);
    time_t dataFinal = dataInicial + (30 * 24 * 60 * 60); // Aproximadamente 1 mês

    // Calcula o número total de horários
    int numHorarios = 0;
    time_t temp_time = dataInicial;
    while (temp_time <= dataFinal) {
        if (tm.tm_wday != 0 && tm.tm_wday != 6) { // Não é sábado ou domingo
            numHorarios++;
        }
        tm.tm_min += 10; // Intervalo de 10 minutos
        if (tm.tm_min >= 60) {
            tm.tm_min = 0;
            tm.tm_hour += 1;
        }
        if (tm.tm_hour >= 18) {
            tm.tm_hour = 8;
            tm.tm_min = 0;
            tm.tm_mday += 1; // Avança um dia
        }
        temp_time = mktime(&tm);
    }

    // Cria um array de índices e embaralha
    int indices[numHorarios];
    for (int i = 0; i < numHorarios; i++) {
        indices[i] = i;
    }
    embaralha(indices, numHorarios, qtdTrocas);

    // Gera e salva os horários na ordem embaralhada
    printf("\nGerando a base desordenada de horários...\n");

    int idx = 0;
    tm.tm_year = 2024 - 1900;
    tm.tm_mon = 7;
    tm.tm_mday = 13;
    tm.tm_hour = 8;
    tm.tm_min = 0;

    while (1) {
        if (tm.tm_wday != 0 && tm.tm_wday != 6) { // Não é sábado ou domingo
            if (idx < numHorarios) {
                // Define o horário
                horarioBase.horario = mktime(&tm);

                // Inicializa os agendamentos e o status
                for (int j = 0; j < 10; j++) {
                    horarioBase.listaHorario[j].id_profissional = -1;
                    horarioBase.listaHorario[j].id_cliente = -1;
                }
                horarioBase.status = true;

                // Salva o horário na base de dados na posição desordenada
                fseek(base_de_dados, indices[idx] * sizeof(THorario), SEEK_SET);
                fwrite(&horarioBase, sizeof(THorario), 1, base_de_dados);

                idx++;
            }
        }

        tm.tm_min += 10; // Intervalo de 10 minutos
        if (tm.tm_min >= 60) {
            tm.tm_min = 0;
            tm.tm_hour += 1;
        }
        if (tm.tm_hour >= 18) {
            tm.tm_hour = 8;
            tm.tm_min = 0;
            tm.tm_mday += 1; // Avança um dia
        }
        time_t temp_time = mktime(&tm);
        if (temp_time > dataFinal) {
            break;
        }
    }
}


/* Ordenação */

int compararClientes(const void *a, const void *b) {
    TCliente *clienteA = (TCliente *)a;
    TCliente *clienteB = (TCliente *)b;
    return clienteA->id - clienteB->id;
}

// Função de comparação para ordenar Profissionais por ID
int compararProfissionais(const void *a, const void *b) {
    TProfissional *profissionalA = (TProfissional *)a;
    TProfissional *profissionalB = (TProfissional *)b;
    return profissionalA->id - profissionalB->id;
}

// Função de comparação para ordenar Horarios por timestamp
int compararHorarios(const void *a, const void *b) {
    THorario *horarioA = (THorario *)a;
    THorario *horarioB = (THorario *)b;
    return difftime(horarioA->horario, horarioB->horario);
}

// Função para ordenar base de dados
void ordenarBase(FILE *base_de_dados, const char *tipo) {
    fseek(base_de_dados, 0, SEEK_END);
    long tamanho = ftell(base_de_dados);
    fseek(base_de_dados, 0, SEEK_SET);

    if (strcmp(tipo, "Cliente") == 0) {
        size_t tamanho_registro = sizeof(TCliente);
        size_t num_registros = tamanho / tamanho_registro;

        TCliente *clientes = malloc(tamanho);
        if (clientes == NULL) {
            perror("Falha ao alocar memória");
            return;
        }

        fread(clientes, tamanho_registro, num_registros, base_de_dados);
        qsort(clientes, num_registros, tamanho_registro, compararClientes);

        fseek(base_de_dados, 0, SEEK_SET);
        fwrite(clientes, tamanho_registro, num_registros, base_de_dados);
        free(clientes);
    } else if (strcmp(tipo, "Profissional") == 0) {
        size_t tamanho_registro = sizeof(TProfissional);
        size_t num_registros = tamanho / tamanho_registro;

        TProfissional *profissionais = malloc(tamanho);
        if (profissionais == NULL) {
            perror("Falha ao alocar memória");
            return;
        }

        fread(profissionais, tamanho_registro, num_registros, base_de_dados);
        qsort(profissionais, num_registros, tamanho_registro, compararProfissionais);

        fseek(base_de_dados, 0, SEEK_SET);
        fwrite(profissionais, tamanho_registro, num_registros, base_de_dados);
        free(profissionais);
    } else if (strcmp(tipo, "Horario") == 0) {
        size_t tamanho_registro = sizeof(THorario);
        size_t num_registros = tamanho / tamanho_registro;

        THorario *horarios = malloc(tamanho);
        if (horarios == NULL) {
            perror("Falha ao alocar memória");
            return;
        }

        fread(horarios, tamanho_registro, num_registros, base_de_dados);
        qsort(horarios, num_registros, tamanho_registro, compararHorarios);

        fseek(base_de_dados, 0, SEEK_SET);
        fwrite(horarios, tamanho_registro, num_registros, base_de_dados);
        free(horarios);
    } else {
        fprintf(stderr, "Tipo desconhecido: %s\n", tipo);
    }
}
/* Agendamento de Consutas */


void arredondaMinutos(struct tm *tm_info) {
    // Arredonda os minutos para o intervalo mais próximo de 10 minutos
    int minutos = tm_info->tm_min;
    tm_info->tm_min = (minutos + 5) / 10 * 10;

    // Se os minutos foram arredondados para 60, ajustar para a próxima hora
    if (tm_info->tm_min == 60) {
        tm_info->tm_min = 0;
        tm_info->tm_hour++;
    }
}

void exibirListaDentistas(FILE* base_de_dados, int tamanho) {
    int qtd = 0;
    TProfissional* profissional;
    fseek(base_de_dados, 0, SEEK_SET);

    for (int i = 0; i < tamanho; i++) {
        profissional = le_profissional(base_de_dados);

        if (profissional == NULL) {
            printf("Erro ao ler profissional.\n");
            break;
        }

        if (strcmp(profissional->profissao, "Dentista") == 0) {
            imprime_profissional(profissional);
            qtd++;
        }

        free(profissional);

        if (qtd == 10) {
            return;
        }
    }

    if (qtd == 0) {
        printf("Nenhum dentista encontrado.\n");
    }
}


bool verificarDisponibilidade(THorario* horario, int id_profissional, int id_cliente) {
    for (int i = 0; i < 10; i++) {
        if (horario->listaHorario[i].id_profissional == id_profissional ||
            horario->listaHorario[i].id_cliente == id_cliente) {
            return false;
        }
    }
    return true;
}


void marcarConsulta(FILE* base_horario, THorario* horario, int id_dentista, int id_cliente) {
    for (int i = 0; i < 10; i++) {

        if (horario->listaHorario[i].id_profissional == -1 && horario->listaHorario[i].id_cliente == -1) {

            horario->listaHorario[i].id_profissional = id_dentista;
            horario->listaHorario[i].id_cliente = id_cliente;


            salva_horario(horario, base_horario);
            imprime_horario(horario);

            if(i == 0){
                horario->status = false;
            }
            break;
        }
    }
}


void editar(FILE* base_de_dados) {
    TProfissional* profissional = busca_binaria_profissional(base_de_dados, 15, 5);
    if (profissional == NULL) {
        printf("Profissional não encontrado.\n");
        return;
    }

    strcpy(profissional->profissao, "VAGABUNDO");

    salva_profissional(profissional,base_de_dados);

    free(profissional);
}


void agendarConsulta(FILE* base_clientes, FILE* base_profissionais, FILE* base_horarios) {
    char horario[6];
    char data[9];
    struct tm tm_info = {0};
    time_t horario_consulta;
    THorario* horarioTemporario;

    // Loop para garantir a entrada correta do horário e da data
    while (1) {
        printf("Informe um horario (hh:mm) ou 0 para sair: ");
        fgets(horario, sizeof(horario), stdin);
        horario[strcspn(horario, "\n")] = 0;

        if (strcmp(horario, "0") == 0) {
            printf("Operação cancelada.\n");
            return;
        }

        getchar();
        printf("Informe um dia (dd/mm/aa) ou 0 para sair: ");
        fgets(data, sizeof(data), stdin);
        data[strcspn(data, "\n")] = 0;

        if (strcmp(data, "0") == 0) {
            printf("Operação cancelada.\n");
            return;
        }

        // Verifica se o horário está no formato correto (hh:mm)
        if (strlen(horario) != 5 || horario[2] != ':') {
            printf("Formato de horário inválido. Use hh:mm.\n");
            continue;
        }

        // Verifica se a data está no formato correto (dd/mm/aa)
        if (strlen(data) != 8 || data[2] != '/' || data[5] != '/') {
            printf("Formato de data inválido. Use dd/mm/aa.\n");
            continue;
        }

        // Combina data e horário em uma string para análise
        char datetime[16];
        snprintf(datetime, sizeof(datetime), "%s %s", data, horario);

        // Analisar a string de data e hora para preencher tm_info
        if (strptime(datetime, "%d/%m/%y %H:%M", &tm_info) == NULL) {
            printf("Formato de data ou horário inválido.\n");
            continue;
        }

        // Arredonda os minutos para o intervalo de 10 em 10 minutos
        arredondaMinutos(&tm_info);

        // Converte tm_info para time_t
        horario_consulta = mktime(&tm_info);

        if (horario_consulta == -1) {
            printf("Erro ao converter para time_t.\n");
            continue;
        }

        break;
    }

    // Caso a base não esteja ordenada, ele irá ordenar
    if (verificaOrdenacaoHorario(base_horarios) == 0) {
        ordenarBase(base_horarios, "Horario");
    }

    horarioTemporario = busca_binaria_horario(base_horarios, calculaTamanhoBase(base_horarios, sizeof(THorario)), horario_consulta);

    if (horarioTemporario) {
        exibirListaDentistas(base_profissionais, calculaTamanhoBase(base_profissionais, sizeof(TProfissional)));

        bool id_dentValido = false;
        bool id_clieValido = false;
        int id_dentista;
        int id_cliente;

        while (!id_dentValido) {
            printf("\nInforme um ID de um dentista (Seja os da lista mostrada acima ou qualquer outro ID de um dentista)\n");
            scanf("%d", &id_dentista);

            if (verificaOrdenacaoPessoa(base_profissionais, "Profissional") == 0) {
                ordenarBase(base_profissionais, "Profissional");
            }

            TProfissional* tp = busca_binaria_profissional(base_profissionais, calculaTamanhoBase(base_profissionais, sizeof(TProfissional)), id_dentista);
            if (tp != NULL && strcmp(tp->profissao, "Dentista") == 0) {
                id_dentValido = true;

                while (!id_clieValido) {
                    printf("\nInforme um ID de um cliente válido\n");
                    scanf("%d", &id_cliente);


                    if (verificaOrdenacaoPessoa(base_clientes, "Cliente") == 0) {
                        ordenarBase(base_clientes, "Cliente");
                    }

                    TCliente* tc = busca_binaria_cliente(base_clientes, calculaTamanhoBase(base_clientes, sizeof(TCliente)), id_cliente);
                    if (tc != NULL) {
                        id_clieValido = true;
                        marcarConsulta(base_horarios, horarioTemporario, id_dentista, id_cliente);
                    }

                    free(tc);
                }
            } else {
                printf("\nProfissional inexistente.\n");
            }

            free(tp);
        }
        getchar();
    } else {
        printf("\nHorário inexistente.\n");
    }
}
