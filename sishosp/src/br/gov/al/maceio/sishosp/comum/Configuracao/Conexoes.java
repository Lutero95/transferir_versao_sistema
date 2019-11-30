package br.gov.al.maceio.sishosp.comum.Configuracao;

import java.util.Objects;

public class Conexoes {

    private String urlBanco;
    private String usuario;
    private String senha;

    public Conexoes() {
    }

    public Conexoes(String urlBanco, String usuario, String senha) {
        this.urlBanco = urlBanco;
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUrlBanco() {
        return urlBanco;
    }

    public void setUrlBanco(String urlBanco) {
        this.urlBanco = urlBanco;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conexoes conexoes = (Conexoes) o;
        return Objects.equals(urlBanco, conexoes.urlBanco) &&
                Objects.equals(usuario, conexoes.usuario) &&
                Objects.equals(senha, conexoes.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlBanco, usuario, senha);
    }

    @Override
    public String toString() {
        return "Conexoes{" +
                "urlBanco='" + urlBanco + '\'' +
                ", usuario='" + usuario + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
