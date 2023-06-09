package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.hosp.model.EquipeBean;
import br.gov.al.maceio.sishosp.hosp.model.GrupoBean;
import br.gov.al.maceio.sishosp.hosp.model.ProgramaBean;
import br.gov.al.maceio.sishosp.hosp.model.dto.BuscaSessaoDTO;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.util.Date;

import static br.gov.al.maceio.sishosp.comum.shared.DadosSessao.BUSCA_SESSAO;

public class SessionUtil {

    public static HttpSession getSession() {
        ExternalContext extCon = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extCon.getSession(true);
        return session;
    }

    public static FuncionarioBean recuperarDadosSessao() {
        FuncionarioBean user_session = (FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("obj_funcionario");

        return user_session;
    }

    public static void adicionarNaSessao(Object objeto, String nomeObjetoSessao){
        FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().put(nomeObjetoSessao, objeto);
    }

    public static Object resgatarDaSessao(String nomeObjetoSessao){
        Object objeto = (Object) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get(nomeObjetoSessao);

        return objeto;
    }
    
    public static void adicionarBuscaPtsNaSessao(Date periodoInicial, Date periodoFinal, String tela){
        BuscaSessaoDTO buscaSessaoDTO = new BuscaSessaoDTO(periodoInicial, periodoFinal, tela);
        SessionUtil.adicionarNaSessao(buscaSessaoDTO, BUSCA_SESSAO);
    }

    public static void adicionarBuscaPtsNaSessao(ProgramaBean programa, GrupoBean grupo, EquipeBean equipe, Date periodoInicial, Date periodoFinal, String tela){
        BuscaSessaoDTO buscaSessaoDTO = new BuscaSessaoDTO(programa, grupo, equipe, periodoInicial, periodoFinal, tela);
        SessionUtil.adicionarNaSessao(buscaSessaoDTO, BUSCA_SESSAO);
    }
    
    public static void adicionarBuscaPacienteInstituicaoNaSessao(ProgramaBean programa, GrupoBean grupo, EquipeBean equipeBean,
    		String tipoBusca, String campoBusca, String tela){
        BuscaSessaoDTO buscaSessaoDTO = new BuscaSessaoDTO(programa, grupo, equipeBean, tipoBusca, campoBusca, tela);
        SessionUtil.adicionarNaSessao(buscaSessaoDTO, BUSCA_SESSAO);
    }
    
    public static void adicionarBuscaPtsNaSessao(ProgramaBean programa, GrupoBean grupo, EquipeBean equipe, Date periodoInicial, Date periodoFinal, String tela,
    		String campoBusca, String tipoBusca, String buscaEvolucao, boolean listarEvolucoesPendentes){
    	
        BuscaSessaoDTO buscaSessaoDTO = new BuscaSessaoDTO(programa, grupo, equipe, periodoInicial, periodoFinal, tela, 
        		campoBusca, tipoBusca, buscaEvolucao, listarEvolucoesPendentes);

        SessionUtil.adicionarNaSessao(buscaSessaoDTO, BUSCA_SESSAO);
    }
    
    public static void removerDaSessao(String nomeObjetoSessao){
    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(nomeObjetoSessao);
    }
}