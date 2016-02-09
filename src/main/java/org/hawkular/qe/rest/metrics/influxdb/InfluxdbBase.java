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
package org.hawkular.qe.rest.metrics.influxdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hawkular.qe.rest.base.HawkularRestTestBase;
import org.hawkular.qe.rest.metrics.model.RandomDoubleData;
import org.hawkular.qe.rest.metrics.model.TimeseriesData;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.dto.Serie;
import org.influxdb.InfluxDBFactory;
import org.testng.Assert;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class InfluxdbBase extends HawkularRestTestBase {
    private static InfluxDB influxDB = null;
    public static final String INFLUXDB_REST_PATH = "hawkular/metrics";
    static final String DB_PREFIX = getRandomAlphanumericString();

    public static InfluxDB getInfluxDB() {
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(
                    getHawkularRestTestProperties().getHawkularUrl() + "/" + INFLUXDB_REST_PATH,
                    getHawkularRestTestProperties().getHawkularUsername(),
                    getHawkularRestTestProperties().getHawkularPassword());
            if (_logger.isDebugEnabled()) {
                influxDB.setLogLevel(LogLevel.FULL);
            } else if (_logger.isInfoEnabled()) {
                influxDB.setLogLevel(LogLevel.BASIC);
            } else {
                influxDB.setLogLevel(LogLevel.HEADERS);
            }

        }
        return influxDB;
    }

    public static String getDatabaseName(String dbName) {
        return DB_PREFIX + dbName;
    }

    public List<TimeseriesData> getDoubleData(RandomDoubleData randomDoubleData) {

        List<TimeseriesData> timeseriesData = new ArrayList<TimeseriesData>();
        long firstDataTimestamp = System.currentTimeMillis()
                - (randomDoubleData.getCount() * randomDoubleData.getDelay());
        for (long count = 0; count < randomDoubleData.getCount(); count++) {
            timeseriesData.add(
                    TimeseriesData.builder()
                            .timestamp(firstDataTimestamp + (count * randomDoubleData.getDelay()))
                            .data(getRandomDouble(randomDoubleData.getMinLimit(), randomDoubleData.getMaxLimit()))
                            .build());
        }
        return timeseriesData;
    }

    public enum FUNCTION_TYPE {
        MIN, MAX, MEAN, SUM, COUNT, FIRST, LAST, TOP, BOTTOM;

    }

    public Double getValue(List<TimeseriesData> timeseriesDataList, FUNCTION_TYPE type) {
        Double returnValue = null;

        switch (type) {
            case MIN:
                for (TimeseriesData timeseriesData : timeseriesDataList) {
                    if (returnValue != null) {
                        if (timeseriesData.getData() < returnValue) {
                            returnValue = timeseriesData.getData();
                        }
                    } else {
                        returnValue = timeseriesData.getData();
                    }
                }
                break;
            case MAX:
                for (TimeseriesData timeseriesData : timeseriesDataList) {
                    if (returnValue != null) {
                        if (timeseriesData.getData() > returnValue) {
                            returnValue = timeseriesData.getData();
                        }
                    } else {
                        returnValue = timeseriesData.getData();
                    }
                }
                break;
            case MEAN:
                for (TimeseriesData timeseriesData : timeseriesDataList) {
                    if (returnValue != null) {
                        returnValue += timeseriesData.getData();
                    } else {
                        returnValue = timeseriesData.getData();
                    }
                }
                returnValue = returnValue / timeseriesDataList.size();
                break;
            case SUM:
                for (TimeseriesData timeseriesData : timeseriesDataList) {
                    if (returnValue != null) {
                        returnValue += timeseriesData.getData();
                    } else {
                        returnValue = timeseriesData.getData();
                    }
                }
                break;

            default:
                break;
        }
        return returnValue;
    }

    public Double getValue(List<Serie> series, String key) {
        Assert.assertNotNull(series);
        Assert.assertTrue(series.size() > 0);
        return (Double) series.get(0).getRows().get(0).get(key);
    }

    public void validateDoubleList(RandomDoubleData randomDoubleData, List<TimeseriesData> timeseriesDataList,
            List<Serie> series) {
        Assert.assertNotNull(series);
        Assert.assertNotNull(timeseriesDataList);
        Assert.assertTrue(series.size() > 0);
        Assert.assertTrue(timeseriesDataList.size() > 0);
        Serie serie = series.get(0);
        Assert.assertEquals(randomDoubleData.getMeasurement(), serie.getName());

        List<Map<String, Object>> values = serie.getRows();

        Assert.assertEquals(values.size(), timeseriesDataList.size(), "Returned row count different");

        for (TimeseriesData timeseriesData : timeseriesDataList) {
            boolean found = false;
            for (Map<String, Object> map : values) {
                if (((Double) map.get("time")).longValue() == timeseriesData.getTimestamp()) {
                    Assert.assertEquals(timeseriesData.getData(), map.get("value"));
                    values.remove(map);
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }
}
