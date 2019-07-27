package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.FiltroBuscaVencimentoPTS;
import br.gov.al.maceio.sishosp.hosp.enums.StatusPTS;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "PtsController")
@ViewScoped
public class PtsController implements Serializable {

    private static final long serialVersionUID = 1L;
    private PtsDAO pDao = new PtsDAO();
    private EspecialidadeDAO eDao = new EspecialidadeDAO();
    private Pts pts;
    private PtsArea ptsAreaAnterior;
    private List<EspecialidadeBean> listaEspecialidadesEquipe;
    private List<Pts> listaPts;
    private List<GrupoBean> listaGrupos;
    private Boolean existePts;
    private String filtroTipoVencimento;
    private Integer filtroMesVencimento;
    private Integer filtroAnoVencimento;
    private Pts rowBean;
    private Boolean renderizarBotaoNovo;
    private Integer idParametroEndereco;
    private String statusPts;

    //CONSTANTES
    private static final String ENDERECO_PTS = "pts?faces-redirect=true";
    private static final String ENDERECO_RENOVACAO = "ptsrenovacao?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String ENDERECO_ID_GER_PAC_INSTITUICAO = "&amp;idgerpaciente=";    

    public PtsController() {
        pts = new Pts();
        ptsAreaAnterior = new PtsArea();
        listaPts = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        filtroTipoVencimento = FiltroBuscaVencimentoPTS.TODOS.getSigla();
        rowBean = new Pts();
        renderizarBotaoNovo = false;
    }

    public void carregarPts() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            Integer idGerenciamentoPaciente = Integer.parseInt(params.get("idgerpaciente"));
            
            idParametroEndereco = id;
            existePts = pDao.verificarSeExistePts(id);
            if (existePts) {
                this.pts = pDao.ptsCarregarPtsPorId(id);
            } else {
                this.pts = pDao.ptsCarregarPacientesInstituicao(id);
            }
            listaEspecialidadesEquipe = eDao.listarEspecialidadesEquipe(idGerenciamentoPaciente);

        } else {
            existePts = false;
            pts = (Pts) SessionUtil.resgatarDaSessao("pts");
            listaEspecialidadesEquipe = eDao.listarEspecialidadesEquipe(pts.getGerenciarPaciente().getId());
        }
    }

    public void carregarPtsRenovacao() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            idParametroEndereco = id;
            statusPts = pDao.verificarStatusPts(id);
            if (statusPts.equals(StatusPTS.RENOVADO.getSigla())) {
                this.pts = pDao.ptsCarregarPtsPorId(id);
            } else {
                pts = (Pts) SessionUtil.resgatarDaSessao("pts");
            }
            listaEspecialidadesEquipe = eDao.listarEspecialidadesEquipe(pts.getGerenciarPaciente().getId());

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }
    }

    public void buscarPtsPacientesAtivos() throws ProjetoException {
        listaPts = pDao.buscarPtsPacientesAtivos(pts.getPrograma().getIdPrograma(), pts.getGrupo().getIdGrupo(), filtroTipoVencimento, filtroMesVencimento, filtroAnoVencimento);
    }

    public void limparBusca() {
        listaPts = new ArrayList<>();
        pts = new Pts();
    }

    public String redirectEdit() throws ProjetoException {
        String retorno = "";
        statusPts = pDao.verificarStatusPts(rowBean.getId());
        if (statusPts.equals(StatusPTS.RENOVADO.getSigla())) {
            SessionUtil.adicionarNaSessao(rowBean, "pts");
            retorno = RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_RENOVACAO, ENDERECO_ID, this.rowBean.getId(),ENDERECO_ID_GER_PAC_INSTITUICAO, this.rowBean.getGerenciarPaciente().getId());
        } else {
            retorno = RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_PTS, ENDERECO_ID, this.rowBean.getId(),ENDERECO_ID_GER_PAC_INSTITUICAO, this.rowBean.getGerenciarPaciente().getId());
        }
        return retorno;
    }

    public String redirectEditAposGravar(Integer novoIdPts, Integer PacienteInstituicao) {
        return RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_PTS, ENDERECO_ID, novoIdPts,ENDERECO_ID_GER_PAC_INSTITUICAO,PacienteInstituicao);
    }

    public String redirectInsert() {
        SessionUtil.adicionarNaSessao(rowBean, "pts");
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_PTS);
    }

    public String redirectRenovacao() {
        SessionUtil.adicionarNaSessao(rowBean, "pts");
        return RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_RENOVACAO, ENDERECO_ID, this.rowBean.getId(),ENDERECO_ID_GER_PAC_INSTITUICAO,this.rowBean.getGerenciarPaciente().getId());
    }

    public void abrirDialogInclusaoPts() {
        limparInclusaoAreaPts();

        JSFUtil.abrirDialog("dlgInclusaoAreaPts");
    }

    public void abrirDialogEdicaoPts(PtsArea ptsEditar) {
        pts.setPtsArea(ptsEditar);

        JSFUtil.abrirDialog("dlgInclusaoAreaPts");
    }

    public void abrirDialogCancelarPts() {
        JSFUtil.abrirDialog("dlgCancelarPts");
    }

    private void limparInclusaoAreaPts() {
        pts.setPtsArea(new PtsArea());
    }

    private void limparDados() {
        pts = new Pts();
        pts.setListaPtsArea(new ArrayList<>());
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query)
            throws ProjetoException {

        GrupoDAO grDao = new GrupoDAO();

        if (pts.getPrograma().getIdPrograma() != null) {
            return grDao.listarGruposNoAutoComplete(query,
                    this.pts.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    public void carregaGruposDoPrograma() throws ProjetoException {

        GrupoDAO grDao = new GrupoDAO();
        if (pts.getPrograma() != null) {
            if (pts.getPrograma().getIdPrograma() != null) {
                listaGrupos = grDao.listarGruposPorPrograma(this.pts
                        .getPrograma().getIdPrograma());
            }
        }

    }

    private void addPtsNaLista() throws ProjetoException {
        if (!VerificadorUtil.verificarSeObjetoNulo(ptsAreaAnterior)) {
            removerPtsDaLista(ptsAreaAnterior);
        }

        pts.getPtsArea().setArea(eDao.listarEspecialidadePorId(pts.getPtsArea().getArea().getCodEspecialidade()));
        pts.getListaPtsArea().add(pts.getPtsArea());

        JSFUtil.fecharDialog("dlgInclusaoAreaPts");
    }

    public void removerPtsDaLista(PtsArea ptsRemover) {
        pts.getListaPtsArea().remove(ptsRemover);
    }

    public String gravarPts() {

        String retorno = null;

        Integer novoIdPts = pDao.gravarPts(pts, existePts, StatusPTS.ATIVO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(novoIdPts)) {
            existePts = true;
            JSFUtil.adicionarMensagemSucesso("PTS cadastrado com sucesso!", "Sucesso");

            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(idParametroEndereco)) {
                retorno = redirectEditAposGravar(novoIdPts, pts.getGerenciarPaciente().getId());
            }

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

        return retorno;
    }

    public String gravarRenovacaoPts() {

        String retorno = null;

        if (statusPts.equals(StatusPTS.RENOVADO.getSigla())) {
            existePts = true;
        } else {
            existePts = false;
        }

        Integer novoIdPts = pDao.gravarPts(pts, existePts, StatusPTS.RENOVADO.getSigla());

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(novoIdPts)) {
            existePts = true;
            JSFUtil.adicionarMensagemSucesso("PTS renovado com sucesso!", "Sucesso");

            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(idParametroEndereco)) {
                retorno = redirectEditAposGravar(novoIdPts, null);
            }

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a renovacao!", "Erro");
        }

        return retorno;
    }

    public void cancelarPts() {

        Boolean cancelou = pDao.cancelarPts(rowBean.getId());

        if (cancelou) {
            JSFUtil.adicionarMensagemSucesso("PTS cancelado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgCancelarPts");
            limparBusca();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cancelamento!", "Erro");
        }
    }

    public void validarSenhaAdicionarAreaPts() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        Integer idFuncionario = funcionarioDAO.validarCpfIhSenha(pts.getPtsArea().getFuncionario().getCpf(),
                pts.getPtsArea().getFuncionario().getSenha(), ValidacaoSenha.ADICIONAR_AREA_PTS.getSigla());

        if (idFuncionario > 0) {
            pts.getPtsArea().getFuncionario().setId(ConverterUtil.converterIntParaLong(idFuncionario));
            JSFUtil.fecharDialog("dlgSenhaAreaPts");
            addPtsNaLista();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada!", "Erro!");
        }
    }

    public void abrirDialogDigitacaoSenha() {
        JSFUtil.abrirDialog("dlgSenhaAreaPts");
    }

    public Pts carregarPtsPaciente(Integer codPaciente) throws ProjetoException {
        return pDao.carregarPtsDoPaciente(codPaciente);
    }

    public void verificarSeExistePtsParaProgramaGrupoPaciente() throws ProjetoException {

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rowBean.getGerenciarPaciente().getId())) {
            if (!pDao.verificarSeExistePtsParaProgramaGrupoPaciente(rowBean)) {
                renderizarBotaoNovo = true;
            }
        }

    }

    public Pts getPts() {
        return pts;
    }

    public void setPts(Pts pts) {
        this.pts = pts;
    }

    public List<EspecialidadeBean> getListaEspecialidadesEquipe() {
        return listaEspecialidadesEquipe;
    }

    public void setListaEspecialidadesEquipe(List<EspecialidadeBean> listaEspecialidadesEquipe) {
        this.listaEspecialidadesEquipe = listaEspecialidadesEquipe;
    }

    public PtsArea getPtsAreaAnterior() {
        return ptsAreaAnterior;
    }

    public void setPtsAreaAnterior(PtsArea ptsAreaAnterior) {
        this.ptsAreaAnterior = ptsAreaAnterior;
    }

    public List<Pts> getListaPts() {
        return listaPts;
    }

    public void setListaPts(List<Pts> listaPts) {
        this.listaPts = listaPts;
    }

    public String getFiltroTipoVencimento() {
        return filtroTipoVencimento;
    }

    public void setFiltroTipoVencimento(String filtroTipoVencimento) {
        this.filtroTipoVencimento = filtroTipoVencimento;
    }

    public Integer getFiltroMesVencimento() {
        return filtroMesVencimento;
    }

    public void setFiltroMesVencimento(Integer filtroMesVencimento) {
        this.filtroMesVencimento = filtroMesVencimento;
    }

    public Integer getFiltroAnoVencimento() {
        return filtroAnoVencimento;
    }

    public void setFiltroAnoVencimento(Integer filtroAnoVencimento) {
        this.filtroAnoVencimento = filtroAnoVencimento;
    }

    public List<GrupoBean> getListaGrupos() {
        return listaGrupos;
    }

    public void setListaGrupos(List<GrupoBean> listaGrupos) {
        this.listaGrupos = listaGrupos;
    }

    public Pts getRowBean() {
        return rowBean;
    }

    public void setRowBean(Pts rowBean) {
        this.rowBean = rowBean;
    }

    public Boolean getRenderizarBotaoNovo() {
        return renderizarBotaoNovo;
    }

    public void setRenderizarBotaoNovo(Boolean renderizarBotaoNovo) {
        this.renderizarBotaoNovo = renderizarBotaoNovo;
    }
}
