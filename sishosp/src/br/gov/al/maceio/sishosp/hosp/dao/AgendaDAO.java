package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.AgendaBean;
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

	public List<BloqueioBean> verificarBloqueioProfissional(
			ProfissionalBean prof, Date dataAtendimento, String turno) {

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

	public int verQtdMaxAgenda(AgendaBean agenda) {
		int qtdMax = 0;
		String sqlPro = "select qtdmax from hosp.config_agenda where codmedico = ? and dataagenda = ? and turno = ?";
		String sqlEqui = "select qtdmax from hosp.config_agenda_equipe where codequipe = ? and dataagenda = ? and turno = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if(agenda.getProfissional().getIdProfissional()!=null){
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			}else if(agenda.getEquipe().getCodEquipe()!=null){
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());				
			}
			stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				qtdMax = rs.getInt("qtdmax");
			}
			return qtdMax;
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
	
	public int verQtdAgendados(AgendaBean agenda) {
		int qtd = 0;
		String sqlPro = "select count(*) as qtd from hosp.atendimentos where codmedico = ? and dtaatende = ? and turno = ?;";
		String sqlEqui = "select count(*) as qtd from hosp.atendimentos where codequipe = ? and dtaatende = ? and turno = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;
			if(agenda.getProfissional().getIdProfissional()!=null){
				stm = con.prepareStatement(sqlPro);
				stm.setInt(1, agenda.getProfissional().getIdProfissional());
			}else if(agenda.getEquipe().getCodEquipe()!=null){
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(1, agenda.getEquipe().getCodEquipe());				
			}
			stm.setDate(2, new java.sql.Date(agenda.getDataAtendimento().getTime()));
			stm.setString(3, agenda.getTurno().toUpperCase());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				qtd = rs.getInt("qtd");
			}
			System.out.println("QTD "+qtd);
			return qtd;
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

	public boolean gravarAgenda(AgendaBean agenda) {

		String sql = "INSERT INTO hosp.atendimentos(codpaciente, codmedico, codredeatende,"
				+ " codconvenio, dtaatende, horaatende, situacao, codatendente, dtamarcacao, codtipoatendimento,"
				+ " turno, codequipe, observacao, ativo, codempresa)"
				+ " VALUES "
				+ "(?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?);";
		try {
			con = ConnectionFactory.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, agenda.getPaciente().getId_paciente());
			if(agenda.getProfissional().getIdProfissional()!=null){
				ps.setInt(2, agenda.getProfissional().getIdProfissional());
			}else{
				ps.setInt(2, 0);
			}
			ps.setInt(3, 0);//codredeatende
			ps.setInt(4, 0);//codconvenio
			ps.setDate(5, new java.sql.Date(agenda.getDataAtendimento().getTime()));
			ps.setString(6, "");//horaatende
			ps.setString(7, "X");//situacao
			ps.setInt(8, 0);//codatendente
			ps.setDate(9, new java.sql.Date(new Date().getTime()));
			ps.setInt(10, agenda.getTipoAt().getIdTipo());
			ps.setString(11, agenda.getTurno().toUpperCase());
			if(agenda.getEquipe().getCodEquipe()!=null){
				ps.setInt(12, agenda.getEquipe().getCodEquipe());
			}else{
				ps.setInt(12, 0);
			}
			ps.setString(13, agenda.getObservacao().toUpperCase());
			ps.setString(14, "S");//ativo
			ps.setInt(15, 0);//COD EMPRESA ?
			
			ps.execute();
			con.commit();
			return true;
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
	
	public List<AgendaBean> listarAgendamentosData(AgendaBean ag) throws ProjetoException {
		List<AgendaBean> lista = new ArrayList<>();
		PacienteDAO pDao = new PacienteDAO();
		ProfissionalDAO mDao = new ProfissionalDAO();
		TipoAtendimentoDAO tDao = new TipoAtendimentoDAO();
		EquipeDAO eDao = new EquipeDAO();
		
		String sqlProf = "SELECT id_atendimento, codpaciente, codmedico, codredeatende,"
				+ " codconvenio, dtaatende, horaatende, situacao, codatendente,"
				+ " dtamarcacao, codtipoatendimento, turno, codequipe, observacao, ativo, codempresa"
				+ " FROM  hosp.atendimentos "
				+ " WHERE dtaatende = ? and codmedico = ? and turno = ?;";
		String sqlEqui = "SELECT id_atendimento, codpaciente, codmedico, codredeatende,"
				+ " codconvenio, dtaatende, horaatende, situacao, codatendente,"
				+ " dtamarcacao, codtipoatendimento, turno, codequipe, observacao, ativo, codempresa"
				+ " FROM  hosp.atendimentos "
				+ " WHERE dtaatende = ? and codequipe = ? and turno = ?;";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = null;

			if(ag.getProfissional().getIdProfissional()!=null){
				System.out.println("COM PROF");
				stm = con.prepareStatement(sqlProf);
				stm.setInt(2, ag.getProfissional().getIdProfissional());
			}else if(ag.getEquipe().getCodEquipe()!=null){
				System.out.println("COM EQUIPE");
				stm = con.prepareStatement(sqlEqui);
				stm.setInt(2, ag.getEquipe().getCodEquipe());
			}
			
			stm.setDate(1,new java.sql.Date(ag.getDataAtendimento().getTime()));
			stm.setString(3, ag.getTurno());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AgendaBean agenda = new AgendaBean();
				agenda.setIdAgenda(rs.getInt("id_atendimento"));
				agenda.setPaciente(pDao.listarPacientePorID(rs.getInt("codpaciente")));
				agenda.setProfissional(mDao.buscarProfissionalPorID(rs.getInt("codmedico")));
				agenda.setDataAtendimento(rs.getDate("dtaatende"));
				agenda.setSituacao(rs.getString("situacao"));
				agenda.setDataMarcacao(rs.getDate("dtamarcacao"));
				agenda.setTipoAt(tDao.listarTipoPorId(rs.getInt("codtipoatendimento")));
				agenda.setTurno(rs.getString("turno"));
				agenda.setEquipe(eDao.buscarEquipePorID(rs.getInt("codequipe")));
				agenda.setObservacao(rs.getString("observacao"));
				agenda.setAtivo(rs.getString("ativo"));
				lista.add(agenda);
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
