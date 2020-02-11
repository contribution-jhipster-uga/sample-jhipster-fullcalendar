import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICalendarProvider } from 'app/shared/model/calendar-provider.model';
import { CalendarProviderService } from './calendar-provider.service';

@Component({
  templateUrl: './calendar-provider-delete-dialog.component.html'
})
export class CalendarProviderDeleteDialogComponent {
  calendarProvider?: ICalendarProvider;

  constructor(
    protected calendarProviderService: CalendarProviderService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calendarProviderService.delete(id).subscribe(() => {
      this.eventManager.broadcast('calendarProviderListModification');
      this.activeModal.close();
    });
  }
}
