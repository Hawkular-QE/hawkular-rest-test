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

import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition;
import org.hawkular.alerts.api.model.condition.ThresholdRangeCondition;
import org.hawkular.alerts.api.model.data.NumericData;
import org.hawkular.qe.rest.alerts.model.ConditionsModel;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class ValidateConditions extends AlertsTestBase {

    public void validateConditions(ConditionsModel conditionsModel) {

        for (Condition condition : conditionsModel.getConditions()) {
            switch (condition.getType()) {
                case THRESHOLD:
                    validateThresholdCondition((ThresholdCondition) condition, conditionsModel);
                    break;
                case RANGE:
                    validateThresholdRangeCondition((ThresholdRangeCondition) condition, conditionsModel);
                    break;
                case COMPARE:
                    break;
                case AVAILABILITY:
                    break;
                case STRING:
                    break;
                case EXTERNAL:
                    break;
                default:
                    break;
            }
        }
    }

    public void validateThresholdCondition(ThresholdCondition condition, ConditionsModel conditionsModel) {
        for (NumericData data : conditionsModel.getMixedData().getNumericData()) {
            switch (condition.getOperator()) {
                case GT:
                    if (data.getValue() > condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case GTE:
                    if (data.getValue() >= condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case LT:
                    if (data.getValue() < condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case LTE:
                    if (data.getValue() <= condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void validateThresholdRangeCondition(ThresholdRangeCondition condition, ConditionsModel conditionsModel) {
        for (NumericData data : conditionsModel.getMixedData().getNumericData()) {
            switch (condition.getOperatorLow()) {
                case INCLUSIVE:
                    if (condition.isInRange() && data.getValue() >= condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    } else if (!condition.isInRange() && data.getValue() <= condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case EXCLUSIVE:
                    if (condition.isInRange() && data.getValue() > condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    } else if (!condition.isInRange() && data.getValue() < condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                default:
                    break;
            }
            switch (condition.getOperatorHigh()) {
                case INCLUSIVE:
                    if (condition.isInRange() && data.getValue() <= condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    } else if (!condition.isInRange() && data.getValue() >= condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case EXCLUSIVE:
                    if (condition.isInRange() && data.getValue() < condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    } else if (!condition.isInRange() && data.getValue() > condition.getThresholdLow()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
