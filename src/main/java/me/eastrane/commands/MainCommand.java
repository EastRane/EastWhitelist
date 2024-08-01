package me.eastrane.commands;

import me.eastrane.EastWhitelist;
import me.eastrane.commands.subcommands.*;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.DebugManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements CommandExecutor, TabCompleter {
    private LanguageManager languageManager;
    private EastWhitelist plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    public MainCommand(EastWhitelist plugin) {
        this.plugin = plugin;
        languageManager = plugin.getLanguageManager();
        registerSubCommand("add", new AddCommand(plugin));
        registerSubCommand("remove", new RemoveCommand(plugin));
        registerSubCommand("list", new ListCommand(plugin));
        registerSubCommand("enable", new EnableCommand(plugin));
        registerSubCommand("disable", new DisableCommand(plugin));
        registerSubCommand("reload", new ReloadCommand(plugin));
        registerSubCommand("check", new CheckCommand(plugin));
    }

    private void registerSubCommand(String command, SubCommand subCommand) {
        subCommands.put(command, subCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("eastwhitelist.help")) {
                languageManager.sendMessage(sender, "commands.help");
                return true;
            }
            languageManager.sendMessage(sender, "commands.errors.no_permission");
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null) {
            subCommand.execute(sender, args);
        } else {
            String availableCommands = String.join(", ", subCommands.keySet());
            languageManager.sendMessage(sender, "commands.errors.invalid_subcommand", availableCommands);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                    .filter(subCommand -> subCommand.startsWith(args[0].toLowerCase()))
                    .toList();
        } else {
            SubCommand subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                return subCommand.onTabComplete(sender, args);
            }
        }
        return null;
    }
}
