import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SearchOrderPage } from './search-order';
import { CrumbsModule } from '../../../../components/crumbs/crumbs.module';
@NgModule({
  declarations: [
    SearchOrderPage,
  ],
  imports: [
    IonicPageModule.forChild(SearchOrderPage),
    CrumbsModule
  ],
  schemas:[NO_ERRORS_SCHEMA]
})
export class SearchOrderPageModule {}
