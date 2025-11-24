package net.sapfii.modutilities.features.interfaces;

import net.minecraft.client.option.KeyBinding;

public interface KeyBindListeningFeature {
    void onKeyBindPress(KeyBinding keyBind);
}
