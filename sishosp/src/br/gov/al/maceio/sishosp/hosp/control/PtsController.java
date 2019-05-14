package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConverterUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
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
    private Boolean existePts;

    public PtsController() {
        pts = new Pts();
        ptsAreaAnterior = new PtsArea();
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
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }

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

    private void addPtsNaLista() throws ProjetoException {
        if(!VerificadorUtil.verificarSeObjetoNulo(ptsAreaAnterior)) {
            removerPtsDaLista(ptsAreaAnterior);
        }

        pts.getPtsArea().setArea(eDao.listarEspecialidadePorId(pts.getPtsArea().getArea().getCodEspecialidade()));
        pts.getListaPtsArea().add(pts.getPtsArea());

        JSFUtil.fecharDialog("dlgInclusaoAreaPts");
    }

    public void removerPtsDaLista(PtsArea ptsRemover) {
        pts.getListaPtsArea().remove(ptsRemover);
    }

    public void gravarPts() {

        boolean cadastrou = pDao.gravarPts(pts, existePts);

        if (cadastrou == true) {
            limparDados();
            existePts = true;
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

}
