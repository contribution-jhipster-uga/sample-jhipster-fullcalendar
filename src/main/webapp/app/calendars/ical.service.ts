import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';


@Injectable({ providedIn: 'root' })
export class IcalService {

  public resourceUrl = SERVER_API_URL + 'api/calendar-events';

  constructor(protected http: HttpClient) { }

  ical(): Observable<HttpResponse<String>> {
    return this.http.get<String>(`${this.resourceUrl}/ical`, { observe: 'response' })
  }
}