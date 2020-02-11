import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CalendarEventService } from 'app/entities/calendar-event/calendar-event.service';
import { ICalendarEvent, CalendarEvent } from 'app/shared/model/calendar-event.model';
import { TypeCalendarEventStatus } from 'app/shared/model/enumerations/type-calendar-event-status.model';

describe('Service Tests', () => {
  describe('CalendarEvent Service', () => {
    let injector: TestBed;
    let service: CalendarEventService;
    let httpMock: HttpTestingController;
    let elemDefault: ICalendarEvent;
    let expectedResult: ICalendarEvent | ICalendarEvent[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CalendarEventService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new CalendarEvent(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        TypeCalendarEventStatus.TENTATIVE,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        currentDate,
        currentDate,
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        currentDate,
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CalendarEvent', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createdAt: currentDate,
            updatedAt: currentDate
          },
          returnedFromService
        );

        service.create(new CalendarEvent()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CalendarEvent', () => {
        const returnedFromService = Object.assign(
          {
            uid: 'BBBBBB',
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            description: 'BBBBBB',
            longDescription: 'BBBBBB',
            status: 'BBBBBB',
            priority: 1,
            place: 'BBBBBB',
            location: 'BBBBBB',
            cssTheme: 'BBBBBB',
            url: 'BBBBBB',
            isPublic: true,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            openingHours: 'BBBBBB',
            image: 'BBBBBB',
            imageSha1: 'BBBBBB',
            imageUrl: 'BBBBBB',
            thumbnail: 'BBBBBB',
            thumbnailSha1: 'BBBBBB',
            ical: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createdAt: currentDate,
            updatedAt: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CalendarEvent', () => {
        const returnedFromService = Object.assign(
          {
            uid: 'BBBBBB',
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            description: 'BBBBBB',
            longDescription: 'BBBBBB',
            status: 'BBBBBB',
            priority: 1,
            place: 'BBBBBB',
            location: 'BBBBBB',
            cssTheme: 'BBBBBB',
            url: 'BBBBBB',
            isPublic: true,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            openingHours: 'BBBBBB',
            image: 'BBBBBB',
            imageSha1: 'BBBBBB',
            imageUrl: 'BBBBBB',
            thumbnail: 'BBBBBB',
            thumbnailSha1: 'BBBBBB',
            ical: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            updatedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createdAt: currentDate,
            updatedAt: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CalendarEvent', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
