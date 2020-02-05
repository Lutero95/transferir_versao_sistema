package br.gov.al.maceio.sishosp.administrativo.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.dao.InsercaoProfissionalEquipeDAO;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.enums.TipoDataAgenda;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import org.primefaces.event.SelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class InsercaoProfissionalEquipeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private InsercaoProfissionalEquipe insercaoProfissionalEquipe;
    private InsercaoProfissionalEquipeDAO iDao = new InsercaoProfissionalEquipeDAO();
    private List<InsercaoProfissionalEquipe> listaAInsercaoProfissionalEquipe;
    private ProgramaBean programaSelecionado;
    private List<GrupoBean> listaGruposProgramas;
    private GrupoDAO gDao = new GrupoDAO();
    private GrupoBean grupoSelecionado;
    private String tipoData;
    private List<EquipeBean> listaEquipePorGrupo;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "incluirprofissionalatendimento?faces-redirect=true";


    public InsercaoProfissionalEquipeController() {
        this.insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();
        listaAInsercaoProfissionalEquipe = new ArrayList<>();
        listaGruposProgramas = new ArrayList<>();
        listaEquipePorGrupo = new ArrayList<>();
        tipoData = TipoDataAgenda.DATA_UNICA.getSigla();
    }


    public String redirectInsert() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_CADASTRO);
    }

    public void limparDados() {
        insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();

    }

    public void gravarInsercaoProfissionalEquipe() {
        setarDataUnica();
        boolean cadastrou = iDao.gravarInsercaoGeral(this.insercaoProfissionalEquipe);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Inserção de profisisonal na equipe cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
        }

    }

    public void validarDatasInicioIhFim() {
        if (!VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoInicio())
                && !VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())) {
            if (insercaoProfissionalEquipe.getPeriodoFinal().before(insercaoProfissionalEquipe.getPeriodoInicio())) {
                limparDatas();
                JSFUtil.adicionarMensagemErro("A data de início não pode ser maior que a data fim!", "Erro");
            }
        }
    }

    public void setarDataUnica(){
        if(tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())){
            insercaoProfissionalEquipe.setPeriodoFinal(insercaoProfissionalEquipe.getPeriodoInicio());
        }
    }

    public void limparDatas() {
        insercaoProfissionalEquipe.setPeriodoInicio(null);
        insercaoProfissionalEquipe.setPeriodoFinal(null);
    }

    public void limparNaBuscaPrograma() {
        insercaoProfissionalEquipe.setGrupo(new GrupoBean());
    }

    public void limparNaBuscaGrupo() {
        insercaoProfissionalEquipe.setEquipe(new EquipeBean());
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
        this.programaSelecionado = (ProgramaBean) event.getObject();
        atualizaListaGrupos(programaSelecionado);
        limparNaBuscaPrograma();
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
        this.programaSelecionado = p;
        this.listaGruposProgramas = gDao.listarGruposPorPrograma(p.getIdPrograma());
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query) throws ProjetoException {
        if (insercaoProfissionalEquipe.getPrograma().getIdPrograma() != null) {
            return gDao.listarGruposNoAutoComplete(query, this.insercaoProfissionalEquipe.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    public void selectGrupo(SelectEvent event) {
        this.grupoSelecionado = (GrupoBean) event.getObject();
        limparNaBuscaGrupo();
    }

    public List<EquipeBean> listaEquipeAutoComplete(String query) throws ProjetoException {
        EquipeDAO eDao = new EquipeDAO();
        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
        return result;
    }

    public List<FuncionarioBean> listaProfissionalPorGrupoAutoComplete(String query) throws ProjetoException {
        FuncionarioDAO fDao = new FuncionarioDAO();
        List<FuncionarioBean> result = fDao.listarProfissionalBuscaPorGrupo(query, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
        return result;
    }

    public void carregaListaEquipePorGrupo() throws ProjetoException {
        EquipeDAO eDao = new EquipeDAO();
        if (insercaoProfissionalEquipe.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = eDao.listarEquipePorGrupo(insercaoProfissionalEquipe.getGrupo().getIdGrupo());
        }
    }

    public InsercaoProfissionalEquipe getInsercaoProfissionalEquipe() {
        return insercaoProfissionalEquipe;
    }

    public void setInsercaoProfissionalEquipe(InsercaoProfissionalEquipe insercaoProfissionalEquipe) {
        this.insercaoProfissionalEquipe = insercaoProfissionalEquipe;
    }

    public List<InsercaoProfissionalEquipe> getListaAInsercaoProfissionalEquipe() {
        return listaAInsercaoProfissionalEquipe;
    }

    public void setListaAInsercaoProfissionalEquipe(List<InsercaoProfissionalEquipe> listaAInsercaoProfissionalEquipe) {
        this.listaAInsercaoProfissionalEquipe = listaAInsercaoProfissionalEquipe;
    }

    public ProgramaBean getProgramaSelecionado() {
        return programaSelecionado;
    }

    public void setProgramaSelecionado(ProgramaBean programaSelecionado) {
        this.programaSelecionado = programaSelecionado;
    }

    public List<GrupoBean> getListaGruposProgramas() {
        return listaGruposProgramas;
    }

    public void setListaGruposProgramas(List<GrupoBean> listaGruposProgramas) {
        this.listaGruposProgramas = listaGruposProgramas;
    }

    public String getTipoData() {
        return tipoData;
    }

    public void setTipoData(String tipoData) {
        this.tipoData = tipoData;
    }

    public List<EquipeBean> getListaEquipePorGrupo() {
        return listaEquipePorGrupo;
    }

    public void setListaEquipePorGrupo(List<EquipeBean> listaEquipePorGrupo) {
        this.listaEquipePorGrupo = listaEquipePorGrupo;
    }
}
