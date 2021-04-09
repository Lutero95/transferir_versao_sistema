package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.dao.FuncionalidadeDAO;
import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.shared.DadosSessao;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.UnidadeDAO;
import br.gov.al.maceio.sishosp.hosp.enums.SituacaoLaudo;
import br.gov.al.maceio.sishosp.hosp.enums.TipoBuscaLaudo;
import br.gov.al.maceio.sishosp.hosp.log.control.LaudoLog;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaIdadePacienteDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaLaudoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacienteLaudoEmLoteDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacientesComInformacaoAtendimentoDTO;
import br.gov.al.maceio.sishosp.questionario.enums.ModeloSexo;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.UnidadeLimiteType;

@ManagedBean(name = "LaudoController")
@ViewScoped
public class LaudoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private LaudoBean laudo;
    private String cabecalho;
    private String cabecalhoLaudoEmLote;
    private Integer tipo;
    private List<LaudoBean> listaLaudo;
    private List<CidBean> listaCids;
    private LaudoDAO lDao = new LaudoDAO();
    private CidDAO cDao = new CidDAO();
    private Boolean renderizarDataAutorizacao;
    private Integer idLaudoGerado = null;
    private BuscaLaudoDTO buscaLaudoDTO;
    private UnidadeDAO unidadeDAO;
    private ProcedimentoDAO procedimentoDAO;
    private FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_usuario");
    private Boolean unidadeValidaDadosSigtap;
    private boolean existeAlgumaCargaSigtap;
    private Boolean usuarioPodeRealizarAutorizacao;
    private boolean existeCargaSigtapParaDataSolicitacao;
    private List<PacienteLaudoEmLoteDTO> listaPacienteLaudoEmLoteDTO;
    private List<PacienteBean> listaPacientes;
    private List<PacienteBean> listaPacientesFiltro;
    private PacienteLaudoEmLoteDTO pacienteLaudoEmLoteSelecionado;
    private PacienteDAO pacienteDAO;

    // CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroLaudoDigita?faces-redirect=true";
    private static final String ENDERECO_GERENCIAMENTO = "gerenciarLaudoDigita?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Laudo";
    private static final String CABECALHO_ALTERACAO = "Alteração de Laudo";
    private static final String CABECALHO_RENOVACAO = "Renovação de Laudo";
    private static final String CABECALHO_INCLUSAO_LOTE = "Inclusão de Laudo em Lote";
    private static final String CABECALHO_ALTERACAO_LOTE = "Alteração de Laudo em Lote";
    private static final String TITULO_CID_PRIMARIO = "Este campo é associado ao Procedimento Primário e Data de Solicitação, selecione-os para buscar o CID 1";

    public LaudoController() {
        this.laudo = new LaudoBean();
        this.cabecalho = "";
        listaLaudo = new ArrayList<>();
        listaCids = new ArrayList<>();
        renderizarDataAutorizacao = false;
        buscaLaudoDTO = new BuscaLaudoDTO();
        buscaLaudoDTO.setSituacao("P");
        buscaLaudoDTO.setTipoPeriodoData("P");
        buscaLaudoDTO.setTipoBusca("paciente");
        buscaLaudoDTO.setCampoBusca("");
        tipo = 0;
        unidadeDAO = new UnidadeDAO();
        procedimentoDAO = new ProcedimentoDAO();
        listaPacienteLaudoEmLoteDTO = new ArrayList<>();
        listaPacientes = new ArrayList<>();
        listaPacientesFiltro = new ArrayList<>();
        pacienteDAO = new PacienteDAO();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.laudo.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void setaTipoUm() {
        tipo = 1;
    }

    public void setaTipoDois() {
        tipo = 2;
    }

    public void setaTipoTres() {
        tipo = 3;
    }

    public void carregaLaudoPorId(Integer idLaudo) throws ProjetoException {
        this.laudo = lDao.buscarLaudosPorId(idLaudo);
    }

    public void carregaLaudoParaRenovacao(Integer idLaudo) throws ProjetoException {
        this.laudo = lDao.carregaLaudoParaRenovacao(idLaudo);
        calcularPeriodoLaudo();
    }


    public void getEditLaudo() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        this.usuarioPodeRealizarAutorizacao = user_session.getPermiteAutorizacaoLaudo();

        verificaSeUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
        verificaSeExisteAlgumaCargaSigtap();

        if ((params.get("id") != null)) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            if (tipo == 2)
                this.laudo = lDao.buscarLaudosPorId(id);
            if (tipo == 3) {
                carregaLaudoParaRenovacao(id);
            }

            renderizarDadosDeAutorizacao();
            existeCargaSigtapParaDataSolicitacao();
        } else {
            tipo = Integer.parseInt(params.get("tipo"));
        }
    }

    public void setaValidadeProcPrimLaudo(Integer validade) {
        laudo.setPeriodo(validade);
        calcularPeriodoLaudo();
        limpaDadosCids();
    }

    public void limpaDadosCids() {
        if(this.unidadeValidaDadosSigtap) {
            laudo.setCid1(new CidBean());
        }
        this.listaCids = new ArrayList<>();
    }

    public void renderizarDadosDeAutorizacao() {
        if (laudo.getSituacao().equals(SituacaoLaudo.AUTORIZADO.getSigla())) {
            renderizarDataAutorizacao = true;
        }
    }

    public void limparDados() {
        laudo = new LaudoBean();
    }

    public void calcularPeriodoLaudo() {

        limpaDadosCids();

        laudo.setMesInicio(null);
        laudo.setMesFinal(null);
        if (laudo.getDataSolicitacao() != null) {
            laudo.setMesInicio(DataUtil.extrairMesDeData(laudo.getDataSolicitacao()));
            laudo.setAnoInicio(DataUtil.extrairAnoDeData(laudo.getDataSolicitacao()));
        }
        if (laudo.getPeriodo() != null && laudo.getPeriodo() != 0 && laudo.getMesInicio() != null
                && laudo.getAnoInicio() != null) {

            int periodo = (laudo.getPeriodo() / 30) - 1; // o periodo do laudo considera o mes atual, por isso a
            // inclusao do -1
            int mes = 0;
            int ano = 0;

            if (laudo.getMesInicio() + periodo > 12) {
                mes = laudo.getMesInicio() + periodo - 12;
                ano = laudo.getAnoInicio() + 1;
            } else {
                mes = laudo.getMesInicio() + periodo;
                ano = laudo.getAnoInicio();
            }

            laudo.setMesFinal(mes);
            laudo.setAnoFinal(ano);

        }

    }

    public void gravarLaudo() throws ProjetoException {
        try {
            verificaSeCid1FoiInserido(this.laudo.getCid1());
            
            if (!existeLaudoComMesmosDados(this.laudo.getPaciente())) {
                validarDadosSigtap(this.laudo.getPaciente(), this.laudo.getCid1());
                idLaudoGerado = null;
                idLaudoGerado = lDao.cadastrarLaudo(laudo);

                if (idLaudoGerado != null) {
                    limparDados();
                    JSFUtil.adicionarMensagemSucesso("Laudo cadastrado com sucesso!", "Sucesso");
                    JSFUtil.abrirDialog("dlgImprimir");
                } else {
                    JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
                }

            }
        } catch (Exception e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
            e.printStackTrace();
        }
    }

    private void verificaSeCid1FoiInserido(CidBean cid1) throws ProjetoException {
        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(cid1.getCid()))
            throw new ProjetoException("Por favor informe o campo CID 1");
    }

    public void imprime(){
        DataUtil.montarDataCompletaInicioMesPorAnoMesCompetencia("202004");
    }

    private void validarDadosSigtap(PacienteBean paciente, CidBean cid1) throws ProjetoException {
        if(this.unidadeValidaDadosSigtap) {
            Date dataSolicitacaoPeloSigtap = this.laudo.getDataSolicitacao();

            if (!existeCargaSigtapParaDataSolicitacao) {
                dataSolicitacaoPeloSigtap = DataUtil.retornaDataComMesAnterior(dataSolicitacaoPeloSigtap);
                this.laudo.setValidadoPeloSigtapAnterior(true);
            }
            else {
                this.laudo.setValidadoPeloSigtapAnterior(false);
            }

            idadeValida(dataSolicitacaoPeloSigtap, paciente, this.laudo.getProcedimentoPrimario().getCodProc(), true);
            validaSexoDoPacienteProcedimentoSigtap(dataSolicitacaoPeloSigtap, this.laudo.getProcedimentoPrimario().getCodProc(), paciente, true);
            if(procedimentoPossuiCidsAssociados(dataSolicitacaoPeloSigtap, this.laudo.getProcedimentoPrimario().getCodProc()))
                validaCidsDoLaudo(dataSolicitacaoPeloSigtap, cid1, this.laudo.getProcedimentoPrimario().getCodProc(), paciente, true);
            
            if ((!laudo.isProfissionalExternoLaudo()) && (!new FuncionarioDAO().funcionarioPossuiCboCompativelComProcedimento(dataSolicitacaoPeloSigtap, this.laudo.getProcedimentoPrimario().getIdProc(), this.laudo.getProfissionalLaudo().getId()))) {
                throw new ProjetoException
                ("Cbo do profissional selecionado é incompatível com o permitido no SIGTAP para o procedimento "+this.laudo.getProcedimentoPrimario().getCodProc());
            }
            //validaCboDoProfissionalLaudo(dataSolicitacaoPeloSigtap, this.laudo.getProcedimentoPrimario().getCodProc(), this.laudo.getProfissionalLaudo().getCbo(), true);
        }
    }

    public String idadeValida(Date dataValidacao, PacienteBean paciente, String codigoProcedimento, boolean lancarExcecao) throws ProjetoException {
        String msgRetornoErro = null;
        BuscaIdadePacienteDTO idadePaciente = obtemIdadePaciente(paciente.getDtanascimento());
        ProcedimentoType procedimento = buscarIdadeMinimaIhMaximaDeProcedimento(dataValidacao, codigoProcedimento);
        Boolean valido = false;


        if(procedimento.getIdadeMinimaPermitida().getUnidadeLimite().equals(UnidadeLimiteType.ANOS)
                && procedimento.getIdadeMaximaPermitida().getUnidadeLimite().equals(UnidadeLimiteType.ANOS)) {
            if(idadePaciente.getIdadeAnos() >= 0) {
                if(idadePaciente.getIdadeAnos() >= procedimento.getIdadeMinimaPermitida().getQuantidadeLimite() &&
                        idadePaciente.getIdadeAnos() <= procedimento.getIdadeMaximaPermitida().getQuantidadeLimite())
                    valido = true;
            }
        }

        else if(procedimento.getIdadeMinimaPermitida().getUnidadeLimite().equals(UnidadeLimiteType.MESES)
                && procedimento.getIdadeMaximaPermitida().getUnidadeLimite().equals(UnidadeLimiteType.MESES)) {
            if(idadePaciente.getIdadeMeses() >= 0) {
                if(idadePaciente.getIdadeMeses() >= procedimento.getIdadeMinimaPermitida().getQuantidadeLimite() &&
                        idadePaciente.getIdadeMeses() <= procedimento.getIdadeMaximaPermitida().getQuantidadeLimite())
                    valido = true;
            }
        }

        else if(procedimento.getIdadeMinimaPermitida().getUnidadeLimite().equals(UnidadeLimiteType.MESES)
                && procedimento.getIdadeMaximaPermitida().getUnidadeLimite().equals(UnidadeLimiteType.ANOS)) {
            Integer idadeEmMeses = transformaIdadeEmMeses(idadePaciente);
            if(idadeEmMeses >= 0) {
                if(idadeEmMeses >= procedimento.getIdadeMinimaPermitida().getQuantidadeLimite() && (idadeEmMeses / 12) <= procedimento.getIdadeMaximaPermitida().getQuantidadeLimite())
                    valido = true;
            }
        }

        if (!valido) {
            msgRetornoErro =  "A idade do paciente "+ paciente.getNome()+" não compreende o intervalo permitido entre idade minima e máxima para o procedimento "+codigoProcedimento;
            if (lancarExcecao)
            throw new ProjetoException(msgRetornoErro);
        }
        return msgRetornoErro;
    }

    public Boolean procedimentoPossuiCidsAssociados(Date dataSolicitacaoPeloSigtap, String codProc) throws ProjetoException {
        Boolean possuiCidsAssociados = lDao.verificaSeProcedimentoPossuiCidsAssociados
                (dataSolicitacaoPeloSigtap, codProc);
        return possuiCidsAssociados;
    }

    private Integer transformaIdadeEmMeses(BuscaIdadePacienteDTO idadePaciente) {
        Integer meses;
        if(idadePaciente.getIdadeAnos() > 0)
            meses = ((idadePaciente.getIdadeAnos() * 12) + idadePaciente.getIdadeMeses());
        else
            meses = idadePaciente.getIdadeMeses();
        return meses;
    }

    private BuscaIdadePacienteDTO obtemIdadePaciente(Date dataNascimento) throws ProjetoException {
        BuscaIdadePacienteDTO idadePaciente =
                lDao.buscarIdadePacienteEmAnoIhMes(dataNascimento);
        return idadePaciente;
    }

    private ProcedimentoType buscarIdadeMinimaIhMaximaDeProcedimento(Date dataSolicitacaoPeloSigtap, String codigoProcedimento) throws ProjetoException {
        ProcedimentoType procedimento = lDao.buscarIdadeMinimaIhMaximaDeProcedimento
                (codigoProcedimento, dataSolicitacaoPeloSigtap);
        return procedimento;
    }

    public String validaCidsDoLaudo(Date dataSolicitacaoPeloSigtap, CidBean cid1, String codProc, PacienteBean paciente, boolean lancarExcecao) throws ProjetoException {
        String msgRetornoErro = null;
    /*
        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(this.laudo.getCid2().getCid()))
            listaCidsLaudo.add(this.laudo.getCid2());

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(this.laudo.getCid3().getCid()))
            listaCidsLaudo.add(this.laudo.getCid3());
        */

           msgRetornoErro =  validarCidPorProcedimento(cid1, dataSolicitacaoPeloSigtap, codProc, paciente, lancarExcecao);
           return msgRetornoErro;
    }

    public String validarCidPorProcedimento(CidBean cidBean, Date data, String codigoProcedimento, PacienteBean paciente, boolean lancarExcecao) throws ProjetoException {
        String msgRetornoErro = null;
        if(!lDao.validaCodigoCidEmLaudo(cidBean.getCid(), data, codigoProcedimento)) {
            if (cidBean.getCid() != null) {
                msgRetornoErro = "O Paciente " + paciente.getNome() + " possui o procedimento " + codigoProcedimento + " incompatível com o CID " + cidBean.getCid();
                if (lancarExcecao)
                    throw new ProjetoException(msgRetornoErro);
            }
            if (cidBean.getCid() == null) {
                msgRetornoErro = "O Paciente " + paciente.getNome() + " possui o procedimento " + codigoProcedimento + " sem CID INFORMADO";
                if (lancarExcecao)
                throw new ProjetoException(msgRetornoErro);
            }
        }
        return msgRetornoErro;
    }

    public boolean  validaCboDoProfissionalLaudo(Date dataSolicitacaoPeloSigtap,  
    		String codProcedimento, CboBean cbo, boolean lancarExcecao) throws ProjetoException {
        boolean rst = true;
        //String codigoCboSelecionado = obtemCodigoCboSelecionado(idProfissional);
        if (!lDao.validaCodigoCboEmLaudo(cbo.getCodigo(), dataSolicitacaoPeloSigtap, codProcedimento)) {
            rst = false;
            if (lancarExcecao)
            throw new ProjetoException
                    ("Cbo do profissional selecionado é incompatível com o permitido no SIGTAP para o procedimento "+codProcedimento);
        }
        return rst;
    }

    public String validaCboPermitidoProcedimento(Date dataSolicitacaoPeloSigtap, String codigoCboSelecionado, String codProcedimento, PacienteBean paciente, boolean lancarExcecao) throws ProjetoException {
        String msgRetornoErro = null;
        if (!lDao.validaCodigoCboEmLaudo(codigoCboSelecionado, dataSolicitacaoPeloSigtap,
                codProcedimento)) {
            if (paciente != null) {
                msgRetornoErro = "Paciente: " + paciente.getNome() + ". Cbo " + codigoCboSelecionado + " é incompatível com o permitido no SIGTAP para o procedimento " + codProcedimento;
                if (lancarExcecao)
                    throw new ProjetoException
                            (msgRetornoErro);
            } else {
                msgRetornoErro = "Cbo " + codigoCboSelecionado + " é incompatível com o permitido no SIGTAP para o procedimento " + codProcedimento;
                if (lancarExcecao)
                throw new ProjetoException
                        (msgRetornoErro);
            }
        }
        return msgRetornoErro;
    }

    private String obtemCodigoCboSelecionado(Long idProfissional) throws ProjetoException {
        String codigoCboSelecionado = lDao.buscaCodigoCboProfissionalSelecionado(idProfissional);
        return codigoCboSelecionado;
    }

    public String validaSexoDoPacienteProcedimentoSigtap(Date dataSolicitacaoPeloSigtap, String codProcedimento, PacienteBean paciente, boolean lancarExcecao) throws ProjetoException {
        String msgRetornoErro = null;
        if(!lDao.sexoDoPacienteValidoComProcedimentoSigtap
                (dataSolicitacaoPeloSigtap, paciente.getSexo(), codProcedimento)) {
            msgRetornoErro = "O sexo do paciente "+paciente.getNome()+" não compreende o permitido no SIGTAP para o procedimento "+codProcedimento;
            if (lancarExcecao)
            throw new ProjetoException(msgRetornoErro);
        }
        return msgRetornoErro;
    }

    public boolean existeLaudoComMesmosDados(PacienteBean paciente) {
        try {
            if ((!laudo.isProfissionalExternoLaudo()) && (lDao.existeLaudoComMesmosDados(laudo, paciente))) {
                JSFUtil.adicionarMensagemErro
                        ("Já existe laudo para o paciente "+paciente.getNome()+ " com o mesmo profissional, data de vencimento e procedimento!", "Erro");
                return true;
            }
        } catch (ProjetoException e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
            e.printStackTrace();
        }
        return false;
    }

    public void alterarLaudo() throws ProjetoException {

        //  if(verificarSeLaudoAssociadoPacienteTerapia()) {

        verificaSeCid1FoiInserido(this.laudo.getCid1());
        validarDadosSigtap(this.laudo.getPaciente(), this.laudo.getCid1());
        boolean alterou = lDao.alterarLaudo(laudo);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Laudo alterado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgcadlaudo");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
        //   }
    }

    public void verificaSeUnidadeEstaConfiguradaParaValidarDadosDoSigtap() throws ProjetoException {
        this.unidadeValidaDadosSigtap = unidadeDAO.verificarUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
    }

    public void validaCboProfissionalParaProcedimento() throws ProjetoException {
        if (!procedimentoDAO.validaCboProfissionalParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
                laudo.getProfissionalLaudo().getId(), laudo.getDataSolicitacao())) {
            throw new ProjetoException("O profissional " + laudo.getProfissionalLaudo().getNome()
                    + " não possui um CBO válido para o procedimento primário");
        }
    }

    public void validaCidParaProcedimento() throws ProjetoException {
        if (!procedimentoDAO.validaCidParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
                laudo.getCid1().getCid(), laudo.getDataSolicitacao())) {
            throw new ProjetoException("O CID '"+laudo.getCid1().getDescCid()+"' não é válido para o procedimento primário");
        }

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(laudo.getCid2().getCid())) {
            if (!procedimentoDAO.validaCidParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
                    laudo.getCid2().getCid(), laudo.getDataSolicitacao())) {
                throw new ProjetoException("O CID "+laudo.getCid2().getDescCid()+" não é válido para o procedimento primário");
            }
        }

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(laudo.getCid3().getCid())) {
            if (!procedimentoDAO.validaCidParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
                    laudo.getCid3().getCid(), laudo.getDataSolicitacao())) {
                throw new ProjetoException("O CID "+laudo.getCid3().getDescCid()+" não é válido para o procedimento primário");
            }
        }
    }

    public void validaIdadePacienteParaProcedimento() throws ProjetoException {
        if (!procedimentoDAO.validaIdadePacienteParaProcedimento(laudo.getProcedimentoPrimario().getIdProc(),
                laudo.getPaciente().getId_paciente(), laudo.getDataSolicitacao())) {
            throw new ProjetoException("A idade do paciente é inválida para o limite aceito para o procedimento primário");
        }
    }

    public Boolean verificarSeLaudoAssociadoPacienteTerapia() throws ProjetoException {
        if (!lDao.verificarSeLaudoAssociadoPacienteTerapia(laudo.getId())) {
            JSFUtil.adicionarMensagemErro("Não é possível alterar pois o laudo já está associado a um paciente em terapia!", "Erro");
            return false;
        }
        return true;
    }

    public void excluirLaudo() throws ProjetoException {
        boolean excluiu = lDao.excluirLaudo(laudo);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Laudo excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
            listarLaudo(buscaLaudoDTO);
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }

    }

    public void alterarCheckboxPeriodoData(BuscaLaudoDTO buscaLaudoDTO) {
        if(buscaLaudoDTO.getSituacao().equals(SituacaoLaudo.TODOS.getSigla()) || buscaLaudoDTO.getSituacao().equals(SituacaoLaudo.PENDENTE.getSigla()))
            buscaLaudoDTO.setTipoPeriodoData(SituacaoLaudo.PENDENTE.getSigla());
        else
            buscaLaudoDTO.setTipoPeriodoData(SituacaoLaudo.AUTORIZADO.getSigla());

        this.buscaLaudoDTO = buscaLaudoDTO;
    }

    public void validaPeriodoBusca(BuscaLaudoDTO buscaLaudoDTO) throws ProjetoException {

        if( (!VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataInicial())
                && VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataFinal())) ||
                (VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataInicial())
                        && !VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO.getDataFinal()))) {
            JSFUtil.adicionarMensagemErro("Insira as duas datas do período de busca", "");
        }
        if (((!VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getDataFinal())) && (!VerificadorUtil.verificarSeObjetoNuloOuVazio(buscaLaudoDTO.getDataInicial()))) && (buscaLaudoDTO.getDataFinal().before(buscaLaudoDTO.getDataInicial()))){
            JSFUtil.adicionarMensagemErro("Data Final não pode ser menor que a Inicial", "");
        }

        else {
            listarLaudo(buscaLaudoDTO);
        }
    }

    public void listarLaudo(BuscaLaudoDTO buscaLaudoDTO) throws ProjetoException {
        if(VerificadorUtil.verificarSeObjetoNulo(buscaLaudoDTO)) {
            buscaLaudoDTO = new BuscaLaudoDTO();
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Integer campoBuscaSeTemPacienteLaudoNaSessao = verificarSeExistePacienteLaudoNaSessao();
        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBuscaSeTemPacienteLaudoNaSessao)) {
            buscaLaudoDTO.setCampoBusca(campoBuscaSeTemPacienteLaudoNaSessao.toString());
            buscaLaudoDTO.setTipoBusca("prontpaciente");
            buscaLaudoDTO.setSituacao("T");
        }

        listaLaudo = lDao.listaLaudos(buscaLaudoDTO);
    }

    public void iniciaNovoLaudoTelaEvolucao() {
        PacienteBean pacienteLaudo = (PacienteBean) SessionUtil.resgatarDaSessao(DadosSessao.PACIENTE_LAUDO);
        if (!VerificadorUtil.verificarSeObjetoNulo(pacienteLaudo)) {
            laudo.setPaciente(pacienteLaudo);
            laudo.setProfissionalLaudo(user_session);
            if (user_session.getProc1().getIdProc() != null) {
                laudo.getProcedimentoPrimario().setIdProc(user_session.getProc1().getIdProc());
                laudo.getProcedimentoPrimario().setNomeProc(user_session.getProc1().getNomeProc());
                laudo.setPeriodo(90); //laudo.getProcedimentoPrimario().getValidade_laudo());
            }
            laudo.setDataSolicitacao(new java.util.Date());
            laudo.setCid1(null);

            calcularPeriodoLaudo();
        }

    }

    private Integer verificarSeExistePacienteLaudoNaSessao() {
        PacienteBean pacienteLaudo = (PacienteBean) SessionUtil.resgatarDaSessao(DadosSessao.PACIENTE_LAUDO);
        if (!VerificadorUtil.verificarSeObjetoNulo(pacienteLaudo)) {
            return pacienteLaudo.getId_paciente();
        } else {
            return null;
        }
    }

    public void fecharTelaLaudoEvolucao() {
        JSFUtil.fecharDialog("dlgImprimir");
        JSFUtil.fecharDialog("dlglaudo");
        JSFUtil.fecharDialog("dlgcadlaudo");
    }

    public List<CidBean> listaCidAutoCompletePorProcedimento(String query) throws ProjetoException {
        List<CidBean> result = cDao.listarCidsAutoComplete(query);
        return result;

    }

    public void listarCids(String campoBusca) throws ProjetoException {
        listaCids = cDao.listarCidsBusca(campoBusca);
    }

    public List<CidBean> listaCidAutoCompletePorProcedimentoCid1(String query) throws ProjetoException {
        List<CidBean> result = new ArrayList<CidBean>();
        if(this.unidadeValidaDadosSigtap) {
            if(dataSolicitacaoNaoEhNula()) {
                Date dataSolicitacaoPeloSigtap = this.laudo.getDataSolicitacao();

                if (!existeCargaSigtapParaDataSolicitacao)
                    dataSolicitacaoPeloSigtap = DataUtil.retornaDataComMesAnterior(dataSolicitacaoPeloSigtap);

                if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(this.laudo.getProcedimentoPrimario().getCodProc()))
                    result = cDao.listarCidsAutoCompletePorProcedimento(query,
                            this.laudo.getProcedimentoPrimario().getCodProc(), dataSolicitacaoPeloSigtap);
            }
        }
        else
            result = cDao.listarCidsAutoComplete(query);
        return result;
    }

    public void listarCids1(String campoBusca) throws ProjetoException {
        if(this.unidadeValidaDadosSigtap) {
            if(dataSolicitacaoNaoEhNula()) {
                Date dataSolicitacaoPeloSigtap = this.laudo.getDataSolicitacao();

                if (!existeCargaSigtapParaDataSolicitacao)
                    dataSolicitacaoPeloSigtap = DataUtil.retornaDataComMesAnterior(dataSolicitacaoPeloSigtap);

                if (VerificadorUtil.verificarSeObjetoNuloOuZero(this.laudo.getProcedimentoPrimario().getIdProc()))
                    JSFUtil.adicionarMensagemAdvertencia("Selecione o procedimento primário", "");
                else
                    listaCids = cDao.listarCidsBuscaPorProcedimento(campoBusca,
                            this.laudo.getProcedimentoPrimario().getCodProc(), dataSolicitacaoPeloSigtap);
            }
        }
        else
            listaCids = cDao.listarCidsBusca(campoBusca);
    }

    private boolean dataSolicitacaoNaoEhNula() {
        if (VerificadorUtil.verificarSeObjetoNulo(this.laudo.getDataSolicitacao())) {
            JSFUtil.adicionarMensagemErro("Informe antes a data de Solicitação", "Erro");
            return false;
        }
        return true;
    }

    public String retornaTituloCidPrimarioSeHouverValidacaoSigtap() {
        if(this.unidadeValidaDadosSigtap)
            return TITULO_CID_PRIMARIO;
        return "";
    }

    public LaudoBean getLaudo() {
        return laudo;
    }

    public void setLaudo(LaudoBean laudo) {
        this.laudo = laudo;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        } else if (this.tipo.equals(TipoCabecalho.RENOVACAO.getSigla())) {
            cabecalho = CABECALHO_RENOVACAO;
        }
        return cabecalho;
    }

    private void verificaSeExisteAlgumaCargaSigtap() {
        if(this.unidadeValidaDadosSigtap) {
            this.existeAlgumaCargaSigtap = procedimentoDAO.verificaSeExisteAlgumaCargaSigtap();
            if (!this.existeAlgumaCargaSigtap)
                JSFUtil.adicionarMensagemAdvertencia("NÃO É POSSÍVEL GRAVAR OS DADOS DO LAUDO POIS NENHUMA CARGA DO SIGTAP FOI REALIZADA", "");
        }
        else
            this.existeAlgumaCargaSigtap = true;
    }

    public void existeCargaSigtapParaDataSolicitacao() {
        if(this.unidadeValidaDadosSigtap) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.laudo.getDataSolicitacao());
            int mesSolicitacao = calendar.get(Calendar.MONTH);
            mesSolicitacao++;
            int anoSolicitacao = calendar.get(Calendar.YEAR);
            this.existeCargaSigtapParaDataSolicitacao = procedimentoDAO.verificaExisteCargaSigtapParaData(mesSolicitacao, anoSolicitacao);
        }
        else
            this.existeCargaSigtapParaDataSolicitacao = true;
    }

    public void iniciarConfiguracoesLaudoEmLote() throws ProjetoException {
        this.usuarioPodeRealizarAutorizacao = user_session.getPermiteAutorizacaoLaudo();
        verificaSeUnidadeEstaConfiguradaParaValidarDadosDoSigtap();
        verificaSeExisteAlgumaCargaSigtap();
        listarPacientes();
    }

    private void listarPacientes() throws ProjetoException {
        this.listaPacientes = pacienteDAO.listaPacientes();
        this.listaPacientesFiltro.addAll(listaPacientes);
    }

    public void adicionarPacienteSelecionado(PacienteLaudoEmLoteDTO pacienteSelecionado) throws ProjetoException {
        verificaSeCid1FoiInserido(pacienteSelecionado.getCid1());
        this.listaPacienteLaudoEmLoteDTO.add(pacienteSelecionado);
        JSFUtil.fecharDialog("dlgPaciente");
    }

    public void removerPacienteSelecionado(PacienteLaudoEmLoteDTO pacienteSelecionado) throws ProjetoException {
        this.listaPacienteLaudoEmLoteDTO.remove(pacienteSelecionado);
    }

    public void pacienteFoiAdicionado(PacienteBean pacienteSelecionado) {
        for (PacienteLaudoEmLoteDTO pacienteLaudoEmLoteDTO : listaPacienteLaudoEmLoteDTO) {
            if(pacienteLaudoEmLoteDTO.getPaciente().getId_paciente().equals(pacienteSelecionado.getId_paciente())) {
                JSFUtil.adicionarMensagemAdvertencia("Este paciente já foi adicionado", "");
                return;
            }
        }

        this.pacienteLaudoEmLoteSelecionado = new PacienteLaudoEmLoteDTO(pacienteSelecionado);
        JSFUtil.abrirDialog("dlgPaciente");
    }

    public void gravarLaudosEmLotes() throws ProjetoException {

        for (PacienteLaudoEmLoteDTO pacienteLaudoEmLoteDTO : listaPacienteLaudoEmLoteDTO) {
            if (!existeLaudoComMesmosDados(pacienteLaudoEmLoteDTO.getPaciente())) {
                validarDadosSigtap(pacienteLaudoEmLoteDTO.getPaciente(), pacienteLaudoEmLoteDTO.getCid1());
            }
            else {
                return;
            }
        }

        if (lDao.cadastrarLaudosEmLote(laudo, listaPacienteLaudoEmLoteDTO)) {
            limparDadosLaudoEmLotes();
            JSFUtil.adicionarMensagemSucesso("Laudos cadastrados com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    private void limparDadosLaudoEmLotes() {
        this.laudo = new LaudoBean();
        this.listaPacienteLaudoEmLoteDTO.clear();
        this.pacienteLaudoEmLoteSelecionado = new PacienteLaudoEmLoteDTO();
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public List<LaudoBean> getListaLaudo() {
        return listaLaudo;
    }

    public void setListaLaudo(List<LaudoBean> listaLaudo) {
        this.listaLaudo = listaLaudo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<CidBean> getListaCids() {
        return listaCids;
    }

    public void setListaCids(List<CidBean> listaCids) {
        this.listaCids = listaCids;
    }

    public Boolean getRenderizarDataAutorizacao() {
        return renderizarDataAutorizacao;
    }

    public void setRenderizarDataAutorizacao(Boolean renderizarDataAutorizacao) {
        this.renderizarDataAutorizacao = renderizarDataAutorizacao;
    }

    public Integer getIdLaudoGerado() {
        return idLaudoGerado;
    }

    public void setIdLaudoGerado(Integer idLaudoGerado) {
        this.idLaudoGerado = idLaudoGerado;
    }


    public BuscaLaudoDTO getBuscaLaudoDTO() {
        return buscaLaudoDTO;
    }

    public void setBuscaLaudoDTO(BuscaLaudoDTO buscaLaudoDTO) {
        this.buscaLaudoDTO = buscaLaudoDTO;
    }

    public boolean isExisteAlgumaCargaSigtap() {
        return existeAlgumaCargaSigtap;
    }

    public void setExisteAlgumaCargaSigtap(boolean existeAlgumaCargaSigtap) {
        this.existeAlgumaCargaSigtap = existeAlgumaCargaSigtap;
    }

    public Boolean getUsuarioPodeRealizarAutorizacao() {
        return usuarioPodeRealizarAutorizacao;
    }

    public void setUsuarioPodeRealizarAutorizacao(Boolean usuarioPodeRealizarAutorizacao) {
        this.usuarioPodeRealizarAutorizacao = usuarioPodeRealizarAutorizacao;
    }

    public String getCabecalhoLaudoEmLote() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalhoLaudoEmLote = CABECALHO_INCLUSAO_LOTE;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalhoLaudoEmLote = CABECALHO_ALTERACAO_LOTE;
        }
        return cabecalhoLaudoEmLote;
    }

    public void setCabecalhoLaudoEmLote(String cabecalhoLaudoEmLote) {
        this.cabecalhoLaudoEmLote = cabecalhoLaudoEmLote;
    }

    public List<PacienteLaudoEmLoteDTO> getListaPacienteLaudoEmLoteDTO() {
        return listaPacienteLaudoEmLoteDTO;
    }

    public void setListaPacienteLaudoEmLoteDTO(List<PacienteLaudoEmLoteDTO> listaPacienteLaudoEmLoteDTO) {
        this.listaPacienteLaudoEmLoteDTO = listaPacienteLaudoEmLoteDTO;
    }

    public List<PacienteBean> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<PacienteBean> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<PacienteBean> getListaPacientesFiltro() {
        return listaPacientesFiltro;
    }

    public void setListaPacientesFiltro(List<PacienteBean> listaPacientesFiltro) {
        this.listaPacientesFiltro = listaPacientesFiltro;
    }

    public PacienteLaudoEmLoteDTO getPacienteLaudoEmLoteSelecionado() {
        return pacienteLaudoEmLoteSelecionado;
    }

    public void setPacienteLaudoEmLoteSelecionado(PacienteLaudoEmLoteDTO pacienteLaudoEmLoteSelecionado) {
        this.pacienteLaudoEmLoteSelecionado = pacienteLaudoEmLoteSelecionado;
    }
}
