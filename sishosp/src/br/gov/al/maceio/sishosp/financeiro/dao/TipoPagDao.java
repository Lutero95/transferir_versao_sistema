package br.gov.al.maceio.sishosp.financeiro.dao;

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
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.TipoPagBean;



public class TipoPagDao {

	public boolean inserirTipoPag(TipoPagBean tipoPagBean) throws ProjetoException {

		boolean result = true;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "INSERT INTO clin.tippag(descricao, tipo, vlrmin, descreduz,periodicidade, qtdparc,forma, taxaadm,codfilial, tpdoc, idfonrec, codport, baixaautomaticafin, codcliente, idbanco, status) "
				+ "   VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?,?,?,'A');  ";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setString(1, tipoPagBean.getDescricao().toUpperCase());
			ps.setString(2, tipoPagBean.getTipoDocString());

			if (tipoPagBean.getValorMinimo() == null || tipoPagBean.getValorMinimo() == 0) {
				ps.setNull(3, Types.NULL);
			} else {
				ps.setDouble(3, tipoPagBean.getValorMinimo());
			}

			ps.setString(4, tipoPagBean.getDescReduz());

			if (tipoPagBean.getPeriodicidade() == null) {
				ps.setNull(5, Types.NULL);
			} else {
				ps.setInt(5, tipoPagBean.getPeriodicidade());
			}

			if (tipoPagBean.getQtdParc() == null) {
				ps.setNull(6, Types.NULL);
			} else {
				ps.setInt(6, tipoPagBean.getQtdParc());
			}

			if (tipoPagBean.getForma() == null) {
				ps.setNull(7, Types.NULL);
			} else {
				ps.setString(7, tipoPagBean.getForma());
			}

			if (tipoPagBean.getTaxaAdm() == null) {
				ps.setNull(8, Types.NULL);
			} else {
				ps.setDouble(8, tipoPagBean.getTaxaAdm());
			}


			if (tipoPagBean.getTipoDoc() == null) {
				ps.setNull(9, Types.NULL);
			} else {
				ps.setInt(9, tipoPagBean.getTipoDoc());
			}

			if (tipoPagBean.getCodFonrec() == null) {
				ps.setNull(10, Types.NULL);
			} else {
				ps.setInt(10, tipoPagBean.getCodFonrec());
			}

			if (tipoPagBean.getCodport() == null) {
				ps.setNull(11, Types.NULL);
			} else {
				ps.setInt(11, tipoPagBean.getCodport());
			}

			ps.setBoolean(12, tipoPagBean.isBaixaAutFin());

			if (tipoPagBean.getCliente().getCodcliente() == null || tipoPagBean.getCliente().getCodcliente() == 0) {
				ps.setNull(13, Types.NULL);
			} else {
				ps.setInt(13, tipoPagBean.getCliente().getCodcliente());
			}

			if (tipoPagBean.getBanco().getId() == null || tipoPagBean.getBanco().getId() == 0) {
				ps.setNull(14, Types.NULL);
			} else {
				ps.setInt(14, tipoPagBean.getBanco().getId());
			}

			ps.execute();

			con.commit();

		} catch (SQLException e) {
			result = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	public boolean alterarTipPag(TipoPagBean tipoPagBean) throws ProjetoException {
		boolean result = true;

		String sql = "UPDATE clin.tippag   SET  descricao=?, tipo=?, vlrmin=?,   descreduz=?, periodicidade=?, qtdparc=?,"
				+ " forma=?, taxaadm=?, tpdoc=?,idfonrec=?, codport=?, baixaautomaticafin=?, codcliente=?, idbanco=? WHERE codtippag =? ;";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setString(1, tipoPagBean.getDescricao().toUpperCase());
			ps.setString(2, tipoPagBean.getTipoDocString());

			if (tipoPagBean.getValorMinimo() == null) {
				ps.setNull(3, Types.NULL);
			} else {
				ps.setDouble(3, tipoPagBean.getValorMinimo());
			}

			ps.setString(4, tipoPagBean.getDescReduz());

			if (tipoPagBean.getPeriodicidade() == 0) {
				ps.setNull(5, Types.NULL);
			} else {
				ps.setInt(5, tipoPagBean.getPeriodicidade());
			}

			if (tipoPagBean.getQtdParc() == 0) {
				ps.setNull(6, Types.NULL);
			} else {
				ps.setInt(6, tipoPagBean.getQtdParc());
			}

			ps.setString(7, tipoPagBean.getForma());

			if (tipoPagBean.getTaxaAdm() == 0) {
				ps.setNull(8, Types.NULL);
			} else {
				ps.setDouble(8, tipoPagBean.getTaxaAdm());
			}

			if (tipoPagBean.getTipoDoc() == 0) {
				ps.setNull(9, Types.NULL);
			} else {
				ps.setInt(9, tipoPagBean.getTipoDoc());
			}

			if (tipoPagBean.getCodFonrec() == 0) {
				ps.setNull(10, Types.NULL);
			} else {
				ps.setInt(10, tipoPagBean.getCodFonrec());
			}

			if (tipoPagBean.getCodport() == 0) {
				ps.setNull(11, Types.NULL);
			} else {
				ps.setInt(11, tipoPagBean.getCodport());
			}

			ps.setBoolean(12, tipoPagBean.isBaixaAutFin());

			if (tipoPagBean.getCliente() != null) {
				if (tipoPagBean.getCliente().getCodcliente() != null) {
					if (tipoPagBean.getCliente().getCodcliente() == 0) {
						ps.setNull(13, Types.NULL);
					} else {
						ps.setInt(13, tipoPagBean.getCliente().getCodcliente());
					}
				} else {
					ps.setNull(13, Types.NULL);
				}
			}

			if ((tipoPagBean.getBanco().getId() == null) || (tipoPagBean.getBanco().getId() == 0)) {
				ps.setNull(14, Types.NULL);
			} else {
				ps.setInt(14, tipoPagBean.getBanco().getId());
			}

			ps.setInt(15, tipoPagBean.getCodtippag());

			ps.execute();

			con.commit();

		} catch (SQLException e) {
			result = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "Erro!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			result = false;
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public List<TipoPagBean> lstTipoPagCheque() throws ProjetoException {

		String sql = "select * from clin.tippag where tipo='CH'  and status = 'A' order by descricao";
		List<TipoPagBean> lst = new ArrayList<TipoPagBean>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();

				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));
				tipBean.setDescReduz(rs.getString("descreduz"));
				tipBean.setValorMinimo(rs.getDouble("vlrmin"));
				tipBean.setTipo(rs.getString("tipo"));
				tipBean.setPeriodicidade(rs.getInt("periodicidade"));
				tipBean.setTaxaAdm(rs.getDouble("taxaadm"));
				tipBean.setQtdParc(rs.getInt("qtdparc"));
				tipBean.setForma(rs.getString("forma"));
				tipBean.setTipoDoc(rs.getInt("tpdoc"));

				lst.add(tipBean);
			}

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

		return lst;
	}

	public List<TipoPagBean> lstTipoPagCartao() throws ProjetoException {
		String sql = "select * from clin.tippag where tipo='CT' and status = 'A' order by descricao";
		List<TipoPagBean> lst = new ArrayList<TipoPagBean>();

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();

				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));
				tipBean.setDescReduz(rs.getString("descreduz"));
				tipBean.setValorMinimo(rs.getDouble("vlrmin"));
				tipBean.setTipo(rs.getString("tipo"));
				tipBean.setPeriodicidade(rs.getInt("periodicidade"));
				tipBean.setTaxaAdm(rs.getDouble("taxaadm"));
				tipBean.setQtdParc(rs.getInt("qtdparc"));
				tipBean.setForma(rs.getString("forma"));

				lst.add(tipBean);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;
	}

	public List<TipoPagBean> lstTipoPagDinheiro() throws ProjetoException {

		String sql = "select * from clin.tippag where tipo='D'  and status = 'A' order by descricao";
		List<TipoPagBean> lista = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();
				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));
				tipBean.setTipo(rs.getString("tipo"));
				tipBean.setPeriodicidade(rs.getInt("periodicidade"));
				tipBean.setQtdParc(rs.getInt("qtdparc"));
				tipBean.setForma(rs.getString("forma"));
				tipBean.setTaxaAdm(rs.getDouble("taxaadm"));

				lista.add(tipBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<TipoPagBean> lstTipoPagamentos() throws ProjetoException {

		String sql = "select codtippag, descricao from clin.tippag where   status = 'A' order by descricao";

		List<TipoPagBean> lista = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();
				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));

				lista.add(tipBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<TipoPagBean> lstTipoPagCredito() throws ProjetoException {

		String sql = "select codtippag, descricao,tipo, periodicidade, qtdparc, forma, taxaadm  "
				+ "from clin.tippag where tipo='CR'  and status = 'A' order by descricao";

		List<TipoPagBean> lista = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");

			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();
				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));
				tipBean.setTipo(rs.getString("tipo"));
				tipBean.setPeriodicidade(rs.getInt("periodicidade"));
				tipBean.setQtdParc(rs.getInt("qtdparc"));
				tipBean.setForma(rs.getString("forma"));
				tipBean.setTaxaAdm(rs.getDouble("taxaadm"));

				lista.add(tipBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<TipoPagBean> lstTipoPagPromissoria() throws ProjetoException {

		String sql = "select * from clin.tippag where tipo='NP'  and status = 'A' order by descricao";

		List<TipoPagBean> lista = new ArrayList<>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("obj_usuario");
			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();
				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));
				tipBean.setTipo(rs.getString("tipo"));
				tipBean.setPeriodicidade(rs.getInt("periodicidade"));
				tipBean.setQtdParc(rs.getInt("qtdparc"));
				tipBean.setForma(rs.getString("forma"));
				tipBean.setTaxaAdm(rs.getDouble("taxaadm"));

				lista.add(tipBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	public List<TipoPagBean> lstTipoPag() throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		String sql = "select tippag.*, c.nome, b.descricao descbanco," + " case when tippag.tipo = 'CH' then 'Cheque'"
				+ " when tippag.tipo = 'D' then 'Dinheiro' " + "  when tippag.tipo = 'CR' then 'Crédito'"
				+ " when tippag.tipo = 'CT' then 'Cartão' " + " when tippag.tipo = 'NP' then 'Nota Promissória' "
				+ " end as nomeTipo " + " from clin.tippag "
				+ " left join clin.cliente c on c.codcliente = tippag.codcliente "
				+ " left join clin.banco b on b.id = tippag.idbanco "
				+ " where  status = 'A' order by descricao";

		List<TipoPagBean> lst = new ArrayList<TipoPagBean>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				TipoPagBean tipBean = new TipoPagBean();

				tipBean.setCodtippag(rs.getInt("codtippag"));
				tipBean.setDescricao(rs.getString("descricao"));
				tipBean.setDescReduz(rs.getString("descreduz"));
				tipBean.setValorMinimo(rs.getDouble("vlrmin"));
				tipBean.setTipoDocString(rs.getString("tipo"));
				tipBean.setPeriodicidade(rs.getInt("periodicidade"));
				tipBean.setTaxaAdm(rs.getDouble("taxaadm"));
				tipBean.setQtdParc(rs.getInt("qtdparc"));
				tipBean.setForma(rs.getString("forma"));
				tipBean.setCodFilial(rs.getInt("codfilial"));
				tipBean.setTipoDoc(rs.getInt("tpdoc"));
				tipBean.setCodport(rs.getInt("codport"));
				tipBean.setCodFonrec(rs.getInt("idfonrec"));
				tipBean.setBaixaAutFin(rs.getBoolean("baixaautomaticafin"));
				if (rs.getInt("codcliente") != 0) {
					tipBean.getCliente().setCodcliente(rs.getInt("codcliente"));
					tipBean.getCliente().setNome(rs.getString("nome"));

				}
				tipBean.getBanco().setId(rs.getInt("idbanco"));
				tipBean.getBanco().setDescricao(rs.getString("descbanco"));
				tipBean.setNomeTipo(rs.getString("nomeTipo"));

				lst.add(tipBean);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return lst;

	}

	public String excluir(TipoPagBean bean) throws ProjetoException {
		String retorno = "OK";
		String sql = "delete from  clin.tippag   WHERE codtippag= ? ;";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, bean.getCodtippag());

			ps.execute();

			con.commit();

			return retorno;

		} catch (SQLException e) {
			e.printStackTrace();
			retorno = e.getMessage();
			return retorno;

		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
