package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.ObjetivoPtsCifBean;
import br.gov.al.maceio.sishosp.hosp.model.PtsCifBean;


public class PtsCifDAO {

	private final Integer SEIS_MESES_VENCIMENTO = 180;
	
	public List<PtsCifBean> buscarPtsPacientesAtivos(String tipoFiltroVencimento,
			Integer filtroMesVencimento, Integer filtroAnoVencimento, String campoBusca, 
			String tipoBusca, String filtroTurno, boolean filtroApenasPacientesSemPTS) throws ProjetoException {


		String sql = "SELECT p.id, pi.id as id_paciente_instituicao, pa.nome, extract (year from age(pa.dtanascimento)) idade, \r\n" + 
				"pa.id_paciente codpaciente, pa.cpf, pa.cns, pa.matricula, pr.id_programa, pr.descprograma, g.id_grupo, g.descgrupo, \r\n" + 
				"p.data_vencimento, p.data_avaliacao, "+
				"case when p.data_vencimento is null " + 
				"		then false " + 
				"	when p.data_vencimento < current_date " + 
				"		then true " + 
				"	else " + 
				"		false " + 
				"	end pts_vencido "+
				"FROM hosp.paciente_instituicao pi  \r\n" + 
				"LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo)   \r\n" + 
				"LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = coalesce(laudo.codpaciente, pi.id_paciente) ) \r\n" + 
				"LEFT JOIN hosp.programa pr ON (pr.id_programa = pi.codprograma)  \r\n" + 
				"LEFT JOIN hosp.grupo g ON (g.id_grupo = pi.codgrupo)  \r\n" + 
				"LEFT JOIN hosp.pts_cif p ON (p.id_paciente_instituicao = pi.id)  \r\n" + 
				" left join (select p2.id, p2.id_paciente_instituicao from hosp.pts_cif p2 where p2.id = (select max(id) from hosp.pts_cif p3  \r\n" + 
				" where (p3.id_paciente_instituicao = p2.id_paciente_instituicao)) ) ptsmax on  \r\n" + 
				"  ptsmax.id_paciente_instituicao = p.id_paciente_instituicao \r\n" + 
				"WHERE pi.status = 'A' and (case when p.id is not null then p.id=ptsmax.id else 1=1 end) \r\n";


		if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca) 
				&& (tipoBusca.equals("paciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca) )) {
			sql = sql + " and pa.nome ilike ?";
		}

		if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca) 
				&& (tipoBusca.equals("matpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca) )) {
			sql = sql + " and pa.matricula = ?";
		}

		if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca)
				&& (tipoBusca.equals("prontpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca) )) {
			sql = sql + " and pa.id_paciente = ?";
		}


		if (!filtroTurno.equals(Turno.AMBOS.getSigla())) {
			sql = sql + " AND pi.turno=? ";
		}
		
		if(filtroApenasPacientesSemPTS) {
			sql += " and not exists (select pc.id from hosp.pts_cif pc where pc.id_paciente_instituicao = pi.id) ";
		}
		
        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroMesVencimento)) {
            sql = sql + " AND EXTRACT(month FROM p.data_vencimento) = ? ";
        }

        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroAnoVencimento)) {
            sql = sql + " AND EXTRACT(year FROM p.data_vencimento) = ? ";
        }
		
		sql += " ORDER BY pa.nome ";

		List<PtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);

			int i = 1;

			if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca)
					&& ((tipoBusca.equals("paciente")) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca) )) {
				ps.setString(i, "%" + campoBusca.toUpperCase() + "%");
				i++;
			}

			if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca)
					&& (tipoBusca.equals("matpaciente")) && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca) ) {
				ps.setString(i, campoBusca.toUpperCase());
				i++;
			}

			if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca)
					&& (tipoBusca.equals("prontpaciente") && !VerificadorUtil.verificarSeObjetoNuloOuVazio(campoBusca) )) {
				ps.setInt(i, Integer.valueOf(campoBusca));
				i++;
			}

			if (!filtroTurno.equals(Turno.AMBOS.getSigla())) {
				ps.setString(i, filtroTurno);
				i++;
			}
			
	        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroMesVencimento)) {
	            ps.setInt(i, filtroMesVencimento);
	            i++;
	        }

	        if (!VerificadorUtil.verificarSeObjetoNuloOuZero(filtroAnoVencimento)) {
	            ps.setInt(i, filtroAnoVencimento);
	            i++;
	        }

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PtsCifBean pts = new PtsCifBean();
				mapearResultSetPtsCif(rs, pts);
				lista.add(pts);
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}
	
    public Boolean verificarSeExistePtsPaciente(PtsCifBean pts) throws ProjetoException {

        Boolean retorno = false;

        String sql = "SELECT id FROM hosp.pts_cif WHERE id_paciente_instituicao = ? ";
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, pts.getGerenciarPaciente().getId());

            ResultSet rs = stm.executeQuery();

            if (rs.next())
                retorno = true;

        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
    
    public Boolean cadastrarPtsCif(PtsCifBean pts) throws ProjetoException {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.pts_cif " + 
        		"(id_paciente_instituicao, queixa_principal, condicoes_saude, funcao_estrutura_corpo, " +
        		"atividade_participacao, fatores_contextuais, id_fator_ambiental, fatores_pessoais, "+
        		" data_avaliacao, data_vencimento) " + 
        		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id; ";
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement ps = conexao.prepareStatement(sql);
            
            pts.getFatoresAmbientais().setId(cadastrarFatoresAmbientais(pts, conexao));
            
            mapearPreparedStatementPtsCif(pts, ps);
            
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                pts.setId(rs.getInt("id"));

            cadastrarObjetivoPtsCif(pts, conexao);
            cadastrarAvaliadoresPts(pts, conexao);
            retorno = true;
            conexao.commit();
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
    
    public Boolean alterarPtsCif(PtsCifBean pts) throws ProjetoException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.pts_cif " + 
        		"SET id_paciente_instituicao = ?, queixa_principal = ?, condicoes_saude = ?, "
        		+ "funcao_estrutura_corpo = ?, atividade_participacao = ?, fatores_contextuais = ?, "
        		+ "id_fator_ambiental = ?, fatores_pessoais = ?, data_avaliacao = ?, data_vencimento = ? " + 
        		"WHERE id = ?";
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement ps = conexao.prepareStatement(sql);
            
            alterarFatoresAmbientais(pts, conexao);
            
            mapearPreparedStatementPtsCif(pts, ps);
            ps.setInt(11, pts.getId());
            ps.executeUpdate();

            excluirObjetivoPtsCif(pts, conexao);
            cadastrarObjetivoPtsCif(pts, conexao);
            excluirAvaliadoresPts(pts, conexao);
            cadastrarAvaliadoresPts(pts, conexao);
            retorno = true;
            conexao.commit();
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }

	private void mapearPreparedStatementPtsCif(PtsCifBean pts, PreparedStatement ps) throws SQLException {
		ps.setInt(1, pts.getGerenciarPaciente().getId());
		ps.setString(2, pts.getQueixaPrincipal());
		ps.setString(3, pts.getCondicoesSaude());
		ps.setString(4, pts.getFuncaoIhEstruturaCorpo());
		ps.setString(5, pts.getAtividadeIhParticipacao());
		ps.setString(6, pts.getFatoresContextuais());
		ps.setInt(7, pts.getFatoresAmbientais().getId());
		ps.setString(8, pts.getFatoresPessoais());
		ps.setDate(9, new Date(pts.getDataAvaliacao().getTime()));
		ps.setDate(10, DataUtil.converterDateUtilParaDateSql(DataUtil.adicionarDiasAData(pts.getDataAvaliacao(), SEIS_MESES_VENCIMENTO)));
	}
    
    private void cadastrarObjetivoPtsCif(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        
        String sql = "INSERT INTO hosp.objetivos_pts_cif " + 
        		"(id_pts_cif, objetivo, intervencao, forma_avaliacao) " + 
        		"VALUES(?, ?, ?, ?); ";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);

            for (ObjetivoPtsCifBean objetivo : pts.getListaObjetivos()) {
            	ps.setInt(1, pts.getId());
            	ps.setString(2, objetivo.getObjetivo());
            	ps.setString(3, objetivo.getIntervencao());
            	ps.setString(4, objetivo.getFormaAvaliacao());
            	ps.executeUpdate();
            }

        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private void excluirObjetivoPtsCif(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "DELETE FROM hosp.objetivos_pts_cif WHERE id_pts_cif = ? ; ";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            ps.setInt(1, pts.getId());
            ps.executeUpdate();
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private Integer cadastrarFatoresAmbientais(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "INSERT INTO hosp.fator_ambiental " + 
        		"(facilitador, barreira) VALUES(?, ?) returning id; ";
        
        Integer idFatorAmbiental = null;
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);

            ps.setString(1, pts.getFatoresAmbientais().getFacilitador());
            ps.setString(2, pts.getFatoresAmbientais().getBarreira());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
            	idFatorAmbiental = rs.getInt("id");
            }
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return idFatorAmbiental;
    }
    
    private void alterarFatoresAmbientais(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "UPDATE hosp.fator_ambiental SET facilitador = ?, barreira = ? WHERE id = ?; ";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);

            ps.setString(1, pts.getFatoresAmbientais().getFacilitador());
            ps.setString(2, pts.getFatoresAmbientais().getBarreira());
            ps.setInt(3, pts.getFatoresAmbientais().getId());
            ps.executeUpdate();
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private void cadastrarAvaliadoresPts(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "INSERT INTO hosp.avaliador_pts_cif " + 
        		"(id_avaliador, id_pts_cif) VALUES(?, ?); ";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            
            for (FuncionarioBean avaliadorDTO : pts.getListaAvaliadores()) {
            	ps.setLong(1, avaliadorDTO.getId());
            	ps.setInt(2, pts.getId());
            	ps.executeUpdate();
			}
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private void excluirAvaliadoresPts(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "DELETE FROM hosp.avaliador_pts_cif WHERE id_pts_cif = ? ";
        
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            ps.setInt(1, pts.getId());
            ps.executeUpdate();
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
	public PtsCifBean buscarPtsCifPorId(Integer idPts) throws ProjetoException {


		String sql = "SELECT p.id, pi.id as id_paciente_instituicao, pa.nome, extract (year from age(pa.dtanascimento)) idade, \r\n" + 
				"pa.id_paciente codpaciente, p.queixa_principal, p.condicoes_saude, p.funcao_estrutura_corpo, p.atividade_participacao,\r\n" + 
				"p.fatores_contextuais, p.fatores_pessoais, fa.id id_fator_ambiental, fa.facilitador, fa.barreira, \r\n" + 
				"p.data_vencimento, p.data_avaliacao, \r\n" + 
				"case when p.data_vencimento is null \r\n" + 
				"		then false \r\n" + 
				"	when p.data_vencimento < current_date \r\n" + 
				"		then true \r\n" + 
				"	else \r\n" + 
				"		false \r\n" + 
				"	end pts_vencido \r\n" + 
				"FROM hosp.paciente_instituicao pi  \r\n" + 
				"LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo)   \r\n" + 
				"LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = coalesce(laudo.codpaciente, pi.id_paciente) ) \r\n" + 
				"JOIN hosp.pts_cif p ON (p.id_paciente_instituicao = pi.id)  \r\n" + 
				"join hosp.fator_ambiental fa on p.id_fator_ambiental = fa.id \r\n" + 
				"WHERE p.id = ?";

		Connection conexao = null;
		PtsCifBean pts = new PtsCifBean();
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setInt(1, idPts);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				pts.setId(rs.getInt("id"));
				pts.getGerenciarPaciente().setId(rs.getInt("id_paciente_instituicao"));
				pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				pts.getPaciente().setNome(rs.getString("nome"));
				pts.setIdadePaciente(rs.getInt("idade"));
				pts.setDataAvaliacao(rs.getDate("data_avaliacao"));
				pts.setDataVencimento(rs.getDate("data_vencimento"));
				pts.setPtsVencido(rs.getBoolean("pts_vencido"));
				pts.setQueixaPrincipal(rs.getString("queixa_principal"));
				pts.setCondicoesSaude(rs.getString("condicoes_saude"));
				pts.setFuncaoIhEstruturaCorpo(rs.getString("funcao_estrutura_corpo"));
				pts.setAtividadeIhParticipacao(rs.getString("atividade_participacao"));
				pts.setFatoresContextuais(rs.getString("fatores_contextuais"));
				pts.setFatoresPessoais(rs.getString("fatores_pessoais"));
				pts.getFatoresAmbientais().setId(rs.getInt("id_fator_ambiental"));
				pts.getFatoresAmbientais().setFacilitador(rs.getString("facilitador"));
				pts.getFatoresAmbientais().setBarreira(rs.getString("barreira"));
				pts.setListaAvaliadores(buscaAvaliadoresPtsCif(pts, conexao));
				pts.setListaObjetivos(buscaObjetivosPtsCif(pts, conexao));
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return pts;
	}

    private List<FuncionarioBean> buscaAvaliadoresPtsCif(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "select f.id_funcionario, f.descfuncionario, f.cns from acl.funcionarios f \r\n" + 
        		"	join hosp.avaliador_pts_cif apc on f.id_funcionario = apc.id_avaliador \r\n" + 
        		"	join hosp.pts_cif pc on apc.id_pts_cif = pc.id \r\n" + 
        		"	where pc.id = ?";
        
        List<FuncionarioBean> listaAvaliadores = new ArrayList<>(); 
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            ps.setInt(1, pts.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	FuncionarioBean avaliador = new FuncionarioBean();
            	avaliador.setId(rs.getLong("id_funcionario"));
            	avaliador.setNome(rs.getString("descfuncionario"));
            	avaliador.setCns(rs.getString("cns"));
            	listaAvaliadores.add(avaliador);
            }
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaAvaliadores;
    }
    
    private List<ObjetivoPtsCifBean> buscaObjetivosPtsCif(PtsCifBean pts, Connection conexaoAux) 
    		throws ProjetoException, SQLException {

        String sql = "select opc.id, opc.objetivo, opc.forma_avaliacao, opc.intervencao from hosp.objetivos_pts_cif opc \r\n" + 
        		"	join hosp.pts_cif pc on opc.id_pts_cif = pc.id \r\n" + 
        		"	where pc.id = ?	";
        
        List<ObjetivoPtsCifBean> listaObjetivos = new ArrayList<>(); 
        try {
            PreparedStatement ps = conexaoAux.prepareStatement(sql);
            ps.setInt(1, pts.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	ObjetivoPtsCifBean objetivo = new ObjetivoPtsCifBean();
            	objetivo.setId(rs.getInt("id"));
            	objetivo.setObjetivo(rs.getString("objetivo"));
            	objetivo.setFormaAvaliacao(rs.getString("forma_avaliacao"));
            	objetivo.setIntervencao(rs.getString("intervencao"));
            	listaObjetivos.add(objetivo);
            }
        } catch (SQLException sqle) {
        	conexaoAux.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexaoAux.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
        return listaObjetivos;
    }
    
	public List<PtsCifBean> buscarPtsAvaliador(Long idAvaliador) throws ProjetoException {

		String sql = "SELECT p.id, pi.id as id_paciente_instituicao, pa.nome, extract (year from age(pa.dtanascimento)) idade, \r\n" + 
				"pa.id_paciente codpaciente, pa.cpf, pa.cns, pa.matricula, pr.id_programa, pr.descprograma, g.id_grupo, g.descgrupo, \r\n" + 
				"p.data_vencimento, p.data_avaliacao, \r\n" + 
				"case when p.data_vencimento is null \r\n" + 
				"		then false \r\n" + 
				"	when p.data_vencimento < current_date \r\n" + 
				"		then true \r\n" + 
				"	else \r\n" + 
				"		false \r\n" + 
				"	end pts_vencido \r\n" + 
				"FROM hosp.paciente_instituicao pi  \r\n" + 
				"LEFT JOIN hosp.laudo  ON (laudo.id_laudo = pi.codlaudo)   \r\n" + 
				"LEFT JOIN hosp.pacientes pa ON (pa.id_paciente = coalesce(laudo.codpaciente, pi.id_paciente) ) \r\n" + 
				"LEFT JOIN hosp.programa pr ON (pr.id_programa = pi.codprograma)  \r\n" + 
				"LEFT JOIN hosp.grupo g ON (g.id_grupo = pi.codgrupo)  \r\n" + 
				"JOIN hosp.pts_cif p ON (p.id_paciente_instituicao = pi.id)  \r\n" + 
				"JOIN hosp.avaliador_pts_cif apc on (p.id = apc.id_pts_cif)\r\n" + 
				" left join (select p2.id, p2.id_paciente_instituicao from hosp.pts_cif p2 where p2.id = (select max(id) from hosp.pts_cif p3  \r\n" + 
				" where (p3.id_paciente_instituicao = p2.id_paciente_instituicao)) ) ptsmax on  \r\n" + 
				"  ptsmax.id_paciente_instituicao = p.id_paciente_instituicao \r\n" + 
				"WHERE pi.status = 'A' and (case when p.id is not null then p.id=ptsmax.id else 1=1 end) \r\n" + 
				"and apc.id_avaliador = ? and apc.validado is false";


		List<PtsCifBean> lista = new ArrayList<>();

		Connection conexao = null;
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setLong(1, idAvaliador);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PtsCifBean pts = new PtsCifBean();
				mapearResultSetPtsCif(rs, pts);
				lista.add(pts);
			}

		} catch (SQLException sqle) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(),
					sqle);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return lista;
	}

	private void mapearResultSetPtsCif(ResultSet rs, PtsCifBean pts) throws SQLException {
		pts.setId(rs.getInt("id"));
		pts.getGerenciarPaciente().setId(rs.getInt("id_paciente_instituicao"));
		pts.getGerenciarPaciente().getPrograma().setIdPrograma(rs.getInt("id_programa"));
		pts.getGerenciarPaciente().getPrograma().setDescPrograma(rs.getString("descprograma"));
		pts.getGerenciarPaciente().getGrupo().setIdGrupo(rs.getInt("id_grupo"));
		pts.getGerenciarPaciente().getGrupo().setDescGrupo(rs.getString("descgrupo"));
		pts.getPaciente().setId_paciente(rs.getInt("codpaciente"));
		pts.getPaciente().setNome(rs.getString("nome"));
		pts.getPaciente().setCns(rs.getString("cns"));
		pts.getPaciente().setCpf(rs.getString("cpf"));
		pts.getPaciente().setMatricula(rs.getString("matricula"));
		pts.setIdadePaciente(rs.getInt("idade"));
		pts.setDataAvaliacao(rs.getDate("data_avaliacao"));
		pts.setDataVencimento(rs.getDate("data_vencimento"));
		pts.setPtsVencido(rs.getBoolean("pts_vencido"));
	}
	
    public Boolean gravarValidacaoAvaliador(Long idAvaliador, Integer idPts) throws ProjetoException {

        Boolean retorno = false;

        String sql = "UPDATE hosp.avaliador_pts_cif " + 
        		"SET data_validacao = CURRENT_TIMESTAMP, validado = TRUE " + 
        		"WHERE id_avaliador = ? AND id_pts_cif = ?; ";
        
        Connection conexao = null;
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement ps = conexao.prepareStatement(sql);
            
            ps.setLong(1, idAvaliador);
            ps.setInt(2, idPts);
            ps.executeUpdate();

            retorno = true;
            conexao.commit();
        } catch (SQLException sqle) {
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
            throw new ProjetoException(ex, this.getClass().getName());
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retorno;
    }
}
