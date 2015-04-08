package org.hawkular.qe.rest.test.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.MetricUnit;
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
public class MetricTypeTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(MetricTypeTest.class);
    private static final String TENANT_ID = "tenant-metrictype-test";

    @BeforeClass
    public void setup() {
        Assert.assertTrue(getHawkularClient().inventory().createTenant(TENANT_ID));
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteTenant(TENANT_ID));
    }

    @Test(dataProvider = "metricTypeDataProvider", priority = 1)
    public void creatTest(MetricType metricType) {
        _logger.debug("Creating MetricType[{}] under tenant[{}]", metricType.getId(), metricType.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().createMetricType(metricType));
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        Set<MetricType> metricTypes = getHawkularClient().inventory().getMetricTypes(TENANT_ID);
        Assert.assertNotNull(metricTypes);
        _logger.debug("Number of MetricType:[{}], list:[{}]", metricTypes.size(), metricTypes);
        assertMetricTypesList((List<MetricType>) getMetricTypes(), new ArrayList<MetricType>(metricTypes));
    }

    @Test(dataProvider = "metricTypeDataProvider", priority = 3)
    public void deleteTest(MetricType metricType) {
        _logger.debug("Deleting metricType[{}] under tenant[{}]", metricType.getId(), metricType.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().deleteMetricType(metricType));
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "metricTypeDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getMetricTypes());
    }

    public static List<? extends Object> getMetricTypes() {
        List<MetricType> metricTypes = new ArrayList<>();
        metricTypes.add(new MetricType(TENANT_ID, "metrictype1", MetricUnit.BYTE));
        metricTypes.add(new MetricType(TENANT_ID, "_mt",MetricUnit.KILO_BYTE));
        metricTypes.add(new MetricType(TENANT_ID, "3mt_",MetricUnit.MILLI_SECOND));
        metricTypes.add(new MetricType(TENANT_ID, "mt-4",MetricUnit.MINUTE));
        metricTypes.add(new MetricType(TENANT_ID, "metrictype-withlooooooooooooooooooooooooooooooooongstring",MetricUnit.NONE));
        metricTypes.add(new MetricType(TENANT_ID, "metrictypewith....dot",MetricUnit.SECONDS));
        return metricTypes;
    }

}
