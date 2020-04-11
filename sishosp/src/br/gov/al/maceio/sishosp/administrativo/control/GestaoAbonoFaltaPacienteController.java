package br.gov.al.maceio.sishosp.administrativo.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.dao.GestaoAbonoFaltaPacienteDAO;
import br.gov.al.maceio.sishosp.administrativo.model.GestaoAbonoFaltaPaciente;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoAbonoFaltaPacienteDTO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.EquipeDAO;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;

@ViewScoped
@ManagedBean
public class GestaoAbonoFaltaPacienteController {
	
	private List<GestaoAbonoFaltaPaciente> listaAbonosFaltaPaciente;
	private GestaoAbonoFaltaPacienteDAO gestaoAbonoFaltaPacienteDAO;
	private GestaoAbonoFaltaPaciente abonoFaltaPaciente;
	private List<EquipeBean> listaEquipePorGrupo;
	private EquipeDAO equipeDao;
	private String tipoData;
	private List<AtendimentoBean> listaAtendimentosParaAbono;
	private List<AtendimentoBean> listaAtendimentosSelecionadosParaAbono;
	
	private static final String ENDERECO_CADASTRO = "abonarfaltapaciente?faces-redirect=true";
	
	public GestaoAbonoFaltaPacienteController() {
		this.listaAbonosFaltaPaciente = new ArrayList<>();
		this.gestaoAbonoFaltaPacienteDAO = new GestaoAbonoFaltaPacienteDAO();
		this.abonoFaltaPaciente = new GestaoAbonoFaltaPaciente();
		this.listaEquipePorGrupo = new ArrayList<>();
		this.equipeDao = new EquipeDAO();
		this.listaAtendimentosParaAbono = new ArrayList<>();
		listaAtendimentosSelecionadosParaAbono = new ArrayList<>();
		tipoData = "U";
	}
	
    public String redirectNovo() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_CADASTRO);
    }
    
    public void carregaListaEquipePorGrupo() throws ProjetoException {
        if (abonoFaltaPaciente.getGrupo().getIdGrupo() != null) {
            listaEquipePorGrupo = equipeDao.listarEquipePorGrupo(abonoFaltaPaciente.getGrupo().getIdGrupo());
        }
    }
	
	public void listarAbonosFaltaPaciente() {
		this.listaAbonosFaltaPaciente = gestaoAbonoFaltaPacienteDAO.listarAfastamentoProfissionais();
	}
	
	public void listarAtendimentosParaAbono() {
		this.listaAtendimentosParaAbono = gestaoAbonoFaltaPacienteDAO.listarAtendimentosParaAbono(abonoFaltaPaciente);
	}
	
	public void gravaAbonos() throws SQLException {
		if(haAtendimentoSelecionadoParaAbono() && !quantidadeDeCaracteresDaJustificativaEhMaiorQue500()) {
			FuncionarioBean userSession = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().get("obj_usuario");
			GravarInsercaoAbonoFaltaPacienteDTO gravarInsercaoAbonoFaltaPacienteDTO = 
					new GravarInsercaoAbonoFaltaPacienteDTO(listaAtendimentosSelecionadosParaAbono, userSession.getId(), this.abonoFaltaPaciente.getJustificativa());
			boolean inseriuAbonos = gestaoAbonoFaltaPacienteDAO.inserirAbonoFaltaPaciente(gravarInsercaoAbonoFaltaPacienteDTO);
					
			if(inseriuAbonos) {
				JSFUtil.adicionarMensagemSucesso("Abonos Gravados com sucesso!", "");
				limparDados();
			}
		}
	}

	private boolean haAtendimentoSelecionadoParaAbono() {
		if(listaAtendimentosSelecionadosParaAbono.isEmpty()
				|| VerificadorUtil.verificarSeObjetoNulo(listaAtendimentosSelecionadosParaAbono)) {
			JSFUtil.adicionarMensagemErro("Selecione pelo menos um atendimento", "Erro");
			return false;
		}
		return true;
	}
	
	private boolean quantidadeDeCaracteresDaJustificativaEhMaiorQue500() {
		if(this.abonoFaltaPaciente.getJustificativa().length() > 500) {
			JSFUtil.adicionarMensagemErro("O campo justificativa não pode exceder 500 caracteres", "Erro");
			return true;
		}
		return false;
	}
	
	private void limparDados() {
		this.listaAbonosFaltaPaciente = new ArrayList<GestaoAbonoFaltaPaciente>();
		this.abonoFaltaPaciente = new GestaoAbonoFaltaPaciente();
		this.listaEquipePorGrupo = new ArrayList<EquipeBean>();
		this.listaAtendimentosParaAbono = new ArrayList<AtendimentoBean>();
		this.listaAtendimentosSelecionadosParaAbono = new ArrayList<AtendimentoBean>();
	}

	public List<GestaoAbonoFaltaPaciente> getListaAbonosFaltaPaciente() {
		return listaAbonosFaltaPaciente;
	}

	public void setListaAbonosFaltaPaciente(List<GestaoAbonoFaltaPaciente> listaAbonosFaltaPaciente) {
		this.listaAbonosFaltaPaciente = listaAbonosFaltaPaciente;
	}

	public GestaoAbonoFaltaPaciente getAbonoFaltaPaciente() {
		return abonoFaltaPaciente;
	}

	public void setAbonoFaltaPaciente(GestaoAbonoFaltaPaciente abonoFaltaPaciente) {
		this.abonoFaltaPaciente = abonoFaltaPaciente;
	}

	public List<EquipeBean> getListaEquipePorGrupo() {
		return listaEquipePorGrupo;
	}

	public void setListaEquipePorGrupo(List<EquipeBean> listaEquipePorGrupo) {
		this.listaEquipePorGrupo = listaEquipePorGrupo;
	}

	public String getTipoData() {
		return tipoData;
	}

	public void setTipoData(String tipoData) {
		this.tipoData = tipoData;
	}

	public List<AtendimentoBean> getListaAtendimentosParaAbono() {
		return listaAtendimentosParaAbono;
	}

	public void setListaAtendimentosParaAbono(List<AtendimentoBean> listaAtendimentosParaAbono) {
		this.listaAtendimentosParaAbono = listaAtendimentosParaAbono;
	}

	public List<AtendimentoBean> getListaAtendimentosSelecionadosParaAbono() {
		return listaAtendimentosSelecionadosParaAbono;
	}

	public void setListaAtendimentosSelecionadosParaAbono(List<AtendimentoBean> listaAtendimentosSelecionadosParaAbono) {
		this.listaAtendimentosSelecionadosParaAbono = listaAtendimentosSelecionadosParaAbono;
	}

}
