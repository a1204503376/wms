import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform, App } from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { NativeService } from "../../../services/NativeService"
import { AppService } from "../../../services/App.service";
import { Api, ContenType, Method } from '../../../utils/appConstant';

@IonicPage()
@Component({
    selector: 'page-settingpwd',
    templateUrl: 'settingpwd.html',
})
export class SettingPwdPage {
    public id: string;
    public oldPassword: string;
    public newPassword: string;
    public newPassword1: string;
    public userInfo: any;//用户信息
    constructor(public navCtrl: NavController,
        public navParams: NavParams,
        public platform: Platform,
        public appService: AppService,
        public nativeService: NativeService,
        private app: App
    ) {
        if (Utils.isNotEmpty(this.navParams.get('userInfo'))) {
            this.userInfo = this.navParams.get('userInfo');
            this.id = this.userInfo.user_id;
        } else {
            this.nativeService.showToast('获取用户信息失败！');
        }
    }
    btnOk() {
        // let params = {
        //     id: this.id,
        //     oldPassword: this.oldPassword,
        //     newPassword: this.newPassword,
        //     newPassword1: this.newPassword1
        // }
        let params = "id=" + this.id
            + "&oldPassword=" + this.oldPassword
            + "&newPassword=" + this.newPassword
            + "&newPassword1=" + this.newPassword1;
        this.appService.httpRequest(Api.api + Api.UpdatePassword, Method.Post, params, ContenType.Form, result => {
            if (result) {
                this.nativeService.showToast('修改成功！');
                this.app.getRootNav().setRoot('LoginPage');
            } else {
                this.nativeService.showToast('修改失败！');
            }
        });
    }
    /**
       * 返回
       */
    back() {
        this.navCtrl.pop();
    }
}
