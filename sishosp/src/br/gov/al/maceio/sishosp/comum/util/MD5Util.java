package br.gov.al.maceio.sishosp.comum.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final String MD5 = "MD5";

    private MD5Util() {
    }

    public static String gerarHash(String texto) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(texto.getBytes());
            byte[] digest = messageDigest.digest();
            return retornarHashDeArquivo(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String gerarHashDeArquivo(byte[] arquivo) {
        byte[] bytesArquivoHashMD5 = new byte[0];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(arquivo);
            bytesArquivoHashMD5 = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return retornarHashDeArquivo(bytesArquivoHashMD5);
    }

    private static String retornarHashDeArquivo(byte[] arquivo) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte byteArquivo : arquivo) {
            int parteAlta = ((byteArquivo >> 4) & 0xf) << 4;
            int parteBaixa = byteArquivo & 0xf;
            if (parteAlta == 0) stringBuilder.append('0');
            stringBuilder.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return stringBuilder.toString();
    }
}
