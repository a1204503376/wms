webpackJsonp([57],{

/***/ 1356:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CollageTaskPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__ = __webpack_require__(360);
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
var CollageTaskPage = /** @class */ (function () {
    function CollageTaskPage(navCtrl, navParams, nativeService, appService, barcodeService, modalCtrl, ngZone, events, alertControl) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.barcodeService = barcodeService;
        this.modalCtrl = modalCtrl;
        this.ngZone = ngZone;
        this.events = events;
        this.alertControl = alertControl;
        this.scanModelList = []; //????????????????????????
        this.isShow = true;
        this.resultList = this.navParams.get('result');
        this.taskId = this.navParams.get('taskId');
    }
    CollageTaskPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad CollageTaskPage');
    };
    CollageTaskPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
        if (__WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag) {
            __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
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
    CollageTaskPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
     * ?????????????????????
     *
     */
    CollageTaskPage.prototype.scanModelChange = function (event) {
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
    CollageTaskPage.prototype.getStockInfoPutawayForBox = function (scanView) {
        var _this = this;
        this.barcodeService.ScanSkuBarcode(scanView).subscribe(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                _this.scanModel = val;
                //????????????
                _this.scanWmsSku(val);
            }
            else {
                __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
            }
        });
    };
    /*????????????*/
    CollageTaskPage.prototype.scanWmsSku = function (val) {
        var _this = this;
        var params = {
            skuCode: val.skucode
        };
        //????????????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
            _this.resultSkuItem = null;
            //????????????????????????
            if (result.data.length > 1) {
                //????????????
                _this.openAsnRecordModal(result.data, val);
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('?????????????????????');
            }
            else {
                _this.resultSkuItem = result.data[0];
                val['skuId'] = _this.resultSkuItem.skuId;
                val['skuName'] = val.skuname;
                if (_this.CheckFinish()) {
                    _this.scanModelList.push(val);
                }
            }
        });
    };
    CollageTaskPage.prototype.CheckFinish = function () {
        if (this.scanModelList.length + this.resultList.finish + 1 > this.resultList.total) {
            return false;
        }
        else {
            return true;
        }
    };
    /**
     * ??????????????????
     */
    CollageTaskPage.prototype.removeItem = function (item) {
        this.scanModelList = this.scanModelList.filter(function (x) { return x != item; });
    };
    /**
    *??????????????????
    */
    CollageTaskPage.prototype.openAsnRecordModal = function (skuList, val) {
        var _this = this;
        var myModal = this.modalCtrl.create('SkuListModal', {
            skuList: skuList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data)) {
                _this.resultSkuItem = data;
                val['skuId'] = _this.resultSkuItem.skuId;
                val['skuName'] = val.skuname;
                if (_this.CheckFinish()) {
                    _this.scanModelList.push(val);
                }
            }
        });
        myModal.present();
    };
    /**
     * ????????????????????????
     */
    CollageTaskPage.prototype.submitOutStockLpnInfo = function () {
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
        for (var index = 0; index < this.scanModelList.length; index++) {
            this.scanModelList[index]['pickQty'] = this.scanModelList[index].qty;
        }
        var params = {
            taskId: this.taskId,
            soBillId: this.resultList.soBillId,
            whId: this.resultList.whId,
            lpnCode: this.lpnCode,
            length: this.length,
            width: this.width,
            height: this.height,
            weight: this.weight,
            detailList: this.scanModelList,
            remaker: this.remaker,
            isUpdate: this.isUpdate
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].submitOutStockLpnInfo, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            if (result.success) {
                _this.getOutstockLpnInfo();
            }
            else {
                _this.alertControl.create({
                    title: '??????',
                    subTitle: result.message,
                    buttons: [{ text: '??????' },
                        {
                            text: '??????',
                            handler: function () {
                                _this.isUpdate = true;
                                _this.submitOutStockLpnInfo();
                            }
                        }
                    ]
                }).present();
            }
        });
    };
    /**
     * ??????????????????
     */
    CollageTaskPage.prototype.getOutstockLpnInfo = function () {
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
    CollageTaskPage.prototype.taskPDAClose = function () {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].taskPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].taskPDAClose, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, { ids: this.taskId }, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            _this.navCtrl.pop();
        });
    };
    /**
     * ????????????????????????
     */
    CollageTaskPage.prototype.clearModel = function () {
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
    CollageTaskPage.prototype.handItemClick = function () {
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
    CollageTaskPage.prototype.btnOk = function () {
        if (this.isShow) {
            if (this.scanModelList.length <= 0) {
                this.nativeService.showToast('??????????????????,?????????');
                return;
            }
            this.isShow = false;
            // this.getLpnCode();
        }
        else {
            //????????????
            this.submitOutStockLpnInfo();
        }
    };
    CollageTaskPage.prototype.getLpnCode = function () {
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
    CollageTaskPage.prototype.exit = function () {
        if (this.isShow) {
            this.navCtrl.pop();
        }
        else {
            this.isShow = true;
        }
    };
    CollageTaskPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-collage-task',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\collage-task\collage-task.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      ????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n          <button ion-button icon-only>\n\n              <ion-icon name="menu"></ion-icon>\n\n          </button>\n\n      </ion-buttons> -->\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <div class="order-info">????????????{{scanModelList.length+ resultList.finish}}/{{resultList.total}}</div>\n\n      <crumbs></crumbs>\n\n      <!-- <ul>\n\n        <li>??????</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>????????????</li>\n\n      </ul> -->\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n  <div>\n\n    <!-- ??????????????? -->\n\n    <div class="title-ms clearfix" (click)="handItemClick()">\n\n      <div class="title-ms-info clearfix">\n\n        <div>???????????????{{resultList.soBillNo}}</div>\n\n        <div>???????????????{{resultList.billTypeName}}</div>\n\n        <div>???&emsp;&emsp;??????{{resultList.clientName}}</div>\n\n        <div *ngIf="isShow">?????????:{{resultList.lpnList.length}}</div>\n\n        <div *ngIf="!isShow">?????????:{{scanModelList.length}}</div>\n\n        <div>{{resultList.whName}}</div>\n\n        <div class="ionic_right">\n\n          <ion-icon name="arrow-forward"></ion-icon>\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <!-- ???????????? -->\n\n  <div *ngIf="isShow">\n\n    <!-- <ion-item> -->\n\n      <div class="list_header_box">\n\n        <ion-label>?????????</ion-label>\n\n        <ion-input type="text" (keyup)="scanModelChange($event)" [(ngModel)]="scanView"></ion-input>\n\n        <ion-avatar item-end>\n\n          <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n        </ion-avatar>\n\n      </div>\n\n    \n\n    <!-- </ion-item> -->\n\n    <!-- ???????????? -->\n\n    <div style="margin-top: 1px;">\n\n      <ion-list class="list_item_div" *ngIf="scanModelList.length>0">\n\n        <ion-scroll scrollY="true" style="height:35rem;">\n\n          <ion-item-sliding *ngFor="let item of scanModelList">\n\n            <ion-item>\n\n              <div class="list_item_ls_box ">\n\n                <div class="left">\n\n                  <span>{{item.skuname}}</span>\n\n                  <span>{{item.skucode}}</span>\n\n                </div>\n\n                <div class="right">\n\n                  <span>{{item.qty}}{{item.um}}</span>\n\n                </div>\n\n              </div>\n\n            </ion-item>\n\n            <ion-item-options side="right">\n\n              <button (click)="removeItem(item)" ion-button color="danger">\n\n                ??????\n\n              </button>\n\n            </ion-item-options>\n\n          </ion-item-sliding>\n\n        </ion-scroll>\n\n      </ion-list>\n\n    </div>\n\n  </div>\n\n  <!-- ?????????????????? -->\n\n  <div *ngIf="!isShow">\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="text" [(ngModel)]="lpnCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="length"></ion-input>\n\n      <ion-avatar item-end>\n\n        ???\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="width"></ion-input>\n\n      <ion-avatar item-end>\n\n        ???\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="height"></ion-input>\n\n      <ion-avatar item-end>\n\n        ???\n\n      </ion-avatar>\n\n    </ion-item>\n\n\n\n    <ion-item>\n\n      <ion-label>?????????</ion-label>\n\n      <ion-input type="number" (keyup)="scanModelChange($event)" [(ngModel)]="weight"></ion-input>\n\n      <ion-avatar item-end>\n\n        ??????\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>?????????</ion-label>\n\n      <ion-textarea placeholder="???????????????" [(ngModel)]="remaker"></ion-textarea>\n\n    </ion-item>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      <span *ngIf="!isShow">??????[Ent]</span><span *ngIf="isShow">?????????[Ent]</span>\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\collage-task\collage-task.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_5__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */]])
    ], CollageTaskPage);
    return CollageTaskPage;
}());

//# sourceMappingURL=collage-task.js.map

/***/ }),

/***/ 761:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CollageTaskPageModule", function() { return CollageTaskPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__collage_task__ = __webpack_require__(1356);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CollageTaskPageModule = /** @class */ (function () {
    function CollageTaskPageModule() {
    }
    CollageTaskPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__collage_task__["a" /* CollageTaskPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__collage_task__["a" /* CollageTaskPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CollageTaskPageModule);
    return CollageTaskPageModule;
}());

//# sourceMappingURL=collage-task.module.js.map

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
//# sourceMappingURL=57.js.map