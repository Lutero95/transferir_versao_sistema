
package sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de UnidadeLimiteType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conte�do esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="UnidadeLimiteType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MESES"/&gt;
 *     &lt;enumeration value="ANOS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "UnidadeLimiteType")
@XmlEnum
public enum UnidadeLimiteType {

    MESES,
    ANOS;

    public String value() {
        return name();
    }

    public static UnidadeLimiteType fromValue(String v) {
        return valueOf(v);
    }

}
