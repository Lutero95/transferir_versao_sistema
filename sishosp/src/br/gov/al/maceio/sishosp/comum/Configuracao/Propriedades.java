package br.gov.al.maceio.sishosp.comum.Configuracao;

public class Propriedades {

    public static Conexoes Conexao = Conexoes.PRODUCAO ;

    public enum Conexoes {
        LOCALHOST,
        PRODUCAO,
        DEPLOY;
    }




}
