webpackJsonp([68],{

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
//# sourceMappingURL=68.js.map