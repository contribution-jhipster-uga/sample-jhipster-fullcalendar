import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';


@Injectable({ providedIn: 'root' })
export class IcalService {

  public resourceUrl = SERVER_API_URL + 'api/calendar-events';

  constructor(protected http: HttpClient) { }

  exportIcal(): Observable<string> {
    return this.http.get(`${this.resourceUrl}/ical`, { observe: 'body', responseType: "text" })
  }

  importIcal(uploadData: FormData): Observable<HttpResponse<Object>> {
    return this.http.post(`${this.resourceUrl}/ical`, uploadData, { observe: 'response' });
  }
}