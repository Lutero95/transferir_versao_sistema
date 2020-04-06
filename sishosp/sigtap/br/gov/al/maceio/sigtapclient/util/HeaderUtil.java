package br.gov.al.maceio.sigtapclient.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

import br.gov.saude.servicos.schema.sigtap.autenticacao.constants.HeaderConstants;

public class HeaderUtil {
	
	public static void montaSecurityHeader(Object port) {
		Client client = ClientProxy.getClient(port);
		client.getRequestContext().put(Message.ENDPOINT_ADDRESS, HeaderConstants.ENDPOINT_PROCEDIMENTO);
		
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(HeaderConstants.ACTION, HeaderConstants.USERNAME_TOKEN);
		outProps.put(HeaderConstants.USER, HeaderConstants.USERNAME);
		outProps.put(HeaderConstants.PASSWORD_TYPE, HeaderConstants.PASSWORD_TEXT);
		outProps.put(HeaderConstants.PASSWORD_CALLBACK_CLASS, HeaderConstants.PASSWORD_HANDLER_CLASS);
		WSS4JOutInterceptor outInterceptor = new WSS4JOutInterceptor(outProps);
		client.getOutInterceptors().add(outInterceptor);
	}
}
