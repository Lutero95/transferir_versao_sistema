package br.gov.al.maceio.sishosp.administrativo.model.dto;

import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GravarInsercaoProfissionalEquipeAtendimento1DTO implements Serializable {

    private List<Integer> listaInsercao;
    private Integer codInsercaoProfissionalEquipeAtendimento;
    private Connection conexaoAuxiliar;
    private InsercaoProfissionalEquipe insercaoProfissionalEquipe;

    public GravarInsercaoProfissionalEquipeAtendimento1DTO() {
        listaInsercao = new ArrayList<>();
        insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();
    }

    public GravarInsercaoProfissionalEquipeAtendimento1DTO(List<Integer> listaInsercao, Integer codInsercaoProfissionalEquipeAtendimento,
                                                           Connection conexaoAuxiliar, InsercaoProfissionalEquipe insercaoProfissionalEquipe) {
        this.listaInsercao = listaInsercao;
        this.codInsercaoProfissionalEquipeAtendimento = codInsercaoProfissionalEquipeAtendimento;
        this.conexaoAuxiliar = conexaoAuxiliar;
        this.insercaoProfissionalEquipe = insercaoProfissionalEquipe;
    }

    public List<Integer> getListaInsercao() {
        return listaInsercao;
    }

    public Integer getCodInsercaoProfissionalEquipeAtendimento() {
        return codInsercaoProfissionalEquipeAtendimento;
    }

    public Connection getConexaoAuxiliar() {
        return conexaoAuxiliar;
    }

    public InsercaoProfissionalEquipe getInsercaoProfissionalEquipe() {
        return insercaoProfissionalEquipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GravarInsercaoProfissionalEquipeAtendimento1DTO that = (GravarInsercaoProfissionalEquipeAtendimento1DTO) o;
        return Objects.equals(listaInsercao, that.listaInsercao) &&
                Objects.equals(codInsercaoProfissionalEquipeAtendimento, that.codInsercaoProfissionalEquipeAtendimento) &&
                Objects.equals(conexaoAuxiliar, that.conexaoAuxiliar) &&
                Objects.equals(insercaoProfissionalEquipe, that.insercaoProfissionalEquipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listaInsercao, codInsercaoProfissionalEquipeAtendimento, conexaoAuxiliar, insercaoProfissionalEquipe);
    }

    @Override
    public String toString() {
        return "GravarInsercaoProfissionalEquipeAtendimento1DTO{" +
                "listaInsercao=" + listaInsercao +
                ", codInsercaoProfissionalEquipeAtendimento=" + codInsercaoProfissionalEquipeAtendimento +
                ", conexaoAuxiliar=" + conexaoAuxiliar +
                ", insercaoProfissionalEquipe=" + insercaoProfissionalEquipe +
                '}';
    }
}
