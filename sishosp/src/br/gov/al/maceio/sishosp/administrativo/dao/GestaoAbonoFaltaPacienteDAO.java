package br.gov.al.maceio.sishosp.administrativo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.administrativo.model.GestaoAbonoFaltaPaciente;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;

public class GestaoAbonoFaltaPacienteDAO {

	Connection con = null;
	PreparedStatement ps = null;
	
	public List<GestaoAbonoFaltaPaciente> listarAfastamentoProfissionais() {

		List<GestaoAbonoFaltaPaciente> listaAbonosFaltaPaciente = new ArrayList<>();

		String sql = "select afp.id, afp.codusuario_operacao, afp.cod_programa, " + 
				"afp.cod_grupo, afp.cod_equipe, afp.data_abono, afp.turno, afp.codpaciente, " + 
				"pa.nome paciente, pro.descprograma, g.descgrupo, e.descequipe " + 
				"	from adm.abono_falta_paciente afp " + 
				"	join acl.funcionarios f on f.id_funcionario = afp.codusuario_operacao " + 
				"	join hosp.programa pro on pro.id_programa = afp.cod_programa " + 
				"	join hosp.grupo g on g.id_grupo = afp.cod_grupo " + 
				"	join hosp.equipe e on e.id_equipe = afp.cod_equipe " + 
				"	join hosp.pacientes pa on pa.id_paciente = afp.codpaciente ";

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GestaoAbonoFaltaPaciente abonoFaltaPaciente = new GestaoAbonoFaltaPaciente();
				abonoFaltaPaciente.setId(rs.getInt("id"));
				abonoFaltaPaciente.getFuncionario().setId(rs.getLong("codusuario_operacao"));
				abonoFaltaPaciente.getPrograma().setIdPrograma(rs.getInt("cod_programa"));
				abonoFaltaPaciente.getPrograma().setDescPrograma(rs.getString("descprograma"));
				abonoFaltaPaciente.getGrupo().setIdGrupo(rs.getInt("cod_grupo"));
				abonoFaltaPaciente.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				abonoFaltaPaciente.getEquipe().setCodEquipe(rs.getInt("cod_equipe"));
				abonoFaltaPaciente.getEquipe().setDescEquipe(rs.getString("descequipe"));
				abonoFaltaPaciente.setDataAbono(rs.getDate("data_abono"));
				abonoFaltaPaciente.setTurno(rs.getString("turno"));
				abonoFaltaPaciente.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				abonoFaltaPaciente.getPaciente().setNome(rs.getString("paciente"));
				listaAbonosFaltaPaciente.add(abonoFaltaPaciente);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaAbonosFaltaPaciente;
	}
}
