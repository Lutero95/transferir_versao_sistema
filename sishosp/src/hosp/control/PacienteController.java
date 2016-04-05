package br.gov.al.maceio.sishosp.hosp.control;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;






import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ConvenioBean;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;



public class PacienteController {
	private Integer abaAtiva = 0;
	
	//CLASSES HERDADAS
	private PacienteBean paciente;
	private EnderecoBean endereco;
    private EscolaBean escola;
	private EscolaridadeBean escolaridade;
	private EspecialidadeBean especialidade;
	private ConvenioBean convenio;
	private EncaminhamentoBean encaminhamento;
	private ProfissaoBean profissao;
	private EncaminhadoBean encaminhado;
	
	//LISTAS 
	private List<PacienteBean> listaPacientes;
	private List<EscolaBean> listaEscolas;
	private List<EscolaridadeBean> listaEscolararidade;
	private List<ProfissaoBean> listaProfissao;
	


public PacienteController(){
		paciente = new PacienteBean();
		endereco = new EnderecoBean();
		escola = new EscolaBean();
		escolaridade = new EscolaridadeBean();
		especialidade = new EspecialidadeBean();
        encaminhamento = new EncaminhamentoBean();
        encaminhado = new EncaminhadoBean();
        profissao = new ProfissaoBean();
        
        //LISTAS
        listaPacientes = new ArrayList<>();
        listaPacientes = null;
        listaEscolas = new ArrayList<>();
        listaEscolas = null;
        listaEscolararidade = new ArrayList<>();
        listaEscolararidade = null;
        listaProfissao = new ArrayList<>();
        listaProfissao = null;
		
	}

	public void gravarPaciente() throws ProjetoException {
        PacienteDAO udao = new PacienteDAO();
System.out.println("passou aqui");
                boolean cadastrou = udao.cadastrar(paciente);

                if(cadastrou) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Paciente cadastrado com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    //listaPaciente = null;

                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante o cadastro!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

 
                }
            
    }
	
	public void excluirPaciente() throws ProjetoException {
                PacienteDAO udao = new PacienteDAO();
                System.out.println("excluio");
                boolean excluio = udao.excluir(paciente);

                if(excluio) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Paciente excluido com sucesso!", "Sucesso");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ocorreu um erro durante a exclusao!", "Erro");
                    FacesContext.getCurrentInstance().addMessage(null, msg);

                    RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
                }
            
    }
	
	
	public void onRowSelect(SelectEvent event) {
		Long codpaciente;
		codpaciente = ((PacienteBean) event.getObject()).getId_paciente();

	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	public PacienteBean getPaciente() {
		return paciente;
	}
	public void setPaciente(PacienteBean paciente) {
		this.paciente = paciente;
	}
	public EnderecoBean getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoBean endereco) {
		this.endereco = endereco;
	}

	public EscolaBean getEscola() {
		return escola;
	}

	public void setEscola(EscolaBean escola) {
		this.escola = escola;
	}

	public EscolaridadeBean getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(EscolaridadeBean escolaridade) {
		this.escolaridade = escolaridade;
	}

	public EspecialidadeBean getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(EspecialidadeBean especialidade) {
		this.especialidade = especialidade;
	}

	public ConvenioBean getConvenio() {
		return convenio;
	}

	public void setConvenio(ConvenioBean convenio) {
		this.convenio = convenio;
	}

	public EncaminhamentoBean getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(EncaminhamentoBean encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public ProfissaoBean getProfissao() {
		return profissao;
	}

	public void setProfissao(ProfissaoBean profissao) {
		this.profissao = profissao;
	}

	public List<EscolaBean> getListaEscolas(){
		   if(listaEscolas == null) {
			   System.out.println("passou aqui");
	            EscolaDAO fdao = new EscolaDAO();
	            listaEscolas = fdao.listaEscolas();
	            System.out.println("passou aqui2");
	        }
		return listaEscolas;
	}

	public void setListaEscolas(List<EscolaBean> listaEscolas) {
		this.listaEscolas = listaEscolas;
	}

	public List<EscolaridadeBean> getListaEscolararidade() {
		  if(listaEscolararidade == null) {
			   System.out.println("passou aqui");
	            EscolaridadeDAO fdao = new EscolaridadeDAO();
	            listaEscolararidade = fdao.listaEscolaridade();
	            System.out.println("passou aqui2");
	        }
		return listaEscolararidade;
	}

	public void setListaEscolararidade(List<EscolaridadeBean> listaEscolararidade) {
		this.listaEscolararidade = listaEscolararidade;
	}

	public List<ProfissaoBean> getListaProfissao() {
		if(listaProfissao == null) {
			   System.out.println("passou aqui");
	            ProfissaoDAO fdao = new ProfissaoDAO();
	            listaProfissao = fdao.listaProfissoes();
	            System.out.println("passou aqui2");
	        }
		return listaProfissao;
	}

	public void setListaProfissao(List<ProfissaoBean> listaProfissao) {
		this.listaProfissao = listaProfissao;
	}

	public EncaminhadoBean getEncaminhado() {
		return encaminhado;
	}

	public void setEncaminhado(EncaminhadoBean encaminhado) {
		this.encaminhado = encaminhado;
	}

	public List<PacienteBean> getListaPacientes() {
		if(listaPacientes == null) {
			   System.out.println("passou aqui");
	            PacienteDAO fdao = new PacienteDAO();
	            listaPacientes = fdao.listaPacientes();
	            System.out.println("passou aqui2");
	        }
		return listaPacientes;
	}

	public void setListaPacientes(List<PacienteBean> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}
	
	
	
	
}
