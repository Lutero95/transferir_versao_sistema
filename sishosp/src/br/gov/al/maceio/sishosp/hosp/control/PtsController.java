package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.FiltroBuscaVencimentoPTS;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenhaAgenda;
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

    //CONSTANTES
    private static final String ENDERECO_PTS = "pts?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de PTS";
    private static final String CABECALHO_ALTERACAO = "Alteração de PTS";

    public PtsController() {
        pts = new Pts();
        ptsAreaAnterior = new PtsArea();
        listaPts = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        filtroTipoVencimento = FiltroBuscaVencimentoPTS.VINGENTES.getSigla();
        rowBean = new Pts();
        renderizarBotaoNovo = false;
    }

    public void carregarPts() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            try {
                existePts = pDao.verificarSeExistePts(id);
                if (existePts) {
                    this.pts = pDao.ptsCarregarPacientesInstituicaoIhPts(id);
                } else {
                    this.pts = pDao.ptsCarregarPacientesInstituicao(id);
                }
                listaEspecialidadesEquipe = eDao.listarEspecialidadesEquipe(id);
            } catch (ProjetoException e) {
                e.printStackTrace();
            }
        }
        else{
            pts = (Pts) SessionUtil.resgatarDaSessao("pts");
        }
    }

    public void buscarPtsPacientesAtivos() throws ProjetoException {
        listaPts = pDao.buscarPtsPacientesAtivos(pts.getPrograma().getIdPrograma(), pts.getGrupo().getIdGrupo(), filtroTipoVencimento, filtroMesVencimento, filtroAnoVencimento);
    }

    public void limparBusca(){
        listaPts = new ArrayList<>();
        pts = new Pts();
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_PTS, ENDERECO_ID, this.pts.getId());
    }

    public String redirectInsert() {
        SessionUtil.adicionarNaSessao(rowBean, "pts");
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_PTS);
    }

    public void abrirDialogInclusaoPts() {
        limparInclusaoAreaPts();

        JSFUtil.abrirDialog("dlgInclusaoAreaPts");
    }

    public void abrirDialogEdicaoPts(PtsArea ptsEditar) {
        pts.setPtsArea(ptsEditar);

        JSFUtil.abrirDialog("dlgInclusaoAreaPts");
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
                listaGrupos=  grDao.listarGruposPorPrograma(this.pts
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

    public void gravarPts() throws ProjetoException {

        boolean cadastrou = pDao.gravarPts(pts, existePts);

        if (cadastrou == true) {
            existePts = true;
            this.pts = pDao.ptsCarregarPacientesInstituicaoIhPts(pts.getGerenciarPaciente().getId());
            JSFUtil.adicionarMensagemSucesso("PTS cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }
    }

    public void validarSenhaAdicionarAreaPts() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        Integer idFuncionario = funcionarioDAO.validarCpfIhSenha(pts.getPtsArea().getFuncionario().getCpf(),
                pts.getPtsArea().getFuncionario().getSenha(), ValidacaoSenhaAgenda.ADICIONAR_AREA_PTS.getSigla());

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

        if(!VerificadorUtil.verificarSeObjetoNuloOuZero(rowBean.getInsercao().getId())){
            if(!pDao.verificarSeExistePtsParaProgramaGrupoPaciente(rowBean)){
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
