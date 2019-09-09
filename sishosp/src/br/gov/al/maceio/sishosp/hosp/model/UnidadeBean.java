package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;

public class UnidadeBean extends EmpresaBean{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String nomeUnidade;
    private Boolean matriz;
    private Integer motivoDesligamento;
    private String opcaoAtendimento;
    private Integer quantidadeSimultaneaProfissional;
    private Integer quantidadeSimultaneaEquipe;
    private Date horarioInicial;
    private Date horarioFinal;
    private Integer intervalo;
    private OrteseProtese orteseProtese;
    private ParametroBean parametro;
    private Integer codEmpresa;
    private String nomeFantasia;
    private String nomeEmpresa;


    public UnidadeBean() {
        orteseProtese = new OrteseProtese();
        parametro = new ParametroBean();
    //    empresa = new EmpresaBean();
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

	

	public String getNomeUnidade() {
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getMotivoDesligamento() {
		return motivoDesligamento;
	}

	public String getOpcaoAtendimento() {
		return opcaoAtendimento;
	}

	public Integer getQuantidadeSimultaneaProfissional() {
		return quantidadeSimultaneaProfissional;
	}

	public Integer getQuantidadeSimultaneaEquipe() {
		return quantidadeSimultaneaEquipe;
	}

	public Date getHorarioInicial() {
		return horarioInicial;
	}

	public Date getHorarioFinal() {
		return horarioFinal;
	}

	public Integer getIntervalo() {
		return intervalo;
	}



	public OrteseProtese getOrteseProtese() {
		return orteseProtese;
	}

	public void setMotivoDesligamento(Integer motivoDesligamento) {
		this.motivoDesligamento = motivoDesligamento;
	}

	public void setOpcaoAtendimento(String opcaoAtendimento) {
		this.opcaoAtendimento = opcaoAtendimento;
	}

	public void setQuantidadeSimultaneaProfissional(Integer quantidadeSimultaneaProfissional) {
		this.quantidadeSimultaneaProfissional = quantidadeSimultaneaProfissional;
	}

	public void setQuantidadeSimultaneaEquipe(Integer quantidadeSimultaneaEquipe) {
		this.quantidadeSimultaneaEquipe = quantidadeSimultaneaEquipe;
	}

	public void setHorarioInicial(Date horarioInicial) {
		this.horarioInicial = horarioInicial;
	}

	public void setHorarioFinal(Date horarioFinal) {
		this.horarioFinal = horarioFinal;
	}

	public void setIntervalo(Integer intervalo) {
		this.intervalo = intervalo;
	}


	public void setOrteseProtese(OrteseProtese orteseProtese) {
		this.orteseProtese = orteseProtese;
	}

	public ParametroBean getParametro() {
		return parametro;
	}

	public void setParametro(ParametroBean parametro) {
		this.parametro = parametro;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	

	

   
}
