import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FullCalendarTestModule } from '../../../test.module';
import { CalendarProviderDetailComponent } from 'app/entities/calendar-provider/calendar-provider-detail.component';
import { CalendarProvider } from 'app/shared/model/calendar-provider.model';

describe('Component Tests', () => {
  describe('CalendarProvider Management Detail Component', () => {
    let comp: CalendarProviderDetailComponent;
    let fixture: ComponentFixture<CalendarProviderDetailComponent>;
    const route = ({ data: of({ calendarProvider: new CalendarProvider(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FullCalendarTestModule],
        declarations: [CalendarProviderDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CalendarProviderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CalendarProviderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load calendarProvider on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.calendarProvider).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
