import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PutawayboxPage } from './putawaybox';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PutawayboxPage,
  ],
  imports: [
    DropdownModule,
    CalendarModule,
    IonicPageModule.forChild(PutawayboxPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PutawayboxPageModule { }
