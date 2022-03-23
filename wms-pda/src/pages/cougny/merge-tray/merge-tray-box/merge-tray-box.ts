import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams, ViewController } from 'ionic-angular';
import { ScanModel } from '../../../../models/DataBase.Model';
import { Utils } from '../../../../services/Utils';
import { NativeService } from "../../../../services/NativeService"

/**
 * Generated class for the CollageTaskBoxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-merge-tray-box',
  templateUrl: 'merge-tray-box.html',
})
export class MergeTrayBoxPage {
  scanModelList: ScanModel[] = [];//扫描箱码实体集合

  isShow: boolean = true;
  isChecked: boolean = false;
  resultList: any;

  skuListItem: any;
  constructor(public navCtrl: NavController, public navParams: NavParams,
    public nativeService: NativeService, public viewCtrl: ViewController,) {
    this.scanModelList = this.navParams.get('scanModelList');
    this.scanModelList.forEach(element => {
      element["checked"] = false;
    });
    this.resultList = this.navParams.get('result');

  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad CollageTaskBoxPage');
  }

  exit() {
    this.viewCtrl.dismiss(this.scanModelList);
  }

  itemClick(item) {
    if (item.checked) {
      item.checked = false;
    } else {
      item.checked = true;
    }
    let temp = [];
    this.scanModelList.forEach(element => {
      if (element["checked"]) {
        temp.push(true);
      }
    });
    if (temp.length == this.scanModelList.length) {
      this.isChecked = true;
    } else {
      this.isChecked = false;
    }
  }

  checkBoxChage() {
    if (this.isChecked) {
      this.scanModelList.forEach(element => {
        element["checked"] = true;
      });
    } else {
      let temp = [];
      this.scanModelList.forEach(element => {
        if (element["checked"]) {
          temp.push(true);
        }
      });
      if (temp.length == 0) {
        this.scanModelList.forEach(element => {
          element["checked"] = false;
        });
      }

    }
  }

  headSelected(item) {
    this.skuListItem = item;
  }
  btnDel() {
    // if (Utils.isEmpty(this.skuListItem)) {
    //   this.nativeService.showToast('请选择要删除的箱码');
    //   return;
    // }

    let temp = [];
    this.scanModelList.forEach(element => {
      if (element["checked"]) {
        temp.push(true);
      }
    });
    if (temp.length == 0) {
      this.nativeService.showToast('请选择要删除的箱码');
      return;
    }
    this.scanModelList = this.scanModelList.filter(x => x['checked'] != true);
  }
}
