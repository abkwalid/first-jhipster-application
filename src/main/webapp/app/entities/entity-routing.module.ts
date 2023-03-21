import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'daily-task-log',
        data: { pageTitle: 'firstJhipsterApplicationApp.dailyTaskLog.home.title' },
        loadChildren: () => import('./daily-task-log/daily-task-log.module').then(m => m.DailyTaskLogModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
