import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FullCalendarTestModule } from '../../../test.module';
import { CalendarProviderUpdateComponent } from 'app/entities/calendar-provider/calendar-provider-update.component';
import { CalendarProviderService } from 'app/entities/calendar-provider/calendar-provider.service';
import { CalendarProvider } from 'app/shared/model/calendar-provider.model';

describe('Component Tests', () => {
  describe('CalendarProvider Management Update Component', () => {
    let comp: CalendarProviderUpdateComponent;
    let fixture: ComponentFixture<CalendarProviderUpdateComponent>;
    let service: CalendarProviderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FullCalendarTestModule],
        declarations: [CalendarProviderUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CalendarProviderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalendarProviderUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CalendarProviderService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CalendarProvider(123);
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
        const entity = new CalendarProvider();
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
