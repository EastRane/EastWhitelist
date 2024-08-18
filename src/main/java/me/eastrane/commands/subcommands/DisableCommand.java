package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class DisableCommand extends SubCommand {
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;

    public DisableCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        configProvider = plugin.getConfigProvider();
        languageProvider = plugin.getLanguageProvider();
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
        if (!configProvider.isEnabled()) {
            languageProvider.sendMessage(sender, "commands.disable.already_disabled");
            return;
        }
        configProvider.setEnabled(false);
        languageProvider.sendMessage(sender, "commands.disable.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
