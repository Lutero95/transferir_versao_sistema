package br.gov.al.maceio.sishosp.comum.validator;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.*;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;

public class DataNascimentoPacienteValidator implements Validator {
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object valorTela) {

		Date dataNascimento = (Date) valorTela;
		Date dataAtual = DataUtil.retornarDataAtual();
		if (!VerificadorUtil.verificarSeObjetoNulo(dataNascimento)) {
			if (dataAtual.before(dataNascimento) ) {
				FacesMessage message = new FacesMessage();
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("Data de Nascimento não pode ser maior que a data atual!");
				throw new ValidatorException(message);
			}


		}
	}

}
