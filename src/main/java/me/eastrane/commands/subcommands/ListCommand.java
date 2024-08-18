package me.eastrane.commands.subcommands;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.storages.core.PlayerData;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListCommand extends SubCommand {
    private final BaseStorage baseStorage;
    private final LanguageProvider languageProvider;

    public ListCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        baseStorage = plugin.getBaseStorage();
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
        Map<String, PlayerData> players = baseStorage.getPlayers();
        if (players.isEmpty()) {
            languageProvider.sendMessage(sender, "commands.list.empty");
            return;
        }
        languageProvider.sendMessage(sender,"commands.list.players", String.join(", ", players.keySet()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
