import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICalendarEvent } from 'app/shared/model/calendar-event.model';

type EntityResponseType = HttpResponse<ICalendarEvent>;
type EntityArrayResponseType = HttpResponse<ICalendarEvent[]>;

@Injectable({ providedIn: 'root' })
export class CalendarEventService {
  public resourceUrl = SERVER_API_URL + 'api/calendar-events';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/calendar-events';

  constructor(protected http: HttpClient) {}

  create(calendarEvent: ICalendarEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendarEvent);
    return this.http
      .post<ICalendarEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(calendarEvent: ICalendarEvent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendarEvent);
    return this.http
      .put<ICalendarEvent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICalendarEvent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendarEvent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendarEvent[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(calendarEvent: ICalendarEvent): ICalendarEvent {
    const copy: ICalendarEvent = Object.assign({}, calendarEvent, {
      startDate: calendarEvent.startDate && calendarEvent.startDate.isValid() ? calendarEvent.startDate.toJSON() : undefined,
      endDate: calendarEvent.endDate && calendarEvent.endDate.isValid() ? calendarEvent.endDate.toJSON() : undefined,
      createdAt: calendarEvent.createdAt && calendarEvent.createdAt.isValid() ? calendarEvent.createdAt.toJSON() : undefined,
      updatedAt: calendarEvent.updatedAt && calendarEvent.updatedAt.isValid() ? calendarEvent.updatedAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? moment(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((calendarEvent: ICalendarEvent) => {
        calendarEvent.startDate = calendarEvent.startDate ? moment(calendarEvent.startDate) : undefined;
        calendarEvent.endDate = calendarEvent.endDate ? moment(calendarEvent.endDate) : undefined;
        calendarEvent.createdAt = calendarEvent.createdAt ? moment(calendarEvent.createdAt) : undefined;
        calendarEvent.updatedAt = calendarEvent.updatedAt ? moment(calendarEvent.updatedAt) : undefined;
      });
    }
    return res;
  }
}
