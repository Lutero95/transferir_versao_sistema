
package sigtap.br.gov.saude.servicos.wsdl.mensageria.v1.paginacao;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.wsdl.mensageria.v1.paginacao package. 
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

    private final static QName _Paginacao_QNAME = new QName("http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao", "Paginacao");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.wsdl.mensageria.v1.paginacao
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaginacaoType }
     * 
     */
    public PaginacaoType createPaginacaoType() {
        return new PaginacaoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PaginacaoType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PaginacaoType }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao", name = "Paginacao")
    public JAXBElement<PaginacaoType> createPaginacao(PaginacaoType value) {
        return new JAXBElement<PaginacaoType>(_Paginacao_QNAME, PaginacaoType.class, null, value);
    }

}
