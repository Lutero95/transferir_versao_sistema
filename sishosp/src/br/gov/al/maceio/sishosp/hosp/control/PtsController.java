package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConverterUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
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
    private Pts pts;
    private List<EspecialidadeBean> listaEspecialidadesEquipe;
    private List<PtsArea> listaPts;
    private String tipo;

    public PtsController() {
       pts = new Pts();
       listaPts = new ArrayList<>();
    }

    public void carregarPts() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            try {
                this.pts = pDao.carregarPacientesInstituicaoPts(id);
                EspecialidadeDAO eDao = new EspecialidadeDAO();
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

    private void limparInclusaoAreaPts(){
       pts.setPtsArea(new PtsArea());
    }

    private void limparDados(){
        pts = new Pts();
        listaPts = new ArrayList<>();
    }

    private void addPtsNaLista(){
        listaPts.add(pts.getPtsArea());
        JSFUtil.fecharDialog("dlgInclusaoAreaPts");
    }

    public void gravarPts() {

        boolean cadastrou = pDao.gravarPts(pts, listaPts);

        if (cadastrou == true) {
            limparDados();
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

    public List<PtsArea> getListaPts() {
        return listaPts;
    }

    public void setListaPts(List<PtsArea> listaPts) {
        this.listaPts = listaPts;
    }

}
