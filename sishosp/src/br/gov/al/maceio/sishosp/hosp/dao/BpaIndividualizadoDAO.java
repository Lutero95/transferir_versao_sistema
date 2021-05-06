package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.control.LaudoController;
import br.gov.al.maceio.sishosp.hosp.model.AtendimentoBean;
import br.gov.al.maceio.sishosp.hosp.model.BpaIndividualizadoBean;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class BpaIndividualizadoDAO {

	private static final String CODIGO_BPA_INDIVIDUALIZADO =  "02";

	/* ESTAS CONSTANTES SERAM SUBSTITUÍDAS DEPOIS POR DADOS DO BANCO */
	private static final String PRD_CATEN = "01";
	private static final String PRD_NAUT = "             ";
	private static final String PRD_NAC = "010";
	private static final String PRD_EQUIPE_SEQ = "        ";
	private static final String PRD_EQUIPE_AREA = "    ";
	private static final String PRD_LOGRAD_PCNTE = "   ";
	private static final String PRD_DDTEL_PCNT = "           ";
	private static final String PRD_INE = "          ";
	
	private FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().get("obj_funcionario");

	public List<BpaIndividualizadoBean> carregaDadosBpaIndividualizado
			(Date dataInicio, Date dataFim, String competencia, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro, List<Integer> idUnidades,
			 Integer codigoConfiguracaoProducao)
			throws ProjetoException {
		
		List<BpaIndividualizadoBean> listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
		String sql = "select * from (\r\n" + 
				"		\r\n" + 
				"		 select count(*) qtdproc , a.cod_unidade, \r\n" + 
				"		 proc.codproc,  \r\n" + 
				" coalesce(cpb.cnes_producao, emp.cnes) cnes, 		 pm.competencia_atual, \r\n" + 
				"		 func.cns cnsprofissional\r\n" + 
				"		 , cbo.codigo cbo, \r\n" + 
				"		 p.cns cnspaciente, lpad(coalesce(etnia.codetnia ,'    '),4,'0')  codetnia, \r\n" + 
				"		 p.sexo, substring(to_char(m.codigo,'9999999')	,1,7) codibgemun, cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) idade, \r\n" + 
				"		 p.nome nomepaciente, p.dtanascimento , raca.codraca, p.cep, tl.codigo  codlogradouro, \r\n" + 
				"		 p.logradouro enderecopaciente, p.complemento complendpaciente, p.numero numendpaciente,    case when proc.exige_cnpj is true then  emp.cnpj else '              ' end  cnpj, \r\n" + 
				"		 bairros.descbairro bairropaciente, p.email, dtaatende, p.dtanascimento,\r\n" + 
				"		 case when proc.exige_info_servico is true then  sm.codigo else '' end  codigo_servico, \r\n" + 
				"		 case when proc.exige_info_classificacao is true then  cm.codigo else '' end  codigo_classificacao,\r\n" + 
				"		 proc.id as codprocedimento, sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, func.cns as cnsfunc, p.cns as cnspac,\r\n" + 
				"		 l.situacao, l.data_solicitacao, l.mes_final, l.ano_final  \r\n" + 
				"		 from hosp.atendimentos1 a1  \r\n" + 
				"		 join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" + 
				"		 join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  \r\n" + 
				"		 left join hosp.cbo on cbo.id = a1.cbo  \r\n" + 
				"		 join hosp.pacientes p on p.id_paciente  = a.codpaciente  \r\n" + 
				"		 left join hosp.bairros on bairros.id_bairro = p.codbairro  \r\n" + 
				"		 left join hosp.tipologradouro tl on tl.id = p.codtipologradouro  \r\n" + 
				"		 left join hosp.raca on raca.id_raca = p.codraca  \r\n" + 
				"		 left join hosp.etnia on etnia.id_etnia = p.codetnia  \r\n" + 
				"		 left  join hosp.municipio m on m.id_municipio  = p.codmunicipio  \r\n" + 
				"		 join hosp.proc on proc.id = a1.codprocedimento  \r\n" + 
				"		 left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" + 
				"		 left join hosp.laudo l on l.id_laudo  = pi.codlaudo  \r\n" + 
				"		 left join hosp.cid on cid.cod = a1.id_cidprimario  \r\n" + 
				"		 join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento  \r\n" + 
				"		 join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" + 
				"		 join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  \r\n" + 
				"		 left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento cross join hosp.empresa emp\r\n" + 
				" left join hosp.configuracao_producao_bpa cpb on cpb.id = ? 		 left join sigtap.servico sm on sm.id = proc.id_servico \r\n" + 
				"		 left join sigtap.classificacao cm on cm.id = proc.id_classificacao \r\n" + 
				"		 where   hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" + 
				"   		 and coalesce(a1.excluido, 'N')= 'N' \r\n" + 
				"		 and a.dtaatende  between ?  and ? \r\n" + 
				"		 and ir.codigo = ? and ir.nome like '%BPA%' \r\n" + 
				"		 and pm.competencia_atual = ? and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id \r\n" + 
				"		 group by \r\n" + 
				"		 proc.codproc, a.cod_unidade, \r\n" + 
				"		  coalesce(cpb.cnes_producao, emp.cnes) , pm.competencia_atual, \r\n" + 
				"		 func.cns ,\r\n" + 
				"		 cbo.codigo, proc.codproc, \r\n" + 
				"		 p.cns,\r\n" + 
				"		 p.sexo, m.codigo , cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) , \r\n" + 
				"		 p.nome , p.dtanascimento , raca.codraca, p.cep, tl.codigo  , \r\n" + 
				"		 p.logradouro, p.complemento , p.numero, \r\n" + 
				"		 bairros.descbairro , p.email, a.dtaatende, p.dtanascimento, \r\n" + 
				"		 case when proc.exige_info_servico is true then  sm.codigo else '' end  , \r\n" + 
				"		 case when proc.exige_info_classificacao is true then  cm.codigo else '' end, \r\n" + 
				"		 case when proc.exige_cnpj is true then  emp.cnpj else '              ' end, etnia.codetnia,\r\n" + 
				"		 proc.id, sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, l.situacao, l.data_solicitacao, l.mes_final, l.ano_final   	\r\n" + 
				" \r\n" + 
				"UNION \r\n" + 
				" \r\n" + 
				"		 select count(*) qtdproc , a.cod_unidade, \r\n" + 
				"		 proc.codproc,  \r\n" + 
				" coalesce(cpb.cnes_producao, emp.cnes) cnes, 		 pm.competencia_atual, \r\n" + 
				"		 func.cns cnsprofissional\r\n" + 
				"		 , cbo.codigo cbo, \r\n" + 
				"		 p.cns cnspaciente, lpad(coalesce(etnia.codetnia ,'    '),4,'0')  codetnia, \r\n" + 
				"		 p.sexo, substring(to_char(m.codigo,'9999999')	,1,7) codibgemun, cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) idade, \r\n" + 
				"		 p.nome nomepaciente, p.dtanascimento , raca.codraca, p.cep, tl.codigo  codlogradouro, \r\n" + 
				"		 p.logradouro enderecopaciente, p.complemento complendpaciente, p.numero numendpaciente,    case when proc.exige_cnpj is true then  emp.cnpj else '              ' end  cnpj, \r\n" + 
				"		 bairros.descbairro bairropaciente, p.email, dtaatende, p.dtanascimento,\r\n" + 
				"		 case when proc.exige_info_servico is true then  sm.codigo else '' end  codigo_servico, \r\n" + 
				"		 case when proc.exige_info_classificacao is true then  cm.codigo else '' end  codigo_classificacao,  \r\n" + 
				"		 proc.id as codprocedimento, sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, func.cns as cnsfunc, p.cns as cnspac,\r\n" + 
				"		 l.situacao, l.data_solicitacao, l.mes_final, l.ano_final  \r\n" + 
				"		 from hosp.atendimentos1 a1  \r\n" + 
				"		 join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" + 
				"		  join hosp.atendimentos1_procedimento_secundario aps on a1.id_atendimentos1 = aps.id_atendimentos1 \r\n" + 
				"		 join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  \r\n" + 
				"		 left join hosp.cbo on cbo.id = a1.cbo  \r\n" + 
				"		 join hosp.pacientes p on p.id_paciente  = a.codpaciente  \r\n" + 
				"		 left join hosp.bairros on bairros.id_bairro = p.codbairro  \r\n" + 
				"		 left join hosp.tipologradouro tl on tl.id = p.codtipologradouro  \r\n" + 
				"		 left join hosp.raca on raca.id_raca = p.codraca  \r\n" + 
				"		 left join hosp.etnia on etnia.id_etnia = p.codetnia  \r\n" + 
				"		 left  join hosp.municipio m on m.id_municipio  = p.codmunicipio  \r\n" + 
				"		 join hosp.proc on proc.id = aps.id_procedimento  \r\n" + 
				"		 left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" + 
				"		 left join hosp.laudo l on l.id_laudo  = pi.codlaudo  \r\n" + 
				"		 left join hosp.cid on cid.cod = a1.id_cidprimario  \r\n" + 
				"		 join sigtap.procedimento_mensal pm on pm.id_procedimento  = aps.id_procedimento  \r\n" + 
				"		 join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" + 
				"		 join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  \r\n" + 
				"		 left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento cross join hosp.empresa emp\r\n" + 
				"		 left join sigtap.servico sm on sm.id = proc.id_servico \r\n" + 
				"		 left join sigtap.classificacao cm on cm.id = proc.id_classificacao \r\n" + 
				" left join hosp.configuracao_producao_bpa cpb on cpb.id = ? 		 where   hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" + 
				"	 	 and coalesce(a1.excluido, 'N')= 'N' \r\n" + 
				"		 and a.dtaatende  between ?  and ?  and  coalesce(aps.excluido,'N') ='N' \r\n" + 
				"		 and ir.codigo = ? and ir.nome like '%BPA%'  \r\n" + 
				"		 and pm.competencia_atual = ? and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id \r\n" + 
				"		 group by \r\n" + 
				"		 proc.codproc, a.cod_unidade, \r\n" + 
				"		  coalesce(cpb.cnes_producao, emp.cnes), pm.competencia_atual, \r\n" + 
				"		 func.cns ,\r\n" + 
				"		 cbo.codigo, proc.codproc, \r\n" + 
				"		 p.cns,\r\n" + 
				"		 p.sexo, m.codigo , cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) , \r\n" + 
				"		 p.nome , p.dtanascimento , raca.codraca, p.cep, tl.codigo  , \r\n" + 
				"		 p.logradouro, p.complemento , p.numero, \r\n" + 
				"		 bairros.descbairro , p.email, a.dtaatende, p.dtanascimento, \r\n" + 
				"		 case when proc.exige_info_servico is true then  sm.codigo else '' end  , \r\n" + 
				"		 case when proc.exige_info_classificacao is true then  cm.codigo else '' end, \r\n" + 
				"		 case when proc.exige_cnpj is true then  emp.cnpj else '              ' end, etnia.codetnia,\r\n" + 
				"		 proc.id, sa.atendimento_realizado, a.presenca, a1.id_situacao_atendimento, l.situacao, l.data_solicitacao, l.mes_final, l.ano_final\r\n" + 
				") a	\r\n" + 
				"where 1 = 1  ";

		if (listaProcedimentosFiltro.size()>0)
			sql+=" and a.codprocedimento = any(?) ";

		if(!idUnidades.isEmpty())
			sql += " and a.cod_unidade = any(?) ";

		if (tipoGeracao.equals("A")){
			sql+=" and a.atendimento_realizado = true";
		}
		else
			sql+=" and a.presenca='S' and ((a.atendimento_realizado is true) or (a.id_situacao_atendimento is null)) ";
		
		if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
			sql += " and a.situacao = 'A' and ((a.data_solicitacao between ? and ?) \r\n" + 
				"	or hosp.fn_GetLastDayOfMonth(to_date(a.ano_final||'-'||'0'||''||a.mes_final||'-'||'01', 'YYYY-MM-DD'))  between ? and ?) ";
		}

		sql+=" order by a.cnsfunc, a.cnspac	";

		Connection con = ConnectionFactory.getConnection();

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(codigoConfiguracaoProducao))
				ps.setNull(1, Types.NULL);
			else
				ps.setInt(1, codigoConfiguracaoProducao);
			ps.setDate(2, new java.sql.Date(dataInicio.getTime()));
			ps.setDate(3, new java.sql.Date(dataFim.getTime()));
			ps.setString(4, CODIGO_BPA_INDIVIDUALIZADO);
			ps.setString(5, competencia);
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(codigoConfiguracaoProducao))
				ps.setNull(6, Types.NULL);
			else
				ps.setInt(6, codigoConfiguracaoProducao);
			ps.setDate(7, new java.sql.Date(dataInicio.getTime()));
			ps.setDate(8, new java.sql.Date(dataFim.getTime()));
			ps.setString(9, CODIGO_BPA_INDIVIDUALIZADO);
			ps.setString(10, competencia);
			ArrayList<Integer> lista = new ArrayList<>();
			for (int i = 0; i < listaProcedimentosFiltro.size(); i++) {
				lista.add(listaProcedimentosFiltro.get(i).getIdProc());
			}

			int parametro = 11;
			if (listaProcedimentosFiltro.size() > 0) {
				ps.setObject(11, ps.getConnection().createArrayOf("INTEGER", lista.toArray()));
				parametro++;
			}

			if (!idUnidades.isEmpty())
				ps.setObject(parametro, ps.getConnection().createArrayOf("INTEGER", idUnidades.toArray()));
			
			if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
				ps.setDate(parametro, new java.sql.Date(dataInicio.getTime()));
				parametro++;
				ps.setDate(parametro, new java.sql.Date(dataFim.getTime()));
				parametro++;
				ps.setDate(parametro, new java.sql.Date(dataInicio.getTime()));
				parametro++;
				ps.setDate(parametro, new java.sql.Date(dataFim.getTime()));
			}

			LaudoController validacaoSigtap = new LaudoController();
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String retornoCid = null;
				String retornoCbo = null;
				String retornoIdade = null;
				String retornoSexo = null;
				BpaIndividualizadoBean bpaIndividualizado = new BpaIndividualizadoBean();
				Date dataSolicitacaoRefSigtap = DataUtil.montarDataCompletaInicioMesPorAnoMesCompetencia(rs.getString("competencia_atual"));
				String cbo = rs.getString("cbo");
				String codProc = rs.getString("codproc");
				PacienteBean paciente =new PacienteBean();
				paciente.setNome(rs.getString("nomepaciente"));
				paciente.setNome(rs.getString("nomepaciente"));
				paciente.setDtanascimento(rs.getDate("dtanascimento"));
				CidBean cid = new CidBean();
				cid.setCid(rs.getString("cid"));
				retornoCbo =  validacaoSigtap.validaCboPermitidoProcedimento(dataSolicitacaoRefSigtap, cbo, codProc, paciente, false);
				if (retornoCbo!=null)
					bpaIndividualizado.getListaInconsistencias().add(retornoCbo);
				retornoIdade = validacaoSigtap.idadeValida(dataSolicitacaoRefSigtap, paciente, codProc, false);
				if (retornoIdade!=null)
					bpaIndividualizado.getListaInconsistencias().add(retornoIdade);
				retornoSexo = validacaoSigtap.validaSexoDoPacienteProcedimentoSigtap(dataSolicitacaoRefSigtap, codProc, paciente, false);
				if (retornoSexo!=null)
					bpaIndividualizado.getListaInconsistencias().add(retornoSexo);
				if(validacaoSigtap.procedimentoPossuiCidsAssociados(dataSolicitacaoRefSigtap, codProc)) {
					retornoCid =validacaoSigtap.validaCidsDoLaudo(dataSolicitacaoRefSigtap, cid, codProc, paciente, false);
					if (retornoCid!=null)
					bpaIndividualizado.getListaInconsistencias().add(retornoCid);
				}



				bpaIndividualizado.setPrdCnes(rs.getString("cnes"));
				bpaIndividualizado.setPrdCmp(rs.getString("competencia_atual"));
				bpaIndividualizado.setPrdCnsmed(rs.getString("cnsprofissional"));
				bpaIndividualizado.setPrdCbo(rs.getString("cbo"));
				bpaIndividualizado.setPrdDtaten(rs.getDate("dtaatende").toString().replaceAll("-", ""));
				bpaIndividualizado.setPrdPa(rs.getString("codproc"));
				bpaIndividualizado.setPrdCnspac(rs.getString("cnspaciente"));
				bpaIndividualizado.setPrdSexo(rs.getString("sexo"));
				bpaIndividualizado.setPrdIbge(String.valueOf(rs.getInt("codibgemun")));
				bpaIndividualizado.setPrdCid(rs.getString("cid"));
				bpaIndividualizado.setPrdIdade(String.valueOf(rs.getInt("idade")));
				bpaIndividualizado.setPrdQt(String.valueOf(rs.getInt("qtdproc")));
				/* POSTERIORMENTE ALIMENTAR OS DADOS QUE RECEBEM CONSTRANTES COM DADOS DO BANCO */
				bpaIndividualizado.setPrdCaten(PRD_CATEN);
				bpaIndividualizado.setPrdNaut(PRD_NAUT);

				bpaIndividualizado.setPrdNmpac(rs.getString("nomepaciente"));
				bpaIndividualizado.setPrdDtnasc(rs.getString("dtanascimento").toString().replaceAll("-", ""));
				bpaIndividualizado.setPrdRaca(rs.getString("codraca"));

				bpaIndividualizado.setPrdEtnia(rs.getString("codetnia"));
				bpaIndividualizado.setPrdNac(PRD_NAC);
				bpaIndividualizado.setPrdSrv(rs.getString("codigo_servico"));
				bpaIndividualizado.setPrdClf(rs.getString("codigo_classificacao"));
				bpaIndividualizado.setPrdEquipeSeq(PRD_EQUIPE_SEQ);
				bpaIndividualizado.setPrdEquipeArea(PRD_EQUIPE_AREA);
				bpaIndividualizado.setPrdCnpj(rs.getString("cnpj"));
				bpaIndividualizado.setPrdCepPcnte(rs.getString("cep"));
				bpaIndividualizado.setPrdLogradPcnte(rs.getString("codlogradouro"));
				bpaIndividualizado.setPrdEndPcnte(rs.getString("enderecopaciente"));
				bpaIndividualizado.setPrdComplPcnte(rs.getString("complendpaciente"));
				bpaIndividualizado.setPrdNumPcnte(rs.getString("numendpaciente"));
				bpaIndividualizado.setPrdBairroPcnte(rs.getString("bairropaciente"));
				bpaIndividualizado.setPrdDDtelPcnte(PRD_DDTEL_PCNT);
				bpaIndividualizado.setPrdEmailPcnte(rs.getString("email"));
				bpaIndividualizado.setPrdIne(PRD_INE);
				listaDeBpaIndividualizado.add(bpaIndividualizado);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaDeBpaIndividualizado;
	}

	public List<String> listarCompetencias() throws ProjetoException {
		String sql = "select distinct concat(substring (pm.competencia_atual from 5 for 2), '/', substring (pm.competencia_atual from 0 for 5)) " +
				"as mes_ano, " +
				"concat (substring (pm.competencia_atual from 0 for 5), '/', substring (pm.competencia_atual from 5 for 2)) " +
				"as ano_mes " +
				"from sigtap.procedimento_mensal pm order by ano_mes desc;";
		List<String> listaCompetencias = new ArrayList<String>();
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String competencia = rs.getString("mes_ano");
				listaCompetencias.add(competencia);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaCompetencias;
	}

	public List<AtendimentoBean> listaPossiveisDuplicidades(BpaIndividualizadoBean bpa, Integer codigoConfiguracaoProducao, Date dataInicio, Date dataFim)
			throws ProjetoException {

		String sql = "select a.* from (\r\n" +
				"	select a.id_atendimento, a1.id_atendimentos1, p.id_paciente, p.nome paciente, func.id_funcionario, func.descfuncionario, \r\n" +
				"		 a.dtaatende, pro.id_programa, pro.descprograma, g.id_grupo, g.descgrupo, proc.id id_procedimento, proc.nome procedimento, " +
				"		 l.situacao, l.data_solicitacao, l.mes_final, l.ano_final \r\n" +
				"		 from hosp.atendimentos1 a1  \r\n" +
				"		 join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" +
				"		 left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" + 
				"		 left join hosp.laudo l on l.id_laudo  = pi.codlaudo" +
				"		 left join hosp.programa pro on a.codprograma = pro.id_programa \r\n" +
				"		 left join hosp.grupo g on a.codgrupo = g.id_grupo \r\n" +
				"		 join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  \r\n" +
				"		 join hosp.pacientes p on p.id_paciente  = a.codpaciente  \r\n" +
				"		 join hosp.proc on proc.id = a1.codprocedimento  \r\n" +
				"		 join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento \r\n" +
				"		 join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  \r\n" +
				"		 left join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" +
				"		 left join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  \r\n" +
				"		 left join hosp.unidade u on a.cod_unidade = u.id\r\n" +
				"		 cross join hosp.empresa emp \r\n" +
				"		left join hosp.configuracao_producao_bpa cpb on cpb.id =? " +
				"		 where  hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" +
				"	 	 and coalesce(a1.excluido, 'N')= 'N' \r\n" +
				"	 	 and func.cns = ? and p.cns = ? \r\n" +
				"	 	 and proc.codproc = ? and a.dtaatende = ? and hc.status = 'A'\r\n" +
				"		 and coalesce(cpb.cnes_producao, emp.cnes) = ? and ir.codigo = '02' \r\n" +
				"		 and pm.competencia_atual = ? and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id \r\n" +
				"\r\n" +
				"union		 \r\n" +
				"		 \r\n" +
				"	select a.id_atendimento, a1.id_atendimentos1, p.id_paciente, p.nome paciente, func.id_funcionario, func.descfuncionario, \r\n" +
				"  		 a.dtaatende, pro.id_programa, pro.descprograma, g.id_grupo, g.descgrupo, proc.id id_procedimento, proc.nome procedimento, " +
				" 		 l.situacao, l.data_solicitacao, l.mes_final, l.ano_final \r\n" +
				"		 from hosp.atendimentos1 a1  \r\n" +
				"		 join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \r\n" +
				"		 left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \r\n" + 
				"		 left join hosp.laudo l on l.id_laudo  = pi.codlaudo" +
				"		 left join hosp.programa pro on a.codprograma = pro.id_programa\r\n" +
				"		 left join hosp.grupo g on a.codgrupo = g.id_grupo \r\n" +
				"		 join hosp.atendimentos1_procedimento_secundario aps on a1.id_atendimentos1 = aps.id_atendimentos1 \r\n" +
				"		 join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  \r\n" +
				"		 join hosp.pacientes p on p.id_paciente  = a.codpaciente  \r\n" +
				"		 join hosp.proc on proc.id = aps.id_procedimento  \r\n" +
				"		 join sigtap.procedimento_mensal pm on pm.id_procedimento  = aps.id_procedimento\r\n" +
				"		 join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap  \r\n" +
				"		 left join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \r\n" +
				"		 left join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  \r\n" +
				"		 left join hosp.unidade u on a.cod_unidade = u.id\r\n" +
				"		 cross join hosp.empresa emp \r\n" +
				"		 left join hosp.configuracao_producao_bpa cpb on cpb.id =? " +
				"		 where   hc.status='A' and coalesce(a.situacao, '')<> 'C'\r\n" +
				"	 	 and coalesce(a1.excluido, 'N')= 'N' \r\n" +
				"	 	 and func.cns = ? and p.cns = ? \r\n" +
				"	 	 and proc.codproc = ? and a.dtaatende = ? and hc.status = 'A'\r\n" +
				"		 and coalesce(cpb.cnes_producao, emp.cnes) = ? and ir.codigo = '02' \r\n" +
				"		 and pm.competencia_atual = ? and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id \r\n" +
				"	) a	 where not exists (select id_atendimentos1 from hosp.atendimentos_para_ignorar_duplicidade apid where id_atendimentos1 = a.id_atendimentos1) ";
		
		if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
			sql += " and a.situacao = 'A' and ((a.data_solicitacao between ? and ?) \r\n" + 
				"	or hosp.fn_GetLastDayOfMonth(to_date(a.ano_final||'-'||'0'||''||a.mes_final||'-'||'01', 'YYYY-MM-DD'))  between ? and ?) ";
		}
		
		List<AtendimentoBean> listaAtendimento = new ArrayList<>();
		Connection con = null;
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(codigoConfiguracaoProducao))
				ps.setNull(1, Types.NULL);
			else
				ps.setInt(1, codigoConfiguracaoProducao);
			ps.setString(2, bpa.getPrdCnsmed());
			ps.setString(3, bpa.getPrdCnspac());
			ps.setString(4, bpa.getPrdPa());

			Calendar calendar = Calendar.getInstance();

			int ano = Integer.valueOf(bpa.getPrdDtaten().substring(0, 4));
			int mes = Integer.valueOf(bpa.getPrdDtaten().substring(4, 6)) - 1;
			int data = Integer.valueOf(bpa.getPrdDtaten().substring(6, 8));
			calendar.set(ano, mes, data);

			ps.setDate(5, new java.sql.Date(calendar.getTime().getTime()));
			ps.setString(6, bpa.getPrdCnes());
			ps.setString(7, bpa.getPrdCmp());
			if(VerificadorUtil.verificarSeObjetoNuloOuZero(codigoConfiguracaoProducao))
				ps.setNull(8, Types.NULL);
			else
				ps.setInt(8, codigoConfiguracaoProducao);
			ps.setString(9, bpa.getPrdCnsmed());
			ps.setString(10, bpa.getPrdCnspac());
			ps.setString(11, bpa.getPrdPa());
			ps.setDate(12, new java.sql.Date(calendar.getTime().getTime()));
			ps.setString(13, bpa.getPrdCnes());
			ps.setString(14, bpa.getPrdCmp());
			
			if(user_session.getUnidade().getParametro().isBpaComLaudoAutorizado()) {
				ps.setDate(15, new java.sql.Date(dataInicio.getTime()));
				ps.setDate(16, new java.sql.Date(dataFim.getTime()));
				ps.setDate(17, new java.sql.Date(dataInicio.getTime()));
				ps.setDate(18, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				AtendimentoBean atendimento = new AtendimentoBean();
				atendimento.setId(rs.getInt("id_atendimento"));
				atendimento.setId1(rs.getInt("id_atendimentos1"));
				atendimento.getPaciente().setId_paciente(rs.getInt("id_paciente"));
				atendimento.getPaciente().setNome(rs.getString("paciente"));
				atendimento.getFuncionario().setId(rs.getLong("id_funcionario"));
				atendimento.getFuncionario().setNome(rs.getString("descfuncionario"));
				atendimento.setDataAtendimento(rs.getDate("dtaatende"));
				atendimento.getPrograma().setIdPrograma(rs.getInt("id_programa"));
				atendimento.getPrograma().setDescPrograma(rs.getString("descprograma"));
				atendimento.getGrupo().setIdGrupo(rs.getInt("id_grupo"));
				atendimento.getGrupo().setDescGrupo(rs.getString("descgrupo"));
				atendimento.getProcedimento().setIdProc(rs.getInt("id_procedimento"));
				atendimento.getProcedimento().setNomeProc(rs.getString("procedimento"));
				listaAtendimento.add(atendimento);
			}
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return listaAtendimento;
	}

	public Boolean inserirAtendimentoParaIgnorarDuplicidades
			(AtendimentoBean atendimento, FuncionarioBean usuarioLiberacao, String motivoLiberacao) throws ProjetoException {

		String sql = "INSERT INTO hosp.atendimentos_para_ignorar_duplicidade \r\n" +
				"(id_atendimentos1, id_usuario_gravacao, datahora) VALUES(?, ?, current_timestamp);";

		Boolean gravou = false;
		Connection con = null;

		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, atendimento.getId1());
			ps.setLong(2, usuarioLiberacao.getId());

			ps.executeUpdate();

			new AgendaDAO().gravarLiberacaoAtendimento1(con, atendimento, usuarioLiberacao, motivoLiberacao);
			gravou = true;
			con.commit();
		} catch (SQLException ex2) {
			throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(ex2), this.getClass().getName(), ex2);
		} catch (Exception ex) {
			throw new ProjetoException(ex, this.getClass().getName());
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return gravou;
	}
}
