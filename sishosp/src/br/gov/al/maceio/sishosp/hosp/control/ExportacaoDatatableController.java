package br.gov.al.maceio.sishosp.hosp.control;

import com.lowagie.text.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "exportacaoMB")
@SessionScoped
public class ExportacaoDatatableController {
    public void preProcessLaudo(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Laudos");
    }

    public void preProcessCredores(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Despesas por Credores");
    }

    public void preProcessDespesasPorOrgao(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Despesas por Órgão");
    }

    public void preProcessDiarias(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Despesas por Diárias");
    }

    public void preProcessProgramas(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Despesas por Programas");
    }

    public void preProcessQDD(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Despesas QDD");
    }

    public void preProcessRestosAPagarResumido(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Restos a Pagar Resumido");
    }

    public void preProcessRestosAPagarNaoResumido(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Restos a Pagar");
    }

    public void preProcessPlanejamentoOrcamentario(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Planejamento Orçamentário");
    }

    public void preProcessBalancete(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Receita Balancete");
    }

    public void preProcessTransferenciaUniao(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Receita Transferencias da União");
    }

    public void preProcessTransferenciaEstado(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Receita Transferencias do Estado");
    }

    public void preProcessReceitaPorOrgao(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Receita por Órgão");
    }

    public void preProcessBolsaFamilia(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Bolsa Família");
    }

    public void preProcessContratos(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Contratos");
    }

    public void preProcessConvenio(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Convênios");
    }

    public void preProcessDemonstrativosContabeis(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Demonstrativos Contábeis");
    }

    public void preProcessLRF(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de LRF");
    }

    public void preProcessLegislacao(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Legislação");
    }

    public void preProcessObras(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Obras");
    }

    public void preProcessTransferenciaUniaoCovid(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Transferencias da União COVID-19");
    }

    public void preProcessDespesasCovid(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Despesas COVID-19");
    }

    public void preProcessListagemEmpenho(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Empenho COVID-19");
    }


    public void preProcessListagemLicitacaoCovid(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Licitações COVID-19");
    }

    public void preProcessListagemEmpenhoLiquidado(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Empenho Liquidado COVID-19");
    }

    public void preProcessListagemEmpenhoPago(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Empenho Pago COVID-19");
    }

    public void preProcessListagemContratosCovid(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Contratos COVID-19");
    }

    public void preProcessListagemAnexosContratos(Object document) throws IOException, DocumentException {
        preProcessPDF(document, "Listagem de Anexos dos Contratos");
    }

    public void preProcessPDF(Object document) throws IOException, DocumentException {
        this.preProcessPDF(document, "");
    }

    public void preProcessPDF(Object document, String textoTitulo) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.setMargins(-50.10f, -50.10f, 20.5f, 50.5f);
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.open();
/*
        Paragraph geradoEm = new Paragraph("Gerado em: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        geradoEm.setAlignment(Element.ALIGN_RIGHT);
        geradoEm.setSpacingAfter(3);
        geradoEm.setIndentationRight(80);

        Paragraph textoSec = new Paragraph("Município de Maceió\n" +
                "Secretaria de Controle Interno");
        textoSec.setAlignment(Element.ALIGN_LEFT);
        textoSec.setKeepTogether(false);
        textoSec.setIndentationLeft(80);
*/
        Paragraph titulo = new Paragraph(textoTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(8);
/*
        Image img = Image.getInstance(FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + File.separator + "imgs" + File.separator + "logo-novo.png");
        img.setAlignment(Image.ALIGN_LEFT);
        img.scalePercent(60);
        img.setIndentationLeft(85);

        pdf.add(img);
        pdf.add(textoSec);
        pdf.add(geradoEm);*/
        pdf.add(titulo);
    }


    public void preProcessPDFHorizontal(Object document) {
        Document doc = (Document) document;
        doc.setPageSize(new RectangleReadOnly(842.0F, 595.0F));
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        HSSFCellStyle cellStyle = wb.createCellStyle();

        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
        }

        HSSFRow row;
        HSSFCellStyle rowStyle = wb.createCellStyle();
       // rowStyle.setAlignment(HorizontalAlignment.RIGHT);

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                HSSFCell cell = row.getCell(j);
                cell.setCellStyle(rowStyle);
            }
        }
    }



    private FacesContext getFacesContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context;
    }

    private ServletContext getServleContext() {
        ServletContext scontext = (ServletContext) this.getFacesContext().getExternalContext().getContext();
        return scontext;
    }

}
