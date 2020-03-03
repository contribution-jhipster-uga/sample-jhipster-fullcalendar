import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FullCalendarSharedModule } from 'app/shared/shared.module';
import { FullCalendarModule } from '@fullcalendar/angular';

import { CALENDARS_ROUTE } from './calendars.route';
import { CalendarsComponent } from './calendars.component';
import { EventModalComponent } from './event-modal.component'

@NgModule({
  imports: [FullCalendarSharedModule, FullCalendarModule, RouterModule.forChild([CALENDARS_ROUTE])],
  declarations: [CalendarsComponent, EventModalComponent],
  entryComponents: [EventModalComponent]
})
export class CalendarsModule {
  
}