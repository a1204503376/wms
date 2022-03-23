webpackJsonp([25],{

/***/ 1366:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return TakeawayinPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__ = __webpack_require__(360);
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
 * Generated class for the TakeawayinPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var TakeawayinPage = /** @class */ (function () {
    function TakeawayinPage(navCtrl, navParams, nativeService, appService, modalCtrl, storage, barcodeService, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.nativeService = nativeService;
        this.appService = appService;
        this.modalCtrl = modalCtrl;
        this.storage = storage;
        this.barcodeService = barcodeService;
        this.events = events;
        this.ngZone = ngZone;
        this.recList = 1;
        this.IsShow = 0; //显隐标识
        this.IsSku = 0; //物品显示
        this.IsSkuList = 1; //物品列表显示
        this.IsSkuSerial = 4; //序列号物品显示
        this.IsSkuSerialInfo = 5; //序列号物品明细显示
        this.InNumberLength = 0; //已收序列号数量
        this.ScanNumberLength = 0; //已扫描序列号数量
        this.serialNumber = []; //序列号已扫描集合
        this.InserialNumber = []; //序列号已提交
        this.NumberListFalag = 1; //扫描标识 1：已扫描 2:已收货
        this.isTilebool = true;
        this.targetLocCode = 'PICK'; //落放位置
        this.sysparms = []; //系统参数
        this.pickPlansModel = this.navParams.get('pickPlansModel');
        this.PickInfoResult = this.navParams.get('PickInfoResult');
        this.pickPlansList = this.navParams.get('pickPlansList');
        this.wellenNo = this.navParams.get('wellenNo');
        this.pickPlansInModel();
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    TakeawayinPage.prototype.ionViewDidLoad = function () {
        this.HeadModel();
    };
    TakeawayinPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        if (this.navParams.get('flag') || false) {
            this.wellenNo = "";
        }
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].TuoPan) {
                _this.ngZone.run(function () {
                    _this.targetLpnCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.targetLocCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["b" /* BarcodeType */].SKU) {
                _this.ngZone.run(function () {
                    _this.skuCodeModel = barcode;
                    _this.getSkuInfo();
                });
            }
        });
    };
    TakeawayinPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_7__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    TakeawayinPage.prototype.clearModel = function () {
        this.targetLpnCode = '';
        this.skuCodeModel = '';
        this.lotNumber = '';
        this.pickQty = '';
        this.serialNumber = [];
        this.ScanNumberLength = 0;
    };
    /**
   * 选项卡数据
   */
    TakeawayinPage.prototype.HeadModel = function () {
        //头部
        this.count = this.pickPlansModel.count;
        this.finish = this.pickPlansModel.finish;
        //物品选项卡
        this.skuName = this.pickPlansModel.skuName;
        this.skuCode = this.pickPlansModel.skuCode;
        this.skuSpec = this.pickPlansModel.skuSpec;
        this.skuLot = this.pickPlansModel.skuLot;
        this.lotNumberTitle = this.pickPlansModel.lotNumber;
        this.planCountQty = this.pickPlansModel.planCountQty;
        this.realCountQty = this.pickPlansModel.realCountQty;
        this.planQtyName = this.pickPlansModel.planQtyName;
        this.realQtyName = this.pickPlansModel.realQtyName;
    };
    TakeawayinPage.prototype.pickPlansInModel = function () {
        this.HeadModel();
        //页面物品详情元素
        this.sourceLocCode = this.pickPlansModel.sourceLocCode;
        this.sourceLpnCode = this.pickPlansModel.sourceLpnCode;
        this.packageDetails = this.pickPlansModel.packageDetails;
        this.defaultpackageDetail = this.pickPlansModel.defaultPackageDetail;
        this.InserialNumber = this.pickPlansModel.serialList;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.pickPlansModel.serialList)) {
            this.InNumberLength = this.pickPlansModel.serialList.length;
        }
        //判断是否为序列号
        if (this.pickPlansModel.isSn == '1') {
            this.IsShow = this.IsSkuSerial;
        }
        else {
            this.IsShow = this.IsSku;
        }
    };
    /**
     * 切换物品列表
     */
    TakeawayinPage.prototype.onClickItem = function () {
        this.IsShow = this.IsSkuList;
        this.pickPlansModelTemp = this.pickPlansModel;
        // this.clearModel();
    };
    /**
     * 拣货记录
     */
    TakeawayinPage.prototype.detailed = function () {
        var _this = this;
        var params = {
            wellenNo: this.wellenNo
        };
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].getSopickLog, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Form, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data.pickPlans)) {
                _this.navCtrl.push('TakeawayindetailedPage', { pickPlans: result.data.pickPlans, finish: _this.finish, count: _this.count });
            }
            else {
                _this.nativeService.showToast('没有查询到记录');
            }
        });
    };
    /**
     * 物品列表选中
     */
    TakeawayinPage.prototype.headSelected = function (item) {
        this.pickPlansModelTemp = item;
    };
    TakeawayinPage.prototype.togglePage = function () {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.defaultpackageDetail.convertQty)) {
            this.skuSpec = '1-' + this.defaultpackageDetail.convertQty;
        }
        else {
            this.skuSpec = '';
        }
        this.planQtyPage();
        this.realQtyPage();
    };
    TakeawayinPage.prototype.planQtyPage = function () {
        var _this = this;
        var result = "";
        var mainWsuName = "";
        var planQtynum = parseInt(this.planCountQty);
        this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        this.packageDetails.forEach(function (element) {
            if (element.skuLevel == _this.defaultpackageDetail.skuLevel) {
                mainWsuName = element.wsuName;
            }
            if (element.skuLevel <= _this.defaultpackageDetail.skuLevel) {
                var tmp = Math.floor(planQtynum / element.convertQty);
                planQtynum = planQtynum - element.convertQty * tmp;
                if (tmp != 0) {
                    result += tmp + element.wsuName;
                }
            }
        });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.planQtyName = 0 + mainWsuName;
        }
        else {
            this.planQtyName = result;
        }
    };
    TakeawayinPage.prototype.realQtyPage = function () {
        var _this = this;
        var result = "";
        var mainWsuName = "";
        var planQtynum = parseInt(this.realCountQty);
        this.packageDetails = this.packageDetails.sort((function (n1, n2) { return n2.convertQty - n1.convertQty; }));
        this.packageDetails.forEach(function (element) {
            if (element.skuLevel == _this.defaultpackageDetail.skuLevel) {
                mainWsuName = element.wsuName;
            }
            if (element.skuLevel <= _this.defaultpackageDetail.skuLevel) {
                var tmp = Math.floor(planQtynum / element.convertQty);
                planQtynum = planQtynum - element.convertQty * tmp;
                if (tmp != 0) {
                    result += tmp + element.wsuName;
                }
            }
        });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result)) {
            this.realQtyName = 0 + mainWsuName;
        }
        else {
            this.realQtyName = result;
        }
    };
    /**
    * 物品回车事件
    */
    TakeawayinPage.prototype.skuCode_KeyDown = function (event) {
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.skuCodeModel)) {
                this.nativeService.showToast('请输入或扫描物品编码');
                return;
            }
            this.getSkuInfo();
        }
    };
    TakeawayinPage.prototype.getSkuInfo = function () {
        var _this = this;
        //通过编号查询物品
        var model = this.pickPlansList.filter(function (x) { return x.skuCode == _this.skuCodeModel && x.sourceLocCode == _this.sourceLocCode && x.sourceLpnCode == _this.sourceLpnCode; });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model)) {
            //this.nativeService.showToast('当前任务下没找到该物品');
            return;
        }
        this.pickPlansModel = model[0];
        this.pickPlansInModel();
    };
    /**
   *查看物品明细
  */
    // openAsnRecordModal(skuList) {
    //   let myModal = this.modalCtrl.create('SkuListModal', {
    //     skuList: skuList,
    //   });
    //   myModal.onDidDismiss(data => {
    //     if (Utils.isNotEmpty(data)) {
    //       this.resultSkuItem = data;
    //       this.getSkuInfo();
    //     }
    //   });
    //   myModal.present();
    // }
    /**
     * 提交按钮
     */
    TakeawayinPage.prototype.btnOk = function () {
        if (this.IsShow == this.IsSkuList) {
            this.pickPlansModel = this.pickPlansModelTemp;
            this.pickPlansInModel();
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.submitPickInfo();
        }
        else {
            //拣货提交
            this.submitPickInfo();
        }
    };
    /**
     * 拣货提交
     */
    TakeawayinPage.prototype.submitPickInfo = function () {
        var _this = this;
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLpnCode)) {
                this.nativeService.showToast('拣货容器不能为空');
                return;
            }
        }
        if (this.sysparms.paramValue == 0) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLpnCode)) {
                this.nativeService.showToast('原容器不能为空');
                return;
            }
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.sourceLocCode)) {
            this.nativeService.showToast('原库位不能为空');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.lotNumber)) {
            this.nativeService.showToast('批次号不能为空');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.targetLocCode)) {
            this.nativeService.showToast('落放位置不能为空');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.defaultpackageDetail)) {
            this.nativeService.showToast('没有包装单位');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.wellenNo)) {
            this.nativeService.showToast('波次编码不能为空');
            return;
        }
        var params = {
            wellenNo: this.wellenNo,
            pickPlanId: this.pickPlansModel.pickPlanId,
            targetLpnCode: this.targetLpnCode,
            sourceLpnCode: this.sourceLpnCode,
            sourceLocCode: this.sourceLocCode,
            skuCode: this.skuCode,
            lotNumber: this.lotNumber,
            pickQty: this.pickQty,
            targetLocCode: this.targetLocCode,
            wspdId: this.defaultpackageDetail.wspdId,
            isSn: this.pickPlansModel.isSn,
        };
        if (this.pickPlansModel.isSn == '1') {
            if (this.ScanNumberLength == 0) {
                this.nativeService.showToast('请扫描序列号');
                return;
            }
            params['serialList'] = this.serialNumber;
            params['pickQty'] = this.serialNumber.length.toString();
        }
        else {
            if (this.skuCode != this.skuCodeModel) {
                this.nativeService.showToast('当前扫描物品编码与选中物品编码不一致');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(this.pickQty)) {
                this.nativeService.showToast('数量不能为空');
                return;
            }
            if (!__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].IsPositiveInt(this.pickQty)) {
                this.nativeService.showToast('数量必须为正整数');
                return;
            }
        }
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].submitPickInfo, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            //重置数据
            _this.PickInfoResult = result.data; //数据源
            _this.pickPlansList = _this.PickInfoResult.pickPlans; //数据集合
            var model = _this.pickPlansList.filter(function (x) { return x.skuCode == _this.skuCodeModel && x.sourceLocCode == _this.skuCodeModel && x.sourceLocCode == _this.sourceLpnCode; });
            // let model = this.pickPlansList.filter(x => x.skuCode == this.skuCode);//当前操作数据
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(model)) {
                _this.pickPlansModel = model[0];
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.pickPlansList)) {
                _this.pickPlansModel = _this.pickPlansList[0];
            }
            else {
                _this.navCtrl.pop();
            }
            _this.navCtrl.getPrevious().data.flag = true;
            _this.nativeService.showToast('提交成功');
            _this.clearModel();
            _this.HeadModel();
        });
    };
    /**
     * 返回按钮
     */
    TakeawayinPage.prototype.exit = function () {
        if (this.IsShow == this.IsSkuList) {
            this.IsShow = this.IsSku;
        }
        else if (this.IsShow == this.IsSku) {
            this.navCtrl.pop();
        }
        else if (this.IsShow == this.IsSkuSerial) {
            this.lotNumber = '';
            this.targetLpnCode = '';
            this.IsShow = this.IsSku;
        }
        else if (this.IsShow == this.IsSkuSerialInfo) {
            this.IsShow = this.IsSkuSerial;
        }
    };
    TakeawayinPage.prototype.compareFn = function (e1, e2) {
        return e1 && e2 ? e1.wspdId === e2.wspdId : e1 === e2;
    };
    TakeawayinPage.prototype.isTitle = function (event) {
        this.isTilebool = event;
        if (event) {
            this.NumberListFalag = 1;
        }
        else {
            this.NumberListFalag = 2;
        }
    };
    /**
    * 序列号回车事件
    */
    TakeawayinPage.prototype.SkuSerial_KeyDown = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.serialNumberMode)) {
                var flag_1 = true;
                this.serialNumber.forEach(function (element) {
                    if (element.toUpperCase() == _this.serialNumberMode.toUpperCase()) {
                        flag_1 = false;
                    }
                });
                var body = {
                    skuId: this.pickPlansModel.skuId,
                    serialNumber: this.serialNumberMode
                };
                if (this.sysparms.paramValue != 0) {
                    body["lpnCode"] = this.sourceLpnCode;
                }
                else {
                    body["lpnCode"] = this.targetLpnCode;
                }
                this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].Outstock + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].OutIsHasSerial, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Get, body, '', function (result) {
                    if (flag_1) {
                        _this.serialNumber.push(_this.serialNumberMode);
                        _this.serialNumberMode = '';
                    }
                    _this.ScanNumberLength = _this.serialNumber.length;
                });
            }
            ;
        }
    };
    TakeawayinPage.prototype.setNameLineClass = function () {
        return this.fontStypeSize(this.realQtyName);
    };
    TakeawayinPage.prototype.setNameLineScanClass = function () {
        return this.fontStypeSize(this.planQtyName);
    };
    TakeawayinPage.prototype.fontStypeSize = function (obj) {
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(obj)) {
            return '16px';
        }
        if (obj.length <= 8) {
            return '16px';
        }
        else {
            return '12px;';
        }
    };
    /**
    * 查看序列号
    */
    TakeawayinPage.prototype.numclick = function () {
        this.IsShow = this.IsSkuSerialInfo;
    };
    /**
    * 删除扫描序列号
    * @param item
    */
    TakeawayinPage.prototype.removeItem = function (item) {
        this.serialNumber = this.serialNumber.filter(function (x) { return x != item; });
        //更新长度
        this.ScanNumberLength = this.serialNumber.length;
    };
    TakeawayinPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-takeawayin',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiece\takeawayin\takeawayin.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      执行拣货\n\n    </ion-title>\n\n    <!-- <ion-buttons end (click)="detailed()">\n\n      <button ion-button icon-only>\n\n        <ion-icon name="menu"></ion-icon>\n\n      </button>\n\n    </ion-buttons> -->\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="order">\n\n      <div class="order-number">{{wellenNo}}</div>\n\n      <div class="order-info">单据拣货进度 {{finish}}/{{count}}</div>\n\n    </div>\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>拣货</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>按件拣货</li>\n\n        <li>\n\n          <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>执行拣货</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n\n\n<ion-content class="no-scroll" overflow-scroll="true" style="overflow: hidden">\n\n  <!-- 物品选显卡 -->\n\n  <div class="title-ms clearfix" *ngIf="IsShow===IsSku||IsShow===IsSkuSerial||IsShow===IsSkuSerialInfo"\n\n    (tap)="onClickItem()">\n\n    <div class="title-ms-info">\n\n      <div class="title-font-s1">\n\n        <label>{{skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div>\n\n          <label>{{skuCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label>{{lotNumberTitle}}</label>\n\n        </div>\n\n        <div class="specs_code">\n\n          拣货规格：<span style="color: black;">{{skuSpec}}</span>\n\n        </div>\n\n        <div class="ionic_right">\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </div>\n\n      </div>\n\n    </div>\n\n    <div *ngIf="realQtyName!=\'\'&& realQtyName!=null " [ngStyle]="{\'font-size\':setNameLineClass()}"\n\n      class="received-scan">\n\n      已拣货: <span style="color: #F59A23;"> {{realQtyName ==\'\'?\'0\':realQtyName}}</span>\n\n    </div>\n\n    <div *ngIf="planQtyName!=\'\'&& planQtyName!=null " [ngStyle]="{\'font-size\':setNameLineScanClass()}"\n\n      class="received-plan">\n\n      计划量: <span style="color: #F59A23;"> {{planQtyName ==\'\'?\'0\':planQtyName}}</span>\n\n    </div>\n\n  </div>\n\n\n\n  <!-- 批次号物品 -->\n\n  <div class="list_item" *ngIf="IsShow===IsSku">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>拣货容器：</ion-label>\n\n      <ion-input maxlength=\'30\' type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(true)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>原货位：</ion-label>\n\n      <ion-input [disabled]=\'true\' [(ngModel)]="sourceLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>原容器：</ion-label>\n\n      <ion-input maxlength=\'30\' [disabled]=\'true\' [(ngModel)]="sourceLpnCode" type="text"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(false)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>物 品：</ion-label>\n\n      <ion-input (keyup)="skuCode_KeyDown($event)" [(ngModel)]="skuCodeModel" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>批次号：</ion-label>\n\n      <ion-input [(ngModel)]="lotNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>数 量：</ion-label>\n\n      <ion-input [(ngModel)]="pickQty" type="number"></ion-input>\n\n      <ion-select (ionChange)="togglePage()" [(ngModel)]="defaultpackageDetail" [compareWith]="compareFn"\n\n        interface="action-sheet" cancelText="取消" okText="确定">\n\n        <div *ngFor="let skuPackage of packageDetails">\n\n          <ion-option [value]="skuPackage">{{skuPackage.wsuName}}</ion-option>\n\n        </div>\n\n      </ion-select>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>落放位置：</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n  <!-- 序列号 -->\n\n  <div class="list_item" *ngIf="IsShow==IsSkuSerial">\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>拣货容器：</ion-label>\n\n      <ion-input maxlength=\'30\' type="text" [(ngModel)]="targetLpnCode"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(true)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>原货位：</ion-label>\n\n      <ion-input [disabled]=\'true\' [(ngModel)]="sourceLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>原容器：</ion-label>\n\n      <ion-input maxlength=\'30\' [disabled]=\'true\' [(ngModel)]="sourceLpnCode" type="text"></ion-input>\n\n      <ion-avatar (tap)="isLocSelect(false)" item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>序列号：</ion-label>\n\n      <ion-input type="text" maxlength=\'100\' (keyup)="SkuSerial_KeyDown($event)" [(ngModel)]="serialNumberMode">\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>批次号：</ion-label>\n\n      <ion-input [(ngModel)]="lotNumber" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item (tap)="numclick()">\n\n      <ion-label>数量：</ion-label>\n\n      <ion-input [disabled]=\'true\' type="text" readonly="readonly" value=\'{{ScanNumberLength + " /" +InNumberLength}}\'>\n\n      </ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconicon_arr_right"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>落放位置：</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n  </div>\n\n  <!-- 序列号明细 -->\n\n  <div class="list_item_serial" *ngIf="IsShow===IsSkuSerialInfo">\n\n    <div class="list_item_title">\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === true ? \'3px solid #008080\' : \'0\' }"\n\n        (tap)="isTitle(true)">已扫序列号(<b>{{ScanNumberLength}}</b>)</span>\n\n      <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n        (tap)="isTitle(false)">已拣序列号(<b>{{InNumberLength}}</b>)</span>\n\n    </div>\n\n\n\n  </div>\n\n\n\n  <!-- 已扫描序列号 -->\n\n  <div *ngIf="NumberListFalag==1 && IsShow===IsSkuSerialInfo ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of serialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n          <ion-item-options side="right">\n\n            <button (tap)="removeItem(item)" ion-button color="danger">\n\n              删除\n\n            </button>\n\n          </ion-item-options>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n  <!-- 已收序列号 -->\n\n  <div *ngIf="NumberListFalag==2 && IsShow===IsSkuSerialInfo ">\n\n    <ion-list class="list_item_div">\n\n      <ion-scroll scrollY="true" style="height:35rem;">\n\n        <ion-item-sliding *ngFor="let item of InserialNumber">\n\n          <ion-item>\n\n            <h2>{{item}}</h2>\n\n          </ion-item>\n\n        </ion-item-sliding>\n\n      </ion-scroll>\n\n    </ion-list>\n\n  </div>\n\n\n\n\n\n  <!-- 物品列表 -->\n\n  <div class="title-ms-list" *ngIf="IsShow===IsSkuList">\n\n    <div class="title-ms-info-list" [ngClass]="{selected:pickPlansModelTemp.pickPlanId == item.pickPlanId}"\n\n      (tap)="headSelected(item)" *ngFor="let item of pickPlansList">\n\n      <div class="title-font-s1">\n\n        <label>{{item.skuName}}</label>\n\n      </div>\n\n      <div class="title-font-small">\n\n        <div>\n\n          <label>{{item.skuCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label>原库位:{{item.sourceLocCode}}</label>\n\n        </div>\n\n        <div>\n\n          <label style="color: black;" *ngIf="item.skuLot.skuLot1!=\'\'; else elseBlock">{{item.lotNumber}}\n\n            &nbsp;</label>\n\n          <ng-template #elseBlock> <label style="color: black;">暂无批属性</label></ng-template>\n\n        </div>\n\n        <div class="list_specs_code">\n\n          拣货规格：<span style="color: black;">{{item.skuSpec}}</span>\n\n        </div>\n\n        <!-- <div class="specs-code-lpn">\n\n          原容器：<span style="color: black;">{{item.sourceLpnCode}}</span>\n\n        </div> -->\n\n        <div class="list_received">\n\n          已拣货: <span style="color: #F59A23;">{{item.realCountQty}}</span>/{{item.planCountQty}}\n\n        </div>\n\n      </div>\n\n    </div>\n\n  </div>\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      提交[Ent]\n\n    </button>\n\n  </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\takeawayhome\takeawaypiece\takeawayin\takeawayin.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */], __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_6__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_8__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], TakeawayinPage);
    return TakeawayinPage;
}());

//# sourceMappingURL=takeawayin.js.map

/***/ }),

/***/ 771:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "TakeawayinPageModule", function() { return TakeawayinPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__takeawayin__ = __webpack_require__(1366);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var TakeawayinPageModule = /** @class */ (function () {
    function TakeawayinPageModule() {
    }
    TakeawayinPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__takeawayin__["a" /* TakeawayinPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__takeawayin__["a" /* TakeawayinPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], TakeawayinPageModule);
    return TakeawayinPageModule;
}());

//# sourceMappingURL=takeawayin.module.js.map

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
//# sourceMappingURL=25.js.map