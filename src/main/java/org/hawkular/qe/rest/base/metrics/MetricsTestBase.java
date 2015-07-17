package org.hawkular.qe.rest.base.metrics;

import java.util.List;

import org.hawkular.client.metrics.model.TenantParam;
import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.testng.Assert;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class MetricsTestBase extends HawkularRestTestBase {

    public void assertTenantsList(List<TenantParam> actual, List<TenantParam> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    assertTenants(actual.get(loc), expected.get(locAnother));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Tenant:{}, Found?:{}", actual.get(loc).getId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertTenants(TenantParam actual, TenantParam expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
    }

}
