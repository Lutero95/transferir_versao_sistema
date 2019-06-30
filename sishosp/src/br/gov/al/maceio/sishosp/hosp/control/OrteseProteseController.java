package br.gov.al.maceio.sishosp.hosp.control;

import br.gov.al.maceio.sishosp.comum.enums.TipoCabecalho;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.JSFUtil;
import br.gov.al.maceio.sishosp.comum.util.RedirecionarUtil;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.OrteseProteseDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.enums.StatusMovimentacaoOrteseProtese;
import br.gov.al.maceio.sishosp.hosp.model.EquipamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.FornecedorBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.OrteseProtese;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "OrteseProteseController")
@ViewScoped
public class OrteseProteseController implements Serializable {

    private static final long serialVersionUID = 1L;
    private OrteseProtese orteseProtese;
    private Integer tipo;
    private OrteseProteseDAO oDao = new OrteseProteseDAO();
    private Boolean temOrteseIhProteseCadastrado;
    private String cabecalho;
    private List<OrteseProtese> listOrteseProtese;
    private Boolean renderizarBotaoAlterar;
    private Boolean existeMedicao;

    //CONSTANTES
    private static final String ENDERECO_CADASTRO = "orteseProtese?faces-redirect=true";
    private static final String ENDERECO_ENCAMINHAMENTO = "encaminhamentoopm?faces-redirect=true";
    private static final String ENDERECO_TIPO = "&amp;tipo=";
    private static final String ENDERECO_ID = "&amp;id=";
    private static final String CABECALHO_INCLUSAO = "Inserção de Órtese/Prótese";
    private static final String CABECALHO_ALTERACAO = "Alteração de Órtese/Prótese";


    public OrteseProteseController() {
        this.orteseProtese = new OrteseProtese();
        temOrteseIhProteseCadastrado = false;
        listOrteseProtese = new ArrayList<>();
        renderizarBotaoAlterar = false;
        existeMedicao = false;
    }

    public String redirectAlterar() {
        return RedirecionarUtil.redirectEdit(ENDERECO_CADASTRO, ENDERECO_ID, this.orteseProtese.getId(), ENDERECO_TIPO, tipo);
    }

    public String redirectEncaminhar() {
        return RedirecionarUtil.redirectEditSemTipo(ENDERECO_ENCAMINHAMENTO, ENDERECO_ID, this.orteseProtese.getId());
    }

    public String redirectInserir() {
        return RedirecionarUtil.redirectInsert(ENDERECO_CADASTRO, ENDERECO_TIPO, tipo);
    }

    public void carregarOrteseIhProtese() throws ProjetoException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();

        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            tipo = Integer.parseInt(params.get("tipo"));
            orteseProtese = oDao.carregarOrteseIhProtesePorId(id);
            LaudoDAO lDao = new LaudoDAO();
            orteseProtese.setLaudo(lDao.listarLaudosVigentesPorId(orteseProtese.getLaudo().getId()));
            temOrteseIhProteseCadastrado = true;
        } else {
            orteseProtese = oDao.carregarGrupoProgramaOrteseIhProtese();
            tipo = Integer.parseInt(params.get("tipo"));

            if (!VerificadorUtil.verificarSeObjetoNuloOuVazio(orteseProtese.getGrupo().getDescGrupo())
                    || !VerificadorUtil.verificarSeObjetoNuloOuVazio(orteseProtese.getPrograma().getDescPrograma())) {
                temOrteseIhProteseCadastrado = true;
            } else {
                JSFUtil.abrirDialog("dlgAviso");
            }
        }
    }

    public void carregarEncaminhamentoOrteseIhProtese() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext()
                .getRequestParameterMap();

        if (params.get("id") != null) {
            Integer id = Integer.parseInt(params.get("id"));
            orteseProtese = oDao.carregarEncaminhamentoOrteseIhProtese(id);
            if (VerificadorUtil.verificarSeObjetoNuloOuZero(orteseProtese.getId())) {
                orteseProtese.setId(id);
            } else {
                renderizarBotaoAlterar = true;
            }
        }
    }

    public void limparInsercao() {
        orteseProtese.setEquipamento(new EquipamentoBean());
        orteseProtese.setFornecedor(new FornecedorBean());
        orteseProtese.setLaudo(new LaudoBean());
        orteseProtese.setNotaFiscal(null);
    }

    public void gravarOrteseIhProtese() {
        boolean cadastrou = oDao.gravarInsercaoOrteseIhProtese(this.orteseProtese);

        if (cadastrou == true) {
            JSFUtil.adicionarMensagemSucesso("Alterado com sucesso!", "Sucesso");
            limparInsercao();
            JSFUtil.atualizarComponente("formInsercao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
        }
    }

    public void alterarOrteseIhProtese() {
        boolean alterou = oDao.alterarOrteseIhProtese(orteseProtese);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void gravarEncaminhamentoOrteseIhProtese() {
        boolean cadastrou = oDao.gravarEncaminhamentoOrteseIhProtese(this.orteseProtese);

        if (cadastrou == true) {
            JSFUtil.adicionarMensagemSucesso("Cadastrado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cadastro", "Erro");
        }
    }

    public void alterarEncaminhamentoOrteseIhProtese() {
        boolean alterou = oDao.alterarEncaminhamentoOrteseIhProtese(orteseProtese);

        if (alterou == true) {
            JSFUtil.adicionarMensagemSucesso("Alterado com sucesso!", "Sucesso");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a alteração", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void cancelarEncaminhamentoOrteseIhProtese() {
        boolean cancelou = oDao.cancelarEncaminhamentoOrteseIhProtese(orteseProtese);

        if (cancelou == true) {
            JSFUtil.adicionarMensagemSucesso("Cancelado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgCancelar");
            JSFUtil.abrirDialog("dlgCancelado");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cancelamento", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void gravarMedicaoOrteseIhProtese() {
        boolean gravou = oDao.gravarMedicaoOrteseIhProtese(orteseProtese);

        if (gravou == true) {
            JSFUtil.adicionarMensagemSucesso("Medição efetuada com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgMedicao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a gravação", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void cancelarMedicaoOrteseIhProtese() {

        boolean cancelou = oDao.cancelarMedicaoOrteseIhProtese(orteseProtese.getId());

        if (cancelou == true) {
            JSFUtil.adicionarMensagemSucesso("Medição cancelada com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgCancelar");
            JSFUtil.fecharDialog("dlgMedicao");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cancelamento", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void iniciarMedicao() throws ProjetoException {
        existeMedicao = oDao.verificarSeExisteMedicao(orteseProtese.getId());
        if (existeMedicao) {
            orteseProtese = oDao.carregarMedicaoPorIdOrteseProtese(orteseProtese.getId());
        }
        carregarDadosPacientePorOrteseIhProtese();
        JSFUtil.abrirDialog("dlgMedicao");
    }

    public void verificarEntrega() {
        String situacao = oDao.verificarSituacao(orteseProtese.getId());

        if (situacao.equals(StatusMovimentacaoOrteseProtese.EQUIPAMENTO_ENTREGUE.getSigla())) {
            JSFUtil.abrirDialog("dlgCancelarEntrega");
        } else {
            JSFUtil.abrirDialog("dlgEntrega");
        }
    }

    public void gravarEntregaOrteseIhProtese() {
        boolean gravou = oDao.gravarEntregaOrteseIhProtese(orteseProtese.getId());

        if (gravou == true) {
            JSFUtil.adicionarMensagemSucesso("Entrega efetuada com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgEntrega");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante a entrega!", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void cancelarEntregaOrteseIhProtese() {
        boolean cancelou = oDao.cancelarEntregaOrteseIhProtese(orteseProtese.getId());

        if (cancelou == true) {
            JSFUtil.adicionarMensagemSucesso("Cancelamento efetuado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgCancelarEntrega");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cancelamento!", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void verificarRecebimento() {
        String situacao = oDao.verificarSituacao(orteseProtese.getId());

        if (situacao.equals(StatusMovimentacaoOrteseProtese.EQUIPAMENTO_RECEBIDO.getSigla())) {
            JSFUtil.abrirDialog("dlgCancelarRecebimento");
        } else {
            JSFUtil.abrirDialog("dlgRecebimento");
        }
    }

    public void gravarRecebimentoOrteseIhProtese() {
        boolean gravou = oDao.gravarRecebimentoOrteseIhProtese(orteseProtese.getId());

        if (gravou == true) {
            JSFUtil.adicionarMensagemSucesso("Recebimento efetuado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgRecebimento");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o recebimento!", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void cancelarRecebimentoOrteseIhProtese() {
        boolean cancelou = oDao.cancelarRecebimentoOrteseIhProtese(orteseProtese.getId());

        if (cancelou == true) {
            JSFUtil.adicionarMensagemSucesso("Cancelamento efetuado com sucesso!", "Sucesso");
            JSFUtil.fecharDialog("dlgCancelarRecebimento");
        } else {
            JSFUtil.adicionarMensagemErro("Ocorreu um erro durante o cancelamento!", "Erro");
        }

        listarOrteseIhProtese();
    }

    public void carregarDadosPacientePorOrteseIhProtese() throws ProjetoException {
        orteseProtese.getLaudo().setPaciente(new PacienteDAO().buscarPacientePorIdOrteseProtese(orteseProtese.getId()));
    }

    public void listarOrteseIhProtese() {
        listOrteseProtese = oDao.listarOrteseIhProtese();
        atualizarTabelaPrincipalIhBotoesOrteseIhProtese();
    }

    public void atualizarTabelaPrincipalIhBotoesOrteseIhProtese() {
        JSFUtil.atualizarComponente("dtNovosAg");
        JSFUtil.atualizarComponente("formGerenOrteseEProtese:btnAlterar, formGerenOrteseEProtese:btnEncaminhar, " +
                "formGerenOrteseEProtese:btnMedicao, formGerenOrteseEProtese:btnEntrega, formGerenOrteseEProtese:btnRecebimento");
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

    public List<OrteseProtese> getListOrteseProtese() {
        return listOrteseProtese;
    }

    public void setListOrteseProtese(List<OrteseProtese> listOrteseProtese) {
        this.listOrteseProtese = listOrteseProtese;
    }

    public Boolean getRenderizarBotaoAlterar() {
        return renderizarBotaoAlterar;
    }

    public void setRenderizarBotaoAlterar(Boolean renderizarBotaoAlterar) {
        this.renderizarBotaoAlterar = renderizarBotaoAlterar;
    }

    public Boolean getExisteMedicao() {
        return existeMedicao;
    }

    public void setExisteMedicao(Boolean existeMedicao) {
        this.existeMedicao = existeMedicao;
    }

}
