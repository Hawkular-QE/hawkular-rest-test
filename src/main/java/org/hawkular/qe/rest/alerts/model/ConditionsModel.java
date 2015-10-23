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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hawkular.alerts.api.model.condition.Alert;
import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.data.MixedData;
import org.hawkular.alerts.api.model.trigger.Match;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ConditionsModel {

    private String triggerId;
    private List<Condition> conditions;
    private MixedData mixedData;
    private Match match;
    private Map<String, TriggeredCondition> triggeredConditions = new HashMap<String, TriggeredCondition>();
    private List<Alert> alerts;

    public ConditionsModel() {

    }

    public ConditionsModel(List<Condition> conditions, MixedData mixedData, Match match) {
        this.conditions = conditions;
        this.mixedData = mixedData;
        this.match = match;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public MixedData getMixedData() {
        return mixedData;
    }

    public void setMixedData(MixedData mixedData) {
        this.mixedData = mixedData;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Map<String, TriggeredCondition> getTriggeredConditionsMap() {
        return triggeredConditions;
    }

    public TriggeredCondition getTriggeredCondition(String conditionId) {
        if (triggeredConditions.get(conditionId) == null) {
            triggeredConditions.put(conditionId, new TriggeredCondition());
        }
        return triggeredConditions.get(conditionId);
    }

    public TriggeredCondition getTriggeredCondition(Condition condition) {
        if (triggeredConditions.get(condition.getConditionId()) == null) {
            triggeredConditions.put(condition.getConditionId(), new TriggeredCondition(condition));
        }
        return triggeredConditions.get(condition.getConditionId());
    }

    public void increaseTriggeredConditionCount(Condition condition) {
        this.getTriggeredCondition(condition).increaseTriggeredCount();
    }

    public int getTotalTriggeredCount() {
        int count = 0;
        for (String conditionKey : triggeredConditions.keySet()) {
            count += triggeredConditions.get(conditionKey).getTriggeredCount();
        }
        return count;
    }
}
