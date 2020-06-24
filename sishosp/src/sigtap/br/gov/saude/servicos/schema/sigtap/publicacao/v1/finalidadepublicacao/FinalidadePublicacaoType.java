
package sigtap.br.gov.saude.servicos.schema.sigtap.publicacao.v1.finalidadepublicacao;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de FinalidadePublicacaoType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="FinalidadePublicacaoType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INCLUSAO"/&gt;
 *     &lt;enumeration value="ALTERACAO"/&gt;
 *     &lt;enumeration value="REVOGACAO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FinalidadePublicacaoType", namespace = "http://servicos.saude.gov.br/schema/sigtap/publicacao/v1/finalidadepublicacao")
@XmlEnum
public enum FinalidadePublicacaoType {

    INCLUSAO,
    ALTERACAO,
    REVOGACAO;

    public String value() {
        return name();
    }

    public static FinalidadePublicacaoType fromValue(String v) {
        return valueOf(v);
    }

}
