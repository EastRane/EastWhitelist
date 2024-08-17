package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {
    private final LanguageProvider languageProvider;

    public ReloadCommand(EastWhitelist plugin) {
        this.plugin = plugin;
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
        plugin.getConfigManager().reloadConfig();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> plugin.getBaseStorage().loadStorage());
        languageProvider.sendMessage(sender, "commands.reload.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
