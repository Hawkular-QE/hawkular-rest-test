package org.hawkular.qe.rest.test.inventory;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class TenantTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(TenantTest.class);

    @Test(dataProvider = "tenantDataProvider", priority = 1)
    public void creatTest(Tenant tenant) {
        _logger.debug("Creating tenant:{}", tenant.getId());
        Assert.assertTrue(getHawkularClient().inventory().createTenant(tenant));
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Tenant> tenantsReceived = getHawkularClient().inventory().getTenants();
        _logger.debug("Number of Tenants:[{}], list:[{}]", tenantsReceived.size(), tenantsReceived);
        assertTenantsList(tenantsReceived, (List<Tenant>) getTenants());
    }

    @Test(dataProvider = "tenantDataProvider", priority = 3)
    public void deleteTest(Tenant tenant) {
        _logger.debug("Deleting tenant:{}", tenant.getId());
        Assert.assertTrue(getHawkularClient().inventory().deleteTenant(tenant));
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "tenantDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getTenants());
    }

    public static List<? extends Object> getTenants() {
        List<Tenant> tenants = new ArrayList<>();
        tenants.add(new Tenant("tenant1"));
        tenants.add(new Tenant("_t2"));
        tenants.add(new Tenant("3t_"));
        tenants.add(new Tenant("t-4"));
        tenants.add(new Tenant("tenantwithlooooooooooooooooooooooooooooooooongstring"));
        tenants.add(new Tenant("tenantwith.dot"));
        return tenants;
    }
}
