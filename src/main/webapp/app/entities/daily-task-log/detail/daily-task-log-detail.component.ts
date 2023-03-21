import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDailyTaskLog } from '../daily-task-log.model';

@Component({
  selector: 'jhi-daily-task-log-detail',
  templateUrl: './daily-task-log-detail.component.html',
})
export class DailyTaskLogDetailComponent implements OnInit {
  dailyTaskLog: IDailyTaskLog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dailyTaskLog }) => {
      this.dailyTaskLog = dailyTaskLog;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
