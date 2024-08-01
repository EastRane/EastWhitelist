package me.eastrane.listeners.core;

import me.eastrane.EastWhitelist;
import me.eastrane.listeners.*;

import java.util.*;

public class ListenerManager {
    private final EastWhitelist plugin;
    private final Map<String, BaseListener> listeners = new HashMap<>();

    public ListenerManager(EastWhitelist plugin) {
        this.plugin = plugin;
        registerListeners();
    }

    private void registerListeners() {
        registerListener(new AsyncPlayerPreLoginListener(plugin, false));
    }

    private void registerListener(BaseListener listener) {
        listeners.put(listener.getClass().getSimpleName(), listener);
    }

    /**
     * Rechecks the registration status of all listeners.
     *
     * @return A map containing the names of listeners and their registration status changes.
     *         If a listener's registration status does not change, it is not included in the map.
     */
    public Map<String, Boolean> recheckListeners() {
        Map<String, Boolean> changes = new HashMap<>();

        for (BaseListener listener : listeners.values()) {
            String listenerName = listener.getClass().getSimpleName();
            if (listener.isRegistered()) {
                if (!listener.shouldRegister()) {
                    if (listener.unregister()) {
                        changes.put(listenerName, false);
                    }
                }
            } else {
                if (listener.shouldRegister()) {
                    if (listener.register()) {
                        changes.put(listenerName, true);
                    }
                }
            }
        }
        return changes;
    }

    /**
     * Unregisters all the listeners.
     */
    public void unregisterListeners() {
        for (BaseListener listener : listeners.values()) {
            listener.unregister();
        }
    }

    /**
     * Retrieves a list of registered listener names.
     *
     * @return A list of registered listener names.
     */
    public List<String> getRegisteredListeners() {
        List<String> registeredListeners = new ArrayList<>();
        for (BaseListener listener : listeners.values()) {
            if (listener.isRegistered()) {
                registeredListeners.add(listener.getClass().getSimpleName());
            }
        }
        return registeredListeners;
    }

    /**
     * Retrieves a listener by its name.
     *
     * @param listenerName The name of the listener.
     * @return The listener instance or null if not found.
     */
    public BaseListener getListener(String listenerName) {
        return listeners.get(listenerName);
    }
}
