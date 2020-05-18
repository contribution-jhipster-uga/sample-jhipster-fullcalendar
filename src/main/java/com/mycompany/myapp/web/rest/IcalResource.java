package com.mycompany.myapp.web.rest;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.mycompany.myapp.service.CalendarEventQueryService;
import com.mycompany.myapp.service.CalendarEventService;
import com.mycompany.myapp.service.CalendarService;
import com.mycompany.myapp.service.dto.CalendarDTO;
import com.mycompany.myapp.service.dto.CalendarEventDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.validate.ValidationException;

@RestController
@RequestMapping("/api")
public class IcalResource {

    private final Logger log = LoggerFactory.getLogger(IcalResource.class);
    private final CalendarEventQueryService calendarEventQueryService;
    private final CalendarEventService calendarEventService;
    private final CalendarService calendarService;

    public IcalResource(CalendarEventQueryService calendarEventQueryService, CalendarEventService calendarEventService,
            CalendarService calendarService) {
        this.calendarEventQueryService = calendarEventQueryService;
        this.calendarEventService = calendarEventService;
        this.calendarService = calendarService;
    }

    /**
     * Exports all events as an ics file without filtering. File can be found in the
     * root folder
     */
    @GetMapping("/calendar-events/ical")
    public ResponseEntity<String> exportIcal() throws ParseException, ValidationException, IOException {
        log.debug("REST request to export calendar events as an ics file");
        List<CalendarEventDTO> allEvents = calendarEventQueryService.findByCriteria(null);
        Calendar ical = new Calendar();
        ical.getProperties().add(new ProdId("-//JHipster//Generated 1.0//EN"));
        ical.getProperties().add(Version.VERSION_2_0);
        ical.getProperties().add(CalScale.GREGORIAN);

        for (CalendarEventDTO e : allEvents) {
            VEvent ev = new VEvent(new DateTime(DateTime.from(e.getStartDate())),
                    new DateTime(DateTime.from(e.getEndDate())), e.getTitle());
            ev.getProperties().add(new Uid(e.getUid().toString()));
            ical.getComponents().add(ev);
        }

        return ResponseEntity.ok().body(ical.toString());
    }

    /**
     * Imports an ics file and posts the events in a new calendar.
     * 
     * @throws ParserException
     */
    @PostMapping("/calendar-events/ical")
    public ResponseEntity<String> importIcal(@RequestParam(value = "icsFile") MultipartFile file) throws ParseException, IOException, ParserException {
        log.debug("REST request to import calendar events from file" + file.getName());
        
        CalendarBuilder builder = new CalendarBuilder();
        Calendar cal = builder.build(file.getInputStream());

        Instant now = Instant.now();

        CalendarDTO c = new CalendarDTO();
        String cTitle = cal.getProductId().toString();
        c.setTitle(cTitle.substring(cTitle.indexOf("//") + 2));
        c.setCreatedAt(now);
        c.setOwnedById((long) 3);
        c.setUid(UUID.randomUUID());
        long cId = calendarService.save(c).getId();

        for (CalendarComponent ev : cal.getComponents()) {
            CalendarEventDTO e = new CalendarEventDTO();
            e.setTitle(ev.getProperty("SUMMARY").getValue());
            e.setStartDate(Instant.parse(new StringBuilder(ev.getProperty("DTSTART").getValue()).insert(4, "-")
                    .insert(7, "-").insert(13, ":").insert(16, ":").toString()));
            e.setEndDate(Instant.parse(new StringBuilder(ev.getProperty("DTEND").getValue()).insert(4, "-")
                    .insert(7, "-").insert(13, ":").insert(16, ":").toString()));
            e.setUid(UUID.randomUUID());
            e.setIsPublic(true);
            e.setCreatedAt(now);
            e.setUpdatedAt(now);
            e.setCreatedById((long) 3);
            e.setCalendarId(cId);
            calendarEventService.save(e);
        }
        
        return ResponseEntity.ok().body("\"Import iCal OK\"");
    }
}