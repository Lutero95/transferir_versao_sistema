package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class Permissao implements Serializable {
    
    private Long id;
    private String descricao;
    
    private Integer idRecurso;
    private Integer idAux;
    
    private Integer idSistema;
    private String descSistema;
    private String siglaSistema;

    public Permissao() {
        
    }

    public Permissao(Long id, String descricao, Integer idRecurso, Integer idAux, 
        Integer idSistema, String descSistema, String siglaSistema) {
        this.id = id;
        this.descricao = descricao;
        this.idRecurso = idRecurso;
        this.idAux = idAux;
        this.idSistema = idSistema;
        this.descSistema = descSistema;
        this.siglaSistema = siglaSistema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public Integer getIdAux() {
        return idAux;
    }

    public void setIdAux(Integer idAux) {
        this.idAux = idAux;
    }

    public Integer getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Integer idSistema) {
        this.idSistema = idSistema;
    }

    public String getDescSistema() {
        return descSistema;
    }

    public void setDescSistema(String descSistema) {
        this.descSistema = descSistema;
    }

    public String getSiglaSistema() {
        return siglaSistema;
    }

    public void setSiglaSistema(String siglaSistema) {
        this.siglaSistema = siglaSistema;
    }
}