package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.Telefone;

public class PacienteDAO {
    private Connection conexao = null;
    private PreparedStatement ps = null;

    FuncionarioBean user_session = (FuncionarioBean) FacesContext
            .getCurrentInstance().getExternalContext().getSessionMap()
            .get("obj_usuario");
    
    public Boolean cadastrarPaciente(PacienteBean paciente, Boolean bairroExiste) {
        boolean retorno = false;

        Integer idPaciente = null;

        try {
            conexao = ConnectionFactory.getConnection();

            if ( (bairroExiste!=null) && (!bairroExiste)) {
                EnderecoDAO enderecoDAO = new EnderecoDAO();
                paciente.getEndereco().setCodbairro(enderecoDAO.inserirNovoBairro(paciente.getEndereco(), conexao));
            }

            String sql = "insert into hosp.pacientes "
                    + " (dtacadastro, "
                    + "nome, dtanascimento, estcivil, sexo, sangue, pai, mae, conjuge, codraca, " //1 ao 9
                    + "cep, uf, logradouro, numero, complemento, referencia, rg, oe, dtaexpedicaorg, cpf, "   //10 ao 19
                    + "cns, protreab, reservista, ctps, serie, pis, cartorio, regnascimento, livro, folha, "  //20 ao 29
                    + "dtaregistro, id_escolaridade, id_escola, id_profissao, trabalha, localtrabalha, codparentesco, nomeresp, rgresp, "  //30 ao 38
                    + "cpfresp, dtanascimentoresp, id_encaminhado, id_formatransporte ,deficiencia, " //39 a 43
                    + "codmunicipio, deficienciafisica, deficienciamental, deficienciaauditiva, deficienciavisual, " //44 ao 48
                    + "deficienciamultipla, codbairro, email, facebook, instagram, nome_social, necessita_nome_social, id_religiao, id_genero, codigo_usuario_cadastro, matricula)" //49 ao 58
                    + " values (CURRENT_TIMESTAMP, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "  //1 ao 10
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "  //11 ao 20
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "  //21 ao 30
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "  //31 ao 40
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "  //41 ao 50
                    + "?, ?, ?, ?, ?, ?,?,?, ?) returning id_paciente"; //51 ao 58

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, paciente.getNome().toUpperCase().trim());
            stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento()
                    .getTime()));
            if (paciente.getEstadoCivil() == null) {
                stmt.setNull(3, Types.CHAR);
            } else {
                stmt.setString(3, paciente.getEstadoCivil());
            }

            stmt.setString(4, paciente.getSexo());

            if (paciente.getSangue() == null) {
                stmt.setNull(5, Types.CHAR);
            } else {
                stmt.setString(5, paciente.getSangue());
            }
            if (paciente.getNomePai() == null) {
                stmt.setNull(6, Types.CHAR);
            } else {
                stmt.setString(6, paciente.getNomePai().toUpperCase().trim());
            }
            stmt.setString(7, paciente.getNomeMae().toUpperCase().trim());
            if (paciente.getConjuge() == null) {
                stmt.setNull(8, Types.CHAR);
            } else {
                stmt.setString(8, paciente.getConjuge().toUpperCase().trim());
            }
            if (paciente.getCodRaca() != null) {
                stmt.setInt(9, paciente.getCodRaca());
            } else {
                stmt.setNull(9, Types.NULL);
            }
            if (paciente.getEndereco().getCep() == null) {
                stmt.setNull(10, Types.INTEGER);
            } else {
                stmt.setString(10, paciente.getEndereco().getCep());
            }
            if (paciente.getEndereco().getUf() == null) {
                stmt.setNull(11, Types.CHAR);
            } else {
                stmt.setString(11, paciente.getEndereco().getUf().toUpperCase()
                        .trim());
            }
            stmt.setString(12, paciente.getEndereco().getLogradouro()
                    .toUpperCase().trim());
            stmt.setString(13, paciente.getEndereco().getNumero().toUpperCase()
                    .trim());
            if (paciente.getEndereco().getComplemento() == null) {
                stmt.setNull(14, Types.CHAR);
            } else {
                stmt.setString(14, paciente.getEndereco().getComplemento()
                        .toUpperCase().trim());
            }
            if (paciente.getEndereco().getReferencia() == null) {
                stmt.setNull(15, Types.CHAR);
            } else {
                stmt.setString(15, paciente.getEndereco().getReferencia()
                        .toUpperCase().trim());
            }
            if (paciente.getRg() == null) {
                stmt.setNull(16, Types.CHAR);
            } else {
                stmt.setString(16, paciente.getRg().toUpperCase().trim());
            }
            if (paciente.getOe() == null) {
                stmt.setNull(17, Types.CHAR);
            } else {
                stmt.setString(17, paciente.getOe().toUpperCase().trim());
            }
            if (paciente.getDataExpedicao1() == null) {
                stmt.setNull(18, Types.DATE);
            } else {
                stmt.setDate(18, new java.sql.Date(paciente.getDataExpedicao1()
                        .getTime()));
            }
            if ((paciente.getCpf() == null) || (paciente.getCpf().equals(""))) {
                stmt.setNull(19, Types.CHAR);
            } else {
                stmt.setString(19, paciente.getCpf().replaceAll("[^0-9]", ""));
            }
            stmt.setString(20, paciente.getCns().toUpperCase().trim());
            if (paciente.getProtant() == null) {
                stmt.setNull(21, Types.INTEGER);
            } else {
                stmt.setInt(21, paciente.getProtant());
            }
            if (paciente.getReservista() == null) {
                stmt.setNull(22, Types.CHAR);
            } else {
                stmt.setString(22, paciente.getReservista().toUpperCase()
                        .trim());
            }
            if (paciente.getCtps() == null) {
                stmt.setNull(23, Types.INTEGER);
            } else {
                stmt.setInt(23, paciente.getCtps());
            }
            if (paciente.getSerie() == null) {
                stmt.setNull(24, Types.NULL);
            } else {
                stmt.setString(24, paciente.getSerie());
            }
            if (paciente.getPis() == null) {
                stmt.setNull(25, Types.CHAR);
            } else {
                stmt.setString(25, paciente.getPis().toUpperCase().trim());
            }
            if (paciente.getCartorio() == null) {
                stmt.setNull(26, Types.CHAR);
            } else {
                stmt.setString(26, paciente.getCartorio().toUpperCase().trim());
            }
            if (paciente.getNumeroCartorio() == null) {
                stmt.setNull(27, Types.CHAR);
            } else {
                stmt.setString(27, paciente.getNumeroCartorio().toUpperCase()
                        .trim());
            }
            if (paciente.getLivro() == null) {
                stmt.setNull(28, Types.CHAR);
            } else {
                stmt.setString(28, paciente.getLivro().toUpperCase().trim());
            }
            if (paciente.getFolha() == null) {
                stmt.setNull(29, Types.INTEGER);
            } else {
                stmt.setString(29, paciente.getFolha());
            }
            if (paciente.getDataExpedicao2() == null) {
                stmt.setNull(30, Types.DATE);
            } else {
                stmt.setDate(30, new java.sql.Date(paciente.getDataExpedicao2()
                        .getTime()));
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEscolaridade()))
                stmt.setInt(31, paciente.getEscolaridade().getCodescolaridade());
            else
                stmt.setNull(31, Types.INTEGER);

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEscola()))
                stmt.setInt(32, paciente.getEscola().getCodEscola());
            else
                stmt.setNull(32, Types.INTEGER);

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getProfissao())) {
                if (VerificadorUtil.verificarSeObjetoNulo(paciente.getProfissao().getCodprofissao())) {
                    stmt.setNull(33, Types.INTEGER);
                } else {
                    stmt.setInt(33, paciente.getProfissao().getCodprofissao());
                }
            } else {
                stmt.setNull(33, Types.INTEGER);
            }

            if (paciente.getTrabalha() == null) {
                stmt.setNull(34, Types.CHAR);
            } else {
                stmt.setString(34, paciente.getTrabalha().toUpperCase().trim());
            }
            if (paciente.getLocaltrabalha() == null) {
                stmt.setNull(35, Types.CHAR);
            } else {
                stmt.setString(35, paciente.getLocaltrabalha().toUpperCase()
                        .trim());
            }
            if (paciente.getCodparentesco() == null) {
                stmt.setNull(36, Types.INTEGER);
            } else {
                stmt.setInt(36, paciente.getCodparentesco());
            }
            if (paciente.getNomeresp() == null) {
                stmt.setNull(37, Types.CHAR);
            } else {
                stmt.setString(37, paciente.getNomeresp().toUpperCase().trim());
            }
            if (paciente.getRgresp() == null) {
                stmt.setNull(38, Types.CHAR);
            } else {
                stmt.setString(38, paciente.getRgresp().toUpperCase().trim());
            }
            if (paciente.getCpfresp() == null) {
                stmt.setNull(39, Types.CHAR);
            } else {
                stmt.setString(39,
                        paciente.getCpfresp().replaceAll("[^0-9]", ""));
            }
            if (paciente.getDataNascimentoresp() == null) {
                stmt.setNull(40, Types.DATE);
            } else {
                stmt.setDate(40, new java.sql.Date(paciente
                        .getDataNascimentoresp().getTime()));
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEncaminhado()))
                stmt.setInt(41, paciente.getEncaminhado().getCodencaminhado());
            else
                stmt.setNull(41, Types.INTEGER);

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getFormatransporte()))
                stmt.setInt(42, paciente.getFormatransporte()
                        .getCodformatransporte());
            else
                stmt.setNull(42, Types.INTEGER);

            stmt.setString(43, paciente.getDeficiencia().toUpperCase().trim());
            
            if (paciente.getEndereco().getCodmunicipio() != null) {
                stmt.setInt(44, paciente.getEndereco().getCodmunicipio());
            } else {
            	stmt.setNull(44, Types.NULL);
            }


            if (paciente.getDeficienciaFisica() != null) {
                stmt.setBoolean(45, paciente.getDeficienciaFisica());
            } else {
                stmt.setBoolean(45, false);
            }

            if (paciente.getDeficienciaMental() != null) {
                stmt.setBoolean(46, paciente.getDeficienciaMental());
            } else {
                stmt.setBoolean(46, false);
            }

            if (paciente.getDeficienciaAuditiva() != null) {
                stmt.setBoolean(47, paciente.getDeficienciaAuditiva());
            } else {
                stmt.setBoolean(47, false);
            }

            if (paciente.getDeficienciaVisual() != null) {
                stmt.setBoolean(48, paciente.getDeficienciaVisual());
            } else {
                stmt.setBoolean(48, false);
            }

            if (paciente.getDeficienciaMultipla() != null) {
                stmt.setBoolean(49, paciente.getDeficienciaMultipla());
            } else {
                stmt.setBoolean(49, false);
            }

            if (paciente.getEndereco().getCodbairro() != null) {
                stmt.setInt(50,paciente.getEndereco().getCodbairro());
            } else {
            	stmt.setNull(50, Types.NULL);
            }
            
            if (paciente.getEmail() == null) {
                stmt.setNull(51, Types.CHAR);
            } else {
                stmt.setString(51, paciente.getEmail());
            }

            if (paciente.getFacebook() == null) {
                stmt.setNull(52, Types.CHAR);
            } else {
                stmt.setString(52, paciente.getFacebook());
            }

            if (paciente.getInstagram() == null) {
                stmt.setNull(53, Types.CHAR);
            } else {
                stmt.setString(53, paciente.getInstagram());
            }

            if (paciente.getNomeSocial() == null) {
                stmt.setNull(54, Types.CHAR);
            } else {
                stmt.setString(54, paciente.getNomeSocial().toUpperCase());
            }

            stmt.setBoolean(55, paciente.getNecessitaNomeSocial());

            if (paciente.getReligiao().getId() == null) {
                stmt.setNull(56, Types.NULL);
            } else {
                stmt.setInt(56, paciente.getReligiao().getId());
            }

            if (paciente.getGenero().getId() == null) {
                stmt.setNull(57, Types.NULL);
            } else {
                stmt.setInt(57, paciente.getGenero().getId());
            }

            stmt.setLong(58, user_session.getId());
            
            if (paciente.getMatricula() == null) {
                stmt.setNull(59, Types.CHAR);
            } else {
                stmt.setString(59, paciente.getMatricula().toUpperCase());
            }
            
            
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                idPaciente = set.getInt("id_paciente");
            }

            if (inserirTelefone(paciente.getListaTelefones(), idPaciente, conexao)) {
                conexao.commit();
                retorno = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean alterarPaciente(PacienteBean paciente, boolean bairroExiste) {
        boolean retorno = false;

        try {
            conexao = ConnectionFactory.getConnection();

            if (!bairroExiste) {
                EnderecoDAO enderecoDAO = new EnderecoDAO();
                paciente.getEndereco().setCodbairro(enderecoDAO.inserirNovoBairro(paciente.getEndereco(), conexao));
            }

            String sql = "update hosp.pacientes set nome = ?, dtanascimento = ?, estcivil = ?, sexo = ? , sangue = ?, pai = ? " //1 ao 6
                    + ", mae = ?, conjuge = ?, codraca = ?, cep = ?, uf = ?, logradouro = ?, numero = ?" //7 ao 13
                    + ", complemento = ?, referencia = ? , rg = ?,  oe = ?, dtaexpedicaorg = ?, cpf = ?, cns = ?, protreab = ?" //14 ao 21
                    + ", reservista = ?, ctps = ?, serie = ?, pis = ?, cartorio = ?, regnascimento = ?, livro = ?, folha = ?, dtaregistro = ?" //22 ao 30
                    + ", id_escolaridade = ?, id_escola = ?, id_profissao = ?, trabalha = ?, localtrabalha = ?" //31 ao 35
                    + ", codparentesco = ?, nomeresp = ?, rgresp = ?, cpfresp = ?, dtanascimentoresp = ?, id_encaminhado = ?" //36 ao 41
                    + ", id_formatransporte = ?, deficiencia = ?, codmunicipio = ?" //42 ao 44
                    + ", deficienciafisica = ?, deficienciamental = ?, deficienciaauditiva = ?, deficienciavisual = ?, deficienciamultipla = ?" //45 ao 49
                    + ", email = ?, facebook = ?, instagram = ?, nome_social = ?, necessita_nome_social = ?, id_religiao =?, codbairro=?, id_genero = ?, matricula=? " //50 ao 58
                    + " where id_paciente = ?"; //59

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, paciente.getNome());
            stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento()
                    .getTime()));
            stmt.setString(3, paciente.getEstadoCivil());
            stmt.setString(4, paciente.getSexo());
            stmt.setString(5, paciente.getSangue());
            stmt.setString(6, paciente.getNomePai().toUpperCase());
            stmt.setString(7, paciente.getNomeMae().toUpperCase());
            stmt.setString(8, paciente.getConjuge().toUpperCase());
            stmt.setInt(9, paciente.getCodRaca());
            stmt.setString(10, paciente.getEndereco().getCep());
            stmt.setString(11, paciente.getEndereco().getUf());
            stmt.setString(12, paciente.getEndereco().getLogradouro().toUpperCase());
            stmt.setString(13, paciente.getEndereco().getNumero().toUpperCase());
            stmt.setString(14, paciente.getEndereco().getComplemento().toUpperCase());
            stmt.setString(15, paciente.getEndereco().getReferencia().toUpperCase());
            stmt.setString(16, paciente.getRg());
            stmt.setString(17, paciente.getOe());

            if (paciente.getDataExpedicao1() != null) {
                stmt.setDate(18, new java.sql.Date(paciente.getDataExpedicao1()
                        .getTime()));
            } else {
                stmt.setNull(18, Types.DATE);
            }
            
            if ((paciente.getCpf() == null) || (paciente.getCpf() .equals(""))) {
                stmt.setNull(19, Types.NULL);
            } else {
            	stmt.setString(19, paciente.getCpf().replaceAll("[^0-9]", ""));
            }
            
            stmt.setString(20, paciente.getCns());

            if (paciente.getProtant() == null) {
                stmt.setNull(21, Types.INTEGER);
            } else {
                stmt.setInt(21, paciente.getProtant());
            }

            stmt.setString(22, paciente.getReservista().toUpperCase());
            if (paciente.getCtps() != null) {
                stmt.setInt(23, paciente.getCtps());
            } else {
                stmt.setNull(23, Types.NULL);
            }
            stmt.setString(24, paciente.getSerie());
            stmt.setString(25, paciente.getPis());
            stmt.setString(26, paciente.getCartorio().toUpperCase());
            stmt.setString(27, paciente.getNumeroCartorio().toUpperCase());
            stmt.setString(28, paciente.getLivro().toUpperCase());
            stmt.setString(29, paciente.getFolha().toUpperCase());

            if (paciente.getDataExpedicao2() != null) {
                stmt.setDate(30, new java.sql.Date(paciente.getDataExpedicao2()
                        .getTime()));
            } else {
                stmt.setNull(30, Types.DATE);
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEscolaridade()))
                stmt.setInt(31, paciente.getEscolaridade().getCodescolaridade());
            else
                stmt.setNull(31, Types.INTEGER);

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEscola()))
                stmt.setInt(32, paciente.getEscola().getCodEscola());
            else
                stmt.setNull(32, Types.INTEGER);

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getProfissao())) {
                if (VerificadorUtil.verificarSeObjetoNulo(paciente.getProfissao().getCodprofissao())) {
                    stmt.setNull(33, Types.INTEGER);
                } else {
                    stmt.setInt(33, paciente.getProfissao().getCodprofissao());
                }
            } else {
                stmt.setNull(33, Types.INTEGER);
            }

            stmt.setString(34, paciente.getTrabalha());
            stmt.setString(35, paciente.getLocaltrabalha());

            if (paciente.getCodparentesco() != null) {
                stmt.setInt(36, paciente.getCodparentesco());
            } else {
                stmt.setNull(36, Types.INTEGER);
            }

            stmt.setString(37, paciente.getNomeresp());
            stmt.setString(38, paciente.getRgresp());
            stmt.setString(39, paciente.getCpfresp().replaceAll("[^0-9]", ""));

            if (paciente.getDataNascimentoresp() != null) {
                stmt.setDate(40, new java.sql.Date(paciente
                        .getDataNascimentoresp().getTime()));
            } else {
                stmt.setNull(40, Types.DATE);
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEncaminhado()))
                stmt.setInt(41, paciente.getEncaminhado().getCodencaminhado());
            else
                stmt.setNull(41, Types.INTEGER);

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getFormatransporte()))
                stmt.setInt(42, paciente.getFormatransporte()
                        .getCodformatransporte());
            else
                stmt.setNull(42, Types.INTEGER);

            stmt.setString(43, paciente.getDeficiencia());
            
            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEndereco().getCodmunicipio()))
                stmt.setInt(44, paciente.getEndereco().getCodmunicipio());
            else
                stmt.setNull(44, Types.INTEGER);


            stmt.setBoolean(45, paciente.getDeficienciaFisica());

            stmt.setBoolean(46, paciente.getDeficienciaMental());

            stmt.setBoolean(47, paciente.getDeficienciaAuditiva());

            stmt.setBoolean(48, paciente.getDeficienciaVisual());

            stmt.setBoolean(49, paciente.getDeficienciaMultipla());

            stmt.setString(50, paciente.getEmail());

            stmt.setString(51, paciente.getFacebook());

            stmt.setString(52, paciente.getInstagram());

            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(paciente.getNomeSocial())) {
                stmt.setNull(53, Types.CHAR);
            } else {
                stmt.setString(53, paciente.getNomeSocial().toUpperCase());
            }

            stmt.setBoolean(54, paciente.getNecessitaNomeSocial());

            if (paciente.getReligiao().getId() == null) {
                stmt.setNull(55, Types.NULL);
            } else {
                stmt.setInt(55, paciente.getReligiao().getId());
            }

            if (!VerificadorUtil.verificarSeObjetoNulo(paciente.getEndereco().getCodbairro()))
                stmt.setInt(56, paciente.getEndereco().getCodbairro());
            else
                stmt.setNull(56, Types.INTEGER);

            
            
            if (paciente.getGenero().getId() == null) {
                stmt.setNull(57, Types.NULL);
            } else {
                stmt.setInt(57, paciente.getGenero().getId());
            }            

            
            if (paciente.getMatricula() == null) {
                stmt.setNull(58, Types.NULL);
            } else {
            	stmt.setString(58, paciente.getMatricula().toUpperCase());
            }  

            
            
            stmt.setLong(59, paciente.getId_paciente());

            stmt.executeUpdate();

            if (deletarTelefone(paciente.getId_paciente(), conexao)) {
                if (inserirTelefone(paciente.getListaTelefones(), paciente.getId_paciente(), conexao)) {
                    conexao.commit();
                    retorno = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean excluir(PacienteBean paciente) {
        boolean retorno = false;
        String sql =""; 
        try {
        	sql = "delete from hosp.telefone_paciente where id_paciente = ?";
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, paciente.getId_paciente());
            stmt.executeUpdate();
        	
        	sql = "delete from hosp.pacientes where id_paciente = ?";
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, paciente.getId_paciente());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public ArrayList<PacienteBean> listaPacientes() throws ProjetoException {

        String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.cpf, pacientes.cns "
                + " from hosp.pacientes where id_paciente is not null order by pacientes.nome ";

        ArrayList<PacienteBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                PacienteBean p = new PacienteBean();

                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
                lista.add(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }

    public List<PacienteBean> buscarPacienteAgenda(String tipo, String conteudo)
            throws ProjetoException {

        String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
                + "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf,  "
                + "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, "
                + "pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
                + "pacientes.protreab, "
                + "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
                + "pacientes.folha, pacientes.dtaregistro, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
                + "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
                + "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
                + "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.email, pacientes.facebook, pacientes.instagram, "
                + "escolaridade.descescolaridade, escola.descescola, profissao.descprofissao, "
                + " pacientes.nome_social, pacientes.necessita_nome_social, pacientes.id_genero, "
                + " encaminhado.descencaminhado, formatransporte.descformatransporte "
                + "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
                + " left join hosp.escola on pacientes.id_escola=escola.id_escola "
                + "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
                + "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
                + "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte where";

        List<PacienteBean> listaP = new ArrayList<PacienteBean>();
        try {
            conexao = ConnectionFactory.getConnection();

            PacienteBean p = null;

            if (tipo.equals("CPF")) {
                sql += " cpf = ? ;";
                ps = conexao.prepareStatement(sql);
                ps.setString(1, conteudo.toUpperCase());
            }
            if (tipo.equals("NOME")) {
                sql += " nome LIKE ? ;";
                ps = conexao.prepareStatement(sql);
                ps.setString(1, '%' + conteudo.toUpperCase() + '%');
            }
            if (tipo.equals("PRONTVELHO")) {
                sql += " protreab = ? ;";
                ps = conexao.prepareStatement(sql);
                Integer prontVelho = null;
                try {
                    prontVelho = Integer.parseInt(conteudo);
                } catch (Exception e) {
                    return listaP;
                }

                if (prontVelho != null)
                    ps.setInt(1, prontVelho);
            }
            if (tipo.equals("PRONTNOVO")) {
                sql += " id_paciente = ? ;";
                ps = conexao.prepareStatement(sql);
                Long prontNovo = null;
                try {
                    prontNovo = Long.parseLong(conteudo);
                } catch (Exception e) {
                    return listaP;
                }
                if (prontNovo != null)
                    ps.setLong(1, prontNovo);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                p = new PacienteBean();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setDtanascimento(rs.getDate("dtanascimento"));
                p.setEstadoCivil(rs.getString("estcivil"));
                p.setSexo(rs.getString("sexo"));
                p.getGenero().setId(rs.getInt("id_genero"));
                p.setSangue(rs.getString("sangue"));
                p.setNomePai(rs.getString("pai"));
                p.setNomeMae(rs.getString("mae"));
                p.setConjuge(rs.getString("conjuge"));
                p.setCodRaca(rs.getInt("codraca"));
                p.getEndereco().setCep(rs.getString("cep"));
                p.getEndereco().setUf(rs.getString("uf"));
                p.getEndereco().setLogradouro(rs.getString("logradouro"));
                p.getEndereco().setNumero(rs.getString("numero"));
                p.getEndereco().setComplemento(rs.getString("complemento"));
                p.getEndereco().setReferencia(rs.getString("referencia"));
                p.setRg(rs.getString("rg").toUpperCase());
                p.setOe(rs.getString("oe").toUpperCase());
                p.setDataExpedicao1(rs.getDate("dtaexpedicaorg"));
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
                p.setProtant(rs.getInt("protreab"));
                p.setReservista(rs.getString("reservista"));
                p.setCtps(rs.getInt("ctps"));
                p.setSerie(rs.getString("serie"));
                p.setPis(rs.getString("pis"));
                p.setCartorio(rs.getString("cartorio"));
                p.setNumeroCartorio(rs.getString("regnascimento"));
                p.setLivro(rs.getString("livro"));
                p.setFolha(rs.getString("folha"));
                p.setDataExpedicao2(rs.getDate("dtaregistro"));
                p.setEmail(rs.getString("email"));
                p.setFacebook(rs.getString("facebook"));
                p.setInstagram(rs.getString("instagram"));
                p.getEscolaridade().setCodescolaridade(
                        rs.getInt("id_escolaridade"));
                p.getEscolaridade().setDescescolaridade(
                        rs.getString("descescolaridade"));
                p.getEscola().setCodEscola(rs.getInt("id_escola"));
                p.getProfissao().setCodprofissao(rs.getInt("id_profissao"));
                p.setTrabalha(rs.getString("trabalha"));
                p.setLocaltrabalha(rs.getString("localtrabalha"));
                p.setCodparentesco(rs.getInt("codparentesco"));
                p.setNomeresp(rs.getString("nomeresp"));
                p.setRgresp(rs.getString("rgresp"));
                p.setCpfresp(rs.getString("cpfresp"));
                p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));
                p.getEncaminhado().setCodencaminhado(
                        rs.getInt("id_encaminhado"));
                p.getFormatransporte().setCodformatransporte(
                        rs.getInt("id_formatransporte"));
                p.setDeficiencia(rs.getString("deficiencia"));
                p.getEscola().setDescescola(rs.getString("descescola"));
                p.getProfissao()
                        .setDescprofissao(rs.getString("descprofissao"));
                p.getEncaminhado().setDescencaminhado(
                        rs.getString("descencaminhado"));
                p.getFormatransporte().setDescformatransporte(
                        rs.getString("descformatransporte"));
                p.setNomeSocial(rs.getString("nome_social"));
                p.setNecessitaNomeSocial(rs.getBoolean("necessita_nome_social"));
                listaP.add(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listaP;
    }

    public PacienteBean listarPacientePorID(int id) throws ProjetoException {
        PacienteBean p = new PacienteBean();

        String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
                + "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf, municipio.codigo, municipio.nome descmunicipio, "
                + "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, "
                + "pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
                + "pacientes.protreab, pacientes.codbairro, "
                + "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
                + "pacientes.folha, pacientes.dtaregistro, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
                + "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
                + "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
                + "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.email, pacientes.facebook, pacientes.instagram, "
                + "escolaridade.descescolaridade, escola.descescola, profissao.descprofissao,"
                + " encaminhado.descencaminhado, formatransporte.descformatransporte,"
                + " deficienciafisica, deficienciamental, deficienciaauditiva, deficienciavisual, deficienciamultipla, "
                + " pacientes.nome_social, pacientes.necessita_nome_social, "
                + " pacientes.codmunicipio, b.descbairro, pacientes.id_genero, id_religiao, pacientes.matricula "
                + "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
                + " left join hosp.escola on pacientes.id_escola=escola.id_escola "
                + "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
                + "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
                + "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte "
                + "left join hosp.municipio on pacientes.codmunicipio = municipio.id_municipio "
                + " left join hosp.bairros b on b.id_bairro = pacientes.codbairro "
                + "where id_paciente = ?";

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setDtanascimento(rs.getDate("dtanascimento"));
                p.setEstadoCivil(rs.getString("estcivil"));
                p.setSexo(rs.getString("sexo"));
                p.getGenero().setId(rs.getInt("id_genero"));
                p.setSangue(rs.getString("sangue"));
                p.setNomePai(rs.getString("pai"));
                p.setNomeMae(rs.getString("mae"));
                p.setConjuge(rs.getString("conjuge"));
                p.setCodRaca(rs.getInt("codraca"));
                p.getEndereco().setCep(rs.getString("cep"));
                p.getEndereco().setMunicipio(rs.getString("descmunicipio"));
                p.getEndereco().setUf(rs.getString("uf"));
                p.getEndereco().setLogradouro(rs.getString("logradouro"));
                p.getEndereco().setNumero(rs.getString("numero"));
                p.getEndereco().setComplemento(rs.getString("complemento"));
                p.getEndereco().setReferencia(rs.getString("referencia"));
                p.getEndereco().setBairro(rs.getString("descbairro"));
                if (rs.getString("rg") != null) 
                p.setRg(rs.getString("rg").toUpperCase());
                if (rs.getString("oe") != null)
                p.setOe(rs.getString("oe").toUpperCase());
                p.setDataExpedicao1(rs.getDate("dtaexpedicaorg"));
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
                p.setMatricula(rs.getString("matricula"));
                if (rs.getString("protreab") != null) 
                p.setProtant(rs.getInt("protreab"));
                p.setReservista(rs.getString("reservista"));
                if (rs.getString("ctps") != null) 
                p.setCtps(rs.getInt("ctps"));
                p.setSerie(rs.getString("serie"));
                p.setPis(rs.getString("pis"));
                p.setCartorio(rs.getString("cartorio"));
                p.setNumeroCartorio(rs.getString("regnascimento"));
                p.setLivro(rs.getString("livro"));
                p.setFolha(rs.getString("folha"));
                p.setDataExpedicao2(rs.getDate("dtaregistro"));
                p.setEmail(rs.getString("email"));
                p.setFacebook(rs.getString("facebook"));
                p.setInstagram(rs.getString("instagram"));
                if (rs.getString("id_escolaridade") != null) {
                    p.getEscolaridade().setCodescolaridade(
                            rs.getInt("id_escolaridade"));
                    p.getEscolaridade().setDescescolaridade(
                            rs.getString("descescolaridade"));
                }
                if (rs.getString("id_escola") != null) {
                    p.getEscola().setCodEscola(rs.getInt("id_escola"));
                    p.getEscola().setDescescola(rs.getString("descescola"));
                }

                if (rs.getString("id_profissao") != null) {
                    p.getProfissao().setCodprofissao(rs.getInt("id_profissao"));
                    p.getProfissao()
                            .setDescprofissao(rs.getString("descprofissao"));
                }
                
                p.setTrabalha(rs.getString("trabalha"));
                p.setLocaltrabalha(rs.getString("localtrabalha"));
                p.setCodparentesco(rs.getInt("codparentesco"));
                p.setNomeresp(rs.getString("nomeresp"));
                p.setRgresp(rs.getString("rgresp"));
                p.setCpfresp(rs.getString("cpfresp"));
                p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));


                p.setDeficiencia(rs.getString("deficiencia"));


                if (rs.getString("id_encaminhado") != null) {
                    p.getEncaminhado().setCodencaminhado(
                            rs.getInt("id_encaminhado"));
                    p.getEncaminhado().setDescencaminhado(
                            rs.getString("descencaminhado"));
                }


                if (rs.getString("id_formatransporte") != null) {
                    p.getFormatransporte().setCodformatransporte(
                            rs.getInt("id_formatransporte"));
                    p.getFormatransporte().setDescformatransporte(
                            rs.getString("descformatransporte"));
                }
                p.setDeficienciaFisica(rs.getBoolean("deficienciafisica"));
                p.setDeficienciaMental(rs.getBoolean("deficienciamental"));
                p.setDeficienciaAuditiva(rs.getBoolean("deficienciaauditiva"));
                p.setDeficienciaVisual(rs.getBoolean("deficienciavisual"));
                p.setDeficienciaMultipla(rs.getBoolean("deficienciamultipla"));
                p.getEndereco().setCodIbge(rs.getInt("codigo"));
                p.setNomeSocial(rs.getString("nome_social"));
                p.setNecessitaNomeSocial(rs.getBoolean("necessita_nome_social"));
                p.setListaTelefones(listarTelefonesDoPaciente(id));
                p.getEndereco().setCodmunicipio(rs.getInt("codmunicipio"));
                p.getEndereco().setCodbairro(rs.getInt("codbairro"));
                
                if (rs.getString("id_religiao") != null) {
                    p.getReligiao().setId(rs.getInt("id_religiao"));
                }

            }

            return p;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public List<Telefone> listarTelefonesDoPaciente(Integer idPaciente) throws ProjetoException {

        List<Telefone> lista = new ArrayList<Telefone>();
        PreparedStatement ps = null;


        try {
            conexao = ConnectionFactory.getConnection();
            String sql = " select id, ddd, telefone, whatsapp, responsavel, id_parentesco, id_operadora " +
                    "from hosp.telefone_paciente where id_paciente = ? order by id";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Telefone t = new Telefone();
                if (rs.getString("ddd")!= null) 
					t.setDdd(rs.getInt("ddd"));
                if (rs.getString("id")!= null)               
                	t.setIdTelefone(rs.getInt("id"));
                if (rs.getString("telefone")!= null)
                	t.setTelefone(rs.getString("telefone"));
                t.setWhatsapp(rs.getBoolean("whatsapp"));
                t.setResponsavel(rs.getString("responsavel"));
                if (rs.getString("id_parentesco")!= null)
                	t.getParentesco().setCodParentesco(rs.getInt("id_parentesco"));
                if (rs.getString("id_operadora")!= null)
                	t.getOperadora().setId(rs.getInt("id_operadora"));

                lista.add(t);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return lista;
    }


    public List<PacienteBean> listaPaciente() throws ProjetoException {
        PreparedStatement ps = null;

        try {
            conexao = ConnectionFactory.getConnection();
            String sql = " select id_paciente, nome, cpf, cns from hosp.pacientes order by nome";

            ps = conexao.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            List<PacienteBean> lista = new ArrayList<PacienteBean>();

            while (rs.next()) {
                PacienteBean p = new PacienteBean();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));

                lista.add(p);

            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<PacienteBean> buscarPacientes(String campoBusca, String tipo) {
        PreparedStatement ps = null;

        try {
            conexao = ConnectionFactory.getConnection();
            String sql = " select id_paciente, nome, cpf, cns, codprontuario_anterior, matricula from hosp.pacientes where ";

            if(tipo.equals("nome")){
                sql = sql + "nome like ?";
            }
            else if(tipo.equals("cpf")){
                sql = sql + "cpf like ?";
            }
            else if(tipo.equals("cns")){
                sql = sql + "cns like ?";
            }
            else if(tipo.equals("prontuario")){
                sql = sql + "id_paciente = ?";
            }
            else if(tipo.equals("matricula")){
                sql = sql + "matricula like ?";
            }

            sql = sql + " order by nome";

            ps = conexao.prepareStatement(sql);
            if ((tipo.equals("nome")) || (tipo.equals("cpf")) || (tipo.equals("cns")) || (tipo.equals("matricula")))
            ps.setString(1, "%" + campoBusca.toUpperCase() + "%");
            else
            	ps.setInt(1,Integer.valueOf(campoBusca));
            ResultSet rs = ps.executeQuery();

            List<PacienteBean> lista = new ArrayList<PacienteBean>();

            while (rs.next()) {
                PacienteBean p = new PacienteBean();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setMatricula(rs.getString("matricula"));
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));

                lista.add(p);

            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<PacienteBean> buscaPacienteAutoCompleteOk(String str)
            throws ProjetoException {
        PreparedStatement ps = null;

        try {
            conexao = ConnectionFactory.getConnection();
            String sql = " select id_paciente, nome, cpf, cns from hosp.pacientes where nome like ? order by nome";

            ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + str.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();

            List<PacienteBean> lista = new ArrayList<PacienteBean>();

            while (rs.next()) {
                PacienteBean p = new PacienteBean();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));

                lista.add(p);

            }
            return lista;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public PacienteBean buscarPacientePorIdOrteseProtese(Integer id)
            throws ProjetoException {

        PreparedStatement ps = null;
        PacienteBean p = new PacienteBean();

        try {
            conexao = ConnectionFactory.getConnection();

            String sql = "SELECT p.id_paciente, p.nome, p.cpf, p.cns, o.medicao, o.data_medicao " +
                    "FROM hosp.pacientes p " +
                    "LEFT JOIN hosp.laudo l ON (l.codpaciente = p.id_paciente) " +
                    "LEFT JOIN hosp.ortese_protese o ON (o.cod_laudo = l.id_laudo) " +
                    "WHERE o.id = ?; ";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
            }
            return p;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                conexao.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Boolean inserirTelefone(List<Telefone> lista, Integer idPaciente, Connection conexaoAuxiliar) {

        Boolean retorno = false;

        String sql = "INSERT INTO hosp.telefone_paciente (ddd, telefone, whatsapp, responsavel, id_parentesco, id_paciente, id_operadora) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getDdd() != null) 
                	stmt.setInt(1, lista.get(i).getDdd());
                else
                	stmt.setNull(1, Types.NULL);
                if (lista.get(i).getTelefone() != null) 
                stmt.setString(2, lista.get(i).getTelefone());
                else
                	stmt.setNull(2, Types.NULL);
                stmt.setBoolean(3, lista.get(i).getWhatsapp());
                stmt.setString(4, lista.get(i).getResponsavel());
                if (lista.get(i).getParentesco().getCodParentesco()!= null) 
                stmt.setInt(5, lista.get(i).getParentesco().getCodParentesco());
                else
                	stmt.setNull(5, Types.NULL);
                stmt.setInt(6, idPaciente);
                if (lista.get(i).getOperadora().getId()!= null) 
                stmt.setInt(7, lista.get(i).getOperadora().getId());
                else
                	stmt.setNull(7, Types.NULL);
                stmt.execute();
            }

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }

    public Boolean deletarTelefone(Integer idPaciente, Connection conexaoAuxiliar) {

        Boolean retorno = false;

        String sql = "delete from hosp.telefone_paciente where id_paciente = ?";

        try {
            PreparedStatement stmt = conexaoAuxiliar.prepareStatement(sql);
            stmt.setInt(1, idPaciente);
            stmt.execute();

            retorno = true;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return retorno;
        }
    }
    
    public PacienteBean verificaExisteCpfCadastrado(String cpfPaciente, Integer idPaciente) {

    	PacienteBean pacienteRetorno = null;

        String sql = "select nome from hosp.pacientes where cpf=?";
        if (idPaciente!=null)
        	sql  = sql +" and id_paciente<>?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cpfPaciente);
            if (idPaciente!=null)
            	stmt.setInt(2, idPaciente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	pacienteRetorno = new PacienteBean();
            	pacienteRetorno.setNome(rs.getString("nome"));
            }
            

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return pacienteRetorno;
        }
    }    
    
    public PacienteBean verificaExisteCnsCadastrado(String cnsPaciente, Integer idPaciente) {

    	PacienteBean pacienteRetorno = null;

        String sql = "select nome from hosp.pacientes where cns=?";
        if (idPaciente!=null)
        	sql  = sql +" and id_paciente<>?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, cnsPaciente);
            if (idPaciente!=null)
            	stmt.setInt(2, idPaciente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	pacienteRetorno = new PacienteBean();
            	pacienteRetorno.setNome(rs.getString("nome"));
            }
            

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return pacienteRetorno;
        }
    }        
}
