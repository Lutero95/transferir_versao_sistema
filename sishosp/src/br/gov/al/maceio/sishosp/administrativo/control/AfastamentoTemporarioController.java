package br.gov.al.maceio.sishosp.administrativo.control;

import br.gov.al.maceio.sishosp.administrativo.dao.AfastamentoTemporarioDAO;
import br.gov.al.maceio.sishosp.administrativo.model.AfastamentoTemporario;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "AfastamentoTemporarioController")
@ViewScoped
public class AfastamentoTemporarioController implements Serializable {

    private static final long serialVersionUID = 1L;
    private AfastamentoTemporario afastamentoTemporario;
    private AfastamentoTemporarioDAO aDao = new AfastamentoTemporarioDAO();
    private List<AfastamentoTemporario> listaAfastamentosTemporarios;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "cadastroafastamentotemporario?faces-redirect=true";


    public AfastamentoTemporarioController() {
        this.afastamentoTemporario = new AfastamentoTemporario();
        listaAfastamentosTemporarios = new ArrayList<>();
    }

    public String redirectInsert() {
        return RedirecionarUtil.redirectInsertSemTipo(ENDERECO_CADASTRO);
    }

    public void limparDados() {
        afastamentoTemporario = new AfastamentoTemporario();

    }

    public void gravarAfastamentoTemporario() {
        boolean cadastrou = aDao.gravarAfastamentoTemporario(this.afastamentoTemporario);

        if (cadastrou == true) {
            limparDados();
            JSFUtil.adicionarMensagemSucesso("Afastamento temporário cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
        }
    }

    public void excluirAfastamentoTemporario() {
        boolean excluiu = aDao.excluirAfastamentoTemporario(afastamentoTemporario.getId());
        if (excluiu == true) {
            JSFUtil.adicionarMensagemSucesso("Afastamento Temporário excluído com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgExclusao");
            listarAfastamentosTemporarios();
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a exclusão", "Erro");
            JSFUtil.fecharDialog("dlgExclusao");
        }
    }

    public void validarDatasDeAfastamentoTemporario() {
        if (!VerificadorUtil.verificarSeObjetoNulo(afastamentoTemporario.getPeriodoInicio())
                && !VerificadorUtil.verificarSeObjetoNulo(afastamentoTemporario.getPeriodoFinal())) {
            if (afastamentoTemporario.getPeriodoFinal().before(afastamentoTemporario.getPeriodoInicio()) ||
                    afastamentoTemporario.getPeriodoInicio().equals(afastamentoTemporario.getPeriodoFinal())) {
                limparDatas();
                JSFUtil.adicionarMensagemErro("A data fim de afastamento deve ser maior que a data início!", "Erro");
            }
        }
    }

    public void limparDatas(){
        afastamentoTemporario.setPeriodoInicio(null);
        afastamentoTemporario.setPeriodoFinal(null);
    }

    public void listarAfastamentosTemporarios() {
        listaAfastamentosTemporarios = aDao.listarAfastamentoTemporario();
    }

    public AfastamentoTemporario getAfastamentoTemporario() {
        return afastamentoTemporario;
    }

    public void setAfastamentoTemporario(AfastamentoTemporario afastamentoTemporario) {
        this.afastamentoTemporario = afastamentoTemporario;
    }

    public List<AfastamentoTemporario> getListaAfastamentosTemporarios() {
        return listaAfastamentosTemporarios;
    }

    public void setListaAfastamentosTemporarios(List<AfastamentoTemporario> listaAfastamentosTemporarios) {
        this.listaAfastamentosTemporarios = listaAfastamentosTemporarios;
    }
}
