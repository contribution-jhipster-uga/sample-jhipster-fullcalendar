import { Route } from '@angular/router';
import { CalendarListComponent } from './calendar-list.component';

export const CALENDARLIST_ROUTE: Route = {
  path: 'calendar-list',
  component: CalendarListComponent,
  data: {
    authorities: [],
    pageTitle: 'global.title'
  }
};
