package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class Sistema implements Serializable{
    
    private Integer id;
    private String descricao;
    private String sigla;
    private String imagem;
    private String url;
    private String versao;
    private Boolean ativo;
    private List<Long> listaIdMenuSistema;

    public Sistema() {
        
    }

    public Sistema(Integer id, String descricao, String sigla, String imagem, String url, 
        String versao, Boolean ativo, List<Long> listaIdMenuSistema) {
        this.id = id;
        this.descricao = descricao;
        this.sigla = sigla;
        this.imagem = imagem;
        this.url = url;
        this.versao = versao;
        this.ativo = ativo;
        this.listaIdMenuSistema = listaIdMenuSistema;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Long> getListaIdMenuSistema() {
        return listaIdMenuSistema;
    }

    public void setListaIdMenuSistema(List<Long> listaIdMenuSistema) {
        this.listaIdMenuSistema = listaIdMenuSistema;
    }
}