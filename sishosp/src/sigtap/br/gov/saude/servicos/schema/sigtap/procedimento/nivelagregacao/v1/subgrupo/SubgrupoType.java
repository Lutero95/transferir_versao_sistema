
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.subgrupo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.grupo.GrupoType;


/**
 * <p>Classe Java de SubgrupoType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="SubgrupoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codigo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/subgrupo}CodigoSubgrupoType"/&gt;
 *         &lt;element name="nome" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Grupo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/grupo}GrupoType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubgrupoType", propOrder = {
    "codigo",
    "nome",
    "grupo"
})
public class SubgrupoType {

    @XmlElement(required = true, nillable = true)
    protected String codigo;
    protected String nome;
    @XmlElement(name = "Grupo")
    protected GrupoType grupo;

    /**
     * Obtém o valor da propriedade codigo.
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
     * Obtém o valor da propriedade nome.
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
     * Obtém o valor da propriedade grupo.
     * 
     * @return
     *     possible object is
     *     {@link GrupoType }
     *     
     */
    public GrupoType getGrupo() {
        return grupo;
    }

    /**
     * Define o valor da propriedade grupo.
     * 
     * @param value
     *     allowed object is
     *     {@link GrupoType }
     *     
     */
    public void setGrupo(GrupoType value) {
        this.grupo = value;
    }

}
