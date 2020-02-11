import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICalendarEvent } from 'app/shared/model/calendar-event.model';
import { CalendarEventService } from './calendar-event.service';

@Component({
  templateUrl: './calendar-event-delete-dialog.component.html'
})
export class CalendarEventDeleteDialogComponent {
  calendarEvent?: ICalendarEvent;

  constructor(
    protected calendarEventService: CalendarEventService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calendarEventService.delete(id).subscribe(() => {
      this.eventManager.broadcast('calendarEventListModification');
      this.activeModal.close();
    });
  }
}
