package org.hawkular.qe.rest.test.metrics;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.client.metrics.model.TenantParam;
import org.hawkular.metrics.core.api.Tenant;
import org.hawkular.qe.rest.base.metrics.MetricsTestBase;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class TenantTest extends MetricsTestBase {

    @Test(dataProvider = "tenantDataProvider", priority = 1)
    public void createsTest(TenantParam tenant) {
        Assert.assertTrue(getHawkularClient().metrics().createTenant(new Tenant(tenant.getId())));
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void findTest(TenantParam tenant) {
        List<TenantParam> tenantsRx = getHawkularClient().metrics().getTenants();
        assertTenantsList(tenantsRx, (List<TenantParam>) getTenants());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "tenantDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getTenants());
    }

    public static List<? extends Object> getTenants() {
        List<TenantParam> tenants = new ArrayList<>();
        tenants.add(getTenant("tenant1"));
        tenants.add(getTenant("_t2"));
        tenants.add(getTenant("3t_"));
        tenants.add(getTenant("t-4"));
        tenants.add(getTenant("tenantwithlooooooooooooooooooooooooooooooooongstring"));
        tenants.add(getTenant("tenantwith.dot"));
        return tenants;
    }

    private static TenantParam getTenant(String id) {
        TenantParam tenant = new TenantParam(new Tenant(id));
        return tenant;
    }

}
