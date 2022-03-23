import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CougnyPage } from './cougny';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CougnyPage
  ],
  imports: [
    IonicPageModule.forChild(CougnyPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CougnyPageModule { } 
