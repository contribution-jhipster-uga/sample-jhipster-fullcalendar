import { Moment } from 'moment';
import { TypeCalendarEventStatus } from 'app/shared/model/enumerations/type-calendar-event-status.model';

export interface ICalendarEvent {
  id?: number;
  uid?: string;
  title?: string;
  subTitle?: string;
  description?: string;
  longDescription?: any;
  status?: TypeCalendarEventStatus;
  priority?: number;
  place?: string;
  location?: string;
  cssTheme?: string;
  url?: string;
  isPublic?: boolean;
  startDate?: Moment;
  endDate?: Moment;
  openingHours?: string;
  imageContentType?: string;
  image?: any;
  imageSha1?: string;
  imageUrl?: string;
  thumbnailContentType?: string;
  thumbnail?: any;
  thumbnailSha1?: string;
  icalContentType?: string;
  ical?: any;
  createdAt?: Moment;
  updatedAt?: Moment;
  createdByLogin?: string;
  createdById?: number;
  calendarTitle?: string;
  calendarId?: number;
}

export class CalendarEvent implements ICalendarEvent {
  constructor(
    public id?: number,
    public uid?: string,
    public title?: string,
    public subTitle?: string,
    public description?: string,
    public longDescription?: any,
    public status?: TypeCalendarEventStatus,
    public priority?: number,
    public place?: string,
    public location?: string,
    public cssTheme?: string,
    public url?: string,
    public isPublic?: boolean,
    public startDate?: Moment,
    public endDate?: Moment,
    public openingHours?: string,
    public imageContentType?: string,
    public image?: any,
    public imageSha1?: string,
    public imageUrl?: string,
    public thumbnailContentType?: string,
    public thumbnail?: any,
    public thumbnailSha1?: string,
    public icalContentType?: string,
    public ical?: any,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public createdByLogin?: string,
    public createdById?: number,
    public calendarTitle?: string,
    public calendarId?: number
  ) {
    this.isPublic = this.isPublic || false;
  }
}
