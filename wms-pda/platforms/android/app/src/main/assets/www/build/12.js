webpackJsonp([12,68],{

/***/ 1058:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckinBoxInfoPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__ = __webpack_require__(158);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__ = __webpack_require__(360);
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
 * Generated class for the CheckinBoxInfoPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckinBoxInfoPage = /** @class */ (function () {
    function CheckinBoxInfoPage(navCtrl, navParams, appService, nativeService, barcodeService, events, ngZone) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.scanModelFlag = true; //二维码状态
        this.EidtFlag = true; //箱码是否可编辑
        this.isEidtFlag = true; //箱码是否显示
        this.totalPlanQty = 0;
        this.totalScanQty = 0;
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */]();
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_8__models_DataBase_Model__["a" /* ScanModel */](); //箱码解析实体
        this.scanModelRecords = [];
        this.skuPackageDetails = [];
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.navParams.get('result'))) {
            this.result = this.navParams.get('result');
            this.dataFilling();
        }
    }
    CheckinBoxInfoPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        this.labelTypecom.qtyDisabled = false;
        //事件注册（扫描结果接收）
        if (__WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].scanFlag) {
            __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].scanFlag = false;
            this.events.subscribe('barcode:scan', function (barcode, time) {
                _this.analysisDate(barcode);
            });
        }
    };
    CheckinBoxInfoPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
   * 数据解析
   */
    CheckinBoxInfoPage.prototype.analysisDate = function (barcode) {
        var _this = this;
        var bt = this.barcodeService.GetBarcodeType(barcode);
        if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].BoxCode) {
            this.ngZone.run(function () {
                _this.scanWmsSku(barcode);
            });
        }
        if (bt == __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
            this.ngZone.run(function () {
                _this.labelTypecom.locCode = barcode;
            });
        }
    };
    CheckinBoxInfoPage.prototype.dataFilling = function () {
        this.orderNo = this.result.orderNo;
        this.asnBillNo = this.result.asnBillNo;
        this.sname = this.result.sname;
        this.planCountQty = this.result.planCountQty;
        this.scanCountQty = this.result.scanCountQty;
        this.finish = this.result.finish;
        this.count = this.result.count;
        this.ruleCode = this.result.ruleCode;
        this.boxCode = this.result.boxCode;
        //箱码状态
        if (this.ruleCode == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["c" /* AppConstant */].ruleCode_00) {
            this.EidtFlag = false;
        }
        else if (this.ruleCode == __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["c" /* AppConstant */].ruleCode_01) {
            this.EidtFlag = true;
        }
        else {
            this.isEidtFlag = false;
        }
    };
    CheckinBoxInfoPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad CheckinBoxInfoPage');
    };
    CheckinBoxInfoPage.prototype.scanModelChange = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView)) {
                this.scanWmsSku(this.scanView);
            }
            else {
                this.nativeService.showToast('请扫描箱码');
            }
        }
    };
    /**
     * 检测物品信息
     * @param scanView
     */
    CheckinBoxInfoPage.prototype.scanWmsSku = function (scanView) {
        var _this = this;
        //箱码解析
        this.barcodeService.ScanSkuBarcode(scanView).subscribe(function (val) {
            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(val.skucode)) {
                _this.nativeService.showToast('物品编码不能为空');
            }
            var params = {
                skuCode: val.skucode
            };
            //获取物品
            _this.nativeService.showLoading();
            _this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                _this.nativeService.hideLoading();
                __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].scanFlag = true;
                //判断是否多个物品
                if (result.data.length >= 1) {
                    //基础计量单位
                    var levelUm = result.data[0].skuPackage.skuPackageDetailVOList.find(function (x) { return x.skuLevel == 1; });
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(levelUm)) {
                        _this.nativeService.showToast('没有检测到当前物品的基础计量单位');
                        return;
                    }
                    _this.getAsnHeaderDetailForBox(result.data[0], val, levelUm);
                }
                else {
                    _this.nativeService.showToast('没有检测到物品信息');
                }
            });
        });
    };
    /**
     *根据箱码数据获取数据详情
     */
    CheckinBoxInfoPage.prototype.getAsnHeaderDetailForBox = function (data, model, levelUm) {
        var _this = this;
        this.scanModel = model;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(model)) {
            var skuLotModel = this.barcodeService.convertLotModel(model);
            var param = {
                asnBillNo: this.asnBillNo,
                skuCode: model.skucode,
                skuName: model.skuname,
                wsuName: levelUm.wsuName,
                skuLots: skuLotModel
            };
            this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getAsnHeaderDetailForBox, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, param, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
                _this.nativeService.hideLoading();
                if (!result.success) {
                    _this.nativeService.showToast(result.msg);
                }
                else {
                    _this.asnDetails = result.data.asnDetails[0];
                    _this.asnInfo = result.data.asnDetail;
                    _this.sku = result.data.sku;
                    _this.totalPlanQty = result.data.totalPlanQty;
                    _this.totalScanQty = result.data.totalScanQty;
                    _this.planQty = _this.asnInfo.planCountQty;
                    _this.scanQty = _this.asnInfo.scanCountQty;
                    _this.planQtyName = _this.asnInfo.planQtyName;
                    _this.scanQtyName = _this.asnInfo.scanQtyName;
                    ;
                    _this.skuName = _this.asnInfo.skuName;
                    _this.scanQty = _this.asnInfo.scanCountQty;
                    _this.detailStatus = _this.asnInfo.detailStatusName;
                    _this.skuPackageDetails = result.data.skuPackageDetails;
                    //绑定批属性
                    result.data.skuConfig.forEach(function (element) {
                        model.skuLotModel["skuLotName" + element.skuLotIndex] = element.skuLotLabel;
                    });
                    model.um = levelUm.wsuName;
                    _this.skuSpec = levelUm.skuSpec;
                    if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(_this.scanModelRecords)) {
                        _this.scanModelRecords.push(model);
                    }
                    else {
                        for (var _i = 0, _a = _this.scanModelRecords; _i < _a.length; _i++) {
                            var item = _a[_i];
                            if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isObjectValueEqual1(model, item, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                                item.qty = Number.parseInt(item.qty) + Number.parseInt(model.qty);
                                model = item;
                            }
                            else {
                                if (!__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].contains(_this.scanModelRecords, model, ['skuname', 'qty', 'setLotModel', 'setLotValue'])) {
                                    _this.scanModelRecords.push(model);
                                    break;
                                }
                            }
                        }
                    }
                    _this.labelTypecom.changeRun(model);
                }
            });
        }
    };
    CheckinBoxInfoPage.prototype.runParent = function (asnInfo) {
        this.scanModelFlag = false;
    };
    CheckinBoxInfoPage.prototype.detailed = function () {
        this.navCtrl.push('CheckinDetailedPage', { asnBillNo: this.asnBillNo });
    };
    /**
     * 收货提交
     */
    CheckinBoxInfoPage.prototype.btnOk = function () {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isEmpty(this.asnInfo)) {
            this.nativeService.showToast('没有扫描物品信息');
            return;
        }
        var params = {
            asnBillNo: this.asnBillNo,
            whId: this.sku.whId,
            asnBillId: this.asnDetails.asnBillId,
            asnDetailId: this.asnDetails.asnDetailId,
            skuCode: this.labelTypecom.scanModel.skucode,
            scanQty: this.labelTypecom.scanModel.qty,
            wsuName: this.labelTypecom.scanModel.um,
            locCode: this.labelTypecom.locCode,
            skuLots: this.barcodeService.convertLotModel(this.labelTypecom.scanModel),
            boxCode: this.boxCode,
            lpnCode: " "
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitAsnHeaderForBox, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            _this.nativeService.showToast('提交成功');
            _this.clearModel();
            _this.finish = result.data.asnDetail.finish;
            _this.count = result.data.asnDetail.count;
            if (_this.finish == _this.count) {
                _this.navCtrl.getPrevious().data.flag = true;
                _this.navCtrl.pop();
            }
            else {
                //更新箱号
                _this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getBoxCode, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, '', __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Content, function (result) {
                    _this.boxCode = result.data;
                });
            }
        });
    };
    /**
     * 数据初始化
     */
    CheckinBoxInfoPage.prototype.clearModel = function () {
        this.totalPlanQty = 0;
        this.totalScanQty = 0;
        this.planQty = ''; //单据明细计划
        this.scanQty = ''; //单据明细已完成
        this.planQtyName = '';
        this.scanQtyName = '';
        this.skuCode = ''; //物品编号
        this.skuName = ''; //物品名称
        this.skuSpec = ''; //物品规格
        this.detailStatus = ''; //接受状态 10:未接收,20:已接收
        this.sku = null; //物品信息
        this.labelTypecom.clearModel();
        this.scanModelFlag = true;
        this.scanModel = null;
        this.scanView = '';
        this.scanModelRecords = [];
    };
    /**
     * 退出
     */
    CheckinBoxInfoPage.prototype.exit = function () {
        if (this.labelTypecom.ComponentFlag) {
            this.labelTypecom.ComponentFlag = false;
            this.clearModel();
        }
        else {
            this.navCtrl.pop();
        }
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])('labelTypecom'),
        __metadata("design:type", Object)
    ], CheckinBoxInfoPage.prototype, "labelTypecom", void 0);
    CheckinBoxInfoPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-checkin-box-info',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkinbox\checkin-box-info\checkin-box-info.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      按箱收货<div class="title_f_name">({{result.whName}})</div>\n\n    </ion-title>\n\n    <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="order">\n\n      <div class="order-number">{{orderNo||asnBillNo}}</div>\n\n      <div class="order-info">单据收货进度 {{finish}}/{{count}}</div>\n\n    </div>\n\n    <!-- <div class="nav-tile clearfix">\n\n      <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>收货</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>按箱收货</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>执行收货</li>\n\n      </ul>\n\n    </div> -->\n\n    <div class="nav-tile"> \n\n    <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content no-bounce>\n\n  <div class="title-ms clearfix">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div *ngIf="skuSpec!=\'\'&& skuSpec!=null" class="title-font-s2">\n\n          <label>收货规格：</label>\n\n          <label style="color: black;">{{skuSpec}}</label>\n\n        </div>\n\n        <div *ngIf="detailStatus!=\'\'&& detailStatus!=null" class="title-font-s3">\n\n          <label>状 态:</label>\n\n          <label style="color: black;">{{detailStatus}}</label>\n\n        </div>\n\n        <div class="title-font-s4">\n\n          <label>{{sname}}</label>\n\n        </div>\n\n      </div>\n\n      <div *ngIf="scanQtyName!=\'\'&& scanQtyName!=null" class="received-scan">\n\n        已接收: <span style="color: #F59A23;"> {{scanQtyName ==\'\'?\'0\':scanQtyName}}</span>\n\n      </div>\n\n      <div *ngIf="planQtyName!=\'\'&& planQtyName!=null" class="received-plan">\n\n        计划量: <span style="color: #F59A23;"> {{planQtyName ==\'\'?\'0\':planQtyName}}</span>\n\n      </div>\n\n      <div *ngIf="totalPlanQty>0" class="received-total">\n\n        <span style="color: #F59A23;"> {{totalScanQty}}/{{totalPlanQty}}</span>\n\n      </div>\n\n    </div>\n\n  </div>\n\n  <div class="list_item">\n\n    <ion-item *ngIf="isEidtFlag">\n\n      <ion-label>箱号：</ion-label>\n\n      <ion-input [disabled]="EidtFlag" maxlength=\'30\' type="text" [(ngModel)]="boxCode"></ion-input>\n\n      <ion-avatar *ngIf="!EidtFlag" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="scanModelFlag">\n\n      <ion-label>箱码：</ion-label>\n\n      <ion-input (keyup)="scanModelChange($event)" type="text" [(ngModel)]="scanView"></ion-input>\n\n\n\n    </ion-item>\n\n  </div>\n\n\n\n  <page-check-in-box-label-type [asnBillNo]=\'asnBillNo\' [ruleCode]=\'ruleCode\' (outer)="runParent($event)" #labelTypecom>\n\n\n\n  </page-check-in-box-label-type>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      提交[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkinbox\checkin-box-info\checkin-box-info.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_7__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], CheckinBoxInfoPage);
    return CheckinBoxInfoPage;
}());

//# sourceMappingURL=checkin-box-info.js.map

/***/ }),

/***/ 701:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckInBoxLabelTypePageModule", function() { return CheckInBoxLabelTypePageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__ = __webpack_require__(792);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var CheckInBoxLabelTypePageModule = /** @class */ (function () {
    function CheckInBoxLabelTypePageModule() {
    }
    CheckInBoxLabelTypePageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */]),
            ],
            exports: [
                __WEBPACK_IMPORTED_MODULE_2__check_in_box_label_type__["a" /* CheckInBoxLabelTypePage */]
            ]
        })
    ], CheckInBoxLabelTypePageModule);
    return CheckInBoxLabelTypePageModule;
}());

//# sourceMappingURL=check-in-box-label-type.module.js.map

/***/ }),

/***/ 758:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckinBoxInfoPageModule", function() { return CheckinBoxInfoPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__checkin_box_info__ = __webpack_require__(1058);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_components_module__ = __webpack_require__(362);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__shared_check_in_box_label_type_check_in_box_label_type_module__ = __webpack_require__(701);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};






var CheckinBoxInfoPageModule = /** @class */ (function () {
    function CheckinBoxInfoPageModule() {
    }
    CheckinBoxInfoPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__checkin_box_info__["a" /* CheckinBoxInfoPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_3__components_components_module__["a" /* ComponentsModule */],
                __WEBPACK_IMPORTED_MODULE_4__shared_check_in_box_label_type_check_in_box_label_type_module__["CheckInBoxLabelTypePageModule"],
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__checkin_box_info__["a" /* CheckinBoxInfoPage */]),
                __WEBPACK_IMPORTED_MODULE_5__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CheckinBoxInfoPageModule);
    return CheckinBoxInfoPageModule;
}());

//# sourceMappingURL=checkin-box-info.module.js.map

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

/***/ }),

/***/ 792:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckInBoxLabelTypePage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__ = __webpack_require__(158);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__ = __webpack_require__(361);
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
 * Generated class for the CheckInBoxLabelTypePage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckInBoxLabelTypePage = /** @class */ (function () {
    function CheckInBoxLabelTypePage(navCtrl, navParams, nativeService, appService, barcodeService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.barcodeService = barcodeService;
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */](); //批属性对象
        this.ComponentFlag = true; //组件显示
        this.EidtFlag = true; //是否可编辑
        this.qtyDisabled = true; //数量是否可编辑
        this.locDisabled = false; //库位是否可编辑
        this.locCode = 'STAGE'; //库位编码
        this.locCodeFlag = true; //库位显隐
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_7__models_DataBase_Model__["a" /* ScanModel */](); //箱码解析实体
        this.isCopyLabel = false;
        this.outer = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
        this.labelType = __WEBPACK_IMPORTED_MODULE_2__utils_appConstant__["c" /* AppConstant */].labelType;
        console.log('constructor CheckInBoxLabelTypePage');
    }
    CheckInBoxLabelTypePage.prototype.ionViewDidLoad = function () {
        console.log('子组件---ionViewDidLoad---CheckInBoxLabelTypePage');
    };
    CheckInBoxLabelTypePage.prototype.ionViewWillLeave = function () {
        console.log('子组件---ionViewWillLeave---CheckInBoxLabelTypePage');
    };
    /**
     * 初始化数据
     * @param scan
     */
    CheckInBoxLabelTypePage.prototype.changeRun = function (scan) {
        //解析条码规则
        if (__WEBPACK_IMPORTED_MODULE_3__services_Utils__["a" /* Utils */].isNotEmpty(scan)) {
            this.ComponentFlag = true;
            this.scanModel = scan;
            this.outer.emit('');
        }
    };
    /**
     * 数据初始化
     */
    CheckInBoxLabelTypePage.prototype.clearModel = function () {
        this.skuCode = '';
        this.scanQty = 0;
        this.wsuName = '';
        this.instockDate = null;
        this.produceDate = null;
        this.typeCode = '';
        this.lotNumber = '';
        this.version = '';
        this.remarks = '';
        this.type = '';
        this.labelCode = '';
        this.locCode = 'STAGE';
        this.skuLotModel = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotModel__["a" /* SkuLotModel */]();
        this.ComponentFlag = false;
    };
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
        __metadata("design:type", Object)
    ], CheckInBoxLabelTypePage.prototype, "outer", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
        __metadata("design:type", Object)
    ], CheckInBoxLabelTypePage.prototype, "ruleCode", void 0);
    __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
        __metadata("design:type", String)
    ], CheckInBoxLabelTypePage.prototype, "asnBillNo", void 0);
    CheckInBoxLabelTypePage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-check-in-box-label-type',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\check-in-box-label-type\check-in-box-label-type.html"*/'<div *ngIf="ComponentFlag">\n\n  <ion-item *ngIf="locCodeFlag">\n\n    <ion-label>库位编码：</ion-label>\n\n    <ion-input [disabled]=\'locDisabled\' maxlength=\'30\' type="text" [(ngModel)]="locCode">\n\n    </ion-input>\n\n  </ion-item>\n\n  <div class="list_item">\n\n    <ion-item *ngIf="skuCodeFlag">\n\n      <ion-label>物品编码：</ion-label>\n\n      <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skucode"></ion-input>\n\n    </ion-item>\n\n  </div>\n\n  <ion-item *ngIf="skuNameFlag">\n\n    <ion-label>物品名称：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuname">\n\n    </ion-input>\n\n  </ion-item>\n\n  <ion-item>\n\n    <ion-label>数量：</ion-label>\n\n    <ion-input [disabled]=\'qtyDisabled\' maxlength=\'30\' type="number" [(ngModel)]="scanModel.qty">\n\n    </ion-input>\n\n    <ion-select [disabled]=\'EidtFlag\' [(ngModel)]="scanModel.um" cancelText="取消">\n\n      <ion-option [value]="scanModel.um">{{scanModel.um}}</ion-option>\n\n    </ion-select>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName1!=null &&scanModel.skuLotModel.skulot1!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName1}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot1">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName2!=null &&scanModel.skuLotModel.skulot2!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName2}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot2">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName3!=null &&scanModel.skuLotModel.skulot3!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName3}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot3">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName4!=null &&scanModel.skuLotModel.skulot4!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName4}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot4">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName5!=null&&scanModel.skuLotModel.skuLotName5!=\'\' &&scanModel.skuLotModel.skulot5!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName5}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot5">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName6!=null &&scanModel.skuLotModel.skulot6!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName6}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot6">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName7!=null &&scanModel.skuLotModel.skulot7!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName7}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot7">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName8!=null &&scanModel.skuLotModel.skulot8!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotNam8}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot8">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName9!=null &&scanModel.skuLotModel.skulot9!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName9}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot9">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="!isCopyLabel&&scanModel.skuLotModel.skuLotName10!=null &&scanModel.skuLotModel.skulot10!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName10}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot10">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName1!=null &&scanModel.skuLotModel.skuLotName1!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName1}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot1">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName2!=null &&scanModel.skuLotModel.skuLotName2!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName2}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot2">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName3!=null &&scanModel.skuLotModel.skuLotName3!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName3}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot3">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName4!=null &&scanModel.skuLotModel.skuLotName4!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName4}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot4">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName5!=null &&scanModel.skuLotModel.skuLotName5!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName5}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot5">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName6!=null &&scanModel.skuLotModel.skuLotName6!=\'\' ">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName6}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot6">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName7!=null &&scanModel.skuLotModel.skuLotName7!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName7}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot7">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName8!=null &&scanModel.skuLotModel.skuLotName8!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotNam8}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot8">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName9!=null &&scanModel.skuLotModel.skuLotName9!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName9}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot9">\n\n    </ion-input>\n\n  </ion-item>\n\n\n\n  \n\n  <ion-item *ngIf="isCopyLabel&&scanModel.skuLotModel.skuLotName10!=null &&scanModel.skuLotModel.skuLotName10!=\'\'">\n\n    <ion-label>{{scanModel.skuLotModel.skuLotName10}}：</ion-label>\n\n    <ion-input [disabled]=\'EidtFlag\' maxlength=\'30\' type="text" [(ngModel)]="scanModel.skuLotModel.skulot10">\n\n    </ion-input>\n\n  </ion-item>\n\n</div>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\shared\check-in-box-label-type\check-in-box-label-type.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_4__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_5__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["a" /* BarcodeService */]])
    ], CheckInBoxLabelTypePage);
    return CheckInBoxLabelTypePage;
}());

//# sourceMappingURL=check-in-box-label-type.js.map

/***/ })

});
//# sourceMappingURL=12.js.map