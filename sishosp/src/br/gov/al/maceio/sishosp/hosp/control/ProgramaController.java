package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProgramaDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

public class ProgramaController {
	
	private ProgramaBean prog;
	private List<ProgramaBean> listaProgramas;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipo;
	private String cabecalho;
	private Integer abaAtiva = 0;
	
	ProgramaDAO pDao = new ProgramaDAO();
	
	public ProgramaController() {
	
		this.prog = new ProgramaBean();
		this.listaProgramas = null;
		this.descricaoBusca = new String();
		this.tipo = new String();
	}
	
	public void limparDados(){
		prog = new ProgramaBean();
		descricaoBusca = new String();
		tipoBuscar = 1;
        listaProgramas = pDao.listarProgramas();
	}

	public void gravarPrograma() throws ProjetoException, SQLException {
		ProgramaDAO pDao = new ProgramaDAO();
		boolean cadastrou = pDao.gravarPrograma(prog);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Programa cadastrado com sucesso!",
					"Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante o cadastro!",
					"Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
        listaProgramas = pDao.listarProgramas();
	}
	
	public void excluirPrograma() throws ProjetoException {
        boolean ok = pDao.excluirPrograma(prog);
        if(ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Programa excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
        listaProgramas = pDao.listarProgramas();
	}
	
	public String alterarPrograma() throws ProjetoException {
         boolean alterou = pDao.alterarPrograma(prog);
         if(alterou == true) {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                 "Programa alterado com sucesso!", "Sucesso");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             listaProgramas = pDao.listarProgramas();
             return "/pages/sishosp/gerenciarPrograma.faces?faces-redirect=true";
         } else {
             FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                 "Ocorreu um erro durante o cadastro!", "Erro");
             FacesContext.getCurrentInstance().addMessage(null, msg);
             return "";
         }
         
		
	}

	public ProgramaBean getProg() {
		return prog;
	}

	public void setProg(ProgramaBean prog) {
		this.prog = prog;
	}

	public List<ProgramaBean> getListaProgramas() {
		if (listaProgramas == null) {
			listaProgramas = pDao.listarProgramas();
		}
		return listaProgramas;
	}

	public void setListaProgramas(List<ProgramaBean> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
	}

	public void buscarProgramas() {
		this.listaProgramas = pDao.listarProgramasBusca(descricaoBusca,
				tipoBuscar);
	}


	public String getDescricaoBusca() {
		return descricaoBusca;
	}

	public void setDescricaoBusca(String descricaoBusca) {
		this.descricaoBusca = descricaoBusca;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getAbaAtiva() {
		return abaAtiva;
	}

	public void setAbaAtiva(Integer abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE PROGRAMA";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR PROGRAMA";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
	public List<ProgramaBean> listaProgramaAutoComplete(String query)
			throws ProjetoException {
		ProgramaDAO pDao = new ProgramaDAO();
		List<ProgramaBean> result = pDao.listarProgramasBusca(query, 1);
		return result;
	}
	
}
