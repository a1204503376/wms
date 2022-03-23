import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CopyLabelPage } from './copy-label';
import { ComponentsModule } from '../../../components/components.module';
import { CheckInBoxLabelTypePageModule } from '../../../shared/check-in-box-label-type/check-in-box-label-type.module';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CopyLabelPage,
  ],
  imports: [
    ComponentsModule,
    CheckInBoxLabelTypePageModule,
    IonicPageModule.forChild(CopyLabelPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CopyLabelPageModule { }
