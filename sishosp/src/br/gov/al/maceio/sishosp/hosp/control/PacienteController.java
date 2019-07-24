package br.gov.al.maceio.sishosp.hosp.control;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.util.*;
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

    // LISTAS
    private List<PacienteBean> listaPacientes;
    private List<PacienteBean> listaPacientesParaAgenda;
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
        telefone = new Telefone();

        // LISTAS
        listaPacientes = new ArrayList<>();
        listaPacientesParaAgenda = new ArrayList<>();
        listaPacientesAgenda = new ArrayList<PacienteBean>();
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
        paciente.getEndereco().setCodbairro(enderecoDAO.verificarSeBairroExiste(paciente.getEndereco().getBairro(), paciente.getEndereco().getCodmunicipio()));
        if (paciente.getEndereco().getCodbairro() != null) {
            if (paciente.getEndereco().getCodbairro() > 0) {
                bairroExiste = true;
            } else {
                bairroExiste = false;
            }
        } else {
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

    public void gravarPaciente() {

        boolean cadastrou = false;

        cadastrou = pDao.cadastrarPaciente(paciente, bairroExiste);


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

    public void alterarPaciente() {
        if ((paciente.getEndereco().getCodbairro() == null) && (paciente.getEndereco().getBairro() == null)) {
            JSFUtil.adicionarMensagemAdvertencia("Informe o Bairro!", "Advertência");
        } else {

            if (VerificadorUtil.verificarSeObjetoNulo(bairroExiste)) {
                bairroExiste = true;
            }

            boolean alterou = pDao.alterarPaciente(paciente, bairroExiste);

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
    }
    
    public void verificaGrauParentescoResponsavel() throws Exception {
    	System.out.println("verificaGrauParentescoResponsavel");
        Parentesco parentesco = new Parentesco();
      //  Integer codParentesco= (Integer) event.getObject();
        HttpServletRequest request = (HttpServletRequest) FacesContext
        		.getCurrentInstance().getExternalContext().getRequest();
        		String nome = request.getParameter(":frmPrincipal:tbv:mae");
        ParentescoDAO dao = new ParentescoDAO();
        parentesco = dao.buscaParentesCocodigo(paciente.getCodparentesco());
        if (parentesco.getTipoParentesco().equals("M")) {
        	paciente.setNomeresp(paciente.getNomeMae());
        }
        
        if (parentesco.getTipoParentesco().equals("P")) {
        	paciente.setNomeresp(paciente.getNomePai());
        }        
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

    }

    public void limparDados() {
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

    public void addListaTelefone() throws ProjetoException {
    	ParentescoDAO pDAo = new ParentescoDAO();
    	if (telefone.getParentesco().getCodParentesco()!=null) {
    		Parentesco parente =  new Parentesco();
    		parente = pDAo.buscaParentesCocodigo(telefone.getParentesco().getCodParentesco());
    		telefone.getParentesco().setDescParentesco(parente.getDescParentesco());
    	}
        paciente.getListaTelefones().add(telefone);
        limparTelefone();
    }

    public void removeListaTelefone() {
        this.paciente.getListaTelefones().remove(telefone);
    }

    public void limparTelefone() {

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

    public EncaminhadoBean getEncaminhado() {
        return encaminhado;
    }

    public void setEncaminhado(EncaminhadoBean encaminhado) {
        this.encaminhado = encaminhado;
    }

    public void setListaPacientes(List<PacienteBean> listaPacientes) {
        this.listaPacientes = listaPacientes;
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