package org.hawkular.qe.rest.test.inventory;

import java.util.List;

import org.hawkular.inventory.api.model.Environment;
import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class InventoryTestBase extends HawkularRestTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(InventoryTestBase.class);

    public void assertTenantsList(List<Tenant> tListOne, List<Tenant> tListTwo) {
        Assert.assertNotNull(tListOne);
        Assert.assertNotNull(tListTwo);
        Assert.assertEquals(tListOne.size(), tListTwo.size());
        for (int loc = 0; loc < tListOne.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < tListOne.size(); locAnother++) {
                if (tListOne.get(loc).getId().equals(tListTwo.get(locAnother).getId())) {
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Tenant:{}, Found?:{}", tListOne.get(loc).getId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertEnvironmentsList(List<Environment> eListOne, List<Environment> eListTwo) {
        Assert.assertNotNull(eListOne);
        Assert.assertNotNull(eListTwo);
        Assert.assertEquals(eListOne.size(), eListTwo.size());
        for (int loc = 0; loc < eListOne.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < eListOne.size(); locAnother++) {
                if (eListOne.get(loc).getId().equals(eListTwo.get(locAnother).getId())) {
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Environment:{} under the Tenant:{}, Found?:{}",
                    eListOne.get(loc).getId(), eListOne.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertMetricTypesList(List<MetricType> mtListOne, List<MetricType> mtListTwo) {
        Assert.assertNotNull(mtListOne);
        Assert.assertNotNull(mtListTwo);
        Assert.assertEquals(mtListOne.size(), mtListTwo.size());
        for (int loc = 0; loc < mtListOne.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < mtListOne.size(); locAnother++) {
                if (mtListOne.get(loc).getId().equals(mtListTwo.get(locAnother).getId())) {
                    Assert.assertEquals(mtListOne.get(loc).getUnit().getDisplayName(),
                            mtListTwo.get(locAnother).getUnit().getDisplayName());
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the MetricType:{} under the Tenant:{}, Found?:{}",
                    mtListOne.get(loc).getId(), mtListOne.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }
}