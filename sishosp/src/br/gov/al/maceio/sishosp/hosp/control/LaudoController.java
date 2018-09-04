package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.dao.*;
import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.TipoAtendimentoBean;

@ManagedBean(name = "LaudoController")
@ViewScoped
public class LaudoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer abaAtiva = 0;
    private FuncionarioBean usuario;
    private LaudoBean laudo;
    private PacienteBean paciente;
    private GrupoBean grupo;
    private ProgramaBean programa;
    private TipoAtendimentoBean tipoAt;
    private FuncionarioBean profissional;
    private EquipeBean equipe;
    private ProcedimentoBean procedimento;
    private FornecedorBean fornecedor;
    private CidBean cid;
    private EquipamentoBean equipamento;
    private String cabecalho;
    private Integer mes_inicio;

    // LISTAS
    private List<LaudoBean> listaLaudo;
    private List<LaudoBean> listaLaudoDigita;
    private List<ProgramaBean> buscalistaProgramas;
    private List<GrupoBean> listaGruposProgramas;

    // BUSCAS
    private int tipo2;
    private String tipo;
    private Integer tipoBuscaLaudo;
    private String campoBuscaLaudo;
    private Integer campoBuscaNumero;
    private Date campoBuscaData;
    private String statusLaudo;

    // BUSCA PERSONALIZADA
    private String situacao;
    private String nome;
    private String recurso;
    private Date dataAtorizacao;
    private Date dataSolicitacao;
    private Integer prontuario;
    private String programa2;

    private LaudoDAO lDao = new LaudoDAO();

    public LaudoController() {
        // CLASSES
        laudo = new LaudoBean();
        programa = new ProgramaBean();
        grupo = new GrupoBean();
        this.tipoAt = null;// new TipoAtendimentoBean();
        mes_inicio = 0;

        // BUSCA
        tipo = "";
        tipoBuscaLaudo = 1;
        campoBuscaLaudo = "";
        statusLaudo = "P";
        campoBuscaNumero = 1;
        campoBuscaData = null;
        this.cabecalho = "";
        this.situacao = "P";
        this.recurso = "T";
        this.nome = null;
        // this.prontuario = 1;
        listaLaudo = new ArrayList<>();
        listaLaudo = null;

        listaLaudoDigita = new ArrayList<>();
        listaLaudoDigita = null;
        buscalistaProgramas = new ArrayList<>();
        buscalistaProgramas = null;
        this.listaGruposProgramas = new ArrayList<>();
    }

    public String redirectInsert() {
        return "cadastroLaudoDigita?faces-redirect=true&amp;tipo2=" + this.tipo2;
    }

    public String redirectEdit() {
        return "cadastroLaudo?faces-redirect=true&amp;id="
                + this.laudo.getId() + "&amp;tipo2=" + tipo2;
    }

    public void getEditLaudo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo2 = Integer.parseInt(params.get("tipo2"));

            this.laudo = lDao.buscarLaudosPorId(id);
        } else {

            tipo2 = Integer.parseInt(params.get("tipo2"));

        }

    }

    public void limparDados() {
        laudo = new LaudoBean();
        fornecedor = new FornecedorBean();
        paciente = new PacienteBean();
        grupo = new GrupoBean();
        programa = new ProgramaBean();
        profissional = new FuncionarioBean();
        equipe = new EquipeBean();
        procedimento = new ProcedimentoBean();
        this.buscalistaProgramas = null;
        prontuario = null;
        recurso = null;
        situacao = null;
    }

    public void limparBuscaDados() {
        tipoBuscaLaudo = 1;
        campoBuscaLaudo = "";
        statusLaudo = "P";
        listaLaudo = null;
        listaLaudoDigita = null;
        this.dataAtorizacao = null;
        this.dataSolicitacao = null;
        this.situacao = null;
        this.recurso = null;
        this.nome = null;
        this.prontuario = null;

    }

    public void gravarLaudo() throws ProjetoException {
        LaudoDAO udao = new LaudoDAO();
        boolean cadastrou = udao.cadastrarLaudo(laudo);

        if (cadastrou == true) {
            limparDados();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Laudo cadastrado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listaLaudo = null;

        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

    }

    public void calcularDias() {
        try {
            if (laudo.getPeriodo() != null) {
                Integer dias = this.laudo.getPeriodo();
                Date dataFim = this.laudo.getData_solicitacao();

                Calendar cl = Calendar.getInstance();
                cl.setTime(dataFim);
                cl.add(Calendar.DATE, (dias - 1));

                Date dataFinal = cl.getTime();

                // this.laudo.setDtafim(dataFinal);
            }
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Informe a Data de autorização.", "Aviso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            // this.laudo.setProrrogar(null);
        }

    }

    public void getValoresLaudo() throws ProjetoException {

        Date dataDoUsuario = this.dataSolicitacao;
        Date dataDoUsuario2 = this.dataAtorizacao;
        Calendar dataInicial = Calendar.getInstance();
        Calendar dataFinal = Calendar.getInstance();
        dataInicial.setTime(new Date());
        dataFinal.setTime(new Date());
        dataInicial.set(Calendar.DAY_OF_MONTH,
                dataInicial.getActualMinimum(Calendar.DAY_OF_MONTH));
        dataFinal.set(Calendar.DAY_OF_MONTH,
                dataFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
        dataDoUsuario = dataInicial.getTime();
        dataDoUsuario2 = dataFinal.getTime();
        this.dataSolicitacao = dataDoUsuario;
        this.dataAtorizacao = dataDoUsuario2;

        ProgramaDAO pdaos = new ProgramaDAO();
        buscalistaProgramas = pdaos.BuscalistarProgramasDefaut();
        if (buscalistaProgramas.size() == 1) {
            for (int i = 0; i < buscalistaProgramas.size(); i++) {
                programa.setIdPrograma(buscalistaProgramas.get(i)
                        .getIdPrograma());
                GrupoDAO gdao = new GrupoDAO();
                this.listaGruposProgramas = gdao
                        .listarGruposPorPrograma(programa.getIdPrograma());

            }

        }
    }

    @SuppressWarnings("deprecation")
    // public void calcularDiasCalendario() {
    // if (laudo.getDtasolicitacao() != null) {
    // Date dataDoUsuario = this.laudo.getDtasolicitacao();
    //
    // // Atrav�s do Calendar, trabalhamos a data informada e adicionamos 1
    // // dia nela
    // Calendar c = Calendar.getInstance();
    // c.setTime(dataDoUsuario);
    // c.add(Calendar.MONTH, getLaudo().getProcedimento()
    // .getValidade_laudo());
    // c.add(Calendar.DAY_OF_MONTH, -1);
    // // Obtemos a data alterada
    // dataDoUsuario = c.getTime();
    //
    // this.laudo.setDtavencimento(dataDoUsuario);
    // }
    // }

    public void calcularPeriodoLaudo() {
        laudo.setMes_inicio(11);
        laudo.setPeriodo(90);

        if (laudo.getPeriodo() != null) {

            int periodo = laudo.getPeriodo() / 30;
            int mes = 0;
            int ano = 0;

            if (laudo.getMes_inicio() + periodo > 12) {
                mes = laudo.getMes_inicio() + periodo - 12;
                ano = laudo.getAno_inicio() + 1;
            } else {
                mes = laudo.getMes_inicio() + periodo;
                ano = laudo.getAno_inicio();
            }

            laudo.setMes_final(mes);
            laudo.setAno_final(ano);


        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Informe o período do Laudo.", "Aviso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void listarLaudo() throws ProjetoException {

        LaudoDAO fdao = new LaudoDAO();
        listaLaudo = fdao.listaLaudos();

    }

    public List<CidBean> listaCidAutoCompletePorProcedimento(String query)
            throws ProjetoException {
        CidDAO gDao = new CidDAO();
        List<CidBean> result = gDao.listarCidsBuscaPorProcedimento(query, laudo.getProcedimento_primario().getIdProc());
        return result;
    }

    public Integer getAbaAtiva() {
        return abaAtiva;
    }

    public void setAbaAtiva(Integer abaAtiva) {
        this.abaAtiva = abaAtiva;
    }

    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getTipoBuscaLaudo() {
        return tipoBuscaLaudo;
    }

    public void setTipoBuscaLaudo(Integer tipoBuscaLaudo) {
        this.tipoBuscaLaudo = tipoBuscaLaudo;
    }

    public String getCampoBuscaLaudo() {
        return campoBuscaLaudo;
    }

    public void setCampoBuscaLaudo(String campoBuscaLaudo) {
        this.campoBuscaLaudo = campoBuscaLaudo;
    }

    public String getStatusLaudo() {
        return statusLaudo;
    }

    public void setStatusLaudo(String statusLaudo) {
        this.statusLaudo = statusLaudo;
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public GrupoBean getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoBean grupo) {
        this.grupo = grupo;
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

    public FuncionarioBean getProfissional() {
        return profissional;
    }

    public void setProfissional(FuncionarioBean profissional) {
        this.profissional = profissional;
    }

    public EquipeBean getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeBean equipe) {
        this.equipe = equipe;
    }

    public ProcedimentoBean getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(ProcedimentoBean procedimento) {
        this.procedimento = procedimento;
    }

    public String getCabecalho() {
        if (this.tipo2 == 2) {
            cabecalho = "Alterar Laudo";
        } else {
            cabecalho = "Cadastro de Laudo";
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public FornecedorBean getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(FornecedorBean fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<LaudoBean> getListaLaudo() throws ProjetoException {
        if (listaLaudo == null) {

            LaudoDAO fdao = new LaudoDAO();
            listaLaudo = fdao.listaLaudos();
        }
        return listaLaudo;
    }

    public void setListaLaudo(List<LaudoBean> listaLaudo) {
        this.listaLaudo = listaLaudo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public CidBean getCid() {
        return cid;
    }

    public void setCid(CidBean cid) {
        this.cid = cid;
    }

    public EquipamentoBean getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(EquipamentoBean equipamento) {
        this.equipamento = equipamento;
    }

    public List<LaudoBean> getListaLaudoDigita() throws ProjetoException {
        if (listaLaudoDigita == null) {

            LaudoDAO fdao = new LaudoDAO();
            listaLaudoDigita = fdao.listaLaudos();

        }
        return listaLaudoDigita;
    }

    public void setListaLaudoDigita(List<LaudoBean> listaLaudoDigita) {
        this.listaLaudoDigita = listaLaudoDigita;
    }

    public String getCabecalho2() {
        if (this.tipo2 == 1) {
            cabecalho = "CADASTRO DE LAUDO DIGITA";
        } else if (this.tipo2 == 2) {
            cabecalho = "ALTERAR LAUDO DIGITA";
        }
        return cabecalho;
    }

    public String getCabecalho3() {
        if (this.tipo2 == 1) {
            cabecalho = "CONTROLE LAUDO";
        } else if (this.tipo2 == 2) {
            cabecalho = "ALTERAR LAUDO DIGITA";
        }
        return cabecalho;
    }

    public Integer getCampoBuscaNumero() {
        return campoBuscaNumero;
    }

    public void setCampoBuscaNumero(Integer campoBuscaNumero) {
        this.campoBuscaNumero = campoBuscaNumero;
    }

    public Date getCampoBuscaData() {
        return campoBuscaData;
    }

    public void setCampoBuscaData(Date campoBuscaData) {
        this.campoBuscaData = campoBuscaData;
    }

    public FuncionarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(FuncionarioBean usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    public Date getDataAtorizacao() {
        return dataAtorizacao;
    }

    public void setDataAtorizacao(Date dataAtorizacao) {
        this.dataAtorizacao = dataAtorizacao;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Integer getProntuario() {
        return prontuario;
    }

    public void setProntuario(Integer prontuario) {
        this.prontuario = prontuario;
    }

    public String getPrograma2() {
        return programa2;
    }

    public void setPrograma2(String programa2) {
        this.programa2 = programa2;
    }

    public int getTipo2() {
        return tipo2;
    }

    public void setTipo2(int tipo2) {
        this.tipo2 = tipo2;
    }

    public List<ProgramaBean> getBuscalistaProgramas() {
        return buscalistaProgramas;
    }

    public void setBuscalistaProgramas(List<ProgramaBean> buscalistaProgramas) {
        this.buscalistaProgramas = buscalistaProgramas;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public Integer getMes_inicio() {
        return mes_inicio;
    }

    public void setMes_inicio(Integer mes_inicio) {
        this.mes_inicio = mes_inicio;
    }

}
