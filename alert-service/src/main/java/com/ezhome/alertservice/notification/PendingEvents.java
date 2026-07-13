package com.ezhome.alertservice.notification;

import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.stereotype.Component;

import com.ezhome.alertservice.event.UsageEvent;


@Component
public class PendingEvents {

    private final ConcurrentLinkedDeque<UsageEvent> events = new ConcurrentLinkedDeque<>();

    // maintains a list of usage-events that met the alert condition
    // using FIFO queue for quick insertion and deletion

    public void add(UsageEvent event) {
        if (event == null) {
            return;
        }
        // insert at back
        events.offerLast(event);
    }

    public UsageEvent poll() {
        // take from front
        return events.pollFirst();
    }

    public void requeue(UsageEvent event) {
        add(event); // failed event goes back to tail
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public int size() {
        return events.size();
    }
}
