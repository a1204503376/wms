import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ReplenishmenhomePage } from './replenishmenhome';
import { CheckInBoxLabelTypePageModule } from '../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    ReplenishmenhomePage,
  ],
  imports: [
    CheckInBoxLabelTypePageModule,
    DropdownModule,
    CalendarModule,
    IonicPageModule.forChild(ReplenishmenhomePage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class ReplenishmenhomePageModule { }
