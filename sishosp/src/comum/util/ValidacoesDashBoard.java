package br.gov.al.maceio.sishosp.comum.util;

public class ValidacoesDashBoard {

	public boolean validata(String data, String data2) {

		String dia, mes, ano;
		String dia2, mes2, ano2;
		int diaint, mesint, anoint;
		int diaint2, mesint2, anoint2;
		
		
		dia = data.substring(0, 2);// captura apenas o dia
		mes = data.substring(3, 5);// captura apenas o m�s
		ano = data.substring(6, 10);// captura apenas o ano

		dia2 = data2.substring(0, 2);// captura apenas o dia
		mes2 = data2.substring(3, 5);// captura apenas o m�s
		ano2 = data2.substring(6, 10);// captura apenas o ano

		diaint = Integer.parseInt(dia);// transforma o valor de String para int
		mesint = Integer.parseInt(mes);
		anoint = Integer.parseInt(ano);

		diaint2 = Integer.parseInt(dia2);// transforma o valor de String para
											// int
		mesint2 = Integer.parseInt(mes2);
		anoint2 = Integer.parseInt(ano2);

		
		if (diaint < 1 || diaint > 31 || mesint < 1 || mesint > 12
				|| diaint2 < 1 || diaint2 > 31 || mesint2 < 1 || mesint2 > 12) {

			

			return false;

		}
		if(anoint == anoint2 && mesint > mesint2){
			
			
			
			
			return false;
			
		}

		if (anoint > anoint2) {

			

			return false;
			
		}

	

		return true;

	}

}
