package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.hosp.model.BpaIndividualizadoBean;

public class BpaDAO {

    public List<BpaIndividualizadoBean> carregarParametro(String codigoInstrumentoRegistro) throws ProjetoException {

    	List<BpaIndividualizadoBean> listaDeBpaIndividualizado = new ArrayList<BpaIndividualizadoBean>();
        String sql = "select * from hosp.atendimentos1 a1 " + 
        		" join hosp.atendimentos a on a.id_atendimento  = a1.id_atendimento " + 
        		" join hosp.pacientes p on p.id_paciente  = a.codpaciente " + 
        		" join hosp.procedimento_mensal pm on pm.id_procedimento  = a1.codprocedimento " + 
        		" join hosp.instrumento_registro_procedimento_mensal irpm on irpm.id_procedimento_mensal  = pm.id " + 
        		" join hosp.instrumento_registro ir on ir.id  = irpm.id_instrumento_registro " + 
        		" where a1.situacao ='A' " + 
        		" and ir.codigo = ? ";
        
        Connection con = ConnectionFactory.getConnection();
       
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigoInstrumentoRegistro);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	BpaIndividualizadoBean bpaIndividualizado = new BpaIndividualizadoBean();
            	bpaIndividualizado.setPrdCnes("9971974");
            	bpaIndividualizado.setPrdCmp("202002");
            	bpaIndividualizado.setPrdCnsmed("702104726597892");
            	bpaIndividualizado.setPrdCbo("223810");
            	bpaIndividualizado.setPrdDtaten("20200203");
            	bpaIndividualizado.setPrdFlh("001"); //  VERIFICAR REGRA DESSE Nº QUANDO QUE ELE É ++
            	bpaIndividualizado.setPrdSeq("01"); //   VERIFICAR QUANDO O INDICE SERA ++
            	bpaIndividualizado.setPrdPa("0301070105"); 
            	bpaIndividualizado.setPrdCnspac("94639190"); 
            	bpaIndividualizado.setPrdSexo("M"); 
            	bpaIndividualizado.setPrdIbge("270450");
            	bpaIndividualizado.setPrdCid("I694");
            	bpaIndividualizado.setPrdIdade("065");
            	bpaIndividualizado.setPrdQt("000002");
            	bpaIndividualizado.setPrdCaten("01");
            	bpaIndividualizado.setPrdNaut("             ");
            	bpaIndividualizado.setPrdNmpac("MANOEL HENRIQUE AMORIM DOS SANTOS"); //"AMARO EUGENIO CHAVES          "
            	bpaIndividualizado.setPrdDtnasc("19540405");
            	bpaIndividualizado.setPrdRaca("04");
            	bpaIndividualizado.setPrdEtnia("    ");
            	bpaIndividualizado.setPrdNac("010");
            	bpaIndividualizado.setPrdSrv("135");
            	bpaIndividualizado.setPrdClf("003");
            	bpaIndividualizado.setPrdEquipeSeq("        ");
            	bpaIndividualizado.setPrdEquipeArea("    ");
            	bpaIndividualizado.setPrdCnpj("              ");
            	bpaIndividualizado.setPrdCepPcnte("57955000");
            	bpaIndividualizado.setPrdLogradPcnte("081");
            	bpaIndividualizado.setPrdEndPcnte("RUA DA PEIXARIA               ");
            	bpaIndividualizado.setPrdComplPcnte("          ");
            	bpaIndividualizado.setPrdNumPcnte("SN   ");
            	bpaIndividualizado.setPrdBairroPcnte("CENTRO                        ");
            	bpaIndividualizado.setPrdDDtelPcnte("           ");
            	bpaIndividualizado.setPrdEmailPcnte("                                        ");
            	bpaIndividualizado.setPrdIne("          ");
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
        String extensao = "";
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
}
