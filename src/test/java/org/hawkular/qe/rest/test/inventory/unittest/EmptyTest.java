package org.hawkular.qe.rest.test.inventory.unittest;

import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptyTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(EmptyTest.class);

    @Test(priority = 1)
    public void tenantTest() {
        Assert.assertTrue(TENANT != null, "Default Tenant test for each user.");
        if (TENANT != null) {
            _logger.debug("Tenant[{}] assigned for this user", TENANT);
        }

    }
}
