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
import java.util.Map;

import org.hawkular.inventory.api.model.CanonicalPath;
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

    @SuppressWarnings("unchecked")
    public static List<? extends Object> getResourceTypes() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        List<ResourceType> resourceTypes = new ArrayList<>();
        properties.put("version", "v_1.0");
        resourceTypes.add(new ResourceType(CanonicalPath.of().tenant(TENANT.getId()).resourceType("resourcetype1")
                .get(), (Map<String, Object>) properties.clone()));
        properties.put("version", "v_1.1");
        resourceTypes.add(new ResourceType(CanonicalPath.of().tenant(TENANT.getId()).resourceType("_rt")
                .get(), (Map<String, Object>) properties.clone()));
        properties.put("version", "v_10.0");
        resourceTypes.add(new ResourceType(CanonicalPath.of().tenant(TENANT.getId()).resourceType("3rt_")
                .get(), (Map<String, Object>) properties.clone()));
        properties.put("version", "v_1.0.0.01");
        resourceTypes.add(new ResourceType(CanonicalPath.of().tenant(TENANT.getId()).resourceType("rt-4")
                .get(), (Map<String, Object>) properties.clone()));
        properties.put("version", "v_1.0");
        resourceTypes.add(new ResourceType(CanonicalPath.of().tenant(TENANT.getId())
                .resourceType("resourcetype-withlooooooooooooooooooooooooooooooooongstring")
                .get(), (Map<String, Object>) properties.clone()));
        resourceTypes.add(new ResourceType(CanonicalPath.of().tenant(TENANT.getId())
                .resourceType("resourcetypewith....dot")
                .get(), (Map<String, Object>) properties.clone()));
        return resourceTypes;
    }

}
