package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

public class AtendimentoBean implements Serializable {

    private Integer id;
    private Integer id1;
    private Date dataAtendimentoInicio;
    private Date dataAtendimentoFinal;
    private String turno;
    private String status;
    private String situacao;
    private String ehEquipe;
    

    // HERDADOS
    private PacienteBean paciente;
    private ProcedimentoBean procedimento;
    private ProgramaBean programa;
    private TipoAtendimentoBean tipoAt;
    private FuncionarioBean funcionario;
    private EquipeBean equipe;
    private CboBean cbo;
    private EmpresaBean empresa;

    public AtendimentoBean() {
        this.paciente = new PacienteBean();
        this.procedimento = new ProcedimentoBean();
        this.programa = new ProgramaBean();
        this.tipoAt = new TipoAtendimentoBean();
        this.funcionario = new FuncionarioBean();
        this.equipe = new EquipeBean();
        this.cbo = new CboBean();
        this.empresa = new EmpresaBean();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataAtendimentoInicio() {
        return dataAtendimentoInicio;
    }

    public void setDataAtendimentoInicio(Date dataAtendimentoInicio) {
        this.dataAtendimentoInicio = dataAtendimentoInicio;
    }

    public Date getDataAtendimentoFinal() {
        return dataAtendimentoFinal;
    }

    public void setDataAtendimentoFinal(Date dataAtendimentoFinal) {
        this.dataAtendimentoFinal = dataAtendimentoFinal;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getEhEquipe() {
        return ehEquipe;
    }

    public void setEhEquipe(String ehEquipe) {
        this.ehEquipe = ehEquipe;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public ProcedimentoBean getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(ProcedimentoBean procedimento) {
        this.procedimento = procedimento;
    }

    public ProgramaBean getPrograma() {
        return programa;
    }

    public void setPrograma(ProgramaBean programa) {
        this.programa = programa;
    }

    public TipoAtendimentoBean getTipoAt() {
        return tipoAt;
    }

    public void setTipoAt(TipoAtendimentoBean tipoAt) {
        this.tipoAt = tipoAt;
    }

    public FuncionarioBean getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(FuncionarioBean funcionario) {
        this.funcionario = funcionario;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public CboBean getCbo() {
        return cbo;
    }

    public void setCbo(CboBean cbo) {
        this.cbo = cbo;
    }

    public EmpresaBean getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaBean empresa) {
        this.empresa = empresa;
    }

}
