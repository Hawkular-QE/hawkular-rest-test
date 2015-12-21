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
package org.hawkular.qe.rest.test.inventory.unittest;

import java.util.ArrayList;
import java.util.List;

import org.hawkular.client.ClientResponse;
import org.hawkular.inventory.api.model.CanonicalPath;
import org.hawkular.inventory.api.model.Feed;
import org.hawkular.qe.rest.inventory.InventoryTestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class FeedTest extends InventoryTestBase {
    private static final Logger _logger = LoggerFactory.getLogger(FeedTest.class);
    private static final String ENVIRONMENT_ID = "environment-feed-test";

    @BeforeClass
    public void setup() {
        Assert.assertTrue(getHawkularClient().inventory().createEnvironment(ENVIRONMENT_ID).isSuccess());
    }

    @AfterClass
    public void clean() {
        Assert.assertTrue(getHawkularClient().inventory().deleteEnvironment(ENVIRONMENT_ID).isSuccess());
    }

    @Test(dataProvider = "feedDataProvider", priority = 1)
    public void registerTest(Feed feed) {
        _logger.debug("Creating Feed[{}] under [tenant:{},environment:{}]", feed.getId(), feed.getPath().ids()
                .getTenantId(),
                feed.getPath().ids().getEnvironmentId());
        ClientResponse<String> clientResponse = getHawkularClient().inventory().registerFeed(feed);
        _logger.debug("Client Response:[{}]", clientResponse);
        Assert.assertTrue(clientResponse.isSuccess());
    }

    @SuppressWarnings("unchecked")
    @Test(priority = 2)
    public void listTest() {
        List<Feed> feeds = getHawkularClient().inventory().getAllFeeds().getEntity();
        Assert.assertNotNull(feeds);
        _logger.debug("Number of Metrics:[{}], list:[{}]", feeds.size(), feeds);
        assertFeedsList(feeds, (List<Feed>) getMetrics());
    }

    @Test(dataProvider = "feedDataProvider", priority = 3)
    public void getTest(Feed feed) {
        _logger.debug("Fetching feed[{}] under [tenant:{},environment:{}]", feed.getId(), feed.getPath().ids()
                .getTenantId(),
                feed.getPath().ids().getEnvironmentId());
        Feed feedRx = getHawkularClient().inventory().getFeed(feed).getEntity();
        assertFeeds(feedRx, feed);
    }

    @Test(dataProvider = "feedDataProvider", priority = 4)
    public void deleteTest(Feed feed) {
        _logger.debug("Deleting feed[{}] under [tenant:{},environment:{}]", feed.getId(), feed.getPath().ids()
                .getTenantId(),
                feed.getPath().ids().getEnvironmentId());
        Assert.assertTrue(getHawkularClient().inventory().deleteFeed(feed).isSuccess());
    }

    @SuppressWarnings("unchecked")
    @DataProvider(name = "feedDataProvider")
    public Object[][] resourceDataProvider() {
        return this.get2dArray((List<Object>) getMetrics());
    }

    public static List<? extends Object> getMetrics() {
        List<Feed> feeds = new ArrayList<>();
        feeds.add(new Feed(CanonicalPath.of().tenant(TENANT.getId()).feed("feed1").get()));
        feeds.add(new Feed(CanonicalPath.of().tenant(TENANT.getId()).feed("_f").get()));
        feeds.add(new Feed(CanonicalPath.of().tenant(TENANT.getId()).feed("3feed-_").get()));
        feeds.add(new Feed(CanonicalPath.of().tenant(TENANT.getId()).feed("feed-2323").get()));
        feeds.add(new Feed(CanonicalPath.of().tenant(TENANT.getId())
                .feed("feed-withlooooooooooooooooooooooooooooooooongstring").get()));
        return feeds;
    }

}
