import { Component, ViewChild, NgZone } from '@angular/core';
import { IonicPage, NavController, NavParams, ModalController, Events, PopoverController} from 'ionic-angular';
import { Utils } from '../../../services/Utils';
import { AppService } from "../../../services/App.service";
import { Api,ContenType, Method } from '../../../utils/appConstant';
import { NativeService } from "../../../services/NativeService"
import { BarcodeService, BarcodeType } from '../../../services/BarCodeService';
import { AppGlobal } from '../../../services/AppGlobal';
import { Storage } from '@ionic/storage';
@IonicPage()
@Component({
  selector: 'page-stock-frozen',
  templateUrl: 'stock-frozen.html',
})
export class StockFrozenPage {
  public searchQuery: string;//搜索框文字
  public infiniteScroll:any;
  public current: number = 1;//当前页 
  public size: number = 10;//每页条数
  public stockVOList: any[] = [];//库存查询列表
  public locCode: string = "";//库位编码
  public skuCode:string = "";
  public skuLotNum:any;
  // public paramValue:string;
  public locFrozenFlag:boolean = false;
  public multFrozenFlag:boolean = false;
  public currentType:string = "";
  public lockFlag=false;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public popoverCtrl: PopoverController,
    private storage: Storage,
    public modalController: ModalController,
    public appService: AppService,
    public nativeService: NativeService,
    public barcodeService: BarcodeService,
    public events: Events,
    public ngZone: NgZone
  ) {
    this.skuLotNum = Array(30).fill(0).map((x,i)=>i);
    // this.storage.get('sysParam').then(data => {
    //   this.paramValue = data.filter(x => x.paramKey == 'system::lpnEnable')[0]["paramValue"];
    // });
  }
  ionViewWillEnter() {
     this.events.subscribe('barcode:scan', (barcode, time) => {
      let bt = this.barcodeService.GetBarcodeType(barcode);
      if(bt!= BarcodeType.HuoWei&&bt!= BarcodeType.BoxCode){
        this.nativeService.showToast("请扫描库位或箱码！");
        return;
      }
      if (bt == BarcodeType.BoxCode) {
        this.ngZone.run(() => {
          this.barcodeService.ScanSkuBarcode(this.searchQuery).subscribe(val => {
            if(Utils.isEmpty(val))return;
            this.skuCode = val.skucode;
            this.resetPage();
            this.currentType = '1';
            this.queryStockRequest();
          });
        });
      } 
      if(bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.locCode = barcode;
          this.resetPage();
          this.currentType = '0';
          this.queryStockRequest();
        });
      }
    });
  }
  ionViewWillLeave() {
    AppGlobal.removeSubscribe(this);
  }
  ionViewDidLoad() {
  }
  presentPopover(myEvent) {
    let popover = this.popoverCtrl.create('PopoverPage',{show_item_3:false});
    popover.onDidDismiss(data => {
      if (Utils.isNotEmpty(data) && Utils.isNotEmpty(this.stockVOList)) {
        console.log(this.stockVOList);
        if (data == 'allCheck') {
          //全选
          for (let index = 0; index < this.stockVOList.length; index++) {
            this.stockVOList[index].CheckStock = true;
          }
        } else if (data == 'clearCheck') {
          //取消全选
          for (let index = 0; index < this.stockVOList.length; index++) {
            this.stockVOList[index].CheckStock = false;
          }
        }
      }
    });
    popover.present({
      ev: myEvent
    });
  }
  queryStockRequest(){
    let params = {
      'locCode':this.locCode,
      'skuCode':this.skuCode
    };
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.StockQuery
      + "?current=" + this.current
      + "&size=" + this.size
      + "&type="+this.currentType,
      Method.Post, params, ContenType.Json, result => {
        this.nativeService.hideLoading();
        if (Utils.isEmpty(result.data.records)) {
          if(this.infiniteScroll){
            this.infiniteScroll.enable(false);
            this.infiniteScroll.complete(); 
          }else{
            this.nativeService.showToast('暂无库存信息！');
          }
          return;
        } 
        this.stockVOList = this.stockVOList.concat(result.data.records);
        let lockFlag = this.stockVOList[0]['lockFlag'];
        if(lockFlag=='1'){
          this.locFrozenFlag = true;
          this.lockFlag=true;
        }else{
          this.locFrozenFlag= false;
          this.lockFlag=false;
        }
        this.current++;
        if (this.infiniteScroll) {
          this.infiniteScroll.complete();  
        }
      });
  }
  keyEnter(event) {
    if (event && event.keyCode == 13) {
      if(Utils.isEmpty(this.searchQuery)){
        this.nativeService.showToast("请输入库位或物料编码！");
        return;
      }
      let bt = this.barcodeService.GetBarcodeType(this.searchQuery);
      if(bt!= BarcodeType.HuoWei&&bt!= BarcodeType.SKU){
        this.nativeService.showToast("请输入正确格式的库位或物料编码！");
        return;
      }
      if(bt == BarcodeType.HuoWei) {
        this.ngZone.run(() => {
          this.locCode = this.searchQuery;
          this.resetPage();
          this.currentType = '0';
          this.queryStockRequest();
        });
      }
      if (bt == BarcodeType.SKU) {
        this.ngZone.run(() => {
            this.skuCode = this.searchQuery;
            this.resetPage();
            this.currentType = '1';
            this.queryStockRequest();
        });
      } 
   
    }
  }
  resetPage(){
    this.stockVOList = [];
    if(this.infiniteScroll){
      this.infiniteScroll.enable(true);
    }
    this.size = 10;
    this.current = 1;
  }
 
  range(item:any) { 
    var arr = [];
    for (var i = 1; i <= this.skuLotNum; i++) {
      if(item[`skuLot${i}`]&&item[`skuLot${i}`]!=''){
        arr.push(item[`skuLot${i}`]);
      }
    } 
    return arr;
  }
  singleClick(item){
      this.requstFrozen('singleFrozen',item.stockId,'',item);
  }
  /** 
   *  type:冻结类型 singleFrozen:单个冻结,multFrozen:批量冻结,locFrozen:库位冻结
   *  querykey:单个库存id|多个库存id|库位编号
   *  flag:1:冻结 0:解冻
  **/
  requstFrozen(type,querykey,flag,item){
    let params = {
      'type':type,
      'queryKey':querykey,
      'flag':flag
    };
    this.nativeService.showLoading();
    this.appService.httpRequest(Api.api + Api.frozenForSku,
      Method.Post, params, ContenType.Json, result => {
        this.nativeService.hideLoading();
        if(result.success){
          if(type == "singleFrozen"){
            if(result.data){
              item.stockStatus = 1;
            }else{
              item.stockStatus = 0;
            }
          }else{
            if(flag=='1'){
              this.locFrozenFlag = true;
              this.multFrozenFlag = true;
            }else{
              this.locFrozenFlag = false;
              this.multFrozenFlag = false;
            }
            this.resetPage();
            this.queryStockRequest();
          }
        }else{
          this.nativeService.showToast(result.msg);
        }
      });
  }
  multClick(type){
    if(type==1){
      if(Utils.isEmpty(this.locCode)){
        this.nativeService.showToast('请先扫描要操作的库位！');
        return;
      }
    debugger
      if(!this.locFrozenFlag){
        this.requstFrozen('locFrozen',this.locCode,'1',null);
      }else{
        this.requstFrozen('locFrozen',this.locCode,'0',null);
      }
    }else{
      let temp = [];
      for (let index = 0; index < this.stockVOList.length; index++) {
        if(this.stockVOList[index].CheckStock){
            temp.push(this.stockVOList[index].stockId);
        }
      }
      if(Utils.isEmpty(temp)){
        this.nativeService.showToast('请先选择要操作的选项！');
        return;
      }
      let stockIds =  temp.join(',');
      if(!this.multFrozenFlag){
        this.requstFrozen('multFrozen',stockIds,'1',null);
      }else{
        this.requstFrozen('multFrozen',stockIds,'0',null);
      }
    }
  }
}
