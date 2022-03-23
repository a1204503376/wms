import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { Utils } from '../../../../services/Utils';
/**
 * Generated class for the TakeawayindetailedPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-takeawayindetailed',
  templateUrl: 'takeawayindetailed.html',
})
export class TakeawayindetailedPage {
  trailerTaskList: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];
  public pickPlans: any;
  public finish: any;
  public count: any;
  public wellenNo: any;
  public orderNo: any;
  public sobillNo: any;
  public groupRecords:any=[];
  public skuLotNum:any;
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.skuLotNum = Array(30).fill(0).map((x,i)=>i);
    this.pickPlans = this.navParams.get('pickPlans');
    this.finish = this.navParams.get('finish');
    this.count = this.navParams.get('count');
    this.wellenNo = this.navParams.get('wellenNo'); 
    this.orderNo = this.navParams.get('orderNo'); 
    this.sobillNo = this.navParams.get('sobillNo'); 
    if(Utils.isNotEmpty(this.pickPlans)){
      for(let item of this.pickPlans){
        if(Utils.isEmpty(this.groupRecords)){
          this.groupRecords.push(item);
        }else{
          let arr=this.groupRecords.find((v) => {
            return v.skuCode == item.skuCode;
          });
          if(Utils.isEmpty(arr)){
            this.groupRecords.push(item);
            break;
          }
        }
      }
    }
  }
  range(item:any) { 
    var arr = [];
    for (var i = 1; i <= 30; i++) {
      if(item[`skuLot${i}`]&&item[`skuLot${i}`]!=''){
        arr.push(item[`skuLot${i}`]);
      }
    } 
    return arr;
  }
  ionViewDidLoad() {
    console.log('ionViewDidLoad TakeawayindetailedPage');
  }
  exit(){
    this.navCtrl.pop();
  }
}
