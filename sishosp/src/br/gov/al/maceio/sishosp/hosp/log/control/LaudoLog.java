package br.gov.al.maceio.sishosp.hosp.log.control;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.context.FacesContext;

import br.gov.al.maceio.sishosp.acl.model.FuncionarioBean;
import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.comum.util.VerificadorUtil;
import br.gov.al.maceio.sishosp.hosp.dao.CidDAO;
import br.gov.al.maceio.sishosp.hosp.dao.LaudoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProcedimentoDAO;
import br.gov.al.maceio.sishosp.hosp.log.enums.Rotina;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;
import br.gov.al.maceio.sishosp.hosp.log.util.StringUtils;
import br.gov.al.maceio.sishosp.hosp.model.CidBean;
import br.gov.al.maceio.sishosp.hosp.model.LaudoBean;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProcedimentoBean;

public class LaudoLog {
    
    private static final String NOME = " Nome: ";
	private static LaudoBean laudoAntigo;
    private static LaudoBean laudoAtualizar;
    private static FuncionarioBean user_session  = 
    		(FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");
    
    private static List<String> valoresLaudoAntigo;
    private static List<String> valoresLaudoNovo;
    private static List<String> camposMetodos;
    private static List<String> camposAlterados;
    
    private static final String NUMERO_UM = "1";
    private static final String NUMERO_DOIS = "2";
    private static final String NUMERO_TRES = "3";
    private static final String NUMERO_QUATRO = "4";
    private static final String NUMERO_CINCO = "5";
    
    public static LogBean compararLaudos(LaudoBean novoLaudo) throws ProjetoException {
    	String descricao = "";
        laudoAtualizar = novoLaudo;
        laudoAntigo = consultarDadosAntigosLaudo();
		compararLaudosIniciarDados();
		int i = 0;
		while (i < valoresLaudoAntigo.size()) {
			if (compararLaudosComparacao(i)) {
				descricao += compararLaudosPreencherGravarLog(camposMetodos.get(i), valoresLaudoAntigo.get(i),
						valoresLaudoNovo.get(i));
			}
			i++;
		}
        
        return new LogBean(user_session.getId(), descricao, Rotina.LAUDO.getSigla());
    }
    
    private static void compararLaudosIniciarDados() throws ProjetoException {
        valoresLaudoAntigo = new ArrayList<>();
        valoresLaudoNovo = new ArrayList<>();
        camposMetodos = new ArrayList<>();
        camposAlterados = new ArrayList<>();
        
        valoresLaudoAntigo = invocarAtributos(laudoAntigo);
        valoresLaudoNovo = invocarAtributos(laudoAtualizar);
        
        camposMetodos = listarCampos(laudoAntigo);
    }
    
    private static boolean compararLaudosComparacao(int i) {
        return (VerificadorUtil.verificarSeObjetoNulo(valoresLaudoAntigo.get(i)) && !VerificadorUtil.verificarSeObjetoNulo(valoresLaudoNovo.get(i)))
                || (!VerificadorUtil.verificarSeObjetoNulo(valoresLaudoAntigo.get(i)) && VerificadorUtil.verificarSeObjetoNulo(valoresLaudoNovo.get(i)))
                || (!(Objects.equals(retornaValorRetirarZeroEsquerda(valoresLaudoAntigo.get(i)), retornaValorRetirarZeroEsquerda(valoresLaudoNovo.get(i)))));
    }
    
    private static String compararLaudosPreencherGravarLog(String campo, String valorAntigo, String valorNovo) {
        LogBean log = new LogBean();
        log.adicionarDescricao(campo, valorAntigo, valorNovo);
        camposAlterados.add(campo);
        return log.getDescricao();
    }
    
    private static String retornaValorRetirarZeroEsquerda(String valor) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? StringUtils.retirarZeroEsq(valor.trim()) : null;
    }
    
    private static List<String> listarCampos(LaudoBean laudo) {
        List<String> nomesCampos = new ArrayList<>();
        @SuppressWarnings("rawtypes")
		Class cls = laudo.getClass();
        
        Method[] metodosPublicos = cls.getDeclaredMethods();
        for (Method metodo : metodosPublicos) {
            if (StringUtils.retornarPrimeiroCaracter(metodo.getName()) == 'G') {
                if (metodo.getReturnType().getName().equals(ArrayList.class.getName())) {
                    nomesCampos.add("ARRAY");
                } else {
                    String[] campo = metodo.getName().split("get");
                    nomesCampos.add(campo[1]);
                }
            }

        }
        return nomesCampos;
    }
    
    private static LaudoBean consultarDadosAntigosLaudo() throws ProjetoException {
        LaudoBean laudo = new LaudoDAO().buscarLaudosPorId(laudoAtualizar.getId());
        return laudo;
    }
    
    private static List<String> invocarAtributos(LaudoBean laudo) throws ProjetoException {
        List<String> valoresMetodos = new ArrayList<>();
        @SuppressWarnings("rawtypes")
		Class cls = laudo.getClass();
        
        Method[] metodosPublicos = cls.getDeclaredMethods();
        for (Method metodo : metodosPublicos) {
            if (condicaoApenasGet(metodo)) {
                retornandoValores(laudo, valoresMetodos, metodo);
            }

        }
        return valoresMetodos;
    }
    
    private static boolean condicaoApenasGet(Method metodo) {
        return StringUtils.retornarPrimeiroCaracter(metodo.getName()) == 'G';
    }
    
    private static void retornandoValores(LaudoBean laudo, List<String> valoresMetodos, Method metodo) throws ProjetoException {
        try {
        	
            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(metodo.invoke(laudo))) {
                valoresMetodos.add(null);
            } else if (metodo.getReturnType().getName().equals(Date.class.getName())) {
                valoresMetodos.add(new SimpleDateFormat("yyyy-MM-dd").format(metodo.invoke(laudo)));
            }
            
            else if (metodo.getReturnType().getName().equals(PacienteBean.class.getName())) {
            	valoresMetodos.add(laudo.getPaciente().getId_paciente().toString());
            }
            
            else if (metodo.getReturnType().getName().equals(FuncionarioBean.class.getName())) {
            	valoresMetodos.add(laudo.getProfissionalLaudo().getId().toString());
            }
            
            else if (metodo.getReturnType().getName().equals(CidBean.class.getName())) {
            	if(metodo.getName().contains(NUMERO_UM))
            		tratamentoValoresCid(laudo.getCid1(), valoresMetodos);
            	else if(metodo.getName().contains(NUMERO_DOIS))
            		tratamentoValoresCid(laudo.getCid2(), valoresMetodos);
            	else
            		tratamentoValoresCid(laudo.getCid3(), valoresMetodos);
            } 
            
            else if (metodo.getReturnType().getName().equals(ProcedimentoBean.class.getName())) {
            	if(metodo.getName().contains(NUMERO_UM))
            		tratamentoValoresProcedimento(laudo.getProcedimentoSecundario1(), valoresMetodos);
            	else if(metodo.getName().contains(NUMERO_DOIS))
            		tratamentoValoresProcedimento(laudo.getProcedimentoSecundario2(), valoresMetodos);
            	else if(metodo.getName().contains(NUMERO_TRES))
            		tratamentoValoresProcedimento(laudo.getProcedimentoSecundario3(), valoresMetodos);
            	else if(metodo.getName().contains(NUMERO_QUATRO))
            		tratamentoValoresProcedimento(laudo.getProcedimentoSecundario4(), valoresMetodos);
            	else if(metodo.getName().contains(NUMERO_CINCO))
            		tratamentoValoresProcedimento(laudo.getProcedimentoSecundario5(), valoresMetodos);
            	else
            		tratamentoValoresProcedimento(laudo.getProcedimentoPrimario(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(Boolean.class.getName())) {
                valoresMetodos.add(retornarValoresBoolean(metodo.invoke(laudo).toString()));
            } else {
                if (VerificadorUtil.verificarSeObjetoNuloOuVazio(metodo.invoke(laudo).toString())) {
                    valoresMetodos.add(null);
                } else {
                    valoresMetodos.add(StringUtils.stripAccents(metodo.invoke(laudo).toString()));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }
    
    private static void tratamentoValoresCid(CidBean cid, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(cid) || VerificadorUtil.verificarSeObjetoNuloOuZero(cid.getIdCid()))
    		valoresMetodos.add(null);
    	else {
    		cid = new CidDAO().buscaCidPorId(cid.getIdCid());
    		valoresMetodos.add(cid.getIdCid()+NOME+cid.getDescCidAbrev());
    	}
    }
    
    private static void tratamentoValoresProcedimento(ProcedimentoBean procedimento, List<String> valoresMetodos) 
    		throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(procedimento) || VerificadorUtil.verificarSeObjetoNuloOuZero(procedimento.getIdProc()))
    		valoresMetodos.add(null);
    	else {
    		procedimento = new ProcedimentoDAO().listarProcedimentoPorId(procedimento.getIdProc());
    		valoresMetodos.add(procedimento.getIdProc()+NOME+procedimento.getNomeProc());
    	}
    }
    
    private static String retornarValoresBoolean(String valor) {
        return valor.toUpperCase().trim().equals("FALSE") ? null : valor;
    }
}
