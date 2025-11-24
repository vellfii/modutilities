package net.sapfii.modutilities.config.enums;

public enum LogDirection {
    UP("up"), DOWN("down");
    private final String id;
    LogDirection(String id) { this.id = id; }
    public String id() {
        return this.id;
    }
}
