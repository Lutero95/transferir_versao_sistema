package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.administrativo.model.InsercaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.RemocaoProfissionalEquipe;
import br.gov.al.maceio.sishosp.administrativo.model.SubstituicaoProfissional;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.BuscaEvolucao;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;

import javax.faces.context.FacesContext;

public class AtendimentoDAO {
	Connection con = null;
	PreparedStatement ps = null;

	FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_usuario");

	public Boolean verificarSeCboEhDoProfissionalPorEquipe(List<AtendimentoBean> lista) {

		Boolean retorno = false;
		ArrayList<Integer> listaAux = new ArrayList<>();
		int valor = 0;

		try {

			con = ConnectionFactory.getConnection();
			for (int i = 0; i < lista.size(); i++) {
				String sql = "SELECT p.id_cbo " + "FROM hosp.proc_cbo p " + "LEFT JOIN hosp.cbo c ON (c.id = p.id_cbo) "
						+ "LEFT JOIN acl.funcionarios f ON (f.codcbo = p.id_cbo) " + "WHERE 1=1 " + // p.id_proc =
																									// f.codprocedimentopadrao
						" AND f.id_funcionario = ? AND p.id_proc = ?;";

				PreparedStatement stm = con.prepareStatement(sql);

				stm.setLong(1, lista.get(i).getFuncionario().getId());
				stm.setInt(2, lista.get(i).getProcedimento().getIdProc());

				ResultSet rs = stm.executeQuery();

				valor = 0;

				while (rs.next()) {
					valor = rs.getInt("id_cbo");

					if (valor > 0) {
						listaAux.add(lista.get(i).getProcedimento().getIdProc());
					}

				}

			}

			if (lista.size() == listaAux.size()) {
				retorno = true;
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
			return retorno;
		}
	}

	public String gerarMensagemSeCboNaoEhPermitidoParaProcedimento(List<AtendimentoBean> lista) {

		String retorno = "";
		Boolean permiteAtendimento = false;

		try {

			con = ConnectionFactory.getConnection();
			for (int i = 0; i < lista.size(); i++) {
				permiteAtendimento = false;

				String sql = "SELECT p.id_cbo " + "FROM hosp.proc_cbo p " + "LEFT JOIN hosp.cbo c ON (c.id = p.id_cbo) "
						+ "LEFT JOIN acl.funcionarios f ON (f.codcbo = p.id_cbo) " + "WHERE 1=1 " + // p.id_proc =
																									// f.codprocedimentopadrao
						" AND f.id_funcionario = ? AND p.id_proc = ?;";

				PreparedStatement stm = con.prepareStatement(sql);

				stm.setLong(1, lista.get(i).getFuncionario().getId());
				stm.setInt(2, lista.get(i).getProcedimento().getIdProc());

				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					permiteAtendimento = true;
				}

				if (!permiteAtendimento) {
					retorno = retorno + "O procedimento: " + lista.get(i).getProcedimento().getNomeProc()
							+ " não pode ser atendimento pelo CBO: " + lista.get(i).getCbo().getDescCbo() + ".\n";
				}

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
			return retorno;
		}
	}

	public Boolean verificarSeCboEhDoProfissionalPorProfissional(Long idProfissional, Integer idProcedimento) {

		Boolean retorno = false;

		String sql = "select p.id_cbo from hosp.proc_cbo p left join acl.funcionarios f on (p.id_proc = ?) left join hosp.cbo c on (c.id = p.id_cbo)"
				+ " where p.id_proc = f.codprocedimentopadrao and c.id = p.id_cbo and f.id_funcionario = ?";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idProcedimento);
			stm.setLong(2, idProfissional);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				retorno = true;
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
			return retorno;
		}
	}

	public Boolean realizaAtendimentoProfissional(FuncionarioBean funcionario, AtendimentoBean atendimento)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql = "update hosp.atendimentos1 set codprocedimento = ?, "
					+ "dtaatendido = current_timestamp, id_situacao_atendimento = ?, evolucao = ? "
					+ " where id_atendimento = ? and codprofissionalatendimento = ?";

			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, atendimento.getProcedimento().getIdProc());
			stmt.setInt(2, atendimento.getSituacaoAtendimento().getId());
			stmt.setString(3, atendimento.getEvolucao());
			stmt.setInt(4, atendimento.getId());
			stmt.setLong(5, funcionario.getId());

			stmt.executeUpdate();

			String sql2 = "update hosp.atendimentos set situacao = 'F' " + " where id_atendimento = ?";

			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setInt(1, atendimento.getId());

			stmt2.executeUpdate();

			con.commit();

			alterou = true;

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
		return alterou;
	}

	public Boolean realizaAtendimentoEquipe(List<AtendimentoBean> lista, Integer idLaudo, Integer grupoAvaliacao,  List<AtendimentoBean> listaExcluir, Integer idAtendimento)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			GerenciarPacienteDAO gerenciarPacienteDAO = new GerenciarPacienteDAO();
			
			ArrayList<SubstituicaoProfissional> listaSubstituicao =  gerenciarPacienteDAO.listaAtendimentosQueTiveramSubstituicaoProfissionalEmUmAtendimento(idAtendimento, con) ;
			
			ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosAtendimentoEquipe = new ArrayList<>();
			
			ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosAtendimentoEquipe = new ArrayList<>();
			
			
			for (int i = 0; i < lista.size(); i++) {
				ArrayList<InsercaoProfissionalEquipe> listaProfissionaisInseridosAtendimentoEquipeAux = gerenciarPacienteDAO.listaAtendimentosQueTiveramInsercaoProfissionalAtendimentoEquipePeloIdAtendimentoCodProfissionalAtendimento(idAtendimento, lista.get(i).getFuncionario().getId(), con) ;
				listaProfissionaisInseridosAtendimentoEquipe.addAll(listaProfissionaisInseridosAtendimentoEquipeAux);
				
				ArrayList<RemocaoProfissionalEquipe> listaProfissionaisRemovidosAtendimentoEquipeAux = gerenciarPacienteDAO.listaAtendimentosQueTiveramRemocaoProfissionalAtendimentoEquipePeloIdAtendimentoCodProfissionalAtendimento(idAtendimento, lista.get(i).getFuncionario().getId(), con) ;
				listaProfissionaisRemovidosAtendimentoEquipe.addAll(listaProfissionaisRemovidosAtendimentoEquipeAux);				
			}
	
			
			if (!gerenciarPacienteDAO.apagarAtendimentosDeUmAtendimento (idAtendimento, con,  listaSubstituicao, listaExcluir, listaProfissionaisInseridosAtendimentoEquipe, listaProfissionaisRemovidosAtendimentoEquipe)) {
				con.close();
				return alterou;
			}			
			
			
			for (int i = 0; i < lista.size(); i++) {


				String sql2 = "INSERT INTO hosp.atendimentos1(codprofissionalatendimento, id_atendimento, "
						+ " cbo, codprocedimento, id_situacao_atendimento, evolucao, perfil_avaliacao, horario_atendimento) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";
				if (VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getSituacaoAtendimentoAnterior().getId())) {
				PreparedStatement stmt2 = con.prepareStatement(sql2);
				stmt2.setLong(1, lista.get(i).getFuncionario().getId());
				stmt2.setInt(2, idAtendimento);
				if (VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getCbo().getCodCbo())) {
					stmt2.setNull(3, Types.NULL);
				} else {
					stmt2.setInt(3, lista.get(i).getCbo().getCodCbo());
				}

				if (VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getProcedimento().getIdProc())) {
					stmt2.setNull(4, Types.NULL);
				} else {
					stmt2.setInt(4, lista.get(i).getProcedimento().getIdProc());
				}

				if (!VerificadorUtil.verificarSeObjetoNuloOuZero(lista.get(i).getSituacaoAtendimento().getId()))
					stmt2.setInt(5, lista.get(i).getSituacaoAtendimento().getId());
				else
					stmt2.setNull(5, Types.NULL);
				stmt2.setString(6, lista.get(i).getEvolucao());
				stmt2.setString(7, lista.get(i).getPerfil());
				if (lista.get(i).getHorarioAtendimento() != null)
					stmt2.setTime(8, DataUtil.retornarHorarioEmTime(lista.get(i).getHorarioAtendimento()));
				else
					stmt2.setNull(8, Types.NULL);
				stmt2.executeUpdate();
				}

			}

			if (!VerificadorUtil.verificarSeObjetoNuloOuZero(grupoAvaliacao)) {
				String sql3 = "update hosp.atendimentos set cod_laudo = ?, grupo_avaliacao = ? where id_atendimento = ?";

				PreparedStatement stmt3 = con.prepareStatement(sql3);
				stmt3.setInt(1, idLaudo);
				stmt3.setInt(2, grupoAvaliacao);
				stmt3.setInt(3, lista.get(0).getId());
				stmt3.executeUpdate();
			}
			
			AtendimentoDAO aDao = new AtendimentoDAO();
			if (listaSubstituicao.size()>0) {
			String sql6 = "insert into adm.substituicao_funcionario (id_atendimentos1,id_afastamento_funcionario,\n" + 
					"id_funcionario_substituido, id_funcionario_substituto, usuario_acao, data_hora_acao)	\n" + 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and a11.codprofissionalatendimento=? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?,?,?, current_timestamp)";
			PreparedStatement ps6 = null;
			ps6 = con.prepareStatement(sql6);
			for (int i = 0; i < listaSubstituicao.size(); i++) {
				String sql8 = "update hosp.atendimentos1 set codprofissionalatendimento=?, cbo=? where atendimentos1.id_atendimentos1 = (\n" +
						"select distinct a1.id_atendimentos1 from hosp.paciente_instituicao pi\n" + 
						"join hosp.atendimentos a on a.id_paciente_instituicao = pi.id\n" + 
						"join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento\n" + 
						"where a1.id_atendimento=? and a.dtaatende=? and a1.codprofissionalatendimento = ? and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N' limit 1)";
				PreparedStatement ps8 = null;
				ps8 = con.prepareStatement(sql8);
				ps8.setLong(1, listaSubstituicao.get(i).getFuncionario().getId());
				ps8.setLong(2, listaSubstituicao.get(i).getFuncionario().getCbo().getCodCbo());
				ps8.setLong(3, idAtendimento);
				ps8.setDate(4,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps8.setLong(5, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps8.execute();
				
				ps6 = null;
				ps6 = con.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaSubstituicao.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(3, listaSubstituicao.get(i).getAfastamentoProfissional().getId());
				ps6.setLong(4, listaSubstituicao.get(i).getAfastamentoProfissional().getFuncionario().getId());
				ps6.setLong(5, listaSubstituicao.get(i).getFuncionario().getId());
				ps6.setLong(6, listaSubstituicao.get(i).getUsuarioAcao().getId());
				ps6.execute();
			}
			}
			
			if (listaProfissionaisInseridosAtendimentoEquipe.size()>0) {			
			String sql6 = "insert into adm.insercao_profissional_equipe_atendimento_1 (id_atendimentos1,id_insercao_profissional_equipe_atendimento, id_profissional) "+ 
					"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
					"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
					"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'  limit 1),?,?)";
			PreparedStatement ps6 = null;
			ps6 = null;
			ps6 = con.prepareStatement(sql6);
			for (int i = 0; i < listaProfissionaisInseridosAtendimentoEquipe.size(); i++) {
				/*
				String sql8 = "INSERT INTO hosp.atendimentos1 " +
		                "(codprofissionalatendimento, id_atendimento, cbo, codprocedimento) " +
		                "VALUES (?, (select id_atendimento from hosp.atendimentos aa " + 
		                " where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=? limit 1), ?, (select cod_procedimento from hosp.programa " + 
		                "where programa.id_programa = ? )) RETURNING id_atendimentos1";

				PreparedStatement ps8 = null;
				ps8 = con.prepareStatement(sql8);
				ps8.setLong(1, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps8.setDate(2,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps8.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps8.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getCbo().getCodCbo());
				ps8.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps8.execute();
				*/
				ps6 = null;
				ps6 = con.prepareStatement(sql6);
				
				ps6.setDate(1,new java.sql.Date( listaProfissionaisInseridosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
				ps6.setLong(2, listaProfissionaisInseridosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
				ps6.setLong(3, listaProfissionaisInseridosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
				ps6.setLong(4, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps6.setLong(5, listaProfissionaisInseridosAtendimentoEquipe.get(i).getId());
				ps6.setLong(6, listaProfissionaisInseridosAtendimentoEquipe.get(i).getFuncionario().getId());
				ps6.execute();
			}
			}
			
			
			if (listaProfissionaisRemovidosAtendimentoEquipe.size()>0) {			
				String sql6 = "insert into adm.remocao_profissional_equipe_atendimento_1 (id_atendimentos1,id_remocao_profissional_equipe_atendimento, id_profissional) "+ 
						"values ((select id_atendimentos1 from hosp.atendimentos1\n" + 
						"a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento\n" + 
						"where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N' limit 1),?,?)";
				PreparedStatement ps6 = null;
				ps6 = null;
				ps6 = con.prepareStatement(sql6);
				for (int i = 0; i < listaProfissionaisRemovidosAtendimentoEquipe.size(); i++) {
					AtendimentoBean atendimento = new AtendimentoBean();
					atendimento.setDataAtendimentoInicio( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento());
					atendimento.getPrograma().setIdPrograma(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					atendimento.getGrupo().setIdGrupo(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					atendimento.getEquipe().setCodEquipe(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getEquipe().getCodEquipe());
					atendimento.getFuncionario().setId(listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					if (aDao.verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(atendimento) ) {	
					String sql8 = "update hosp.atendimentos1 set excluido='S', usuario_exclusao=?, data_hora_exclusao=current_timestamp where id_atendimentos1=(select id_atendimentos1 from hosp.atendimentos1 " + 
							"	a11 join hosp.atendimentos aa on aa.id_atendimento = a11.id_atendimento " + 
							"	 where aa.dtaatende=? and  aa.codprograma=? and aa.codgrupo=?  and a11.codprofissionalatendimento =? and coalesce(aa.situacao, 'A')<> 'C'	and coalesce(a11.excluido, 'N' )= 'N'  limit 1)" ;
			               

					PreparedStatement ps8 = null;
					ps8 = con.prepareStatement(sql8);
					ps8.setLong(1, user_session.getId());
					ps8.setDate(2,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps8.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps8.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps8.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps8.execute();
					listaProfissionaisRemovidosAtendimentoEquipe = null;
					ps6 = con.prepareStatement(sql6);
					
					ps6.setDate(1,new java.sql.Date( listaProfissionaisRemovidosAtendimentoEquipe.get(i).getDataAtendimento().getTime()));
					ps6.setLong(2, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getPrograma().getIdPrograma());
					ps6.setLong(3, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getGrupo().getIdGrupo());
					ps6.setLong(4, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.setLong(5, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getId());
					ps6.setLong(6, listaProfissionaisRemovidosAtendimentoEquipe.get(i).getFuncionario().getId());
					ps6.execute();
				}
				}			
		}
			

			con.commit();

			alterou = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return alterou;
		}
	}

	public Boolean insereProfissionalParaRealizarAtendimentoNaEquipe(AtendimentoBean atendimento, FuncionarioBean novoProfissional)
			throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

				String sql2 = "INSERT INTO hosp.atendimentos1( id_atendimento, codprofissionalatendimento, "
						+ " cbo, codprocedimento) VALUES ( ?, ?, ?,(select coalesce(cod_procedimento, null) from hosp.programa where programa.id_programa=?));";

				PreparedStatement stmt2 = con.prepareStatement(sql2);
				stmt2.setLong(1, atendimento.getId());
				stmt2.setLong(2, novoProfissional.getId());
				if (VerificadorUtil.verificarSeObjetoNuloOuZero(novoProfissional.getCbo().getCodCbo())) {
					stmt2.setNull(3, Types.NULL);
				} else {
					stmt2.setInt(3, novoProfissional.getCbo().getCodCbo());
				}
				
					stmt2.setInt(4,atendimento.getPrograma().getIdPrograma());

				stmt2.executeUpdate();

			

			con.commit();

			alterou = true;

		}catch(

	Exception ex)
	{
		ex.printStackTrace();
		throw new RuntimeException(ex);
	}finally
	{
		try {
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return alterou;
	}
	}

	public Boolean limpaAtendimentoProfissional(AtendimentoBean atendimento) throws ProjetoException {
		boolean alterou = false;
		con = ConnectionFactory.getConnection();
		try {

			String sql = "update hosp.atendimentos1 set dtaatendido = null, situacao = ? "
					+ " where id_atendimento = ?";
//alterar para id_atendimento1 para o caso de profissional 
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, "");
			stmt.setInt(2, atendimento.getId());

			stmt.executeUpdate();

			con.commit();

			alterou = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return alterou;
		}
	}

	public List<AtendimentoBean> carregaAtendimentos(AtendimentoBean atendimento, String campoBusca, String tipo)
			throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns, a.turno, a.codmedico, f.descfuncionario,"
				+ " a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento,"
				+ " a.codequipe, e.descequipe, a.avaliacao,  "
				+ " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe,"

				+ " case when "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is null and coalesce(a1.excluido,'N')='N') =  "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and coalesce(a1.excluido,'N')='N') "
				+ " then 'Atendimento Não Informado' " + " when "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and situacao is not null and coalesce(a1.excluido,'N')='N') = "
				+ " (select count(*) from hosp.atendimentos1 a1 where a1.id_atendimento = a.id_atendimento and coalesce(a1.excluido,'N')='N') "
				+ " then 'Atendimento Informado' " + " else 'Atendimento Informado Parcialmente' " + " end as situacao "

				+ " from hosp.atendimentos a" + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
				+ " left join acl.funcionarios f on (f.id_funcionario = a.codmedico)"
				+ " left join hosp.programa pr on (pr.id_programa = a.codprograma)"
				+ " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)"
				+ " left join hosp.equipe e on (e.id_equipe = a.codequipe)"
				+ " where a.dtaatende >= ? and a.dtaatende <= ? and a.cod_unidade = ? and coalesce(a.situacao, '')<> 'C'";

		if ((atendimento.getPrograma() != null) && (atendimento.getPrograma().getIdPrograma() != null)) {
			sql = sql + " and  a.codprograma = ?";
		}
		if ((atendimento.getGrupo() != null) && (atendimento.getGrupo().getIdGrupo() != null)) {
			sql = sql + " and  a.codgrupo = ?";
		}

		if (tipo.equals("nome")) {
			sql = sql + " and p.nome like ?";
		} else if (tipo.equals("cpf")) {
			sql = sql + " and p.cpf like ?";
		} else if (tipo.equals("cns")) {
			sql = sql + " and p.cns like ?";
		} else if (tipo.equals("prontuario")) {
			sql = sql + " and p.id_paciente = ?";
		} else if (tipo.equals("matricula")) {
			sql = sql + " and p.matricula like ?";
		}

		sql = sql + " order by a.dtaatende";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i = 4;
			stm.setDate(1, new java.sql.Date(atendimento.getDataAtendimentoInicio().getTime()));
			stm.setDate(2, new java.sql.Date(atendimento.getDataAtendimentoFinal().getTime()));
			stm.setInt(3, user_session.getUnidade().getId());

			if ((atendimento.getPrograma() != null) && (atendimento.getPrograma().getIdPrograma() != null)) {
				stm.setInt(i, atendimento.getPrograma().getIdPrograma());
				i = i + 1;
			}
			if ((atendimento.getGrupo() != null) && (atendimento.getGrupo().getIdGrupo() != null)) {
				stm.setInt(i, atendimento.getGrupo().getIdGrupo());
				i = i + 1;
			}

			if (!campoBusca.equals(null)) {
				if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns")) || (tipo.equals("matricula")))
					stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
				else
					stm.setInt(i, Integer.valueOf(campoBusca));
				i = i + 1;
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.setId(rs.getInt("id_atendimento"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				at.getPaciente().setNome(rs.getString("nome"));
				at.getPaciente().setCns(rs.getString("cns"));
				at.setTurno(rs.getString("turno"));
				at.getFuncionario().setId(rs.getLong("codmedico"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				at.getPrograma().setDescPrograma(rs.getString("descprograma"));
				at.setSituacao(rs.getString("situacao"));
				at.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
				at.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				at.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				at.getEquipe().setDescEquipe(rs.getString("descequipe"));
				at.setEhEquipe(rs.getString("ehEquipe"));
				at.setAvaliacao(rs.getBoolean("avaliacao"));

				lista.add(at);
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
		return lista;
	}
	
	
	public Integer retornaQuantidadeDePendenciasAnterioresDeEvolucao
		(Integer idUnidade, Long codigoProfissionalAtendimento) throws ProjetoException {

		Integer quantidadeDePendenciasAnterioresDeEvolucao = 0;
		String sql = "select count(*) qtd from hosp.atendimentos1 a1 " + 
				"join hosp.atendimentos a on a.id_atendimento = a1.id_atendimento " + 
				"join hosp.pacientes pac on pac.id_paciente = a.codpaciente " + 
				"join acl.funcionarios f on f.id_funcionario = a1.codprofissionalatendimento " + 
				"join hosp.especialidade e on e.id_especialidade = f.codespecialidade " + 
				"JOIN hosp.unidade u ON u.id = ? " + 
				"JOIN hosp.empresa emp ON emp.cod_empresa = u.cod_empresa " + 
				"join hosp.config_evolucao_unidade_programa_grupo ceu on ceu.codunidade = u.id " + 
				"join hosp.programa p on p.id_programa = a.codprograma and ceu.codprograma = p.id_programa " + 
				"join hosp.grupo g on g.id_grupo = a.codgrupo and ceu.codgrupo = g.id_grupo " + 
				"where a.presenca='S' and a1.evolucao is null " + 
				"and a.codprograma = ceu.codprograma " + 
				"and a.codgrupo = ceu.codgrupo " + 
				"and a.dtaatende>=ceu.inicio_evolucao " + 
				"and a.dtaatende<current_date " + 
				"and a1.codprofissionalatendimento = ?" + 
				"and coalesce(a.situacao,'A')<>'C'" + 
				"and coalesce(a1.excluido,'N' )='N'";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, idUnidade);
			preparedStatement.setLong(2, codigoProfissionalAtendimento);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				quantidadeDePendenciasAnterioresDeEvolucao = rs.getInt("qtd");
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
		return quantidadeDePendenciasAnterioresDeEvolucao;
	}

	public List<AtendimentoBean> carregaAtendimentosDoProfissionalNaEquipe(AtendimentoBean atendimento,
			String campoBusca, String tipo, String buscaEvolucao, String buscaTurno, boolean listaEvolucoesPendentes) throws ProjetoException {

		FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("obj_funcionario");

		String sql = "select distinct  coalesce(a.presenca,'N') presenca, a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, p.cns,p.cpf,  a.turno, a.codmedico, f.descfuncionario,"
				+ " a.codprograma, pr.descprograma, a.codtipoatendimento, t.desctipoatendimento,"
				+ " a.codequipe, e.descequipe, a.codgrupo, g.descgrupo, a.avaliacao, parm.bloqueia_por_pendencia_evolucao_anterior, "
				+ " case when t.equipe_programa is true then 'Sim' else 'Não' end as ehEquipe, a.cod_unidade, "

				+ " case\n" + "		when exists (\n" + "		select\n" + "			a11.id_atendimento\n"
				+ "		from\n" + "			hosp.atendimentos1 a11\n" + "		where\n"
				+ "			a11.id_atendimento = a.id_atendimento\n"
				+ "			and a11.codprofissionalatendimento=?\n"
				+ "			and a11.evolucao is null and coalesce(a11.excluido,'N')='N')  then 'Evolução Não Realizada'\n"
				+ "		 else 'Evolução Realizada'\n" + "	end as situacao "

				+ " from hosp.atendimentos a" + " left join hosp.pacientes p on (p.id_paciente = a.codpaciente)"
				+ " JOIN hosp.atendimentos1 a1 ON (a.id_atendimento = a1.id_atendimento)"
				+ " left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento)"
				+ " left join hosp.programa pr on (pr.id_programa = a.codprograma)"
				+ " left join hosp.grupo g on (g.id_grupo = a.codgrupo)"
				+ " left join hosp.tipoatendimento t on (t.id = a.codtipoatendimento)"
				+ " left join hosp.equipe e on (e.id_equipe = a.codequipe)"
				+ " left join hosp.parametro parm on (parm.codunidade = a.cod_unidade) and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N' ";

		if(listaEvolucoesPendentes) {
			sql +=  " join hosp.config_evolucao_unidade_programa_grupo ceu on ceu.codunidade = a.cod_unidade and ceu.codprograma = a.codprograma and ceu.codgrupo = a.codgrupo  "
					+ " where a.dtaatende >= ceu.inicio_evolucao and a.dtaatende<=current_date and coalesce(a.presenca,'N')='S' and a1.evolucao is null";
		}
		
		else
			sql += " where a.dtaatende >= ? and a.dtaatende <= ? ";
		
		sql = sql + " and a.cod_unidade = ? and coalesce(a1.excluido,'N')='N' and coalesce(a.situacao,'A')<>'C'"
				+ " and exists (select id_atendimento from hosp.atendimentos1 a11 "
				+ " where a11.codprofissionalatendimento=? and a11.id_atendimento = a.id_atendimento and coalesce(a11.excluido,'N')='N') and a1.codprofissionalatendimento=?";

		if ((atendimento.getPrograma() != null) && (atendimento.getPrograma().getIdPrograma() != null)) {
			sql = sql + " and  a.codprograma = ?";
		}
		if ((atendimento.getGrupo() != null) && (atendimento.getGrupo().getIdGrupo() != null)) {
			sql = sql + " and  a.codgrupo = ?";
		}

		if (buscaEvolucao.equals(BuscaEvolucao.COM_EVOLUCAO.getSigla())) {
			sql = sql + " and a1.evolucao IS NOT NULL ";
		}
		if (buscaEvolucao.equals(BuscaEvolucao.SEM_EVOLUCAO.getSigla())) {
			sql = sql + " and a1.evolucao IS NULL ";
		}

		if (!buscaTurno.equals("A")) {
			sql = sql + " AND a.turno=? ";
		}

		if (tipo.equals("nome")) {
			sql = sql + " and p.nome like ?";
		} else if (tipo.equals("cpf")) {
			sql = sql + " and p.cpf like ?";
		} else if (tipo.equals("cns")) {
			sql = sql + " and p.cns like ?";
		} else if (tipo.equals("prontuario")) {
			sql = sql + " and p.id_paciente = ?";
		} else if (tipo.equals("matricula")) {
			sql = sql + " and p.matricula like ?";
		}

		sql = sql + " order by presenca desc, a.dtaatende, p.nome";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			int i;
			if(listaEvolucoesPendentes) {
				stm.setLong(1, user_session.getId());
				stm.setInt(2, user_session.getUnidade().getId());
				stm.setLong(3, user_session.getId());
				stm.setLong(4, user_session.getId());
				i = 5;
			}
			else {
				stm.setLong(1, user_session.getId());
				stm.setDate(2, new java.sql.Date(atendimento.getDataAtendimentoInicio().getTime()));
				stm.setDate(3, new java.sql.Date(atendimento.getDataAtendimentoFinal().getTime()));
				stm.setInt(4, user_session.getUnidade().getId());
				stm.setLong(5, user_session.getId());
				stm.setLong(6, user_session.getId());
				i = 7;
			}

			if ((atendimento.getPrograma() != null) && (atendimento.getPrograma().getIdPrograma() != null)) {
				stm.setInt(i, atendimento.getPrograma().getIdPrograma());
				i = i + 1;
			}
			if ((atendimento.getGrupo() != null) && (atendimento.getGrupo().getIdGrupo() != null)) {
				stm.setInt(i, atendimento.getGrupo().getIdGrupo());
				i = i + 1;
			}

			if (!buscaTurno.equals("A")) {
				stm.setString(i, buscaTurno);
				i++;
			}

			if (campoBusca != null) {
				if (!campoBusca.equals(null)) {
					if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns"))
							|| (tipo.equals("matricula")))
						stm.setString(i, "%" + campoBusca.toUpperCase() + "%");
					else
						stm.setInt(i, Integer.valueOf(campoBusca));
					i = i + 1;
				}
			}

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean atendimentoBean = new AtendimentoBean();
				atendimentoBean.setPresenca(rs.getString("presenca"));
				atendimentoBean.setId(rs.getInt("id_atendimento"));
				atendimentoBean.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimentoBean.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				atendimentoBean.getPaciente().setNome(rs.getString("nome"));
				atendimentoBean.getPaciente().setCns(rs.getString("cns"));
				atendimentoBean.getPaciente().setCpf(rs.getString("cpf"));
				atendimentoBean.setTurno(rs.getString("turno"));
				atendimentoBean.getFuncionario().setId(rs.getLong("codmedico"));
				atendimentoBean.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimentoBean.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimentoBean.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimentoBean.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				atendimentoBean.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimentoBean.setSituacao(rs.getString("situacao"));
				atendimentoBean.getTipoAt().setIdTipo(rs.getInt("codtipoatendimento"));
				atendimentoBean.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				atendimentoBean.getEquipe().setCodEquipe(rs.getInt("codequipe"));
				atendimentoBean.getEquipe().setDescEquipe(rs.getString("descequipe"));
				atendimentoBean.setEhEquipe(rs.getString("ehEquipe"));
				atendimentoBean.setAvaliacao(rs.getBoolean("avaliacao"));
				atendimentoBean.getUnidade().setId(rs.getInt("cod_unidade"));
				atendimentoBean.getUnidade().getParametro().setBloqueiaPorPendenciaEvolucaoAnterior
					(rs.getBoolean("bloqueia_por_pendencia_evolucao_anterior"));

				lista.add(atendimentoBean);
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
		return lista;
	}

	public AtendimentoBean listarAtendimentoProfissionalPorId(int id) throws ProjetoException {

		AtendimentoBean atendimento = new AtendimentoBean();
		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, a.codmedico, f.descfuncionario, a1.codprocedimento, "
				+ "pr.nome as procedimento, a1.id_situacao_atendimento, sa.descricao, sa.atendimento_realizado, a1.evolucao, a.avaliacao, a.cod_laudo, a.grupo_avaliacao, a.codprograma "
				+ "from hosp.atendimentos a " + "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento "
				+ "left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento "
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario = a.codmedico) "
				+ "left join hosp.programa on (programa.id_programa = a.codprograma) "
				+ "left join hosp.proc pr on (pr.id = coalesce(a1.codprocedimento, programa.cod_procedimento)) " + "where a.id_atendimento = ? and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N'";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimento.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				atendimento.getPaciente().setNome(rs.getString("nome"));
				atendimento.getProcedimento().setIdProc(rs.getInt("codprocedimento"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getFuncionario().setId(rs.getLong("codmedico"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				
				atendimento.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimento().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				atendimento.getSituacaoAtendimentoAnterior().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimentoAnterior().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimentoAnterior().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				
				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setAvaliacao(rs.getBoolean("avaliacao"));
				atendimento.getInsercaoPacienteBean().getLaudo().setId(rs.getInt("cod_laudo"));
				atendimento.setGrupoAvaliacao(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				atendimento.setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
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
		return atendimento;
	}

	public AtendimentoBean listarAtendimentoProfissionalPaciente(int id) throws ProjetoException {

		AtendimentoBean atendimento = new AtendimentoBean();
		String sql = "select a.id_atendimento, a.dtaatende, a.codpaciente, p.nome, a.codmedico, f.descfuncionario, a1.codprocedimento, "
				+ "pr.nome as procedimento, a1.id_situacao_atendimento, sa.descricao, sa.atendimento_realizado, a1.evolucao, a.avaliacao, "
				+ "a.cod_laudo, a.grupo_avaliacao, a.codprograma, pro.descprograma, coalesce(a.presenca,'N') presenca, "
				+ " a.codgrupo, g.descgrupo from hosp.atendimentos a "
				+ "join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento "
				+ "left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento "
				+ "left join hosp.programa pro on (pro.id_programa = a.codprograma)"
				+ "left join hosp.grupo g on (g.id_grupo = a.codgrupo)"
				+ "left join hosp.pacientes p on (p.id_paciente = a.codpaciente) "
				+ "left join acl.funcionarios f on (f.id_funcionario =a1.codprofissionalatendimento) "
				+ "left join hosp.proc pr on (pr.id = a1.codprocedimento) "
				+ "where a.id_atendimento = ? and a1.codprofissionalatendimento=? and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N'";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, id);
			stm.setLong(2, user_session.getId());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				atendimento.getPaciente().setId_paciente(rs.getInt("codpaciente"));
				atendimento.getPaciente().setNome(rs.getString("nome"));
				atendimento.getProcedimento().setIdProc(rs.getInt("codprocedimento"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getFuncionario().setId(rs.getLong("codmedico"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimento().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setAvaliacao(rs.getBoolean("avaliacao"));
				atendimento.getInsercaoPacienteBean().getLaudo().setId(rs.getInt("cod_laudo"));
				atendimento.getPrograma().setIdPrograma(rs.getInt("codprograma"));
				atendimento.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimento.getGrupo().setIdGrupo(rs.getInt("codgrupo"));
				atendimento.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimento.setGrupoAvaliacao(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_avaliacao"), con));
				atendimento.setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("codprograma"), con));
				atendimento.setPresenca(rs.getString("presenca"));
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
		return atendimento;
	}

	public List<AtendimentoBean> carregaAtendimentosEquipe(Integer idAtendimento) throws ProjetoException {

		String sql = "select a1.id_atendimentos1, a1.id_atendimento, a1.codprofissionalatendimento, f.descfuncionario, f.cns,"
				+ " f.codcbo, c.descricao, a1.id_situacao_atendimento, sa.descricao, sa.atendimento_realizado, pr.id, a1.codprocedimento, pr.nome as procedimento, a1.evolucao, a1.perfil_avaliacao, to_char(a1.horario_atendimento,'HH24:MI') horario_atendimento "
				+ " from hosp.atendimentos1 a1"
				+ " left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento "
				+ " left join acl.funcionarios f on (f.id_funcionario = a1.codprofissionalatendimento)"
				+ " left join hosp.cbo c on (f.codcbo = c.id)"
				+ " left join hosp.proc pr on (a1.codprocedimento = pr.id)" + " where a1.id_atendimento = ? and coalesce(a1.excluido,'N')='N'"
				+ " order by a1.id_atendimentos1";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, idAtendimento);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean atendimento = new AtendimentoBean();
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setId1(rs.getInt("id_atendimentos1"));
				atendimento.getFuncionario().setId(rs.getLong("codprofissionalatendimento"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.getFuncionario().setCns(rs.getString("cns"));
				atendimento.getCbo().setCodCbo(rs.getInt("codcbo"));
				atendimento.getCbo().setDescCbo(rs.getString("descricao"));
				
				atendimento.getSituacaoAtendimento().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimento().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimento().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				atendimento.getSituacaoAtendimentoAnterior().setId(rs.getInt("id_situacao_atendimento"));
				atendimento.getSituacaoAtendimentoAnterior().setDescricao(rs.getString("descricao"));
				atendimento.getSituacaoAtendimentoAnterior().setAtendimentoRealizado(rs.getBoolean("atendimento_realizado"));
				
				atendimento.getProcedimento().setCodProc(rs.getString("codprocedimento"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				atendimento.getProcedimento().setIdProc(rs.getInt("id"));
				atendimento.setEvolucao(rs.getString("evolucao"));
				atendimento.setPerfil(rs.getString("perfil_avaliacao"));
				if (!VerificadorUtil.verificarSeObjetoNulo(rs.getString("horario_atendimento")))
					atendimento.setHorarioAtendimento(rs.getString("horario_atendimento"));
				lista.add(atendimento);
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
		return lista;
	}

	public List<AtendimentoBean> carregarEvolucoesDoPaciente(Integer codPaciente) throws ProjetoException {

		String sql = "SELECT a1.evolucao, a.dtaatende, f.descfuncionario, p.nome FROM hosp.atendimentos1 a1 "
				+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
				+ "LEFT JOIN hosp.proc p ON (p.id = a1.codprocedimento) "
				+ "LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a1.codprofissionalatendimento) "
				+ "WHERE a1.evolucao IS NOT NULL AND a.codpaciente = ? and a1.codprofissionalatendimento = ?  and coalesce(a.situacao, 'A')<> 'C'	and coalesce(a1.excluido, 'N' )= 'N'"
				+ "ORDER BY a.dtaatende DESC ";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPaciente);
			stm.setLong(2, user_session.getId());

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.getProcedimento().setNomeProc(rs.getString("nome"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.setEvolucao(rs.getString("evolucao"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));

				lista.add(at);
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
		return lista;
	}
	
	public List<AtendimentoBean> carregarTodasAsEvolucoesDoPaciente(Integer codPaciente, Date periodoInicialEvolucao, Date periodoFinalEvolucao) throws ProjetoException {

		String sql = "SELECT a1.evolucao, a.dtaatende, f.descfuncionario, p.nome, ta.desctipoatendimento, programa.descprograma, g.descgrupo FROM hosp.atendimentos1 a1 "
				+ "LEFT JOIN hosp.atendimentos a ON (a.id_atendimento = a1.id_atendimento) "
				+ " left join hosp.tipoatendimento ta on ta.id = a.codtipoatendimento "
				+ " left join hosp.programa  on programa.id_programa = a.codprograma "
				+ " left join hosp.grupo g on g.id_grupo = a.codgrupo "
				+ "LEFT JOIN hosp.proc p ON (p.id = a1.codprocedimento) "
				+ "LEFT JOIN acl.funcionarios f ON (f.id_funcionario = a1.codprofissionalatendimento) "
				+ "WHERE a1.evolucao IS NOT NULL AND a.codpaciente = ? and coalesce(a.situacao,'')<>'C' and coalesce(a1.excluido,'N')='N' ";
		        if (periodoInicialEvolucao != null)
		            sql = sql + " AND a.dtaatende >= ? AND a.dtaatende <= ?";
		        sql = sql + "ORDER BY a.dtaatende ";

		ArrayList<AtendimentoBean> lista = new ArrayList<AtendimentoBean>();

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			stm.setInt(1, codPaciente);
            int i = 2;
            if (periodoInicialEvolucao != null) {
                stm.setDate(i, new java.sql.Date(periodoInicialEvolucao.getTime()));
                i = i + 1;
                stm.setDate(i, new java.sql.Date(periodoFinalEvolucao.getTime()));
                i = i + 1;
            }
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AtendimentoBean at = new AtendimentoBean();
				at.getProcedimento().setNomeProc(rs.getString("nome"));
				at.getFuncionario().setNome(rs.getString("descfuncionario"));
				at.setEvolucao(rs.getString("evolucao"));
				at.setDataAtendimentoInicio(rs.getDate("dtaatende"));
				at.getTipoAt().setDescTipoAt(rs.getString("desctipoatendimento"));
				at.getPrograma().setDescPrograma(rs.getString("descprograma"));
				at.getGrupo().setDescGrupo(rs.getString("descgrupo"));

				lista.add(at);
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
		return lista;
	}
	
    public boolean verificaSeExisteAtendimentoparaProfissionalNaDataNaEquipe(AtendimentoBean atendimento) throws ProjetoException {
        ArrayList<FuncionarioBean> lista = new ArrayList<>();
        String sql = "select a.id_atendimento from hosp.atendimentos a  " + 
        		"join hosp.atendimentos1 a1 on a1.id_atendimento = a.id_atendimento " + 
        		"where dtaatende=? " + 
        		"and a.codprograma=? and a.codgrupo=? and a.codequipe=? " + 
        		"and a1.codprofissionalatendimento=? and coalesce(a.situacao,'A')<>'C' " + 
        		" and coalesce(a1.excluido,'N' )='N'" +
        		"";
        boolean rst = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setDate(1,new java.sql.Date( atendimento.getDataAtendimentoInicio().getTime()));
            stm.setInt(2,  atendimento.getPrograma().getIdPrograma());
            stm.setInt(3,  atendimento.getGrupo().getIdGrupo());
            stm.setInt(4,  atendimento.getEquipe().getCodEquipe());
            stm.setLong(5,  atendimento.getFuncionario().getId());
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
               rst = true;
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
        return rst;
    }	
	
    public boolean alteraSituacaoDeAtendimentoPorProfissional(Integer idSituacaoAtendimento, Integer idAtendimento) throws ProjetoException {
        
        String sql = "update hosp.atendimentos1 set id_situacao_atendimento = ? where id_atendimento = ?";
        boolean alterado = false;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1,  idSituacaoAtendimento);
            stm.setInt(2,  idAtendimento);
            
            stm.executeUpdate();
            con.commit();
            alterado = true;
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
        return alterado;
    }	
}
