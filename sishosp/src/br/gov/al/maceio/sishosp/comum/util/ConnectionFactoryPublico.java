package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactoryPublico {

    public static Connection getConnection() throws ProjetoException {

    	//LOCAL
        String url = "jdbc:postgresql://localhost:5432/publico";
        String usuario = "postgres";
        String senha  = "post";
        // String senha  = "engetron";

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
			// String msg =
            // "Driver de conexao com o banco nao encontrado \n Mensagem original: "
            // + cnf.getMessage();

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(msg.toString());
        } catch (SQLException sql) {
        	System.out.println("nao abriu con");
			// String msg =
            // "Nao foi possivel abrir a conexao com o banco \n Mensagem original: "
            // + sql.getMessage();
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Nao foi possivel abrir a conexao com o banco \n Mensagem original: "
                    + sql.getMessage(), "Erro");
			// String msg =
            // "Driver de conexao com o banco nao encontrado \n Mensagem original: "
            // + cnf.getMessage();

            FacesContext.getCurrentInstance().addMessage(null, msg);
            throw new ProjetoException(sql.getMessage());
        }
    }
}