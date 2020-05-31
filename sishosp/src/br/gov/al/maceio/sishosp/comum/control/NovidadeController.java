package br.gov.al.maceio.sishosp.comum.control;


import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.acl.model.Sistema;
import br.gov.al.maceio.sishosp.comum.dao.NovidadeDAO;
import br.gov.al.maceio.sishosp.comum.model.Novidade;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.gov.al.maceio.sishosp.comum.util.JSFUtil.resgatarObjetoDaSessao;


@ManagedBean
@ViewScoped
public class NovidadeController {
    private List<Novidade> novidades;
    private NovidadeDAO novidadeDAO;
    private Novidade novidade;

    public NovidadeController() {
        this.novidades = new ArrayList<>();
        this.novidadeDAO = new NovidadeDAO();
        this.novidade = new Novidade();
    }

    public void listarNovidades(){
        FuncionarioBean usuarioLogado = (FuncionarioBean) resgatarObjetoDaSessao("obj_usuario");
        this.novidades = novidadeDAO.listarNovidades(obterSistemasDoUsuarioLogado(), usuarioLogado.getId());
    }

    public void marcarVisualizado(){
        FuncionarioBean usuarioLogado = (FuncionarioBean) resgatarObjetoDaSessao("obj_usuario");
        novidadeDAO.marcarVisualizado(obterNovidadesVisualizadas(), usuarioLogado.getId());
    }

    private List<Sistema> obterSistemasDoUsuarioLogado(){
        List<Sistema> sistemas = (List<Sistema>) resgatarObjetoDaSessao("perms_usuario_sis");
        return sistemas;// sistemas.stream().map(Sistema::getId).collect(Collectors.toList());
    }

    public List<Novidade> obterNovidadesVisualizadas(){
        List<Novidade> novidadesVisualizadas = new ArrayList<>();
        for(Novidade novidade : novidades){
            if(novidade.getVisualizado().equals(true)){
                novidadesVisualizadas.add(novidade);
            }
        }
        return novidadesVisualizadas;
    }

    public void visualizarAnexo(Novidade novidade) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        response.reset();
        response.setContentType(novidade.getExtensaoDocumento());
        response.setHeader("Content-disposition", "inline; filename=" + novidade.getDocumento() + novidade.getExtensaoDocumento());
        try {
            response.getOutputStream().write(novidade.getDocumento());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            fc.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean verificarCondicaoVisibleDialog(){
        return this.novidades.size() > 0;
    }

    public List<Novidade> getNovidades() {
        return novidades;
    }

    public void setNovidades(List<Novidade> novidades) {
        this.novidades = novidades;
    }

    public Novidade getNovidade() {
        return novidade;
    }

    public void setNovidade(Novidade novidade) {
        this.novidade = novidade;
    }
}
