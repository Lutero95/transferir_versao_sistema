
package sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1.paginacao.PaginacaoType;


/**
 * <p>Classe Java de DetalheAdicionalType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="DetalheAdicionalType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="categoriaDetalheAdicional" type="{http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional}CategoriaDetalheAdicionalType"/&gt;
 *         &lt;element name="Paginacao" type="{http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao}PaginacaoType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DetalheAdicionalType", propOrder = {
    "categoriaDetalheAdicional",
    "paginacao"
})
public class DetalheAdicionalType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CategoriaDetalheAdicionalType categoriaDetalheAdicional;
    @XmlElement(name = "Paginacao")
    protected PaginacaoType paginacao;

    /**
     * Obt�m o valor da propriedade categoriaDetalheAdicional.
     * 
     * @return
     *     possible object is
     *     {@link CategoriaDetalheAdicionalType }
     *     
     */
    public CategoriaDetalheAdicionalType getCategoriaDetalheAdicional() {
        return categoriaDetalheAdicional;
    }

    /**
     * Define o valor da propriedade categoriaDetalheAdicional.
     * 
     * @param value
     *     allowed object is
     *     {@link CategoriaDetalheAdicionalType }
     *     
     */
    public void setCategoriaDetalheAdicional(CategoriaDetalheAdicionalType value) {
        this.categoriaDetalheAdicional = value;
    }

    /**
     * Obt�m o valor da propriedade paginacao.
     * 
     * @return
     *     possible object is
     *     {@link PaginacaoType }
     *     
     */
    public PaginacaoType getPaginacao() {
        return paginacao;
    }

    /**
     * Define o valor da propriedade paginacao.
     * 
     * @param value
     *     allowed object is
     *     {@link PaginacaoType }
     *     
     */
    public void setPaginacao(PaginacaoType value) {
        this.paginacao = value;
    }

}
