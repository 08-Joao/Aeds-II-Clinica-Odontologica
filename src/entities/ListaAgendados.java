package entities;

public class ListaAgendados {
	Integer id_cliente;
	Integer id_profissional;

	public ListaAgendados() {
		this.id_cliente = 0;
		this.id_profissional = 0;
	}

	public ListaAgendados(Integer id_cliente, Integer id_profissional) {
		this.id_cliente = id_cliente;
		this.id_profissional = id_profissional;
	}

	public Integer getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(Integer id_cliente) {
		this.id_cliente = id_cliente;
	}

	public Integer getId_profissional() {
		return id_profissional;
	}

	public void setId_profissional(Integer id_profissional) {
		this.id_profissional = id_profissional;
	}
}
