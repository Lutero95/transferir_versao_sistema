package br.gov.al.maceio.sishosp.acl.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class Funcionalidade implements Serializable{

    private Long id;
    private String descricao;
    private String icone;
    private String imagem;
    private String url;    
    private String action;
    private String onclick;
    
    private String codigo;
    private String indice;
    private String tipo;
    private boolean ativo;
    
	private Integer idPermissao;
    private Integer idSistema;
    private String descSistema;
    private String siglaSistema;
    
    private Integer idRecSistema;
    private String descPagina;
	private String diretorio;
    private String extensao;    

    private List<Integer> listaSistemas;

    private String indiceAux;  

    
    
    // ALTERADO AS 3 VARIAVEIS DESC_PAGINA , EXTENSAO E DIRETORIO PARA ROTINA!
    // ADICIONADO ROTINA rotina como objeto !
 
	public Funcionalidade() {
		

    }

    public Funcionalidade(Long id, String descricao, String icone, String url, String action, 
        String onclick, String codigo, String indice, String tipo, boolean ativo, 
        Integer idPermissao, Integer idRotina, String desRotina, Integer idSistema, 
        String descSistema, String siglaSistema, Integer idRecSistema, List<Integer> listaSistemas, 
        String indiceAux) {
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
        this.idSistema = idSistema;
        this.descSistema = descSistema;
        this.siglaSistema = siglaSistema;
        this.idRecSistema = idRecSistema;
        this.listaSistemas = listaSistemas;
        this.indiceAux = indiceAux;

        
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

	
		public String getDescPagina() {
			return descPagina;
		}

		public void setDescPagina(String descPagina) {
			this.descPagina = descPagina;
		}

		public String getDiretorio() {
			return diretorio;
		}

		public void setDiretorio(String diretorio) {
			this.diretorio = diretorio;
		}

		public String getExtensao() {
			return extensao;
		}

		public void setExtensao(String extensao) {
			this.extensao = extensao;
		}

		public String getImagem() {
			return imagem;
		}

		public void setImagem(String imagem) {
			this.imagem = imagem;
		}


}