import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICalendarProvider } from 'app/shared/model/calendar-provider.model';

type EntityResponseType = HttpResponse<ICalendarProvider>;
type EntityArrayResponseType = HttpResponse<ICalendarProvider[]>;

@Injectable({ providedIn: 'root' })
export class CalendarProviderService {
  public resourceUrl = SERVER_API_URL + 'api/calendar-providers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/calendar-providers';

  constructor(protected http: HttpClient) {}

  create(calendarProvider: ICalendarProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendarProvider);
    return this.http
      .post<ICalendarProvider>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(calendarProvider: ICalendarProvider): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(calendarProvider);
    return this.http
      .put<ICalendarProvider>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICalendarProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendarProvider[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICalendarProvider[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(calendarProvider: ICalendarProvider): ICalendarProvider {
    const copy: ICalendarProvider = Object.assign({}, calendarProvider, {
      createdAt: calendarProvider.createdAt && calendarProvider.createdAt.isValid() ? calendarProvider.createdAt.toJSON() : undefined,
      updatedAt: calendarProvider.updatedAt && calendarProvider.updatedAt.isValid() ? calendarProvider.updatedAt.toJSON() : undefined
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
      res.body.forEach((calendarProvider: ICalendarProvider) => {
        calendarProvider.createdAt = calendarProvider.createdAt ? moment(calendarProvider.createdAt) : undefined;
        calendarProvider.updatedAt = calendarProvider.updatedAt ? moment(calendarProvider.updatedAt) : undefined;
      });
    }
    return res;
  }
}
