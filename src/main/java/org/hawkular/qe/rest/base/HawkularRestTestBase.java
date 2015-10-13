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
package org.hawkular.qe.rest.base;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.hawkular.client.HawkularClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class HawkularRestTestBase {
    public static final Logger _logger = LoggerFactory.getLogger(HawkularRestTestBase.class);
    private static HawkularRestTestProperties hawkularRestTestProperties;
    private static HawkularClient hawkularClient = null;
    static Random random = new Random();

    @BeforeSuite
    public void loadInitialProperties() throws URISyntaxException, Exception {
        String propertiesFile = System.getProperty("hawkular-rest-test-file");

        Properties properties = new Properties();
        if (propertiesFile == null) {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("hawkular-rest-test.properties"));
        } else {
            properties.load(new URL(propertiesFile).openStream());
        }
        for (Object key : properties.keySet()) {
            // we only load properties that are not yet defined
            if (System.getProperty(key.toString()) == null) {
                System.setProperty((String) key, properties.getProperty((String) (key)));
                _logger.debug("{}={}", key, properties.getProperty((String) (key)));
            }
        }
        hawkularRestTestProperties = new HawkularRestTestProperties();
        hawkularRestTestProperties.loadProperties();
        hawkularClient = new HawkularClient(
                new URI(getHawkularRestTestProperties().getHawkularUrl()),
                getHawkularRestTestProperties().getHawkularUsername(),
                getHawkularRestTestProperties().getHawkularPassword());
        _logger.debug("'HawkularClient' client loaded...");
    }

    public static HawkularRestTestProperties getHawkularRestTestProperties() {
        return hawkularRestTestProperties;
    }

    public static HawkularClient getHawkularClient() {
        return hawkularClient;
    }

    public static Double getRandomDouble(Double min, Double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static String getRandomId() {
        return RandomStringUtils.randomAlphanumeric(8).toLowerCase();
    }

    public Object[][] get2dArray(List<Object> list) {
        if (list.size() == 0)
            return new Object[0][0]; // avoid a null pointer exception
        Object[][] array = new Object[list.size()][];
        int i = 0;
        for (Object item : list) {
            array[i] = new Object[] { item };
            i++;
        }
        return array;
    }
}
