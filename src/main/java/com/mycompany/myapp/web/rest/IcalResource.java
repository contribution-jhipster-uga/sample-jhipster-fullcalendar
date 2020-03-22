package com.mycompany.myapp.web.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.mycompany.myapp.service.CalendarEventQueryService;
import com.mycompany.myapp.service.dto.CalendarEventDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.validate.ValidationException;

@RestController
@RequestMapping("/api")
public class IcalResource {

    private final Logger log = LoggerFactory.getLogger(CalendarEventResource.class);
    private final CalendarEventQueryService calendarEventQueryService;

    public IcalResource(CalendarEventQueryService calendarEventQueryService) {
        this.calendarEventQueryService = calendarEventQueryService;
    }

    @GetMapping("/calendar-events/ical")
    public ResponseEntity<String> ExportIcal() throws ParseException, ValidationException, IOException {
        log.debug("============================== ICAL ===================================");
        List<CalendarEventDTO> allEvents = calendarEventQueryService.findByCriteria(null);
        Calendar ical = new Calendar();
        ical.getProperties().add(new ProdId("-//JHipster//Generated 1.0//EN"));
        ical.getProperties().add(Version.VERSION_2_0);
        ical.getProperties().add(CalScale.GREGORIAN);

        for (CalendarEventDTO e : allEvents) {
            VEvent ev = new VEvent(new Date(DateTime.from(e.getStartDate())), new Date(DateTime.from(e.getEndDate())), e.getTitle());
            ev.getProperties().add(new Uid (e.getUid().toString()));
            ical.getComponents().add(ev);
        }

        FileOutputStream fout = new FileOutputStream("ical4j_test.ics");
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(ical, fout);

        return ResponseEntity.ok().body("A-OK");
    }
}