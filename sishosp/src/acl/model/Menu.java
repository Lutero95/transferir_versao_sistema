package br.gov.al.maceio.sishosp.acl.model;

import java.util.List;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class Menu {

    private Long id;
    private String descricao;
    private String icone;
    
    private String url;    
    private String action;
    private String onclick;
    
    private String codigo;
    private String indice;
    private String tipo;
    private boolean ativo;
    
	private Integer idPermissao;
	private Integer idRotina;
    private String descRotina;
    private Integer idSistema;
    private String descSistema;
    private String siglaSistema;
    
    private Integer idRecSistema;

    private List<Integer> listaSistemas;

    private String indiceAux;  

    private Rotina rotina;
    
    // ALTERADO AS 3 VARIAVEIS DESC_PAGINA , EXTENSAO E DIRETORIO PARA ROTINA!
    // ADICIONADO ROTINA rotina como objeto !
 
	public Menu() {
		rotina = new Rotina();

    }

    public Menu(Long id, String descricao, String icone, String url, String action, 
        String onclick, String codigo, String indice, String tipo, boolean ativo, 
        Integer idPermissao, Integer idRotina, String desRotina, Integer idSistema, 
        String descSistema, String siglaSistema, Integer idRecSistema, List<Integer> listaSistemas, 
        String indiceAux, Rotina rotina) {
        this.id = id;
        this.descricao = descricao;
        this.icone = icone;
        this.url = url;
        this.action = action;
        this.onclick = onclick;
        this.codigo = codigo;
        this.indice = indice;
        this.tipo = tipo;
        this.ativo = ativo;
        this.idPermissao = idPermissao;
        this.idRotina = idRotina;
        
        this.descRotina = desRotina;
        this.idSistema = idSistema;
        this.descSistema = descSistema;
        this.siglaSistema = siglaSistema;
        this.idRecSistema = idRecSistema;
        this.listaSistemas = listaSistemas;
        this.indiceAux = indiceAux;
        this.rotina = rotina;
        
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

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }
    
    public String getDescRotina() {
        return descRotina;
    }

    public void setDescRotina(String descRotina) {
        this.descRotina = descRotina;
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

    public Integer getIdRecSistema() {
        return idRecSistema;
    }

    public void setIdRecSistema(Integer idRecSistema) {
        this.idRecSistema = idRecSistema;
    }

    public List<Integer> getListaSistemas() {
        return listaSistemas;
    }

    public void setListaSistemas(List<Integer> listaSistemas) {
        this.listaSistemas = listaSistemas;
    }

    public String getIndiceAux() {
        return indiceAux;
    }

    public void setIndiceAux(String indiceAux) {
        this.indiceAux = indiceAux;
    }

	   public Rotina getRotina() {
			return rotina;
		}

		public void setRotina(Rotina rotina) {
			this.rotina = rotina;
		}
		public Integer getIdRotina() {
			return idRotina;
		}

		public void setIdRotina(Integer idRotina) {
			this.idRotina = idRotina;
		}


}