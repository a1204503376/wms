webpackJsonp([66],{

/***/ 1000:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SkuListModal; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_SkuModel__ = __webpack_require__(1001);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};







var SkuListModal = /** @class */ (function () {
    function SkuListModal(navCtrl, navParams, viewCtrl, storage, nativeService, appService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.viewCtrl = viewCtrl;
        this.storage = storage;
        this.nativeService = nativeService;
        this.appService = appService;
        //收货单列表
        this.asnBill = [];
        this.skuList = [];
        this.skuListItem = new __WEBPACK_IMPORTED_MODULE_6__models_SkuModel__["a" /* SkuModel */]();
        this.skuList = this.navParams.get('skuList');
    }
    /**
       *   页面进入
       */
    SkuListModal.prototype.ionViewWillEnter = function () {
        //入库单列表取得
        this.getAsnBill(this.asnType);
    };
    /**
       *   取到入库单列表
       */
    SkuListModal.prototype.getAsnBill = function (asnType) {
    };
    /**
   * 选择要处理的入库单
   */
    SkuListModal.prototype.headSelected = function (item) {
        this.skuListItem = item;
    };
    /**
     * 消失调用
     */
    SkuListModal.prototype.dismiss = function () {
        this.viewCtrl.dismiss();
    };
    /**
     * 点击确定
     */
    SkuListModal.prototype.submitdata = function () {
        if (__WEBPACK_IMPORTED_MODULE_5__services_Utils__["a" /* Utils */].isEmpty(this.skuListItem)) {
            this.nativeService.showToast("请选择需要执行的物品!");
            return;
        }
        this.viewCtrl.dismiss(this.skuListItem);
    };
    SkuListModal = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-skulist-modal',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\skulist-modal\skulist-modal.html"*/'<ion-content>\n\n  <ion-list>\n\n    <ion-item-group>\n\n      物品选择\n\n    </ion-item-group>\n\n    <ion-item-group>\n\n      <ion-scroll zooming="true" scrollY="true">\n\n        <ion-item *ngFor="let item of skuList" (click)="headSelected(item)"\n\n          [ngClass]="{selected:skuListItem.skuId == item.skuId}">\n\n          <h2><b>{{item.skuName}}</b></h2>\n\n          <span>{{item.ownerName}}</span><span>{{item.skuId}}</span>\n\n          <div *ngIf="item.skuSpec!=null&&item.wspName!=null" class="owner-div">\n\n            <span>{{item.skuSpec}}</span><span>{{item.wspName}}</span></div>\n\n        </ion-item>\n\n      </ion-scroll>\n\n    </ion-item-group>\n\n    <ion-item-group>\n\n      <button ion-button (click)="dismiss()">取消</button>\n\n      <button ion-button (click)="submitdata()">确认</button>\n\n    </ion-item-group>\n\n  </ion-list>\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\skulist-modal\skulist-modal.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */]])
    ], SkuListModal);
    return SkuListModal;
}());

//# sourceMappingURL=skulist-modal.js.map

/***/ }),

/***/ 1001:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SkuModel; });
var SkuModel = /** @class */ (function () {
    function SkuModel() {
    }
    return SkuModel;
}());

//# sourceMappingURL=SkuModel.js.map

/***/ }),

/***/ 704:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AsnBillnoModalModule", function() { return AsnBillnoModalModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__skulist_modal__ = __webpack_require__(1000);
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
            declarations: [__WEBPACK_IMPORTED_MODULE_1__skulist_modal__["a" /* SkuListModal */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__skulist_modal__["a" /* SkuListModal */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__skulist_modal__["a" /* SkuListModal */]]
        })
    ], AsnBillnoModalModule);
    return AsnBillnoModalModule;
}());

//# sourceMappingURL=skulist-modal.module.js.map

/***/ })

});
//# sourceMappingURL=66.js.map