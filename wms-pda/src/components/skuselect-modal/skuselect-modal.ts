import { Component } from '@angular/core';
import { NavController, NavParams, ViewController, IonicPage } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { NativeService } from '../../services/NativeService';
import { AppService } from '../../services/App.service';
import { Utils } from '../../services/Utils';
import { SkuSelectModel } from '../../models/SkuCount.model';


@IonicPage()
@Component({
  selector: 'page-skuselect-modal',
  templateUrl: 'skuselect-modal.html'
})
export class SkuSelectModal {
  records:SkuSelectModal[]=[];
  public selectItem:SkuSelectModel = new SkuSelectModel();
  public recordsbak:SkuSelectModal[]=[];
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController,
    public storage: Storage,
    public nativeService: NativeService,
    public appService: AppService) {
    this.records = this.navParams.get('skuRecords');
    Object.assign(this.recordsbak,this.records);
    this.resetSelected();
  }

fillskulots(item){
  let skulots = [];
  for (let prop in item) {
    if (prop.search('skuLot') != -1 && item[prop] != '') {
      skulots.push(item[prop]);
    }
  }
  return skulots;
}
  /**
	 *   页面进入
	 */
  ionViewWillEnter() {
  }

 
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
  getItems(ev: any) {
    this.records = []; 
    this.records = this.recordsbak;
    const val = ev.target.value;
    if (val && val.trim() != '') {
      this.records = this.records.filter((item) => {
        return (item['skuCode'].indexOf(val) > -1);
      })
    }
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

