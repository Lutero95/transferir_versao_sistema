package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.comum.exception.ProjetoException;
import br.gov.al.maceio.sishosp.hosp.dao.EnderecoDAO;
import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;

public final class CEPUtil {

    public static EnderecoBean encontraCEP(String cep) throws ProjetoException {
        EnderecoBean endereco = new EnderecoBean();
        EnderecoDAO enderecoDAO = new EnderecoDAO();

        CepWebService cepWebService = new CepWebService(cep);
        if (cepWebService.getResultado() != 0) {
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
        }
        return endereco;
    }

}
