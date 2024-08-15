package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.storages.core.PlayerData;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class CheckCommand extends SubCommand {
    private final BaseStorage baseStorage;
    private final LanguageManager languageManager;

    public CheckCommand(EastWhitelist plugin) {
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
        Map<String, PlayerData> players = baseStorage.getPlayers();
        if (players.containsKey(args[1])) {
            String addedBy = players.get(args[1]).getAddedBy();
            String addedAt = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(players.get(args[1]).getAddedAt()));
            languageManager.sendMessage(sender, "commands.check.whitelisted", args[1], addedBy, addedAt);
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
