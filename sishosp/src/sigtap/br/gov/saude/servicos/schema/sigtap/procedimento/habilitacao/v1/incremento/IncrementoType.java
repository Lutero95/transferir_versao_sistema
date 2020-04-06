
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.habilitacao.v1.incremento;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.habilitacao.v1.habilitacao.HabilitacaoType;


/**
 * <p>Classe Java de IncrementoType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="IncrementoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Habilitacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/habilitacao}HabilitacaoType"/&gt;
 *         &lt;element name="valorPercentualSH" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="99999.99"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="valorPercentualSA" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="99999.99"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="valorPercentualSP" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="99999.99"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
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
@XmlType(name = "IncrementoType", propOrder = {
    "habilitacao",
    "valorPercentualSH",
    "valorPercentualSA",
    "valorPercentualSP"
})
public class IncrementoType {

    @XmlElement(name = "Habilitacao", required = true, nillable = true)
    protected HabilitacaoType habilitacao;
    protected BigDecimal valorPercentualSH;
    protected BigDecimal valorPercentualSA;
    protected BigDecimal valorPercentualSP;

    /**
     * Obt�m o valor da propriedade habilitacao.
     * 
     * @return
     *     possible object is
     *     {@link HabilitacaoType }
     *     
     */
    public HabilitacaoType getHabilitacao() {
        return habilitacao;
    }

    /**
     * Define o valor da propriedade habilitacao.
     * 
     * @param value
     *     allowed object is
     *     {@link HabilitacaoType }
     *     
     */
    public void setHabilitacao(HabilitacaoType value) {
        this.habilitacao = value;
    }

    /**
     * Obt�m o valor da propriedade valorPercentualSH.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorPercentualSH() {
        return valorPercentualSH;
    }

    /**
     * Define o valor da propriedade valorPercentualSH.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorPercentualSH(BigDecimal value) {
        this.valorPercentualSH = value;
    }

    /**
     * Obt�m o valor da propriedade valorPercentualSA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorPercentualSA() {
        return valorPercentualSA;
    }

    /**
     * Define o valor da propriedade valorPercentualSA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorPercentualSA(BigDecimal value) {
        this.valorPercentualSA = value;
    }

    /**
     * Obt�m o valor da propriedade valorPercentualSP.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorPercentualSP() {
        return valorPercentualSP;
    }

    /**
     * Define o valor da propriedade valorPercentualSP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorPercentualSP(BigDecimal value) {
        this.valorPercentualSP = value;
    }

}
