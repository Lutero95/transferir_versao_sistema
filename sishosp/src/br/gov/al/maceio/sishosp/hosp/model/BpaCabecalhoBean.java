package br.gov.al.maceio.sishosp.hosp.model;

public class BpaCabecalhoBean {
	private final String cbcHdrIndicadorLinha = "01";
	private final String cbcHdrIndicadorInicio = "#BPA#";
	private String cbcMvm;
	private String cbcLin;
	private String cbcFlh;
	private String cbcSmtVrf;
	private String cbcRsp;
	private String cbcSgl; 
	private String cbcCgccpf;
	private String cbcDst;            
	private String cbcDstIn;
	private String cbcVersao;
	
	/*********CBC-FIM*****
	 * 
	 *  É Correspondente aos caracteres CR - CHR(13) LF - CHR(10), do padrão
	 * ASCII (.TXT), indicando fim do cabeçalho.
	 * Ou seja é igual a quebra de linhas gerada de cada item da lista do arrayList<String> usado na 
	 * geração do layout 
	 *
	 */
	
	@Override
	public String toString() {
		return cbcHdrIndicadorLinha + cbcHdrIndicadorInicio + cbcMvm +  cbcLin +  cbcFlh
				+ cbcSmtVrf + cbcRsp + cbcSgl + cbcCgccpf + cbcDst + cbcDstIn + cbcVersao;
	}

	
	public String getCbcMvm() {
		return cbcMvm;
	}
		
	public void setCbcMvm(String cbcMvm) {
		this.cbcMvm = cbcMvm;
	}
	public String getCbcLin() {
		return cbcLin;
	}
	public void setCbcLin(String cbcLin) {
		this.cbcLin = cbcLin;
	}
	public String getCbcFlh() {
		return cbcFlh;
	}
	public void setCbcFlh(String cbcFlh) {
		this.cbcFlh = cbcFlh;
	}
	public String getCbcSmtVrf() {
		return cbcSmtVrf;
	}
	public void setCbcSmtVrf(String cbcSmtVrf) {
		this.cbcSmtVrf = cbcSmtVrf;
	}
	public String getCbcRsp() {
		return cbcRsp;
	}
	public void setCbcRsp(String cbcRsp) {
		this.cbcRsp = cbcRsp;
	}
	public String getCbcSgl() {
		return cbcSgl;
	}
	public void setCbcSgl(String cbcSgl) {
		this.cbcSgl = cbcSgl;
	}
	public String getCbcCgccpf() {
		return cbcCgccpf;
	}
	public void setCbcCgccpf(String cbcCgccpf) {
		this.cbcCgccpf = cbcCgccpf;
	}
	public String getCbcDst() {
		return cbcDst;
	}
	public void setCbcDst(String cbcDst) {
		this.cbcDst = cbcDst;
	}
	public String getCbcDstIn() {
		return cbcDstIn;
	}
	public void setCbcDstIn(String cbcDstIn) {
		this.cbcDstIn = cbcDstIn;
	}
	public String getCbcVersao() {
		return cbcVersao;
	}
	public void setCbcVersao(String cbcVersao) {
		this.cbcVersao = cbcVersao;
	}
	public String getCbcHdrIndicadorLinha() {
		return cbcHdrIndicadorLinha;
	}
	public String getCbcHdrIndicadorInicio() {
		return cbcHdrIndicadorInicio;
	}	
}
