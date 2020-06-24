
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servico.ServicoType;


/**
 * <p>Classe Java de ServicoClassificacaoType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ServicoClassificacaoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Servico" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/servicoclassificacao/v1/servico}ServicoType"/&gt;
 *         &lt;element name="codigoClassificacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/servicoclassificacao/v1/servicoclassificacao}CodigoClassificacaoType"/&gt;
 *         &lt;element name="nomeClassificacao" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="150"/&gt;
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
@XmlType(name = "ServicoClassificacaoType", propOrder = {
    "servico",
    "codigoClassificacao",
    "nomeClassificacao"
})
public class ServicoClassificacaoType {

    @XmlElement(name = "Servico", required = true, nillable = true)
    protected ServicoType servico;
    @XmlElement(required = true, nillable = true)
    protected String codigoClassificacao;
    protected String nomeClassificacao;

    /**
     * Obtém o valor da propriedade servico.
     * 
     * @return
     *     possible object is
     *     {@link ServicoType }
     *     
     */
    public ServicoType getServico() {
        return servico;
    }

    /**
     * Define o valor da propriedade servico.
     * 
     * @param value
     *     allowed object is
     *     {@link ServicoType }
     *     
     */
    public void setServico(ServicoType value) {
        this.servico = value;
    }

    /**
     * Obtém o valor da propriedade codigoClassificacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoClassificacao() {
        return codigoClassificacao;
    }

    /**
     * Define o valor da propriedade codigoClassificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoClassificacao(String value) {
        this.codigoClassificacao = value;
    }

    /**
     * Obtém o valor da propriedade nomeClassificacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeClassificacao() {
        return nomeClassificacao;
    }

    /**
     * Define o valor da propriedade nomeClassificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeClassificacao(String value) {
        this.nomeClassificacao = value;
    }

}
