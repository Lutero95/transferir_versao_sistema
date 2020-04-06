
package sigtap.br.gov.saude.servicos.schema.cbo.v1.cbo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.schema.cbo.v1.cbo package. 
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

    private final static QName _CBO_QNAME = new QName("http://servicos.saude.gov.br/schema/cbo/v1/cbo", "CBO");
    private final static QName _CodigoCBO_QNAME = new QName("http://servicos.saude.gov.br/schema/cbo/v1/cbo", "codigoCBO");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.schema.cbo.v1.cbo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CBOType }
     * 
     */
    public CBOType createCBOType() {
        return new CBOType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CBOType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CBOType }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/schema/cbo/v1/cbo", name = "CBO")
    public JAXBElement<CBOType> createCBO(CBOType value) {
        return new JAXBElement<CBOType>(_CBO_QNAME, CBOType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/schema/cbo/v1/cbo", name = "codigoCBO")
    public JAXBElement<String> createCodigoCBO(String value) {
        return new JAXBElement<String>(_CodigoCBO_QNAME, String.class, null, value);
    }

}
