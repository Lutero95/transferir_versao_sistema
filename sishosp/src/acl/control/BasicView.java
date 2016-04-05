package br.gov.al.maceio.sishosp.acl.control;
 
import br.gov.al.maceio.sishosp.acl.model.Menu;
import br.gov.al.maceio.sishosp.acl.model.Rotina;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
 
@ManagedBean(name="treeBasicView")
@ViewScoped
public class BasicView implements Serializable {
     
    private TreeNode root;
    private Rotina rotina;
    
    private TreeNode[] selectedNodes;
     
    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        rotina = new Rotina();
        
        Rotina r = new Rotina();
        r.setId(Long.valueOf("1"));
        r.setDescricao("Rotina 1");
        r.setDescSistema("Protocolo");
        
        Menu m1 = new Menu();
        m1.setId(Long.valueOf("1"));
        m1.setDescricao("Menu 1");
        
        Menu m2 = new Menu();
        m2.setId(Long.valueOf("2"));
        m2.setDescricao("Menu 2");

        TreeNode node0 = new DefaultTreeNode("rot" ,new Rotina("Rotina 1", null), root);
        
        TreeNode node1 = new DefaultTreeNode("menu", m1, node0);
        TreeNode node2 = new DefaultTreeNode("menu", m2, node0);
    }
    
    public void displaySelectedMultiple(TreeNode[] nodes) {
        if(nodes != null && nodes.length > 0) {
            StringBuilder builder = new StringBuilder();
 
            for(TreeNode node : nodes) {
                builder.append(node.getData().toString());
                builder.append("<br />");
            }
            
            ////System.out.println("sl - " + builder.toString());
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, builder.toString(), "Selecionado");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
 
    public TreeNode getRoot() {
        root.setExpanded(true);
        
        return root;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }
}