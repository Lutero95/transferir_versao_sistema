package br.gov.al.maceio.sishosp.acl.control;

import br.gov.al.maceio.sishosp.acl.model.Rotina;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ViewScoped
public class ConsultaCategoriasBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Rotina rotina;
    
    private TreeNode raiz;

    public void consultar() {
        List<Rotina> categoriasRaizes = new ArrayList<>();

        this.raiz = new DefaultTreeNode("Raiz", null);

        adicionarNos(categoriasRaizes, this.raiz);
    }

    private void adicionarNos(List<Rotina> rotinas, TreeNode pai) {
        for (Rotina r : rotinas) {
            TreeNode no = new DefaultTreeNode(r, pai);

            //adicionarNos(rotina.getListaFuncoesAss(), no);
        }
    }

    public TreeNode getRaiz() {
        return raiz;
    }
}
