package utils;

public enum CaminhoArquivo {
    CLIENTES("clientes.dat"),
    PROFISSIONAIS("profissionais.dat"),
    HORARIOS("horarios.dat");

    public final String caminho;

    CaminhoArquivo(String caminho) {
        this.caminho = caminho;
    }
}
