package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.financeiro.model.AntecipacaoBean;
import br.gov.al.maceio.sishosp.financeiro.model.BaixaReceber;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.financeiro.model.BuscaBeanRecibo;
import br.gov.al.maceio.sishosp.financeiro.model.CaixaDiarioBean;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ClienteBean;
import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;
import br.gov.al.maceio.sishosp.financeiro.model.FonteReceitaBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.ImpostoBean;
import br.gov.al.maceio.sishosp.financeiro.model.NegociarBean;
import br.gov.al.maceio.sishosp.financeiro.model.PagamentoBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;
import br.gov.al.maceio.sishosp.financeiro.model.TipoDocumentoBean;
import br.gov.al.maceio.sishosp.financeiro.model.TituloReceberBean;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

public class TituloReceberDao {

	public int salvarReceber(TituloReceberBean tituloReceber, List<ImpostoBean> lstRetencao, Connection conexao,
			boolean comitar) throws ProjetoException {

		FuncionarioBean usuarioLogado = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		int retornoid = 0;

		CallableStatement cs = null;

		try {

			cs = conexao.prepareCall(
					"{ ? = call clin.recdup" + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) }");
			cs.registerOutParameter(1, Types.INTEGER);

			cs.setString(2, tituloReceber.getDocumento().toUpperCase());
			cs.setString(3, tituloReceber.getParcela().toUpperCase());
			cs.setDate(4, new java.sql.Date(tituloReceber.getDataEmissao().getTime()));
			cs.setString(5, tituloReceber.getCompetencia());
			cs.setInt(6, tituloReceber.getCliente().getCodcliente());
			if (tituloReceber.getTipoDocumento().getCodtipodocumento() != 0)
				cs.setInt(7, tituloReceber.getTipoDocumento().getCodtipodocumento());
			else
				cs.setNull(7, Types.NULL);

			if (tituloReceber.getFonrec().getId() == null)
				cs.setNull(8, Types.NULL);
			else
				cs.setInt(8, tituloReceber.getFonrec().getId());

			if (String.valueOf(tituloReceber.getPortador().getCodportador()) == null) {
				cs.setNull(9, Types.NULL);
			} else {
				cs.setInt(9, tituloReceber.getPortador().getCodportador());
			}

			if (tituloReceber.getDescricaoFatura() == null) {
				cs.setNull(10, Types.NULL);
			} else {
				cs.setString(10, tituloReceber.getDescricaoFatura().toUpperCase());
			}

			if (tituloReceber.getValor2() != null) {
				cs.setDouble(11, tituloReceber.getValor2());
			} else {
				cs.setDouble(11, tituloReceber.getValor());
			}

			cs.setDouble(12, tituloReceber.getDesconto());

			if (String.valueOf(tituloReceber.getValortaxaadm()) == null) {
				cs.setNull(13, Types.NULL);
			} else {
				cs.setDouble(13, tituloReceber.getValortaxaadm());
			}

			cs.setDate(14, new java.sql.Date(tituloReceber.getVencimento().getTime()));
			cs.setString(15, tituloReceber.getHistorico().toUpperCase());

			if (tituloReceber.getCaixaloja().getCodCaixaLoja() == null) {
				cs.setNull(16, Types.NULL);
			} else {
				cs.setInt(16, tituloReceber.getCaixaloja().getCodCaixaLoja());
			}

			cs.setInt(17, usuarioLogado.getCodigo());
			cs.setString(18, "A");
			cs.execute();

			retornoid = cs.getInt(1);

			if (lstRetencao != null && lstRetencao.size() > 0) {
				List<ImpostoBean> listaRetAux = lstRetencao;
				for (int i = 0; i < listaRetAux.size(); i++) {
					listaRetAux.get(i).setDuplicata(retornoid);
				}

				String sql = "insert into clin.retencao_receber(duplicata, imposto, "
						+ "perc_ret, valor_ret, cd_pagar, base_ret) values (?, ?, ?, ?, ?, ?)";

				for (ImpostoBean ip : listaRetAux) {
					PreparedStatement pstm = conexao.prepareStatement(sql);
					pstm.setInt(1, ip.getDuplicata());
					pstm.setString(2, ip.getDescImposto());
					pstm.setDouble(3, ip.getPcRentencao());
					pstm.setDouble(4, ip.getValorRetencao());
					pstm.setInt(5, 0);
					pstm.setDouble(6, ip.getValorBase());
					pstm.execute();
				}
			}

			if (comitar) {
				conexao.commit();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				cs.close();
				if (comitar)
					conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return retornoid;

	}

	// teste
	public boolean editarReceber(TituloReceberBean tituloReceber, List<ImpostoBean> lstRetencao)
			throws ProjetoException {

		String sql = "update clin.recdup set documento = ?, parcela = ?, data_emissao = ?, "
				+ "competencia = ?, id_cliente = ?, codtipdoc = ?, codfonterec = ?, id_portador = ?, "
				+ "desc_fatura = ?, valor = ?, desconto = ?, valortaxaadm = ?, vencimento = ?, "
				+ "historico = ?, codcaixaloja = ? where idreceber = ?";

		Connection conexao = null;
		PreparedStatement ps = null;

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);

			ps.setString(1, tituloReceber.getDocumento().toUpperCase());
			ps.setString(2, tituloReceber.getParcela().toUpperCase());
			ps.setDate(3, new java.sql.Date(tituloReceber.getDataEmissao().getTime()));
			ps.setString(4, tituloReceber.getCompetencia());
			ps.setInt(5, tituloReceber.getCliente().getCodcliente());
			if (tituloReceber.getTipoDocumento().getCodtipodocumento() != 0)
				ps.setInt(6, tituloReceber.getTipoDocumento().getCodtipodocumento());
			else
				ps.setNull(6, Types.NULL);
			ps.setInt(7, tituloReceber.getFonrec().getId());
			ps.setInt(8, tituloReceber.getPortador().getCodportador());
			ps.setString(9, tituloReceber.getDescricaoFatura().toUpperCase());
			ps.setDouble(10, tituloReceber.getValor());
			ps.setDouble(11, tituloReceber.getDesconto());

			if (String.valueOf(tituloReceber.getValortaxaadm()) == null) {
				ps.setNull(12, Types.NULL);
			} else {
				ps.setDouble(12, tituloReceber.getValortaxaadm());
			}

			ps.setDate(13, new java.sql.Date(tituloReceber.getVencimento().getTime()));
			ps.setString(14, tituloReceber.getHistorico().toUpperCase());

			if (tituloReceber.getCaixaloja().getCodCaixaLoja() == null) {
				ps.setNull(15, Types.NULL);
			} else {
				ps.setInt(15, tituloReceber.getCaixaloja().getCodCaixaLoja());
			}

			ps.setInt(16, tituloReceber.getIdFinanceiro());
			ps.executeUpdate();

			ps.close();

			String sql2 = "delete from clin.retencao_receber where duplicata = ?";

			ps = conexao.prepareStatement(sql2);
			ps.setInt(1, tituloReceber.getIdFinanceiro());
			ps.execute();

			if (lstRetencao != null && lstRetencao.size() > 0) {
				List<ImpostoBean> listaRetAux = lstRetencao;
				for (int i = 0; i < listaRetAux.size(); i++) {
					listaRetAux.get(i).setDuplicata(tituloReceber.getIdFinanceiro());
				}

				sql = "insert into clin.retencao_receber(duplicata, imposto, "
						+ "perc_ret, valor_ret, cd_pagar, base_ret) values (?, ?, ?, ?, ?, ?)";

				for (ImpostoBean ip : listaRetAux) {
					PreparedStatement pstm = conexao.prepareStatement(sql);
					pstm.setInt(1, ip.getDuplicata());
					pstm.setString(2, ip.getDescImposto());
					pstm.setDouble(3, ip.getPcRentencao());
					pstm.setDouble(4, ip.getValorRetencao());
					pstm.setInt(5, 0);
					pstm.setDouble(6, ip.getValorBase());
					pstm.execute();
					pstm.close();
				}
			}

			conexao.commit();

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}

	public List<ImpostoBean> listarRetencoesDocRec(Integer duplicata) throws ProjetoException {

		String sql = "select * from clin.retencao_receber where duplicata = ?";

		List<ImpostoBean> lista = new ArrayList<>();

		Connection conexao = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, duplicata);
			rs = ps.executeQuery();

			while (rs.next()) {
				ImpostoBean ip = new ImpostoBean();
				ip.setDuplicata(rs.getInt("duplicata"));
				ip.setDescImposto(rs.getString("imposto"));
				ip.setPcRentencao(rs.getDouble("perc_ret"));
				ip.setValorRetencao(rs.getDouble("valor_ret"));
				ip.setValorBase(rs.getDouble("base_ret"));

				lista.add(ip);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public Double recuperarValorTotalRetDocRec(Integer duplicata) throws ProjetoException {
		String sql = "select sum(valor_ret) as total from clin.retencao_receber " + "where duplicata = ?";

		Double valorTotal = 0.0;

		Connection conexao = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			ps = conexao.prepareStatement(sql);
			ps.setInt(1, duplicata);
			rs = ps.executeQuery();

			while (rs.next()) {
				valorTotal = +rs.getDouble("total");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return valorTotal;
	}

	public void salvarRetencao(ImpostoBean impostoBean) throws ProjetoException {

		Connection con = null;
		PreparedStatement ps = null;

		String sql = " INSERT INTO clin.retencao_receber(duplicata, imposto, perc_ret, "
				+ "cd_pagar, base_ret) VALUES (?, ?, ?, ?, ?); ";

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			ps.setInt(1, impostoBean.getDuplicata());
			ps.setString(2, impostoBean.getDescImposto());
			ps.setDouble(3, impostoBean.getPcRentencao());
			ps.setInt(4, 0);
			ps.setDouble(5, impostoBean.getValorBase());
			ps.execute();

			// con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean salvarBaixa(BaixaReceber baixa, Boolean commit, Connection conn, Integer codcaixaloja)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Connection con = null;
		if (commit)
			con = ConnectionFactory.getConnection();
		else
			con = conn;

		boolean baixou = false;

		String sql = " INSERT INTO clin.recdupbx(duplicata, dtpag, valorpago, tpbaixa,  "
				+ "  vlrdesc, vlrjuros, vlrmulta, opcad, dtcadastro, vlratual, "
				+ "  seqcaixa, vlrcorrecao, codbanco,ord, frbaixa, codcaixaloja) "
				+ "  VALUES (?, current_date, ?,?,?,?,?,?,current_date,?,?,?,?,((select coalesce(max(ord)+1,1) from clin.recdupbx where duplicata=?)),?,?) returning id ";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement(sql);

			ps.setInt(1, baixa.getDuplicata());
			ps.setDouble(2, baixa.getValorBaixado());
			ps.setString(3, baixa.getTipoBaixaString());

			if (baixa.getDesconto() == null) {
				ps.setNull(4, Types.NUMERIC);
			} else {
				ps.setDouble(4, baixa.getDesconto());
			}

			if (baixa.getJuros() == null) {
				ps.setNull(5, Types.NUMERIC);
			} else {
				ps.setDouble(5, baixa.getJuros());
			}

			if (baixa.getMulta() == null) {
				ps.setNull(6, Types.NUMERIC);
			} else {
				ps.setDouble(6, baixa.getMulta());
			}

			ps.setInt(7, user_session.getCodigo());
			ps.setDouble(8, baixa.getValorAtual());
			ps.setInt(9, baixa.getCodSeqCaixa());

			if (baixa.getVlrCorrecao() == null) {
				ps.setNull(10, Types.NUMERIC);
			} else {
				ps.setDouble(10, baixa.getVlrCorrecao());
			}

			ps.setInt(11, baixa.getBanco().getId());
			ps.setInt(12, baixa.getDuplicata());
			ps.setString(13, baixa.getFormaBaixa());
			ps.setInt(14, codcaixaloja);

			rs = ps.executeQuery();
			Integer idRecdupbx = null;
			if (rs.next()) {
				idRecdupbx = rs.getInt("id");
			}

			ps.close();

			// INSERT MOVTESOURARIA
			sql = " INSERT INTO clin.movtesouraria (idbanco,  dtmovimento, valor, complemento, "
					+ " tipo, opcad, codreceber, obs, seqcaixadiario, idrecdupbx) VALUES (?,  CURRENT_DATE, ?, ?, ?, ?, ?, ?, ?,?); ";

			ps = con.prepareStatement(sql);

			ps.setInt(1, baixa.getBanco().getId());
			ps.setDouble(2, baixa.getValorBaixado());
			ps.setString(3, "");
			ps.setString(4, "CD");
			ps.setInt(5, user_session.getCodigo());
			ps.setInt(6, baixa.getDuplicata());
			ps.setString(7, "");
			ps.setInt(8, baixa.getCodSeqCaixa());
			ps.setInt(9, idRecdupbx);

			ps.execute();

			if ((baixa.getValorBaixado() - baixa.getValorEmAberto()) == 0) {

				String sql3 = "update clin.recdup set status = 'F' where idreceber = ?";

				PreparedStatement ps3 = con.prepareStatement(sql3);
				ps3.setInt(1, baixa.getDuplicata());
				ps3.executeUpdate();
				ps3.close();
			}
			if (commit == true)
				con.commit();

			baixou = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				if (commit == true)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return baixou;
	}

	public boolean salvarBaixaPagamentoNP(BaixaReceber baixa, Boolean commit, Connection conn, Integer codcaixaloja,
			PagamentoBean pg, Integer codvenda2, Integer codCredCaixaLoja) throws ProjetoException, SQLException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Connection con = null;
		if (commit)
			con = ConnectionFactory.getConnection();
		else
			con = conn;

		boolean baixou = false;
		PreparedStatement ps = null;

		try {

			String sql = " INSERT INTO clin.recdupbx(duplicata, dtpag, valorpago, tpbaixa,  "
					+ "  vlrdesc, vlrjuros, vlrmulta, opcad, dtcadastro, vlratual, "
					+ "  seqcaixa, vlrcorrecao, codbanco,ord, frbaixa, codcaixaloja, baixa_temporaria, codvenda2, codcredcaixaloja) "
					+ "  VALUES (?, current_date, ?,?,?,?,?,?,current_date,?,?,?,?,((select coalesce(max(ord)+1,1) from clin.recdupbx where duplicata=?)),?,?,?,?,?) returning id ";

			ps = con.prepareStatement(sql);

			ps.setInt(1, baixa.getDuplicata());
			ps.setDouble(2, baixa.getValorBaixado());
			ps.setString(3, baixa.getTipoBaixaString());

			if (baixa.getDesconto() == null) {
				ps.setNull(4, Types.NUMERIC);
			} else {
				ps.setDouble(4, baixa.getDesconto());
			}

			if (baixa.getJuros() == null) {
				ps.setNull(5, Types.NUMERIC);
			} else {
				ps.setDouble(5, baixa.getJuros());
			}

			if (baixa.getMulta() == null) {
				ps.setNull(6, Types.NUMERIC);
			} else {
				ps.setDouble(6, baixa.getMulta());
			}

			ps.setInt(7, user_session.getCodigo());

			double valorAtual = valorAbertoRecdupBxNpParcial(baixa.getDuplicata(), con);
			ps.setDouble(8, valorAtual);

			ps.setInt(9, baixa.getCodSeqCaixa());

			if (baixa.getVlrCorrecao() == null) {
				ps.setNull(10, Types.NUMERIC);
			} else {
				ps.setDouble(10, baixa.getVlrCorrecao());
			}

			int codbanco = codBancoTesouraria();
			if (pg.getTipo().equals("D")) {
				ps.setInt(11, codbanco);
			} else {
				ps.setNull(11, Types.NULL);
			}

			ps.setInt(12, baixa.getDuplicata());

			if (pg.getTipo().equals("D")) {
				ps.setString(13, "1");
			} else {
				ps.setString(13, "4");
			}

			ps.setInt(14, codcaixaloja);
			ps.setBoolean(15, true);
			ps.setInt(16, codvenda2);
			ps.setInt(17, codCredCaixaLoja);

			if (commit)
				con.commit();

			// atualizarValorAtual(baixa.getDuplicata());

			baixou = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				if (commit)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return baixou;
	}

	/*
	 * public boolean atualizarValorAtual(Integer cod) throws ProjetoException {
	 * String sql = "update clin.recdupbx set vlratual = ? where duplicata = ?";
	 * Double valor = valorAbertoRecdupBxNpParcial((cod); Connection conexao = null;
	 * try { conexao = ConnectionFactory.getConnection(); PreparedStatement ps =
	 * conexao.prepareStatement(sql);
	 * 
	 * ps.setDouble(1, valor); ps.setInt(2, cod);
	 * 
	 * ps.executeUpdate();
	 * 
	 * conexao.commit();
	 * 
	 * return true; } catch (SQLException ex) { ex.printStackTrace(); } finally {
	 * try { conexao.close(); } catch (SQLException ex) { ex.printStackTrace(); } }
	 * return false; }
	 */

	public boolean atualizarTituloPagoTemporario(Integer cod, Connection conexao) throws ProjetoException {
		String sql = "update clin.recdupbx set baixa_temporaria = false where codcredcaixaloja = ?";
		PreparedStatement ps = null;
		try {

			ps = conexao.prepareStatement(sql);

			ps.setInt(1, cod);
			ps.executeUpdate();

			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public Double valorAbertoRecdupBxNpParcial(Integer duplicata, Connection conexao) throws ProjetoException {
		String sql = "select r.valor - coalesce(r.desconto,0)+coalesce(r.juros_dia,0)+coalesce(r.multaatraso,0)-coalesce(r.vlrretencao,0)-coalesce(r.valortaxaadm,0)"
				+ " - coalesce((select sum(coalesce(valorpago,0)-coalesce(vlrdesc,0)-coalesce(vlrjuros,0)-coalesce(vlrmulta,0)) from clin.recdupbx"
				+ " where recdupbx.duplicata=r.idreceber),0) abertofinal, *  from clin.recdup r join clin.cliente c on (r.id_cliente = c.codcliente)"
				+ " join clin.portador p on (r.id_portador = p.codportador)"
				+ " join clin.tipodocumento t on (r.codtipdoc = t.codtipodocumento) " + " where r.idreceber=?";

		Double aberto = 0.0;

		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {

			pstm = conexao.prepareStatement(sql);
			pstm.setInt(1, duplicata);
			rs = pstm.executeQuery();

			while (rs.next()) {
				aberto = rs.getDouble("abertofinal");
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pstm.close();
				rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return aberto;
	}

	public Integer codBancoTesouraria() throws ProjetoException {

		Connection conexao = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		Integer cod = 0;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		String sql = "select cod_banco_tesouraria from clin.parametros";

		try {
			conexao = ConnectionFactory.getConnection();
			pstm = conexao.prepareStatement(sql);
			rs = pstm.executeQuery();

			while (rs.next()) {
				cod = rs.getInt("cod_banco_tesouraria");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pstm.close();
				rs.close();
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return cod;
	}

	public Double recuperarValorPagoBaixa(Integer duplicata) throws ProjetoException {

		String sql = "select sum(valorpago) as valorPago from clin.recdupbx " + "where duplicata = ?";
		Double valor = 0.0;

		Connection conexao = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			pstm = conexao.prepareStatement(sql);
			pstm.setInt(1, duplicata);
			rs = pstm.executeQuery();

			while (rs.next()) {
				valor = rs.getDouble("valorPago");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pstm.close();
				rs.close();
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return valor;
	}

	public Double recuperarValorJurosBaixa(Integer duplicata) throws ProjetoException {

		String sql = "select sum(vlrjuros) as valorJuros from clin.recdupbx " + "where duplicata = ?";
		Double valor = 0.0;

		Connection conexao = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			pstm = conexao.prepareStatement(sql);
			pstm.setInt(1, duplicata);
			rs = pstm.executeQuery();

			while (rs.next()) {
				valor = rs.getDouble("valorJuros");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pstm.close();
				rs.close();
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return valor;
	}

	public Double recuperarValorMultaBaixa(Integer duplicata) throws ProjetoException {

		String sql = "select sum(vlrmulta) as valorMulta from clin.recdupbx " + "where duplicata = ?";
		Double valor = 0.0;

		Connection conexao = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			pstm = conexao.prepareStatement(sql);
			pstm.setInt(1, duplicata);
			rs = pstm.executeQuery();

			while (rs.next()) {
				valor = rs.getDouble("valorMulta");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pstm.close();
				rs.close();
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return valor;
	}

	public Double recuperarValorDescontoBaixa(Integer duplicata) throws ProjetoException {

		String sql = "select sum(vlrdesc) as valorDesconto from clin.recdupbx " + "where duplicata = ?";
		Double valor = 0.0;

		Connection conexao = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			pstm = conexao.prepareStatement(sql);
			pstm.setInt(1, duplicata);
			rs = pstm.executeQuery();

			while (rs.next()) {
				valor = rs.getDouble("valorDesconto");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pstm.close();
				rs.close();
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return valor;
	}

	public ArrayList<TituloReceberBean> todasAtrasadasPorCliente(Integer codCliente) throws ProjetoException {

		String sql = " select * from clin.recdup  r join clin.cliente c on "
				+ " (r.id_cliente = c.codcliente) join clin.portador p "
				+ " on (r.id_portador = p.codportador) where id_cliente = ? and vencimento<=current_date  order by 1; ";

		ArrayList<TituloReceberBean> lista = new ArrayList<>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setInt(1, codCliente);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			set = ps.executeQuery();

			while (set.next()) {

				ClienteBean clienteBean = new ClienteBean();
				PortadorBean portBean = new PortadorBean();
				TituloReceberBean bean = new TituloReceberBean();

				bean.setIdFinanceiro(set.getInt("idreceber"));
				bean.setDataEmissao(set.getDate("data_emissao"));
				bean.setCompetencia(set.getString("competencia"));

				clienteBean.setCodcliente(set.getInt("id_cliente"));
				clienteBean.setNome(set.getString("nome"));

				portBean.setCodportador(set.getInt("id_portador"));
				portBean.setDescricao(set.getString("descricao"));

				bean.setValor(set.getDouble("valor"));
				bean.setDesconto(set.getDouble("desconto"));

				bean.setVencimento(set.getDate("vencimento"));
				bean.setHistorico(set.getString("historico"));
				bean.setDescricaoFatura(set.getString("desc_fatura"));
				bean.setObjCobranca(set.getString("obs_cobranca"));
				bean.setStatus(set.getString("status"));
				bean.setCliente(clienteBean);
				bean.setPortador(portBean);
				lista.add(bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	// argemiro metodo
	public ArrayList<TituloReceberBean> todasAtrasadasPorCliente2(Integer codCliente) throws ProjetoException {
		String sql = " select * from clin.recdup  r join clin.cliente c on "
				+ " (r.id_cliente = c.codcliente) join clin.portador p "
				+ " on (r.id_portador = p.codportador) where id_cliente = ? and vencimento>current_date  order by 1; ";

		ArrayList<TituloReceberBean> lista2 = new ArrayList<>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setInt(1, codCliente);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
						set = ps.executeQuery();

			while (set.next()) {

				ClienteBean clienteBean = new ClienteBean();
				PortadorBean portBean = new PortadorBean();
				TituloReceberBean bean = new TituloReceberBean();

				bean.setIdFinanceiro(set.getInt("idreceber"));
				bean.setDataEmissao(set.getDate("data_emissao"));
				bean.setCompetencia(set.getString("competencia"));

				clienteBean.setCodcliente(set.getInt("id_cliente"));
				clienteBean.setNome(set.getString("nome"));

				portBean.setCodportador(set.getInt("id_portador"));
				portBean.setDescricao(set.getString("descricao"));

				bean.setValor(set.getDouble("valor"));
				bean.setDesconto(set.getDouble("desconto"));

				bean.setVencimento(set.getDate("vencimento"));
				bean.setHistorico(set.getString("historico"));
				bean.setDescricaoFatura(set.getString("desc_fatura"));
				bean.setObjCobranca(set.getString("obs_cobranca"));
				bean.setStatus(set.getString("status"));
				bean.setCliente(clienteBean);
				bean.setPortador(portBean);
				lista2.add(bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista2;
	}

	public ArrayList<TituloReceberBean> listarTitulorEmAberto(AntecipacaoBean antecipa) throws ProjetoException {

		String sql = " select r.valor - coalesce(r.desconto,0)+coalesce(r.juros_dia,0)+coalesce(r.multaatraso,0)-coalesce(r.vlrretencao,0)-coalesce(r.valortaxaadm,0)"
				+ "- coalesce((select sum(coalesce(valorpago,0)-coalesce(vlrdesc,0)-coalesce(vlrjuros,0)-coalesce(vlrmulta,0)) from clin.recdupbx"
				+ " where recdupbx.duplicata=r.idreceber),0) aberto, *  from clin.recdup r join clin.cliente c on (r.id_cliente = c.codcliente)"
				+ " join clin.portador p on (r.id_portador = p.codportador)"
				+ "  join clin.tipodocumento t on (r.codtipdoc = t.codtipodocumento) ";

		ArrayList<TituloReceberBean> lista = new ArrayList<>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			if (antecipa.getCliente() != null) {
				if (antecipa.getCliente().getCodcliente() != null) {
					sql += "where c.codcliente =  " + antecipa.getCliente().getCodcliente() + "and r.status = 'A' ";
				}
			} else {
				sql += "where r.status = 'A' ";
			}
			if (antecipa.getDataRef().equals("E")) {
				sql += "and r.data_emissao between ? and ?  and t.codtipodocumento = ? order by 1; ";
			} else {
				sql += "and r.vencimento between ? and ?  and t.codtipodocumento = ? order by 1; ";
			}
			ps = con.prepareStatement(sql);

			ps.setDate(1, new java.sql.Date(antecipa.getDataIniFiltro().getTime()));
			ps.setDate(2, new java.sql.Date(antecipa.getDataFimFiltro().getTime()));
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			ps.setInt(3, antecipa.getTipo().getCodtipodocumento());

			set = ps.executeQuery();

			while (set.next()) {

				ClienteBean clienteBean = new ClienteBean();
				PortadorBean portBean = new PortadorBean();
				TituloReceberBean bean = new TituloReceberBean();

				bean.setIdFinanceiro(set.getInt("idreceber"));
				bean.setDataEmissao(set.getDate("data_emissao"));
				bean.setCompetencia(set.getString("competencia"));

				bean.setValoraberto(set.getDouble("aberto"));

				clienteBean.setCodcliente(set.getInt("id_cliente"));
				clienteBean.setNome(set.getString("nome"));

				portBean.setCodportador(set.getInt("id_portador"));
				portBean.setDescricao(set.getString("descricao"));

				bean.setValor(set.getDouble("valor"));
				bean.setDesconto(set.getDouble("desconto"));

				bean.setVencimento(set.getDate("vencimento"));
				bean.setHistorico(set.getString("historico"));
				bean.setDescricaoFatura(set.getString("desc_fatura"));
				bean.setObjCobranca(set.getString("obs_cobranca"));
				bean.setStatus(set.getString("status"));
				bean.setCliente(clienteBean);
				bean.setPortador(portBean);
				lista.add(bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	public boolean salvarAntecipa(AntecipacaoBean a, List<TituloReceberBean> lista, Integer idSelecionado,
			AntecipacaoBean antecipaBean) throws ProjetoException, ParseException {
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "INSERT INTO clin.movtesouraria(idbanco, valor, dtmovimento, "
				+ "complemento, tipo, seqcaixadiario) VALUES (?, ?, ?, ?, ?, ?) ";

		PreparedStatement stmt = null;
		Connection con = null;

		try {
			con = ConnectionFactory.getConnection();

			TesourariaDAO dao = new TesourariaDAO();
			CaixaDiarioBean caixa = dao.retornaCaixaAtual();

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, a.getBanco().getId());
			stmt.setDouble(2, a.getValorAntecipado());
			stmt.setDate(3, new java.sql.Date(a.getDataEmissao().getTime()));
			stmt.setString(4, "ANTECIPAÇÃO DE RECEB�?VEIS");
			stmt.setString(5, "CD");
			stmt.setLong(6, caixa.getSeqcaixadiario());
			stmt.execute();

			stmt.close();

			sql = " INSERT INTO clin.bordero_desc(codbancocred, codtipdoc, "
					+ "dtemissao, valorbruto, valorliquido, encargos, situacao, status, opcad, "
					+ "dtcreditado, codfilial, dtcadastro) " + " VALUES (?, "
					+ "(select codtipdoc_antec from clin.parametros ), "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?,  current_timestamp) " + "returning seqbordero ";

			stmt = con.prepareStatement(sql);

			stmt.setInt(1, a.getBanco().getId());
			stmt.setDate(2, new java.sql.Date(a.getDataEmissao().getTime()));
			stmt.setDouble(3, a.getValorAntecipado());
			stmt.setDouble(4, a.getLiquidoReceber());
			stmt.setDouble(5, a.getEncargos());
			stmt.setString(6, "CD");
			stmt.setString(7, "CRED");
			stmt.setInt(8, user_session.getCodigo());
			stmt.setDate(9, new java.sql.Date(a.getDataRecebimento().getTime()));

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				a.setId_antecipa(rs.getInt("seqbordero"));

				stmt.close();

				for (TituloReceberBean listAnt : lista) {
					String sql3 = "INSERT INTO clin.bordero_desc1(seqbordero, codrecdup, situacao) VALUES (?, ?, 'CD') ";
					stmt = con.prepareStatement(sql3);
					stmt.setDouble(1, a.getId_antecipa());
					stmt.setDouble(2, listAnt.getIdFinanceiro()); // id Receber
					stmt.execute();

					stmt.close();

					String sql4 = "INSERT INTO clin.recdupbx  (duplicata, ord, dtpag, dtvenc, vlratual, valorpago, "
							+ " tpbaixa, frbaixa)" + " VALUES (?, ?, ?, ?, ?, ?, " + " ?, ?) ";

					stmt = con.prepareStatement(sql4);
					stmt.setDouble(1, listAnt.getIdFinanceiro());
					stmt.setInt(2, 1);
					stmt.setDate(3, new java.sql.Date(a.getDataRecebimento().getTime()));
					stmt.setDate(4, new java.sql.Date(a.getDataRecebimento().getTime()));
					stmt.setDouble(5, a.getLiquidoReceber());
					stmt.setDouble(6, a.getLiquidoReceber());
					stmt.setString(7, "1");
					stmt.setString(8, "4");

					stmt.execute();

					stmt.close();

					String updateSituacao = "UPDATE clin.recdup SET situacao = 'F' WHERE  idreceber = ? ";
					stmt = con.prepareStatement(updateSituacao);
					stmt.setDouble(1, listAnt.getIdFinanceiro()); // id Receber

					stmt.execute();

					stmt.close();

				}

			}

			rs.close();

			String sql5 = "INSERT INTO clin.pagdup(codigo, valor,  dtemissao, dtvcto, "
					+ "codfilial, parcela, historico, desconto, situacao, " + "codtipdoc, iddespesa, codport, idccusto)"
					+ " VALUES(?, ?, ?, ?, ?)  "
					+ "(select codtipdoc_antec from clin.parametros where codfilial=?), "
					+ "(select coddesp_antec from clin.parametros where codfilial=?),  ";

			stmt = con.prepareStatement(sql5);

			stmt.setDouble(1, a.getId_antecipa());
			stmt.setDouble(2, a.getValorAntecipado());
			stmt.setDate(3, new java.sql.Date(a.getDataEmissao().getTime()));
			stmt.setDate(4, new java.sql.Date(a.getDataRecebimento().getTime()));
			stmt.setString(5, "UN");
			stmt.setString(6, "ENCARGOS DA ANTECIPAÇÃO");
			stmt.setDouble(7, 0);
			stmt.setString(8, "F");


			stmt.execute();

			stmt.close();

			String sql6 = "INSERT INTO clin.pagdupbx  (codigo, valorpago, valoratual, dtvcto, dtpagto, tpbaixa,  frbaixa, "
					+ " vlrjuros, vlrmulta, vlrdesc, ord) " + " VALUES (?, ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?) ";

			/*
			 * TIPO <f:selectItem itemLabel="Total" itemValue="1" /> <f:selectItem
			 * itemLabel="Parcial" itemValue="2" /> FORMA: <f:selectItem
			 * itemLabel="Dinheiro" itemValue="1" /> <f:selectItem itemLabel="Cheque"
			 * itemValue="2" /> <f:selectItem itemLabel="Cartão" itemValue="3" />
			 */
			stmt = con.prepareStatement(sql6);
			stmt.setDouble(1, a.getId_antecipa());
			stmt.setDouble(2, a.getLiquidoReceber());
			stmt.setDouble(3, a.getLiquidoReceber());
			stmt.setDate(4, new java.sql.Date(a.getDataRecebimento().getTime()));
			stmt.setDate(5, new java.sql.Date(a.getDataRecebimento().getTime()));
			stmt.setString(6, "1");
			stmt.setString(7, "1");
			stmt.setFloat(8, 0);
			stmt.setFloat(9, 0);
			stmt.setDouble(10, a.getEncargos());
			stmt.setInt(11, 1);
			stmt.execute();

			stmt.close();

			String sql7 = "update clin.recdup set status='AN' where idreceber= ?";
			PreparedStatement ps7 = con.prepareStatement(sql7);
			ps7.setInt(1, idSelecionado);
			ps7.executeUpdate();

			ps7.close();

			String sql8 = "INSERT INTO clin.recdupbx (duplicata, ord,  valorpago, vlrdesc, vlratual, tpbaixa)"
					+ " VALUES (?, ?, ?, ?, ?, ?) ";

			stmt = con.prepareStatement(sql8);
			stmt.setInt(1, idSelecionado);
			stmt.setInt(2, 0);
			stmt.setDouble(3, antecipaBean.getLiquidoReceber());
			stmt.setDouble(4, antecipaBean.getEncargos());
			stmt.setDouble(5, antecipaBean.getValorAntecipado());
			stmt.setString(6, "1");
			stmt.execute();

			stmt.close();

			String sql9 = "INSERT INTO clin.movtesouraria(idbanco, valor, dtmovimento, "
					+ "complemento, tipo, seqcaixadiario, codfilial, codpagar) " + " VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";

			stmt = con.prepareStatement(sql9);
			stmt.setInt(1, a.getBanco().getId());
			stmt.setDouble(2, a.getEncargos());
			stmt.setDate(3, new java.sql.Date(a.getDataEmissao().getTime()));
			stmt.setString(4, "ENCARGOS DE ANTECIPAÇÃO");
			stmt.setString(5, "DB");
			stmt.setLong(6, caixa.getSeqcaixadiario());
			stmt.setInt(7, idSelecionado);
			stmt.execute();

			con.commit();

			return true;

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void salvarAntecipaasdsad(AntecipacaoBean a) throws ProjetoException {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet set = null;

		try {
			con = ConnectionFactory.getConnection();

			int id = 0;
			String sql = "INSERT INTO clin.movtesouraria(idbanco, valor) VALUES (?, ?) returning codseq; ";

			ps = con.prepareStatement(sql);
			set = ps.executeQuery();

			while (set.next()) {

				ps.setInt(1, a.getBanco().getId());
				ps.setDouble(2, a.getValorAntecipado());
				ps.setInt(3, id);
				ps.execute();
			}

			ps.close();

			sql += " INSERT INTO clin.bordero_desc(valorbruto, encargos)  VALUES (?, ?)";

			ps = con.prepareStatement(sql);
			while (set.next()) {

				ps.setDouble(1, a.getValorAntecipado());
				ps.setDouble(2, a.getEncargos());
				ps.execute();
			}

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<TituloReceberBean> listarTitulorNegociarPromissoria(AntecipacaoBean antecipa)
			throws ProjetoException {

		String sql = "select (r.valor - coalesce(baixa.valorpago,0)) as final, coalesce(baixa.valorpago,0) valorpago, * from clin.recdup r  "
				+ "join clin.cliente c on (r.id_cliente = c.codcliente) "
				+ "join clin.portador p on (r.id_portador = p.codportador) "
				+ "left join (select sum (valorpago) valorpago, duplicata from clin.recdupbx x group by duplicata) baixa on (baixa.duplicata = r.idreceber) "

				+ "where c.codcliente = ? and r.status = 'A' and (r.valor - coalesce(baixa.valorpago,0))>0 and codtipdoc =(select codtipdoc_promissoria from clin.parametros where codempresa=?)  order by 1; ";

		ArrayList<TituloReceberBean> lista = new ArrayList<>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setInt(1, antecipa.getCliente().getCodcliente());
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");


			set = ps.executeQuery();

			while (set.next()) {

				ClienteBean clienteBean = new ClienteBean();
				PortadorBean portBean = new PortadorBean();
				TituloReceberBean bean = new TituloReceberBean();

				bean.setIdFinanceiro(set.getInt("idreceber"));
				bean.setDataEmissao(set.getDate("data_emissao"));
				bean.setCompetencia(set.getString("competencia"));
				clienteBean.setCodcliente(set.getInt("id_cliente"));
				clienteBean.setNome(set.getString("nome"));

				portBean.setCodportador(set.getInt("id_portador"));
				portBean.setDescricao(set.getString("descricao"));

				bean.setValor(set.getDouble("valor"));
				bean.setValoraberto(set.getDouble("final"));
				bean.setDesconto(set.getDouble("desconto"));

				bean.setVencimento(set.getDate("vencimento"));
				bean.setHistorico(set.getString("historico"));
				bean.setDescricaoFatura(set.getString("desc_fatura"));
				bean.setObjCobranca(set.getString("obs_cobranca"));
				bean.setStatus(set.getString("status"));
				bean.setCliente(clienteBean);
				bean.setPortador(portBean);
				lista.add(bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	public ArrayList<TituloReceberBean> todosFinanceiro(AntecipacaoBean a) throws ProjetoException {

		String sql = " select r.documento, r.parcela, r.codfonterec, r.idreceber,r.data_emissao,r.competencia,r.id_cliente,c.nome,r.valor,r.desconto,coalesce(r.valortaxaadm,0) valortaxaadm, "
				+ " r.valor - coalesce(r.desconto,0)+coalesce(r.juros_dia,0)+coalesce(r.multaatraso,0)-coalesce(r.vlrretencao,0)-coalesce(r.valortaxaadm,0)"
				+ " - coalesce((select sum(coalesce(valorpago,0)+coalesce(vlrdesc,0)-coalesce(vlrjuros,0)-coalesce(vlrmulta,0)) from clin.recdupbx"
				+ " where recdupbx.duplicata=r.idreceber),0) valoraberto,"
				+ " r.vencimento,r.historico,r.desc_fatura,r.obs_cobranca,r.status, "
				+ " p.codportador,p.descricao descPortador,td.codtipodocumento,td.descricao as descDocumento  from clin.recdup r join clin.cliente c on  "
				+ " (r.id_cliente = c.codcliente)  left join clin.portador p  on (r.id_portador = p.codportador) "
				+ "	left join clin.tipodocumento td  on (r.codtipdoc = td.codtipodocumento) where  ((r.codfilial = ?) or (r.codfilial in (select codempresa_autorizada from clin.empresa_aut_clientes"
				+ " where codempresa_origem=?))) ";

		if (a.getCliente() != null) {
			if (a.getCliente().getCodcliente() != null) {
				sql += " and id_cliente = ?";
			}
		}

		if ((a.getTipo().getCodtipodocumento() != null) && (a.getTipo().getCodtipodocumento() != 0)) {
			sql += " and codtipodocumento = ?";
		}

		if (a.getDataRecebimento() != null || a.getDataEmissao() != null) {
			sql += " and r.vencimento between ? and ? ";
		}
		if ((a.getTipoDoc() != null) && (!a.getTipoDoc().equals(""))) {
			sql += " and  r.documento ilike ? ";
		}

		if ((a.getCodigo() != null) && (a.getCodigo() != 0)) {
			sql += " and r.idreceber = ? ";
		}

		if ((a.getSituacao() != null) && (!a.getSituacao().equals("T")) && (!a.getSituacao().equals(""))) {
			sql += "  and status = ? ";
		}

		sql += "  order by 1 desc; ";

		ArrayList<TituloReceberBean> lista = new ArrayList<>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			Integer i = 0;

			if (a.getCliente() != null) {
				if (a.getCliente().getCodcliente() != null) {
					ps.setInt(i, a.getCliente().getCodcliente());
					i++;
				}
			}
			if ((a.getTipo().getCodtipodocumento() != null) && (a.getTipo().getCodtipodocumento() != 0)) {
				ps.setInt(i, a.getTipo().getCodtipodocumento());
				i++;
			}
			if (a.getDataRecebimento() != null || a.getDataEmissao() != null) {
				ps.setDate(i, new java.sql.Date(a.getDataEmissao().getTime()));
				ps.setDate(i + 1, new java.sql.Date(a.getDataRecebimento().getTime()));
				i = i + 2;
			}
			if ((a.getTipoDoc() != null) && (!a.getTipoDoc().equals(""))) {
				ps.setString(i, a.getTipoDoc());
				i++;
			}
			if ((a.getCodigo() != null) && (a.getCodigo() != 0)) {
				ps.setInt(i, a.getCodigo());
				i++;
			}
			if ((a.getSituacao() != null) && (!a.getSituacao().equals("T")) && (!a.getSituacao().equals(""))) {
				ps.setString(i, a.getSituacao());
				i++;
			}

			set = ps.executeQuery();

			while (set.next()) {

				ClienteBean clienteBean = new ClienteBean();
				PortadorBean portBean = new PortadorBean();
				TituloReceberBean bean = new TituloReceberBean();
				TipoDocumentoBean doc = new TipoDocumentoBean();
				FonteReceitaBean rec = new FonteReceitaBean();

				bean.setIdFinanceiro(set.getInt("idreceber"));
				bean.setValoraberto(set.getDouble("valoraberto"));
				bean.setValortaxaadm(set.getDouble("valortaxaadm"));
				bean.setDataEmissao(set.getDate("data_emissao"));
				// bean.setCompetencia(set.getDate("competencia"));
				if (set.getString("competencia") != null)
					bean.setCompetencia(set.getString("competencia").replace("/", ""));

				clienteBean.setCodcliente(set.getInt("id_cliente"));
				clienteBean.setNome(set.getString("nome"));

				portBean.setCodportador(set.getInt("codportador"));
				portBean.setDescricao(set.getString("descPortador"));

				bean.setValor(set.getDouble("valor"));
				bean.setDesconto(set.getDouble("desconto"));

				doc.setCodtipodocumento(set.getInt("codtipodocumento"));
				doc.setDescricao(set.getString("descDocumento"));

				bean.setVencimento(set.getDate("vencimento"));
				bean.setHistorico(set.getString("historico"));
				bean.setDescricaoFatura(set.getString("desc_fatura"));
				bean.setObjCobranca(set.getString("obs_cobranca"));
				bean.setStatus(set.getString("status"));
				bean.setCliente(clienteBean);
				bean.setPortador(portBean);
				bean.setTipoDocumento(doc);

				bean.setParcela(set.getString("parcela"));
				bean.setDocumento(set.getString("documento"));
				rec.setId(set.getInt("codfonterec"));
				bean.setFonrec(rec);

				lista.add(bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	public ArrayList<ImpostoBean> lstImpostosEdit(Integer duplicata) throws ProjetoException {

		String sql = " SELECT duplicata, imposto, perc_ret, cd_pagar, base_ret "
				+ "  FROM clin.retencao_receber where duplicata = ? ";

		ArrayList<ImpostoBean> lista = new ArrayList<ImpostoBean>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, duplicata);
			set = ps.executeQuery();

			while (set.next()) {

				ImpostoBean impostobean = new ImpostoBean();

				impostobean.setDescImposto(set.getString("imposto"));
				impostobean.setPcRentencao(set.getDouble("perc_ret"));
				impostobean.setValorBase(set.getDouble("base_ret"));
				impostobean.setDuplicata(set.getInt("duplicata"));
				lista.add(impostobean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	public ArrayList<BancoBean> lstTodosBancos() throws ProjetoException {

		String sql = " select * from clin.banco";
		ArrayList<BancoBean> lista = new ArrayList<BancoBean>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			set = ps.executeQuery();

			while (set.next()) {

				BancoBean banco = new BancoBean();

				banco.setId(set.getInt("id"));
				banco.setCodbanco(set.getString("codbanco"));
				banco.setDescricao(set.getString("banco"));
				lista.add(banco);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

	public ArrayList<BaixaReceber> lstBaixas(Integer duplicata) throws ProjetoException {
		String sql = "select * from clin.recdupbx where duplicata = ?";

		ArrayList<BaixaReceber> lista = new ArrayList<>();

		Connection conexao = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conexao = ConnectionFactory.getConnection();
			stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, duplicata);
			rs = stmt.executeQuery();

			while (rs.next()) {
				BaixaReceber bx = new BaixaReceber();
				bx.setId(rs.getInt("duplicata"));
				bx.getBanco().setId(rs.getInt("codbanco"));
				bx.setDuplicata(rs.getInt("duplicata"));
				bx.setTipoBaixaString(rs.getString("tpbaixa"));
				bx.setValorBaixado(rs.getDouble("valorpago"));
				bx.setDesconto(rs.getDouble("vlrdesc"));
				bx.setJuros(rs.getDouble("vlrjuros"));
				bx.setMulta(rs.getDouble("vlrmulta"));
				bx.setValorAtual(rs.getDouble("vlratual"));
				bx.getBanco().setCodbanco(rs.getString("codbanco"));
				bx.getFunc().setCodigo(rs.getInt("opcad"));
				bx.setDtRecebimento(rs.getDate("dtpag"));

				lista.add(bx);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				stmt.close();
				rs.close();
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public void negociacao(List<TituloReceberBean> lstAntecipar, List<TituloReceberBean> lstLiquidacao,
			NegociarBean bean) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");
		Date data = new Date(System.currentTimeMillis());
		String historico = "";

		String sql = " UPDATE clin.recdup SET status='NG' WHERE idreceber = ? ; ";

		Connection con = null;

		try {
			con = ConnectionFactory.getConnection();

			for (int i = 0; i < lstAntecipar.size(); i++) {
				historico = lstAntecipar.get(i).getHistorico();
				PreparedStatement ps = con.prepareStatement(sql);

				ps.setInt(1, lstAntecipar.get(i).getIdFinanceiro());

				ps.execute();

				ps.close();

			}

			String sql2 = " INSERT INTO clin.recdup (data_emissao, id_cliente,  id_portador, valor, desconto, vencimento,status, historico, "
					+ "codfilial, dtcadastro, parcela) " + " VALUES (current_date, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

			for (int i = 0; i < lstLiquidacao.size(); i++) {

				PreparedStatement ps2 = con.prepareStatement(sql2);

				ps2.setInt(1, bean.getCliente().getCodcliente());
				ps2.setInt(2, lstLiquidacao.get(i).getPortador().getCodportador());
				ps2.setDouble(3, lstLiquidacao.get(i).getValor());
				ps2.setDouble(4, lstLiquidacao.get(i).getDesconto());
				ps2.setDate(5, new java.sql.Date(lstLiquidacao.get(i).getVencimento().getTime()));
				ps2.setString(6, "S");
				ps2.setString(7, historico);
				ps2.setDate(8, data);
				ps2.setString(9, "UN");

				ps2.execute();

				ps2.close();

			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public ArrayList<TituloReceberBean> todosFinanceiro(BuscaBeanRecibo busca) throws ProjetoException {

		String sql = "	select  case when situacao='F' then true else false end pago, "
				+ "	  case when vencimento <=current_date then true else false end vencido, "
				+ "	  case when vencimento >current_date then true else false end vencer, "
				+ "	  idreceber,codfilial,id_cliente,competencia,vencimento,juros_dia,multaatraso,valor - coalesce(desconto,0) + coalesce(multaatraso,0) + coalesce(juros_dia,0) valor,"
				+ "     processo,desconto,data_emissao, " + "	  historico,duplicata,codfonrec, situacao,parcela, "
				+ "	  nomecliente, portdesc,codportador, nome,"
				+ "	  codtipdoc,   desctipdoc, case when valoraberto<0 then 0 else valoraberto end valoraberto,totalpago "
				+ "	  from( "
				+ "	  select  r.idreceber,r.codfilial,r.id_cliente,r.competencia,r.vencimento,r.juros_dia,r.multaatraso,r.valor,r.processo,r.desconto,r.data_emissao, "
				+ "	   r.historico,r.duplicata,f.codfonrec, r.situacao,r.parcela, "
				+ "	   c.nome nomecliente,f.descricao as descfonrec,pt.descricao portdesc,pt.codportador, c.nome, "
				+ "	   r.codtipdoc,  td.descricao desctipdoc, "
				+ "	   r.valor - coalesce(r.desconto,0)+coalesce(r.juros_dia,0)+coalesce(r.multaatraso,0)-coalesce(r.vlrretencao,0) "
				+ "	   - coalesce((select sum(coalesce(valorpago,0)-coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0)) from clin.recdupbx "
				+ "	  where recdupbx.duplicata=r.idreceber),0) valoraberto, "
				+ "	  coalesce((select sum(valorpago) from clin.recdupbx where duplicata=r.idreceber),0) totalpago       "
				+ "	  from clin.recdup r join clin.cliente c on (r.id_cliente = c.codcliente)  "
				+ "	  left join clin.fonrec f on (f.codfonrec = r.codfonrec)  "
				+ "	  left join clin.tipodocumento td on td.codtipodocumento = r.codtipdoc "
				+ "	  left join clin.portador pt on(pt.codportador = r.id_portador) "
				+ "	  where r.codfilial=? and vencimento between ? And ? ";

		/*
		 * String sql = " select  case when situacao='F' then true else false end pago,"
		 * + " case when dtvcto <=current_date then true else false end vencido," +
		 * " case when dtvcto >current_date then true else false end vencer," +
		 * " codigo,codfilial,codforn,dtcompete,dtvcto,juros,multa,valor - coalesce(desconto,0) + coalesce(multa,0) + coalesce(juros,0) valor,processo,desconto,dtemissao,"
		 * + "  historico,duplicata,idccusto,coddespesa, situacao,parcela,icmssubt," +
		 * " nomeFornecedor,despesa,descCusto, portdesc,codportador, iddespesa," +
		 * "  codtipdoc,   desctipdoc,nominal, dtprevisao,case when valoraberto<0 then 0 else valoraberto end valoraberto,totalpago"
		 * + "  from(" +
		 * "select  p.codigo,p.codfilial,p.codforn,p.dtcompete,p.dtvcto,p.juros,p.multa,p.valor,p.processo,p.desconto,p.dtemissao,"
		 * +
		 * "  p.historico,p.duplicata,c.idccusto,d.coddespesa, p.situacao,p.parcela,p.dtprevisao,p.icmssubt,"
		 * +
		 * "  f.nome as nomeFornecedor,d.descricao as despesa,c.descricao descCusto,pt.descricao portdesc,pt.codportador, d.iddespesa, "
		 * + "  p.codtipdoc,  td.descricao desctipdoc, p.nominal, " +
		 * "  p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)"
		 * +
		 * " - coalesce((select sum(coalesce(valorpago,0)-coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0)) from clin.pagdupbx"
		 * + " where pagdupbx.codigo=p.codigo),0) valoraberto," +
		 * " coalesce((select sum(valorpago) from clin.pagdupbx where codigo=p.codigo),0) totalpago                  "
		 * + " from clin.pagdup p join clin.fornecedor f on (p.codforn = f.codforn) " +
		 * "  join clin.despesa d on (d.iddespesa = p.iddespesa) " +
		 * "  join clin.ccusto c on (c.idccusto = p.idccusto) " +
		 * " left join clin.tipodocumento td on td.codtipodocumento = p.codtipdoc" +
		 * "	 left join clin.portador pt on(pt.codportador = p.codport) where p.codfilial=? and dtvcto between ? and ?"
		 * ;//
		 */

		if ((busca.getFornecedor().getCodforn() != null) && (busca.getFornecedor().getCodforn() != 0)) {
			sql = sql + " and p.codforn=?  ";
		}
		if ((busca.getIddespesa() != null) && (busca.getIddespesa() != 0)) {

			sql = sql + " and p.iddespesa=?  ";
		}
		if (busca.getSituacao().equals("A")) {
			sql = sql + " and p.situacao='A' ";
		}

		if (busca.getSituacao().equals("F")) {
			sql = sql + " and p.situacao='F' ";
		}
		if ((busca.getDocumento() != null) && (!busca.getDocumento().equals(""))) {
			sql = sql + " and p.duplicata=?  ";
		}
		if ((busca.getCodigo() != null) && (busca.getCodigo() != 0)) {
			sql = sql + " and p.codigo=?  ";
		}

		sql = sql + " )t";

		if (busca.getOrdenacao().equals("C")) {
			sql = sql + " order by idreceber desc; ";
		}

		if (busca.getOrdenacao().equals("V")) {
			sql = sql + " order by dtvcto ";
		}

		if (busca.getOrdenacao().equals("F")) {
			sql = sql + " order by codforn ";
		}

		ArrayList<TituloReceberBean> lista = new ArrayList<TituloReceberBean>();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet set = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			Integer i = 2;

			ps.setDate(1, new java.sql.Date(busca.getPeriodoinicial().getTime()));
			ps.setDate(2, new java.sql.Date(busca.getPeriodofinal().getTime()));

			if ((busca.getFornecedor().getCodforn() != null) && (busca.getFornecedor().getCodforn() != 0)) {
				i = i + 1;
				ps.setInt(i, busca.getFornecedor().getCodforn());
			}

			if ((busca.getIddespesa() != null) && (busca.getIddespesa() != 0)) {
				i = i + 1;
				ps.setInt(i, busca.getIddespesa());
			}

			if ((busca.getDocumento() != null) && (!busca.getDocumento().equals(""))) {
				i = i + 1;
				ps.setString(i, busca.getDocumento());
			}

			if ((busca.getCodigo() != null) && (busca.getCodigo() != 0)) {
				i = i + 1;
				ps.setInt(i, busca.getCodigo());
			}

			set = ps.executeQuery();

			while (set.next()) {

				PortadorBean portBean = new PortadorBean();
				TituloReceberBean bean = new TituloReceberBean();
				CentroCustoBean ccbean = new CentroCustoBean();
				DespesaBean despesa = new DespesaBean();
				FornecedorBean fornecedor = new FornecedorBean();

				// ccbean.setIdccusto(set.getInt("idccusto"));
				// ccbean.setDescricao(set.getString("descCusto"));

				portBean.setCodportador(set.getInt("codportador"));
				portBean.setDescricao(set.getString("portdesc"));

				// despesa.setCodDespesa(set.getString("coddespesa"));
				// despesa.setId(set.getInt("iddespesa"));
				// despesa.setDescricao(set.getString("despesa"));
				bean.setValor(set.getDouble("valor"));
				bean.getCliente().setNome(set.getString("nome"));
				bean.setDesconto(set.getDouble("desconto"));
				bean.setVencido(set.getBoolean("vencido"));
				bean.setVencer(set.getBoolean("vencer"));
				bean.setPago(set.getBoolean("pago"));
				bean.setDuplicata(set.getString("duplicata"));
				// bean.setDtcompete(set.getString("dtcompete"));

				bean.setDtvcto(set.getDate("vencimento"));
				// bean.setDtprevisao(set.getDate("dtprevisao"));
				// bean.setDtemissao(set.getDate("dtemissao"));

				bean.setHistorico(set.getString("historico"));
				bean.setParcela(set.getString("parcela"));
				bean.setCodigo(set.getInt("idreceber"));
				bean.setNotaFiscal(set.getString("processo"));
				// bean.setIcmsst(set.getDouble("icmssubt"));
				// bean.setJuros(set.getDouble("juros"));
				// bean.setMulta(set.getDouble("multa"));
				bean.setSituacao(set.getString("situacao"));
				bean.setValoraberto(set.getDouble("valoraberto"));
				bean.setTotalpago(set.getDouble("totalpago"));
				// fornecedor.setNome(set.getString("nomefornecedor"));
				// fornecedor.setCodforn(set.getInt("codforn"));
				bean.getTipoDocumento().setCodtipodocumento(set.getInt("codtipdoc"));
				bean.getTipoDocumento().setDescricao(set.getString("desctipdoc"));
				/*
				 * for (ImpostoBean impostoBean : listaImposto) {
				 * imposto.setDescImposto(set.getString("imposto"));
				 * imposto.setPcRentencao(set.getDouble("perc_ret"));
				 * imposto.setValorBase(set.getDouble("base_ret"));
				 * listaImposto.add(impostoBean); }
				 * 
				 * bean.setLstImposto(listaImposto);
				 */
				bean.setPortador(portBean);
				bean.setCcusto(ccbean);
				bean.setDespesa(despesa);
				bean.setForn(fornecedor);

				lista.add(bean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				set.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lista;
	}

}
