package br.gov.al.maceio.sishosp.administrativo.model.dto;

import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GravarRemocaoAtendimentoDTO implements Serializable {

    private RemocaoProfissionalEquipe remocaoProfissionalEquipe;
    private List<RemocaoProfissionalEquipe> listaRemocao;
    private Connection conexaoAuxiliar;
    private Integer codRemocaoProfissional;

    public GravarRemocaoAtendimentoDTO() {
        remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
        listaRemocao = new ArrayList<>();
    }

    public GravarRemocaoAtendimentoDTO(RemocaoProfissionalEquipe remocaoProfissionalEquipe, List<RemocaoProfissionalEquipe> listaRemocao,
                                       Connection conexaoAuxiliar, Integer codRemocaoProfissional) {
        this.remocaoProfissionalEquipe = remocaoProfissionalEquipe;
        this.listaRemocao = listaRemocao;
        this.conexaoAuxiliar = conexaoAuxiliar;
        this.codRemocaoProfissional = codRemocaoProfissional;
    }

    public RemocaoProfissionalEquipe getRemocaoProfissionalEquipe() {
        return remocaoProfissionalEquipe;
    }

    public void setRemocaoProfissionalEquipe(RemocaoProfissionalEquipe remocaoProfissionalEquipe) {
        this.remocaoProfissionalEquipe = remocaoProfissionalEquipe;
    }

    public List<RemocaoProfissionalEquipe> getListaRemocao() {
        return listaRemocao;
    }

    public void setListaRemocao(List<RemocaoProfissionalEquipe> listaRemocao) {
        this.listaRemocao = listaRemocao;
    }

    public Connection getConexaoAuxiliar() {
        return conexaoAuxiliar;
    }

    public void setConexaoAuxiliar(Connection conexaoAuxiliar) {
        this.conexaoAuxiliar = conexaoAuxiliar;
    }

    public Integer getCodRemocaoProfissional() {
        return codRemocaoProfissional;
    }

    public void setCodRemocaoProfissional(Integer codRemocaoProfissional) {
        this.codRemocaoProfissional = codRemocaoProfissional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GravarRemocaoAtendimentoDTO that = (GravarRemocaoAtendimentoDTO) o;
        return Objects.equals(remocaoProfissionalEquipe, that.remocaoProfissionalEquipe) &&
                Objects.equals(listaRemocao, that.listaRemocao) &&
                Objects.equals(conexaoAuxiliar, that.conexaoAuxiliar) &&
                Objects.equals(codRemocaoProfissional, that.codRemocaoProfissional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remocaoProfissionalEquipe, listaRemocao, conexaoAuxiliar, codRemocaoProfissional);
    }

    @Override
    public String toString() {
        return "GravarRemocaoAtendimentoDTO{" +
                "remocaoProfissionalEquipe=" + remocaoProfissionalEquipe +
                ", listaRemocao=" + listaRemocao +
                ", conexaoAuxiliar=" + conexaoAuxiliar +
                ", codRemocaoProfissional=" + codRemocaoProfissional +
                '}';
    }
}
