import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICalendarEvent, CalendarEvent } from 'app/shared/model/calendar-event.model';
import { CalendarEventService } from './calendar-event.service';
import { CalendarEventComponent } from './calendar-event.component';
import { CalendarEventDetailComponent } from './calendar-event-detail.component';
import { CalendarEventUpdateComponent } from './calendar-event-update.component';

@Injectable({ providedIn: 'root' })
export class CalendarEventResolve implements Resolve<ICalendarEvent> {
  constructor(private service: CalendarEventService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalendarEvent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((calendarEvent: HttpResponse<CalendarEvent>) => {
          if (calendarEvent.body) {
            return of(calendarEvent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CalendarEvent());
  }
}

export const calendarEventRoute: Routes = [
  {
    path: '',
    component: CalendarEventComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarEvent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CalendarEventDetailComponent,
    resolve: {
      calendarEvent: CalendarEventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarEvent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CalendarEventUpdateComponent,
    resolve: {
      calendarEvent: CalendarEventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarEvent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CalendarEventUpdateComponent,
    resolve: {
      calendarEvent: CalendarEventResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarEvent.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
