import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { moveStock } from './movestock';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
    declarations: [
        moveStock,
    ],
    imports: [
        IonicPageModule.forChild(moveStock),
        CrumbsModule
    ],
    schemas:[NO_ERRORS_SCHEMA]
})
export class moveStockModule { }
