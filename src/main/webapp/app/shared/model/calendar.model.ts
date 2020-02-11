import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface ICalendar {
  id?: number;
  uid?: string;
  title?: string;
  subTitle?: string;
  description?: string;
  longDescription?: any;
  createdAt?: Moment;
  updatedAt?: Moment;
  ownedByLogin?: string;
  ownedById?: number;
  sharedWiths?: IUser[];
}

export class Calendar implements ICalendar {
  constructor(
    public id?: number,
    public uid?: string,
    public title?: string,
    public subTitle?: string,
    public description?: string,
    public longDescription?: any,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public ownedByLogin?: string,
    public ownedById?: number,
    public sharedWiths?: IUser[]
  ) {}
}
