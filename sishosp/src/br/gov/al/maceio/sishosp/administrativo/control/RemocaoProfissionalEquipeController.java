package br.gov.al.maceio.sishosp.administrativo.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.dao.RemocaoProfissionalEquipeDAO;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarRemocaoAtendimentoDTO;
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
public class RemocaoProfissionalEquipeController implements Serializable {

    private static final long serialVersionUID = 1L;
    private RemocaoProfissionalEquipe remocaoProfissionalEquipe;
    private RemocaoProfissionalEquipeDAO rDao = new RemocaoProfissionalEquipeDAO();
    private List<RemocaoProfissionalEquipe> listaRemocaoProfissionalEquipe;
    private ProgramaBean programaSelecionado;
    private List<GrupoBean> listaGruposProgramas;
    private GrupoDAO gDao = new GrupoDAO();
    private GrupoBean grupoSelecionado;
    private String tipoData;
    private List<EquipeBean> listaEquipePorGrupo;
    private List<RemocaoProfissionalEquipe> listaRemovidos;
    private List<RemocaoProfissionalEquipe> listaSeremRemovidos;
    private List<RemocaoProfissionalEquipe> listaSelecaoSeremRemovidos;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "excluirprofissionalatendimento?faces-redirect=true";


    public RemocaoProfissionalEquipeController() {
        this.remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
        listaRemocaoProfissionalEquipe = new ArrayList<>();
        listaGruposProgramas = new ArrayList<>();
        listaEquipePorGrupo = new ArrayList<>();
        tipoData = TipoDataAgenda.DATA_UNICA.getSigla();
        listaSeremRemovidos = new ArrayList<>();
        listaSelecaoSeremRemovidos = new ArrayList<>();
        listaRemovidos = new ArrayList<>();
    }


    public String redirectNovo() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_CADASTRO);
    }

    public void limparDados() {
        remocaoProfissionalEquipe = new RemocaoProfissionalEquipe();
        tipoData = TipoDataAgenda.DATA_UNICA.getSigla();
        listaSeremRemovidos = new ArrayList<>();
        listaSelecaoSeremRemovidos = new ArrayList<>();
    }

    public void listarRemovidos() {
        listaRemovidos = rDao.listarRemovidos();
    }

    public void listarSeremRemovidos() {
        tratarGrupo();
        listaSeremRemovidos = rDao.listarSeremRemovidos(remocaoProfissionalEquipe);
    }

    public void gravarRemocaoProfissionalEquipe() {
        setarDataUnica();
        tratarGrupo();

        if(listaSelecaoSeremRemovidos.isEmpty()){
            JSFUtil.adicionarMensagemErro("Selecione ao menos 1 atendimento", "Erro");
            return;
        }

        GravarRemocaoAtendimentoDTO gravarRemocaoAtendimentoDTO = new GravarRemocaoAtendimentoDTO(remocaoProfissionalEquipe, listaSelecaoSeremRemovidos,
                null, null);

        Boolean removeu = rDao.gravarRemocao(gravarRemocaoAtendimentoDTO);

        if (removeu) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Remoção de profisisonal na equipe realizada com sucesso!", "Sucesso");
        }
        else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a remoção", "Erro");
        }

    }

    public void validarDatasInicioIhFim() {
        if (!VerificadorUtil.verificarSeObjetoNulo(remocaoProfissionalEquipe.getPeriodoInicio())
                && !VerificadorUtil.verificarSeObjetoNulo(remocaoProfissionalEquipe.getPeriodoFinal())) {
            if (remocaoProfissionalEquipe.getPeriodoFinal().before(remocaoProfissionalEquipe.getPeriodoInicio())) {
                limparDatas();
                JSFUtil.adicionarMensagemErro("A data de início não pode ser maior que a data fim!", "Erro");
            }
        }
    }

    private void setarDataUnica(){
        if(tipoData.equals(TipoDataAgenda.DATA_UNICA.getSigla())){
            remocaoProfissionalEquipe.setPeriodoFinal(remocaoProfissionalEquipe.getPeriodoInicio());
        }
    }

    private void limparDatas() {
        remocaoProfissionalEquipe.setPeriodoInicio(null);
        remocaoProfissionalEquipe.setPeriodoFinal(null);
    }

    public void limparNaBuscaPrograma() {
        remocaoProfissionalEquipe.setGrupo(new GrupoBean());
    }

    public void limparNaBuscaGrupo() {
        remocaoProfissionalEquipe.setEquipe(new EquipeBean());
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
        if (remocaoProfissionalEquipe.getPrograma().getIdPrograma() != null) {
            return gDao.listarGruposNoAutoComplete(query, this.remocaoProfissionalEquipe.getPrograma().getIdPrograma());
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
        List<EquipeBean> result = eDao.listarEquipePorGrupoAutoComplete(query, remocaoProfissionalEquipe.getGrupo().getIdGrupo());
        return result;
    }

    public List<FuncionarioBean> listaProfissionalAutoComplete(String query) throws ProjetoException {
        FuncionarioDAO fDao = new FuncionarioDAO();
        List<FuncionarioBean> result = fDao.listarProfissionalBusca(query, 1);
        return result;
    }

    public void carregaListaEquipePorGrupo() throws ProjetoException {
        EquipeDAO eDao = new EquipeDAO();
        if (remocaoProfissionalEquipe.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = eDao.listarEquipePorGrupo(remocaoProfissionalEquipe.getGrupo().getIdGrupo());
        }
    }

    private void tratarGrupo(){
        if(VerificadorUtil.verificarSeObjetoNulo(remocaoProfissionalEquipe.getGrupo())){
            remocaoProfissionalEquipe.setGrupo(new GrupoBean());
        }
    }

    public RemocaoProfissionalEquipe getRemocaoProfissionalEquipe() {
        return remocaoProfissionalEquipe;
    }

    public void setRemocaoProfissionalEquipe(RemocaoProfissionalEquipe remocaoProfissionalEquipe) {
        this.remocaoProfissionalEquipe = remocaoProfissionalEquipe;
    }

    public List<RemocaoProfissionalEquipe> getListaRemocaoProfissionalEquipe() {
        return listaRemocaoProfissionalEquipe;
    }

    public void setListaRemocaoProfissionalEquipe(List<RemocaoProfissionalEquipe> listaRemocaoProfissionalEquipe) {
        this.listaRemocaoProfissionalEquipe = listaRemocaoProfissionalEquipe;
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

    public List<RemocaoProfissionalEquipe> getListaSeremRemovidos() {
        return listaSeremRemovidos;
    }

    public void setListaSeremRemovidos(List<RemocaoProfissionalEquipe> listaSeremRemovidos) {
        this.listaSeremRemovidos = listaSeremRemovidos;
    }

    public List<RemocaoProfissionalEquipe> getListaSelecaoSeremRemovidos() {
        return listaSelecaoSeremRemovidos;
    }

    public void setListaSelecaoSeremRemovidos(List<RemocaoProfissionalEquipe> listaSelecaoSeremRemovidos) {
        this.listaSelecaoSeremRemovidos = listaSelecaoSeremRemovidos;
    }

    public List<RemocaoProfissionalEquipe> getListaRemovidos() {
        return listaRemovidos;
    }

    public void setListaRemovidos(List<RemocaoProfissionalEquipe> listaRemovidos) {
        this.listaRemovidos = listaRemovidos;
    }
}
