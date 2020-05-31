package br.gov.al.maceio.sishosp.comum.model.builder;



import br.gov.al.maceio.sishosp.comum.model.Novidade;

import java.sql.ResultSet;
import java.sql.SQLException;

import static br.gov.al.maceio.sishosp.comum.util.BancoUtil.recuperarInteiro;
import static br.gov.al.maceio.sishosp.comum.util.BancoUtil.recuperarString;


public class NovidadeBuilder {
    private Novidade novidade;

    public NovidadeBuilder() {
        this.novidade = new Novidade();
    }

    public NovidadeBuilder comId(Integer id){
        novidade.setId(id);
        return this;
    }

    public NovidadeBuilder comVisualizado(Boolean visualizado){
        novidade.setVisualizado(visualizado);
        return this;
    }

    public NovidadeBuilder comNomeSistema(String nomeSistema){
        novidade.setNomeSistema(nomeSistema);
        return this;
    }

    public NovidadeBuilder comNovidade(String novidade){
        this.novidade.setNovidade(novidade);
        return this;
    }

    public NovidadeBuilder comDescricao(String descricao){
        novidade.setDescricao(descricao);
        return this;
    }

    public NovidadeBuilder comDocumento(byte[] documento){
        novidade.setDocumento(documento);
        return this;
    }

    public NovidadeBuilder comNomeDocumento(String nomeDocumento){
        novidade.setNomeDocumento(nomeDocumento);
        return this;
    }

    public NovidadeBuilder comExtensaoDocumento(String extensaoDocumento){
        novidade.setExtensaoDocumento(extensaoDocumento);
        return this;
    }

    public Novidade contruir(){
        return this.novidade;
    }

    public Novidade mapear(ResultSet resultSet) throws SQLException {
        return this
                .comId(recuperarInteiro(resultSet, "id"))
                .comNomeSistema(recuperarString(resultSet, "descricao_sistema"))
                .comNovidade(recuperarString(resultSet, "novidade"))
                .comExtensaoDocumento(recuperarString(resultSet, "documento_extensao"))
                .comDocumento(resultSet.getBytes("documento"))
                .comNomeDocumento(recuperarString(resultSet, "documento_nome"))
                .comDescricao(recuperarString(resultSet, "descricao"))
                .contruir();
    }

}
