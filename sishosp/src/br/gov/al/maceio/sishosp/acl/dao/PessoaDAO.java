package br.gov.al.maceio.sishosp.acl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.ConnectionFactory;
import br.gov.al.maceio.sishosp.acl.control.PessoaController;
import br.gov.al.maceio.sishosp.acl.model.PessoaBean;

public class PessoaDAO {

	public List<PessoaBean> ConsultaPessoa(String campotexto, Integer busca)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		try {
			String sql = "select "
					+ "nome, dtnasc2, sexo,estciv,coalesce( sangue,''), instruc, naturale, coalesce(nomepai,''), coalesce(nomemae,''),"
					+ "coalesce(conjuge,''), dtnascconjuge, naciona, Trim(coalesce(munnat,'')), flgnat, coalesce(ender,''), endernum, coalesce(enderc,''),"
					+ "coalesce(bairro,''),coalesce(cidade,''), coalesce(uf,''), idnum, idser, iduf,Dtincor2, Trim(profnum), Trim(profser), profuf,"
					+ "pispas, replace(to_char(cpf, '000:000:000-00'), ':', '.'), Trim(resnum), Trim(rescsm), elenum, elezona, elesec, eleuf , rpnum,rpuf, rpexp, chcrs, catchcrs,"
					+ " dtvalchcrs, id_pessoa, racacor, cep,trim(to_char(telef,'999999999999')) as telef, email1,trim(to_char(celular,'999999999999')) AS celular "
					+ " from fp.cadpessoa ";

			if (busca == 1) {
				sql += " where lpad(trim(to_char(cpf,'99999999999')),11,'0') like ? ";

			} else if (busca == 2) {
				sql += " where nome like ? ";
			}
			sql += " order by nome desc";
			ps = con.prepareStatement(sql);

			if (busca == 1) {
				ps.setString(1, "%" + campotexto.replaceAll("[^0-9]", "") + "%");
			} else if (busca == 2) {
				ps.setString(1, "%" + campotexto.toUpperCase() + "%");
			}

			ResultSet rs = ps.executeQuery();

			List<PessoaBean> c = new ArrayList<PessoaBean>();

			while (rs.next()) {
				PessoaBean aux = new PessoaBean();
				aux.setNome(rs.getString(1));
				aux.setDtanasc2(rs.getDate(2));
				aux.setSexo(rs.getInt(3));
				aux.setEstcivil(rs.getInt(4));
				aux.setSangue(rs.getString(5).trim());
				aux.setInstruc(rs.getInt(6));
				aux.setNaturale(rs.getString(7));
				aux.setNomepai(rs.getString(8));
				aux.setNomemae(rs.getString(9));
				aux.setConjuge(rs.getString(10));
				aux.setDtnascconjuge(rs.getDate(11));
				aux.setNaciona(rs.getInt(12));
				aux.setMunnat(rs.getString(13));
				aux.setFlagnat(rs.getString(14));
				aux.setEnder(rs.getString(15));
				aux.setEndernum(rs.getInt(16));
				aux.setEnderc(rs.getString(17));
				aux.setBairro(rs.getString(18));
				aux.setCidade(rs.getString(19));
				aux.setUf(rs.getString(20));
				aux.setIdnum(rs.getString(21));
				aux.setIdser(rs.getString(22));
				aux.setIduf(rs.getString(23));
				aux.setDtincor2(rs.getDate(24));
				aux.setProfnum(rs.getString(25));
				aux.setProfser(rs.getString(26));
				aux.setProfuf(rs.getString(27));
				aux.setPispas(rs.getString(28));
				aux.setCpf(rs.getString(29));
				aux.setResnum(rs.getString(30));
				aux.setRescsm(rs.getString(31));
				aux.setElezona(rs.getInt(33));
				aux.setElesec(rs.getInt(34));
				aux.setEleuf(rs.getString(35));
				aux.setRpnum(rs.getString(36));
				aux.setRpuf(rs.getString(37));
				aux.setRpexp(rs.getString(38));
				aux.setChcrs(rs.getString(39));
				aux.setCatchcrs(rs.getString(40));
				aux.setDtvalchcrs(rs.getDate(41));
				aux.setId_pessoa(rs.getInt(42));
				if (rs.getObject("racacor") != null)
					aux.setRacacor(rs.getInt(43));
				if (rs.getObject("cep") != null) {
					aux.setCep(Integer.valueOf((String.valueOf(rs.getInt(44))
							.replaceAll("[^0123456789]", ""))));
				} else {
					aux.setCep(null);
				}
				if (rs.getObject("telef") != null) {
					aux.setTelef(rs.getString(45));
				}
				if (rs.getObject("email1") != null) {
					aux.setEmail1((rs.getString(46)));
				}
				// Double.valueOf(rs.getObject("celular").toString()).doubleValue()
				// == 0
				if (rs.getObject("celular") != null) {
					aux.setCelular(rs.getString(47));
				}

				c.add(aux);
			}

			return c;

		} catch (Exception sqle) {
			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}

	public List<PessoaBean> buscarpessoas(PessoaBean pess)
			throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		List<PessoaBean> c = new ArrayList<PessoaBean>();
		try {
			String sql = "select nome,lpad(trim(to_char(cpf,'99999999999')),11,'0') as cpf,id_pessoa from fp.cadpessoa "
					+ "where nome like ? and lpad(trim(to_char(cpf,'99999999999')),11,'0') like ? order by nome,cpf";

			ps = con.prepareStatement(sql);
			ps.setString(1, "%" + pess.getNome().toUpperCase() + "%");
			ps.setString(2, "%" + pess.getCpf().replaceAll("[^0-9]", "") + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PessoaBean aux = new PessoaBean();
				aux.setNome(rs.getString("nome"));
				aux.setCpf(rs.getString("cpf"));
				aux.setId_pessoa(rs.getInt("id_pessoa"));
				c.add(aux);
			}
			return c;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}

	}

	public PessoaBean pegarDadosPessoais(String cpf) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		try {
			String sql = "select nome, dtnasc2, sexo,estciv,coalesce( sangue,''), instruc, naturale, coalesce(nomepai,''), coalesce(nomemae,''),"
					+ " coalesce(conjuge,''), dtnascconjuge, naciona, coalesce(munnat,''), flgnat, coalesce(ender,''), endernum, coalesce(enderc,''), "
					+ "  coalesce(bairro,''),coalesce(cidade,''), coalesce(uf,''), idnum, idser, iduf,Dtincor2, profnum, profser, profuf,"
					+ " pispas, replace(to_char(cpf, '000:000:000-00'), ':', '.'), resnum, rescsm, elenum, elezona, elesec, eleuf , rpnum,rpuf, rpexp, chcrs, catchcrs,"
					+ " dtvalchcrs, id_pessoa, racacor, cep,trim(to_char(telef,'999999999999')) as telef,email1,trim(to_char(celular,'999999999999')) AS celular from fp.cadpessoa where cpf=?";

			ps = con.prepareStatement(sql);
			ps.setDouble(1, Double.valueOf(cpf));

			ResultSet rs = ps.executeQuery();
			// con.close();
			PessoaBean aux = new PessoaBean();
			while (rs.next()) {
				aux = new PessoaBean();
				aux.setNome(rs.getString(1));
				aux.setDtanasc2(rs.getDate(2));
				aux.setSexo(rs.getInt(3));
				aux.setEstcivil(rs.getInt(4));

				aux.setSangue(rs.getString(5).trim());

				aux.setInstruc(rs.getInt(6));

				aux.setNaturale(rs.getString(7));
				aux.setNomepai(rs.getString(8));
				aux.setNomemae(rs.getString(9));
				aux.setConjuge(rs.getString(10));
				aux.setDtnascconjuge(rs.getDate(11));
				aux.setNaciona(rs.getInt(12));
				aux.setMunnat(rs.getString(13));
				aux.setFlagnat(rs.getString(14));
				aux.setEnder(rs.getString(15));
				aux.setEndernum(rs.getInt(16));
				aux.setEnderc(rs.getString(17));
				aux.setBairro(rs.getString(18));
				aux.setCidade(rs.getString(19));
				aux.setUf(rs.getString(20));
				aux.setIdnum(rs.getString(21));
				aux.setIdser(rs.getString(22));
				aux.setIduf(rs.getString(23));
				aux.setDtincor2(rs.getDate(24));
				aux.setProfnum(rs.getString(25));
				aux.setProfser(rs.getString(26));
				aux.setProfuf(rs.getString(27));
				aux.setPispas(rs.getString(28));
				aux.setCpf(rs.getString(29));
				aux.setResnum(rs.getString(30));
				aux.setRescsm(rs.getString(31));
				aux.setElezona(rs.getInt(33));
				aux.setElesec(rs.getInt(34));
				aux.setEleuf(rs.getString(35));
				aux.setRpnum(rs.getString(36));
				aux.setRpuf(rs.getString(37));
				aux.setRpexp(rs.getString(38));
				aux.setChcrs(rs.getString(39));
				aux.setCatchcrs(rs.getString(40));
				aux.setDtvalchcrs(rs.getDate(41));
				aux.setId_pessoa(rs.getInt(42));
				if (rs.getObject("racacor") != null)
					aux.setRacacor(rs.getInt(43));
				if (rs.getObject("cep") != null)
					aux.setCep(Integer.valueOf((String.valueOf(rs.getInt(44))
							.replaceAll("[^0123456789]", ""))));
				if (rs.getObject("telef") != null)
					aux.setTelef(rs.getString(45));
				if (rs.getObject("email1") != null)
					aux.setEmail1((rs.getString(46)));
				if (rs.getObject("celular") != null)
					aux.setCelular(rs.getString(47));

			}

			return aux;

		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}
		}

	}

	public boolean atualizaDadosPessoais(PessoaBean U) throws ProjetoException {

		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();
		try {
			String sql = "update fp.cadpessoa set nome=?, dtnasc2=?,sexo=? ,"

					+ "estciv=?, sangue=?, instruc=?"
					+ " ,flgnat=?, nomepai=?, nomemae=?, conjuge=?, dtnascconjuge=?,"
					+ " naciona=?,naturale=?, munnat=?,  ender=?, endernum=?, enderc=?, "
					+ "  bairro=?,cidade=?, uf=?, "
					+ " idnum=?, idser=?, iduf=?,Dtincor2=?, profnum=?, profser=?, profuf=?,"
					+ " pispas=?, cpf=?, resnum=?, rescsm=?, elenum=?, elezona=?, elesec=?, eleuf=? , rpnum=?,rpuf=?, rpexp=?, chcrs=?, catchcrs=?,"
					+ " dtvalchcrs=?, racacor=?, cep=?, telef=?,celular=?,email1=? where id_pessoa=?";

			ps = con.prepareStatement(sql);

			ps.setString(1, U.getNome().toUpperCase());
			if (U.getDtanasc2() != null) {
				Calendar caldtnasc = Calendar.getInstance();
				caldtnasc.setTime(U.getDtanasc2());
				caldtnasc.add(Calendar.DATE, 1);
				java.util.Date dtnasc = caldtnasc.getTime();
				ps.setDate(2, new java.sql.Date(dtnasc.getTime()));
			} else {
				ps.setDate(2, null);
			}
			ps.setInt(3, U.getSexo());
			if (U.getEstcivil() != null) {
				ps.setInt(4, (U.getEstcivil()));
			} else {
				ps.setNull(4, Types.INTEGER);
			}
			ps.setString(5, U.getSangue());
			if (U.getInstruc() != null) {
				ps.setInt(6, U.getInstruc());
			} else {
				ps.setNull(6, Types.INTEGER);
			}
			ps.setString(7, U.getFlagnat());
			ps.setString(8, U.getNomepai().toUpperCase());
			ps.setString(9, U.getNomemae().toUpperCase());
			ps.setString(10, U.getConjuge().toUpperCase());

			if (U.getDtnascconjuge() != null) {
				Calendar caldtnascconj = Calendar.getInstance();
				caldtnascconj.setTime(U.getDtnascconjuge());
				caldtnascconj.add(Calendar.DATE, 1);
				java.util.Date dtnascconj = caldtnascconj.getTime();

				ps.setDate(11, new java.sql.Date(dtnascconj.getTime()));
			} else {
				ps.setDate(11, null);
			}
			if (U.getNaciona() != null) {
				ps.setInt(12, (U.getNaciona()));
			} else {
				ps.setNull(12, Types.INTEGER);
			}
			if (U.getNaturale() != null) {
				ps.setString(13, U.getNaturale());
			} else {
				ps.setNull(13, Types.VARCHAR);
			}
			ps.setString(14, U.getMunnat().toUpperCase());
			ps.setString(15, U.getEnder().toUpperCase());
			if (U.getEndernum() != null) {
				ps.setInt(16, (U.getEndernum()));
			} else {
				ps.setNull(16, Types.INTEGER);
			}
			ps.setString(17, U.getEnderc().toUpperCase());
			ps.setString(18, U.getBairro().toUpperCase());
			ps.setString(19, U.getCidade().toUpperCase());
			ps.setString(20, U.getUf());
			ps.setString(21, U.getIdnum());
			ps.setString(22, U.getIdser());
			ps.setString(23, U.getIduf());

			if (U.getDtincor2() != null) {
				Calendar caldtincor2 = Calendar.getInstance();
				caldtincor2.setTime(U.getDtincor2());
				caldtincor2.add(Calendar.DATE, 1);
				java.util.Date dtincor2 = caldtincor2.getTime();

				ps.setDate(24, new java.sql.Date(dtincor2.getTime()));

			} else {
				ps.setDate(24, null);
			}
			ps.setString(25, U.getProfnum());
			ps.setString(26, U.getProfser());
			ps.setString(27, U.getProfuf());
			if ((U.getPispas() != null) && (U.getPispas().trim().length() > 0)) {
				ps.setDouble(28, Double.valueOf((U.getPispas().replaceAll(
						"[^0123456789]", ""))));
			} else {
				ps.setNull(28, Types.DOUBLE);
			}
			if ((U.getCpf() != null) && (U.getCpf().trim().length() > 0)) {
				ps.setDouble(29, Double.valueOf((U.getCpf().toUpperCase()
						.replaceAll("[^0123456789]", ""))));
			} else {
				ps.setNull(29, Types.DOUBLE);
			}
			ps.setString(30, U.getResnum());
			ps.setString(31, U.getRescsm());
			if ((U.getElenum() != null) && (U.getElenum().trim().length() > 0)) {
				ps.setDouble(32, Double.valueOf((U.getElenum().replaceAll(
						"[^0123456789]", ""))));
			} else {
				ps.setNull(32, Types.DOUBLE);
			}
			if (U.getElezona() != null) {
				ps.setInt(33, (U.getElezona()));
			} else {
				ps.setNull(33, Types.INTEGER);
			}
			if (U.getElesec() != null) {
				ps.setInt(34, (U.getElesec()));
			} else {
				ps.setNull(34, Types.INTEGER);
			}
			if (U.getEleuf() != null) {
				ps.setString(35, U.getEleuf());
			} else {
				ps.setString(35, null);
			}
			if (U.getRpnum() != null) {
				ps.setString(36, U.getRpnum());
			} else {
				ps.setString(36, null);
			}
			if (U.getRpuf() != null) {
				ps.setString(37, U.getRpuf());
			} else {
				ps.setString(37, null);
			}
			if (U.getRpexp() != null) {
				ps.setString(38, U.getRpexp());
			} else {
				ps.setString(38, null);
			}
			if (U.getChcrs() != null) {
				ps.setString(39, U.getChcrs());
			} else {
				ps.setString(39, null);
			}
			if (U.getCatchcrs() != null) {
				ps.setString(40, U.getCatchcrs());
			} else {
				ps.setString(40, null);
			}
			if (U.getDtvalchcrs() != null) {
				Calendar caldtvalchcrs = Calendar.getInstance();
				caldtvalchcrs.setTime(U.getDtvalchcrs());
				caldtvalchcrs.add(Calendar.DATE, 1);
				java.util.Date dtvalchcrs = caldtvalchcrs.getTime();

				ps.setDate(41, new java.sql.Date(dtvalchcrs.getTime()));
			} else {
				ps.setDate(41, null);
			}
			if (U.getRacacor() != null) {
				ps.setInt(42, (U.getRacacor()));
			} else {
				ps.setNull(42, Types.INTEGER);
			}
			if (U.getCep() != null) {
				ps.setInt(43, Integer.valueOf((String.valueOf(U.getCep())
						.replaceAll("[^0123456789]", ""))));
			} else {
				ps.setNull(43, Types.INTEGER);
			}
			if ((U.getTelef() != null) && (U.getTelef() != "")) {
				ps.setDouble(44, Integer.valueOf((U.getTelef()).replaceAll(
						"[^0123456789]", "")));
			} else {
				ps.setNull(44, Types.DOUBLE);
			}
			if ((U.getCelular() != null) && (U.getCelular() != "")) {
				ps.setDouble(45, Integer.valueOf((U.getCelular()).replaceAll(
						"[^0123456789]", "")));
			} else {
				ps.setNull(45, Types.DOUBLE);
			}
			if ((U.getEmail1()) != null && (U.getEmail1() != "")) {
				ps.setString(46, U.getEmail1().toUpperCase());
			} else {
				ps.setNull(46, Types.VARCHAR);
			}
			ps.setInt(47, U.getId_pessoa());
			ps.executeUpdate();

			con.commit();
			ps.close();
			return true;
		} catch (Exception sqle) {
			FacesMessage msg = new FacesMessage(sqle.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);
				// TODO: handle exception
			}

		}
	}

	public boolean gravarPessoa(PessoaBean p) throws ProjetoException {
		boolean gravou = false;

		Connection con = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		String sql = "insert into fp.cadpessoa (nome, dtnasc2, sexo, estciv, sangue, instruc,"
				+ " naturale,nomepai, nomemae, naciona, munnat, idnum, idser, iduf, profnum,"
				+ " profser, profuf, cpf, pispas, elenum, elezona, elesec, eleuf, resnum, rescsm, resser,"
				+ " ender, endernum, enderc, bairro, cep, telef, celular, email1, depir, depsf, dtincor, "
				+ " conjugue, dtnasconjugue, racacor, tipodeficiencia, dtcheg2, flgnat, rpnum, rpexp, rpuf,"
				+ " chcrs, dtvalchcrs, catchcrs)"
				+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, p.getNome());
			ps.setDate(2, null);
			ps.setInt(3, p.getSexo());
			ps.setInt(4, p.getEstcivil());
			ps.setString(5, p.getSangue());
			ps.setInt(6, p.getInstruc());
			ps.setString(7, p.getNaturale());
			ps.setString(8, p.getNomepai());
			ps.setString(9, p.getNomemae());
			ps.setInt(10, p.getNaciona());
			ps.setString(11, p.getMunnat());
			ps.setString(12, p.getIdnum());
			ps.setString(13, p.getIdser());
			ps.setString(14, p.getIduf());
			ps.setString(15, p.getProfnum());
			ps.setString(16, "profser");
			ps.setString(17, "profuf");
			ps.setString(18, "cpf");
			ps.setString(19, "pispas");
			ps.setString(20, "elenum");
			ps.setString(21, "elezona");
			ps.setString(22, "elesec");
			ps.setString(23, "eleuf");
			ps.setString(24, "resnum");
			ps.setString(25, "rescsm");
			ps.setString(26, "resser");
			ps.setString(27, "ender");
			ps.setString(28, "endernum");
			ps.setString(29, "enderc");
			ps.setString(30, "bairro");
			ps.setString(31, "cep");
			ps.setString(32, "telef");
			ps.setString(33, "celular");
			ps.setString(34, "email1");
			ps.setString(35, "depir");
			ps.setString(36, "depsf");
			ps.setString(37, "dtincor");
			ps.setString(38, "conjugue");
			ps.setString(39, "dtnasconjungue");
			ps.setString(40, "racacor");
			ps.setString(41, "tipodeficiencia");
			ps.setString(42, "dtcheg2");
			ps.setString(43, "flgnat");
			ps.setString(44, "rpnum");
			ps.setString(45, "rpexp");
			ps.setString(46, "rpuf");
			ps.setString(47, "chcrs");
			ps.setString(48, "dtvalchcrs");
			ps.setString(49, "catchcrs");

			ps.execute();
			con.commit();
			con.close();
			ps.close();
			gravou = true;

		} catch (SQLException e) {
			gravou = false;
			e.printStackTrace();
		}

		return gravou;
	}

	public int buscaIDPessoa(double cpf) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		try {
			String sql = "select cadpessoa.id_pessoa from fp.cadpessoa where cpf=?";

			ps = con.prepareStatement(sql);
			ps.setDouble(1, cpf);
			ResultSet rs = ps.executeQuery();

			int idpessoa = 0;

			while (rs.next()) {

				idpessoa = rs.getInt(1);
			}
			return idpessoa;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);

			}
		}
	}

	public boolean buscaCPF(double cpf) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		try {
			String sql = "select cadpessoa.id_pessoa from fp.cadpessoa where cpf=?";

			ps = con.prepareStatement(sql);
			ps.setDouble(1, cpf);
			ResultSet rs = ps.executeQuery();

			boolean result = false;

			while (rs.next()) {

				result = true;
			}
			return result;

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("CPF já cadastrado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);

			}
		}
		return false;
	}

	public Integer buscaIdPessoa(double cpf) throws ProjetoException {
		PreparedStatement ps = null;
		Connection con = ConnectionFactory.getConnection();

		try {
			String sql = "select cadpessoa.id_pessoa from fp.cadpessoa where cpf=?";
			ps = con.prepareStatement(sql);
			ps.setDouble(1, cpf);
			ResultSet rs = ps.executeQuery();

			Integer result = null;

			while (rs.next()) {

				result = rs.getInt(1);
			}

			return result;
		} catch (Exception sqle) {

			throw new ProjetoException(sqle);

		} finally {
			try {
				con.close();
			} catch (Exception sqlc) {
				sqlc.printStackTrace();
				System.exit(1);

			}
		}
	}

	public int calcularDigito(String numero) {
		char[] n = numero.toCharArray();
		int soma = 0;
		int index = 0;

		for (int i = n.length - 1; i >= 0; i--) {
			// Acha o mult (seq: 2,3,4,5,6,7,8,9, 2,3,4,5,6,7,8,9, 2...)
			int mult = index + 2;
			while (mult >= 10) {
				mult -= 8;
			}

			int value = Integer.valueOf(String.valueOf(n[i]));
			soma += value * mult;
			index++;
		}
		int intResto = (soma * 10) % 11;
		int intDigito = intResto;

		// Se o Digito Verificador for 0, 1 ou 10
		// entao ele assume o valor 1
		// if (intDigito == 10 || intDigito == 0) {
		// intDigito = 1;
		// }
		if (intDigito == 10) {
			intDigito = 0;
		}

		return intDigito;
	}

}
