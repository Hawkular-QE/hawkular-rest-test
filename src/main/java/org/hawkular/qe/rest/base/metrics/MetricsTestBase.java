/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates
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
        Assert.assertTrue(actual.size() >= expected.size());
        for (int expectedLocation = 0; expectedLocation < expected.size(); expectedLocation++) {
            boolean found = false;
            for (int actualLocation = 0; actualLocation < actual.size(); actualLocation++) {
                if (actual.get(actualLocation).getId().equals(expected.get(expectedLocation).getId())) {
                    assertTenants(actual.get(actualLocation), expected.get(expectedLocation));
                    found = true;
                    break;
                }
            }
            _logger.debug("Processed for the Tenant:[{}], Found?:[{}]", expected.get(expectedLocation).getId(), found);
            Assert.assertTrue(found);
        }
    }

    public void assertTenants(TenantParam actual, TenantParam expected) {
        Assert.assertNotNull(actual);
        Assert.assertNotNull(expected);
        Assert.assertEquals(actual.getId(), expected.getId());
    }

    public boolean isTenantAvailable(List<TenantParam> tenantParams, TenantParam tenant) {
        for (TenantParam tenantParam : tenantParams) {
            if (tenantParam.getId().equals(tenant.getId())) {
                return true;
            }
        }
        return false;
    }

}
