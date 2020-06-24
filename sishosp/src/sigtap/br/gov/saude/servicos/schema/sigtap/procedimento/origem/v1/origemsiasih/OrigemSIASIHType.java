
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.origem.v1.origemsiasih;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de OrigemSIASIHType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="OrigemSIASIHType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigoProcedimentoSIASIH" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/origem/v1/origemsiasih}CodigoProcedimentoSIASIH"/&gt;
 *         &lt;element name="nomeProcedimentoSIASIH" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="tipoOrigemSIASIHType" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/origem/v1/origemsiasih}TipoOrigemSIASIHType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrigemSIASIHType", propOrder = {
    "codigoProcedimentoSIASIH",
    "nomeProcedimentoSIASIH",
    "tipoOrigemSIASIHType"
})
public class OrigemSIASIHType {

    @XmlElement(required = true, nillable = true)
    protected String codigoProcedimentoSIASIH;
    protected String nomeProcedimentoSIASIH;
    @XmlSchemaType(name = "string")
    protected TipoOrigemSIASIHType tipoOrigemSIASIHType;

    /**
     * Obtém o valor da propriedade codigoProcedimentoSIASIH.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoProcedimentoSIASIH() {
        return codigoProcedimentoSIASIH;
    }

    /**
     * Define o valor da propriedade codigoProcedimentoSIASIH.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoProcedimentoSIASIH(String value) {
        this.codigoProcedimentoSIASIH = value;
    }

    /**
     * Obtém o valor da propriedade nomeProcedimentoSIASIH.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeProcedimentoSIASIH() {
        return nomeProcedimentoSIASIH;
    }

    /**
     * Define o valor da propriedade nomeProcedimentoSIASIH.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeProcedimentoSIASIH(String value) {
        this.nomeProcedimentoSIASIH = value;
    }

    /**
     * Obtém o valor da propriedade tipoOrigemSIASIHType.
     * 
     * @return
     *     possible object is
     *     {@link TipoOrigemSIASIHType }
     *     
     */
    public TipoOrigemSIASIHType getTipoOrigemSIASIHType() {
        return tipoOrigemSIASIHType;
    }

    /**
     * Define o valor da propriedade tipoOrigemSIASIHType.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoOrigemSIASIHType }
     *     
     */
    public void setTipoOrigemSIASIHType(TipoOrigemSIASIHType value) {
        this.tipoOrigemSIASIHType = value;
    }

}
