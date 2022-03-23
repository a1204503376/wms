webpackJsonp([16],{

/***/ 1059:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CheckintrayPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__ = __webpack_require__(825);
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
 * Generated class for the CheckintrayPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var CheckintrayPage = /** @class */ (function () {
    function CheckintrayPage(navCtrl, navParams, appService, nativeService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.skuPackageDetailsItem = {}; //包装管理选中
        this.isSn = 0; //是否为序列号管理 1,序列号管理  0,,非序列号管理
        this.serialNumber = []; //序列号已扫描集合
        this.InNumberLength = 0; //已收序列号数量
        this.ScanNumberLength = 0; //已扫描序列号数量
        this.isTilebool = true;
        this.NumberListFalag = 1; //扫描标识 1：已扫描 2:已收货
        this.lotArray = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
        this.locCode = "STAGE"; //库位
        this.isShow = false;
    }
    CheckintrayPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad CheckintrayPage');
    };
    /**
     * LPN输入框按下回车输入焦点移动
     */
    CheckintrayPage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
                this.nativeService.showToast('请输入容器编码');
                return;
            }
            this.getData(this.lpnCode);
        }
    };
    CheckintrayPage.prototype.getData = function (lpnCode) {
        var _this = this;
        var params = {
            lpnCode: lpnCode
        };
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].GetAsnLpnDetail, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('当前信息不存在');
            }
            else {
                console.log(result.data);
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.asnLpnDetail)) {
                    _this.lpnDetail = result.data.asnLpnDetail;
                    _this.asnLpnId = _this.lpnDetail.asnLpnId;
                    _this.lpnCode = _this.lpnDetail.lpnCode;
                    _this.lpnStatus = _this.lpnDetail.lpnStatus;
                    _this.asnBillId = _this.lpnDetail.asnBillId;
                    _this.asnDetailId = _this.lpnDetail.asnDetailId;
                    _this.wspId = _this.lpnDetail.wspId;
                    _this.skuSpec = _this.lpnDetail.skuSpec;
                    _this.lpnQty = _this.lpnDetail.lpnQty;
                    _this.umName = _this.lpnDetail.umName;
                }
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.asnHeader)) {
                    _this.asnHeader = result.data.asnHeader;
                    _this.whId = _this.asnHeader.whId;
                    _this.whName = _this.asnHeader.whName;
                    _this.sname = _this.asnHeader.sName;
                    _this.ownerName = _this.asnHeader.ownerName;
                    _this.asnBillState = _this.asnHeader.asnBillState;
                    _this.asnBillNo = _this.asnHeader.asnBillNo;
                    _this.billType = _this.asnHeader.billType;
                }
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.asnDetails)) {
                    _this.planQty = result.data.asnDetail.planCountQty;
                    _this.scanQty = result.data.asnDetail.scanCountQty;
                    _this.asnDetails = result.data.asnDetails[0];
                    //获取30个批属性的值
                    for (var index = 1; index <= 30; index++) {
                        var lotkey = 'skuLot' + index;
                        var lotval = _this.asnDetails[lotkey];
                        _this.lotArray[lotkey] = lotval;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.skuPackageDetails)) {
                        _this.skuPackageDetails = result.data.skuPackageDetails;
                        var pageUm = _this.skuPackageDetails.filter(function (x) { return x.wsuCode == _this.asnDetails.umCode; });
                        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.skuPackageDetails.filter(function (x) { return x.wsuCode == _this.asnDetails.umCode; }))) {
                            _this.skuPackageDetailsItem = pageUm[0];
                        }
                    }
                }
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.sku)) {
                    _this.sku = result.data.sku;
                    _this.skuId = _this.sku.skuId;
                    _this.skuName = _this.sku.skuName;
                    _this.isSn = _this.sku.isSn;
                }
                _this.isShow = true;
            }
        });
    };
    /**
    * 查看序列号
    */
    CheckintrayPage.prototype.numclick = function () {
        this.isSn = 3;
    };
    /**
    * 收货提交
    */
    CheckintrayPage.prototype.btnOk = function () {
        var _this = this;
        var arrStr = '';
        if (this.sku == null) {
            this.nativeService.showToast('请先获取物品信息');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.locCode)) {
            this.nativeService.showToast('请填写库位');
            return;
        }
        if (this.isSn == 3) {
            this.isSn = 1;
        }
        if (this.isSn == 1) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.serialNumber)) {
                this.nativeService.showToast('没有扫描序列号');
                return;
            }
            this.serialNumber.forEach(function (element) {
                arrStr += element + ',';
            });
            arrStr = arrStr.substring(0, arrStr.length - 1);
            if (this.serialNumber.length < this.lpnQty) {
                this.nativeService.showToast('该托盘物品序列号未全部扫描');
                return;
            }
        }
        var params = {
            skuId: this.sku.skuId,
            skuCode: this.sku.skuCode,
            isSn: this.sku.isSn,
            planCountQty: this.planQty,
            scanCountQty: this.scanQty,
            wspId: this.wspId,
            wspdId: this.skuPackageDetailsItem.wspdId,
            snDetailCode: arrStr,
            asnLpnId: this.asnLpnId,
            lpnCode: this.lpnCode,
            asnBillNo: this.asnBillNo,
            scanQty: this.lpnQty,
            locCode: this.locCode,
            whId: this.whId,
            asnDetailSkuLotParam: this.lotArray,
            asnBillId: this.asnBillId,
            asnDetailId: this.asnDetailId
        };
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Instock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockAddByLPN, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                //this.getData(this.lpnCode);
                _this.nativeService.showToast('提交成功');
                _this.clearModel();
                _this.isShow = false;
            }
        });
    };
    /*返回*/
    CheckintrayPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    /**
     * 对象初始化
     */
    CheckintrayPage.prototype.clearModel = function () {
        this.lotArray = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
        this.lpnDetail = null;
        this.lpnCode = "";
        this.asnBillId = "";
        this.skuSpec = "";
        this.lpnQty = 0;
        this.umName = "";
        this.asnHeader = null;
        this.whName = "";
        this.sname = "";
        this.ownerName = "";
        this.asnBillState = 0;
        this.asnBillNo = "";
        this.billType = "";
        this.sku = null;
        this.skuName = "";
        this.isSn = 0; //是否为序列号管理 1,序列号管理  0,,非序列号管理
        this.planQty = "";
        this.scanQty = "";
        this.wspId = "";
        this.skuPackageDetailsItem = null;
        this.asnLpnId = "";
        this.locCode = "STAGE";
        this.whId = "";
        this.asnDetailId = "";
    };
    CheckintrayPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-checkintray',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkintray\checkintray.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title (click)="expression()">\n\n      按托收货\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <ion-searchbar [(ngModel)]="lpnCode" (keyup)="keyEnter($event)" Description=\'primary\' placeholder="LPN编码">\n\n    <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n  </ion-searchbar>\n\n  <!-- <div class="nav-tile">\n\n    <ul>\n\n      <li>首页</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>按托收货</li>\n\n    </ul>\n\n  </div> -->\n\n  <div class="nav-tile"> \n\n  <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n<ion-content class="no-scroll">\n\n  <div class="title-ms" *ngIf="isShow">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div class="title-font-s2">\n\n          <label>{{asnBillNo}}</label>\n\n        </div>\n\n        <div class="ware_house">\n\n          {{whName}}\n\n        </div>\n\n        <div class="title-font-s3">\n\n          <label>收货规格:</label>\n\n          <label style="color: black;">{{skuSpec}}</label>\n\n        </div>\n\n        <div>\n\n          <label>状&emsp;&emsp;态:</label>\n\n          <label *ngIf="lpnStatus==\'1\'" style="color: black;">未接收</label>\n\n          <label *ngIf="lpnStatus==\'2\'" style="color: black;">已接收</label>\n\n        </div>\n\n        <div>\n\n          <label>{{sname}}</label>\n\n        </div>\n\n        <div>\n\n          <label>{{ownerName}}</label>\n\n        </div>\n\n        <div class="received_lpn">\n\n          LPN: {{lpnCode}}\n\n        </div>\n\n        <div class="received_num">\n\n          数量: {{lpnQty}}{{umName}}\n\n        </div>\n\n        <!-- <div class="ionic_right">\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </div> -->\n\n      </div>\n\n    </div>\n\n  </div>\n\n\n\n  <div class="list_item" *ngIf="isShow">\n\n    <ion-item>\n\n      <ion-label>库&emsp;&ensp;位：</ion-label>\n\n      <ion-input [(ngModel)]="locCode" type="text"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <!-- <ion-item *ngIf="isSn===1">\n\n      <ion-label>序列号：</ion-label>\n\n      <ion-input (keyup)="serialNumber_KeyDown($event)" maxlength=\'50\' [(ngModel)]="serialNumberMode" type="text">\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item> -->\n\n    <!-- <ion-item>\n\n      <ion-label>批属性1：</ion-label>\n\n      <ion-input type="text" (blur)="scrollToTop()"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>批属性2：</ion-label>\n\n      <ion-input type="text" (blur)="scrollToTop()"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item> -->\n\n  </div>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button block class="btn-l" (click)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button ion-button block class="btn-d" (click)="btnOk()">\n\n      提交[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkintray\checkintray.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_4_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_4_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */]])
    ], CheckintrayPage);
    return CheckintrayPage;
}());

//# sourceMappingURL=checkintray.js.map

/***/ }),

/***/ 759:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CheckintrayPageModule", function() { return CheckintrayPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__checkintray__ = __webpack_require__(1059);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var CheckintrayPageModule = /** @class */ (function () {
    function CheckintrayPageModule() {
    }
    CheckintrayPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__checkintray__["a" /* CheckintrayPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__checkintray__["a" /* CheckintrayPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], CheckintrayPageModule);
    return CheckintrayPageModule;
}());

//# sourceMappingURL=checkintray.module.js.map

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

/***/ 825:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return SkuLotValResultModel; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SkuLot; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__utils_appConstant__ = __webpack_require__(65);


var SkuLotValResultModel = /** @class */ (function () {
    function SkuLotValResultModel() {
        this.getLotEditVal();
    }
    SkuLotValResultModel.prototype.getLotEditVal = function () {
        if (__WEBPACK_IMPORTED_MODULE_0__services_Utils__["a" /* Utils */].isNotEmpty(this.skuLotMixMask)) {
            if (this.skuLotMixMask.indexOf('YYYY') != -1 || this.skuLotMixMask.indexOf('yyyy') != -1) {
                return this.CtlLotEditVal = __WEBPACK_IMPORTED_MODULE_1__utils_appConstant__["c" /* AppConstant */].Date;
            }
            else {
                return this.CtlLotEditVal = __WEBPACK_IMPORTED_MODULE_1__utils_appConstant__["c" /* AppConstant */].Text;
            }
        }
    };
    return SkuLotValResultModel;
}());

var SkuLot = /** @class */ (function () {
    function SkuLot() {
    }
    return SkuLot;
}());

//# sourceMappingURL=SkuLotValResultModel.js.map

/***/ })

});
//# sourceMappingURL=16.js.map