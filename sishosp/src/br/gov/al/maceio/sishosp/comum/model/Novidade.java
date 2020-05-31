package br.gov.al.maceio.sishosp.comum.model;

public class Novidade {
    private Integer id;
    private Boolean Visualizado;
    private String nomeSistema;
    private String novidade;
    private String descricao;
    private byte[] documento;
    private String nomeDocumento;
    private String extensaoDocumento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getVisualizado() {
        return Visualizado;
    }

    public void setVisualizado(Boolean visualizado) {
        Visualizado = visualizado;
    }

    public String getNomeSistema() {
        return nomeSistema;
    }

    public void setNomeSistema(String nomeSistema) {
        this.nomeSistema = nomeSistema;
    }

    public String getNovidade() {
        return novidade;
    }

    public void setNovidade(String novidade) {
        this.novidade = novidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public String getExtensaoDocumento() {
        return extensaoDocumento;
    }

    public void setExtensaoDocumento(String extensaoDocumento) {
        this.extensaoDocumento = extensaoDocumento;
    }
}
