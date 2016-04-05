package br.gov.al.maceio.sishosp.acl.control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.UIData;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.WebServiceCep;
import br.gov.al.maceio.sishosp.acl.dao.PessoaDAO;
import br.gov.al.maceio.sishosp.acl.model.PessoaBean;
import br.gov.al.maceio.sishosp.acl.model.UsuarioPosseBean;


import javax.faces.component.EditableValueHolder;
import javax.servlet.ServletContext;

@ManagedBean(name = "MBPessoa")
@ViewScoped
public class PessoaController {

	private PessoaBean pessoa;
	private List<UsuarioPosseBean> listaUsuario;
	private UIData linhaservidor, linhalistagem;
	private Integer idpessoa;
	private boolean desativar_campos;
	private boolean efetivo;
	private List<PessoaBean> listapessoa;
	private Integer tipo;
	private String campotexto;
	private Integer campobusca;
	
	

	public PessoaController() {
		pessoa = new PessoaBean();
		pessoa.setCpf("");
		desativar_campos = false;
		pessoa.setUf("AL");
		campobusca = 2;
	}

	public List<UsuarioPosseBean> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<UsuarioPosseBean> listaUsuario) {
		this.listaUsuario = listaUsuario;
	
	}

	public void limparBeans() {
		pessoa = new PessoaBean();
	
	}

	public void LimparBusca() {
		campobusca = 1;
		campotexto = "";
		listapessoa = null;
	}

	public void verificaCep() {
		String cep_aux = String.valueOf(pessoa.getCep()).trim()
				.replaceAll("[^0-9]", "");
		if (cep_aux.length() == 8) {
			String cep = String.valueOf(pessoa.getCep());
			WebServiceCep webServiceCep = WebServiceCep.searchCep(cep
					.substring(0, 4) + "-" + cep.substring(4, cep.length()));
			if (webServiceCep.wasSuccessful()) {
				pessoa.setCep(Integer.valueOf(cep_aux));
				pessoa.setEnder(webServiceCep.getLogradouroFull());
				pessoa.setBairro(webServiceCep.getBairro());
				pessoa.setCidade(webServiceCep.getCidade());
				pessoa.setUf(webServiceCep.getUf());
				// caso haja problemas imprime as exce��es.
			} else {
				FacesMessage msg = new FacesMessage("Erro:"
						+ webServiceCep.getResultText());
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	public void ConsultaPessoa() throws ProjetoException {
		if (campotexto != null || campotexto != "0" || campobusca != null || campobusca != 0) {
			listapessoa = new ArrayList<>();
			PessoaDAO pdao = new PessoaDAO();
			listapessoa = pdao.ConsultaPessoa(campotexto, campobusca);
			
			if (campobusca == 1
					&& (listapessoa.size() == 0 || listapessoa == null)) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"O servidor a partir desse CPF não foi encontrado!",
						"Aviso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			if (campobusca == 2
					&& (listapessoa.size() == 0 || listapessoa == null)) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"O servidor a partir desse nome não foi encontrado!",
						"Aviso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}
	
	


	

	/*
	public void removerZeros(){
			if(Double.valueOf(pessoa.getCelular().toString()).doubleValue() == 0 ||
			   Double.valueOf(pessoa.getTelef().toString()).doubleValue() == 0	||
			   Integer.valueOf(pessoa.getElesec().toString()).intValue() == 0	||
			   Integer.valueOf(pessoa.getElezona().toString()).intValue() == 0 ){
				
	
				System.out.println("Entrou Teste");
				pessoa.setCelular(null);
				pessoa.setTelef(null);
				pessoa.setElesec(null);
				pessoa.setElezona(null);

			}
	}
	*/
	public void updateDadosServidor() throws ProjetoException {
		
		boolean alterou = false;
		PessoaDAO pdao = new PessoaDAO();
		
		if (pessoa != null) {
			alterou = pdao.atualizaDadosPessoais(pessoa);
			
		}
		
		if (alterou) {
			FacesMessage msg = new FacesMessage("Atualizado com Sucesso");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
		} else {
			FacesMessage msg = new FacesMessage(
					"Ocorreu um erro na alteração, por favor verificar os dados");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	




	public PessoaBean getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaBean pessoa) {
		this.pessoa = pessoa;
	}

	public UIData getLinhaservidor() {
		return linhaservidor;
	}

	public void setLinhaservidor(UIData linhaservidor) {
		this.linhaservidor = linhaservidor;
	}

	public UIData getLinhalistagem() {
		return linhalistagem;
	}

	public void setLinhalistagem(UIData linhalistagem) {
		this.linhalistagem = linhalistagem;
	}

	public Integer getIdpessoa() {
		return idpessoa;
	}

	public void setIdpessoa(Integer idpessoa) {
		this.idpessoa = idpessoa;
	}

	public boolean isDesativar_campos() {
		return desativar_campos;
	}

	public void setDesativar_campos(boolean desativar_campos) {
		this.desativar_campos = desativar_campos;
	}

	public boolean isEfetivo() {
		return efetivo;
	}

	public void setEfetivo(boolean efetivo) {
		this.efetivo = efetivo;
	}

	public List<PessoaBean> getListapessoa() throws ProjetoException {
		return listapessoa;
	}

	public void setListapessoa(List<PessoaBean> listapessoa) {
		this.listapessoa = listapessoa;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getCampotexto() {
		return campotexto;
	}

	public void setCampotexto(String campotexto) {
		this.campotexto = campotexto;
	}

	public Integer getCampobusca() {
		return campobusca;
	}

	public void setCampobusca(Integer campobusca) {
		this.campobusca = campobusca;
	}
}