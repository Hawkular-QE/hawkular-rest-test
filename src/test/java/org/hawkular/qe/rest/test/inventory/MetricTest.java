package org.hawkular.qe.rest.test.inventory;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.inventory.api.model.Feed;
import org.hawkular.inventory.api.model.Metric;
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
    private static final String TENANT_ID = "tenant-metric-test";
    private static final String ENVIRONMENT_ID = "environment-metric-test";
    private static final String FEED_ID = "feed-metric-test";
    private static final String METRIC_TYPE_ID = "metric-type-test";
    private static final MetricType METRIC_TYPE = new MetricType(TENANT_ID, METRIC_TYPE_ID, MetricUnit.MILLI_SECOND);

    @BeforeClass
    public void setup() {
        Assert.assertTrue(getHawkularClient().inventory().createTenant(TENANT_ID));
        Assert.assertTrue(getHawkularClient().inventory().createEnvironment(TENANT_ID, ENVIRONMENT_ID));
        Assert.assertTrue(getHawkularClient().inventory().registerFeed(new Feed(TENANT_ID, ENVIRONMENT_ID, FEED_ID)));
        Assert.assertTrue(getHawkularClient().inventory().createMetricType(METRIC_TYPE));
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteMetricType(METRIC_TYPE));
        Assert.assertTrue(getHawkularClient().inventory().deleteFeed(TENANT_ID, ENVIRONMENT_ID, FEED_ID));
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(TENANT_ID, ENVIRONMENT_ID));
        Assert.assertTrue(getHawkularClient().inventory().deleteTenant(TENANT_ID));
    }

    @Test(dataProvider = "metricDataProvider", priority = 1)
    public void creatTest(Metric metric) {
        _logger.debug("Creating Metric[{}] under [tenant:{},environment:{}]", metric.getId(), metric.getTenantId(),
                metric.getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().createMetric(metric));
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Metric> metrics = getHawkularClient().inventory().getMetrics(TENANT_ID, ENVIRONMENT_ID);
        Assert.assertNotNull(metrics);
        _logger.debug("Number of Metrics:[{}], list:[{}]", metrics.size(), metrics);
        assertMetricsList(metrics, (List<Metric>) getMetrics());
    }

    @Test(dataProvider = "metricDataProvider", priority = 3)
    public void getTest(Metric metric) {
        _logger.debug("Fetching metric[{}] under [tenant:{},environment:{}]", metric.getId(), metric.getTenantId(),
                metric.getEnvironmentId());
        Metric metricRx = getHawkularClient().inventory().getMetric(metric);
        assertMetrics(metricRx, metric);
    }

    @Test(dataProvider = "metricDataProvider", priority = 4)
    public void deleteTest(Metric metric) {
        _logger.debug("Deleting metric[{}] under [tenant:{},environment:{}]", metric.getId(), metric.getTenantId(),
                metric.getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().deleteMetric(metric));
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "metricDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getMetrics());
    }

    public static List<? extends Object> getMetrics() {
        List<Metric> metrics = new ArrayList<>();
        metrics.add(new Metric(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "metric1", METRIC_TYPE));
        metrics.add(new Metric(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "_m", METRIC_TYPE));
        metrics.add(new Metric(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "3metric-_", METRIC_TYPE));
        metrics.add(new Metric(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "metric-2323", METRIC_TYPE));
        metrics.add(new Metric(TENANT_ID, ENVIRONMENT_ID, FEED_ID,
                "metric-withlooooooooooooooooooooooooooooooooongstring", METRIC_TYPE));
        return metrics;
    }

}
