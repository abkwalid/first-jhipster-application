import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DailyTaskLogFormService, DailyTaskLogFormGroup } from './daily-task-log-form.service';
import { IDailyTaskLog } from '../daily-task-log.model';
import { DailyTaskLogService } from '../service/daily-task-log.service';

@Component({
  selector: 'jhi-daily-task-log-update',
  templateUrl: './daily-task-log-update.component.html',
})
export class DailyTaskLogUpdateComponent implements OnInit {
  isSaving = false;
  dailyTaskLog: IDailyTaskLog | null = null;

  editForm: DailyTaskLogFormGroup = this.dailyTaskLogFormService.createDailyTaskLogFormGroup();

  constructor(
    protected dailyTaskLogService: DailyTaskLogService,
    protected dailyTaskLogFormService: DailyTaskLogFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyTaskLog }) => {
      this.dailyTaskLog = dailyTaskLog;
      if (dailyTaskLog) {
        this.updateForm(dailyTaskLog);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dailyTaskLog = this.dailyTaskLogFormService.getDailyTaskLog(this.editForm);
    if (dailyTaskLog.id !== null) {
      this.subscribeToSaveResponse(this.dailyTaskLogService.update(dailyTaskLog));
    } else {
      this.subscribeToSaveResponse(this.dailyTaskLogService.create(dailyTaskLog));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDailyTaskLog>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(dailyTaskLog: IDailyTaskLog): void {
    this.dailyTaskLog = dailyTaskLog;
    this.dailyTaskLogFormService.resetForm(this.editForm, dailyTaskLog);
  }
}
