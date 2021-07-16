package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.MotivoDesligamentoBean;

public class MotivoDesligamentoDAO {
	
	Connection con = null;
	PreparedStatement ps = null;

	public boolean gravarMotivo(MotivoDesligamentoBean motivo) throws ProjetoException {
		Boolean retorno = false;
		String sql = "insert into hosp.motivo_desligamento (motivo) values (?);";

		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, motivo.getMotivo_desligamento().toUpperCase());
			ps.execute();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean alterarMotivo(MotivoDesligamentoBean motivo) throws ProjetoException {
		Boolean retorno = false;
		String sql = "update hosp.motivo_desligamento set motivo = ? where id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, motivo.getMotivo_desligamento().toUpperCase());
			stmt.setInt(2, motivo.getId_motivo());
			stmt.executeUpdate();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}

	public Boolean excluirMotivo(MotivoDesligamentoBean motivo) throws ProjetoException {
		Boolean retorno = false;
		String sql = "delete from hosp.motivo_desligamento where id = ?";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setLong(1, motivo.getId_motivo());
			stmt.execute();
			con.commit();
			retorno = true;
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return retorno;
	}

	public ArrayList<MotivoDesligamentoBean> listarMotivos() throws ProjetoException {

		String sql = "select id, motivo from hosp.motivo_desligamento order by motivo";

		ArrayList<MotivoDesligamentoBean> lista = new ArrayList();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				MotivoDesligamentoBean motivoDesligamento = new MotivoDesligamentoBean();
				motivoDesligamento.setId_motivo(rs.getInt("id"));
				motivoDesligamento.setMotivo_desligamento(rs.getString("motivo"));
				lista.add(motivoDesligamento);
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return lista;
	}

	public MotivoDesligamentoBean buscaMotivoPorId(Integer i)
			throws ProjetoException {
		
		String sql = "select id, motivo from hosp.motivo_desligamento where id =? order by motivo";
		MotivoDesligamentoBean motivoDesligamento = new MotivoDesligamentoBean();
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				motivoDesligamento.setId_motivo(rs.getInt("id"));
				motivoDesligamento.setMotivo_desligamento(rs.getString("motivo"));
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return motivoDesligamento;
	}
}
