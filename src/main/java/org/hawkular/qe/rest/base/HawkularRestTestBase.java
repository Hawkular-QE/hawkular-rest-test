package org.hawkular.qe.rest.base;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.hawkular.client.HawkularClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 */
public class HawkularRestTestBase {
    private static Logger _logger = LoggerFactory.getLogger(HawkularRestTestBase.class);
    private static HawkularRestTestProperties hawkularRestTestProperties;
    private static HawkularClient hawkularClient = null;
    @BeforeSuite
    public void loadInitialProperties() throws URISyntaxException, Exception{
        String logProperties = System.getProperty("log4j.file");
        String propertiesFile = System.getProperty("hawkular.file");

        if(logProperties != null){
            PropertyConfigurator.configure(new URL(logProperties));
        }else{
            PropertyConfigurator.configure(this.getClass().getClassLoader().getResource("log4j-logger.properties"));
        }
        Properties properties = new Properties();
        if(propertiesFile == null){
            properties.load(this.getClass().getClassLoader().getResourceAsStream("hawkular-rest-test.properties"));
        }else{
            properties.load(new URL(propertiesFile).openStream());
        }
        for (Object key: properties.keySet()){
            // we only load properties that are not yet defined
            if (System.getProperty(key.toString()) == null) {
                System.setProperty((String)key, properties.getProperty((String)(key)));
                _logger.debug("{}={}",key,properties.getProperty((String)(key)));
            }
        }
        HawkularRestTestProperties hawkularRestTestProperties = new HawkularRestTestProperties();
        hawkularRestTestProperties.loadProperties();
        HawkularRestTestBase.setHawkularRestTestProperties(hawkularRestTestProperties);
        hawkularClient = new HawkularClient(
                                            new URI(getHawkularRestTestProperties().getHawkularUrl()),
                                            getHawkularRestTestProperties().getHawkularUsername(),
                                            getHawkularRestTestProperties().getHawkularPassword());
        _logger.debug("'HawkularClient' client loaded...");
    }

    public Object[][] get2dArray(List<Object> list) {
        if (list.size() == 0) return new Object[0][0]; // avoid a null pointer exception
        Object[][] array = new Object[list.size()][];
        int i=0;
        for (Object item: list){
            array[i] = new Object[]{item};
            i++;
        }
        return array;
    }

    public static HawkularRestTestProperties getHawkularRestTestProperties() {
        return hawkularRestTestProperties;
    }

    public static void setHawkularRestTestProperties(
                                                     HawkularRestTestProperties hawkularRestTestProperties) {
        HawkularRestTestBase.hawkularRestTestProperties = hawkularRestTestProperties;
    }

    public static HawkularClient getHawkularClient(){
        return hawkularClient;
    }
}
