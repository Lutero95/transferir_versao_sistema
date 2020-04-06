
package sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos package. 
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

    private final static QName _ResultadosDetalhaProcedimentos_QNAME = new QName("http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadosdetalhaprocedimentos", "ResultadosDetalhaProcedimentos");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResultadosDetalhaProcedimentosType }
     * 
     */
    public ResultadosDetalhaProcedimentosType createResultadosDetalhaProcedimentosType() {
        return new ResultadosDetalhaProcedimentosType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadosDetalhaProcedimentosType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ResultadosDetalhaProcedimentosType }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadosdetalhaprocedimentos", name = "ResultadosDetalhaProcedimentos")
    public JAXBElement<ResultadosDetalhaProcedimentosType> createResultadosDetalhaProcedimentos(ResultadosDetalhaProcedimentosType value) {
        return new JAXBElement<ResultadosDetalhaProcedimentosType>(_ResultadosDetalhaProcedimentos_QNAME, ResultadosDetalhaProcedimentosType.class, null, value);
    }

}
