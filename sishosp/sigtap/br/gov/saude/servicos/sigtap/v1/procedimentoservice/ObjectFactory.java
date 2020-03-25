
package br.gov.saude.servicos.sigtap.v1.procedimentoservice;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.sigtap.v1.procedimentoservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.sigtap.v1.procedimentoservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RequestDetalharProcedimento }
     * 
     */
    public RequestDetalharProcedimento createRequestDetalharProcedimento() {
        return new RequestDetalharProcedimento();
    }

    /**
     * Create an instance of {@link RequestPesquisarProcedimentos }
     * 
     */
    public RequestPesquisarProcedimentos createRequestPesquisarProcedimentos() {
        return new RequestPesquisarProcedimentos();
    }

    /**
     * Create an instance of {@link ResponsePesquisarProcedimentos }
     * 
     */
    public ResponsePesquisarProcedimentos createResponsePesquisarProcedimentos() {
        return new ResponsePesquisarProcedimentos();
    }

    /**
     * Create an instance of {@link RequestDetalharProcedimento.DetalhesAdicionais }
     * 
     */
    public RequestDetalharProcedimento.DetalhesAdicionais createRequestDetalharProcedimentoDetalhesAdicionais() {
        return new RequestDetalharProcedimento.DetalhesAdicionais();
    }

    /**
     * Create an instance of {@link ResponseDetalharProcedimento }
     * 
     */
    public ResponseDetalharProcedimento createResponseDetalharProcedimento() {
        return new ResponseDetalharProcedimento();
    }

}
