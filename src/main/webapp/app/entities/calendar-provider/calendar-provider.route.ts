import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICalendarProvider, CalendarProvider } from 'app/shared/model/calendar-provider.model';
import { CalendarProviderService } from './calendar-provider.service';
import { CalendarProviderComponent } from './calendar-provider.component';
import { CalendarProviderDetailComponent } from './calendar-provider-detail.component';
import { CalendarProviderUpdateComponent } from './calendar-provider-update.component';

@Injectable({ providedIn: 'root' })
export class CalendarProviderResolve implements Resolve<ICalendarProvider> {
  constructor(private service: CalendarProviderService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICalendarProvider> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((calendarProvider: HttpResponse<CalendarProvider>) => {
          if (calendarProvider.body) {
            return of(calendarProvider.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CalendarProvider());
  }
}

export const calendarProviderRoute: Routes = [
  {
    path: '',
    component: CalendarProviderComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'fullCalendarApp.calendarProvider.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CalendarProviderDetailComponent,
    resolve: {
      calendarProvider: CalendarProviderResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarProvider.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CalendarProviderUpdateComponent,
    resolve: {
      calendarProvider: CalendarProviderResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarProvider.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CalendarProviderUpdateComponent,
    resolve: {
      calendarProvider: CalendarProviderResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fullCalendarApp.calendarProvider.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
