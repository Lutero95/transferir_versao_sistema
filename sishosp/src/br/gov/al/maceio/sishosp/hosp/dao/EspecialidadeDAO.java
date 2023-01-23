package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.model.EspecialidadeBean;

public class EspecialidadeDAO {

    Connection con = null;
    PreparedStatement ps = null;

    public boolean gravarEspecialidade(EspecialidadeBean esp) throws ProjetoException {
        Boolean retorno = false;
        String sql = "insert into hosp.especialidade (descespecialidade) values (?);";
        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, esp.getDescEspecialidade().toUpperCase());
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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean alterarEspecialidade(EspecialidadeBean espec) throws ProjetoException {
        Boolean retorno = false;
        String sql = "update hosp.especialidade set descespecialidade = ? where id_especialidade = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, espec.getDescEspecialidade().toUpperCase());
            stmt.setInt(2, espec.getCodEspecialidade());
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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean excluirEspecialidade(EspecialidadeBean espec) throws ProjetoException {
        Boolean retorno = false;
        String sql = "delete from hosp.especialidade where id_especialidade = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, espec.getCodEspecialidade());
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
                ex.printStackTrace();
            }
        }
        return retorno;
    }

    public List<EspecialidadeBean> listarEspecialidades()
            throws ProjetoException {
        List<EspecialidadeBean> lista = new ArrayList<>();
        String sql = "select id_especialidade, descespecialidade from hosp.especialidade order by descespecialidade";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));

                lista.add(esp);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EspecialidadeBean> listarEspecialidadesBusca
    	(String descricao,  Integer tipo) throws ProjetoException {
        List<EspecialidadeBean> lista = new ArrayList<>();
        String sql = "select id_especialidade, descespecialidade from hosp.especialidade ";
        if (tipo == 1) {
            sql += " where descespecialidade LIKE ?";
        }
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + descricao.toUpperCase() + "%");
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                lista.add(esp);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public EspecialidadeBean listarEspecialidadePorId(int id)
            throws ProjetoException {

        EspecialidadeBean esp = new EspecialidadeBean();
        String sql = "select id_especialidade, descespecialidade from hosp.especialidade where id_especialidade = ?";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
            }

        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return esp;
    }

    public List<EspecialidadeBean> listarEspecialidadesEquipe(Integer idPacienteInstituicao)
            throws ProjetoException {

        List<EspecialidadeBean> lista = new ArrayList<>();

        String sql = "SELECT distinct es.id_especialidade, es.descespecialidade " +
                "FROM hosp.equipe e " +
                "LEFT JOIN hosp.equipe_medico em ON (em.equipe = e.id_equipe) " +
                "LEFT JOIN acl.funcionarios f ON (f.id_funcionario = em.medico) " +
                "LEFT JOIN hosp.especialidade es ON (es.id_especialidade = f.codespecialidade) " +
                "WHERE id_equipe = (SELECT p.codequipe FROM hosp.paciente_instituicao p WHERE id = ?)";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idPacienteInstituicao);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                lista.add(esp);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<EspecialidadeBean> listarEspecialidadesPorEquipe(Integer idEquipe)
            throws ProjetoException {

        List<EspecialidadeBean> lista = new ArrayList<>();

        String sql = "SELECT es.id_especialidade, es.descespecialidade " +
                "FROM hosp.equipe e " +
                "LEFT JOIN hosp.equipe_medico em ON (em.equipe = e.id_equipe) " +
                "LEFT JOIN acl.funcionarios f ON (f.id_funcionario = em.medico) " +
                "LEFT JOIN hosp.especialidade es ON (es.id_especialidade = f.codespecialidade) " +
                "WHERE id_equipe = ?";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, idEquipe);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                lista.add(esp);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }
    

    public List<EspecialidadeBean> listarEspecialidadesPacienteEmTerapia(Integer codPrograma, Integer codGrupo, Integer codPaciente)
            throws ProjetoException {

        List<EspecialidadeBean> lista = new ArrayList<>();

        String sql = "select distinct pi.codprograma, pi.codgrupo,es.id_especialidade, es.descespecialidade from hosp.paciente_instituicao pi\n" + 
        		" left join hosp.laudo l on l.id_laudo = pi.codlaudo\n" + 
        		"join hosp.profissional_dia_atendimento pda on pda.id_paciente_instituicao = pi.id\n" + 
        		"LEFT JOIN acl.funcionarios f ON (f.id_funcionario = pda.id_profissional) \n" +
        		"LEFT JOIN hosp.especialidade es ON (es.id_especialidade = f.codespecialidade) \n" +
        		"where pi.codprograma=? and pi.codgrupo=? and coalesce(l.codpaciente, pi.id_paciente) =? and status='A'";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, codPrograma);
            stm.setInt(2, codGrupo);
            stm.setInt(3, codPaciente);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                lista.add(esp);
            }
        } catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

//    MÉTODO ADICIONADO - MARTINHO
    public List<EspecialidadeBean> listarEspecialidadesEvolucaoEmTerapia()
            throws ProjetoException {

        List<EspecialidadeBean> lista = new ArrayList<>();

        String sql = "select distinct e.id_especialidade, e.descespecialidade from acl.funcionarios f \n" +
                "inner join hosp.especialidade e on f.codespecialidade = e.id_especialidade \n" +
                "order by e.descespecialidade ";


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                EspecialidadeBean esp = new EspecialidadeBean();
                esp.setCodEspecialidade(rs.getInt("id_especialidade"));
                esp.setDescEspecialidade(rs.getString("descespecialidade"));
                lista.add(esp);
            }
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }
}
