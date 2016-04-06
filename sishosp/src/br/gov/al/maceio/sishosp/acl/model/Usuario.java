package br.gov.al.maceio.sishosp.acl.model;

import java.util.List;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
public class Usuario {
    
    private Long id;
    private String nome;
    private String sexo;
    private String cpf;
    private String email;
    private String login;
    private String senha;
    private boolean ativo;
    private Integer idPerfil;
    private String descPerfil;
    private List<Integer> listaIdSistemas;
    private List<Long> listaIdPermissoes;

    public Usuario() {
        
    }

    public Usuario(Long id, String nome, String sexo, String cpf, String email, 
        String login, String senha, boolean ativo, Integer idPerfil, String descPerfil, 
        List<Integer> listaIdSistemas, List<Long> listaIdPermissoes) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.cpf = cpf;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
        this.idPerfil = idPerfil;
        this.descPerfil = descPerfil;
        this.listaIdSistemas = listaIdSistemas;
        this.listaIdPermissoes = listaIdPermissoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
}