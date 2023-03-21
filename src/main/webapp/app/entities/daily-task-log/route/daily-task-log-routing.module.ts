import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DailyTaskLogComponent } from '../list/daily-task-log.component';
import { DailyTaskLogDetailComponent } from '../detail/daily-task-log-detail.component';
import { DailyTaskLogUpdateComponent } from '../update/daily-task-log-update.component';
import { DailyTaskLogRoutingResolveService } from './daily-task-log-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dailyTaskLogRoute: Routes = [
  {
    path: '',
    component: DailyTaskLogComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DailyTaskLogDetailComponent,
    resolve: {
      dailyTaskLog: DailyTaskLogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DailyTaskLogUpdateComponent,
    resolve: {
      dailyTaskLog: DailyTaskLogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DailyTaskLogUpdateComponent,
    resolve: {
      dailyTaskLog: DailyTaskLogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dailyTaskLogRoute)],
  exports: [RouterModule],
})
export class DailyTaskLogRoutingModule {}
