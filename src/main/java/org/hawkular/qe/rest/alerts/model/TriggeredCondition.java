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
package org.hawkular.qe.rest.alerts.model;

import org.hawkular.alerts.api.model.condition.Condition;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class TriggeredCondition {
    private Condition condition;
    private int triggeredCount = 0;

    public TriggeredCondition() {

    }

    public TriggeredCondition(Condition condition) {
        this.condition = condition;
    }

    public String toString() {
        return new StringBuffer()
                .append("Triggered Count: ").append(this.triggeredCount)
                .append(", Condition:[").append(this.condition).append("]")
                .toString();
    }

    public void increaseTriggeredCount() {
        this.triggeredCount++;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getTriggeredCount() {
        return triggeredCount;
    }

    public void setTriggeredCount(int triggeredCount) {
        this.triggeredCount = triggeredCount;
    }
}
