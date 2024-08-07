//Criar as bases com os seguintes nomes: base_clientes, base_profissionais, base_horarios



#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "Controller.h"
#include <time.h>
#include <string.h>

int main()
{
    FILE *clientesDB,*profissinaisDB,*horariosDB;

    if((clientesDB = fopen("clientes.dat","w+b")) == NULL){
      printf("Erro ao abrir o arquivo dos clientes.\n");
      exit(1);
      return 1;
    }

    if((profissinaisDB = fopen("profissionais.dat","w+b")) == NULL){
      printf("Erro ao abrir o arquivo dos profissionais.\n");
      exit(1);
      return 1;
    }

    if((horariosDB = fopen("horarios.dat","w+b")) == NULL){
      printf("Erro ao abrir o arquivo dos horarios.\n");
      exit(1);
      return 1;
    }

    //criarBaseDesordenada(clientesDB,10,4,"Cliente");
    criaBaseOrdenada(clientesDB,10);
    imprimirBase_cliente(clientesDB);

    //printf("Nome gerado: %s\n CPF %s \nNumero %s\n Data%s Data2 %s\n Endereco %s\n Especialidade %s", gerarNome(), gerarCPF(),gerarTelefone(),gerarDataNascimento(false),gerarDataNascimento(true),gerarEndereco(),gerarEspecialidade());


    return 0;
}
