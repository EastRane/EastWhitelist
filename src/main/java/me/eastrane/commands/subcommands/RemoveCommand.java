package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoveCommand extends SubCommand {
    private final DataManager dataManager;
    private final LanguageManager languageManager;

    public RemoveCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
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
        dataManager.removePlayer(args[1]);
        languageManager.sendMessage(sender,"commands.remove.player_removed", args[1]);
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
