import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { StockdetailPage } from './stockdetail';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
    declarations: [
        StockdetailPage,
    ],
    imports: [
        IonicPageModule.forChild(StockdetailPage),
        CrumbsModule
    ],
    schemas:[NO_ERRORS_SCHEMA]
})
export class StockdetailPageModule { }
