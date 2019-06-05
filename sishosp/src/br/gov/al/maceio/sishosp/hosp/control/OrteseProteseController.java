package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
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
    private OrteseProteseDAO oDao = new OrteseProteseDAO();
    private Boolean temOrteseIhProteseCadastrado;

    public OrteseProteseController() {
        this.orteseProtese = new OrteseProtese();
        temOrteseIhProteseCadastrado = false;
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
}
