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
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissionalBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;

public class PacienteDAO {
	private Connection conexao = null;
	//private PreparedStatement ps;
	private Connection con = null;
	private PreparedStatement ps = null;

	// COMEÇO DO CODIGO

	public Boolean cadastrar(PacienteBean paciente) throws ProjetoException {
		boolean cadastrou = false;
		System.out.println("passou aqui 2");

		/*
		 * PacienteBean user_session = (PacienteBean) FacesContext
		 * .getCurrentInstance().getExternalContext().getSessionMap()
		 * .get("obj_paciente");
		 */

		String sql = "insert into hosp.pacientes (dtacadastro, nome, dtanascimento, estcivil, sexo, sangue, "
				+ "pai, mae, conjuge,codraca, cep, uf, cidade, bairro, logradouro, numero, complemento, referencia, telres, telcel, teltrab, telorelhao, rg, oe, dtaexpedicaorg, cpf, cns, protreab, "
				+ "reservista, ctps, serie, pis, cartorio, regnascimento, livro, folha, dtaregistro, contribuinte, id_escolaridade, id_escola, id_profissao, trabalha, localtrabalha, codparentesco, "
				+ "nomeresp, rgresp, cpfresp, dtanascimentoresp, id_encaminhado, id_formatransporte ,deficiencia, tipodeficiencia)"
				+ " values (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ? , ? , "
				+ "? , ? , ?, ?, ?, ? , ? , ?, ? , ?, ?, ?, ? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, paciente.getNome().toUpperCase().trim());
			stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento()
					.getTime()));
			stmt.setString(3, paciente.getEstadoCivil());
			stmt.setString(4, paciente.getSexo());
			stmt.setString(5, paciente.getSangue());
			stmt.setString(6, paciente.getNomePai().toUpperCase().trim());
			stmt.setString(7, paciente.getNomeMae().toUpperCase().trim());
			stmt.setString(8, paciente.getConjuge().toUpperCase().trim());
			stmt.setInt(9, paciente.getCodRaca());
			stmt.setInt(10, paciente.getEndereco().getCep());
			stmt.setString(11, paciente.getEndereco().getUf().toUpperCase()
					.trim());
			stmt.setString(12, paciente.getEndereco().getMunicipio()
					.toUpperCase().trim());
			stmt.setString(13, paciente.getEndereco().getBairro().toUpperCase()
					.trim());
			stmt.setString(14, paciente.getEndereco().getLogradouro()
					.toUpperCase().trim());
			stmt.setString(15, paciente.getEndereco().getNumero().toUpperCase()
					.trim());
			stmt.setString(16, paciente.getEndereco().getComplemento()
					.toUpperCase().trim());
			stmt.setString(17, paciente.getEndereco().getReferencia()
					.toUpperCase().trim());
			if (paciente.getEndereco().getTelefoneres() == null) {
				stmt.setNull(18, Types.CHAR);
			} else {
				stmt.setString(18, paciente.getEndereco().getTelefoneres());
			}
			if (paciente.getEndereco().getTelefonecel() == null) {
				stmt.setNull(19, Types.CHAR);
			} else {
				stmt.setString(19, paciente.getEndereco().getTelefonecel());
			}
			if (paciente.getEndereco().getTelefonetrab() == null) {
				stmt.setNull(20, Types.CHAR);
			} else {
				stmt.setString(20, paciente.getEndereco().getTelefonetrab());
			}
			if (paciente.getEndereco().getTelefoneorelhao() == null) {
				stmt.setNull(21, Types.CHAR);
			} else {
				stmt.setString(21, paciente.getEndereco().getTelefoneorelhao());
			}
			stmt.setString(22, paciente.getRg().toUpperCase().trim());
			stmt.setString(23, paciente.getOe().toUpperCase().trim());
			stmt.setDate(24, new java.sql.Date(paciente.getDataExpedicao1()
					.getTime()));
			stmt.setString(25, paciente.getCpf());
			stmt.setString(26, paciente.getCns().toUpperCase().trim());
			stmt.setInt(27, paciente.getProtant());
			stmt.setString(28, paciente.getReservista().toUpperCase().trim());
			stmt.setInt(29, paciente.getCtps());
			stmt.setInt(30, paciente.getSerie());
			stmt.setString(31, paciente.getPis().toUpperCase().trim());
			stmt.setString(32, paciente.getCartorio().toUpperCase().trim());
			stmt.setString(33, paciente.getNumeroCartorio().toUpperCase()
					.trim());
			stmt.setString(34, paciente.getLivro().toUpperCase().trim());
			stmt.setInt(35, paciente.getFolha());
			stmt.setDate(36, new java.sql.Date(paciente.getDataExpedicao2()
					.getTime()));
			stmt.setString(37, paciente.getAssociado().toUpperCase().trim());
			if (paciente.getEscolaridade().getCodescolaridade() == null) {
				stmt.setNull(38, Types.INTEGER);
			} else {
				stmt.setInt(38, paciente.getEscolaridade().getCodescolaridade());
			}
			if (paciente.getEscola().getCodEscola() == null) {
				stmt.setInt(39, Types.INTEGER);
			} else {
				stmt.setInt(39, paciente.getEscola().getCodEscola());
			}
			if (paciente.getProfissao().getCodprofissao() == null) {
				stmt.setNull(40, Types.INTEGER);
			} else {
				stmt.setInt(40, paciente.getProfissao().getCodprofissao());
			}
			stmt.setString(41, paciente.getTrabalha().toUpperCase().trim());
			stmt.setString(42, paciente.getLocaltrabalha().toUpperCase().trim());
			stmt.setInt(43, paciente.getCodparentesco());
			stmt.setString(44, paciente.getNomeresp().toUpperCase().trim());
			stmt.setString(45, paciente.getRgresp().toUpperCase().trim());
			stmt.setString(46, paciente.getCpfresp());
			stmt.setDate(47, new java.sql.Date(paciente.getDataNascimentoresp()
					.getTime()));
			if (paciente.getEncaminhado().getCodencaminhado() == null) {
				stmt.setNull(48, Types.INTEGER);
			} else {
				stmt.setInt(48, paciente.getEncaminhado().getCodencaminhado());
			}
			if (paciente.getFormatransporte().getCodformatransporte() == null) {
				stmt.setNull(49, Types.INTEGER);
			} else {
				stmt.setInt(49, paciente.getFormatransporte()
						.getCodformatransporte());
			}
			stmt.setString(50, paciente.getDeficiencia().toUpperCase().trim());
			if (paciente.getTipoDeficiencia() == null) {
				stmt.setNull(51, Types.CHAR);
			} else {
				stmt.setString(51, paciente.getTipoDeficiencia());
			}

			// ResultSet rs = stmt.executeQuery();
			/*
			 * if(rs.next()) { PacienteBean p = paciente; String idRetorno =
			 * null; idRetorno = String.valueOf(rs.getLong("id_paciente"));
			 * p.setId_paciente(Long.parseLong(idRetorno));
			 * System.out.println("|THU1|"+ idRetorno); }
			 */
			stmt.execute();
			System.out.println("|THU|" + paciente.getNome());
			System.out.println("passou aqui 4");
			conexao.commit();
			cadastrou = true;
			conexao.close();

			return cadastrou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Boolean alterar(PacienteBean paciente) throws ProjetoException {
		boolean alterou = false;
		String sql = "update hosp.pacientes set nome = ?, dtanascimento = ?, estcivil = ?, sexo = ? , sangue = ?, pai = ? "
				+ ", mae = ?, conjuge = ?, codraca = ?, cep = ?, uf = ?, cidade = ?, bairro = ?, logradouro = ?, numero = ?"
				+ ", complemento = ?, referencia = ?, telres = ?, telcel = ?, teltrab = ?, telorelhao = ?"
				+ ", rg = ?,  oe = ?, dtaexpedicaorg = ?, cpf = ?, cns = ?, protreab = ?"
				+ ", reservista = ?, ctps = ?, serie = ?, pis = ?, cartorio = ?, regnascimento = ?, livro = ?, folha = ?, dtaregistro = ?"
				+ ", contribuinte = ?, id_escolaridade = ?, id_escola = ?, id_profissao = ?, trabalha = ?, localtrabalha = ?"
				+ ", codparentesco = ?, nomeresp = ?, rgresp = ?, cpfresp = ?, dtanascimentoresp = ?, id_encaminhado = ?"
				+ ", id_formatransporte = ?, deficiencia = ?, tipodeficiencia = ? where id_paciente = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, paciente.getNome());
			stmt.setDate(2, new java.sql.Date(paciente.getDtanascimento().getTime()));
			stmt.setString(3, paciente.getEstadoCivil());
			stmt.setString(4, paciente.getSexo());
			stmt.setString(5, paciente.getSangue());
			stmt.setString(6, paciente.getNomePai());
			stmt.setString(7, paciente.getNomeMae());
			stmt.setString(8, paciente.getConjuge());
			stmt.setInt(9, paciente.getCodRaca());
			stmt.setInt(10, paciente.getEndereco().getCep());
			stmt.setString(11, paciente.getEndereco().getUf());
			stmt.setString(12, paciente.getEndereco().getMunicipio());
			stmt.setString(13, paciente.getEndereco().getBairro());
			stmt.setString(14, paciente.getEndereco().getLogradouro());
			stmt.setString(15, paciente.getEndereco().getNumero());
			stmt.setString(16, paciente.getEndereco().getComplemento());
			stmt.setString(17, paciente.getEndereco().getReferencia());
            stmt.setString(18, paciente.getEndereco().getTelefoneres());
            stmt.setString(19, paciente.getEndereco().getTelefonecel());
            stmt.setString(20, paciente.getEndereco().getTelefonetrab());
		    stmt.setString(21, paciente.getEndereco().getTelefoneorelhao());
			stmt.setString(22, paciente.getRg());
			stmt.setString(23, paciente.getOe());
			stmt.setDate(24, new java.sql.Date(paciente.getDataExpedicao1().getTime()));
			stmt.setString(25, paciente.getCpf());
			stmt.setString(26, paciente.getCns());
			stmt.setInt(27, paciente.getProtant());
			stmt.setString(28, paciente.getReservista());
			stmt.setInt(29, paciente.getCtps());
			stmt.setInt(30, paciente.getSerie());
			stmt.setString(31, paciente.getPis());
			stmt.setString(32, paciente.getCartorio());
			stmt.setString(33, paciente.getNumeroCartorio());
			stmt.setString(34, paciente.getLivro());
			stmt.setInt(35, paciente.getFolha());
			stmt.setDate(36, new java.sql.Date(paciente.getDataExpedicao2().getTime()));
			stmt.setString(37, paciente.getAssociado().toUpperCase().trim());
            stmt.setInt(38, paciente.getEscolaridade().getCodescolaridade());
            stmt.setInt(39, paciente.getEscola().getCodEscola());
            stmt.setInt(40, paciente.getProfissao().getCodprofissao());
			stmt.setString(41, paciente.getTrabalha());
			stmt.setString(42, paciente.getLocaltrabalha());
			stmt.setInt(43, paciente.getCodparentesco());
			stmt.setString(44, paciente.getNomeresp());
			stmt.setString(45, paciente.getRgresp());
			stmt.setString(46, paciente.getCpfresp());
			stmt.setDate(47, new java.sql.Date(paciente.getDataNascimentoresp().getTime()));
            stmt.setInt(48, paciente.getEncaminhado().getCodencaminhado());
            stmt.setInt(49, paciente.getFormatransporte().getCodformatransporte());
            stmt.setString(50, paciente.getDeficiencia());
            stmt.setString(51, paciente.getTipoDeficiencia());
			stmt.setLong(52, paciente.getId_paciente());
			stmt.executeUpdate();

			conexao.commit();

			alterou = true;

			return alterou;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public Boolean excluir(PacienteBean paciente) throws ProjetoException {
		boolean excluir = false;
		String sql = "delete from hosp.pacientes where id_paciente = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setLong(1, paciente.getId_paciente());
			stmt.executeUpdate();

			conexao.commit();

			excluir = true;

			return excluir;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	  public ArrayList<PacienteBean> listaPacientes() {

          String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
          		+ "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf, pacientes.cidade, pacientes.bairro, "
          		+ "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, pacientes.telres, pacientes.telcel,"
          		+ " pacientes.teltrab, pacientes.telorelhao, pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
          		+ "pacientes.protreab, "
          		+ "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
          		+ "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
          		+ "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
          		+ "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
          		+ "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.tipodeficiencia, escolaridade.descescolaridade, escola.descescola, profissao.descprofissao,"
          		+ " encaminhado.descencaminhado, formatransporte.descformatransporte "
          		+ "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
          		+ " left join hosp.escola on pacientes.id_escola=escola.id_escola "
          		+ "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
          		+ "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
          		+ "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte order by pacientes.nome";

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
	                p.getEndereco().setCep(rs.getInt("cep"));
	                p.getEndereco().setUf(rs.getString("uf"));
	                p.getEndereco().setMunicipio(rs.getString("cidade"));
	                p.getEndereco().setBairro(rs.getString("bairro"));
	                p.getEndereco().setLogradouro(rs.getString("logradouro"));
	                p.getEndereco().setNumero(rs.getString("numero"));
	                p.getEndereco().setComplemento(rs.getString("complemento"));
	                p.getEndereco().setReferencia(rs.getString("referencia"));
	                p.getEndereco().setTelefoneres(rs.getString("telres"));
	                p.getEndereco().setTelefonecel(rs.getString("telcel"));
	                p.getEndereco().setTelefonetrab(rs.getString("teltrab"));
	                p.getEndereco().setTelefoneorelhao(rs.getString("telorelhao"));             
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
	                p.getEscolaridade().setCodescolaridade(rs.getInt("id_escolaridade"));
	                p.getEscolaridade().setDescescolaridade(rs.getString("descescolaridade"));
	                p.getEscola().setCodEscola(rs.getInt("id_escola"));
	                p.getProfissao().setCodprofissao(rs.getInt("id_profissao"));
	                p.setTrabalha(rs.getString("trabalha"));
	                p.setLocaltrabalha(rs.getString("localtrabalha"));
	                p.setCodparentesco(rs.getInt("codparentesco"));
	                p.setNomeresp(rs.getString("nomeresp"));
	                p.setRgresp(rs.getString("rgresp"));
	                p.setCpfresp(rs.getString("cpfresp"));
	                p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));
	                p.getEncaminhado().setCodencaminhado(rs.getInt("id_encaminhado"));
	                p.getFormatransporte().setCodformatransporte(rs.getInt("id_formatransporte"));
	                p.setDeficiencia(rs.getString("deficiencia"));
	                p.setTipoDeficiencia(rs.getString("tipodeficiencia"));
	                p.getEscola().setDescescola(rs.getString("descescola"));
	                p.getProfissao().setDescprofissao(rs.getString("descprofissao"));
	                p.getEncaminhado().setDescencaminhado(rs.getString("descencaminhado"));
	                p.getFormatransporte().setDescformatransporte(rs.getString("descformatransporte"));
	                 
	                
	                lista.add(p);
              }
          } catch (SQLException ex) {
              throw new RuntimeException(ex);//////////
          } finally {
              try {
                  conexao.close();
              } catch (Exception ex) {
                  ex.printStackTrace();
                  System.exit(1);
              }
          }
          return lista;
      }

	public ArrayList<RacaBean> listaCor() {

		String sql = "select  id_raca, descraca from hosp.raca order by descraca";

		ArrayList<RacaBean> lista = new ArrayList<RacaBean>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				RacaBean p = new RacaBean();
				System.out.println("|1|");
				p.setCodRaca(rs.getInt("id_raca"));
				p.setDescRaca(rs.getString("descraca").toUpperCase());

				lista.add(p);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public ArrayList<EscolaBean> listaEscolas() {

		String sql = "select * from hosp.escola order by descescola";

		ArrayList<EscolaBean> lista = new ArrayList<EscolaBean>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				EscolaBean p = new EscolaBean();
				System.out.println("|1|");
				p.setCodEscola(rs.getInt("id_escola"));
				p.setDescescola(rs.getString("descescola").toLowerCase());
				p.setCodtipoescola(rs.getInt("codtipoescola"));

				lista.add(p);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}
	
	public List<PacienteBean> buscarTipoPaciente(String valor, Integer tipo) {
		System.out.println("Entrou Ass");
		//lpad(trim(to_char(pa.cpf,'99999999999')),11,'0') cpf
		 String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
          		+ "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf, pacientes.cidade, pacientes.bairro, "
          		+ "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, pacientes.telres, pacientes.telcel,"
          		+ " pacientes.teltrab, pacientes.telorelhao, pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
          		+ "pacientes.protreab, "
          		+ "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
          		+ "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
          		+ "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
          		+ "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
          		+ "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.tipodeficiencia, escolaridade.descescolaridade, escola.descescola, profissao.descprofissao,"
          		+ " encaminhado.descencaminhado, formatransporte.descformatransporte "
          		+ "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
          		+ " left join hosp.escola on pacientes.id_escola=escola.id_escola "
          		+ "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
          		+ "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
          		+ "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte where";
		
		if (tipo == 1) {
			sql += " pacientes.nome like ? order by pacientes.nome ";
		} else if (tipo == 2) {
			sql += " pacientes.cpf like ? order by pacientes.nome ";
			// lpad(trim(to_char(pacientes.cpf,'99999999999')),11,'0')
		} 

		List<PacienteBean> lista = new ArrayList<>();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			if (tipo == 1) {
				stmt.setString(1, "%" + valor.toUpperCase() + "%");
			} else if (tipo == 2) {
				stmt.setString(1, valor.replaceAll("[^0-9]", ""));
			} 

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				PacienteBean p = new PacienteBean();
				
				 p.setId_paciente(rs.getInt("id_paciente"));
	                p.setNome(rs.getString("nome").toLowerCase());
	                p.setDtanascimento(rs.getDate("dtanascimento"));
	                p.setEstadoCivil(rs.getString("estcivil"));
	                p.setSexo(rs.getString("sexo"));
	                p.setSangue(rs.getString("sangue"));
	                p.setNomePai(rs.getString("pai"));
	                p.setNomeMae(rs.getString("mae"));
	                p.setConjuge(rs.getString("conjuge"));
	                p.setCodRaca(rs.getInt("codraca"));
	                p.getEndereco().setCep(rs.getInt("cep"));
	                p.getEndereco().setUf(rs.getString("uf"));
	                p.getEndereco().setMunicipio(rs.getString("cidade"));
	                p.getEndereco().setBairro(rs.getString("bairro"));
	                p.getEndereco().setLogradouro(rs.getString("logradouro"));
	                p.getEndereco().setNumero(rs.getString("numero"));
	                p.getEndereco().setComplemento(rs.getString("complemento"));
	                p.getEndereco().setReferencia(rs.getString("referencia"));
	                p.getEndereco().setTelefoneres(rs.getString("telres"));
	                p.getEndereco().setTelefonecel(rs.getString("telcel"));
	                p.getEndereco().setTelefonetrab(rs.getString("teltrab"));
	                p.getEndereco().setTelefoneorelhao(rs.getString("telorelhao"));             
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
	                p.getEscolaridade().setCodescolaridade(rs.getInt("id_escolaridade"));
	                p.getEscolaridade().setDescescolaridade(rs.getString("descescolaridade"));
	                p.getEscola().setCodEscola(rs.getInt("id_escola"));
	                p.getProfissao().setCodprofissao(rs.getInt("id_profissao"));
	                p.setTrabalha(rs.getString("trabalha"));
	                p.setLocaltrabalha(rs.getString("localtrabalha"));
	                p.setCodparentesco(rs.getInt("codparentesco"));
	                p.setNomeresp(rs.getString("nomeresp"));
	                p.setRgresp(rs.getString("rgresp"));
	                p.setCpfresp(rs.getString("cpfresp"));
	                p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));
	                p.getEncaminhado().setCodencaminhado(rs.getInt("id_encaminhado"));
	                p.getFormatransporte().setCodformatransporte(rs.getInt("id_formatransporte"));
	                p.setDeficiencia(rs.getString("deficiencia"));
	                p.setTipoDeficiencia(rs.getString("tipodeficiencia"));
	                p.getEscola().setDescescola(rs.getString("descescola"));
	                p.getProfissao().setDescprofissao(rs.getString("descprofissao"));
	                p.getEncaminhado().setDescencaminhado(rs.getString("descencaminhado"));
	                p.getFormatransporte().setDescformatransporte(rs.getString("descformatransporte"));
				
				

				lista.add(p);

			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			// throw new RuntimeException(ex); //
		} finally {
			try {
				conexao.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public PacienteBean buscapacientecodigo(Integer i) throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {

			String sql = "select id_profissao from hosp.paciente where id_paciente=? order by id_profissao";

			ps = conexao.prepareStatement(sql);
			ps.setInt(1, i);
			ResultSet rs = ps.executeQuery();

			PacienteBean paciente = new PacienteBean();
			while (rs.next()) {

				paciente.setCodProfissao(rs.getInt(1));

			}
			return paciente;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}

	public List<PacienteBean> listarPacientesAgenda() {
		Connection con = null;
		List<PacienteBean> lista = new ArrayList<>();
		String sql = "select id_paciente, protreab, nome, cpf from hosp.pacientes";
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				PacienteBean paci = new PacienteBean();
				paci.setId_paciente(rs.getInt("id_paciente"));
				paci.setProtant(rs.getInt("protreab"));
				paci.setNome(rs.getString("nome"));
				paci.setCpf(rs.getString("cpf"));
				lista.add(paci);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		return lista;
	}

	public PacienteBean buscarPacienteAgenda(String tipo, String conteudo)
			throws ProjetoException, SQLException {
		
		 String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
	          		+ "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf, pacientes.cidade, pacientes.bairro, "
	          		+ "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, pacientes.telres, pacientes.telcel,"
	          		+ " pacientes.teltrab, pacientes.telorelhao, pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
	          		+ "pacientes.protreab, "
	          		+ "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
	          		+ "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
	          		+ "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
	          		+ "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
	          		+ "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.tipodeficiencia, escolaridade.descescolaridade, escola.descescola, profissao.descprofissao,"
	          		+ " encaminhado.descencaminhado, formatransporte.descformatransporte "
	          		+ "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
	          		+ " left join hosp.escola on pacientes.id_escola=escola.id_escola "
	          		+ "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
	          		+ "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
	          		+ "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte where";
		
		conexao = ConnectionFactory.getConnection();
		
		PacienteBean p = new PacienteBean();

		if (tipo.equals("CPF")) {
			System.out.println("Vai buscar por cpf");
			sql += " cpf = ? ;";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, conteudo.toUpperCase());
		}
		if (tipo.equals("NOME")) {
			System.out.println("Vai buscar por NOME");
			sql += " nome LIKE ? ;";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, '%' + conteudo.toUpperCase() + '%');
		}
		if (tipo.equals("PRONTVELHO")) {
			System.out.println("Vai buscar por pront velho");
			sql += " protreab = ? ;";
			ps = conexao.prepareStatement(sql);
			Integer prontVelho = null;
			try {
				prontVelho = Integer.parseInt(conteudo);
			} catch (Exception e) {
				return p;
			}

			if (prontVelho != null)
				ps.setInt(1, prontVelho);
		}
		if (tipo.equals("PRONTNOVO")) {
			System.out.println("Vai buscar por pront novo");
			sql += " id_paciente = ? ;";
			ps = conexao.prepareStatement(sql);
			Long prontNovo = null;
			try {
				prontNovo = Long.parseLong(conteudo);
			} catch (Exception e) {
				return p;
			}
			if (prontNovo != null)
				ps.setLong(1, prontNovo);
		}
		try {
			ResultSet rs = ps.executeQuery();

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
                p.getEndereco().setCep(rs.getInt("cep"));
                p.getEndereco().setUf(rs.getString("uf"));
                p.getEndereco().setMunicipio(rs.getString("cidade"));
                p.getEndereco().setBairro(rs.getString("bairro"));
                p.getEndereco().setLogradouro(rs.getString("logradouro"));
                p.getEndereco().setNumero(rs.getString("numero"));
                p.getEndereco().setComplemento(rs.getString("complemento"));
                p.getEndereco().setReferencia(rs.getString("referencia"));
                p.getEndereco().setTelefoneres(rs.getString("telres"));
                p.getEndereco().setTelefonecel(rs.getString("telcel"));
                p.getEndereco().setTelefonetrab(rs.getString("teltrab"));
                p.getEndereco().setTelefoneorelhao(rs.getString("telorelhao"));             
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
                p.getEscolaridade().setCodescolaridade(rs.getInt("id_escolaridade"));
                p.getEscolaridade().setDescescolaridade(rs.getString("descescolaridade"));
                p.getEscola().setCodEscola(rs.getInt("id_escola"));
                p.getProfissao().setCodprofissao(rs.getInt("id_profissao"));
                p.setTrabalha(rs.getString("trabalha"));
                p.setLocaltrabalha(rs.getString("localtrabalha"));
                p.setCodparentesco(rs.getInt("codparentesco"));
                p.setNomeresp(rs.getString("nomeresp"));
                p.setRgresp(rs.getString("rgresp"));
                p.setCpfresp(rs.getString("cpfresp"));
                p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));
                p.getEncaminhado().setCodencaminhado(rs.getInt("id_encaminhado"));
                p.getFormatransporte().setCodformatransporte(rs.getInt("id_formatransporte"));
                p.setDeficiencia(rs.getString("deficiencia"));
                p.setTipoDeficiencia(rs.getString("tipodeficiencia"));
                p.getEscola().setDescescola(rs.getString("descescola"));
                p.getProfissao().setDescprofissao(rs.getString("descprofissao"));
                p.getEncaminhado().setDescencaminhado(rs.getString("descencaminhado"));
                p.getFormatransporte().setDescformatransporte(rs.getString("descformatransporte"));
                 
			}
			return p;
		} catch (Exception sqle) {
			throw new ProjetoException(sqle);
		} finally {
			try {
				conexao.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public PacienteBean listarPacientePorID(int id) throws SQLException {
		PacienteBean p = new PacienteBean();
		
		 String sql = "select pacientes.id_paciente, pacientes.nome, pacientes.dtanascimento, pacientes.estcivil, pacientes.sexo, pacientes.sangue, "
	          		+ "pacientes.pai, pacientes.mae, pacientes.conjuge,pacientes.codraca, pacientes.cep, pacientes.uf, pacientes.cidade, pacientes.bairro, "
	          		+ "pacientes.logradouro, pacientes.numero, pacientes.complemento, pacientes.referencia, pacientes.telres, pacientes.telcel,"
	          		+ " pacientes.teltrab, pacientes.telorelhao, pacientes.rg, pacientes.oe, pacientes.dtaexpedicaorg, pacientes.cpf, pacientes.cns, "
	          		+ "pacientes.protreab, "
	          		+ "pacientes.reservista, pacientes.ctps, pacientes.serie, pacientes.pis, pacientes.cartorio, pacientes.regnascimento, pacientes.livro, "
	          		+ "pacientes.folha, pacientes.dtaregistro, pacientes.contribuinte, pacientes.id_escolaridade, pacientes.id_escola, pacientes.id_profissao, "
	          		+ "pacientes.trabalha, pacientes.localtrabalha, pacientes.codparentesco, "
	          		+ "pacientes.nomeresp, pacientes.rgresp, pacientes.cpfresp, pacientes.dtanascimentoresp, pacientes.id_encaminhado, "
	          		+ "pacientes.id_formatransporte ,pacientes.deficiencia, pacientes.tipodeficiencia, escolaridade.descescolaridade, escola.descescola, profissao.descprofissao,"
	          		+ " encaminhado.descencaminhado, formatransporte.descformatransporte "
	          		+ "from hosp.pacientes left join hosp.escolaridade on pacientes.id_escolaridade=escolaridade.id_escolaridade "
	          		+ " left join hosp.escola on pacientes.id_escola=escola.id_escola "
	          		+ "left join hosp.profissao on pacientes.id_profissao=profissao.id_profissao "
	          		+ "left join hosp.encaminhado on pacientes.id_encaminhado=encaminhado.id_encaminhado "
	          		+ "left join hosp.formatransporte on pacientes.id_formatransporte=formatransporte.id_formatransporte where id_paciente = ?";
		
		try {
			con = ConnectionFactory.getConnection();
			PreparedStatement stm = con.prepareStatement(sql);
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
	                p.getEndereco().setCep(rs.getInt("cep"));
	                p.getEndereco().setUf(rs.getString("uf"));
	                p.getEndereco().setMunicipio(rs.getString("cidade"));
	                p.getEndereco().setBairro(rs.getString("bairro"));
	                p.getEndereco().setLogradouro(rs.getString("logradouro"));
	                p.getEndereco().setNumero(rs.getString("numero"));
	                p.getEndereco().setComplemento(rs.getString("complemento"));
	                p.getEndereco().setReferencia(rs.getString("referencia"));
	                p.getEndereco().setTelefoneres(rs.getString("telres"));
	                p.getEndereco().setTelefonecel(rs.getString("telcel"));
	                p.getEndereco().setTelefonetrab(rs.getString("teltrab"));
	                p.getEndereco().setTelefoneorelhao(rs.getString("telorelhao"));             
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
	                p.getEscolaridade().setCodescolaridade(rs.getInt("id_escolaridade"));
	                p.getEscolaridade().setDescescolaridade(rs.getString("descescolaridade"));
	                p.getEscola().setCodEscola(rs.getInt("id_escola"));
	                p.getProfissao().setCodprofissao(rs.getInt("id_profissao"));
	                p.setTrabalha(rs.getString("trabalha"));
	                p.setLocaltrabalha(rs.getString("localtrabalha"));
	                p.setCodparentesco(rs.getInt("codparentesco"));
	                p.setNomeresp(rs.getString("nomeresp"));
	                p.setRgresp(rs.getString("rgresp"));
	                p.setCpfresp(rs.getString("cpfresp"));
	                p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));
	                p.getEncaminhado().setCodencaminhado(rs.getInt("id_encaminhado"));
	                p.getFormatransporte().setCodformatransporte(rs.getInt("id_formatransporte"));
	                p.setDeficiencia(rs.getString("deficiencia"));
	                p.setTipoDeficiencia(rs.getString("tipodeficiencia"));
	                p.getEscola().setDescescola(rs.getString("descescola"));
	                p.getProfissao().setDescprofissao(rs.getString("descprofissao"));
	                p.getEncaminhado().setDescencaminhado(rs.getString("descencaminhado"));
	                p.getFormatransporte().setDescformatransporte(rs.getString("descformatransporte"));
			}
			
			return p;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				con.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		
	}
	
	
	

}
