<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            :permission="permissionList"
            @on-load="onLoad"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
            @search-change="searchChange"
        >
            <template slot="menuLeft">
                <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    @click="showSkuLotTraceDialog"
                    v-if="permission.stock_batchTrack"
                >批次跟踪
                </el-button>
                <el-button
                    icon="el-icon-printer"
                    type="success"
                    size="mini"
                    @click="showMakeupDialog"
                    v-if="permission.stock_reprintLabel"
                >补打标签
                </el-button>
                <el-dropdown trigger="click" @command="handleCommand" style="margin-left:10px;">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.stock_stockFreeze" command="1">库存冻结
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.stock_stockThaw" command="6">库存解冻
                        </el-dropdown-item>
                        <!--                        <el-dropdown-item v-if="permission.stock_batchFreezeOrThaw" command="2">批次冻结/解冻-->
                        <!--                        </el-dropdown-item>-->
                        <el-dropdown-item v-if="permission.stock_internalTransfer" command="3" divided
                                          icon="el-icon-connection">内部转移
                        </el-dropdown-item>
                        <el-dropdown-item
                            command="5"
                            divided
                            v-if="permission.stock_stockParameter"
                        >库存台账
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.stock_export" command="4" divided icon="el-icon-download">
                            导出
                        </el-dropdown-item>

                    </el-dropdown-menu>
                </el-dropdown>
<!--                <el-button-->
<!--                    style="margin-left:10px"-->
<!--                    type="primary"-->
<!--                    v-show="value1=='1'"-->
<!--                    icon="el-icon-plus"-->
<!--                    size="mini"-->
<!--                    @click="createRelenishmentHeader"-->
<!--                >创建补货单-->
<!--                </el-button>-->
<!--                <el-switch-->
<!--                    style="margin-left:10px"-->
<!--                    v-model="value1"-->
<!--                    active-value="1"-->
<!--                    inactive-value="0"-->
<!--                    @change="getRelenishment"-->
<!--                    active-text="待补货">-->
<!--                </el-switch>-->


                <el-radio-group v-model="skuLevel" style="margin-left:10px" size="mini" @change="getCountByLevel">
                    <el-radio-button label="30">托</el-radio-button>
                    <el-radio-button label="20">箱</el-radio-button>
                    <el-radio-button label="10">内包装</el-radio-button>
                    <el-radio-button label="1">基础计量单位</el-radio-button>
                </el-radio-group>

            </template>
            <template slot="occupyQtyUm" slot-scope="row">
                <el-link v-if="row.occupyQty>0" type="primary"
                         @click="showOccupyDialog(10, row.stockId)">{{ row.occupyQtyUm }}
                </el-link>
                <span v-else>{{ row.occupyQtyUm }}</span>
            </template>

            <template slot="stockQtyUm" slot-scope="row">
                <el-link v-if="row.stockQty > 0" type="primary"
                         @click="showInventoryDialog(row.stockId)">{{ row.stockQtyUm }}
                </el-link>
                <span v-else>{{ row.stockQtyUm }}</span>
            </template>
            <template slot="pickQty" slot-scope="row">
                <!--                <el-link v-if="row.pickQty > 0" type="primary"-->
                <!--                         @click="showShelvesDialog(row.stockId)">{{ row.pickQty }}-->
                <!--                </el-link>-->
                <!--                <span v-else>{{ row.pickQty }}</span>-->
                <span>{{ row.pickQty }}</span>
            </template>

            <template slot="stockStatus" slot-scope="row">
                <span v-if="row.stockStatus == 0">
                    {{ row.stockStatusDesc }}
                </span>
                <span v-else style="color:red;">
                    {{ row.stockStatusDesc }}
                </span>
            </template>
            <template slot="lotStatusDesc" slot-scope="row">
                <span v-if="row.lotStatus == 0">
                    {{ row.lotStatusDesc }}
                </span>
                <span v-else style="color:red;">
                    {{ row.lotStatusDesc }}
                </span>
            </template>
            <template v-slot:stock>
                库存汇总：{{ data[0] ? data[0].totalQty : 0 }}
            </template>
        </nodes-crud>
        <!--        <trace-dialog-->
        <!--            ref="deptDialogName"-->
        <!--            :traceShow="trace.visible"-->
        <!--            :whId="trace.whId"-->
        <!--            :sku="trace.sku"-->
        <!--            :lotNumber="trace.lotNumber"-->
        <!--            @on-result-change="changeIsShowDialog"-->
        <!--            @trace-cancel="childCancel"-->
        <!--        ></trace-dialog>-->
        <!-- 库存台账 -->
        <standing-book
            :visible="standingBook.visible"
            :searchData="standingBook.searchData"
            :data-source="standingBook.dataSource"
            @callback="callbackStandingBook"
        ></standing-book>
        <serial-dialog
            :serialShow="serial.visible"
            :stockId="serial.stockId"
            @serial-cancel="serialDialogResult"
        ></serial-dialog>
        <stockoccupy-dialog
            :isShowDialog="stockOccupy.visible"
            :stockId="stockOccupy.stockId"
            :type="stockOccupy.type"
            :titleText="stockOccupy.title"
            @child-cancel="occupyDialogResult"
        ></stockoccupy-dialog>

        <inventory-dialog
            :isShowDialog="inventory.visible"
            :stockId="inventory.stockId"
            @child-cancel="inventoryDialogResult"
        ></inventory-dialog>

        <shelves-dialog
            :isShowDialog="shelves.visible"
            :stockId="shelves.stockId"
            @child-cancel="shelvesDialogResult"
        ></shelves-dialog>
        <change-dialog
            :visible="changeDialog.visible"
            :data-source="changeDialog.dataSource"
            @callback="changeDialogResult"
        ></change-dialog>
    </basic-container>
</template>
<script>
import {getPage, lockByStock, lockByLot, exportFile, createRelenishOrder, unlockByStock} from "@/api/wms/core/stock";
import serialDialog from "./stock/serialDialog";
import stockoccupyDialog from "./stock/stockoccupyDialog";
import changeDialog from "./stock/changeDialog";
import {getParamValue} from "../../../util/param";
import fileDownload from "js-file-download";
import StandingBook from "./stock/standingBook";
import inventoryDialog from "./stock/inventoryDialog"
import shelvesDialog from "./stock/shelvesDialog";
import {mapGetters} from "vuex";

export default {
    name: "stock",
    components: {
        StandingBook,
        serialDialog,
        stockoccupyDialog,
        changeDialog,
        inventoryDialog,
        shelvesDialog
    },
    created() {
        // 获取批属性数量
        let lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 120,
                search: true,
                sortable: true,
                placeholder: '支持模糊查询',
            };
            this.option.column.push(skuLot);
        }
    },
    data() {
        return {
            loading: false,
            isView: false,
            isShow: false,
            skuLevel: '1',
            makeupSource: [],
            value1: '0',
            trace: {
                visible: false,
                sku: {}
            }, //批次跟踪弹框
            changeDialog: {
                visible: false,
                dataSource: {}
            },
            serial: {
                visible: false
            }, //序列号弹框
            makeup: {
                visible: false,
            },  //补打标签
            stockOccupy: {
                stockId: "",
                type: 10,
                title: "",
                visible: false
            }, //分配占用
            inventory: {
                stockId: "",
                visible: false
            },//库存数量明细
            shelves: {
                stockId: "",
                visible: false
            },//下架数量
            selectionList: [], //选中的数据
            standingBook: {
                visible: false,
                searchData: {
                    stockIds: ""
                }
            },
            data: [], //列表数据
            searchData: {},
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: true,
                menuItem: [
                    {
                        label: "序列号",
                        command: 1
                    },
                ],
                column: [
                    {
                        label: '库存状态',
                        prop: 'stockStatus',
                        search: true,
                        slot: true,
                        fixed: true,
                        type: 'select',
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.stockStatus,
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        }
                    },
                    {
                        label: "所属库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        },
                    },
                    {
                        label: "所属库区",
                        prop: "zoneName",
                    },
                    {
                        label: "库位编号",
                        prop: "locCode",
                        overHidden: true,
                        width: 150,
                        search: true,
                        sortable: true,
                        sortProp: 'locId'
                    },
                    {
                        label: "货主",
                        prop: "woId",
                        width: 120,
                        sortable: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId"
                        }
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        overHidden: true,
                        width: 140,
                        search: true,
                        sortable: true,
                        sortProp: 'skuId'
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        overHidden: true,
                        width: 200,
                        search: true,
                        sortable: true,
                        sortProp: 'skuId'
                    },
                    {
                        label: "包装",
                        prop: "wspName",
                        width: 180,
                        sortable: true,
                        sortProp: 'wspId',
                        search: true
                    },
                    {
                        label: "包装层级",
                        prop: "skuLevel",
                        width: 120,
                        hide:true,
                        overHidden: true,
                        multiple: false,
                        sortable: true,
                        sortProp: 'skuLevel',
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.skuLevel,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "可用数量",
                        prop: "availableQty",
                        search: false,
                    },
                    {
                        label: "库存数量",
                        prop: "stockQtyUm",
                        overHidden: true,
                        slot: true
                    },
                    {
                        label: "下架数量",
                        prop: "pickQty",
                        slot: true
                    },
                    {
                        label: "占用数量",
                        prop: "occupyQtyUm",
                        slot: true
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        overHidden: true,
                        width: 170,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "最近入库时间(库存数量增加时更新)",
                        prop: "lastInTime",
                        overHidden: true,
                        width: 280,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "最近出库时间(下架数量增加时更新)",
                        prop: "lastOutTime",
                        overHidden: true,
                        width: 280,
                        sortable: true,
                        type: 'date-picker'
                    },
                ]
            }
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                批次跟踪: this.vaildData(this.permission.stock_batchTrack, false),
                库存冻结: this.vaildData(this.permission.stock_stockFreeze, false),
                库存解冻: this.vaildData(this.permission.stock_stockThaw, false),
                // 批次冻结or解冻: this.vaildData(this.permission.batchFreezeOrThaw, false),
                内部转移: this.vaildData(this.permission.stock_internalTransfer, false),
                库存台账: this.vaildData(this.permission.stock_stockParameter, false),
                导出: this.vaildData(this.permission.stock_export, false),
                序列号: this.vaildData(this.permission.stock_serialNumber, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.stockId);
            });
            return ids.join(",");
        },
    },
    methods: {
        createRelenishmentHeader() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.loading = true;
            createRelenishOrder({
                ids: this.ids,
                whId: this.selectionList[0]['whId'],
                woId: this.selectionList[0]['woId'],
                woCode: this.selectionList[0]['woCode']
            }).then(data => {
                this.loading = false;
                if (data.data.success) {
                    this.$message.success(data.data.msg);
                }
            })
        },
        getRelenishment(val) {
            this.$refs.table.onLoad();
        },
        getCountByLevel(val) {
            this.data.forEach(res => {
                res.skuPackageDetailList.forEach(level => {
                    if (val == level.skuLevel) {
                        //可用数量
                        var count = parseInt(res.availableQtybak / level.convertQty);
                        //库存数量
                        var stockQty = parseInt(res.stockQtybak / level.convertQty);
                        //下架数量
                        var pickQty = parseInt(res.pickQtybak / level.convertQty);
                        //占用数量
                        var occupyQty = parseInt(res.occupyQtybak / level.convertQty);


                        var yu = res.availableQtybak % level.convertQty;
                        var yu1 = res.stockQtybak % level.convertQty;
                        var yu2 = res.pickQtybak % level.convertQty;
                        var yu3 = res.occupyQtybak % level.convertQty;
                        if (yu != 0) {
                            var base = res.availableQtybak - count * level.convertQty;
                            res.availableQty = count + level.wsuName + base + res.wsuName;
                        } else {
                            res.availableQty = count + level.wsuName;
                        }

                        if (yu1 != 0) {
                            var base1 = res.stockQtybak - stockQty * level.convertQty;
                            //res.stockQty = stockQty + level.wsuName + base1 + res.wsuName;
                            res.stockQtyUm=stockQty + level.wsuName + base1 + res.wsuName;
                        } else {
                            //res.stockQty = stockQty + level.wsuName;
                            res.stockQtyUm=stockQty + level.wsuName;
                        }
                        if (yu2 != 0) {
                            var base2 = res.pickQtybak - pickQty * level.convertQty;
                            res.pickQty = pickQty + level.wsuName + base2 + res.wsuName;
                        } else {
                            res.pickQty = pickQty + level.wsuName;
                        }
                        if (yu3 != 0) {
                            var base3 = res.occupyQtybak - occupyQty * level.convertQty;
                            res.occupyQtyUm= occupyQty + level.wsuName + base3 + res.wsuName;
                            // res.occupyQty = occupyQty + level.wsuName + base3 + res.wsuName;
                        } else {
                            // res.occupyQty = occupyQty + level.wsuName;
                            res.occupyQtyUm=occupyQty + level.wsuName;
                        }
                    }
                })
            })
        },
        //取消回调
        childCancel() {
            this.trace.visible = false;
        },
        //默认渲染数据
        onLoad(page, params = {}) {
            // params['isRelenish'] = this.value1;
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                const data = res.data.data;
                page.total = data.total;
                data.records.forEach(rec=>{
                    this.$set(rec, 'stockQtyUm', 0);
                    rec.stockQtyUm=0;
                    rec.occupyQtyUm=0;
                    //可用数量
                    rec.availableQtybak=rec.availableQty;
                    //库存数量
                    rec.stockQtybak=rec.stockQty;
                    //下架数量
                    rec.pickQtybak=rec.pickQty;
                    //占用数量
                    rec.occupyQtybak=rec.occupyQty;

                });
                this.data = data.records;
                this.getCountByLevel(this.skuLevel);
                this.loading = false;
                this.selectionClear();
            });
        },
        //选中的数据
        selectionChange(list) {
            if (this.selectionList && this.selectionList.length > 0 && this.value1 == '1') {
                let whId = this.selectionList[0]['whId'];
                let data = list.filter(item => item['whId'] != whId);
                if (data.length > 0) {
                    this.$refs.table.$refs.table.toggleRowSelection(data[0], false);
                    this.$message.warning("必须选择相同库房的才能创建补货单！");
                } else {
                    this.selectionList = list;
                }
            } else {
                this.selectionList = list;
            }
        },
        selectionClear() {
            this.selectionList = [];
        },
        changeIsShowDialog(val) {
            this.trace.visible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        //序列号弹框打开
        showSerialDialog(stockId) {
            this.serial.stockId = stockId;
            this.serial.visible = true;
        },
        //序列号弹框关闭
        serialDialogResult() {
            this.serial.visible = false;
        },
        randomSuccess() {
            this.makeup.visible = false;
            this.$refs.table.onLoad();
        },
        //补打标签弹框打开
        showMakeupDialog() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.makeup.visible = true;
        },
        //补打标签弹框关闭
        makeupDialogResult() {
            this.makeupSource = Object.assign([], this.selectionList);
            this.makeup.visible = false;
        },
        showOccupyDialog(type, stockId) {
            if (type === 10) this.stockOccupy.title = "分配占用明细";
            else if (type === 20) this.stockOccupy.title = "盘点占用明细";
            else if (type === 30) this.stockOccupy.title = "出库预约占用明细";
            this.stockOccupy.type = type;
            this.stockOccupy.stockId = stockId;
            this.stockOccupy.visible = true;
        },
        occupyDialogResult() {
            this.stockOccupy.visible = false;
        },
        //inventory
        showInventoryDialog(stockId) {
            this.inventory.stockId = stockId;
            this.inventory.visible = true;
        },
        inventoryDialogResult() {
            this.inventory.visible = false;
        },
        showShelvesDialog(stockId) {
            this.shelves.stockId = stockId;
            this.shelves.visible = true;
        },
        shelvesDialogResult() {
            this.shelves.visible = false;
        },
        changeDialogResult() {
            this.changeDialog.visible = false;
        },
        showSkuLotTraceDialog() {
            if (this.selectionList && this.selectionList.length > 0) {
                let row = this.selectionList[0];
                this.trace.whId = row.whId;
                this.trace.sku = {
                    skuId: row.skuId,
                    skuCode: row.skuCode,
                    skuName: row.skuName
                };
                this.trace.lotNumber = row.lotNumber;
            }
            this.trace.visible = true;
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.showSerialDialog(row.stockId);
                    break;
                case 2:
                    break;
            }
        },
        handleCommand(cmd) {
            let tag = parseInt(cmd);
            if (tag === 4) {
                this.loading = true;
                exportFile(this.searchData).then(res => {
                    this.$message.success('操作成功，正在下载中...');
                    fileDownload(res.data, "库存.xlsx");
                }).catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在")
                }).finally(() => {
                    this.loading = false;
                });
            } else {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一条信息才能执行此操作！");
                    return;
                }
                switch (tag) {
                    case 1: // 库存冻结/库存解冻
                        this.$confirm("是否确认 冻结 当前勾选库存？", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                            lockByStock(this.ids).then(res => {
                                this.$refs.table.onLoad();
                            }).finally(() => {
                                this.loading = false;
                            });
                        }).catch(() => {
                            this.loading = false;
                        });
                        break;
                    case 6: // 库存冻结/库存解冻
                        this.$confirm("是否确认 解冻 当前勾选库存？", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                            unlockByStock(this.ids).then(res => {
                                this.$refs.table.onLoad();
                            }).finally(() => {
                                this.loading = false;
                            });
                        }).catch(() => {
                            this.loading = false;
                        });
                        break;
                    case 2: // 批次冻结/解冻
                        this.$confirm("是否确认 冻结/解冻 当前勾选的批次？", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                            lockByLot(this.ids).then(res => {
                                this.$refs.table.onLoad();
                            }).finally(() => {
                                this.loading = false;
                            });
                        }).catch(() => {
                            this.loading = false;
                        });
                        break;
                    case 3: // 内部转移
                        this.changeDialog.dataSource = {
                            stockList: Object.assign([], this.selectionList)
                        };
                        this.changeDialog.visible = true;
                        break;
                    case 5:
                        this.standingBook.searchData.stockIds = this.ids;
                        this.standingBook.visible = true;
                        break;
                }
            }
        },
        searchChange(data) {
            data['isSearch'] = '1';
            this.searchData = data;
        },
        callbackStandingBook(res) {
            this.standingBook.visible = res.visible;
        },
    },
};
</script>
<style lang="scss">
</style>
