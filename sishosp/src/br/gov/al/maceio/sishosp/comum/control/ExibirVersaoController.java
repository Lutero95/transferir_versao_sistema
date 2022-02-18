package br.gov.al.maceio.sishosp.comum.control;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import br.gov.al.maceio.sishosp.comum.Configuracao.Propriedades;

@ManagedBean(name = "exibirVersao", eager = true)
@SessionScoped
public class ExibirVersaoController implements Serializable {
    private String versao;

    public ExibirVersaoController() {
    	setVersao("Versão: 1.2.3");
	}
    
	public String getVersao() {
		if((boolean) Propriedades.Conexao.equals(Propriedades.Conexoes.valueOf("LOCALHOST"))) {
			return versao.concat(" LOCAL");
		}
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

}
