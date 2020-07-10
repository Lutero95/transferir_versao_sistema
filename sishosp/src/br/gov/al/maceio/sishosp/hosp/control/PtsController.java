package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.shared.TelasBuscaSessao;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.enums.FiltroBuscaVencimentoPTS;
import br.gov.al.maceio.sishosp.hosp.enums.StatusPTS;
import br.gov.al.maceio.sishosp.hosp.enums.ValidacaoSenha;
import br.gov.al.maceio.sishosp.hosp.model.*;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

import static br.gov.al.maceio.sishosp.comum.shared.DadosSessao.*;

@ManagedBean(name = "PtsController")
@ViewScoped
public class PtsController implements Serializable {

    private static final long serialVersionUID = 1L;
    private PtsDAO ptsDao = new PtsDAO();
    private EspecialidadeDAO eDao = new EspecialidadeDAO();
    private Pts pts;
    private PtsArea ptsAreaAnterior,ptsAreaAtual;
    private List<EspecialidadeBean> listaEspecialidadesEquipe;
    private List<Pts> listaPts;
    private List<GrupoBean> listaGrupos;
    private Boolean existePts;
    private String filtroTipoVencimento;
    private String filtroTurno;
    private Integer filtroMesVencimento;
    private Boolean filtroApenasPacientesSemPTS;
    private Integer filtroAnoVencimento;
    private Pts rowBean;
    private Boolean renderizarBotaoNovo;
    private Integer idParametroEndereco;
    private String statusPts;
    private String campoBusca;
    private String tipoBusca;
    private FuncionarioBean usuarioLiberacao;
    private Boolean liberacaoAlterarDataPts, liberacaoIncluirPtsVencimentoAnteriorAtualPts;
    private Boolean confirmaCadastroPtsComAreaExistente;
    //CONSTANTES
    private static final String ENDERECO_PTS = "cadastropts?faces-redirect=true";
    private static final String ENDERECO_GERENCIAMENTOPTS = "gerenciamentopts.?faces-redirect=true";
    private static final String ENDERECO_RENOVACAO = "ptsrenovacao?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String ENDERECO_ID_GER_PAC_INSTITUICAO = "&amp;idgerpaciente=";

    public PtsController() {
        pts = new Pts();
        ptsAreaAtual = new PtsArea();
        ptsAreaAnterior = new PtsArea();
        listaPts = new ArrayList<>();
        listaGrupos = new ArrayList<>();
        filtroTipoVencimento = FiltroBuscaVencimentoPTS.TODOS.getSigla();
        rowBean = new Pts();
        renderizarBotaoNovo = false;
        usuarioLiberacao = new FuncionarioBean();
        liberacaoAlterarDataPts = false;
        filtroTurno = "A";
        liberacaoIncluirPtsVencimentoAnteriorAtualPts = false;
    }

    public void carregarPts() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            Integer idGerenciamentoPaciente = Integer.parseInt(params.get("idgerpaciente"));

            idParametroEndereco = id;
            existePts = ptsDao.verificarSeExistePts(id);
            if (existePts) {
                this.pts = ptsDao.ptsCarregarPtsPorId(id);
            } else {
                this.pts = ptsDao.ptsCarregarPacientesInstituicao(id);
            }
            listaEspecialidadesEquipe = eDao.listarEspecialidadesPacienteEmTerapia(pts.getPrograma().getIdPrograma(), pts.getGrupo().getIdGrupo(), pts.getPaciente().getId_paciente());

        } else {
            existePts = false;
            pts = (Pts) SessionUtil.resgatarDaSessao("pts");
            Date d = new Date();
            pts.setData(d);
            listaEspecialidadesEquipe = eDao.listarEspecialidadesPacienteEmTerapia(pts.getPrograma().getIdPrograma(), pts.getGrupo().getIdGrupo(), pts.getPaciente().getId_paciente());
        }
    }

    public void carregarBuscaPts(){
        BuscaSessaoDTO buscaSessaoDTO = (BuscaSessaoDTO) SessionUtil.resgatarDaSessao(BUSCA_SESSAO);
        if(!VerificadorUtil.verificarSeObjetoNulo(buscaSessaoDTO)) {
            if (buscaSessaoDTO.getTela().equals(TelasBuscaSessao.PTS.getSigla())) {
                pts.setGrupo(buscaSessaoDTO.getGrupoBean());
                pts.setPrograma(buscaSessaoDTO.getProgramaBean());
            }
        }

    }

    public void carregarPtsRenovacao() throws ProjetoException {
        existePts = false;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            idParametroEndereco = id;
            statusPts = ptsDao.verificarStatusPts(id);

            pts = (Pts) SessionUtil.resgatarDaSessao("pts");
            pts.setUltimaDataVencimento(pts.getDataVencimento());
            pts.setAnaliseDosResultadosDosObjetivosGerias(null);
            pts.setObjetivosFamiliarPaciente(null);
            pts.setObjetivosGeraisMultidisciplinar(null);
            pts.setData(null);

            listaEspecialidadesEquipe = eDao.listarEspecialidadesEquipe(pts.getGerenciarPaciente().getId());

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }
    }

    public void buscarPtsPacientesAtivos() throws ProjetoException {
        SessionUtil.adicionarBuscaPtsNaSessao(pts.getPrograma(), pts.getGrupo(), null, null, TelasBuscaSessao.PTS.getSigla());
        listaPts = ptsDao.buscarPtsPacientesAtivos(pts.getPrograma().getIdPrograma(), pts.getGrupo().getIdGrupo(), filtroTipoVencimento, filtroMesVencimento, filtroAnoVencimento,filtroApenasPacientesSemPTS, campoBusca, tipoBusca, filtroTurno);
    }

    public void limparBusca() {
        listaPts = new ArrayList<>();
        pts = new Pts();
    }

    public String redirectEdit() throws ProjetoException {
        String retorno = "";
        /*
        statusPts = pDao.verificarStatusPts(rowBean.getId());
        if (statusPts.equals(StatusPTS.RENOVADO.getSigla())) {
            SessionUtil.adicionarNaSessao(rowBean, "pts");
            retorno = RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_RENOVACAO, ENDERECO_ID, this.rowBean.getId(),ENDERECO_ID_GER_PAC_INSTITUICAO, this.rowBean.getGerenciarPaciente().getId());
        } else {
            retorno = RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_PTS, ENDERECO_ID, this.rowBean.getId(),ENDERECO_ID_GER_PAC_INSTITUICAO, this.rowBean.getGerenciarPaciente().getId());
        }
        */
        retorno = RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_PTS, ENDERECO_ID, this.rowBean.getId(),ENDERECO_ID_GER_PAC_INSTITUICAO, this.rowBean.getGerenciarPaciente().getId());
        return retorno;
    }

    public String redirectEditAposGravar(Integer novoIdPts, Integer PacienteInstituicao) {
        return RedirecionarUtil.redirectEditSemTeipoComDoisParametros(ENDERECO_PTS, ENDERECO_ID, novoIdPts,ENDERECO_ID_GER_PAC_INSTITUICAO,PacienteInstituicao);
    }

    public String redirectGerPts() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_GERENCIAMENTOPTS);
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

    public void abrirDialogLiberacaoPts() {

        limparDialogLiberacaoPts();

        JSFUtil.abrirDialog("dlgSenhaLiberacaoPts");
    }

    public void limparDialogLiberacaoPts(){
        usuarioLiberacao = new FuncionarioBean();
    }

    public void abrirDialogEdicaoPts(PtsArea ptsEditar) {
        pts.setPtsArea(ptsEditar);

        JSFUtil.abrirDialog("dlgInclusaoAreaPts");
    }

    public void abrirDialogCancelarPts() {
        JSFUtil.abrirDialog("dlgCancelarPts");
    }

    public void abrirDialogDesligarPts() {
        JSFUtil.abrirDialog("dlgDesligarPts");
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
        Boolean existe = false;



        ptsAreaAtual.setArea(eDao.listarEspecialidadePorId(ptsAreaAtual.getArea().getCodEspecialidade()));
        if (!pts.getListaPtsArea().isEmpty()) {
            if (VerificadorUtil.verificarSeObjetoNulo(ptsAreaAnterior)) {
			/*
				 for (int i = 0; i < pts.getListaPtsArea().size(); i++) {

				if (pts.getListaPtsArea().get(i).getArea().getCodEspecialidade() == ptsAreaAtual.getArea().getCodEspecialidade()) {
					existe = true;

				}
			}
			*/
            }
            if (existe == true) {
                JSFUtil.adicionarMensagemAdvertencia("Esse Área do PTS já foi adicionada!", "Advertência");
            } else {
                if (!VerificadorUtil.verificarSeObjetoNulo(ptsAreaAnterior)) {
                    removerPtsDaLista(ptsAreaAnterior);
                }

                pts.getListaPtsArea().add(ptsAreaAtual);
                ptsAreaAtual = new PtsArea();
                ptsAreaAnterior = null;
                JSFUtil.fecharDialog("dlgInclusaoAreaPts");
            }
        } else {
            if (!VerificadorUtil.verificarSeObjetoNulo(ptsAreaAnterior)) {
                removerPtsDaLista(ptsAreaAnterior);
            }
            pts.getListaPtsArea().add(ptsAreaAtual);
            ptsAreaAtual = new PtsArea();
            ptsAreaAnterior = null;
            JSFUtil.fecharDialog("dlgInclusaoAreaPts");
        }





    }

    public void removerPtsDaLista(PtsArea ptsRemover) {
        pts.getListaPtsArea().remove(ptsRemover);
    }

    public String gravarPts() throws ProjetoException {

        String retorno = null;

        Integer novoIdPts = ptsDao.gravarPts(pts, existePts, StatusPTS.ATIVO.getSigla(), this.usuarioLiberacao);

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(novoIdPts)) {
            existePts = true;
            JSFUtil.adicionarMensagemSucesso("PTS cadastrado com sucesso!", "Sucesso");
            JSFUtil.abrirDialog("dlgPtsGravado");
            pts = new Pts();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }

        return retorno;
    }

    public String gravarAlteracaoPts() throws ProjetoException {

        String retorno = null;

        Boolean cadastrou = ptsDao.alterarPts(pts, usuarioLiberacao);

        if (cadastrou) {
            JSFUtil.adicionarMensagemSucesso("PTS alterado com sucesso!", "Sucesso");
            JSFUtil.abrirDialog("dlgPtsGravado");
            pts = new Pts();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }

        return retorno;
    }

    public String gravarRenovacaoPts() throws ProjetoException {

        String retorno = null;

        if ((pts.getData().before(pts.getUltimaDataVencimento())) && (!liberacaoIncluirPtsVencimentoAnteriorAtualPts)){
            JSFUtil.abrirDialog("dlgSenhaLiberacaoPtsVenctoAnterior");
            return null;
        }

        Integer novoIdPts = ptsDao.gravarPts(pts, false, StatusPTS.RENOVADO.getSigla(), usuarioLiberacao);

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(novoIdPts)) {
            existePts = true;
            JSFUtil.adicionarMensagemSucesso("PTS renovado com sucesso!", "Sucesso");
            JSFUtil.abrirDialog("dlgPtsGravado");
            liberacaoIncluirPtsVencimentoAnteriorAtualPts = false;

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a renovacao!", "Erro");
        }

        return retorno;
    }

    public void cancelarPts() throws ProjetoException {

        Boolean cancelou = ptsDao.cancelarPts(rowBean.getId());

        if (cancelou) {
            JSFUtil.adicionarMensagemSucesso("PTS cancelado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgCancelarPts");
            buscarPtsPacientesAtivos();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cancelamento!", "Erro");
        }
    }

    public void desligarPts() throws ProjetoException {

        pts.setId(rowBean.getId());

        Boolean desligou = ptsDao.desligarPts(pts);

        if (desligou) {
            JSFUtil.adicionarMensagemSucesso("PTS desligado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgDesligarPts");
            buscarPtsPacientesAtivos();

        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o desligamento!", "Erro");
        }
    }

    public void validarSenhaLiberacaoPts() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(usuarioLiberacao.getCpf(),
                usuarioLiberacao.getSenha(), ValidacaoSenha.LIBERACAO.getSigla());



        if (func!=null) {
            usuarioLiberacao.setId(func.getId());
            usuarioLiberacao.getUnidade().setId(func.getUnidade().getId());
            JSFUtil.fecharDialog("dlgSenhaLiberacaoPts");
            liberacaoAlterarDataPts = true;
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou Sem Permissão para liberação!", "Erro!");
        }
    }

    public void validarSenhaLiberacaoInclusaoPtsVencimentoAnteriorAoAtual() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(usuarioLiberacao.getCpf(),
                usuarioLiberacao.getSenha(), ValidacaoSenha.LIBERACAO.getSigla());



        if (func!=null) {
            usuarioLiberacao.setId(func.getId());
            usuarioLiberacao.getUnidade().setId(func.getUnidade().getId());
            JSFUtil.fecharDialog("dlgSenhaLiberacaoPtsVenctoAnterior");
            liberacaoIncluirPtsVencimentoAnteriorAtualPts = true;
            gravarRenovacaoPts();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada ou Sem Permissão para liberação!", "Erro!");
        }
    }
    ///

    public void cancelarCadastroPtsComAreaExistente() {
        confirmaCadastroPtsComAreaExistente = false;
        JSFUtil.fecharDialog("dlgExisteArea");
        JSFUtil.fecharDialog("dlgSenhaAreaPts");
        ptsAreaAtual.getFuncionario().setSenha(new String());
        ptsAreaAtual.getFuncionario().setCpf(new String());
    }

    public void confirmarCadastroPtsComAreaExistente() {
        confirmaCadastroPtsComAreaExistente = true;
        JSFUtil.fecharDialog("dlgExisteArea");
    }

    public void verificarSeExistePtsComEspecialidadeSelecionada() throws ProjetoException {
        Boolean existeEspecialidadeSelecionadaNoBanco = ptsDao.verificarSeExistePtsComEspecialidadeSelecionada(ptsAreaAtual.getArea().getCodEspecialidade());
        Boolean existeEspecialidadeSelecionadaNaTela = verificaSeExistePtsComEspecialidadeSelecionadaNaTela();
        if((existeEspecialidadeSelecionadaNaTela || existeEspecialidadeSelecionadaNoBanco) && !confirmaCadastroPtsComAreaExistente)
            JSFUtil.abrirDialog("dlgExisteArea");
        else
            validarSenhaAdicionarAreaPts();
    }

    public Boolean verificaSeExistePtsComEspecialidadeSelecionadaNaTela() {
        List<Integer> listaCodigoEspecialidade = new ArrayList<Integer>();
        for (PtsArea ptsArea : pts.getListaPtsArea()) {
            listaCodigoEspecialidade.add(ptsArea.getArea().getCodEspecialidade());
        }
        if(listaCodigoEspecialidade.contains(ptsAreaAtual.getArea().getCodEspecialidade()))
            return true;
        return false;
    }

    public void validarSenhaAdicionarAreaPts() throws ProjetoException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

        FuncionarioBean func = funcionarioDAO.validarCpfIhSenha(ptsAreaAtual.getFuncionario().getCpf(),
                ptsAreaAtual.getFuncionario().getSenha(), ValidacaoSenha.ADICIONAR_AREA_PTS.getSigla());

        if (func!=null) {
            ptsAreaAtual.setFuncionario(func);
            JSFUtil.fecharDialog("dlgSenhaAreaPts");
            addPtsNaLista();
        } else {
            JSFUtil.adicionarMensagemErro("Funcionário com senha errada!", "Erro!");
        }
    }

    public void abrirDialogDigitacaoSenha() {
        confirmaCadastroPtsComAreaExistente = false;
        JSFUtil.abrirDialog("dlgSenhaAreaPts");
    }

    public Pts carregarPtsPaciente(Integer codPrograma, Integer codGrupo, Integer codPaciente) throws ProjetoException {
        return ptsDao.carregarPtsDoPaciente(codPrograma, codGrupo, codPaciente);
    }

    public void verificarSeExistePtsParaProgramaGrupoPaciente() throws ProjetoException {

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(rowBean.getGerenciarPaciente().getId())) {
            if (!ptsDao.verificarSeExistePtsParaProgramaGrupoPaciente(rowBean)) {
                renderizarBotaoNovo = true;
            }
            else {
            	renderizarBotaoNovo = false;
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

    public String getCampoBusca() {
        return campoBusca;
    }

    public String getTipoBusca() {
        return tipoBusca;
    }

    public void setCampoBusca(String campoBusca) {
        this.campoBusca = campoBusca;
    }

    public void setTipoBusca(String tipoBusca) {
        this.tipoBusca = tipoBusca;
    }

    public PtsArea getPtsAreaAtual() {
        return ptsAreaAtual;
    }

    public void setPtsAreaAtual(PtsArea ptsAreaAtual) {
        this.ptsAreaAtual = ptsAreaAtual;
    }

    public PtsArea getPtsAreaAnterior() {
        return ptsAreaAnterior;
    }

    public void setPtsAreaAnterior(PtsArea ptsAreaAnterior) {
        this.ptsAreaAnterior = ptsAreaAnterior;
    }

    public Boolean getFiltroApenasPacientesSemPTS() {
        return filtroApenasPacientesSemPTS;
    }

    public void setFiltroApenasPacientesSemPTS(Boolean filtroApenasPacientesSemPTS) {
        this.filtroApenasPacientesSemPTS = filtroApenasPacientesSemPTS;
    }

    public Boolean getExistePts() {
        return existePts;
    }

    public void setExistePts(Boolean existePts) {
        this.existePts = existePts;
    }

    public FuncionarioBean getUsuarioLiberacao() {
        return usuarioLiberacao;
    }

    public void setUsuarioLiberacao(FuncionarioBean usuarioLiberacao) {
        this.usuarioLiberacao = usuarioLiberacao;
    }

    public Boolean getLiberacaoAlterarDataPts() {
        return liberacaoAlterarDataPts;
    }

    public void setLiberacaoAlterarDataPts(Boolean liberacaoAlterarDataPts) {
        this.liberacaoAlterarDataPts = liberacaoAlterarDataPts;
    }

    public String getFiltroTurno() {
        return filtroTurno;
    }

    public void setFiltroTurno(String filtroTurno) {
        this.filtroTurno = filtroTurno;
    }
}
