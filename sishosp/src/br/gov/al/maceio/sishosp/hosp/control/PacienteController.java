package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.CEPUtil;
import br.gov.al.maceio.sishosp.comum.util.DocumentosUtil;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.model.*;
import org.primefaces.event.SelectEvent;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;

@ManagedBean(name = "PacienteController")
@ViewScoped
public class PacienteController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tipoBuscar;
    private String descricaoParaBuscar;
    private String cabecalho;
    private Boolean cidadeDoCep;
    private PacienteBean paciente;
    private EnderecoBean endereco;
    private EscolaBean escola;
    private EscolaridadeBean escolaridade;
    private EspecialidadeBean especialidade;
    private EncaminhamentoBean encaminhamento;
    private EncaminhadoBean encaminhado;
    private Integer tipo;
    PacienteDAO pDao = new PacienteDAO();
    EnderecoDAO eDao = new EnderecoDAO();
    EscolaDAO esDao = new EscolaDAO();
    private Telefone telefone;
    private Boolean bairroExiste;

    // AUTO COMPLETE
    private EscolaBean escolaSuggestion;
    private EscolaridadeBean escolaridadeSuggestion;
    private EncaminhadoBean encaminhadoSuggestion;
    private FormaTransporteBean transporteSuggestion;
    private ProfissaoBean profissaoSuggestion;

    // LISTAS
    private List<PacienteBean> listaPacientes;
    private List<RacaBean> listaRaca;
    private List<PacienteBean> listaPacientesParaAgenda;
    private List<EscolaBean> listaEscolas;
    private List<EscolaridadeBean> listaEscolaridade;
    private List<ProfissaoBean> listaProfissao;
    private List<EncaminhadoBean> listaEncaminhado;
    private List<FormaTransporteBean> listaTransporte;
    private List<PacienteBean> listaPacientesAgenda;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroPaciente?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inclusão de Paciente";
    private static final String CABECALHO_ALTERACAO = "Alteração de Paciente";

    public PacienteController() {
        paciente = new PacienteBean();
        endereco = new EnderecoBean();
        escola = new EscolaBean();
        escolaridade = new EscolaridadeBean();
        especialidade = new EspecialidadeBean();
        encaminhamento = new EncaminhamentoBean();
        encaminhado = new EncaminhadoBean();
        cidadeDoCep = false;
        escolaSuggestion = new EscolaBean();
        transporteSuggestion = null;
        telefone = new Telefone();

        // LISTAS
        listaPacientes = new ArrayList<>();
        listaPacientesParaAgenda = new ArrayList<>();
        listaEscolas = new ArrayList<>();
        listaEscolaridade = new ArrayList<>();
        listaProfissao = new ArrayList<>();
        listaEncaminhado = new ArrayList<>();
        listaTransporte = new ArrayList<>();
        listaPacientesAgenda = new ArrayList<PacienteBean>();
        listaRaca = new ArrayList<>();
        listaRaca = null;
        bairroExiste = null;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.paciente.getId_paciente(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void getEditPaciente() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            this.paciente = pDao.listarPacientePorID(id);
            escolaridadeSuggestion = paciente.getEscolaridade();
            escolaSuggestion = paciente.getEscola();
            profissaoSuggestion = paciente.getProfissao();
            encaminhadoSuggestion = paciente.getEncaminhado();
            encaminhadoSuggestion = paciente.getEncaminhado();
            transporteSuggestion = paciente.getFormatransporte();
            if (paciente.getEndereco().getCep() != null) {
                cidadeDoCep = true;
            }
        } else {
            tipo = Integer.parseInt(params.get("tipo"));

        }

    }

    public void buscarPacientes() throws ProjetoException {
        listaPacientes = pDao.listaPacientes();
    }

    public List<PacienteBean> listarPacientes() throws ProjetoException {
        listaPacientes = pDao.listaPacientes();

        return listaPacientes;
    }

    public void encontraCEP() throws ProjetoException {
        bairroExiste = false;
        paciente.setEndereco(CEPUtil.encontraCEP(paciente.getEndereco().getCep()));
        EnderecoDAO enderecoDAO = new EnderecoDAO();
        paciente.getEndereco().setCodbairro(enderecoDAO.verificarSeBairroExiste(paciente.getEndereco().getBairro()));
        if(paciente.getEndereco().getCodbairro() != null) {
            if (paciente.getEndereco().getCodbairro() > 0) {
                bairroExiste = true;
            }
            else{
                bairroExiste = false;
            }
        }
        else{
            bairroExiste = false;
        }
        enderecoDAO.listaBairrosPorMunicipio(paciente.getEndereco().getCodmunicipio());
        if (paciente.getEndereco().getCepValido()) {
            cidadeDoCep = true;
        } else {
            cidadeDoCep = false;
            JSFUtil.adicionarMensagemAdvertencia("CEP inválido!", "Advertência");
        }
    }

    public void gravarPaciente() throws ProjetoException {
        if (escolaSuggestion != null)
            paciente.getEscola().setCodEscola(
                    escolaSuggestion.getCodEscola());
        else
            paciente.getEscola().setCodEscola(null);
        if (escolaridadeSuggestion != null)
            paciente.getEscolaridade().setCodescolaridade(
                    escolaridadeSuggestion.getCodescolaridade());
        else
            paciente.getEscolaridade().setCodescolaridade(null);
        if (profissaoSuggestion != null)
            paciente.getProfissao().setCodprofissao(
                    profissaoSuggestion.getCodprofissao());
        else
            paciente.getProfissao().setCodprofissao(null);

        if (encaminhadoSuggestion != null)
            paciente.getEncaminhado().setCodencaminhado(
                    encaminhadoSuggestion.getCodencaminhado());
        else
            paciente.getEncaminhado().setCodencaminhado(null);
        if (transporteSuggestion != null)
            paciente.getFormatransporte().setCodformatransporte(
                    transporteSuggestion.getCodformatransporte());
        else
            paciente.getFormatransporte().setCodformatransporte(null);

        boolean cadastrou = false;

        cadastrou = pDao.cadastrar(paciente, bairroExiste);


        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Paciente cadastrado com sucesso!", "Sucesso");
            JSFUtil.selecionarTabEspecifica("tbv", "1");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro!", "Erro");
        }


    }

    public void buscarPaciente() throws ProjetoException, SQLException {
        if (tipoBuscar.equals("VAZIO") || descricaoParaBuscar.isEmpty()) {
            JSFUtil.adicionarMensagemAdvertencia("Escolha uma opção válida e insira uma descrição!",
                    "Escolha uma opção válida e insira uma descrição!");
        } else {
            this.listaPacientesParaAgenda = new ArrayList<PacienteBean>();
            this.listaPacientesParaAgenda = pDao.buscarPacienteAgenda(
                    tipoBuscar, descricaoParaBuscar);

            if (this.listaPacientesParaAgenda.isEmpty()) {
                JSFUtil.adicionarMensagemAdvertencia("Não existe paciente com essa descrição!",
                        "Paciente não encontrado");
            }
        }

    }

    public void alterarPaciente() throws ProjetoException {
    	if ((paciente.getEndereco().getCodbairro() == null) && (paciente.getEndereco().getBairro() == null)) {
    	JSFUtil.adicionarMensagemAdvertencia("Informe o Bairro!", "Advertência");
    	}
    	else
    	{
        if (escolaSuggestion != null) {
            paciente.getEscola().setCodEscola(escolaSuggestion.getCodEscola());
        }
        if (escolaridadeSuggestion != null) {
            paciente.getEscolaridade().setCodescolaridade(
                    escolaridadeSuggestion.getCodescolaridade());
        }
        if (profissaoSuggestion != null) {
            paciente.getProfissao().setCodprofissao(
                    profissaoSuggestion.getCodprofissao());
        }
        if (encaminhadoSuggestion != null) {
            paciente.getEncaminhado().setCodencaminhado(
                    encaminhadoSuggestion.getCodencaminhado());
        }
        if (transporteSuggestion != null) {
            paciente.getFormatransporte().setCodformatransporte(
                    transporteSuggestion.getCodformatransporte());
        }

        boolean alterou = pDao.alterar(paciente, bairroExiste);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Paciente alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração!", "Erro");
        }
    	}

    }

    public void excluirPaciente() throws ProjetoException {
        boolean excluiu = pDao.excluir(paciente);

        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Paciente excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dialogExclusao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão!", "Erro");
            JSFUtil.fecharDialog("dialogExclusao");
        }
        listaPacientes = pDao.listaPacientes();
    }

    public void buscaProfissaoCod(Integer codprofissao) throws Exception {
        ProfissaoBean profissao = new ProfissaoBean();

        Integer in = (Integer) Integer.valueOf(codprofissao);

        ProfissaoDAO icdao = new ProfissaoDAO();

        profissao = icdao.buscaprofissaocodigo(Integer.valueOf(codprofissao));
        if (profissao.getCodprofissao() != null) {
            icdao = new ProfissaoDAO();

        } else {
            JSFUtil.adicionarMensagemAdvertencia("Código da Profissão incorreto!",
                    "Advertência");
        }

    }

    public List<ProfissaoBean> completeText6(String query)
            throws ProjetoException {
        List<ProfissaoBean> result = new ArrayList<ProfissaoBean>();
        ProfissaoDAO icdao = new ProfissaoDAO();
        result = icdao.buscaprofissao(query);
        return result;
    }

    public void onItemSelect6(SelectEvent event) throws Exception {
        ProfissaoBean prodsel = new ProfissaoBean();
        prodsel = (ProfissaoBean) event.getObject();

        ProfissaoDAO dao = new ProfissaoDAO();
        buscaProfissaoCod(prodsel.getCodprofissao());
        profissaoSuggestion = new ProfissaoBean();
        profissaoSuggestion = null;
    }

    public void buscaTransporteCod(Integer codformatransporte) throws Exception {
        FormaTransporteBean transporte = new FormaTransporteBean();

        Integer in = (Integer) Integer.valueOf(codformatransporte);

        FormaTransporteDAO icdao = new FormaTransporteDAO();

        transporte = icdao.buscatransportecodigo(Integer
                .valueOf(codformatransporte));
        if (transporte.getCodformatransporte() != null) {
            icdao = new FormaTransporteDAO();

        } else {
            JSFUtil.adicionarMensagemAdvertencia("Código da Encaminhamento incorreto!",
                    "Advertência");
        }

    }

    public List<FormaTransporteBean> completeText5(String query)
            throws ProjetoException {
        List<FormaTransporteBean> result = new ArrayList<FormaTransporteBean>();
        FormaTransporteDAO icdao = new FormaTransporteDAO();
        result = icdao.buscatransporte(query);
        return result;
    }

    public void onItemSelect5(SelectEvent event) throws Exception {
        FormaTransporteBean prodsel = new FormaTransporteBean();
        prodsel = (FormaTransporteBean) event.getObject();

        FormaTransporteDAO dao = new FormaTransporteDAO();
        buscaTransporteCod(prodsel.getCodformatransporte());
        transporteSuggestion = new FormaTransporteBean();
        transporteSuggestion = null;
    }

    public void buscaEncaminhadoCod(Integer codencaminhado) throws Exception {
        EncaminhadoBean encaminhado = new EncaminhadoBean();

        Integer in = (Integer) Integer.valueOf(codencaminhado);

        EncaminhadoDAO icdao = new EncaminhadoDAO();

        encaminhado = icdao.buscaencaminhadocodigo(Integer
                .valueOf(codencaminhado));
        if (encaminhado.getCodencaminhado() != null) {

            icdao = new EncaminhadoDAO();

        } else {
            JSFUtil.adicionarMensagemAdvertencia("Código da Encaminhamento incorreto!",
                    "Advertência");
        }

    }

    public List<EncaminhadoBean> completeText4(String query)
            throws ProjetoException {
        List<EncaminhadoBean> result = new ArrayList<EncaminhadoBean>();
        EncaminhadoDAO icdao = new EncaminhadoDAO();
        result = icdao.buscaencaminhado(query);
        return result;
    }

    public void onItemSelect4(SelectEvent event) throws Exception {
        EncaminhadoBean prodsel = new EncaminhadoBean();
        prodsel = (EncaminhadoBean) event.getObject();

        EncaminhadoDAO dao = new EncaminhadoDAO();
        buscaEncaminhadoCod(prodsel.getCodencaminhado());
        encaminhadoSuggestion = new EncaminhadoBean();
        encaminhadoSuggestion = null;
    }

    public void buscaEscolaridadeCod(Integer codescolaridade) throws Exception {
        EscolaridadeBean escolaridade = new EscolaridadeBean();

        Integer in = (Integer) Integer.valueOf(codescolaridade);

        EscolaridadeDAO icdao = new EscolaridadeDAO();

        escolaridade = icdao.buscaescolaridadecodigo(Integer
                .valueOf(codescolaridade));
        if (escolaridade.getCodescolaridade() != null) {

            icdao = new EscolaridadeDAO();

        } else {
            JSFUtil.adicionarMensagemAdvertencia("Código da Escolaridade incorreto!",
                    "Advertência");
        }

    }

    public List<EscolaridadeBean> completeText2(String query)
            throws ProjetoException {
        List<EscolaridadeBean> result = new ArrayList<EscolaridadeBean>();
        EscolaridadeDAO icdao = new EscolaridadeDAO();
        result = icdao.buscaescolaridade(query);
        return result;
    }

    public List<PacienteBean> listaPacienteAutoComplete(String query)
            throws ProjetoException {
        List<PacienteBean> result = pDao.buscaPacienteAutoCompleteOk(query);
        return result;
    }

    public void onItemSelect2(SelectEvent event) throws Exception {
        EscolaridadeBean prodsel = new EscolaridadeBean();
        prodsel = (EscolaridadeBean) event.getObject();

        EscolaridadeDAO dao = new EscolaridadeDAO();
        buscaEscolaridadeCod(prodsel.getCodescolaridade());
        escolaridadeSuggestion = new EscolaridadeBean();
        escolaridadeSuggestion = null;
    }

    public void buscaEscolaCod(Integer codescola) throws Exception {
        EscolaBean escola = new EscolaBean();

        Integer in = (Integer) Integer.valueOf(codescola);

        escola = esDao.buscaescolacodigo(Integer.valueOf(codescola));
        if (escola.getCodEscola() != null) {

            esDao = new EscolaDAO();

        } else {
            JSFUtil.adicionarMensagemAdvertencia("Código da Escola incorreto!",
                    "Advertência");
        }

    }

    public List<EscolaBean> completeText(String query) throws ProjetoException {
        List<EscolaBean> result = new ArrayList<EscolaBean>();
        result = esDao.buscaescola(query);
        return result;
    }

    public void onItemSelect(SelectEvent event) throws Exception {
        EscolaBean prodsel = new EscolaBean();
        prodsel = (EscolaBean) event.getObject();

        buscaEscolaCod(prodsel.getCodEscola());

        escolaSuggestion = new EscolaBean();
        escolaSuggestion = null;

    }

    public void limparDados() {
        transporteSuggestion = new FormaTransporteBean();
        encaminhadoSuggestion = new EncaminhadoBean();
        profissaoSuggestion = new ProfissaoBean();
        escolaridadeSuggestion = new EscolaridadeBean();
        escolaSuggestion = new EscolaBean();
        paciente = new PacienteBean();
        endereco = new EnderecoBean();
        escola = new EscolaBean();
        escolaridade = new EscolaridadeBean();
        especialidade = new EspecialidadeBean();
        encaminhamento = new EncaminhamentoBean();
        encaminhado = new EncaminhadoBean();

        tipoBuscar = "";
        descricaoParaBuscar = "";
        listaPacientesParaAgenda = new ArrayList<>();

    }

    public void listarTodosPaciente() throws ProjetoException {
        listaPacientesAgenda = pDao.listaPaciente();
    }

    public void validaCns(String s) {
        if (!DocumentosUtil.validaCns(s)) {
            JSFUtil.adicionarMensagemAdvertencia("Esse número de CNS não é valido", "Advertência");
            paciente.setCns("");
        }
    }


    public void validaPIS(String pis) {
        if (!DocumentosUtil.validaPIS(pis)) {
            JSFUtil.adicionarMensagemAdvertencia("Esse número do PIS não é valido", "Advertência");
            paciente.setPis("");
        }
    }

    public void validaCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        if (!DocumentosUtil.validaCPF(cpf)) {
            JSFUtil.adicionarMensagemAdvertencia("Esse número de CPF não é valido", "Advertência");
            paciente.setCpfresp("");
        }
    }

    public List<EnderecoBean> listaBairroAutoComplete(String query)
            throws ProjetoException {
        List<EnderecoBean> result = eDao.buscaBairroAutoComplete(query,
                endereco.getCodmunicipio());
        return result;
    }

    public void addListaTelefone() {
        paciente.getListaTelefones().add(telefone);
        limparTelefone();
    }

    public void removeListaTelefone() {
        this.paciente.getListaTelefones().remove(telefone);
    }

    public void limparTelefone(){
    	
        telefone = new Telefone();
    }

    public PacienteBean getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteBean paciente) {
        this.paciente = paciente;
    }

    public EnderecoBean getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoBean endereco) {
        this.endereco = endereco;
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

    public EncaminhamentoBean getEncaminhamento() {
        return encaminhamento;
    }

    public void setEncaminhamento(EncaminhamentoBean encaminhamento) {
        this.encaminhamento = encaminhamento;
    }

    public List<EscolaBean> getListaEscolas() throws ProjetoException {
        if (listaEscolas == null) {

            listaEscolas = esDao.listaEscolas();

        }
        return listaEscolas;
    }

    public void setListaEscolas(List<EscolaBean> listaEscolas) {
        this.listaEscolas = listaEscolas;
    }

    public List<EscolaridadeBean> getListaEscolaridade()
            throws ProjetoException {
        if (listaEscolaridade == null) {

            EscolaridadeDAO fdao = new EscolaridadeDAO();
            listaEscolaridade = fdao.listaEscolaridade();

        }
        return listaEscolaridade;
    }

    public void setListaEscolaridade(List<EscolaridadeBean> listaEscolaridade) {
        this.listaEscolaridade = listaEscolaridade;
    }

    public List<ProfissaoBean> getListaProfissao() throws ProjetoException {
        if (listaProfissao == null) {

            ProfissaoDAO fdao = new ProfissaoDAO();
            listaProfissao = fdao.listaProfissoes();

        }
        return listaProfissao;
    }

    public void setListaProfissao(List<ProfissaoBean> listaProfissao) {
        this.listaProfissao = listaProfissao;
    }

    public EncaminhadoBean getEncaminhado() {
        return encaminhado;
    }

    public void setEncaminhado(EncaminhadoBean encaminhado) {
        this.encaminhado = encaminhado;
    }

    public void setListaPacientes(List<PacienteBean> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<EncaminhadoBean> getListaEncaminhado() throws ProjetoException {
        if (listaEncaminhado == null) {
            EncaminhadoDAO fdao = new EncaminhadoDAO();
            listaEncaminhado = fdao.listaEncaminhados();

        }
        return listaEncaminhado;
    }

    public void setListaEncaminhado(List<EncaminhadoBean> listaEncaminhado) {
        this.listaEncaminhado = listaEncaminhado;
    }

    public List<FormaTransporteBean> getListaTransporte()
            throws ProjetoException {
        if (listaTransporte == null) {

            FormaTransporteDAO fdao = new FormaTransporteDAO();
            listaTransporte = fdao.listaTransportes();

        }
        return listaTransporte;
    }

    public void setListaTransporte(List<FormaTransporteBean> listaTransporte) {
        this.listaTransporte = listaTransporte;
    }

    public EscolaBean getEscolaSuggestion() {
        return escolaSuggestion;
    }

    public void setEscolaSuggestion(EscolaBean escolaSuggestion) {
        this.escolaSuggestion = escolaSuggestion;
    }

    public EscolaridadeBean getEscolaridadeSuggestion() {
        return escolaridadeSuggestion;
    }

    public void setEscolaridadeSuggestion(
            EscolaridadeBean escolaridadeSuggestion) {
        this.escolaridadeSuggestion = escolaridadeSuggestion;
    }

    public EncaminhadoBean getEncaminhadoSuggestion() {
        return encaminhadoSuggestion;
    }

    public void setEncaminhadoSuggestion(EncaminhadoBean encaminhadoSuggestion) {
        this.encaminhadoSuggestion = encaminhadoSuggestion;
    }

    public FormaTransporteBean getTransporteSuggestion() {
        return transporteSuggestion;
    }

    public void setTransporteSuggestion(FormaTransporteBean transporteSuggestion) {
        this.transporteSuggestion = transporteSuggestion;
    }

    public ProfissaoBean getProfissaoSuggestion() {
        return profissaoSuggestion;
    }

    public void setProfissaoSuggestion(ProfissaoBean profissaoSuggestion) {
        this.profissaoSuggestion = profissaoSuggestion;
    }

    public String getDescricaoParaBuscar() {
        return descricaoParaBuscar;
    }

    public void setDescricaoParaBuscar(String descricaoParaBuscar) {
        this.descricaoParaBuscar = descricaoParaBuscar;
    }

    public String getTipoBuscar() {
        return tipoBuscar;
    }

    public void setTipoBuscar(String tipoBuscar) {
        this.tipoBuscar = tipoBuscar;
    }

    public String getCabecalho() {
        if (this.tipo.equals(TipoCabecalho.INCLUSAO.getSigla())) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo.equals(TipoCabecalho.ALTERACAO.getSigla())) {
            cabecalho = CABECALHO_ALTERACAO;
        }
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public List<PacienteBean> getListaPacientes() {
        return listaPacientes;
    }

    public List<PacienteBean> getListaPacientesParaAgenda() {

        return listaPacientesParaAgenda;
    }

    public void setListaPacientesParaAgenda(
            List<PacienteBean> listaPacientesParaAgenda) {
        this.listaPacientesParaAgenda = listaPacientesParaAgenda;
    }

    public Boolean getCidadeDoCep() {
        return cidadeDoCep;
    }

    public void setCidadeDoCep(Boolean cidadeDoCep) {
        this.cidadeDoCep = cidadeDoCep;
    }

    public List<RacaBean> getListaRaca() throws ProjetoException {
        if (listaRaca == null) {
            RacaDAO rDao = new RacaDAO();
            listaRaca = rDao.listaCor();
        }
        return listaRaca;
    }

    public void setListaRaca(List<RacaBean> listaRaca) {
        this.listaRaca = listaRaca;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public Boolean getBairroExiste() {
        return bairroExiste;
    }

    public void setBairroExiste(Boolean bairroExiste) {
        this.bairroExiste = bairroExiste;
    }

	public List<PacienteBean> getListaPacientesAgenda() {
		return listaPacientesAgenda;
	}

	public void setListaPacientesAgenda(List<PacienteBean> listaPacientesAgenda) {
		this.listaPacientesAgenda = listaPacientesAgenda;
	}
}