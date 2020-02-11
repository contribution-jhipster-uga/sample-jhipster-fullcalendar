import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICalendar } from 'app/shared/model/calendar.model';
import { CalendarService } from './calendar.service';

@Component({
  templateUrl: './calendar-delete-dialog.component.html'
})
export class CalendarDeleteDialogComponent {
  calendar?: ICalendar;

  constructor(protected calendarService: CalendarService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calendarService.delete(id).subscribe(() => {
      this.eventManager.broadcast('calendarListModification');
      this.activeModal.close();
    });
  }
}
