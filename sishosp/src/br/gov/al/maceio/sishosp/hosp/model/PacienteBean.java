package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteBean implements Serializable {

    private Integer id_paciente;
    private String nome;
    private String estadoCivil;
    private Date dtanascimento;
    private String nomeMae;
    private String nomePai;
    private String sangue;
    private String conjuge;
    private String trabalha;
    private String corRaca;
    private String localtrabalha;
    private Integer codProfissao;
    private Integer codRaca;
    private String descRaca;
    private String deficiencia;
    private String tipoDeficiencia;
    private Boolean necessitaNomeSocial;
    private String nomeSocial;
    private String sexo;
    private String matricula;

    // ENCAMINHADO ATRIBUTOS
    private Integer codEnchaminhado;
    private Integer codFormaTransporte;

    // RENPONSAVEL ATRIBUTOS
    private Integer codparentesco;
    private String responsavel;
    private String nomeresp;
    private String rgresp;
    private String cpfresp;
    private Date dataNascimentoresp;

    // DOCUMENTOS
    private String cpf;
    private String oe;
    private String rg;
    private String reservista;
    private Integer ctps;
    private String pis;
    private String cns;
    private Integer protant;
    private String serie;
    private Date dataExpedicao1;

    //DADOS INTERNET
    private String email;
    private String facebook;
    private String instagram;

    // DEFICIÊNCIAS
    private Boolean deficienciaFisica;
    private Boolean deficienciaMental;
    private Boolean deficienciaAuditiva;
    private Boolean deficienciaVisual;
    private Boolean deficienciaMultipla;

    // REGISTROS DO CARTORIO
    private String cartorio;
    private String numeroCartorio;
    private String livro;
    private String folha;
    private Date dataregistro;
    private Date dataExpedicao2;

    // HERDADOS
    private EnderecoBean endereco;
    private EscolaBean escola;
    private EscolaridadeBean escolaridade;
    private EncaminhamentoBean encaminhamento;
    private ProfissaoBean profissao;
    private EncaminhadoBean encaminhado;
    private FormaTransporteBean formatransporte;
    private Telefone telefone;
    private Genero genero;
    private Religiao religiao;
    private TipoLogradouroBean tipoLogradouro;

    //LISTAS
    private List<Telefone> listaTelefones;

    public PacienteBean() {
        listaTelefones = new ArrayList<>();
        id_paciente = null;
        endereco = new EnderecoBean();
        escola = new EscolaBean();
        escolaridade = new EscolaridadeBean();
        encaminhamento = new EncaminhamentoBean();
        formatransporte = new FormaTransporteBean();
        encaminhado = new EncaminhadoBean();
        profissao = new ProfissaoBean();
        deficiencia = "N";
        telefone = new Telefone();
        genero = new Genero();
        religiao = new Religiao();
        tipoLogradouro = new TipoLogradouroBean();
    }

    public Integer getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(Integer id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Religiao getReligiao() {
        return religiao;
    }

    public void setReligiao(Religiao religiao) {
        this.religiao = religiao;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getConjuge() {
        return conjuge;
    }

    public void setConjuge(String conjuge) {
        this.conjuge = conjuge;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getReservista() {
        return reservista;
    }

    public void setReservista(String reservista) {
        this.reservista = reservista;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getCartorio() {
        return cartorio;
    }

    public void setCartorio(String cartorio) {
        this.cartorio = cartorio;
    }

    public String getNumeroCartorio() {
        return numeroCartorio;
    }

    public void setNumeroCartorio(String numeroCartorio) {
        this.numeroCartorio = numeroCartorio;
    }

    public String getLivro() {
        return livro;
    }

    public void setLivro(String livro) {
        this.livro = livro;
    }

    public Date getDataExpedicao1() {
        return dataExpedicao1;
    }

    public void setDataExpedicao1(Date dataExpedicao1) {
        this.dataExpedicao1 = dataExpedicao1;
    }

    public Date getDataExpedicao2() {
        return dataExpedicao2;
    }

    public void setDataExpedicao2(Date dataExpedicao2) {
        this.dataExpedicao2 = dataExpedicao2;
    }

    public EnderecoBean getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoBean endereco) {
        this.endereco = endereco;
    }

    public String getTrabalha() {
        return trabalha;
    }

    public void setTrabalha(String trabalha) {
        this.trabalha = trabalha;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Integer getCodparentesco() {
        return codparentesco;
    }

    public void setCodparentesco(Integer codparentesco) {
        this.codparentesco = codparentesco;
    }

    public String getNomeresp() {
        return nomeresp;
    }

    public void setNomeresp(String nomeresp) {
        this.nomeresp = nomeresp;
    }

    public String getRgresp() {
        return rgresp;
    }

    public void setRgresp(String rgresp) {
        this.rgresp = rgresp;
    }

    public String getCpfresp() {
        return cpfresp;
    }

    public void setCpfresp(String cpfresp) {
        this.cpfresp = cpfresp;
    }

    public Date getDataNascimentoresp() {
        return dataNascimentoresp;
    }

    public void setDataNascimentoresp(Date dataNascimentoresp) {
        this.dataNascimentoresp = dataNascimentoresp;
    }

    public String getLocaltrabalha() {
        return localtrabalha;
    }

    public void setLocaltrabalha(String localtrabalha) {
        this.localtrabalha = localtrabalha;
    }

    public Integer getCodProfissao() {
        return codProfissao;
    }

    public void setCodProfissao(Integer codProfissao) {
        this.codProfissao = codProfissao;
    }

    public Integer getCodEnchaminhado() {
        return codEnchaminhado;
    }

    public void setCodEnchaminhado(Integer codEnchaminhado) {
        this.codEnchaminhado = codEnchaminhado;
    }

    public Integer getCodFormaTransporte() {
        return codFormaTransporte;
    }

    public void setCodFormaTransporte(Integer codFormaTransporte) {
        this.codFormaTransporte = codFormaTransporte;
    }

    public String getTipoDeficiencia() {
        return tipoDeficiencia;
    }

    public void setTipoDeficiencia(String tipoDeficiencia) {
        this.tipoDeficiencia = tipoDeficiencia;
    }

    public EscolaBean getEscola() {
        return escola;
    }

    public void setEscola(EscolaBean escola) {
        this.escola = escola;
    }

    public EscolaridadeBean getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(EscolaridadeBean escolaridade) {
        this.escolaridade = escolaridade;
    }

    public ProfissaoBean getProfissao() {
        return profissao;
    }

    public void setProfissao(ProfissaoBean profissao) {
        this.profissao = profissao;
    }

    public String getCorRaca() {
        return corRaca;
    }

    public void setCorRaca(String corRaca) {
        this.corRaca = corRaca;
    }

    public String getSangue() {
        return sangue;
    }

    public void setSangue(String sangue) {
        this.sangue = sangue;
    }

    public EncaminhamentoBean getEncaminhamento() {
        return encaminhamento;
    }

    public void setEncaminhamento(EncaminhamentoBean encaminhamento) {
        this.encaminhamento = encaminhamento;
    }

    public EncaminhadoBean getEncaminhado() {
        return encaminhado;
    }

    public void setEncaminhado(EncaminhadoBean encaminhado) {
        this.encaminhado = encaminhado;
    }

    public String getOe() {
        return oe;
    }

    public void setOe(String oe) {
        this.oe = oe;
    }

    public Integer getProtant() {
        return protant;
    }

    public void setProtant(Integer protant) {
        this.protant = protant;
    }

    

    public Date getDataregistro() {
        return dataregistro;
    }

    public void setDataregistro(Date dataregistro) {
        this.dataregistro = dataregistro;
    }

 

    public Date getDtanascimento() {
        return dtanascimento;
    }

    public void setDtanascimento(Date dtanascimento) {
        this.dtanascimento = dtanascimento;
    }

    public Integer getCtps() {
        return ctps;
    }

    public void setCtps(Integer ctps) {
        this.ctps = ctps;
    }

    public FormaTransporteBean getFormatransporte() {
        return formatransporte;
    }

    public void setFormatransporte(FormaTransporteBean formatransporte) {
        this.formatransporte = formatransporte;
    }

    public Integer getCodRaca() {
        return codRaca;
    }

    public void setCodRaca(Integer codRaca) {
        this.codRaca = codRaca;
    }

    public String getDeficiencia() {
        return deficiencia;
    }

    public void setDeficiencia(String deficiencia) {
        this.deficiencia = deficiencia;
    }

    public String getDescRaca() {
        return descRaca;
    }

    public void setDescRaca(String descRaca) {
        this.descRaca = descRaca;
    }

    public Boolean getDeficienciaFisica() {
        return deficienciaFisica;
    }

    public void setDeficienciaFisica(Boolean deficienciaFisica) {
        this.deficienciaFisica = deficienciaFisica;
    }

    public Boolean getDeficienciaMental() {
        return deficienciaMental;
    }

    public void setDeficienciaMental(Boolean deficienciaMental) {
        this.deficienciaMental = deficienciaMental;
    }

    public Boolean getDeficienciaAuditiva() {
        return deficienciaAuditiva;
    }

    public void setDeficienciaAuditiva(Boolean deficienciaAuditiva) {
        this.deficienciaAuditiva = deficienciaAuditiva;
    }

    public Boolean getDeficienciaVisual() {
        return deficienciaVisual;
    }

    public void setDeficienciaVisual(Boolean deficienciaVisual) {
        this.deficienciaVisual = deficienciaVisual;
    }

    public Boolean getDeficienciaMultipla() {
        return deficienciaMultipla;
    }

    public void setDeficienciaMultipla(Boolean deficienciaMultipla) {
        this.deficienciaMultipla = deficienciaMultipla;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public Boolean getNecessitaNomeSocial() {
        return necessitaNomeSocial;
    }

    public void setNecessitaNomeSocial(Boolean necessitaNomeSocial) {
        this.necessitaNomeSocial = necessitaNomeSocial;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public List<Telefone> getListaTelefones() {
        return listaTelefones;
    }

    public void setListaTelefones(List<Telefone> listaTelefones) {
        this.listaTelefones = listaTelefones;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

	public String getFolha() {
		return folha;
	}

	public void setFolha(String folha) {
		this.folha = folha;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

    public TipoLogradouroBean getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(TipoLogradouroBean tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }
}
