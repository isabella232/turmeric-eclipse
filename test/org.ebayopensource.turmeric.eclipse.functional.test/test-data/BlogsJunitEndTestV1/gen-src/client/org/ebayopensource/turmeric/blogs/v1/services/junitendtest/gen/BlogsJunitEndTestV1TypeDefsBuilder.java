
package org.ebayopensource.turmeric.blogs.v1.services.junitendtest.gen;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.BaseTypeDefsBuilder;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.FlatSchemaComplexTypeImpl;
import org.ebayopensource.turmeric.runtime.common.impl.internal.schema.FlatSchemaElementDeclImpl;
import org.ebayopensource.turmeric.runtime.common.types.SOAFrameworkCommonTypeDefsBuilder;


/**
 * Note : Generated file, any changes will be lost upon regeneration.
 * 
 */
public class BlogsJunitEndTestV1TypeDefsBuilder
    extends BaseTypeDefsBuilder
{

    private final static String NS1 = "http://www.ebayopensource.org/turmeric/blogs/v1/services";

    public void build() {
        ArrayList<FlatSchemaComplexTypeImpl> complexTypes = new ArrayList<FlatSchemaComplexTypeImpl>();
        addComplexTypes0(complexTypes);
         
        addComplexTypeElements0(complexTypes);
         
        HashMap<QName, FlatSchemaElementDeclImpl> rootElements = new HashMap<QName, FlatSchemaElementDeclImpl>();
        addRootElements0(complexTypes, rootElements);
         
        SOAFrameworkCommonTypeDefsBuilder.includeTypeDefs(complexTypes, rootElements);
         
        m_complexTypes = complexTypes;
        m_rootElements = rootElements;
    }

    private void addComplexTypes0(ArrayList<FlatSchemaComplexTypeImpl> complexTypes) {
        // Type #0 (<Anonymous>)
        complexTypes.add(new FlatSchemaComplexTypeImpl());
        // Type #1 (<Anonymous>)
        complexTypes.add(new FlatSchemaComplexTypeImpl());
    }

    private void addComplexTypeElements0(ArrayList<FlatSchemaComplexTypeImpl> complexTypes) {
        FlatSchemaComplexTypeImpl currType;
         
        // Type #0 (<Anonymous>)
        currType = complexTypes.get(0);
        currType.addSimpleElement(new QName(NS1, "return"), 1);
         
        // Type #1 (<Anonymous>)
        currType = complexTypes.get(1);
        currType.addSimpleElement(new QName(NS1, "value1"), 1);
        currType.addSimpleElement(new QName(NS1, "value2"), 1);
    }

    private void addRootElements0(ArrayList<FlatSchemaComplexTypeImpl> complexTypes, HashMap<QName, FlatSchemaElementDeclImpl> rootElements) {
        rootElements.put(new QName(NS1, "Inparams"), FlatSchemaElementDeclImpl.createRootComplexElement(new QName(NS1, "Inparams"), complexTypes.get(1)));
        rootElements.put(new QName(NS1, "Response"), FlatSchemaElementDeclImpl.createRootComplexElement(new QName(NS1, "Response"), complexTypes.get(0)));
    }

}
