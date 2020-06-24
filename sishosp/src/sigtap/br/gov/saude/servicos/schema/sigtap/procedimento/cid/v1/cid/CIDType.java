
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de CIDType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="CIDType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/cid}CodigoCIDType"/&gt;
 *         &lt;element name="nome" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="300"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="agravo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/cid}AgravoType" minOccurs="0"/&gt;
 *         &lt;element name="sexoAplicavel" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/sexopermitido}SexoPermitidoType" minOccurs="0"/&gt;
 *         &lt;element name="estadio" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="quantidadeCamposIrradiados" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="9999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CIDType", propOrder = {
    "codigo",
    "nome",
    "agravo",
    "sexoAplicavel",
    "estadio",
    "quantidadeCamposIrradiados"
})
public class CIDType {

    @XmlElement(required = true, nillable = true)
    protected String codigo;
    protected String nome;
    @XmlSchemaType(name = "string")
    protected AgravoType agravo;
    protected String sexoAplicavel;
    protected Boolean estadio;
    protected Integer quantidadeCamposIrradiados;

    /**
     * Obtém o valor da propriedade codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define o valor da propriedade codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Obtém o valor da propriedade agravo.
     * 
     * @return
     *     possible object is
     *     {@link AgravoType }
     *     
     */
    public AgravoType getAgravo() {
        return agravo;
    }

    /**
     * Define o valor da propriedade agravo.
     * 
     * @param value
     *     allowed object is
     *     {@link AgravoType }
     *     
     */
    public void setAgravo(AgravoType value) {
        this.agravo = value;
    }

    /**
     * Obtém o valor da propriedade sexoAplicavel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexoAplicavel() {
        return sexoAplicavel;
    }

    /**
     * Define o valor da propriedade sexoAplicavel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexoAplicavel(String value) {
        this.sexoAplicavel = value;
    }

    /**
     * Obtém o valor da propriedade estadio.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEstadio() {
        return estadio;
    }

    /**
     * Define o valor da propriedade estadio.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEstadio(Boolean value) {
        this.estadio = value;
    }

    /**
     * Obtém o valor da propriedade quantidadeCamposIrradiados.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQuantidadeCamposIrradiados() {
        return quantidadeCamposIrradiados;
    }

    /**
     * Define o valor da propriedade quantidadeCamposIrradiados.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQuantidadeCamposIrradiados(Integer value) {
        this.quantidadeCamposIrradiados = value;
    }

}
