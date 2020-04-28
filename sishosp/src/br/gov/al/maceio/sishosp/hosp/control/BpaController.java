package br.gov.al.maceio.sishosp.hosp.control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	
	private static final int MES_MINIMO_COMPETENCIA_PARA_INE = 8;
	private static final int ANO_MINIMO_COMPETENCIA_PARA_INE = 2015;
	private static final int MAXIMO_DE_REGISTROS_FOLHA_INDIVIDUALIZADO = 99;
	private static final int MAXIMO_DE_REGISTROS_LINHA_INDIVIDUALIZADO = 99;
	
	private BpaDAO bpaDAO = new BpaDAO();
	private BpaCabecalhoBean bpaCabecalho = new BpaCabecalhoBean();
	private List<BpaIndividualizadoBean> listaDeBpaIndividualizado;
	private List<String> linhasLayoutImportacao;
	private final String PASTA_RAIZ = "C:\\Users\\Public\\Documents\\";
	
	private Date dataInicioAtendimento;
	private Date dataFimAtendimento;
	private String competencia;
	private List<String> listaCompetencias;
	
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
	
	public void listarCompetencias() {
		this.listaCompetencias = bpaDAO.listarCompetencias();
		formataCompetenciasParaExibicaoNaTela();
	}
	
	private void formataCompetenciasParaExibicaoNaTela() {
		List<String> listaCompetenciasAux = new ArrayList<String>();
		listaCompetenciasAux.addAll(this.listaCompetencias);
		this.listaCompetencias.clear();
		for (String competencia : listaCompetenciasAux) {
			String diaCompetencia = competencia.substring(4, 6);
			String anoCompetencia = competencia.substring(0, 4);
			String competenciaFormatada = diaCompetencia+"/"+anoCompetencia;
			this.listaCompetencias.add(competenciaFormatada);
		}
	}

	public void gerarLayoutBpaImportacao() throws ProjetoException {

		try {
			this.linhasLayoutImportacao.add(bpaCabecalho.toString());
			setaDataInicioIhFimAtendimentoParaIndividualizado(this.competencia);
			this.competencia = formataCompetenciaParaBanco();
			buscaBpasIndividualizadosDoProcedimento(this.dataInicioAtendimento, this.dataFimAtendimento, this.competencia);
			geraNumeroDaFolhaIndividualizado();
			geraNumeroDaLinhaDaFolhaIndividualizado();
			adicionaCaracteresEmCamposOndeTamanhoNaoEhValido();
			adicionarLinhasBpaIndividualizado();
			String extensao = bpaDAO.buscaExtencaoArquivoPeloMesAtual();
			String caminhoIhArquivo = PASTA_RAIZ+"BPA_layout_Importacao"+extensao; 
			Path file = Paths.get(caminhoIhArquivo);
			Files.write(file, this.linhasLayoutImportacao, StandardCharsets.UTF_8).getFileSystem();
			JSFUtil.adicionarMensagemSucesso("Layout Gerado com successo na pasta: "+caminhoIhArquivo, "");
		} catch (IOException ioe) {
			JSFUtil.adicionarMensagemErro(ioe.getMessage(), "Erro");
			ioe.printStackTrace();
		} catch (ProjetoException pe) {
			JSFUtil.adicionarMensagemErro(pe.getMessage(), "");
		}
	}
	
	private void setaDataInicioIhFimAtendimentoParaIndividualizado(String competencia) {
		Integer ano = Integer.valueOf(competencia.substring(3, 7));
		Integer mes = Integer.valueOf(competencia.substring(0, 2));
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes, 1);
		
		Integer primeiraDataMes = calendar.getMinimum(Calendar.DAY_OF_MONTH);
		Integer ultimaDataMes = calendar.getMaximum(Calendar.DAY_OF_MONTH);
		
		String dataInicial = ano+"/"+mes+"/"+primeiraDataMes;
		String dataFinal = ano+"/"+mes+"/"+ultimaDataMes;
		
		try {
			this.dataInicioAtendimento = new SimpleDateFormat("yyyy/MM/dd").parse(dataInicial);
			this.dataFimAtendimento = new SimpleDateFormat("yyyy/MM/dd").parse(dataFinal);
		} catch (ParseException e) {
			JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
			e.printStackTrace();
		}
	}
	
	private String formataCompetenciaParaBanco() {
		String diaCompetencia = competencia.substring(0, 2);
		String anoCompetencia = competencia.substring(3, 7);
		String competenciaFormatada = anoCompetencia+diaCompetencia;
		return competenciaFormatada; 
	}
	
	public void buscaBpasIndividualizadosDoProcedimento(Date dataInicio, Date dataFim, String competencia) throws ProjetoException {
		this.listaDeBpaIndividualizado = bpaDAO.carregarParametro(dataInicio, dataFim, competencia);
	}
	
	public void geraNumeroDaFolhaIndividualizado() {
		List<BpaIndividualizadoBean> listaBpaIndividualizadoAuxiliar = new ArrayList<BpaIndividualizadoBean>();
		listaBpaIndividualizadoAuxiliar.addAll(this.listaDeBpaIndividualizado);
				
		List<String> listaCnsDosMedicos = retornaUnicamenteCnsDeCadaMedico();
		for (String cnsMedico : listaCnsDosMedicos) {
			Integer quantidadeRegistrosPorFolha = 0;
			Integer numeroFolhaBpaIndividualizado = 1;
			for(int i = 0; i < this.listaDeBpaIndividualizado.size(); i++) {
				if(this.listaDeBpaIndividualizado.get(i).getPrdCnsmed().equals(cnsMedico)) {
					if(quantidadeRegistrosPorFolha < MAXIMO_DE_REGISTROS_FOLHA_INDIVIDUALIZADO) { 
						this.listaDeBpaIndividualizado.get(i).setPrdFlh(numeroFolhaBpaIndividualizado.toString());
						quantidadeRegistrosPorFolha++;
					}
					else {
						numeroFolhaBpaIndividualizado++;
						this.listaDeBpaIndividualizado.get(i).setPrdFlh(numeroFolhaBpaIndividualizado.toString());
						quantidadeRegistrosPorFolha = 1;
					}
				}
			}
		}
	}
	
	public void geraNumeroDaLinhaDaFolhaIndividualizado() {
		List<BpaIndividualizadoBean> listaBpaIndividualizadoAuxiliar = new ArrayList<BpaIndividualizadoBean>();
		listaBpaIndividualizadoAuxiliar.addAll(this.listaDeBpaIndividualizado);

		String cnsMedicoAuxiliador = this.listaDeBpaIndividualizado.get(0).getPrdCnsmed();
		String numeroFolhaAuxiliador = this.listaDeBpaIndividualizado.get(0).getPrdFlh();
		Integer numeroLinha = 0;
		for (int i = 0; i < this.listaDeBpaIndividualizado.size(); i++) {
			if (this.listaDeBpaIndividualizado.get(i).getPrdCnsmed().equals(cnsMedicoAuxiliador)
					&& this.listaDeBpaIndividualizado.get(i).getPrdFlh().equals(numeroFolhaAuxiliador)) {
				numeroLinha++;
				this.listaDeBpaIndividualizado.get(i).setPrdSeq(numeroLinha.toString());
			}
			else {
				cnsMedicoAuxiliador = this.listaDeBpaIndividualizado.get(i).getPrdCnsmed();
				numeroFolhaAuxiliador = this.listaDeBpaIndividualizado.get(i).getPrdFlh();
				numeroLinha = 1;
				this.listaDeBpaIndividualizado.get(i).setPrdSeq(numeroLinha.toString());
			}
		}
	}

	private List<String> retornaUnicamenteCnsDeCadaMedico() {
		List<String> listaCnsDosMedicos = new ArrayList<String>(); 
		for(BpaIndividualizadoBean individualizado : this.listaDeBpaIndividualizado) {
			if(!listaCnsDosMedicos.contains(individualizado.getPrdCnsmed()))
				listaCnsDosMedicos.add(individualizado.getPrdCnsmed());
		}
		return listaCnsDosMedicos;
	}
	
	public void adicionaCaracteresEmCamposOndeTamanhoNaoEhValido() throws ProjetoException {
		List<BpaIndividualizadoBean> listaBpaIndividualizadoAuxiliar = new ArrayList<BpaIndividualizadoBean>();
		listaBpaIndividualizadoAuxiliar.addAll(this.listaDeBpaIndividualizado);
		
		this.listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		
		for (BpaIndividualizadoBean individualizado : listaBpaIndividualizadoAuxiliar) {
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
			individualizado.setPrdIne(validaCompetenciaParaPreencherINE(individualizado.getPrdIne(), individualizado.getPrdCmp()));
			
			this.listaDeBpaIndividualizado.add(individualizado);
		}		
		
	}
	
	public String validaCompetenciaParaPreencherINE(String ine, String competencia) throws ProjetoException {
		Integer anoCompetencia = Integer.valueOf(competencia.substring(0, 4));
		Integer mesCompetencia = Integer.valueOf(competencia.substring(4, 6));
		if (anoCompetencia < ANO_MINIMO_COMPETENCIA_PARA_INE || (anoCompetencia == ANO_MINIMO_COMPETENCIA_PARA_INE
				&& mesCompetencia < MES_MINIMO_COMPETENCIA_PARA_INE)) {
			Integer tamanho = 10;
			ine = new String();
			while (ine.length() < tamanho) {
				ine += " ";
			}
			return ine;
		} else
			return CamposBpaIndividualizados.PRD_INE.preencheCaracteresRestantes(ine);
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

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public List<String> getListaCompetencias() {
		return listaCompetencias;
	}

	public void setListaCompetencias(List<String> listaCompetencias) {
		this.listaCompetencias = listaCompetencias;
	}	
	
}
