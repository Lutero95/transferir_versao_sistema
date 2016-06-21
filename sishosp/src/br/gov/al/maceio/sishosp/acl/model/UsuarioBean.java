package br.gov.al.maceio.sishosp.acl.model;

import br.gov.al.maceio.sishosp.acl.model.PermissoesBean;
import br.gov.al.maceio.sishosp.acl.model.SecretariaBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

public class UsuarioBean {

    private Integer codigo;
    private String nome;
    private Integer user_secretaria;
    private Integer user_setor;
    private Integer nivel;
    private String senha;
    private Date datacriacao;
    private String login;
    private String ativo;
    private boolean administrador;
    private String email;
    private String cpf;
    private boolean primeiroAcesso;
    private Boolean favorito;
    private Integer codSecre;
    private String descSecretaria;
    private String descSetor;
    private ArrayList<Integer> codSetor;
    private PermissoesBean permissao;
    private Integer idpessoa;
    private Boolean autorizado;

    //ACL
    private Long id; //id usuário para o acl.
    private boolean usuarioAtivo;
    private Integer idPerfil;
    private String descPerfil;
    private List<Integer> listaIdSistemas;
    private List<Long> listaIdPermissoes;

    private List<SecretariaBean> listaSecreFolha;

    //RH
    private Integer matricula;
    private Integer empfil;
    private boolean juntaMedica;
    private Integer operador;

    private boolean permissaoJunta;

    public UsuarioBean() {
        permissao = new PermissoesBean();
    }

    public UsuarioBean(Integer codigo, String nome, Integer user_secretaria,
        Integer user_setor, Integer nivel, String senha, Date datacriacao,
        String login, String ativo, boolean administrador, String email, Boolean autorizado,
        String cpf, boolean primeiroAcesso, String descSecretaria,
        String descSetor, Integer idpessoa, Integer operador, boolean permissaoJunta) {

        setCodigo(codigo);
        setNome(nome);
        setUser_secretaria(user_secretaria);
        setUser_setor(user_setor);
        setNivel(nivel);
        setSenha(senha);
        setDatacriacao(datacriacao);
        setLogin(login);
        setAtivo(ativo);
        setAdministrador(administrador);
        setEmail(email);
        setAutorizado(autorizado);
        setCpf(cpf);
        setPrimeiroAcesso(primeiroAcesso);
        setDescSecretaria(descSecretaria);
        setDescSetor(descSetor);
        setIdpessoa(idpessoa);
        setOperador(operador);
        setPermissaoJunta(permissaoJunta);
    }

    public UsuarioBean(String idpessoa) {

    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getUser_secretaria() {
        return user_secretaria;
    }

    public void setUser_secretaria(Integer user_secretaria) {
        this.user_secretaria = user_secretaria;
    }

    public Integer getUser_setor() {
        return user_setor;
    }

    public void setUser_setor(Integer user_setor) {
        this.user_setor = user_setor;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDatacriacao() {
        return datacriacao;
    }

    public void setDatacriacao(Date datacriacao) {
        this.datacriacao = datacriacao;
    }

    public String getLogin() {
        return login;
    }

    public boolean isPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public String getDescSetor() {
        return descSetor;
    }

    public void setDescSetor(String descSetor) {
        this.descSetor = descSetor;
    }

    public String getDescSecretaria() {
        return descSecretaria;
    }

    public void setDescSecretaria(String descSecretaria) {
        this.descSecretaria = descSecretaria;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public PermissoesBean getPermissao() {
        return permissao;
    }

    public void setPermissao(PermissoesBean permissao) {
        this.permissao = permissao;
    }

    public ArrayList<Integer> getCodSetor() {
        return codSetor;
    }

    public void setCodSetor(ArrayList<Integer> codSetor) {
        this.codSetor = codSetor;
    }

    public Integer getCodSecre() {
        return codSecre;
    }

    public void setCodSecre(Integer codSecre) {
        this.codSecre = codSecre;
    }

    public String toString() {
        return nome + login + senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDescPerfil() {
        return descPerfil;
    }

    public void setDescPerfil(String descPerfil) {
        this.descPerfil = descPerfil;
    }

    public List<Integer> getListaIdSistemas() {
        return listaIdSistemas;
    }

    public void setListaIdSistemas(List<Integer> listaIdSistemas) {
        this.listaIdSistemas = listaIdSistemas;
    }

    public List<Long> getListaIdPermissoes() {
        return listaIdPermissoes;
    }

    public void setListaIdPermissoes(List<Long> listaIdPermissoes) {
        this.listaIdPermissoes = listaIdPermissoes;
    }

    public boolean isUsuarioAtivo() {
        return usuarioAtivo;
    }

    public void setUsuarioAtivo(boolean usuarioAtivo) {
        this.usuarioAtivo = usuarioAtivo;
    }

    public List<SecretariaBean> getListaSecreFolha() {
        return listaSecreFolha;
    }

    public void setListaSecreFolha(List<SecretariaBean> listaSecreFolha) {
        this.listaSecreFolha = listaSecreFolha;
    }

    public Integer getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Integer idpessoa) {
        this.idpessoa = idpessoa;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public Integer getEmpfil() {
        return empfil;
    }

    public void setEmpfil(Integer empfil) {
        this.empfil = empfil;
    }

    public boolean isJuntaMedica() {
        return juntaMedica;
    }

    public void setJuntaMedica(boolean juntaMedica) {
        this.juntaMedica = juntaMedica;
    }

    public Integer getOperador() {
        return operador;
    }

    public void setOperador(Integer operador) {
        this.operador = operador;
    }

    public boolean isPermissaoJunta() {
        return permissaoJunta;
    }

    public void setPermissaoJunta(boolean permissaoJunta) {
        this.permissaoJunta = permissaoJunta;
    }

	public Boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Boolean autorizado) {
		this.autorizado = autorizado;
	}
    
    
}