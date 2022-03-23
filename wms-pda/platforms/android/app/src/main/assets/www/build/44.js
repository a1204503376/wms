webpackJsonp([44],{

/***/ 1360:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MovesboxPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__ = __webpack_require__(361);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__ = __webpack_require__(160);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__ionic_storage__ = __webpack_require__(66);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__services_AppGlobal__ = __webpack_require__(360);
var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
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
 * Generated class for the MovesboxPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var MovesboxPage = /** @class */ (function () {
    function MovesboxPage(navCtrl, navParams, popoverCtrl, nativeService, barcodeService, appService, modalCtrl, alertCtrl, keyboard, storage, events, ngZone) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.navParams = navParams;
        this.popoverCtrl = popoverCtrl;
        this.nativeService = nativeService;
        this.barcodeService = barcodeService;
        this.appService = appService;
        this.modalCtrl = modalCtrl;
        this.alertCtrl = alertCtrl;
        this.keyboard = keyboard;
        this.storage = storage;
        this.events = events;
        this.ngZone = ngZone;
        this.isShow = true;
        this.skuStockList = []; //移动列表实体
        this.skuStockItem = {}; //当前移动实体
        this.sysparms = []; //系统参数
        this.selectOptions = {
            title: 'Pizza Toppings',
            subTitle: 'Select your toppings',
            mode: 'md'
        };
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    MovesboxPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            if (bt == __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["b" /* BarcodeType */].HuoWei) {
                _this.ngZone.run(function () {
                    _this.sourceLocCode = barcode;
                });
            }
            if (bt == __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["b" /* BarcodeType */].BoxCode) {
                _this.ngZone.run(function () {
                    //条码解析
                    _this.barcodeService.ScanSkuBarcode(barcode).subscribe(function (val) {
                        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                            _this.scanModel = val;
                            //查询物品
                            _this.scanWmsSku(val);
                        }
                    });
                });
            }
        });
    };
    MovesboxPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_10__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    MovesboxPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage');
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(_this.skuStockList)) {
                console.log(_this.skuStockList);
                if (data == 'allCheck') {
                    //全选
                    for (var index = 0; index < _this.skuStockList.length; index++) {
                        _this.skuStockList[index].isCheck = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //取消全选
                    for (var index = 0; index < _this.skuStockList.length; index++) {
                        _this.skuStockList[index].isCheck = false;
                    }
                }
                else {
                    //删除选中
                    _this.skuStockIsCheckDel();
                }
            }
        });
        popover.present({
            ev: myEvent
        });
    };
    /**
     * 删除选中
     */
    MovesboxPage.prototype.skuStockIsCheckDel = function () {
        this.skuStockList = this.skuStockList.filter(function (x) { return x.isCheck === false; });
    };
    MovesboxPage.prototype.removeItem = function (item) {
        this.skuStockList = this.skuStockList.filter(function (x) { return x != item; });
    };
    MovesboxPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    MovesboxPage.prototype.ionViewDidLoad = function () {
    };
    /**
     * 折叠状态
     */
    MovesboxPage.prototype.onIsShowClick = function () {
        this.isShow = !this.isShow;
    };
    /**
     * 扫码回车事件
     * @param event
     */
    MovesboxPage.prototype.scanModelChange = function (event) {
        var _this = this;
        if (event && event.keyCode == 13) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.scanView) && __WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(this.sourceLocCode)) {
                this.barcodeService.ScanSkuBarcode(this.scanView).subscribe(function (val) {
                    if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(val)) {
                        _this.scanModel = val;
                        //查询物品
                        _this.scanWmsSku(val);
                    }
                });
            }
            else {
                this.nativeService.showToast('请扫描箱码或库位');
            }
        }
    };
    /*物品扫描*/
    MovesboxPage.prototype.scanWmsSku = function (val) {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(val.skucode)) {
            this.nativeService.showToast('物品编码不能为空');
        }
        var params = {
            skuCode: val.skucode
        };
        //获取物品
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].SkuList, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            //判断是否多个物品
            if (result.data.length > 1) {
                //选择物品
                _this.openAsnRecordModal(result.data, val);
            }
            else if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data)) {
                _this.nativeService.showToast('没有查询到物品');
            }
            else {
                _this.getMoveStockModel(val, result.data[0].skuId);
            }
        });
    };
    /**
    * 查询库存信息
    */
    MovesboxPage.prototype.getMoveStockModel = function (model, skuId) {
        var _this = this;
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model.qty)) {
            this.nativeService.showToast('物品数量不能为空');
            return;
        }
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(model.skucode)) {
            this.nativeService.showToast('物品编码不能为空');
            return;
        }
        var skuLotModel = this.barcodeService.convertLotsModel(model);
        // this.skuCode = model.skucode;
        // this.skuName = model.skuname;
        var param = __assign({ 
            // locCode: this.locCode,
            // qty: model.qty,
            // skuCode: model.skucode,
            // whId: this.warhouserItem.whId,
            // skuLots: skuLotModel,
            // skuId: this.resultSkuItem.skuId
            skuId: skuId, sourceLocCode: this.sourceLocCode, sourceLpnCode: this.sourceLpnCode, wsuName: model.um, skuCode: model.skucode, skuName: model.skuname, moveQty: model.qty }, skuLotModel);
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].getMoveStock, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Get, param, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Content, function (result) {
            _this.nativeService.hideLoading();
            if (!result.success) {
                _this.nativeService.showToast(result.msg);
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(result.data.stockList))
                    return;
                if (result.data.stockList.length > 1) {
                    _this.openStockSelectModal(result.data);
                }
                else {
                    result.data['isCheck'] = false;
                    result.data['isWarning'] = false;
                    result.data['stockId'] = result.data.stockList[0].stockId;
                    result.data['wspId'] = result.data.stockList[0].wspId;
                    result.data['soBillId'] = result.data.stockList[0].soBillId;
                    result.data['soBillNo'] = result.data.stockList[0].soBillNo;
                    result.data['orderNo'] = result.data.stockList[0].orderNo;
                    result.data['billDetailId'] = result.data.stockList[0].billDetailId;
                    result.data['wellenId'] = result.data.stockList[0].wellenId;
                    result.data['sourceLpnCode'] = _this.sourceLpnCode;
                    _this.skuStockList.push(result.data);
                    _this.skuStockItem = result.data;
                }
            }
        });
    };
    /**
    *查看物品明细
    */
    MovesboxPage.prototype.openAsnRecordModal = function (skuList, val) {
        var _this = this;
        var myModal = this.modalCtrl.create('SkuListModal', {
            skuList: skuList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(data)) {
                _this.getMoveStockModel(val, data.skuId);
            }
        });
        myModal.present();
    };
    MovesboxPage.prototype.openStockSelectModal = function (data1) {
        var _this = this;
        var myModal = this.modalCtrl.create('StockSelectModal', {
            stockRecords: data1.stockList,
        });
        myModal.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(data))
                return;
            data1['isCheck'] = false;
            data1['isWarning'] = false;
            data1['stockId'] = data.stockId;
            data1['wspId'] = data.wspId;
            data1['soBillId'] = data.soBillId;
            data1['soBillNo'] = data.soBillNo;
            data1['orderNo'] = data.orderNo;
            data1['billDetailId'] = data.billDetailId;
            data1['wellenId'] = data.wellenId;
            data1['sourceLpnCode'] = _this.sourceLpnCode;
            _this.skuStockList.push(data1);
            _this.skuStockItem = data1;
        });
        myModal.present();
    };
    /**
     * 切换规格物品
     */
    MovesboxPage.prototype.specOnclick = function (skuStockItem) {
        console.log(skuStockItem);
        var alert = this.alertCtrl.create();
        alert.setTitle('选择包装规格');
        skuStockItem.stockList.forEach(function (element) {
            alert.addInput({
                type: 'radio',
                label: element.skuSpec,
                value: element,
                checked: false
            });
        });
        alert.addButton('返回');
        alert.addButton({
            text: '确定',
            handler: function (data) {
                console.log(data);
                for (var index = 0; index < skuStockItem.stockList.length; index++) {
                    var element = skuStockItem.stockList[index];
                    if (element === data) {
                        var temp = skuStockItem.stockList[0];
                        skuStockItem.stockList[0] = element;
                        skuStockItem.stockList[index] = temp;
                        //更新库存IDskuStockItem.stockList[0]
                        skuStockItem.stockId = skuStockItem.stockList[0].stockId;
                    }
                }
            }
        });
        alert.present();
    };
    /**
     * 转移至
     */
    MovesboxPage.prototype.btnOk = function () {
        var _this = this;
        var skuStockList = this.skuStockList.filter(function (x) { return x.isCheck == true; });
        if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isEmpty(skuStockList)) {
            this.nativeService.showToast('转移列表中不存在数据');
            return;
        }
        //转移库存验证
        console.log(this.skuStockList);
        console.log(skuStockList);
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].stockInner + __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["b" /* Api */].verifyStockForMoveByBox, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["f" /* Method */].Post, skuStockList, __WEBPACK_IMPORTED_MODULE_7__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_2__services_Utils__["a" /* Utils */].isNotEmpty(result.data)) {
                //渲染当前不足的库存
                for (var index = 0; index < result.data.length; index++) {
                    var element = result.data[index];
                    for (var j = 0; j < _this.skuStockList.length; j++) {
                        if (_this.skuStockList[j].stockId == element.stockId) {
                            _this.skuStockList[j].isWarning = true;
                        }
                    }
                }
            }
            else {
                //进行页面跳转喽  
                //this.navCtrl.push('MovesboxInfoPage', { skuStockList: this.skuStockList });
                var myModal = _this.modalCtrl.create('MovesboxInfoPage', {
                    skuStockList: skuStockList,
                    sourceLocCode: _this.sourceLocCode,
                    sourceLpnCode: _this.sourceLpnCode //原容器
                });
                //回调
                myModal.onDidDismiss(function (data) {
                    if (data) {
                        //数据清空
                        _this.clearModel();
                    }
                });
                myModal.present();
            }
        });
    };
    MovesboxPage.prototype.clearModel = function () {
        this.scanView = '';
        this.sourceLocCode = '';
        this.scanModel = new __WEBPACK_IMPORTED_MODULE_5__models_DataBase_Model__["a" /* ScanModel */]();
        this.skuStockList = [];
        this.skuStockItem = { skuName: '', moveQty: '', wsuName: '' };
    };
    MovesboxPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-movesbox',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesbox\movesbox.html"*/'<ion-header>\n\n  <ion-navbar color="wmscolor">\n\n    <ion-title>\n\n      按箱移动\n\n      <!-- <div class="title_f_name">(欣天新)</div> -->\n\n    </ion-title>\n\n  </ion-navbar>\n\n  <div class="title-bt">\n\n    <div class="nav-tile">\n\n      <!-- <ul>\n\n        <li>首页</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>移动</li>\n\n        <li>\n\n          <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n        </li>\n\n        <li>物品移动</li>\n\n      </ul> -->\n\n      <crumbs></crumbs>\n\n    </div>\n\n  </div>\n\n</ion-header>\n\n<ion-content>\n\n\n\n\n\n  <!-- 头部列表 -->\n\n  <div class="head_lable" *ngIf="isShow">\n\n    <ion-item>\n\n      <ion-label>原库位：</ion-label>\n\n      <ion-input type="text" [(ngModel)]="sourceLocCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item *ngIf="sysparms.paramValue==0">\n\n      <ion-label>原容器：</ion-label>\n\n      <ion-input type="text" [(ngModel)]="sourceLpnCode"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>物&emsp;品：</ion-label>\n\n      <ion-input [disabled]=\'true\' type="text" [(ngModel)]="skuStockItem.skuName"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>数&emsp;量：</ion-label>\n\n      <ion-input  type="number" [(ngModel)]="skuStockItem.moveQty"></ion-input>\n\n      <ion-select [disabled]=\'true\' [(ngModel)]="skuStockItem.wsuName" cancelText="取消">\n\n        <ion-option [value]="skuStockItem.wsuName">{{skuStockItem.wsuName}}</ion-option>\n\n      </ion-select>\n\n    </ion-item>\n\n    <ion-item>\n\n      <ion-label>扫&emsp;描：</ion-label>\n\n      <ion-input (keyup)="scanModelChange($event)" [(ngModel)]="scanView" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n    </ion-item>\n\n\n\n  </div>\n\n  <!-- 序列号明细 -->\n\n  <div class="list_item_serial">\n\n    <div class="list_item_title">\n\n      <!-- <span [ngStyle]="{\'border-bottom\':isTilebool === false ? \'3px solid #008080\' : \'0\' }"\n\n              (click)="isTitle(false)">已收序列号(<b>{{InNumberLength}}</b>)</span> -->\n\n      <span *ngIf="isShow; else elseBlock" (click)="onIsShowClick()">\n\n        <ion-icon name="md-arrow-dropup"></ion-icon>\n\n      </span>\n\n\n\n      <ng-template #elseBlock>\n\n        <span (click)="onIsShowClick()">\n\n          <ion-icon name="md-arrow-dropdown"></ion-icon>\n\n        </span>\n\n\n\n      </ng-template>\n\n      <span [ngStyle]="{\'border-bottom\': \'3px solid #008080\' }"\n\n        (click)="isTitle(true)">已扫物品(<b>{{skuStockList.length}}</b>)</span>\n\n      <span>\n\n        <button ion-button icon-only (click)="presentPopover($event)">\n\n          <ion-icon name="md-more"></ion-icon>\n\n        </button>\n\n\n\n      </span>\n\n    </div>\n\n  </div>\n\n\n\n  <ion-list class="list_item_div">\n\n\n\n    <ion-scroll scrollY="true" >\n\n      <!--接货列表-->\n\n      <ion-item-sliding *ngFor="let skuStockItem of skuStockList">\n\n        <ion-item>\n\n          <ion-checkbox [(ngModel)]="skuStockItem.isCheck"></ion-checkbox>\n\n          <ion-label>\n\n            <div class="title-ms clearfix">\n\n              <div class="title-ms-info clearfix">\n\n                <div>\n\n                  <span>物&emsp;&emsp;品：</span>\n\n                  <span>{{skuStockItem.skuName}}</span>\n\n                </div>\n\n                <div>\n\n                  <span>原&ensp;库&ensp;位：</span>\n\n                  <span>{{skuStockItem.sourceLocCode}}</span>\n\n                </div>\n\n                <div *ngFor="let item of skuStockItem.skuLotValList">\n\n                  <span *ngIf="item.skuLotValue!=\'\'">{{item.skuLotLabel}}：</span>\n\n                  <span *ngIf="item.skuLotValue!=\'\'">{{item.skuLotValue}}</span>\n\n                </div>\n\n                <div [ngStyle]="{color: skuStockItem.isWarning?\'red\':\'\'}" class="number-msg">\n\n                  <span>数量：</span>\n\n                  <span>{{skuStockItem.moveQty + skuStockItem.wsuName}}\n\n                  </span>\n\n                </div>\n\n                <div [ngStyle]="{\'position\': skuStockItem.stockList[0].skuSpec.length>=6?\'static\':\'absolute\'}"\n\n                  class="spec-msg">\n\n                  <span>规格：</span>\n\n                  <span>{{skuStockItem.stockList[0].skuSpec}}</span>\n\n                  <span *ngIf="skuStockItem.stockList.length>=2" class="spec-msg-dropdown"\n\n                    (tap)="specOnclick(skuStockItem)">\n\n                    <ion-icon name="md-arrow-dropdown"></ion-icon>\n\n                  </span>\n\n                </div>\n\n              </div>\n\n            </div>\n\n          </ion-label>\n\n        </ion-item>\n\n        <ion-item-options side="right">\n\n          <button (tap)="removeItem(skuStockItem)" ion-button color="danger">\n\n            删除\n\n          </button>\n\n        </ion-item-options>\n\n      </ion-item-sliding>\n\n    </ion-scroll>\n\n  </ion-list>\n\n\n\n  <!-- 转移至 -->\n\n  <!-- <div class="list_item" *ngIf="IsShow==IsSkuMove">\n\n    <ion-item>\n\n      <ion-label>目标库位：</ion-label>\n\n      <ion-input [(ngModel)]="targetLocCode" type="text"></ion-input>\n\n      <ion-avatar item-end>\n\n        <ion-icon class="iconfont iconsaomiao"></ion-icon>\n\n      </ion-avatar>\n\n\n\n    </ion-item>\n\n  </div> -->\n\n\n\n\n\n</ion-content>\n\n<ion-footer>\n\n  <div class="btn-box">\n\n    <button no-dbl-click ion-button block class="btn-l" (tap)="exit()">\n\n      返回[Esc]\n\n    </button>\n\n    <button no-dbl-click ion-button block class="btn-d" (tap)="btnOk()">\n\n      转移至[Ent]\n\n    </button>\n\n  </div>\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\movehome\movesbox\movesbox.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_4__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_6__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["j" /* ModalController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["a" /* AlertController */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_native_keyboard__["a" /* Keyboard */],
            __WEBPACK_IMPORTED_MODULE_9__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"]])
    ], MovesboxPage);
    return MovesboxPage;
}());

//# sourceMappingURL=movesbox.js.map

/***/ }),

/***/ 765:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MovesboxPageModule", function() { return MovesboxPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__movesbox__ = __webpack_require__(1360);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var MovesboxPageModule = /** @class */ (function () {
    function MovesboxPageModule() {
    }
    MovesboxPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__movesbox__["a" /* MovesboxPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__movesbox__["a" /* MovesboxPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], MovesboxPageModule);
    return MovesboxPageModule;
}());

//# sourceMappingURL=movesbox.module.js.map

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
//# sourceMappingURL=44.js.map