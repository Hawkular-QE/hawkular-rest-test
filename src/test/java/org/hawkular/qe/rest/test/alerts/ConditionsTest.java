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

import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition.Operator;
import org.hawkular.alerts.api.model.trigger.Match;
import org.hawkular.alerts.api.model.trigger.Mode;
import org.hawkular.alerts.api.model.trigger.Trigger;
import org.hawkular.client.metrics.model.GaugeDataPoint;
import org.hawkular.qe.rest.alerts.AlertsTestBase;
import org.hawkular.qe.rest.mapper.RandomDouble;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ConditionsTest extends AlertsTestBase {

    @Test(priority = 1)
    public void testThresholdConditionAny() {
        String dataId = "metric-data-id-" + getRandomId(); //MetricId also called dataId
        String triggerId = "trigger-id-" + getRandomId();

        Trigger trigger = new Trigger(triggerId, "Trigger Name " + triggerId);
        trigger.setFiringMatch(Match.ANY);

        //Create Trigger
        createTrigger(trigger);

        //Setup new conditions
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new ThresholdCondition(triggerId, dataId, Operator.GT, 34.56));
        conditions.add(new ThresholdCondition(triggerId, dataId, Operator.LT, 40.75));

        //Add Conditions in to trigger
        addTriggerCondition(trigger, conditions, Mode.FIRING);

        //Enable Trigger
        trigger.setEnabled(true);

        //Update Trigger
        updateTrigger(trigger.getId(), trigger);

        //Prepare data
        RandomDouble randomDouble = new RandomDouble(TENANT.getId(), dataId, 0.0, 100.0, 10, 1000);
        List<GaugeDataPoint> dataPoints = getRandomGaugeDataPoints(randomDouble);

        //Add data
        addGaugeData(dataId, dataPoints);

        //Check is there any alert

        //Acknowledge alert

        //Delete alert

        //Remove Triggers
    }

}
