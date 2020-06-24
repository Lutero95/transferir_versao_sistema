
package sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de IdadeLimiteType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="IdadeLimiteType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="quantidadeLimite"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="unidadeLimite" type="{http://servicos.saude.gov.br/schema/sigtap/v1/idadelimite}UnidadeLimiteType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdadeLimiteType", propOrder = {
    "quantidadeLimite",
    "unidadeLimite"
})
public class IdadeLimiteType {

    protected Integer quantidadeLimite;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected UnidadeLimiteType unidadeLimite;

    /**
     * Obtém o valor da propriedade quantidadeLimite.
     * 
     */
    public Integer getQuantidadeLimite() {
        return quantidadeLimite;
    }

    /**
     * Define o valor da propriedade quantidadeLimite.
     * 
     */
    public void setQuantidadeLimite(Integer value) {
        this.quantidadeLimite = value;
    }

    /**
     * Obtém o valor da propriedade unidadeLimite.
     * 
     * @return
     *     possible object is
     *     {@link UnidadeLimiteType }
     *     
     */
    public UnidadeLimiteType getUnidadeLimite() {
        return unidadeLimite;
    }

    /**
     * Define o valor da propriedade unidadeLimite.
     * 
     * @param value
     *     allowed object is
     *     {@link UnidadeLimiteType }
     *     
     */
    public void setUnidadeLimite(UnidadeLimiteType value) {
        this.unidadeLimite = value;
    }

}
