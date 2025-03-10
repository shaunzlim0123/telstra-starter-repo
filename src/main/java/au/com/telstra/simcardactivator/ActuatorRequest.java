package au.com.telstra.simcardactivator;

public class ActuatorRequest {
    private final String iccid;

    public ActuatorRequest(String iccid) {
        this.iccid = iccid;
    }

    public String getIccid() {
        return iccid;
    }
}