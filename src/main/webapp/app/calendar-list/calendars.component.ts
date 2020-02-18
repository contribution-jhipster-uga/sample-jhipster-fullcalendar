import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import dayGridPlugin from '@fullcalendar/daygrid';

import { CalendarService } from 'app/entities/calendar/calendar.service';
import { ICalendar } from 'app/shared/model/calendar.model';

@Component({
  selector: 'jhi-calendars',
  templateUrl: './calendars.component.html'
})
export class CalendarsComponent implements OnInit, OnDestroy {
  calendarList?: ICalendar[];
  eventSubscriber?: Subscription;
  calendarPlugins = [dayGridPlugin];

  constructor(
    protected calendarService: CalendarService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
  ) { }

  loadAll(): void {
    this.calendarService
      .query()
      .subscribe(
        (res: HttpResponse<ICalendar[]>) => this.onSuccess(res.body),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.loadAll()
    this.registerChangeInCalendars();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInCalendars(): void {
    this.eventSubscriber = this.eventManager.subscribe('calendarsModification', () => this.loadAll());
  }

  protected onSuccess(data: ICalendar[] | null): void {
    this.calendarList = data || [];
  }

  protected onError(): void {
  }
}

