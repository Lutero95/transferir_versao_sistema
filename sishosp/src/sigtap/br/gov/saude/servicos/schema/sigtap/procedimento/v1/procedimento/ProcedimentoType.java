
package sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.procedimento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import sigtap.br.gov.saude.servicos.schema.cbo.v1.categoriacbo.CategoriaCBOType;
import sigtap.br.gov.saude.servicos.schema.cbo.v1.cbo.CBOType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.cid.CIDType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.cid.v1.vinculocid.VinculoCIDType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.rubrica.RubricaType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.financiamento.v1.tipofinanciamento.TipoFinanciamentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.habilitacao.v1.grupohabilitacao.GrupoHabilitacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.habilitacao.v1.habilitacao.HabilitacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.habilitacao.v1.incremento.IncrementoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.nivelagregacao.v1.formaorganizacao.FormaOrganizacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.origem.v1.origemsiasih.OrigemSIASIHType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.redeatencao.v1.componenteredeatencao.ComponenteRedeAtencaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.regracondicionada.v1.regracondicionada.RegraCondicionadaType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.renases.v1.renases.RENASESType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.servicoclassificacao.v1.servicoclassificacao.ServicoClassificacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.tipoleito.v1.tipoleito.TipoLeitoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.tuss.v1.tuss.TUSSType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.atributocomplementar.AtributoComplementarType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.complexidade.ComplexidadeType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.instrumentoregistro.InstrumentoRegistroType;
import sigtap.br.gov.saude.servicos.schema.sigtap.procedimento.v1.modalidadeatendimento.ModalidadeAtendimentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.publicacao.v1.documento.DocumentoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.publicacao.v1.finalidadepublicacao.FinalidadePublicacaoType;
import sigtap.br.gov.saude.servicos.schema.sigtap.v1.idadelimite.IdadeLimiteType;


/**
 * 
 *                 
 *                 Este tipo representa um Procedimento completo.
 *                 
 *             
 * 
 * <p>Classe Java de ProcedimentoType complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ProcedimentoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento}BaseProcedimentoType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FormaOrganizacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/nivelagregacao/v1/formaorganizacao}FormaOrganizacaoType" minOccurs="0"/&gt;
 *         &lt;element name="competenciaInicial" type="{http://servicos.saude.gov.br/schema/corporativo/v1/competencia}CompetenciaType" minOccurs="0"/&gt;
 *         &lt;element name="competenciaFinal" type="{http://servicos.saude.gov.br/schema/corporativo/v1/competencia}CompetenciaType" minOccurs="0"/&gt;
 *         &lt;element name="finalidadePublicacao" type="{http://servicos.saude.gov.br/schema/sigtap/publicacao/v1/finalidadepublicacao}FinalidadePublicacaoType" minOccurs="0"/&gt;
 *         &lt;element name="DocumentoPublicacao" type="{http://servicos.saude.gov.br/schema/sigtap/publicacao/v1/documento}DocumentoType" minOccurs="0"/&gt;
 *         &lt;element name="ModalidadesAtendimento" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ModalidadeAtendimento" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/modalidadeatendimento}ModalidadeAtendimentoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="complexidade" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/complexidade}ComplexidadeType" minOccurs="0"/&gt;
 *         &lt;element name="TipoFinanciamento" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/financiamento/v1/tipofinanciamento}TipoFinanciamentoType" minOccurs="0"/&gt;
 *         &lt;element name="Rubrica" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/financiamento/v1/rubrica}RubricaType" minOccurs="0"/&gt;
 *         &lt;element name="InstrumentosRegistro" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="InstrumentoRegistro" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/instrumentoregistro}InstrumentoRegistroType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="mediaPermanencia" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="9999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="tempoPermanencia" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="9999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="quantidadeMaxima" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="9999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="sexoPermitido" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/sexopermitido}SexoPermitidoType" minOccurs="0"/&gt;
 *         &lt;element name="idadeMinimaPermitida" type="{http://servicos.saude.gov.br/schema/sigtap/v1/idadelimite}IdadeLimiteType" minOccurs="0"/&gt;
 *         &lt;element name="idadeMaximaPermitida" type="{http://servicos.saude.gov.br/schema/sigtap/v1/idadelimite}IdadeLimiteType" minOccurs="0"/&gt;
 *         &lt;element name="pontos" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="9999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="valorSH" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="99999999.99"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="valorSA" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="99999999.99"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="valorSP" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="99999999.99"/&gt;
 *               &lt;fractionDigits value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="AtributosComplementares" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="AtributoComplementar" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/atributocomplementar}AtributoComplementarType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="descricao" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="4000"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CIDsVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CIDVinculado" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="CID" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/cid}CIDType" minOccurs="0"/&gt;
 *                             &lt;element name="vinculo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/vinculocid}VinculoCIDType" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CBOsVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CBO" type="{http://servicos.saude.gov.br/schema/cbo/v1/cbo}CBOType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CategoriasCBOVinculadas" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="CategoriaCBO" type="{http://servicos.saude.gov.br/schema/cbo/v1/categoriacbo}CategoriaCBOType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TiposLeitoVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TipoLeito" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/tipoleito/v1/tipoleito}TipoLeitoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ServicosClassificacoesVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ServicoClassificacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/servicoclassificacao/v1/servicoclassificacao}ServicoClassificacaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="HabilitacoesVinculadas" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Habilitacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/habilitacao}HabilitacaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="GruposHabilitacaoVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="GrupoHabilitacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/grupohabilitacao}GrupoHabilitacaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="IncrementosVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Incremento" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/incremento}IncrementoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ComponentesRedesVinculados" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ComponenteRedeAtencao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/redeatencao/v1/componenteredeatencao}ComponenteRedeAtencaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="OrigensSIGTAP" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="OrigemSIGTAP" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento}BaseProcedimentoType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="OrigensSIASIH" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="OrigemSIASIH" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/origem/v1/origemsiasih}OrigemSIASIHType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RegrasCondicionadasVinculadas" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="RegraCondicionada" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/regracondicionada/v1/regracondicionada}RegraCondicionadaType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RENASESVinculadas" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="RENASES" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/renases/v1/renases}RENASESType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TUSSVinculadas" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="TUSS" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/tuss/v1/tuss}TUSSType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcedimentoType", propOrder = {
    "formaOrganizacao",
    "competenciaInicial",
    "competenciaFinal",
    "finalidadePublicacao",
    "documentoPublicacao",
    "modalidadesAtendimento",
    "complexidade",
    "tipoFinanciamento",
    "rubrica",
    "instrumentosRegistro",
    "mediaPermanencia",
    "tempoPermanencia",
    "quantidadeMaxima",
    "sexoPermitido",
    "idadeMinimaPermitida",
    "idadeMaximaPermitida",
    "pontos",
    "valorSH",
    "valorSA",
    "valorSP",
    "atributosComplementares",
    "descricao",
    "ciDsVinculados",
    "cbOsVinculados",
    "categoriasCBOVinculadas",
    "tiposLeitoVinculados",
    "servicosClassificacoesVinculados",
    "habilitacoesVinculadas",
    "gruposHabilitacaoVinculados",
    "incrementosVinculados",
    "componentesRedesVinculados",
    "origensSIGTAP",
    "origensSIASIH",
    "regrasCondicionadasVinculadas",
    "renasesVinculadas",
    "tussVinculadas"
})
public class ProcedimentoType
    extends BaseProcedimentoType
{

    @XmlElement(name = "FormaOrganizacao")
    protected FormaOrganizacaoType formaOrganizacao;
    protected String competenciaInicial;
    protected String competenciaFinal;
    protected String competenciaValidade;
    @XmlSchemaType(name = "string")
    protected FinalidadePublicacaoType finalidadePublicacao;
    @XmlElement(name = "DocumentoPublicacao")
    protected DocumentoType documentoPublicacao;
    @XmlElement(name = "ModalidadesAtendimento")
    protected ProcedimentoType.ModalidadesAtendimento modalidadesAtendimento;
    @XmlSchemaType(name = "string")
    protected ComplexidadeType complexidade;
    @XmlElement(name = "TipoFinanciamento")
    protected TipoFinanciamentoType tipoFinanciamento;
    @XmlElement(name = "Rubrica")
    protected RubricaType rubrica;
    @XmlElement(name = "InstrumentosRegistro")
    protected ProcedimentoType.InstrumentosRegistro instrumentosRegistro;
    protected Integer mediaPermanencia;
    protected Integer tempoPermanencia;
    protected Integer quantidadeMaxima;
    protected String sexoPermitido;
    protected IdadeLimiteType idadeMinimaPermitida;
    protected IdadeLimiteType idadeMaximaPermitida;
    protected Integer pontos;
    protected BigDecimal valorSH;
    protected BigDecimal valorSA;
    protected BigDecimal valorSP;
    @XmlElement(name = "AtributosComplementares")
    protected ProcedimentoType.AtributosComplementares atributosComplementares;
    protected String descricao;
    @XmlElement(name = "CIDsVinculados")
    protected ProcedimentoType.CIDsVinculados ciDsVinculados;
    @XmlElement(name = "CBOsVinculados")
    protected ProcedimentoType.CBOsVinculados cbOsVinculados;
    @XmlElement(name = "CategoriasCBOVinculadas")
    protected ProcedimentoType.CategoriasCBOVinculadas categoriasCBOVinculadas;
    @XmlElement(name = "TiposLeitoVinculados")
    protected ProcedimentoType.TiposLeitoVinculados tiposLeitoVinculados;
    @XmlElement(name = "ServicosClassificacoesVinculados")
    protected ProcedimentoType.ServicosClassificacoesVinculados servicosClassificacoesVinculados;
    @XmlElement(name = "HabilitacoesVinculadas")
    protected ProcedimentoType.HabilitacoesVinculadas habilitacoesVinculadas;
    @XmlElement(name = "GruposHabilitacaoVinculados")
    protected ProcedimentoType.GruposHabilitacaoVinculados gruposHabilitacaoVinculados;
    @XmlElement(name = "IncrementosVinculados")
    protected ProcedimentoType.IncrementosVinculados incrementosVinculados;
    @XmlElement(name = "ComponentesRedesVinculados")
    protected ProcedimentoType.ComponentesRedesVinculados componentesRedesVinculados;
    @XmlElement(name = "OrigensSIGTAP")
    protected ProcedimentoType.OrigensSIGTAP origensSIGTAP;
    @XmlElement(name = "OrigensSIASIH")
    protected ProcedimentoType.OrigensSIASIH origensSIASIH;
    @XmlElement(name = "RegrasCondicionadasVinculadas")
    protected ProcedimentoType.RegrasCondicionadasVinculadas regrasCondicionadasVinculadas;
    @XmlElement(name = "RENASESVinculadas")
    protected ProcedimentoType.RENASESVinculadas renasesVinculadas;
    @XmlElement(name = "TUSSVinculadas")
    protected ProcedimentoType.TUSSVinculadas tussVinculadas;

    /**
     * Obtém o valor da propriedade formaOrganizacao.
     * 
     * @return
     *     possible object is
     *     {@link FormaOrganizacaoType }
     *     
     */
    public FormaOrganizacaoType getFormaOrganizacao() {
        return formaOrganizacao;
    }

    /**
     * Define o valor da propriedade formaOrganizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link FormaOrganizacaoType }
     *     
     */
    public void setFormaOrganizacao(FormaOrganizacaoType value) {
        this.formaOrganizacao = value;
    }

    /**
     * Obtém o valor da propriedade competenciaInicial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetenciaInicial() {
        return competenciaInicial;
    }

    /**
     * Define o valor da propriedade competenciaInicial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetenciaInicial(String value) {
        this.competenciaInicial = value;
    }


    /**
     * Obtém o valor da propriedade competenciaFinal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetenciaFinal() {
        return competenciaFinal;
    }

    /**
     * Define o valor da propriedade competenciaFinal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetenciaFinal(String value) {
        this.competenciaFinal = value;
    }


    public String getCompetenciaValidade() {
        return competenciaValidade;
    }

    public void setCompetenciaValidade(String value) {
        this.competenciaValidade = value;
    }

    /**
     * Obtém o valor da propriedade finalidadePublicacao.
     * 
     * @return
     *     possible object is
     *     {@link FinalidadePublicacaoType }
     *     
     */
    public FinalidadePublicacaoType getFinalidadePublicacao() {
        return finalidadePublicacao;
    }

    /**
     * Define o valor da propriedade finalidadePublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link FinalidadePublicacaoType }
     *     
     */
    public void setFinalidadePublicacao(FinalidadePublicacaoType value) {
        this.finalidadePublicacao = value;
    }

    /**
     * Obtém o valor da propriedade documentoPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoType }
     *     
     */
    public DocumentoType getDocumentoPublicacao() {
        return documentoPublicacao;
    }

    /**
     * Define o valor da propriedade documentoPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoType }
     *     
     */
    public void setDocumentoPublicacao(DocumentoType value) {
        this.documentoPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade modalidadesAtendimento.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.ModalidadesAtendimento }
     *     
     */
    public ProcedimentoType.ModalidadesAtendimento getModalidadesAtendimento() {
        return modalidadesAtendimento;
    }

    /**
     * Define o valor da propriedade modalidadesAtendimento.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.ModalidadesAtendimento }
     *     
     */
    public void setModalidadesAtendimento(ProcedimentoType.ModalidadesAtendimento value) {
        this.modalidadesAtendimento = value;
    }

    /**
     * Obtém o valor da propriedade complexidade.
     * 
     * @return
     *     possible object is
     *     {@link ComplexidadeType }
     *     
     */
    public ComplexidadeType getComplexidade() {
        return complexidade;
    }

    /**
     * Define o valor da propriedade complexidade.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplexidadeType }
     *     
     */
    public void setComplexidade(ComplexidadeType value) {
        this.complexidade = value;
    }

    /**
     * Obtém o valor da propriedade tipoFinanciamento.
     * 
     * @return
     *     possible object is
     *     {@link TipoFinanciamentoType }
     *     
     */
    public TipoFinanciamentoType getTipoFinanciamento() {
        return tipoFinanciamento;
    }

    /**
     * Define o valor da propriedade tipoFinanciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoFinanciamentoType }
     *     
     */
    public void setTipoFinanciamento(TipoFinanciamentoType value) {
        this.tipoFinanciamento = value;
    }

    /**
     * Obtém o valor da propriedade rubrica.
     * 
     * @return
     *     possible object is
     *     {@link RubricaType }
     *     
     */
    public RubricaType getRubrica() {
        return rubrica;
    }

    /**
     * Define o valor da propriedade rubrica.
     * 
     * @param value
     *     allowed object is
     *     {@link RubricaType }
     *     
     */
    public void setRubrica(RubricaType value) {
        this.rubrica = value;
    }

    /**
     * Obtém o valor da propriedade instrumentosRegistro.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.InstrumentosRegistro }
     *     
     */
    public ProcedimentoType.InstrumentosRegistro getInstrumentosRegistro() {
        return instrumentosRegistro;
    }

    /**
     * Define o valor da propriedade instrumentosRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.InstrumentosRegistro }
     *     
     */
    public void setInstrumentosRegistro(ProcedimentoType.InstrumentosRegistro value) {
        this.instrumentosRegistro = value;
    }

    /**
     * Obtém o valor da propriedade mediaPermanencia.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMediaPermanencia() {
        return mediaPermanencia;
    }

    /**
     * Define o valor da propriedade mediaPermanencia.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMediaPermanencia(Integer value) {
        this.mediaPermanencia = value;
    }

    /**
     * Obtém o valor da propriedade tempoPermanencia.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTempoPermanencia() {
        return tempoPermanencia;
    }

    /**
     * Define o valor da propriedade tempoPermanencia.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTempoPermanencia(Integer value) {
        this.tempoPermanencia = value;
    }

    /**
     * Obtém o valor da propriedade quantidadeMaxima.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    /**
     * Define o valor da propriedade quantidadeMaxima.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQuantidadeMaxima(Integer value) {
        this.quantidadeMaxima = value;
    }

    /**
     * Obtém o valor da propriedade sexoPermitido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSexoPermitido() {
        return sexoPermitido;
    }

    /**
     * Define o valor da propriedade sexoPermitido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSexoPermitido(String value) {
        this.sexoPermitido = value;
    }

    /**
     * Obtém o valor da propriedade idadeMinimaPermitida.
     * 
     * @return
     *     possible object is
     *     {@link IdadeLimiteType }
     *     
     */
    public IdadeLimiteType getIdadeMinimaPermitida() {
        return idadeMinimaPermitida;
    }

    /**
     * Define o valor da propriedade idadeMinimaPermitida.
     * 
     * @param value
     *     allowed object is
     *     {@link IdadeLimiteType }
     *     
     */
    public void setIdadeMinimaPermitida(IdadeLimiteType value) {
        this.idadeMinimaPermitida = value;
    }

    /**
     * Obtém o valor da propriedade idadeMaximaPermitida.
     * 
     * @return
     *     possible object is
     *     {@link IdadeLimiteType }
     *     
     */
    public IdadeLimiteType getIdadeMaximaPermitida() {
        return idadeMaximaPermitida;
    }

    /**
     * Define o valor da propriedade idadeMaximaPermitida.
     * 
     * @param value
     *     allowed object is
     *     {@link IdadeLimiteType }
     *     
     */
    public void setIdadeMaximaPermitida(IdadeLimiteType value) {
        this.idadeMaximaPermitida = value;
    }

    /**
     * Obtém o valor da propriedade pontos.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPontos() {
        return pontos;
    }

    /**
     * Define o valor da propriedade pontos.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPontos(Integer value) {
        this.pontos = value;
    }

    /**
     * Obtém o valor da propriedade valorSH.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorSH() {
        return valorSH;
    }

    /**
     * Define o valor da propriedade valorSH.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorSH(BigDecimal value) {
        this.valorSH = value;
    }

    /**
     * Obtém o valor da propriedade valorSA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorSA() {
        return valorSA;
    }

    /**
     * Define o valor da propriedade valorSA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorSA(BigDecimal value) {
        this.valorSA = value;
    }

    /**
     * Obtém o valor da propriedade valorSP.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorSP() {
        return valorSP;
    }

    /**
     * Define o valor da propriedade valorSP.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorSP(BigDecimal value) {
        this.valorSP = value;
    }

    /**
     * Obtém o valor da propriedade atributosComplementares.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.AtributosComplementares }
     *     
     */
    public ProcedimentoType.AtributosComplementares getAtributosComplementares() {
        return atributosComplementares;
    }

    /**
     * Define o valor da propriedade atributosComplementares.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.AtributosComplementares }
     *     
     */
    public void setAtributosComplementares(ProcedimentoType.AtributosComplementares value) {
        this.atributosComplementares = value;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

    /**
     * Obtém o valor da propriedade ciDsVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.CIDsVinculados }
     *     
     */
    public ProcedimentoType.CIDsVinculados getCIDsVinculados() {
        return ciDsVinculados;
    }

    /**
     * Define o valor da propriedade ciDsVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.CIDsVinculados }
     *     
     */
    public void setCIDsVinculados(ProcedimentoType.CIDsVinculados value) {
        this.ciDsVinculados = value;
    }

    /**
     * Obtém o valor da propriedade cbOsVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.CBOsVinculados }
     *     
     */
    public ProcedimentoType.CBOsVinculados getCBOsVinculados() {
        return cbOsVinculados;
    }

    /**
     * Define o valor da propriedade cbOsVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.CBOsVinculados }
     *     
     */
    public void setCBOsVinculados(ProcedimentoType.CBOsVinculados value) {
        this.cbOsVinculados = value;
    }

    /**
     * Obtém o valor da propriedade categoriasCBOVinculadas.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.CategoriasCBOVinculadas }
     *     
     */
    public ProcedimentoType.CategoriasCBOVinculadas getCategoriasCBOVinculadas() {
        return categoriasCBOVinculadas;
    }

    /**
     * Define o valor da propriedade categoriasCBOVinculadas.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.CategoriasCBOVinculadas }
     *     
     */
    public void setCategoriasCBOVinculadas(ProcedimentoType.CategoriasCBOVinculadas value) {
        this.categoriasCBOVinculadas = value;
    }

    /**
     * Obtém o valor da propriedade tiposLeitoVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.TiposLeitoVinculados }
     *     
     */
    public ProcedimentoType.TiposLeitoVinculados getTiposLeitoVinculados() {
        return tiposLeitoVinculados;
    }

    /**
     * Define o valor da propriedade tiposLeitoVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.TiposLeitoVinculados }
     *     
     */
    public void setTiposLeitoVinculados(ProcedimentoType.TiposLeitoVinculados value) {
        this.tiposLeitoVinculados = value;
    }

    /**
     * Obtém o valor da propriedade servicosClassificacoesVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.ServicosClassificacoesVinculados }
     *     
     */
    public ProcedimentoType.ServicosClassificacoesVinculados getServicosClassificacoesVinculados() {
        return servicosClassificacoesVinculados;
    }

    /**
     * Define o valor da propriedade servicosClassificacoesVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.ServicosClassificacoesVinculados }
     *     
     */
    public void setServicosClassificacoesVinculados(ProcedimentoType.ServicosClassificacoesVinculados value) {
        this.servicosClassificacoesVinculados = value;
    }

    /**
     * Obtém o valor da propriedade habilitacoesVinculadas.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.HabilitacoesVinculadas }
     *     
     */
    public ProcedimentoType.HabilitacoesVinculadas getHabilitacoesVinculadas() {
        return habilitacoesVinculadas;
    }

    /**
     * Define o valor da propriedade habilitacoesVinculadas.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.HabilitacoesVinculadas }
     *     
     */
    public void setHabilitacoesVinculadas(ProcedimentoType.HabilitacoesVinculadas value) {
        this.habilitacoesVinculadas = value;
    }

    /**
     * Obtém o valor da propriedade gruposHabilitacaoVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.GruposHabilitacaoVinculados }
     *     
     */
    public ProcedimentoType.GruposHabilitacaoVinculados getGruposHabilitacaoVinculados() {
        return gruposHabilitacaoVinculados;
    }

    /**
     * Define o valor da propriedade gruposHabilitacaoVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.GruposHabilitacaoVinculados }
     *     
     */
    public void setGruposHabilitacaoVinculados(ProcedimentoType.GruposHabilitacaoVinculados value) {
        this.gruposHabilitacaoVinculados = value;
    }

    /**
     * Obtém o valor da propriedade incrementosVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.IncrementosVinculados }
     *     
     */
    public ProcedimentoType.IncrementosVinculados getIncrementosVinculados() {
        return incrementosVinculados;
    }

    /**
     * Define o valor da propriedade incrementosVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.IncrementosVinculados }
     *     
     */
    public void setIncrementosVinculados(ProcedimentoType.IncrementosVinculados value) {
        this.incrementosVinculados = value;
    }

    /**
     * Obtém o valor da propriedade componentesRedesVinculados.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.ComponentesRedesVinculados }
     *     
     */
    public ProcedimentoType.ComponentesRedesVinculados getComponentesRedesVinculados() {
        return componentesRedesVinculados;
    }

    /**
     * Define o valor da propriedade componentesRedesVinculados.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.ComponentesRedesVinculados }
     *     
     */
    public void setComponentesRedesVinculados(ProcedimentoType.ComponentesRedesVinculados value) {
        this.componentesRedesVinculados = value;
    }

    /**
     * Obtém o valor da propriedade origensSIGTAP.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.OrigensSIGTAP }
     *     
     */
    public ProcedimentoType.OrigensSIGTAP getOrigensSIGTAP() {
        return origensSIGTAP;
    }

    /**
     * Define o valor da propriedade origensSIGTAP.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.OrigensSIGTAP }
     *     
     */
    public void setOrigensSIGTAP(ProcedimentoType.OrigensSIGTAP value) {
        this.origensSIGTAP = value;
    }

    /**
     * Obtém o valor da propriedade origensSIASIH.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.OrigensSIASIH }
     *     
     */
    public ProcedimentoType.OrigensSIASIH getOrigensSIASIH() {
        return origensSIASIH;
    }

    /**
     * Define o valor da propriedade origensSIASIH.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.OrigensSIASIH }
     *     
     */
    public void setOrigensSIASIH(ProcedimentoType.OrigensSIASIH value) {
        this.origensSIASIH = value;
    }

    /**
     * Obtém o valor da propriedade regrasCondicionadasVinculadas.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.RegrasCondicionadasVinculadas }
     *     
     */
    public ProcedimentoType.RegrasCondicionadasVinculadas getRegrasCondicionadasVinculadas() {
        return regrasCondicionadasVinculadas;
    }

    /**
     * Define o valor da propriedade regrasCondicionadasVinculadas.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.RegrasCondicionadasVinculadas }
     *     
     */
    public void setRegrasCondicionadasVinculadas(ProcedimentoType.RegrasCondicionadasVinculadas value) {
        this.regrasCondicionadasVinculadas = value;
    }

    /**
     * Obtém o valor da propriedade renasesVinculadas.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.RENASESVinculadas }
     *     
     */
    public ProcedimentoType.RENASESVinculadas getRENASESVinculadas() {
        return renasesVinculadas;
    }

    /**
     * Define o valor da propriedade renasesVinculadas.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.RENASESVinculadas }
     *     
     */
    public void setRENASESVinculadas(ProcedimentoType.RENASESVinculadas value) {
        this.renasesVinculadas = value;
    }

    /**
     * Obtém o valor da propriedade tussVinculadas.
     * 
     * @return
     *     possible object is
     *     {@link ProcedimentoType.TUSSVinculadas }
     *     
     */
    public ProcedimentoType.TUSSVinculadas getTUSSVinculadas() {
        return tussVinculadas;
    }

    /**
     * Define o valor da propriedade tussVinculadas.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcedimentoType.TUSSVinculadas }
     *     
     */
    public void setTUSSVinculadas(ProcedimentoType.TUSSVinculadas value) {
        this.tussVinculadas = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="AtributoComplementar" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/atributocomplementar}AtributoComplementarType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "atributoComplementar"
    })
    public static class AtributosComplementares {

        @XmlElement(name = "AtributoComplementar")
        protected List<AtributoComplementarType> atributoComplementar;

        /**
         * Gets the value of the atributoComplementar property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the atributoComplementar property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAtributoComplementar().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AtributoComplementarType }
         * 
         * 
         */
        public List<AtributoComplementarType> getAtributoComplementar() {
            if (atributoComplementar == null) {
                atributoComplementar = new ArrayList<AtributoComplementarType>();
            }
            return this.atributoComplementar;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="CBO" type="{http://servicos.saude.gov.br/schema/cbo/v1/cbo}CBOType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cbo"
    })
    public static class CBOsVinculados {

        @XmlElement(name = "CBO")
        protected List<CBOType> cbo;

        /**
         * Gets the value of the cbo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cbo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCBO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CBOType }
         * 
         * 
         */
        public List<CBOType> getCBO() {
            if (cbo == null) {
                cbo = new ArrayList<CBOType>();
            }
            return this.cbo;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="CIDVinculado" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="CID" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/cid}CIDType" minOccurs="0"/&gt;
     *                   &lt;element name="vinculo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/vinculocid}VinculoCIDType" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cidVinculado"
    })
    public static class CIDsVinculados {

        @XmlElement(name = "CIDVinculado")
        protected List<ProcedimentoType.CIDsVinculados.CIDVinculado> cidVinculado;

        /**
         * Gets the value of the cidVinculado property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cidVinculado property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCIDVinculado().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProcedimentoType.CIDsVinculados.CIDVinculado }
         * 
         * 
         */
        public List<ProcedimentoType.CIDsVinculados.CIDVinculado> getCIDVinculado() {
            if (cidVinculado == null) {
                cidVinculado = new ArrayList<ProcedimentoType.CIDsVinculados.CIDVinculado>();
            }
            return this.cidVinculado;
        }


        /**
         * <p>Classe Java de anonymous complex type.
         * 
         * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="CID" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/cid}CIDType" minOccurs="0"/&gt;
         *         &lt;element name="vinculo" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/cid/v1/vinculocid}VinculoCIDType" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "cid",
            "vinculo"
        })
        public static class CIDVinculado {

            @XmlElement(name = "CID")
            protected CIDType cid;
            @XmlSchemaType(name = "string")
            protected VinculoCIDType vinculo;

            /**
             * Obtém o valor da propriedade cid.
             * 
             * @return
             *     possible object is
             *     {@link CIDType }
             *     
             */
            public CIDType getCID() {
                return cid;
            }

            /**
             * Define o valor da propriedade cid.
             * 
             * @param value
             *     allowed object is
             *     {@link CIDType }
             *     
             */
            public void setCID(CIDType value) {
                this.cid = value;
            }

            /**
             * Obtém o valor da propriedade vinculo.
             * 
             * @return
             *     possible object is
             *     {@link VinculoCIDType }
             *     
             */
            public VinculoCIDType getVinculo() {
                return vinculo;
            }

            /**
             * Define o valor da propriedade vinculo.
             * 
             * @param value
             *     allowed object is
             *     {@link VinculoCIDType }
             *     
             */
            public void setVinculo(VinculoCIDType value) {
                this.vinculo = value;
            }

        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="CategoriaCBO" type="{http://servicos.saude.gov.br/schema/cbo/v1/categoriacbo}CategoriaCBOType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "categoriaCBO"
    })
    public static class CategoriasCBOVinculadas {

        @XmlElement(name = "CategoriaCBO")
        protected List<CategoriaCBOType> categoriaCBO;

        /**
         * Gets the value of the categoriaCBO property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the categoriaCBO property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategoriaCBO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CategoriaCBOType }
         * 
         * 
         */
        public List<CategoriaCBOType> getCategoriaCBO() {
            if (categoriaCBO == null) {
                categoriaCBO = new ArrayList<CategoriaCBOType>();
            }
            return this.categoriaCBO;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="ComponenteRedeAtencao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/redeatencao/v1/componenteredeatencao}ComponenteRedeAtencaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "componenteRedeAtencao"
    })
    public static class ComponentesRedesVinculados {

        @XmlElement(name = "ComponenteRedeAtencao")
        protected List<ComponenteRedeAtencaoType> componenteRedeAtencao;

        /**
         * Gets the value of the componenteRedeAtencao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the componenteRedeAtencao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getComponenteRedeAtencao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ComponenteRedeAtencaoType }
         * 
         * 
         */
        public List<ComponenteRedeAtencaoType> getComponenteRedeAtencao() {
            if (componenteRedeAtencao == null) {
                componenteRedeAtencao = new ArrayList<ComponenteRedeAtencaoType>();
            }
            return this.componenteRedeAtencao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="GrupoHabilitacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/grupohabilitacao}GrupoHabilitacaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "grupoHabilitacao"
    })
    public static class GruposHabilitacaoVinculados {

        @XmlElement(name = "GrupoHabilitacao")
        protected List<GrupoHabilitacaoType> grupoHabilitacao;

        /**
         * Gets the value of the grupoHabilitacao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the grupoHabilitacao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGrupoHabilitacao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GrupoHabilitacaoType }
         * 
         * 
         */
        public List<GrupoHabilitacaoType> getGrupoHabilitacao() {
            if (grupoHabilitacao == null) {
                grupoHabilitacao = new ArrayList<GrupoHabilitacaoType>();
            }
            return this.grupoHabilitacao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Habilitacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/habilitacao}HabilitacaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "habilitacao"
    })
    public static class HabilitacoesVinculadas {

        @XmlElement(name = "Habilitacao")
        protected List<HabilitacaoType> habilitacao;

        /**
         * Gets the value of the habilitacao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the habilitacao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getHabilitacao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link HabilitacaoType }
         * 
         * 
         */
        public List<HabilitacaoType> getHabilitacao() {
            if (habilitacao == null) {
                habilitacao = new ArrayList<HabilitacaoType>();
            }
            return this.habilitacao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="Incremento" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/habilitacao/v1/incremento}IncrementoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "incremento"
    })
    public static class IncrementosVinculados {

        @XmlElement(name = "Incremento")
        protected List<IncrementoType> incremento;

        /**
         * Gets the value of the incremento property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the incremento property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIncremento().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link IncrementoType }
         * 
         * 
         */
        public List<IncrementoType> getIncremento() {
            if (incremento == null) {
                incremento = new ArrayList<IncrementoType>();
            }
            return this.incremento;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="InstrumentoRegistro" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/instrumentoregistro}InstrumentoRegistroType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "instrumentoRegistro"
    })
    public static class InstrumentosRegistro {

        @XmlElement(name = "InstrumentoRegistro")
        protected List<InstrumentoRegistroType> instrumentoRegistro;

        /**
         * Gets the value of the instrumentoRegistro property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the instrumentoRegistro property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInstrumentoRegistro().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InstrumentoRegistroType }
         * 
         * 
         */
        public List<InstrumentoRegistroType> getInstrumentoRegistro() {
            if (instrumentoRegistro == null) {
                instrumentoRegistro = new ArrayList<InstrumentoRegistroType>();
            }
            return this.instrumentoRegistro;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="ModalidadeAtendimento" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/modalidadeatendimento}ModalidadeAtendimentoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "modalidadeAtendimento"
    })
    public static class ModalidadesAtendimento {

        @XmlElement(name = "ModalidadeAtendimento")
        protected List<ModalidadeAtendimentoType> modalidadeAtendimento;

        /**
         * Gets the value of the modalidadeAtendimento property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the modalidadeAtendimento property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getModalidadeAtendimento().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ModalidadeAtendimentoType }
         * 
         * 
         */
        public List<ModalidadeAtendimentoType> getModalidadeAtendimento() {
            if (modalidadeAtendimento == null) {
                modalidadeAtendimento = new ArrayList<ModalidadeAtendimentoType>();
            }
            return this.modalidadeAtendimento;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="OrigemSIASIH" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/origem/v1/origemsiasih}OrigemSIASIHType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "origemSIASIH"
    })
    public static class OrigensSIASIH {

        @XmlElement(name = "OrigemSIASIH")
        protected List<OrigemSIASIHType> origemSIASIH;

        /**
         * Gets the value of the origemSIASIH property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the origemSIASIH property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOrigemSIASIH().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OrigemSIASIHType }
         * 
         * 
         */
        public List<OrigemSIASIHType> getOrigemSIASIH() {
            if (origemSIASIH == null) {
                origemSIASIH = new ArrayList<OrigemSIASIHType>();
            }
            return this.origemSIASIH;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="OrigemSIGTAP" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/v1/procedimento}BaseProcedimentoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "origemSIGTAP"
    })
    public static class OrigensSIGTAP {

        @XmlElement(name = "OrigemSIGTAP")
        protected List<BaseProcedimentoType> origemSIGTAP;

        /**
         * Gets the value of the origemSIGTAP property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the origemSIGTAP property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOrigemSIGTAP().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BaseProcedimentoType }
         * 
         * 
         */
        public List<BaseProcedimentoType> getOrigemSIGTAP() {
            if (origemSIGTAP == null) {
                origemSIGTAP = new ArrayList<BaseProcedimentoType>();
            }
            return this.origemSIGTAP;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="RENASES" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/renases/v1/renases}RENASESType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "renases"
    })
    public static class RENASESVinculadas {

        @XmlElement(name = "RENASES")
        protected List<RENASESType> renases;

        /**
         * Gets the value of the renases property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the renases property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRENASES().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RENASESType }
         * 
         * 
         */
        public List<RENASESType> getRENASES() {
            if (renases == null) {
                renases = new ArrayList<RENASESType>();
            }
            return this.renases;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="RegraCondicionada" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/regracondicionada/v1/regracondicionada}RegraCondicionadaType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "regraCondicionada"
    })
    public static class RegrasCondicionadasVinculadas {

        @XmlElement(name = "RegraCondicionada")
        protected List<RegraCondicionadaType> regraCondicionada;

        /**
         * Gets the value of the regraCondicionada property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the regraCondicionada property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRegraCondicionada().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RegraCondicionadaType }
         * 
         * 
         */
        public List<RegraCondicionadaType> getRegraCondicionada() {
            if (regraCondicionada == null) {
                regraCondicionada = new ArrayList<RegraCondicionadaType>();
            }
            return this.regraCondicionada;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="ServicoClassificacao" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/servicoclassificacao/v1/servicoclassificacao}ServicoClassificacaoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "servicoClassificacao"
    })
    public static class ServicosClassificacoesVinculados {

        @XmlElement(name = "ServicoClassificacao")
        protected List<ServicoClassificacaoType> servicoClassificacao;

        /**
         * Gets the value of the servicoClassificacao property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the servicoClassificacao property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getServicoClassificacao().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ServicoClassificacaoType }
         * 
         * 
         */
        public List<ServicoClassificacaoType> getServicoClassificacao() {
            if (servicoClassificacao == null) {
                servicoClassificacao = new ArrayList<ServicoClassificacaoType>();
            }
            return this.servicoClassificacao;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="TUSS" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/tuss/v1/tuss}TUSSType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tuss"
    })
    public static class TUSSVinculadas {

        @XmlElement(name = "TUSS")
        protected List<TUSSType> tuss;

        /**
         * Gets the value of the tuss property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tuss property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTUSS().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TUSSType }
         * 
         * 
         */
        public List<TUSSType> getTUSS() {
            if (tuss == null) {
                tuss = new ArrayList<TUSSType>();
            }
            return this.tuss;
        }

    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="TipoLeito" type="{http://servicos.saude.gov.br/schema/sigtap/procedimento/tipoleito/v1/tipoleito}TipoLeitoType" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoLeito"
    })
    public static class TiposLeitoVinculados {

        @XmlElement(name = "TipoLeito")
        protected List<TipoLeitoType> tipoLeito;

        /**
         * Gets the value of the tipoLeito property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tipoLeito property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTipoLeito().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TipoLeitoType }
         * 
         * 
         */
        public List<TipoLeitoType> getTipoLeito() {
            if (tipoLeito == null) {
                tipoLeito = new ArrayList<TipoLeitoType>();
            }
            return this.tipoLeito;
        }

    }

}
