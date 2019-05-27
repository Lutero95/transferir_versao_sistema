package br.gov.al.maceio.sishosp.hosp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.comum.util.StringUtil;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.Telefone;

public class PacienteDAO {
    private Connection conexao = null;
    private PreparedStatement ps = null;

    public Boolean cadastrar(PacienteBean paciente, Boolean bairroExiste) throws ProjetoException {
        boolean retorno = false;
        
        Integer idPaciente = null;

        try {
        	conexao = ConnectionFactory.getConnection();
            if (!bairroExiste) {
                String sql2 = "INSERT INTO hosp.bairros(descbairro, codmunicipio) "
                        + " VALUES (?, ?) returning id_bairro;";

                PreparedStatement ps2 = conexao.prepareStatement(sql2);

                ps2.setString(1, StringUtil.removeAcentos(paciente.getEndereco().getBairro()
                        .toUpperCase()));
                ps2.setInt(2, paciente.getEndereco().getCodmunicipio());

                ResultSet set = ps2.executeQuery();
                while (set.next()) {
                    paciente.getEndereco().setCodbairro(set.getInt(1));
                }

            }

            String sql = "insert into hosp.pacientes (dtacadastro, nome, dtanascimento, estcivil, sexo, sangue, "
                    + "pai, mae, conjuge,codraca, cep, uf, logradouro, numero, complemento, referencia, "
                    + "rg, oe, dtaexpedicaorg, cpf, cns, protreab, "
                    + "reservista, ctps, serie, pis, cartorio, regnascimento, livro, folha, dtaregistro, contribuinte, id_escolaridade, id_escola, id_profissao, "
                    + "trabalha, localtrabalha, codparentesco, "
                    + "nomeresp, rgresp, cpfresp, dtanascimentoresp, id_encaminhado, id_formatransporte ,deficiencia, codmunicipio, "
                    + "deficienciafisica, deficienciamental, deficienciaauditiva, deficienciavisual, deficienciamultipla, codbairro, email, facebook, instagram, "
                    + "nome_social, necessita_nome_social, motivo_nome_social, associado)"
                    + " values (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ? , ? , "
                    + "? , ? , ?, ?, ?, ? , ? , ?, ? , ?, ?, ?, ? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?, ?) returning id_paciente";

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, paciente.getNome().toUpperCase().trim());
            stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento()
                    .getTime()));
            if (paciente.getEstadoCivil() == null) {
                stmt.setNull(3, Types.CHAR);
            } else {
                stmt.setString(3, paciente.getEstadoCivil());
            }
            if (paciente.getSexo() == null) {
                stmt.setNull(4, Types.CHAR);
            } else {
                stmt.setString(4, paciente.getSexo());
            }
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
            if (paciente.getCpf() == null) {
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
                stmt.setNull(24, Types.INTEGER);
            } else {
                stmt.setInt(24, paciente.getSerie());
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
                stmt.setInt(29, paciente.getFolha());
            }
            if (paciente.getDataExpedicao2() == null) {
                stmt.setNull(30, Types.DATE);
            } else {
                stmt.setDate(30, new java.sql.Date(paciente.getDataExpedicao2()
                        .getTime()));
            }
            if (paciente.getAssociado() == null) {
                stmt.setNull(31, Types.CHAR);
            } else {
                stmt.setString(31, paciente.getAssociado().toUpperCase().trim());
            }
            if (paciente.getEscolaridade().getCodescolaridade() == null) {
                stmt.setNull(32, Types.INTEGER);
            } else {
                stmt.setInt(32, paciente.getEscolaridade().getCodescolaridade());
            }
            if (paciente.getEscola().getCodEscola() != null) {
                stmt.setInt(33, paciente.getEscola().getCodEscola());
            } else {
                stmt.setNull(33, Types.NULL);
            }
            if (paciente.getProfissao().getCodprofissao() == null) {
                stmt.setNull(34, Types.INTEGER);
            } else {
                stmt.setInt(34, paciente.getProfissao().getCodprofissao());
            }
            if (paciente.getTrabalha() == null) {
                stmt.setNull(35, Types.CHAR);
            } else {
                stmt.setString(35, paciente.getTrabalha().toUpperCase().trim());
            }
            if (paciente.getLocaltrabalha() == null) {
                stmt.setNull(36, Types.CHAR);
            } else {
                stmt.setString(36, paciente.getLocaltrabalha().toUpperCase()
                        .trim());
            }
            if (paciente.getCodparentesco() == null) {
                stmt.setNull(37, Types.INTEGER);
            } else {
                stmt.setInt(37, paciente.getCodparentesco());
            }
            if (paciente.getNomeresp() == null) {
                stmt.setNull(38, Types.CHAR);
            } else {
                stmt.setString(38, paciente.getNomeresp().toUpperCase().trim());
            }
            if (paciente.getRgresp() == null) {
                stmt.setNull(39, Types.CHAR);
            } else {
                stmt.setString(39, paciente.getRgresp().toUpperCase().trim());
            }
            if (paciente.getCpfresp() == null) {
                stmt.setNull(40, Types.CHAR);
            } else {
                stmt.setString(40,
                        paciente.getCpfresp().replaceAll("[^0-9]", ""));
            }
            if (paciente.getDataNascimentoresp() == null) {
                stmt.setNull(41, Types.DATE);
            } else {
                stmt.setDate(41, new java.sql.Date(paciente
                        .getDataNascimentoresp().getTime()));
            }
            if (paciente.getEncaminhado().getCodencaminhado() == null) {
                stmt.setNull(42, Types.INTEGER);
            } else {
                stmt.setInt(42, paciente.getEncaminhado().getCodencaminhado());
            }
            
            if (paciente.getFormatransporte().getCodformatransporte() == null) {
                stmt.setNull(43, Types.INTEGER);
            } else {
                stmt.setInt(43, paciente.getFormatransporte()
                        .getCodformatransporte());
            }
            stmt.setString(44, paciente.getDeficiencia().toUpperCase().trim());

            stmt.setInt(45, paciente.getEndereco().getCodmunicipio());

            if (paciente.getDeficienciaFisica() != null) {
                stmt.setBoolean(46, paciente.getDeficienciaFisica());
            } else {
                stmt.setBoolean(46, false);
            }

            if (paciente.getDeficienciaMental() != null) {
                stmt.setBoolean(47, paciente.getDeficienciaMental());
            } else {
                stmt.setBoolean(47, false);
            }

            if (paciente.getDeficienciaAuditiva() != null) {
                stmt.setBoolean(48, paciente.getDeficienciaAuditiva());
            } else {
                stmt.setBoolean(48, false);
            }

            if (paciente.getDeficienciaVisual() != null) {
                stmt.setBoolean(49, paciente.getDeficienciaVisual());
            } else {
                stmt.setBoolean(49, false);
            }

            if (paciente.getDeficienciaMultipla() != null) {
                stmt.setBoolean(50, paciente.getDeficienciaMultipla());
            } else {
                stmt.setBoolean(50, false);
            }

            stmt.setInt(51, paciente.getEndereco().getCodbairro());

            if (paciente.getEmail() == null) {
                stmt.setNull(52, Types.CHAR);
            } else {
                stmt.setString(52, paciente.getEmail());
            }

            if (paciente.getFacebook() == null) {
                stmt.setNull(53, Types.CHAR);
            } else {
                stmt.setString(53, paciente.getFacebook());
            }

            if (paciente.getInstagram() == null) {
                stmt.setNull(54, Types.CHAR);
            } else {
                stmt.setString(54, paciente.getInstagram());
            }

            if (paciente.getNomeSocial() == null) {
                stmt.setNull(55, Types.CHAR);
            } else {
                stmt.setString(55, paciente.getNomeSocial().toUpperCase());
            }

            stmt.setBoolean(56, paciente.getNecessitaNomeSocial());

            if (paciente.getMotivoNomeSocial() == null) {
                stmt.setNull(57, Types.CHAR);
            } else {
                stmt.setString(57, paciente.getMotivoNomeSocial());
            }
            
            if (paciente.getAssociado() == null) {
                stmt.setNull(58, Types.CHAR);
            } else {
                stmt.setString(58, paciente.getAssociado().trim().toUpperCase());
            }

            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                idPaciente = set.getInt("id_paciente");
            }

            String sql2 = "INSERT INTO hosp.telefone_paciente (ddd, telefone, whatsapp, responsavel, id_parentesco, id_paciente) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt2 = conexao.prepareStatement(sql2);

            for (int i = 0; i < paciente.getListaTelefones().size(); i++) {
                stmt2.setInt(1, paciente.getListaTelefones().get(i).getDdd());
                stmt2.setInt(2, paciente.getListaTelefones().get(i).getTelefone());
                stmt2.setBoolean(3, paciente.getListaTelefones().get(i).getWhatsapp());
                stmt2.setString(4, paciente.getListaTelefones().get(i).getResponsavel());
                stmt2.setInt(5, paciente.getListaTelefones().get(i).getParentesco().getCodParentesco());
                stmt2.setInt(6, idPaciente);
                stmt2.execute();
            }

            conexao.commit();
            retorno = true;

        } catch (SQLException ex) {
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

    public Boolean alterar(PacienteBean paciente, boolean bairroExiste)
            throws ProjetoException {
        boolean retorno = false;
       

        try {
        	 conexao = ConnectionFactory.getConnection();
             if (!bairroExiste) {
                 String sql2 = "INSERT INTO hosp.bairros(descbairro, codmunicipio) "
                         + " VALUES (?, ?) returning id_bairro;";

                 PreparedStatement ps2 = conexao.prepareStatement(sql2);

                 ps2.setString(1,StringUtil.removeAcentos( paciente.getEndereco().getBairro()
                         .toUpperCase()));
                 ps2.setInt(2, paciente.getEndereco().getCodmunicipio());

                 ResultSet set = ps2.executeQuery();
                 while (set.next()) {
                     paciente.getEndereco().setCodbairro(set.getInt(1));
                 }

             }
             
            String sql = "update hosp.pacientes set nome = ?, dtanascimento = ?, estcivil = ?, sexo = ? , sangue = ?, pai = ? "
                    + ", mae = ?, conjuge = ?, codraca = ?, cep = ?, uf = ?, logradouro = ?, numero = ?"
                    + ", complemento = ?, referencia = ? "
                    + ", rg = ?,  oe = ?, dtaexpedicaorg = ?, cpf = ?, cns = ?, protreab = ?"
                    + ", reservista = ?, ctps = ?, serie = ?, pis = ?, cartorio = ?, regnascimento = ?, livro = ?, folha = ?, dtaregistro = ?"
                    + ", contribuinte = ?, id_escolaridade = ?, id_escola = ?, id_profissao = ?, trabalha = ?, localtrabalha = ?"
                    + ", codparentesco = ?, nomeresp = ?, rgresp = ?, cpfresp = ?, dtanascimentoresp = ?, id_encaminhado = ?"
                    + ", id_formatransporte = ?, deficiencia = ?, codmunicipio = ?"
                    + ", deficienciafisica = ?, deficienciamental = ?, deficienciaauditiva = ?, deficienciavisual = ?, deficienciamultipla = ?"
                    + ", email = ?, facebook = ?, instagram = ?, nome_social = ?, necessita_nome_social = ?, motivo_nome_social = ?, associado=?, codbairro=?  "
                    + " where id_paciente = ?";

            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, paciente.getNome());
            stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento()
                    .getTime()));
            stmt.setString(3, paciente.getEstadoCivil());
            stmt.setString(4, paciente.getSexo());
            stmt.setString(5, paciente.getSangue());
            stmt.setString(6, paciente.getNomePai());
            stmt.setString(7, paciente.getNomeMae());
            stmt.setString(8, paciente.getConjuge());
            stmt.setInt(9, paciente.getCodRaca());
            stmt.setString(10, paciente.getEndereco().getCep());
            stmt.setString(11, paciente.getEndereco().getUf());
            stmt.setString(12, paciente.getEndereco().getLogradouro());
            stmt.setString(13, paciente.getEndereco().getNumero());
            stmt.setString(14, paciente.getEndereco().getComplemento());
            stmt.setString(15, paciente.getEndereco().getReferencia());
            stmt.setString(16, paciente.getRg());
            stmt.setString(17, paciente.getOe());

            if (paciente.getDataExpedicao1() != null) {
                stmt.setDate(18, new java.sql.Date(paciente.getDataExpedicao1()
                        .getTime()));
            } else {
                stmt.setNull(18, Types.DATE);
            }

            stmt.setString(19, paciente.getCpf().replaceAll("[^0-9]", ""));
            stmt.setString(20, paciente.getCns());

            if (paciente.getProtant() == null) {
                stmt.setNull(21, Types.INTEGER);
            } else {
                stmt.setInt(21, paciente.getProtant());
            }

            stmt.setString(22, paciente.getReservista());
            stmt.setInt(23, paciente.getCtps());
            stmt.setInt(24, paciente.getSerie());
            stmt.setString(25, paciente.getPis());
            stmt.setString(26, paciente.getCartorio());
            stmt.setString(27, paciente.getNumeroCartorio());
            stmt.setString(28, paciente.getLivro());
            stmt.setInt(29, paciente.getFolha());

            if (paciente.getDataExpedicao2() != null) {
                stmt.setDate(30, new java.sql.Date(paciente.getDataExpedicao2()
                        .getTime()));
            } else {
                stmt.setNull(30, Types.DATE);
            }

            stmt.setString(31, paciente.getAssociado().toUpperCase().trim());
            
            if (paciente.getEscolaridade().getCodescolaridade() != null) 
            	stmt.setInt(32, paciente.getEscolaridade().getCodescolaridade());
            else
            	stmt.setNull(32, Types.INTEGER);
            
            if (paciente.getEscola().getCodEscola() != null) 
            	stmt.setInt(33, paciente.getEscola().getCodEscola());
            else
            	stmt.setNull(33, Types.INTEGER);

            if (paciente.getProfissao().getCodprofissao() == null) {
                stmt.setNull(34, Types.INTEGER);
            } else {
                stmt.setInt(34, paciente.getProfissao().getCodprofissao());
            }

            stmt.setString(35, paciente.getTrabalha());
            stmt.setString(36, paciente.getLocaltrabalha());

            if (paciente.getCodparentesco() != null) {
                stmt.setInt(37, paciente.getCodparentesco());
            } else {
                stmt.setNull(37, Types.INTEGER);
            }

            stmt.setString(38, paciente.getNomeresp());
            stmt.setString(39, paciente.getRgresp());
            stmt.setString(40, paciente.getCpfresp().replaceAll("[^0-9]", ""));

            if (paciente.getDataNascimentoresp() != null) {
                stmt.setDate(41, new java.sql.Date(paciente
                        .getDataNascimentoresp().getTime()));
            } else {
                stmt.setNull(41, Types.DATE);
            }

            if (paciente.getEncaminhado().getCodencaminhado() != null) 
            stmt.setInt(42, paciente.getEncaminhado().getCodencaminhado());
            else 
                stmt.setNull(42, Types.INTEGER);
            
            if (paciente.getFormatransporte()
                    .getCodformatransporte() != null) 
                stmt.setInt(43, paciente.getFormatransporte()
                        .getCodformatransporte());
                else 
                    stmt.setNull(43, Types.INTEGER);
           stmt.setString(44, paciente.getDeficiencia());

            stmt.setInt(45, paciente.getEndereco().getCodmunicipio());

            stmt.setBoolean(46, paciente.getDeficienciaFisica());

            stmt.setBoolean(47, paciente.getDeficienciaMental());

            stmt.setBoolean(48, paciente.getDeficienciaAuditiva());

            stmt.setBoolean(49, paciente.getDeficienciaVisual());

            stmt.setBoolean(50, paciente.getDeficienciaMultipla());

            stmt.setString(51, paciente.getEmail());

            stmt.setString(52, paciente.getFacebook());

            stmt.setString(53, paciente.getInstagram());

            if (paciente.getNomeSocial() == null) {
                stmt.setNull(54, Types.CHAR);
            } else {
                stmt.setString(54, paciente.getNomeSocial().toUpperCase());
            }

            stmt.setBoolean(55, paciente.getNecessitaNomeSocial());

            if (paciente.getMotivoNomeSocial() == null) {
                stmt.setNull(56, Types.CHAR);
            } else {
                stmt.setString(56, paciente.getMotivoNomeSocial());
            }
            
            if (paciente.getMotivoNomeSocial() == null) {
                stmt.setNull(57, Types.CHAR);
            } else {
                stmt.setString(57, paciente.getAssociado());
            }
            
            stmt.setInt(58, paciente.getEndereco().getCodbairro());

            stmt.setLong(59, paciente.getId_paciente());

            stmt.executeUpdate();

            String sql2 = "delete from hosp.telefone_paciente where id_paciente = ?";
            PreparedStatement stmt2 = conexao.prepareStatement(sql2);

            stmt2.setInt(1, paciente.getId_paciente());
            stmt2.execute();


            String sql3 = "INSERT INTO hosp.telefone_paciente (ddd, telefone, whatsapp, responsavel, id_parentesco, id_paciente) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt3 = conexao.prepareStatement(sql3);

            for (int i = 0; i < paciente.getListaTelefones().size(); i++) {
                stmt3.setInt(1, paciente.getListaTelefones().get(i).getDdd());
                stmt3.setInt(2, paciente.getListaTelefones().get(i).getTelefone());
                stmt3.setBoolean(3, paciente.getListaTelefones().get(i).getWhatsapp());
                stmt3.setString(4, paciente.getListaTelefones().get(i).getResponsavel());
                stmt3.setInt(5, paciente.getListaTelefones().get(i).getParentesco().getCodParentesco());
                stmt3.setInt(6, paciente.getId_paciente());
                stmt3.execute();
            }

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
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
        String sql = "delete from hosp.pacientes where id_paciente = ?";
        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, paciente.getId_paciente());
            stmt.executeUpdate();

            conexao.commit();

            retorno = true;

        } catch (SQLException ex) {
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

        String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
                + "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf, municipio.codigo,  "
                + "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, "
                + "pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
                + "pacientes.protreab, "
                + "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
                + "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
                + "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
                + "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
                + "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.email, pacientes.facebook, pacientes.instagram, "
                + "escolaridade.descescolaridade, escola.descescola, profissao.descprofissao, "
                + "encaminhado.descencaminhado, formatransporte.descformatransporte, pacientes.nome_social, pacientes.necessita_nome_social, pacientes.motivo_nome_social "
                + "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
                + " left join hosp.escola on pacientes.id_escola=escola.id_escola "
                + "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
                + "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
                + "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte "
                + " left join hosp.municipio on pacientes.codmunicipio = municipio.id_municipio "
                + " order by pacientes.nome ";

        ArrayList<PacienteBean> lista = new ArrayList();

        try {
            conexao = ConnectionFactory.getConnection();
            PreparedStatement stm = conexao.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                PacienteBean p = new PacienteBean();

                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome").toUpperCase());
                p.setDtanascimento(rs.getDate("dtanascimento"));
                p.setEstadoCivil(rs.getString("estcivil"));
                p.setSexo(rs.getString("sexo"));
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
                p.setRg(rs.getString("rg").toLowerCase());
                p.setOe(rs.getString("oe").toLowerCase());
                p.setDataExpedicao1(rs.getDate("dtaexpedicaorg"));
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
                p.setProtant(rs.getInt("protreab"));
                p.setReservista(rs.getString("reservista"));
                p.setCtps(rs.getInt("ctps"));
                p.setSerie(rs.getInt("serie"));
                p.setPis(rs.getString("pis"));
                p.setCartorio(rs.getString("cartorio"));
                p.setNumeroCartorio(rs.getString("regnascimento"));
                p.setLivro(rs.getString("livro"));
                p.setFolha(rs.getInt("folha"));
                p.setDataExpedicao2(rs.getDate("dtaregistro"));
                p.setAssociado(rs.getString("contribuinte"));
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
                p.setEmail(rs.getString("email"));
                p.setFacebook(rs.getString("facebook"));
                p.setInstagram(rs.getString("instagram"));
                p.getEscola().setDescescola(rs.getString("descescola"));
                p.getProfissao()
                        .setDescprofissao(rs.getString("descprofissao"));
                p.getEncaminhado().setDescencaminhado(
                        rs.getString("descencaminhado"));
                p.getFormatransporte().setDescformatransporte(
                        rs.getString("descformatransporte"));
                p.getEndereco().setCodIbge(rs.getInt("codigo"));
                p.setNomeSocial(rs.getString("nome_social"));
                p.setMotivoNomeSocial(rs.getString("motivo_nome_social"));
                p.setNecessitaNomeSocial(rs.getBoolean("necessita_nome_social"));

                lista.add(p);
            }
        } catch (SQLException ex) {
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
            throws ProjetoException, SQLException {

        String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
                + "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf,  "
                + "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, "
                + "pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
                + "pacientes.protreab, "
                + "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
                + "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
                + "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
                + "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
                + "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.email, pacientes.facebook, pacientes.instagram, "
                + "escolaridade.descescolaridade, escola.descescola, profissao.descprofissao, "
                + " pacientes.nome_social, pacientes.necessita_nome_social, pacientes.motivo_nome_social"
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
                p.setRg(rs.getString("rg").toLowerCase());
                p.setOe(rs.getString("oe").toLowerCase());
                p.setDataExpedicao1(rs.getDate("dtaexpedicaorg"));
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
                p.setProtant(rs.getInt("protreab"));
                p.setReservista(rs.getString("reservista"));
                p.setCtps(rs.getInt("ctps"));
                p.setSerie(rs.getInt("serie"));
                p.setPis(rs.getString("pis"));
                p.setCartorio(rs.getString("cartorio"));
                p.setNumeroCartorio(rs.getString("regnascimento"));
                p.setLivro(rs.getString("livro"));
                p.setFolha(rs.getInt("folha"));
                p.setDataExpedicao2(rs.getDate("dtaregistro"));
                p.setAssociado(rs.getString("contribuinte"));
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
                p.setMotivoNomeSocial(rs.getString("motivo_nome_social"));
                p.setNecessitaNomeSocial(rs.getBoolean("necessita_nome_social"));
                listaP.add(p);
            }
        } catch (SQLException ex) {
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
                + "pacientes.protreab, "
                + "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
                + "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
                + "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
                + "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
                + "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.email, pacientes.facebook, pacientes.instagram, "
                + "escolaridade.descescolaridade, escola.descescola, profissao.descprofissao,"
                + " encaminhado.descencaminhado, formatransporte.descformatransporte,"
                + " deficienciafisica, deficienciamental, deficienciaauditiva, deficienciavisual, deficienciamultipla, "
                + " pacientes.nome_social, pacientes.necessita_nome_social, pacientes.motivo_nome_social, coalesce(pacientes.associado,'N') associado, "
                +" pacientes.codmunicipio, b.descbairro "
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
                p.setRg(rs.getString("rg").toLowerCase());
                p.setOe(rs.getString("oe").toLowerCase());
                p.setDataExpedicao1(rs.getDate("dtaexpedicaorg"));
                p.setCpf(rs.getString("cpf"));
                p.setCns(rs.getString("cns"));
                p.setProtant(rs.getInt("protreab"));
                p.setReservista(rs.getString("reservista"));
                p.setCtps(rs.getInt("ctps"));
                p.setSerie(rs.getInt("serie"));
                p.setPis(rs.getString("pis"));
                p.setCartorio(rs.getString("cartorio"));
                p.setNumeroCartorio(rs.getString("regnascimento"));
                p.setLivro(rs.getString("livro"));
                p.setFolha(rs.getInt("folha"));
                p.setDataExpedicao2(rs.getDate("dtaregistro"));
                p.setAssociado(rs.getString("contribuinte"));
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
                p.setMotivoNomeSocial(rs.getString("motivo_nome_social"));
                p.setNecessitaNomeSocial(rs.getBoolean("necessita_nome_social"));
                p.setListaTelefones(listarTelefonesDoPaciente(id));
                p.setAssociado(rs.getString("associado"));
                p.getEndereco().setCodmunicipio(rs.getInt("codmunicipio"));

            }

            return p;
        } catch (SQLException ex) {
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
            String sql = " select id, ddd, telefone, whatsapp, responsavel, id_parentesco " +
                    "from hosp.telefone_paciente where id_paciente = ? order by id";

            ps = conexao.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Telefone t = new Telefone();
                t.setDdd(rs.getInt("ddd"));
                t.setIdTelefone(rs.getInt("id"));
                t.setTelefone(rs.getInt("telefone"));
                t.setWhatsapp(rs.getBoolean("whatsapp"));
                t.setResponsavel(rs.getString("responsavel"));
                t.getParentesco().setCodParentesco(rs.getInt("id_parentesco"));

                lista.add(t);

            }

        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
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
}
