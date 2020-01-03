package br.gov.al.maceio.sishosp.comum.Configuracao;

public class ConexaoBuilder {

    public static Conexoes carregarDadosConexao(String nomeBanco){

        Propriedades propriedades = new Propriedades();

        Conexoes conexoes = new Conexoes();
        if(propriedades.Conexao.equals(Propriedades.Conexoes.LOCALHOST)){
            conexoes.setUrlBanco("jdbc:postgresql://localhost:5432/"+nomeBanco);
            conexoes.setUsuario("postgres");
            conexoes.setSenha("engetron");
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.PRODUCAO)){
            conexoes.setUrlBanco("jdbc:postgresql://node39025-env-8766995.nordeste-idc.saveincloud.net:11511/"+nomeBanco);
            conexoes.setUsuario("webadmin");
            conexoes.setSenha("BVEsvr50661");
        }
        else if(propriedades.Conexao.equals(Propriedades.Conexoes.DEPLOY)){
            conexoes.setUrlBanco("jdbc:postgresql://10.101.17.65:5432/"+nomeBanco);
            conexoes.setUsuario("webadmin");
            conexoes.setSenha("BVEsvr50661");
        }

        return conexoes;
    }

}
