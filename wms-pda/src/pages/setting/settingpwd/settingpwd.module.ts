import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SettingPwdPage } from './settingpwd';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    SettingPwdPage,
  ],
  imports: [
    IonicPageModule.forChild(SettingPwdPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class SettingPwdPageModule {}
