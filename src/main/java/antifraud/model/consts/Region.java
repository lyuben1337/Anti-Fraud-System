package antifraud.model.consts;

public enum Region {
    EAP("East Asia and Pacific"),
    ECA("Europe and Central Asia"),
    HIC("High-Income countries"),
    LAC("Latin America and the Caribbean"),
    MENA("The Middle East and North Africa"),
    SA("South Africa"),
    SSA("Sub-Saharan Africa");

    private final String description;

    Region(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
