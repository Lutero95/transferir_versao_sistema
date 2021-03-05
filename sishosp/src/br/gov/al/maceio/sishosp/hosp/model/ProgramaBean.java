package br.gov.al.maceio.sishosp.hosp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaGrupoFrequenciaDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoCboEspecificoDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoIdadeEspecificaDTO;
import br.gov.al.maceio.sishosp.hosp.model.dto.ProcedimentoProfissionalEquipeEspecificoDTO;

public class ProgramaBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String descPrograma;
	private Integer idPrograma;

	// LISTAS
	private List<GrupoBean> grupo;
	private List<GrupoBean> grupoNovo;

	// HERDADOS
	private GrupoBean grupoBean;
	private GrupoBean grupoParaAdd;
	private GrupoBean grupoRmv;
	private ProcedimentoBean procedimento;
	private Integer codUnidade;
	private String descricaoUnidade;  
	private List<ProcedimentoCboEspecificoDTO> listaProcedimentoCboEspecificoDTO;
	private List<BuscaGrupoFrequenciaDTO> listaGrupoFrequenciaDTO;
	private List<ProcedimentoIdadeEspecificaDTO> listaProcedimentoIdadeEspecificaDTO;
    private List<EspecialidadeBean> listaEspecialidadesEspecificas;
    private List<ProcedimentoBean> listaProcedimentosPermitidos;
    private List<ProcedimentoProfissionalEquipeEspecificoDTO> listaProcedimentoProfissionalEquipeEspecificaDTO;
    private List<CidBean> listaCidsPermitidos;
    private boolean permitePacienteSemLaudo;
    private Integer diasPacienteSemLaudoAtivo;
    private boolean permiteAlteracaoCidNaEvolucao;
    private Integer idClassificacao;
    private Integer idServico;

	public ProgramaBean() {
		this.grupoBean = new GrupoBean();
		this.grupo = new ArrayList<GrupoBean>();
		this.grupoRmv = new GrupoBean();
		this.grupoParaAdd = new GrupoBean();
		this.grupoNovo = new ArrayList<GrupoBean>();
		this.idPrograma = null;
		this.descPrograma = new String();
		this.procedimento = new ProcedimentoBean();
		this.listaProcedimentoCboEspecificoDTO = new ArrayList<>();
		this.listaGrupoFrequenciaDTO = new ArrayList<>();
		this.listaProcedimentoIdadeEspecificaDTO = new ArrayList<>();
		this.listaEspecialidadesEspecificas = new ArrayList<>();
		this.listaProcedimentosPermitidos = new ArrayList<>();
		this.listaCidsPermitidos = new ArrayList<>();
		this.listaProcedimentoProfissionalEquipeEspecificaDTO = new ArrayList<>();
	}

	public Integer getIdPrograma() {
		return idPrograma;
	}

	public void setIdPrograma(Integer idPrograma) {
		this.idPrograma = idPrograma;
	}
	
	public String getDescPrograma() {
		return descPrograma;
	}

	public void setDescPrograma(String descPrograma) {
		this.descPrograma = descPrograma;
	}

	public List<GrupoBean> getGrupo() {
		return grupo;
	}

	public void setGrupo(List<GrupoBean> grupo) {
		this.grupo = grupo;
	}

	public GrupoBean getGrupoParaAdd() {
		return grupoParaAdd;
	}

	public List<GrupoBean> getGrupoNovo() {
		this.grupoNovo = this.grupo;
		return grupoNovo;
	}

	public void setGrupoNovo(List<GrupoBean> grupoNovo) {
		this.grupoNovo = grupoNovo;
	}

	public void setGrupoParaAdd(GrupoBean grupoParaAdd) {
		this.grupoParaAdd = grupoParaAdd;
	}

	public GrupoBean getGrupoRmv() {
		return grupoRmv;
	}

	public void setGrupoRmv(GrupoBean grupoRmv) {
		this.grupoRmv = grupoRmv;
	}

	public void addGrupoLista() {
		boolean existe = false;
		if (grupo.size() == 0) {
			this.grupo.add(this.grupoParaAdd);
		} else {
			for (int i = 0; i < grupo.size(); i++) {
				if (grupo.get(i).getIdGrupo() == grupoParaAdd.getIdGrupo()) {
					existe = true;
				}
			}
			if (existe == false) {
				this.grupo.add(this.grupoParaAdd);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Esse Grupo já foi adicionado!", "Sucesso");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	public void removeGrupoLista() {
		this.grupo.remove(this.grupoRmv);
	}

	public GrupoBean getGrupoBean() {
		return grupoBean;
	}

	public void setGrupoBean(GrupoBean grupoBean) {
		this.grupoBean = grupoBean;
	}

	public ProcedimentoBean getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(ProcedimentoBean procedimento) {
		this.procedimento = procedimento;
	}

	public Integer getCodUnidade() {
		return codUnidade;
	}

	public void setCodUnidade(Integer codUnidade) {
		this.codUnidade = codUnidade;
	}

	public String getDescricaoUnidade() {
		return descricaoUnidade;
	}

	public void setDescricaoUnidade(String descricaoUnidade) {
		this.descricaoUnidade = descricaoUnidade;
	}

	public List<ProcedimentoCboEspecificoDTO> getListaProcedimentoCboEspecificoDTO() {
		return listaProcedimentoCboEspecificoDTO;
	}

	public void setListaProcedimentoCboEspecificoDTO(List<ProcedimentoCboEspecificoDTO> listaProcedimentoCboEspecificoDTO) {
		this.listaProcedimentoCboEspecificoDTO = listaProcedimentoCboEspecificoDTO;
	}

	public List<BuscaGrupoFrequenciaDTO> getListaGrupoFrequenciaDTO() {
		return listaGrupoFrequenciaDTO;
	}

	public void setListaGrupoFrequenciaDTO(List<BuscaGrupoFrequenciaDTO> listaGrupoFrequenciaDTO) {
		this.listaGrupoFrequenciaDTO = listaGrupoFrequenciaDTO;
	}

	public List<ProcedimentoIdadeEspecificaDTO> getListaProcedimentoIdadeEspecificaDTO() {
		return listaProcedimentoIdadeEspecificaDTO;
	}

	public void setListaProcedimentoIdadeEspecificaDTO(
			List<ProcedimentoIdadeEspecificaDTO> listaProcedimentoIdadeEspecificaDTO) {
		this.listaProcedimentoIdadeEspecificaDTO = listaProcedimentoIdadeEspecificaDTO;
	}

	public List<EspecialidadeBean> getListaEspecialidadesEspecificas() {
		return listaEspecialidadesEspecificas;
	}

	public void setListaEspecialidadesEspecificas(List<EspecialidadeBean> listaEspecialidadesEspecificas) {
		this.listaEspecialidadesEspecificas = listaEspecialidadesEspecificas;
	}

	public List<ProcedimentoBean> getListaProcedimentosPermitidos() {
		return listaProcedimentosPermitidos;
	}

	public void setListaProcedimentosPermitidos(List<ProcedimentoBean> listaProcedimentosPermitidos) {
		this.listaProcedimentosPermitidos = listaProcedimentosPermitidos;
	}

	public List<CidBean> getListaCidsPermitidos() {
		return listaCidsPermitidos;
	}

	public void setListaCidsPermitidos(List<CidBean> listaCidsPermitidos) {
		this.listaCidsPermitidos = listaCidsPermitidos;
	}
	
	public List<ProcedimentoProfissionalEquipeEspecificoDTO> getListaProcedimentoProfissionalEquipeEspecificaDTO() {
		return listaProcedimentoProfissionalEquipeEspecificaDTO;
	}

	public void setListaProcedimentoProfissionalEquipeEspecificaDTO(
			List<ProcedimentoProfissionalEquipeEspecificoDTO> listaProcedimentoProfissionalEquipeEspecificaDTO) {
		this.listaProcedimentoProfissionalEquipeEspecificaDTO = listaProcedimentoProfissionalEquipeEspecificaDTO;
	}

	public boolean isPermitePacienteSemLaudo() {
		return permitePacienteSemLaudo;
	}

	public void setPermitePacienteSemLaudo(boolean permitePacienteSemLaudo) {
		this.permitePacienteSemLaudo = permitePacienteSemLaudo;
	}

	public Integer getDiasPacienteSemLaudoAtivo() {
		return diasPacienteSemLaudoAtivo;
	}

	public void setDiasPacienteSemLaudoAtivo(Integer diasPacienteSemLaudoAtivo) {
		this.diasPacienteSemLaudoAtivo = diasPacienteSemLaudoAtivo;
	}

	public boolean isPermiteAlteracaoCidNaEvolucao() {
		return permiteAlteracaoCidNaEvolucao;
	}

	public void setPermiteAlteracaoCidNaEvolucao(boolean permiteAlteracaoCidNaEvolucao) {
		this.permiteAlteracaoCidNaEvolucao = permiteAlteracaoCidNaEvolucao;
	}

	public Integer getIdClassificacao() {
		return idClassificacao;
	}

	public void setIdClassificacao(Integer idClassificacao) {
		this.idClassificacao = idClassificacao;
	}

	public Integer getIdServico() {
		return idServico;
	}

	public void setIdServico(Integer idServico) {
		this.idServico = idServico;
	}
	
}
