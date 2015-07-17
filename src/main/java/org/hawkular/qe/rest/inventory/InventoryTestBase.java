package org.hawkular.qe.rest.inventory;

import java.util.List;

import org.hawkular.inventory.api.model.Environment;
import org.hawkular.inventory.api.model.Feed;
import org.hawkular.inventory.api.model.Metric;
import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.Resource;
import org.hawkular.inventory.api.model.ResourceType;
import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class InventoryTestBase extends HawkularRestTestBase {
    public static final Logger _logger = LoggerFactory.getLogger(InventoryTestBase.class);
    public static Tenant TENANT = null;

    @BeforeClass
    public void loadTenant() {
        if (TENANT == null) {
            TENANT = getHawkularClient().inventory().getTenant().getEntity();
        }
    }

    public void assertTenantsList(List<Tenant> actual, List<Tenant> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Tenant:{}, Found?:{}", actual.get(loc).getId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertEnvironmentsList(List<Environment> actual, List<Environment> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Environment:{} under the Tenant:{}, Found?:{}",
                    actual.get(loc).getId(), actual.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertEnvironments(Environment actual, Environment expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getTenantId(), expected.getTenantId());
    }

    public void assertMetricTypes(MetricType actual, MetricType expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getTenantId(), expected.getTenantId());
        Assert.assertEquals(actual.getUnit(), expected.getUnit());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
    }

    public void assertMetricTypesList(List<MetricType> actual, List<MetricType> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    assertMetricTypes(actual.get(loc), expected.get(locAnother));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the MetricType:{} under the Tenant:{}, Found?:{}",
                    actual.get(loc).getId(), actual.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertMetrics(Metric actual, Metric expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getTenantId(), expected.getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getEnvironmentId(), expected.getEnvironmentId());
        assertMetricTypes(actual.getType(), expected.getType());
    }

    public void assertMetricsList(List<Metric> actual, List<Metric> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    assertMetrics(actual.get(loc), expected.get(locAnother));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Metric:{} under the Tenant:{}, Found?:{}",
                    actual.get(loc).getId(), actual.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertResourceTypes(ResourceType actual, ResourceType expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getTenantId(), expected.getTenantId());
        Assert.assertEquals(actual.getVersion(), expected.getVersion());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
    }

    public void assertResourceTypesList(List<ResourceType> actual, List<ResourceType> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    assertResourceTypes(actual.get(loc), expected.get(locAnother));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the ResourceType:{} under the Tenant:{}, Found?:{}",
                    actual.get(loc).getId(), actual.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertResources(Resource actual, Resource expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getTenantId(), expected.getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getEnvironmentId(), expected.getEnvironmentId());
        assertResourceTypes(actual.getType(), expected.getType());
    }

    public void assertResourcesList(List<Resource> actual, List<Resource> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    assertResources(actual.get(loc), expected.get(locAnother));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Resource:{} under the Tenant:{}, Found?:{}",
                    actual.get(loc).getId(), actual.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertFeeds(Feed actual, Feed expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getTenantId(), expected.getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getEnvironmentId(), expected.getEnvironmentId());
    }

    public void assertFeedsList(List<Feed> actual, List<Feed> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.size(), expected.size());
        for (int loc = 0; loc < actual.size(); loc++) {
            boolean found = false;
            for (int locAnother = 0; locAnother < actual.size(); locAnother++) {
                if (actual.get(loc).getId().equals(expected.get(locAnother).getId())) {
                    assertFeeds(actual.get(loc), expected.get(locAnother));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Feed:{} under the Tenant:{}, Found?:{}",
                    actual.get(loc).getId(), actual.get(loc).getTenantId(), found);
            Assert.assertTrue(found);
        }
    }
}