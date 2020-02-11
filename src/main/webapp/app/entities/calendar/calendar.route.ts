import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICalendar, Calendar } from 'app/shared/model/calendar.model';
import { CalendarService } from './calendar.service';
import { CalendarComponent } from './calendar.component';
import { CalendarDetailComponent } from './calendar-detail.component';
import { CalendarUpdateComponent } from './calendar-update.component';

@Injectable({ providedIn: 'root' })
export class CalendarResolve implements Resolve<ICalendar> {
  constructor(private service: CalendarService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalendar> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((calendar: HttpResponse<Calendar>) => {
          if (calendar.body) {
            return of(calendar.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Calendar());
  }
}

export const calendarRoute: Routes = [
  {
    path: '',
    component: CalendarComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'fullCalendarApp.calendar.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CalendarDetailComponent,
    resolve: {
      calendar: CalendarResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendar.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CalendarUpdateComponent,
    resolve: {
      calendar: CalendarResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendar.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CalendarUpdateComponent,
    resolve: {
      calendar: CalendarResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendar.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
