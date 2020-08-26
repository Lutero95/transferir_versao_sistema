package br.gov.al.maceio.sishosp.comum.validator;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.DocumentosUtil;
import br.gov.al.maceio.sishosp.comum.util.SessionUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.control.PacienteController;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


public class CNSValidator implements Validator {
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object valorTela) {

		String valorTelaString = (String) valorTela;

		if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(valorTelaString)) {

			if (!DocumentosUtil.validaCNS(valorTelaString)) {
				FacesMessage message = new FacesMessage();
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary("CNS não válida!");
				throw new ValidatorException(message);
			}
			
			PacienteDAO pDAo = new PacienteDAO();
			PacienteBean pacienteRetorno;
			Integer idPaciente =  (Integer) SessionUtil.resgatarDaSessao( "paramIdPaciente");
			try {
				pacienteRetorno = pDAo.verificaExisteCnsCadastrado(valorTelaString,idPaciente);
				if (pacienteRetorno != null) {
					FacesMessage message = new FacesMessage();
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					message.setSummary("Já existe cns cadastrado para o paciente " + pacienteRetorno.getNome());
					throw new ValidatorException(message);
				}
			} catch (ProjetoException e) {
				e.printStackTrace();
			} 
		}
	}

}
