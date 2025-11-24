package net.sapfii.modutilities.config.enums;

public enum ReportType {
    CLASSIC("classic"), REVAMPED("remastered"), REIMAGINED("reimagined");
    private final String id;
    ReportType(String id) { this.id = id; }
    public String id() {
        return this.id;
    }
}
