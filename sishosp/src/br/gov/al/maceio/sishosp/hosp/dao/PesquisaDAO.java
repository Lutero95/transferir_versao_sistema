package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.TratamentoErrosUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.enums.StatusRespostaPaciente;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.PerguntaBean;
import br.gov.al.maceio.sishosp.hosp.model.PesquisaBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.PacientePesquisaDTO;

public class PesquisaDAO {

	
    public Boolean gravarPesquisa(PesquisaBean pesquisa, List<PacienteBean> pacientesSelecionados) throws ProjetoException {

    	String sql = "INSERT INTO hosp.pesquisa (titulo, data_inicial, data_final) " + 
    			"VALUES(?, ?, ?) returning id;";
    	
        Boolean retorno = false;
        Connection conexao = null;
        
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, pesquisa.getTitulo());
            stmt.setDate(2, new Date(pesquisa.getDataInicial().getTime()));
            stmt.setDate(3, new Date(pesquisa.getDataFinal().getTime()));
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()) {
            	Integer idPesquisa = rs.getInt("id");
            	inserirPerguntas(pesquisa.getPerguntas(), idPesquisa, conexao);
            	inserirPacientes(pacientesSelecionados, idPesquisa, conexao);
            }
            conexao.commit();
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
    
    private void inserirPerguntas(List<PerguntaBean> perguntas, Integer idPesquisa, Connection conexao)
    		throws ProjetoException, SQLException {

    	String sql = "INSERT INTO hosp.pergunta_pesquisa (id_pesquisa, pergunta) VALUES(?, ?) ";
        
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            
            for (PerguntaBean perguntaBean : perguntas) {
            	stmt.setInt(1, idPesquisa);
            	stmt.setString(2, perguntaBean.getDescricao());
            	stmt.executeUpdate();
			}

        } catch (SQLException sqle) {
        	conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    private void inserirPacientes(List<PacienteBean> pacientes, Integer idPesquisa, Connection conexao)
    		throws ProjetoException, SQLException {

    	String sql = "INSERT INTO hosp.pacientes_pesquisa (id_paciente, id_pesquisa) VALUES(?, ?); ";
        
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            
            for (PacienteBean paciente : pacientes) {
            	stmt.setInt(1, paciente.getId_paciente());
            	stmt.setInt(2, idPesquisa);
            	stmt.executeUpdate();
			}

        } catch (SQLException sqle) {
        	conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
    
    public List<PesquisaBean> listarPesquisas() throws ProjetoException {

    	String sql = "SELECT id, titulo, data_inicial, data_final FROM hosp.pesquisa order by data_inicial, data_final desc;";
        Connection conexao = null;
        
        List<PesquisaBean> listaPesquisas = new ArrayList<>();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	PesquisaBean pesquisa = new PesquisaBean();
            	pesquisa.setId(rs.getInt("id"));
            	pesquisa.setTitulo(rs.getString("titulo"));
            	pesquisa.setDataInicial(rs.getDate("data_inicial"));
            	pesquisa.setDataFinal(rs.getDate("data_final"));
            	
            	listaPesquisas.add(pesquisa);
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
        return listaPesquisas;
    }
    
    public List<PacientePesquisaDTO> listarPacientesDaPesquisa
    	(Integer idPesquisa, String statusResposta, String tipoBusca, String campoBusca) throws ProjetoException {

    	String sql = "select p.id_paciente, p.nome, pp.respondido, pes.id as id_pesquisa, pes.titulo " + 
    			"from hosp.pacientes p " + 
    			"join hosp.pacientes_pesquisa pp on p.id_paciente = pp.id_paciente " + 
    			"join hosp.pesquisa pes on pp.id_pesquisa = pes.id " + 
    			"where pes.id = ? ";
    	
    	if(statusResposta.equals(StatusRespostaPaciente.RESPONDIDO.getSigla()))
    		sql += " and pp.respondido = true ";
    	else if(statusResposta.equals(StatusRespostaPaciente.NAO_RESPONDIDO.getSigla()))
    		sql += " and pp.respondido = false ";
    	
    	if(tipoBusca.equals("nome")){
            sql = sql + "and p.nome like ?";
        }
        else if(tipoBusca.equals("cpf")){
            sql = sql + "and p.cpf like ?";
        }
        else if(tipoBusca.equals("cns")){
            sql = sql + "and p.cns like ?";
        }
        else if(tipoBusca.equals("prontuario")){
            sql = sql + "and p.id_paciente = ?";
        }
        else if(tipoBusca.equals("matricula")){
            sql = sql + "and p.matricula like ?";
        }
    	
        Connection conexao = null;
        
        List<PacientePesquisaDTO> listaPacientes = new ArrayList<>();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            
            
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPesquisa);
            
            if ((tipoBusca.equals("nome")) || (tipoBusca.equals("cpf")) || (tipoBusca.equals("cns")) || (tipoBusca.equals("matricula")))
            	stmt.setString(2, "%" + campoBusca.toUpperCase() + "%");
            else if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(tipoBusca))
                stmt.setInt(2, Integer.valueOf(campoBusca));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	PacientePesquisaDTO paciente = new PacientePesquisaDTO();
            	paciente.getPaciente().setId_paciente(rs.getInt("id_paciente"));
            	paciente.getPaciente().setNome(rs.getString("nome"));
            	paciente.setRespondido(rs.getBoolean("respondido"));
            	paciente.getPesquisa().setId(rs.getInt("id_pesquisa"));
            	paciente.getPesquisa().setTitulo(rs.getString("titulo"));
            	
            	listaPacientes.add(paciente);
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
        return listaPacientes;
    }
    
    public List<PerguntaBean> listarPerguntas(Integer idPesquisa) throws ProjetoException {

    	String sql = "select pp.id, pp.pergunta from hosp.pergunta_pesquisa pp " + 
    			"join hosp.pesquisa p on pp.id_pesquisa = p.id where p.id = ?;";
    	
        Connection conexao = null;
        List<PerguntaBean> listaPerguntas = new ArrayList<>();
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPesquisa);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	PerguntaBean pergunta = new PerguntaBean();
            	pergunta.setId(rs.getInt("id"));
            	pergunta.setDescricao(rs.getString("pergunta"));
            	listaPerguntas.add(pergunta);
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
        return listaPerguntas;
    }
    
    public boolean inserirRespostasPaciente(PesquisaBean pesquisa)
    		throws ProjetoException, SQLException {

    	boolean respostasInseridas = false;
    	String sql = "INSERT INTO hosp.respostas_pesquisa " + 
    			"(id_pergunta, id_paciente, resposta) VALUES(?, ?, ?);";
        
    	Connection conexao = null;
    	
        try {
        	conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            for (PerguntaBean pergunta : pesquisa.getPerguntas()) {
            	stmt.setInt(1, pergunta.getId());
            	stmt.setInt(2, pergunta.getResposta().getPaciente().getId_paciente());
            	stmt.setString(3, pergunta.getResposta().getResposta());
            	stmt.executeUpdate();
			}
            Integer idPaciente = pesquisa.getPerguntas().get(0).getResposta().getPaciente().getId_paciente();
            atualizaStatusPesquisaParaPaciente(pesquisa.getId(), idPaciente, conexao);
            respostasInseridas = true;
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
        return respostasInseridas;
    }
    
    private void atualizaStatusPesquisaParaPaciente(Integer idPesquisa, Integer idPaciente, Connection conexao)
    		throws ProjetoException, SQLException {

    	String sql = "UPDATE hosp.pacientes_pesquisa SET respondido = true " + 
    			"WHERE id_paciente = ? AND id_pesquisa = ?";
        
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            stmt.setInt(2, idPesquisa);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
        	conexao.rollback();
            throw new ProjetoException(TratamentoErrosUtil.retornarMensagemDeErro(sqle), this.getClass().getName(), sqle);
        } catch (Exception ex) {
        	conexao.rollback();
            throw new ProjetoException(ex, this.getClass().getName());
        }
    }
}
