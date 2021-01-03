package br.gov.al.maceio.sishosp.comum.Configuracao;

public class Propriedades {

    public static Conexoes Conexao = Conexoes.DEPLOY ;


    public enum Conexoes {
        LOCALHOST,
        PRODUCAO,
        DEPLOY;
    }




}
