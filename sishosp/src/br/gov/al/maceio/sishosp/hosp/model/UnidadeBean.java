package br.gov.al.maceio.sishosp.hosp.model;

public class UnidadeBean extends EmpresaBean{

    private Integer id;
    private String nomeUnidade;
    private Boolean matriz;
    private ParametroBean parametro;

    public UnidadeBean() {
    	parametro = new ParametroBean();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Boolean getMatriz() {
		return matriz;
	}

	public void setMatriz(Boolean matriz) {
		this.matriz = matriz;
	}

	public ParametroBean getParametro() {
		return parametro;
	}

	public void setParametro(ParametroBean parametro) {
		this.parametro = parametro;
	}

	public String getNomeUnidade() {
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

   
}
