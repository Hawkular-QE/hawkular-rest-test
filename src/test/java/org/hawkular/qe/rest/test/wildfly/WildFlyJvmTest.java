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
package org.hawkular.qe.rest.test.wildfly;

import java.util.ArrayList;
import java.util.HashMap;

import org.hawkular.client.ClientResponse;
import org.hawkular.inventory.api.model.CanonicalPath;
import org.hawkular.inventory.api.model.Environment;
import org.hawkular.inventory.api.model.Feed;
import org.hawkular.inventory.api.model.Metric;
import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.Resource;
import org.hawkular.inventory.api.model.ResourceType;
import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.metrics.core.api.AvailabilityType;
import org.hawkular.qe.rest.inventory.WildFlyServerBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class WildFlyJvmTest extends WildFlyServerBase {
    public static Tenant TENANT;
    public Environment ENVIRONMENT;
    private static final String FEED_ID = "wildFly-via-rest";
    private static final String RESOURCE_ID = "wildfly-server-via-rest-api-1";
    public Feed FEED;
    private ResourceType resourceType;
    private Resource resource;
    private HashMap<String, ArrayList<Double>> metricsDataMap;
    private long DATA_DELAY = 5000;//In milliseconds
    private long DATA_SIZE = 10;
    private long startTimestamp;

    private ArrayList<Metric> metrics = new ArrayList<Metric>();

    @BeforeClass
    public void loadBefore() {
        TENANT = getHawkularClient().inventory().getTenant().getEntity();
        ENVIRONMENT = getHawkularClient().inventory().getEnvironment("test").getEntity();
    }

    @Test(priority = 1)
    public void createEnvironment() {
        //Create Environment if not available
        if (ENVIRONMENT == null) {
            ClientResponse<String> createResponse = getHawkularClient().inventory().createEnvironment("test");
            _logger.debug("Create Environment Response:[{}]", createResponse.toString());
            Assert.assertTrue(createResponse.isSuccess());
            _logger.debug("Created Environment:[{}]", ENVIRONMENT);
        } else {
            _logger.debug("Environment already available:[{}]", ENVIRONMENT);
        }
    }

    @Test(dependsOnMethods = { "createEnvironment" })
    public void registerFeed() {
        //Register Feed
        FEED = new Feed(CanonicalPath.of().tenant(TENANT.getId()).environment(ENVIRONMENT.getId()).feed(FEED_ID).get());
        if (getHawkularClient().inventory().getFeed(FEED).getEntity() == null) {
            ClientResponse<String> registerResponse = getHawkularClient().inventory().registerFeed(FEED);
            _logger.debug("Register Feed Response:[{}]", registerResponse.toString());
            Assert.assertTrue(registerResponse.isSuccess(), "Feed creation failed");
            _logger.debug("Created Feed:[{}]", FEED);
        } else {
            _logger.debug("Feed already available:[{}]", FEED);
        }
    }

    @Test(dependsOnMethods = { "registerFeed" })
    public void createResourceTypes() {
        //Create ResourceTypes If not available
        resourceType = getResourceType(TENANT, RESOURCE_TYPES.WILDFLY_SERVER);
        if (getHawkularClient().inventory().getResourceType(resourceType).getEntity() == null) {
            ClientResponse<String> createResponse = getHawkularClient().inventory().createResourceType(resourceType);
            _logger.debug("Create Resource Type Response:[{}]", createResponse.toString());
            Assert.assertTrue(createResponse.isSuccess(), "ResourceType[" + resourceType.getId() + "] creation status");
            _logger.debug("Created ResourceType:[{}]", resourceType);
        } else {
            _logger.debug("ResourceType already available:[{}]", resourceType);
        }
    }

    @Test(dependsOnMethods = { "createResourceTypes" })
    public void addResourcs() {//Add Resource if not available
        resource = getResource(TENANT, ENVIRONMENT, FEED, RESOURCE_ID, resourceType);
        if (getHawkularClient().inventory().getResource(resource).getEntity() == null) {
            ClientResponse<String> addReponse = getHawkularClient().inventory().addResource(resource);
            _logger.debug("Add Resource Response:[{}]", addReponse.toString());
            Assert.assertTrue(addReponse.isSuccess(), "Resource[" + resource.getId() + "] creation status");
            _logger.debug("Created Resource:[{}]", resource);
        } else {
            _logger.debug("Resource already available:[{}]", resource);
        }
    }

    @Test(dependsOnMethods = { "addResourcs" })
    public void createMetricTypes() {
        //Create MetricTypes if not available
        for (METRIC_TYPES type : METRIC_TYPES.values()) {
            MetricType metricType = getMetricType(TENANT, type);
            if (getHawkularClient().inventory().getMetricType(metricType).getEntity() == null) {
                ClientResponse<String> createResponse = getHawkularClient().inventory().createMetricType(metricType);
                _logger.debug("Create Metric Type Response:[{}]", createResponse.toString());
                Assert.assertTrue(createResponse.isSuccess(), "MetricType[" + metricType.getId() + "] creation status");
                _logger.debug("Created MetricType:[{}]", metricType);
            } else {
                _logger.debug("MetricType already available:[{}]", metricType);
            }
        }
    }

    @Test(dependsOnMethods = { "createMetricTypes" })
    public void createMetrics() {
        //Create Metrics if not available
        for (METRICS metricType : METRICS.values()) {
            Metric metric = getMetric(TENANT, ENVIRONMENT, FEED, RESOURCE_ID, metricType);
            metrics.add(metric);
            if (getHawkularClient().inventory().getMetric(metric).getEntity() == null) {
                ClientResponse<String> createResponse = getHawkularClient().inventory().createMetric(metric);
                _logger.debug("Create Metric Response:[{}]", createResponse.toString());
                Assert.assertTrue(createResponse.isSuccess(), "Metric[" + metric.getId() + "] creation status");
                _logger.debug("Created Metric:[{}]", metric);
            } else {
                _logger.debug("Metric already available:[{}]", metric);
            }
        }
    }

    @Test(dependsOnMethods = { "createMetrics" })
    public void pushMetricsData() {
        //Add Metrics data for wildfly server availability
        getHawkularClient().metrics().addAvailabilityData(TENANT.getId(),
                METRICS.APP_SERVER.value(RESOURCE_ID),
                getAvailablityDataPoint(AvailabilityType.UP));

        //Add Metrics data for other metrics
        setMetricData(RESOURCE_ID);
        for (int count = 0; count < DATA_SIZE; count++) {
            startTimestamp = System.currentTimeMillis();
            for (METRICS metric : METRICS.values()) {
                getHawkularClient().metrics().addGaugeData(TENANT.getId(),
                        metric.value(RESOURCE_ID),
                        getHeapDataPoints(
                                startTimestamp + (count * DATA_DELAY),
                                metricsDataMap.get(metric.value(RESOURCE_ID)).get(count)));
            }
        }

    }

    @Test(dependsOnMethods = { "pushMetricsData" })
    public void validateMetricsData() {

        //Validate availability data
        Assert.assertTrue(AvailabilityType.fromString(getHawkularClient().metrics()
                .getAvailabilityData(TENANT.getId(),
                        METRICS.APP_SERVER.value(RESOURCE_ID)).get(0).getValue()) == AvailabilityType.UP);

        //long endTimestamp = startTimestamp + (DATA_DELAY * DATA_SIZE);

        //validate metrics data
        //TODO: add code
    }

    public void setMetricData(String resourceId) {
        metricsDataMap = new HashMap<String, ArrayList<Double>>();
        Double min = null;
        Double max = null;
        for (METRICS metric : METRICS.values()) {
            switch (metric) {
                case HEAP_USED:
                    min = 230.4;
                    max = 950.2;
                    break;
                case HEAP_COMMITTED:
                    min = 1024.4;
                    max = 1569.3;
                    break;
                case HEAP_MAX:
                    min = 2048.0;
                    max = 2048.0;
                    break;
                case NONHEAP_USED:
                    min = 128.5;
                    max = 256.2;
                    break;
                case NONHEAP_COMMITTED:
                    min = 298.5;
                    max = 410.2;
                    break;
                default:
                    break;
            }
            ArrayList<Double> doubleList = new ArrayList<Double>();
            for (int count = 0; count < DATA_SIZE; count++) {
                doubleList.add(getRandomDouble(min, max) * DATA_MB);
            }
            metricsDataMap.put(metric.value(resourceId), doubleList);
        }
    }

}
