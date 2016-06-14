package br.gov.al.maceio.sishosp.comum.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ConnectionFactory {

    private static final String DRIVER_CLASS = "org.postgresql.Driver";
    //Banco real \/
    //private static final String URL = "jdbc:postgresql://localhost:5432/sishosp";
    //private static final String USER = "postgres";
    //private static final String PASS = "post";
    
    // Banco Teste  \/
    private static final String URL = "jdbc:postgresql://localhost:5432/sishosp";
    private static final String USER = "postgres";
    private static final String PASS = "thiago123";
    public static Connection getConnection() {
        
        Connection conexao = null;

        try {            
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Não foi possível localizar o driver de conexão com o banco!"
                + "\nPor favor, entre em contato com o administrador." 
                + "\nErro: " + ex, "Erro"); //2161 / MAN69 / 929450

            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        try {
            //System.out.println("Conectando ao banco de dados...");
            conexao = DriverManager.getConnection(URL, USER, PASS);
            conexao.setAutoCommit(false);
            //System.out.println("Conexão realizada com sucesso!");
        } catch (SQLException ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Não foi possível realizar a conexão com o banco de dados!"
                + "\nPor favor, entre em contato com o administrador." 
                + "\nErro: " + ex, "Erro");

            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return conexao;
    }
}