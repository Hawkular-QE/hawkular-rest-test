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
package org.hawkular.qe.rest.test.alerts.unittest;

import java.util.Arrays;
import java.util.List;

import org.hawkular.client.ClientResponse;
import org.hawkular.qe.rest.alerts.AlertsTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class PluginsUnitTest extends AlertsTestBase {
    public PluginsUnitTest() throws Exception {
        super();
    }

    public static final String EMAIL_PLUGIN_NAME = "email";

    @Test(groups={"known-failure"})
    public void findPlugins() {
        ClientResponse<String[]> response = getHawkularClient().alerts().findActionPlugins();
        _logger.debug("Alert available Plugins Result: " + response.toString());
        Assert.assertTrue(response.isSuccess());
        Assert.assertTrue(response.getEntity().length > 0);
    }

    @Test(groups={"known-failure"})
    public void findEmailPlugin() {
        ClientResponse<String[]> response = getHawkularClient().alerts().getActionPlugin(EMAIL_PLUGIN_NAME);
        _logger.debug("Alert Email Plugin variables Result: " + response.toString());
        Assert.assertTrue(response.isSuccess());
        List<String> result = Arrays.asList(response.getEntity());
        Assert.assertTrue(result.contains("cc"));
        Assert.assertTrue(result.contains("from"));
        Assert.assertTrue(result.contains("from-name"));
        Assert.assertTrue(result.contains("template.hawkular.url"));
        Assert.assertTrue(result.contains("template.html"));
        Assert.assertTrue(result.contains("template.plain"));
        Assert.assertTrue(result.contains("to"));
    }

}
