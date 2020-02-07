package br.gov.al.maceio.sishosp.administrativo.model.dto;

import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class GravarInsercaoAtendimento1DTO implements Serializable {

    private InsercaoProfissionalEquipe insercaoProfissionalEquipe;
    private List<Integer> listaAtendimentos;
    private Integer codInsercaoProfissionalEquipeAtendimento;
    private Connection conexaoAuxiliar;

    public GravarInsercaoAtendimento1DTO() {
        insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();
    }

    public GravarInsercaoAtendimento1DTO(InsercaoProfissionalEquipe insercaoProfissionalEquipe, List<Integer> listaAtendimentos, Integer codInsercaoProfissionalEquipeAtendimento, Connection conexaoAuxiliar) {
        this.insercaoProfissionalEquipe = insercaoProfissionalEquipe;
        this.listaAtendimentos = listaAtendimentos;
        this.codInsercaoProfissionalEquipeAtendimento = codInsercaoProfissionalEquipeAtendimento;
        this.conexaoAuxiliar = conexaoAuxiliar;
    }

    public InsercaoProfissionalEquipe getInsercaoProfissionalEquipe() {
        return insercaoProfissionalEquipe;
    }

    public List<Integer> getListaAtendimentos() {
        return listaAtendimentos;
    }

    public Integer getCodInsercaoProfissionalEquipeAtendimento() {
        return codInsercaoProfissionalEquipeAtendimento;
    }

    public Connection getConexaoAuxiliar() {
        return conexaoAuxiliar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GravarInsercaoAtendimento1DTO that = (GravarInsercaoAtendimento1DTO) o;
        return Objects.equals(insercaoProfissionalEquipe, that.insercaoProfissionalEquipe) &&
                Objects.equals(listaAtendimentos, that.listaAtendimentos) &&
                Objects.equals(codInsercaoProfissionalEquipeAtendimento, that.codInsercaoProfissionalEquipeAtendimento) &&
                Objects.equals(conexaoAuxiliar, that.conexaoAuxiliar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insercaoProfissionalEquipe, listaAtendimentos, codInsercaoProfissionalEquipeAtendimento, conexaoAuxiliar);
    }

    @Override
    public String toString() {
        return "GravarInsercaoAtendimento1DTO{" +
                "insercaoProfissionalEquipe=" + insercaoProfissionalEquipe +
                ", listaAtendimentos=" + listaAtendimentos +
                ", codInsercaoProfissionalEquipeAtendimento=" + codInsercaoProfissionalEquipeAtendimento +
                ", conexaoAuxiliar=" + conexaoAuxiliar +
                '}';
    }
}
