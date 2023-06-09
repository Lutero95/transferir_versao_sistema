package br.gov.al.maceio.sishosp.comum.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BancoUtil {
	
	private static final String SALT = "mgJ*UMO@E7dZ8D4iqW";

    public static Integer recuperarInteiro(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getInt(nomeColuna);
        } catch (SQLException e) {
            return null;
        }
    }

    public static Long recuperarLong(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getLong(nomeColuna);
        } catch (SQLException e) {
            return null;
        }
    }

    public static BigDecimal recuperarBigDecimal(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getBigDecimal(nomeColuna);
        } catch (SQLException e) {
            return null;
        }
    }

    public static String recuperarString(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getString(nomeColuna);
        } catch (SQLException e) {
            return null;
        }
    }

    public static Timestamp recuperarTimeStamp(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getTimestamp(nomeColuna);
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean recuperarBoolean(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getBoolean(nomeColuna);
        } catch (SQLException e) {
            return false;
        }
    }

    public static byte[] recuperarBytes(ResultSet resultSet, String nomeColuna) {
        try {
            return resultSet.getBytes(nomeColuna);
        } catch (SQLException e) {
            return null;
        }
    }
    
    public static String obterSenhaCriptografada(String senha) {
        try {
            String var10000 = senha.toUpperCase();
            return MD5Util.gerarHash(var10000 + SALT.toUpperCase());
        } catch (Exception var2) {
            System.out.println(var2);
            return null;
        }
    }
}
