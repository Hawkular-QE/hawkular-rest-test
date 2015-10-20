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
package org.hawkular.qe.rest.model;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class RandomDouble {
    private String tenantId;
    private String id;
    private double minLimit;
    private double maxLimit;
    private long count;
    private long delay;


    public RandomDouble() {

    }

    public RandomDouble(String tenantId, String id, double minValue, double maxValue, long count, long delay) {
        this.tenantId = tenantId;
        this.id = id;
        this.minLimit = minValue;
        this.maxLimit = maxValue;
        this.count = count;
        this.delay = delay;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tenant Id:").append(this.tenantId);
        builder.append(", Id:").append(this.id);
        builder.append(", Min Value:").append(this.minLimit);
        builder.append(", Max Value:").append(this.maxLimit);
        builder.append(", Count:").append(this.count);
        builder.append(", Delay:").append(this.delay);
        return builder.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(double lowValue) {
        this.minLimit = lowValue;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(double highValue) {
        this.maxLimit = highValue;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
