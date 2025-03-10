package au.com.telstra.simcardactivator;

public class Sim {
    private final String iccid;
    private final String customerEmail;

    public Sim(String iccid, String customerEmail) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
    }

    public String getIccid() {
        return iccid;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
