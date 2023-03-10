webpackJsonp([25],{

/***/ 1366:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TakeawayinPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__ = __webpack_require__(360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__ = __webpack_require__(359);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};









/**
 * Generated class for the TakeawayinPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var TakeawayinPage = /** @class */ (function () {
    function TakeawayinPage(navCtrl, navParams, nativeService, appService, modalCtrl, storage, barcodeService, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.modalCtrl = modalCtrl;
        this.storage = storage;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.recList = 1;
        this.IsShow = 0; //????????????
        this.IsSku = 0; //????????????
        this.IsSkuList = 1; //??????????????????
        this.IsSkuSerial = 4; //?????????????????????
        this.IsSkuSerialInfo = 5; //???????????????????????????
        this.InNumberLength = 0; //?????????????????????
        this.ScanNumberLength = 0; //????????????????????????
        this.serialNumber = []; //????????????????????????
        this.InserialNumber = []; //??????????????????
        this.NumberListFalag = 1; //???????????? 1???????????? 2:?????????
        this.isTilebool = true;
        this.targetLocCode = 'PICK'; //????????????
        this.sysparms = []; //????????????
        this.pickPlansModel = this.navParams.get('pickPlansModel');
        this.PickInfoResult = this.navParams.get('PickInfoResult');
        this.pickPlansList = this.navParams.get('pickPlansList');
        this.wellenNo = this.navParams.get('wellenNo');
        this.pickPlansInModel();
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    TakeawayinPage.prototype.ionViewDidLoad = function () {
        this.HeadModel();
    };
    TakeawayinPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        if (this.navParams.get('flag') || false) {
            this.wellenNo = "";
        }
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.targetLpnCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.targetLocCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].SKU) {
                _this.ngZone.run(function () {
                    _this.skuCodeModel = barcode;
                    _this.getSkuInfo();
                });
            }
        });
    };
    TakeawayinPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    TakeawayinPage.prototype.clearModel = function () {
        this.targetLpnCode = '';
        this.skuCodeModel = '';
        this.lotNumber = '';
        this.pickQty = '';
        this.serialNumber = [];
        this.ScanNumberLength = 0;
    };
    /**
   * ???????????????
   */
    TakeawayinPage.prototype.HeadModel = function () {
        //??????
        this.count = this.pickPlansModel.count;
        this.finish = this.pickPlansModel.finish;
        //???????????????
        this.skuName = this.pickPlansModel.skuName;
        this.skuCode = this.pickPlansModel.skuCode;
        this.skuSpec = this.pickPlansModel.skuSpec;
        this.skuLot = this.pickPlansModel.skuLot;
        this.lotNumberTitle = this.pickPlansModel.lotNumber;
        this.planCountQty = this.pickPlansModel.planCountQty;
        this.realCountQty = this.pickPlansModel.realCountQty;
        this.planQtyName = this.pickPlansModel.planQtyName;
        this.realQtyName = this.pickPlansModel.realQtyName;
    };
    TakeawayinPage.prototype.pickPlansInModel = function () {
        this.HeadModel();
        //????????????????????????
        this.sourceLocCode = this.pickPlansModel.sourceLocCode;
        this.sourceLpnCode = this.pickPlansModel.sourceLpnCode;
        this.packageDetails = this.pickPlansModel.packageDetails;
        this.defaultpackageDetail = this.pickPlansModel.defaultPackageDetail;
        this.InserialNumber = this.pickPlansModel.serialList;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.pickPlansModel.serialList)) {
            this.InNumberLength = this.pickPlansModel.serialList.length;
        }
        //????????????????????????
        if (this.pickPlansModel.isSn == '1') {
            this.IsShow = this.IsSkuSerial;
        }
        else {
            this.IsShow = this.IsSku;
        }
    };
    /**
     * ??????????????????
     */
    TakeawayinPage.prototype.onClickItem = function () {
        this.IsShow = this.IsSkuList;
        this.pickPlansModelTemp = this.pickPlansModel;
        // this.clearModel();
    };
    /**
     * ????????????
     */
    TakeawayinPage.prototype.detailed = function () {
        var _this = this;
        var params = {
            wellenNo: this.wellenNo
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getSopickLog, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.pickPlans)) {
                _this.navCtrl.push('TakeawayindetailedPage', { pickPlans: result.data.pickPlans, finish: _this.finish, count: _this.count });
            }
            else {
                _this.nativeService.showToast('?????????????????????');
            }
        });
    };
    /**
     * ??????????????????
     */
    TakeawayinPage.prototype.headSelected = function (item) {
        this.pickPlansModelTemp = item;
    };
    TakeawayinPage.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.defaultpackageDetail.convertQty)) {
            this.skuSpec = '1-' + this.defaultpackageDetail.convertQty;
        }
        else {
            this.skuSpec = '';
        }
        this.planQtyPage();
        this.realQtyPage();
    };
    TakeawayinPage.prototype.planQtyPage = function () {
        var _this = this;
        var result = "";
        var mainWsuName = "";
        var planQtynum = parseInt(this.planCountQty);
        this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        this.packageDetails.forEach(function (element) {
            if (element.skuLevel == _this.defaultpackageDetail.skuLevel) {
                mainWsuName = element.wsuName;
            }
            if (element.skuLevel <= _this.defaultpackageDetail.skuLevel) {
                var tmp = Math.floor(planQtynum / element.convertQty);
                planQtynum = planQtynum - element.convertQty * tmp;
                if (tmp != 0) {
                    result += tmp + element.wsuName;
                }
            }
        });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.planQtyName = 0 + mainWsuName;
        }
        else {
            this.planQtyName = result;
        }
    };
    TakeawayinPage.prototype.realQtyPage = function () {
        var _this = this;
        var result = "";
        var mainWsuName = "";
        var planQtynum = parseInt(this.realCountQty);
        this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        this.packageDetails.forEach(function (element) {
            if (element.skuLevel == _this.defaultpackageDetail.skuLevel) {
                mainWsuName = element.wsuName;
            }
            if (element.skuLevel <= _this.defaultpackageDetail.skuLevel) {
                var tmp = Math.floor(planQtynum / element.convertQty);
                planQtynum = planQtynum - element.convertQty * tmp;
                if (tmp != 0) {
                    result += tmp + element.wsuName;
                }
            }
        });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.realQtyName = 0 + mainWsuName;
        }
        else {
            this.realQtyName = result;
        }
    };
    /**
    * ??????????????????
    */
    TakeawayinPage.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.skuCodeModel)) {
                this.nativeService.showToast('??????????????????????????????');
                return;
            }
            this.getSkuInfo();
        }
    };
    TakeawayinPage.prototype.getSkuInfo = function () {
        var _this = this;
        //????????????????????????
        var model = this.pickPlansList.filter(function (x) { return x.skuCode == _this.skuCodeModel && x.sourceLocCode == _this.sourceLocCode && x.sourceLpnCode == _this.sourceLpnCode; });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model)) {
            //this.nativeService.showToast('?????????????????????????????????');
            return;
        }
        this.pickPlansModel = model[0];
        this.pickPlansInModel();
    };
    /**
   *??????????????????
  */
    // openAsnRecordModal(skuList) {
    //   let myModal = this.modalCtrl.create('SkuListModal', {
    //     skuList: skuList,
    //   });
    //   myModal.onDidDismiss(data => {
    //     if (Utils.isNotEmpty(data)) {
    //       this.resultSkuItem = data;
    //       this.getSkuInfo();
    //     }
    //   });
    //   myModal.present();
    // }
    /**
     * ????????????
     */
    TakeawayinPage.prototype.btnOk = function () {
        if (this.IsShow == this.IsSkuList) {
            this.pickPlansModel = this.pickPlansModelTemp;
            this.pickPlansInModel();
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.submitPickInfo();
        }
        else {
            //????????????
            this.submitPickInfo();
        }
    };
    /**
     * ????????????
     */
    TakeawayinPage.prototype.submitPickInfo = function () {
        var _this = this;
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLpnCode)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
        }
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.lotNumber)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.defaultpackageDetail)) {
            this.nativeService.showToast('??????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.wellenNo)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        var params = {
            wellenNo: this.wellenNo,
            pickPlanId: this.pickPlansModel.pickPlanId,
            targetLpnCode: this.targetLpnCode,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode,
            skuCode: this.skuCode,
            lotNumber: this.lotNumber,
            pickQty: this.pickQty,
            targetLocCode: this.targetLocCode,
            wspdId: this.defaultpackageDetail.wspdId,
            isSn: this.pickPlansModel.isSn,
        };
        if (this.pickPlansModel.isSn == '1') {
            if (this.ScanNumberLength == 0) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            params['serialList'] = this.serialNumber;
            params['pickQty'] = this.serialNumber.length.toString();
        }
        else {
            if (this.skuCode != this.skuCodeModel) {
                this.nativeService.showToast('??????????????????????????????????????????????????????');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.pickQty)) {
                this.nativeService.showToast('??????????????????');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].IsPositiveInt(this.pickQty)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
        }
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitPickInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            //????????????
            _this.PickInfoResult = result.data; //?????????
            _this.pickPlansList = _this.PickInfoResult.pickPlans; //????????????
            var model = _this.pickPlansList.filter(function (x) { return x.skuCode == _this.skuCodeModel && x.sourceLocCode == _this.skuCodeModel && x.sourceLocCode == _this.sourceLpnCode; });
            // let model = this.pickPlansList.filter(x => x.skuCode == this.skuCode);//??????????????????
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(model)) {
                _this.pickPlansModel = model[0];
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansList)) {
                _this.pickPlansModel = _this.pickPlansList[0];
            }
            else {
                _this.navCtrl.pop();
            }
            _this.navCtrl.getPrevious().data.flag = true;
            _this.nativeService.showToast('????????????');
            _this.clearModel();
            _this.HeadModel();
        });
    };
    /**
     * ????????????
     */
    TakeawayinPage.prototype.exit = function () {
        if (this.IsShow == this.IsSkuList) {
            this.IsShow = this.IsSku;
        }
        else if (this.IsShow == this.IsSku) {
            this.navCtrl.pop();
        }
        else if (this.IsShow == this.IsSkuSerial) {
            this.lotNumber = '';
            this.targetLpnCode = '';
            this.IsShow = this.IsSku;
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.IsShow = this.IsSkuSerial;
        }
    };
    TakeawayinPage.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    TakeawayinPage.prototype.isTitle = function (event) {
        this.isTilebool = event;
        if (event) {
            this.NumberListFalag = 1;
        }
        else {
            this.NumberListFalag = 2;
        }
    };
    /**
    * ?????????????????????
    */
    TakeawayinPage.prototype.SkuSerial_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.serialNumberMode)) {
                var flag_1 = true;
                this.serialNumber.forEach(function (element) {
                    if (element.toUpperCase() == _this.serialNumberMode.toUpperCase()) {
                        flag_1 = false;
                    }
                });
                var body = {
                    skuId: this.pickPlansModel.skuId,
                    serialNumber: this.serialNumberMode
                };
                if (this.sysparms.paramValue != 0) {
                    body["lpnCode"] = this.sourceLpnCode;
                }
                else {
                    body["lpnCode"] = this.targetLpnCode;
                }
                this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].OutIsHasSerial, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, body, '', function (result) {
                    if (flag_1) {
                        _this.serialNumber.push(_this.serialNumberMode);
                        _this.serialNumberMode = '';
                    }
                    _this.ScanNumberLength = _this.serialNumber.length;
                });
            }
            ;
        }
    };
    TakeawayinPage.prototype.setNameLineClass = function () {
        return this.fontStypeSize(this.realQtyName);
    };
    TakeawayinPage.prototype.setNameLineScanClass = function () {
        return this.fontStypeSize(this.planQtyName);
    };
    TakeawayinPage.prototype.fontStypeSize = function (obj) {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(obj)) {
            return '16px';
        }
        if (obj.length <= 8) {
            return '16px';
        }
        else {
            return '12px;';
        }
    };
    /**
    * ???????????????
    */
    TakeawayinPage.prototype.numclick = function () {
        this.IsShow = this.IsSkuSerialInfo;
    };
    /**
    * ?????????????????????
    * @param item
    */
    TakeawayinPage.prototype.removeItem = function (item) {
        this.serialNumber = this.serialNumber.filter(function (x) { return x != item; });
        //????????????
        this.ScanNumberLength = this.serialNumber.length;
    };
    TakeawayinPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-takeawayin',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiece\takeawayin\takeawayin.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ????????????\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="order">\n\n      <div class="order-number">{{wellenNo}}</div>\n\n      <div class="order-info">?????????????????? {{finish}}/{{count}}</div>\n\n    </div>\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n\n\n<ion-content class="no-scroll" overflow-scroll="true" style="overflow: hidden">\n\n  <!-- ??????????????? -->\n\n  <div class="title-ms clearfix" *ngIf="IsShow===IsSku||IsShow===IsSkuSerial||IsShow===IsSkuSerialInfo"\n\n    (tap)="onClickItem()">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div>\n\n          <label>{{skuCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label>{{lotNumberTitle}}</label>\n\n        </div>\n\n        <div class="specs_code">\n\n          ???????????????<span style="color: black;">{{skuSpec}}</span>\n\n        </div>\n\n        <div class="ionic_right">\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </div>\n\n      </div>\n\n    </div>\n\n    <div *ngIf="realQtyName!=\'\'&& realQtyName!=null " [ngStyle]="{\'font-size\':setNameLineClass()}"\n\n      class="received-scan">\n\n      ?????????: <span style="color: #F59A23;"> {{realQtyName ==\'\'?\'0\':realQtyName}}</span>\n\n    </div>\n\n    <div *ngIf="planQtyName!=\'\'&& planQtyName!=null " [ngStyle]="{\'font-size\':setNameLineScanClass()}"\n\n      class="received-plan">\n\n      ?????????: <span style="color: #F59A23;"> {{planQtyName ==\'\'?\'0\':planQtyName}}</span>\n\n    </div>\n\n  </div>\n\n\n\n  <!-- ??????????????? -->\n\n  <div class="list_item" *ngIf="IsShow===IsSku">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input maxlength=\'30\' type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(true)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input [disabled]=\'true\' [(ngModel)]="sourceLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input maxlength=\'30\' [disabled]=\'true\' [(ngModel)]="sourceLpnCode" type="text"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(false)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>??? ??????</ion-label>\n\n      <ion-input (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCodeModel" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input [(ngModel)]="lotNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>??? ??????</ion-label>\n\n      <ion-input [(ngModel)]="pickQty" type="number"></ion-input>\n\n      <ion-select (ionChange)="togglePage()" [(ngModel)]="defaultpackageDetail" [compareWith]="compareFn"\n\n        interface="action-sheet" cancelText="??????" okText="??????">\n\n        <div *ngFor="let skuPackage of packageDetails">\n\n          <ion-option [value]="skuPackage">{{skuPackage.wsuName}}</ion-option>\n\n        </div>\n\n      </ion-select>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n  <!-- ????????? -->\n\n  <div class="list_item" *ngIf="IsShow==IsSkuSerial">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input maxlength=\'30\' type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(true)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input [disabled]=\'true\' [(ngModel)]="sourceLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input maxlength=\'30\' [disabled]=\'true\' [(ngModel)]="sourceLpnCode" type="text"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(false)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="text" maxlength=\'100\' (keyup)="SkuSerial_KeyDown($event)" [(ngModel)]="serialNumberMode">\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input [(ngModel)]="lotNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item (tap)="numclick()">\n\n      <ion-label>?????????</ion-label>\n\n      <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength + " /" +InNumberLength}}\'>\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n  <!-- ??????????????? -->\n\n  <div class="list_item_serial" *ngIf="IsShow===IsSkuSerialInfo">\n\n    <div class="list_item_title">\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n        (tap)="isTitle(true)">???????????????(<b>{{ScanNumberLength}}</b>)</span>\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n        (tap)="isTitle(false)">???????????????(<b>{{InNumberLength}}</b>)</span>\n\n    </div>\n\n\n\n  </div>\n\n\n\n  <!-- ?????????????????? -->\n\n  <div *ngIf="NumberListFalag==1 && IsShow===IsSkuSerialInfo ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of serialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n          <ion-item-options side="right">\n\n            <button (tap)="removeItem(item)" ion-button color="danger">\n\n              ??????\n\n            </button>\n\n          </ion-item-options>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n  <!-- ??????????????? -->\n\n  <div *ngIf="NumberListFalag==2 && IsShow===IsSkuSerialInfo ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of InserialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n\n\n  <!-- ???????????? -->\n\n  <div class="title-ms-list" *ngIf="IsShow===IsSkuList">\n\n    <div class="title-ms-info-list" [ngClass]="{selected:pickPlansModelTemp.pickPlanId == item.pickPlanId}"\n\n      (tap)="headSelected(item)" *ngFor="let item of pickPlansList">\n\n      <div class="title-font-s1">\n\n        <label>{{item.skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div>\n\n          <label>{{item.skuCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label>?????????:{{item.sourceLocCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label style="color: black;" *ngIf="item.skuLot.skuLot1!=\'\'; else elseBlock">{{item.lotNumber}}\n\n            &nbsp;</label>\n\n          <ng-template #elseBlock> <label style="color: black;">???????????????</label></ng-template>\n\n        </div>\n\n        <div class="list_specs_code">\n\n          ???????????????<span style="color: black;">{{item.skuSpec}}</span>\n\n        </div>\n\n        <!-- <div class="specs-code-lpn">\n\n          ????????????<span style="color: black;">{{item.sourceLpnCode}}</span>\n\n        </div> -->\n\n        <div class="list_received">\n\n          ?????????: <span style="color: #F59A23;">{{item.realCountQty}}</span>/{{item.planCountQty}}\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiece\takeawayin\takeawayin.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], TakeawayinPage);
    return TakeawayinPage;
}());

//# sourceMappingURL=takeawayin.js.map

/***/ }),

/***/ 771:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TakeawayinPageModule", function() { return TakeawayinPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__takeawayin__ = __webpack_require__(1366);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var TakeawayinPageModule = /** @class */ (function () {
    function TakeawayinPageModule() {
    }
    TakeawayinPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__takeawayin__["a" /* TakeawayinPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__takeawayin__["a" /* TakeawayinPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], TakeawayinPageModule);
    return TakeawayinPageModule;
}());

//# sourceMappingURL=takeawayin.module.js.map

/***/ }),

/***/ 781:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CrumbsModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__crumbs__ = __webpack_require__(782);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var CrumbsModule = /** @class */ (function () {
    function CrumbsModule() {
    }
    CrumbsModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__crumbs__["a" /* CrumbsComponent */]]
        })
    ], CrumbsModule);
    return CrumbsModule;
}());

//# sourceMappingURL=crumbs.module.js.map

/***/ }),

/***/ 782:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CrumbsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CrumbsComponent = /** @class */ (function () {
    function CrumbsComponent(navCtrl) {
        this.navCtrl = navCtrl;
        this.dicts = [
            { id: "HomePage", name: "??????" },
            { id: "TaskviewPage", name: "????????????" },
            { id: "CheckinhomePage", name: "??????" },
            { id: "CheckinpiecePage", name: "????????????" },
            { id: "CheckInPage", name: "????????????" },
            { id: "CheckinboxPage", name: "????????????" },
            { id: "SearchOrderPage", name: "????????????" },
            { id: "CheckinBoxInfoPage", name: "????????????" },
            { id: "CheckintrayPage", name: "????????????" },
            { id: "CheckinDetailedPage", name: "????????????" },
            { id: "CollageTaskPage", name: "????????????" },
            { id: "CollageTaskListPage", name: "????????????" },
            { id: "CollageTaskBoxPage", name: "????????????" },
            { id: "CyclecounthomePage", name: "??????" },
            { id: "CyclecountrandominfoPage", name: "????????????" },
            { id: "CyclecountrantaskPage", name: "????????????" },
            { id: "CyclecountBillPage", name: "????????????" },
            { id: "CyclecoutrantaskinfoPage", name: "????????????" },
            { id: "MovehomePage", name: "??????" },
            { id: "MovesboxPage", name: "????????????" },
            { id: "MovesboxInfoPage", name: "????????????" },
            { id: "moveSku", name: "????????????" },
            { id: "moveStock", name: "????????????" },
            { id: "moveStocks", name: "????????????" },
            { id: "PackageTaskPage", name: "????????????" },
            { id: "PackageTaskListPage", name: "????????????" },
            { id: "PackageTaskInfoPage", name: "????????????" },
            { id: "PutawayhomePage", name: "??????" },
            { id: "PutawayPage", name: "????????????" },
            { id: "PutawayboxPage", name: "????????????" },
            { id: "ReplenishmenhomePage", name: "????????????" },
            { id: "ReplenishmenDetilePage", name: "????????????" },
            { id: "SettingPage", name: "????????????" },
            { id: "SettingPwdPage", name: "????????????" },
            { id: "SettingThemePage", name: "????????????" },
            { id: "ShipmenthomePage", name: "??????" },
            { id: "ShipmentLpnPage", name: "????????????" },
            { id: "StockhomePage", name: "??????" },
            { id: "StockdetailPage", name: "????????????" },
            { id: "StockqueryPage", name: "????????????" },
            { id: "StockPritPage", name: "????????????" },
            { id: "TakeawayhomePage", name: "??????" },
            { id: "TakeawaypiecePage", name: "????????????" },
            { id: "TakeawayPage", name: "????????????" },
            { id: "TakeawayinPage", name: "????????????" },
            { id: "TakeawayindetailedPage", name: "????????????" },
            { id: "TakeawaypieceboxPage", name: "????????????" },
            { id: "TakeawayinboxPage", name: "????????????" },
            { id: "CougnyPage", name: "??????" },
            { id: "CopyLabelPage", name: "????????????" },
            { id: "RandomCheckPage", name: "????????????" },
            { id: "RandomCheckTaskPage", name: "????????????" },
            { id: "ReplenishmenPage", name: "????????????" },
            { id: "ReplenishmenRecordPage", name: "????????????" },
            { id: "StockFrozenPage", name: "????????????" },
            { id: "CheckOrderPage", name: "????????????" },
            { id: "UpdateDetailPage", name: "????????????" },
            { id: "TaskwayMultPage", name: "????????????" },
            { id: "BindPrintterPage", name: "???????????????" },
            { id: "BindOrderPage", name: "????????????" },
            { id: "BindLpnPage", name: "????????????" },
            { id: "CyclecountRecordPage", name: "????????????" },
            { id: "MergeTrayPage", name: "??????" }
        ];
        this.alls = [];
    }
    CrumbsComponent.prototype.ngOnInit = function () {
        var _this = this;
        if (this.navCtrl.getViews().length > 0) {
            this.navCtrl.getViews().forEach(function (a) {
                _this.alls.push(_this.distsValue(a));
                _this.alls.push({ id: '_', name: '_' });
            });
        }
        if (this.alls.length > 0 && this.alls[this.alls.length - 1].id == '_') {
            this.alls.pop();
        }
        console.log(this.alls);
    };
    CrumbsComponent.prototype.toPush = function (item) {
        var active = this.navCtrl.getActive();
        if (active) {
            if (active.id != item.id) {
                var a = this.navCtrl.getByIndex(item.index);
                this.navCtrl.popTo(a);
            }
        }
    };
    CrumbsComponent.prototype.distsValue = function (vc) {
        var value;
        this.dicts.forEach(function (a) {
            if (a.id == vc.name) {
                a["index"] = vc.index;
                value = a;
            }
        });
        return value;
    };
    CrumbsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'crumbs',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\crumbs\crumbs.html"*/'<ul >\n\n   <ng-container *ngFor="let item of alls">\n\n      <li *ngIf="item&&item.id!=\'_\';else elseBlock" (click)="toPush(item)"> \n\n         {{item.name}} \n\n      </li>\n\n      <ng-template #elseBlock>\n\n      <li>\n\n         <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      </ng-template>\n\n   </ng-container>\n\n</ul>\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\crumbs\crumbs.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */]])
    ], CrumbsComponent);
    return CrumbsComponent;
}());

//# sourceMappingURL=crumbs.js.map

/***/ })

});
//# sourceMappingURL=25.js.map