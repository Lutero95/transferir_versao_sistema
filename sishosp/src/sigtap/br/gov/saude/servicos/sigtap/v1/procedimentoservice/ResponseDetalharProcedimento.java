
package br.gov.saude.servicos.sigtap.v1.procedimentoservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import br.gov.saude.servicos.wsdl.mensageria.v1r0.resultadosdetalhaprocedimentos.ResultadosDetalhaProcedimentosType;


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
 *         &lt;element ref="{http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadosdetalhaprocedimentos}ResultadosDetalhaProcedimentos"/&gt;
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
    "resultadosDetalhaProcedimentos"
})
@XmlRootElement(name = "responseDetalharProcedimento")
public class ResponseDetalharProcedimento {

    @XmlElement(name = "ResultadosDetalhaProcedimentos", namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1r0/resultadosdetalhaprocedimentos", required = true)
    protected ResultadosDetalhaProcedimentosType resultadosDetalhaProcedimentos;

    /**
     * Resultado pesquisa de detalha do procedimentos.
     * 
     * @return
     *     possible object is
     *     {@link ResultadosDetalhaProcedimentosType }
     *     
     */
    public ResultadosDetalhaProcedimentosType getResultadosDetalhaProcedimentos() {
        return resultadosDetalhaProcedimentos;
    }

    /**
     * Define o valor da propriedade resultadosDetalhaProcedimentos.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadosDetalhaProcedimentosType }
     *     
     */
    public void setResultadosDetalhaProcedimentos(ResultadosDetalhaProcedimentosType value) {
        this.resultadosDetalhaProcedimentos = value;
    }

}
