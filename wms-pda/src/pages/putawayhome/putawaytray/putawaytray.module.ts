import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PutawayTrayPage } from './putawaytray';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PutawayTrayPage,
  ],
  imports: [
    DropdownModule,
    CalendarModule,
    IonicPageModule.forChild(PutawayTrayPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PutawayTrayPageModule { }
