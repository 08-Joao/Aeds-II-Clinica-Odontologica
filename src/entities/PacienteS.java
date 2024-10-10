package entities;

import java.io.Serializable;

public class PacienteS implements Serializable {
 int id;
 String nome;
 String telefone;
 String dataNascimento;

 public PacienteS(int id, String nome, String telefone, String dataNascimento) {
     this.id = id;
     this.nome = nome;
     this.telefone = telefone;
     this.dataNascimento = dataNascimento;
 }

 public int getId() {
     return this.id;
 }

 public void setId(int id) {
     this.id = id;
 }

 public String getNome() {
     return this.nome;
 }

 public void setNome(String nome) {
     this.nome = nome;
 }

 public String getTelefone() {
     return this.telefone;
 }

 public void setTelefone(String telefone) {
     this.telefone = telefone;
 }

 public String getDataNascimento() {
     return this.dataNascimento;
 }

 public void setDataNascimento(String dataNascimento) {
     this.dataNascimento = dataNascimento;
 }
}
