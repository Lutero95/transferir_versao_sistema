package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;


import br.gov.al.maceio.sishosp.financeiro.dao.BuscaDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.FornecedorDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.PortadorDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.TesourariaDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.TituloPagarDao;
import br.gov.al.maceio.sishosp.financeiro.dao.TituloReceberDao;
import br.gov.al.maceio.sishosp.financeiro.model.BaixaPagar;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.financeiro.model.BuscaBeanPagar;
import br.gov.al.maceio.sishosp.financeiro.model.CaixaDiarioBean;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ChequeEmitidoBean;
import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.ImpostoBean;
import br.gov.al.maceio.sishosp.financeiro.model.PagamentoBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;
import br.gov.al.maceio.sishosp.financeiro.model.TipoDocumentoBean;
import br.gov.al.maceio.sishosp.financeiro.model.TipoImposto;
import br.gov.al.maceio.sishosp.financeiro.model.TituloPagarBean;
import br.gov.al.maceio.sishosp.financeiro.model.TituloReceberBean;
import br.gov.al.maceio.sishosp.financeiro.model.TotalizadorBeanPagar;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;

@ManagedBean
@ViewScoped
public class TituloPagarController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Date periodoInicial, periodoFinal;
	private TotalizadorBeanPagar totBuscaPagar;
	private TituloPagarBean tituloPagarBean;
	private ArrayList<TituloPagarBean> lstTitPagar;
	private List<FornecedorBean> lstFornecedor;
	private List<FornecedorBean> lstFornecedorAlt;
	private List<BancoBean> lstBancos;
	private ArrayList<CentroCustoBean> lstccusto;
	private ArrayList<TipoDocumentoBean> lsttipdoc;
	private ArrayList<DespesaBean> lstDespesa;
	private ArrayList<ImpostoBean> lstImpostos;
	private List<ImpostoBean> lstImpostosAlt;
	private ArrayList<TituloPagarBean> lstTituloPagar;
	private Integer codFornecedor;
	private TituloPagarBean rowBean;
	private BaixaPagar rowBeanBaixa;	
	private List<TipoImposto> tipoImposto;
	private ImpostoBean rowBeanImposto;
	private ImpostoBean impostoBean;
	private List<TituloPagarBean> listaAbertos;
	private List<BaixaPagar> lstBaixa;
	private String tipoPagamento;
	private int tipo;
	private FornecedorBean fornecedorBean;
	private PortadorBean portadorBean;
	private List<PortadorBean> lstPortador;
	private List<PortadorBean> lstPortadorAlt;
	private BuscaBeanPagar buscaPagar;
	
	private Integer idSelecionado = 0;
	private Integer parcelas = 0;
	private Integer DocumentoParcelas = 0;
	private TipoDocumentoBean tipoDoc; 
	private List<PagamentoBean> lstDesistencia;
	private TituloReceberBean funcBean;
 	private PagamentoBean pagamentoDesi;
 	
	//CONSTANTES
	private static final String ENDERECO_CADASTRO = "cadastropagar?faces-redirect=true";
	private static final String ENDERECO_TIPO = "&amp;tipo=";
	private static final String ENDERECO_ID = "&amp;id=";
	private static final String CABECALHO_INCLUSAO = "Inclus�o de T�tulo a Pagar";
	private static final String CABECALHO_ALTERACAO = "Altera��o de T�tulo a Pagar";
	
	public TituloPagarController() throws ParseException {
		buscaPagar = new BuscaBeanPagar();
		buscaPagar.setOrdenacao("C");
		buscaPagar.setSituacao("T");
		Calendar calendar = Calendar.getInstance();
			//calendar.setTime(data);
			 Format format = new SimpleDateFormat("dd/MM/yyyy");
			    
			int dia = 1;
			int mes = 1;
			int ano = calendar.get(Calendar.YEAR);
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date)formatter.parse(String.valueOf(dia)+"/"+String.valueOf(mes)+"/"+ String.valueOf(ano));
		buscaPagar.setPeriodoinicial(date);
		buscaPagar.setPeriodofinal(new java.util.Date(System.currentTimeMillis()));
		lstBancos = new ArrayList<BancoBean>();
		lstTitPagar = new ArrayList<TituloPagarBean>();
		lstTitPagar = null;
		tituloPagarBean = new TituloPagarBean();
		tituloPagarBean.setParcela("UN");
		rowBean = new TituloPagarBean();
		rowBeanBaixa = new BaixaPagar();
		rowBeanImposto = new ImpostoBean();
		fornecedorBean = new FornecedorBean();
		portadorBean = new PortadorBean();
		lstFornecedor = new ArrayList<>();
		lstFornecedorAlt = new ArrayList<>();
		lstImpostos = new ArrayList<>();
		lstImpostosAlt = new ArrayList<>();
		lstTituloPagar = new ArrayList<>();
		lstPortadorAlt = new ArrayList<>();
		tipoImposto = new ArrayList<>();
		impostoBean = new ImpostoBean();
		lstFornecedor = null;
		lstFornecedorAlt = null;
		lstPortadorAlt = null;
		listaAbertos = new ArrayList<TituloPagarBean>();
		carregarImposto();
		lstPortador = null;
		listaAbertos = null;
		lstBaixa = new ArrayList<>();
		tituloPagarBean.setParcela("UN");
		tipoDoc = new TipoDocumentoBean();
		lstDesistencia = new ArrayList<PagamentoBean>();
		funcBean = new TituloReceberBean();
		pagamentoDesi = new PagamentoBean();
	}
	
	public String redirectInsert() {
		return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
	}

	
	public void limparDados(){
		totBuscaPagar = new TotalizadorBeanPagar();
		tituloPagarBean = new TituloPagarBean();
		tituloPagarBean.setParcela("UN");
		portadorBean = new PortadorBean();
		fornecedorBean = new FornecedorBean();
		listaAbertos = new ArrayList<TituloPagarBean>();
		lstBaixa = new ArrayList<>();
		lstTituloPagar = new ArrayList<>();
		lstImpostos = new ArrayList<>();
		
	}
	

	
	public void limpaBusca() throws ParseException {

        buscaPagar = new BuscaBeanPagar();
        buscaPagar.setOrdenacao("C");
        buscaPagar.setSituacao("T");
        Calendar calendar = Calendar.getInstance();
		//calendar.setTime(data);
		 Format format = new SimpleDateFormat("dd/MM/yyyy");
		    
		int dia = 1;
		int mes = 1;
		int ano = calendar.get(Calendar.YEAR);
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = (Date)formatter.parse(String.valueOf(dia)+"/"+String.valueOf(mes)+"/"+ String.valueOf(ano));
	buscaPagar.setPeriodoinicial(date);
        buscaPagar.setPeriodofinal(new java.util.Date(System
                .currentTimeMillis()));
        lstTitPagar = null;
    }
	
	public void limparDadosRetAlt(){
		lstImpostosAlt = new ArrayList<>();
		lstImpostosAlt = null;
		lstFornecedor = new ArrayList<>();
		lstFornecedor = null;
	}
	
	public void busca(){
		lstTitPagar = null;
	}
	
	   /*replicar argemiro*/
			public void replicarDocumentoPagar() throws ProjetoException, ParseException {
				
				SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat fcomp = new SimpleDateFormat("MM/yyyy");
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date d = new Date();
				d = rowBean.getDtvcto();
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(d);
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				//int mes = 0;
				
					
				//c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
				//c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 30);
				//c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
				//c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
				
				
				boolean gravou = false;
				TituloPagarDao pagarDAo = new TituloPagarDao();
	
				
				this.DocumentoParcelas = 0;
				if(rowBean.getValor() > 0) {
					if(this.parcelas > 0){
						if(this.parcelas == 1){
							//mes = 0;
							rowBean.setParcela("UN");
						}
						
						for(int i = 0; i < this.parcelas; i++){
							this.DocumentoParcelas ++;

							Integer dia = calendar.getActualMaximum(c.DAY_OF_MONTH);
							Integer mes = c.get(Calendar.MONTH);
							Integer ano = c.get(Calendar.YEAR);
												
							if (mes==0 || mes==2 || mes==4 || mes==6 || mes==7 || mes==9 || mes==11){
							dia = 31;
							}
							else if (mes==3 || mes==5 || mes==8 || mes==10){
							dia = 30;
							}
							else if (mes==1){
							if(ano % 400 == 0){
							dia = 29;
							} else if((ano % 4 == 0) && (ano % 100 != 0)){
							dia = 29;
							} else{
							dia = 28;
							}
							}					
												
							c.set(ano, mes, dia);
							String DataString = f.format(c.getTime());
							String Dtcompete = fcomp.format(c.getTime());
							java.sql.Date data = new java.sql.Date(f.parse(DataString).getTime());
							tituloPagarBean.setDtvcto(data); 
						
							
							rowBean.setDuplicata(this.DocumentoParcelas +" / "+ this.parcelas);
							rowBean.setDtvcto(data);
							rowBean.setDtcompete(Dtcompete);
							
							
							//String par = String.valueOf(this.parcelas);
							String par = String.valueOf(this.DocumentoParcelas);
							rowBean.setParcela(par);
					
							c.set(ano, mes, 1);
							c.add(Calendar.MONTH, 1);
							gravou = pagarDAo.replicarPagar2(rowBean, this.lstImpostos);
						}
						
						
					}else{
						JSFUtil.adicionarMensagemErro("Parcelas deve ser maior que 0.","Aten��o");
					}
					
					
				} else {
					JSFUtil.adicionarMensagemErro("O valor deve ser maior que 0.","Aten��o");
				}
				
				
				if (valorTotal() <0){
					JSFUtil.adicionarMensagemErro("O valor total deve ser maior que 0.","Aten��o");
				
				}
				else
				if(gravou == true) {		
				limparDados();
				rowBean = new TituloPagarBean();
				RequestContext.getCurrentInstance().execute("PF('dlgRepetir').hide();");
				
								
				
				JSFUtil.adicionarMensagemSucesso("Salvo com sucesso!","Sucesso");
				lstTitPagar = null;
				
				} else {
					
					JSFUtil.adicionarMensagemErro("Ocorreu um erro ao gravar.","Aten��o");
				}
			}

	public void verificarParcelas() throws ProjetoException {
		if (tituloPagarBean.getParcela().equals("UN") || tituloPagarBean.getParcela().equals("1")){
			salvarDocumentoPagarUnico();
		}
		else{
			RequestContext.getCurrentInstance().execute(
					"PF('simounao').show();");
		}
	}
			
    
	public void salvarDocumentoPagar() throws ProjetoException, ParseException {
		
		boolean gravou = false;
		TituloPagarDao pagarDAo = new TituloPagarDao();
				
		this.tituloPagarBean.setForn(this.fornecedorBean);
		this.tituloPagarBean.setPortador(this.portadorBean);

		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat fcomp = new SimpleDateFormat("MM/yyyy");	
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date d = new Date();
		d = tituloPagarBean.getDtvcto();
		
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(d);
		
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		
		this.funcBean.getCliente().getNome();
		this.pagamentoDesi.getValor();
		this.pagamentoDesi.getDtCadastro();
		int parcelas2=0;
		if (tituloPagarBean.getParcela().equals("UN")){
			parcelas2 = 1;
		}
		parcelas2 = Integer.parseInt(tituloPagarBean.getParcela());
		Boolean podeSalvar = true;
				
		
		if(tituloPagarBean.getValor() > 0) {
			for(int i = 1; i <= parcelas2; i++){
				Integer dia = calendar.getActualMaximum(c.DAY_OF_MONTH);
					Integer mes = c.get(Calendar.MONTH);
					int ano = c.get(Calendar.YEAR);
					
					if (mes==0 || mes==2 || mes==4 || mes==6 || mes==7 || mes==9 || mes==11){
					dia = 31;
					}
					else if (mes==3 || mes==5 || mes==8 || mes==10){
					dia = 30;
					}
					else if (mes==1){
						if(ano % 400 == 0){
				           dia = 29;
				        } else if((ano % 4 == 0) && (ano % 100 != 0)){
				            dia = 29;
				        } else{
				            dia = 28;
				        }
				    }					
					
					c.set(ano, mes, dia);
					
					String DataString = f.format(c.getTime());
					String Dtcompete = fcomp.format(c.getTime());
					java.sql.Date data = new java.sql.Date(f.parse(DataString).getTime());
					tituloPagarBean.setDtvcto(data); 
					
			
			if(this.tipoDoc.getDevolucao_venda().equals(true)){
			
				if( this.tituloPagarBean.getValor() <= this.pagamentoDesi.getValor()){

					podeSalvar = true;
					
				}else{
					JSFUtil.adicionarMensagemErro("O valor não pode ser maior que o valor da desistência.","Aten��o");
					podeSalvar = false;
				}
			}else{
				this.pagamentoDesi.setId_desistencia(null);
				podeSalvar = true;
			}
			
			if(podeSalvar){
				this.tituloPagarBean.setParcela(String.valueOf(i));
					gravou = pagarDAo.salvarPagar(this.tituloPagarBean, this.lstImpostos, this.pagamentoDesi.getId_desistencia());
					c.set( ano, mes, 1);
					c.add(Calendar.MONTH, 1);
					RequestContext.getCurrentInstance().execute("PF('simounao').hide();");	
			}
			}
		} else {
			JSFUtil.adicionarMensagemErro("O valor deve ser maior que 0.","Aten��o");
		}
		
		
		 if (valorTotal() <0){
			 JSFUtil.adicionarMensagemErro("O valor total deve ser maior que 0.","Aten��o");
		
		}
		else{
		if(gravou == true) {
			limparDados();
		RequestContext.getCurrentInstance().execute("PF('dlgNovo').hide();");
	

		JSFUtil.adicionarMensagemSucesso("Salvo com sucesso!","Aten��o");
		lstTitPagar = null;
		
		} else {
			

				JSFUtil.adicionarMensagemErro("Ocorreu um erro ao gravar.","Aten��o");
		}
		
	}
	}
	
public void salvarDocumentoPagarUnico() throws ProjetoException {
		
		boolean gravou = false;
		TituloPagarDao pagarDAo = new TituloPagarDao();
		
		this.tituloPagarBean.setForn(this.fornecedorBean);
		this.tituloPagarBean.setPortador(this.portadorBean);
		
		//Date data = new java.util.Date();
		//this.tituloPagarBean.setDtemissao(new java.sql.Date(data.getTime()));
		
		this.funcBean.getCliente().getNome();
		this.pagamentoDesi.getValor();
		this.pagamentoDesi.getDtCadastro();
		
		Boolean podeSalvar = true;
				
		
		if(tituloPagarBean.getValor() > 0) {
			
			

			
			if(podeSalvar){
				
					gravou = pagarDAo.salvarPagar(this.tituloPagarBean, this.lstImpostos, this.pagamentoDesi.getId_desistencia());
		    	
			}
			
		} else {
			JSFUtil.adicionarMensagemErro("O valor deve ser maior que 0.","Aten��o");
		}
		
		
		 if (valorTotal() <0){
			JSFUtil.adicionarMensagemErro("O valor total deve ser maior que 0.","Aten��o");
		
		}
		else{
		if(gravou == true) {
			limparDados();
		RequestContext.getCurrentInstance().execute("PF('dlgNovo').hide();");
		RequestContext.getCurrentInstance().execute("PF('simounao').hide();");
		

		JSFUtil.adicionarMensagemSucesso("Salvo com sucesso!","Aten��o");
		lstTitPagar = null;
		
		} else {
			
			JSFUtil.adicionarMensagemErro("Ocorreu um erro ao gravar.","Aten��o");
		}
		}
	}

	public void excluirDocumentoPagar() throws ProjetoException {
		boolean excluiu = false;
		TituloPagarDao pagarDao = new TituloPagarDao();
		excluiu = pagarDao.excluir(this.rowBean);
		if(excluiu == true) {
			RequestContext.getCurrentInstance().execute("PF('dlgAt').hide();");

			JSFUtil.adicionarMensagemSucesso("Excluído com sucesso!","Aten��o");
			lstTitPagar = null;
		} else {
			
			JSFUtil.adicionarMensagemErro("Falha ao excluir!","Aten��o");
		}

	}
	
	
	   public void editar() throws ProjetoException {
	        TituloPagarDao dao = new TituloPagarDao();
	        
	        
	        	if(rowBean.getValor() > 0) {
	        		if(rowBean.getDespesa().getId() != null && rowBean.getCcusto().getIdccusto() != null){	        		
	        	boolean alterou = dao.editar(this.rowBean, this.lstImpostosAlt);	        	
	        	 if(alterou) {
	 	            
	 	            lstTituloPagar = null;

	 	           JSFUtil.adicionarMensagemSucesso("Editado com sucesso!","Aten��o");
	 	            lstTitPagar = null;
	 	            RequestContext.getCurrentInstance().execute("PF('dlgEditRet').hide();");
	 	        } else {
	 	            JSFUtil.adicionarMensagemErro("Erro ao realizar a alteração!","Aten��o");
	 	        	}
	        	 
	        } else {
	        	JSFUtil.adicionarMensagemErro("Os campos Centro de Custo e Despesa não podem ser vazios.","Aten��o");	        	
	        	}
	        		
	        
	        } else {
	        	JSFUtil.adicionarMensagemErro("O valor não pode ser menor ou igual a 0.","Aten��o");
	        	}	
	        
	        
	    }
	   


	public void buscaFornecedor() throws ProjetoException {

		FornecedorDAO dao = new FornecedorDAO();

		lstFornecedor = (ArrayList<FornecedorBean>) dao.buscaFornecedor(
				this.fornecedorBean.getNome(), this.fornecedorBean.getCnpj());

	}
	
	public void buscaFornecedorAlt() throws ProjetoException {

		FornecedorDAO dao = new FornecedorDAO();

		lstFornecedorAlt = (ArrayList<FornecedorBean>) dao.buscaFornecedor(
				this.rowBean.getForn().getNome(), this.rowBean.getForn().getCnpj());

	}

	public void buscaPortador() throws ProjetoException {

		PortadorDAO dao = new PortadorDAO();

		lstPortador = dao.lstPortadores(this.portadorBean.getDescricao());

	}

	public void buscaPortadorAlt() throws ProjetoException {

		PortadorDAO dao = new PortadorDAO();

		lstPortadorAlt = dao.lstPortadores(this.rowBean.getPortador().getDescricao());

	}
	public ArrayList<TituloPagarBean> lstTituloPagarBean()
			throws ProjetoException {
		  TituloPagarDao tDao = new TituloPagarDao();

	        if (lstTitPagar == null) {
	        	
	        	lstTitPagar = tDao.todosFinanceiro(buscaPagar);
	        	totBuscaPagar = tDao.totalizaTitulosBusca(buscaPagar);
	        	
	        }

	        return lstTitPagar;		

	}
	
	public void buscaEstornar() throws ProjetoException {

		TituloPagarDao tDao = new TituloPagarDao();
		
		if (lstTitPagar == null) {

        	lstTitPagar = tDao.titulosEstornar(buscaPagar); //tDao.todosFinanceiro(buscaPagar);
        	totBuscaPagar = tDao.totalizaTitulosBusca(buscaPagar);
        	
        }

      //  return lstTitPagar;	

	}


	
	public List<FornecedorBean> listDeFornecedores() throws ProjetoException, SQLException{
		
		BuscaDAO buscaDao = new BuscaDAO();
		
		if(lstFornecedor == null){
		lstFornecedor = buscaDao.buscaFornecedor();
		
	
		}
		return lstFornecedor;

	}

	public ArrayList<CentroCustoBean> listCentroCusto()
			throws ProjetoException, SQLException

	{
		if (lstccusto==null) {
		BuscaDAO buscaDao = new BuscaDAO();
		lstccusto = (ArrayList<CentroCustoBean>) buscaDao.buscaCentroCusto();
		}
		return lstccusto;

	}
	
	public void carregarBuscas() throws ProjetoException, SQLException{
		limparDados();
		listarTipoDoc();
	}
	
	public void verificarDevolucao(){
		
		int i = tituloPagarBean.getTipoDocumento().getCodtipodocumento();
		for(TipoDocumentoBean t : lsttipdoc){
			if(i == t.getCodtipodocumento()){
				setTipoDoc(t);
			}
		}
	}
	
	public void listarTipoDoc()
			throws ProjetoException, SQLException

	{
		
		BuscaDAO buscaDao = new BuscaDAO();
		lsttipdoc = (ArrayList<TipoDocumentoBean>) buscaDao.buscaTipoDoc();
		
	}
	
	

	public ArrayList<TipoDocumentoBean> listTipDoc()
			throws ProjetoException, SQLException

	{
		if (lsttipdoc==null) {
		BuscaDAO buscaDao = new BuscaDAO();
		lsttipdoc = (ArrayList<TipoDocumentoBean>) buscaDao.buscaTipoDoc();
		}
		return 		lsttipdoc;

	}
	

	public ArrayList<DespesaBean> listDepesas() throws ProjetoException,
			SQLException

	{
		if(lstDespesa==null) {
		BuscaDAO buscaDao = new BuscaDAO();
		lstDespesa = (ArrayList<DespesaBean>) buscaDao.buscaDespesa();
		}
		return lstDespesa;

	}
	
	public void estornarTitPagar() throws ProjetoException{
    	TesourariaDAO tdao = new TesourariaDAO();
    	ChequeEmitidoBean chq = new ChequeEmitidoBean();
    	
    	//estou carregando esse rowBeanBaixa.getCodChequeEmitido dentro do metodo onRowSelect
    	chq = tdao.pesquisaChequeEmitido(rowBeanBaixa.getCodChequeEmitido(), idSelecionado);
    	CaixaDiarioBean cx = new CaixaDiarioBean();
    	cx = tdao.retornaCaixaAtual();
    	chq.setCaixa(cx);
    	FacesContext msg = FacesContext.getCurrentInstance();
    	if (chq.getCodcheque() == null){
    		msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Esse documento já foi estornado.", "Aviso"));
    	}
    	else{
    	boolean cancelou = tdao.cancelarCheque(chq, "Estorno de Débito", rowBean.getCodigo());    	
    	if (cancelou) {
    		lstBaixa = new ArrayList<BaixaPagar>();
    		lstTitPagar = null;
    		buscaEstornar();
    		idSelecionado = 0;
    		rowBeanBaixa  = new BaixaPagar();
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Título estornado com sucesso.", "Sucesso"));
			RequestContext.getCurrentInstance().execute("PF('dlgBaixa').hide();");
		} else {
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao realizar estorno.", "Aviso"));
		}
    	}
    }

	/*public String vlrTotal() {

		
		
		String vlr = null;
		
		vlr = String.valueOf(this.tituloPagarBean.getValor());

		if (tituloPagarBean.getDesconto() != 0) {

			vlr = String.valueOf(this.tituloPagarBean.getValor()
					- this.tituloPagarBean.getDesconto());

		}
		if (tituloPagarBean.getJuros() != 0) {

			vlr = String.valueOf(this.tituloPagarBean.getValor()
					+ this.tituloPagarBean.getJuros());

		}

		return vlr;
	}*/
	
	public Double valorTotal() {

		
		Double vlr = tituloPagarBean.getValor();
		
		if(tituloPagarBean.getDesconto() !=0){
			vlr = tituloPagarBean.getValor() - tituloPagarBean.getDesconto();
		}
		
		if(tituloPagarBean.getMulta() !=0){
			vlr += tituloPagarBean.getMulta();
		}
		
		if(tituloPagarBean.getJuros() !=0){
			vlr += tituloPagarBean.getJuros();
		}
		BigDecimal vlrAux = new BigDecimal(vlr);
		BigDecimal vlrAux2 = vlrAux.setScale(2, BigDecimal.ROUND_HALF_UP);
		vlr = vlrAux2.doubleValue();
		return vlr;
	}
	
	public Double valorTotalEdit() {
	
			
			Double vlr = rowBean.getValor();
			
			if(rowBean.getDesconto() !=0){
				vlr = rowBean.getValor() - rowBean.getDesconto();
			}
			if(rowBean.getMulta() !=0){
				vlr += rowBean.getMulta();
			}
			if(rowBean.getJuros() !=0){
				vlr += rowBean.getJuros();
			}
			
			BigDecimal vlrAux = new BigDecimal(vlr);
			BigDecimal vlrAux2 = vlrAux.setScale(2, BigDecimal.ROUND_HALF_UP);
			
			vlr = vlrAux2.doubleValue();
			return vlr;
		}
		
	/*public String vlrTotalEdit() {
	
			
			
			String vlr = null;
			
			vlr = String.valueOf(this.rowBean.getValor());
	
			if (rowBean.getDesconto() != 0) {
	
				vlr = String.valueOf(this.rowBean.getValor()
						- this.tituloPagarBean.getDesconto());
	
			}
			if (rowBean.getJuros() != 0) {
	
				vlr = String.valueOf(this.rowBean.getValor()
						+ this.rowBean.getJuros());
	
			}
	
			return vlr;
		}*/
	
	 public void verificaAdd() {

	        

	        for (int i = 0; i < lstImpostos.size(); i++) {

	            

	            validarTipo(lstImpostos.get(i).getDescImposto());

	        }

	    }

	 public void validarTipo(String desc) {

	        

	        for (int i = 0; i < tipoImposto.size(); i++) {

	            if (tipoImposto.get(i).getDescricao().equals(desc)) {

	                


	                tipoImposto.remove(i);

	                

	            }

	        }

	    }
	 
	 public void onRowSelect(SelectEvent event) throws ProjetoException {
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        facesContext.addMessage(
	                null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO,
	                        "ccodagendacapt Selected" + String.valueOf(1), String
	                        .valueOf(1)));
	        TituloPagarDao tpdao = new TituloPagarDao();
	        idSelecionado = rowBean.getCodigo();
	        lstBaixa = tpdao.lstBaixas(idSelecionado);
	        
	        /*for (int i=0; i<lstBaixa.size(); i++){
	        	rowBeanBaixa.setCodChequeEmitido(lstBaixa.get(i).getCodChequeEmitido());
	        }*/
	        
	        
	    }
	 
	 public void onRowSelect2(SelectEvent event) throws ProjetoException {
	        
	        
	    }
	 
	 
	 
	 public void carregarImposto() {

	        TipoImposto t = new TipoImposto();
	        t.setIndice(0);
	        t.setDescricao("CSLL");

	        TipoImposto t2 = new TipoImposto();
	        t2.setIndice(1);
	        t2.setDescricao("IRPF");

	        TipoImposto t3 = new TipoImposto();
	        t3.setIndice(2);
	        t3.setDescricao("IRPI");

	        TipoImposto t4 = new TipoImposto();
	        t4.setIndice(3);
	        t4.setDescricao("INSS");

	        TipoImposto t5 = new TipoImposto();
	        t5.setIndice(4);
	        t5.setDescricao("ISS");

	        tipoImposto.add(t);
	        tipoImposto.add(t2);
	        tipoImposto.add(t3);
	        tipoImposto.add(t4);
	        tipoImposto.add(t5);

	    }
	 
	  public void addNaListImpostos() {
		  boolean existe = false;	
		  
		  	for(ImpostoBean ip: lstImpostos){
		  		if(ip.getDescImposto().equals(impostoBean.getDescImposto())){
		  			existe = true;
		  		} 
		  		
		  	}
		  	if(existe){
		  		JSFUtil.adicionarMensagemErro("Imposto já incluso", "Aten��o");
		  	} else {
		  		lstImpostos.add(impostoBean);
		  	}
	        this.impostoBean = new ImpostoBean();


	    }
	  
	  public void addNaListImpostosAlt() {

		  boolean existe = false;	
		  
		  	for(ImpostoBean ip: lstImpostosAlt){
		  		if(ip.getDescImposto().equals(rowBeanImposto.getDescImposto())){
		  			existe = true;
		  		} 
		  		
		  	}
		  	if(existe){
		  		JSFUtil.adicionarMensagemErro("Imposto já incluso","Aten��o");
		  	} else {
		  		lstImpostosAlt.add(rowBeanImposto);
		  	}
	        rowBeanImposto = new ImpostoBean();


	    }
	  
	  
	  
	  public void removerRetencao(){
		  lstImpostos.remove(this.rowBeanImposto);		  
	  }
	  
	  public void removerRetencaoAlt(){
		  
		  lstImpostosAlt.remove(rowBeanImposto);
		  
	  }
	 
	  public void alterarRetencaoAlt() {
	      
	            for(ImpostoBean ip : lstImpostosAlt) {
	            	if(ip.getDescImposto().equals(rowBeanImposto.getDescImposto())){
                    ip.setPcRentencao(rowBeanImposto.getPcRentencao());
                    ip.setValorBase(rowBeanImposto.getValorBase());
                    }
                    rowBeanImposto = new ImpostoBean();
	        } 
	        RequestContext.getCurrentInstance().execute("PF('dlgAltRetencao').hide();");
	    }
	  
	  
	  public ArrayList<BancoBean> lstBancosTotal() throws ProjetoException {

	        TituloReceberDao dao = new TituloReceberDao();

	        this.lstBancos = dao.lstTodosBancos();

	        return (ArrayList<BancoBean>) this.lstBancos;

	    }
	  
	  public void alterarRetencao() {
     		
          for(ImpostoBean ip : lstImpostos) {
        	  if(ip.getDescImposto().equals(rowBeanImposto.getDescImposto())){
              ip.setPcRentencao(rowBeanImposto.getPcRentencao());
              ip.setValorBase(rowBeanImposto.getValorBase());
              }
              
      } 
      this.rowBeanImposto = new ImpostoBean();
      RequestContext.getCurrentInstance().execute("PF('dlgEditRetencao').hide();");
  }
	public ArrayList<TituloPagarBean> getLstTituloPagar() {
		return lstTituloPagar;
	}

	public void setLstTituloPagar(ArrayList<TituloPagarBean> lstTituloPagar) {
		this.lstTituloPagar = lstTituloPagar;
	}

	public List<FornecedorBean> getLstFornecedor() {
		return lstFornecedor;
	}

	public void setLstFornecedor(ArrayList<FornecedorBean> lstFornecedor) {
		this.lstFornecedor = lstFornecedor;
	}

	public TituloPagarBean getTituloPagarBean() {
		return tituloPagarBean;
	}

	public void setTituloPagarBean(TituloPagarBean tituloPagarBean) {
		this.tituloPagarBean = tituloPagarBean;
	}

	public Integer getCodFornecedor() {
		return codFornecedor;
	}

	public void setCodFornecedor(Integer codFornecedor) {
		this.codFornecedor = codFornecedor;
	}

	public ArrayList<CentroCustoBean> getLstccusto() {
		return lstccusto;
	}

	public void setLstccusto(ArrayList<CentroCustoBean> lstccusto) {
		this.lstccusto = lstccusto;
	}

	public ArrayList<DespesaBean> getLstDespesa() {
		return lstDespesa;
	}

	public void setLstDespesa(ArrayList<DespesaBean> lstDespesa) {
		this.lstDespesa = lstDespesa;
	}

	public ArrayList<ImpostoBean> getLstImpostos() {
		return lstImpostos;
	}

	public void setLstImpostos(ArrayList<ImpostoBean> lstImpostos) {
		this.lstImpostos = lstImpostos;
	}

	public TituloPagarBean getRowBean() {
		return rowBean;
	}

	public void setRowBean(TituloPagarBean rowBean) {
		this.rowBean = rowBean;
	}

	public FornecedorBean getFornecedorBean() {
		return fornecedorBean;
	}

	public void setFornecedorBean(FornecedorBean fornecedorBean) {
		this.fornecedorBean = fornecedorBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PortadorBean getPortadorBean() {
		return portadorBean;
	}

	public void setPortadorBean(PortadorBean portadorBean) {
		this.portadorBean = portadorBean;
	}

	
	public List<PortadorBean> lstPortador() throws ProjetoException{
		  BuscaDAO buscaDao = new BuscaDAO();
		  	
	        if (lstPortador == null) {

	            lstPortador = buscaDao.lstTodosPortadores();

	        }

	        
			return lstPortador;
	}

	public List<PortadorBean> getLstPortador() throws ProjetoException {
	    BuscaDAO buscaDao = new BuscaDAO();

        if (lstPortador == null) {

            lstPortador = buscaDao.lstTodosPortadores();

        }

        
		return lstPortador;
	}

	public void setLstPortador(List<PortadorBean> lstPortador) {
		this.lstPortador = lstPortador;
	}

	public void setLstPortador(ArrayList<PortadorBean> lstPortador) {
		this.lstPortador = lstPortador;
	}

	public List<TipoImposto> getTipoImposto() {
		return tipoImposto;
	}

	public void setTipoImposto(List<TipoImposto> tipoImposto) {
		this.tipoImposto = tipoImposto;
	}

	public ImpostoBean getRowBeanImposto() {
		return rowBeanImposto;
	}

	public void setRowBeanImposto(ImpostoBean rowBeanImposto) {
		this.rowBeanImposto = rowBeanImposto;
	}

	public ImpostoBean getImpostoBean() {
		return impostoBean;
	}

	public void setImpostoBean(ImpostoBean impostoBean) {
		this.impostoBean = impostoBean;
	}

	public List<TituloPagarBean> getListaAbertos() throws ProjetoException {
		if(listaAbertos == null) {
			TituloPagarDao tpdao = new TituloPagarDao();
			listaAbertos = tpdao.listaAbertos(null, periodoInicial, periodoFinal);
		}
		return listaAbertos;
	}

	public void setListaAbertos(List<TituloPagarBean> listaAbertos) {
		this.listaAbertos = listaAbertos;
	}

	public List<BaixaPagar> getLstBaixa() throws ProjetoException {
		if ((idSelecionado > 0) && (lstBaixa.size()==0)) {
            TituloPagarDao dao = new TituloPagarDao();
            lstBaixa = dao.lstBaixas(idSelecionado);
        }
        return lstBaixa;
	}

	public void setLstBaixa(List<BaixaPagar> lstBaixa) {
		this.lstBaixa = lstBaixa;
	}

	public Integer getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Integer idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public List<ImpostoBean> getLstImpostosAlt() throws SQLException, ProjetoException{
		
		if(lstImpostosAlt == null) {
			TituloPagarDao tpdao = new TituloPagarDao();
			lstImpostosAlt = tpdao.listaImposto(rowBean.getCodigo());
		}
		return lstImpostosAlt;
	}

	public void setLstImpostosAlt(ArrayList<ImpostoBean> lstImpostosAlt) {
		this.lstImpostosAlt = lstImpostosAlt;
	}

	public List<FornecedorBean> getLstFornecedorAlt() {
		return lstFornecedorAlt;
	}

	public void setLstFornecedorAlt(List<FornecedorBean> lstFornecedorAlt) {
		this.lstFornecedorAlt = lstFornecedorAlt;
	}

	public List<PortadorBean> getLstPortadorAlt() {
		return lstPortadorAlt;
	}

	public void setLstPortadorAlt(List<PortadorBean> lstPortadorAlt) {
		this.lstPortadorAlt = lstPortadorAlt;
	}

	public ArrayList<TipoDocumentoBean> getLsttipdoc() {
		return lsttipdoc;
	}

	public void setLsttipdoc(ArrayList<TipoDocumentoBean> lsttipdoc) {
		this.lsttipdoc = lsttipdoc;
	}

	public BaixaPagar getRowBeanBaixa() {
		return rowBeanBaixa;
	}

	public void setRowBeanBaixa(BaixaPagar rowBeanBaixa) {
		this.rowBeanBaixa = rowBeanBaixa;
	}

	public BuscaBeanPagar getBuscaPagar() {
		return buscaPagar;
	}

	public void setBuscaPagar(BuscaBeanPagar buscaPagar) {
		this.buscaPagar = buscaPagar;
	}

	public TotalizadorBeanPagar getTotBuscaPagar() {
		return totBuscaPagar;
	}

	public void setTotBuscaPagar(TotalizadorBeanPagar totBuscaPagar) {
		this.totBuscaPagar = totBuscaPagar;
	}

	public Integer getParcelas() {
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public Integer getDocumentoParcelas() {
		return DocumentoParcelas;
	}

	public void setDocumentoParcelas(Integer documentoParcelas) {
		DocumentoParcelas = documentoParcelas;
	}

	public TipoDocumentoBean getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDocumentoBean tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public List<PagamentoBean> getLstDesistencia() {
		return lstDesistencia;
	}

	public void setLstDesistencia(List<PagamentoBean> lstDesistencia) {
		this.lstDesistencia = lstDesistencia;
	}

	public TituloReceberBean getFuncBean() {
		return funcBean;
	}

	public void setFuncBean(TituloReceberBean funcBean) {
		this.funcBean = funcBean;
	}

	public PagamentoBean getPagamentoDesi() {
		return pagamentoDesi;
	}

	public void setPagamentoDesi(PagamentoBean pagamentoDesi) {
		this.pagamentoDesi = pagamentoDesi;
	}

	public ArrayList<TituloPagarBean> getLstTitPagar() {
		return lstTitPagar;
	}

	public void setLstTitPagar(ArrayList<TituloPagarBean> lstTitPagar) {
		this.lstTitPagar = lstTitPagar;
	}
	
	
	
	
}
