package org.hawkular.qe.rest.test.inventory;

import java.util.List;

import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.testng.Assert;
/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class InventoryTestBase extends HawkularRestTestBase {

    public void assertTenantsList(List<Tenant> tListOne, List<Tenant> tListTwo){
        Assert.assertNotNull(tListOne);
        Assert.assertNotNull(tListTwo);
        Assert.assertEquals(tListOne.size(), tListTwo.size());
        for(int loc=0;loc<tListOne.size();loc++){
            boolean found = false;
            for(int locAnother=0;locAnother<tListOne.size();locAnother++){
                if(tListOne.get(loc).getId().equals(tListTwo.get(loc).getId())){
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }
}