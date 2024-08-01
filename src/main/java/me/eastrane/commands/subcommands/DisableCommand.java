package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class DisableCommand extends SubCommand {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;

    public DisableCommand(EastWhitelist plugin) {
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
        if (!configManager.isEnabled()) {
            languageManager.sendMessage(sender, "commands.disable.already_disabled");
            return;
        }
        configManager.setEnabled(false);
        languageManager.sendMessage(sender, "commands.disable.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
