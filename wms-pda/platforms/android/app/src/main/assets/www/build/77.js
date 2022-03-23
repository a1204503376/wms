webpackJsonp([77],{

/***/ 703:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AsnBillnoModalModule", function() { return AsnBillnoModalModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__skuBill_modal__ = __webpack_require__(999);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var AsnBillnoModalModule = /** @class */ (function () {
    function AsnBillnoModalModule() {
    }
    AsnBillnoModalModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__skuBill_modal__["a" /* SkuBillModal */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__skuBill_modal__["a" /* SkuBillModal */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__skuBill_modal__["a" /* SkuBillModal */]]
        })
    ], AsnBillnoModalModule);
    return AsnBillnoModalModule;
}());

//# sourceMappingURL=skuBill-modal.module.js.map

/***/ }),

/***/ 999:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SkuBillModal; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var SkuBillModal = /** @class */ (function () {
    function SkuBillModal(navCtrl, navParams, viewCtrl, storage, nativeService, appService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.viewCtrl = viewCtrl;
        this.storage = storage;
        this.nativeService = nativeService;
        this.appService = appService;
        this.result = [];
        this.result = this.navParams.get('result');
        this.resultInfo = this.result.stockPdaVOList[0];
    }
    /**
       *   页面进入
       */
    SkuBillModal.prototype.ionViewWillEnter = function () {
    };
    /**
       *   取到入库单列表
       */
    SkuBillModal.prototype.getAsnBill = function (asnType) {
    };
    /**
   * 选择要处理的入库单
   */
    SkuBillModal.prototype.headSelected = function (item) {
    };
    /**
     * 消失调用
     */
    SkuBillModal.prototype.dismiss = function () {
        this.viewCtrl.dismiss();
    };
    /**
     * 点击确定
     */
    SkuBillModal.prototype.submitdata = function () {
    };
    SkuBillModal = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-skuBill-modal',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\skuBill-modal\skuBill-modal.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>单据详情\n\n      <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <!-- <div class="nav-tile">\n\n    <ul>\n\n      <li>首页</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>发运</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>托盘发运</li>\n\n      <li>\n\n        <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n      </li>\n\n      <li>单据详情</li>\n\n    </ul>\n\n  </div> -->\n\n</ion-header>\n\n<ion-content>\n\n  <div class="title-ms-nav">\n\n    <div class="title-ms-info">\n\n      <div class="title-font">\n\n        <div><a>{{resultInfo.lpnCode}}</a></div>\n\n        <div style="color:orange;">物品种类:{{resultInfo.skuCount}}</div>\n\n        <div>暂存区:{{resultInfo.locCode}}</div>\n\n      </div>\n\n    </div>\n\n    <span class="span-top">{{resultInfo.whName}}</span>\n\n    <span class="span-bt">{{resultInfo.scanQty}}{{resultInfo.wsuName}}</span>\n\n  </div>\n\n\n\n  <div class="title-ms" *ngFor="let item of result.stockPdaVOList">\n\n    <div class="title-ms-info">\n\n      <div class="title-font">\n\n        <div>{{item.skuName}}</div>\n\n        <!-- <div>{{item.billDetailId}}</div> -->\n\n        <div>物品规格:{{item.skuSpec}}</div>\n\n        <div>{{result.address}}</div>\n\n        <div>{{result.contact}} &nbsp;{{result.phone}}</div>\n\n      </div>\n\n    </div>\n\n    <span class="span-top"> {{item.stockQty}}{{item.wsuName}} </span>\n\n    <span class="span-bt">{{item.locCode}}</span>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button ion-button block class="btn-l" (click)="dismiss()">\n\n      返回[Esc]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\skuBill-modal\skuBill-modal.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */]])
    ], SkuBillModal);
    return SkuBillModal;
}());

//# sourceMappingURL=skuBill-modal.js.map

/***/ })

});
//# sourceMappingURL=77.js.map