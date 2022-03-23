import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { PutawayboxNewPage } from './putawayboxnew';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    PutawayboxNewPage,
  ],
  imports: [
    DropdownModule,
    CalendarModule,
    IonicPageModule.forChild(PutawayboxNewPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class PutawayboxNewPageModule { }
