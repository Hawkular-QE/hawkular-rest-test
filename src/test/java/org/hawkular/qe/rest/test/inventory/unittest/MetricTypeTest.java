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
package org.hawkular.qe.rest.test.inventory.unittest;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.client.ClientResponse;
import org.hawkular.inventory.api.model.CanonicalPath;
import org.hawkular.inventory.api.model.MetricDataType;
import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.MetricUnit;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class MetricTypeTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(MetricTypeTest.class);

    @Test(dataProvider = "metricTypeDataProvider", priority = 1)
    public void creatTest(MetricType metricType) {
        _logger.debug("Creating MetricType[{}] under tenant[{}]", metricType.getId(), metricType.getPath().ids()
                .getTenantId());
        ClientResponse<String> clientResponse = getHawkularClient().inventory().createMetricType(metricType);
        _logger.debug("Client response:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        ClientResponse<List<MetricType>> clientResponse = getHawkularClient().inventory().getMetricTypes();
        _logger.debug("Client response:[{}]", clientResponse);
        List<MetricType> metricTypes = clientResponse.getEntity();
        Assert.assertNotNull(metricTypes);
        _logger.debug("Number of MetricType:[{}], list:[{}]", metricTypes.size(), metricTypes);
        assertMetricTypesList(metricTypes, (List<MetricType>) getMetricTypes());
    }

    @Test(dataProvider = "metricTypeDataProvider", priority = 3)
    public void getTest(MetricType metricType) {
        _logger.debug("Fetching metricType[{}] under tenant[{}]", metricType.getId(), metricType.getPath().ids()
                .getTenantId());
        MetricType metricTypeRx = getHawkularClient().inventory().getMetricType(metricType).getEntity();
        assertMetricTypes(metricTypeRx, metricType);
    }

    @Test(dataProvider = "metricTypeDataProvider", priority = 4)
    public void deleteTest(MetricType metricType) {
        _logger.debug("Deleting metricType[{}] under tenant[{}]", metricType.getId(), metricType.getPath().ids()
                .getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().deleteMetricType(metricType).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "metricTypeDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getMetricTypes());
    }

    public static List<? extends Object> getMetricTypes() {
        List<MetricType> metricTypes = new ArrayList<>();
        metricTypes.add(new MetricType(CanonicalPath.of().tenant(TENANT.getId()).metricType("metrictype1").get(),
                MetricUnit.BYTES, MetricDataType.GAUGE, MINUTE * 30));
        metricTypes.add(new MetricType(CanonicalPath.of().tenant(TENANT.getId()).metricType("_mt").get(),
                MetricUnit.MILLISECONDS, MetricDataType.GAUGE, MINUTE * 30));
        metricTypes.add(new MetricType(CanonicalPath.of().tenant(TENANT.getId()).metricType("3mt_").get(),
                MetricUnit.MINUTES, MetricDataType.COUNTER, MINUTE * 30));
        metricTypes.add(new MetricType(CanonicalPath.of().tenant(TENANT.getId()).metricType("mt-4").get(),
                MetricUnit.NONE, MetricDataType.AVAILABILITY, MINUTE * 30));
        metricTypes.add(new MetricType(CanonicalPath.of().tenant(TENANT.getId())
                .metricType("metrictype-withlooooooooooooooooooooooooooooooooongstring").get(),
                MetricUnit.SECONDS, MetricDataType.COUNTER_RATE, MINUTE * 30));
        metricTypes.add(new MetricType(CanonicalPath.of().tenant(TENANT.getId()).metricType("metrictypewith....dot")
                .get(),
                MetricUnit.KILOBYTES, MetricDataType.GAUGE, MINUTE * 30));
        return metricTypes;
    }

}
