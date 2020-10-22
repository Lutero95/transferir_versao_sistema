package br.com.correios.bsb.sigep.master.bean.cliente;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class Teste {

	public static void main(String[] args) throws SQLException, SigepClienteException, RemoteException, ServiceException {
		// TODO Auto-generated method stub
		
		AtendeClienteServiceLocator locator = new AtendeClienteServiceLocator();
		AtendeClienteServiceSoapBindingStub bindingStub	= (AtendeClienteServiceSoapBindingStub) locator.getAtendeClientePort();
		
		EnderecoERP enderecoERP = bindingStub.consultaCEP("57025-772"); 
		System.out.println("CEP "+enderecoERP.getCep());
		System.out.println("UF "+enderecoERP.getUf());
		System.out.println("CIDADE "+enderecoERP.getCidade());
		System.out.println("BAIRRO "+enderecoERP.getBairro());
		System.out.println("ENDEREÇO "+enderecoERP.getEnd());
		
	}

}
