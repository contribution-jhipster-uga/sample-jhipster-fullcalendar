import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';


@Component({
  selector: 'jhi-event-modal',
  templateUrl: './event-modal.component.html'
})
export class EventModalComponent {
  @Input() eventObject: any;
  
  constructor(
    public activeModal: NgbActiveModal,
    protected jhiAlertService: JhiAlertService,
  ) {}

  toto(): void {
    // console.log(this.eventObject);
  }
}