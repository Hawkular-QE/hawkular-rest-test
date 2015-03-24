package org.hawkular.qe.rest.test.inventory;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.client.inventory.IdWrapper;
import org.hawkular.client.inventory.StringWrapper;
import org.hawkular.inventory.api.Resource;
import org.hawkular.inventory.api.ResourceType;
import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.hawkular.qe.rest.inventory.api.TenantAndResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ResourceTest extends HawkularRestTestBase{
    private Logger _logger = LoggerFactory.getLogger(ResourceTest.class);

    @Test(priority=1)
    public void pingTest(){
        StringWrapper pingResult = getHawkularClient().inventory().ping();
        _logger.debug("Hawkular Inventory Ping response: "+pingResult);
        Assert.assertEquals(pingResult.getValue(), "Hello World");    
    }

    @Test(dataProvider = "resourceDataProvider", priority=2)
    public void addResourceTest(TenantAndResource tenantAndResource){
        IdWrapper id = getHawkularClient().inventory().addResource(
                tenantAndResource.getTenant().getId(),
                tenantAndResource.getResource());
        Assert.assertEquals(id.getId(), tenantAndResource.getResource().getId());
    }

    @Test(dataProvider = "resourceDataProvider", priority=3)
    public void getResourceTest(TenantAndResource tenantAndResource){
        Resource resource = getHawkularClient().inventory().getResource(
                tenantAndResource.getTenant().getId(),
                tenantAndResource.getResource().getId());
        Assert.assertEquals(resource.getId(),
                tenantAndResource.getResource().getId());
        Assert.assertEquals(resource.getType(),
                tenantAndResource.getResource().getType());
        Assert.assertEquals(resource.getParameters(),
                tenantAndResource.getResource().getParameters());
    }


    @DataProvider(name = "resourceDataProvider")
    public Object[][] resourceDataProvider(){
        return this.get2dArray(this.getTenantAndResourceList());
    }

    public List<Object> getTenantAndResourceList(){
        List<Object> tenantAndResourceList = new ArrayList<>();
        //Resource 1
        Resource resource = new Resource();
        resource.setId("resource_1");
        resource.addParameter("url", "http://hawkular.org/");
        resource.setType(ResourceType.URL);
        tenantAndResourceList.add(new TenantAndResource("tenant_1", resource));
        //Resource 2
        resource = new Resource();
        resource.setId("resource_2");
        resource.addParameter("url", "http://jboss.org/");
        resource.setType(ResourceType.URL);
        tenantAndResourceList.add(new TenantAndResource("tenant_1", resource));
        return tenantAndResourceList;
    }
}