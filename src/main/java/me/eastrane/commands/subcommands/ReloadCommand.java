package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {
    private final LanguageManager languageManager;

    public ReloadCommand(EastWhitelist plugin) {
        this.plugin = plugin;
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
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> plugin.getBaseStorage().loadStorage());
        languageManager.sendMessage(sender, "commands.reload.success");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
