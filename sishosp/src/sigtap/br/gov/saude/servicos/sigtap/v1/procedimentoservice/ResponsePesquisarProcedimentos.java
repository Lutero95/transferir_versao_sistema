
package sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadospesquisaprocedimentos.ResultadosPesquisaProcedimentosType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadospesquisaprocedimentos}ResultadosPesquisaProcedimentos"/&gt;
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
    "resultadosPesquisaProcedimentos"
})
@XmlRootElement(name = "responsePesquisarProcedimentos")
public class ResponsePesquisarProcedimentos {

    @XmlElement(name = "ResultadosPesquisaProcedimentos", namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadospesquisaprocedimentos", required = true)
    protected ResultadosPesquisaProcedimentosType resultadosPesquisaProcedimentos;

    /**
     * Resultado pesquisa do procedimentos.
     * 
     * @return
     *     possible object is
     *     {@link ResultadosPesquisaProcedimentosType }
     *     
     */
    public ResultadosPesquisaProcedimentosType getResultadosPesquisaProcedimentos() {
        return resultadosPesquisaProcedimentos;
    }

    /**
     * Define o valor da propriedade resultadosPesquisaProcedimentos.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadosPesquisaProcedimentosType }
     *     
     */
    public void setResultadosPesquisaProcedimentos(ResultadosPesquisaProcedimentosType value) {
        this.resultadosPesquisaProcedimentos = value;
    }

}
