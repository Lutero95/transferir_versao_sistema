package br.gov.al.maceio.sishosp.comum.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Menus {

	public List<String> meses(){
		List<String> meses = new ArrayList<String>();
		meses.add("Janeiro");
		meses.add("Fevereiro");
		meses.add("Março");
		meses.add("Abril");
		meses.add("Maio");
		meses.add("Junho");
		meses.add("Julho");
		meses.add("Agosto");
		meses.add("Setembro");
		meses.add("Outubro");
		meses.add("Novembro");
		meses.add("Dezembro");
		return meses;
	}
	
	public List<Integer> anos(){
		Calendar c = Calendar.getInstance();
		List<Integer> anos = new ArrayList<Integer>();
		for(int ano = c.get(Calendar.YEAR); ano >= 2003; ano--){
			anos.add(ano);
		}
		return anos;
	}
	
}
