package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveCommand extends SubCommand {
    private final BaseStorage baseStorage;
    private final LanguageManager languageManager;

    public RemoveCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        baseStorage = plugin.getBaseStorage();
        languageManager = plugin.getLanguageManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            return;
        }
        if (args.length != 2) {
            languageManager.sendMessage(sender, "commands.errors.too_many_arguments");
            return;
        }
        if (baseStorage.removePlayer(args[1])) {
            languageManager.sendMessage(sender,"commands.remove.player_removed", args[1]);
        } else {
            languageManager.sendMessage(sender, "commands.check.not_whitelisted", args[1]);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<String>();
        if (args.length == 2) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                list.add(p.getName());
            }
            return list;
        }
        return Collections.emptyList();
    }
}
