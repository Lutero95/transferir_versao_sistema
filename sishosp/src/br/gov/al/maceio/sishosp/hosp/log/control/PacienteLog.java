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
import br.gov.al.maceio.sishosp.hosp.dao.EncaminhadoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaDAO;
import br.gov.al.maceio.sishosp.hosp.dao.EscolaridadeDAO;
import br.gov.al.maceio.sishosp.hosp.dao.FormaTransporteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.GeneroDAO;
import br.gov.al.maceio.sishosp.hosp.dao.OperadoraDAO;
import br.gov.al.maceio.sishosp.hosp.dao.PacienteDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ParentescoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ProfissaoDAO;
import br.gov.al.maceio.sishosp.hosp.dao.ReligiaoDAO;
import br.gov.al.maceio.sishosp.hosp.log.enums.Rotina;
import br.gov.al.maceio.sishosp.hosp.log.model.LogBean;
import br.gov.al.maceio.sishosp.hosp.log.util.StringUtils;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhadoBean;
import br.gov.al.maceio.sishosp.hosp.model.EncaminhamentoBean;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaBean;
import br.gov.al.maceio.sishosp.hosp.model.EscolaridadeBean;
import br.gov.al.maceio.sishosp.hosp.model.FormaTransporteBean;
import br.gov.al.maceio.sishosp.hosp.model.Genero;
import br.gov.al.maceio.sishosp.hosp.model.PacienteBean;
import br.gov.al.maceio.sishosp.hosp.model.ProfissaoBean;
import br.gov.al.maceio.sishosp.hosp.model.Religiao;
import br.gov.al.maceio.sishosp.hosp.model.Telefone;

public class PacienteLog {
    
    private  final String CAMPO_LISTA_DE_TELEFONES = "Lista de Telefones";
	private  final String NOME = " Nome: ";
	private  PacienteBean pacienteAntigo;
    private  PacienteBean pacienteAtualizar;
    private  FuncionarioBean user_session  = 
    		(FuncionarioBean) FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().get("obj_funcionario");
    
    private  List<String> valoresPacienteAntigo;
    private  List<String> valoresPacienteNovo;
    private  List<String> camposMetodos;
    private  List<String> camposAlterados;
    private  List<Telefone> telefonesAdicionados;
    private  List<Telefone> telefonesExcluidos;
    
    public  LogBean compararPacientes(PacienteBean novoPaciente, List<Telefone> listaTelefonesAdicionados,
    		List<Telefone> listaTelefonesExcluidos) throws ProjetoException {
        pacienteAtualizar = novoPaciente;
        pacienteAntigo = consultarDadosAntigosPaciente();
        telefonesAdicionados = listaTelefonesAdicionados;
        telefonesExcluidos = listaTelefonesExcluidos;
        
        String descricao = retornaNomeIDPaciente();
		compararPacientesIniciarDados();
		int i = 0; 
		while (i < valoresPacienteAntigo.size()) {
			if (compararPacientesComparacao(i)) {
				descricao += compararPacientesPreencherGravarLog(camposMetodos.get(i), valoresPacienteAntigo.get(i),
						valoresPacienteNovo.get(i));
			}
			i++;
		}
		
		descricao += geraLogDaListaTelefones();
        
        return new LogBean(user_session.getId(), descricao, Rotina.ALTERACAO_PACIENTE.getSigla());
    }
    
    private  String retornaNomeIDPaciente() {
    	return "Paciente: "+pacienteAntigo.getNome()+" ID: "+pacienteAntigo.getId_paciente()+" ";
    }
    
    private  String geraLogDaListaTelefones() throws ProjetoException {
        camposAlterados.add(CAMPO_LISTA_DE_TELEFONES);
        return montarDescricaoListaTelefones(tratamentoValoresListaTelefones(telefonesExcluidos), 
        		tratamentoValoresListaTelefones(telefonesAdicionados));
	}

	private  void compararPacientesIniciarDados() throws ProjetoException {
        valoresPacienteAntigo = new ArrayList<>();
        valoresPacienteNovo = new ArrayList<>();
        camposMetodos = new ArrayList<>();
        camposAlterados = new ArrayList<>();
        
        valoresPacienteAntigo = invocarAtributos(pacienteAntigo);
        valoresPacienteNovo = invocarAtributos(pacienteAtualizar);
        camposMetodos = listarCampos(pacienteAntigo);
    }
    
    private  boolean compararPacientesComparacao(int i) {
        return (VerificadorUtil.verificarSeObjetoNulo(valoresPacienteAntigo.get(i)) && !VerificadorUtil.verificarSeObjetoNulo(valoresPacienteNovo.get(i)))
                || (!VerificadorUtil.verificarSeObjetoNulo(valoresPacienteAntigo.get(i)) && VerificadorUtil.verificarSeObjetoNulo(valoresPacienteNovo.get(i)))
                || (!(Objects.equals(retornaValorRetirarZeroEsquerda(valoresPacienteAntigo.get(i)), retornaValorRetirarZeroEsquerda(valoresPacienteNovo.get(i)))));
    }
    
    private  String compararPacientesPreencherGravarLog(String campo, String valorAntigo, String valorNovo) {
        LogBean log = new LogBean();
        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(valorAntigo))
        	valorAntigo = " ";
        if(VerificadorUtil.verificarSeObjetoNuloOuVazio(valorNovo))
        	valorNovo = " ";
        
        log.adicionarDescricao(campo, valorAntigo, valorNovo);
        camposAlterados.add(campo);
        return log.getDescricao();
    }
    
    private  String retornaValorRetirarZeroEsquerda(String valor) {
        return !VerificadorUtil.verificarSeObjetoNuloOuVazio(valor) ? StringUtils.retirarZeroEsq(valor.trim()) : null;
    }
    
    private  List<String> listarCampos(PacienteBean paciente) {
        List<String> nomesCampos = new ArrayList<>();
        @SuppressWarnings("rawtypes")
		Class cls = paciente.getClass();
        
        Method[] metodosPublicos = cls.getDeclaredMethods();
        for (Method metodo : metodosPublicos) {
            if (StringUtils.retornarPrimeiroCaracter(metodo.getName()) == 'G') {
                if (metodo.getReturnType().getName().equals(List.class.getName())) {
                    nomesCampos.add("ARRAY");
                } else {
                    String[] campo = metodo.getName().split("get");
                    nomesCampos.add(campo[1]);
                }
            }

        }
        return nomesCampos;
    }
    
    private  PacienteBean consultarDadosAntigosPaciente() throws ProjetoException {
        PacienteBean paciente = new PacienteDAO().listarPacientePorID(pacienteAtualizar.getId_paciente());
        return paciente;
    }
    
    private  List<String> invocarAtributos(PacienteBean paciente) throws ProjetoException {
        List<String> valoresMetodos = new ArrayList<>();
        @SuppressWarnings("rawtypes")
		Class cls = paciente.getClass();
        
        Method[] metodosPublicos = cls.getDeclaredMethods();
        for (Method metodo : metodosPublicos) {
            if (condicaoApenasGet(metodo)) {
                retornandoValores(paciente, valoresMetodos, metodo);
            }

        }
        return valoresMetodos;
    }
    
    private  boolean condicaoApenasGet(Method metodo) {
        return StringUtils.retornarPrimeiroCaracter(metodo.getName()) == 'G';
    }
    
    private  void retornandoValores(PacienteBean paciente, List<String> valoresMetodos, Method metodo) 
    		throws ProjetoException {
        try {
        	
            if (VerificadorUtil.verificarSeObjetoNuloOuVazio(metodo.invoke(paciente))) {
                valoresMetodos.add(null);
            }
            
            else if (metodo.getReturnType().getName().equals(Telefone.class.getName())
            		|| metodo.getReturnType().getName().equals(EncaminhamentoBean.class.getName())) {
            	valoresMetodos.add(null);
            }
            
            else if (metodo.getReturnType().getName().equals(Date.class.getName())) {
                valoresMetodos.add(new SimpleDateFormat("yyyy-MM-dd").format(metodo.invoke(paciente)));
            }
            
            else if (metodo.getReturnType().getName().equals(EscolaBean.class.getName())) {
            	tratamentoValoresEscola(paciente.getEscola(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(EscolaridadeBean.class.getName())) {
            	tratamentoValoresEscolaridade(paciente.getEscolaridade(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(EncaminhadoBean.class.getName())) {
            	tratamentoValoresEncaminhado(paciente.getEncaminhado(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(ProfissaoBean.class.getName())) {
            	tratamentoValoresProfissao(paciente.getProfissao(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(Genero.class.getName())) {
            	tratamentoValoresGenero(paciente.getGenero(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(Religiao.class.getName())) {
            	tratamentoValoresReligiao(paciente.getReligiao(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(FormaTransporteBean.class.getName())) {
            	tratamentoValoresFormaTransporte(paciente.getFormatransporte(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(EnderecoBean.class.getName())) {
            	tratamentoValoresEndereco(paciente.getEndereco(), valoresMetodos);
            }
            
            else if (metodo.getReturnType().getName().equals(List.class.getName())) {
            	valoresMetodos.add(null);
//            	if(metodo.getName().equals("getListaTelefones")) {
//            		tratamentoValoresListaTelefones(paciente.getListaTelefones(), valoresMetodos);
//            	}
            }
            
            else if (metodo.getReturnType().getName().equals(Boolean.class.getName())) {
                valoresMetodos.add(retornarValoresBoolean(metodo.invoke(paciente).toString()));
            } else {
                if (VerificadorUtil.verificarSeObjetoNuloOuVazio(metodo.invoke(paciente).toString())) {
                    valoresMetodos.add(null);
                } else {
                    valoresMetodos.add(StringUtils.stripAccents(metodo.invoke(paciente).toString()));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }
    
    private  void tratamentoValoresEscola(EscolaBean escola, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(escola) || VerificadorUtil.verificarSeObjetoNuloOuZero(escola.getCodEscola()))
    		valoresMetodos.add(null);
    	else {
    		escola = new EscolaDAO().buscaescolacodigo(escola.getCodEscola());
    		valoresMetodos.add(escola.getCodEscola()+NOME+escola.getDescescola());
    	}
    }
    
    private  void tratamentoValoresEscolaridade(EscolaridadeBean escolaridade, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(escolaridade) || VerificadorUtil.verificarSeObjetoNuloOuZero(escolaridade.getCodescolaridade()))
    		valoresMetodos.add(null);
    	else {
    		escolaridade = new EscolaridadeDAO().buscaescolaridadecodigo(escolaridade.getCodescolaridade());
    		valoresMetodos.add(escolaridade.getCodescolaridade()+NOME+escolaridade.getDescescolaridade());
    	}
    }
    
    private  void tratamentoValoresEncaminhado(EncaminhadoBean encaminhado, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(encaminhado) || VerificadorUtil.verificarSeObjetoNuloOuZero(encaminhado.getCodencaminhado()))
    		valoresMetodos.add(null);
    	else {
    		encaminhado = new EncaminhadoDAO().buscaencaminhadocodigo(encaminhado.getCodencaminhado());
    		valoresMetodos.add(encaminhado.getCodencaminhado()+NOME+encaminhado.getDescencaminhado());
    	}
    }
    
    private  void tratamentoValoresProfissao(ProfissaoBean profissao, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(profissao) || VerificadorUtil.verificarSeObjetoNuloOuZero(profissao.getCodprofissao()))
    		valoresMetodos.add(null);
    	else {
    		profissao = new ProfissaoDAO().buscaprofissaocodigo(profissao.getCodprofissao());
    		valoresMetodos.add(profissao.getCodprofissao()+NOME+profissao.getDescprofissao());
    	}
    }
    
    private  void tratamentoValoresGenero(Genero genero, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(genero) || VerificadorUtil.verificarSeObjetoNuloOuZero(genero.getId()))
    		valoresMetodos.add(null);
    	else {
    		genero = new GeneroDAO().buscaGeneroPorId(genero.getId());
    		valoresMetodos.add(genero.getId()+NOME+genero.getDescricao());
    	}
    }
    
    private  void tratamentoValoresReligiao(Religiao religiao, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(religiao) || VerificadorUtil.verificarSeObjetoNuloOuZero(religiao.getId()))
    		valoresMetodos.add(null);
    	else {
    		religiao = new ReligiaoDAO().buscarReligiaoPorId(religiao.getId());
    		valoresMetodos.add(religiao.getId()+NOME+religiao.getDescricao());
    	}
    } 
    
    private  void tratamentoValoresFormaTransporte(FormaTransporteBean formaTransporte, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(formaTransporte) || VerificadorUtil.verificarSeObjetoNuloOuZero(formaTransporte.getCodformatransporte()))
    		valoresMetodos.add(null);
    	else {
    		formaTransporte = new FormaTransporteDAO().buscatransportecodigo(formaTransporte.getCodformatransporte());
    		valoresMetodos.add(formaTransporte.getCodformatransporte()+NOME+formaTransporte.getDescformatransporte());
    	}
    } 	

    private  void tratamentoValoresEndereco(EnderecoBean endereco, List<String> valoresMetodos) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(endereco))
    		valoresMetodos.add(null);
    	else {
    		
    		valoresMetodos.add(endereco.getCep());
    		valoresMetodos.add(endereco.getLogradouro());
    		valoresMetodos.add(endereco.getComplemento());
    		valoresMetodos.add(endereco.getReferencia());
    		valoresMetodos.add(endereco.getNumero());
    		
    		endereco = new EnderecoDAO().listarBairroPorId(endereco.getCodbairro());
    		
    		valoresMetodos.add(endereco.getCodbairro()+NOME+endereco.getBairro());
    		valoresMetodos.add(endereco.getCodmunicipio()+NOME+endereco.getMunicipio());
    		valoresMetodos.add(" UF: "+endereco.getUf());
    	}
    } 
    
    private  String tratamentoValoresListaTelefones(List<Telefone> listaTelefones) throws ProjetoException {
    	
    	if(VerificadorUtil.verificarSeObjetoNulo(listaTelefones) || listaTelefones.isEmpty())
    		return " ";
    	else {
    		String valoresTelefone = "";
    		for (Telefone telefone : listaTelefones) {
    			
    			valoresTelefone += "Telefone: "+telefone.getTelefone();
    			valoresTelefone += " ddd: " +telefone.getDdd();
    			valoresTelefone += " Responsáves: "+ telefone.getResponsavel();
    			valoresTelefone += " Whatsapp: "+ telefone.getWhatsapp();

    			if(!VerificadorUtil.verificarSeObjetoNuloOuZero(telefone.getOperadora().getId())) {
    				telefone.setOperadora(new OperadoraDAO().buscarOperadoraPorId(telefone.getOperadora().getId()));
    			
    				valoresTelefone += " Id Operadora: " +telefone.getOperadora().getId();
    				valoresTelefone += " Operadora: " +telefone.getOperadora().getDescricao();
    			}
    			
    			if(!VerificadorUtil.verificarSeObjetoNuloOuZero(telefone.getParentesco().getCodParentesco())) {
    				telefone.setParentesco(new ParentescoDAO().buscaParentesCocodigo(telefone.getParentesco().getCodParentesco()));
    			
    				valoresTelefone += " Cod Parentesco: "+telefone.getParentesco().getCodParentesco();
    				valoresTelefone += " Parentesco: "+telefone.getParentesco().getDescParentesco();
    			}
    			valoresTelefone += "; ";
			}
    		if(valoresTelefone.isEmpty())
    			return null;
    		else
    			return valoresTelefone;
    	}
    }
    
	public  String montarDescricaoListaTelefones(String valorAntigos, String valorNovos) {
		return CAMPO_LISTA_DE_TELEFONES+ ": Itens Excluídos: "+valorAntigos+"\n, Itens Adicionados: "+valorNovos+"\n";
	}
    
    private  String retornarValoresBoolean(String valor) {
        return valor.toUpperCase().trim().equals("FALSE") ? null : valor;
    }
}
