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
        this.truckView = 0; //装车牌界面
        this.ShipmentLpnView = 1; //托盘发运界面
        this.ShipmentBllView = 2; //托盘发运单据界面
        this.IsShow = 0; //当前界面
        //发运首界面
        this.loadId = ''; //装车牌
        this.truckId = ''; //车次id
        this.truckCode = ''; //车次编号
        this.driverName = ''; //司机名称
        this.driverPhone = ''; //司机电话
        this.plateNumber = ''; //车牌号
        this.truckHeaderWaybill = ''; //运单编号
        this.whId = '';
        this.ShipmenArray = []; //装车集合
        this.LpnCode = ''; //容器编码  
    }
    ShipmentLpnPage.prototype.clearModel = function () {
        this.IsShow = 0; //当前界面
        this.loadId = ''; //装车牌
        this.truckId = ''; //车次id
        this.truckCode = ''; //车次编号
        this.driverName = ''; //司机名称
        this.driverPhone = ''; //司机电话
        this.plateNumber = ''; //车牌号
        this.ShipmenArray = []; //装车集合
        this.LpnCode = '';
        this.truckHeaderWaybill = '';
        this.whId = '';
    };
    ShipmentLpnPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad ShipmentLpnPage');
    };
    ShipmentLpnPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
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
    * 装车牌回车事件
    */
    // truckCode_KeyDown(event) {
    //   if (event && event.keyCode == 13) {
    //     this.getTruckCode();
    //   }
    // }
    /**
    * 容器编码回车事件
    */
    ShipmentLpnPage.prototype.LpnCode_Enter = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.LpnCode)) {
                this.nativeService.showToast('请输入托盘或容器编码！');
                return;
            }
            this.getSkuStock(this.LpnCode);
        }
    };
    /**
     * 发运生成
     */
    ShipmentLpnPage.prototype.nextBtnOk = function () {
        var _this = this;
        if (!this.whId) {
            this.nativeService.showToast('请先扫描运单信息！');
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
            //获取单据明细
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
     * 检查是否重复扫描容器
     */
    ShipmentLpnPage.prototype.checkScaned = function (lpnCode) {
        var arr = this.ShipmenArray.filter(function (x) { return x.stockPdaVO.lpnCode.toUpperCase() == lpnCode.toUpperCase(); });
        if (arr.length > 0) {
            this.nativeService.showToast('请不要重复扫描容器');
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
        //获取物品库存简版
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getSkuStock, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'LpnCode=' + lpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            // this.ShipmenArray.push(result.data);
            //添加发车明细
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
        //获取物品库存简版
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].getSkuStock, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'LpnCode=' + lpnCode, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.ShipmenArray.push(result.data);
            _this.nativeService.hideLoading();
        });
    };
    /**
    * 根据车牌号查询车次明细信息
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
                this.nativeService.showToast('请输入装车牌!');
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
            this.nativeService.showToast('请扫描发运托盘');
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
        //发运
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].LoadingPDA + __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["b" /* Api */].SaveconfirmSo, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["f" /* Method */].Post, 'truckId=' + this.truckId, __WEBPACK_IMPORTED_MODULE_3__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            _this.nativeService.showToast('提交成功');
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
   * 删除扫描序列号
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
   * 查询装车牌明细
   */
    ShipmentLpnPage.prototype.keyEnter = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.loadId)) {
                this.nativeService.showToast('请输入装车牌！');
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
            selector: 'page-shipment-lpn',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\shipmenthome\shipment-lpn\shipment-lpn.html"*/'<!--\n\n  Generated template for the ShipmentLpnPage page.\n\n\n\n  See http://ionicframework.com/docs/components/#navigation for more info on\n\n  Ionic pages and navigation.\n\n-->\n\n<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>托盘发运\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <ion-searchbar *ngIf="IsShow===ShipmentLpnView" [(ngModel)]="LpnCode" (keyup)="LpnCode_Enter($event)"\n\n    Description=\'primary\' placeholder="托盘ID/LPN">\n\n  </ion-searchbar>\n\n  <div class="nav-tile">\n\n    <!-- <ul>\n\n      <li>首页</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>发运</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>托盘发运</li>\n\n    </ul> -->\n\n    <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n\n\n<ion-content>\n\n  <!-- 车牌 -->\n\n  <div *ngIf="IsShow===truckView" class="truck-nav">\n\n    <ion-item>\n\n      <ion-label>装车牌：</ion-label>\n\n      <ion-input maxlength=\'60\' (keyup)="keyEnter($event)" [(ngModel)]="loadId" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>车牌号：</ion-label>\n\n      <ion-input maxlength=\'30\' [(ngModel)]="plateNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>司机：</ion-label>\n\n      <ion-input maxlength=\'60\' [(ngModel)]="driverName" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>电话：</ion-label>\n\n      <ion-input maxlength=\'20\' [(ngModel)]="driverPhone" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>运单号：</ion-label>\n\n      <ion-input maxlength=\'60\' [(ngModel)]="truckHeaderWaybill" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n\n\n  <div *ngIf="IsShow===ShipmentLpnView">\n\n    <div class="nav-header">\n\n      <div>\n\n        <b>{{truckCode}}</b>\n\n      </div>\n\n      <div>\n\n        {{driverName}}&nbsp;{{driverPhone}}\n\n      </div>\n\n      <div>\n\n        {{plateNumber}}\n\n      </div>\n\n    </div>\n\n    <ion-list no-lines class="list_item_div">\n\n      <ion-item-sliding *ngFor="let item of ShipmenArray" class="title-ms">\n\n        <ion-item (click)="headSelected(item)" style="height: auto;">\n\n          <div class="title-ms-info">\n\n            <div class="title-font">\n\n              <div><a>{{item.stockPdaVO.lpnCode}}</a></div>\n\n              <div style="color:orange;">物品种类:{{item.stockPdaVO.skuCount}}</div>\n\n              <div>暂存区:{{item.stockPdaVO.locCode}}</div>\n\n            </div>\n\n            <div class="ionic_right">\n\n              <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n            </div>\n\n          </div>\n\n          <span class="span-top">{{item.stockPdaVO.whName}}</span>\n\n          <span class="span-bt">{{item.stockPdaVO.scanQty}}{{item.stockPdaVO.wsuName}}</span>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button (click)="removeItem(item)" ion-button color="danger">\n\n            删除\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-list>\n\n    <!-- <div *ngFor="let item of ShipmenArray" class="title-ms">\n\n      <div class="title-ms-info">\n\n        <div class="title-font">\n\n          <div><a>{{item.stockPdaVO.lpnCode}}</a></div>\n\n          <div style="color:orange;">物品种类:{{item.stockPdaVO.skuCount}}</div>\n\n          <div>暂存区:{{item.stockPdaVO.locCode}}</div>\n\n        </div>\n\n        <div class="ionic_right">\n\n          <ion-icon class="iconfont icon-xiangyou"></ion-icon>\n\n        </div>\n\n      </div>\n\n      <span class="span-top">{{item.stockPdaVO.whName}}</span>\n\n      <span class="span-bt">{{item.stockPdaVO.occupyQty}}{{item.stockPdaVO.wsuName}}</span>\n\n    </div> -->\n\n  </div>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box" *ngIf="IsShow===truckView">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="nextBtnOk()">\n\n      下一步[→]\n\n    </button>\n\n  </div>\n\n  <div class="btn-box" *ngIf="IsShow===ShipmentLpnView">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      上一步[←]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      发运[Ent]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\shipmenthome\shipment-lpn\shipment-lpn.html"*/,
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
            { id: "HomePage", name: "首页" },
            { id: "TaskviewPage", name: "任务查看" },
            { id: "CheckinhomePage", name: "收货" },
            { id: "CheckinpiecePage", name: "按件收货" },
            { id: "CheckInPage", name: "执行收货" },
            { id: "CheckinboxPage", name: "按箱收货" },
            { id: "SearchOrderPage", name: "按单收货" },
            { id: "CheckinBoxInfoPage", name: "执行收货" },
            { id: "CheckintrayPage", name: "按托收货" },
            { id: "CheckinDetailedPage", name: "收货记录" },
            { id: "CollageTaskPage", name: "拼托任务" },
            { id: "CollageTaskListPage", name: "托盘详情" },
            { id: "CollageTaskBoxPage", name: "托盘详情" },
            { id: "CyclecounthomePage", name: "盘点" },
            { id: "CyclecountrandominfoPage", name: "随机盘点" },
            { id: "CyclecountrantaskPage", name: "盘点任务" },
            { id: "CyclecountBillPage", name: "静态盘点" },
            { id: "CyclecoutrantaskinfoPage", name: "执行盘点" },
            { id: "MovehomePage", name: "移动" },
            { id: "MovesboxPage", name: "按箱移动" },
            { id: "MovesboxInfoPage", name: "按箱移动" },
            { id: "moveSku", name: "物品移动" },
            { id: "moveStock", name: "托盘移动" },
            { id: "moveStocks", name: "多拖移动" },
            { id: "PackageTaskPage", name: "打包任务" },
            { id: "PackageTaskListPage", name: "打包任务" },
            { id: "PackageTaskInfoPage", name: "任务明细" },
            { id: "PutawayhomePage", name: "上架" },
            { id: "PutawayPage", name: "按托上架" },
            { id: "PutawayboxPage", name: "按箱上架" },
            { id: "ReplenishmenhomePage", name: "补货任务" },
            { id: "ReplenishmenDetilePage", name: "补货任务" },
            { id: "SettingPage", name: "个人设置" },
            { id: "SettingPwdPage", name: "修改密码" },
            { id: "SettingThemePage", name: "主题颜色" },
            { id: "ShipmenthomePage", name: "发运" },
            { id: "ShipmentLpnPage", name: "托盘发运" },
            { id: "StockhomePage", name: "库存" },
            { id: "StockdetailPage", name: "库存查询" },
            { id: "StockqueryPage", name: "库存查询" },
            { id: "StockPritPage", name: "库存打印" },
            { id: "TakeawayhomePage", name: "拣货" },
            { id: "TakeawaypiecePage", name: "按件拣货" },
            { id: "TakeawayPage", name: "执行拣货" },
            { id: "TakeawayinPage", name: "执行拣货" },
            { id: "TakeawayindetailedPage", name: "拣货记录" },
            { id: "TakeawaypieceboxPage", name: "按箱拣货" },
            { id: "TakeawayinboxPage", name: "执行拣货" },
            { id: "CougnyPage", name: "库内" },
            { id: "CopyLabelPage", name: "复制标签" },
            { id: "RandomCheckPage", name: "随机盘点" },
            { id: "RandomCheckTaskPage", name: "执行盘点" },
            { id: "ReplenishmenPage", name: "补货任务" },
            { id: "ReplenishmenRecordPage", name: "补货任务" },
            { id: "StockFrozenPage", name: "库存冻结" },
            { id: "CheckOrderPage", name: "执行收货" },
            { id: "UpdateDetailPage", name: "修改明细" },
            { id: "TaskwayMultPage", name: "批量拣货" },
            { id: "BindPrintterPage", name: "绑定打印机" },
            { id: "BindOrderPage", name: "单据绑定" },
            { id: "BindLpnPage", name: "绑定容器" },
            { id: "CyclecountRecordPage", name: "盘点记录" },
            { id: "MergeTrayPage", name: "拼托" }
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