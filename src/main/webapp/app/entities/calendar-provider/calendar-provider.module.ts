import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FullCalendarSharedModule } from 'app/shared/shared.module';
import { CalendarProviderComponent } from './calendar-provider.component';
import { CalendarProviderDetailComponent } from './calendar-provider-detail.component';
import { CalendarProviderUpdateComponent } from './calendar-provider-update.component';
import { CalendarProviderDeleteDialogComponent } from './calendar-provider-delete-dialog.component';
import { calendarProviderRoute } from './calendar-provider.route';

@NgModule({
  imports: [FullCalendarSharedModule, RouterModule.forChild(calendarProviderRoute)],
  declarations: [
    CalendarProviderComponent,
    CalendarProviderDetailComponent,
    CalendarProviderUpdateComponent,
    CalendarProviderDeleteDialogComponent
  ],
  entryComponents: [CalendarProviderDeleteDialogComponent]
})
export class FullCalendarCalendarProviderModule {}
