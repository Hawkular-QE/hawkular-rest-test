package org.hawkular.qe.rest.model;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.alerts.api.model.condition.Alert;
import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.data.MixedData;
import org.hawkular.alerts.api.model.trigger.Match;

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
