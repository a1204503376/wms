import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CyclecountrantaskPage } from './cyclecountrantask';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    CyclecountrantaskPage,
  ],
  imports: [
    IonicPageModule.forChild(CyclecountrantaskPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class CyclecountrantaskPageModule {}
