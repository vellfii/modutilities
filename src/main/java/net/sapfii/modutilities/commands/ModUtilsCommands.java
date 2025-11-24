package net.sapfii.modutilities.commands;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.sapfii.modutilities.config.ConfigScreen;
import net.sapfii.modutilities.features.reportoverlay.ReportScreen;
import net.velli.scelli.ScreenHandler;

public class ModUtilsCommands {
    public static void init() {
        registerSimpleCommand("modutils", context -> {
            ScreenHandler.openScreen(new ConfigScreen());
            return 1;
        });
        registerSimpleCommand("reports", context -> {
            ScreenHandler.openScreen(new ReportScreen());
            return 1;
        });
        
    }

    private static void registerSimpleCommand(String id, Command<FabricClientCommandSource> command) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(id).executes(command));
        });
    }
}
