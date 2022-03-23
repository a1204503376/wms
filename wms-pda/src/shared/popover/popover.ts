import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { ViewController } from 'ionic-angular';
/**
 * Generated class for the PopoverPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-popover',
  templateUrl: 'popover.html',
})
export class PopoverPage {

  public isShow:boolean = true;
  public isShow1:boolean = true;
  public isShow11:boolean = true;
  public isShow12:boolean = true;
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    public viewCtrl: ViewController) {
      if(navParams.get('show_item_3')!=undefined){
        this.isShow = navParams.get('show_item_3');
      }
      if(navParams.get('show_item_4')!=undefined){
        this.isShow1 = navParams.get('show_item_4');
      }
      if(navParams.get('show_item_5')!=undefined){
        this.isShow11 = navParams.get('show_item_5');
      }
      if(navParams.get('show_item_6')!=undefined){
        this.isShow12 = navParams.get('show_item_6');
      }
    }

  ionViewDidLoad() {
    console.log('ionViewDidLoad PopoverPage');
  }
  onPick(item) {
    this.viewCtrl.dismiss(item);
  }
}
