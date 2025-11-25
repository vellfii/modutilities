package net.sapfii.modutilities.features;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.features.interfaces.CommandListeningFeature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;
import net.sapfii.modutilities.features.interfaces.RenderedFeature;

import java.util.ArrayList;
import java.util.List;

public class Features {

    public static List<Feature> features = new ArrayList<>();

    public static void init() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.CHAT, Identifier.of(ModUtilities.MOD_ID, "before_chat"), Features::render);
    }

    private static void render(DrawContext context, RenderTickCounter renderTickCounter) {
        MinecraftClient MC = ModUtilities.MC;
        float mouseX = MC.currentScreen instanceof ChatScreen ? (float) MC.mouse.getScaledX(MC.getWindow()) : 0;
        float mouseY = MC.currentScreen instanceof ChatScreen ? (float) MC.mouse.getScaledY(MC.getWindow()) : 0;
        for (Feature feature : Features.registeredFeatures()) {
            if (feature instanceof RenderedFeature rf) {
                rf.render(context, mouseX, mouseY);
                rf.hoverWidgets(mouseX, mouseY, true);
            }
        }
    }

    public static void onClick(float mouseX, float mouseY) {
        for (Feature feature : Features.registeredFeatures()) {
            if (feature instanceof RenderedFeature rf) rf.onClick(mouseX, mouseY, true);
        }
    }

    public static void registerFeatures(Feature... fts) {
        List<Feature> features = List.of(fts);
        Features.features.addAll(features);
    }

    public static List<Feature> registeredFeatures() {
        return features;
    }

    public static boolean onPacket(Packet<?> packet) {
        boolean result = false;
        for (Feature feature : Features.registeredFeatures()) {
            if (feature instanceof PacketListeningFeature plf &&
                    plf.onPacket(packet).Value()) {
                result = true;
            }
        }
        return result;
    }

    public static boolean onCommand(String command) {
        boolean result = false;
        for (Feature feature : Features.registeredFeatures()) {
            if (feature instanceof CommandListeningFeature clf &&
                    clf.onCommand(command).Value()) {
                result = true;
            }
        }
        return result;
    }

}
