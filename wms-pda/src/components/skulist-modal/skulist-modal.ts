import { Component } from '@angular/core';
import { NavController, NavParams, ViewController, IonicPage } from 'ionic-angular';
import { Storage } from '@ionic/storage';
import { NativeService } from '../../services/NativeService';
import { AppService } from '../../services/App.service';
import { AsnHeaderInfoModel } from '../../models/Instroe.model';
import { Utils } from '../../services/Utils';
import { SkuModel } from '../../models/SkuModel';


@IonicPage()
@Component({
  selector: 'page-skulist-modal',
  templateUrl: 'skulist-modal.html'
})
export class SkuListModal {
  //收货单列表
  asnBill: AsnHeaderInfoModel[] = [];

  //选中的收货单
  selectedAsnBIll: AsnHeaderInfoModel;
  asnType: string;
  skuList:SkuModel[]=[];
  skuListItem:SkuModel=new SkuModel();
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController,
    public storage: Storage,
    public nativeService: NativeService,
    public appService: AppService) {

    this.skuList = this.navParams.get('skuList');
  }

  /**
	 *   页面进入
	 */
  ionViewWillEnter() {
    //入库单列表取得
    this.getAsnBill(this.asnType);
  }

  /**
	 *   取到入库单列表
	 */
  getAsnBill(asnType) {

  }

  /**
 * 选择要处理的入库单
 */
  headSelected(item) {
    this.skuListItem = item;
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
    if (Utils.isEmpty(this.skuListItem)) {
      this.nativeService.showToast("请选择需要执行的物品!");
      return;
    }
    this.viewCtrl.dismiss(this.skuListItem);
  }

}

