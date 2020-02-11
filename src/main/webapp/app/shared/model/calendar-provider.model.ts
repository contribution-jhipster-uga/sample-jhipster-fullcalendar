import { Moment } from 'moment';
import { TypeCalendarProvider } from 'app/shared/model/enumerations/type-calendar-provider.model';

export interface ICalendarProvider {
  id?: number;
  provider?: TypeCalendarProvider;
  identifier?: string;
  credential?: string;
  credentialExtra1?: string;
  credentialExtra2?: string;
  createdAt?: Moment;
  updatedAt?: Moment;
  ownedByLogin?: string;
  ownedById?: number;
}

export class CalendarProvider implements ICalendarProvider {
  constructor(
    public id?: number,
    public provider?: TypeCalendarProvider,
    public identifier?: string,
    public credential?: string,
    public credentialExtra1?: string,
    public credentialExtra2?: string,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public ownedByLogin?: string,
    public ownedById?: number
  ) {}
}
