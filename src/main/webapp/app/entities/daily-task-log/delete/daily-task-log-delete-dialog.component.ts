import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDailyTaskLog } from '../daily-task-log.model';
import { DailyTaskLogService } from '../service/daily-task-log.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './daily-task-log-delete-dialog.component.html',
})
export class DailyTaskLogDeleteDialogComponent {
  dailyTaskLog?: IDailyTaskLog;

  constructor(protected dailyTaskLogService: DailyTaskLogService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dailyTaskLogService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
