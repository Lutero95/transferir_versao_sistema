package br.gov.al.maceio.sishosp.hosp.dao;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.DataUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.UnidadeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ParametroBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;

import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadeDAO {

    Connection con = null;
    PreparedStatement ps = null;
    FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");

    public boolean gravarUnidade(UnidadeBean unidade) {
        Boolean retorno = false;
        Integer codUnidade = null;
        String sql = "INSERT INTO hosp.unidade(rua, bairro, numero, cep, cidade, " +
                " estado, complemento, ddd_1, telefone_1, ddd_2, telefone_2, email, site, matriz, ativo, nome,  cod_empresa) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?,?, true, ?, ?) returning id";

        try {
            con = ConnectionFactory.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, unidade.getRua());
            ps.setString(2, unidade.getBairro());
            if(unidade.getNumero() != null) {
                ps.setInt(3, unidade.getNumero());
            }
            else{
                ps.setNull(3, Types.NULL);
            }            
            ps.setString(4, unidade.getCep());
            ps.setString(5, unidade.getCidade());
            ps.setString(6, unidade.getEstado());
            ps.setString(7, unidade.getComplemento());
            if(unidade.getDdd1() != null) {
                ps.setInt(8, unidade.getDdd1());
            }
            else{
                ps.setNull(8, Types.NULL);
            }   
            
            if(unidade.getTelefone1() != null) {
                ps.setString(9, unidade.getTelefone1());
            }
            else{
                ps.setNull(9, Types.NULL);
            }  

            if(unidade.getDdd2() != null) {
                ps.setInt(10, unidade.getDdd2());
            }
            else{
                ps.setNull(10, Types.NULL);
            }
            if(unidade.getTelefone2() != null) {
                ps.setString(11, unidade.getTelefone2());
            }
            else{
                ps.setNull(11, Types.NULL);
            }
            if(unidade.getEmail() != null) {
                ps.setString(12, unidade.getEmail());
            }
            else{
                ps.setNull(12, Types.NULL);
            }
            if(unidade.getSite() != null) {
                ps.setString(13, unidade.getSite());
            }
            else{
                ps.setNull(13, Types.NULL);
            }
            if(unidade.getMatriz() != null) {
                ps.setBoolean(14, unidade.getMatriz());
            }
            else{
                ps.setNull(14, Types.NULL);
            }
            
            ps.setString(15, unidade.getNomeUnidade());
            

            ps.setInt(16, unidade.getCodEmpresa());
            

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                codUnidade = rs.getInt("id");
            }

            sql = "INSERT INTO hosp.parametro(motivo_padrao_desligamento_opm, opcao_atendimento, qtd_simultanea_atendimento_profissional, " +
                    "qtd_simultanea_atendimento_equipe, codunidade, horario_inicial, horario_final, intervalo, tipo_atendimento_terapia, " +
                    "programa_ortese_protese, grupo_ortese_protese) " +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);

            if(unidade.getParametro().getMotivoDesligamento() != null) {
                ps.setInt(1, unidade.getParametro().getMotivoDesligamento());
            }
            else{
                ps.setNull(1, Types.NULL);
            }
            if(unidade.getParametro().getOpcaoAtendimento() != null) {
                ps.setString(2, unidade.getParametro().getOpcaoAtendimento());
            }
            else{
                ps.setNull(2, Types.NULL);
            }
            if(unidade.getParametro().getQuantidadeSimultaneaProfissional() != null) {
                ps.setInt(3, unidade.getParametro().getQuantidadeSimultaneaProfissional());
            }
            else{
                ps.setNull(3, Types.NULL);
            }
            if(unidade.getParametro().getQuantidadeSimultaneaEquipe() != null) {
                ps.setInt(4, unidade.getParametro().getQuantidadeSimultaneaEquipe());
            }
            else{
                ps.setNull(4, Types.NULL);
            }
            ps.setInt(5, codUnidade);

            if(unidade.getParametro().getHorarioInicial() != null) {
                ps.setTime(6, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioInicial()));
            }
            else{
                ps.setNull(6, Types.NULL);
            }

            if(unidade.getParametro().getHorarioFinal() != null) {
                ps.setTime(7, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioFinal()));
            }
            else{
                ps.setNull(7, Types.NULL);
            }

            if(unidade.getParametro().getIntervalo() != null) {
                ps.setInt(8, unidade.getParametro().getIntervalo());
            }
            else{
                ps.setNull(8, Types.NULL);
            }
            if(unidade.getParametro().getTipoAtendimento().getIdTipo() != null) {
                ps.setInt(9, unidade.getParametro().getTipoAtendimento().getIdTipo());
            }
            else{
                ps.setNull(9, Types.NULL);
            }
            if((unidade.getParametro().getOrteseProtese().getPrograma() != null) && (unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma() != null)) {
                ps.setInt(10, unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma());
            }
            else{
                ps.setNull(10, Types.NULL);
            }
            if ((unidade.getParametro().getOrteseProtese().getGrupo() != null) && (unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo() != null) ){
                ps.setInt(11, unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo());
            }
            else{
                ps.setNull(11, Types.NULL);
            }

            ps.execute();

            con.commit();
            retorno = true;

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

    public List<UnidadeBean> listarUnidade() throws ProjetoException {

        List<UnidadeBean> lista = new ArrayList<>();
        String sql = "SELECT id,nome,  nome_principal, e.nome_fantasia, e.cnpj, unidade.rua, unidade.bairro, " +
                " unidade.numero, unidade.complemento, unidade.cep, unidade.cidade, unidade.estado, unidade.ddd_1, unidade.telefone_1, unidade.ddd_2, unidade.telefone_2, " +
                " unidade.email, unidade.site, matriz, unidade.ativo, case when matriz is true then 'Matriz' else 'Filial' end as tipo " +
                " FROM hosp.unidade join hosp.empresa e on e.cod_empresa = unidade.cod_empresa where unidade.ativo is true;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                UnidadeBean unidade = new UnidadeBean();
                unidade.setId(rs.getInt("id"));
                unidade.setNomeUnidade(rs.getString("nome"));
                unidade.setNomeEmpresa(rs.getString("nome_principal"));
                unidade.setNomeFantasia(rs.getString("nome_fantasia"));
                unidade.setCnpj(rs.getString("cnpj"));
                unidade.setRua(rs.getString("rua"));
                unidade.setBairro(rs.getString("bairro"));
                unidade.setNumero(rs.getInt("numero"));
                unidade.setComplemento(rs.getString("complemento"));
                unidade.setCep(rs.getString("cep"));
                unidade.setCidade(rs.getString("cidade"));
                unidade.setEstado(rs.getString("estado"));
                unidade.setDdd1(rs.getInt("ddd_1"));
                unidade.setTelefone1(rs.getString("telefone_1"));
                unidade.setDdd2(rs.getInt("ddd_2"));
                unidade.setTelefone2(rs.getString("telefone_2"));
                unidade.setEmail(rs.getString("email"));
                unidade.setSite(rs.getString("site"));
                unidade.setMatriz(rs.getBoolean("matriz"));
                unidade.setAtivo(rs.getBoolean("ativo"));
                unidade.setTipoString(rs.getString("tipo"));
                unidade.setParametro(carregarParametro(unidade.getId(), con));

                lista.add(unidade);
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

    public Boolean alterarUnidade(UnidadeBean unidade) {

        Boolean retorno = false;
        String sql = "UPDATE hosp.unidade SET   rua=?, " +
                " bairro=?, numero=?, cep=?, cidade=?, estado=?, ddd_1=?, telefone_1=?, " +
                " ddd_2=?, telefone_2=?, email=?, site=?, matriz=?, complemento=?, nome=?,  cod_empresa=? " +
                " WHERE id = ?;";
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, unidade.getRua());
            ps.setString(2, unidade.getBairro());
            if (unidade.getNumero()!=null){
                ps.setInt(3, unidade.getNumero());
            }
            else{
                ps.setNull(3, Types.NULL);
            }   

            ps.setString(4, unidade.getCep());
            ps.setString(5, unidade.getCidade());
            ps.setString(6, unidade.getEstado());
            if (unidade.getDdd1()!=null){
                ps.setInt(7, unidade.getDdd1());
            }
            else{
                ps.setNull(7, Types.NULL);
            }     
            
            if (unidade.getTelefone1()!=null){
                ps.setString(8, unidade.getTelefone1());
            }
            else{
                ps.setNull(8, Types.NULL);
            }     
            
            if (unidade.getDdd2()!=null){
                ps.setInt(9, unidade.getDdd2());
            }
            else{
                ps.setNull(9, Types.NULL);
            }     
            
            if (unidade.getTelefone2()!=null){
                ps.setString(10, unidade.getTelefone2());
            }
            else{
                ps.setNull(10, Types.NULL);
            }                

            
            
            ps.setString(11, unidade.getEmail());
            ps.setString(12, unidade.getSite());
            ps.setBoolean(13, unidade.getMatriz());
            ps.setString(14, unidade.getComplemento());
            ps.setString(15, unidade.getNomeUnidade());
            ps.setInt(16, unidade.getCodEmpresa());
            ps.setInt(17, unidade.getId());
            ps.executeUpdate();

            sql = "UPDATE hosp.parametro SET motivo_padrao_desligamento_opm = ?, opcao_atendimento = ?, " +
                    "qtd_simultanea_atendimento_profissional = ?, qtd_simultanea_atendimento_equipe = ?, " +
                    "horario_inicial = ?, horario_final = ?, intervalo = ?, tipo_atendimento_terapia = ?, " +
                    "programa_ortese_protese = ?, grupo_ortese_protese = ? " +
                    "WHERE codunidade = ?";

            ps = con.prepareStatement(sql);

            ps.setInt(1, unidade.getParametro().getMotivoDesligamento());
            ps.setString(2, unidade.getParametro().getOpcaoAtendimento());
            ps.setInt(3, unidade.getParametro().getQuantidadeSimultaneaProfissional());
            ps.setInt(4, unidade.getParametro().getQuantidadeSimultaneaEquipe());
            ps.setTime(5, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioInicial()));
            ps.setTime(6, DataUtil.transformarDateEmTime(unidade.getParametro().getHorarioFinal()));
            ps.setInt(7, unidade.getParametro().getIntervalo());
            if ((unidade.getParametro().getTipoAtendimento() != null) && (unidade.getParametro().getTipoAtendimento().getIdTipo() != null)){
                ps.setInt(8, unidade.getParametro().getTipoAtendimento().getIdTipo());
            }
            else{
                ps.setNull(8, Types.NULL);
            }   
            
            if ((unidade.getParametro().getOrteseProtese().getPrograma() != null) && (unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma() != null)){
                ps.setInt(9, unidade.getParametro().getOrteseProtese().getPrograma().getIdPrograma());
            }
            else{
                ps.setNull(9, Types.NULL);
            }   
            

            if (( unidade.getParametro().getOrteseProtese().getGrupo() != null) && ( unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo() != null)){
                ps.setInt(10,  unidade.getParametro().getOrteseProtese().getGrupo().getIdGrupo());
            }
            else{
                ps.setNull(10, Types.NULL);
            }               

            ps.setInt(11, unidade.getId());
            ps.executeUpdate();

            con.commit();
            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean desativarUnidade(UnidadeBean unidade) {

        Boolean retorno = false;
        String sql = "update hosp.unidade set ativo = false where id = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, unidade.getId());
            stmt.execute();

            con.commit();
            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return retorno;
        }
    }

    public UnidadeBean buscarUnidadePorId(Integer id) throws ProjetoException {

        UnidadeBean unidade = new UnidadeBean();
        String sql = "SELECT id,nome, e.cod_empresa,nome_principal, e.nome_fantasia, cnpj, unidade.rua, unidade.bairro, " +
                " unidade.numero, unidade.complemento, unidade.cep, unidade.cidade, unidade.estado, unidade.ddd_1, unidade.telefone_1, unidade.ddd_2, unidade.telefone_2, " +
                " unidade.email, unidade.site, unidade.matriz, unidade.ativo " +
                " FROM hosp.unidade join hosp.empresa e on e.cod_empresa = unidade.cod_empresa where unidade.id = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

            	unidade.setId(rs.getInt("id"));
            	unidade.setNomeUnidade(rs.getString("nome"));
            	unidade.setNomeEmpresa(rs.getString("nome_principal"));
            	unidade.setNomeFantasia(rs.getString("nome_fantasia"));
            	unidade.setCodEmpresa(rs.getInt("cod_empresa"));            	
            	unidade.setCnpj(rs.getString("cnpj"));
                unidade.setRua(rs.getString("rua"));
                unidade.setBairro(rs.getString("bairro"));
                unidade.setNumero(rs.getInt("numero"));
                unidade.setComplemento(rs.getString("complemento"));
                unidade.setCep(rs.getString("cep"));
                unidade.setCidade(rs.getString("cidade"));
                unidade.setEstado(rs.getString("estado"));
                unidade.setDdd1(rs.getInt("ddd_1"));
                unidade.setTelefone1(rs.getString("telefone_1"));
                unidade.setDdd2(rs.getInt("ddd_2"));
                unidade.setTelefone2(rs.getString("telefone_2"));
                unidade.setEmail(rs.getString("email"));
                unidade.setSite(rs.getString("site"));
                unidade.setMatriz(rs.getBoolean("matriz"));
                unidade.setAtivo(rs.getBoolean("ativo"));
                unidade.setParametro(carregarParametro(id, con));
                unidade.setNomeUnidade(rs.getString("nome"));

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
        return unidade;
    }

    public ParametroBean carregarParametro(Integer id, Connection conAuxiliar) throws ProjetoException {

        ParametroBean parametro = new ParametroBean();

        String sql = "SELECT id, motivo_padrao_desligamento_opm, opcao_atendimento, qtd_simultanea_atendimento_profissional, qtd_simultanea_atendimento_equipe, " +
                "horario_inicial, horario_final, intervalo, tipo_atendimento_terapia, programa_ortese_protese, grupo_ortese_protese " +
                " FROM hosp.parametro where codunidade = ?;";

        try {
            PreparedStatement ps = conAuxiliar.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                parametro.setMotivoDesligamento(rs.getInt("motivo_padrao_desligamento_opm"));
                parametro.setOpcaoAtendimento(rs.getString("opcao_atendimento"));
                parametro.setQuantidadeSimultaneaProfissional(rs.getInt("qtd_simultanea_atendimento_profissional"));
                parametro.setQuantidadeSimultaneaEquipe(rs.getInt("qtd_simultanea_atendimento_equipe"));
                parametro.setHorarioInicial(rs.getTime("horario_inicial"));
                parametro.setHorarioFinal(rs.getTime("horario_final"));
                parametro.setIntervalo(rs.getInt("intervalo"));
                parametro.getTipoAtendimento().setIdTipo(rs.getInt("tipo_atendimento_terapia"));
                if(!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getInt("programa_ortese_protese"))) {
                    parametro.getOrteseProtese().setPrograma(new ProgramaDAO().listarProgramaPorIdComConexao(rs.getInt("programa_ortese_protese"), conAuxiliar));
                }
                else{
                    parametro.getOrteseProtese().setPrograma(new ProgramaBean());
                }
                if(!VerificadorUtil.verificarSeObjetoNuloOuZero(rs.getInt("grupo_ortese_protese"))) {
                    parametro.getOrteseProtese().setGrupo(new GrupoDAO().listarGrupoPorIdComConexao(rs.getInt("grupo_ortese_protese"), conAuxiliar));
                }
                else{
                    parametro.getOrteseProtese().setGrupo(new GrupoBean());
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return parametro;
    }

    public String carregarOpcaoAtendimentoDaUnidade() throws ProjetoException {

        String retorno = null;

        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        String sql = "SELECT opcao_atendimento FROM hosp.parametro where codunidade = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                retorno = rs.getString("opcao_atendimento");
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
        return retorno;
    }

    public ParametroBean carregarDetalhesAtendimentoDaUnidade() {

        ParametroBean parametro = new ParametroBean();

        String sql = "SELECT qtd_simultanea_atendimento_profissional, qtd_simultanea_atendimento_equipe, " +
                "horario_inicial, horario_final, intervalo " +
                " FROM hosp.parametro where codunidade = ?;";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user_session.getUnidade().getId());
	            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                parametro.setQuantidadeSimultaneaProfissional(rs.getInt("qtd_simultanea_atendimento_profissional"));
                parametro.setQuantidadeSimultaneaEquipe(rs.getInt("qtd_simultanea_atendimento_equipe"));
                parametro.setHorarioInicial(rs.getTime("horario_inicial"));
                parametro.setHorarioFinal(rs.getTime("horario_final"));
                parametro.setIntervalo(rs.getInt("intervalo"));

            }
        } catch (SQLException | ProjetoException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return parametro;
    }

    public List<UnidadeBean> carregarUnidadesDoFuncionario() {

        List<UnidadeBean> lista = new ArrayList<>();

        String sql = "SELECT u.nome, fu.cod_unidade " +
                "FROM hosp.funcionario_unidades fu " +
                "JOIN hosp.unidade u ON (fu.cod_unidade = u.id) " +
                "WHERE fu.cod_funcionario = ? " +
                "UNION  " +
                "SELECT u.nome, f.codunidade " +
                "FROM acl.funcionarios f " +
                "JOIN hosp.unidade u ON (f.codunidade = u.id) " +
                "WHERE f.id_funcionario = ?";

        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, user_session.getId());
            ps.setLong(2, user_session.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UnidadeBean unidade = new UnidadeBean();
                unidade.setId(rs.getInt("cod_unidade"));
                unidade.setNomeUnidade(rs.getString("nome"));
                lista.add(unidade);
            }

        } catch (SQLException | ProjetoException ex) {
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

}
