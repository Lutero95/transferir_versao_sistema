
package br.gov.saude.servicos.schema.sigtap.procedimento.origem.v1.origemsiasih;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TipoOrigemSIASIHType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoOrigemSIASIHType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AMBULATORIAL"/&gt;
 *     &lt;enumeration value="HOSPITALAR"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TipoOrigemSIASIHType")
@XmlEnum
public enum TipoOrigemSIASIHType {

    AMBULATORIAL,
    HOSPITALAR;

    public String value() {
        return name();
    }

    public static TipoOrigemSIASIHType fromValue(String v) {
        return valueOf(v);
    }

}
