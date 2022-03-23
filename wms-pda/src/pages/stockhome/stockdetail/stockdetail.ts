import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";
import { Utils } from '../../../services/Utils';
import { AppConstant, Api, ContenType, Method, BaseCodeConstant } from '../../../utils/appConstant';

/**
 * Generated class for the moveSku page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
    selector: 'page-stockdetail',
    templateUrl: 'stockdetail.html',
})
export class StockdetailPage {
    public stockVO: any;//库存信息
    public serialList: any;//序列号列表
    constructor(public navCtrl: NavController,
        public navParams: NavParams,
        public nativeService: NativeService) {
        if (Utils.isNotEmpty(this.navParams.get('stockVO'))) {
            this.stockVO = this.navParams.get('stockVO');
            if (this.stockVO.serialList != null)
                this.serialList = this.stockVO.serialList;
        } else {
            this.nativeService.showToast('请选择库存信息');
        }
    }
    ionViewDidLoad() {
        console.log('ionViewDidLoad StockdetailPage');
    }
    expression() {
    }
    detailed() {
    }
    scrollToTop() {
        setTimeout(() => {
            window.scrollTo(0, document.body.scrollTop + 1);
            document.body.scrollTop >= 1 && window.scrollTo(0, document.body.scrollTop - 1);
        }, 10)
    }

}
