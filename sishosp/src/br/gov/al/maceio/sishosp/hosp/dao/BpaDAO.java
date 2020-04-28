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
import br.gov.al.maceio.sishosp.hosp.model.BpaIndividualizadoBean;

public class BpaDAO {

	private static final String CODIGO_BPA_INDIVIDUALIZADO =  "02";
	private static final String CODIGO_BPA_CONSOLIDADO =  "01";
	/* ESTAS CONSTANTES SERAM SUBSTITUÍDAS DEPOIS POR DADOS DO BANCO */
	private static final String PRD_CATEN = "01";
	private static final String PRD_NAUT = "             ";
	private static final String PRD_ETNIA = "    ";
	private static final String PRD_NAC = "010";
	private static final String PRD_SRV = "135";
	private static final String PRD_CLF = "003";
	private static final String PRD_EQUIPE_SEQ = "        ";
	private static final String PRD_EQUIPE_AREA = "    ";
	private static final String PRD_CNPJ = "              ";
	private static final String PRD_LOGRAD_PCNTE = "   ";
	private static final String PRD_DDTEL_PCNT = "           ";
	private static final String PRD_INE = "          ";
	
    public List<BpaIndividualizadoBean> carregarParametro(Date dataInicio, Date dataFim, String competencia) throws ProjetoException {

    	List<BpaIndividualizadoBean> listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
        String sql = "select count(*) qtdproc,  " + 
        		"  proc.codproc, emp.cnes, pm.competencia, func.cns cnsprofissional, cbo.codigo cbo, proc.codproc, p.cns cnspaciente, " + 
        		" p.sexo, substring(to_char(m.codigo,'9999999')	,1,7) codibgemun, cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) idade, " + 
        		" p.nome nomepaciente, p.dtanascimento , raca.codraca, p.cep, tl.codigo  codlogradouro, " + 
        		" p.logradouro enderecopaciente, p.complemento complendpaciente, p.numero numendpaciente,  " + 
        		" bairros.descbairro bairropaciente, p.email, dtaatende, p.dtanascimento  " + 
        		" from hosp.atendimentos1 a1  " + 
        		" join acl.funcionarios func on func.id_funcionario  = a1.codprofissionalatendimento  " + 
        		" left join hosp.cbo on cbo.id = func.codcbo  " + 
        		" join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento  " + 
        		" join hosp.pacientes p on p.id_paciente  = a.codpaciente  " + 
        		" left join hosp.bairros on bairros.id_bairro = p.codbairro  " + 
        		" left join hosp.tipologradouro tl on tl.id = p.codtipologradouro  " + 
        		" left join hosp.raca on raca.id_raca = p.codraca  " + 
        		" left  join hosp.municipio m on m.id_municipio  = p.codmunicipio  " + 
        		" join hosp.proc on proc.id = a1.codprocedimento  " + 
        		" left join hosp.paciente_instituicao pi on pi.id  = a.id_paciente_instituicao  " + 
        		" left join hosp.laudo l on l.id_laudo  = pi.codlaudo  " + 
        		" left join hosp.cid on cid.cod = l.cid1  " + 
        		" join hosp.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento  " + 
        		" join hosp.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id  " + 
        		" join hosp.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro  " + 
        		" cross join hosp.empresa emp " + 
        		" where a1.situacao ='A' " + 
        		" and a.dtaatende  between ? and ? " + 
        		" and ir.codigo = ? " + 
        		" and pm.competencia = ? " + 
        		" group by " + 
        		" proc.codproc, emp.cnes, pm.competencia, func.cns , cbo.codigo, proc.codproc, p.cns , " + 
        		" p.sexo, m.codigo , cid.cid, extract(year from age(CURRENT_DATE, p.dtanascimento)) , " + 
        		" p.nome , p.dtanascimento , raca.codraca, p.cep, tl.codigo  , " + 
        		" p.logradouro , p.complemento , p.numero , " + 
        		" bairros.descbairro , p.email, a.dtaatende, p.dtanascimento " + 
        		"order by func.cns, p.cns ";
        
        Connection con = ConnectionFactory.getConnection();
       
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, new java.sql.Date(dataInicio.getTime()));
            ps.setDate(2, new java.sql.Date(dataFim.getTime()));
            ps.setString(3, CODIGO_BPA_INDIVIDUALIZADO);
            ps.setString(4, competencia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	BpaIndividualizadoBean bpaIndividualizado = new BpaIndividualizadoBean();
            	bpaIndividualizado.setPrdCnes(rs.getString("cnes"));
            	bpaIndividualizado.setPrdCmp(rs.getString("competencia"));
            	bpaIndividualizado.setPrdCnsmed(rs.getString("cnsprofissional"));
            	bpaIndividualizado.setPrdCbo(rs.getString("cbo"));
            	bpaIndividualizado.setPrdDtaten(rs.getDate("dtaatende").toString().replaceAll("-", ""));
            	bpaIndividualizado.setPrdFlh("001"); //  VERIFICAR REGRA DESSE Nº QUANDO QUE ELE É ++
            	bpaIndividualizado.setPrdSeq("01"); //   VERIFICAR QUANDO O INDICE SERA ++
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
            	
            	bpaIndividualizado.setPrdEtnia(PRD_ETNIA);
            	bpaIndividualizado.setPrdNac(PRD_NAC);
            	bpaIndividualizado.setPrdSrv(PRD_SRV);
            	bpaIndividualizado.setPrdClf(PRD_CLF);
            	bpaIndividualizado.setPrdEquipeSeq(PRD_EQUIPE_SEQ);
            	bpaIndividualizado.setPrdEquipeArea(PRD_EQUIPE_AREA);
            	bpaIndividualizado.setPrdCnpj(PRD_CNPJ);
            	bpaIndividualizado.setPrdCepPcnte(rs.getString("cep"));
            	bpaIndividualizado.setPrdLogradPcnte(PRD_LOGRAD_PCNTE);
            	bpaIndividualizado.setPrdEndPcnte(rs.getString("enderecopaciente"));
            	bpaIndividualizado.setPrdComplPcnte(rs.getString("complendpaciente"));
            	bpaIndividualizado.setPrdNumPcnte(rs.getString("numendpaciente"));
            	bpaIndividualizado.setPrdBairroPcnte(rs.getString("bairropaciente"));
            	bpaIndividualizado.setPrdDDtelPcnte(PRD_DDTEL_PCNT);
            	bpaIndividualizado.setPrdEmailPcnte(rs.getString("email"));
            	bpaIndividualizado.setPrdIne(PRD_INE);
            	listaDeBpaIndividualizado.add(bpaIndividualizado);
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
        return listaDeBpaIndividualizado;
    }
    
    public String buscaExtencaoArquivoPeloMesAtual() throws ProjetoException{
        String extensao = new String();
        String sql = " SELECT CASE " + 
        		" WHEN extract(month from current_date) = 1  then '.JAN' " + 
        		" WHEN  extract(month from current_date) = 2 THEN '.FEV' " + 
        		" WHEN  extract(month from current_date) = 3 THEN '.MAR' " + 
        		" WHEN  extract(month from current_date) = 4 THEN '.ABR' " + 
        		" WHEN  extract(month from current_date) = 5 THEN '.MAI' " + 
        		" WHEN  extract(month from current_date) = 6 THEN '.JUN' " + 
        		" WHEN  extract(month from current_date) = 7 THEN '.JUL' " + 
        		" WHEN  extract(month from current_date) = 8 THEN '.AGO' " + 
        		" WHEN  extract(month from current_date) = 9 THEN '.SET' " + 
        		" WHEN  extract(month from current_date) = 10 THEN '.OUT' " + 
        		" WHEN  extract(month from current_date) = 11 THEN '.NOV' " + 
        		" WHEN  extract(month from current_date) = 12 THEN '.DEZ' " + 
        		" END AS extensao";
        
        Connection con = ConnectionFactory.getConnection();
        try {
        	
        	PreparedStatement ps = con.prepareStatement(sql);          
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            	extensao = rs.getString("extensao");
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
        return extensao;
    }

	public List<String> listarCompetencias() {
		String sql = "select distinct pm.competencia from hosp.procedimento_mensal pm ";
		List<String> listaCompetencias = new ArrayList<String>();
		Connection con = null;
        try {
        	con = ConnectionFactory.getConnection();	
        	PreparedStatement ps = con.prepareStatement(sql);          
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	String competencia = rs.getString("competencia");
            	listaCompetencias.add(competencia);
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
        return listaCompetencias;
	}

	private String formataCompetenciaParaExibicaoNaTela(String competencia) {
		String diaCompetencia = competencia.substring(4, 6);
		String anoCompetencia = competencia.substring(0, 4);
		String competenciaFormatada = diaCompetencia+"/"+anoCompetencia;
		return competenciaFormatada;
	}
}
