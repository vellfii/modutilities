package net.sapfii.modutilities.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.Features;
import net.sapfii.modutilities.features.interfaces.KeyBindListeningFeature;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModUtilsKeyBinds {
    private ModUtilsKeyBinds() {}

    public static KeyBinding DISMISS_REPORT = registerKeyBind("dismiss_report", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X);
    public static KeyBinding REPEAT_TP = registerKeyBind("repeat_tp", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J);


    private static KeyBinding registerKeyBind(String id, InputUtil.Type keyType, int code) {
        KeyBinding keyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + ModUtilities.MOD_ID +"." + id,
                keyType,
                code,
                "key." + ModUtilities.MOD_ID +".category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBind.wasPressed()) {
                for (Feature feature : Features.registeredFeatures()) {
                    if (feature instanceof KeyBindListeningFeature kblf) {
                        kblf.onKeyBindPress(keyBind);
                    }
                }
            }
        });
        return keyBind;
    }

    public static void init() {
        ModUtilities.LOGGER.info("initializing keybinds");
    }
}
