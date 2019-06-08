package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.OrteseProteseDAO;
import br.gov.al.maceio.sishosp.hosp.model.OrteseProtese;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean(name = "OrteseProteseController")
@ViewScoped
public class OrteseProteseController implements Serializable {

    private static final long serialVersionUID = 1L;
    private OrteseProtese orteseProtese;
    private int tipo;
    private OrteseProteseDAO oDao = new OrteseProteseDAO();
    private Boolean temOrteseIhProteseCadastrado;
    private String cabecalho;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "orteseProtese?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inserção de Órtese/Prótese";
    private static final String CABECALHO_ALTERACAO = "Alteração de Órtese/Prótese";

    public OrteseProteseController() {
        this.orteseProtese = new OrteseProtese();
        temOrteseIhProteseCadastrado = false;
    }

    public String redirectEdit() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.orteseProtese.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void carregarOrteseIhProtese(){

        orteseProtese = oDao.carregarOrteseIhProtese();

        if(!VerificadorUtil.verificarSeObjetoNuloOuVazio(orteseProtese.getGrupo().getDescGrupo())
                || !VerificadorUtil.verificarSeObjetoNuloOuVazio(orteseProtese.getPrograma().getDescPrograma())){
            temOrteseIhProteseCadastrado = true;
        }
        else{
            JSFUtil.abrirDialog("dlgAviso");
        }

    }

    public OrteseProtese getOrteseProtese() {
        return orteseProtese;
    }

    public void setOrteseProtese(OrteseProtese orteseProtese) {
        this.orteseProtese = orteseProtese;
    }

    public Boolean getTemOrteseIhProteseCadastrado() {
        return temOrteseIhProteseCadastrado;
    }

    public void setTemOrteseIhProteseCadastrado(Boolean temOrteseIhProteseCadastrado) {
        this.temOrteseIhProteseCadastrado = temOrteseIhProteseCadastrado;
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
