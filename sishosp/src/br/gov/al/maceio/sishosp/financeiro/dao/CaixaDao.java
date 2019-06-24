package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.financeiro.model.CreditoCaixaBean;



public class CaixaDao {

	public ArrayList<CreditoCaixaBean> listCredito(Integer codcaixaloja) throws ProjetoException {

		String sql = "select v.codvenda, coalesce(idreceber,0) idreceber,v.codcliente, venda2.valor, c.nome, t.descricao, venda2.codtippag, t.tipo, venda2.codvenda2, estornado, venda2.seqcaixa  FROM clin.venda2"
				+ " join clin.venda v on v.codvenda = venda2.codvenda"
				+ " join clin.cliente c on c.codcliente = v.codcliente"
				+ " join clin.tippag t on t.codtippag = venda2.codtippag"
				+ " where venda2.seqcaixa=? and venda2.provisorio is not true and  idreceber is null and v.status <> 'CAN'";
		/*
		 * + " union all " +
		 * "select 0 codvenda,  venda2.idreceber,r.id_cliente, venda2.valor, c.nome, t.descricao, venda2.codtippag, t.tipo, venda2.codvenda2, estornado,"
		 * + " venda2.seqcaixa  FROM clin.venda2" +
		 * "   join clin.recdup r on r.idreceber = venda2.idreceber" +
		 * " join clin.cliente c on c.codcliente = r.id_cliente" +
		 * " join clin.tippag t on t.codtippag = venda2.codtippag" +
		 * " where venda2.seqcaixa=? and venda2.provisorio is not true";
		 */
		Connection con = null;
		ResultSet set = null;

		ArrayList<CreditoCaixaBean> lista = new ArrayList<CreditoCaixaBean>();
		try {

			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codcaixaloja);
			// ps.setInt(2, codcaixaloja);
			// ps.setInt(2, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				CreditoCaixaBean caixaBean = new CreditoCaixaBean();
				if (set.getInt("codvenda") == 0)
					caixaBean.getVenda().setCodVenda(null);
				else
					caixaBean.getVenda().setCodVenda(set.getInt("codvenda"));
				if (set.getInt("idreceber") == 0)
					caixaBean.getTitreceber().setIdFinanceiro(null);
				else
					caixaBean.getTitreceber().setIdFinanceiro(set.getInt("idreceber"));

				caixaBean.setValor(set.getDouble("valor"));
				caixaBean.getCliente().setCodcliente(set.getInt("codcliente"));
				caixaBean.getCliente().setNome(set.getString("nome"));
				caixaBean.getTippag().setDescricao(set.getString("descricao"));
				caixaBean.setCodvenda2(set.getInt("codvenda2"));
				caixaBean.setEstornado(set.getString("estornado"));
				caixaBean.setCodcaixaloja(set.getInt("seqcaixa"));
				caixaBean.getTippag().setTipo(set.getString("tipo"));
				caixaBean.getTippag().setCodtippag(set.getInt("codtippag"));
				lista.add(caixaBean);

			}

			set.close();
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();

				// TODO: handle exception
			}
			// fecharconexao();

		}
		return lista;
	}

	public boolean recebeCheque(ChequeRecebidoBean cheque) throws ProjetoException, SQLException {

		Connection con = ConnectionFactory.getConnection();

		boolean result = false;
		
		try {
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			CallableStatement ps = con.prepareCall("{ ? = call clin.cheque_recebido(?,?,?,?,?,?,?,?,?,?,?,?) }");

			ps.registerOutParameter(1, Types.INTEGER);

			ps.setInt(2, user_session.getEmpresa().getCodfilial());

			ps.setInt(3, cheque.getBanco().getId());

			ps.setString(4, cheque.getNumcheque());

			ps.setDate(5, new java.sql.Date(cheque.getDtemissao().getTime()));

			ps.setDouble(6, cheque.getValor());

			ps.setString(7, cheque.getNominal());

			ps.setString(8, cheque.getCompensado());

			ps.setInt(9, user_session.getCodfunc());

			ps.setString(10, cheque.getStatus());

			ps.setDate(11, new java.sql.Date(cheque.getDtemissao().getTime()));

			ps.setInt(12, cheque.getCaixa().getSeqcaixadiario());
			if (cheque.getDtcompensado() != null)
				ps.setDate(13, new java.sql.Date(cheque.getDtcompensado().getTime()));
			else
				ps.setNull(13, Types.OTHER);

			ps.executeUpdate();
			
			con.commit();
			con.close();

			result = true;
			
			ps.close();
		} catch (Exception sqle) {
			// retorno[0] = "false";
			sqle.printStackTrace();
			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}
			// fecharconexao();

		}

		return result;

	}

	public ArrayList<CreditoCaixaBean> listCreditoReceber(Integer codcaixaloja) throws ProjetoException {

		String sql = "select v.codvenda, coalesce(idreceber,0) idreceber,v.codcliente, venda2.valor, c.nome, t.descricao, venda2.codtippag, t.tipo, venda2.codvenda2, estornado, venda2.seqcaixa  FROM clin.venda2"
				+ " left join clin.venda v on v.codvenda = venda2.codvenda"
				+ " left join clin.cliente c on c.codcliente = v.codcliente"
				+ " left join clin.tippag t on t.codtippag = venda2.codtippag"
				+ " where venda2.seqcaixa=? and  idreceber is not null";
		/*
		 * + " union all " +
		 * "select 0 codvenda,  venda2.idreceber,r.id_cliente, venda2.valor, c.nome, t.descricao, venda2.codtippag, t.tipo, venda2.codvenda2, estornado,"
		 * + " venda2.seqcaixa  FROM clin.venda2" +
		 * "   join clin.recdup r on r.idreceber = venda2.idreceber" +
		 * " join clin.cliente c on c.codcliente = r.id_cliente" +
		 * " join clin.tippag t on t.codtippag = venda2.codtippag" +
		 * " where venda2.seqcaixa=? and venda2.provisorio is not true";
		 */
		Connection con = null;
		ResultSet set = null;

		ArrayList<CreditoCaixaBean> lista = new ArrayList<CreditoCaixaBean>();
		try {

			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codcaixaloja);
			// ps.setInt(2, codcaixaloja);
			// ps.setInt(2, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				CreditoCaixaBean caixaBean = new CreditoCaixaBean();
				if (set.getInt("codvenda") == 0)
					caixaBean.getVenda().setCodVenda(null);
				else
					caixaBean.getVenda().setCodVenda(set.getInt("codvenda"));
				if (set.getInt("idreceber") == 0)
					caixaBean.getTitreceber().setIdFinanceiro(null);
				else
					caixaBean.getTitreceber().setIdFinanceiro(set.getInt("idreceber"));

				caixaBean.setValor(set.getDouble("valor"));
				caixaBean.getCliente().setCodcliente(set.getInt("codcliente"));
				caixaBean.getCliente().setNome(set.getString("nome"));
				caixaBean.getTippag().setDescricao(set.getString("descricao"));
				caixaBean.setCodvenda2(set.getInt("codvenda2"));
				caixaBean.setEstornado(set.getString("estornado"));
				caixaBean.setCodcaixaloja(set.getInt("seqcaixa"));
				caixaBean.getTippag().setTipo(set.getString("tipo"));
				caixaBean.getTippag().setCodtippag(set.getInt("codtippag"));
				lista.add(caixaBean);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();

				// TODO: handle exception
			}
			// fecharconexao();

		}
		return lista;
	}

	// teste
	public ArrayList<CaixaBean> lstCaixas() throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		String sql = " select codcaixaloja,numcaixa,situacao,dt_abertura,saldoinicial,saldofinal,codfilial,prestacaocontas,"
				+ "((select sum(valrecebido) from clin.credcaixaloja where codcaixaloja=caixaloja.codcaixaloja)"
				+ " - (select sum(valor) from clin.venda2 where provisorio is true and seqcaixa = codcaixaloja)) creditos,"
				+ " (select coalesce(sum(valor),0) from clin.debcaixaloja where codcaixaloja=caixaloja.codcaixaloja) debitos from clin.caixaloja "
				+ " where caixaloja.codfilial = ? order by caixaloja.codcaixaloja desc, caixaloja.dt_abertura ";

		Connection con = null;
		ResultSet set = null;
		ArrayList<CaixaBean> lista = new ArrayList<CaixaBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());

			set = ps.executeQuery();

			while (set.next()) {

				CaixaBean caixaBean = new CaixaBean();
				caixaBean.setCodCaixaLoja(set.getInt("codcaixaloja"));
				caixaBean.setNumcaixa(set.getString("numcaixa"));
				caixaBean.setDataAbertura(set.getDate("dt_abertura"));
				caixaBean.setSaldoInicial(set.getDouble("saldoinicial"));
				caixaBean.setSaldoFinal(set.getDouble("saldofinal"));
				caixaBean.setTotalCreditos(set.getDouble("creditos"));
				caixaBean.setTotalDebitos(set.getDouble("debitos"));
				caixaBean.setPrestacaocontas(set.getString("prestacaocontas"));
				caixaBean.setSituacao(set.getString("situacao"));

				lista.add(caixaBean);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();

				// TODO: handle exception
			}
			// fecharconexao();

		}
		return lista;
	}

	public ArrayList<DebitoCaixaBean> listDebito(Integer codcaixaloja) throws ProjetoException {

		Connection con = null;
		ResultSet set = null;
		String sql = "select * from clin.debcaixaloja where dt_confirmado = current_date and codfilial =? and codcaixaloja=?;";

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		ArrayList<DebitoCaixaBean> lista = new ArrayList<DebitoCaixaBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());
			ps.setInt(2, codcaixaloja);
			set = ps.executeQuery();

			while (set.next()) {

				DebitoCaixaBean caixaBean = new DebitoCaixaBean();

				caixaBean.setDt_confirmado(set.getDate("dt_confirmado"));
				caixaBean.setValor(set.getDouble("valor"));
				caixaBean.setHistorico(set.getString("historico"));
				caixaBean.setCodDeb(set.getInt("coddeb"));

				lista.add(caixaBean);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();

				// TODO: handle exception
			}
			// fecharconexao();

		}
		return lista;
	}

	public boolean validacaoCaixa() throws ProjetoException {

		Connection con = null;
		boolean rst = false;
		ResultSet set = null;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "select * from clin.caixaloja where  situacao='A'  and codfilial = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				rst = true;

			}
			
			set.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();

				// TODO: handle exception
			}
			// fecharconexao();

		}
		return rst;
	}

	public boolean validacaoCaixaDia() throws ProjetoException {

		Connection con = null;
		boolean rst = false;
		ResultSet set = null;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "select * from clin.caixaloja where  situacao='A'  and codfilial = ? and dt_abertura = current_date";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				rst = true;

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();

				// TODO: handle exception
			}
			// fecharconexao();

		}
		return rst;
	}

	public CaixaBean codCaixaLoja() throws ProjetoException {
		CaixaBean caixa = new CaixaBean();
		Connection con = null;
		ResultSet set = null;
		String sql = "select codcaixaloja, numcaixa from clin.caixaloja where  situacao='A' and codfilial=?;";
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {
				caixa = new CaixaBean();
				caixa.setCodCaixaLoja(set.getInt("codcaixaloja"));
				caixa.setNumcaixa(set.getString("numcaixa"));

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
			// fecharconexao();

		}

		return caixa;
	}

	public CaixaBean objInfo() throws ProjetoException {

		Connection con = null;
		ResultSet set = null;
		String sql = "select * from clin.caixaloja where  situacao='A' ;";

		CaixaBean caixaBen = new CaixaBean();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {

				caixaBen.setCodCaixaLoja(set.getInt("codcaixaloja"));
				caixaBen.setHoraAbertura(set.getTime("hora_abertura"));
				caixaBen.setSaldoInicial(set.getDouble("saldoinicial"));
				caixaBen.setDataAbertura(set.getDate("dt_abertura"));
				caixaBen.setTurno(set.getString("turno"));

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}

		}

		return caixaBen;

	}

	public boolean inserirDebito(Double valor, String historico, CaixaBean caixa, Connection con)
			throws ProjetoException, SQLException {
		boolean rst = false;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = " INSERT INTO clin.debcaixaloja(codcaixaloja, valor, historico, dt_confirmado,codfilial)   "
				+ "   VALUES (?, ?, ?, current_date,?)";

		try {

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, caixa.getCodCaixaLoja());
			ps.setDouble(2, valor);
			ps.setString(3, historico.toUpperCase());
			ps.setInt(4, user_session.getEmpresa().getCodfilial());
			ps.execute();

			rst = true;
			con.commit();

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			rst = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
			// fecharconexao();

		}

		return rst;
	}

	public boolean verificaCaixaAberto() throws ProjetoException {

		Connection con = null;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "select count(*) as qtd from clin.caixaloja where situacao='A' and codfilial = ?";

		ResultSet set = null;
		Integer retorno = 0;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {
				retorno = set.getInt("qtd");
			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}

		}
		if (retorno > 0) {

			return true;
		} else {

			return false;

		}

	}

	public ArrayList<PrestacaoContasBean> listCreditosPcontas(Integer codcaixaloja) throws ProjetoException {
		String sql = " select row_number() OVER (ORDER BY tipo) AS linha, t.* from ( " +

				" select codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc, "
				+ "  idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente, "
				+ " b.nomecliente, " + " b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   , "
				+ " b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id "
				+ ",false as medico " + " from ( " + " select case when caixa=true then ( "
				+ " select distinct id_fonte_receita from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ " and v1.codvenda=a.codvenda) else " + " a.idfonrec end idfonreceita,a.* from ( " + " select "
				+ " case when (select count(*) qtd from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ "  and v1.codvenda=v2.codvenda)>0 then true else false end caixa, "
				+ " v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm, "
				+ " tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd, "
				+ " v2.qtdparc, "
				+ " tp.descricao desctippag,sum(V2.valor) as Total,cast ('Cheque       ' as text) as tipo,c.CodCliente, "
				+ " c.nome nomecliente, " + " CR.confirmado, TP.codtippag, " + " TP.Periodicidade, TP.tipo as tip   , "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento " + " from clin.venda2 V2"
				+ " left join clin.venda V on (V2.codvenda=V.codvenda)"
				+ " left join clin.tippag TP on (V2.codtippag=TP.codtippag) "
				+ " left join clin.cliente c on (c.codcliente = V.codcliente)"
				+ " left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa "
				+ " and CR.ORD = V2.ORD) " + " left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " where V2.seqcaixa =?  and V2.provisorio is not true and " + " TP.tipo = 'CH' "
				+ " and coalesce(cr.estornado,'')<>'S'  and cr.idreceber is  null "
				+ " group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc, "
				+ " tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,c.CodCliente,"
				+ " TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag, "
				+ " TP.Tipo ,cr.estornado, " + " c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc "
				+ " having sum(V2.valor) >0 " + " ) a   " + " )b "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita  " + " group by   " + " codcaixaloja "
				+ " , numcaixa " + " ,dt_abertura,coalesce(taxaadm,0) " + " ,idbanco,tpdoc, " + " idfonrec,  "
				+ " codport,baixaautomaticafin, desctippag, " + " cast (tipo as TEXT),CodCliente, " + " nomecliente, "
				+ " codport " + " ,confirmado, codtippag " + " , Periodicidade,   "
				+ " estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa, "
				+ " b.taxaadm, b.cd,  b.codvenda2, b.vencimento " + " having sum(b.total) >0 " +

				" union all " +

				" select codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc, "
				+ " idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente, "
				+ " b.nomecliente, " + " b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   , "
				+ " b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id "
				+ ",case when  b.idfonreceita = idfonrec then false else true end as medico " + " from ( "
				+ " select case when caixa=true then ( " + " select distinct id_fonte_receita from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ " and v1.codvenda=a.codvenda) else " + " a.idfonrec end idfonreceita,a.* from ( " + " select "
				+ " case when (select count(*) qtd from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ " and v1.codvenda=v2.codvenda)>0 then true else false end caixa, "
				+ " v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm, "
				+ " tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd, "
				+ " v2.qtdparc, "
				+ " tp.descricao desctippag,sum(V2.valor) as Total,cast ('Dinheiro       ' as text) as tipo,c.CodCliente, "
				+ " c.nome nomecliente, " + " CR.confirmado, TP.codtippag, " + " TP.Periodicidade, TP.tipo as tip   , "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento " + " from clin.venda2 V2"
				+ " left join clin.venda V on (V2.codvenda=V.codvenda) "
				+ " left join clin.tippag TP on (V2.codtippag=TP.codtippag) "
				+ " left join clin.cliente c on (c.codcliente = V.codcliente) "
				+ " left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa  "
				+ " and CR.ORD = V2.ORD) " + " left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " where V2.seqcaixa =?  and V2.provisorio is not true and " + " TP.tipo = 'D' "
				+ " and coalesce(cr.estornado,'')<>'S'  and cr.idreceber is  null"
				+ " group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc, "
				+ " tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,c.CodCliente,"
				+ " TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag, "
				+ " TP.Tipo ,cr.estornado, " + " c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc "
				+ " having sum(V2.valor) >0 " + " ) a   " + " )b "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita   " + " group by   " + " codcaixaloja "
				+ " , numcaixa " + " ,dt_abertura,coalesce(taxaadm,0) " + " ,idbanco,tpdoc, " + " idfonrec,  "
				+ " codport,baixaautomaticafin, desctippag, " + " cast (tipo as TEXT),CodCliente, " + " nomecliente, "
				+ " codport " + " ,confirmado, codtippag " + " , Periodicidade,   "
				+ " estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa, "
				+ " b.taxaadm, b.cd,  b.codvenda2, b.vencimento " + " having sum(b.total) >0 " +

				" union all " +

				" select  " + " codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc, "
				+ " idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente, "
				+ " b.nomecliente, " + " b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   , "
				+ " b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id "
				+ ",false as medico " + " from ( " + " select case when caixa=true then ( "
				+ " select distinct id_fonte_receita from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ " and v1.codvenda=a.codvenda) else " + " a.idfonrec end idfonreceita,a.* from ( " + " select "
				+ " case when (select count(*) qtd from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ " and v1.codvenda=v2.codvenda)>0 then true else false end caixa, "
				+ " v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm, "
				+ " tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd, "
				+ " v2.qtdparc, "
				+ " tp.descricao desctippag,sum(V2.valor) as Total,cast ('Cartao       ' as text) as tipo,TP.CodCliente, "
				+ " c.nome nomecliente, " + " CR.confirmado, TP.codtippag, " + " TP.Periodicidade, TP.tipo as tip   , "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento " + " from clin.venda2 V2 "
				+ " left join clin.tippag TP on (V2.codtippag=TP.codtippag) "
				+ " left join clin.cliente c on c.codcliente = tp.codcliente "
				+ " left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa "
				+ " and CR.ORD = V2.ORD) " + " left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " where V2.seqcaixa =?  and V2.provisorio is not true and " + " TP.tipo = 'CT' "
				+ " and coalesce(cr.estornado,'')<>'S'  and cr.idreceber is  null"
				+ " group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc, "
				+ " tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,TP.CodCliente, "
				+ " TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag, "
				+ " TP.Tipo ,cr.estornado, " + " c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc "
				+ " having sum(V2.valor) >0 " + " ) a   " + " )b "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita            " + " group by   " + " codcaixaloja "
				+ " , numcaixa " + " ,dt_abertura,coalesce(taxaadm,0) " + " ,idbanco,tpdoc, " + " idfonrec,  "
				+ " codport,baixaautomaticafin, desctippag, " + " cast (tipo as TEXT),CodCliente, " + " nomecliente, "
				+ " codport " + " ,confirmado, codtippag " + " , Periodicidade,   "
				+ " estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa, "
				+ " b.taxaadm, b.cd,  b.codvenda2, b.vencimento " + " having sum(b.total) >0 " +

				" union all" +

				" select cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,tp.idbanco, "
				+ " tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,'C' cd, "
				+ " tp.descricao desctippag,sum(V2.valor) as Total,'Crediario    ' as tipo,TP.CodCliente, "
				+ " c.nome nomecli, "
				+ " CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip   ,  "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento, fr.descricao as descfonrec, fr.id "
				+ ",false as medico " + " from clin.venda2 V2 " + " join clin.tippag TP on (V2.codtippag=TP.codtippag) "
				+ " left join clin.cliente c on c.codcliente = tp.codcliente "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = TP.IDFONREC "
				+ " join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD) "
				+ " join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " where V2.seqcaixa = ? and V2.provisorio is not true and " + " TP.tipo = 'CD' "
				+ " and coalesce(cr.estornado,'')<>'S' and cr.idreceber is  null "
				+ " group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco,tp.tpdoc,  "
				+ " tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,TP.CodCliente,  "
				+ " CR.confirmado, V2.qtdparc, TP.Periodicidade, TP.codtippag, TP.Tipo ,cr.estornado, "
				+ " c.nome, v2.vencimento, fr.descricao, fr.id, v2.codvenda " + " having sum(V2.valor) >0 " +

				" union all " +

				" select cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm, "
				+ " tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,'C' cd, "
				+ " tp.descricao desctippag,V2.valor as Total,'Fatura       ' as tipo,V.CODCLIENTE as CodCli, "
				+ " c.nome nomecliente, "
				+ " CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip    , "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento, fr.descricao as descfonrec, fr.id "
				+ ",false as medico " + " from clin.venda2 V2 "
				+ " left join clin.tippag TP on (V2.codtippag=TP.codtippag) "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = TP.IDFONREC "
				+ "left  join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD) "
				+ "left  join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " left join clin.Venda V on V2.CODVENDA = V.CODVENDA "
				+ " left join clin.cliente c on c.codcliente = v.codcliente "
				+ " where V2.seqcaixa = ? and V2.provisorio is not true and " + " TP.tipo = 'FT' "
				+ " and coalesce(cr.estornado,'')<>'S' and cr.idreceber is  null " + " and V2.valor >0 " +

				" union all " +

				" select cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm, "
				+ " tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,'C' cd, "
				+ " tp.descricao desctippag,sum(V2.valor) as Total,'Sem Movimento' as tipo,TP.CodCliente,c.nome nomecli, "
				+ " CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip  , "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento, fr.descricao as descfonrec, fr.id "
				+ ",false as medico " + " from clin.venda2 V2 " + " join clin.tippag TP on (V2.codtippag=TP.codtippag) "
				+ " left join clin.cliente c on c.codcliente = tp.codcliente "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = TP.IDFONREC "
				+ " join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD) "
				+ " join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " where V2.seqcaixa = ? and V2.provisorio is not true and " + " TP.tipo = 'SM' "
				+ " and coalesce(cr.estornado,'')<>'S' and cr.idreceber is  null "
				+ " group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, "
				+ " tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao , "
				+ " tipo,TP.CodCliente,CR.confirmado,V2.qtdparc, TP.Periodicidade, TP.codtippag, TP.Tipo ,cr.estornado, "
				+ " c.nome, v2.vencimento, fr.descricao, fr.id, v2.codvenda " +

				" union all " +

				" select codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc, "
				+ " idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente, "
				+ " b.nomecliente, " + " b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   , "
				+ " b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id "
				+ ",false as medico " + " from ( " + " select case when caixa=true then ( "
				+ " select distinct id_fonte_receita from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true  "
				+ " and v1.codvenda=a.codvenda) else  " + " a.idfonrec end idfonreceita,a.* from (  " + " select "
				+ " case when (select count(*) qtd from clin.func "
				+ " join clin.venda1 v1 on v1.codangariador = func.codfunc " + " where func.caixa is true "
				+ " and v1.codvenda=v2.codvenda)>0 then true else false end caixa, "
				+ " v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm, "
				+ " tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd, "
				+ " v2.qtdparc, "
				+ " tp.descricao desctippag,sum(V2.valor) as Total,cast ('Nota Promissoria       ' as text) as tipo,c.CodCliente, "
				+ " c.nome nomecliente, " + " CR.confirmado, TP.codtippag, " + " TP.Periodicidade, TP.tipo as tip   , "
				+ " cr.estornado, v2.codvenda codvenda2, v2.vencimento " + " from clin.venda2 V2 "
				+ " left join clin.venda V on (V2.codvenda=V.codvenda) "
				+ " left join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " left join clin.cliente c on (c.codcliente = V.codcliente) "
				+ " left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa "
				+ " and CR.ORD = V2.ORD) " + " left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja "
				+ " where V2.seqcaixa =?  and V2.provisorio is not true and " + " TP.tipo = 'NP' "
				+ " and coalesce(cr.estornado,'')<>'S'  and cr.idreceber is  null"
				+ " group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc, "
				+ " tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,c.CodCliente, "
				+ " TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag,  "
				+ " TP.Tipo ,cr.estornado,  " + " c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc "
				+ " having sum(V2.valor) >0 " + " ) a   " + " )b  "
				+ " LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita      " + " group by   " + " codcaixaloja "
				+ " , numcaixa " + " ,dt_abertura,coalesce(taxaadm,0) " + " ,idbanco,tpdoc, " + " idfonrec,  "
				+ " codport,baixaautomaticafin, desctippag, " + " cast (tipo as TEXT),CodCliente, " + " nomecliente, "
				+ " codport " + " ,confirmado, codtippag " + " , Periodicidade,   "
				+ " estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa, "
				+ " b.taxaadm, b.cd,  b.codvenda2, b.vencimento " + " having sum(b.total) >0)  t";
		// argemiro vencimento
		Connection con = null;
		ResultSet set = null;

		ArrayList<PrestacaoContasBean> lista = new ArrayList<PrestacaoContasBean>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codcaixaloja);
			// ps.setInt(2, user_session.getEmpresa().getCodfilial());
			ps.setInt(2, codcaixaloja);
			ps.setInt(3, codcaixaloja);
			ps.setInt(4, codcaixaloja);
			ps.setInt(5, codcaixaloja);
			ps.setInt(6, codcaixaloja);
			ps.setInt(7, codcaixaloja);
			// ps.setInt(2, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				PrestacaoContasBean pcontas = new PrestacaoContasBean();

				if (set.getString("cd").equals("C")) {
					TituloReceberBean credito = new TituloReceberBean();
					credito.setVencimento(set.getDate("vencimento"));
					credito.getCliente().setCodcliente(set.getInt("CodCliente"));
					credito.getCliente().setNome(set.getString("nomecliente"));
					credito.getTipoDocumento().setCodtipodocumento(set.getInt("tpdoc"));
					credito.getPortador().setCodportador(set.getInt("codport"));
					credito.getFonrec().setId(set.getInt("id"));
					credito.setValor(set.getDouble("total"));
					credito.getTituloBaixareceber().getBanco().setId(set.getInt(("idbanco")));
					pcontas.setCreddeb("CD");
					pcontas.getCaixa().setCodCaixaLoja(set.getInt("codcaixaloja"));
					pcontas.getCaixa().setNumcaixa(set.getString("numcaixa"));
					pcontas.getCaixa().setDataAbertura(set.getDate("dt_abertura"));
					pcontas.getTippag().setBaixaAutFin(set.getBoolean("baixaautomaticafin"));
					pcontas.setValor(set.getDouble("total"));
					pcontas.setTipo(set.getString("tip"));
					pcontas.getCliente().setCodcliente(set.getInt("CodCliente"));
					pcontas.getCliente().setNome(set.getString("nomecliente"));
					pcontas.setQtdparc(set.getInt("qtdparc"));
					pcontas.setPeriodicidade(set.getInt("periodicidade"));
					pcontas.setEstornado(set.getString("estornado"));
					pcontas.setTituloreceber(credito);
					pcontas.getTippag().setCodtippag(set.getInt("codtippag"));
					pcontas.getTippag().setDescricao(set.getString("desctippag"));
					pcontas.getTippag().setTaxaAdm(set.getDouble("taxaadm"));
					pcontas.setCodVenda2(set.getInt("codvenda2"));
					pcontas.getDescfonrec().setDescricao(set.getString("descfonrec"));
					pcontas.setMedico(set.getBoolean("medico"));

				}
				// credito.setCodCred(set.getInt("linha"));
				// credito.setValor(se

				lista.add(pcontas);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}

		}
		return lista;
	}

	public ArrayList<PrestacaoContasBean> listCreditosPcontasRec(Integer codcaixaloja) throws ProjetoException {

		String sql = "  select row_number() OVER (ORDER BY tipo) AS linha, t.* from ( "
				+ "    				 select codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc,  "
				+ "   				   idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente,  "
				+ " 				  b.nomecliente,  "
				+ " 				  b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   ,  "
				+ " 				  b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id  "
				+ " 				 ,false as medico, b.valor, b.venctorec , codcred " + " 				  from (  "
				+ " 				  select case when caixa=true then (  "
				+ " 				  select distinct id_fonte_receita from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				  and v1.codvenda=a.codvenda) else  "
				+ " 				  a.idfonrec end idfonreceita,a.* from (  " + " 				  select  "
				+ " 				  case when (select count(*) qtd from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				   and v1.codvenda=v2.codvenda)>0 then true else false end caixa,  "
				+ " 				  v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,  "
				+ " 				  tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd,  "
				+ " 				  v2.qtdparc,  "
				+ " 				  tp.descricao desctippag,sum(V2.valor) as Total,cast ('Cheque       ' as text) as tipo,c.CodCliente,  "
				+ " 				  c.nome nomecliente,  " + " 				  CR.confirmado, TP.codtippag,  "
				+ " 				  TP.Periodicidade, TP.tipo as tip   ,  "
				+ " 				  cr.estornado, v2.codvenda codvenda2, v2.vencimento, rec.valor, rec.vencimento venctorec, "
				+ "                   cr.codcred " + " 				  from clin.venda2 V2 "
				+ " 				  left join clin.venda V on (V2.codvenda=V.codvenda) "
				+ " 				  left join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " 				  left join clin.cliente c on (c.codcliente = V.codcliente) "
				+ " 				  left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa  "
				+ " 				  and CR.ORD = V2.ORD)  "
				+ " 				  left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ "                   left join clin.recdup rec on rec.idreceber = cr.idreceber "
				+ " 				  where V2.seqcaixa =?  and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'CH'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ "                   and cr.idreceber is not null "
				+ "  				  group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc,  "
				+ " 				  tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,c.CodCliente, "
				+ " 				  TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag,  "
				+ " 				  TP.Tipo ,cr.estornado,  "
				+ " 				  c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc ,rec.valor, rec.vencimento  ,cr.codcred "
				+ " 				  having sum(V2.valor) >0  " + " 				  ) a    " + " 				  )b  "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita   "
				+ " 				  group by    " + " 				  codcaixaloja  "
				+ " 				  , numcaixa  " + " 				  ,dt_abertura,coalesce(taxaadm,0)  "
				+ " 				  ,idbanco,tpdoc,  " + " 				  idfonrec,   "
				+ " 				  codport,baixaautomaticafin, desctippag,  "
				+ " 				  cast (tipo as TEXT),CodCliente,  " + " 				  nomecliente,  "
				+ " 				  codport  " + " 				  ,confirmado, codtippag  "
				+ " 				  , Periodicidade,    "
				+ " 				  estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa,  "
				+ " 				  b.taxaadm, b.cd,  b.codvenda2, b.vencimento , b.valor, b.venctorec , codcred "
				+ " 				  having sum(b.total) >0  " + " 				 union all  "
				+ " 				 select codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc,  "
				+ " 				  idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente,  "
				+ " 				  b.nomecliente,  "
				+ " 				  b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   ,  "
				+ " 				  b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id  "
				+ " 				 ,case when  b.idfonreceita = idfonrec then false else true end as medico , "
				+ "                  b.valor, b.venctorec , codcred " + " 				  from (  "
				+ " 				  select case when caixa=true then (  "
				+ " 				  select distinct id_fonte_receita from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				  and v1.codvenda=a.codvenda) else  "
				+ " 				  a.idfonrec end idfonreceita,a.* from (  " + " 				  select  "
				+ " 				  case when (select count(*) qtd from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				  and v1.codvenda=v2.codvenda)>0 then true else false end caixa,  "
				+ " 				  v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,  "
				+ " 				  tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd,  "
				+ " 				  v2.qtdparc,  "
				+ " 				  tp.descricao desctippag,sum(V2.valor) as Total,cast ('Dinheiro       ' as text) as tipo,c.CodCliente,  "
				+ " 				  c.nome nomecliente,  " + " 				  CR.confirmado, TP.codtippag,  "
				+ " 				  TP.Periodicidade, TP.tipo as tip   ,  "
				+ " 					  cr.estornado, v2.codvenda codvenda2, v2.vencimento , rec.valor, rec.vencimento venctorec , codcred "
				+ " 				  from clin.venda2 V2 "
				+ " 				  left join clin.venda V on (V2.codvenda=V.codvenda)  "
				+ " 				  left join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " 				  left join clin.cliente c on (c.codcliente = V.codcliente)  "
				+ " 				  left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa   "
				+ " 				  and CR.ORD = V2.ORD)  "
				+ " 				  left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ "                   left join clin.recdup rec on rec.idreceber = cr.idreceber                   "
				+ " 				  where V2.seqcaixa =?  and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'D'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ "                    and cr.idreceber is not null "
				+ " 				  group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc,  "
				+ " 				  tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,c.CodCliente, "
				+ " 				  TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag,  "
				+ " 				  TP.Tipo ,cr.estornado,  "
				+ " 				  c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc , rec.valor, rec.vencimento  , codcred "
				+ " 				  having sum(V2.valor) >0  " + " 				  ) a    " + " 				  )b  "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita    "
				+ " 				  group by    " + " 				  codcaixaloja  "
				+ " 				  , numcaixa  " + " 				  ,dt_abertura,coalesce(taxaadm,0)  "
				+ " 				  ,idbanco,tpdoc,  " + " 				  idfonrec,   "
				+ " 				  codport,baixaautomaticafin, desctippag,  "
				+ " 				  cast (tipo as TEXT),CodCliente,  " + " 				  nomecliente,  "
				+ " 				  codport  " + " 				  ,confirmado, codtippag  "
				+ " 				  , Periodicidade,    "
				+ " 				  estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa,  "
				+ " 				  b.taxaadm, b.cd,  b.codvenda2, b.vencimento ,b.valor, b.venctorec , codcred "
				+ " 				  having sum(b.total) >0  " + " 				 union all  "
				+ " 				 select   "
				+ " 				  codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc,  "
				+ " 				  idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente,  "
				+ " 				  b.nomecliente,  "
				+ " 				  b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   ,  "
				+ " 				  b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id  "
				+ " 				 ,false as medico ,b.valor, b.venctorec , codcred " + " 				  from (  "
				+ " 				  select case when caixa=true then (  "
				+ " 				  select distinct id_fonte_receita from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				  and v1.codvenda=a.codvenda) else  "
				+ " 				  a.idfonrec end idfonreceita,a.* from (  " + " 				  select  "
				+ " 				  case when (select count(*) qtd from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				  and v1.codvenda=v2.codvenda)>0 then true else false end caixa,  "
				+ " 				  v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,  "
				+ " 				  tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd,  "
				+ " 				  v2.qtdparc,  "
				+ " 				  tp.descricao desctippag,sum(V2.valor) as Total,cast ('Cartao       ' as text) as tipo,TP.CodCliente,  "
				+ " 				  c.nome nomecliente,  " + " 				  CR.confirmado, TP.codtippag,  "
				+ " 				  TP.Periodicidade, TP.tipo as tip   ,  "
				+ " 				  cr.estornado, v2.codvenda codvenda2, v2.vencimento , rec.valor, rec.vencimento venctorec ,codcred "
				+ " 				  from clin.venda2 V2  "
				+ " 				  left join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " 				  left join clin.cliente c on c.codcliente = tp.codcliente  "
				+ " 				  left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa  "
				+ " 				  and CR.ORD = V2.ORD)  "
				+ " 				  left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ "                   left join clin.recdup rec on rec.idreceber = cr.idreceber                   "
				+ " 				  where V2.seqcaixa =?  and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'CT'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ "                    and cr.idreceber is not null "
				+ " 				  group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc,  "
				+ " 				  tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,TP.CodCliente,  "
				+ " 				  TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag,  "
				+ " 				  TP.Tipo ,cr.estornado,  "
				+ " 				  c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc , rec.valor, rec.vencimento , codcred  "
				+ " 				  having sum(V2.valor) >0  " + " 				  ) a    " + " 				  )b  "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita             "
				+ " 				  group by    " + " 				  codcaixaloja  "
				+ " 				  , numcaixa  " + " 				  ,dt_abertura,coalesce(taxaadm,0)  "
				+ " 				  ,idbanco,tpdoc,  " + " 				  idfonrec,   "
				+ " 				  codport,baixaautomaticafin, desctippag,  "
				+ " 				  cast (tipo as TEXT),CodCliente,  " + " 				  nomecliente,  "
				+ " 				  codport  " + " 				  ,confirmado, codtippag  "
				+ " 				  , Periodicidade,    "
				+ "  				  estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa,  "
				+ " 				  b.taxaadm, b.cd,  b.codvenda2, b.vencimento ,b.valor, b.venctorec , codcred "
				+ " 				  having sum(b.total) >0  " + " 				 union all "
				+ " 				 select cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,tp.idbanco,  "
				+ " 				  tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,'C' cd,  "
				+ " 				  tp.descricao desctippag,sum(V2.valor) as Total,'Crediario    ' as tipo,TP.CodCliente,  "
				+ " 				  c.nome nomecli,  "
				+ " 				  CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip   ,   "
				+ " 				  cr.estornado, v2.codvenda codvenda2, v2.vencimento, fr.descricao as descfonrec, fr.id  "
				+ " 				 ,false as medico ,0 valor, null venctorec, codcred  "
				+ " 				  from clin.venda2 V2  "
				+ " 				  join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " 				  left join clin.cliente c on c.codcliente = tp.codcliente  "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = TP.IDFONREC  "
				+ " 				  join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD)  "
				+ " 				  join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ " 				  where V2.seqcaixa = ? and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'CD'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ "                    and cr.idreceber is not null "
				+ " 				  group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco,tp.tpdoc,   "
				+ " 				  tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,TP.CodCliente,   "
				+ " 				  CR.confirmado, V2.qtdparc, TP.Periodicidade, TP.codtippag, TP.Tipo ,cr.estornado,  "
				+ " 				  c.nome, v2.vencimento, fr.descricao, fr.id, v2.codvenda , codcred "
				+ " 				  having sum(V2.valor) >0  " + " 				 union all  "
				+ " 				 select cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,  "
				+ " 				  tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,'C' cd,  "
				+ " 				  tp.descricao desctippag,V2.valor as Total,'Fatura       ' as tipo,V.CODCLIENTE as CodCli,  "
				+ " 				  c.nome nomecliente,  "
				+ " 				  CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip    ,  "
				+ " 				  cr.estornado, v2.codvenda codvenda2, v2.vencimento, fr.descricao as descfonrec, fr.id  "
				+ " 				 ,false as medico ,0 valor, null venctorec , codcred "
				+ " 				  from clin.venda2 V2  "
				+ " 				  left join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = TP.IDFONREC  "
				+ " 				 left  join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD)  "
				+ "  				 left  join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ " 				  left join clin.Venda V on V2.CODVENDA = V.CODVENDA  "
				+ " 				  left join clin.cliente c on c.codcliente = v.codcliente  "
				+ " 				  where V2.seqcaixa = ? and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'FT'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ " 				  and V2.valor >0  " + " 				 union all  "
				+ "  				 select cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,  "
				+ " 				  tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,'C' cd,  "
				+ " 				  tp.descricao desctippag,sum(V2.valor) as Total,'Sem Movimento' as tipo,TP.CodCliente,c.nome nomecli,  "
				+ " 				  CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip  ,  "
				+ " 				  cr.estornado, v2.codvenda codvenda2, v2.vencimento, fr.descricao as descfonrec, fr.id  "
				+ " 				 ,false as medico ,0  valor, null venctorec , codcred "
				+ " 				  from clin.venda2 V2  "
				+ " 				  join clin.tippag TP on (V2.codtippag=TP.codtippag)  "
				+ " 				  left join clin.cliente c on c.codcliente = tp.codcliente  "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = TP.IDFONREC  "
				+ " 				  join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD)  "
				+ " 				  join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ " 				  where V2.seqcaixa = ? and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'SM'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ "                     and cr.idreceber is not null "
				+ " 				  group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco,  "
				+ " 				  tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,  "
				+ " 				  tipo,TP.CodCliente,CR.confirmado,V2.qtdparc, TP.Periodicidade, TP.codtippag, TP.Tipo ,cr.estornado,  "
				+ " 				  c.nome, v2.vencimento, fr.descricao, fr.id, v2.codvenda, codcred "
				+ " 				 union all  "
				+ " 				 select codcaixaloja, numcaixa,dt_abertura,coalesce(taxaadm,0) taxaadm,idbanco,tpdoc,  "
				+ " 				  idfonrec, codport, baixaautomaticafin, cd,desctippag,sum(b.total) as Total,b.tipo,b.CodCliente,  "
				+ " 				  b.nomecliente,  "
				+ "  				  b.confirmado, b.codtippag, MAX(b.qtdparc) qtdparc, b.Periodicidade, b.tip   ,  "
				+ " 				  b.estornado, b.codvenda2, b.vencimento, fr.descricao descfonrec, b.idfonreceita id  "
				+ " 				 ,false as medico ,b.valor, b.venctorec , codcred " + " 				  from (  "
				+ " 				  select case when caixa=true then (  "
				+ " 				  select distinct id_fonte_receita from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true   "
				+ " 				  and v1.codvenda=a.codvenda) else   "
				+ " 				  a.idfonrec end idfonreceita,a.* from (   " + " 				  select  "
				+ " 				  case when (select count(*) qtd from clin.func  "
				+ " 				  join clin.venda1 v1 on v1.codangariador = func.codfunc  "
				+ " 				  where func.caixa is true  "
				+ " 				  and v1.codvenda=v2.codvenda)>0 then true else false end caixa,  "
				+ " 				  v2.codvenda,cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,coalesce(TP.taxaadm,0) taxaadm,  "
				+ " 				  tp.idbanco,tp.tpdoc, tp.idfonrec, tp.codport, tp.baixaautomaticafin,cast ('C' as text)  cd,  "
				+ " 				  v2.qtdparc,  "
				+ " 				  tp.descricao desctippag,sum(V2.valor) as Total,cast ('Nota Promissoria       ' as text) as tipo,c.CodCliente,  "
				+ " 				  c.nome nomecliente,  " + " 				  CR.confirmado, TP.codtippag,  "
				+ " 				  TP.Periodicidade, TP.tipo as tip   ,  "
				+ " 				  cr.estornado, v2.codvenda codvenda2, v2.vencimento , rec.valor, rec.vencimento venctorec,codcred  "
				+ "  				  from clin.venda2 V2  "
				+ " 				  left join clin.venda V on (V2.codvenda=V.codvenda)  "
				+ " 				  left join clin.tippag TP on (V2.codtippag=TP.codtippag)   "
				+ " 				  left join clin.cliente c on (c.codcliente = V.codcliente)  "
				+ " 				  left join clin.CREDCAIXALOJA CR on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa  "
				+ " 				  and CR.ORD = V2.ORD)  "
				+ " 				  left join clin.caixaloja cx on cx.codcaixaloja = cr.codcaixaloja  "
				+ "                   left join clin.recdup rec on rec.idreceber = cr.idreceber "
				+ " 				  where V2.seqcaixa =?  and V2.provisorio is not true and  "
				+ " 				  TP.tipo = 'NP'  " + " 				  and coalesce(cr.estornado,'')<>'S'  "
				+ "                    and cr.idreceber is not null "
				+ " 				  group by cx.codcaixaloja, cx.numcaixa,cx.dt_abertura,TP.taxaadm,tp.idbanco, tp.tpdoc,  "
				+ " 				  tp.idfonrec, tp.codport, tp.baixaautomaticafin,tp.descricao ,tipo,c.CodCliente,  "
				+ " 				  TP.tpdoc,tp.idfonrec,TP.codport,CR.confirmado, TP.Periodicidade, TP.codtippag,   "
				+ " 				  TP.Tipo ,cr.estornado,   "
				+ " 				  c.nome, v2.vencimento,  v2.codvenda,v2.qtdparc ,rec.valor, rec.vencimento  , codcred "
				+ " 				  having sum(V2.valor) >0  " + " 				  ) a    " + " 				  )b   "
				+ " 				  LEFT JOIN CLIN.FONREC FR ON FR.ID = b.idfonreceita       "
				+ " 				  group by    " + "  				  codcaixaloja  "
				+ " 				  , numcaixa  " + " 				  ,dt_abertura,coalesce(taxaadm,0)  "
				+ " 				  ,idbanco,tpdoc,  " + " 				  idfonrec,   "
				+ " 				  codport,baixaautomaticafin, desctippag,  "
				+ " 				  cast (tipo as TEXT),CodCliente,  " + " 				  nomecliente,  "
				+ " 				  codport  " + " 				  ,confirmado, codtippag  "
				+ " 				  , Periodicidade,    "
				+ " 				  estornado,  vencimento,  tip, fr.descricao,  b.idfonreceita, b.caixa,  "
				+ " 				  b.taxaadm, b.cd,  b.codvenda2, b.vencimento ,b.valor, b.venctorec , codcred "
				+ " 				  having sum(b.total) >0)  t";

		Connection con = null;
		ResultSet set = null;

		ArrayList<PrestacaoContasBean> lista = new ArrayList<PrestacaoContasBean>();
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codcaixaloja);
			// ps.setInt(2, user_session.getEmpresa().getCodfilial());
			ps.setInt(2, codcaixaloja);
			ps.setInt(3, codcaixaloja);
			ps.setInt(4, codcaixaloja);
			ps.setInt(5, codcaixaloja);
			ps.setInt(6, codcaixaloja);
			ps.setInt(7, codcaixaloja);
			// ps.setInt(2, user_session.getEmpresa().getCodfilial());
			set = ps.executeQuery();

			while (set.next()) {

				PrestacaoContasBean pcontas = new PrestacaoContasBean();

				if (set.getString("cd").equals("C")) {
					TituloReceberBean credito = new TituloReceberBean();
					credito.setVencimento(set.getDate("vencimento"));
					credito.getCliente().setCodcliente(set.getInt("CodCliente"));
					credito.getCliente().setNome(set.getString("nomecliente"));
					credito.getTipoDocumento().setCodtipodocumento(set.getInt("tpdoc"));
					credito.getPortador().setCodportador(set.getInt("codport"));
					credito.getFonrec().setId(set.getInt("id"));
					credito.setValor(set.getDouble("total"));
					credito.getTituloBaixareceber().getBanco().setId(set.getInt(("idbanco")));
					TituloReceberBean tituloAnt = new TituloReceberBean();
					tituloAnt.setVencimento(set.getDate("venctorec"));
					tituloAnt.setValor(set.getDouble("valor"));
					pcontas.setTituloReceberAnterior(tituloAnt);
					pcontas.setCreddeb("CD");
					pcontas.getCaixa().setCodCaixaLoja(set.getInt("codcaixaloja"));
					pcontas.getCaixa().setNumcaixa(set.getString("numcaixa"));
					pcontas.getCaixa().setDataAbertura(set.getDate("dt_abertura"));
					pcontas.getTippag().setBaixaAutFin(set.getBoolean("baixaautomaticafin"));
					pcontas.setValor(set.getDouble("total"));
					pcontas.setTipo(set.getString("tip"));
					pcontas.getCliente().setCodcliente(set.getInt("CodCliente"));
					pcontas.getCliente().setNome(set.getString("nomecliente"));
					pcontas.setQtdparc(set.getInt("qtdparc"));
					pcontas.setPeriodicidade(set.getInt("periodicidade"));
					pcontas.setEstornado(set.getString("estornado"));
					pcontas.setTituloreceber(credito);
					pcontas.getTippag().setCodtippag(set.getInt("codtippag"));
					pcontas.getTippag().setDescricao(set.getString("desctippag"));
					pcontas.getTippag().setTaxaAdm(set.getDouble("taxaadm"));
					pcontas.setCodVenda2(set.getInt("codvenda2"));
					pcontas.setCodCred(set.getInt("codcred"));
					pcontas.getDescfonrec().setDescricao(set.getString("descfonrec"));
					pcontas.setMedico(set.getBoolean("medico"));

				}
				// credito.setCodCred(set.getInt("linha"));
				// credito.setValor(se

				lista.add(pcontas);

			}

			set.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
			// fecharconexao();

		}
		return lista;
	}

	public boolean setarCaixaPrestadoContas(CaixaBean caixa, Connection con) throws ProjetoException {
		PreparedStatement ps = null;

		boolean result = false;

		try {
			// insere na tabela caixadiario

			String sql = "UPDATE clin.caixaloja set prestacaocontas='S', opprestacaocontas=? where codcaixaloja=? and codfilial=?";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			ps = con.prepareStatement(sql);

			ps.setInt(1, user_session.getCodfunc());
			ps.setInt(2, caixa.getCodCaixaLoja());
			ps.setInt(3, user_session.getEmpresa().getCodfilial());

			ps.executeUpdate();

			result = true;

			ps.close();
		} catch (Exception sqle) {
			result = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, sqle.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

		return result;
	}

	public boolean estornaRecebimento(CreditoCaixaBean cred, Connection con) throws ProjetoException {
		PreparedStatement ps = null;

		boolean result = false;
		String sql = "";

		try {
			// insere na tabela caixadiario

			sql = "UPDATE clin.credcaixaloja set estornado='S' where codvenda2=? and codfilial=?";

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			ps = con.prepareStatement(sql);

			ps.setInt(1, cred.getCodvenda2());
			ps.setInt(2, user_session.getEmpresa().getCodfilial());

			ps.executeUpdate();

			sql = "update  clin.venda2 set estornado='S' where codvenda2=?";

			ps = con.prepareStatement(sql);

			ps.setInt(1, cred.getCodvenda2());

			ps.executeUpdate();

			sql = "update  clin.venda set status='PE' where codvenda=?";

			ps = con.prepareStatement(sql);

			ps.setInt(1, cred.getVenda().getCodVenda());

			ps.executeUpdate();

			sql = " INSERT INTO clin.debcaixaloja(codcaixaloja, valor, historico, dt_confirmado,codfilial, estornado)   "
					+ "   VALUES (?, ?, ?, current_date,?,'S')";

			ps = con.prepareStatement(sql);

			ps.setInt(1, cred.getCodcaixaloja());

			ps.setDouble(2, cred.getValor());

			ps.setString(3, "ESTORNO REFERENTE AO CREDITO");

			ps.setInt(4, user_session.getEmpresa().getCodfilial());

			ps.execute();

			con.commit();
			result = true;
			
			ps.close();
		} catch (Exception sqle) {
			sqle.printStackTrace();
			result = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, sqle.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}

		return result;
	}

	public ArrayList<AgrupPrestContasBean> carregaParcelasRecebimentoVendaCartao(Integer codcaixaloja,
			Integer codtippag, Integer codfonrec, Connection con) throws ProjetoException, SQLException {

		// argemiro
		ArrayList<AgrupPrestContasBean> lista = new ArrayList<AgrupPrestContasBean>();
		ResultSet set = null;
		String sql = "select v2.codvenda,V2.valor,tp.tipo as tipo,TP.CodCli,TP.tpdoc,"
				+ " tp.idfonrec, fon.descricao, TP.codport,CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip   ,"
				+ " cr.estornado, v2.ord" + " from clin.venda2 V2"
				+ " join clin.tippag TP on (V2.codtippag=TP.codtippag)"
				+ " join clin.fonrec fon on (TP.idfonrec=fon.id)"
				+ " join clin.credcaixaloja cr on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD)"
				+ " where V2.seqcaixa = ? and tp.idfonrec = ? and v2.codtippag=?"
				+ " and coalesce(cr.estornado,'')<>'S'";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codcaixaloja);
			ps.setInt(2, codfonrec);
			ps.setInt(3, codtippag);
			set = ps.executeQuery();
			int ord = 1;

			while (set.next()) {
				AgrupPrestContasBean agrup = new AgrupPrestContasBean();
				agrup.setTipo(set.getString("tipo"));
				agrup.setCodvenda(set.getInt("codvenda"));
				agrup.setValor(set.getDouble("valor"));
				agrup.setOrd(set.getInt("ord"));
				agrup.setQtdparc(set.getInt("qtdparc"));
				agrup.setQtdparcrestante(set.getInt("qtdparc"));
				agrup.setValorparcela(set.getDouble("valor") / set.getInt("qtdparc"));
				agrup.setValorparcelaRestante(set.getDouble("valor") / set.getInt("qtdparc"));
				ord = ord + 1;
				lista.add(agrup);
			}

			set.close();
			ps.close();
		} catch (SQLException e) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}

		return lista;
	}

	public ArrayList<AgrupPrestContasBean> carregaParcelasRecebimentoVendaExcetoCartao(Integer codcaixaloja,
			Integer codtippag, Connection con, Integer codvenda2) throws ProjetoException, SQLException {

		ArrayList<AgrupPrestContasBean> lista = new ArrayList<AgrupPrestContasBean>();
		ResultSet set = null;
		String sql = "select v2.codvenda,V2.valor,tp.tipo as tipo,TP.CodCli,TP.tpdoc,"
				+ " tp.idfonrec,TP.codport,CR.confirmado, TP.codtippag, V2.qtdparc, TP.Periodicidade, TP.tipo as tip   ,"
				+ " cr.estornado, v2.ord" + " from clin.venda2 V2"
				+ " join clin.tippag TP on (V2.codtippag=TP.codtippag)"
				+ " join clin.credcaixaloja cr on (cr.codvenda=V2.codvenda and CR.codcaixaloja=v2.seqcaixa and CR.ORD = V2.ORD)"
				+ " where V2.seqcaixa = ?    and v2.codtippag=?"
				+ " and coalesce(cr.estornado,'')<>'S' and v2.codvenda2=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, codcaixaloja);
			ps.setInt(2, codtippag);
			ps.setInt(3, codvenda2);
			set = ps.executeQuery();
			int ord = 1;

			while (set.next()) {
				AgrupPrestContasBean agrup = new AgrupPrestContasBean();
				agrup.setTipo(set.getString("tipo"));
				agrup.setCodvenda(set.getInt("codvenda"));
				agrup.setValor(set.getDouble("valor"));
				agrup.setOrd(set.getInt("ord"));
				agrup.setQtdparc(set.getInt("qtdparc"));
				agrup.setQtdparcrestante(set.getInt("qtdparc"));
				agrup.setValorparcela(set.getDouble("valor") / set.getInt("qtdparc"));
				agrup.setValorparcelaRestante(set.getDouble("valor") / set.getInt("qtdparc"));
				ord = ord + 1;
				lista.add(agrup);
			}

			set.close();
			ps.close();
		} catch (SQLException e) {

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}

		return lista;
	}

	public boolean gravaRecebimentoVenda(Integer codcaixaloja, Integer codtippag, Connection con,
			ArrayList<AgrupPrestContasBean> lista, TituloReceberBean tit, List<ImpostoBean> lstreten)
			throws ProjetoException, SQLException {

		boolean rst = false;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Double valorParcela = 0.0;
		TituloReceberDao daorec = new TituloReceberDao();
		// Loop abaixo vai calcular o valor atual do titulo a receber,
		// considerando o somatorio das parcelas
		for (AgrupPrestContasBean obj : lista) {
			if ((obj.getQtdparcrestante() > 0) && (obj.isCalculado() == false)) {
				// aqui ele soma

				valorParcela = valorParcela + obj.getValorparcela();
			}
			// while (set.next()) {
			// }
		}
		// if abaixo foi porque so estava salvando o valorParcela, e sempre
		// passava pro TituloReceberDao zerado
		// então botamos o if para pegar o valor e não o valorParcela quando o
		// valorParcela estiver 0.

		// Caso tenha sido passado percentual da taxa de adm ele calcula em cima
		// do valor da parcela
		tit.setValortaxaadm(tit.getValortaxaadmperc() * valorParcela);
		if (valorParcela == 0) {
			tit.setValor(tit.getValor());
		} else {
			tit.setValor(valorParcela);
		}

		int codreceber = daorec.salvarReceber(tit, lstreten, con, false);

		try {

			for (AgrupPrestContasBean obj : lista) {

				if ((obj.getQtdparcrestante() > 0) && (obj.isCalculado() == false)) {

					String sql = " INSERT INTO clin.recebimentos_venda(codvenda, codcaixaloja, codreceber, tipo, valorpago, opcad, datacadastro, valor_parcela, parcela, ord)   "
							+ "   VALUES (?, ?, ?,?,?,?, current_date,?, ?, ?)";
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setInt(1, obj.getCodvenda());
					ps.setInt(2, codcaixaloja);
					ps.setInt(3, codreceber);
					ps.setString(4, obj.getTipo());
					ps.setDouble(5, obj.getValor());
					ps.setInt(6, user_session.getCodfunc());
					ps.setDouble(7, obj.getValorparcela());
					ps.setInt(8, obj.getQtdparc());
					ps.setInt(9, obj.getOrd());
					ps.execute();
					con.commit();
					obj.setQtdparcrestante(obj.getQtdparcrestante() - 1);

				}
				// while (set.next()) {
				// }
			}

			for (AgrupPrestContasBean obj : lista) {
				obj.setCalculado(false);
			}

			rst = true;
		} catch (SQLException e) {
			rst = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}

		return rst;
	}



}
