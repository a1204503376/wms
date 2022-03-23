import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, Platform, App } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { AppService } from "../../services/App.service";
import { Storage } from '@ionic/storage';
import { Utils } from '../../services/Utils';
import { NativeService } from "../../services/NativeService"
/**
 * Generated class for the HomePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-cougny',
  templateUrl: 'cougny.html',
})
export class CougnyPage {
  public menuRole: any = [];

  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public platform: Platform,
    public statusBar: StatusBar,
    public appService: AppService,
    public storage: Storage,
    public nativeService: NativeService,
    private app: App,

  ) {
    console.log('父组件');
  }

  ionViewWillEnter() {
    if (Utils.isNotEmpty(this.navParams.get('menuRole'))) {
      this.menuRole = this.navParams.get('menuRole');
      if (this.menuRole.length < 9) {
        let count = 9 - this.menuRole.length;
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
