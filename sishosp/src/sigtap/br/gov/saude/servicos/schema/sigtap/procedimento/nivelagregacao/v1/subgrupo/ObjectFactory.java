
package br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo package. 
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

    private final static QName _Subgrupo_QNAME = new QName("http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo", "Subgrupo");
    private final static QName _CodigoSubgrupo_QNAME = new QName("http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo", "codigoSubgrupo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubgrupoType }
     * 
     */
    public SubgrupoType createSubgrupoType() {
        return new SubgrupoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubgrupoType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubgrupoType }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo", name = "Subgrupo")
    public JAXBElement<SubgrupoType> createSubgrupo(SubgrupoType value) {
        return new JAXBElement<SubgrupoType>(_Subgrupo_QNAME, SubgrupoType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo", name = "codigoSubgrupo")
    public JAXBElement<String> createCodigoSubgrupo(String value) {
        return new JAXBElement<String>(_CodigoSubgrupo_QNAME, String.class, null, value);
    }

}
