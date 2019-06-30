package br.gov.al.maceio.sishosp.questionario.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.control.EscolaridadeController;
import br.gov.al.maceio.sishosp.hosp.control.ParentescoController;
import br.gov.al.maceio.sishosp.hosp.dao.ParentescoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.Parentesco;
import br.gov.al.maceio.sishosp.questionario.dao.PestalozziDAO;
import br.gov.al.maceio.sishosp.questionario.model.ComposicaoFamiliar;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;
import br.gov.al.maceio.sishosp.questionario.enums.ModeloSexo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "PestalozziController")
@ViewScoped
public class PestalozziController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Pestalozzi pestalozzi;
    private String cabecalho;
    private PestalozziDAO pDao = new PestalozziDAO();
    private ComposicaoFamiliar composicaoFamiliarAdd;
    private ComposicaoFamiliar composicaoFamiliarDel;
    private List<EscolaridadeBean> listaEscolaridade;
    private List<Parentesco> listaParentesco;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "questionariopestalozzi?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";


    public PestalozziController() {
        this.pestalozzi = new Pestalozzi();
        composicaoFamiliarAdd = new ComposicaoFamiliar();
        composicaoFamiliarDel = new ComposicaoFamiliar();
    }

    public void iniciarDadosQuestionario() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            pestalozzi.getPaciente().setId_paciente(id);
            this.iniciarListaEscolaridade();
            this.iniciarListaParentesco();
            this.iniciarQuestionario();
        }

    }

    public void iniciarQuestionario() throws ProjetoException{
        pestalozzi = pDao.retornarQuestionario(pestalozzi.getPaciente().getId_paciente());
    }

    public void adicionarComposicaoFamiliar() throws ProjetoException {
        ParentescoDAO parentescoDAO = new ParentescoDAO();
        this.composicaoFamiliarAdd.setParentesco(parentescoDAO.buscaParentesCocodigo(this.composicaoFamiliarAdd.getParentesco().getCodParentesco()));
        this.pestalozzi.getListaComposicaoFamiliar().add(this.composicaoFamiliarAdd);
        composicaoFamiliarAdd = new ComposicaoFamiliar();
        JSFUtil.fecharDialog("dlgAddComposicaoFamiliar");
    }

    public void deletarComposicaoFamiliar(){
        this.pestalozzi.getListaComposicaoFamiliar().remove(this.composicaoFamiliarDel);
        composicaoFamiliarDel= new ComposicaoFamiliar();
    }

    public void gravarQuestionario() throws ProjetoException {
        pDao.gravarQuestionario(pestalozzi);
    }

    public void iniciarListaEscolaridade() throws ProjetoException{
        EscolaridadeController escolaridadeController = new EscolaridadeController();
        listaEscolaridade = escolaridadeController.listarEscolaridades();
    }

    public void iniciarListaParentesco() throws ProjetoException{
        ParentescoController parentescoController = new ParentescoController();
        listaParentesco = parentescoController.listarParentescos();
    }

    public String retonarSimNao(String sigla){
        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(sigla)){
            if(sigla.equals("S")){
                return "Sim";
            }else{
                return "Não";
            }
        }
        return "";
    }

    public String retonarMasculinoFeminino(String sigla){
        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(sigla)) {
            if (sigla.equals(ModeloSexo.MASCULINO.getSigla())) {
                return "Masculino";
            } else if (sigla.equals(ModeloSexo.FEMININO.getSigla())){
                return "Feminino";
            }
        }
        return "";
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_CADASTRO, ENDERECO_ID, this.pestalozzi.getPaciente().getId_paciente());
    }

    public Pestalozzi getPestalozzi() {
        return pestalozzi;
    }

    public void setPestalozzi(Pestalozzi pestalozzi) {
        this.pestalozzi = pestalozzi;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public ComposicaoFamiliar getComposicaoFamiliarAdd() {
        return composicaoFamiliarAdd;
    }

    public void setComposicaoFamiliarAdd(ComposicaoFamiliar composicaoFamiliarAdd) {
        this.composicaoFamiliarAdd = composicaoFamiliarAdd;
    }

    public ComposicaoFamiliar getComposicaoFamiliarDel() {
        return composicaoFamiliarDel;
    }

    public void setComposicaoFamiliarDel(ComposicaoFamiliar composicaoFamiliarDel) {
        this.composicaoFamiliarDel = composicaoFamiliarDel;
    }

    public List<EscolaridadeBean> getListaEscolaridade() {
        return listaEscolaridade;
    }

    public void setListaEscolaridade(List<EscolaridadeBean> listaEscolaridade) {
        this.listaEscolaridade = listaEscolaridade;
    }

    public List<Parentesco> getListaParentesco() {
        return listaParentesco;
    }

    public void setListaParentesco(List<Parentesco> listaParentesco) {
        this.listaParentesco = listaParentesco;
    }
}
