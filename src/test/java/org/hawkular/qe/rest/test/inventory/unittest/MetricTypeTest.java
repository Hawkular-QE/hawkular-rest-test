package org.hawkular.qe.rest.test.inventory.unittest;

import java.util.ArrayList;
import java.util.List;

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
        _logger.debug("Creating MetricType[{}] under tenant[{}]", metricType.getId(), metricType.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().createMetricType(metricType).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<MetricType> metricTypes = getHawkularClient().inventory().getMetricTypes().getEntity();
        Assert.assertNotNull(metricTypes);
        _logger.debug("Number of MetricType:[{}], list:[{}]", metricTypes.size(), metricTypes);
        assertMetricTypesList(metricTypes, (List<MetricType>) getMetricTypes());
    }

    @Test(dataProvider = "metricTypeDataProvider", priority = 3)
    public void getTest(MetricType metricType) {
        _logger.debug("Fetching metricType[{}] under tenant[{}]", metricType.getId(), metricType.getTenantId());
        MetricType metricTypeRx = getHawkularClient().inventory().getMetricType(metricType).getEntity();
        assertMetricTypes(metricTypeRx, metricType);
    }

    @Test(dataProvider = "metricTypeDataProvider", priority = 4)
    public void deleteTest(MetricType metricType) {
        _logger.debug("Deleting metricType[{}] under tenant[{}]", metricType.getId(), metricType.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().deleteMetricType(metricType).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "metricTypeDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getMetricTypes());
    }

    public static List<? extends Object> getMetricTypes() {
        List<MetricType> metricTypes = new ArrayList<>();
        metricTypes.add(new MetricType(TENANT.getId(), "metrictype1", MetricUnit.BYTE));
        metricTypes.add(new MetricType(TENANT.getId(), "_mt", MetricUnit.KILO_BYTE));
        metricTypes.add(new MetricType(TENANT.getId(), "3mt_", MetricUnit.MILLI_SECOND));
        metricTypes.add(new MetricType(TENANT.getId(), "mt-4", MetricUnit.MINUTE));
        metricTypes.add(new MetricType(TENANT.getId(), "metrictype-withlooooooooooooooooooooooooooooooooongstring",
                MetricUnit.NONE));
        metricTypes.add(new MetricType(TENANT.getId(), "metrictypewith....dot", MetricUnit.SECONDS));
        return metricTypes;
    }

}