package br.gov.al.maceio.sishosp.acl.dao;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;

import java.util.List;

import javax.faces.model.SelectItem;

public interface IUsuarioDAO {

	public abstract FuncionarioBean autenticarUsuario(FuncionarioBean b) throws ProjetoException;
	public abstract FuncionarioBean consultaUsuario() throws ProjetoException;
	public  abstract List buscaUsuariosSecretaria() throws ProjetoException;
	public  abstract List buscaUsuariosPorSecretaria(int cod) throws ProjetoException;
	public abstract int newUser(FuncionarioBean b) throws ProjetoException;	
	public void desativarUser() throws ProjetoException;
	public void ativarUser() throws ProjetoException;
	public void permUsusario(FuncionarioBean user,Integer id)throws ProjetoException;
	public boolean updateEditSenha(FuncionarioBean U) throws ProjetoException;
	public boolean updateEditUsuario(FuncionarioBean U) throws ProjetoException;
	public int updateUsuario(FuncionarioBean user)throws ProjetoException;
	public void updatePerm(FuncionarioBean user)throws ProjetoException;
	public String verificaUserCadastrado(String s)	throws ProjetoException ;
	public String alteraSenha(FuncionarioBean u) throws ProjetoException;
	public String verificaLoginCadastrado(String login)	throws ProjetoException;	
	public Integer verificaUltimoAdm(FuncionarioBean U) throws ProjetoException;
	public List<SelectItem> consultaAnoAberturaProc() throws ProjetoException ;
        //public List<String> consultaAnoAberturaProc() throws ProjetoException ;
	public boolean existeUsuarioAtivo(String cpf ) throws ProjetoException ;	
//	public abstract FuncionarioBean novoFuncionario(FuncionarioBean fb) throws ProjetoException;
//  public List<FuncionarioBean> allPassword() throws ProjetoException;
}
