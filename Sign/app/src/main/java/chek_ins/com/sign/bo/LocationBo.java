package chek_ins.com.sign.bo;

/**
 * Created by Administrator on 2016/3/26.
 */
public class LocationBo {
   private String USERID;

    private String LATITUDE;
    private String LONTITUDE;
    private String  RADIUS;
    private String  LOCALTIME;
    private  String ALTITUDE;

    public String getALTITUDE() {
        return ALTITUDE;
    }

    public void setALTITUDE(String ALTITUDE) {
        this.ALTITUDE = ALTITUDE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONTITUDE() {
        return LONTITUDE;
    }

    public void setLONTITUDE(String LONTITUDE) {
        this.LONTITUDE = LONTITUDE;
    }

    public String getRADIUS() {
        return RADIUS;
    }

    public void setRADIUS(String RADIUS) {
        this.RADIUS = RADIUS;
    }

    public String getLOCALTIME() {
        return LOCALTIME;
    }

    public void setLOCALTIME(String LOCALTIME) {
        this.LOCALTIME = LOCALTIME;
    }
}
