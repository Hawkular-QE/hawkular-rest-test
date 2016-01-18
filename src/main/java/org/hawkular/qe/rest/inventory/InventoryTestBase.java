/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    public static final double DATA_MB = 1024.0 * 1024.0;
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
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Tenant:[{}], Found?:[{}]", expected.get(expectedCount).getId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertEnvironmentsList(List<Environment> actual, List<Environment> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Environment:[{}] under the Tenant:[{}], Found?:[{}]",
                    expected.get(expectedCount).getId(), expected.get(expectedCount).getPath().ids().getTenantId(),
                    found);
            Assert.assertTrue(found);
        }
    }

    public void assertEnvironments(Environment actual, Environment expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getPath().ids().getTenantId(), expected.getPath().ids().getTenantId());
    }

    public void assertMetricTypes(MetricType actual, MetricType expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getPath().ids().getTenantId(), expected.getPath().ids().getTenantId());
        Assert.assertEquals(actual.getUnit(), expected.getUnit());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getCollectionInterval(), expected.getCollectionInterval());
    }

    public void assertMetricTypesList(List<MetricType> actual, List<MetricType> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    assertMetricTypes(actual.get(actualCount), expected.get(expectedCount));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the MetricType:[{}] under the Tenant:[{}], Found?:[{}]",
                    expected.get(expectedCount).getId(), expected.get(expectedCount).getPath().ids().getTenantId(),
                    found);
            Assert.assertTrue(found);
        }
    }

    public void assertMetrics(Metric actual, Metric expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getPath().ids().getTenantId(), expected.getPath().ids().getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getPath().ids().getEnvironmentId(), expected.getPath().ids().getEnvironmentId());
        assertMetricTypes(actual.getType(), expected.getType());
    }

    public void assertMetricsList(List<Metric> actual, List<Metric> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    assertMetrics(actual.get(actualCount), expected.get(expectedCount));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Metric:[{}] under the Tenant:[{}], Found?:[{}]",
                    expected.get(expectedCount).getId(), expected.get(expectedCount).getPath().ids().getTenantId(),
                    found);
            Assert.assertTrue(found);
        }
    }

    public void assertResourceTypes(ResourceType actual, ResourceType expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getPath().ids().getTenantId(), expected.getPath().ids().getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
    }

    public void assertResourceTypesList(List<ResourceType> actual, List<ResourceType> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    assertResourceTypes(actual.get(actualCount), expected.get(expectedCount));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the ResourceType:[{}] under the Tenant:[{}], Found?:[{}]",
                    expected.get(expectedCount).getId(), expected.get(expectedCount).getPath().ids().getTenantId(),
                    found);
            Assert.assertTrue(found);
        }
    }

    public void assertResources(Resource actual, Resource expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getPath().ids().getTenantId(), expected.getPath().ids().getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getPath().ids().getEnvironmentId(), expected.getPath().ids().getEnvironmentId());
        assertResourceTypes(actual.getType(), expected.getType());
    }

    public void assertResourcesList(List<Resource> actual, List<Resource> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    assertResources(actual.get(actualCount), expected.get(expectedCount));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Resource:[{}] under the Tenant:[{}], Found?:[{}]",
                    expected.get(expectedCount).getId(), expected.get(expectedCount).getPath().ids().getTenantId(),
                    found);
            Assert.assertTrue(found);
        }
    }

    public void assertFeeds(Feed actual, Feed expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getPath().ids().getTenantId(), expected.getPath().ids().getTenantId());
        Assert.assertEquals(actual.getProperties(), expected.getProperties());
        Assert.assertEquals(actual.getPath().ids().getEnvironmentId(), expected.getPath().ids().getEnvironmentId());
    }

    public void assertFeedsList(List<Feed> actual, List<Feed> expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedCount = 0; expectedCount < expected.size(); expectedCount++) {
            boolean found = false;
            for (int actualCount = 0; actualCount < actual.size(); actualCount++) {
                if (actual.get(actualCount).getId().equals(expected.get(expectedCount).getId())) {
                    assertFeeds(actual.get(actualCount), expected.get(expectedCount));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Feed:[{}] under the Tenant:[{}], Found?:[{}]",
                    expected.get(expectedCount).getId(), expected.get(expectedCount).getPath().ids().getTenantId(),
                    found);
            Assert.assertTrue(found);
        }
    }
}