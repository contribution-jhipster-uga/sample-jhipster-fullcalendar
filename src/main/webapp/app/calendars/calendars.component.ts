import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import dayGridPlugin from '@fullcalendar/daygrid';

import { CalendarService } from 'app/entities/calendar/calendar.service';
import { ICalendar } from 'app/shared/model/calendar.model';
import { ICalendarEvent } from 'app/shared/model/calendar-event.model';
import { CalendarEventService } from 'app/entities/calendar-event/calendar-event.service';

@Component({
  selector: 'jhi-calendars',
  templateUrl: './calendars.component.html'
})
export class CalendarsComponent implements OnInit, OnDestroy {
  calendarList: ICalendar[];
  calendarEvents: ICalendarEvent[];
  displayedEvents: {}[];
  checkedCals: { calid?: number, checked: boolean }[];
  eventSubscriber?: Subscription;
  calendarPlugins = [dayGridPlugin];

  constructor(
    protected calendarService: CalendarService,
    protected calendarEventService: CalendarEventService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
  ) {
    this.displayedEvents = [];
    this.checkedCals = [];
    this.calendarEvents = [];
    this.calendarList = [];
  }

  loadAll(): void {
    this.calendarService
      .query()
      .subscribe((res: HttpResponse<ICalendar[]>) => this.onCalendarSuccess(res.body));
    this.calendarEventService
      .query()
      .subscribe((res: HttpResponse<ICalendarEvent[]>) => this.onCalendarEventSuccess(res.body));
  }

  ngOnInit(): void {
    this.loadAll()
    this.registerChangeInCalendars();
    this.registerChangeInCalendarEvents();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInCalendars(): void {
    this.eventSubscriber = this.eventManager.subscribe('calendarsModification', () => this.loadAll());
  }

  registerChangeInCalendarEvents(): void {
    this.eventSubscriber = this.eventManager.subscribe('calendarEventListModification', () => this.loadAll());
  }

  protected onCalendarSuccess(data: ICalendar[] | null): void {
    this.calendarList = data || [];
    this.checkedCals = [];
    this.calendarList.forEach(c => {
      this.checkedCals.push({ calid: c.id, checked: true });
    });
  }

  protected onCalendarEventSuccess(data: ICalendarEvent[] | null): void {
    this.calendarEvents = data || [];
    this.displayedEvents = [];
    this.calendarEvents.forEach(e => {
      if (e.calendarId) this.displayedEvents.push(this.parseEvent(e));
    });
  }

  /** Convert an ICalendarEvent Object to a FullCalendar Event Object */
  protected parseEvent(event: ICalendarEvent): {} {
    const r = {};
    if (event.uid) r["id"] = event.uid;
    if (event.title) r["title"] = event.title;
    if (event.startDate) r["start"] = event.startDate.toISOString();
    if (event.endDate) r["end"] = event.endDate.toISOString(); else r["allDay"] = true;
    if (event.url) r["url"] = event.url;
    return r;
  }

  protected eventsToDisplay(cid: number, event: any): void {
    this.displayedEvents = [];
    (this.checkedCals.find(c => c.calid === cid))!.checked = event.target.checked;
    this.checkedCals.forEach(c => {
      if (c.checked) {
        const e = this.calendarEvents.filter(f => f.calendarId === c.calid);
        e.forEach(ev => {
          this.displayedEvents.push(this.parseEvent(ev));
        });
      }
    });
  }
}

