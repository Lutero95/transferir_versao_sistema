package br.gov.al.maceio.sishosp.comum.util;

import br.com.correios.bsb.sigep.master.bean.cliente.AtendeClienteServiceLocator;
import br.com.correios.bsb.sigep.master.bean.cliente.AtendeClienteServiceSoapBindingStub;
import br.com.correios.bsb.sigep.master.bean.cliente.EnderecoERP;
import br.com.correios.bsb.sigep.master.bean.cliente.SigepClienteException;

public class CepWebService {

	private String estado = "";
	private String cidade = "";
	private String bairro = "";
	private String tipoLogradouro = "";
	private String logradouro = "";

	private int resultado = 0;

	public CepWebService(String cep) {
		try {
			AtendeClienteServiceLocator locator = new AtendeClienteServiceLocator();
			AtendeClienteServiceSoapBindingStub bindingStub	= (AtendeClienteServiceSoapBindingStub) locator.getAtendeClientePort();
			EnderecoERP enderecoERP = bindingStub.consultaCEP(cep); 
			
			setEstado(enderecoERP.getUf());
			setCidade(enderecoERP.getCidade());
			setBairro(enderecoERP.getBairro());
			setLogradouro(enderecoERP.getEnd());
			setResultado(1);
		} 
		catch (SigepClienteException se) {
			setResultado(0);
			JSFUtil.adicionarMensagemAdvertencia("CEP inválido!", "Advertência");
			se.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}
}