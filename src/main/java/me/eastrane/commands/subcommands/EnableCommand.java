package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class EnableCommand extends SubCommand {
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;

    public EnableCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        configProvider = plugin.getConfigManager();
        languageProvider = plugin.getLanguageManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length != 1) {
            languageProvider.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        if (configProvider.isEnabled()) {
            languageProvider.sendMessage(sender, "commands.enable.already_enabled");
            return;
        }
        configProvider.setEnabled(true);
        languageProvider.sendMessage(sender, "commands.enable.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
