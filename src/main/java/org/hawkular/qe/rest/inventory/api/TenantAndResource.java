package org.hawkular.qe.rest.inventory.api;

import org.hawkular.inventory.api.model.Resource;
import org.hawkular.metrics.core.api.Tenant;
/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class TenantAndResource {
    private Tenant tenant;
    private Resource resource;
    
    public TenantAndResource(){
    }
    
    public TenantAndResource(Tenant tenant, Resource resource){
        this.tenant = tenant;
        this.resource = resource;
    }
    
    public TenantAndResource(String tenantId, Resource resource){
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        this.tenant = tenant;
        this.resource = resource;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
    
    public String toString(){
        return "tenantId:"+tenant.getId()+", resourceId:"+resource.getId();
    }
}
