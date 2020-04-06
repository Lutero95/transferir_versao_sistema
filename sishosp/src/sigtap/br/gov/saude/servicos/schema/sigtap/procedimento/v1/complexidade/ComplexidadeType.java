
package br.gov.saude.servicos.schema.sigtap.procedimento.v1.complexidade;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ComplexidadeType.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="ComplexidadeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ATENCAO_BASICA"/&gt;
 *     &lt;enumeration value="MEDIA_COMPLEXIDADE"/&gt;
 *     &lt;enumeration value="ALTA_COMPLEXIDADE_OU_CUSTO"/&gt;
 *     &lt;enumeration value="NAO_SE_APLICA"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ComplexidadeType", namespace = "http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/complexidade")
@XmlEnum
public enum ComplexidadeType {

    ATENCAO_BASICA,
    MEDIA_COMPLEXIDADE,
    ALTA_COMPLEXIDADE_OU_CUSTO,
    NAO_SE_APLICA;

    public String value() {
        return name();
    }

    public static ComplexidadeType fromValue(String v) {
        return valueOf(v);
    }

}
