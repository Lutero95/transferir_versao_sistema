package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.GerenciarPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PesquisaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.StatusRespostaPaciente;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.PerguntaBean;
import br.gov.al.maceio.sishosp.hosp.model.PesquisaBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacientePesquisaDTO;

@ManagedBean
@ViewScoped
public class PesquisaController {

	private List<PacienteBean> listaPacientes;
	private GerenciarPacienteDAO gerenciarPacienteDAO;
	private List<PacienteBean> listaPacientesSelecionados;
	private PesquisaBean pesquisa;
	private PerguntaBean pergunta;
	private PesquisaDAO pesquisaDAO;
	private List<PesquisaBean> listaPesquisas;
	private List<PacientePesquisaDTO> listaPacientesDaPesquisa;
	private String nomePacienteSelecionado;
    private Integer tipo;
    private String statusRespostaFiltro;
    private String campoBusca;
    private String tipoBusca;
	private static final String ENDERECO_PACIENTES = "gerenciarpacientespesquisas?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
	
	public PesquisaController() {
		this.listaPacientes = new ArrayList<>();
		this.listaPacientesSelecionados = new ArrayList<>();
		this.gerenciarPacienteDAO = new GerenciarPacienteDAO();
		this.pesquisa = new PesquisaBean();
		this.pergunta = new PerguntaBean();
		this.pesquisaDAO = new PesquisaDAO();
		this.listaPesquisas = new ArrayList<>();
		this.listaPacientesDaPesquisa = new ArrayList<>();
		this.campoBusca = new String();
		this.tipoBusca = new String();
	}
	
	public void listarPacienteAtivos() throws ProjetoException {
		this.listaPacientes = gerenciarPacienteDAO.listaPacientesAtivos();
	}
	
    public void adicionarPacienteSelecionado(PacienteBean paciente) {
        if(!pacienteFoiAdicionado(paciente))
            this.listaPacientesSelecionados.add(paciente);
    }
    
    public void removerPacienteSelecionado(PacienteBean paciente) {
        this.listaPacientesSelecionados.remove(paciente);
    }
    
    public void adicionarTodosPacientes() {
    	this.listaPacientesSelecionados.clear();
    	this.listaPacientesSelecionados.addAll(this.listaPacientes);
    }
    
    public void removerTodosPacientes() {
    	this.listaPacientesSelecionados.clear();
    }

    private boolean pacienteFoiAdicionado(PacienteBean pacienteSelecionado) {
        for (PacienteBean paciente : listaPacientesSelecionados) {
            if(paciente.getId_paciente().equals(pacienteSelecionado.getId_paciente())) {
                JSFUtil.adicionarMensagemAdvertencia("Este paciente já foi adicionado", "");
                return true;
            }
        }
        return false;
    }
    
    public void abrirDialogAdicionarPergunta() {
    	this.pergunta = new PerguntaBean();
    	JSFUtil.abrirDialog("dlgPergunta");
    }
    
    public void adicionarPergunta(PerguntaBean pergunta) {
    	this.pesquisa.getPerguntas().add(pergunta);
    	JSFUtil.fecharDialog("dlgPergunta");
    }
    
    public void removerPergunta(PerguntaBean pergunta) {
    	this.pesquisa.getPerguntas().remove(pergunta);
    }
    
    public void gravarPesquisa() throws ProjetoException {
    	if(pacientesValidos() && perguntasValidas() && validarDatas()) {
    		if(pesquisaDAO.gravarPesquisa(pesquisa, listaPacientesSelecionados)) {
    			JSFUtil.adicionarMensagemSucesso("Pesquisa Cadastrada com Sucesso", "");
    			this.pesquisa = new PesquisaBean();
    			this.listaPacientesSelecionados.clear();
    		}
    	}
    }
    
    private boolean pacientesValidos() {
    	if(this.listaPacientesSelecionados.isEmpty()) {
    		JSFUtil.adicionarMensagemErro("Adicione pelo menos um Paciente", "");
    		return false;
    	}
    	return true;
    }
    
    private boolean perguntasValidas() {
    	if(this.pesquisa.getPerguntas().isEmpty()) {
    		JSFUtil.adicionarMensagemErro("Adicione pelo menos uma Pergunta", "");
    		return false;
    	}
    	return true;
    }
    
    private boolean validarDatas() {
    	if(this.pesquisa.getDataFinal().before(this.pesquisa.getDataInicial())) {
    		JSFUtil.adicionarMensagemErro("Data Final deve ser maior que a Data Inicial", "");
    		return false;
    	}
    	return true;
    }    
    
    public void listarPesquisas() throws ProjetoException {
    	this.listaPesquisas = pesquisaDAO.listarPesquisas();
    }
   
    
    public String redirecionaPacientesPesquisa(Integer idPesquisa) {
        return RedirecionarUtil.redirectEdit(ENDERECO_PACIENTES, ENDERECO_ID, idPesquisa, ENDERECO_TIPO, tipo);
    }
    
    public void getIdPaciente() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (!VerificadorUtil.verificarSeObjetoNulo(params.get("id"))) {
            Integer id = Integer.parseInt(params.get("id"));
            listarDadosParaResponderPesquisa(id);
        }
    }
    
    private void listarDadosParaResponderPesquisa(Integer id) throws ProjetoException {
    	this.listaPacientesDaPesquisa = pesquisaDAO.listarPacientesDaPesquisa
    			(id, StatusRespostaPaciente.TODOS.getSigla(), new String(), new String());
        this.pesquisa = this.listaPacientesDaPesquisa.get(0).getPesquisa();
        this.pesquisa.setPerguntas(pesquisaDAO.listarPerguntas(id));
    }
    
    public void atribuiPacienteEmRespostas(PacientePesquisaDTO pacientePesquisaDTO) {
    	
    	PacienteBean paciente = pacientePesquisaDTO.getPaciente();
    	
    	for(int i = 0; i < this.pesquisa.getPerguntas().size(); i++) {
    		this.pesquisa.getPerguntas().get(i).getResposta().setResposta(new String());
    		this.pesquisa.getPerguntas().get(i).getResposta().getPaciente().setId_paciente(paciente.getId_paciente());
    	}
    	this.nomePacienteSelecionado = paciente.getNome();
    	JSFUtil.abrirDialog("dlgPergunta");
    }
    
    public void abrirDialogConfirmacao() {
    	if(validarRespostas())
    		JSFUtil.abrirDialog("dlgConfirmacao");
    }
    
    private boolean validarRespostas() {
    	for (PerguntaBean pergunta : this.pesquisa.getPerguntas()) {
			if(VerificadorUtil.verificarSeObjetoNuloOuVazio(pergunta.getResposta().getResposta())) {
				JSFUtil.adicionarMensagemErro("Responda todas as perguntas", "");
				return false;
			}
		}
    	return true;
    }
    
    public void gravarRespostasPaciente() throws ProjetoException, SQLException {
    	if(pesquisaDAO.inserirRespostasPaciente(pesquisa)) {
    		listarDadosParaResponderPesquisa(pesquisa.getId());
    		JSFUtil.fecharDialog("dlgConfirmacao");
    		JSFUtil.fecharDialog("dlgPergunta");
    		JSFUtil.adicionarMensagemSucesso("Respostas salvas com sucesso", "");
    	}
    }
    
    public void listarPacientesDaPesquisaFiltro(String status) throws ProjetoException {
    	this.listaPacientesDaPesquisa = pesquisaDAO.listarPacientesDaPesquisa(pesquisa.getId(), status, this.tipoBusca, this.campoBusca);
    }
    
    public void limparBuscaPacientesDaPesquisaFiltro() throws ProjetoException {
    	this.tipoBusca = new String();
    	this.campoBusca = new String();
    	this.statusRespostaFiltro = new String();
    	this.listaPacientesDaPesquisa = pesquisaDAO.listarPacientesDaPesquisa(pesquisa.getId(), statusRespostaFiltro, this.tipoBusca, this.campoBusca);
    }

	public List<PacienteBean> getListaPacientes() {
		return listaPacientes;
	}

	public void setListaPacientes(List<PacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}

	public List<PacienteBean> getListaPacientesSelecionados() {
		return listaPacientesSelecionados;
	}

	public void setListaPacientesSelecionados(List<PacienteBean> listaPacientesSelecionados) {
		this.listaPacientesSelecionados = listaPacientesSelecionados;
	}

	public PesquisaBean getPesquisa() {
		return pesquisa;
	}

	public void setPesquisa(PesquisaBean pesquisa) {
		this.pesquisa = pesquisa;
	}

	public PerguntaBean getPergunta() {
		return pergunta;
	}

	public void setPergunta(PerguntaBean pergunta) {
		this.pergunta = pergunta;
	}

	public List<PesquisaBean> getListaPesquisas() {
		return listaPesquisas;
	}

	public void setListaPesquisas(List<PesquisaBean> listaPesquisas) {
		this.listaPesquisas = listaPesquisas;
	}

	public List<PacientePesquisaDTO> getListaPacientesDaPesquisa() {
		return listaPacientesDaPesquisa;
	}

	public void setListaPacientesDaPesquisa(List<PacientePesquisaDTO> listaPacientesDaPesquisa) {
		this.listaPacientesDaPesquisa = listaPacientesDaPesquisa;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getNomePacienteSelecionado() {
		return nomePacienteSelecionado;
	}

	public void setNomePacienteSelecionado(String nomePacienteSelecionado) {
		this.nomePacienteSelecionado = nomePacienteSelecionado;
	}

	public String getStatusRespostaFiltro() {
		return statusRespostaFiltro;
	}

	public void setStatusRespostaFiltro(String statusRespostaFiltro) {
		this.statusRespostaFiltro = statusRespostaFiltro;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public String getTipoBusca() {
		return tipoBusca;
	}

	public void setTipoBusca(String tipoBusca) {
		this.tipoBusca = tipoBusca;
	}
	
}
