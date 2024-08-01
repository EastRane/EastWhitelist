package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AddCommand extends SubCommand {
    private final DataManager dataManager;
    private final LanguageManager languageManager;

    public AddCommand(EastWhitelist plugin) {
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
        Set<String> players = dataManager.getPlayers();
        if (players.contains(args[1])) {
            languageManager.sendMessage(sender, "commands.add.already_present", args[1]);
            return;
        }
        dataManager.addPlayer(args[1]);
        languageManager.sendMessage(sender,"commands.add.player_added", args[1]);
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
