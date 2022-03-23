import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../services/Utils';
import { Api, ContenType, Method } from '../../utils/appConstant';
import { AppService } from "../../services/App.service";
/**
 * Generated class for the StockhomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
    selector: 'page-stockhome',
    templateUrl: 'stockhome.html',
})
export class StockhomePage {
    public menuRole: any = [];

    constructor(public navCtrl: NavController, public navParams: NavParams,public appService: AppService,) {
    }
    ionViewWillEnter() {
        if (Utils.isNotEmpty(this.navParams.get('menuRole'))) {
            this.menuRole = this.navParams.get('menuRole');
            if (this.menuRole.length < 15) {
                let count = 15 - this.menuRole.length;
                for (let index = 0; index < count; index++) {
                    this.menuRole.push({ source: '' });
                }
            }
        } else {
            this.navCtrl.pop();
        }
    }
    expression(event) {
        this.navCtrl.push(event.path);
      
    }
}
