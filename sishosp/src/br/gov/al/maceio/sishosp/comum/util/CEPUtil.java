package br.gov.al.maceio.sishosp.comum.util;

import br.gov.al.maceio.sishosp.hosp.model.EnderecoBean;

public final class CEPUtil {

    public static EnderecoBean encontraCEP(String cep) {
        EnderecoBean endereco = new EnderecoBean();

        CepWebService cepWebService = new CepWebService(cep);
        if (cepWebService.getResultado() != 0) {
            endereco.setLogradouro(cepWebService.getTipoLogradouro() + " " + cepWebService.getLogradouro());
            endereco.setUf(cepWebService.getEstado());
            endereco.setMunicipio(cepWebService.getCidade());
            endereco.setBairro(cepWebService.getBairro());
            endereco.setCodibge(cepWebService.getResultado());
            endereco.setCepValido(true);

        } else {
            endereco.setCepValido(false);
        }
        return endereco;
    }

}
