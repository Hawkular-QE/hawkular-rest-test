package org.hawkular.qe.rest.test.inventory;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.inventory.api.model.Environment;
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
public class EnvironmentTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(EnvironmentTest.class);
    private static final String TENANT_ID = "tenant-env-test";

    @BeforeClass
    public void setup() {
        Assert.assertTrue(getHawkularClient().inventory().createTenant(TENANT_ID));
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteTenant(TENANT_ID));
    }

    @Test(dataProvider = "environmentDataProvider", priority = 1)
    public void creatTest(Environment environment) {
        _logger.debug("Creating environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().createEnvironment(environment));
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Environment> environments = getHawkularClient().inventory().getEnvironments(TENANT_ID);
        Assert.assertNotNull(environments);
        _logger.debug("Number of Environement:[{}], list:[{}]", environments.size(), environments);
        assertEnvironmentsList(environments, (List<Environment>) getEnvironments());
    }

    @Test(dataProvider = "environmentDataProvider", priority = 3)
    public void getTest(Environment environment) {
        _logger.debug("Getting environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        Environment environmentRx = getHawkularClient().inventory().getEnvironment(environment);
        Assert.assertNotNull(environmentRx);
        _logger.debug("Received environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        assertEnvironments(environmentRx, environment);
    }

    @Test(dataProvider = "environmentDataProvider", priority = 4)
    public void deleteTest(Environment environment) {
        _logger.debug("Deleting environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(environment));
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "environmentDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getEnvironments());
    }

    public static List<? extends Object> getEnvironments() {
        List<Environment> environments = new ArrayList<>();
        environments.add(new Environment(TENANT_ID, "env1"));
        environments.add(new Environment(TENANT_ID, "_env"));
        environments.add(new Environment(TENANT_ID, "3env_"));
        environments.add(new Environment(TENANT_ID, "env-4"));
        environments.add(new Environment(TENANT_ID, "env-withlooooooooooooooooooooooooooooooooongstring"));
        environments.add(new Environment(TENANT_ID, "envwith.dot"));
        return environments;
    }
}
