package fr.alten.test_back.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Database initialization trigger.
 */
public class DatabaseInitTrigger {

    /**
     * Database initialization task.
     */
    private final DatabaseInitializer initializer;

    /**
     * Flag to indicate that initialization was already done
     */
    private static boolean alreadyInitialized = false;

    /**
     * Create trigger.
     * @param initializer Database initialization task.
     */
    public DatabaseInitTrigger(DatabaseInitializer initializer) {
        this.initializer = initializer;
    }

    /**
     * Run task when application is ready.
     * @param event Triggered event.
     */
    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        // If DB not already initialized
        if (!alreadyInitialized) {
            try {
                // Initialize DB
                this.initializer.run(null);
                // Set flag to prevent other initialization
                alreadyInitialized = true;
            } catch (Exception e) {
                throw new RuntimeException("Erreur init BDD", e);
            }
        }
    }
}