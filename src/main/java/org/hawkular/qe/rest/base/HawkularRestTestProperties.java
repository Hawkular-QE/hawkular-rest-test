package org.hawkular.qe.rest.base;

public class HawkularRestTestProperties {
    private String hawkularUrl;
    private String hawkularUsername;
    private String hawkularPassword;
    
    public void loadProperties(){
        this.setHawkularUrl(System.getProperty("hawkular.url"));
        this.setHawkularUsername(System.getProperty("hawkular.username"));
        this.setHawkularPassword(System.getProperty("hawkular.password"));
    }

    public String getHawkularUrl() {
        if(hawkularUrl.endsWith("/")){
            return hawkularUrl.substring(0, hawkularUrl.length()-1);
        }        
        return hawkularUrl;
    }

    public void setHawkularUrl(String hawkularUrl) {
        this.hawkularUrl = hawkularUrl;
    }

    public String getHawkularPassword()
    {
        return hawkularPassword;
    }

    public void setHawkularPassword( String hawkularPassword )
    {
        this.hawkularPassword = hawkularPassword;
    }

    public String getHawkularUsername()
    {
        return hawkularUsername;
    }

    public void setHawkularUsername( String hawkularUsername )
    {
        this.hawkularUsername = hawkularUsername;
    }
}
