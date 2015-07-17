package org.hawkular.qe.rest.test.inventory.unittest;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.inventory.api.model.Environment;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class EnvironmentTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(EnvironmentTest.class);

    @Test(dataProvider = "environmentDataProvider", priority = 1)
    public void creatTest(Environment environment) {
        _logger.debug("Creating environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().createEnvironment(environment).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Environment> environments = getHawkularClient().inventory().getEnvironments().getEntity();
        Assert.assertNotNull(environments);
        _logger.debug("Number of Environement:[{}], list:[{}]", environments.size(), environments);
        assertEnvironmentsList(environments, (List<Environment>) getEnvironments());
    }

    @Test(dataProvider = "environmentDataProvider", priority = 3)
    public void getTest(Environment environment) {
        _logger.debug("Getting environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        Environment environmentRx = getHawkularClient().inventory().getEnvironment(environment).getEntity();
        Assert.assertNotNull(environmentRx);
        _logger.debug("Received environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        assertEnvironments(environmentRx, environment);
    }

    @Test(dataProvider = "environmentDataProvider", priority = 4)
    public void deleteTest(Environment environment) {
        _logger.debug("Deleting environment[{}] under tenant[{}]", environment.getId(), environment.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(environment).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "environmentDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getEnvironments());
    }

    public static List<? extends Object> getEnvironments() {
        List<Environment> environments = new ArrayList<>();
        environments.add(new Environment(TENANT.getId(), "env1"));
        environments.add(new Environment(TENANT.getId(), "_env"));
        environments.add(new Environment(TENANT.getId(), "3env_"));
        environments.add(new Environment(TENANT.getId(), "env-4"));
        environments.add(new Environment(TENANT.getId(), "env-withlooooooooooooooooooooooooooooooooongstring"));
        environments.add(new Environment(TENANT.getId(), "envwith.dot"));
        return environments;
    }
}
