
package sigtap.br.gov.saude.servicos.sigtap.v1.procedimentoservice;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.3.5
 * 2020-03-18T11:58:00.978-03:00
 * Generated source version: 3.3.5
 */

@WebFault(name = "MSFalha", targetNamespace = "http://servicos.saude.gov.br/wsdl/mensageria/falha/v5r0/msfalha")
public class SIGTAPFault extends Exception {

    private sigtap.br.gov.saude.servicos.wsdl.mensageria.falha.v5r0.msfalha.MSFalha msFalha;

    public SIGTAPFault() {
        super();
    }

    public SIGTAPFault(String message) {
        super(message);
    }

    public SIGTAPFault(String message, java.lang.Throwable cause) {
        super(message, cause);
    }

    public SIGTAPFault(String message, sigtap.br.gov.saude.servicos.wsdl.mensageria.falha.v5r0.msfalha.MSFalha msFalha) {
        super(message);
        this.msFalha = msFalha;
    }

    public SIGTAPFault(String message, sigtap.br.gov.saude.servicos.wsdl.mensageria.falha.v5r0.msfalha.MSFalha msFalha, java.lang.Throwable cause) {
        super(message, cause);
        this.msFalha = msFalha;
    }

    public sigtap.br.gov.saude.servicos.wsdl.mensageria.falha.v5r0.msfalha.MSFalha getFaultInfo() {
        return this.msFalha;
    }
}
