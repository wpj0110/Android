package com.example.testcode;

import java.io.Serializable;

public class Covid implements Serializable {

    private final String country;
    private final String confirmed;
    private final String recovered;
    private final String critical;
    private final String deaths;
    private final String lastChange;
    private final String lastUpdate;

    Covid(String country, String confirmed, String recovered, String critical, String deaths, String lastChange, String lastUpdate){
        this.country = country;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.critical = critical;
        this.deaths = deaths;
        this.lastChange = lastChange;
        this.lastUpdate = lastUpdate;
    }

    String getCountry() { return country;}
    String getConfirmed() { return confirmed;}
    String getRecovered() { return recovered;}
    String getCritical() { return critical;}
    String getDeaths() { return deaths;}
    String getLastChange() { return lastChange;}
    String getLastUpdate() { return lastUpdate;}

}
