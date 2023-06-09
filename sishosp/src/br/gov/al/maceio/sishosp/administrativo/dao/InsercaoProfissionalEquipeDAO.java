package br.gov.al.maceio.sishosp.administrativo.dao;

import br.gov.al.maceio.sishosp.acl.dao.FuncionarioDAO;
import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.enums.RetornoGravarInsercaoProfissionalAtendimento;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.dto.BuscarAtendimentosParaAdicionarProfissionalDTO;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoAtendimento1DTO;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoProfissionalEquipeAtendimento1DTO;
import br.gov.al.maceio.sishosp.administrativo.model.dto.GravarInsercaoProfissionalEquipeAtendimentoDTO;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.InsercaoPacienteDAO;
import br.gov.al.maceio.sishosp.hosp.enums.Turno;
import br.gov.al.maceio.sishosp.hosp.model.CboBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCidDTO;

import javax.faces.context.FacesContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class InsercaoProfissionalEquipeDAO {

    private Connection con = null;
    private PreparedStatement ps = null;

    private List<BuscarAtendimentosParaAdicionarProfissionalDTO> buscarAtendimentosParaAdicionarProfissional(InsercaoProfissionalEquipe insercaoProfissionalEquipe) throws ProjetoException {

        List<BuscarAtendimentosParaAdicionarProfissionalDTO> lista = new ArrayList<>();
        int i = 2;

        String sql = "SELECT DISTINCT a1.id_atendimento, a.turno " +
                "FROM hosp.atendimentos1 a1 " +
                "JOIN hosp.atendimentos a ON (a1.id_atendimento = a.id_atendimento) " +
                "WHERE a.codprograma = ? " +
                " 	and a1.id_atendimento not in ( " + 
                "	select " +
                "		aa1.id_atendimento " +
                "	from " +
                "		hosp.atendimentos1 aa1 " +
                "	where " +
                "		codprofissionalatendimento = ? " +
                "		and coalesce(aa1.excluido, 'N')= 'N') " +
                "AND a.codequipe is not null and coalesce(a1.excluido,'N')='N' ";


        if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())){
            sql = sql + "AND a.turno = ? ";
        }
        if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo()))) {
            sql = sql + "AND a.codgrupo = ? ";
        }
        if ( (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe().getCodEquipe()))) {
            sql = sql + "AND a.codequipe = ? ";
        }
        if(VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
            sql = sql + "AND a.dtaatende >= ?";
        }
        if(!VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
            sql = sql + "AND a.dtaatende >= ? AND a.dtaatende <= ?";
        }

        if(insercaoProfissionalEquipe.getDiasSemana().size() > 0){
            sql = sql + " AND ( ";
            for(int j=0; j<insercaoProfissionalEquipe.getDiasSemana().size(); j++) {
                if(j == 0){
                    sql = sql + "EXTRACT(DOW FROM a.dtaatende) = ? ";
                }
                else{
                    sql = sql + "OR EXTRACT(DOW FROM a.dtaatende) = ? ";
                }
            }
            sql = sql + " ) ";
        }


        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            stm.setInt(1, insercaoProfissionalEquipe.getPrograma().getIdPrograma());
            stm.setLong(2, insercaoProfissionalEquipe.getFuncionario().getId());

            if(!insercaoProfissionalEquipe.getTurno().equals(Turno.AMBOS.getSigla())) {
                i++;
                stm.setString(i, insercaoProfissionalEquipe.getTurno());
            }

            if ((!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo())) && (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getGrupo().getIdGrupo()))) {
                i++;
                stm.setInt(i, insercaoProfissionalEquipe.getGrupo().getIdGrupo());
            }
            if (!VerificadorUtil.verificarSeObjetoNuloOuZero(insercaoProfissionalEquipe.getEquipe().getCodEquipe())) {
                i++;
                stm.setInt(i, insercaoProfissionalEquipe.getEquipe().getCodEquipe());
            }
            if(VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
                i++;
                stm.setDate(i, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
            }
            if(!VerificadorUtil.verificarSeObjetoNulo(insercaoProfissionalEquipe.getPeriodoFinal())){
                i++;
                stm.setDate(i, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoInicio()));
                i++;
                stm.setDate(i, DataUtil.converterDateUtilParaDateSql(insercaoProfissionalEquipe.getPeriodoFinal()));
            }
            if(insercaoProfissionalEquipe.getDiasSemana().size() > 0){
                for(int j=0; j<insercaoProfissionalEquipe.getDiasSemana().size(); j++) {
                    i++;
                    stm.setInt(i, Integer.parseInt(insercaoProfissionalEquipe.getDiasSemana().get(j)));
                }
            }

            ResultSet rs = stm.executeQuery();
                        
            while (rs.next()) {
            	BuscarAtendimentosParaAdicionarProfissionalDTO buscarAtendimentosParaAdicionarProfissionalDTO 
            		= new BuscarAtendimentosParaAdicionarProfissionalDTO();
            	buscarAtendimentosParaAdicionarProfissionalDTO.setIdAtendimento(rs.getInt("id_atendimento"));
            	buscarAtendimentosParaAdicionarProfissionalDTO.setTurno(rs.getString("turno"));
                lista.add(buscarAtendimentosParaAdicionarProfissionalDTO);
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

    public String gravarInsercaoGeral(InsercaoProfissionalEquipe insercaoProfissionalEquipe) throws ProjetoException {
        List<BuscarAtendimentosParaAdicionarProfissionalDTO> lista = buscarAtendimentosParaAdicionarProfissional(insercaoProfissionalEquipe);

        if(!lista.isEmpty()){

            GravarInsercaoProfissionalEquipeAtendimentoDTO gravarInsercaoDTO =
                    new GravarInsercaoProfissionalEquipeAtendimentoDTO(insercaoProfissionalEquipe, lista);

            if(gravarInsercaoProfissionalEquipeAtendimento(gravarInsercaoDTO)){
                return RetornoGravarInsercaoProfissionalAtendimento.SUCESSO_GRAVACAO.getSigla();
            }
            else{
                return RetornoGravarInsercaoProfissionalAtendimento.FALHA_GRAVACAO.getSigla();
            }
        }

        else{
            return RetornoGravarInsercaoProfissionalAtendimento.FALHA_PROFISSIONAL.getSigla();
        }

    }

    private boolean gravarInsercaoProfissionalEquipeAtendimento(GravarInsercaoProfissionalEquipeAtendimentoDTO gravarInsercaoDTO) throws ProjetoException {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.insercao_profissional_equipe_atendimento " +
                "(data_hora_operacao, codusuario_operacao, cod_programa, cod_grupo, cod_equipe, data_inicio, data_final, turno) " +
                "VALUES (current_timestamp, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try {
        	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
        			.getSessionMap().get("obj_usuario");
        	
        	con = ConnectionFactory.getConnection();
        	ps = con.prepareStatement(sql);
        	List<Integer> listaIdAtendimentos;
        	
        	if(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getTurno().equals(Turno.AMBOS.getSigla())) {
        		mapearPreparedStatementGravarInsercaoProfissionalEquipeAtendimento(gravarInsercaoDTO, user_session);
        		ps.setString(7, Turno.MANHA.getSigla());
        		
        		listaIdAtendimentos = retornaListaIdAtendimentoPeloTurno(Turno.MANHA.getSigla(), gravarInsercaoDTO.getListaAtendimentos());
        		
        		ResultSet rs = ps.executeQuery();

            	int id = 0;

            	if (rs.next()) {
                	id = rs.getInt("id");
            	}
            	GravarInsercaoAtendimento1DTO gravarAtendimento1 = new GravarInsercaoAtendimento1DTO(
                		gravarInsercaoDTO.getInsercaoProfissionalEquipe(), listaIdAtendimentos, id, con);
            	
            	retorno = gravarInsercaoAtendimento1(gravarAtendimento1);
            	id = 0;
            	
            	mapearPreparedStatementGravarInsercaoProfissionalEquipeAtendimento(gravarInsercaoDTO, user_session);
        		ps.setString(7, Turno.TARDE.getSigla());
        		
        		listaIdAtendimentos = retornaListaIdAtendimentoPeloTurno(Turno.TARDE.getSigla(), gravarInsercaoDTO.getListaAtendimentos());
        		
        		 rs = ps.executeQuery();
        		if (rs.next()) {
                	id = rs.getInt("id");
            	}
            	gravarAtendimento1 = new GravarInsercaoAtendimento1DTO(
                		gravarInsercaoDTO.getInsercaoProfissionalEquipe(), listaIdAtendimentos, id, con);
            	retorno = gravarInsercaoAtendimento1(gravarAtendimento1);
        	} 
        	else {
        		mapearPreparedStatementGravarInsercaoProfissionalEquipeAtendimento(gravarInsercaoDTO, user_session);
            	ps.setString(7, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getTurno());

            	listaIdAtendimentos = retornaListaIdAtendimentoPeloTurno(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getTurno(), gravarInsercaoDTO.getListaAtendimentos());
            	ResultSet rs = ps.executeQuery();

            	int id = 0;

            	if (rs.next()) {
                	id = rs.getInt("id");
            	}

            	GravarInsercaoAtendimento1DTO gravarAtendimento1 = new GravarInsercaoAtendimento1DTO(
            		gravarInsercaoDTO.getInsercaoProfissionalEquipe(), listaIdAtendimentos, id, con);

            	retorno = gravarInsercaoAtendimento1(gravarAtendimento1);
        	}
            if(retorno) {
                con.commit();
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
        return retorno;
    }
    
    private List<Integer> retornaListaIdAtendimentoPeloTurno(String turno, List<BuscarAtendimentosParaAdicionarProfissionalDTO> lista) {
    	
    	List<Integer> listaIdAtendimentos = new ArrayList<>();
    	for (BuscarAtendimentosParaAdicionarProfissionalDTO atendimentoParaAdicionarProfissional : lista) {
			if(atendimentoParaAdicionarProfissional.getTurno().equals(turno)) {
				listaIdAtendimentos.add(atendimentoParaAdicionarProfissional.getIdAtendimento());
			}
		}
    	return listaIdAtendimentos;
    }

	private void mapearPreparedStatementGravarInsercaoProfissionalEquipeAtendimento(
			GravarInsercaoProfissionalEquipeAtendimentoDTO gravarInsercaoDTO, FuncionarioBean user_session)
			throws SQLException {
		ps.setLong(1, user_session.getId());
		ps.setInt(2, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPrograma().getIdPrograma());
         
		if((VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo())) || (VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo().getIdGrupo()))){
			ps.setNull(3, Types.NULL);
		}
		else {
			ps.setInt(3, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getGrupo().getIdGrupo());
		}            
         
		if ((VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe())) || (VerificadorUtil.verificarSeObjetoNuloOuZero(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe().getCodEquipe()))){
			ps.setNull(4, Types.NULL);
		}
		else {
			ps.setInt(4, gravarInsercaoDTO.getInsercaoProfissionalEquipe().getEquipe().getCodEquipe());
		}
		ps.setDate(5, DataUtil.converterDateUtilParaDateSql(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoInicio()));
		if(VerificadorUtil.verificarSeObjetoNulo(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoFinal())){
			ps.setNull(6, Types.DATE);
		}
		else {
			ps.setDate(6, DataUtil.converterDateUtilParaDateSql(gravarInsercaoDTO.getInsercaoProfissionalEquipe().getPeriodoFinal()));
		}
	}

    private boolean gravarInsercaoAtendimento1(GravarInsercaoAtendimento1DTO gravarAtendimento1) throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.atendimentos1 " +
                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
                "VALUES (?, ?, ?, ?) RETURNING id_atendimentos1";

        try {
            PreparedStatement ps = gravarAtendimento1.getConexaoAuxiliar().prepareStatement(sql);

            List<Integer> listaInsercaoProfissionalEquipes = new ArrayList<>();
            int codigoAtendimento1 = 0;

            for (int i = 0; i < gravarAtendimento1.getListaAtendimentos().size(); i++) {

            	Integer idProcedimento = retornaIdProcedimentoDoAtendimento
            			(gravarAtendimento1.getListaAtendimentos().get(i), gravarAtendimento1.getConexaoAuxiliar());
            			
                ps.setLong(1, gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario().getId());
                ps.setInt(2, gravarAtendimento1.getListaAtendimentos().get(i));

				CboBean cboCompativel = new InsercaoPacienteDAO().retornaCboCompativelParaAgenda
						(gravarAtendimento1.getInsercaoProfissionalEquipe().getPeriodoInicio(), gravarAtendimento1.getInsercaoProfissionalEquipe().getFuncionario(), idProcedimento, gravarAtendimento1.getConexaoAuxiliar());
                
                ps.setInt(3, cboCompativel.getCodCbo());
                ps.setInt(4, idProcedimento);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    codigoAtendimento1 = rs.getInt("id_atendimentos1");
                }
                listaInsercaoProfissionalEquipes.add(codigoAtendimento1);
            }

            GravarInsercaoProfissionalEquipeAtendimento1DTO gravarInsercao1 = new GravarInsercaoProfissionalEquipeAtendimento1DTO(
                    listaInsercaoProfissionalEquipes, gravarAtendimento1.getCodInsercaoProfissionalEquipeAtendimento(), con,
                    gravarAtendimento1.getInsercaoProfissionalEquipe());

            retorno = gravarInsercaoProfissionalEquipeAtendimento1(gravarInsercao1);
        } catch (SQLException sqle) {
        	gravarAtendimento1.getConexaoAuxiliar().rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			gravarAtendimento1.getConexaoAuxiliar().rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
        return retorno;
    }
    
	public Integer retornaIdProcedimentoDoAtendimento(Integer idAtendimento,
			Connection conAuxiliar) throws ProjetoException, SQLException {

        String sql = "(select cod_procedimento from hosp.programa \n" + 
                "where programa.id_programa =\n" + 
                "(select codprograma from hosp.atendimentos where atendimentos.id_atendimento = ? ))"; 
        
        Integer idProcedimento = null;

		try {
			PreparedStatement ps = conAuxiliar.prepareStatement(sql);
			ps.setInt(1, idAtendimento);
				
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				idProcedimento = rs.getInt("cod_procedimento");
			}

		} catch (SQLException sqle) {
			conAuxiliar.rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			conAuxiliar.rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
		return idProcedimento;
	}
	

    private boolean gravarInsercaoProfissionalEquipeAtendimento1(GravarInsercaoProfissionalEquipeAtendimento1DTO gravarInsercao1) throws ProjetoException, SQLException {

        Boolean retorno = false;

        String sql = "INSERT INTO adm.insercao_profissional_equipe_atendimento_1 " +
                "( id_atendimentos1, id_insercao_profissional_equipe_atendimento, id_profissional ) " +
                "VALUES (?, ?, ?) ;";

        try {

            PreparedStatement stm = null; 	
            stm = gravarInsercao1.getConexaoAuxiliar().prepareStatement(sql);

            for (int i = 0; i < gravarInsercao1.getListaInsercao().size(); i++) {
                stm.setInt(1, gravarInsercao1.getListaInsercao().get(i));
                stm.setInt(2, gravarInsercao1.getCodInsercaoProfissionalEquipeAtendimento());
                stm.setLong(3, gravarInsercao1.getInsercaoProfissionalEquipe().getFuncionario().getId());
                stm.executeUpdate();
            }

            retorno = true;
        } catch (SQLException sqle) {
        	gravarInsercao1.getConexaoAuxiliar().rollback();
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
		} catch (Exception ex) {
			gravarInsercao1.getConexaoAuxiliar().rollback();
			throw new ProjetoException(ex, this.getClass().getName());
		} 
        return retorno;
    }

    public List<InsercaoProfissionalEquipe> listarInsercoesRealizadas() throws ProjetoException {

        List<InsercaoProfissionalEquipe> lista = new ArrayList<>();

        String sql = "SELECT distinct f.descfuncionario, a.data_inicio, a.data_final, p.descprograma, g.descgrupo, e.descequipe, " +
                "CASE WHEN a.turno = 'A' THEN 'AMBOS' WHEN a.turno = 'T' THEN 'TARDE' " +
                "WHEN a.turno = 'M' THEN 'MANHÃ' END AS turno " +
                "FROM adm.insercao_profissional_equipe_atendimento a " +
                "JOIN adm.insercao_profissional_equipe_atendimento_1 a1 ON (a.id = a1.id_insercao_profissional_equipe_atendimento) " +
                "JOIN acl.funcionarios f ON (a1.id_profissional = f.id_funcionario) " +
                "LEFT JOIN hosp.programa p ON (a.cod_programa = p.id_programa) " +
                "LEFT JOIN hosp.grupo g ON (a.cod_grupo = g.id_grupo) " +
                "LEFT JOIN hosp.equipe e ON (a.cod_equipe = e.id_equipe);";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                InsercaoProfissionalEquipe insercaoProfissionalEquipe = new InsercaoProfissionalEquipe();
                insercaoProfissionalEquipe.getFuncionario().setNome(rs.getString("descfuncionario"));
                insercaoProfissionalEquipe.setPeriodoInicio(rs.getDate("data_inicio"));
                insercaoProfissionalEquipe.setPeriodoFinal(rs.getDate("data_final"));
                insercaoProfissionalEquipe.getPrograma().setDescPrograma(rs.getString("descprograma"));
                insercaoProfissionalEquipe.getGrupo().setDescGrupo(rs.getString("descgrupo"));
                insercaoProfissionalEquipe.getEquipe().setDescEquipe(rs.getString("descequipe"));
                insercaoProfissionalEquipe.setTurno(rs.getString("turno"));

                lista.add(insercaoProfissionalEquipe);
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

}
