import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FullCalendarTestModule } from '../../../test.module';
import { CalendarEventUpdateComponent } from 'app/entities/calendar-event/calendar-event-update.component';
import { CalendarEventService } from 'app/entities/calendar-event/calendar-event.service';
import { CalendarEvent } from 'app/shared/model/calendar-event.model';

describe('Component Tests', () => {
  describe('CalendarEvent Management Update Component', () => {
    let comp: CalendarEventUpdateComponent;
    let fixture: ComponentFixture<CalendarEventUpdateComponent>;
    let service: CalendarEventService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FullCalendarTestModule],
        declarations: [CalendarEventUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CalendarEventUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalendarEventUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalendarEventService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CalendarEvent(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CalendarEvent();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
