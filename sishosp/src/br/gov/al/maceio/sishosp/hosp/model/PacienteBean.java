package br.gov.al.maceio.sishosp.hosp.model;

import java.util.Date;
import java.util.List;

public class PacienteBean {
    //ATRIBUTOS
	private Long id_paciente;
    private String nome;
    private String estadoCivil;
    private Date dtanascimento;
    private String sexo;
    private String nomeMae;
    private String nomePai;
    private String sangue;
    private String conjugue;
    private String associado;
    private String trabalha;
    private String corRaca;
    
    private String localtrabalha;
    private Integer codProfissao;
    private Integer codRaca;
    
   
    private boolean deficiencia;
    private String tipoDeficiencia;
    
    //ENCAMINHADO ATRIBUTOS
    private Integer codEnchaminhado;
    private Integer codFormaTransporte;
    
    //RENPONSAVEL ATRIBUTOS
    private Integer codparentesco;
    private String responsavel;
    private String nomeresp;
    private String rgresp;
    private Double cpfresp;
    private Date dataNascimentoresp;
    
    //DOCUMENTOS
    private Double cpf;
    private String oe;
    private String rg;
    private String reservista;
    private Integer ctps;
    private String pis;
    private String cns;
    private Double protant;
    private Integer serie;
    private Date dataExpedicao1;
    //REGISTROS DO CARTORIO
    private String cartorio;
  	private String numeroCartorio;
    private String livro;
    private Integer folha;
    private Date dataregistro;
    private Date dataExpedicao2;
    
    //CLASSES HERDADAS
    private EnderecoBean endereco;
    private EscolaBean escola;
    private EscolaridadeBean escolaridade;
    private EspecialidadeBean especialidade;
    private ConvenioBean convenio;
    private EncaminhamentoBean encaminhamento;
    private ProfissaoBean profissao;
    private EncaminhadoBean encaminhado;
    private FormaTransporteBean formatransporte;
    
   

	//LISTAS 
    private List<Integer> listaCorRaca;
    private List<Integer> listaSangue;
    
 public PacienteBean() {
		endereco = new EnderecoBean();
		escola = new EscolaBean();
		escolaridade = new EscolaridadeBean();
		especialidade = new EspecialidadeBean();
        encaminhamento = new EncaminhamentoBean();
        formatransporte = new FormaTransporteBean();
        encaminhado = new EncaminhadoBean();
        profissao = new ProfissaoBean();
        
    }

	public Long getId_paciente() {
	return id_paciente;
    }

    public void setId_paciente(Long id_paciente) {
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

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
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

	public String getConjugue() {
		return conjugue;
	}

	public void setConjugue(String conjugue) {
		this.conjugue = conjugue;
	}

	public Double getCpf() {
		return cpf;
	}

	public void setCpf(Double cpf) {
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

	public List<Integer> getListaCorRaca() {
		
		return listaCorRaca;
	}

	public void setListaCorRaca(List<Integer> listaCorRaca) {
		this.listaCorRaca = listaCorRaca;
	}

	public List<Integer> getListaSangue() {
		return listaSangue;
	}

	public void setListaSangue(List<Integer> listaSangue) {
		this.listaSangue = listaSangue;
	}
	 public EnderecoBean getEndereco() {
			return endereco;
		}

		public void setEndereco(EnderecoBean endereco) {
			this.endereco = endereco;
		}

		public String getAssociado() {
			return associado;
		}

		public void setAssociado(String associado) {
			this.associado = associado;
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

		public Double getCpfresp() {
			return cpfresp;
		}

		public void setCpfresp(Double cpfresp) {
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

		public boolean getDeficiencia() {
			return deficiencia;
		}

		public void setDeficiencia(boolean deficiencia) {
			this.deficiencia = deficiencia;
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

		public EspecialidadeBean getEspecialidade() {
			return especialidade;
		}

		public void setEspecialidade(EspecialidadeBean especialidade) {
			this.especialidade = especialidade;
		}

		public ConvenioBean getConvenio() {
			return convenio;
		}

		public void setConvenio(ConvenioBean convenio) {
			this.convenio = convenio;
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

		public Double getProtant() {
			return protant;
		}

		public void setProtant(Double protant) {
			this.protant = protant;
		}

		public Integer getSerie() {
			return serie;
		}

		public void setSerie(Integer serie) {
			this.serie = serie;
		}

		public Date getDataregistro() {
			return dataregistro;
		}

		public void setDataregistro(Date dataregistro) {
			this.dataregistro = dataregistro;
		}

		public Integer getFolha() {
			return folha;
		}

		public void setFolha(Integer folha) {
			this.folha = folha;
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
		
		
		
		
		
}
