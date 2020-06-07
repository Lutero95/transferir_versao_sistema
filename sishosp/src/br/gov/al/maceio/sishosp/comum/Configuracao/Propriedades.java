package br.gov.al.maceio.sishosp.comum.Configuracao;

public class Propriedades {

    public static Conexoes Conexao = Conexoes.LOCALHOST ;

    public enum Conexoes {
        LOCALHOST,
        PRODUCAO,
        DEPLOY;
    }




}
