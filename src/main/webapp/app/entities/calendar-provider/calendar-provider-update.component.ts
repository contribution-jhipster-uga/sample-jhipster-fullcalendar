import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ICalendarProvider, CalendarProvider } from 'app/shared/model/calendar-provider.model';
import { CalendarProviderService } from './calendar-provider.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-calendar-provider-update',
  templateUrl: './calendar-provider-update.component.html'
})
export class CalendarProviderUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    provider: [null, [Validators.required]],
    identifier: [null, [Validators.required]],
    credential: [null, [Validators.required]],
    credentialExtra1: [],
    credentialExtra2: [],
    createdAt: [null, [Validators.required]],
    updatedAt: [],
    ownedById: []
  });

  constructor(
    protected calendarProviderService: CalendarProviderService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calendarProvider }) => {
      if (!calendarProvider.id) {
        const today = moment().startOf('day');
        calendarProvider.createdAt = today;
        calendarProvider.updatedAt = today;
      }

      this.updateForm(calendarProvider);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(calendarProvider: ICalendarProvider): void {
    this.editForm.patchValue({
      id: calendarProvider.id,
      provider: calendarProvider.provider,
      identifier: calendarProvider.identifier,
      credential: calendarProvider.credential,
      credentialExtra1: calendarProvider.credentialExtra1,
      credentialExtra2: calendarProvider.credentialExtra2,
      createdAt: calendarProvider.createdAt ? calendarProvider.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: calendarProvider.updatedAt ? calendarProvider.updatedAt.format(DATE_TIME_FORMAT) : null,
      ownedById: calendarProvider.ownedById
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calendarProvider = this.createFromForm();
    if (calendarProvider.id !== undefined) {
      this.subscribeToSaveResponse(this.calendarProviderService.update(calendarProvider));
    } else {
      this.subscribeToSaveResponse(this.calendarProviderService.create(calendarProvider));
    }
  }

  private createFromForm(): ICalendarProvider {
    return {
      ...new CalendarProvider(),
      id: this.editForm.get(['id'])!.value,
      provider: this.editForm.get(['provider'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      credential: this.editForm.get(['credential'])!.value,
      credentialExtra1: this.editForm.get(['credentialExtra1'])!.value,
      credentialExtra2: this.editForm.get(['credentialExtra2'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? moment(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      ownedById: this.editForm.get(['ownedById'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalendarProvider>>): void {
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
}
