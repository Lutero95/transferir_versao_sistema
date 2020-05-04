package br.gov.al.maceio.sishosp.hosp.model;

public class BpaConsolidadoBean {
	
	private final String prdIdent = "02";
	private String prdCnes;
	private String prdCmp;
	private String prdCbo;
	private String prdFlh;
	private String prdSeq;
	private String prdPa;
	private String prdIdade;
	private String prdQt;
	private final String prdOrg = "BPA";
	
	/*********PRD-FIM*****
	 * 
	 *  É Correspondente aos caracteres CR - CHR(13) LF - CHR(10), do padrão
	 * ASCII (.TXT), indicando fim do cabeçalho.
	 * Ou seja é igual a quebra de linhas gerada de cada item da lista do arrayList<String> usado na 
	 * geração do layout 
	 *
	 */
	
	@Override
	public String toString() {
		return prdIdent + prdCnes + prdCmp + prdCbo + prdFlh + prdSeq + prdPa + prdIdade + prdQt + prdOrg;
	}
	
	public String getPrdCnes() {
		return prdCnes;
	}
	public void setPrdCnes(String prdCns) {
		this.prdCnes = prdCns;
	}
	public String getPrdCmp() {
		return prdCmp;
	}
	public void setPrdCmp(String prdCmp) {
		this.prdCmp = prdCmp;
	}
	public String getPrdCbo() {
		return prdCbo;
	}
	public void setPrdCbo(String prdCbo) {
		this.prdCbo = prdCbo;
	}
	public String getPrdFlh() {
		return prdFlh;
	}
	public void setPrdFlh(String prdFlh) {
		this.prdFlh = prdFlh;
	}
	public String getPrdSeq() {
		return prdSeq;
	}
	public void setPrdSeq(String prdSeq) {
		this.prdSeq = prdSeq;
	}
	public String getPrdPa() {
		return prdPa;
	}
	public void setPrdPa(String prdPa) {
		this.prdPa = prdPa;
	}
	public String getPrdIdade() {
		return prdIdade;
	}
	public void setPrdIdade(String prdIdade) {
		this.prdIdade = prdIdade;
	}
	public String getPrdQt() {
		return prdQt;
	}
	public void setPrdQt(String prdQt) {
		this.prdQt = prdQt;
	}
	public String getPrdIdent() {
		return prdIdent;
	}
	public String getPrdOrg() {
		return prdOrg;
	}
	
}
