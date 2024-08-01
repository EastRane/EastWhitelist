package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ReloadCommand extends SubCommand {
    private final DataManager dataManager;
    private final LanguageManager languageManager;

    public ReloadCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
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
        plugin.getConfigManager().reloadConfig();
        plugin.getDataManager().loadData();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
