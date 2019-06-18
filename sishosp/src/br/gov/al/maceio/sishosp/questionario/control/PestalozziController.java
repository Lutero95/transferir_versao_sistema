package br.gov.al.maceio.sishosp.questionario.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.hosp.control.EscolaridadeController;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.questionario.dao.PestalozziDAO;
import br.gov.al.maceio.sishosp.questionario.model.ComposicaoFamiliar;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "PestalozziController")
@ViewScoped
public class PestalozziController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Pestalozzi pestalozzi;
    private String cabecalho;
    private PestalozziDAO pDao = new PestalozziDAO();
    private ComposicaoFamiliar composicaoFamiliarAdd = new ComposicaoFamiliar();
    private ComposicaoFamiliar composicaoFamiliarDel = new ComposicaoFamiliar();
    private List<EscolaridadeBean> listaEscolaridade;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "questionariopestalozzi?faces-redirect=true";
    private static final String ENDERECO_ID = "&amp;id=";


    public PestalozziController() {
        this.pestalozzi = new Pestalozzi();
    }

    public void adicionarComposicaoFamiliar(){
        this.pestalozzi.getListaComposicaoFamiliar().add(this.composicaoFamiliarAdd);
        composicaoFamiliarAdd = new ComposicaoFamiliar();
    }

    public void deletarComposicaoFamiliar(){
        this.pestalozzi.getListaComposicaoFamiliar().remove(this.composicaoFamiliarDel);
        composicaoFamiliarDel= new ComposicaoFamiliar();
    }

    public void gravarQuestionario() throws ProjetoException {
        PestalozziDAO pestalozziDAO = new PestalozziDAO();
        pestalozziDAO.gravarQuestionario(pestalozzi, 1, 1);
    }

    public void iniciarListaEscolaridade() throws ProjetoException{
        EscolaridadeController escolaridadeController = new EscolaridadeController();
        listaEscolaridade = escolaridadeController.listarEscolaridades();
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
}
