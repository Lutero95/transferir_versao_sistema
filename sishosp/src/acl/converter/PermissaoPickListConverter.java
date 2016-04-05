package br.gov.al.maceio.sishosp.acl.converter;

import br.gov.al.maceio.sishosp.acl.model.Permissao;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Arthur Alves, Emerson Gama & Jerônimo do Nascimento 
 * @since 17/03/2015
 */
@FacesConverter(value = "conPickListPermissao")
public class PermissaoPickListConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        Object ret = null;
        if (arg1 instanceof PickList) {
            Object dualList = ((ValueHolder) arg1).getValue();
            DualListModel dl = (DualListModel) dualList;
            for (Iterator iterator = dl.getSource().iterator(); iterator.hasNext();) {
                Object o = iterator.next();
                String id = (new StringBuilder()).append(((Permissao) o).getId()).toString();
                if (arg2.equals(id)) {
                    ret = o;
                    break;
                }
            }

            if (ret == null) {
                for (Iterator iterator1 = dl.getTarget().iterator(); iterator1.hasNext();) {
                    Object o = iterator1.next();
                    String id = (new StringBuilder()).append(((Permissao) o).getId()).toString();
                    if (arg2.equals(id)) {
                        ret = o;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String str = "";
        if (arg2 instanceof Permissao) {
            str = ((Permissao) arg2).getId().toString();
        }
        return str;
    }
}