package br.gov.al.maceio.sishosp.administrativo.model.dto;

import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GravarInsercaoProfissionalEquipeAtendimento1DTO implements Serializable {

    private List<InsercaoProfissionalEquipe> listaInsercao;
    private Integer codInsercaoProfissionalEquipeAtendimento;
    private Connection conexaoAuxiliar;

    public GravarInsercaoProfissionalEquipeAtendimento1DTO() {
        listaInsercao = new ArrayList<>();
    }

    public GravarInsercaoProfissionalEquipeAtendimento1DTO(List<InsercaoProfissionalEquipe> listaInsercao, Integer codInsercaoProfissionalEquipeAtendimento, Connection conexaoAuxiliar) {
        this.listaInsercao = listaInsercao;
        this.codInsercaoProfissionalEquipeAtendimento = codInsercaoProfissionalEquipeAtendimento;
        this.conexaoAuxiliar = conexaoAuxiliar;
    }

    public List<InsercaoProfissionalEquipe> getListaInsercao() {
        return listaInsercao;
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
        GravarInsercaoProfissionalEquipeAtendimento1DTO that = (GravarInsercaoProfissionalEquipeAtendimento1DTO) o;
        return Objects.equals(listaInsercao, that.listaInsercao) &&
                Objects.equals(codInsercaoProfissionalEquipeAtendimento, that.codInsercaoProfissionalEquipeAtendimento) &&
                Objects.equals(conexaoAuxiliar, that.conexaoAuxiliar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listaInsercao, codInsercaoProfissionalEquipeAtendimento, conexaoAuxiliar);
    }

    @Override
    public String toString() {
        return "GravarInsercaoProfissionalEquipeAtendimento1DTO{" +
                "listaInsercao=" + listaInsercao +
                ", codInsercaoProfissionalEquipeAtendimento=" + codInsercaoProfissionalEquipeAtendimento +
                ", conexaoAuxiliar=" + conexaoAuxiliar +
                '}';
    }
}
