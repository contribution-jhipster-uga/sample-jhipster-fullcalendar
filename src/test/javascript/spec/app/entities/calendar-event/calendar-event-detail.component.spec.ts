import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { FullCalendarTestModule } from '../../../test.module';
import { CalendarEventDetailComponent } from 'app/entities/calendar-event/calendar-event-detail.component';
import { CalendarEvent } from 'app/shared/model/calendar-event.model';

describe('Component Tests', () => {
  describe('CalendarEvent Management Detail Component', () => {
    let comp: CalendarEventDetailComponent;
    let fixture: ComponentFixture<CalendarEventDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ calendarEvent: new CalendarEvent(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FullCalendarTestModule],
        declarations: [CalendarEventDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CalendarEventDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalendarEventDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load calendarEvent on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.calendarEvent).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
