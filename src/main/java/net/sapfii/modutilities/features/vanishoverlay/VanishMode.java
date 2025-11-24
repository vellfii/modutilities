package net.sapfii.modutilities.features.vanishoverlay;

public enum VanishMode {
    NONE, MOD, ADMIN;

    private static VanishMode mode = NONE;

    public static VanishMode get() { return mode; }
    public static void set(VanishMode mode) { VanishMode.mode = mode; }
    public static boolean is(VanishMode mode) { return VanishMode.mode == mode; }
}
