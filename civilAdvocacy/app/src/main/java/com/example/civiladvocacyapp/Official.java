package com.example.civiladvocacyapp;

import org.json.JSONArray;

public class Official {

    //private final String title;
    //private final String name;
    //private final String party;
    //private final JSONObject normInput;
    //private final JSONObject offices;
    //private final JSONObject officials;
    private final JSONArray offices;
    private final JSONArray officials;
    private final String address;

    //Official(JSONObject normInput, String title, String name, String party) {
    //Official(JSONObject normInput, JSONArray offices, JSONArray officials) {
    //Official(JSONObject normInput, JSONObject offices, JSONObject officials) {
    Official(String address, JSONArray offices, JSONArray officials) {
        this.offices = offices;
        this.officials = officials;
        //this.normInput = normInput;
        this.address = address;
    }

    //public JSONObject getNormInput() {
    //    return normInput;
    //}

    public String getAddress(){
        return address;
    }

    public JSONArray getOffices() {
    //public JSONObject getOffices() {
        return offices;
    }

    public JSONArray getOfficials() {
    //public JSONObject getOfficials() {
        return officials;
    }

}
