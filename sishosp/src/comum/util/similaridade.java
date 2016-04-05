package br.gov.al.maceio.sishosp.comum.util;

import java.util.ArrayList;


public class similaridade {
	
	public ArrayList<String> source(String name){
		ArrayList<String> array_s = new ArrayList<String>();
		char first = name.charAt(0);
		array_s.add(String.valueOf(first));
		
		for (int i=0; i<name.length(); i++){

			char ch = name.charAt(i);
			if (ch == ' '){
				ch = name.charAt(i+1);
				array_s.add(String.valueOf(ch));
			}
		}
		for (int i=0; i<array_s.size(); i++){

		}		
		return array_s;
	}
	
	
	public ArrayList<String> destination(String name){
		ArrayList<String> array_d = new ArrayList<String>();
		char first = name.charAt(0);
		array_d.add(String.valueOf(first));
		
		for (int i=0; i<name.length(); i++){

			char ch = name.charAt(i);
			if (ch == ' '){
				ch = name.charAt(i+1);
				array_d.add(String.valueOf(ch));
			}
		}
		for (int i=0; i<array_d.size(); i++){

		}
		return array_d;
	}
	
	
	public int merge(ArrayList<String> a, ArrayList<String> b) {
		int count = 0;

		for (int x = 0; x < a.size(); x++) {
			for (int y = 0; y < b.size(); y++) {
				
				if (a.get(x).equals(b.get(y))){
					count++;
				}
			}
		}
		return count;
	}
		
		
	/*public static void main(String[] args) {
		
		similaridade obj = new similaridade();
		
		

	}*/
	
	
}
