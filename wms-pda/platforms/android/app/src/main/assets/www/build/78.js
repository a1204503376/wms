webpackJsonp([78],{

/***/ 702:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MergeTraySelectModule", function() { return MergeTraySelectModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__mergetrayselect_modal__ = __webpack_require__(998);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var MergeTraySelectModule = /** @class */ (function () {
    function MergeTraySelectModule() {
    }
    MergeTraySelectModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__mergetrayselect_modal__["a" /* MergeTraySelectModal */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__mergetrayselect_modal__["a" /* MergeTraySelectModal */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__mergetrayselect_modal__["a" /* MergeTraySelectModal */]]
        })
    ], MergeTraySelectModule);
    return MergeTraySelectModule;
}());

//# sourceMappingURL=mergetrayselect-modal.module.js.map

/***/ }),

/***/ 998:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MergeTraySelectModal; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_DataBase_Model__ = __webpack_require__(361);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};







var MergeTraySelectModal = /** @class */ (function () {
    function MergeTraySelectModal(navCtrl, navParams, viewCtrl, storage, nativeService, appService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.viewCtrl = viewCtrl;
        this.storage = storage;
        this.nativeService = nativeService;
        this.appService = appService;
        this.records = [];
        this.selectItem = new __WEBPACK_IMPORTED_MODULE_6__models_DataBase_Model__["a" /* ScanModel */]();
        this.records = this.navParams.get('records');
        this.resetSelected();
    }
    /**
     *   页面进入
     */
    MergeTraySelectModal.prototype.ionViewWillEnter = function () {
    };
    /**
   * 选择要处理的入库单
   */
    MergeTraySelectModal.prototype.resetSelected = function () {
        for (var _i = 0, _a = this.records; _i < _a.length; _i++) {
            var item = _a[_i];
            item['selected'] = false;
        }
    };
    MergeTraySelectModal.prototype.headSelected = function (item) {
        this.resetSelected();
        item['selected'] = true;
        this.selectItem = item;
    };
    /**
     * 消失调用
     */
    MergeTraySelectModal.prototype.dismiss = function () {
        this.viewCtrl.dismiss();
    };
    /**
     * 点击确定
     */
    MergeTraySelectModal.prototype.submitdata = function () {
        if (__WEBPACK_IMPORTED_MODULE_5__services_Utils__["a" /* Utils */].isEmpty(this.selectItem)) {
            this.nativeService.showToast("请选择需要执行的选项!");
            return;
        }
        this.viewCtrl.dismiss(this.selectItem);
    };
    MergeTraySelectModal = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-mergetrayselect-modal',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\mergetrayselect-modal\mergetrayselect-modal.html"*/'<ion-content>\n\n  <ion-list>\n\n    <ion-item-group>\n\n      请选择\n\n    </ion-item-group>\n\n    <ion-item-group>\n\n      <ion-scroll zooming="true" scrollY="true">\n\n        <ion-item *ngFor="let item of records" (click)="headSelected(item)" [ngClass]="{selected:item.selected}">\n\n          <div class="nodes-item">\n\n            <div>\n\n              <span>物料编号：</span>\n\n              <span>{{item.skuCode}}</span>\n\n            </div>\n\n            <div>\n\n              <span>物料名称：</span>\n\n              <span>{{item.skuName}}</span>\n\n            </div>\n\n            <div>\n\n              <span>拣货数量：</span>\n\n              <span>{{item.qty}}</span>\n\n            </div>\n\n            <div>\n\n              <span>客户名称：</span>\n\n              <span>{{item.cname}}</span>\n\n            </div>\n\n            <div>\n\n              <span>客户地址：</span>\n\n              <span>{{item.address}}</span>\n\n            </div>\n\n          </div>\n\n        </ion-item>\n\n      </ion-scroll>\n\n    </ion-item-group>\n\n    <ion-item-group>\n\n      <button ion-button (click)="dismiss()">取消</button>\n\n      <button ion-button (click)="submitdata()">确认</button>\n\n    </ion-item-group>\n\n  </ion-list>\n\n</ion-content>\n\n'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\mergetrayselect-modal\mergetrayselect-modal.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */]])
    ], MergeTraySelectModal);
    return MergeTraySelectModal;
}());

//# sourceMappingURL=mergetrayselect-modal.js.map

/***/ })

});
//# sourceMappingURL=78.js.map