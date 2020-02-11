import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICalendar, Calendar } from 'app/shared/model/calendar.model';
import { CalendarService } from './calendar.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-calendar-update',
  templateUrl: './calendar-update.component.html'
})
export class CalendarUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [],
    title: [],
    subTitle: [],
    description: [],
    longDescription: [],
    createdAt: [null, [Validators.required]],
    updatedAt: [],
    ownedById: [],
    sharedWiths: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected calendarService: CalendarService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendar }) => {
      if (!calendar.id) {
        const today = moment().startOf('day');
        calendar.createdAt = today;
        calendar.updatedAt = today;
      }

      this.updateForm(calendar);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(calendar: ICalendar): void {
    this.editForm.patchValue({
      id: calendar.id,
      uid: calendar.uid,
      title: calendar.title,
      subTitle: calendar.subTitle,
      description: calendar.description,
      longDescription: calendar.longDescription,
      createdAt: calendar.createdAt ? calendar.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: calendar.updatedAt ? calendar.updatedAt.format(DATE_TIME_FORMAT) : null,
      ownedById: calendar.ownedById,
      sharedWiths: calendar.sharedWiths
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calendar = this.createFromForm();
    if (calendar.id !== undefined) {
      this.subscribeToSaveResponse(this.calendarService.update(calendar));
    } else {
      this.subscribeToSaveResponse(this.calendarService.create(calendar));
    }
  }

  private createFromForm(): ICalendar {
    return {
      ...new Calendar(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      title: this.editForm.get(['title'])!.value,
      subTitle: this.editForm.get(['subTitle'])!.value,
      description: this.editForm.get(['description'])!.value,
      longDescription: this.editForm.get(['longDescription'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      ownedById: this.editForm.get(['ownedById'])!.value,
      sharedWiths: this.editForm.get(['sharedWiths'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalendar>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
