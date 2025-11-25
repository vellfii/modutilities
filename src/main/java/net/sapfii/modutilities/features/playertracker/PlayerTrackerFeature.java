package net.sapfii.modutilities.features.playertracker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;
import net.sapfii.modutilities.features.vanishoverlay.VanishMode;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerTrackerFeature extends Feature implements PacketListeningFeature {

    private static final Pattern LOCATE = Pattern.compile(" {39}\\\\n(?<player>.+) is currently (?<mode>.+) on:\\\\n\\\\n→ (?<game>.+) \\\\n→ Owner: (?<owner>.+)\\\\n→ Server: (?<node>.+)\\\\n {39}");
    private static final Pattern LOCATESPAWN = Pattern.compile(" {39}\\n(?<player>.+) is currently at spawn\\n→ Server: (?<node>.+)\\n {39}");


    private static Entity trackedEntity = null;
    private static String player = "";
    private static String tpTo = "";
    private static String node = "";
    private static int tp = 0;

    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (packet instanceof GameStateChangeS2CPacket && !player.isEmpty() && node.isEmpty() && tp == 0) {
            ModUtilities.sendCommand("tp " + player);
            tpTo = player;
            player = "";
            tp = 1;
        }
        if (packet instanceof PlayerPositionLookS2CPacket(int teleportId, PlayerPosition change, Set<PositionFlag> relatives) && tp == 2) {
            System.out.println("FINAL");
            tp = 0;
            tpTo = "";
        }
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketResult.PASS;
        String str = msgText.getString();
        Matcher matcher = LOCATE.matcher(str);
        if (matcher.find() && !player.isEmpty() && node.isEmpty()) {
            node = matcher.group("node").toLowerCase().replace(" ", "");
            if (node.contains("private")) node = node.replace("node", "");
            if (VanishMode.is(VanishMode.NONE)) ModUtilities.sendCommand("mod v");
            else {
                ModUtilities.sendCommand("server " + node);
                node = "";
            }
            return PacketResult.CANCEL;
        }
        matcher = LOCATESPAWN.matcher(str);
        if (matcher.find() && !player.isEmpty() && node.isEmpty()) {
            node = matcher.group("node").toLowerCase().replace(" ", "");
            if (node.contains("private")) node = node.replace("node", "");
            if (VanishMode.is(VanishMode.NONE)) ModUtilities.sendCommand("mod v");
            else {
                ModUtilities.sendCommand("server " + node);
                node = "";
            }
            return PacketResult.CANCEL;
        }
        if (str.matches("Error: Could not find that player.") && !player.isEmpty() && node.isEmpty()) player = "";
        if (str.matches("» Vanish enabled. You will not be visible to other players.") && !player.isEmpty() && !node.isEmpty()) {
            ModUtilities.sendCommand("server " + node);
            node = "";
        }
        if (str.matches("Teleported (.+) to (.+)") && tp == 1) {
            ModUtilities.sendCommand("tp " + tpTo);
            tp = 2;
            return PacketResult.CANCEL;
        }
        if (str.matches("You are already connected to this server!") && !player.isEmpty() && node.isEmpty() && tp == 0) {
            ModUtilities.sendCommand("tp " + player);
            tpTo = player;
            player = "";
            tp = 1;
            return PacketResult.CANCEL;
        }
        return PacketResult.PASS;
    }


    public static void startTracking(String player) {
        player = player.toLowerCase();
        if (player.equals(ModUtilities.MC.player.getName().getString().toLowerCase())) return;
        System.out.println(player);
        ModUtilities.sendCommand("locate " + player);
        PlayerTrackerFeature.player = player;
    }
}
