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
import org.hawkular.qe.rest.alerts.AlertsTestBase;
import org.hawkular.qe.rest.mapper.RandomDouble;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ConditionsTest extends AlertsTestBase {

    @Test(priority = 1)
    public void testThresholdConditionGT() {
        String dataId = "metric-data-id-" + getRandomId(); //MetricId also called dataId
        String triggerId = "trigger-id-" + getRandomId();

        double gtValue = getRandomDouble(0.0, 100.0);

        Trigger trigger = new Trigger(triggerId, "Trigger Name " + triggerId);
        trigger.setFiringMatch(Match.ANY);

        //Create Trigger
        createTrigger(trigger);

        //Setup new conditions
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new ThresholdCondition(triggerId, Mode.FIRING, dataId, Operator.GT, gtValue));

        //Add Conditions in to trigger
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Enable Trigger
        trigger.setEnabled(true);

        //Update Trigger
        updateTrigger(trigger.getId(), trigger);

        //Add Mixed Data

        //Prepare data
        RandomDouble randomDouble = new RandomDouble(TENANT.getId(), dataId, 0.0, 100.0, 10, 1000);
        List<NumericData> numericDataList = getNumericData(randomDouble);

        boolean alertShouldTrigger = getGtValue(numericDataList) >= gtValue;

        MixedData mixedData = new MixedData();
        mixedData.setNumericData(numericDataList);

        //Add data
        sendData(mixedData);

        //Check is there any alert, wait and check for at least 10 seconds
        List<Alert> alerts = null;
        for (int count = 0; count < 20; count++) {
            alerts = getAlerts(triggerId);
            if (alerts != null && !alerts.isEmpty()) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                _logger.error("Exception on thread sleep, ", ex);
            }
        }

        if (alerts != null && !alerts.isEmpty()) {
            Assert.assertTrue(alertShouldTrigger, "Alerts should be not triggered");
        } else {
            Assert.assertFalse(alertShouldTrigger, "Alerts should be triggered");
        }

        //Acknowledge alert
        String alertIds = getAlertIds(alerts);

        if (alertIds != null) {
            //Acknowledge alerts
            acknowledgeAlerts(alertIds, "rest-test-automation", "This alert acknowledged by REST test automation");
            //Delete alert
            int deleteAlertsCount = deleteAlerts(alertIds);
            _logger.debug("Number of alerts deleted:{}", deleteAlertsCount);
            Assert.assertEquals(deleteAlertsCount, alerts.size());
        }

        //Remove Conditions
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Remove Trigger
        deleteTrigger(trigger);
    }

}
