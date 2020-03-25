
package br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento.BaseProcedimentoType;
import br.gov.saude.servicos.wsdl.mensageria.v1.paginacao.PaginacaoType;


/**
 * <p>Classe Java de ResultadosPesquisaProcedimentosType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ResultadosPesquisaProcedimentosType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento}BaseProcedimento" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao}Paginacao" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadosPesquisaProcedimentosType", propOrder = {
    "baseProcedimento",
    "paginacao"
})
public class ResultadosPesquisaProcedimentosType {

    @XmlElement(name = "BaseProcedimento", namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento")
    protected List<BaseProcedimentoType> baseProcedimento;
    @XmlElement(name = "Paginacao", namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao")
    protected PaginacaoType paginacao;

    /**
     * Lista de Procedimento base Gets the value of the baseProcedimento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the baseProcedimento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBaseProcedimento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BaseProcedimentoType }
     * 
     * 
     */
    public List<BaseProcedimentoType> getBaseProcedimento() {
        if (baseProcedimento == null) {
            baseProcedimento = new ArrayList<BaseProcedimentoType>();
        }
        return this.baseProcedimento;
    }

    /**
     * Resposta da paginação
     * 					
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
