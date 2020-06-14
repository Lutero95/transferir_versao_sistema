
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.al.maceio.sishosp.hosp.model.HistoricoSigtapBean;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo.SubgrupoType;


/**
 * <p>Classe Java de FormaOrganizacaoType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="FormaOrganizacaoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/formaorganizacao}CodigoFormaOrganizacaoType"/&gt;
 *         &lt;element name="nome" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Subgrupo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo}SubgrupoType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormaOrganizacaoType", propOrder = {
    "codigo",
    "nome",
    "subgrupo"
})
public class FormaOrganizacaoType {

    @XmlElement(required = true, nillable = true)
    protected String codigo;
    protected String nome;
    @XmlElement(name = "Subgrupo")
    protected SubgrupoType subgrupo;
    protected HistoricoSigtapBean historicoSigtap;

    /**
     * Obt�m o valor da propriedade codigo.
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
     * Obt�m o valor da propriedade nome.
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
     * Obt�m o valor da propriedade subgrupo.
     * 
     * @return
     *     possible object is
     *     {@link SubgrupoType }
     *     
     */
    public SubgrupoType getSubgrupo() {
        return subgrupo;
    }

    /**
     * Define o valor da propriedade subgrupo.
     * 
     * @param value
     *     allowed object is
     *     {@link SubgrupoType }
     *     
     */
    public void setSubgrupo(SubgrupoType value) {
        this.subgrupo = value;
    }

    public HistoricoSigtapBean getHistoricoSigtap() {
        return historicoSigtap;
    }

    public void setHistoricoSigtap(HistoricoSigtapBean historicoSigtap) {
        this.historicoSigtap = historicoSigtap;
    }
}
