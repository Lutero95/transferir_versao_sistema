
package sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos package. 
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

    private final static QName _ResultadosPesquisaProcedimentos_QNAME = new QName("http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadospesquisaprocedimentos", "ResultadosPesquisaProcedimentos");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResultadosPesquisaProcedimentosType }
     * 
     */
    public ResultadosPesquisaProcedimentosType createResultadosPesquisaProcedimentosType() {
        return new ResultadosPesquisaProcedimentosType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadosPesquisaProcedimentosType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ResultadosPesquisaProcedimentosType }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadospesquisaprocedimentos", name = "ResultadosPesquisaProcedimentos")
    public JAXBElement<ResultadosPesquisaProcedimentosType> createResultadosPesquisaProcedimentos(ResultadosPesquisaProcedimentosType value) {
        return new JAXBElement<ResultadosPesquisaProcedimentosType>(_ResultadosPesquisaProcedimentos_QNAME, ResultadosPesquisaProcedimentosType.class, null, value);
    }

}
