package br.gov.al.maceio.sishosp.administrativo.control;

import br.gov.al.maceio.sishosp.administrativo.dao.AfastamentoTemporarioDAO;
import br.gov.al.maceio.sishosp.administrativo.dao.SubstituicaoDAO;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoFuncionario;
import br.gov.al.maceio.sishosp.administrativo.model.dto.BuscaAgendamentosParaFuncionarioAfastadoDTO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.GrupoDAO;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import org.primefaces.event.SelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "SubstituicaoController")
@ViewScoped
public class SubstituicaoController implements Serializable {

    private static final long serialVersionUID = 1L;
    private SubstituicaoFuncionario substituicaoFuncionario;
    private SubstituicaoDAO sDao = new SubstituicaoDAO();
    private Integer idAfastamento;
    private BuscaAgendamentosParaFuncionarioAfastadoDTO buscaAgendamentosParaFuncionarioAfastadoDTO;
    private List<GrupoBean> listaGruposPorProgramas;
    private GrupoDAO gDao = new GrupoDAO();
    private List<AtendimentoBean> listaAtendimentos;
    private List<AtendimentoBean> listaAtendimentosSelecionada;

    //CONSTANTES
    private static final String ENDERECO_SUBSTITUICAO = "substituicaofuncionarioafastado?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";

    public SubstituicaoController() {
        this.substituicaoFuncionario = new SubstituicaoFuncionario();
        buscaAgendamentosParaFuncionarioAfastadoDTO = new BuscaAgendamentosParaFuncionarioAfastadoDTO();
        listaGruposPorProgramas = new ArrayList<>();
        listaAtendimentos = new ArrayList<>();
        listaAtendimentosSelecionada = new ArrayList<>();
    }

    public String redirectSubstituicao(Integer codAfastamento) {
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_SUBSTITUICAO, ENDERECO_ID, codAfastamento);
    }

    public void limparDados() {
        substituicaoFuncionario = new SubstituicaoFuncionario();
        listaAtendimentos = new ArrayList<>();
        listaAtendimentosSelecionada = new ArrayList<>();
    }

    public void gravarAfastamentoTemporario() {

        if (validarSeAgendamentosForamSelecionados()) {

            boolean cadastrou = sDao.substituirFuncionario(listaAtendimentosSelecionada, substituicaoFuncionario);

            if (cadastrou == true) {
                limparDados();
                JSFUtil.adicionarMensagemSucesso("Substituição cadastrada com sucesso!", "Sucesso");
                limparDados();
            } else {
                JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
            }
        }
    }

    private Boolean validarSeAgendamentosForamSelecionados() {
        if (listaAtendimentosSelecionada.isEmpty()) {
            JSFUtil.adicionarMensagemErro("Realize uma busca e selecione agendamentos para alterar o profissional!", "Erro!");
            return false;
        }

        return true;
    }

    public void carregarFuncionarioAfastamento() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            idAfastamento = Integer.parseInt(params.get("id"));
            AfastamentoTemporarioDAO aDao = new AfastamentoTemporarioDAO();
            substituicaoFuncionario.getAfastamentoTemporario().setFuncionario(aDao.carregarFuncionarioAfastado(idAfastamento));
            substituicaoFuncionario.getAfastamentoTemporario().setId(idAfastamento);

            if (VerificadorUtil.verificarSeObjetoNuloOuZero(substituicaoFuncionario.getAfastamentoTemporario().getFuncionario().getId())) {
                JSFUtil.adicionarMensagemErro("É preciso passar um valor de afastamento válido", "Erro");
            }
        }
    }

    public void buscarAgendamentoDoFuncionarioAfastado() {
        buscaAgendamentosParaFuncionarioAfastadoDTO.setFuncionario(substituicaoFuncionario.getAfastamentoTemporario().getFuncionario());
        listaAtendimentos = sDao.listarHorariosParaSeremSubstituidos(buscaAgendamentosParaFuncionarioAfastadoDTO);
    }

    public void limparFiltroBuscaAtendimentos() {
        buscaAgendamentosParaFuncionarioAfastadoDTO = new BuscaAgendamentosParaFuncionarioAfastadoDTO();
    }

    public void selectPrograma(SelectEvent event) throws ProjetoException {
        this.buscaAgendamentosParaFuncionarioAfastadoDTO.setPrograma((ProgramaBean) event.getObject());
        atualizaListaGrupos(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma());
    }

    public void atualizaListaGrupos(ProgramaBean p) throws ProjetoException {
        buscaAgendamentosParaFuncionarioAfastadoDTO.setPrograma(p);
        listaGruposPorProgramas = gDao.listarGruposPorPrograma(p.getIdPrograma());
    }

    public List<GrupoBean> listaGrupoAutoComplete(String query) throws ProjetoException {

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma())) {
            return gDao.listarGruposNoAutoComplete(query, buscaAgendamentosParaFuncionarioAfastadoDTO.getPrograma().getIdPrograma());
        } else {
            return null;
        }

    }

    public SubstituicaoFuncionario getSubstituicaoFuncionario() {
        return substituicaoFuncionario;
    }

    public void setSubstituicaoFuncionario(SubstituicaoFuncionario substituicaoFuncionario) {
        this.substituicaoFuncionario = substituicaoFuncionario;
    }

    public BuscaAgendamentosParaFuncionarioAfastadoDTO getBuscaAgendamentosParaFuncionarioAfastadoDTO() {
        return buscaAgendamentosParaFuncionarioAfastadoDTO;
    }

    public void setBuscaAgendamentosParaFuncionarioAfastadoDTO(BuscaAgendamentosParaFuncionarioAfastadoDTO buscaAgendamentosParaFuncionarioAfastadoDTO) {
        this.buscaAgendamentosParaFuncionarioAfastadoDTO = buscaAgendamentosParaFuncionarioAfastadoDTO;
    }

    public List<GrupoBean> getListaGruposPorProgramas() {
        return listaGruposPorProgramas;
    }

    public void setListaGruposPorProgramas(List<GrupoBean> listaGruposPorProgramas) {
        this.listaGruposPorProgramas = listaGruposPorProgramas;
    }

    public List<AtendimentoBean> getListaAtendimentos() {
        return listaAtendimentos;
    }

    public void setListaAtendimentos(List<AtendimentoBean> listaAtendimentos) {
        this.listaAtendimentos = listaAtendimentos;
    }

    public List<AtendimentoBean> getListaAtendimentosSelecionada() {
        return listaAtendimentosSelecionada;
    }

    public void setListaAtendimentosSelecionada(List<AtendimentoBean> listaAtendimentosSelecionada) {
        this.listaAtendimentosSelecionada = listaAtendimentosSelecionada;
    }

}
