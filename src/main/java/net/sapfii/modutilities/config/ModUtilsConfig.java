package net.sapfii.modutilities.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.sapfii.modutilities.config.enums.LogDirection;
import net.sapfii.modutilities.config.enums.ReportType;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModUtilsConfig {
    public ConfigOption<Boolean> useVanishDisplay = ConfigOption.build(true);
    public ConfigOption<Boolean> useReportDisplay = ConfigOption.build(true);
    public ConfigOption<Boolean> useHistoryScreen = ConfigOption.build(true);
    public ConfigOption<Boolean> useLogScreen = ConfigOption.build(true);
    public ConfigOption<Boolean> hideJoinMessages = ConfigOption.build(true);
    public ConfigOption<LogDirection> logDirection = ConfigOption.build(LogDirection.UP);
    public ConfigOption<ReportType> reportType = ConfigOption.build(ReportType.REVAMPED);


    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("modutils.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ModUtilsConfig config = new ModUtilsConfig();

    public static void loadConfig(){
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                config = GSON.fromJson(reader, ModUtilsConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
