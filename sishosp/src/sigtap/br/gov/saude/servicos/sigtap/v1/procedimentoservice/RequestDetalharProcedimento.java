
package sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.DetalheAdicionalType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento}codigoProcedimento"/&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/corporativo/v1/competencia}competencia" minOccurs="0"/&gt;
 *         &lt;element name="DetalhesAdicionais"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref="{http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional}DetalheAdicional" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
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
@XmlType(name = "", propOrder = {
    "codigoProcedimento",
    "competencia",
    "detalhesAdicionais"
})
@XmlRootElement(name = "requestDetalharProcedimento")
public class RequestDetalharProcedimento {

    @XmlElement(namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento", required = true)
    protected String codigoProcedimento;
    @XmlElement(namespace = "http://servicos.saude.gov.br/schema/corporativo/v1/competencia")
    protected String competencia;
    @XmlElement(name = "DetalhesAdicionais", required = true)
    protected RequestDetalharProcedimento.DetalhesAdicionais detalhesAdicionais;

    /**
     * Obt�m o valor da propriedade codigoProcedimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoProcedimento() {
        return codigoProcedimento;
    }

    /**
     * Define o valor da propriedade codigoProcedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoProcedimento(String value) {
        this.codigoProcedimento = value;
    }

    /**
     * Obt�m o valor da propriedade competencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetencia() {
        return competencia;
    }

    /**
     * Define o valor da propriedade competencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetencia(String value) {
        this.competencia = value;
    }

    /**
     * Obt�m o valor da propriedade detalhesAdicionais.
     * 
     * @return
     *     possible object is
     *     {@link RequestDetalharProcedimento.DetalhesAdicionais }
     *     
     */
    public RequestDetalharProcedimento.DetalhesAdicionais getDetalhesAdicionais() {
        return detalhesAdicionais;
    }

    /**
     * Define o valor da propriedade detalhesAdicionais.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestDetalharProcedimento.DetalhesAdicionais }
     *     
     */
    public void setDetalhesAdicionais(RequestDetalharProcedimento.DetalhesAdicionais value) {
        this.detalhesAdicionais = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element ref="{http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional}DetalheAdicional" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "detalheAdicional"
    })
    public static class DetalhesAdicionais {

        @XmlElement(name = "DetalheAdicional", namespace = "http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional")
        protected List<DetalheAdicionalType> detalheAdicional;

        /**
         * Gets the value of the detalheAdicional property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the detalheAdicional property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDetalheAdicional().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DetalheAdicionalType }
         * 
         * 
         */
        public List<DetalheAdicionalType> getDetalheAdicional() {
            if (detalheAdicional == null) {
                detalheAdicional = new ArrayList<DetalheAdicionalType>();
            }
            return this.detalheAdicional;
        }

    }

}
