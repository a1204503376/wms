webpackJsonp([33],{

/***/ 1364:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ShipmentLpnPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__ = __webpack_require__(360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__ = __webpack_require__(359);
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
 * Generated class for the ShipmentLpnPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var ShipmentLpnPage = /** @class */ (function () {
    function ShipmentLpnPage(navCtrl, navParams, appService, nativeService, modalCtrl, barcodeService, events, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.modalCtrl = modalCtrl;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.truckView = 0; //???????????????
        this.ShipmentLpnView = 1; //??????????????????
        this.ShipmentBllView = 2; //????????????????????????
        this.IsShow = 0; //????????????
        //???????????????
        this.loadId = ''; //?????????
        this.truckId = ''; //??????id
        this.truckCode = ''; //????????????
        this.driverName = ''; //????????????
        this.driverPhone = ''; //????????????
        this.plateNumber = ''; //?????????
        this.truckHeaderWaybill = ''; //????????????
        this.whId = '';
        this.ShipmenArray = []; //????????????
        this.LpnCode = ''; //????????????  
    }
    ShipmentLpnPage.prototype.clearModel = function () {
        this.IsShow = 0; //????????????
        this.loadId = ''; //?????????
        this.truckId = ''; //??????id
        this.truckCode = ''; //????????????
        this.driverName = ''; //????????????
        this.driverPhone = ''; //????????????
        this.plateNumber = ''; //?????????
        this.ShipmenArray = []; //????????????
        this.LpnCode = '';
        this.truckHeaderWaybill = '';
        this.whId = '';
    };
    ShipmentLpnPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad ShipmentLpnPage');
    };
    ShipmentLpnPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //????????????????????????????????????
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.LpnCode = barcode;
                    _this.getSkuStock(_this.LpnCode);
                });
            }
        });
    };
    ShipmentLpnPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_6__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
    * ?????????????????????
    */
    // truckCode_KeyDown(event) {
    //   if (event && event.keyCode == 13) {
    //     this.getTruckCode();
    //   }
    // }
    /**
    * ????????????????????????
    */
    ShipmentLpnPage.prototype.LpnCode_Enter = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.LpnCode)) {
                this.nativeService.showToast('?????????????????????????????????');
                return;
            }
            this.getSkuStock(this.LpnCode);
        }
    };
    /**
     * ????????????
     */
    ShipmentLpnPage.prototype.nextBtnOk = function () {
        var _this = this;
        if (!this.whId) {
            this.nativeService.showToast('???????????????????????????');
            return;
        }
        var param = {
            whId: this.whId,
            plateNumber: this.plateNumber,
            driverName: this.driverName,
            driverPhone: this.driverPhone,
            truckHeaderWaybill: this.truckHeaderWaybill
        };
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].soTruckHeaderSubmit, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, param, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            _this.truckCode = result.truckCode;
            _this.truckId = result.truckId;
            _this.IsShow = _this.ShipmentLpnView;
            //??????????????????
            _this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].Lodingdetail, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, { truckId: result.truckId }, '', function (result) {
                if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(result)) {
                    result.data.forEach(function (element) {
                        if (_this.checkScaned(element.lpnCode)) {
                            return;
                        }
                        _this.getSkuStockInfo(element.lpnCode);
                    });
                }
            });
        });
    };
    /**
     * ??????????????????????????????
     */
    ShipmentLpnPage.prototype.checkScaned = function (lpnCode) {
        var arr = this.ShipmenArray.filter(function (x) { return x.stockPdaVO.lpnCode.toUpperCase() == lpnCode.toUpperCase(); });
        if (arr.length > 0) {
            this.nativeService.showToast('???????????????????????????');
            return true;
        }
        return false;
    };
    ShipmentLpnPage.prototype.getSkuStock = function (lpnCode) {
        var _this = this;
        if (this.checkScaned(lpnCode)) {
            return;
        }
        this.nativeService.showLoading();
        //????????????????????????
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getSkuStock, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'LpnCode=' + lpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            // this.ShipmenArray.push(result.data);
            //??????????????????
            var soTruckDetailList = [];
            soTruckDetailList.push({
                lpnCode: result.data.stockPdaVO.lpnCode,
                soBillId: result.data.soBillId,
                truckId: _this.truckId,
                truckCode: _this.truckCode
            });
            _this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].saveSoTruckDetail, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, soTruckDetailList, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Json, function (result2) {
                _this.nativeService.hideLoading();
                _this.ShipmenArray.push(result.data);
            });
        });
    };
    ShipmentLpnPage.prototype.getSkuStockInfo = function (lpnCode) {
        var _this = this;
        this.nativeService.showLoading();
        //????????????????????????
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getSkuStock, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'LpnCode=' + lpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.ShipmenArray.push(result.data);
            _this.nativeService.hideLoading();
        });
    };
    /**
    * ???????????????????????????????????????
    */
    ShipmentLpnPage.prototype.getTruckDetailByPlateNumber = function () {
        var _this = this;
        this.nativeService.showLoading();
        var param = {
            driverName: this.driverName,
            driverPhone: this.driverPhone,
            plateNumber: this.plateNumber,
            truckHeaderWaybill: this.truckHeaderWaybill
        };
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getTruckHeader, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, param, '', function (result) {
            _this.nativeService.hideLoading();
            _this.truckCode = result.truckCode;
            _this.truckId = result.truckId;
            _this.IsShow = _this.ShipmentLpnView;
        });
    };
    ShipmentLpnPage.prototype.exit = function () {
        if (this.IsShow == this.truckView) {
            this.navCtrl.pop();
        }
        else if (this.IsShow == this.ShipmentLpnView) {
            this.IsShow = this.truckView;
        }
    };
    ShipmentLpnPage.prototype.btnOk = function () {
        if (this.IsShow == this.truckView) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.loadId)) {
                this.nativeService.showToast('??????????????????!');
                return;
            }
            this.getTruckDetailByPlateNumber();
        }
        else if (this.IsShow == this.ShipmentLpnView) {
            this.saveSoTruckDetail();
        }
    };
    ShipmentLpnPage.prototype.saveSoTruckDetail = function () {
        var _this = this;
        if (this.ShipmenArray.length == 0) {
            this.nativeService.showToast('?????????????????????');
            return;
        }
        var soTruckDetailList = [];
        this.ShipmenArray.forEach(function (element) {
            soTruckDetailList.push({
                lpnCode: element.stockPdaVO.lpnCode,
                soBillId: element.soBillId,
                truckId: _this.truckId,
                truckCode: _this.truckCode
            });
        });
        //??????
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].SaveconfirmSo, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'truckId=' + this.truckId, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            _this.nativeService.showToast('????????????');
            _this.clearModel();
        });
    };
    ShipmentLpnPage.prototype.headSelected = function (item) {
        var _this = this;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getSkuStockDetailList, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'LpnCode=' + item.stockPdaVO.lpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(result)) {
                var profileModal2 = _this.modalCtrl.create('SkuBillModal', { result: result.data });
                profileModal2.present();
            }
        });
    };
    /**
   * ?????????????????????
   * @param item
   */
    ShipmentLpnPage.prototype.removeItem = function (item) {
        var _this = this;
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].deleteTruckDetail, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, "truckId=" + this.truckId + "&truckCode=" + this.truckCode + "&lpnCode=" + item.stockPdaVO.lpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (result.code == 200) {
                _this.ShipmenArray = _this.ShipmenArray.filter(function (x) { return x != item; });
            }
            _this.nativeService.showToast(result.msg);
            _this.IsShow = _this.ShipmentLpnView;
        });
    };
    /**
   * ?????????????????????
   */
    ShipmentLpnPage.prototype.keyEnter = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.loadId)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            this.nativeService.showLoading();
            var param = {
                loadId: this.loadId
            };
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].soRegisterDetail, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Get, param, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
                _this.nativeService.hideLoading();
                _this.ShipmenArray = [];
                if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                    _this.plateNumber = result.data.plateNumber;
                    _this.driverName = result.data.driverName;
                    _this.driverPhone = result.data.driverPhone;
                    _this.loadId = result.data.loadId;
                    _this.whId = result.data.whId;
                }
                else {
                    _this.nativeService.showToast(result.msg);
                }
            });
        }
    };
    ShipmentLpnPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-shipment-lpn',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\shipmenthome\shipment-lpn\shipment-lpn.html"*/'<!--\n\n  Generated template for the ShipmentLpnPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <ion-searchbar *ngIf="IsShow===ShipmentLpnView" [(ngModel)]="LpnCode" (keyup)="LpnCode_Enter($event)"\n\n    Description=\'primary\' placeholder="??????ID/LPN">\n\n  </ion-searchbar>\n\n  <div class="nav-tile">\n\n    <!-- <ul>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>????????????</li>\n\n    </ul> -->\n\n    <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n\n\n<ion-content>\n\n  <!-- ?????? -->\n\n  <div *ngIf="IsShow===truckView" class="truck-nav">\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input maxlength=\'60\' (keyup)="keyEnter($event)" [(ngModel)]="loadId" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input maxlength=\'30\' [(ngModel)]="plateNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>?????????</ion-label>\n\n      <ion-input maxlength=\'60\' [(ngModel)]="driverName" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>?????????</ion-label>\n\n      <ion-input maxlength=\'20\' [(ngModel)]="driverPhone" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input maxlength=\'60\' [(ngModel)]="truckHeaderWaybill" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n\n\n  <div *ngIf="IsShow===ShipmentLpnView">\n\n    <div class="nav-header">\n\n      <div>\n\n        <b>{{truckCode}}</b>\n\n      </div>\n\n      <div>\n\n        {{driverName}}&nbsp;{{driverPhone}}\n\n      </div>\n\n      <div>\n\n        {{plateNumber}}\n\n      </div>\n\n    </div>\n\n    <ion-list no-lines class="list_item_div">\n\n      <ion-item-sliding *ngFor="let item of ShipmenArray" class="title-ms">\n\n        <ion-item (click)="headSelected(item)" style="height: auto;">\n\n          <div class="title-ms-info">\n\n            <div class="title-font">\n\n              <div><a>{{item.stockPdaVO.lpnCode}}</a></div>\n\n              <div style="color:orange;">????????????:{{item.stockPdaVO.skuCount}}</div>\n\n              <div>?????????:{{item.stockPdaVO.locCode}}</div>\n\n            </div>\n\n            <div class="ionic_right">\n\n              <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n            </div>\n\n          </div>\n\n          <span class="span-top">{{item.stockPdaVO.whName}}</span>\n\n          <span class="span-bt">{{item.stockPdaVO.scanQty}}{{item.stockPdaVO.wsuName}}</span>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button (click)="removeItem(item)" ion-button color="danger">\n\n            ??????\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-list>\n\n    <!-- <div *ngFor="let item of ShipmenArray" class="title-ms">\n\n      <div class="title-ms-info">\n\n        <div class="title-font">\n\n          <div><a>{{item.stockPdaVO.lpnCode}}</a></div>\n\n          <div style="color:orange;">????????????:{{item.stockPdaVO.skuCount}}</div>\n\n          <div>?????????:{{item.stockPdaVO.locCode}}</div>\n\n        </div>\n\n        <div class="ionic_right">\n\n          <ion-icon class="iconfont icon-xiangyou"></ion-icon>\n\n        </div>\n\n      </div>\n\n      <span class="span-top">{{item.stockPdaVO.whName}}</span>\n\n      <span class="span-bt">{{item.stockPdaVO.occupyQty}}{{item.stockPdaVO.wsuName}}</span>\n\n    </div> -->\n\n  </div>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box" *ngIf="IsShow===truckView">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="nextBtnOk()">\n\n      ?????????[???]\n\n    </button>\n\n  </div>\n\n  <div class="btn-box" *ngIf="IsShow===ShipmentLpnView">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      ?????????[???]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\shipmenthome\shipment-lpn\shipment-lpn.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_5__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], ShipmentLpnPage);
    return ShipmentLpnPage;
}());

//# sourceMappingURL=shipment-lpn.js.map

/***/ }),

/***/ 769:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ShipmentLpnPageModule", function() { return ShipmentLpnPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__shipment_lpn__ = __webpack_require__(1364);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var ShipmentLpnPageModule = /** @class */ (function () {
    function ShipmentLpnPageModule() {
    }
    ShipmentLpnPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__shipment_lpn__["a" /* ShipmentLpnPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__shipment_lpn__["a" /* ShipmentLpnPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], ShipmentLpnPageModule);
    return ShipmentLpnPageModule;
}());

//# sourceMappingURL=shipment-lpn.module.js.map

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
//# sourceMappingURL=33.js.map