import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

/**
 * Generated class for the CollageTaskListPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-collage-task-list',
  templateUrl: 'collage-task-list.html',
})
export class CollageTaskListPage {
  resultList: any;//打包任务详情

  taskId: string;//任务ID

  isShow: boolean = true;

  resultListlpnItem: any;//选中的托盘

  isShowFalg: boolean;//判断上一级传过来的状态
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.resultList = this.navParams.get('result');
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad CollageTaskListPage');
  }
  /**
   * 选择托盘
   */
  lpnListOnClick(item) {
    if (this.isShow) {
      this.resultListlpnItem = item;
      console.log(this.resultListlpnItem.detailList.length);
      this.isShow = false;
    }
  }
  exit() {
    if (this.isShow) {
      this.navCtrl.pop();
    } else {
      this.isShow = true;
    }
  }
}
