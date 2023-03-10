webpackJsonp([54],{

/***/ 1370:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MergeTrayPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__ = __webpack_require__(360);
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
 * Generated class for the CollageTaskPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var MergeTrayPage = /** @class */ (function () {
    function MergeTrayPage(navCtrl, navParams, nativeService, appService, barcodeService, modalCtrl, ngZone, events, popoverCtrl, alertControl) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.barcodeService = barcodeService;
        this.modalCtrl = modalCtrl;
        this.ngZone = ngZone;
        this.events = events;
        this.popoverCtrl = popoverCtrl;
        this.alertControl = alertControl;
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__["a" /* ScanModel */](); //????????????
        this.scanModelList = []; //????????????????????????
        this.isShow = true;
        this.isNext = true;
        this.currentTotal = 0;
        this.isShowCurrentTotal = true;
    }
    MergeTrayPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage', { show_item_3: true, show_item_4: false, show_item_5: true });
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.scanModelList)) {
                console.log(_this.scanModelList);
                if (data == 'allCheck') {
                    //??????
                    for (var index = 0; index < _this.scanModelList.length; index++) {
                        _this.scanModelList[index]['isCheck'] = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //????????????
                    for (var index = 0; index < _this.scanModelList.length; index++) {
                        _this.scanModelList[index]['isCheck'] = false;
                    }
                }
                else if (data == 'show') {
                    for (var _i = 0, _a = _this.scanModelList; _i < _a.length; _i++) {
                        var item = _a[_i];
                        item['show'] = true;
                    }
                }
                else if (data == 'hide') {
                    for (var _b = 0, _c = _this.scanModelList; _b < _c.length; _b++) {
                        var item = _c[_b];
                        if (item.skucode != _this.scanModel.skucode) {
                            item['show'] = false;
                        }
                        else {
                            item['show'] = true;
                        }
                    }
                }
                else {
                    //????????????
                    _this.skuStockIsCheckDel();
                }
            }
        });
        popover.present({
            ev: myEvent
        });
    };
    MergeTrayPage.prototype.skuStockIsCheckDel = function () {
        this.scanModelList = this.scanModelList.filter(function (x) { return x['isCheck'] === false; });
        this.execTotal();
    };
    MergeTrayPage.prototype.removeItem = function (item) {
        this.scanModelList = this.scanModelList.filter(function (x) { return x != item; });
        this.execTotal();
    };
    MergeTrayPage.prototype.changeQty = function () {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.scanModel.qty)) {
            this.scanModel.qty = "0";
        }
        this.execTotal();
    };
    MergeTrayPage.prototype.clearRecordsSelected = function () {
        for (var _i = 0, _a = this.scanModelList; _i < _a.length; _i++) {
            var item1 = _a[_i];
            item1['selected'] = false;
            if (item1.skucode != this.scanModel.skucode) {
                item1['show'] = false;
            }
            else {
                item1['show'] = true;
            }
        }
    };
    MergeTrayPage.prototype.execTotal = function () {
        this.currentTotal = 0;
        for (var _i = 0, _a = this.scanModelList; _i < _a.length; _i++) {
            var item1 = _a[_i];
            if (item1.skucode == this.scanModel.skucode) {
                this.currentTotal += Number.parseInt(item1.qty);
            }
        }
    };
    MergeTrayPage.prototype.itemClick = function (item) {
        this.clearRecordsSelected();
        item['selected'] = true;
        this.scanModel = item;
    };
    MergeTrayPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
        if (__WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].scanFlag) {
            __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
            this.events.subscribe('barcode:scan', function (barcode, time) {
                if (!_this.isShow) {
                    _this.ngZone.run(function () {
                        _this.lpnCode = barcode;
                    });
                }
                else {
                    _this.getStockInfoPutawayForBox(barcode);
                }
            });
        }
    };
    MergeTrayPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
     * ?????????????????????
     *
     */
    MergeTrayPage.prototype.scanModelChange = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.scanView)) {
                this.nativeService.showToast('????????????????????????');
                return;
            }
            this.getStockInfoPutawayForBox(this.scanView);
        }
    };
    /**
    * ????????????
    */
    MergeTrayPage.prototype.getStockInfoPutawayForBox = function (scanView) {
        var _this = this;
        this.barcodeService.ScanSkuBarcode(scanView).subscribe(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                _this.scanModel = val;
                //????????????
                _this.scanWmsSku(val);
            }
            else {
                __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
            }
        });
    };
    /*????????????*/
    MergeTrayPage.prototype.scanWmsSku = function (val) {
        var _this = this;
        var temp = this.scanModelList.filter(function (x) { return __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isObjectValueEqual1(x, val, ['address', 'lpnCode', 'soBillId', 'show', 'skuId', 'skuName', 'cCode', 'cName', 'skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue']); });
        var arr = temp.map(function (x) { return parseInt(x.qty); });
        var qty = 0;
        if (arr.length > 0) {
            qty = arr.reduce(function (acc, val) { return acc + val; });
        }
        var params = {
            skuCode: val.skucode
        };
        if (qty > 0) {
            params['qty'] = qty + parseInt(val.qty);
            params['ccode'] = temp[0]['cCode'];
        }
        else {
            params['qty'] = parseInt(val.qty);
        }
        for (var item in val.skuLotModel) {
            if (item.includes('skulot') && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val.skuLotModel[item])) {
                var index = item.substring('skulot'.length, item.length);
                params["skuLot" + index] = val.skuLotModel[item];
            }
        }
        //????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].querySku, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            console.log(result);
            __WEBPACK_IMPORTED_MODULE_8__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
            //????????????????????????
            if (result.data.length > 1) {
                //????????????
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(_this.scanModelList)) {
                    // for (var i = 0; i < result.data.length; i++) {
                    //   // ??????????????????2?????????
                    //   if (result.data[i]['address'].indexOf(',') != -1) {
                    //     let aa = result.data[i]['address'].split(',');
                    //     let arr = [];
                    //     for (let a of aa) {
                    //       let obj = {};
                    //       Object.assign(obj, result.data[i]);
                    //       obj['address'] = a;
                    //       arr.push(obj);
                    //     }
                    //     result.data.splice(i, 1);
                    //     result.data.push.apply(result.data, arr);
                    //   }
                    // }
                    _this.openAsnRecordModal(result.data, val);
                }
                else {
                    var temp = result.data.filter(function (x) { return x.ccode == _this.scanModelList[0]['cCode']; });
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(temp)) {
                        // if (temp[0]['address'].indexOf(',') != -1) {
                        if (temp.length > 1) {
                            // let aa = temp[0]['address'].split(',');
                            // let arr = [];
                            // for (let a of aa) {
                            //   let obj = {};
                            //   Object.assign(obj, temp[0]);
                            //   obj['address'] = a;
                            //   arr.push(obj);
                            // }
                            _this.openAsnRecordModal(temp, val);
                        }
                        else {
                            _this.resultSkuItem = temp[0];
                            _this.fillData(temp[0], val);
                        }
                    }
                    else {
                        _this.openAsnRecordModal(result.data, val);
                    }
                }
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('??????????????????????????????????????????????????????????????????????????????????????????');
            }
            else {
                _this.resultSkuItem = result.data[0];
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(_this.scanModelList)) {
                    _this.fillData(result.data[0], val);
                }
                else {
                    if (_this.scanModelList[0]['cCode'] == _this.resultSkuItem.ccode) {
                        _this.fillData(result.data[0], val);
                    }
                    else {
                        _this.nativeService.showToast('?????????????????????????????????');
                    }
                }
            }
        });
    };
    MergeTrayPage.prototype.fillData = function (item1, val) {
        console.log(item1);
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.scanModelList)) {
            if (this.scanModelList[0]['address'] != item1.address) {
                this.nativeService.showToast('??????????????????????????????');
                return;
            }
        }
        var skulots = [];
        for (var prop in item1) {
            if (prop.search('skuLot') != -1 && item1[prop] != '') {
                skulots.push(item1[prop]);
            }
        }
        this.scanModel['skuId'] = item1.skuId;
        this.scanModel['soBillId'] = item1.soBillId;
        this.scanModel['skuName'] = item1.skuName;
        this.scanModel['cCode'] = item1.ccode;
        this.scanModel['cName'] = item1.cname;
        this.scanModel['address'] = item1.address;
        this.scanModel['lpnCode'] = item1.lpnCode;
        this.scanModel['skulots'] = skulots;
        this.scanModel['qty'] = item1.qty;
        this.scanModel['isCheck'] = false;
        this.scanModel['selected'] = true;
        this.scanModel['show'] = true;
        // if (Utils.isEmpty(this.scanModelList)) {
        this.scanModelList.push(this.scanModel);
        // } else {
        //   this.clearRecordsSelected();
        //
        //   for (let item of this.scanModelList) {
        //     // if (Utils.isObjectValueEqual1(this.scanModel, item,
        //     //   ['address', 'lpnCode', 'soBillId', 'show', 'skuId', 'skuName', 'cCode', 'cName', 'skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue'])) {
        //     //   item.qty = (Number.parseInt(item.qty) + Number.parseInt(this.scanModel.qty)) + '';
        //     //   item['selected'] = true;
        //     //   this.scanModel = item;
        //     // } else {
        //       if (!Utils.contains(this.scanModelList, this.scanModel,
        //         ['address', 'lpnCode', 'soBillId', 'show', 'skuId', 'skuName', 'cCode', 'cName', 'skuname', 'skulots', 'selected', 'isCheck', 'qty', 'setLotModel', 'setLotValue'])) {
        //         this.scanModelList.push(this.scanModel);
        //         break;
        //       }
        //     // }
        //   }
        // }
        this.execTotal();
    };
    // scrollStypeTrue() {
    //   this.scrollHeight = Number(document.body.clientHeight) - 323;
    // }
    // scrollStypeFalse() {
    //   this.scrollHeight = Number(document.body.clientHeight) - 173;
    // }
    // ionViewDidLoad() {
    //   this.scrollStypeTrue();
    // }
    /**
     * ????????????
     */
    MergeTrayPage.prototype.onIsShowClick = function () {
        this.isShow = !this.isShow;
        // if (this.isShow) {
        //   this.scrollStypeTrue();
        // } else {
        //   this.scrollStypeFalse();
        // }
    };
    /**
    *??????????????????
    */
    MergeTrayPage.prototype.openAsnRecordModal = function (mergeTrays, val) {
        var _this = this;
        var myModal = this.modalCtrl.create('MergeTraySelectModal', {
            records: mergeTrays,
        });
        myModal.onDidDismiss(function (data) {
            console.log(data);
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data)) {
                _this.resultSkuItem = data;
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(_this.scanModelList)) {
                    _this.fillData(data, val);
                }
                else {
                    if (_this.scanModelList[0]['cCode'] == _this.resultSkuItem.ccode) {
                        _this.fillData(data, val);
                    }
                    else {
                        _this.nativeService.showToast('?????????????????????????????????');
                    }
                }
            }
        });
        myModal.present();
    };
    /**
     * ????????????????????????
     */
    MergeTrayPage.prototype.submitOutStockLpnInfo = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
            this.nativeService.showToast('??????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.length)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.width)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.height)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.weight)) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        var items = [];
        for (var _i = 0, _a = this.scanModelList; _i < _a.length; _i++) {
            var item = _a[_i];
            var itemTemp = {
                qty: item.qty,
                skuCode: item.skucode,
                lpnCode: item['lpnCode'],
                skuId: item['skuId'],
                soBillId: item['soBillId']
            };
            debugger;
            for (var prop in item.skuLotModel) {
                if (prop.search('skulot') != -1) {
                    var index = prop.substring(prop.indexOf('t') + 1, prop.length);
                    var a = parseInt(index) - 1;
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(item['skulots'][a])) {
                        itemTemp["skuLot" + index] = item['skulots'][a];
                    }
                }
            }
            items.push(itemTemp);
        }
        var params = {
            lpnCode: this.lpnCode,
            length: this.length,
            width: this.width,
            height: this.height,
            weight: this.weight,
            mergeTraySkuVos: items,
            remaker: this.remaker
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].submitOutStockLpnInfo, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            if (result.success) {
                _this.navCtrl.pop();
            }
        });
    };
    /**
     * ??????????????????
     */
    MergeTrayPage.prototype.getOutstockLpnInfo = function () {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getOutstockLpnInfo, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, { taskId: this.taskId }, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            _this.nativeService.showToast('????????????');
            _this.clearModel();
            if (result.complated) {
                _this.navCtrl.pop();
            }
            // if (Utils.isNotEmpty(result.data)) {
            //   this.resultList = result.data;
            //   if (this.resultList.finish == this.resultList.total) {
            //     this.taskPDAClose();
            //   }
            // }
        });
    };
    /**
   * ??????????????????
   */
    MergeTrayPage.prototype.taskPDAClose = function () {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].taskPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].taskPDAClose, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, { ids: this.taskId }, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            _this.navCtrl.pop();
        });
    };
    /**
     * ????????????????????????
     */
    MergeTrayPage.prototype.clearModel = function () {
        this.scanModel = null;
        this.scanModelList = [];
        this.length = '';
        this.lpnCode = '';
        this.width = '';
        this.height = '';
        this.weight = '';
        this.isShow = true;
    };
    /**
     * ??????????????????
     */
    MergeTrayPage.prototype.handItemClick = function () {
        var _this = this;
        if (this.isShow) {
            if (this.resultList.lpnList.length > 0) {
                this.navCtrl.push('CollageTaskListPage', { result: this.resultList });
            }
        }
        else {
            if (this.scanModelList.length > 0) {
                var myModal = this.modalCtrl.create('CollageTaskBoxPage', {
                    scanModelList: this.scanModelList,
                    result: this.resultList
                });
                myModal.onDidDismiss(function (data) {
                    if (data != undefined) {
                        _this.scanModelList = data;
                    }
                });
                myModal.present();
            }
        }
    };
    /**
     * ????????????
     */
    MergeTrayPage.prototype.btnOk = function () {
        if (this.isNext) {
            if (this.scanModelList.length <= 0) {
                this.nativeService.showToast('??????????????????,?????????');
                return;
            }
            this.isNext = false;
            // this.getLpnCode();
        }
        else {
            //????????????
            this.submitOutStockLpnInfo();
        }
    };
    MergeTrayPage.prototype.getLpnCode = function () {
        var _this = this;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getLpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, { billNo: this.resultList.soBillNo }, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            if (result.success) {
                _this.lpnCode = result.data;
            }
        });
    };
    /**
     * ????????????
     */
    MergeTrayPage.prototype.exit = function () {
        if (this.isNext) {
            this.navCtrl.pop();
        }
        else {
            this.isNext = true;
        }
    };
    MergeTrayPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-merge-tray',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cougny\merge-tray\merge-tray.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ??????\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <div class="nav-tile">\n\n    <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n<ion-content class="no-scroll">\n\n  <!-- ???????????? -->\n\n  <div *ngIf="isNext" class="flex_content">\n\n    <div class="head_lable" *ngIf="isShow">\n\n      <ion-item>\n\n        <ion-label>???&emsp;??????</ion-label>\n\n        <ion-input disabled (blur)="scrollToTop()" type="text" [(ngModel)]="scanModel.skucode"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>???&emsp;??????</ion-label>\n\n        <ion-input disabled (blur)="scrollToTop()" type="number" [(ngModel)]="scanModel.qty" (ionChange)="changeQty()">\n\n        </ion-input>\n\n        <ion-select [disabled]=\'true\' [(ngModel)]="scanModel.um" cancelText="??????">\n\n          <ion-option [value]="scanModel.um">{{scanModel.um}}</ion-option>\n\n        </ion-select>\n\n      </ion-item>\n\n      <ion-item>\n\n        <ion-label>???&emsp;??????</ion-label>\n\n        <ion-input (keyup)="scanModelChange($event)" [(ngModel)]="scanView" type="text">\n\n        </ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </ion-item>\n\n\n\n    </div>\n\n    <!-- ??????????????? -->\n\n    <div class="list_item_serial">\n\n      <div class="list_item_title">\n\n        <span *ngIf="isShow; else elseBlock" (click)="onIsShowClick()">\n\n          <ion-icon name="md-arrow-dropup"></ion-icon>\n\n        </span>\n\n\n\n        <ng-template #elseBlock>\n\n          <span (click)="onIsShowClick()">\n\n            <ion-icon name="md-arrow-dropdown"></ion-icon>\n\n          </span>\n\n\n\n        </ng-template>\n\n        <div>\n\n          <span [ngStyle]="{\'border-bottom\': \'3px solid #008080\' }">\n\n            ????????????(<b>{{scanModelList.length}}</b>)\n\n          </span>\n\n          <span *ngIf="isShowCurrentTotal">\n\n            &nbsp;&nbsp;&nbsp;&nbsp;??????:<span style="color: orange;">{{currentTotal}}</span>\n\n          </span>\n\n        </div>\n\n        <span>\n\n          <button ion-button icon-only (click)="presentPopover($event)">\n\n            <ion-icon name="md-more"></ion-icon>\n\n          </button>\n\n\n\n        </span>\n\n      </div>\n\n    </div>\n\n\n\n    <ion-list class="list_item_div">\n\n\n\n      <ion-scroll scrollY="true" [ngStyle]="{\'height\':scrollHeight+\'px\'}">\n\n        <!--????????????-->\n\n        <ion-item-sliding *ngFor="let item of scanModelList;let i = index">\n\n          <ion-item button (click)="itemClick(item)" [ngClass]="{selected:item.selected}" *ngIf="item.show">\n\n            <ion-label>\n\n              <div class="nodes-item">\n\n                <div class="left">\n\n                  <div class="chk_wrapper">\n\n                    <input type="checkbox" id="{{\'checkbox_\'+i}}" class="chk_1" [(ngModel)]="item.isCheck">\n\n                    <label for="{{\'checkbox_\'+i}}"></label>\n\n                  </div>\n\n                  <div class="left-2">\n\n                    <div>\n\n                      <span>{{item.skucode}}</span>\n\n                      <span>{{item.skuname}}</span>\n\n                      <span>{{item.cName}}</span>\n\n                      <span>{{item.address}}</span>\n\n                    </div>\n\n                    <div *ngFor="let item1 of item.skulots">\n\n                      <span *ngIf="item1!=\'\'">{{item1}}</span>\n\n                    </div>\n\n                  </div>\n\n                </div>\n\n                <div class="right">\n\n                  <div>\n\n                    <span>?????????</span>\n\n                    <span>{{item.qty + item.um}}\n\n                    </span>\n\n                  </div>\n\n                </div>\n\n              </div>\n\n            </ion-label>\n\n          </ion-item>\n\n          <ion-item-options side="right">\n\n            <!-- <button (tap)="updateItem(item)" ion-button color="success">\n\n              ??????\n\n            </button> -->\n\n            <button (tap)="removeItem(item)" ion-button color="danger">\n\n              ??????\n\n            </button>\n\n          </ion-item-options>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n  <!-- ?????????????????? -->\n\n  <div *ngIf="!isNext" class="merge_tray_set">\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="text" [(ngModel)]="lpnCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="length"></ion-input>\n\n      <ion-avatar item-end>\n\n        ???\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="width"></ion-input>\n\n      <ion-avatar item-end>\n\n        ???\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="height"></ion-input>\n\n      <ion-avatar item-end>\n\n        ???\n\n      </ion-avatar>\n\n    </ion-item>\n\n\n\n    <ion-item>\n\n      <ion-label>?????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="weight"></ion-input>\n\n      <ion-avatar item-end>\n\n        ??????\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>?????????</ion-label>\n\n      <ion-textarea placeholder="???????????????" [(ngModel)]="remaker"></ion-textarea>\n\n    </ion-item>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      <span *ngIf="!isNext">??????[Ent]</span><span *ngIf="isNext">?????????[Ent]</span>\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\cougny\merge-tray\merge-tray.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_5__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */]])
    ], MergeTrayPage);
    return MergeTrayPage;
}());

//# sourceMappingURL=merge-tray.js.map

/***/ }),

/***/ 775:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MergeTrayPageModule", function() { return MergeTrayPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__merge_tray__ = __webpack_require__(1370);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var MergeTrayPageModule = /** @class */ (function () {
    function MergeTrayPageModule() {
    }
    MergeTrayPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__merge_tray__["a" /* MergeTrayPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__merge_tray__["a" /* MergeTrayPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], MergeTrayPageModule);
    return MergeTrayPageModule;
}());

//# sourceMappingURL=merge-tray.module.js.map

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
//# sourceMappingURL=54.js.map