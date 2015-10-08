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

import org.hawkular.inventory.api.model.CanonicalPath;
import org.hawkular.inventory.api.model.Feed;
import org.hawkular.inventory.api.model.Metric;
import org.hawkular.inventory.api.model.MetricDataType;
import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.MetricUnit;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class MetricTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(MetricTest.class);
    private static final String ENVIRONMENT_ID = "environment-metric-test";
    private static final String FEED_ID = "feed-metric-test";
    private static final String METRIC_TYPE_ID = "metric-type-test";
    private static MetricType METRIC_TYPE = null;

    @BeforeClass
    public void setup() {
        if (METRIC_TYPE == null) {
            METRIC_TYPE = new MetricType(CanonicalPath.of().tenant(TENANT.getId()).metricType(METRIC_TYPE_ID).get(),
                    MetricUnit.MILLISECONDS, MetricDataType.GAUGE);
        }
        Assert.assertTrue(getHawkularClient().inventory().createEnvironment(ENVIRONMENT_ID).isSuccess());
        Assert.assertTrue(getHawkularClient()
                .inventory()
                .registerFeed(
                        new Feed(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID).feed(FEED_ID)
                                .get())).isSuccess());
        Assert.assertTrue(getHawkularClient().inventory().createMetricType(METRIC_TYPE).isSuccess());
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteMetricType(METRIC_TYPE).isSuccess());
        Assert.assertTrue(getHawkularClient().inventory().deleteFeed(ENVIRONMENT_ID, FEED_ID).isSuccess());
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(ENVIRONMENT_ID).isSuccess());
    }

    @Test(dataProvider = "metricDataProvider", priority = 1)
    public void creatTest(Metric metric) {
        _logger.debug("Creating Metric[{}] under [tenant:{},environment:{}]", metric.getId(), metric.getTenantId(),
                metric.getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().createMetric(metric).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Metric> metrics = getHawkularClient().inventory().getMetrics(ENVIRONMENT_ID).getEntity();
        Assert.assertNotNull(metrics);
        _logger.debug("Number of Metrics:[{}], list:[{}]", metrics.size(), metrics);
        assertMetricsList(metrics, (List<Metric>) getMetrics());
    }

    @Test(dataProvider = "metricDataProvider", priority = 3)
    public void getTest(Metric metric) {
        _logger.debug("Fetching metric[{}] under [tenant:{},environment:{}]", metric.getId(), metric.getTenantId(),
                metric.getEnvironmentId());
        Metric metricRx = getHawkularClient().inventory().getMetric(metric).getEntity();
        assertMetrics(metricRx, metric);
    }

    @Test(dataProvider = "metricDataProvider", priority = 4)
    public void deleteTest(Metric metric) {
        _logger.debug("Deleting metric[{}] under [tenant:{},environment:{}]", metric.getId(), metric.getTenantId(),
                metric.getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().deleteMetric(metric).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "metricDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getMetrics());
    }

    public List<? extends Object> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        metrics.add(new Metric(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID).feed(FEED_ID)
                .metric("metric1").get(), METRIC_TYPE));
        metrics.add(new Metric(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID).feed(FEED_ID)
                .metric("_m").get(), METRIC_TYPE));
        metrics.add(new Metric(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID).feed(FEED_ID)
                .metric("3metric-_").get(), METRIC_TYPE));
        metrics.add(new Metric(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID).feed(FEED_ID)
                .metric("metric-2323").get(), METRIC_TYPE));
        metrics.add(new Metric(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID).feed(FEED_ID)
                .metric("metric-withlooooooooooooooooooooooooooooooooongstring").get(), METRIC_TYPE));
        return metrics;
    }

}
