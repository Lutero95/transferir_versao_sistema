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

public final class CEPUtil {

    public static EnderecoBean encontraCEP(String cep) throws ProjetoException {
        EnderecoBean endereco = new EnderecoBean();
        EnderecoDAO enderecoDAO = new EnderecoDAO();
        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        Callable<Object> task = new Callable<Object>() {
        	   public Object call() {
        	      return new CepWebService(cep);
        	   }
        };
        
        Future<Object> future = executor.submit(task);
        try {
           Object result = future.get(10, TimeUnit.SECONDS); 
           CepWebService cepWebService = (CepWebService) result;
           
		if (cepWebService .getResultado() != 0) {
               endereco.setLogradouro(cepWebService.getTipoLogradouro() + " " + cepWebService.getLogradouro());
               endereco.setUf(cepWebService.getEstado());
               endereco.setMunicipio(cepWebService.getCidade());
               endereco.setBairro(cepWebService.getBairro());
               endereco.setCodIbge(cepWebService.getResultado());
               endereco.setCodmunicipio(enderecoDAO.retornarCodigoCidade(endereco.getCodIbge()));
               endereco.setCepValido(true);

               if(endereco.getBairro().equals("")){
                   endereco.setBairroUnico(true);
               }
               else{
                   endereco.setBairroUnico(false);
               }
           } else {
              endereco.setCepValido(false);
			  JSFUtil.adicionarMensagemAdvertencia("CEP inválido!", "Advertência");
           }
           
        } catch (TimeoutException ex) {
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
