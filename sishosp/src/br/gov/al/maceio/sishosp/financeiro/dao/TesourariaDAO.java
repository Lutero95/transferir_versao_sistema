package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.financeiro.model.BaixaReceber;
import br.gov.al.maceio.sishosp.financeiro.model.CaixaDiarioBean;
import br.gov.al.maceio.sishosp.financeiro.model.ChequeEmitidoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ChequeRecebidoBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.PrestacaoContasBean;
import br.gov.al.maceio.sishosp.financeiro.model.TesourariaBean;
import br.gov.al.maceio.sishosp.financeiro.model.TituloPagarBean;
import br.gov.al.maceio.sishosp.financeiro.model.TituloReceberBean;



public class TesourariaDAO {

	public List<TesourariaBean> pesquisaMovimentacao(Integer codbanco, Integer seqcaixa) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		List<TesourariaBean> colecao = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select codseq,idbanco,  dtmovimento, valor, complemento, tipo, opcad, codreceber, codpagar, seqcaixadiario from financeiro.movtesouraria where idbanco = ? and seqcaixadiario = ?  order by codseq";

			ps = con.prepareStatement(sql);
			ps.setInt(1, codbanco);
			ps.setInt(2, seqcaixa);

			rs = ps.executeQuery();
			TesourariaBean tesouraria = new TesourariaBean();
			while (rs.next()) {
				tesouraria = new TesourariaBean();
				tesouraria.setCodseq(rs.getInt("codseq"));
				tesouraria.getBanco().setId(rs.getInt("idbanco"));
				tesouraria.setDtmovimento(rs.getDate("dtmovimento"));
				tesouraria.setValor(rs.getDouble("valor"));
				tesouraria.setComplemento(rs.getString("complemento"));
				tesouraria.setTipo(rs.getString("tipo"));
				tesouraria.getFunc().setId(rs.getLong("opcad"));
				tesouraria.setCodreceber(rs.getInt("codreceber"));
				tesouraria.setCodpagar(rs.getInt("codpagar"));
				tesouraria.setSeqcaixadiario(rs.getInt("seqcaixadiario"));
				colecao.add(tesouraria);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public boolean pesquisaNumCheque(ChequeEmitidoBean cheque) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		boolean rst = false;

		try {
			con = ConnectionFactory.getConnection();

			String sql = "select codcheque from Cheque_Emitido where idBanco = ? and NumCheque = ? and status<>'CA' ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, cheque.getBanco().getId());
			ps.setString(2, cheque.getNumcheque());

			rs = ps.executeQuery();

			while (rs.next()) {
				rst = true;
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}  finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return rst;
	}

	public ChequeEmitidoBean pesquisaChequeEmitido(Integer codcheque, Integer codigo) throws ProjetoException {
		Integer codcheque2 = 0;

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		// PROVAVELMENTE ESSE SQL QUE FIZ ABAIXO TA ERRADO, RETIRAR E DEIXAR SÃ“ O
		// SEGUNDO SQL
		ChequeEmitidoBean cheque = new ChequeEmitidoBean();
		try {
			con = ConnectionFactory.getConnection();
			String sql2 = "select bx.codchqemitido from financeiro.pagdupbx bx join financeiro.pagdup p on (bx.codigo = p.codigo) where bx.codigo=? ";
			ps = con.prepareStatement(sql2);
			ps.setInt(1, codigo);
			rs = ps.executeQuery();
			while (rs.next()) {
				cheque = new ChequeEmitidoBean();
				cheque.setCodcheque(rs.getInt("codchqemitido"));
				codcheque2 = cheque.getCodcheque();
			}

			ps.close();
			rs.close();

			String sql = "select codcheque, idbanco from financeiro.Cheque_Emitido where codcheque=?";

			ps = con.prepareStatement(sql);
			ps.setInt(1, codcheque2);

			// chq = null;
			rs = ps.executeQuery();

			while (rs.next()) {
				cheque = new ChequeEmitidoBean();
				cheque.setCodcheque(rs.getInt("codcheque"));
				cheque.getBanco().setId(rs.getInt("idbanco"));

			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}  finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return cheque;
	}

	public double saldoInicialCaixaBc(Integer idbanco, Integer seqcaixa) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		double saldo = 0;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select saldoinicial from financeiro.CaixaDiario_Bc where idBanco =?"
					+ " and SeqCaixaDiario = ? ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, idbanco);
			ps.setInt(2, seqcaixa);
			rs = ps.executeQuery();

			while (rs.next()) {
				saldo = rs.getDouble("saldoinicial");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return saldo;
	}

	public Integer retornaSeqCaixaTesAberto() throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		Integer seqcaixa = 0;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "SELECT seqcaixadiario FROM financeiro.CAIXADIARIO  WHERE  status='A'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				seqcaixa = rs.getInt("seqcaixadiario");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		}  finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return seqcaixa;
	}

	public double totalCreditosTesBc(Integer idbanco, Integer seqcaixa) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		double total = 0;
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select sum(Valor) valor from financeiro.movtesouraria where idbanco =?"
					+ " and SeqCaixaDiario = ? and Tipo = 'CD' ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, idbanco);
			ps.setInt(2, seqcaixa);
			rs = ps.executeQuery();

			while (rs.next()) {
				total = rs.getDouble("valor");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return total;
	}

	public double totalDebitosTesBc(Integer idbanco, Integer seqcaixa) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		double total = 0;
		try {
			con = ConnectionFactory.getConnection();

			String sql = "select sum(Valor) valor from financeiro.movtesouraria where idbanco =?"
					+ " and SeqCaixaDiario = ? and Tipo = 'DB' ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, idbanco);
			ps.setInt(2, seqcaixa);

			rs = ps.executeQuery();

			while (rs.next()) {
				total = rs.getDouble("valor");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return total;
	}

	public CaixaDiarioBean retornaCaixaAtual() throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		CaixaDiarioBean caixa = new CaixaDiarioBean();
		caixa.setStatus("F");
		try {
			con = ConnectionFactory.getConnection();

			String sql = "SELECT seqcaixadiario,status, data FROM financeiro.CAIXADIARIO"
					+ " WHERE SEQCAIXADIARIO = (SELECT MAX(SEQCAIXADIARIO) FROM financeiro.CAIXADIARIO ) ";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				caixa.setSeqcaixadiario(rs.getInt("seqcaixadiario"));
				caixa.setStatus(rs.getString("status"));
				caixa.setDataCaixaAbertura(rs.getDate("data"));
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return caixa;
	}

	public boolean abreCaixa(Integer codbanco) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		boolean result = false;

		try {
			// insere na tabela caixadiario
			String sql = "insert into financeiro.caixadiario (data,operador, dtabertura,status, saldoinicial) values (current_date,?,current_timestamp,'A', "
					+ " (select sum(coalesce(saldofinal,0)) saldofinal from ("
					+ " SELECT coalesce(saldoinicial,0)+coalesce(creditos,0)-coalesce(debitos,0) saldofinal FROM financeiro.caixadiario"
					+ " WHERE SEQCAIXADIARIO = (SELECT MAX(SEQCAIXADIARIO) FROM financeiro.CAIXADIARIO )"
					+ " union all" + " select 0" + " ) as t))";
			
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			
			ps = con.prepareStatement(sql);
			ps.setLong(1, user_session.getId());
			ps.executeUpdate();
			ps.close();
			// insere na tabela caixadiario_bc
			/*
			 * sql =
			 * "select (select seqcaixadiario from financeiro.caixadiario where status='A' ) seqcaixadiario,"
			 * + " id idbanco,"+
			 * " (select codfilial from financeiro.caixadiario where status='A' ) codfilial,"
			 * + " (select sum(coalesce(saldofinal,0)) saldofinal from ("+
			 * " SELECT coalesce(saldoinicial,0)+coalesce(creditos,0)-coalesce(debitos,0) saldofinal FROM financeiro.caixadiario_bc"
			 * +
			 * " WHERE SEQCAIXADIARIO = (SELECT MAX(SEQCAIXADIARIO) FROM financeiro.CAIXADIARIO WHERE CODFILIAL =?"
			 * + " and idbanco=banco.id"+ " )"+ " union all"+ " select 0 saldofinal"+
			 * " ) t"+ " ) saldoinicial,"+ " 0 creditos,"+ " 0 debitos"+
			 * " from financeiro.banco where codfilial=? and ativo='S'";
			 * 
			 * user_session = (FuncionarioBean) FacesContext
			 * .getCurrentInstance().getExternalContext().getSessionMap()
			 * .get("obj_usuario");
			 * 
			 * ps = con.prepareStatement(sql);
			 * 
			 * 
			 * ps.setInt(1, user_session.getEmpresa().getCodfilial()); ps.setInt(2,
			 * user_session.getEmpresa().getCodfilial()); ps.setInt(3,
			 * user_session.getEmpresa().getCodfilial()); ps.setInt(4,
			 * user_session.getEmpresa().getCodfilial()); ResultSet rs = ps.executeQuery();
			 * TesourariaBean e = new TesourariaBean(); List colecao = new ArrayList();
			 * while (rs.next()) {
			 * 
			 * }
			 */
			sql = "insert into financeiro.caixadiario_bc"
					+ " select (select seqcaixadiario from financeiro.caixadiario where status='A' ) seqcaixadiario,"
					+ " id idbanco,"
					+ " (select sum(coalesce(saldofinal,0)) saldofinal from ("
					+ " SELECT coalesce(saldoinicial,0)+coalesce(creditos,0)-coalesce(debitos,0) saldofinal FROM financeiro.caixadiario_bc"
					+ " WHERE SEQCAIXADIARIO = (SELECT MAX(SEQCAIXADIARIO) FROM financeiro.CAIXADIARIO WHERE  status='F'"
					+ " and idbanco=banco.id" + " )" + " union all" + " select 0 saldofinal" + " ) t"
					+ " ) saldoinicial," + " 0 creditos," + " 0 debitos"
					+ " from financeiro.banco where  ativo='S'";
			user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("obj_usuario");
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
			con.commit();
			result = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return result;
	}

	public boolean fechaCaixa(CaixaDiarioBean caixa) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;
		boolean result = false;

		try {
			con = ConnectionFactory.getConnection();

			// insere na tabela caixadiario
			String sql = "UPDATE financeiro.caixadiario bc set creditos=t.cred, debitos=t.deb,status='F', dtfechamento=current_timestamp"
					+ "	from" + "	(select sum(cred) cred, sum(deb) deb from ("
					+ "	select sum(valor) cred, 0 deb from financeiro.movtesouraria m" + "	where 1=1"
					+ "	and m.seqcaixadiario=?" + "	and tipo='CD'" + "	union all"
					+ "	select 0  cred, sum(valor) deb from financeiro.movtesouraria m" + "	where 1=1"
					+ "	and m.seqcaixadiario=?" + "	and tipo='DB'" + "	)w ) t" + "	where seqcaixadiario=?";

			ps = con.prepareStatement(sql);
			ps.setInt(1, caixa.getSeqcaixadiario());
			ps.setInt(2, caixa.getSeqcaixadiario());
			ps.setInt(3, caixa.getSeqcaixadiario());

			ps.executeUpdate();
			ps.close();

			// insere na tabela caixadiario_bc
			sql = "UPDATE financeiro.caixadiario_bc bc set creditos=t.cred, debitos=t.deb	from"
					+ "	(select sum(cred) cred, sum(deb) deb, idbanco from ("
					+ "	select sum(valor) cred, 0 deb, m.idbanco from financeiro.movtesouraria m	where "
					+ "	 m.seqcaixadiario=?	and tipo='CD'	group by m.idbanco	union all"
					+ "	select 0  cred, sum(valor) deb, m.idbanco from financeiro.movtesouraria m	where 1=1 "
					+ "	and m.seqcaixadiario=?	and tipo='DB'	group by m.idbanco	)w group by idbanco) t"
					+ "	where t.idbanco = bc.idbanco";

			ps = con.prepareStatement(sql);
			ps.setInt(1, caixa.getSeqcaixadiario());
			ps.setInt(2, caixa.getSeqcaixadiario());
			ps.executeUpdate();
			con.commit();

			result = true;

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return result;
	}

	public List<TituloPagarBean> pesquisaTitulosAberto(FornecedorBean forn, Date periodoInicial, Date periodoFinal)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		List<TituloPagarBean> colecao = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();
			String sql = "select  P.VALOR,p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)\n" + 
					"  - coalesce((select sum(coalesce(valorpago,0)-(coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0))) from financeiro.pagdupbx\n" + 
					" where pagdupbx.codigo=p.codigo),0) valoraberto,"
					+ " P.IDDESPESA, P.IDCCUSTO, P.CODFORN, P.CODHISTO, P.CODIGO, P.CODPORT,"
					+ " P.DTEMISSAO, P.DTPREVISAO, P.DTVCTO, P.DUPLICATA, P.HISTORICO, P.PARCELA, P.SITUACAO,"
					+ " P.VALOR ,p.vlr_retencao,  F.nome nomeforn,F.Fantasia as FantasiaForn"
					+ " from financeiro.PagDup P" + "  left Join financeiro.Fornecedor F on (P.CodForn = F.CodForn)"
					+ "  where P.DTVCTO >= ? AND P.DTVCTO <= ? AND P.SITUACAO <> 'F'" + "  ";
			if (forn != null) {
				if (forn.getCodforn() != null) {
					sql = sql + " and p.codforn=?";
				}
			}
			sql = sql + "  AND ((P.CODCHQEMITIDO NOT IN (SELECT CODCHEQUE"
					+ "     FROM financeiro.CHEQUE_EMITIDO WHERE CODCHEQUE = P.CODCHQEMITIDO AND STATUS <> 'CA' ))"
					+ "    OR (P.Valor > ( SELECT sum(valor)"
					+ "    FROM financeiro.Cheque_Emitido2 WHERE CODPAGDUP = P.CODIGO )))";

			ps = con.prepareStatement(sql);

			ps.setDate(1, new java.sql.Date(periodoInicial.getTime()));
			ps.setDate(2, new java.sql.Date(periodoFinal.getTime()));
			if (forn != null) {
				if (forn.getCodforn() != null) {
					ps.setInt(3, forn.getCodforn());
				}
			}

			rs = ps.executeQuery();
			TituloPagarBean tituloPagaar = new TituloPagarBean();
			while (rs.next()) {
				tituloPagaar = new TituloPagarBean();
				tituloPagaar.setValor(rs.getDouble("valor"));
				tituloPagaar.setValoraberto(rs.getDouble("valoraberto"));
				tituloPagaar.getDespesa().setId(rs.getInt("iddespesa"));
				tituloPagaar.getCcusto().setIdccusto(rs.getInt("idccusto"));
				tituloPagaar.getForn().setCodforn(rs.getInt("codforn"));
				tituloPagaar.getForn().setNome(rs.getString("nomeforn"));
				tituloPagaar.getForn().setFantasia(rs.getString("fantasiaforn"));
				tituloPagaar.setCodigo(rs.getInt("codigo"));
				tituloPagaar.getPortador().setCodportador(rs.getInt("codport"));
				tituloPagaar.setDtemissao(rs.getDate("dtemissao"));
				tituloPagaar.setDtprevisao(rs.getDate("dtprevisao"));
				tituloPagaar.setDtvcto(rs.getDate("dtvcto"));
				tituloPagaar.setDuplicata(rs.getString("duplicata"));
				tituloPagaar.setHistorico(rs.getString("historico"));
				tituloPagaar.setParcela(rs.getString("parcela"));
				tituloPagaar.setSituacao(rs.getString("situacao"));
				tituloPagaar.setVlr_retencao(rs.getDouble("vlr_retencao"));
				colecao.add(tituloPagaar);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public boolean compensaTitulosPagar(ChequeEmitidoBean cheque, List<TituloPagarBean> lst, String tipodoc)
			throws ProjetoException, SQLException {

		Connection con = null;
		CallableStatement ps = null;

		boolean result = false;

		try {
			con = ConnectionFactory.getConnection();

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			ps = con.prepareCall("{ ? = call financeiro.cheque_emitido(?,?,?,?,?,?,?,?,?,?,?,?) }");

			ps.registerOutParameter(1, Types.INTEGER);
			ps.setInt(2, cheque.getBanco().getId());
			ps.setString(3, cheque.getNumcheque());
			ps.setDate(4, new java.sql.Date(cheque.getDtemissao().getTime()));
			ps.setString(5, tipodoc);
			ps.setDouble(6, cheque.getValor());
			ps.setString(7, cheque.getNominal());
			ps.setString(8, cheque.getCompensado());
			ps.setInt(9, Integer.valueOf(String.valueOf(user_session.getId())));
			ps.setString(10, cheque.getStatus());
			ps.setDate(11, new java.sql.Date(cheque.getDtemissao().getTime()));
			ps.setInt(12, cheque.getCaixa().getSeqcaixadiario());

			if (cheque.getDtcompensado() != null)
				ps.setDate(13, new java.sql.Date(cheque.getDtcompensado().getTime()));
			else
				ps.setNull(13, Types.OTHER);
			
			ps.executeUpdate();
			int retornoid = 0;
			retornoid = ps.getInt(1);

			for (int x = 0; x < lst.size(); x++) {
				// CHEQUE_EMITIDO2

				String sql = "insert into financeiro.cheque_emitido2 (	codcheque, codpagdup, valor, vlratual, desconto, juros, multa, tipobx) values (?,?,?,?,?,?,?,?) ";
				PreparedStatement psProduto = con.prepareStatement(sql);

				psProduto.setInt(1, retornoid);
				psProduto.setInt(2, lst.get(x).getCodigo());
				psProduto.setDouble(3, lst.get(x).getTitulobaixa().getValorpago());
				psProduto.setDouble(4, lst.get(x).getValoraberto());
				psProduto.setDouble(5, lst.get(x).getTitulobaixa().getVlrdesc());
				psProduto.setDouble(6, lst.get(x).getTitulobaixa().getVlrjuros());
				psProduto.setDouble(7, lst.get(x).getTitulobaixa().getVlrmulta());
				psProduto.setString(8, lst.get(x).getTitulobaixa().getTpbaixa());

				psProduto.execute();
				psProduto.close();

				// insercao pagdupbx
				sql = "select max(Ord) ord from financeiro.PagdupBx where Codigo =? ";
				int ord = 0;

				PreparedStatement ps2 = null;
				ps2 = con.prepareStatement(sql);
				ps2.setInt(1, lst.get(x).getCodigo());

				ResultSet rs = ps2.executeQuery();

				while (rs.next()) {
					ord = rs.getInt("ord");
				}

				ps2.close();
				rs.close();

				sql = "insert into financeiro.pagdupbx (	codigo, ord, dtpagto, dtvcto, valoratual, valorpago, tpbaixa, frbaixa, vlrdesc, vlrjuros, vlrmulta, idbanco, numcheque, codchqemitido, opcad) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				PreparedStatement psbx = con.prepareStatement(sql);

				psbx.setInt(1, lst.get(x).getCodigo());
				psbx.setDouble(2, ord + 1);
				psbx.setDate(3, new java.sql.Date(cheque.getDtvencimento().getTime()));
				psbx.setDate(4, new java.sql.Date(lst.get(x).getDtvcto().getTime()));
				psbx.setDouble(5, lst.get(x).getValoraberto());
				psbx.setDouble(6, lst.get(x).getTitulobaixa().getValorpago());
				psbx.setString(7, lst.get(x).getTitulobaixa().getTpbaixa());
				psbx.setString(8, "2");
				psbx.setDouble(9, lst.get(x).getTitulobaixa().getVlrdesc());
				psbx.setDouble(10, lst.get(x).getTitulobaixa().getVlrjuros());
				psbx.setDouble(11, lst.get(x).getTitulobaixa().getVlrmulta());
				psbx.setDouble(12, cheque.getBanco().getId());
				psbx.setString(13, lst.get(x).getTitulobaixa().getNumcheque());
				psbx.setDouble(14, retornoid);
				psbx.setDouble(15, user_session.getId());

				psbx.execute();
				psbx.close();

				if (lst.get(x).getTitulobaixa().getTpbaixa().equals("1")) {
					sql = "update financeiro.pagdup set situacao='F' where codigo=? ";
					PreparedStatement pspg = con.prepareStatement(sql);

					pspg.setInt(1, lst.get(x).getCodigo());
					pspg.execute();
					pspg.close();
				}

			}

			// insercao movtesouraria
			String sql = "insert into financeiro.movtesouraria (	idbanco, tipo, valor, codchqemitido, complemento, seqcaixadiario,  dtmovimento, opcad) values (?,?,?,?,?,?,current_date,?) ";
			PreparedStatement pstes = con.prepareStatement(sql);

			pstes.setInt(1, cheque.getBanco().getId());
			pstes.setString(2, "DB");
			pstes.setDouble(3, cheque.getValor());
			pstes.setInt(4, retornoid);
			pstes.setString(5, "Cheque N° " + cheque.getNumcheque());
			pstes.setInt(6, cheque.getCaixa().getSeqcaixadiario());
			pstes.setLong(7, user_session.getId());

			pstes.execute();
			con.commit();

			result = true;
			pstes.close();

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}

		return result;
	}
	
	public boolean compensaTituloAvulso(ChequeEmitidoBean cheque, TituloPagarBean titulo, String tipodoc, Connection conexao)
			throws ProjetoException, SQLException {

		CallableStatement ps = null;

		boolean result = false;

		try {
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			ps = conexao.prepareCall("{ ? = call financeiro.cheque_emitido(?,?,?,?,?,?,?,?,?,?,?,?) }");

			ps.registerOutParameter(1, Types.INTEGER);
			ps.setInt(2, cheque.getBanco().getId());
			ps.setString(3, cheque.getNumcheque());
			ps.setDate(4, new java.sql.Date(cheque.getDtemissao().getTime()));
			ps.setString(5, tipodoc);
			ps.setDouble(6, cheque.getValor());
			ps.setString(7, cheque.getNominal());
			ps.setString(8, cheque.getCompensado());
			ps.setInt(9, Integer.valueOf(String.valueOf(user_session.getId())));
			ps.setString(10, cheque.getStatus());
			ps.setDate(11, new java.sql.Date(cheque.getDtemissao().getTime()));
			ps.setInt(12, cheque.getCaixa().getSeqcaixadiario());

			if (cheque.getDtcompensado() != null)
				ps.setDate(13, new java.sql.Date(cheque.getDtcompensado().getTime()));
			else
				ps.setNull(13, Types.OTHER);
			
			ps.executeUpdate();
			int retornoid = 0;
			retornoid = ps.getInt(1);

				// CHEQUE_EMITIDO2

				String sql = "insert into financeiro.cheque_emitido2 (	codcheque, codpagdup, valor, vlratual, desconto, juros, multa, tipobx) values (?,?,?,?,?,?,?,?) ";
				PreparedStatement psProduto = conexao.prepareStatement(sql);

				psProduto.setInt(1, retornoid);
				psProduto.setInt(2, titulo.getCodigo());
				psProduto.setDouble(3, titulo.getTitulobaixa().getValorpago());
				psProduto.setDouble(4, titulo.getValoraberto());
				psProduto.setDouble(5, titulo.getTitulobaixa().getVlrdesc());
				psProduto.setDouble(6, titulo.getTitulobaixa().getVlrjuros());
				psProduto.setDouble(7, titulo.getTitulobaixa().getVlrmulta());
				psProduto.setString(8, titulo.getTitulobaixa().getTpbaixa());

				psProduto.execute();
				psProduto.close();

				// insercao pagdupbx
				sql = "select max(Ord) ord from financeiro.PagdupBx where Codigo =? ";
				int ord = 0;

				PreparedStatement ps2 = null;
				ps2 = conexao.prepareStatement(sql);
				ps2.setInt(1, titulo.getCodigo());

				ResultSet rs = ps2.executeQuery();

				while (rs.next()) {
					ord = rs.getInt("ord");
				}

				ps2.close();
				rs.close();

				sql = "insert into financeiro.pagdupbx (	codigo, ord, dtpagto, dtvcto, valoratual, valorpago, tpbaixa, frbaixa, vlrdesc, vlrjuros, vlrmulta, idbanco, numcheque, codchqemitido, opcad) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
				PreparedStatement psbx = conexao.prepareStatement(sql);

				psbx.setInt(1, titulo.getCodigo());
				psbx.setDouble(2, ord + 1);
				psbx.setDate(3, new java.sql.Date(cheque.getDtvencimento().getTime()));
				psbx.setDate(4, new java.sql.Date(titulo.getDtvcto().getTime()));
				psbx.setDouble(5, titulo.getValoraberto());
				psbx.setDouble(6, titulo.getTitulobaixa().getValorpago());
				psbx.setString(7, titulo.getTitulobaixa().getTpbaixa());
				psbx.setString(8, "2");
				psbx.setDouble(9, titulo.getTitulobaixa().getVlrdesc());
				psbx.setDouble(10, titulo.getTitulobaixa().getVlrjuros());
				psbx.setDouble(11, titulo.getTitulobaixa().getVlrmulta());
				psbx.setDouble(12, cheque.getBanco().getId());
				psbx.setString(13, titulo.getTitulobaixa().getNumcheque());
				psbx.setDouble(14, retornoid);
				psbx.setDouble(15, user_session.getId());

				psbx.execute();
				psbx.close();

				if (titulo.getTitulobaixa().getTpbaixa().equals("1")) {
					sql = "update financeiro.pagdup set situacao='F' where codigo=? ";
					PreparedStatement pspg = conexao.prepareStatement(sql);
					pspg.setInt(1, titulo.getCodigo());
					pspg.execute();
					pspg.close();
				}
			// insercao movtesouraria
			sql = "insert into financeiro.movtesouraria (	idbanco, tipo, valor, codchqemitido, complemento, seqcaixadiario,  dtmovimento, opcad) values (?,?,?,?,?,?,current_date,?) ";
			PreparedStatement pstes = conexao.prepareStatement(sql);

			pstes.setInt(1, cheque.getBanco().getId());
			pstes.setString(2, "DB");
			pstes.setDouble(3, cheque.getValor());
			pstes.setInt(4, retornoid);
			pstes.setString(5, "PAGAMENTO AVULSO");
			pstes.setInt(6, cheque.getCaixa().getSeqcaixadiario());
			pstes.setLong(7, user_session.getId());

			pstes.execute();
			result = true;
			pstes.close();
		} catch (SQLException sqle) {
			conexao.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conexao.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				//con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}

		return result;
	}	

	public boolean cancelarChequeEstornoReceber(ChequeEmitidoBean cheque, String complemento, TituloReceberBean t,
			BaixaReceber baixa) throws ProjetoException {
		boolean cancelou = false;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			/*
			 * String sql = "select valor from financeiro.cheque_emitido where codcheque = ?";
			 * PreparedStatement ps = null; ResultSet rs = null; try { FuncionarioBean
			 * user_session = (FuncionarioBean) FacesContext
			 * .getCurrentInstance().getExternalContext().getSessionMap()
			 * .get("obj_usuario");
			 * 
			 * ps = con.prepareStatement(sql); ps.setInt(1, cheque.getCodcheque()); rs =
			 * ps.executeQuery();
			 * 
			 * while (rs.next()) { valorCheque = rs.getDouble("valor"); }
			 * 
			 * PreparedStatement ps2 = null; ResultSet rs2 = null; String sql2 =
			 * "update financeiro.cheque_emitido set status = 'CA', dtcancela = current_date where codcheque = ?"
			 * ; ps2 = con.prepareStatement(sql2); ps2.setInt(1, cheque.getCodcheque());
			 * ps2.executeUpdate();
			 */

			String sql = "delete from financeiro.recdupbx where duplicata = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, t.getCodigo());

			ps.execute();
			ps.close();

			// insercao movtesouraria
			sql = "insert into financeiro.movtesouraria (	idbanco, tipo, valor, complemento, seqcaixadiario, codfilial, dtmovimento, opcad) values (?,?,?,?,?,?,current_date,?) ";
			ps = con.prepareStatement(sql);

			ps.setInt(1, baixa.getBanco().getId());
			ps.setString(2, "CD");
			ps.setDouble(3, baixa.getValorBaixado());
			ps.setString(4, complemento);
			ps.setInt(5, cheque.getCaixa().getSeqcaixadiario());
			ps.setLong(7, user_session.getId());

			ps.execute();
			ps.close();

			sql = "update financeiro.recdup set situacao = 'A' where idreceber = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, t.getCodigo());
			ps.executeUpdate();

			con.commit();
			cancelou = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return cancelou;
	}

	public boolean cancelarCheque(ChequeEmitidoBean cheque, String complemento, Integer codpagdup)
			throws ProjetoException {
		Double valorCheque = null;
		boolean cancelou = false;
		String sql = "select valor from financeiro.cheque_emitido where codcheque = ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			ps = con.prepareStatement(sql);
			ps.setInt(1, cheque.getCodcheque());
			rs = ps.executeQuery();

			while (rs.next()) {
				valorCheque = rs.getDouble("valor");
			}

			ps.close();

			sql = "update financeiro.cheque_emitido set status = 'CA', dtcancela = current_date where codcheque = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, cheque.getCodcheque());

			ps.executeUpdate();
			ps.close();

			sql = "delete from financeiro.pagdupbx where codchqemitido = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, cheque.getCodcheque());
			ps.execute();
			ps.close();

			// insercao movtesouraria
			sql = "insert into financeiro.movtesouraria (	idbanco, tipo, valor, codchqemitido, complemento, seqcaixadiario,  dtmovimento, opcad) values (?,?,?,?,?,?,current_date,?) ";
			ps = con.prepareStatement(sql);

			ps.setInt(1, cheque.getBanco().getId());
			ps.setString(2, "CD");
			ps.setDouble(3, valorCheque);
			ps.setInt(4, cheque.getCodcheque());
			ps.setString(5, complemento);
			ps.setInt(6, cheque.getCaixa().getSeqcaixadiario());
			ps.setLong(7, user_session.getId());

			ps.execute();
			ps.close();

			sql = "update financeiro.pagdup set situacao = 'A' where codigo = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, codpagdup);

			ps.executeUpdate();

			con.commit();
			cancelou = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return cancelou;
	}

	public List<ChequeEmitidoBean> pesquisaChequesEmitidos(Integer codbanco, ChequeEmitidoBean cheque)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		List<ChequeEmitidoBean> colecao = new ArrayList<>();
		try {
			con = ConnectionFactory.getConnection();

			String sql = " select codcheque, idbanco, valor,  numcheque, dtemissao, dtvencimento,  coalesce(compensado,'N') compensado, dtcompensado,status, nominal from financeiro.cheque_emitido where idbanco = ? and cheque_emitido.tipo='CP' "
					+ "  ";

			if (cheque.getStatus().equals("P")) {
				sql += " and coalesce(compensado,'N') <> 'S' " + "and status = 'OK' ";
			}

			if (cheque.getStatus().equals("CA")) {
				sql += " and status = 'CA' ";

			}

			if (cheque.getStatus().equals("OK")) {
				sql += " and status = 'OK' " + "and compensado = 'S'";
			}

			if ((cheque.getDtemissao() != null) && (cheque.getDtvencimento() != null)) {
				sql += " and dtvencimento between ? and ? ";
			}

			ps = con.prepareStatement(sql);

			ps.setInt(1, codbanco);
			if ((cheque.getDtemissao() != null) && (cheque.getDtvencimento() != null)) {
				ps.setDate(2, new java.sql.Date(cheque.getDtemissao().getTime()));
				ps.setDate(3, new java.sql.Date(cheque.getDtvencimento().getTime()));

			}

			rs = ps.executeQuery();
			ChequeEmitidoBean chequeEmitido = new ChequeEmitidoBean();
			while (rs.next()) {
				chequeEmitido = new ChequeEmitidoBean();
				chequeEmitido.setCodcheque(rs.getInt("codcheque"));
				chequeEmitido.getBanco().setId(rs.getInt("idbanco"));
				chequeEmitido.setValor(rs.getDouble("valor"));
				chequeEmitido.setNumcheque(rs.getString("numcheque"));
				chequeEmitido.setDtemissao(rs.getDate("dtemissao"));
				chequeEmitido.setDtvencimento(rs.getDate("dtvencimento"));
				chequeEmitido.setCompensado(rs.getString("compensado"));
				chequeEmitido.setDtcompensado(rs.getDate("dtcompensado"));
				chequeEmitido.setNominal(rs.getString("nominal"));
				chequeEmitido.setStatus(rs.getString("status"));
				colecao.add(chequeEmitido);
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public List<ChequeRecebidoBean> pesquisaChequesRecebidosBusca(Integer codbanco, ChequeRecebidoBean cheque)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		List<ChequeRecebidoBean> colecao = new ArrayList<ChequeRecebidoBean>();
		try {
			con = ConnectionFactory.getConnection();

			String sql = " select codcheque, codbanco, valor, codfilial, numcheque, dtemissao, dtvencimento, coalesce(compensado,'N') compensado, dtcompensacao,status, nominal from financeiro.cheque_recebido where codbanco = ? "
					+ "  ";

			if (cheque.getStatus() != null)
				if (cheque.getStatus().equals("CA")) {
					sql += " and status = 'CA' ";

				}
			if (cheque.getStatus() != null)
				if (cheque.getStatus().equals("OK")) {
					sql += " and status = 'OK' " + "and compensado = 'S'";
				}
			if (cheque.getStatus() != null)
				if (cheque.getStatus().equals("P")) {
					sql += " and coalesce(compensado,'N') <> 'S' " + "and status = 'OK' ";
				}

			if ((cheque.getDtemissao() != null) && (cheque.getDtvencimento() != null)) {
				sql += " and dtvencimento between ? and ? ";
			}

			if ((cheque.getNumcheque() != null) && (!cheque.getNumcheque().equals(""))) {
				sql += " and numcheque = ? ";
			}

			ps = con.prepareStatement(sql);
			int i = 2;
			ps.setInt(1, codbanco);
			if ((cheque.getDtemissao() != null) && (cheque.getDtvencimento() != null)) {
				ps.setDate(i, new java.sql.Date(cheque.getDtemissao().getTime()));
				ps.setDate(i = i + 1, new java.sql.Date(cheque.getDtvencimento().getTime()));
				i++;
			}
			if ((cheque.getNumcheque() != null) && (!cheque.getNumcheque().equals(""))) {
				ps.setString(i, cheque.getNumcheque());
				i++;
			}

			rs = ps.executeQuery();
			ChequeRecebidoBean chequeRecebido = new ChequeRecebidoBean();
			while (rs.next()) {
				chequeRecebido = new ChequeRecebidoBean();
				chequeRecebido.setCodcheque(rs.getInt("codcheque"));
				chequeRecebido.getBanco().setId(rs.getInt("codbanco"));
				chequeRecebido.setValor(rs.getDouble("valor"));
				chequeRecebido.setNumcheque(rs.getString("numcheque"));
				chequeRecebido.setDtemissao(rs.getDate("dtemissao"));
				chequeRecebido.setDtvencimento(rs.getDate("dtvencimento"));
				chequeRecebido.setCompensado(rs.getString("compensado"));
				chequeRecebido.setDtcompensado(rs.getDate("dtcompensacao"));
				chequeRecebido.setNominal(rs.getString("nominal"));
				chequeRecebido.setStatus(rs.getString("status"));
				colecao.add(chequeRecebido);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public List<ChequeRecebidoBean> pesquisaChequesRecebido(Integer codbanco, ChequeRecebidoBean cheque)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		List<ChequeRecebidoBean> colecao = new ArrayList<ChequeRecebidoBean>();
		try {
			con = ConnectionFactory.getConnection();

			String sql = " select codcheque, codbanco, valor,  numcheque, dtemissao, dtvencimento, coalesce(compensado,'N') compensado, dtcompensacao,status, nominal from financeiro.cheque_recebido where codbanco = ? "
					+ "  ";

			if ((cheque.getStatus() != null) && (cheque.getStatus().equals("OK"))) {
				sql += " and status = 'OK' " + "and compensado = 'S'";
			}
			if ((cheque.getStatus() != null) && (cheque.getStatus().equals("P"))) {
				sql += " and coalesce(compensado,'N') <> 'S' " + "and status = 'OK' ";
			}

			if ((cheque.getDtemissao() != null) && (cheque.getDtvencimento() != null)) {
				sql += " and dtvencimento between ? and ? ";
			}

			if ((cheque.getNumcheque() != null) && (!cheque.getNumcheque().equals(""))) {
				sql += " and numcheque = ? ";
			}

			ps = con.prepareStatement(sql);
			int i = 2;
			ps.setInt(1, codbanco);
			if ((cheque.getDtemissao() != null) && (cheque.getDtvencimento() != null)) {
				ps.setDate(i, new java.sql.Date(cheque.getDtemissao().getTime()));
				ps.setDate(i = i + 1, new java.sql.Date(cheque.getDtvencimento().getTime()));
				i++;
			}
			if ((cheque.getNumcheque() != null) && (!cheque.getNumcheque().equals(""))) {
				ps.setString(i, cheque.getNumcheque());
				i++;
			}

			rs = ps.executeQuery();
			ChequeRecebidoBean chequeRecebido = new ChequeRecebidoBean();
			while (rs.next()) {
				chequeRecebido = new ChequeRecebidoBean();
				chequeRecebido.setCodcheque(rs.getInt("codcheque"));
				chequeRecebido.getBanco().setId(rs.getInt("codbanco"));
				chequeRecebido.setValor(rs.getDouble("valor"));
				chequeRecebido.setNumcheque(rs.getString("numcheque"));
				chequeRecebido.setDtemissao(rs.getDate("dtemissao"));
				chequeRecebido.setDtvencimento(rs.getDate("dtvencimento"));
				chequeRecebido.setCompensado(rs.getString("compensado"));
				chequeRecebido.setDtcompensado(rs.getDate("dtcompensacao"));
				chequeRecebido.setNominal(rs.getString("nominal"));
				chequeRecebido.setStatus(rs.getString("status"));
				colecao.add(chequeRecebido);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return colecao;
	}

	public boolean compensaChq(ChequeEmitidoBean cheque) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;
		boolean result = false;

		try {
			con = ConnectionFactory.getConnection();

			// insere na tabela caixadiario
			String sql = "UPDATE financeiro.cheque_emitido set compensado='S', dtcompensado=? where codcheque=?";

			ps = con.prepareStatement(sql);

			ps.setDate(1, new java.sql.Date(cheque.getDtcompensado().getTime()));
			ps.setInt(2, cheque.getCodcheque());

			ps.executeUpdate();
			con.commit();
			result = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return result;
	}

	public boolean compensaChqReceb(ChequeRecebidoBean cheque) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;

		boolean result = false;

		try {
			con = ConnectionFactory.getConnection();

			// insere na tabela caixadiario
			String sql = "UPDATE financeiro.cheque_recebido set compensado='S', dtcompensacao=? where codcheque=?";

			ps = con.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(cheque.getDtcompensado().getTime()));
			ps.setInt(2, cheque.getCodcheque());

			ps.executeUpdate();
			con.commit();
			result = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return result;
	}

	public boolean gravaLancamento(TesourariaBean lancto, Connection conexao, Boolean comitar,
			PrestacaoContasBean lancto2) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;

		boolean result = false;
		try {
			if (comitar) {
				con = ConnectionFactory.getConnection();
			} else {
				con = conexao;
			}
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");


			// insere na tabela caixadiario
			String sql = "insert into financeiro.movtesouraria (	idbanco, tipo, valor, operacao, complemento, obs, seqcaixadiario, dtmovimento, opcad) values (?,?,?,?,?,?,?,?,(select data from financeiro.caixadiario where codfilial=? and status='A'),?) ";
			ps = con.prepareStatement(sql);
			ps.setInt(1, lancto.getBanco().getId());

			if (lancto.getOperacao().equals("DESPBANC")) {
				ps.setString(2, "DB");
			} else

			if (lancto.getOperacao().equals("CAPGIRD")) {
				ps.setString(2, "DB");
			} else if (lancto.getOperacao().equals("CAPGIRC")) {
				ps.setString(2, "CD");
			} else {
				ps.setString(2, lancto.getTipo());
			}

			ps.setDouble(3, lancto.getValor());
			ps.setString(4, lancto.getOperacao());

			if (lancto.getOperacao().equals("DESPBANC")) {
				ps.setString(5, "DESPESA BANCARIA");
			} else

			if (lancto.getOperacao().equals("CAPGIRD")) {
				ps.setString(5, "CAPITAL DE GIRO - AMORTIZACAO " + lancto.getObs());
			} else if (lancto.getOperacao().equals("CAPGIRC")) {
				ps.setString(5, "CAPITAL DE GIRO - CREDITO " + lancto.getObs());
			} else {
				ps.setNull(5, Types.OTHER);
			}

			ps.setString(6, lancto.getObs());
			ps.setInt(7, lancto.getCaixa().getSeqcaixadiario());
			ps.setLong(10, user_session.getId());

			ps.execute();

			if (comitar) {
				con.commit();
			}
			result = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				if (comitar) {
					con.close();
				}
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return result;
	}

	public boolean gravaAcerto1(TesourariaBean lancto, Integer codbanco, Connection conexao, Boolean comitar,
			PrestacaoContasBean lancto2) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet set = null;

		boolean result = false;
		boolean acerto = false;

		try {
			if (comitar) {
				con = ConnectionFactory.getConnection();
			} else {
				con = conexao;
			}

			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			String sql = "select acerto from financeiro.movtesouraria where codfilial = ? and idbanco=?";

			ps = con.prepareStatement(sql);
			ps.setInt(2, codbanco);
			set = ps.executeQuery();

			while (set.next()) {
				acerto = set.getBoolean("acerto");
			}

			if (acerto) {
				con.close();
				return false;
			} else {
				ps.close();
				sql = "insert into financeiro.movtesouraria (	idbanco, tipo, valor, operacao, complemento, obs, seqcaixadiario, codfilial, dtmovimento, opcad, acerto) "
						+ "values (?,?,?,?,?,?,(SELECT MAX(SEQCAIXADIARIO) FROM financeiro.CAIXADIARIO WHERE CODFILIAL = ?),?,(select data from financeiro.caixadiario where codfilial=? and status='A'),?,?) ";

				ps = con.prepareStatement(sql);

				ps.setInt(1, codbanco);

				if (lancto.getOperacao().equals("DEB")) {
					ps.setString(2, "DB");
				} else if (lancto.getOperacao().equals("CRED")) {
					ps.setString(2, "CD");
				}

				ps.setDouble(3, lancto.getValor());
				ps.setString(4, lancto.getOperacao());

				if (lancto.getObs() != null) {
					ps.setString(5, lancto.getObs());
				} else {
					ps.setNull(5, Types.OTHER);
				}

				ps.setString(6, "REFERENTE AO ACERTO DA CONTA");
				ps.setLong(7, user_session.getId());
				ps.setBoolean(8, true);

				ps.execute();

				if (comitar) {
					con.commit();
				}
			}
			result = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				set.close();
				if (comitar) {
					con.close();
				}
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return result;
	}

	// gravar dados JW
	public Boolean inserirConta(TesourariaBean tesouraria, TesourariaBean tesouraria2) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		PreparedStatement ps = null;
		Connection con = null;

		try {
			con = ConnectionFactory.getConnection();

			String sql = " insert into financeiro.movtesouraria (idbanco, codfilial, valor, complemento, tipo, opcad, operacao, dtmovimento, seqcaixadiario) "
					+ " values (?,?,?,?,'DB',?,?, ?, ?) ";

			if (tesouraria.getBanco().getId() == tesouraria2.getBanco().getId()) {
			} else {
				ps = con.prepareStatement(sql);

				// insersão da primeira string
				ps = con.prepareStatement(sql);
				ps.setInt(1, tesouraria.getBanco().getId());
				ps.setDouble(2, tesouraria.getValor());
				ps.setString(3, "Trasferido para: " + tesouraria2.getBanco().getDescricao() + " N° conta: "
						+ tesouraria2.getBanco().getConta());
				ps.setLong(5, user_session.getId());
				ps.setString(6, tesouraria.getOperacao());
				ps.setDate(7, (java.sql.Date) tesouraria.getCaixa().getDataCaixaAbertura());
				ps.setInt(8, tesouraria.getCaixa().getSeqcaixadiario());

				ps.execute();
			}

			if (tesouraria.getBanco().getId() == tesouraria2.getBanco().getId()) {
			} else {
				ps.close();

				sql = " insert into financeiro.movtesouraria (idbanco, codfilial, valor, complemento, tipo, opcad, operacao, dtmovimento, seqcaixadiario) "
						+ " values (?,?,?,?,'CD',?,?,?,?) ";

				ps = con.prepareStatement(sql);
				// insersão da segunda string

				ps.setInt(1, tesouraria2.getBanco().getId());
				ps.setDouble(2, tesouraria.getValor());
				ps.setString(3, "Recebido de: " + tesouraria.getBanco().getDescricao() + " N° conta: "
						+ tesouraria.getBanco().getConta());
				ps.setLong(4, user_session.getId());
				ps.setString(5, tesouraria.getOperacao());
				ps.setDate(6, (java.sql.Date) tesouraria2.getCaixa().getDataCaixaAbertura());
				ps.setInt(7, tesouraria2.getCaixa().getSeqcaixadiario());

				ps.execute();
			}

			con.commit();
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return true;
	}

	public Double pesquisaChequesReceberDia(Integer codbanco, Integer seqcaixa) throws ProjetoException {

		Double total = 0.;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionFactory.getConnection();
			String sql = " select sum( r.valor - coalesce(r.desconto,0)+coalesce(r.juros_dia,0)+coalesce(r.multaatraso,0) "
					+ " -coalesce(r.vlrretencao,0)-coalesce(r.valortaxaadm,0) "
					+ " - coalesce((select sum(coalesce(valorpago,0)+coalesce(vlrdesc,0) "
					+ "-coalesce(vlrjuros,0)-coalesce(vlrmulta,0)) from financeiro.recdupbx "
					+ "where recdupbx.duplicata=r.idreceber),0)) valoraberto from financeiro.recdup r join financeiro.cliente c on "
					+ "(r.id_cliente = c.codcliente)  left join financeiro.portador p  on "
					+ "(r.id_portador = p.codportador) " + "left join financeiro.tipodocumento td  on "
					+ "(r.codtipdoc = td.codtipodocumento) where  ((r.codfilial = ?) "
					+ "or (r.codfilial in (select codempresa_autorizada from financeiro.empresa_aut_clientes "
					+ "where codempresa_origem=?))) "
					+ "and td.cheque is true and status='A' and vencimento <=current_date ";


			ps = con.prepareStatement(sql);
			ps.setInt(1, codbanco);
			ps.setInt(2, seqcaixa);


			rs = ps.executeQuery();

			while (rs.next()) {
				total = rs.getDouble("valoraberto");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return total;
	}

	public Double pesquisaChequesReceberPrazo(Integer codbanco, Integer seqcaixa) throws ProjetoException {

		Double total = 0.;

		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		try {

			con = ConnectionFactory.getConnection();
			String sql = " select sum( r.valor - coalesce(r.desconto,0)+coalesce(r.juros_dia,0)+coalesce(r.multaatraso,0) "
					+ " -coalesce(r.vlrretencao,0)-coalesce(r.valortaxaadm,0) "
					+ " - coalesce((select sum(coalesce(valorpago,0)+coalesce(vlrdesc,0) "
					+ "-coalesce(vlrjuros,0)-coalesce(vlrmulta,0)) from financeiro.recdupbx "
					+ "where recdupbx.duplicata=r.idreceber),0)) valoraberto from financeiro.recdup r join financeiro.cliente c on "
					+ "(r.id_cliente = c.codcliente)  left join financeiro.portador p  on "
					+ "(r.id_portador = p.codportador) " + "left join financeiro.tipodocumento td  on "
					+ "(r.codtipdoc = td.codtipodocumento) where 1=1 "
					+ "and td.cheque is true and status='A' and vencimento >current_date ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, codbanco);
			ps.setInt(2, seqcaixa);

			rs = ps.executeQuery();

			while (rs.next()) {
				total = rs.getDouble("valoraberto");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return total;
	}

	public Double pesquisaChequesPagarCompensar(Integer codbanco, Integer seqcaixa) throws ProjetoException {

		Double total = 0.;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = ConnectionFactory.getConnection();
			String sql = " SELECT coalesce(SUM(VALOR),0) FROM financeiro.CHEQUE_EMITIDO "
					+ " WHERE COMPENSADO = 'N' AND TIPO = 'CP' AND STATUS = 'OK' " + " CODBANCO = ? ";

			ps = con.prepareStatement(sql);
			ps.setInt(1, codbanco);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				total = rs.getDouble("valoraberto");
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
			}
		}
		return total;
	}
}
