package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected EastWhitelist plugin;

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public boolean hasPermission(CommandSender sender) {
        String permission = "eastwhitelist." + this.getClass().getSimpleName().toLowerCase().replace("command", "");
        if (sender.hasPermission(permission) || sender.hasPermission("eastwhitelist.admin")) {
            return true;
        }
        plugin.getLanguageManager().sendMessage(sender, "commands.errors.no_permission");
        return false;
    }
}
