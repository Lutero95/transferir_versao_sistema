package br.gov.al.maceio.sishosp.administrativo.model.dto;

import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GravarInsercaoProfissionalEquipeAtendimentoDTO implements Serializable {

    private InsercaoProfissionalEquipe insercaoProfissionalEquipe;
    private List<BuscarAtendimentosParaAdicionarProfissionalDTO> listaAtendimentos;

    public GravarInsercaoProfissionalEquipeAtendimentoDTO() {
        insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();
        listaAtendimentos = new ArrayList<>();
    }

    public GravarInsercaoProfissionalEquipeAtendimentoDTO(InsercaoProfissionalEquipe insercaoProfissionalEquipe, List<BuscarAtendimentosParaAdicionarProfissionalDTO> listaAtendimentos) {
        this.insercaoProfissionalEquipe = insercaoProfissionalEquipe;
        this.listaAtendimentos = listaAtendimentos;
    }

    public InsercaoProfissionalEquipe getInsercaoProfissionalEquipe() {
        return insercaoProfissionalEquipe;
    }

    public List<BuscarAtendimentosParaAdicionarProfissionalDTO> getListaAtendimentos() {
        return listaAtendimentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GravarInsercaoProfissionalEquipeAtendimentoDTO that = (GravarInsercaoProfissionalEquipeAtendimentoDTO) o;
        return Objects.equals(insercaoProfissionalEquipe, that.insercaoProfissionalEquipe) &&
                Objects.equals(listaAtendimentos, that.listaAtendimentos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insercaoProfissionalEquipe, listaAtendimentos);
    }

    @Override
    public String toString() {
        return "GravarInsercaoProfissionalEquipeAtendimentoDTO{" +
                "insercaoProfissionalEquipe=" + insercaoProfissionalEquipe +
                ", listaAtendimentos=" + listaAtendimentos +
                '}';
    }
}
