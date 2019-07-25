package br.gov.al.maceio.sishosp.comum.validator;

import br.gov.al.maceio.sishosp.comum.util.DocumentosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


public class CNPJValidator implements Validator {
    @Override
    public void validate(FacesContext arg0, UIComponent arg1, Object valorTela) {

        String valorTelaString = (String) valorTela;

        if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(valorTelaString)) {
            valorTelaString = valorTelaString.replaceAll(" ", "").replaceAll("[^0-9]", "");

            if (!DocumentosUtil.validaCPF(valorTelaString)) {
                FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary("CPF não válido!");
                throw new ValidatorException(message);
            }
        }
    }

}
