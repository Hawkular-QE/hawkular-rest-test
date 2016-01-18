/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
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
public class RandomString {
    private String tenantId;
    private String id;
    private int count;
    private int lengthMin;
    private int lengthMax;
    private int words;
    private String matchingPattern;
    private long delay;

    public RandomString() {

    }

    public RandomString(String tenantId, String id, int lengthMin, int lengthMax, int count, int words,
            String matchingPattern, long delay) {
        this.tenantId = tenantId;
        this.id = id;
        this.count = count;
        this.lengthMin = lengthMin;
        this.lengthMax = lengthMax;
        this.words = words;
        this.matchingPattern = matchingPattern;
        this.delay = delay;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tenant Id:").append(this.tenantId);
        builder.append(", Id:").append(this.id);
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
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

    public int getLengthMin() {
        return lengthMin;
    }

    public void setLengthMin(int lengthMin) {
        this.lengthMin = lengthMin;
    }

    public int getLengthMax() {
        return lengthMax;
    }

    public void setLengthMax(int lengthMax) {
        this.lengthMax = lengthMax;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public String getMatchingPattern() {
        return matchingPattern;
    }

    public void setMatchingPattern(String matchingPattern) {
        this.matchingPattern = matchingPattern;
    }
}
