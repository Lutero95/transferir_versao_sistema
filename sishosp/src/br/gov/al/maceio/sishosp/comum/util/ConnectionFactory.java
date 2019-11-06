package br.gov.al.maceio.sishosp.comum.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

public class ConnectionFactory {

    public static Connection getConnection() throws ProjetoException {

        String nomeBancoAcesso = (String) SessionUtil.resgatarDaSessao("nomeBancoAcesso");

        //LOCAL
/*
        String url = "jdbc:postgresql://localhost:5432/";
        String usuario = "postgres";
        String senha = "engetron";
  */    
        //String senha = "engetron";

        //PRODUÇÃO
  
   /*
        String url = "jdbc:postgresql://72.55.172.244:5432/publico";
        String usuario = "postgres";
        String senha = "E2@spwxlmQo";

    	*/
        
        String url = "jdbc:postgresql://node39025-env-8766995.nordeste-idc.saveincloud.net:11511/";
        String usuario = "webadmin";
        String senha = "BVEsvr50661";

  
  /*
    	String url = "jdbc:postgresql://10.101.17.65:5432/";
        String usuario = "webadmin";
        String senha = "BVEsvr50661";

 */
        url = url + nomeBancoAcesso;

        try {
            Class.forName("org.postgresql.Driver");
            Connection con;
            con = DriverManager.getConnection(url, usuario, senha);
            con.setAutoCommit(false);
            return con;
        } catch (ClassNotFoundException cnf) {

            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Driver de conexao com o banco nao encontrado \n Mensagem original: "
                            + cnf.getMessage(), "Erro");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(msg.toString());

        } catch (SQLException sql) {
            System.out.println("Não abriu conexão");
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Nao foi possivel abrir a conexao com o banco \n Mensagem original: "
                            + sql.getMessage(), "Erro");

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(sql.getMessage());

        }
    }
}