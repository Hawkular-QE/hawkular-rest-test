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
package org.hawkular.qe.rest.alerts;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.alerts.api.model.condition.Alert;
import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.data.MixedData;
import org.hawkular.alerts.api.model.data.NumericData;
import org.hawkular.alerts.api.model.trigger.Mode;
import org.hawkular.alerts.api.model.trigger.Trigger;
import org.hawkular.client.ClientResponse;
import org.hawkular.client.alert.model.AlertsParams;
import org.hawkular.inventory.api.model.Tenant;
import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.hawkular.qe.rest.model.RandomDouble;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class AlertsTestBase extends HawkularRestTestBase {

    public static Tenant TENANT;

    @BeforeClass
    public void loadBefore() {
        TENANT = getHawkularClient().inventory().getTenant().getEntity();
    }

    public void createTrigger(Trigger trigger) {
        ClientResponse<Trigger> triggerCreateResult = getHawkularClient().alerts().createTrigger(trigger);
        _logger.debug("Trigger Creation Status:" + triggerCreateResult);
        Assert.assertTrue(triggerCreateResult.isSuccess());
    }

    public void updateTrigger(String triggerId, Trigger trigger) {
        ClientResponse<String> triggerUpdateResult = getHawkularClient().alerts().updateTrigger(triggerId, trigger);
        _logger.debug("Trigger Update Status:" + triggerUpdateResult);
        Assert.assertTrue(triggerUpdateResult.isSuccess());
    }

    public List<Condition> getConditions(Trigger trigger) {
        //Get Conditions
        ClientResponse<List<Condition>> conditionsResult = getHawkularClient().alerts().getTriggerConditions(
                trigger.getId());
        _logger.debug("Conditions get status: " + conditionsResult);
        Assert.assertTrue(conditionsResult.isSuccess());
        return conditionsResult.getEntity();
    }

    public List<Condition> addTriggerCondition(Trigger trigger, List<Condition> conditions, Mode mode) {
        // Add/Clear Conditions
        ClientResponse<List<Condition>> conditionsResult = getHawkularClient().alerts().setConditions(trigger.getId(),
                mode.name(), conditions);
        _logger.debug("Add/Clear Conditions Status: " + conditionsResult);
        Assert.assertTrue(conditionsResult.isSuccess());
        return conditionsResult.getEntity();
    }

    public void deleteTrigger(Trigger trigger) {
        //Delete trigger
        ClientResponse<String> deleteResult = getHawkularClient().alerts().deleteTrigger(trigger.getId());
        _logger.debug("Trigger[" + trigger + "] Delete Status: " + deleteResult);
        Assert.assertTrue(deleteResult.isSuccess());
    }

    public void sendData(MixedData mixedData) {
        //Send Mixed data
        ClientResponse<String> sendDataResult = getHawkularClient().alerts().sendData(mixedData);
        _logger.debug("Send Alert Mixed data Status: " + sendDataResult);
        Assert.assertTrue(sendDataResult.isSuccess());
    }

    public List<Alert> getAlerts(AlertsParams alertsParams) {
        //Send Mixed data
        ClientResponse<List<Alert>> alertsResult = getHawkularClient().alerts().findAlerts(alertsParams);
        _logger.debug("Alert Status: " + alertsResult);
        Assert.assertTrue(alertsResult.isSuccess());
        return alertsResult.getEntity();
    }

    public void acknowledgeAlerts(String alertIds, String ackBy, String ackNotes) {
        ClientResponse<String> acknowleResult = getHawkularClient().alerts().ackAlerts(alertIds, ackBy, ackNotes);
        _logger.debug("Acknowledge Status:[{}]", acknowleResult);
        Assert.assertTrue(acknowleResult.isSuccess());
    }

    public Integer deleteAlerts(AlertsParams alertsParams) {
        ClientResponse<Integer> deleteResult = getHawkularClient().alerts().deleteAlerts(alertsParams);
        _logger.debug("Delete Status:[{}]", deleteResult);
        Assert.assertTrue(deleteResult.isSuccess());
        return deleteResult.getEntity();

    }

    public String getAlertIds(List<Alert> alerts) {
        StringBuilder alertIds = new StringBuilder();
        for (Alert alert : alerts) {
            if (alertIds.length() == 0) {
                alertIds.append(alert.getAlertId());
            } else {
                alertIds.append(",").append(alert.getAlertId());
            }
        }
        return alertIds.toString();
    }

    public List<NumericData> getNumericData(RandomDouble randomDouble) {
        List<NumericData> numericDataList = new ArrayList<NumericData>();
        long firstDataTimestamp = System.currentTimeMillis() - (randomDouble.getCount() * randomDouble.getDelay());
        for (long count = 0; count < randomDouble.getCount(); count++) {
            numericDataList.add(new NumericData(randomDouble.getId(), firstDataTimestamp
                    + (count * randomDouble.getDelay()),
                    getRandomDouble(randomDouble.getMinLimit(), randomDouble.getMaxLimit())));
        }
        return numericDataList;
    }
}
