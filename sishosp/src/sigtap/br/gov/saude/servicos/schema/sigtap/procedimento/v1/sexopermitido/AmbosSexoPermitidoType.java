
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.sexopermitido;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de _AmbosSexoPermitidoType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="_AmbosSexoPermitidoType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AMBOS"/&gt;
 *     &lt;enumeration value="NAO_SE_APLICA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "_AmbosSexoPermitidoType", namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/sexopermitido")
@XmlEnum
public enum AmbosSexoPermitidoType {

    AMBOS,
    NAO_SE_APLICA;

    public String value() {
        return name();
    }

    public static AmbosSexoPermitidoType fromValue(String v) {
        return valueOf(v);
    }

}
