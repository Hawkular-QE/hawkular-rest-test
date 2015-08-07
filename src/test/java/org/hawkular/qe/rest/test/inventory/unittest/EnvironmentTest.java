package org.hawkular.qe.rest.test.inventory.unittest;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.client.ClientResponse;
import org.hawkular.inventory.api.model.CanonicalPath;
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
        ClientResponse<String> clientResponse = getHawkularClient().inventory().createEnvironment(environment);
        Assert.assertTrue(clientResponse.isSuccess(), clientResponse.getErrorMsg());
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
        ClientResponse<String> clientResponse = getHawkularClient().inventory().deleteEnvironment(environment);
        Assert.assertTrue(clientResponse.isSuccess(), clientResponse.getErrorMsg());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "environmentDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getEnvironments());
    }

    public static List<? extends Object> getEnvironments() {
        List<Environment> environments = new ArrayList<>();
        environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId()).environment("env1").get()));
        environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId()).environment("_env").get()));
        environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId()).environment("3env_").get()));
        environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId()).environment("env-4").get()));
        environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                .environment("env-withlooooooooooooooooooooooooooooooooongstring").get()));
        environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId()).environment("envwith.dot").get()));
        return environments;
    }
}
