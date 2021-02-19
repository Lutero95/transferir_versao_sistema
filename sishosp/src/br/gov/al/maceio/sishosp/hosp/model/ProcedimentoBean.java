package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProcedimentoBean implements Serializable {

    private Integer idProc;
    private String codProc;
    private String nomeProc;
    private Boolean auditivo;
    private String tipoExameAuditivo;
    private Boolean utilizaEquipamento;
    private Integer codEmpresa;
    private boolean gera_laudo_digita;
    private Integer validade_laudo;
    private Integer idadeMinima;
    private Integer idadeMaxima;
    private Integer qtdMaxima;
    private Integer prazoMinimoNovaExecucao;
    private String sexo;
    private Integer codUnidade;
    private InstrumentoRegistroBean instrumentoRegistroPadrao;
    private ServicoBean servico;
    private ClassificacaoBean classificacao;

    // HERDADOS
    private EquipamentoBean equipamento;

    // LISTAS
    private ArrayList<CidBean> listaCid;
    private ArrayList<CboBean> listaCbo;
    private ArrayList<RecursoBean> listaRecurso;
    private ArrayList<UnidadeBean> listaUnidadesVisualizam;

    public ProcedimentoBean() {
        super();
        this.idProc = null;
        this.codProc = null;
        this.nomeProc = new String();
        this.auditivo = false;
        this.tipoExameAuditivo = new String();
        this.utilizaEquipamento = false;
        this.codEmpresa = null;
        this.idadeMinima = null;
        this.idadeMaxima = null;
        this.qtdMaxima = null;
        this.prazoMinimoNovaExecucao = null;
        this.sexo = "A";
        equipamento = new EquipamentoBean();
        listaCid = new ArrayList<CidBean>();
        listaCbo = new ArrayList<CboBean>();
        listaRecurso = new ArrayList<RecursoBean>();
        listaUnidadesVisualizam = new ArrayList<>();
        instrumentoRegistroPadrao = new InstrumentoRegistroBean();
        servico = new ServicoBean();
        classificacao = new ClassificacaoBean();
    }

    public Integer getIdProc() {
        return idProc;
    }

    public void setIdProc(Integer idProc) {
        this.idProc = idProc;
    }


    public String getNomeProc() {
        return nomeProc;
    }

    public void setNomeProc(String nomeProc) {
        this.nomeProc = nomeProc;
    }

    public Boolean getAuditivo() {
        return auditivo;
    }

    public void setAuditivo(Boolean auditivo) {
        this.auditivo = auditivo;
    }

    public String getTipoExameAuditivo() {
        return tipoExameAuditivo;
    }

    public void setTipoExameAuditivo(String tipoExameAuditivo) {
        this.tipoExameAuditivo = tipoExameAuditivo;
    }

    public Boolean getUtilizaEquipamento() {
        return utilizaEquipamento;
    }

    public void setUtilizaEquipamento(Boolean utilizaEquipamento) {
        this.utilizaEquipamento = utilizaEquipamento;
    }

    public Integer getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Integer codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    public Integer getValidade_laudo() {
        return validade_laudo;
    }

    public void setValidade_laudo(Integer validade_laudo) {
        this.validade_laudo = validade_laudo;
    }

    public boolean isGera_laudo_digita() {
        return gera_laudo_digita;
    }

    public void setGera_laudo_digita(boolean gera_laudo_digita) {
        this.gera_laudo_digita = gera_laudo_digita;
    }

    public EquipamentoBean getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoBean equipamento) {
        this.equipamento = equipamento;
    }

    public Integer getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(Integer idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    public Integer getIdadeMaxima() {
        return idadeMaxima;
    }

    public void setIdadeMaxima(Integer idadeMaxima) {
        this.idadeMaxima = idadeMaxima;
    }

    public Integer getQtdMaxima() {
        return qtdMaxima;
    }

    public void setQtdMaxima(Integer qtdMaxima) {
        this.qtdMaxima = qtdMaxima;
    }

    public Integer getPrazoMinimoNovaExecucao() {
        return prazoMinimoNovaExecucao;
    }

    public void setPrazoMinimoNovaExecucao(Integer prazoMinimoNovaExecucao) {
        this.prazoMinimoNovaExecucao = prazoMinimoNovaExecucao;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public ArrayList<CidBean> getListaCid() {
        return listaCid;
    }

    public void setListaCid(ArrayList<CidBean> listaCid) {
        this.listaCid = listaCid;
    }

    public ArrayList<CboBean> getListaCbo() {
        return listaCbo;
    }

    public void setListaCbo(ArrayList<CboBean> listaCbo) {
        this.listaCbo = listaCbo;
    }

    public ArrayList<RecursoBean> getListaRecurso() {
        return listaRecurso;
    }

    public void setListaRecurso(ArrayList<RecursoBean> listaRecurso) {
        this.listaRecurso = listaRecurso;
    }

	public Integer getCodUnidade() {
		return codUnidade;
	}

	public void setCodUnidade(Integer codUnidade) {
		this.codUnidade = codUnidade;
	}

	public String getCodProc() {
		return codProc;
	}

	public void setCodProc(String codProc) {
		this.codProc = codProc;
	}

	public ArrayList<UnidadeBean> getListaUnidadesVisualizam() {
		return listaUnidadesVisualizam;
	}

	public void setListaUnidadesVisualizam(ArrayList<UnidadeBean> listaUnidadesVisualizam) {
		this.listaUnidadesVisualizam = listaUnidadesVisualizam;
	}

	public InstrumentoRegistroBean getInstrumentoRegistroPadrao() {
		return instrumentoRegistroPadrao;
	}

	public void setInstrumentoRegistroPadrao(InstrumentoRegistroBean instrumentoRegistroPadrao) {
		this.instrumentoRegistroPadrao = instrumentoRegistroPadrao;
	}

	public ServicoBean getServico() {
		return servico;
	}

	public void setServico(ServicoBean servico) {
		this.servico = servico;
	}

	public ClassificacaoBean getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoBean classificacao) {
		this.classificacao = classificacao;
	}
	
}
