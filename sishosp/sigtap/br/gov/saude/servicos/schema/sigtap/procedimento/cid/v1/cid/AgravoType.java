
package br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de AgravoType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="AgravoType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SEM_AGRAVO"/&gt;
 *     &lt;enumeration value="AGRAVO_NOTIFICACAO"/&gt;
 *     &lt;enumeration value="AGRAVO_BLOQUEIO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "AgravoType")
@XmlEnum
public enum AgravoType {

    SEM_AGRAVO,
    AGRAVO_NOTIFICACAO,
    AGRAVO_BLOQUEIO;

    public String value() {
        return name();
    }

    public static AgravoType fromValue(String v) {
        return valueOf(v);
    }

}
