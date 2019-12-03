package br.gov.al.maceio.sishosp.hosp.model.dto;

public class AtalhosAmbulatorialDTO {

    private Boolean atalhoCadastroPaciente;
    private Boolean atalhoInsercaoPaciente;
    private Boolean atalhoLaudo;
    private Boolean atalhoAgenda;
    private Boolean atalhoConsultarAgendamentos;
    private Boolean atalhoAtendimentos;

    public AtalhosAmbulatorialDTO() {
        atalhoCadastroPaciente = false;
        atalhoInsercaoPaciente = false;
        atalhoLaudo = false;
        atalhoAgenda = false;
        atalhoConsultarAgendamentos = false;
        atalhoAtendimentos = false;
    }

    public Boolean getAtalhoCadastroPaciente() {
        return atalhoCadastroPaciente;
    }

    public void setAtalhoCadastroPaciente(Boolean atalhoCadastroPaciente) {
        this.atalhoCadastroPaciente = atalhoCadastroPaciente;
    }

    public Boolean getAtalhoInsercaoPaciente() {
        return atalhoInsercaoPaciente;
    }

    public void setAtalhoInsercaoPaciente(Boolean atalhoInsercaoPaciente) {
        this.atalhoInsercaoPaciente = atalhoInsercaoPaciente;
    }

    public Boolean getAtalhoLaudo() {
        return atalhoLaudo;
    }

    public void setAtalhoLaudo(Boolean atalhoLaudo) {
        this.atalhoLaudo = atalhoLaudo;
    }

    public Boolean getAtalhoAgenda() {
        return atalhoAgenda;
    }

    public void setAtalhoAgenda(Boolean atalhoAgenda) {
        this.atalhoAgenda = atalhoAgenda;
    }

    public Boolean getAtalhoConsultarAgendamentos() {
        return atalhoConsultarAgendamentos;
    }

    public void setAtalhoConsultarAgendamentos(Boolean atalhoConsultarAgendamentos) {
        this.atalhoConsultarAgendamentos = atalhoConsultarAgendamentos;
    }

    public Boolean getAtalhoAtendimentos() {
        return atalhoAtendimentos;
    }

    public void setAtalhoAtendimentos(Boolean atalhoAtendimentos) {
        this.atalhoAtendimentos = atalhoAtendimentos;
    }
}
