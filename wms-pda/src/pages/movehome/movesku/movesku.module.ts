import { NgModule ,NO_ERRORS_SCHEMA} from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { moveSku } from './movesku';
import { CrumbsModule } from '../../../components/crumbs/crumbs.module';
@NgModule({
    declarations: [
        moveSku,
    ],
    imports: [
        IonicPageModule.forChild(moveSku),
        CrumbsModule
    ],
    schemas:[NO_ERRORS_SCHEMA]
})
export class moveSkuModule { }
