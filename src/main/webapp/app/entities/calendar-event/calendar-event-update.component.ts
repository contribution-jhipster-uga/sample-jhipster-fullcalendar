import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICalendarEvent, CalendarEvent } from 'app/shared/model/calendar-event.model';
import { CalendarEventService } from './calendar-event.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ICalendar } from 'app/shared/model/calendar.model';
import { CalendarService } from 'app/entities/calendar/calendar.service';

type SelectableEntity = IUser | ICalendar;

@Component({
  selector: 'jhi-calendar-event-update',
  templateUrl: './calendar-event-update.component.html'
})
export class CalendarEventUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  calendars: ICalendar[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [],
    title: [],
    subTitle: [],
    description: [],
    longDescription: [],
    status: [],
    priority: [null, [Validators.min(0)]],
    place: [],
    location: [
      null,
      [
        Validators.pattern(
          '^((-?[1-8]?\\d(?:\\.\\d{1,18})?|90(?:\\.0{1,18})?),(-?(?:1[0-7]|[1-9])?\\d(?:\\.\\d{1,18})?|180(?:\\.0{1,18})?)(,[0-9]{2}))?$'
        )
      ]
    ],
    cssTheme: [],
    url: [null, [Validators.maxLength(200)]],
    isPublic: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [],
    openingHours: [],
    image: [null, []],
    imageContentType: [],
    imageSha1: [null, [Validators.minLength(40), Validators.maxLength(40), Validators.pattern('[a-f0-9]{40}')]],
    imageUrl: [null, [Validators.minLength(200)]],
    thumbnail: [null, []],
    thumbnailContentType: [],
    thumbnailSha1: [null, [Validators.minLength(40), Validators.maxLength(40), Validators.pattern('[a-f0-9]{40}')]],
    ical: [null, []],
    icalContentType: [],
    createdAt: [null, [Validators.required]],
    updatedAt: [],
    createdById: [],
    calendarId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected calendarEventService: CalendarEventService,
    protected userService: UserService,
    protected calendarService: CalendarService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendarEvent }) => {
      if (!calendarEvent.id) {
        const today = moment().startOf('day');
        calendarEvent.startDate = today;
        calendarEvent.endDate = today;
        calendarEvent.createdAt = today;
        calendarEvent.updatedAt = today;
      }

      this.updateForm(calendarEvent);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.calendarService.query().subscribe((res: HttpResponse<ICalendar[]>) => (this.calendars = res.body || []));
    });
  }

  updateForm(calendarEvent: ICalendarEvent): void {
    this.editForm.patchValue({
      id: calendarEvent.id,
      uid: calendarEvent.uid,
      title: calendarEvent.title,
      subTitle: calendarEvent.subTitle,
      description: calendarEvent.description,
      longDescription: calendarEvent.longDescription,
      status: calendarEvent.status,
      priority: calendarEvent.priority,
      place: calendarEvent.place,
      location: calendarEvent.location,
      cssTheme: calendarEvent.cssTheme,
      url: calendarEvent.url,
      isPublic: calendarEvent.isPublic,
      startDate: calendarEvent.startDate ? calendarEvent.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: calendarEvent.endDate ? calendarEvent.endDate.format(DATE_TIME_FORMAT) : null,
      openingHours: calendarEvent.openingHours,
      image: calendarEvent.image,
      imageContentType: calendarEvent.imageContentType,
      imageSha1: calendarEvent.imageSha1,
      imageUrl: calendarEvent.imageUrl,
      thumbnail: calendarEvent.thumbnail,
      thumbnailContentType: calendarEvent.thumbnailContentType,
      thumbnailSha1: calendarEvent.thumbnailSha1,
      ical: calendarEvent.ical,
      icalContentType: calendarEvent.icalContentType,
      createdAt: calendarEvent.createdAt ? calendarEvent.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: calendarEvent.updatedAt ? calendarEvent.updatedAt.format(DATE_TIME_FORMAT) : null,
      createdById: calendarEvent.createdById,
      calendarId: calendarEvent.calendarId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('fullCalendarApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calendarEvent = this.createFromForm();
    if (calendarEvent.id !== undefined) {
      this.subscribeToSaveResponse(this.calendarEventService.update(calendarEvent));
    } else {
      this.subscribeToSaveResponse(this.calendarEventService.create(calendarEvent));
    }
  }

  private createFromForm(): ICalendarEvent {
    return {
      ...new CalendarEvent(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      title: this.editForm.get(['title'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      longDescription: this.editForm.get(['longDescription'])!.value,
      status: this.editForm.get(['status'])!.value,
      priority: this.editForm.get(['priority'])!.value,
      place: this.editForm.get(['place'])!.value,
      location: this.editForm.get(['location'])!.value,
      cssTheme: this.editForm.get(['cssTheme'])!.value,
      url: this.editForm.get(['url'])!.value,
      isPublic: this.editForm.get(['isPublic'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? moment(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? moment(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      openingHours: this.editForm.get(['openingHours'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      imageSha1: this.editForm.get(['imageSha1'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      thumbnailContentType: this.editForm.get(['thumbnailContentType'])!.value,
      thumbnail: this.editForm.get(['thumbnail'])!.value,
      thumbnailSha1: this.editForm.get(['thumbnailSha1'])!.value,
      icalContentType: this.editForm.get(['icalContentType'])!.value,
      ical: this.editForm.get(['ical'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      createdById: this.editForm.get(['createdById'])!.value,
      calendarId: this.editForm.get(['calendarId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalendarEvent>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
