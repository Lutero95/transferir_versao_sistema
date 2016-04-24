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
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.RacaBean;

public class PacienteDAO {
	private Connection conexao = null;
	private PreparedStatement ps;

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
				+ "reservista, ctps, serie, pis, cartorio, regnascimento, livro, folha, dtaregistro, contribuinte, codescolaridade, codescola, codprofissao, trabalha, localtrabalha, codparentesco, "
				+ "nomeresp, rgresp, cpfresp, dtanascimentoresp, codencaminhado, codformatransporte ,deficiencia, tipodeficiencia)"
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
			if (paciente.getCodFormaTransporte() == null) {
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
		String sql = "update hosp.pacientes set nome = ? where id_paciente = ?";
		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, paciente.getNome());
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
			stmt.execute();

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

		String sql = "select id_paciente, nome, dtanascimento, estcivil, sexo, sangue, "
				+ "pai, mae, conjuge,codraca, cep, uf, cidade, bairro, logradouro, numero, complemento, referencia, telres, telcel, teltrab, telorelhao, rg, oe, dtaexpedicaorg, cpf, cns, protreab, "
				+ "reservista, ctps, serie, pis, cartorio, regnascimento, livro, folha, dtaregistro, contribuinte, codescolaridade, codescola, codprofissao, trabalha, localtrabalha, codparentesco, "
				+ "nomeresp, rgresp, cpfresp, dtanascimentoresp, codencaminhado, codformatransporte ,deficiencia, tipodeficiencia from hosp.pacientes order by nome";

		ArrayList<PacienteBean> lista = new ArrayList();

		try {
			conexao = ConnectionFactory.getConnection();
			PreparedStatement stm = conexao.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				PacienteBean p = new PacienteBean();
				p.setId_paciente(rs.getLong("id_paciente"));
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
				p.getEscolaridade().setCodescolaridade(
						rs.getInt("codescolaridade"));
				p.getEscola().setCodEscola(rs.getInt("codescola"));
				p.getProfissao().setCodprofissao(rs.getInt("codprofissao"));
				p.setTrabalha(rs.getString("trabalha"));
				p.setLocaltrabalha(rs.getString("localtrabalha"));
				p.setCodparentesco(rs.getInt("codparentesco"));
				p.setNomeresp(rs.getString("nomeresp"));
				p.setRgresp(rs.getString("rgresp"));
				p.setCpfresp(rs.getString("cpfresp"));
				p.setDataNascimentoresp(rs.getDate("dtanascimentoresp"));
				p.getEncaminhado().setCodencaminhado(
						rs.getInt("codencaminhado"));
				p.getFormatransporte().setCodformatransporte(
						rs.getInt("codformatransporte"));
				p.setDeficiencia(rs.getString("deficiencia"));
				p.setTipoDeficiencia(rs.getString("tipodeficiencia"));

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
				p.setCodEscola(rs.getInt("codescola"));
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

	public PacienteBean buscapacientecodigo(Integer i) throws ProjetoException {
		PreparedStatement ps = null;
		conexao = ConnectionFactory.getConnection();

		try {

			String sql = "select codprofissao from hosp.paciente where id_paciente=? order by codprofissao";

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
				paci.setId_paciente(rs.getLong("id_paciente"));
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

		String sql = "";
		conexao = ConnectionFactory.getConnection();
		PacienteBean paciente = new PacienteBean();

		if (tipo.equals("CPF")) {
			System.out.println("Vai buscar por cpf");
			sql = "select id_paciente, protreab, nome, cpf from hosp.pacientes where cpf = ? ;";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, conteudo.toUpperCase());
		}
		if (tipo.equals("NOME")) {
			System.out.println("Vai buscar por NOME");
			sql = "select id_paciente, protreab, nome, cpf from hosp.pacientes where nome LIKE ? ;";
			ps = conexao.prepareStatement(sql);
			ps.setString(1, '%' + conteudo.toUpperCase() + '%');
		}
		if (tipo.equals("PRONTVELHO")) {
			System.out.println("Vai buscar por pront velho");
			sql = "select id_paciente, protreab, nome, cpf from hosp.pacientes where protreab = ? ;";
			ps = conexao.prepareStatement(sql);
			Integer prontVelho = null;
			try {
				prontVelho = Integer.parseInt(conteudo);
			} catch (Exception e) {
				return paciente;
			}

			if (prontVelho != null)
				ps.setInt(1, prontVelho);
		}
		if (tipo.equals("PRONTNOVO")) {
			System.out.println("Vai buscar por pront novo");
			sql = "select id_paciente, protreab, nome, cpf from hosp.pacientes where id_paciente = ? ;";
			ps = conexao.prepareStatement(sql);
			Long prontNovo = null;
			try {
				prontNovo = Long.parseLong(conteudo);
			} catch (Exception e) {
				return paciente;
			}
			if (prontNovo != null)
				ps.setLong(1, prontNovo);
		}
		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				paciente.setId_paciente(rs.getLong("id_paciente"));
				paciente.setProtant(rs.getInt("protreab"));
				paciente.setNome(rs.getString("nome"));
				paciente.setCpf(rs.getString("cpf"));
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
			}
		}
	}

}
