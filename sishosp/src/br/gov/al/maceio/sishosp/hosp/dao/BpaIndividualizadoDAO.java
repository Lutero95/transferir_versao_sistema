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
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.hosp.control.LaudoController;
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
	
    public List<BpaIndividualizadoBean> carregaDadosBpaIndividualizado(Date dataInicio, Date dataFim, String competencia, String tipoGeracao, List<ProcedimentoBean> listaProcedimentosFiltro) throws ProjetoException {

    	List<BpaIndividualizadoBean> listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
        String sql = "select count(*) qtdproc ,\n" +
				"  proc.codproc, emp.cnes,\n" +
				"  pm.competencia_atual, \n" +
				"  func.cns cnsprofissional\n" +
				"  , cbo.codigo cbo, proc.codproc, \n" +
				"  p.cns cnspaciente, lpad(coalesce(etnia.codetnia ,'    '),4,'0')  codetnia, \n" +
				" p.sexo, substring(to_char(m.codigo,'9999999')\t,1,7) codibgemun, cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) idade, \n" +
				" p.nome nomepaciente, p.dtanascimento , raca.codraca, p.cep, tl.codigo  codlogradouro, \n" +
				" p.logradouro enderecopaciente, p.complemento complendpaciente, p.numero numendpaciente,  "+
				"  case when proc.exige_cnpj is true then  emp.cnpj else '              ' end  cnpj, \n" +
				" bairros.descbairro bairropaciente, p.email, dtaatende, p.dtanascimento,\n" +
				"  case when proc.exige_info_servico is true then  sm.codigo else '' end  codigo_servico, \n" +
				" case when proc.exige_info_classificacao is true then  cm.codigo else '' end  codigo_classificacao    \n" +
				" from hosp.atendimentos1 a1  \n" +
				"join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  \n" +
				"  join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  \n" +
				" left join hosp.cbo on cbo.id = a1.cbo  \n" +
				"  join hosp.pacientes p on p.id_paciente  = a.codpaciente  \n" +
				" left join hosp.bairros on bairros.id_bairro = p.codbairro  \n" +
				" left join hosp.tipologradouro tl on tl.id = p.codtipologradouro  \n" +
				" left join hosp.raca on raca.id_raca = p.codraca  \n" +
				" left join hosp.etnia on etnia.id_etnia = p.codetnia  \n" +
				" left  join hosp.municipio m on m.id_municipio  = p.codmunicipio  \n" +
				" join hosp.proc on proc.id = a1.codprocedimento  \n" +
				" left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  \n" +
				" left join hosp.programa  prog on prog.id_programa  = a.codprograma \n" +
				" left join hosp.laudo l on l.id_laudo  = pi.codlaudo  \n" +
				" left join hosp.cid on cid.cod = a1.id_cidprimario  \n" +
				" join sigtap.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento  \n" +
				" join sigtap.historico_consumo_sigtap hc on hc.id = pm.id_historico_consumo_sigtap "+
				" join sigtap.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  \n" +
				" join sigtap.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  \n" +
				" left join hosp.situacao_atendimento sa on sa.id = a1.id_situacao_atendimento" +
				" cross join hosp.empresa emp\n" +
				"left join sigtap.servico sm on sm.id = prog.id_servico \n" +
				"left   join sigtap.classificacao cm on cm.id = prog.id_classificacao \n" +
				" where  a.cod_unidade<>4 and hc.status='A' and coalesce(a.situacao, '')<> 'C'\n" +
				"\tand coalesce(a1.excluido, 'N')= 'N' \n" +
				" and a.dtaatende  between ?  and ? \n" +
				" and ir.codigo = ? \n" +
				" and pm.competencia_atual = ?   " +
				"  and coalesce(proc.id_instrumento_registro_padrao, ir.id) = ir.id  ";

		if (listaProcedimentosFiltro.size()>0)
			sql+=" and a1.codprocedimento = any(?) ";


		if (tipoGeracao.equals("A")){
			sql+=" and sa.atendimento_realizado = true";
		}
        else
		sql+=" and a.presenca='S' and ((sa.atendimento_realizado is true) or (a1.id_situacao_atendimento is null)) ";

		sql+=" group by \n" +
				" proc.codproc, \n" +
				" emp.cnes, pm.competencia_atual, \n" +
				" func.cns ,\n" +
				" cbo.codigo, proc.codproc, \n" +
				" p.cns,\n" +
				" p.sexo, m.codigo , cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) , \n" +
				" p.nome , p.dtanascimento , raca.codraca, p.cep, tl.codigo  , \n" +
				" p.logradouro, p.complemento , p.numero, \n" +
				" bairros.descbairro , p.email, a.dtaatende, p.dtanascimento, \n" +
				" case when proc.exige_info_servico is true then  sm.codigo else '' end  , \n" +
				" case when proc.exige_info_classificacao is true then  cm.codigo else '' end, \n" +
				" case when proc.exige_cnpj is true then  emp.cnpj else '              ' end, etnia.codetnia \n" ;
		sql+=" order by func.cns, p.cns  \t";
        
        Connection con = ConnectionFactory.getConnection();
       
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
            ps.setDate(2, new java.sql.Date(dataFim.getTime()));
            ps.setString(3, CODIGO_BPA_INDIVIDUALIZADO);
            ps.setString(4, competencia);
			ArrayList<Integer> lista = new ArrayList<>();
			for (int i = 0; i < listaProcedimentosFiltro.size(); i++) {
				lista.add(listaProcedimentosFiltro.get(i).getIdProc());
			}

				if (listaProcedimentosFiltro.size()>0)
				ps.setObject(5, ps.getConnection().createArrayOf(  "INTEGER", lista.toArray()));

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
}
