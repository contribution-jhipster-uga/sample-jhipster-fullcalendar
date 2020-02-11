import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICalendar } from 'app/shared/model/calendar.model';

type EntityResponseType = HttpResponse<ICalendar>;
type EntityArrayResponseType = HttpResponse<ICalendar[]>;

@Injectable({ providedIn: 'root' })
export class CalendarService {
  public resourceUrl = SERVER_API_URL + 'api/calendars';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/calendars';

  constructor(protected http: HttpClient) {}

  create(calendar: ICalendar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendar);
    return this.http
      .post<ICalendar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(calendar: ICalendar): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendar);
    return this.http
      .put<ICalendar>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICalendar>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendar[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendar[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(calendar: ICalendar): ICalendar {
    const copy: ICalendar = Object.assign({}, calendar, {
      createdAt: calendar.createdAt && calendar.createdAt.isValid() ? calendar.createdAt.toJSON() : undefined,
      updatedAt: calendar.updatedAt && calendar.updatedAt.isValid() ? calendar.updatedAt.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? moment(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((calendar: ICalendar) => {
        calendar.createdAt = calendar.createdAt ? moment(calendar.createdAt) : undefined;
        calendar.updatedAt = calendar.updatedAt ? moment(calendar.updatedAt) : undefined;
      });
    }
    return res;
  }
}
