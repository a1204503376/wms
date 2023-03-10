webpackJsonp([44],{

/***/ 1360:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MovesboxPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__ = __webpack_require__(160);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__services_AppGlobal__ = __webpack_require__(360);
var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
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
 * Generated class for the MovesboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var MovesboxPage = /** @class */ (function () {
    function MovesboxPage(navCtrl, navParams, popoverCtrl, nativeService, barcodeService, appService, modalCtrl, alertCtrl, keyboard, storage, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.popoverCtrl = popoverCtrl;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.appService = appService;
        this.modalCtrl = modalCtrl;
        this.alertCtrl = alertCtrl;
        this.keyboard = keyboard;
        this.storage = storage;
        this.events = events;
        this.ngZone = ngZone;
        this.isShow = true;
        this.skuStockList = []; //??????????????????
        this.skuStockItem = {}; //??????????????????
        this.sysparms = []; //????????????
        this.selectOptions = {
            title: 'Pizza Toppings',
            subTitle: 'Select your toppings',
            mode: 'md'
        };
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    MovesboxPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.sourceLocCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["b" /* BarcodeType */].BoxCode) {
                _this.ngZone.run(function () {
                    //????????????
                    _this.barcodeService.ScanSkuBarcode(barcode).subscribe(function (val) {
                        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                            _this.scanModel = val;
                            //????????????
                            _this.scanWmsSku(val);
                        }
                    });
                });
            }
        });
    };
    MovesboxPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_10__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    MovesboxPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage');
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.skuStockList)) {
                console.log(_this.skuStockList);
                if (data == 'allCheck') {
                    //??????
                    for (var index = 0; index < _this.skuStockList.length; index++) {
                        _this.skuStockList[index].isCheck = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //????????????
                    for (var index = 0; index < _this.skuStockList.length; index++) {
                        _this.skuStockList[index].isCheck = false;
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
    /**
     * ????????????
     */
    MovesboxPage.prototype.skuStockIsCheckDel = function () {
        this.skuStockList = this.skuStockList.filter(function (x) { return x.isCheck === false; });
    };
    MovesboxPage.prototype.removeItem = function (item) {
        this.skuStockList = this.skuStockList.filter(function (x) { return x != item; });
    };
    MovesboxPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    MovesboxPage.prototype.ionViewDidLoad = function () {
    };
    /**
     * ????????????
     */
    MovesboxPage.prototype.onIsShowClick = function () {
        this.isShow = !this.isShow;
    };
    /**
     * ??????????????????
     * @param event
     */
    MovesboxPage.prototype.scanModelChange = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.sourceLocCode)) {
                this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(function (val) {
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                        _this.scanModel = val;
                        //????????????
                        _this.scanWmsSku(val);
                    }
                });
            }
            else {
                this.nativeService.showToast('????????????????????????');
            }
        }
    };
    /*????????????*/
    MovesboxPage.prototype.scanWmsSku = function (val) {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(val.skucode)) {
            this.nativeService.showToast('????????????????????????');
        }
        var params = {
            skuCode: val.skucode
        };
        //????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            //????????????????????????
            if (result.data.length > 1) {
                //????????????
                _this.openAsnRecordModal(result.data, val);
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('?????????????????????');
            }
            else {
                _this.getMoveStockModel(val, result.data[0].skuId);
            }
        });
    };
    /**
    * ??????????????????
    */
    MovesboxPage.prototype.getMoveStockModel = function (model, skuId) {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model.qty)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model.skucode)) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        var skuLotModel = this.barcodeService.convertLotsModel(model);
        // this.skuCode = model.skucode;
        // this.skuName = model.skuname;
        var param = __assign({ 
            // locCode: this.locCode,
            // qty: model.qty,
            // skuCode: model.skucode,
            // whId: this.warhouserItem.whId,
            // skuLots: skuLotModel,
            // skuId: this.resultSkuItem.skuId
            skuId: skuId, sourceLocCode: this.sourceLocCode, sourceLpnCode: this.sourceLpnCode, wsuName: model.um, skuCode: model.skucode, skuName: model.skuname, moveQty: model.qty }, skuLotModel);
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].getMoveStock, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Get, param, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            if (!result.success) {
                _this.nativeService.showToast(result.msg);
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data.stockList))
                    return;
                if (result.data.stockList.length > 1) {
                    _this.openStockSelectModal(result.data);
                }
                else {
                    result.data['isCheck'] = false;
                    result.data['isWarning'] = false;
                    result.data['stockId'] = result.data.stockList[0].stockId;
                    result.data['wspId'] = result.data.stockList[0].wspId;
                    result.data['soBillId'] = result.data.stockList[0].soBillId;
                    result.data['soBillNo'] = result.data.stockList[0].soBillNo;
                    result.data['orderNo'] = result.data.stockList[0].orderNo;
                    result.data['billDetailId'] = result.data.stockList[0].billDetailId;
                    result.data['wellenId'] = result.data.stockList[0].wellenId;
                    result.data['sourceLpnCode'] = _this.sourceLpnCode;
                    _this.skuStockList.push(result.data);
                    _this.skuStockItem = result.data;
                }
            }
        });
    };
    /**
    *??????????????????
    */
    MovesboxPage.prototype.openAsnRecordModal = function (skuList, val) {
        var _this = this;
        var myModal = this.modalCtrl.create('SkuListModal', {
            skuList: skuList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data)) {
                _this.getMoveStockModel(val, data.skuId);
            }
        });
        myModal.present();
    };
    MovesboxPage.prototype.openStockSelectModal = function (data1) {
        var _this = this;
        var myModal = this.modalCtrl.create('StockSelectModal', {
            stockRecords: data1.stockList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(data))
                return;
            data1['isCheck'] = false;
            data1['isWarning'] = false;
            data1['stockId'] = data.stockId;
            data1['wspId'] = data.wspId;
            data1['soBillId'] = data.soBillId;
            data1['soBillNo'] = data.soBillNo;
            data1['orderNo'] = data.orderNo;
            data1['billDetailId'] = data.billDetailId;
            data1['wellenId'] = data.wellenId;
            data1['sourceLpnCode'] = _this.sourceLpnCode;
            _this.skuStockList.push(data1);
            _this.skuStockItem = data1;
        });
        myModal.present();
    };
    /**
     * ??????????????????
     */
    MovesboxPage.prototype.specOnclick = function (skuStockItem) {
        console.log(skuStockItem);
        var alert = this.alertCtrl.create();
        alert.setTitle('??????????????????');
        skuStockItem.stockList.forEach(function (element) {
            alert.addInput({
                type: 'radio',
                label: element.skuSpec,
                value: element,
                checked: false
            });
        });
        alert.addButton('??????');
        alert.addButton({
            text: '??????',
            handler: function (data) {
                console.log(data);
                for (var index = 0; index < skuStockItem.stockList.length; index++) {
                    var element = skuStockItem.stockList[index];
                    if (element === data) {
                        var temp = skuStockItem.stockList[0];
                        skuStockItem.stockList[0] = element;
                        skuStockItem.stockList[index] = temp;
                        //????????????IDskuStockItem.stockList[0]
                        skuStockItem.stockId = skuStockItem.stockList[0].stockId;
                    }
                }
            }
        });
        alert.present();
    };
    /**
     * ?????????
     */
    MovesboxPage.prototype.btnOk = function () {
        var _this = this;
        var skuStockList = this.skuStockList.filter(function (x) { return x.isCheck == true; });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(skuStockList)) {
            this.nativeService.showToast('??????????????????????????????');
            return;
        }
        //??????????????????
        console.log(this.skuStockList);
        console.log(skuStockList);
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].verifyStockForMoveByBox, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post, skuStockList, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                //???????????????????????????
                for (var index = 0; index < result.data.length; index++) {
                    var element = result.data[index];
                    for (var j = 0; j < _this.skuStockList.length; j++) {
                        if (_this.skuStockList[j].stockId == element.stockId) {
                            _this.skuStockList[j].isWarning = true;
                        }
                    }
                }
            }
            else {
                //?????????????????????  
                //this.navCtrl.push('MovesboxInfoPage', { skuStockList: this.skuStockList });
                var myModal = _this.modalCtrl.create('MovesboxInfoPage', {
                    skuStockList: skuStockList,
                    sourceLocCode: _this.sourceLocCode,
                    sourceLpnCode: _this.sourceLpnCode //?????????
                });
                //??????
                myModal.onDidDismiss(function (data) {
                    if (data) {
                        //????????????
                        _this.clearModel();
                    }
                });
                myModal.present();
            }
        });
    };
    MovesboxPage.prototype.clearModel = function () {
        this.scanView = '';
        this.sourceLocCode = '';
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__["a" /* ScanModel */]();
        this.skuStockList = [];
        this.skuStockItem = { skuName: '', moveQty: '', wsuName: '' };
    };
    MovesboxPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-movesbox',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesbox\movesbox.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ????????????\n\n      <!-- <div class="title_f_name">(?????????)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n\n\n\n\n  <!-- ???????????? -->\n\n  <div class="head_lable" *ngIf="isShow">\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="text" [(ngModel)]="sourceLpnCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>???&emsp;??????</ion-label>\n\n      <ion-input [disabled]=\'true\' type="text" [(ngModel)]="skuStockItem.skuName"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>???&emsp;??????</ion-label>\n\n      <ion-input  type="number" [(ngModel)]="skuStockItem.moveQty"></ion-input>\n\n      <ion-select [disabled]=\'true\' [(ngModel)]="skuStockItem.wsuName" cancelText="??????">\n\n        <ion-option [value]="skuStockItem.wsuName">{{skuStockItem.wsuName}}</ion-option>\n\n      </ion-select>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>???&emsp;??????</ion-label>\n\n      <ion-input (keyup)="scanModelChange($event)" [(ngModel)]="scanView" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n\n\n  </div>\n\n  <!-- ??????????????? -->\n\n  <div class="list_item_serial">\n\n    <div class="list_item_title">\n\n      <!-- <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n              (click)="isTitle(false)">???????????????(<b>{{InNumberLength}}</b>)</span> -->\n\n      <span *ngIf="isShow; else elseBlock" (click)="onIsShowClick()">\n\n        <ion-icon name="md-arrow-dropup"></ion-icon>\n\n      </span>\n\n\n\n      <ng-template #elseBlock>\n\n        <span (click)="onIsShowClick()">\n\n          <ion-icon name="md-arrow-dropdown"></ion-icon>\n\n        </span>\n\n\n\n      </ng-template>\n\n      <span [ngStyle]="{\'border-bottom\': \'3px solid #008080\' }"\n\n        (click)="isTitle(true)">????????????(<b>{{skuStockList.length}}</b>)</span>\n\n      <span>\n\n        <button ion-button icon-only (click)="presentPopover($event)">\n\n          <ion-icon name="md-more"></ion-icon>\n\n        </button>\n\n\n\n      </span>\n\n    </div>\n\n  </div>\n\n\n\n  <ion-list class="list_item_div">\n\n\n\n    <ion-scroll scrollY="true" >\n\n      <!--????????????-->\n\n      <ion-item-sliding *ngFor="let skuStockItem of skuStockList">\n\n        <ion-item>\n\n          <ion-checkbox [(ngModel)]="skuStockItem.isCheck"></ion-checkbox>\n\n          <ion-label>\n\n            <div class="title-ms clearfix">\n\n              <div class="title-ms-info clearfix">\n\n                <div>\n\n                  <span>???&emsp;&emsp;??????</span>\n\n                  <span>{{skuStockItem.skuName}}</span>\n\n                </div>\n\n                <div>\n\n                  <span>???&ensp;???&ensp;??????</span>\n\n                  <span>{{skuStockItem.sourceLocCode}}</span>\n\n                </div>\n\n                <div *ngFor="let item of skuStockItem.skuLotValList">\n\n                  <span *ngIf="item.skuLotValue!=\'\'">{{item.skuLotLabel}}???</span>\n\n                  <span *ngIf="item.skuLotValue!=\'\'">{{item.skuLotValue}}</span>\n\n                </div>\n\n                <div [ngStyle]="{color: skuStockItem.isWarning?\'red\':\'\'}" class="number-msg">\n\n                  <span>?????????</span>\n\n                  <span>{{skuStockItem.moveQty + skuStockItem.wsuName}}\n\n                  </span>\n\n                </div>\n\n                <div [ngStyle]="{\'position\': skuStockItem.stockList[0].skuSpec.length>=6?\'static\':\'absolute\'}"\n\n                  class="spec-msg">\n\n                  <span>?????????</span>\n\n                  <span>{{skuStockItem.stockList[0].skuSpec}}</span>\n\n                  <span *ngIf="skuStockItem.stockList.length>=2" class="spec-msg-dropdown"\n\n                    (tap)="specOnclick(skuStockItem)">\n\n                    <ion-icon name="md-arrow-dropdown"></ion-icon>\n\n                  </span>\n\n                </div>\n\n              </div>\n\n            </div>\n\n          </ion-label>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button (tap)="removeItem(skuStockItem)" ion-button color="danger">\n\n            ??????\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-scroll>\n\n  </ion-list>\n\n\n\n  <!-- ????????? -->\n\n  <!-- <div class="list_item" *ngIf="IsShow==IsSkuMove">\n\n    <ion-item>\n\n      <ion-label>???????????????</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n\n\n    </ion-item>\n\n  </div> -->\n\n\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      ?????????[Ent]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesbox\movesbox.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_6__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__["a" /* Keyboard */],
            __WEBPACK_IMPORTED_MODULE_9__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], MovesboxPage);
    return MovesboxPage;
}());

//# sourceMappingURL=movesbox.js.map

/***/ }),

/***/ 765:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MovesboxPageModule", function() { return MovesboxPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__movesbox__ = __webpack_require__(1360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var MovesboxPageModule = /** @class */ (function () {
    function MovesboxPageModule() {
    }
    MovesboxPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__movesbox__["a" /* MovesboxPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__movesbox__["a" /* MovesboxPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], MovesboxPageModule);
    return MovesboxPageModule;
}());

//# sourceMappingURL=movesbox.module.js.map

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
//# sourceMappingURL=44.js.map