package br.gov.al.maceio.sishosp.acl.model;
 
import java.util.Date;

import br.gov.al.maceio.sishosp.acl.model.SecretariaBean;

public class UsuarioPosseBean extends PessoaBean{

	private String senha;
	private String operador;	
	private Integer mat;
	private String ativo;
	private SecretariaBean secbean;

	public UsuarioPosseBean(){
		secbean = new SecretariaBean();		
	}
/*
	public UsuarioPosseBean(Integer id_pessoa, String nome, Date dtanasc2,
			Integer sexo, Integer estcivil, Integer naciona, String naturale,
			String munnat, Integer instruc, String sangue, String nomepai,
			String nomemae, String idnum, String idser, String iduf,
			String profnum, String profser, String profuf, String cpf,
			String pispas, String elenum, Integer elezona, Integer elesec,
			String eleuf, String resnum, String rescsm, String resser,
			String ender, Integer endernum, String enderc, String bairro,
			String cidade, String uf, Integer cep, Integer ddd, String telef,
			Integer dddc, String celular, String email1, String email2,
			Integer codmun, Integer depir, Integer depsf, Date dtincor2,
			String conjuge, Date dtnascconjuge, Integer racacor,
			Integer tipodeficiencia, Date dtcheg2, String flagnat,
			String rpnum, String rpexp, String rpuf, String chcrs,
			Date dtvalchcrs, String catchcrs) {
		super(id_pessoa, nome, dtanasc2, sexo, estcivil, naciona, naturale, munnat,
				instruc, sangue, nomepai, nomemae, idnum, idser, iduf, profnum,
				profser, profuf, cpf, pispas, elenum, elezona, elesec, eleuf, resnum,
				rescsm, resser, ender, endernum, enderc, bairro, cidade, uf, cep, ddd,
				telef, dddc, celular, email1, email2, codmun, depir, depsf, dtincor2,
				conjuge, dtnascconjuge, racacor, tipodeficiencia, dtcheg2, flagnat,
				rpnum, rpexp, rpuf, chcrs, dtvalchcrs, catchcrs);
		// TODO Auto-generated constructor stub
	}

	*/

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}
	public Integer getMat() {
		return mat;
	}



	public void setMat(int matricula) {
		this.mat = matricula;
	}
	
	public String getAtivo(){
		return ativo;
	}
	public void setAtivo(String ativo){
		this.ativo = ativo;
	}

	public SecretariaBean getSecbean() {
		return secbean;
	}

	public void setSecbean(SecretariaBean secbean) {
		this.secbean = secbean;
	}



}
