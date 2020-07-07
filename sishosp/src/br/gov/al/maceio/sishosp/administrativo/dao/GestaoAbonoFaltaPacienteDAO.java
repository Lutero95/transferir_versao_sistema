package br.gov.al.maceio.sishosp.administrativo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bridj.jawt.JAWT.GetComponent_callback;

import br.gov.al.maceio.sishosp.administrativo.model.GestaoAbonoFaltaPaciente;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoAbonoFaltaPacienteDTO;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarRemocaoAtendimentoDTO;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;

public class GestaoAbonoFaltaPacienteDAO {

	Connection con = null;
	PreparedStatement ps = null;
	
	public List<GestaoAbonoFaltaPaciente> listarAfastamentoProfissionais() {

		List<GestaoAbonoFaltaPaciente> listaAbonosFaltaPaciente = new ArrayList<>();

		String sql = "select afp.id, afp.codusuario_operacao, afp.cod_programa, " + 
				"afp.cod_grupo, afp.cod_equipe, afp.data_abono, afp.codpaciente, " + 
				"pa.nome paciente, pro.descprograma, g.descgrupo, e.descequipe, " + 
				"CASE WHEN afp.turno = 'T' THEN 'TARDE' " + 
        		"WHEN afp.turno = 'M' THEN 'MANH�' END AS turno "+
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
	
	public List<AtendimentoBean> listarAtendimentosParaAbono(GestaoAbonoFaltaPaciente abonoFaltaPaciente) {

		List<AtendimentoBean> listaAtendimentosParaAbono = new ArrayList<>();

		String sql = "SELECT distinct a.id_atendimento, a1.id_atendimentos1, a.dtaatende, a.codprograma, p.descprograma, " + 
				"a1.codprofissionalatendimento, f.descfuncionario, a.codgrupo, g.descgrupo, a.codequipe, e.descequipe, profissional.descfuncionario, " + 
				"CASE WHEN a.turno = 'T' THEN 'TARDE' " + 
				"WHEN a.turno = 'M' THEN 'MANHA' END AS turno, pa.nome nomepaciente, pa.id_paciente, sa.descricao AS situacao_atendimento, " + 
				"sa.abono_falta " + 
				"FROM hosp.atendimentos a " + 
				"JOIN hosp.pacientes pa on pa.id_paciente = a.codpaciente " + 
				"JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento) " + 
				"JOIN acl.funcionarios f ON (a1.codprofissionalatendimento = f.id_funcionario) " + 
				"JOIN hosp.programa p ON (a.codprograma = p.id_programa) " + 
				"JOIN hosp.grupo g ON (a.codgrupo = g.id_grupo) " + 
				"LEFT JOIN hosp.equipe e ON (a.codequipe = e.id_equipe) " + 
				"LEFT JOIN acl.funcionarios profissional ON (profissional.id_funcionario = a.codmedico) " + 
				"LEFT JOIN hosp.situacao_atendimento sa on a1.id_situacao_atendimento = sa.id " + 
				"WHERE a.codpaciente = ?  AND a.dtaatende = ? " + 
				"AND coalesce(a.situacao,'A') <> 'C' AND coalesce(a1.excluido,'N')='N' and a1.evolucao is null " + 
				"AND NOT EXISTS (SELECT afp1.id FROM adm.abono_falta_paciente_1 afp1 WHERE afp1.id_atendimentos1 = a1.id_atendimentos1 ) ";
		
		if (!abonoFaltaPaciente.getTurno().equals(Turno.AMBOS.getSigla())
				&& !VerificadorUtil.verificarSeObjetoNuloOuVazio(abonoFaltaPaciente.getTurno())) 
            sql = sql + "AND a.turno = ? ";
        
        
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getPrograma())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getPrograma().getIdPrograma()))) 
            sql = sql + "AND a.codprograma = ? ";
        
        
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getGrupo().getIdGrupo()))) 
            sql = sql + "AND a.codgrupo = ? ";
        
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getEquipe())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getEquipe().getCodEquipe()))) 
            sql = sql + "AND a.codequipe = ? ";
        

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, abonoFaltaPaciente.getPaciente().getId_paciente());
			stm.setDate(2, new Date(abonoFaltaPaciente.getDataAbono().getTime()));
			Integer proximoParametroStatment = 3;
			if (!abonoFaltaPaciente.getTurno().equals(Turno.AMBOS.getSigla())
					&& !VerificadorUtil.verificarSeObjetoNuloOuVazio(abonoFaltaPaciente.getTurno())) {
	            stm.setString(proximoParametroStatment, abonoFaltaPaciente.getTurno());
	            proximoParametroStatment++;
			}
	        
	        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getPrograma())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getPrograma().getIdPrograma()))) { 
	            stm.setInt(proximoParametroStatment, abonoFaltaPaciente.getPrograma().getIdPrograma());
	            proximoParametroStatment++;
	        }
	        
	        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getGrupo().getIdGrupo()))) { 
	        	stm.setInt(proximoParametroStatment, abonoFaltaPaciente.getGrupo().getIdGrupo());
	        	proximoParametroStatment++;
	        }
	        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getEquipe())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(abonoFaltaPaciente.getEquipe().getCodEquipe()))) { 
	        	stm.setInt(proximoParametroStatment, abonoFaltaPaciente.getEquipe().getCodEquipe());
	        	proximoParametroStatment++;
	        }    
	            
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean atendimentoParaAbono = new AtendimentoBean();
				atendimentoParaAbono.setId(rs.getInt("id_atendimento"));
				atendimentoParaAbono.setId1(rs.getInt("id_atendimentos1"));
				atendimentoParaAbono.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimentoParaAbono.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimentoParaAbono.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimentoParaAbono.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				atendimentoParaAbono.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimentoParaAbono.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				atendimentoParaAbono.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimentoParaAbono.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				atendimentoParaAbono.getEquipe().setDescEquipe(rs.getString("descequipe"));
				atendimentoParaAbono.setTurno(rs.getString("turno"));
				atendimentoParaAbono.getPaciente().setId_paciente(rs.getInt("id_paciente"));
				atendimentoParaAbono.getPaciente().setNome(rs.getString("nomepaciente"));
				atendimentoParaAbono.getSituacaoAtendimento().setDescricao(rs.getString("situacao_atendimento"));
				atendimentoParaAbono.getSituacaoAtendimento().setAbonoFalta(rs.getBoolean("abono_falta"));
				listaAtendimentosParaAbono.add(atendimentoParaAbono);
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
		return listaAtendimentosParaAbono;
	}
	
    public boolean inserirAbonoFaltaPaciente(GravarInsercaoAbonoFaltaPacienteDTO gravarInsercaoAbonoFaltaPacienteDTO) throws SQLException {

        Boolean retorno = false;
        String sql = "INSERT INTO adm.abono_falta_paciente " + 
        		"(data_hora_operacao, codusuario_operacao, cod_programa, cod_grupo, cod_equipe, data_abono, turno, codpaciente, justificativa)"  + 
        		"VALUES(CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
        AtendimentoBean atendimentoBean = gravarInsercaoAbonoFaltaPacienteDTO.getAtendimentosParaAbono().get(0);
        try {

        	con = ConnectionFactory.getConnection();

            ps = con.prepareStatement(sql);

        	ps.setLong(1, gravarInsercaoAbonoFaltaPacienteDTO.getIdUsuarioOperacao());
        	ps.setInt(2, atendimentoBean.getPrograma().getIdPrograma());
        	ps.setInt(3, atendimentoBean.getGrupo().getIdGrupo());
        	ps.setInt(4, atendimentoBean.getEquipe().getCodEquipe());
        	ps.setDate(5, new Date(atendimentoBean.getDataAtendimentoInicio().getTime()));
        	if(atendimentoBean.getTurno().equalsIgnoreCase(Turno.MANHA.name()))
        		ps.setString(6, Turno.MANHA.getSigla());
        	else if(atendimentoBean.getTurno().equalsIgnoreCase(Turno.TARDE.name()))
        		ps.setString(6, Turno.TARDE.getSigla());
        	
        	ps.setInt(7, atendimentoBean.getPaciente().getId_paciente());
        	ps.setString(8, gravarInsercaoAbonoFaltaPacienteDTO.getJustificativa());
            ResultSet rs = ps.executeQuery();
            
            Integer idAbono;
            if(rs.next()) {
            	idAbono = rs.getInt("id");
            	for (AtendimentoBean atendimento : gravarInsercaoAbonoFaltaPacienteDTO.getAtendimentosParaAbono()) {
                	inserirAbonoFaltaPaciente1(atendimento.getId1(), idAbono);
                	atualizarSituacaoAtendimento1(atendimento.getId1());                
                }
            }
            retorno = true;
            con.commit();
        }
        catch (Exception ex) {
        	con.rollback();
            ex.printStackTrace();
            JSFUtil.adicionarMensagemErro(ex.getMessage(), "Aten��o");
            throw new RuntimeException(ex);
        } finally {
            try {
            	con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return retorno;
    }
    
    private void inserirAbonoFaltaPaciente1(Integer idAtendimento1, Integer idAbono) throws Exception {
    	
    	String sql = "INSERT INTO adm.abono_falta_paciente_1 " + 
    			"(id_atendimentos1, id_abono_falta_paciente) " + 
    			"VALUES(?, ?);";
    	
    	try {
    		ps = con.prepareStatement(sql);
			ps.setInt(1, idAtendimento1);
			ps.setInt(2, idAbono);
			ps.execute();
		} catch (Exception e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Atenção");
			e.printStackTrace();
			throw e;
		}
    }
    
    private void atualizarSituacaoAtendimento1(Integer idAtendimento1) throws Exception {
    	
    	String sql = "update hosp.atendimentos1 set id_situacao_atendimento =(select id from hosp.situacao_atendimento where situacao_atendimento.abono_falta is true) where id_atendimentos1 = ?;";
    	
    	try {
    		ps = con.prepareStatement(sql);
			ps.setInt(1, idAtendimento1);
			ps.execute();
		} catch (Exception e) {
            JSFUtil.adicionarMensagemErro(e.getMessage(), "Atenção");
			e.printStackTrace();
			throw e;
		}
    }
    
}
