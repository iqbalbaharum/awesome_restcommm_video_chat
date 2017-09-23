package loomo.com.awesome.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MuhammadIqbal on 11/2/2017.
 */

public class ATrigger {

    @SerializedName("_id")
    public String id;

    @SerializedName("patient_id")
    public String patientID;

    public double lat;
    public double lon;
    public boolean attended;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}
