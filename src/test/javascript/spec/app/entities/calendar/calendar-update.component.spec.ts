import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FullCalendarTestModule } from '../../../test.module';
import { CalendarUpdateComponent } from 'app/entities/calendar/calendar-update.component';
import { CalendarService } from 'app/entities/calendar/calendar.service';
import { Calendar } from 'app/shared/model/calendar.model';

describe('Component Tests', () => {
  describe('Calendar Management Update Component', () => {
    let comp: CalendarUpdateComponent;
    let fixture: ComponentFixture<CalendarUpdateComponent>;
    let service: CalendarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FullCalendarTestModule],
        declarations: [CalendarUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CalendarUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalendarUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalendarService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Calendar(123);
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
        const entity = new Calendar();
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
