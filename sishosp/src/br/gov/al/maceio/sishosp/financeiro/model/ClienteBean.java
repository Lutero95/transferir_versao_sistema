package br.gov.al.maceio.sishosp.financeiro.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ClienteBean implements Serializable {

    private Integer codcliente;
    private String nome;
    private String endereco;
    private String complemento;
    private String numero;
    private String pontoref;
    private String cep;
    private String cpfcnpj;
    private String inscest;
    private Timestamp datacadastro;
    private String fantasia;
    private String situacao;
    private String site;
    private String email;
    private Integer opcad;
    private String codcontabil;
    private String tipopessoa;
    private Integer score;
    private String nacionalidaade;
    private Date dtnasc;
    private String identidade;
    private String orgaoident;
    private String sexo;
    private String nomeconjuge;
    private Date dtnascconjuge;
    private String sexoconjuge;
    private String cpfconjuge;
    private String rgconjuge;
    private double percdesconto;
    private Integer ddd1;
    private Integer telefone1;
    private String tipotelefone1;
    private Integer ddd2;
    private Integer telefone2;
    private String tipotelefone2;
    private EnderecoBean end;
    private String motivo_negativar;
    private Integer idcategoria;
    private Integer idClasse;
    private Boolean negativar;
    private Integer codigo_alternativo;
    private Integer codfilial;
    private Boolean acertoSessao;
    private Boolean recebida;
    private Boolean visualizada;
    private String acerto;
    private Boolean ativo;
    private String ativado;
    private Boolean crianca;
    private Integer responsavel;
    private String nomeResponsavel;
    private String telefoneCsv;
            
    public ClienteBean() {
      
        end = new EnderecoBean();
    
        
    }

    public double getPercdesconto() {
        return percdesconto;
    }

    public void setPercdesconto(double percdesconto) {
        this.percdesconto = percdesconto;
    }

    public Integer getCodcliente() {
        return codcliente;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getNumero() {
        return numero;
    }

    public String getPontoref() {
        return pontoref;
    }

    public String getCep() {
        return cep;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public String getInscest() {
        return inscest;
    }

    public Timestamp getDatacadastro() {
        return datacadastro;
    }

    public String getFantasia() {
        return fantasia;
    }

    public String getSituacao() {
        return situacao;
    }

    public String getSite() {
        return site;
    }

    public String getEmail() {
        return email;
    }

    public Integer getOpcad() {
        return opcad;
    }

    public String getCodcontabil() {
        return codcontabil;
    }

    public String getTipopessoa() {
        return tipopessoa;
    }

    public Integer getScore() {
        return score;
    }

    public String getNacionalidaade() {
        return nacionalidaade;
    }

  

    public String getIdentidade() {
        return identidade;
    }

    public String getOrgaoident() {
        return orgaoident;
    }

    public String getSexo() {
        return sexo;
    }

    

    public void setCodcliente(Integer codcliente) {
        this.codcliente = codcliente;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setPontoref(String pontoref) {
        this.pontoref = pontoref;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public void setInscest(String inscest) {
        this.inscest = inscest;
    }

    public void setDatacadastro(Timestamp datacadastro) {
        this.datacadastro = datacadastro;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOpcad(Integer opcad) {
        this.opcad = opcad;
    }

    public void setCodcontabil(String codcontabil) {
        this.codcontabil = codcontabil;
    }

    public void setTipopessoa(String tipopessoa) {
        this.tipopessoa = tipopessoa;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setNacionalidaade(String nacionalidaade) {
        this.nacionalidaade = nacionalidaade;
    }

   

    public void setIdentidade(String identidade) {
        this.identidade = identidade;
    }

    public void setOrgaoident(String orgaoident) {
        this.orgaoident = orgaoident;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


    public Date getDtnasc() {
        return dtnasc;
    }

    public void setDtnasc(Date dtnasc) {
        this.dtnasc = dtnasc;
    }

    public String getNomeconjuge() {
        return nomeconjuge;
    }

    public Date getDtnascconjuge() {
        return dtnascconjuge;
    }

    public String getSexoconjuge() {
        return sexoconjuge;
    }

    public String getCpfconjuge() {
        return cpfconjuge;
    }

    public String getRgconjuge() {
        return rgconjuge;
    }

    public void setNomeconjuge(String nomeconjuge) {
        this.nomeconjuge = nomeconjuge;
    }

    public void setDtnascconjuge(Date dtnascconjuge) {
        this.dtnascconjuge = dtnascconjuge;
    }

    public void setSexoconjuge(String sexoconjuge) {
        this.sexoconjuge = sexoconjuge;
    }

    public void setCpfconjuge(String cpfconjuge) {
        this.cpfconjuge = cpfconjuge;
    }

    public void setRgconjuge(String rgconjuge) {
        this.rgconjuge = rgconjuge;
    }

    public Integer getDdd1() {
        return ddd1;
    }

    public Integer getTelefone1() {
        return telefone1;
    }

    public Integer getDdd2() {
        return ddd2;
    }

    public Integer getTelefone2() {
        return telefone2;
    }

    public void setDdd1(Integer ddd1) {
        this.ddd1 = ddd1;
    }

    public void setTelefone1(Integer telefone1) {
        this.telefone1 = telefone1;
    }

    public void setDdd2(Integer ddd2) {
        this.ddd2 = ddd2;
    }

    public void setTelefone2(Integer telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTipotelefone1() {
        return tipotelefone1;
    }

    public String getTipotelefone2() {
        return tipotelefone2;
    }

    public void setTipotelefone1(String tipotelefone1) {
        this.tipotelefone1 = tipotelefone1;
    }

    public void setTipotelefone2(String tipotelefone2) {
        this.tipotelefone2 = tipotelefone2;
    }

    public EnderecoBean getEnd() {
        return end;
    }

    public void setEnd(EnderecoBean end) {
        this.end = end;
    }

	public Boolean getRecebida() {
		return recebida;
	}

	public void setRecebida(Boolean recebida) {
		this.recebida = recebida;
	}

	public Boolean getVisualizada() {
		return visualizada;
	}

	public void setVisualizada(Boolean visualizada) {
		this.visualizada = visualizada;
	}

	

	public String getMotivo_negativar() {
		return motivo_negativar;
	}

	public void setMotivo_negativar(String motivo_negativar) {
		this.motivo_negativar = motivo_negativar;
	}

	public Integer getIdcategoria() {
		return idcategoria;
	}

	public void setIdcategoria(Integer idcategoria) {
		this.idcategoria = idcategoria;
	}

	public Boolean getNegativar() {
		return negativar;
	}

	public void setNegativar(Boolean negativar) {
		this.negativar = negativar;
	}

	public Integer getCodigo_alternativo() {
		return codigo_alternativo;
	}

	public void setCodigo_alternativo(Integer codigo_alternativo) {
		this.codigo_alternativo = codigo_alternativo;
	}

	public Integer getCodfilial() {
		return codfilial;
	}

	public void setCodfilial(Integer codfilial) {
		this.codfilial = codfilial;
	}

	public Boolean getAcertoSessao() {
		return acertoSessao;
	}

	public void setAcertoSessao(Boolean acertoSessao) {
		this.acertoSessao = acertoSessao;
	}

	public String getAcerto() {
		return acerto;
	}

	public void setAcerto(String acerto) {
		this.acerto = acerto;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getAtivado() {
		return ativado;
	}

	public void setAtivado(String ativado) {
		this.ativado = ativado;
	}

	public Integer getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(Integer idClasse) {
		this.idClasse = idClasse;
	}

	public Boolean getCrianca() {
		return crianca;
	}

	public void setCrianca(Boolean crianca) {
		this.crianca = crianca;
	}

	public Integer getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Integer responsavel) {
		this.responsavel = responsavel;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getTelefoneCsv() {
		return telefoneCsv;
	}

	public void setTelefoneCsv(String telefoneCsv) {
		this.telefoneCsv = telefoneCsv;
	}

	
}