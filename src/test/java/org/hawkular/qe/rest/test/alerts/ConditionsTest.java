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
package org.hawkular.qe.rest.test.alerts;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.alerts.api.model.condition.Alert;
import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition.Operator;
import org.hawkular.alerts.api.model.data.MixedData;
import org.hawkular.alerts.api.model.data.NumericData;
import org.hawkular.alerts.api.model.trigger.Match;
import org.hawkular.alerts.api.model.trigger.Mode;
import org.hawkular.alerts.api.model.trigger.Trigger;
import org.hawkular.client.alert.model.AlertsParams;
import org.hawkular.qe.rest.alerts.ValidateConditions;
import org.hawkular.qe.rest.alerts.model.ConditionsModel;
import org.hawkular.qe.rest.model.RandomDouble;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ConditionsTest extends ValidateConditions {

    @Test(priority = 1)
    public void testThresholdConditionGT() {
        testThresholdCondition(Operator.GT, Match.ANY);
    }

    @Test(priority = 2)
    public void testThresholdConditionGTE() {
        testThresholdCondition(Operator.GTE, Match.ANY);
    }

    @Test(priority = 3)
    public void testThresholdConditionLT() {
        testThresholdCondition(Operator.LT, Match.ANY);
    }

    @Test(priority = 4)
    public void testThresholdConditionLTE() {
        testThresholdCondition(Operator.LTE, Match.ANY);
    }

    public void testThresholdCondition(Operator operator, Match match) {
        _logger.debug("Testing condition:{}", operator.toString());
        String dataId = "metric-data-id-" + getRandomId(); //MetricId also called dataId
        String triggerId = "trigger-id-" + getRandomId();

        double valueMin = getRandomDouble(0.0, 1000.0);
        double valueMax = getRandomDouble(valueMin, 1000000.0);

        _logger.debug("Selected Values[Min:{}, Max:{}]", valueMin, valueMax);

        Trigger trigger = new Trigger(triggerId, "Trigger Name " + triggerId);
        trigger.setFiringMatch(match);

        //Create Trigger
        createTrigger(trigger);

        //Setup new conditions
        List<Condition> conditions = new ArrayList<>();
        conditions.add(
                new ThresholdCondition(triggerId, Mode.FIRING, dataId, operator,
                        getRandomDouble(valueMin, valueMax)));

        //Add Conditions in to trigger
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Enable Trigger
        trigger.setEnabled(true);

        //Update Trigger
        updateTrigger(trigger.getId(), trigger);

        //Add Mixed Data

        //Prepare data
        RandomDouble randomDouble = new RandomDouble(TENANT.getId(), dataId, valueMin, valueMax, 10, 1000);
        List<NumericData> numericDataList = getNumericData(randomDouble);

        MixedData mixedData = new MixedData();
        mixedData.setNumericData(numericDataList);

        validateAndDelete(trigger, conditions, mixedData, Match.ANY);

    }

    private void validateAndDelete(Trigger trigger, List<Condition> conditions, MixedData mixedData, Match match) {
        //Validate locally
        ConditionsModel conditionsModel = new ConditionsModel(conditions, mixedData, match);
        validateConditions(conditionsModel);

        _logger.debug("Local validations Result:[{}]", conditionsModel.getTriggeredConditionsMap());

        //Add data
        sendData(mixedData);

        //Update Alerts Params
        AlertsParams alertsParams = new AlertsParams();
        alertsParams.setTriggerIds(trigger.getId());

        //Check is there any alert, wait and check for at least 10 seconds
        List<Alert> alerts = null;
        for (int count = 0; count < 20; count++) {
            alerts = getAlerts(alertsParams);
            if (alerts != null && !alerts.isEmpty()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                _logger.error("Exception on thread sleep, ", ex);
            }
        }
        if (alerts == null) {
            alerts = new ArrayList<Alert>();
        }

        Assert.assertEquals(alerts.size(), conditionsModel.getTotalTriggeredCount(), "Triggers count validation");

        if (alerts.size() > 0) {
            //Acknowledge alert
            String alertIds = getAlertIds(alerts);
            alertsParams.setAlertIds(alertIds);

            if (alertIds != null) {
                //Acknowledge alerts
                acknowledgeAlerts(alertIds, "rest-test-automation", "This alert acknowledged by REST test automation");
                //Delete alert
                int deleteAlertsCount = deleteAlerts(alertsParams);
                _logger.debug("Number of alerts deleted:{}", deleteAlertsCount);
                Assert.assertEquals(deleteAlertsCount, alerts.size());
            }
        }

        //Remove Conditions
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Remove Trigger
        deleteTrigger(trigger);

    }
}
