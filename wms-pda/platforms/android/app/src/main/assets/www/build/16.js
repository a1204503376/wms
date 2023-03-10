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
        this.skuPackageDetailsItem = {}; //??????????????????
        this.isSn = 0; //???????????????????????? 1,???????????????  0,,??????????????????
        this.serialNumber = []; //????????????????????????
        this.InNumberLength = 0; //?????????????????????
        this.ScanNumberLength = 0; //????????????????????????
        this.isTilebool = true;
        this.NumberListFalag = 1; //???????????? 1???????????? 2:?????????
        this.lotArray = new __WEBPACK_IMPORTED_MODULE_6__models_SkuLotValResultModel__["a" /* SkuLot */]();
        this.locCode = "STAGE"; //??????
        this.isShow = false;
    }
    CheckintrayPage.prototype.ionViewDidLoad = function () {
        console.log('ionViewDidLoad CheckintrayPage');
    };
    /**
     * LPN???????????????????????????????????????
     */
    CheckintrayPage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.lpnCode)) {
                this.nativeService.showToast('?????????????????????');
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
                _this.nativeService.showToast('?????????????????????');
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
                    //??????30??????????????????
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
    * ???????????????
    */
    CheckintrayPage.prototype.numclick = function () {
        this.isSn = 3;
    };
    /**
    * ????????????
    */
    CheckintrayPage.prototype.btnOk = function () {
        var _this = this;
        var arrStr = '';
        if (this.sku == null) {
            this.nativeService.showToast('????????????????????????');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.locCode)) {
            this.nativeService.showToast('???????????????');
            return;
        }
        if (this.isSn == 3) {
            this.isSn = 1;
        }
        if (this.isSn == 1) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.serialNumber)) {
                this.nativeService.showToast('?????????????????????');
                return;
            }
            this.serialNumber.forEach(function (element) {
                arrStr += element + ',';
            });
            arrStr = arrStr.substring(0, arrStr.length - 1);
            if (this.serialNumber.length < this.lpnQty) {
                this.nativeService.showToast('???????????????????????????????????????');
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
                _this.nativeService.showToast('????????????');
                _this.clearModel();
                _this.isShow = false;
            }
        });
    };
    /*??????*/
    CheckintrayPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    /**
     * ???????????????
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
        this.isSn = 0; //???????????????????????? 1,???????????????  0,,??????????????????
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
            selector: 'page-checkintray',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkintray\checkintray.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title (click)="expression()">\n\n      ????????????\n\n      <!-- <div class="title_f_name">(????????????)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <ion-searchbar [(ngModel)]="lpnCode" (keyup)="keyEnter($event)" Description=\'primary\' placeholder="LPN??????">\n\n    <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n  </ion-searchbar>\n\n  <!-- <div class="nav-tile">\n\n    <ul>\n\n      <li>??????</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>????????????</li>\n\n    </ul>\n\n  </div> -->\n\n  <div class="nav-tile"> \n\n  <crumbs></crumbs>\n\n  </div>\n\n</ion-header>\n\n<ion-content class="no-scroll">\n\n  <div class="title-ms" *ngIf="isShow">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div class="title-font-s2">\n\n          <label>{{asnBillNo}}</label>\n\n        </div>\n\n        <div class="ware_house">\n\n          {{whName}}\n\n        </div>\n\n        <div class="title-font-s3">\n\n          <label>????????????:</label>\n\n          <label style="color: black;">{{skuSpec}}</label>\n\n        </div>\n\n        <div>\n\n          <label>???&emsp;&emsp;???:</label>\n\n          <label *ngIf="lpnStatus==\'1\'" style="color: black;">?????????</label>\n\n          <label *ngIf="lpnStatus==\'2\'" style="color: black;">?????????</label>\n\n        </div>\n\n        <div>\n\n          <label>{{sname}}</label>\n\n        </div>\n\n        <div>\n\n          <label>{{ownerName}}</label>\n\n        </div>\n\n        <div class="received_lpn">\n\n          LPN: {{lpnCode}}\n\n        </div>\n\n        <div class="received_num">\n\n          ??????: {{lpnQty}}{{umName}}\n\n        </div>\n\n        <!-- <div class="ionic_right">\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </div> -->\n\n      </div>\n\n    </div>\n\n  </div>\n\n\n\n  <div class="list_item" *ngIf="isShow">\n\n    <ion-item>\n\n      <ion-label>???&emsp;&ensp;??????</ion-label>\n\n      <ion-input [(ngModel)]="locCode" type="text"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <!-- <ion-item *ngIf="isSn===1">\n\n      <ion-label>????????????</ion-label>\n\n      <ion-input (keyup)="serialNumber_KeyDown($event)" maxlength=\'50\' [(ngModel)]="serialNumberMode" type="text">\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item> -->\n\n    <!-- <ion-item>\n\n      <ion-label>?????????1???</ion-label>\n\n      <ion-input type="text" (blur)="scrollToTop()"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>?????????2???</ion-label>\n\n      <ion-input type="text" (blur)="scrollToTop()"></ion-input>\n\n      <ion-avatar item-end (click)="setServerIp()">\n\n        <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item> -->\n\n  </div>\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button block class="btn-l" (click)="exit()">\n\n      ??????[Esc]\n\n    </button>\n\n    <button ion-button block class="btn-d" (click)="btnOk()">\n\n      ??????[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\checkinhome\checkintray\checkintray.html"*/,
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