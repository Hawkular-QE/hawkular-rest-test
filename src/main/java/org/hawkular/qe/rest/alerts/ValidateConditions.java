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

import org.hawkular.alerts.api.model.condition.AvailabilityCondition;
import org.hawkular.alerts.api.model.condition.CompareCondition;
import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition;
import org.hawkular.alerts.api.model.condition.ThresholdRangeCondition;
import org.hawkular.alerts.api.model.condition.ThresholdRangeCondition.Operator;
import org.hawkular.alerts.api.model.data.AvailabilityType;
import org.hawkular.alerts.api.model.data.Data;
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
                    validateCompareCondition((CompareCondition) condition, conditionsModel);
                    break;
                case AVAILABILITY:
                    validateAvailabilityCondition((AvailabilityCondition) condition, conditionsModel);
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
        for (Data data : conditionsModel.getDatums()) {
            switch (condition.getOperator()) {
                case GT:
                    if (getDouble(data.getValue()) > condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case GTE:
                    if (getDouble(data.getValue()) >= condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case LT:
                    if (getDouble(data.getValue()) < condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case LTE:
                    if (getDouble(data.getValue()) <= condition.getThreshold()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void validateThresholdRangeCondition(ThresholdRangeCondition condition, ConditionsModel conditionsModel) {
        for (Data data : conditionsModel.getDatums()) {
            if (condition.getOperatorLow() == Operator.INCLUSIVE
                    && condition.getOperatorHigh() == Operator.INCLUSIVE) {
                if (getDouble(data.getValue()) >= condition.getThresholdLow()
                        && getDouble(data.getValue()) <= condition.getThresholdHigh()) {
                    if (condition.isInRange()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }

                } else if (!condition.isInRange()) {
                    conditionsModel.increaseTriggeredConditionCount(condition);
                }
            } else if (condition.getOperatorLow() == Operator.INCLUSIVE
                    && condition.getOperatorHigh() == Operator.EXCLUSIVE) {
                if (getDouble(data.getValue()) >= condition.getThresholdLow()
                        && getDouble(data.getValue()) < condition.getThresholdHigh()) {
                    if (condition.isInRange()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }

                } else if (!condition.isInRange()) {
                    conditionsModel.increaseTriggeredConditionCount(condition);
                }
            } else if (condition.getOperatorLow() == Operator.EXCLUSIVE
                    && condition.getOperatorHigh() == Operator.EXCLUSIVE) {
                if (getDouble(data.getValue()) > condition.getThresholdLow()
                        && getDouble(data.getValue()) < condition.getThresholdHigh()) {
                    if (condition.isInRange()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }

                } else if (!condition.isInRange()) {
                    conditionsModel.increaseTriggeredConditionCount(condition);
                }
            } else if (condition.getOperatorLow() == Operator.EXCLUSIVE
                    && condition.getOperatorHigh() == Operator.INCLUSIVE) {
                if (getDouble(data.getValue()) > condition.getThresholdLow()
                        && getDouble(data.getValue()) <= condition.getThresholdHigh()) {
                    if (condition.isInRange()) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }

                } else if (!condition.isInRange()) {
                    conditionsModel.increaseTriggeredConditionCount(condition);
                }
            }
        }
    }

    public void validateCompareCondition(CompareCondition condition, ConditionsModel conditionsModel) {
        List<Data> data1 = new ArrayList<Data>();
        List<Data> data2 = new ArrayList<Data>();
        for (Data data : conditionsModel.getDatums()) {
            if (data.getId().equals(condition.getDataId())) {
                data1.add(data);
            } else if (data.getId().equals(condition.getData2Id())) {
                data2.add(data);
            } else {
                //This data not required for us...
            }
        }
        //TODO: check data count of both id's? do we need to fail this one?? 
        //Refer this doc: https://github.com/hawkular/hawkular-alerts/blob/master/hawkular-alerts-engine/src/
        //main/resources/org/hawkular/alerts/engine/rules/ConditionMatch.drl

        for (int index = 0; index < data1.size(); index++) {
            switch (condition.getOperator()) {
                case GT:
                    if (getDouble(data1.get(index).getValue()) > (condition.getData2Multiplier() * getDouble(data2
                            .get(index).getValue()))) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case GTE:
                    if (getDouble(data1.get(index).getValue()) >= (condition.getData2Multiplier() * getDouble(data2
                            .get(index).getValue()))) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case LT:
                    if (getDouble(data1.get(index).getValue()) < (condition.getData2Multiplier() * getDouble(data2
                            .get(index).getValue()))) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case LTE:
                    if (getDouble(data1.get(index).getValue()) <= (condition.getData2Multiplier() * getDouble(data2
                            .get(index).getValue()))) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void validateAvailabilityCondition(AvailabilityCondition condition, ConditionsModel conditionsModel) {
        for (Data data : conditionsModel.getDatums()) {
            switch (condition.getOperator()) {
                case UP:
                    if (getAvailability(data.getValue()) == AvailabilityType.UP) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case DOWN:
                    if (getAvailability(data.getValue()) == AvailabilityType.DOWN) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                case NOT_UP:
                    if (getAvailability(data.getValue()) != AvailabilityType.UP) {
                        conditionsModel.increaseTriggeredConditionCount(condition);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public Double getDouble(String value) {
        return Double.valueOf(value);
    }

    public AvailabilityType getAvailability(String value) {
        return AvailabilityType.valueOf(value);
    }

}
