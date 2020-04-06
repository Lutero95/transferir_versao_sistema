
package br.gov.saude.servicos.schema.corporativo.pessoafisica.v2.sexo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de SexoType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="SexoType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MASCULINO"/&gt;
 *     &lt;enumeration value="FEMININO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SexoType", namespace = "http://servicos.saude.gov.br/schema/corporativo/pessoafisica/v2/sexo")
@XmlEnum
public enum SexoType {

    MASCULINO,
    FEMININO;

    public String value() {
        return name();
    }

    public static SexoType fromValue(String v) {
        return valueOf(v);
    }

}
