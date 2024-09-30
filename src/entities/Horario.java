package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Horario implements Serializable {
	private Date horario;
	private List<ListaAgendados> agendamentos;

	public Horario(Date horario) {
		this.horario = horario;
		this.agendamentos = new ArrayList<>(10);
		for (int i = 0; i < 10; i++) {
			this.agendamentos.add(new ListaAgendados(0, 0));
		}
	}

	public Date getHorario() {
		return horario;
	}

	public List<ListaAgendados> getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(List<ListaAgendados> agendamentos) {
		this.agendamentos = agendamentos;
	}

	public void imprimeInformacao() {
		System.out.println("Horário: " + horario);
		for (int i = 0; i < agendamentos.size(); i++) {
			if (agendamentos.get(i).getId_profissional() != 0) {
				System.out.println("Agendamento :" + " Id Profissioanl - " + agendamentos.get(i).getId_profissional()
						+ " Id Cliente - " + agendamentos.get(i).getId_cliente());
			}
		}
	}
}