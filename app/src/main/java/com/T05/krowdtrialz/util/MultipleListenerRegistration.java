package com.T05.krowdtrialz.util;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

/**
 * A ListenerRegistration that holds multiple ListenerRegistration instances.
 * The remove() method will call remove() on each of the contained instances.
 * @author Ryan Shukla
 */
public class MultipleListenerRegistration implements ListenerRegistration {
    private ArrayList<ListenerRegistration> registrations;

    public MultipleListenerRegistration() {
        registrations = new ArrayList<>();
    }

    /**
     * Adds a listener registration
     * @param registration
     */
    public void add(ListenerRegistration registration) {
        registrations.add(registration);
    }

    /**
     * Calls remove() on all registrations.
     */
    @Override
    public void remove() {
        for (ListenerRegistration registration: registrations) {
            registration.remove();
        }
        registrations.clear();
    }
}
