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

public class HawkularRestTestProperties {
    private String hawkularUrl;
    private String hawkularUsername;
    private String hawkularPassword;

    public void loadProperties() {
        this.setHawkularUrl(System.getProperty("hawkular.url"));
        this.setHawkularUsername(System.getProperty("hawkular.username"));
        this.setHawkularPassword(System.getProperty("hawkular.password"));
    }

    public String getHawkularUrl() {
        if (hawkularUrl.endsWith("/")) {
            return hawkularUrl.substring(0, hawkularUrl.length() - 1);
        }
        return hawkularUrl;
    }

    public void setHawkularUrl(String hawkularUrl) {
        this.hawkularUrl = hawkularUrl;
    }

    public String getHawkularPassword() {
        return hawkularPassword;
    }

    public void setHawkularPassword(String hawkularPassword) {
        this.hawkularPassword = hawkularPassword;
    }

    public String getHawkularUsername() {
        return hawkularUsername;
    }

    public void setHawkularUsername(String hawkularUsername) {
        this.hawkularUsername = hawkularUsername;
    }
}
