package me.eastrane.handlers.core;

import me.eastrane.EastWhitelist;
import me.eastrane.handlers.*;

import java.util.*;

public class HandlerManager {
    private final EastWhitelist plugin;
    private final Map<String, BaseHandler> handlers = new HashMap<>();

    public HandlerManager(EastWhitelist plugin) {
        this.plugin = plugin;
        registerHandlers();
    }

    private void registerHandlers() {
        //registerHandler(new APIHandler(plugin, false));
    }

    private void registerHandler(BaseHandler handler) {
        handlers.put(handler.getClass().getSimpleName(), handler);
    }

    /**
     * Rechecks the registration status of all handlers.
     *
     * @return A map containing the names of handlers and their registration status changes.
     *         If a handler's registration status does not change, it is not included in the map.
     */
    public Map<String, Boolean> recheckHandlers() {
        Map<String, Boolean> changes = new HashMap<>();

        for (BaseHandler handler : handlers.values()) {
            String handlerName = handler.getClass().getSimpleName();
            if (handler.isRegistered()) {
                if (!handler.shouldRegister()) {
                    if(handler.unregister()) {
                        changes.put(handlerName, false);
                    }
                }
            } else {
                if (handler.shouldRegister()) {
                    if (handler.register()) {
                        changes.put(handlerName, true);
                    }
                }
            }
        }
        return changes;
    }

    /**
     * Unregisters all the handlers.
     */
    public void unregisterHandlers() {
        for (BaseHandler handler : handlers.values()) {
            handler.unregister();
        }
    }

    /**
     * Retrieves a list of registered handler names.
     *
     * @return A list of registered handler names.
     */
    public List<String> getRegisteredHandlers() {
        List<String> registeredHandlers = new ArrayList<>();
        for (BaseHandler handler : handlers.values()) {
            if (handler.isRegistered()) {
                registeredHandlers.add(handler.getClass().getSimpleName());
            }
        }
        return registeredHandlers;
    }

    /**
     * Retrieves a handler by its name.
     *
     * @param name The name of the handler.
     * @return The handler instance or null if not found.
     */
    public BaseHandler getHandler(String name) {
        return handlers.get(name);
    }
}
