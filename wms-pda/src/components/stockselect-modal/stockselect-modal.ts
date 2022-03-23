import { Component } from '@angular/core';
import { NavController, NavParams, ViewController, IonicPage } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { NativeService } from '../../services/NativeService';
import { AppService } from '../../services/App.service';
import { Utils } from '../../services/Utils';
import { StockSelectModel } from '../../models/Stock.model';


@IonicPage()
@Component({
  selector: 'page-stockselect-modal',
  templateUrl: 'stockselect-modal.html'
})
export class StockSelectModal {
  records:StockSelectModal[]=[];
  public selectItem:StockSelectModel = new StockSelectModel();
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController,
    public storage: Storage,
    public nativeService: NativeService,
    public appService: AppService) {
    this.records = this.navParams.get('stockRecords');
    this.resetSelected();
  }

  /**
	 *   页面进入
	 */
  ionViewWillEnter() {
  }

  /**
 * 选择要处理的入库单
 */
  resetSelected(){
    for(let item of this.records){
      item['selected'] = false;
    }
  }
  headSelected(item) {
    this.resetSelected();
    item['selected'] = true;
    this.selectItem = item;
  }

  /**
   * 消失调用
   */
  dismiss() {
    this.viewCtrl.dismiss();
  }

  /**
   * 点击确定
   */
  submitdata() {
    if (Utils.isEmpty(this.selectItem)) {
      this.nativeService.showToast("请选择需要执行的选项!");
      return;
    }
    this.viewCtrl.dismiss(this.selectItem);
  }

}

