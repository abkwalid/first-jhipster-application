import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DailyTaskLogComponent } from './list/daily-task-log.component';
import { DailyTaskLogDetailComponent } from './detail/daily-task-log-detail.component';
import { DailyTaskLogUpdateComponent } from './update/daily-task-log-update.component';
import { DailyTaskLogDeleteDialogComponent } from './delete/daily-task-log-delete-dialog.component';
import { DailyTaskLogRoutingModule } from './route/daily-task-log-routing.module';

@NgModule({
  imports: [SharedModule, DailyTaskLogRoutingModule],
  declarations: [DailyTaskLogComponent, DailyTaskLogDetailComponent, DailyTaskLogUpdateComponent, DailyTaskLogDeleteDialogComponent],
})
export class DailyTaskLogModule {}
