webpackJsonp([28],{

/***/ 1365:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return StockqueryPage; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_App_service__ = __webpack_require__(157);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_NativeService__ = __webpack_require__(64);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__services_Utils__ = __webpack_require__(89);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__ = __webpack_require__(65);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__utils_common__ = __webpack_require__(91);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__ionic_storage__ = __webpack_require__(66);
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
 * Generated class for the StockqueryPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */
var StockqueryPage = /** @class */ (function () {
    function StockqueryPage(navCtrl, popoverCtrl, navParams, appService, nativeService, barCodeService, common, storage, ngZone, events, barcodeService) {
        var _this = this;
        this.navCtrl = navCtrl;
        this.popoverCtrl = popoverCtrl;
        this.navParams = navParams;
        this.appService = appService;
        this.nativeService = nativeService;
        this.barCodeService = barCodeService;
        this.common = common;
        this.storage = storage;
        this.ngZone = ngZone;
        this.events = events;
        this.barcodeService = barcodeService;
        this.locCode = ""; //库位编码
        this.lpnCode = ""; //容器编码
        this.skuCode = ""; //物品编码
        this.lotNumber = ""; //批次号
        this.serialNumber = ""; //序列号
        this.boxCode = ""; //箱码
        this.stockVOList = []; //库存查询列表
        this.current = 1; //当前页 
        this.size = 10; //每页条数
        this.ascs = ""; //正序字段
        this.descs = ""; //倒序字段
        this.hasPage = false; //是否有下一页
        this.sortMenu = false; //是否显示排序菜单
        this.isResult = false; //是否返回数据
        this.sysparms = []; //系统参数
        this.isCheck = false;
        this.isMore = false;
        this.storage.get('sysParam').then(function (data) {
            _this.sysparms = data.filter(function (x) { return x.paramKey == 'system::lpnEnable'; })[0];
        });
    }
    StockqueryPage.prototype.ionViewWillEnter = function () {
        var _this = this;
        //事件注册（扫描结果接收）
        this.events.subscribe('barcode:scan', function (barcode, time) {
            var bt = _this.barcodeService.GetBarcodeType(barcode);
            _this.queryInit();
            _this.ngZone.run(function () {
                _this.searchQuery = barcode;
                _this.resetPage();
            });
            switch (bt) {
                case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].HuoWei:
                    _this.ngZone.run(function () {
                        _this.locCode = barcode;
                        _this.searchQuery = barcode;
                        _this.StockQuery();
                    });
                    break;
                case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].TuoPan:
                    _this.ngZone.run(function () {
                        _this.lpnCode = barcode;
                        _this.StockQuery();
                    });
                    break;
                case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].SKU:
                    _this.ngZone.run(function () {
                        _this.skuCode = barcode;
                        _this.StockQuery();
                    });
                    break;
                case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].BatchNo:
                    _this.ngZone.run(function () {
                        _this.lotNumber = barcode;
                        _this.StockQuery();
                    });
                    break;
                case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].SerialNumber:
                    _this.ngZone.run(function () {
                        _this.serialNumber = barcode;
                        _this.StockQuery();
                    });
                    break;
                case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].BoxCode:
                    _this.ngZone.run(function () {
                        _this.boxCode = barcode;
                        _this.StockQuery();
                    });
                    break;
            }
        });
    };
    StockqueryPage.prototype.ionViewWillLeave = function () {
        __WEBPACK_IMPORTED_MODULE_9__services_AppGlobal__["a" /* AppGlobal */].removeSubscribe(this);
    };
    /**
     * 选中列项
     * @param stockVO
     */
    StockqueryPage.prototype.itemInfo = function (stockVO) {
        if (this.isCheck) {
            stockVO.CheckStock = !stockVO.CheckStock;
        }
        else {
            if (stockVO.sku.isSn == 1) {
                this.navCtrl.push('StockdetailPage', { stockVO: stockVO });
            }
        }
    };
    StockqueryPage.prototype.keyEnter = function (event) {
        if (event && event.keyCode == 13) {
            this.resetPage();
            this.SearchQuery();
        }
    };
    StockqueryPage.prototype.sortAsc = function (sortName) {
        this.ascs = sortName;
        this.resetPage();
        this.SearchQuery();
    };
    StockqueryPage.prototype.resetPage = function () {
        this.stockVOList = [];
        if (this.infiniteScroll) {
            this.infiniteScroll.enable(true);
        }
        this.size = 10;
        this.current = 1;
    };
    StockqueryPage.prototype.SearchQuery = function () {
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(this.searchQuery)) {
            this.nativeService.showToast('请输入需要搜索的内容');
            return;
        }
        this.isResult = false;
        var bt = this.barCodeService.GetBarcodeType(this.searchQuery);
        this.queryInit();
        switch (bt) {
            case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].HuoWei:
                this.locCode = this.searchQuery;
                break;
            case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].TuoPan:
                this.lpnCode = this.searchQuery;
                break;
            case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].SKU:
                this.skuCode = this.searchQuery;
                break;
            case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].BatchNo:
                this.lotNumber = this.searchQuery;
                break;
            case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].SerialNumber:
                this.serialNumber = this.searchQuery;
                break;
            case __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["b" /* BarcodeType */].BoxCode:
                this.boxCode = this.searchQuery;
                break;
        }
        this.StockQuery();
    };
    StockqueryPage.prototype.StockQuery = function () {
        var _this = this;
        var params = {};
        if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.boxCode)) {
            this.storage.get("sysParam").then(function (v) {
                var flag = false;
                v.forEach(function (item) {
                    if (item.paramKey == 'pda.stock.keywords') {
                        flag = true;
                        var value = item.paramValue;
                        var valueSplit_1 = value.split(",");
                        var aa = valueSplit_1.filter(function (e) {
                            var boxstr = _this.boxCode.toLowerCase();
                            var str = e.toLowerCase();
                            return !(boxstr.indexOf(__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].clearSpace(str)) != -1);
                        });
                        if (aa && aa.length > 0) {
                            _this.nativeService.showToast("箱码格式不正确！");
                            return;
                        }
                        else {
                            _this.barcodeService.ScanSkuBarcode(_this.boxCode).subscribe(function (val) {
                                valueSplit_1.forEach(function (e) {
                                    var str = __WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].clearSpace(e).toLowerCase();
                                    if (str.indexOf("skulot") != -1) {
                                        var attrName = "skuLot" + str.substring(6, str.length);
                                        params[attrName] = val.skuLotModel[str];
                                    }
                                    else {
                                        if (str == 'skucode') {
                                            params['skuCode'] = val[str];
                                        }
                                    }
                                });
                                params["boxCode"] = "1";
                                _this.queryStockRequest(params);
                            });
                        }
                    }
                });
                if (!flag) {
                    _this.nativeService.showToast("您还未设置箱码扫描规则无法为您查询");
                }
            });
        }
        else {
            params['locCode'] = this.locCode,
                params['lpnCode'] = this.lpnCode,
                params['skuCode'] = this.skuCode,
                params['lotNumber'] = this.lotNumber,
                params['serialNumber'] = this.serialNumber;
            params["boxCode"] = "0";
            this.queryStockRequest(params);
        }
    };
    StockqueryPage.prototype.queryStockRequest = function (params) {
        var _this = this;
        this.nativeService.showLoading();
        this.appService.httpRequest(__WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].api + __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["b" /* Api */].StockQuery
            + "?current=" + this.current
            + "&size=" + this.size
            + "&ascs=" + this.ascs
            + "&type=" + params["boxCode"]
            + "&descs" + this.descs, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["f" /* Method */].Post, params, __WEBPACK_IMPORTED_MODULE_5__utils_appConstant__["e" /* ContenType */].Json, function (result) {
            _this.nativeService.hideLoading();
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isEmpty(result.data.records)) {
                if (_this.infiniteScroll) {
                    _this.infiniteScroll.enable(false);
                    _this.infiniteScroll.complete();
                }
                else {
                    _this.nativeService.showToast('暂无库存信息！');
                }
                return;
            }
            _this.isResult = true;
            _this.sortMenu = true;
            _this.stockVOList = _this.stockVOList.concat(result.data.records);
            _this.current++;
            if (_this.infiniteScroll) {
                _this.infiniteScroll.complete();
            }
        });
    };
    StockqueryPage.prototype.queryInit = function () {
        this.locCode = "";
        this.lpnCode = "";
        this.skuCode = "";
        this.lotNumber = "";
        this.serialNumber = "";
        this.boxCode = "";
    };
    /**
     * 是否显示打印
     */
    StockqueryPage.prototype.pritIsShow = function () {
        this.isCheck = !this.isCheck;
        this.isMore = !this.isMore;
        if (!this.isCheck && __WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockVOList)) {
            this.stockVOList = this.stockVOList.map(function (item, index, arr) {
                item.CheckStock = false;
                return item;
            });
        }
    };
    /**
     * 标签打印
     */
    StockqueryPage.prototype.btnOk = function () {
        if (this.isCheck) {
            //获取选择的库存
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(this.stockVOList)) {
                var stockVOListIsTrue = this.stockVOList.filter(function (x) { return x.CheckStock == true; });
                if (stockVOListIsTrue.length > 0) {
                    this.navCtrl.push('StockPritPage', { stockVOListIsTrue: stockVOListIsTrue });
                }
                else {
                    this.nativeService.showToast('请选择要打印的库位');
                }
            }
        }
        else {
            this.nativeService.showToast('请点击右上【打印】按钮开启打印');
        }
    };
    /**
     * 返回上一层
     */
    StockqueryPage.prototype.exit = function () {
        this.navCtrl.pop();
    };
    StockqueryPage.prototype.presentPopover = function (myEvent) {
        var _this = this;
        var popover = this.popoverCtrl.create('PopoverPage', { show_item_3: false });
        popover.onDidDismiss(function (data) {
            if (__WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(data) && __WEBPACK_IMPORTED_MODULE_4__services_Utils__["a" /* Utils */].isNotEmpty(_this.stockVOList)) {
                console.log(_this.stockVOList);
                if (data == 'allCheck') {
                    //全选
                    for (var index = 0; index < _this.stockVOList.length; index++) {
                        _this.stockVOList[index].CheckStock = true;
                    }
                }
                else if (data == 'clearCheck') {
                    //取消全选
                    for (var index = 0; index < _this.stockVOList.length; index++) {
                        _this.stockVOList[index].CheckStock = false;
                    }
                }
            }
        });
        popover.present({
            ev: myEvent
        });
    };
    StockqueryPage.prototype.skuStockIsCheckDel = function () {
        this.stockVOList = this.stockVOList.filter(function (x) { return x.CheckStock === false; });
    };
    StockqueryPage.prototype.loadData = function (infiniteScroll) {
        this.infiniteScroll = infiniteScroll;
        this.SearchQuery();
    };
    StockqueryPage = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'page-stockquery',template:/*ion-inline-start:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\stockhome\stockquery\stockquery.html"*/'<ion-header>\n\n    <ion-navbar color="wmscolor">\n\n        <ion-title>库存查询\n\n            <!-- <div class="title_f_name">(青啤五厂)</div> -->\n\n        </ion-title>\n\n        <ion-buttons end (click)="pritIsShow()">\n\n            <button ion-button icon-only>\n\n                <ion-icon name="menu"></ion-icon>\n\n                打印\n\n            </button>\n\n        </ion-buttons>\n\n    </ion-navbar>\n\n    <ion-searchbar [(ngModel)]="searchQuery" (keyup)="keyEnter($event)" Description=\'primary\' *ngIf="paramValue==0"\n\n        placeholder="库位/托盘/SKU/批次号/序列号">\n\n        <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n    </ion-searchbar>\n\n    <ion-searchbar [(ngModel)]="searchQuery" (keyup)="keyEnter($event)" Description=\'primary\' *ngIf="paramValue!=0"\n\n        placeholder="库位/SKU/批次号/序列号">\n\n        <ion-icon class="iconfont icon-saomiao"></ion-icon>\n\n    </ion-searchbar>\n\n    <div class="nav-tile">\n\n        <!-- <ul>\n\n            <li>首页</li>\n\n            <li>\n\n                <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n            </li>\n\n            <li>库存</li>\n\n            <li>\n\n                <ion-icon class="iconfont  iconicon_arr_right"></ion-icon>\n\n            </li>\n\n            <li>库存查询</li>\n\n        </ul> -->\n\n        <crumbs></crumbs>\n\n    </div>\n\n    <div class="sub_header1" >\n\n        <div (click)="sortAsc(\'sku_id\')">物品排序</div>\n\n        <div *ngIf="paramValue==0" (click)="sortAsc(\'lpn_code\')">托盘排序</div>\n\n        <div (click)="sortAsc(\'loc_id\')">库位排序</div>\n\n        <div (click)="sortAsc(\'wh_id\')">库房排序</div>\n\n        <div style="flex:0.2" *ngIf="isMore" (click)="presentPopover($event)"> \n\n            <ion-icon ios="md-more" md="md-more"></ion-icon>\n\n        </div>\n\n    </div>\n\n   \n\n    <!-- <div class="sub_header1" *ngIf="sysparms!=\'\'">\n\n        <div (click)="sortAsc(\'sku_id\')">物品排序</div>\n\n        <div (click)="sortAsc(\'loc_id\')">库位排序</div>\n\n        <div (click)="sortAsc(\'wh_id\')">库房排序</div>\n\n    </div> -->\n\n</ion-header>\n\n<ion-content>\n\n    <ion-list>\n\n        <div *ngIf="stockVOList&&stockVOList.length>0" style="text-align: right;">\n\n            <span>汇总数量：</span>\n\n            <span>{{stockVOList[0].totalQty}}</span>\n\n        </div>\n\n        <div class="list-item-box" *ngFor="let stockVO of stockVOList;" (click)="itemInfo(stockVO)">\n\n            <div class="list-item-wapper">   \n\n            <div class="left">\n\n                    <ul>\n\n                        <li><span class="bold title">{{stockVO.sku.skuName}}</span></li>\n\n                        <li><span class="gray">规格：{{stockVO.skuPackageVO.spec}}</span></li>\n\n                        <li><span class="gray">库位：</span>{{stockVO.location.locCode}}</li>\n\n                        <li *ngIf="paramValue==0"><span class="gray">容器：</span>{{stockVO.lpnCode}}</li>\n\n                        <li><span class="gray">货主：</span><span>{{stockVO.ownerName}}</span></li>\n\n                        <li *ngIf="stockVO.skuLot2!=\'\'&&stockVO.skuLot2!=\' \'"><span>{{stockVO.skuLot2}}</span></li>\n\n                        <li *ngIf="stockVO.skuLot4!=\'\'&&stockVO.skuLot4!=\' \'"><span>{{stockVO.skuLot4}}</span></li>\n\n                    </ul>\n\n                </div>\n\n                <div class="right">\n\n                    <ul>\n\n                        <li>{{stockVO.warehouse.whName}}</li>\n\n                        <li>总&ensp;&ensp;量：<span style="color: orange;">{{stockVO.stockQty}}</span></li>\n\n                        <li>可用量：<span style="color: orange;">{{stockVO.availableQty}}</span></li>\n\n                        <li>计量单位：{{stockVO.wsuName}}</li>\n\n                        <li *ngIf="isCheck" class="last"><ion-checkbox  slot="end" [(ngModel)]="stockVO.CheckStock"></ion-checkbox></li>\n\n                    </ul>\n\n                </div>\n\n            </div>\n\n        </div>\n\n    </ion-list>\n\n    <ion-infinite-scroll threshold="100px" (ionInfinite)="loadData($event)">\n\n        <ion-infinite-scroll-content\n\n          loadingSpinner="bubbles"\n\n          loadingText="加载更多...">\n\n        </ion-infinite-scroll-content>\n\n      </ion-infinite-scroll>\n\n    <!-- <div *ngFor="let stockVO of stockVOList;" class="title-ms">\n\n        <div class="title-ms-info" (click)="itemInfo(stockVO)">\n\n            <ul>\n\n                <li class="l-width2"><span class="bold title">{{stockVO.sku.skuName}}</span></li>\n\n                <li class="l-width1"><span class="gray">规格：{{stockVO.skuPackageVO.spec}}</span></li>\n\n                <li class="l-width1"><span class="s-right">{{stockVO.warehouse.whName}}</span></li>\n\n                <li class="l-width1"><span class="gray">库位：</span>{{stockVO.location.locCode}}</li>\n\n                <li class="l-width1"><span class="bold cyan">总量：</span><span\n\n                        class="bold orange">{{stockVO.stockQty}}</span></li>\n\n                <li class="l-width1"><span class="gray">容器：</span>{{stockVO.lpnCode}}</li>\n\n                <li class="l-width1"><span class="gray">可用量：</span>{{stockVO.availableQty}}</li>\n\n                <li class="l-width2"><span class="gray">货主：</span>{{stockVO.ownerName}}</li>\n\n            </ul>\n\n            <ion-icon name="arrow-forward" class="icon-right" *ngIf="stockVO.sku.isSn==1"></ion-icon>\n\n        </div>\n\n    </div> -->\n\n</ion-content>\n\n<ion-footer>\n\n    <div class="btn-box">\n\n        <button (click)="exit()" ion-button block class="btn-l">\n\n            返回[Esc]\n\n        </button>\n\n        <button (click)="btnOk()" ion-button block class="btn-d">\n\n            <!-- {{buttonEnt}}[Ent] -->\n\n            补打标签[Ent]\n\n        </button>\n\n    </div>\n\n\n\n</ion-footer>'/*ion-inline-end:"D:\developer_tools\workspace\wms_3.0_base_pda\src\pages\stockhome\stockquery\stockquery.html"*/,
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1_ionic_angular__["l" /* NavController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["p" /* PopoverController */],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["m" /* NavParams */],
            __WEBPACK_IMPORTED_MODULE_2__services_App_service__["a" /* AppService */],
            __WEBPACK_IMPORTED_MODULE_3__services_NativeService__["a" /* NativeService */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */],
            __WEBPACK_IMPORTED_MODULE_7__utils_common__["a" /* CommonService */],
            __WEBPACK_IMPORTED_MODULE_8__ionic_storage__["b" /* Storage */],
            __WEBPACK_IMPORTED_MODULE_0__angular_core__["NgZone"],
            __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["c" /* Events */],
            __WEBPACK_IMPORTED_MODULE_6__services_BarCodeService__["a" /* BarcodeService */]])
    ], StockqueryPage);
    return StockqueryPage;
}());

//# sourceMappingURL=stockquery.js.map

/***/ }),

/***/ 770:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "StockqueryPageModule", function() { return StockqueryPageModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ionic_angular__ = __webpack_require__(43);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__stockquery__ = __webpack_require__(1365);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__ = __webpack_require__(781);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};




var StockqueryPageModule = /** @class */ (function () {
    function StockqueryPageModule() {
    }
    StockqueryPageModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_2__stockquery__["a" /* StockqueryPage */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_1_ionic_angular__["g" /* IonicPageModule */].forChild(__WEBPACK_IMPORTED_MODULE_2__stockquery__["a" /* StockqueryPage */]),
                __WEBPACK_IMPORTED_MODULE_3__components_crumbs_crumbs_module__["a" /* CrumbsModule */]
            ],
            schemas: [__WEBPACK_IMPORTED_MODULE_0__angular_core__["NO_ERRORS_SCHEMA"]]
        })
    ], StockqueryPageModule);
    return StockqueryPageModule;
}());

//# sourceMappingURL=stockquery.module.js.map

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
//# sourceMappingURL=28.js.map