package net.sapfii.modutilities;

public enum Rank {
    NONE, MOD, ADMIN;

    private static Rank rank = Rank.NONE;

    public static Rank get() { return Rank.rank; }
    public static void set(Rank rank) { Rank.rank = rank; }
    public static boolean is(Rank rank) { return Rank.rank == rank; }
}
