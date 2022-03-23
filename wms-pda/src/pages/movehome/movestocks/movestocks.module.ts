import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { moveStocks } from './movestocks';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
    declarations: [
        moveStocks,
    ],
    imports: [
        IonicPageModule.forChild(moveStocks),
        CrumbsModule
    ],
    schemas:[NO_ERRORS_SCHEMA]
})
export class moveStocksModule { }
