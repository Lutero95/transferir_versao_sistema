package br.gov.al.maceio.sishosp.comum.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;
import br.gov.al.maceio.sishosp.hosp.model.MunicipioBean;

public final class CEPUtil {

    public static EnderecoBean encontraCEP(String cep) throws ProjetoException {
        EnderecoBean endereco = new EnderecoBean();
        EnderecoDAO enderecoDAO = new EnderecoDAO();
        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        Callable<Object> task = new Callable<Object>() {
			public Object call() {
				CepWebService cepWebService = new CepWebService(cep);
				if(VerificadorUtil.verificarSeObjetoNulo(cepWebService))
					return null;
				else
					return cepWebService;
			}
        };
        
        Future<Object> future = executor.submit(task);
		try {
			Object result = future.get(10, TimeUnit.SECONDS);
			
			if (!VerificadorUtil.verificarSeObjetoNulo(result)) {

				CepWebService cepWebService = (CepWebService) result;

				if (cepWebService.getResultado() != 0) {
					MunicipioBean municipio = enderecoDAO.retornaCidadeComIbge(cepWebService.getCidade());

					if (!VerificadorUtil.verificarSeObjetoNulo(municipio)) {
						endereco.setCep(cep);
						endereco.setLogradouro(cepWebService.getTipoLogradouro() + " " + cepWebService.getLogradouro());
						endereco.setUf(cepWebService.getEstado());
						endereco.setMunicipio(municipio.getNome());
						endereco.setBairro(cepWebService.getBairro());
						endereco.setCodIbge(municipio.getCodigo());
						endereco.setCodmunicipio(municipio.getId());
						endereco.setCepValido(true);

						if (endereco.getBairro().equals("")) {
							endereco.setBairroUnico(true);
						} else {
							endereco.setBairroUnico(false);
						}
					} else {
						endereco = new EnderecoBean();
						endereco.setCepValido(false);
						JSFUtil.adicionarMensagemAdvertencia("Município do CEP inválido!", "Advertência");
					}

				} else {
					endereco.setCepValido(false);
					JSFUtil.adicionarMensagemAdvertencia("CEP inválido!", "Advertência");
				}
			} else {
				endereco.setCepValido(false);
				JSFUtil.adicionarMensagemAdvertencia("CEP inválido!", "Advertência");
			}

		}
        catch (TimeoutException ex) {
        	JSFUtil.adicionarMensagemAdvertencia("Não foi possível consultar o CEP informado", "");
            endereco.setCepValido(false);
        } catch (InterruptedException e) {
           JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
           e.printStackTrace();
        } catch (ExecutionException e) {
        	JSFUtil.adicionarMensagemErro(e.getMessage(), "Erro");
        	e.printStackTrace();
        } finally {
           future.cancel(true);
        }
        return endereco;
    }

}
