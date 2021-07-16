package br.gov.al.maceio.sishosp.comum.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.model.HorarioFuncionamento;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;

public class ToleranciaDAO {
	
	public HorarioFuncionamento buscaHorarioFuncionamento(FuncionarioBean usuario) throws ProjetoException {
		
		HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento();

		String sql = "select p.horario_inicio_funcionamento, p.horario_final_funcionamento "+
				"	from hosp.parametro p " + 
				"	join hosp.unidade u on p.codunidade = u.id " + 
				"	join acl.funcionarios f on f.codunidade = u.id " + 
				"	where f.id_funcionario = ? and u.id = ?;";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setLong(1, usuario.getId());
			stm.setInt(2, usuario.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				horarioFuncionamento.setHorarioInicio(rs.getTime("horario_inicio_funcionamento"));
				horarioFuncionamento.setHorarioFim(rs.getTime("horario_final_funcionamento"));
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return horarioFuncionamento;
	}
	
	
	public Integer buscaMinutosTolerancia(FuncionarioBean usuario) throws ProjetoException {
		
		Integer minutosTolerancia = 0;
		
		String sql = "select coalesce (p.minutos_tolerancia, 0) minutos_tolerancia "+
				"	from hosp.parametro p " + 
				"	join hosp.unidade u on p.codunidade = u.id " + 
				"	join acl.funcionarios f on f.codunidade = u.id " + 
				"	where f.id_funcionario = ? and u.id = ?;";

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			stm.setLong(1, usuario.getId());
			stm.setInt(2, usuario.getUnidade().getId());
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				minutosTolerancia = rs.getInt("minutos_tolerancia");
			}
		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				//comentado walter erro log ex.printStackTrace();
			}
		}
		return minutosTolerancia;
	}
}
