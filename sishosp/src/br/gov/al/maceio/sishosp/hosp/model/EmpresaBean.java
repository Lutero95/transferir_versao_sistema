package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;

public class EmpresaBean implements Serializable {

    private Integer codEmpresa;
    private String nomeEmpresa;
    private String nomeFantasia;
    private String cnpj;
    private String rua;
    private String bairro;
    private Integer numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String estado;
    private Integer ddd1;
    private String telefone1;
    private Integer ddd2;
    private String telefone2;
    private Byte[] logomarca;
    private String email;
    private String site;
    private Boolean ativo;
    private Boolean restringirLaudoPorUnidade;
    private String tipoString;
    private String cnes;


    public EmpresaBean() {
    }

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getDdd1() {
        return ddd1;
    }

    public void setDdd1(Integer ddd1) {
        this.ddd1 = ddd1;
    }



    public Integer getDdd2() {
        return ddd2;
    }

    public void setDdd2(Integer ddd2) {
        this.ddd2 = ddd2;
    }

   

    public Byte[] getLogomarca() {
        return logomarca;
    }

    public void setLogomarca(Byte[] logomarca) {
        this.logomarca = logomarca;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

   

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getTipoString() {
        return tipoString;
    }

    public void setTipoString(String tipoString) {
        this.tipoString = tipoString;
    }

	public String getCnes() {
		return cnes;
	}

	public void setCnes(String cnes) {
		this.cnes = cnes;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

    public Boolean getRestringirLaudoPorUnidade() {
        return restringirLaudoPorUnidade;
    }

    public void setRestringirLaudoPorUnidade(Boolean restringirLaudoPorUnidade) {
        this.restringirLaudoPorUnidade = restringirLaudoPorUnidade;
    }
}
