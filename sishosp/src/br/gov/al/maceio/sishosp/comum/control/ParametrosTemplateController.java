package br.gov.al.maceio.sishosp.comum.control;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.gov.al.maceio.sishosp.comum.Configuracao.Propriedades;

@ManagedBean(name = "parametrosTemplate", eager = true)
@SessionScoped
public class ParametrosTemplateController implements Serializable {
    private String versao;
	private String ano;
	private String footerLocalStyle = "";
	public Boolean conexaoLocal = Propriedades.Conexao.equals(Propriedades.Conexoes.valueOf("LOCALHOST"));

    public ParametrosTemplateController() {
		setVersao("Versão: 1.3.3");
		setAno("2022");
		if(conexaoLocal){
			setFooterLocalStyle("background-image: -webkit-linear-gradient(#049b1d, #048732 60%, #036923);\n" + //old: #9b0404, #870404 60%, #690303
					"\tbackground-image:                -o-linear-gradient(#049b1d, #048732 60%, #036923);\n" +
					"\tbackground-image:                   linear-gradient(#049b1d, #048732 60%, #036923);");
		}
	}

	public String getVersao() {
		if(conexaoLocal) { return versao.concat(" LOCAL"); }
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getFooterLocalStyle(){
		return footerLocalStyle;
	}

	public void setFooterLocalStyle(String footerLocalStyle) {
		this.footerLocalStyle = footerLocalStyle;
	}
}
