package net.sapfii.modutilities.features.interfaces;

import net.minecraft.network.packet.Packet;

public interface PacketListeningFeature {

    PacketResult onPacket(Packet<?> packet);

    enum PacketResult {
        PASS(false), CANCEL(true);
        final boolean value;
        PacketResult(boolean value) { this.value = value; }
        public boolean Value() { return this.value; }
    }

}
