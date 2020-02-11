import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'calendar-event',
        loadChildren: () => import('./calendar-event/calendar-event.module').then(m => m.FullCalendarCalendarEventModule)
      },
      {
        path: 'calendar',
        loadChildren: () => import('./calendar/calendar.module').then(m => m.FullCalendarCalendarModule)
      },
      {
        path: 'calendar-provider',
        loadChildren: () => import('./calendar-provider/calendar-provider.module').then(m => m.FullCalendarCalendarProviderModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class FullCalendarEntityModule {}
