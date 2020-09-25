package br.gov.al.maceio.sishosp.hosp.enums;

public enum MesExtensaoArquivoBPA {

    JANEIRO("01", ".JAN"),
    FEVEREIRO("02", ".FEV"),
    MARCO("03", ".MAR"),
    ABRIL("04", ".ABR"),
    MAIO("05", ".MAI"),
    JUNHO("06", ".JUN"),
    JULHO("07", ".JUL"),
    AGOSTO("08", ".AGO"),
    SETEMBRO("09", ".SET"),
    OUTUBRO("10", ".OUT"),
    NOVEMBRO("11", ".NOV"),
    DEZEMBRO("12", ".DEZ");

    private String sigla;
    private String extensao;

    MesExtensaoArquivoBPA(String sigla, String extensao) {
        this.sigla = sigla;
        this.extensao = extensao;
    }

    public String getSigla() {
        return sigla;
    }

	public String getExtensao() {
		return extensao;
	}
}
