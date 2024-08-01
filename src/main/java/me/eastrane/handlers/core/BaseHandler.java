package me.eastrane.handlers.core;

import me.eastrane.EastWhitelist;

public abstract class BaseHandler {
    protected EastWhitelist plugin;
    private boolean isRegistered = false;
    private final boolean isReloadable;

    /**
     * Constructs a new BaseHandler instance.
     *
     * @param plugin The main plugin instance.
     * @param isReloadable Indicates whether the handler can be unregistered during a plugin reload.
     */
    public BaseHandler(EastWhitelist plugin, boolean isReloadable) {
        this.plugin = plugin;
        this.isReloadable = isReloadable;
        if (shouldRegister()) {
            register();
        }
    }

    /**
     * Registers the handler.
     * It is expected to be overridden by subclasses with logic for specific handler and calling super.
     *
     * @return True if the handler was successfully registered, false otherwise.
     */
    protected boolean register() {
        plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was registered.");
        isRegistered = true;
        return true;
    }

    /**
     * Unregisters the handler.
     * It is expected to be overridden by subclasses with logic for specific handler and calling super.
     *
     * @return True if the handler was successfully unregistered, false otherwise.
     *         If the handler is not reloadable, this method will always return false.
     */
    protected boolean unregister() {
        if (isReloadable) {
            // This method (same as above) is expected to be overridden with logic for specific handler and calling super
            plugin.getDebugManager().sendInfo(this.getClass().getSimpleName() + " was unregistered.");
            isRegistered = false;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the handler should be registered.
     *
     * @return True if the handler should be registered, false otherwise.
     */
    protected abstract boolean shouldRegister();

    /**
     * Checks whether the handler is currently registered.
     *
     * @return True if the handler is registered, false otherwise.
     */
    public boolean isRegistered() {
        return isRegistered;
    }
}
