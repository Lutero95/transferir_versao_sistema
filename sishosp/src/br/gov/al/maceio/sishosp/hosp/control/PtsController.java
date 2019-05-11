package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.hosp.dao.*;
import br.gov.al.maceio.sishosp.hosp.model.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "PtsController")
@ViewScoped
public class PtsController implements Serializable {

    private static final long serialVersionUID = 1L;
    private PtsDAO pDao = new PtsDAO();
    private PtsBean pts;

    public PtsController() {
       pts = new PtsBean();
    }

    public void carregarPts() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();
        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            try {
                this.pts = pDao.carregarPacientesInstituicaoPts(id);
            } catch (ProjetoException e) {
                e.printStackTrace();
            }
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro!", "Erro");
        }

    }

    public PtsBean getPts() {
        return pts;
    }

    public void setPts(PtsBean pts) {
        this.pts = pts;
    }
}
