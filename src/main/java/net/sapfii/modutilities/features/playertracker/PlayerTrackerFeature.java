package net.sapfii.modutilities.features.playertracker;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;

public class PlayerTrackerFeature extends Feature implements PacketListeningFeature {

    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketResult.PASS;
        String str = msgText.getString();

        return PacketResult.PASS;
    }


    public static void startTracking() {

    }
}
