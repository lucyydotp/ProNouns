package net.lucypoulton.pronouns.discord;

import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.config.ConnectionType;
import net.lucypoulton.pronouns.config.SqlInfoContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.lucypoulton.squirtgun.discord.DiscordFormatProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class TestConfigHandler implements ConfigHandler {

    private final Properties properties;

    public TestConfigHandler() throws IOException {

        if (!Files.exists(Path.of("pronouns.properties"))) {
            File config = new File("pronouns.properties");

            Objects.requireNonNull(getClass().getResourceAsStream("/pronouns.properties"))
                .transferTo(new FileOutputStream(config));
        }

        properties = new Properties();
        properties.load(new FileInputStream("pronouns.properties"));
    }

    public String getDiscordToken() {
        return properties.getProperty("discordToken");
    }

    public String getCommandPrefix() {
        return properties.getProperty("prefix", "-");
    }

    @Override
    public List<String> getPredefinedSets() {
        return List.of();
    }

    @Override
    public List<String> getFilterPatterns() {
        return List.of();
    }

    @Override
    public boolean filterEnabled() {
        return false;
    }

    @Override
    public SqlInfoContainer getSqlConnectionData() {
        return new SqlInfoContainer(properties.getProperty("sql.host", "localhost"),
            Integer.parseInt(properties.getProperty("sql.port", "3306")),
            properties.getProperty("sql.database", "pronouns"),
            properties.getProperty("sql.user", "pronouns"),
            properties.getProperty("sql.password"));
    }

    @Override
    public boolean checkForUpdates() {
        return true;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.MYSQL;
    }

    @Override
    public boolean shouldSyncWithCloud() {
        return false;
    }

    @Override
    public boolean shouldUploadToCloud() {
        return false;
    }

    @Override
    public Component formatMain(@NotNull String input, @NotNull TextDecoration[] formatters) {
        return DiscordFormatProvider.INSTANCE.formatMain(input, formatters);
    }

    @Override
    public Component formatAccent(@NotNull String input, @NotNull TextDecoration[] formatters) {
        return DiscordFormatProvider.INSTANCE.formatAccent(input, formatters);
    }

    @Override
    public Component getPrefix() {
        return DiscordFormatProvider.INSTANCE.getPrefix();
    }

    @Override
    public Component formatTitle(String input) {
        return DiscordFormatProvider.INSTANCE.formatTitle(input);
    }

    @Override
    public Component formatFooter(String input) {
        return DiscordFormatProvider.INSTANCE.formatFooter(input);
    }
}
