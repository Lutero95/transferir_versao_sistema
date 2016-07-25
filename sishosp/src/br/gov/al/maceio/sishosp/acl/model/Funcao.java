package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class Funcao implements Serializable{
    
    private Long id;
    private String descricao;
    private Integer IdRotina;
    private String codigo;
    private String descRotina;
    private boolean ativa;
    
    private Integer idPermissao;
    
    private Integer idSistema;
    private String descSistema;
    private String siglaSistema;
    
    private List<Integer> listaSistemas;

    public Funcao() {
        
    }

    public Funcao(Long id, String descricao, Integer IdRotina, String codigo, 
        String descRotina, boolean ativa, Integer idPermissao, Integer idSistema, 
        String descSistema, String siglaSistema, List<Integer> listaSistemas) {
        this.id = id;
        this.descricao = descricao;
        this.IdRotina = IdRotina;
        this.codigo = codigo;
        this.descRotina = descRotina;
        this.ativa = ativa;
        this.idPermissao = idPermissao;
        this.idSistema = idSistema;
        this.descSistema = descSistema;
        this.siglaSistema = siglaSistema;
        this.listaSistemas = listaSistemas;
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

    public Integer getIdRotina() {
        return IdRotina;
    }

    public void setIdRotina(Integer IdRotina) {
        this.IdRotina = IdRotina;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescRotina() {
        return descRotina;
    }

    public void setDescRotina(String descRotina) {
        this.descRotina = descRotina;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
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

    public List<Integer> getListaSistemas() {
        return listaSistemas;
    }

    public void setListaSistemas(List<Integer> listaSistemas) {
        this.listaSistemas = listaSistemas;
    }
}