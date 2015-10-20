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

import java.util.ArrayList;
import java.util.List;

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
    private List<Condition> triggeredConditions = new ArrayList<Condition>();
    private List<Alert> alerts;

    private ConditionsModel() {

    }

    private ConditionsModel(List<Condition> conditions, MixedData mixedData, Match match) {
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

    public List<Condition> getTriggeredConditions() {
        return triggeredConditions;
    }
}
