package br.gov.al.maceio.sishosp.comum.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


public class CaractereEmBrancoValidator implements Validator {
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object valorTela) {

		String valorTelaString = (String) valorTela;

		if(valorTelaString == null){
			valorTelaString = "";
		}
		if(valorTela != null){
			valorTelaString = valorTelaString.replaceAll(" ", "");
		}

		if (verificarSeEhCaractereEmBranco(valorTelaString)) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("Digite um valor válido, apenas espaços em branco não são permitidos!");
			throw new ValidatorException(message);
		}
	}

	private static Boolean verificarSeEhCaractereEmBranco(String valorTelaString) {
		Boolean retorno = false;

		if (valorTelaString.equals("")) {
			retorno = true;
		}

		return retorno;
	}
}
