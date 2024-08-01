package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EnableCommand extends SubCommand {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;

    public EnableCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        configManager = plugin.getConfigManager();
        languageManager = plugin.getLanguageManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length != 1) {
            languageManager.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        if (configManager.isEnabled()) {
            languageManager.sendMessage(sender, "commands.enable.already_enabled");
            return;
        }
        configManager.setEnabled(true);
        languageManager.sendMessage(sender, "commands.enable.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
