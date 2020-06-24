package br.gov.al.maceio.sishosp.financeiro.control;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.financeiro.dao.BancoDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.TesourariaDAO;
import br.gov.al.maceio.sishosp.financeiro.dao.TituloPagarDao;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.financeiro.model.CaixaDiarioBean;
import br.gov.al.maceio.sishosp.financeiro.model.ChequeEmitidoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ChequeRecebidoBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.ImpostoBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;
import br.gov.al.maceio.sishosp.financeiro.model.PrestacaoContasBean;
import br.gov.al.maceio.sishosp.financeiro.model.TesourariaBean;
import br.gov.al.maceio.sishosp.financeiro.model.TituloPagarBean;




@ManagedBean
@ViewScoped
public class TesourariaController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double saldoinicial, creditos, debitos, saldohoje, disponivel, totalcaixa, aPagar, chequesDia, chequesPrazo, chequesCompensar;
    private CaixaDiarioBean caixa;
    private Date periodoInicial, periodoFinal;
    private BancoBean banco;
    private BancoBean banco2;
    private List<TesourariaBean> listamovimentacao;
    private List<TituloPagarBean> listatitulosaberto;
    private TesourariaBean tesouraria;
    private TesourariaBean tesouraria2;
    private FornecedorBean forn;
    private TituloPagarBean tituloselecionado;
    private TituloPagarBean tituloAvulso;
    private List<TituloPagarBean> listatitulosbaixar;
    private List<ChequeEmitidoBean> listachequesemitidos;
    private List<ChequeRecebidoBean> listaChequesRecebidos;
    private ChequeEmitidoBean cheque, chequeselecionado, rowCheque, chequeBusca;
    private ChequeRecebidoBean chequeRecebidobusca,rowChequeRecebido;
    private TesourariaBean lancamento;
    private ArrayList<BancoBean> listabancos;
    private String tipoDocumento;
    private Integer sequenciaCheque;
    private Integer operacao;
    private Integer idSelecionado, idSelecionadoChequeReceb;
    boolean totalDisable;
    private FornecedorBean fornecedorBean;
	private PortadorBean portadorBean;
	private ArrayList<ImpostoBean> lstImpostos;
    

    public TesourariaController() throws ProjetoException, ParseException {
    	
    	Calendar calendar = Calendar.getInstance();
		//calendar.setTime(data);
		    
		int dia = 1;
		int mes = 1;
		int ano = calendar.get(Calendar.YEAR);
		periodoFinal = new java.util.Date();
		periodoInicial = new java.util.Date();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = (Date)formatter.parse(String.valueOf(dia)+"/"+String.valueOf(mes)+"/"+ String.valueOf(ano));
	
        
        banco = new BancoBean();
        
        TesourariaDAO icdao = new TesourariaDAO();
        caixa = icdao.retornaCaixaAtual();
        
        listatitulosbaixar = new ArrayList<TituloPagarBean>();
        listachequesemitidos = new ArrayList<ChequeEmitidoBean>();
        listaChequesRecebidos = new ArrayList<ChequeRecebidoBean>();
        tituloselecionado = new TituloPagarBean();
        tituloAvulso = new TituloPagarBean();
        tituloselecionado.getTitulobaixa().setTpbaixa("1");
        lancamento = new TesourariaBean();
        listatitulosaberto =  new ArrayList<TituloPagarBean>();
		// listaTitulosAberto();
        //listaAbertos = null;
        tipoDocumento = "";
        forn = new FornecedorBean();
        cheque = new ChequeEmitidoBean();
        chequeBusca = new ChequeEmitidoBean();
        chequeRecebidobusca = new ChequeRecebidoBean();
        chequeRecebidobusca.setStatus("P");
        chequeRecebidobusca.setDtemissao(date);
        chequeRecebidobusca.setDtvencimento(new java.util.Date(System.currentTimeMillis()));        
        chequeBusca.setStatus("P");
        chequeBusca.setDtemissao(date);
        chequeBusca.setDtvencimento(new java.util.Date(System.currentTimeMillis()));
        chequeselecionado = new ChequeEmitidoBean();
        listabancos = new ArrayList<>();
        listabancos = null;
        idSelecionado = 0;
        idSelecionadoChequeReceb = 0;
        tesouraria = new TesourariaBean();
        tesouraria2 = new TesourariaBean();
        banco = new BancoBean();
        banco2 = new BancoBean();
		fornecedorBean = new FornecedorBean();
		portadorBean = new PortadorBean();
		lstImpostos = new ArrayList<>();
        
       
        
    }

    public void novoLancamento() {
        lancamento = new TesourariaBean();
        lancamento.setOperacao("DESPBANC");
        lancamento.setBanco(banco);
        lancamento.setCaixa(caixa);
    }
    
    public void limparDadosTit(){
    	tituloselecionado = new TituloPagarBean();
    }
    
    public void limparDados() {
		 
    	listatitulosbaixar = new ArrayList<TituloPagarBean>();
	     //listachequesemitidos = new ArrayList<ChequeEmitidoBean>();
	     tituloselecionado = new TituloPagarBean();		
	     lancamento = new TesourariaBean();
	     tipoDocumento = "";
        forn = new FornecedorBean();
        cheque = new ChequeEmitidoBean();
        chequeselecionado = new ChequeEmitidoBean();
        listabancos = null;
        idSelecionado = 0;
        idSelecionadoChequeReceb  = 0;
        rowCheque = new ChequeEmitidoBean();
        rowChequeRecebido = new ChequeRecebidoBean();
        operacao = 0;
	     
    }
    
    public void limparAcerto() {
		 
    	lancamento.setValor(0.0);
    	lancamento.setObs("");
	     
    }

    public void onBancoSelect(SelectEvent event) throws ParseException, ProjetoException {
        Integer codagenda;
        idSelecionado = 0;
        idSelecionadoChequeReceb = 0;
        listaMovimentacaoCaixa();
        
		 //FacesContext facesContext = FacesContext.getCurrentInstance();
        //facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Horário Selecionado:"+String.valueOf(codagenda),String.valueOf(codagenda) ));

    }
    
	public void onRowSelect(SelectEvent event) throws ProjetoException {
	        
	        idSelecionado = rowCheque.getCodcheque();
	    
	    }
	
	public void selecionaChequeRecebido(SelectEvent event) throws ProjetoException {
        
        idSelecionadoChequeReceb = rowChequeRecebido.getCodcheque();
    
    }	
	//teste
	public void calcularTotalParcial() throws ProjetoException {
	    if (tituloselecionado.getTitulobaixa().getTpbaixa().equals("1")){
			
			totalDisable = true;
		}
	    
	    if (tituloselecionado.getTitulobaixa().getTpbaixa().equals("2")){
	    	totalDisable = false;
	    	
	    }
	    
	    }

    public List<TesourariaBean> listaMovimentacaoCaixa() throws ParseException, ProjetoException {
    	
        TesourariaDAO icdao = new TesourariaDAO();
        listamovimentacao = icdao.pesquisaMovimentacao(banco.getId(), caixa.getSeqcaixadiario());
        listachequesemitidos = icdao.pesquisaChequesEmitidos(banco.getId(), chequeBusca);
        listaChequesRecebidos = icdao.pesquisaChequesRecebido(banco.getId(), chequeRecebidobusca);        
        
        saldoinicial = icdao.saldoInicialCaixaBc(banco.getId(), caixa.getSeqcaixadiario());
        creditos = icdao.totalCreditosTesBc(banco.getId(), caixa.getSeqcaixadiario());
        debitos = icdao.totalDebitosTesBc(banco.getId(), caixa.getSeqcaixadiario());
//        chequesDia = icdao.pesquisaChequesReceberDia(banco.getId(), caixa.getSeqcaixadiario());
      //  chequesPrazo = icdao.pesquisaChequesReceberPrazo(banco.getId(), caixa.getSeqcaixadiario());
        saldohoje = saldoinicial + creditos - debitos;
        
        return listamovimentacao;
    }

    public void abreCaixa() throws ProjetoException {
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.abreCaixa(banco.getId());
        if (rst) {
            caixa = icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgabrecaixa').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Caixa Aberto com Sucesso.", "Alerta"));
            listabancos = null;
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao abrir Caixa.", "Alerta"));
        }

    }

    
    
    public void gravaLancamento() throws ProjetoException, ParseException {
        
        TesourariaDAO icdao = new TesourariaDAO();
        PrestacaoContasBean lancto2 = new PrestacaoContasBean();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Connection con = null;
        //con = ConnectFactory.getConnection();
        boolean rst = icdao.gravaLancamento(lancamento, null, true, lancto2);
        if (rst) {
            caixa = icdao.retornaCaixaAtual();
            listaMovimentacaoCaixa();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlglancto').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Lançamento efetuado com Sucesso.", "Alerta"));
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao efetuar lançamento.", "Alerta"));
        }

    }
    
public void gravaAcerto() throws ProjetoException, ParseException {
        
        TesourariaDAO icdao = new TesourariaDAO();
        PrestacaoContasBean lancto2 = new PrestacaoContasBean();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Connection con = null;
        //con = ConnectFactory.getConnection();
        boolean rst = icdao.gravaAcerto1(lancamento,banco.getId(), null, true, lancto2);
        if (rst == true) {
            caixa = icdao.retornaCaixaAtual();
            listaMovimentacaoCaixa();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgacerto').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Acerto efetuado com Sucesso.", "Alerta"));
            
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Conta com acerto já realizado. Só é permitido 1 acerto por conta", "Alerta"));
        }

    }

    public void compensaChq() throws ProjetoException {
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.compensaChq(rowCheque);
        if (rst) {
            listachequesemitidos = icdao.pesquisaChequesEmitidos(banco.getId(),chequeBusca);
            idSelecionado = 0;
            //caixa =  icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgcomp').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque compensado com Sucesso.", "Alerta"));
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao compensar Cheque.", "Alerta"));
        }

    }
    
    public void compensaChqRecebido() throws ProjetoException {
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.compensaChqReceb(rowChequeRecebido);
        if (rst) {
            listaChequesRecebidos = icdao.pesquisaChequesRecebido(banco.getId(),chequeRecebidobusca);
            idSelecionadoChequeReceb = 0;
            //caixa =  icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgcompreceb').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque recebido compensado com Sucesso.", "Alerta"));
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao compensar Cheque.", "Alerta"));
        }

    }  
    
    /*
    
    public void depositaChqRecebido() throws ProjetoException {
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.depositaChqReceb(rowChequeRecebido);
        if (rst) {
            listaChequesRecebidos = icdao.pesquisaChequesRecebido(banco.getId(),chequeRecebidobusca);
            idSelecionadoChequeReceb = 0;
            //caixa =  icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgdeposreceb').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque recebido enviado para depósito com Sucesso.", "Alerta"));
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao depositar Cheque.", "Alerta"));
        }

    }  
    
    */
    
    /*
    public void devolveChqRecebido() throws ProjetoException {
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.devolveChqReceb(rowChequeRecebido);
        if (rst) {
            listaChequesRecebidos = icdao.pesquisaChequesRecebido(banco.getId(),chequeRecebidobusca);
            idSelecionadoChequeReceb = 0;
            //caixa =  icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgdevolreceb').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque recebido Devolvido!.", "Alerta"));
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao depositar Cheque.", "Alerta"));
        }

    }  
    
*/
    
    /*
    
    public void reapresentaChqRecebido() throws ProjetoException {
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.reapresentaChqReceb(rowChequeRecebido);
        if (rst) {
            listaChequesRecebidos = icdao.pesquisaChequesRecebido(banco.getId(),chequeRecebidobusca);
            idSelecionadoChequeReceb = 0;
            //caixa =  icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgdreapreceb').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque recebido reapresentado!.", "Alerta"));
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao depositar Cheque.", "Alerta"));
        }

    }    
        
        
        */
    
    public void pesquisaChequesBusca() throws ProjetoException{
    	 TesourariaDAO icdao = new TesourariaDAO();
    	 listachequesemitidos = icdao.pesquisaChequesEmitidos(banco.getId(), chequeBusca);
    }
    
    public void pesquisaChequesRecebidos() throws ProjetoException{
   	 TesourariaDAO icdao = new TesourariaDAO();
   	 listaChequesRecebidos = icdao.pesquisaChequesRecebidosBusca(banco.getId(), chequeRecebidobusca);
   }
    

    public void compensaTitulosPagar() throws ProjetoException, SQLException, ParseException {
        
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        cheque.setBanco(banco);
        cheque.setCaixa(caixa);
        cheque.setTipo(tipoDocumento);
        cheque.setStatus("OK");
        //Calcula o total do cheque
        double totalcheque = 0;
        if(listatitulosbaixar != null && listatitulosbaixar.size() > 0){        	
        
        for (int x = 0; x < listatitulosbaixar.size(); x++) {
            totalcheque = totalcheque + listatitulosbaixar.get(x).getTitulobaixa().getValorpago();

        }
        cheque.setValor(totalcheque);
        if(banco.getContaCaixa().equals("S")){
        	
        	Date data = new Date();
        	cheque.setDtvencimento(data);
        	cheque.setCompensado("S");
        	cheque.setDtcompensado(caixa.getDataCaixaAbertura());
        	
        	
        	cheque.setNumcheque(String.valueOf(sequenciaCheque));
        }
        boolean rst = icdao.compensaTitulosPagar(cheque, listatitulosbaixar, tipoDocumento);
        if (rst) {
            listaMovimentacaoCaixa();
            caixa = icdao.retornaCaixaAtual();
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgchqemi').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pagamento efetuado com Sucesso.", "Alerta"));
            limparDados();
            
            
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao efetuar pagamento.", "Alerta"));
        	}
        } else {
        	facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Insira pelo menos um documento para pagar.", "Alerta"));
        }
    }
    
    public void cancelarCheque() throws ProjetoException{
    	rowCheque.setCaixa(caixa);
    	
    	TesourariaDAO tdao = new TesourariaDAO();
    	boolean cancelou = tdao.cancelarCheque(rowCheque, "Cheque N° " + rowCheque.getNumcheque(), 0);
    	FacesContext msg = FacesContext.getCurrentInstance();
    	if (cancelou) {
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque cancelado com sucesso.", "Sucesso"));
			RequestContext.getCurrentInstance().execute("PF('dlgConfExc').hide();");
		} else {
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao realizar cancelamento.", "Aviso"));
		}
    }
    
    public Double valorTotalPagamentoAvulso() {

		Double vlr = tituloAvulso.getValor();

		if (tituloAvulso.getDesconto() != 0) {
			vlr = tituloAvulso.getValor() - tituloAvulso.getDesconto();
		}

		if (tituloAvulso.getMulta() != 0) {
			vlr += tituloAvulso.getMulta();
		}

		if (tituloAvulso.getJuros() != 0) {
			vlr += tituloAvulso.getJuros();
		}
		BigDecimal vlrAux = new BigDecimal(vlr);
		BigDecimal vlrAux2 = vlrAux.setScale(2, BigDecimal.ROUND_HALF_UP);
		vlr = vlrAux2.doubleValue();
		return vlr;
	}
    
	public void salvarDocumentoPagarAvulso(BancoBean banco) throws ProjetoException, ParseException {

		boolean gravou = false;
		TituloPagarDao pagarDAo = new TituloPagarDao();

		this.tituloAvulso.setForn(this.fornecedorBean);
		this.tituloAvulso.setPortador(this.portadorBean);
		this.tituloAvulso.getTitulobaixa().setTpbaixa("1");
		CaixaDiarioBean cx = new CaixaDiarioBean();
		TesourariaDAO tdao = new TesourariaDAO();
		cx = tdao.retornaCaixaAtual();
		// Date data = new java.util.Date();
		// this.tituloPagarBean.setDtemissao(new java.sql.Date(data.getTime()));



		Boolean podeSalvar = true;

		if (tituloAvulso.getValor() > 0) {

			if (podeSalvar) {

				gravou = pagarDAo.salvarPagarAvulso(this.tituloAvulso, this.lstImpostos,
						 banco, cx);

			}

		} else {
			JSFUtil.adicionarMensagemErro("O valor deve ser maior que 0.", "Atenção");
		}

		if (valorTotalPagamentoAvulso() < 0) {
			JSFUtil.adicionarMensagemErro("O valor total deve ser maior que 0.", "Atenção");

		} else {
			if (gravou == true) {
				limparDados();
				RequestContext.getCurrentInstance().execute("PF('dlgtituloavulso').hide();");
				JSFUtil.adicionarMensagemSucesso("Salvo com sucesso!", "Atenção");
				onBancoSelect(null);
				tituloAvulso  = new TituloPagarBean();

			} else {

				JSFUtil.adicionarMensagemErro("Ocorreu um erro ao gravar.", "Atenção");
			}
		}
	}
    
    public void removerTitulo() {
    	
    	
    	listatitulosbaixar.remove(this.tituloselecionado);
    	
    	
    }

    public void fechaCaixa() throws ProjetoException {
        
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean rst = icdao.fechaCaixa(caixa);
        if (rst) {
            listamovimentacao = new ArrayList<TesourariaBean>();
            caixa = icdao.retornaCaixaAtual();
        
            RequestContext.getCurrentInstance().execute(
                    "PF('dlgfechacaixa').hide();");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Caixa fechado com Sucesso.", "Alerta"));
            listabancos = null;
            listabancos = new ArrayList<>();
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro ao fechar Caixa.", "Alerta"));
        }
    }

    public void pesquisaTitulosAberto() throws ProjetoException {
        
        listatitulosaberto = new ArrayList<TituloPagarBean>();
        TesourariaDAO icdao = new TesourariaDAO();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        listatitulosaberto = icdao.pesquisaTitulosAberto(forn, periodoInicial, periodoFinal);
        

    }

    public void addTituloBaixar() {
    	
        if(tituloselecionado.getTitulobaixa().getTpbaixa().equals("1")){
        	if(tituloselecionado.getTitulobaixa().getValorpago() >= aPagar){
        		if(listatitulosbaixar.contains(tituloselecionado)){
        			JSFUtil.adicionarMensagemErro("Você já inseriu este documento.","Erro");
        		} else {
        			listatitulosbaixar.add(tituloselecionado);
            		RequestContext.getCurrentInstance().execute("PF('dlgliqtitchq').hide();");
        		}
        		
        	} else {
        		JSFUtil.adicionarMensagemErro("Valor a pagar deve ser igual ao valor em aberto","Erro");
        	}
        	
        } else {
            if(tituloselecionado.getTitulobaixa().getTpbaixa().equals("2")){
            	if(tituloselecionado.getTitulobaixa().getValorpago() < aPagar){
            		if(listatitulosbaixar.contains(tituloselecionado)){
            			JSFUtil.adicionarMensagemErro("Você já inseriu este documento.","Erro");
            		} else {
            		listatitulosbaixar.add(tituloselecionado);
            		RequestContext.getCurrentInstance().execute("PF('dlgliqtitchq').hide();");
            		}
            	} else {
            		JSFUtil.adicionarMensagemErro("Valor a pagar deve ser menor que o valor em aberto","Erro");
            	}
            }
        } 
        
    }

    public void calculasaldoPagar() {

        aPagar = tituloselecionado.getValoraberto() - tituloselecionado.getTitulobaixa().getVlrdesc() + tituloselecionado.getTitulobaixa().getVlrjuros() + tituloselecionado.getTitulobaixa().getVlrmulta();
        tituloselecionado.getTitulobaixa().setValorpago(aPagar);
    }

    public boolean verificaCheque() throws ProjetoException {
        boolean rst = true;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if ((cheque.getNumcheque() == null) || (cheque.getNumcheque().equals('0'))) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informe o número do Cheque.", "Alerta"));
            rst = false;

        } else {
            TesourariaDAO icdao = new TesourariaDAO();
            if (icdao.pesquisaNumCheque(cheque)) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cheque já existe.", "Alerta"));
                rst = false;

            }
        }

        return rst;
    }
    
    public ArrayList<BancoBean> listaBancos() throws ProjetoException {
    	   if(listabancos == null) {
               BancoDAO bdao = new BancoDAO();
               listabancos = bdao.buscaTodosBancos();
           }
    	   return listabancos;
	}
    
    public void setarDebito() {
    	operacao = 0;
    	cheque.setDtvencimento( new Date());
    	
    	tipoDocumento = "CC";
    	if(banco.getContaCaixa().equals("S")) {
    		operacao = 1;
    	
    	}
    	
    	
    }
    
    public void setarTituloAvulso() {
    	tituloAvulso.setParcela("UN");
    	
    }
    
  public void setarCheque() {
	  operacao = 0;
	  
    	tipoDocumento = "CP";
    	
    	
    }

  public String dataAtual() {

      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = new Date();
      return dateFormat.format(date);

  }
  public ArrayList<BancoBean> bancoListado() throws ProjetoException{
	  BancoDAO bdao= new BancoDAO();
	  listabancos = bdao.buscaTodosBancos();
	  
	  return listabancos;
  }
  // metodo joao
  public void transferirConta() throws ProjetoException{
	  
	  	if(tesouraria.getValor() == 0){
	  		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"O valor deve ser maior que zero",
					"Concluído!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
	  	}else{
 
	  if (tesouraria.getBanco().getId() == tesouraria2.getBanco().getId() ) {
		  FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Transferência deve ser entre duas contas diferentes!",
					"Concluído!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
	  }
	  else
	  {
	  BancoDAO bdao = new BancoDAO();	  
	  banco =  bdao.buscarBancoPorId(tesouraria.getBanco());
	  banco2 = bdao.buscarBancoPorId(tesouraria2.getBanco());
	  
	  tesouraria.setBanco(banco);
	  tesouraria2.setBanco(banco2);
	  
	  TesourariaDAO badao = new TesourariaDAO();
	  caixa = badao.retornaCaixaAtual();
	  tesouraria.setCaixa(caixa);
	  tesouraria2.setCaixa(caixa); 
	  
	  TesourariaDAO tdao = new TesourariaDAO();
	 
	  Boolean b = tdao.inserirConta(tesouraria, tesouraria2);
		
		if (b==true){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Transferência concluida!",
					"Concluido!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		tesouraria = new TesourariaBean();
		tesouraria2 = new TesourariaBean();
	  
	  }
	  	}
  }
  
  
    //gets e sets
    public double getSaldoinicial() {
        return saldoinicial;
    }

    public double getCreditos() {
        return creditos;
    }

    public double getDebitos() {
        return debitos;
    }

    public double getSaldohoje() {
        return saldohoje;
    }

    public double getDisponivel() {
        return disponivel;
    }

    public double getTotalcaixa() {
        return totalcaixa;
    }

    public CaixaDiarioBean getCaixa() {
        return caixa;
    }

    public BancoBean getBanco() {
        return banco;
    }

    public List<TesourariaBean> getListamovimentacao() {
        return listamovimentacao;
    }

    public TesourariaBean getTesouraria() {
        return tesouraria;
    }

    public void setSaldoinicial(double saldoinicial) {
        this.saldoinicial = saldoinicial;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    public void setDebitos(double debitos) {
        this.debitos = debitos;
    }

    public void setSaldohoje(double saldohoje) {
        this.saldohoje = saldohoje;
    }

    public void setDisponivel(double disponivel) {
        this.disponivel = disponivel;
    }

    public void setTotalcaixa(double totalcaixa) {
        this.totalcaixa = totalcaixa;
    }

    public void setCaixa(CaixaDiarioBean caixa) {
        this.caixa = caixa;
    }

    public void setBanco(BancoBean banco) {
        this.banco = banco;
    }

    public void setListamovimentacao(List<TesourariaBean> listamovimentacao) {
        this.listamovimentacao = listamovimentacao;
    }

    public void setTesouraria(TesourariaBean tesouraria) {
        this.tesouraria = tesouraria;
    }

    public FornecedorBean getForn() {
        return forn;
    }

    public void setForn(FornecedorBean forn) {
        this.forn = forn;
    }

    public Date getPeriodoInicial() {
        return periodoInicial;
    }

    public Date getPeriodoFinal() {
        return periodoFinal;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    public List<TituloPagarBean> getListatitulosaberto() {
        return listatitulosaberto;
    }

    public void setListatitulosaberto(List<TituloPagarBean> listatitulosaberto) {
        this.listatitulosaberto = listatitulosaberto;
    }

    public TituloPagarBean getTituloselecionado() {
        return tituloselecionado;
    }

    public void setTituloselecionado(TituloPagarBean tituloselecionado) {
        this.tituloselecionado = tituloselecionado;
    }
    public List<TituloPagarBean> getListatitulosbaixar() {
    	
    	return listatitulosbaixar;
    }

    public void setListatitulosbaixar(List<TituloPagarBean> listatitulosbaixar) {
        this.listatitulosbaixar = listatitulosbaixar;
    }

    public double getaPagar() {
        return aPagar;
    }

    public void setaPagar(double aPagar) {
        this.aPagar = aPagar;
    }

    public ChequeEmitidoBean getCheque() {        
    	return cheque;
    }

    public void setCheque(ChequeEmitidoBean cheque) {
        this.cheque = cheque;
    }

    public List<ChequeEmitidoBean> getListachequesemitidos() {
        return listachequesemitidos;
    }

    public void setListachequesemitidos(List<ChequeEmitidoBean> listachequesemitidos) {
        this.listachequesemitidos = listachequesemitidos;
    }

    public ChequeEmitidoBean getChequeselecionado() {
        return chequeselecionado;
    }

    public void setChequeselecionado(ChequeEmitidoBean chequeselecionado) {
        this.chequeselecionado = chequeselecionado;
    }

    public TesourariaBean getLancamento() {
        return lancamento;
    }

    public void setLancamento(TesourariaBean lancamento) {
        this.lancamento = lancamento;
    }

    public ArrayList<BancoBean> getListabancos() throws ProjetoException {     
    	 if(listabancos == null) {
             BancoDAO bdao = new BancoDAO();
             listabancos = bdao.buscaTodosBancos();
         }
  	   
    	return listabancos;
    }

    public void setListabancos(ArrayList<BancoBean> listabancos) {
        this.listabancos = listabancos;
    }

	public List<TituloPagarBean> listaTitulosAberto() throws ProjetoException{
		
		if(listatitulosaberto.size() == 0){
		
			TituloPagarDao tpdao = new TituloPagarDao();
			listatitulosaberto = tpdao.listaAbertos(forn, periodoInicial, periodoFinal);
			
		}return listatitulosaberto;
	}
	
	public void calcularValorAberto() throws ProjetoException{
		
		TituloPagarDao tpdao = new TituloPagarDao();
		tituloselecionado.setValoraberto(tpdao.valorAberto(tituloselecionado.getCodigo()));
	}



	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Integer getSequenciaCheque() throws ProjetoException {
		TituloPagarDao tpdao = new TituloPagarDao();
		sequenciaCheque = tpdao.getSequencia();
		return sequenciaCheque;
	}

	public void setSequenciaCheque(Integer sequenciaCheque) {
		this.sequenciaCheque = sequenciaCheque;
	}

	public Integer getOperacao() {
		return operacao;
	}

	public void setOperacao(Integer operacao) {
		this.operacao = operacao;
	}

	public Integer getIdSelecionado() {
		return idSelecionado;
	}

	public void setIdSelecionado(Integer idSelecionado) {
		this.idSelecionado = idSelecionado;
	}

	public ChequeEmitidoBean getRowCheque() {
		return rowCheque;
	}

	public void setRowCheque(ChequeEmitidoBean rowCheque) {
		this.rowCheque = rowCheque;
	}

	public ChequeEmitidoBean getChequeBusca() {
		return chequeBusca;
	}

	public void setChequeBusca(ChequeEmitidoBean chequeBusca) {
		this.chequeBusca = chequeBusca;
	}

	public TesourariaBean getTesouraria2() {
		return tesouraria2;
	}

	public void setTesouraria2(TesourariaBean tesouraria2) {
		this.tesouraria2 = tesouraria2;
	}

	public BancoBean getBanco2() {
		return (BancoBean) banco2;
	}

	public void setBanco2(BancoBean banco2) {
		this.banco2 =  banco2;
	}

	public boolean isTotalDisable() {
		return totalDisable;
	}

	public void setTotalDisable(boolean totalDisable) {
		this.totalDisable = totalDisable;
	}

	public double getChequesDia() {
		return chequesDia;
	}

	public void setChequesDia(double chequesDia) {
		this.chequesDia = chequesDia;
	}

	public double getChequesPrazo() {
		return chequesPrazo;
	}

	public void setChequesPrazo(double chequesPrazo) {
		this.chequesPrazo = chequesPrazo;
	}

	public double getChequesCompensar() {
		return chequesCompensar;
	}

	public void setChequesCompensar(double chequesCompensar) {
		this.chequesCompensar = chequesCompensar;
	}

	public List<ChequeRecebidoBean> getListaChequesRecebidos() {
		return listaChequesRecebidos;
	}

	public void setListaChequesRecebidos(
			List<ChequeRecebidoBean> listaChequesRecebidos) {
		this.listaChequesRecebidos = listaChequesRecebidos;
	}

	public ChequeRecebidoBean getChequeRecebidobusca() {
		return chequeRecebidobusca;
	}

	public void setChequeRecebidobusca(ChequeRecebidoBean chequeRecebidobusca) {
		this.chequeRecebidobusca = chequeRecebidobusca;
	}

	

	public Integer getIdSelecionadoChequeReceb() {
		return idSelecionadoChequeReceb;
	}

	public void setIdSelecionadoChequeReceb(Integer idSelecionadoChequeReceb) {
		this.idSelecionadoChequeReceb = idSelecionadoChequeReceb;
	}

	public ChequeRecebidoBean getRowChequeRecebido() {
		return rowChequeRecebido;
	}

	public void setRowChequeRecebido(ChequeRecebidoBean rowChequeRecebido) {
		this.rowChequeRecebido = rowChequeRecebido;
	}

	public TituloPagarBean getTituloAvulso() {
		return tituloAvulso;
	}

	public void setTituloAvulso(TituloPagarBean tituloAvulso) {
		this.tituloAvulso = tituloAvulso;
	}

	public FornecedorBean getFornecedorBean() {
		return fornecedorBean;
	}

	public PortadorBean getPortadorBean() {
		return portadorBean;
	}

	public void setFornecedorBean(FornecedorBean fornecedorBean) {
		this.fornecedorBean = fornecedorBean;
	}

	public void setPortadorBean(PortadorBean portadorBean) {
		this.portadorBean = portadorBean;
	}

	public ArrayList<ImpostoBean> getLstImpostos() {
		return lstImpostos;
	}

	public void setLstImpostos(ArrayList<ImpostoBean> lstImpostos) {
		this.lstImpostos = lstImpostos;
	}  
	
	
}
