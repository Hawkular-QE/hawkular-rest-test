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
import org.hawkular.alerts.api.model.condition.AvailabilityCondition;
import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition.Operator;
import org.hawkular.alerts.api.model.condition.ThresholdRangeCondition;
import org.hawkular.alerts.api.model.data.Availability;
import org.hawkular.alerts.api.model.data.MixedData;
import org.hawkular.alerts.api.model.data.NumericData;
import org.hawkular.alerts.api.model.trigger.Match;
import org.hawkular.alerts.api.model.trigger.Mode;
import org.hawkular.alerts.api.model.trigger.Trigger;
import org.hawkular.client.alert.model.AlertsParams;
import org.hawkular.qe.rest.alerts.ValidateConditions;
import org.hawkular.qe.rest.alerts.model.ConditionsModel;
import org.hawkular.qe.rest.model.RandomAvailability;
import org.hawkular.qe.rest.model.RandomDouble;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ConditionsTest extends ValidateConditions {
    public static final long delayTime = 1000;

    public static final double doubleMinValue = 0.0;
    public static final double doubleMaxValue = 1000000.0;

    public static final int dataCountMin = 2;
    public static final int dataCountMax = 30;

    @Test(priority = 0)
    public void testThresholdConditionGT() {
        testThresholdCondition(Operator.GT, Match.ANY);
    }

    @Test(priority = 0)
    public void testThresholdConditionGTE() {
        testThresholdCondition(Operator.GTE, Match.ANY);
    }

    @Test(priority = 0)
    public void testThresholdConditionLT() {
        testThresholdCondition(Operator.LT, Match.ANY);
    }

    @Test(priority = 0)
    public void testThresholdConditionLTE() {
        testThresholdCondition(Operator.LTE, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLinHinRin() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                true, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLinHexRin() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                true, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLexHexRin() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                true, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLexHinRin() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                true, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLinHinRout() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                false, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLinHexRout() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                false, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLexHexRout() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                false, Match.ANY);
    }

    @Test(priority = 1)
    public void testThresholdRangeConditionLexHinRout() {
        testThresholdRangeCondition(
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.EXCLUSIVE,
                org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator.INCLUSIVE,
                false, Match.ANY);
    }

    @Test(priority = 2)
    public void testAvailabilityUp() {
        testAvailabilityCondition(
                org.hawkular.alerts.api.model.condition.AvailabilityCondition.Operator.UP,
                Match.ANY);
    }

    @Test(priority = 2)
    public void testAvailabilityDown() {
        testAvailabilityCondition(
                org.hawkular.alerts.api.model.condition.AvailabilityCondition.Operator.DOWN,
                Match.ANY);
    }

    @Test(priority = 2)
    public void testAvailabilityNotUp() {
        testAvailabilityCondition(
                org.hawkular.alerts.api.model.condition.AvailabilityCondition.Operator.NOT_UP,
                Match.ANY);
    }

    public void testThresholdCondition(Operator operator, Match match) {
        _logger.debug("Testing condition:{}", operator.toString());
        String dataId = "metric-data-id-" + getRandomId(); //MetricId also called dataId
        String triggerId = "trigger-id-" + operator.toString() + "-" + getRandomId();

        double valueMin = getRandomDouble(doubleMinValue, 1000.0);
        double valueMax = getRandomDouble(valueMin, doubleMaxValue);

        _logger.debug("Selected Values[Min:{}, Max:{}]", valueMin, valueMax);

        Trigger trigger = new Trigger(triggerId, "Trigger Name Threshold-" + triggerId);
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

        //Prepare data
        RandomDouble randomDouble = new RandomDouble(TENANT.getId(), dataId, valueMin, valueMax,
                getRandomInteger(dataCountMin, dataCountMax), delayTime);
        List<NumericData> numericDataList = getNumericData(randomDouble);

        MixedData mixedData = new MixedData();
        mixedData.setNumericData(numericDataList);

        validateAndDelete(trigger, conditions, mixedData, Match.ANY);

    }

    public void testThresholdRangeCondition(
            org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator operatorLow,
            org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator operatorHigh,
            boolean inRange,
            Match match) {

        _logger.debug("Testing condition:[OperatorLow:{}, OperatorHigh:{}, InRange:{}]",
                operatorLow.toString(), operatorHigh, inRange);

        String dataId = "metric-data-id-" + getRandomId(); //MetricId also called dataId
        String triggerId = "trigger-id-" + operatorLow.toString() + "-" + operatorLow.toString() + "-" + inRange + "-"
                + getRandomId();

        double rangeMin = getRandomDouble(doubleMinValue, 10000.0);
        double rangeMax = getRandomDouble(rangeMin, doubleMaxValue);

        _logger.debug("Selected Values[RangeMin:{}, RangeMax:{}]", rangeMin, rangeMax);

        Trigger trigger = new Trigger(triggerId, "Trigger Name ThresholdRange-" + triggerId);
        trigger.setFiringMatch(match);

        //Create Trigger
        createTrigger(trigger);

        //Setup new conditions
        List<Condition> conditions = new ArrayList<>();
        conditions.add(
                new ThresholdRangeCondition(
                        triggerId,
                        Mode.FIRING,
                        dataId,
                        operatorLow, operatorHigh,
                        rangeMin, rangeMax,
                        inRange));

        //Add Conditions in to trigger
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Enable Trigger
        trigger.setEnabled(true);

        //Update Trigger
        updateTrigger(trigger.getId(), trigger);

        //Prepare data
        RandomDouble randomDouble = new RandomDouble(TENANT.getId(), dataId, doubleMinValue, doubleMaxValue,
                getRandomInteger(dataCountMin, dataCountMax), delayTime);
        List<NumericData> numericDataList = getNumericData(randomDouble);

        MixedData mixedData = new MixedData();
        mixedData.setNumericData(numericDataList);

        validateAndDelete(trigger, conditions, mixedData, Match.ANY);

    }

    public void testAvailabilityCondition(
            org.hawkular.alerts.api.model.condition.AvailabilityCondition.Operator operator,
            Match match) {

        _logger.debug("Testing condition:[Availability:{}]", operator.toString());

        String dataId = "metric-data-id-" + getRandomId(); //MetricId also called dataId
        String triggerId = "trigger-id-" + operator.toString() + "-" + getRandomId();

        Trigger trigger = new Trigger(triggerId, "Trigger Name Availability-" + triggerId);
        trigger.setFiringMatch(match);

        //Create Trigger
        createTrigger(trigger);

        //Setup new conditions
        List<Condition> conditions = new ArrayList<>();
        conditions.add(
                new AvailabilityCondition(triggerId, Mode.FIRING, dataId, operator));

        //Add Conditions in to trigger
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Enable Trigger
        trigger.setEnabled(true);

        //Update Trigger
        updateTrigger(trigger.getId(), trigger);

        //Prepare data
        RandomAvailability randomAvailability = new RandomAvailability(TENANT.getId(), dataId, getRandomInteger(
                dataCountMin, dataCountMax), delayTime);
        List<Availability> dataList = getAvailabilityData(randomAvailability);

        MixedData mixedData = new MixedData();
        mixedData.setAvailability(dataList);

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

        Assert.assertEquals(alerts.size(), conditionsModel.getTotalTriggeredCount(),
                "Trigger[" + trigger.getId() + "] count validation");

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
