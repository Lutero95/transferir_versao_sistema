package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.BloqueioBean;
import br.gov.al.maceio.sishosp.hosp.model.FeriadoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;

public class AgendaDAO {
	Connection con = null;
	PreparedStatement ps = null;

	public FeriadoBean verificarFeriado(Date dataAtendimento) {
		
		String sql = "select codferiado, descferiado, dataferiado"
				+ " from hosp.feriado where dataferiado = ?";
		FeriadoBean fer = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setDate(1, new java.sql.Date(dataAtendimento.getTime()));
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				fer = new FeriadoBean();
				fer.setCodFeriado(rs.getInt("codferiado"));
				fer.setDescFeriado(rs.getString("descferiado"));
				fer.setDataFeriado(rs.getDate("dataferiado"));
			}
			return fer;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public List<BloqueioBean> verificarBloqueioProfissional(ProfissionalBean prof, Date dataAtendimento, String turno) {
		
		List<BloqueioBean> lista = new ArrayList<>();
		ProfissionalDAO pDao = new ProfissionalDAO();
		String sql = "select id_bloqueioagenda, codmedico,"
				+ " dataagenda, turno, descricao, codempresa "
				+ " from hosp.bloqueio_agenda "
				+ " where codmedico = ? and  dataagenda = ? and turno = ?"
				+ " order by id_bloqueioagenda";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, prof.getIdProfissional());
			stm.setDate(2, new java.sql.Date(dataAtendimento.getTime()));
			stm.setString(3, turno.toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				BloqueioBean bloqueio = new BloqueioBean();
				bloqueio.setIdBloqueio(rs.getInt("id_bloqueioagenda"));
				bloqueio.setProf(pDao.buscarProfissionalPorID(rs
						.getInt("codmedico")));
				bloqueio.setDataInicio(rs.getDate("dataagenda"));
				bloqueio.setTurno(rs.getString("turno"));
				bloqueio.setDescBloqueio(rs.getString("descricao"));
				bloqueio.setCodEmpresa(rs.getInt("codempresa"));

				lista.add(bloqueio);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}

		return lista;
	}
	
}
