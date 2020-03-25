
package br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.vinculocid;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de VinculoCIDType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="VinculoCIDType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PRIMARIO"/&gt;
 *     &lt;enumeration value="SECUNDARIO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "VinculoCIDType", namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/vinculocid")
@XmlEnum
public enum VinculoCIDType {

    PRIMARIO,
    SECUNDARIO;

    public String value() {
        return name();
    }

    public static VinculoCIDType fromValue(String v) {
        return valueOf(v);
    }

}
