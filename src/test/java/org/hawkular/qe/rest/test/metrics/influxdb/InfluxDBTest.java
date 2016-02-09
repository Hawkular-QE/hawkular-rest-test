/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
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
package org.hawkular.qe.rest.test.metrics.influxdb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hawkular.qe.rest.metrics.influxdb.InfluxdbBase;
import org.hawkular.qe.rest.metrics.model.RandomDoubleData;
import org.hawkular.qe.rest.metrics.model.TimeseriesData;
import org.influxdb.dto.Serie;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class InfluxDBTest extends InfluxdbBase {
    public static final String DB_NAME = "resetAutomation";
    public static final String MEASUREMENT = "cpu";
    List<TimeseriesData> timeseriesDataList = null;
    RandomDoubleData randomDoubleData = null;
    public static final Double DELTA = 1e-7;

    public static final String WHERE_QUERY = " where time > now() - 30m group by time(30m)";

    static final int MIN_COUNT = 10;
    static final int MAX_COUNT = 1000;
    static final Double MIN_LIMIT = -10000000.0;
    static final Double MAX_LIMIT = 10000000.0;

    @Test(priority = 0)
    public void writeTest() {
        double minLimit = getRandomDouble(MIN_LIMIT, MAX_LIMIT);
        randomDoubleData = RandomDoubleData.builder()
                .tenantId(getDatabaseName(DB_NAME))
                .measurement(MEASUREMENT)
                .count(getRandomInteger(MIN_COUNT, MAX_COUNT))
                .delay(1000l)
                .minLimit(minLimit)
                .maxLimit(getRandomDouble(minLimit, MAX_LIMIT))
                .build();
        _logger.debug("Random data:{}", randomDoubleData);
        timeseriesDataList = getDoubleData(randomDoubleData);

        for (TimeseriesData timeseriesData : timeseriesDataList) {
            getInfluxDB().write(
                    getDatabaseName(DB_NAME),
                    TimeUnit.MILLISECONDS,
                    new Serie.Builder(randomDoubleData.getMeasurement())
                            .columns("time", "value")
                            .values(timeseriesData.getTimestamp(), timeseriesData.getData())
                            .build());
        }
    }

    @Test(priority = 1, dependsOnMethods = { "writeTest" })
    public void validateSelect() {
        List<Serie> series = getInfluxDB().query(getDatabaseName(DB_NAME),
                "select * from " + MEASUREMENT, TimeUnit.MILLISECONDS);
        _logger.debug("Query result:{}", series);
        validateDoubleList(randomDoubleData, timeseriesDataList, series);
    }

    @Test(priority = 2, dependsOnMethods = { "writeTest" })
    public void validateMinimum() {
        List<Serie> series = getInfluxDB().query(getDatabaseName(DB_NAME),
                "select min(value) from " + MEASUREMENT + WHERE_QUERY, TimeUnit.MILLISECONDS);
        _logger.debug("Query result:{}", series);
        Double actual = getValue(series, "min");
        Double expected = getValue(timeseriesDataList, FUNCTION_TYPE.MIN);
        _logger.debug("Values, expected:{}, actual:{}", expected, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 2, dependsOnMethods = { "writeTest" })
    public void validateMaximum() {
        List<Serie> series = getInfluxDB().query(getDatabaseName(DB_NAME),
                "select max(value) from " + MEASUREMENT + WHERE_QUERY, TimeUnit.MILLISECONDS);
        _logger.debug("Query result:{}", series);
        Double actual = getValue(series, "max");
        Double expected = getValue(timeseriesDataList, FUNCTION_TYPE.MAX);
        _logger.debug("Values, expected:{}, actual:{}", expected, actual);
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 2, dependsOnMethods = { "writeTest" })
    public void validateMean() {
        List<Serie> series = getInfluxDB().query(getDatabaseName(DB_NAME),
                "select mean(value) from " + MEASUREMENT + WHERE_QUERY, TimeUnit.MILLISECONDS);
        _logger.debug("Query result:{}", series);
        Double actual = getValue(series, "mean");
        Double expected = getValue(timeseriesDataList, FUNCTION_TYPE.MEAN);
        _logger.debug("Values, expected:{}, actual:{}", expected, actual);
        Assert.assertEquals(actual, expected, DELTA);
    }

    @Test(priority = 2, dependsOnMethods = { "writeTest" })
    public void validateSum() {
        List<Serie> series = getInfluxDB().query(getDatabaseName(DB_NAME),
                "select sum(value) from " + MEASUREMENT + WHERE_QUERY, TimeUnit.MILLISECONDS);
        _logger.debug("Query result:{}", series);
        Double actual = getValue(series, "sum");
        Double expected = getValue(timeseriesDataList, FUNCTION_TYPE.SUM);
        _logger.debug("Values, expected:{}, actual:{}", expected, actual);
        Assert.assertEquals(actual, expected, DELTA);
    }
}
