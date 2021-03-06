/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    private static List<Environment> environments = null;

    @Test(dataProvider = "environmentDataProvider", priority = 1)
    public void creatTest(Environment environment) {
        _logger.debug("Creating environment[{}] under tenant[{}]", environment.getId(), environment.getPath().ids()
                .getTenantId());
        ClientResponse<String> clientResponse = getHawkularClient().inventory().createEnvironment(environment);
        _logger.debug("Environment Creation Status:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess(), clientResponse.toString());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        ClientResponse<List<Environment>> clientResponse = getHawkularClient().inventory().getEnvironments();
        _logger.debug("Get available environment(s) status:[{}]", clientResponse);
        Assert.assertNotNull(clientResponse.getEntity());
        _logger.debug("Number of Environement:[{}], list:[{}]", clientResponse.getEntity().size(),
                clientResponse.getEntity());
        assertEnvironmentsList(clientResponse.getEntity(), (List<Environment>) getEnvironments());
    }

    @Test(dataProvider = "environmentDataProvider", priority = 3)
    public void getTest(Environment environment) {
        _logger.debug("Getting environment[{}] under tenant[{}]", environment.getId(), environment.getPath().ids()
                .getTenantId());
        ClientResponse<Environment> clientResponse = getHawkularClient().inventory().getEnvironment(environment);
        _logger.debug("Get Environment response:[{}]", clientResponse);
        Assert.assertNotNull(clientResponse.getEntity());
        _logger.debug("Received environment[{}] under tenant[{}]", environment.getId(), environment.getPath().ids()
                .getTenantId());
        assertEnvironments(clientResponse.getEntity(), environment);
    }

    @Test(dataProvider = "environmentDataProvider", priority = 4)
    public void deleteTest(Environment environment) {
        _logger.debug("Deleting environment[{}] under tenant[{}]", environment.getId(), environment.getPath().ids()
                .getTenantId());
        ClientResponse<String> clientResponse = getHawkularClient().inventory().deleteEnvironment(environment);
        _logger.debug("Delete Environment Response:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "environmentDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getEnvironments());
    }

    public static List<? extends Object> getEnvironments() {
        if (environments == null) {
            environments = new ArrayList<>();
            environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                    .environment("env1" + getRandomId()).get()));
            environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                    .environment("_env" + getRandomId()).get()));
            environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                    .environment("3env_" + getRandomId()).get()));
            environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                    .environment("env-4" + getRandomId()).get()));
            environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                    .environment("env-withlooooooooooooooooooooooooooooooooongstring").get()));
            environments.add(new Environment(CanonicalPath.of().tenant(TENANT.getId())
                    .environment("envwith.dot").get()));
        }
        return environments;
    }
}
