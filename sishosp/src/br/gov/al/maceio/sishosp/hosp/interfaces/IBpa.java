package br.gov.al.maceio.sishosp.hosp.interfaces;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

public interface IBpa {

	public String preencheCaracteresRestantes(String campo) throws ProjetoException;
}
