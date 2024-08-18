package me.eastrane.listeners.core;

import me.eastrane.EastWhitelist;
import org.bukkit.event.Listener;
import org.bukkit.event.HandlerList;

public abstract class BaseListener implements Listener {
    protected EastWhitelist plugin;
    private boolean isRegistered = false;
    private final boolean isReloadable;

    /**
     * Constructs a new BaseListener instance.
     *
     * @param plugin The main plugin instance.
     * @param isReloadable Indicates whether the listener can be unregistered during a plugin reload.
     */
    public BaseListener(EastWhitelist plugin, boolean isReloadable) {
        this.plugin = plugin;
        this.isReloadable = isReloadable;
        if (shouldRegister()) {
            register();
        }
    }

    /**
     * Registers the listener with the Bukkit plugin manager.
     *
     * @return True if the listener was successfully registered, false otherwise.
     */
    protected boolean register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getDebugProvider().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
        return true;
    }

    /**
     * Unregisters the listener from the Bukkit plugin manager.
     *
     * @return True if the listener was successfully unregistered, false otherwise.
     *         If the listener is not reloadable, this method will always return false.
     */
    protected boolean unregister() {
        if (isReloadable) {
            HandlerList.unregisterAll(this);
            plugin.getDebugProvider().sendInfo(this.getClass().getSimpleName() + " was unregistered.");
            isRegistered = false;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the listener should be registered.
     *
     * @return True if the listener should be registered, false otherwise.
     */
    protected abstract boolean shouldRegister();

    /**
     * Returns whether the listener is currently registered with the Bukkit plugin manager.
     *
     * @return True if the listener is registered, false otherwise.
     */
    public boolean isRegistered() {
        return isRegistered;
    }
}
