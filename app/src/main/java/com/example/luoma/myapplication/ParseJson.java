package com.example.luoma.myapplication;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuoMa on 10/25/2017.
 */

public class ParseJson {

    private String jsonData;

    public void setJsonData(String jsonLine) {
        jsonData = jsonLine;
    }

    public List<String> getNameList() {
        JsonElement jelement = new JsonParser().parse(jsonData);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("businesses");

        List<String> nameList = new ArrayList<String>();
        for (int i=0; i<jarray.size(); i++){
            jobject = jarray.get(i).getAsJsonObject();
            nameList.add(jobject.get("name").toString());
        }

        return nameList;
    }

    public String getYelpSelected (String target) {
        JsonElement jelement = new JsonParser().parse(jsonData);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("businesses");
        String result = null;
        for (int i=0; i<jarray.size(); i++){
            if(jarray.get(i).getAsJsonObject().get("name").toString().equals(target)){
                result = jarray.get(i).toString();
                return result;
            }
        }
        return result;
    }

    public List<String> getResults () {
        List resList = new ArrayList();
        JsonElement jelement = new JsonParser().parse(jsonData);
        JsonObject jobject = jelement.getAsJsonObject();
        resList.add(jobject.get("name").toString().replace("\"", ""));
        resList.add(jobject.get("rating"));
        resList.add(jobject.get("review_count"));
        resList.add(jobject.get("image_url").toString().replace("\"", ""));

        JsonArray jarray = jobject.getAsJsonObject("location").getAsJsonArray("display_address");

        for (int i=0; i<jarray.size(); i++){
            resList.add(jarray.get(i).toString().replace("\"", ""));
        }
        return resList;

    }
}
