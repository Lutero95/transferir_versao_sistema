
package sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional package. 
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

    private final static QName _DetalheAdicional_QNAME = new QName("http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional", "DetalheAdicional");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DetalheAdicionalType }
     * 
     */
    public DetalheAdicionalType createDetalheAdicionalType() {
        return new DetalheAdicionalType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DetalheAdicionalType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DetalheAdicionalType }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional", name = "DetalheAdicional")
    public JAXBElement<DetalheAdicionalType> createDetalheAdicional(DetalheAdicionalType value) {
        return new JAXBElement<DetalheAdicionalType>(_DetalheAdicional_QNAME, DetalheAdicionalType.class, null, value);
    }

}
