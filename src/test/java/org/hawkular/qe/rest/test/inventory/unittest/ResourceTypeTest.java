package org.hawkular.qe.rest.test.inventory.unittest;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.inventory.api.model.ResourceType;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ResourceTypeTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(ResourceTypeTest.class);

    @Test(dataProvider = "resourceTypeDataProvider", priority = 1)
    public void creatTest(ResourceType resourceType) {
        _logger.debug("Creating ResourceType[{}] under tenant[{}]", resourceType.getId(), resourceType.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().createResourceType(resourceType).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<ResourceType> resourceTypes = getHawkularClient().inventory().getResourceTypes().getEntity();
        Assert.assertNotNull(resourceTypes);
        _logger.debug("Number of ResourceType:[{}], list:[{}]", resourceTypes.size(), resourceTypes);
        assertResourceTypesList(resourceTypes, (List<ResourceType>) getResourceTypes());
    }

    @Test(dataProvider = "resourceTypeDataProvider", priority = 3)
    public void getTest(ResourceType resourceType) {
        _logger.debug("Fetching resourceType[{}] under tenant[{}]", resourceType.getId(), resourceType.getTenantId());
        ResourceType resourceTypeRx = getHawkularClient().inventory().getResourceType(resourceType).getEntity();
        assertResourceTypes(resourceTypeRx, resourceType);
    }

    @Test(dataProvider = "resourceTypeDataProvider", priority = 4)
    public void deleteTest(ResourceType resourceType) {
        _logger.debug("Deleting resourceType[{}] under tenant[{}]", resourceType.getId(), resourceType.getTenantId());
        Assert.assertTrue(getHawkularClient().inventory().deleteResourceType(resourceType).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "resourceTypeDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getResourceTypes());
    }

    public static List<? extends Object> getResourceTypes() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(TENANT.getId(), "resourcetype1", "v_1.0"));
        resourceTypes.add(new ResourceType(TENANT.getId(), "_rt", "v_1.1"));
        resourceTypes.add(new ResourceType(TENANT.getId(), "3rt_", "v_10.0"));
        resourceTypes.add(new ResourceType(TENANT.getId(), "rt-4", "v_1.0.0.01"));
        resourceTypes.add(new ResourceType(TENANT.getId(),
                "resourcetype-withlooooooooooooooooooooooooooooooooongstring",
                "v_1.0"));
        resourceTypes.add(new ResourceType(TENANT.getId(), "resourcetypewith....dot", "v_1.0"));
        return resourceTypes;
    }

}
