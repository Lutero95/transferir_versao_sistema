
package sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.wsdl.mensageria.v1.paginacao.PaginacaoType;


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
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/grupo}codigoGrupo"/&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo}codigoSubgrupo" minOccurs="0"/&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/schema/corporativo/v1/competencia}competencia" minOccurs="0"/&gt;
 *         &lt;element ref="{http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao}Paginacao"/&gt;
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
    "codigoGrupo",
    "codigoSubgrupo",
    "competencia",
    "paginacao"
})
@XmlRootElement(name = "requestPesquisarProcedimentos")
public class RequestPesquisarProcedimentos {

    @XmlElement(namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/grupo", required = true)
    protected String codigoGrupo;
    @XmlElement(namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo")
    protected String codigoSubgrupo;
    @XmlElement(namespace = "http://servicos.saude.gov.br/schema/corporativo/v1/competencia")
    protected String competencia;
    @XmlElement(name = "Paginacao", namespace = "http://servicos.saude.gov.br/wsdl/mensageria/v1/paginacao", required = true)
    protected PaginacaoType paginacao;

    /**
     * Obt�m o valor da propriedade codigoGrupo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    /**
     * Define o valor da propriedade codigoGrupo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoGrupo(String value) {
        this.codigoGrupo = value;
    }

    /**
     * Obt�m o valor da propriedade codigoSubgrupo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSubgrupo() {
        return codigoSubgrupo;
    }

    /**
     * Define o valor da propriedade codigoSubgrupo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSubgrupo(String value) {
        this.codigoSubgrupo = value;
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
     * Obt�m o valor da propriedade paginacao.
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
