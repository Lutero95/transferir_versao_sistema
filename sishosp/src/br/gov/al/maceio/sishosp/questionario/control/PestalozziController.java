package br.gov.al.maceio.sishosp.questionario.control;

import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.questionario.dao.PestalozziDAO;
import br.gov.al.maceio.sishosp.questionario.model.Pestalozzi;

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

}
