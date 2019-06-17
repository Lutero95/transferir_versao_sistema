package br.gov.al.maceio.sishosp.questionario.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.questionario.dao.PestalozziDAO;
import br.gov.al.maceio.sishosp.questionario.model.ComposicaoFamiliar;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name = "PestalozziController")
@ViewScoped
public class PestalozziController implements Serializable {

    private static final long serialVersionUID = 1L;
    private Pestalozzi pestalozzi;
    private int tipo;
    private String cabecalho;
    private PestalozziDAO pDao = new PestalozziDAO();
    private ComposicaoFamiliar composicaoFamiliarAdd = new ComposicaoFamiliar();
    private ComposicaoFamiliar composicaoFamiliarDel = new ComposicaoFamiliar();

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "questionariopestalozzi?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Cadastrar Questionário";
    private static final String CABECALHO_ALTERACAO = "Alterar Questionário";

    public PestalozziController() {
        this.pestalozzi = new Pestalozzi();
        tipo = 1;
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

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.pestalozzi.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public Pestalozzi getPestalozzi() {
        return pestalozzi;
    }

    public void setPestalozzi(Pestalozzi pestalozzi) {
        this.pestalozzi = pestalozzi;
    }

    public String getCabecalho() {
        if (this.tipo == 1) {
            cabecalho = CABECALHO_INCLUSAO;
        } else if (this.tipo == 2) {
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
