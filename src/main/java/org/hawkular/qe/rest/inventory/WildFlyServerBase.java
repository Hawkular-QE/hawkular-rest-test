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
package org.hawkular.qe.rest.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hawkular.client.metrics.model.AvailabilityDataPoint;
import org.hawkular.client.metrics.model.GaugeDataPoint;
import org.hawkular.inventory.api.model.CanonicalPath;
import org.hawkular.inventory.api.model.Environment;
import org.hawkular.inventory.api.model.Feed;
import org.hawkular.inventory.api.model.Metric;
import org.hawkular.inventory.api.model.MetricDataType;
import org.hawkular.inventory.api.model.MetricType;
import org.hawkular.inventory.api.model.MetricUnit;
import org.hawkular.inventory.api.model.Resource;
import org.hawkular.inventory.api.model.ResourceType;
import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.metrics.core.api.AvailabilityType;
import org.hawkular.metrics.core.api.DataPoint;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class WildFlyServerBase extends InventoryTestBase {

    private static StringBuilder stringBuilder = new StringBuilder();

    public static final String WILDFLY_MEMORY_METRICS_PREFIX_M = "MI~R~[";
    public static final String WILDFLY_MEMORY_METRICS_SUFFIX_M = "~/]~MT~WildFly Memory Metrics~";

    public static final String WILDFLY_MEMORY_METRICS_PREFIX_A = "AI~R~[";
    public static final String WILDFLY_MEMORY_METRICS_SUFFIX_A = "~/]~AT~Server Availability~";

    public static final String WILDFLY_MEMORY_METRIC_TYPE_PREFIX_M = "WildFly Memory Metrics~";
    public static final String WILDFLY_MEMORY_METRIC_TYPE_PREFIX_A = "Server Availability~";

    public enum RESOURCE_TYPES {
        DATASOURCE("Datasource"),
        DEPLOYMENT("Deployment"),
        WILDFLY_SERVER("WildFly Server"),
        SUBDEPLOYMENT("SubDeployment");

        String name;

        RESOURCE_TYPES(String name) {
            this.name = name;
        }

        public String value() {
            return name;
        }
    }

    /**
     * Note: It is very important to maintain METRIC_TYPES and METRICS enum in same order,
     * Which is linked on 'getMetric' method
     */
    public enum METRIC_TYPES {
        HEAP_USED("Heap Used"),
        HEAP_MAX("Heap Max"),
        HEAP_COMMITTED("Heap Committed"),
        NONHEAP_USED("NonHeap Used"),
        NONHEAP_COMMITTED("NonHeap Committed"),
        APP_SERVER("App Server");

        String name;

        METRIC_TYPES(String name) {
            this.name = name;
        }

        public String value() {
            stringBuilder.setLength(0);
            if (name.equals("App Server")) {
                return stringBuilder
                        .append(WILDFLY_MEMORY_METRIC_TYPE_PREFIX_A)
                        .append(name).toString();
            } else {
                return stringBuilder
                        .append(WILDFLY_MEMORY_METRIC_TYPE_PREFIX_M)
                        .append(name).toString();
            }
        }
    }

    public enum METRICS {
        HEAP_USED("Heap Used"),
        HEAP_MAX("Heap Max"),
        HEAP_COMMITTED("Heap Committed"),
        NONHEAP_USED("NonHeap Used"),
        NONHEAP_COMMITTED("NonHeap Committed"),
        APP_SERVER("App Server");

        String name;

        METRICS(String name) {
            this.name = name;
        }

        public String value(String resourceId) {
            stringBuilder.setLength(0);
            if (name.equals("App Server")) {
                return stringBuilder
                        .append(WILDFLY_MEMORY_METRICS_PREFIX_A)
                        .append(resourceId)
                        .append(WILDFLY_MEMORY_METRICS_SUFFIX_A)
                        .append(name).toString();
            } else {
                return stringBuilder
                        .append(WILDFLY_MEMORY_METRICS_PREFIX_M)
                        .append(resourceId)
                        .append(WILDFLY_MEMORY_METRICS_SUFFIX_M)
                        .append(name).toString();
            }
        }
    }

    public static String getQualifiedResourceId(String resourceId) {
        return "[" + resourceId + "~/]";
    }

    public static ResourceType getResourceType(Tenant tenant, RESOURCE_TYPES type) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("version", "1.0");
        return new ResourceType(CanonicalPath.of().tenant(tenant.getId()).resourceType(type.value()).get(), properties);
    }

    public static Resource getResource(Tenant tenant, Environment environment, Feed feed, String resourceId,
            ResourceType resourceType) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        //properties.put("name", resourceType.getId());
        return new Resource(CanonicalPath.of().tenant(tenant.getId()).environment(environment.getId())
               .resource(getQualifiedResourceId(resourceId)).get(), resourceType, properties);
    }

    public static MetricType getMetricType(Tenant tenant, METRIC_TYPES type) {
        if (type.ordinal() == METRIC_TYPES.APP_SERVER.ordinal()) {
            return new MetricType(CanonicalPath.of().tenant(tenant.getId()).metricType(type.value()).get(),
                    MetricUnit.NONE, MetricDataType.AVAILABILITY);
        } else {
            return new MetricType(CanonicalPath.of().tenant(tenant.getId()).metricType(type.value()).get(),
                    MetricUnit.NONE, MetricDataType.GAUGE);
        }
    }

    public static Metric getMetric(Tenant tenant, Environment environment,
            Feed feed, String resourceId, METRICS metric) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        //properties.put("name", METRIC_TYPES.values()[metric.ordinal()]);
        return new Metric(CanonicalPath.of().tenant(tenant.getId()).environment(environment.getId())
                .metric(metric.value(resourceId)).get(), getMetricType(tenant,
                METRIC_TYPES.values()[metric.ordinal()]), properties);
    }

    public List<AvailabilityDataPoint> getAvailablityDataPoint(AvailabilityType availabilityType) {
        List<AvailabilityDataPoint> availabilityDataPoints = new ArrayList<AvailabilityDataPoint>();
        availabilityDataPoints.add(new AvailabilityDataPoint(new DataPoint<AvailabilityType>(System
                .currentTimeMillis(), availabilityType)));

        return availabilityDataPoints;
    }

    public List<GaugeDataPoint> getHeapDataPoints(long timestamp, double value) {
        List<GaugeDataPoint> gaugeDataPoints = new ArrayList<GaugeDataPoint>();
        gaugeDataPoints.add(new GaugeDataPoint(new DataPoint<Double>(timestamp, value)));
        return gaugeDataPoints;
    }
}
