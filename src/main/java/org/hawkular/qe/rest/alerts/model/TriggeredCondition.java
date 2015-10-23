package org.hawkular.qe.rest.alerts.model;

import org.hawkular.alerts.api.model.condition.Condition;

public class TriggeredCondition {
    private Condition condition;
    private int triggeredCount = 0;

    public TriggeredCondition() {

    }

    public TriggeredCondition(Condition condition) {
        this.condition = condition;
    }

    public String toString() {
        return new StringBuffer()
                .append("Triggered Count: ").append(this.triggeredCount)
                .append(", Condition:[").append(this.condition).append("]")
                .toString();
    }

    public void increaseTriggeredCount() {
        this.triggeredCount++;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getTriggeredCount() {
        return triggeredCount;
    }

    public void setTriggeredCount(int triggeredCount) {
        this.triggeredCount = triggeredCount;
    }
}
