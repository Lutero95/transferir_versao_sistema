package br.gov.al.maceio.sishosp.hosp.control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.dao.BpaDAO;
import br.gov.al.maceio.sishosp.hosp.enums.CamposBpaIndividualizados;
import br.gov.al.maceio.sishosp.hosp.model.BpaCabecalhoBean;
import br.gov.al.maceio.sishosp.hosp.model.BpaIndividualizadoBean;

@ManagedBean
@ViewScoped
public class BpaController {
	
	private BpaDAO bpaDAO = new BpaDAO();
	private BpaCabecalhoBean bpaCabecalho = new BpaCabecalhoBean();
	private List<BpaIndividualizadoBean> listaDeBpaIndividualizado;
	private List<String> linhasLayoutImportacao;
	private final String PASTA_RAIZ = "C:\\Users\\Public\\Documents\\";
	
	public BpaController() {
		this.bpaDAO = new BpaDAO();
		this.bpaCabecalho = new BpaCabecalhoBean();
		this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		this.linhasLayoutImportacao = new ArrayList<String>();
		//teste
		
		bpaCabecalho.setCbcMvm("202002");
		bpaCabecalho.setCbcLin("001227");
		bpaCabecalho.setCbcFlh("000003");
		bpaCabecalho.setCbcSmtVrf("1328"); //CONTROLE DE DOMINIO
		bpaCabecalho.setCbcRsp("Secretaria Municipal de Saude ");
		bpaCabecalho.setCbcSgl("SMS   ");
		bpaCabecalho.setCbcCgccpf("12248522000196");
		bpaCabecalho.setCbcDst("Secretaria estadual de Saude            ");
		bpaCabecalho.setCbcDstIn("M");
		bpaCabecalho.setCbcVersao("D02.89    ");
	}

	public void gerarLayoutBpaImportacao() throws ProjetoException {
		this.linhasLayoutImportacao.add(bpaCabecalho.toString());
		buscaBpasIndividualizadosDoProcedimento("02");
		adicionaCaracteresEmCamposOndeTamanhoNaoEhValido();
		adicionarLinhasBpaIndividualizado();
		String extensao = bpaDAO.buscaExtencaoArquivoPeloMesAtual();
		Path file = Paths.get(PASTA_RAIZ+"BPA_layout_Importacao"+extensao);
		try {
			Files.write(file, this.linhasLayoutImportacao, StandardCharsets.UTF_8).getFileSystem();
		} catch (IOException ioe) {
			JSFUtil.adicionarMensagemErro(ioe.getMessage(), "Erro");
			ioe.printStackTrace();
		}
	}
	
	public void buscaBpasIndividualizadosDoProcedimento(String codigoInstrumentoRegistro) throws ProjetoException {
		this.listaDeBpaIndividualizado = bpaDAO.carregarParametro(codigoInstrumentoRegistro);
	}
	
	public void adicionaCaracteresEmCamposOndeTamanhoNaoEhValido() {
		List<BpaIndividualizadoBean> listaBpaIndividualizadoAux = new ArrayList<BpaIndividualizadoBean>();
		listaBpaIndividualizadoAux.addAll(this.listaDeBpaIndividualizado);
		
		this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		
		for (BpaIndividualizadoBean individualizado : listaBpaIndividualizadoAux) {
			individualizado.setPrdCnes(CamposBpaIndividualizados.PRD_CNES.preencheCaracteresRestantes(individualizado.getPrdCnes()));
			individualizado.setPrdCmp(CamposBpaIndividualizados.PRD_CMP.preencheCaracteresRestantes(individualizado.getPrdCmp()));
			individualizado.setPrdCnsmed(CamposBpaIndividualizados.PRD_CNSMED.preencheCaracteresRestantes(individualizado.getPrdCnsmed()));
			individualizado.setPrdCbo(CamposBpaIndividualizados.PRD_CBO.preencheCaracteresRestantes(individualizado.getPrdCbo()));
			individualizado.setPrdDtaten(CamposBpaIndividualizados.PRD_DTATEN.preencheCaracteresRestantes(individualizado.getPrdDtaten()));
			individualizado.setPrdFlh(CamposBpaIndividualizados.PRD_FLH.preencheCaracteresRestantes(individualizado.getPrdFlh()));
			individualizado.setPrdSeq(CamposBpaIndividualizados.PRD_SEQ.preencheCaracteresRestantes(individualizado.getPrdSeq()));
			individualizado.setPrdPa(CamposBpaIndividualizados.PRD_PA.preencheCaracteresRestantes(individualizado.getPrdPa()));
			individualizado.setPrdCnspac(CamposBpaIndividualizados.PRD_CNSPAC.preencheCaracteresRestantes(individualizado.getPrdCnspac()));
			individualizado.setPrdSexo(CamposBpaIndividualizados.PRD_SEXO.preencheCaracteresRestantes(individualizado.getPrdSexo()));
			individualizado.setPrdIbge(CamposBpaIndividualizados.PRD_IBGE.preencheCaracteresRestantes(individualizado.getPrdIbge()));
			individualizado.setPrdCid(CamposBpaIndividualizados.PRD_CID.preencheCaracteresRestantes(individualizado.getPrdCid()));
			individualizado.setPrdIdade(CamposBpaIndividualizados.PRD_IDADE.preencheCaracteresRestantes(individualizado.getPrdIdade()));
			individualizado.setPrdQt(CamposBpaIndividualizados.PRD_QT.preencheCaracteresRestantes(individualizado.getPrdQt()));
			individualizado.setPrdCaten(CamposBpaIndividualizados.PRD_CATEN.preencheCaracteresRestantes(individualizado.getPrdCaten()));
			individualizado.setPrdNaut(CamposBpaIndividualizados.PRD_NAUT.preencheCaracteresRestantes(individualizado.getPrdNaut()));
			individualizado.setPrdOrg(CamposBpaIndividualizados.PRD_ORG.preencheCaracteresRestantes(individualizado.getPrdOrg()));
			individualizado.setPrdNmpac(CamposBpaIndividualizados.PRD_NMPAC.preencheCaracteresRestantes(individualizado.getPrdNmpac()));
			individualizado.setPrdDtnasc(CamposBpaIndividualizados.PRD_DTNASC.preencheCaracteresRestantes(individualizado.getPrdDtnasc()));
			individualizado.setPrdRaca(CamposBpaIndividualizados.PRD_RACA.preencheCaracteresRestantes(individualizado.getPrdRaca()));
			individualizado.setPrdEtnia(CamposBpaIndividualizados.PRD_ETNIA.preencheCaracteresRestantes(individualizado.getPrdEtnia()));
			individualizado.setPrdNac(CamposBpaIndividualizados.PRD_NAC.preencheCaracteresRestantes(individualizado.getPrdNac()));
			individualizado.setPrdSrv(CamposBpaIndividualizados.PRD_SRV.preencheCaracteresRestantes(individualizado.getPrdSrv()));
			individualizado.setPrdClf(CamposBpaIndividualizados.PRD_CLF.preencheCaracteresRestantes(individualizado.getPrdClf()));
			individualizado.setPrdEquipeSeq(CamposBpaIndividualizados.PRD_EQUIPE_SEQ.preencheCaracteresRestantes(individualizado.getPrdEquipeSeq()));
			individualizado.setPrdEquipeArea(CamposBpaIndividualizados.PRD_EQUIPE_AREA.preencheCaracteresRestantes(individualizado.getPrdEquipeArea()));
			individualizado.setPrdCnpj(CamposBpaIndividualizados.PRD_CNPJ.preencheCaracteresRestantes(individualizado.getPrdCnpj()));
			individualizado.setPrdCepPcnte(CamposBpaIndividualizados.PRD_CEP_PCNTE.preencheCaracteresRestantes(individualizado.getPrdCepPcnte()));
			individualizado.setPrdLogradPcnte(CamposBpaIndividualizados.PRD_LOGRAD_PCNTE.preencheCaracteresRestantes(individualizado.getPrdLogradPcnte()));
			individualizado.setPrdEndPcnte(CamposBpaIndividualizados.PRD_END_PCNTE.preencheCaracteresRestantes(individualizado.getPrdEndPcnte()));
			individualizado.setPrdComplPcnte(CamposBpaIndividualizados.PRD_COMPL_PCNTE.preencheCaracteresRestantes(individualizado.getPrdComplPcnte()));
			individualizado.setPrdNumPcnte(CamposBpaIndividualizados.PRD_NUM_PCNTE.preencheCaracteresRestantes(individualizado.getPrdNumPcnte()));
			individualizado.setPrdBairroPcnte(CamposBpaIndividualizados.PRD_BAIRRO_PCNTE.preencheCaracteresRestantes(individualizado.getPrdBairroPcnte()));
			individualizado.setPrdDDtelPcnte(CamposBpaIndividualizados.PRD_DDTEL_PCNTE.preencheCaracteresRestantes(individualizado.getPrdDDtelPcnte()));
			individualizado.setPrdEmailPcnte(CamposBpaIndividualizados.PRD_EMAIL_PCNTE.preencheCaracteresRestantes(individualizado.getPrdEmailPcnte()));
			individualizado.setPrdIne(CamposBpaIndividualizados.PRD_INE.preencheCaracteresRestantes(individualizado.getPrdIne()));
			
			this.listaDeBpaIndividualizado.add(individualizado);
		}		
		
	}
	
	public void adicionarLinhasBpaIndividualizado() {
		for (BpaIndividualizadoBean bpaIndividualizado : this.listaDeBpaIndividualizado) {
			this.linhasLayoutImportacao.add(bpaIndividualizado.toString());
		}
	}
	
	public BpaCabecalhoBean getBpaCabecalho() {
		return bpaCabecalho;
	}

	public void setBpaCabecalho(BpaCabecalhoBean bpaCabecalho) {
		this.bpaCabecalho = bpaCabecalho;
	}

	public List<BpaIndividualizadoBean> getListaDeBpaIndividualizado() {
		return listaDeBpaIndividualizado;
	}

	public void setListaDeBpaIndividualizado(List<BpaIndividualizadoBean> listaDeBpaIndividualizado) {
		this.listaDeBpaIndividualizado = listaDeBpaIndividualizado;
	}	
	
}
