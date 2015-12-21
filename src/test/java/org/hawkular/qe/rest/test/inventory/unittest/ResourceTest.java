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
import java.util.HashMap;
import java.util.List;

import org.hawkular.client.ClientResponse;
import org.hawkular.inventory.api.model.CanonicalPath;
import org.hawkular.inventory.api.model.Feed;
import org.hawkular.inventory.api.model.Resource;
import org.hawkular.inventory.api.model.ResourceType;
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
public class ResourceTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(ResourceTest.class);
    private static final String ENVIRONMENT_ID = "environment-resource-test";
    private static final String FEED_ID = "feed-resource-test";
    private static final String RESOURCE_TYPE_ID = "resource-type-test";
    private static ResourceType RESOURCE_TYPE = null;

    @BeforeClass
    public void setup() {
        if (RESOURCE_TYPE == null) {
            HashMap<String, Object> properties = new HashMap<String, Object>();
            properties.put("version", "V1.0");
            RESOURCE_TYPE = new ResourceType(CanonicalPath.of().tenant(TENANT.getId()).resourceType(RESOURCE_TYPE_ID)
                    .get(), properties);
        }
        ClientResponse<String> clientResponse = getHawkularClient().inventory().createEnvironment(ENVIRONMENT_ID);
        _logger.debug("Client Response:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess());

        clientResponse = getHawkularClient().inventory().registerFeed(
                new Feed(CanonicalPath.of().tenant(TENANT.getId()).feed(FEED_ID).get()));
        _logger.debug("Client Response:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess());

        clientResponse = getHawkularClient().inventory().createResourceType(RESOURCE_TYPE);
        _logger.debug("Client Response:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess());
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteResourceType(RESOURCE_TYPE).isSuccess());
        Assert.assertTrue(getHawkularClient().inventory().deleteFeed(FEED_ID).isSuccess());
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(ENVIRONMENT_ID).isSuccess());
    }

    @Test(dataProvider = "resourceDataProvider", priority = 1)
    public void addTest(Resource resource) {
        _logger.debug("Creating Resource[{}] under [tenant:{},environment:{}]", resource.getId(),
                resource.getPath().ids().getTenantId(),
                resource.getPath().ids().getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().addResource(resource).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Resource> resources = getHawkularClient().inventory().getResourcesByType(ENVIRONMENT_ID,
                RESOURCE_TYPE_ID, RESOURCE_TYPE.getProperties().get("version").toString()).getEntity();
        Assert.assertNotNull(resources);
        _logger.debug("Number of Resources:[{}], list:[{}]", resources.size(), resources);
        assertResourcesList(resources, (List<Resource>) getResources());
    }

    @Test(dataProvider = "resourceDataProvider", priority = 3)
    public void getTest(Resource resource) {
        _logger.debug("Fetching resource[{}] under [tenant:{},environment:{}]", resource.getId(),
                resource.getPath().ids().getTenantId(),
                resource.getPath().ids().getEnvironmentId());
        Resource resourceRx = getHawkularClient().inventory().getResource(resource).getEntity();
        assertResources(resourceRx, resource);
    }

    @Test(dataProvider = "resourceDataProvider", priority = 4)
    public void deleteTest(Resource resource) {
        _logger.debug("Deleting resource[{}] under [tenant:{},environment:{}]", resource.getId(),
                resource.getPath().ids().getTenantId(),
                resource.getPath().ids().getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().deleteResource(resource).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "resourceDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getResources());
    }

    public static List<? extends Object> getResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID)
                .resource("resource1").get(), RESOURCE_TYPE));
        resources.add(new Resource(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID)
                .resource("_r").get(), RESOURCE_TYPE));
        resources.add(new Resource(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID)
                .resource("3resource-_").get(), RESOURCE_TYPE));
        resources.add(new Resource(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID)
                .resource("resource-2323").get(), RESOURCE_TYPE));
        resources.add(new Resource(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT_ID)
                .resource("resource-withlooooooooooooooooooooooooooooooooongstring").get(), RESOURCE_TYPE));
        return resources;
    }
}
