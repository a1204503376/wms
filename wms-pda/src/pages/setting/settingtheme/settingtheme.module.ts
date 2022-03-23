import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SettingThemePage } from './settingtheme';
//import { ColorPickerModule } from 'ngx-color-picker';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    SettingThemePage,
  ],
  imports: [
    IonicPageModule.forChild(SettingThemePage),
    CrumbsModule
    //ColorPickerModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class SettingThemePageModule {}
