import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ReplenishmenPage } from './replenishmen';
import { CheckInBoxLabelTypePageModule } from '../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    ReplenishmenPage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    DropdownModule,
    CalendarModule,
    IonicPageModule.forChild(ReplenishmenPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class ReplenishmenPageModule { }
