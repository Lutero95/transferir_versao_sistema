package br.gov.al.maceio.sishosp.financeiro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.financeiro.model.BaixaPagar;
import br.gov.al.maceio.sishosp.financeiro.model.BancoBean;
import br.gov.al.maceio.sishosp.financeiro.model.BuscaBeanPagar;
import br.gov.al.maceio.sishosp.financeiro.model.CaixaDiarioBean;
import br.gov.al.maceio.sishosp.financeiro.model.CentroCustoBean;
import br.gov.al.maceio.sishosp.financeiro.model.ChequeEmitidoBean;
import br.gov.al.maceio.sishosp.financeiro.model.DespesaBean;
import br.gov.al.maceio.sishosp.financeiro.model.FornecedorBean;
import br.gov.al.maceio.sishosp.financeiro.model.ImpostoBean;
import br.gov.al.maceio.sishosp.financeiro.model.PortadorBean;
import br.gov.al.maceio.sishosp.financeiro.model.TituloPagarBean;
import br.gov.al.maceio.sishosp.financeiro.model.TotalizadorBeanPagar;

public class TituloPagarDao {

	public Boolean replicarPagar2(TituloPagarBean tituloPagarBean, List<ImpostoBean> lstRetencao)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Boolean boo = false;

		String sql = " insert into financeiro.pagdup (dtcompete, codforn, codport, processo, idccusto, codfilial, "
				+ " iddespesa, valor, desconto, juros, multa,  historico, dtvcto, dtemissao, "
				+ " duplicata, dtprevisao, codtipdoc, icmssubt, nominal, parcela, situacao)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			if (tituloPagarBean.getDtcompete() == null) {
				ps.setNull(1, Types.OTHER);
			} else {
				ps.setString(1, tituloPagarBean.getDtcompete());
			}

			if (tituloPagarBean.getForn().getCodforn() == null) {
				ps.setNull(2, Types.OTHER);
			} else {
				ps.setInt(2, tituloPagarBean.getForn().getCodforn());
			}

			if (tituloPagarBean.getPortador().getCodportador() == null) {
				ps.setNull(3, Types.OTHER);
			} else {
				ps.setInt(3, tituloPagarBean.getPortador().getCodportador());
			}
			if (tituloPagarBean.getNotaFiscal() == null) {
				ps.setNull(4, Types.OTHER);
			} else {
				ps.setString(4, tituloPagarBean.getNotaFiscal().toUpperCase());
			}
			if (tituloPagarBean.getCcusto().getIdccusto() == null) {
				ps.setNull(5, Types.OTHER);
			} else {
				ps.setInt(5, tituloPagarBean.getCcusto().getIdccusto());
			}

			if (tituloPagarBean.getDespesa().getId() == null) {
				ps.setNull(6, Types.OTHER);
			} else {
				ps.setInt(6, tituloPagarBean.getDespesa().getId());
			}

			if (Double.valueOf(tituloPagarBean.getValor()) == null) {
				ps.setNull(7, Types.OTHER);
			} else {
				ps.setDouble(7, tituloPagarBean.getValor());
			}

			if (Double.valueOf(tituloPagarBean.getDesconto()) == null) {
				ps.setNull(8, Types.OTHER);
			} else {
				ps.setDouble(8, tituloPagarBean.getDesconto());
			}

			if (Double.valueOf(tituloPagarBean.getJuros()) == null) {
				ps.setNull(9, Types.OTHER);
			} else {
				ps.setDouble(9, tituloPagarBean.getJuros());
			}

			if (Double.valueOf(tituloPagarBean.getMulta()) == null) {
				ps.setNull(10, Types.OTHER);
			} else {
				ps.setDouble(10, tituloPagarBean.getMulta());
			}

			if (tituloPagarBean.getHistorico() == null) {
				ps.setNull(11, Types.OTHER);
			} else {
				ps.setString(11, tituloPagarBean.getHistorico().toUpperCase());
			}

			if (tituloPagarBean.getDtvcto() == null) {
				ps.setNull(12, Types.OTHER);
			} else {
				ps.setDate(12, new java.sql.Date(tituloPagarBean.getDtvcto().getTime()));
			}

			if (tituloPagarBean.getDtemissao() == null) {
				ps.setNull(13, Types.OTHER);
			} else {
				ps.setDate(13, new java.sql.Date(tituloPagarBean.getDtemissao().getTime()));
			}

			if (tituloPagarBean.getDuplicata() == null) {
				ps.setNull(14, Types.OTHER);
			} else {
				ps.setString(14, tituloPagarBean.getDuplicata().toUpperCase());
			}
			if (tituloPagarBean.getDtprevisao() == null) {
				ps.setNull(15, Types.DATE);
			} else {

				ps.setDate(15, new java.sql.Date(tituloPagarBean.getDtprevisao().getTime()));
			}
			if (tituloPagarBean.getTipoDocumento().getCodtipodocumento() == null) {
				ps.setNull(16, Types.OTHER);
			} else {

				ps.setInt(16, tituloPagarBean.getTipoDocumento().getCodtipodocumento());
			}

			if (String.valueOf(tituloPagarBean.getIcmsst()) == null) {
				ps.setNull(17, Types.OTHER);
			} else {
				ps.setDouble(17, tituloPagarBean.getIcmsst());
			}

			if (tituloPagarBean.getNominal() == null) {
				ps.setNull(18, Types.OTHER);
			} else {
				ps.setString(18, tituloPagarBean.getNominal().toUpperCase());
			}

			if (tituloPagarBean.getParcela() == null) {
				ps.setNull(19, Types.OTHER);
			} else {
				ps.setString(2019, tituloPagarBean.getParcela());
			}

			ps.setString(20, "A");

			ps.execute();

			ps.close();

			sql = "delete from financeiro.pagdup where codigo = ? ;";

			ps = con.prepareStatement(sql);

			ps.setInt(1, tituloPagarBean.getCodigo());

			ps.execute();

			con.commit();
			boo = true;
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

		return boo;

	}

	public boolean replicarPagar(TituloPagarBean tituloPagarBean, List<ImpostoBean> lstRetencao)
			throws ProjetoException {
		boolean rst = false;
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		CallableStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareCall("{ ? = call financeiro.pagdup(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			if (tituloPagarBean.getDtcompete() == null) {
				ps.setNull(2, Types.OTHER);
			} else
				ps.setString(2, tituloPagarBean.getDtcompete());

			if (tituloPagarBean.getForn().getCodforn() == null) {
				ps.setNull(3, Types.OTHER);
			} else
				ps.setInt(3, tituloPagarBean.getForn().getCodforn());

			if (tituloPagarBean.getPortador().getCodportador() == null) {
				ps.setNull(4, Types.OTHER);
			} else
				ps.setInt(4, tituloPagarBean.getPortador().getCodportador());

			if (tituloPagarBean.getNotaFiscal() == null) {
				ps.setNull(5, Types.OTHER);
			} else
				ps.setString(5, tituloPagarBean.getNotaFiscal().toUpperCase());

			if (tituloPagarBean.getCcusto().getIdccusto() == null) {
				ps.setNull(6, Types.OTHER);
			} else
				ps.setInt(6, tituloPagarBean.getCcusto().getIdccusto());

			if (tituloPagarBean.getDespesa().getId() == null) {
				ps.setNull(7, Types.OTHER);
			} else
				ps.setInt(7, tituloPagarBean.getDespesa().getId());

			if (Double.valueOf(tituloPagarBean.getValor()) == null) {
				ps.setNull(8, Types.OTHER);
			} else
				ps.setDouble(8, tituloPagarBean.getValor());

			if (Double.valueOf(tituloPagarBean.getDesconto()) == null) {
				ps.setNull(9, Types.OTHER);
			} else
				ps.setDouble(9, tituloPagarBean.getDesconto());

			if (Double.valueOf(tituloPagarBean.getJuros()) == null) {
				ps.setNull(10, Types.OTHER);
			} else
				ps.setDouble(10, tituloPagarBean.getJuros());

			if (Double.valueOf(tituloPagarBean.getMulta()) == null) {
				ps.setNull(11, Types.OTHER);
			} else
				ps.setDouble(11, tituloPagarBean.getMulta());

			if (tituloPagarBean.getHistorico() == null) {
				ps.setNull(12, Types.OTHER);
			} else
				ps.setString(12, tituloPagarBean.getHistorico().toUpperCase());

			if (tituloPagarBean.getDtvcto() == null) {
				ps.setNull(13, Types.OTHER);
			} else
				ps.setDate(13, new java.sql.Date(tituloPagarBean.getDtvcto().getTime()));

			
			if (tituloPagarBean.getDtemissao() == null) {
				ps.setNull(14, Types.OTHER);
			} else
				ps.setDate(14, new java.sql.Date(tituloPagarBean.getDtemissao().getTime()));

			if (tituloPagarBean.getParcela() == null) {
				ps.setNull(15, Types.OTHER);
			} else
				ps.setString(15, tituloPagarBean.getParcela().toUpperCase());

			if (tituloPagarBean.getDuplicata() == null) {
				ps.setNull(16, Types.OTHER);
			} else
				ps.setString(16, tituloPagarBean.getDuplicata().toUpperCase());

			if (tituloPagarBean.getDtprevisao() == null) {
				ps.setNull(17, Types.DATE);
			} else
				ps.setDate(17, new java.sql.Date(tituloPagarBean.getDtprevisao().getTime()));

			if (tituloPagarBean.getTipoDocumento().getCodtipodocumento() == null) {
				ps.setNull(18, Types.OTHER);
			} else
				ps.setInt(18, tituloPagarBean.getTipoDocumento().getCodtipodocumento());

			if (tituloPagarBean.getNominal() == null) {
				ps.setNull(19, Types.OTHER);
			} else
				ps.setString(19, tituloPagarBean.getNominal().toUpperCase());
			if (String.valueOf(tituloPagarBean.getIcmsst()) == null) {
				ps.setNull(20, Types.OTHER);
			} else
				ps.setDouble(20, tituloPagarBean.getIcmsst());

			ps.execute();

			rst = true;
			con.commit();

		} catch (SQLException e) {
			rst = false;
			e.printStackTrace();

		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rst;
	}
	
	public boolean salvarPagarAvulso(TituloPagarBean tituloPagarBean, List<ImpostoBean> lstRetencao, BancoBean banco, CaixaDiarioBean caixa)
			throws ProjetoException {
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		CallableStatement ps = null;
		PreparedStatement pst = null;
		TesourariaDAO tDAo = new TesourariaDAO();

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareCall("{ ? = call financeiro.pagdup(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			ps.registerOutParameter(1, Types.INTEGER);
			if (tituloPagarBean.getDtcompete() == null) {
				ps.setNull(2, Types.OTHER);
			} else
				ps.setString(2, tituloPagarBean.getDtcompete());

			if (tituloPagarBean.getForn() == null) {
				if (tituloPagarBean.getForn().getCodforn() == null) {
					ps.setNull(3, Types.OTHER);
				}
			} else
				ps.setInt(3, tituloPagarBean.getForn().getCodforn());

			if (tituloPagarBean.getPortador().getCodportador() == null) {
				ps.setNull(4, Types.OTHER);
			} else
				ps.setInt(4, tituloPagarBean.getPortador().getCodportador());

			if (tituloPagarBean.getNotaFiscal() == null) {
				ps.setNull(5, Types.OTHER);
			} else
				ps.setString(5, tituloPagarBean.getNotaFiscal().toUpperCase());

			if (tituloPagarBean.getCcusto().getIdccusto() == null) {
				ps.setNull(6, Types.OTHER);
			} else
				ps.setInt(6, tituloPagarBean.getCcusto().getIdccusto());

			if (tituloPagarBean.getDespesa().getId() == null) {
				ps.setNull(7, Types.OTHER);
			} else
				ps.setInt(7, tituloPagarBean.getDespesa().getId());

			if (Double.valueOf(tituloPagarBean.getValor()) == null) {
				ps.setNull(8, Types.OTHER);
			} else
				ps.setDouble(8, tituloPagarBean.getValor());

			if (Double.valueOf(tituloPagarBean.getDesconto()) == null) {
				ps.setNull(9, Types.OTHER);
			} else
				ps.setDouble(9, tituloPagarBean.getDesconto());

			if (Double.valueOf(tituloPagarBean.getJuros()) == null) {
				ps.setNull(10, Types.OTHER);
			} else
				ps.setDouble(10, tituloPagarBean.getJuros());

			if (Double.valueOf(tituloPagarBean.getMulta()) == null) {
				ps.setNull(11, Types.OTHER);
			} else
				ps.setDouble(11, tituloPagarBean.getMulta());

			if (tituloPagarBean.getHistorico() == null) {
				ps.setNull(12, Types.OTHER);
			} else
				ps.setString(12, tituloPagarBean.getHistorico().toUpperCase());

			if (tituloPagarBean.getDtvcto() == null) {
				ps.setNull(13, Types.OTHER);
			} else
				ps.setDate(13, new java.sql.Date(tituloPagarBean.getDtvcto().getTime()));

		

			if (tituloPagarBean.getDtemissao() == null) {
				ps.setNull(14, Types.OTHER);
			} else
				ps.setDate(14, new java.sql.Date(tituloPagarBean.getDtemissao().getTime()));

			if (tituloPagarBean.getParcela() == null) {
				ps.setNull(15, Types.OTHER);
			} else
				ps.setString(15, tituloPagarBean.getParcela().toUpperCase());

			if (tituloPagarBean.getDuplicata() == null) {
				ps.setNull(16, Types.OTHER);
			} else
				ps.setString(16, tituloPagarBean.getDuplicata().toUpperCase());

			if (tituloPagarBean.getDtprevisao() == null) {
				ps.setNull(17, Types.DATE);
			} else
				ps.setDate(17, new java.sql.Date(tituloPagarBean.getDtprevisao().getTime()));

			if (tituloPagarBean.getTipoDocumento().getCodtipodocumento() == null) {
				ps.setNull(18, Types.OTHER);
			} else
				ps.setInt(18, tituloPagarBean.getTipoDocumento().getCodtipodocumento());

			if (tituloPagarBean.getNominal() == null) {
				ps.setNull(19, Types.OTHER);
			} else
				ps.setString(19, tituloPagarBean.getNominal().toUpperCase());
			if (String.valueOf(tituloPagarBean.getIcmsst()) == null) {
				ps.setNull(20, Types.OTHER);
			} else
				ps.setDouble(20, tituloPagarBean.getIcmsst());
				ps.setNull(21, Types.OTHER);
			

			ps.execute();

			String sql = "update financeiro.pagdup SET situacao = 'F' where codigo = ? ";
			pst = con.prepareStatement(sql);
			pst.setInt(1, ps.getInt(1));
			pst.executeUpdate();
			tituloPagarBean.setCodigo(ps.getInt(1));
			int retornoid = 0;

			retornoid = ps.getInt(1);
			for (int i = 0; i < lstRetencao.size(); i++) {

				lstRetencao.get(i).setDuplicata(retornoid);
				salvarRetencao(lstRetencao.get(i));
			}
			ChequeEmitidoBean cheque = new ChequeEmitidoBean();
			  cheque.setBanco(banco);
		        cheque.setCaixa(caixa);
		        cheque.setTipo("CC");
		        cheque.setStatus("OK");
		        cheque.setValor(tituloPagarBean.getValor());
		        
		        	
		        	java.util.Date data = new java.util.Date();
		        	cheque.setDtvencimento(data);
		        	cheque.setDtemissao(data);
		        	cheque.setCompensado("S");
		        	cheque.setDtcompensado(caixa.getDataCaixaAbertura());
		        	
		        	
		        	//cheque.setNumcheque(String.valueOf(sequenciaCheque));
		       
			tDAo.compensaTituloAvulso(cheque, tituloPagarBean, "CC", con);
			con.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				ps.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	public boolean salvarPagar(TituloPagarBean tituloPagarBean, List<ImpostoBean> lstRetencao, Integer id_desistencia)
			throws ProjetoException {
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		CallableStatement ps = null;
		PreparedStatement pst = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareCall("{ ? = call financeiro.pagdup(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			ps.registerOutParameter(1, Types.INTEGER);
			if (tituloPagarBean.getDtcompete() == null) {
				ps.setNull(2, Types.OTHER);
			} else
				ps.setString(2, tituloPagarBean.getDtcompete());

			if (tituloPagarBean.getForn() == null) {
				if (tituloPagarBean.getForn().getCodforn() == null) {
					ps.setNull(3, Types.OTHER);
				}
			} else
				ps.setInt(3, tituloPagarBean.getForn().getCodforn());

			if (tituloPagarBean.getPortador().getCodportador() == null) {
				ps.setNull(4, Types.OTHER);
			} else
				ps.setInt(4, tituloPagarBean.getPortador().getCodportador());

			if (tituloPagarBean.getNotaFiscal() == null) {
				ps.setNull(5, Types.OTHER);
			} else
				ps.setString(5, tituloPagarBean.getNotaFiscal().toUpperCase());

			if (tituloPagarBean.getCcusto().getIdccusto() == null) {
				ps.setNull(6, Types.OTHER);
			} else
				ps.setInt(6, tituloPagarBean.getCcusto().getIdccusto());

			if (tituloPagarBean.getDespesa().getId() == null) {
				ps.setNull(7, Types.OTHER);
			} else
				ps.setInt(7, tituloPagarBean.getDespesa().getId());

			if (Double.valueOf(tituloPagarBean.getValor()) == null) {
				ps.setNull(8, Types.OTHER);
			} else
				ps.setDouble(8, tituloPagarBean.getValor());

			if (Double.valueOf(tituloPagarBean.getDesconto()) == null) {
				ps.setNull(9, Types.OTHER);
			} else
				ps.setDouble(9, tituloPagarBean.getDesconto());

			if (Double.valueOf(tituloPagarBean.getJuros()) == null) {
				ps.setNull(10, Types.OTHER);
			} else
				ps.setDouble(10, tituloPagarBean.getJuros());

			if (Double.valueOf(tituloPagarBean.getMulta()) == null) {
				ps.setNull(11, Types.OTHER);
			} else
				ps.setDouble(11, tituloPagarBean.getMulta());

			if (tituloPagarBean.getHistorico() == null) {
				ps.setNull(12, Types.OTHER);
			} else
				ps.setString(12, tituloPagarBean.getHistorico().toUpperCase());

			if (tituloPagarBean.getDtvcto() == null) {
				ps.setNull(13, Types.OTHER);
			} else
				ps.setDate(13, new java.sql.Date(tituloPagarBean.getDtvcto().getTime()));

		

			if (tituloPagarBean.getDtemissao() == null) {
				ps.setNull(14, Types.OTHER);
			} else
				ps.setDate(14, new java.sql.Date(tituloPagarBean.getDtemissao().getTime()));

			if (tituloPagarBean.getParcela() == null) {
				ps.setNull(15, Types.OTHER);
			} else
				ps.setString(15, tituloPagarBean.getParcela().toUpperCase());

			if (tituloPagarBean.getDuplicata() == null) {
				ps.setNull(16, Types.OTHER);
			} else
				ps.setString(16, tituloPagarBean.getDuplicata().toUpperCase());

			if (tituloPagarBean.getDtprevisao() == null) {
				ps.setNull(17, Types.DATE);
			} else
				ps.setDate(17, new java.sql.Date(tituloPagarBean.getDtprevisao().getTime()));

			if (tituloPagarBean.getTipoDocumento().getCodtipodocumento() == null) {
				ps.setNull(18, Types.OTHER);
			} else
				ps.setInt(18, tituloPagarBean.getTipoDocumento().getCodtipodocumento());

			if (tituloPagarBean.getNominal() == null) {
				ps.setNull(19, Types.OTHER);
			} else
				ps.setString(19, tituloPagarBean.getNominal().toUpperCase());
			if (String.valueOf(tituloPagarBean.getIcmsst()) == null) {
				ps.setNull(20, Types.OTHER);
			} else
				ps.setDouble(20, tituloPagarBean.getIcmsst());
			if ((id_desistencia != null) && (id_desistencia != 0)) {
				ps.setInt(21, id_desistencia);
			} else {
				ps.setNull(21, Types.OTHER);
			}

			ps.execute();

			String sql = "update financeiro.pagdup SET situacao = 'A' where codigo = ? ";
			pst = con.prepareStatement(sql);
			pst.setInt(1, ps.getInt(1));
			pst.executeUpdate();

			int retornoid = 0;

			retornoid = ps.getInt(1);
			for (int i = 0; i < lstRetencao.size(); i++) {

				lstRetencao.get(i).setDuplicata(retornoid);
				salvarRetencao(lstRetencao.get(i));
			}

			con.commit();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				ps.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}

	public ArrayList<TituloPagarBean> todosFinanceiro(BuscaBeanPagar busca) throws ProjetoException {

		String sql = " select  case when situacao='F' then true else false end pago,"
				+ " case when dtvcto <=current_date then true else false end vencido,"
				+ " case when dtvcto >current_date then true else false end vencer,"
				+ " codigo,codforn,dtcompete,dtvcto,juros,multa,valor - coalesce(desconto,0) + coalesce(multa,0) + coalesce(juros,0) valor,processo,desconto,dtemissao,"
				+ "  historico,duplicata,idccusto, situacao,parcela,icmssubt,"
				+ " nomeFornecedor,despesa,descCusto, portdesc,codportador, iddespesa,"
				+ "  codtipdoc,   desctipdoc,nominal, dtprevisao,case when valoraberto<0 then 0 else valoraberto end valoraberto,totalpago"
				+ "  from("
				+ "select  p.codigo,p.codforn,p.dtcompete,p.dtvcto,p.juros,p.multa,p.valor,p.processo,p.desconto,p.dtemissao,"
				+ "  p.historico,p.duplicata,c.idccusto, p.situacao,p.parcela,p.dtprevisao,p.icmssubt,"
				+ "  f.nome as nomeFornecedor,d.descricao as despesa,c.descricao descCusto,pt.descricao portdesc,pt.codportador, d.iddespesa, "
				+ "  p.codtipdoc,  td.descricao desctipdoc, p.nominal, "
				+ "  p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)"
				+ " - coalesce((select sum(coalesce(valorpago,0)-(coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0))) from financeiro.pagdupbx"
				+ " where pagdupbx.codigo=p.codigo),0) valoraberto,"
				+ " coalesce((select sum(valorpago) from financeiro.pagdupbx where codigo=p.codigo),0) totalpago                  "
				+ " from financeiro.pagdup p join financeiro.fornecedor f on (p.codforn = f.codforn) "
				+ "  join financeiro.despesa d on (d.iddespesa = p.iddespesa) "
				+ "  join financeiro.ccusto c on (c.idccusto = p.idccusto) "
				+ " left join financeiro.tipodocumento td on td.codtipodocumento = p.codtipdoc"
				+ "	 left join financeiro.portador pt on(pt.codportador = p.codport) where   dtvcto between ? and ?";//

		if (busca.getFornecedor() != null) {
			if ((busca.getFornecedor().getCodforn() != null) && (busca.getFornecedor().getCodforn() != 0)) {
				sql = sql + " and p.codforn=?  ";
			}
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
			sql = sql + " order by codigo desc; ";
		}

		if (busca.getOrdenacao().equals("V")) {
			sql = sql + " order by dtvcto ";
		}

		if (busca.getOrdenacao().equals("F")) {
			sql = sql + " order by codforn ";
		}

		ArrayList<TituloPagarBean> lista = new ArrayList<TituloPagarBean>();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			Integer i = 2;

			ps.setDate(1, new java.sql.Date(busca.getPeriodoinicial().getTime()));

			ps.setDate(2, new java.sql.Date(busca.getPeriodofinal().getTime()));

			if (busca.getFornecedor() != null) {
				if ((busca.getFornecedor().getCodforn() != null) && (busca.getFornecedor().getCodforn() != 0)) {

					i = i + 1;
					ps.setInt(i, busca.getFornecedor().getCodforn());

				}
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
				TituloPagarBean bean = new TituloPagarBean();
				CentroCustoBean ccbean = new CentroCustoBean();
				DespesaBean despesa = new DespesaBean();
				FornecedorBean fornecedor = new FornecedorBean();

				ccbean.setIdccusto(set.getInt("idccusto"));

				ccbean.setDescricao(set.getString("descCusto"));

				portBean.setCodportador(set.getInt("codportador"));

				portBean.setDescricao(set.getString("portdesc"));

				despesa.setId(set.getInt("iddespesa"));
				despesa.setDescricao(set.getString("despesa"));
				bean.setValor(set.getDouble("valor"));

				bean.setDesconto(set.getDouble("desconto"));
				bean.setVencido(set.getBoolean("vencido"));
				bean.setVencer(set.getBoolean("vencer"));

				bean.setPago(set.getBoolean("pago"));

				bean.setDuplicata(set.getString("duplicata"));
				bean.setDtcompete(set.getString("dtcompete"));

				bean.setDtvcto(set.getDate("dtvcto"));
				bean.setDtprevisao(set.getDate("dtprevisao"));
				bean.setDtemissao(set.getDate("dtemissao"));

				bean.setHistorico(set.getString("historico"));
				bean.setParcela(set.getString("parcela"));

				bean.setCodigo(set.getInt("codigo"));

				bean.setNotaFiscal(set.getString("processo"));
				bean.setIcmsst(set.getDouble("icmssubt"));
				bean.setJuros(set.getDouble("juros"));
				bean.setMulta(set.getDouble("multa"));
				bean.setSituacao(set.getString("situacao"));
				bean.setValoraberto(set.getDouble("valoraberto"));
				bean.setTotalpago(set.getDouble("totalpago"));
				fornecedor.setNome(set.getString("nomefornecedor"));
				fornecedor.setCodforn(set.getInt("codforn"));
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
	
	
	public TituloPagarBean buscaTituloPorId(Integer id) throws ProjetoException {

		String sql = " select  case when situacao='F' then true else false end pago,"
				+ " case when dtvcto <=current_date then true else false end vencido,"
				+ " case when dtvcto >current_date then true else false end vencer,"
				+ " codigo,codforn,dtcompete,dtvcto,juros,multa,valor - coalesce(desconto,0) + coalesce(multa,0) + coalesce(juros,0) valor,processo,desconto,dtemissao,"
				+ "  historico,duplicata,idccusto, situacao,parcela,icmssubt,"
				+ " nomeFornecedor,despesa,descCusto, portdesc,codportador, iddespesa,"
				+ "  codtipdoc,   desctipdoc,nominal, dtprevisao,case when valoraberto<0 then 0 else valoraberto end valoraberto,totalpago"
				+ "  from("
				+ "select  p.codigo,p.codforn,p.dtcompete,p.dtvcto,p.juros,p.multa,p.valor,p.processo,p.desconto,p.dtemissao,"
				+ "  p.historico,p.duplicata,c.idccusto, p.situacao,p.parcela,p.dtprevisao,p.icmssubt,"
				+ "  f.nome as nomeFornecedor,d.descricao as despesa,c.descricao descCusto,pt.descricao portdesc,pt.codportador, d.iddespesa, "
				+ "  p.codtipdoc,  td.descricao desctipdoc, p.nominal, "
				+ "  p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)"
				+ " - coalesce((select sum(coalesce(valorpago,0)-coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0)) from financeiro.pagdupbx"
				+ " where pagdupbx.codigo=p.codigo),0) valoraberto,"
				+ " coalesce((select sum(valorpago) from financeiro.pagdupbx where codigo=p.codigo),0) totalpago                  "
				+ " from financeiro.pagdup p join financeiro.fornecedor f on (p.codforn = f.codforn) "
				+ "  join financeiro.despesa d on (d.iddespesa = p.iddespesa) "
				+ "  join financeiro.ccusto c on (c.idccusto = p.idccusto) "
				+ " left join financeiro.tipodocumento td on td.codtipodocumento = p.codtipdoc"
				+ "	 left join financeiro.portador pt on(pt.codportador = p.codport) where   p.codigo=?";//

	

		sql = sql + " )t";
		

		ArrayList<TituloPagarBean> lista = new ArrayList<TituloPagarBean>();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;
		PortadorBean portBean = new PortadorBean();
		TituloPagarBean bean = new TituloPagarBean();
		CentroCustoBean ccbean = new CentroCustoBean();
		DespesaBean despesa = new DespesaBean();
		FornecedorBean fornecedor = new FornecedorBean();
		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setInt(1, id);

		
			set = ps.executeQuery();

			while (set.next()) {



				ccbean.setIdccusto(set.getInt("idccusto"));

				ccbean.setDescricao(set.getString("descCusto"));

				portBean.setCodportador(set.getInt("codportador"));

				portBean.setDescricao(set.getString("portdesc"));

				despesa.setId(set.getInt("iddespesa"));
				despesa.setDescricao(set.getString("despesa"));
				bean.setValor(set.getDouble("valor"));

				bean.setDesconto(set.getDouble("desconto"));
				bean.setVencido(set.getBoolean("vencido"));
				bean.setVencer(set.getBoolean("vencer"));

				bean.setPago(set.getBoolean("pago"));

				bean.setDuplicata(set.getString("duplicata"));
				bean.setDtcompete(set.getString("dtcompete"));

				bean.setDtvcto(set.getDate("dtvcto"));
				bean.setDtprevisao(set.getDate("dtprevisao"));
				bean.setDtemissao(set.getDate("dtemissao"));

				bean.setHistorico(set.getString("historico"));
				bean.setParcela(set.getString("parcela"));

				bean.setCodigo(set.getInt("codigo"));

				bean.setNotaFiscal(set.getString("processo"));
				bean.setIcmsst(set.getDouble("icmssubt"));
				bean.setJuros(set.getDouble("juros"));
				bean.setMulta(set.getDouble("multa"));
				bean.setSituacao(set.getString("situacao"));
				bean.setValoraberto(set.getDouble("valoraberto"));
				bean.setTotalpago(set.getDouble("totalpago"));
				fornecedor.setNome(set.getString("nomefornecedor"));
				fornecedor.setCodforn(set.getInt("codforn"));
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

		return bean;
	}


	public ArrayList<TituloPagarBean> titulosEstornar(BuscaBeanPagar busca) throws ProjetoException {

		String sql = " select  distinct case when situacao='F' then true else false end pago,"
				+ " case when dtvcto <=current_date then true else false end vencido,"
				+ " case when dtvcto >current_date then true else false end vencer,"
				+ " codigo,codforn,dtcompete,dtvcto,juros,multa,valor - coalesce(desconto,0) + coalesce(multa,0) + coalesce(juros,0) valor,processo,desconto,dtemissao,"
				+ "  historico,duplicata,idccusto, situacao,parcela,icmssubt,"
				+ " nomeFornecedor,despesa,descCusto, portdesc,codportador, iddespesa,"
				+ "  codtipdoc,   desctipdoc,nominal, dtprevisao,case when valoraberto<0 then 0 else valoraberto end valoraberto,totalpago"
				+ "  from("
				+ "select  p.codigo,p.codforn,p.dtcompete,p.dtvcto,p.juros,p.multa,p.valor,p.processo,p.desconto,p.dtemissao,"
				+ "  p.historico,p.duplicata,c.idccusto,d. p.situacao,p.parcela,p.dtprevisao,p.icmssubt,"
				+ "  f.nome as nomeFornecedor,d.descricao as despesa,c.descricao descCusto,pt.descricao portdesc,pt.codportador, d.iddespesa, "
				+ "  p.codtipdoc,  td.descricao desctipdoc, p.nominal, "
				+ "  p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)"
				+ " - coalesce((select sum(coalesce(valorpago,0)-coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0)) from financeiro.pagdupbx"
				+ " where pagdupbx.codigo=p.codigo),0) valoraberto,"
				+ " coalesce((select sum(valorpago) from financeiro.pagdupbx where codigo=p.codigo),0) totalpago                  "
				+ " from financeiro.pagdup p join financeiro.fornecedor f on (p.codforn = f.codforn) "
				+ " join financeiro.pagdupbx bx on bx.codigo = p.codigo "
				+ "  join financeiro.despesa d on (d.iddespesa = p.iddespesa) "
				+ "  join financeiro.ccusto c on (c.idccusto = p.idccusto) "
				+ " left join financeiro.tipodocumento td on td.codtipodocumento = p.codtipdoc"
				+ "	 left join financeiro.portador pt on(pt.codportador = p.codport) where  dtpagto between ? and ?";//

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
			sql = sql + " order by codigo desc; ";
		}

		if (busca.getOrdenacao().equals("V")) {
			sql = sql + " order by dtvcto ";
		}

		if (busca.getOrdenacao().equals("F")) {
			sql = sql + " order by codforn ";
		}

		ArrayList<TituloPagarBean> lista = new ArrayList<TituloPagarBean>();
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

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
				TituloPagarBean bean = new TituloPagarBean();
				CentroCustoBean ccbean = new CentroCustoBean();
				DespesaBean despesa = new DespesaBean();
				FornecedorBean fornecedor = new FornecedorBean();
				ccbean.setIdccusto(set.getInt("idccusto"));

				ccbean.setDescricao(set.getString("descCusto"));

				portBean.setCodportador(set.getInt("codportador"));

				portBean.setDescricao(set.getString("portdesc"));

				despesa.setId(set.getInt("iddespesa"));
				despesa.setDescricao(set.getString("despesa"));
				bean.setValor(set.getDouble("valor"));

				bean.setDesconto(set.getDouble("desconto"));
				bean.setVencido(set.getBoolean("vencido"));
				bean.setVencer(set.getBoolean("vencer"));

				bean.setPago(set.getBoolean("pago"));

				bean.setDuplicata(set.getString("duplicata"));
				bean.setDtcompete(set.getString("dtcompete"));

				bean.setDtvcto(set.getDate("dtvcto"));
				bean.setDtprevisao(set.getDate("dtprevisao"));
				bean.setDtemissao(set.getDate("dtemissao"));

				bean.setHistorico(set.getString("historico"));
				bean.setParcela(set.getString("parcela"));

				bean.setCodigo(set.getInt("codigo"));

				bean.setNotaFiscal(set.getString("processo"));
				bean.setIcmsst(set.getDouble("icmssubt"));
				bean.setJuros(set.getDouble("juros"));
				bean.setMulta(set.getDouble("multa"));
				bean.setSituacao(set.getString("situacao"));
				bean.setValoraberto(set.getDouble("valoraberto"));
				bean.setTotalpago(set.getDouble("totalpago"));
				fornecedor.setNome(set.getString("nomefornecedor"));
				fornecedor.setCodforn(set.getInt("codforn"));
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

	public TotalizadorBeanPagar totalizaTitulosBusca(BuscaBeanPagar busca) throws ProjetoException {
		TotalizadorBeanPagar tot = new TotalizadorBeanPagar();

		String sql = " select sum(totalgeral) totalgeral, sum(valoraberto) valoraberto,"
				+ " coalesce( sum(totalpago),0) totalpago, coalesce(sum(valorabertovencer),0) valorabertovencer,"
				+ " sum(valorabertovencido) valorabertovencido from ("
				+ " select w.*, case when vencido is true then coalesce(valoraberto,0) end valorabertovencido,"
				+ " case when vencido is false then coalesce(valoraberto,0) end valorabertovencer from ("
				+ " select totalgeral, valoraberto, totalpago, dtvcto,"
				+ " case when current_date > dtvcto then true else false end vencido from (" + " select" + " p.dtvcto,"
				+ " p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0) totalgeral,"
				+ " p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)"
				+ " - coalesce((select sum(coalesce(valorpago,0)+coalesce(vlrdesc,0)-coalesce(vlrjuros,0)-coalesce(vlrmulta,0)) from financeiro.pagdupbx"
				+ " where pagdupbx.codigo=p.codigo),0) valoraberto,"
				+ " coalesce((select sum(valorpago) from financeiro.pagdupbx where codigo=p.codigo),0) totalpago"
				+ " from financeiro.pagdup p join financeiro.fornecedor f on (p.codforn = f.codforn)"
				+ " join financeiro.despesa d on (d.iddespesa = p.iddespesa)"
				+ " join financeiro.ccusto c on (c.idccusto = p.idccusto)"
				+ " left join financeiro.tipodocumento td on td.codtipodocumento = p.codtipdoc"
				+ " left join financeiro.portador pt on(pt.codportador = p.codport) where   dtvcto between ? and ?";//
		if (busca.getFornecedor() != null) {
			if ((busca.getFornecedor().getCodforn() != null) && (busca.getFornecedor().getCodforn() != 0)) {
				sql = sql + " and p.codforn=?  ";
			}
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
		sql = sql + "         ) t" + " )w" + " )k";

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			Integer i = 2;

			ps.setDate(1, new java.sql.Date(busca.getPeriodoinicial().getTime()));
			ps.setDate(2, new java.sql.Date(busca.getPeriodofinal().getTime()));

			if (busca.getFornecedor() != null) {
				if ((busca.getFornecedor().getCodforn() != null) && (busca.getFornecedor().getCodforn() != 0)) {
					i = i + 1;
					ps.setInt(i, busca.getFornecedor().getCodforn());
				}
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
				tot.setTotalgeral(set.getDouble("totalgeral"));
				tot.setTotalvaloraberto(set.getDouble("valoraberto"));
				tot.setTotalpago(set.getDouble("totalpago"));
				tot.setTotalabertovencer(set.getDouble("valorabertovencer"));
				tot.setTotalabertovencido(set.getDouble("valorabertovencido"));
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

		return tot;
	}

	public boolean excluir(TituloPagarBean t) throws ProjetoException {
		String sql = "delete from financeiro.pagdup where codigo = ? ;";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);

			ps.setInt(1, t.getCodigo());

			ps.execute();

			con.commit();
			return true;
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
		return false;
	}

	public boolean editar(TituloPagarBean t, List<ImpostoBean> lstImpostos) throws ProjetoException {

		String sql = " UPDATE financeiro.pagdup  SET  duplicata=?, processo=?,  codforn=?, valor=?, desconto=?,"
				+ " codport=? , historico=? , parcela=?, vlr_retencao=?, juros=?,"
				+ " multa=?, iddespesa=? ,  idccusto=?, dtvcto=?, dtcompete=?, dtprevisao=?, nominal=?,dtemissao=?,icmssubt=?  WHERE codigo=?; ";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setString(1, t.getDuplicata());
			ps.setString(2, t.getNotaFiscal().toUpperCase());
			ps.setInt(3, t.getForn().getCodforn());
			ps.setDouble(4, t.getValor());
			ps.setDouble(5, t.getDesconto());
			ps.setInt(6, t.getPortador().getCodportador());
			ps.setString(7, t.getHistorico().toUpperCase());
			ps.setString(8, t.getParcela());
			ps.setDouble(9, t.getVlr_retencao());
			ps.setDouble(10, t.getJuros());
			ps.setDouble(11, t.getMulta());
			ps.setInt(12, t.getDespesa().getId());
			ps.setInt(13, t.getCcusto().getIdccusto());
			ps.setDate(14, new Date(t.getDtvcto().getTime()));
			ps.setString(15, t.getDtcompete());
			if (t.getDtprevisao() == null) {
				ps.setNull(16, Types.OTHER);
			} else {
				ps.setDate(16, new Date(t.getDtprevisao().getTime()));
			}
			ps.setString(17, t.getNominal());
			if (t.getDtemissao() == null) {
				ps.setNull(18, Types.OTHER);
			} else {
				ps.setDate(18, new Date(t.getDtemissao().getTime()));
			}
			if (String.valueOf(t.getIcmsst()) == null) {
				ps.setNull(19, Types.OTHER);
			} else {
				ps.setDouble(19, t.getIcmsst());
			}
			ps.setInt(20, t.getCodigo());

			ps.executeUpdate();
			ps.close();

			sql = "delete from financeiro.retencao_pagar where codigo = ?";

			ps = con.prepareStatement(sql);
			ps.setInt(1, t.getCodigo());
			ps.execute();

			for (int i = 0; i < lstImpostos.size(); i++) {
				lstImpostos.get(i).setDuplicata(t.getCodigo());
				salvarRetencao(lstImpostos.get(i));
			}

			con.commit();
			return true;

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
		return false;

	}

	public void salvarRetencao(ImpostoBean impostoBean) throws ProjetoException {

		String sql = " INSERT INTO financeiro.retencao_pagar( codigo, imposto, perc_ret, cd_pagar, base_ret) "
				+ " VALUES (?, ?, ?, ?, ?); ";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, impostoBean.getDuplicata());
			ps.setString(2, impostoBean.getDescImposto());
			ps.setDouble(3, impostoBean.getPcRentencao());
			ps.setInt(4, 0);
			ps.setDouble(5, impostoBean.getValorBase());
			ps.execute();

			con.commit();
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

	public ArrayList<TituloPagarBean> listaAbertos(FornecedorBean forn, java.util.Date periodiInicial,
			java.util.Date periodiFinal) throws ProjetoException {

		String sql = " select  p.codigo,p.codforn,p.dtcompete,p.juros,p.multa,p.valor,p.processo,"
				+ "  p.desconto,p.dtemissao,p.dtvcto,p.historico,p.duplicata,c.idccusto, "
				+ "  f.nome as nomeFornecedor,d.descricao as despesa,c.descricao descCusto,pt.descricao portdesc,pt.codportador, "
				+ "  sum(valorpago) as valorpago, p.dtprevisao, p.parcela, "
				+ "  p.valor - coalesce(p.desconto,0)+coalesce(p.juros,0)+coalesce(p.multa,0)-coalesce(p.vlr_retencao,0)"
				+ "  - coalesce((select sum(coalesce(valorpago,0)-(coalesce(vlrdesc,0)+coalesce(vlrjuros,0)+coalesce(vlrmulta,0))) from financeiro.pagdupbx"
				+ " where pagdupbx.codigo=p.codigo),0) valoraberto"
				+ "  from financeiro.pagdup p join financeiro.fornecedor f on (p.codforn = f.codforn) "
				+ "  join financeiro.despesa d on (d.iddespesa = p.iddespesa) "
				+ "  join financeiro.ccusto c on (c.idccusto = p.idccusto) "
				+ "	 left join financeiro.portador pt on(pt.codportador = p.codport) "
				+ "  left join financeiro.pagdupbx pbx on(pbx.codigo = p.codigo )where p.situacao= 'A'  and p.dtvcto between ? and ?";
		if ((forn.getCodforn() != null) && (forn.getCodforn() > 0))
			sql = sql + " and p.codforn=?";
		sql = sql + " group by p.codigo,p.codforn,p.dtcompete,p.juros,p.multa,p.valor,p.processo,"
				+ "  p.desconto,p.dtemissao,p.dtvcto,p.historico,p.duplicata,c.idccusto,p.vlr_retencao,nomeFornecedor,despesa,descCusto,portdesc,pt.codportador,p.parcela, p.dtprevisao ";
		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("obj_usuario");

		ArrayList<TituloPagarBean> lista = new ArrayList<TituloPagarBean>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setDate(1, new java.sql.Date(periodiInicial.getTime()));
			ps.setDate(2, new java.sql.Date(periodiFinal.getTime()));
			if ((forn.getCodforn() != null) && (forn.getCodforn() > 0))
				ps.setInt(1, forn.getCodforn());
			set = ps.executeQuery();

			while (set.next()) {
				PortadorBean portBean = new PortadorBean();
				CentroCustoBean ccbean = new CentroCustoBean();
				DespesaBean despesa = new DespesaBean();
				TituloPagarBean tit = new TituloPagarBean();
				tit.setValor(set.getDouble("valor"));
				tit.setDesconto(set.getDouble("desconto"));
				tit.setDuplicata(set.getString("processo"));
				tit.setDtcompete(set.getString("dtcompete"));
				tit.setHistorico(set.getString("historico"));
				tit.setCodigo(set.getInt("codigo"));
				tit.setNotaFiscal(set.getString("processo"));
				tit.setParcela(set.getString("parcela"));
				tit.setJuros(set.getDouble("juros"));
				tit.setMulta(set.getDouble("multa"));
				tit.setPortador(portBean);
				tit.setCcusto(ccbean);
				tit.setDespesa(despesa);
				tit.setDtvcto(set.getDate("dtvcto"));
				tit.setDtemissao(set.getDate("dtemissao"));
				tit.setDtprevisao(set.getDate("dtprevisao"));
				tit.setValoraberto(set.getDouble("valoraberto"));
				ccbean.setIdccusto(set.getInt("idccusto"));
				ccbean.setDescricao(set.getString("descCusto"));
				portBean.setCodportador(set.getInt("codportador"));
				portBean.setDescricao(set.getString("portdesc"));
				despesa.setDescricao(set.getString("despesa"));

				lista.add(tit);

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

	public Double valorAberto(Integer codigo) throws ProjetoException {
		double valoraberto = 0;
		double valoraux = 0;
		double valorjuros = 0;
		double valormulta = 0;
		double valordesconto = 0;
		double valorauxiliar = 0;
		String sql = "select sum(valorpago) as valor from financeiro.pagdupbx where codigo = ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			ps.setInt(1, codigo);
			rs = ps.executeQuery();

			while (rs.next()) {
				valoraberto = (rs.getDouble("valor"));
			}

			ps.close();
			rs.close();

			sql = "select valor, juros, multa, desconto from financeiro.pagdup where codigo = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, codigo);
			rs = ps.executeQuery();
			while (rs.next()) {
				valoraux = rs.getInt("valor");
				valorjuros = rs.getDouble("juros");
				valormulta = rs.getDouble("multa");
				valordesconto = rs.getDouble("desconto");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		valorauxiliar = valoraux - valoraberto - valordesconto + (valorjuros - valormulta);
		return valorauxiliar;
	}

	public List<BaixaPagar> lstBaixas(Integer idSelecionado) throws ProjetoException {

		String sql = " select pagdupbx.id,codchqemitido, pagdupbx.codigo, pagdupbx.ord,dtpagto,vlrjuros,vlrmulta,tpbaixa,valoratual,valorpago,banco, vlrdesc, opcad from financeiro.pagdupbx "
				+ "join financeiro.banco on banco.id = pagdupbx.idbanco where pagdupbx.codigo = ? order by dtpagto desc, ord";

		ArrayList<BaixaPagar> lista = new ArrayList<BaixaPagar>();

		Connection con = null;
		ResultSet set = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);

			ps.setInt(1, idSelecionado);
			set = ps.executeQuery();

			while (set.next()) {

				BaixaPagar bean = new BaixaPagar();
				BancoBean banco = new BancoBean();

				bean.setJuros(set.getDouble("vlrjuros"));
				bean.setMulta(set.getDouble("vlrmulta"));
				bean.setDesconto(set.getDouble("vlrdesc"));
				bean.setTipoBaixaString(set.getString("tpbaixa"));
				bean.getFunc().setId(set.getLong("opcad"));
				bean.setVlrAtual(set.getDouble("valoratual"));
				bean.setValorBaixado(set.getDouble("valorpago"));
				banco.setBanco(set.getString("banco"));
				bean.setdtPagamento(set.getDate("dtpagto"));
				bean.setBanco(banco);
				bean.setDuplicata(set.getInt("codigo"));
				bean.setOrd(set.getInt("ord"));
				bean.setId(set.getInt("id"));
				bean.setCodChequeEmitido(set.getInt("codchqemitido"));
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

	public Integer getSequencia() throws ProjetoException {
		String sql = "select max (to_number(numcheque,'999999'))+1 as sequencia from financeiro.cheque_emitido ";
		Integer sequencia = 1;

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
				sequencia = rs.getInt("sequencia");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		if (sequencia == 0)
			sequencia = 1;
		return sequencia;
	}

	public List<ImpostoBean> listaImposto(Integer codigo) throws SQLException, ProjetoException {
		List<ImpostoBean> lista = new ArrayList<>();
		String sql = "select * from financeiro.retencao_pagar r join financeiro.pagdup p "
				+ "on p.codigo = r.codigo where r.codigo = ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionFactory.getConnection();

			ps = con.prepareStatement(sql);
			ps.setInt(1, codigo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ImpostoBean imposto = new ImpostoBean();
				imposto.setDescImposto(rs.getString("imposto"));
				imposto.setPcRentencao(rs.getDouble("perc_ret"));
				imposto.setValorBase(rs.getDouble("base_ret"));
				lista.add(imposto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				rs.close();
				con.close();
			} catch (Exception e2) {
			}
		}

		return lista;
	}
}
