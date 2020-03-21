package com.mycompany.myapp.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/*
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.MapTimeZoneCache;
*/

@RestController
@RequestMapping("/api")
public class IcalResource {

    private final Logger log = LoggerFactory.getLogger(CalendarEventResource.class);

    @GetMapping("/calendar-events/ical")
    public ResponseEntity<String> ical() {
        log.debug("============================== ICAL ===================================");
        return ResponseEntity.ok().body("A-OK");
    }
}