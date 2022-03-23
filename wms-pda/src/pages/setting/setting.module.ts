import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SettingPage } from './setting';
import { CrumbsModule } from '../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    SettingPage,
  ],
  imports: [
    IonicPageModule.forChild(SettingPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class SettingPageModule {}
