package br.gov.al.maceio.sishosp.hosp.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class ProcedimentoController {
	
	private ProcedimentoBean proc;
	private List<ProcedimentoBean> listaProcedimentos;
	private Integer tipoBuscar;
	private String descricaoBusca;
	private String tipo;
    private String cabecalho;
	ProcedimentoDAO pDao = new ProcedimentoDAO();

	public ProcedimentoController() {
		this.proc = new ProcedimentoBean();
		this.listaProcedimentos = null;
		this.descricaoBusca = new String();
		this.tipo = new String();
	}

	public void limparDados() {
		proc = new ProcedimentoBean();
		listaProcedimentos = new ArrayList<ProcedimentoBean>();
		this.descricaoBusca = new String();
		listaProcedimentos = pDao.listarProcedimento();
	}

	public ProcedimentoBean getProc() {
		return proc;
	}

	public void setProc(ProcedimentoBean proc) {
		this.proc = proc;
	}
	
	public List<ProcedimentoBean> getListaProcedimentos() {
		if(listaProcedimentos == null){
			this.listaProcedimentos = pDao.listarProcedimento();
		}
		return listaProcedimentos;
	}

	public void setListaProcedimentos(List<ProcedimentoBean> listaProcedimentos) {
		this.listaProcedimentos = listaProcedimentos;
	}

	public Integer getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar(Integer tipoBuscar) {
		this.tipoBuscar = tipoBuscar;
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
	
	public void buscarProcedimento() {
		this.listaProcedimentos = pDao.listarProcedimentoBusca(descricaoBusca, tipoBuscar);
	}

	public void gravarProcedimento() throws ProjetoException, SQLException {

		boolean cadastrou = pDao.gravarProcedimento(proc);

		if (cadastrou == true) {
			limparDados();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Procedimento cadastrado com sucesso!", "Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Ocorreu um erro durante o cadastro!", "Erro");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void alterarProcedimento() throws ProjetoException {
        boolean alterou = pDao.alterarProcedimento(proc);
        if(alterou == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Procedimento alterado com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante o cadastro!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
		listaProcedimentos = pDao.listarProcedimento();
		
	}
	
	public void excluirProcedimento() throws ProjetoException {
        boolean ok = pDao.excluirProcedimento(proc);
        if(ok == true) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Procedimento excluido com sucesso!", "Sucesso");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Ocorreu um erro durante a exclusao!", "Erro");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().execute("PF('dialogAtencao').hide();");
        }
		listaProcedimentos = pDao.listarProcedimento();
	}
	
	public String getCabecalho() {
		if(this.tipo.equals("I")){
			cabecalho = "CADASTRO DE PROCEDIMENTO";
		}else if(this.tipo.equals("A")){
			cabecalho = "ALTERAR PROCEDIMENTO";
		}
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
}