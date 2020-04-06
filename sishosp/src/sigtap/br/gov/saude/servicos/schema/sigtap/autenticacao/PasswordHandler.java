package sigtap.br.gov.saude.servicos.schema.sigtap.autenticacao;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import sigtap.br.gov.saude.servicos.schema.sigtap.autenticacao.constants.HeaderConstants;

public class PasswordHandler implements CallbackHandler{

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback passwordCallback = (WSPasswordCallback) callbacks[0];
		if(passwordCallback.getIdentifier().equals(HeaderConstants.USERNAME)) {
			passwordCallback.setPassword(HeaderConstants.PASSWORD);
		}	
	}

}
