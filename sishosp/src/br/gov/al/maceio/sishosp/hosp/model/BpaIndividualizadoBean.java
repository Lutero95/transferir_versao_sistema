package br.gov.al.maceio.sishosp.hosp.model;

import java.util.ArrayList;
import java.util.List;

public class BpaIndividualizadoBean {

	private final String prdIdent = "03";
	private String prdCnes;
	private String prdCmp;
	private String prdCnsmed;
	private String prdCbo;
	private String prdDtaten;
	private String prdFlh;
	private String prdSeq;
	private String prdPa;
	private String prdCnspac;
	private String prdSexo;
	private String prdIbge;
	private String prdCid;
	private String prdIdade;
	private String prdQt;
	private String prdCaten;
	private String prdNaut;
	private final String prdOrg = "BPA";
	private String prdNmpac;
	private String prdDtnasc;
	private String prdRaca;
	private String prdEtnia;
	private String prdNac;
	private String prdSrv;
	private String prdClf;
	private String prdEquipeSeq;
	private String prdEquipeArea;
	private String prdCnpj;
	private String prdCepPcnte;
	private String prdLogradPcnte;
	private String prdEndPcnte;
	private String prdComplPcnte;
	private String prdNumPcnte;
	private String prdBairroPcnte;
	private String prdDDtelPcnte;
	private String prdEmailPcnte;
	private String prdIne;
	private List<String> listaInconsistencias ;

	public BpaIndividualizadoBean() {
		listaInconsistencias = new ArrayList<>();
	}

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
		return prdIdent + prdCnes + prdCmp + prdCnsmed + prdCbo + prdDtaten + prdFlh + prdSeq + prdPa
				+ prdCnspac + prdSexo + prdIbge + prdCid + prdIdade + prdQt
				+ prdCaten + prdNaut + prdOrg + prdNmpac + prdDtnasc + prdRaca + prdEtnia + prdNac
				+ prdSrv + prdClf + prdEquipeSeq + prdEquipeArea + prdCnpj + prdCepPcnte 
				+ prdLogradPcnte + prdEndPcnte + prdComplPcnte
				+ prdNumPcnte + prdBairroPcnte + prdDDtelPcnte + prdEmailPcnte + prdIne;
	}
	
	public String getPrdCnes() {
		return prdCnes;
	}
	public void setPrdCnes(String prdCnes) {
		this.prdCnes = prdCnes;
	}
	public String getPrdCmp() {
		return prdCmp;
	}
	public void setPrdCmp(String prdCmp) {
		this.prdCmp = prdCmp;
	}
	public String getPrdCnsmed() {
		return prdCnsmed;
	}
	public void setPrdCnsmed(String prdCnsmed) {
		this.prdCnsmed = prdCnsmed;
	}
	public String getPrdCbo() {
		return prdCbo;
	}
	public void setPrdCbo(String prdCbo) {
		this.prdCbo = prdCbo;
	}
	public String getPrdDtaten() {
		return prdDtaten;
	}
	public void setPrdDtaten(String prdDtaten) {
		this.prdDtaten = prdDtaten;
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
	public String getPrdCnspac() {
		return prdCnspac;
	}
	public void setPrdCnspac(String prdCnspac) {
		this.prdCnspac = prdCnspac;
	}
	public String getPrdSexo() {
		return prdSexo;
	}
	public void setPrdSexo(String prdSexo) {
		this.prdSexo = prdSexo;
	}
	public String getPrdIbge() {
		return prdIbge;
	}
	public void setPrdIbge(String prdIbge) {
		this.prdIbge = prdIbge;
	}
	public String getPrdCid() {
		return prdCid;
	}
	public void setPrdCid(String prdCid) {
		this.prdCid = prdCid;
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
	public String getPrdCaten() {
		return prdCaten;
	}
	public void setPrdCaten(String prdCaten) {
		this.prdCaten = prdCaten;
	}
	public String getPrdNaut() {
		return prdNaut;
	}
	public void setPrdNaut(String prdNaut) {
		this.prdNaut = prdNaut;
	}
	public String getPrdOrg() {
		return prdOrg;
	}
	public String getPrdNmpac() {
		return prdNmpac;
	}
	public void setPrdNmpac(String prdNmpac) {
		this.prdNmpac = prdNmpac;
	}
	public String getPrdDtnasc() {
		return prdDtnasc;
	}
	public void setPrdDtnasc(String prdDtnasc) {
		this.prdDtnasc = prdDtnasc;
	}
	public String getPrdRaca() {
		return prdRaca;
	}
	public void setPrdRaca(String prdRaca) {
		this.prdRaca = prdRaca;
	}
	public String getPrdEtnia() {
		return prdEtnia;
	}
	public void setPrdEtnia(String prdEtnia) {
		this.prdEtnia = prdEtnia;
	}
	public String getPrdNac() {
		return prdNac;
	}
	public void setPrdNac(String prdNac) {
		this.prdNac = prdNac;
	}
	public String getPrdSrv() {
		return prdSrv;
	}
	public void setPrdSrv(String prdSrv) {
		this.prdSrv = prdSrv;
	}
	public String getPrdClf() {
		return prdClf;
	}
	public void setPrdClf(String prdClf) {
		this.prdClf = prdClf;
	}
	public String getPrdEquipeSeq() {
		return prdEquipeSeq;
	}
	public void setPrdEquipeSeq(String prdEquipeSeq) {
		this.prdEquipeSeq = prdEquipeSeq;
	}
	public String getPrdEquipeArea() {
		return prdEquipeArea;
	}
	public void setPrdEquipeArea(String prdEquipeArea) {
		this.prdEquipeArea = prdEquipeArea;
	}
	public String getPrdCnpj() {
		return prdCnpj;
	}
	public void setPrdCnpj(String prdCnpj) {
		this.prdCnpj = prdCnpj;
	}
	public String getPrdCepPcnte() {
		return prdCepPcnte;
	}
	public void setPrdCepPcnte(String prdCepPcnte) {
		this.prdCepPcnte = prdCepPcnte;
	}
	public String getPrdLogradPcnte() {
		return prdLogradPcnte;
	}
	public void setPrdLogradPcnte(String prdLogradPcnte) {
		this.prdLogradPcnte = prdLogradPcnte;
	}
	public String getPrdEndPcnte() {
		return prdEndPcnte;
	}
	public void setPrdEndPcnte(String prdEndPcnte) {
		this.prdEndPcnte = prdEndPcnte;
	}
	public String getPrdComplPcnte() {
		return prdComplPcnte;
	}
	public void setPrdComplPcnte(String prdComplPcnte) {
		this.prdComplPcnte = prdComplPcnte;
	}
	public String getPrdNumPcnte() {
		return prdNumPcnte;
	}
	public void setPrdNumPcnte(String prdNumPcnte) {
		this.prdNumPcnte = prdNumPcnte;
	}
	public String getPrdBairroPcnte() {
		return prdBairroPcnte;
	}
	public void setPrdBairroPcnte(String prdBairroPcnte) {
		this.prdBairroPcnte = prdBairroPcnte;
	}
	public String getPrdDDtelPcnte() {
		return prdDDtelPcnte;
	}
	public void setPrdDDtelPcnte(String prdDDtelPcnte) {
		this.prdDDtelPcnte = prdDDtelPcnte;
	}
	public String getPrdEmailPcnte() {
		return prdEmailPcnte;
	}
	public void setPrdEmailPcnte(String prdEmailPcnte) {
		this.prdEmailPcnte = prdEmailPcnte;
	}
	public String getPrdIne() {
		return prdIne;
	}
	public void setPrdIne(String prdIne) {
		this.prdIne = prdIne;
	}

	public String getPrdIdent() {
		return prdIdent;
	}

	public List<String> getListaInconsistencias() {
		return listaInconsistencias;
	}

	public void setListaInconsistencias(List<String> listaInconsistencias) {
		this.listaInconsistencias = listaInconsistencias;
	}
}
