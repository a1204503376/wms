import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { MovesBoxNewPage } from './movesboxnew';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    MovesBoxNewPage,
  ],
  imports: [
    DropdownModule,
    CalendarModule,
    IonicPageModule.forChild(MovesBoxNewPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class MovesBoxNewPageModule { }
