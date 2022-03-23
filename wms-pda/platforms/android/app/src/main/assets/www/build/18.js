webpackJsonp([18],{

/***/ 1002:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return StockSelectModal; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__models_Stock_model__ = __webpack_require__(1003);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};







var StockSelectModal = /** @class */ (function () {
    function StockSelectModal(navCtrl, navParams, viewCtrl, storage, nativeService, appService) {
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.viewCtrl = viewCtrl;
        this.storage = storage;
        this.nativeService = nativeService;
        this.appService = appService;
        this.records = [];
        this.selectItem = new __WEBPACK_IMPORTED_MODULE_6__models_Stock_model__["a" /* StockSelectModel */]();
        this.records = this.navParams.get('stockRecords');
        this.resetSelected();
    }
    /**
       *   页面进入
       */
    StockSelectModal.prototype.ionViewWillEnter = function () {
    };
    /**
   * 选择要处理的入库单
   */
    StockSelectModal.prototype.resetSelected = function () {
        for (var _i = 0, _a = this.records; _i < _a.length; _i++) {
            var item = _a[_i];
            item['selected'] = false;
        }
    };
    StockSelectModal.prototype.headSelected = function (item) {
        this.resetSelected();
        item['selected'] = true;
        this.selectItem = item;
    };
    /**
     * 消失调用
     */
    StockSelectModal.prototype.dismiss = function () {
        this.viewCtrl.dismiss();
    };
    /**
     * 点击确定
     */
    StockSelectModal.prototype.submitdata = function () {
        if (__WEBPACK_IMPORTED_MODULE_5__services_Utils__["a" /* Utils */].isEmpty(this.selectItem)) {
            this.nativeService.showToast("请选择需要执行的选项!");
            return;
        }
        this.viewCtrl.dismiss(this.selectItem);
    };
    StockSelectModal = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-stockselect-modal',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\stockselect-modal\stockselect-modal.html"*/'<ion-content>\n\n  <ion-list>\n\n    <ion-item-group>\n\n       请选择\n\n    </ion-item-group>\n\n    <ion-item-group>\n\n      <ion-scroll zooming="true" scrollY="true">\n\n        <ion-item *ngFor="let item of records" (click)="headSelected(item)" \n\n        [ngClass]="{selected:item.selected}">\n\n          <div class="nodes-item">\n\n            <div>\n\n              <span>上位单号：</span>\n\n              <span>{{item.orderNo}}</span>\n\n            </div>\n\n            <div>\n\n              <span>单据编号：</span>\n\n              <span>{{item.soBillNo}}</span> \n\n            </div>\n\n            <div>\n\n              <span>客户名称：</span>\n\n              <span>{{item.enterpriseName}}</span>\n\n            </div>\n\n          </div>\n\n        </ion-item>\n\n      </ion-scroll>\n\n    </ion-item-group>\n\n    <ion-item-group>\n\n      <button ion-button (click)="dismiss()">取消</button>\n\n      <button ion-button (click)="submitdata()">确认</button>\n\n    </ion-item-group>\n\n  </ion-list>\n\n</ion-content>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\components\stockselect-modal\stockselect-modal.html"*/
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["r" /* ViewController */],
            __WEBPACK_IMPORTED_MODULE_2__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */]])
    ], StockSelectModal);
    return StockSelectModal;
}());

//# sourceMappingURL=stockselect-modal.js.map

/***/ }),

/***/ 1003:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* unused harmony export StockInfo */
/* unused harmony export StockRecordModel */
/* unused harmony export StockQueryModel */
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return StockSelectModel; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__PageInfo_Model__ = __webpack_require__(1004);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_Utils__ = __webpack_require__(89);
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();


var StockInfo = /** @class */ (function () {
    function StockInfo() {
    }
    return StockInfo;
}());

var StockRecordModel = /** @class */ (function () {
    function StockRecordModel() {
    }
    // 显示库存量
    StockRecordModel.prototype.disolayString = function () {
        if (!__WEBPACK_IMPORTED_MODULE_1__services_Utils__["a" /* Utils */].isEmpty(this.DisplayRemnantValue))
            return this.DisplayStockQty + " " + this.DisplayRemnantValue;
        else
            return this.DisplayStockQty;
    };
    return StockRecordModel;
}());

/// 库存查询参数实体类
var StockQueryModel = /** @class */ (function (_super) {
    __extends(StockQueryModel, _super);
    function StockQueryModel() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return StockQueryModel;
}(__WEBPACK_IMPORTED_MODULE_0__PageInfo_Model__["a" /* PageInfoModel */]));

var StockSelectModel = /** @class */ (function () {
    function StockSelectModel() {
    }
    return StockSelectModel;
}());

//# sourceMappingURL=Stock.model.js.map

/***/ }),

/***/ 1004:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PageInfoModel; });
var PageInfoModel = /** @class */ (function () {
    function PageInfoModel() {
    }
    return PageInfoModel;
}());

//# sourceMappingURL=PageInfo.Model.js.map

/***/ }),

/***/ 705:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "StockSelectModalModule", function() { return StockSelectModalModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__stockselect_modal__ = __webpack_require__(1002);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ionic_angular__ = __webpack_require__(43);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};



var StockSelectModalModule = /** @class */ (function () {
    function StockSelectModalModule() {
    }
    StockSelectModalModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [__WEBPACK_IMPORTED_MODULE_1__stockselect_modal__["a" /* StockSelectModal */]],
            imports: [__WEBPACK_IMPORTED_MODULE_2_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_1__stockselect_modal__["a" /* StockSelectModal */]),
            ],
            exports: [__WEBPACK_IMPORTED_MODULE_1__stockselect_modal__["a" /* StockSelectModal */]]
        })
    ], StockSelectModalModule);
    return StockSelectModalModule;
}());

//# sourceMappingURL=stockselect-modal.module.js.map

/***/ })

});
//# sourceMappingURL=18.js.map