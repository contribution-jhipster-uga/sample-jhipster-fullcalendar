import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICalendarProvider } from 'app/shared/model/calendar-provider.model';

@Component({
  selector: 'jhi-calendar-provider-detail',
  templateUrl: './calendar-provider-detail.component.html'
})
export class CalendarProviderDetailComponent implements OnInit {
  calendarProvider: ICalendarProvider | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendarProvider }) => (this.calendarProvider = calendarProvider));
  }

  previousState(): void {
    window.history.back();
  }
}
