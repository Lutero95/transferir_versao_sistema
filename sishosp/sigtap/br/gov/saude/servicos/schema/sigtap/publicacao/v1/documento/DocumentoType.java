
package br.gov.saude.servicos.schema.sigtap.publicacao.v1.documento;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import br.gov.saude.servicos.schema.sigtap.publicacao.v1.orgaoorigem.OrgaoOrigemType;
import br.gov.saude.servicos.schema.sigtap.publicacao.v1.tipodocumento.TipoDocumentoType;


/**
 * <p>Classe Java de DocumentoType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="DocumentoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numeroDocumento" type="{http://servicos.saude.gov.br/schema/sigtap/publicacao/v1/documento}NumeroDocumentoType"/&gt;
 *         &lt;element name="TipoDocumento" type="{http://servicos.saude.gov.br/schema/sigtap/publicacao/v1/tipodocumento}TipoDocumentoType"/&gt;
 *         &lt;element name="OrgaoOrigem" type="{http://servicos.saude.gov.br/schema/sigtap/publicacao/v1/orgaoorigem}OrgaoOrigemType"/&gt;
 *         &lt;element name="dataPublicacao" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoType", propOrder = {
    "numeroDocumento",
    "tipoDocumento",
    "orgaoOrigem",
    "dataPublicacao"
})
public class DocumentoType {

    @XmlElement(required = true, nillable = true)
    protected String numeroDocumento;
    @XmlElement(name = "TipoDocumento", required = true, nillable = true)
    protected TipoDocumentoType tipoDocumento;
    @XmlElement(name = "OrgaoOrigem", required = true, nillable = true)
    protected OrgaoOrigemType orgaoOrigem;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataPublicacao;

    /**
     * Obtém o valor da propriedade numeroDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Define o valor da propriedade numeroDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDocumento(String value) {
        this.numeroDocumento = value;
    }

    /**
     * Obtém o valor da propriedade tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumentoType }
     *     
     */
    public TipoDocumentoType getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Define o valor da propriedade tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumentoType }
     *     
     */
    public void setTipoDocumento(TipoDocumentoType value) {
        this.tipoDocumento = value;
    }

    /**
     * Obtém o valor da propriedade orgaoOrigem.
     * 
     * @return
     *     possible object is
     *     {@link OrgaoOrigemType }
     *     
     */
    public OrgaoOrigemType getOrgaoOrigem() {
        return orgaoOrigem;
    }

    /**
     * Define o valor da propriedade orgaoOrigem.
     * 
     * @param value
     *     allowed object is
     *     {@link OrgaoOrigemType }
     *     
     */
    public void setOrgaoOrigem(OrgaoOrigemType value) {
        this.orgaoOrigem = value;
    }

    /**
     * Obtém o valor da propriedade dataPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define o valor da propriedade dataPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataPublicacao(XMLGregorianCalendar value) {
        this.dataPublicacao = value;
    }

}
