import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { hiprint } from '../../assets/hiprint/hiprint.bundle.js';
import { Printer, PrintOptions } from '@ionic-native/printer';
import { Utils } from '../../services/Utils';
import { NativeService } from "../../services/NativeService"
import { Api, ContenType, Method } from '../../utils/appConstant';
import { AppService } from "../../services/App.service";

/**
 * Generated class for the PrintingPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-printing',
  templateUrl: 'printing.html',
})
export class PrintingPage {
  whName: string;//库房名称
  zoneName: string;//库区名称
  locCode: string;//库位名称
  constructor(public navCtrl: NavController,
    public navParams: NavParams,
    private printer: Printer,
    public appService: AppService,
    public nativeService: NativeService,
  ) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad PrintingPage');
  }
  btnOk() {
    if (Utils.isEmpty(this.whName)) {
      this.nativeService.showToast('库房名称不能为空');
      return;
    }
    if (Utils.isEmpty(this.zoneName)) {
      this.nativeService.showToast('库区名称不能为空');
      return;
    }
    if (Utils.isEmpty(this.locCode)) {
      this.nativeService.showToast('库位名称不能为空');
      return;
    }
    let params = {
      sptType: '20'
    }
    let printTemplateModel = {
      whName: this.whName,
      zoneName: this.zoneName,
      locCode: this.locCode
    }
    this.appService.httpRequest(Api.api + Api.printTemplateList, Method.Get, params, ContenType.Form, result => {
      let printTemplate = new hiprint.PrintTemplate({
        template: JSON.parse(result.data[0].sptData)
      })
      let styleModel = '<style printstyle="">.hiprint-printPaper.hiprint-printPaper-content{position:relative;}</style>↵';
      styleModel += printTemplate.getHtml(printTemplateModel)[0].outerHTML;
      this.printer.isAvailable().then(result => {
      }, result => {
      });
      let options: PrintOptions = {
        name: 'MyDocument',
        printerId: 'printer007',
        duplex: true,
        landscape: true,
        grayscale: true
      };
      this.printer.print(styleModel, options).then(result => {
      }, result => {
        console.log(result);
        this.nativeService.showToast('打印失败，请检查手持打印驱动是否安装');
      });
    });

  }
  exit() {
    this.navCtrl.pop();
  }
}
