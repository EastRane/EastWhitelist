package me.eastrane.listeners;

import me.eastrane.EastWhitelist;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener extends BaseListener implements Listener {
    private final ConfigProvider configProvider;
    private final LanguageProvider languageProvider;
    private final BaseStorage baseStorage;

    public AsyncPlayerPreLoginListener(EastWhitelist plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        configProvider = plugin.getConfigProvider();
        languageProvider = plugin.getLanguageProvider();
        baseStorage = plugin.getBaseStorage();
    }

    @Override
    protected boolean shouldRegister() {
        return true;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!configProvider.isEnabled()) return;
        if (baseStorage.isPlayerWhitelisted(event.getPlayerProfile().getName())) return;
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, languageProvider.Colorize(languageProvider.getTranslation("messages.not_whitelisted")));
        if (!configProvider.isNotifyOP()) return;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission("eastwhitelist.notify")) {
                languageProvider.sendMessage(player, "messages.notify_op", event.getPlayerProfile().getName());
            }
        }
    }
}
