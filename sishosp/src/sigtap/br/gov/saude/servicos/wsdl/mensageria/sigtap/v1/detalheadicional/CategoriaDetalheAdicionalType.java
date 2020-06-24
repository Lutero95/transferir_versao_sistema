
package sigtap.br.gov.saude.servicos.wsdl.mensageria.sigtap.v1.detalheadicional;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de CategoriaDetalheAdicionalType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="CategoriaDetalheAdicionalType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="DESCRICAO"/&gt;
 *     &lt;enumeration value="CIDS"/&gt;
 *     &lt;enumeration value="CBOS"/&gt;
 *     &lt;enumeration value="CATEGORIAS_CBO"/&gt;
 *     &lt;enumeration value="TIPOS_LEITO"/&gt;
 *     &lt;enumeration value="SERVICOS_CLASSIFICACOES"/&gt;
 *     &lt;enumeration value="HABILITACOES"/&gt;
 *     &lt;enumeration value="GRUPOS_HABILITACAO"/&gt;
 *     &lt;enumeration value="INCREMENTOS"/&gt;
 *     &lt;enumeration value="COMPONENTES_REDE"/&gt;
 *     &lt;enumeration value="ORIGENS_SIGTAP"/&gt;
 *     &lt;enumeration value="ORIGENS_SIA_SIH"/&gt;
 *     &lt;enumeration value="REGRAS_CONDICIONADA"/&gt;
 *     &lt;enumeration value="RENASES"/&gt;
 *     &lt;enumeration value="TUSS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CategoriaDetalheAdicionalType")
@XmlEnum
public enum CategoriaDetalheAdicionalType {

    DESCRICAO,
    CIDS,
    CBOS,
    CATEGORIAS_CBO,
    TIPOS_LEITO,
    SERVICOS_CLASSIFICACOES,
    HABILITACOES,
    GRUPOS_HABILITACAO,
    INCREMENTOS,
    COMPONENTES_REDE,
    ORIGENS_SIGTAP,
    ORIGENS_SIA_SIH,
    REGRAS_CONDICIONADA,
    RENASES,
    TUSS;

    public String value() {
        return name();
    }

    public static CategoriaDetalheAdicionalType fromValue(String v) {
        return valueOf(v);
    }

}
