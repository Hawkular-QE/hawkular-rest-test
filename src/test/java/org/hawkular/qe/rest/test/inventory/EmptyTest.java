package org.hawkular.qe.rest.test.inventory;

import java.util.List;

import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptyTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(EmptyTest.class);

    @Test(priority = 1)
    public void tenantTest() {
        List<Tenant> tenants = getHawkularClient().inventory().getTenants();
        _logger.debug("Pre existing tenants:[{}]", tenants.size());
        if (!tenants.isEmpty()) {//Remove existing tenants
            _logger.warn("Removing existing tenants:[{}]", tenants);
            for (Tenant tenant : tenants) {
                if (getHawkularClient().inventory().deleteTenant(tenant)) {
                    _logger.debug("Tenant[{}] deleted successfully.", tenant.getId());
                } else {
                    _logger.warn("Tenant[{}] deletetion failed.", tenant.getId());
                }
            }
        }
        Assert.assertTrue(tenants.isEmpty(), "Tenants list empty test");
    }
}
