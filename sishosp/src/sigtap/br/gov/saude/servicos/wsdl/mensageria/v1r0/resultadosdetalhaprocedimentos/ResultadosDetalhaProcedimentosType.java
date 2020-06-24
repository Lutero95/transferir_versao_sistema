
package sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.ProcedimentoType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional.DetalheAdicionalType;


/**
 * <p>Classe Java de ResultadosDetalhaProcedimentosType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ResultadosDetalhaProcedimentosType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento}Procedimento"/&gt;
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
@XmlType(name = "ResultadosDetalhaProcedimentosType", propOrder = {
    "procedimento",
    "detalheAdicional"
})
public class ResultadosDetalhaProcedimentosType {

    @XmlElement(name = "Procedimento", namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento", required = true)
    protected ProcedimentoType procedimento;
    @XmlElement(name = "DetalheAdicional", namespace = "http://servicos.saude.gov.br/wsdl/mensageria/sigtap/v1/detalheadicional")
    protected List<DetalheAdicionalType> detalheAdicional;

    /**
     * Resposta da procedimento
     * 					
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType }
     *     
     */
    public ProcedimentoType getProcedimento() {
        return procedimento;
    }

    /**
     * Define o valor da propriedade procedimento.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType }
     *     
     */
    public void setProcedimento(ProcedimentoType value) {
        this.procedimento = value;
    }

    /**
     * Lista de detalheadicional Gets the value of the detalheAdicional property.
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
