import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ICalendarEvent } from 'app/shared/model/calendar-event.model';

@Component({
  selector: 'jhi-calendar-event-detail',
  templateUrl: './calendar-event-detail.component.html'
})
export class CalendarEventDetailComponent implements OnInit {
  calendarEvent: ICalendarEvent | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendarEvent }) => (this.calendarEvent = calendarEvent));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
