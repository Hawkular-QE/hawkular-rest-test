package org.hawkular.qe.rest.test.inventory;

import java.util.ArrayList;
import java.util.List;

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
    private static final String TENANT_ID = "tenant-resource-test";
    private static final String ENVIRONMENT_ID = "environment-resource-test";
    private static final String FEED_ID = "feed-resource-test";
    private static final String RESOURCE_TYPE_ID = "resource-type-test";
    private static final ResourceType RESOURCE_TYPE = new ResourceType(TENANT_ID, RESOURCE_TYPE_ID, "V1.0");

    @BeforeClass
    public void setup() {
        Assert.assertTrue(getHawkularClient().inventory().createTenant(TENANT_ID));
        Assert.assertTrue(getHawkularClient().inventory().createEnvironment(TENANT_ID, ENVIRONMENT_ID));
        Assert.assertTrue(getHawkularClient().inventory().registerFeed(new Feed(TENANT_ID, ENVIRONMENT_ID, FEED_ID)));
        Assert.assertTrue(getHawkularClient().inventory().createResourceType(RESOURCE_TYPE));
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteResourceType(RESOURCE_TYPE));
        Assert.assertTrue(getHawkularClient().inventory().deleteFeed(TENANT_ID, ENVIRONMENT_ID, FEED_ID));
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(TENANT_ID, ENVIRONMENT_ID));
        Assert.assertTrue(getHawkularClient().inventory().deleteTenant(TENANT_ID));
    }

    @Test(dataProvider = "resourceDataProvider", priority = 1)
    public void addTest(Resource resource) {
        _logger.debug("Creating Resource[{}] under [tenant:{},environment:{}]", resource.getId(),
                resource.getTenantId(),
                resource.getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().addResource(resource));
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Resource> resources = getHawkularClient().inventory().getResourcesByType(TENANT_ID, ENVIRONMENT_ID,
                RESOURCE_TYPE_ID, RESOURCE_TYPE.getVersion().toString());
        Assert.assertNotNull(resources);
        _logger.debug("Number of Resources:[{}], list:[{}]", resources.size(), resources);
        assertResourcesList(resources, (List<Resource>) getResources());
    }

    @Test(dataProvider = "resourceDataProvider", priority = 3)
    public void getTest(Resource resource) {
        _logger.debug("Fetching resource[{}] under [tenant:{},environment:{}]", resource.getId(),
                resource.getTenantId(),
                resource.getEnvironmentId());
        Resource resourceRx = getHawkularClient().inventory().getResource(resource);
        assertResources(resourceRx, resource);
    }

    @Test(dataProvider = "resourceDataProvider", priority = 4)
    public void deleteTest(Resource resource) {
        _logger.debug("Deleting resource[{}] under [tenant:{},environment:{}]", resource.getId(),
                resource.getTenantId(),
                resource.getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().deleteResource(resource));
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "resourceDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getResources());
    }

    public static List<? extends Object> getResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "resource1", RESOURCE_TYPE));
        resources.add(new Resource(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "_r", RESOURCE_TYPE));
        resources.add(new Resource(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "3resource-_", RESOURCE_TYPE));
        resources.add(new Resource(TENANT_ID, ENVIRONMENT_ID, FEED_ID, "resource-2323", RESOURCE_TYPE));
        resources.add(new Resource(TENANT_ID, ENVIRONMENT_ID, FEED_ID,
                "resource-withlooooooooooooooooooooooooooooooooongstring", RESOURCE_TYPE));
        return resources;
    }

}
