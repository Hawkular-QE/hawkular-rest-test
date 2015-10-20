package org.hawkular.qe.rest.alerts;

import org.hawkular.alerts.api.model.condition.Condition;
import org.hawkular.alerts.api.model.condition.ThresholdCondition;
import org.hawkular.alerts.api.model.condition.ThresholdRangeCondition;
import org.hawkular.alerts.api.model.data.NumericData;
import org.hawkular.qe.rest.model.ConditionsModel;

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
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                case GTE:
                    if (data.getValue() >= condition.getThreshold()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                case LT:
                    if (data.getValue() < condition.getThreshold()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                case LTE:
                    if (data.getValue() <= condition.getThreshold()) {
                        conditionsModel.getTriggeredConditions().add(condition);
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
                        conditionsModel.getTriggeredConditions().add(condition);
                    } else if (!condition.isInRange() && data.getValue() <= condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                case EXCLUSIVE:
                    if (condition.isInRange() && data.getValue() > condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    } else if (!condition.isInRange() && data.getValue() < condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                default:
                    break;
            }
            switch (condition.getOperatorHigh()) {
                case INCLUSIVE:
                    if (condition.isInRange() && data.getValue() <= condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    } else if (!condition.isInRange() && data.getValue() >= condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                case EXCLUSIVE:
                    if (condition.isInRange() && data.getValue() < condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    } else if (!condition.isInRange() && data.getValue() > condition.getThresholdLow()) {
                        conditionsModel.getTriggeredConditions().add(condition);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
