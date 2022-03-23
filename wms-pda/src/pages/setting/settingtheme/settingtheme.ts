import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform } from 'ionic-angular';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";

@IonicPage()
@Component({
    selector: 'page-settingtheme',
    templateUrl: 'settingtheme.html',
})
export class SettingThemePage {
    colorPickerValue: string = '';
    constructor(public navCtrl: NavController,
        public navParams: NavParams,
        public platform: Platform,
        public appService: AppService,
        public nativeService: NativeService
    ) {
    }
    // 颜色选择器中事件，传递出来的值。
    colorPickerFun(colorType) {
        window.document.documentElement.setAttribute('data-theme', colorType);
    }
    btnOk() {

    }
    /**
       * 返回
       */
    back() {
        this.navCtrl.pop();
    }
}
