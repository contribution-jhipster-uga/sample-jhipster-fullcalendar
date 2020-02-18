import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FullCalendarSharedModule } from 'app/shared/shared.module';
import { CALENDARLIST_ROUTE } from './calendar-list.route';
import { CalendarListComponent } from './calendar-list.component';

@NgModule({
  imports: [FullCalendarSharedModule, RouterModule.forChild([CALENDARLIST_ROUTE])],
  declarations: [CalendarListComponent]
})
export class CalendarListModule {
  
}