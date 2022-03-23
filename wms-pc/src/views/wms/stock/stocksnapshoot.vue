<template>
    <basic-container>
        <nodes-crud ref="curd"
                    v-model="form"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    :before-open="beforeOpen"
                    :permission="permissionList"
                    @on-load="onLoad"
                    @row-save="rowSave"
                    @on-del="rowDel"
                    @on-multi-del="onMultiDel"
                    @selection-change="selectionChange">
            <template slot="menuLeft">
                <el-button type="primary" size="mini"
                           style="margin-left:0px;margin-right:10px;"
                           @click="initialize">初始化快照
                </el-button>
                <el-button type="primary" size="mini"
                           style="margin-left:0px;margin-right:10px;"
                           @click="stockSnapshoot">执行快照
                </el-button>
            </template>
            <template slot="menuRight">

            </template>
            <template slot="menu">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage, getDetail, submit, remove, initialize, stockSnapshoot} from "@/api/wms/stock/stocksnapshoot";
// import {group as group_1} from "@/pages/views/stock_snapshoot/stocksnapshoot/group_1";
import {mapGetters} from "vuex";
import {getParamValue} from "@/util/param";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            data: [],
            selectionList: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                editBtn: false,
                delBtn: false,
                menu: false,
                custom: false,
                column: [
                    {
                        label: "快照日期",
                        prop: "snapshootDate",
                        width: 100,
                        view: true,
                        sortable: true,
                        search: true,
                        type: 'date-picker'
                    },
                    {
                        label: "库房编码",
                        prop: "whCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "库房名称",
                        prop: "whId",
                        width: 100,
                        sortable: true,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        }
                    },
                    {
                        label: "库区编码",
                        prop: "zoneCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "库区名称",
                        prop: "zoneName",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "库位编码",
                        prop: "locCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "包装名称",
                        prop: "wspName",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "物品包装层级",
                        prop: "skuLevel",
                        width: 120,
                        sortable: true,
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.skuLevel,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                        show:['skuLevelName']
                    },
                    {
                        label: "库存数量",
                        prop: "stockQty",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "分配占用数量",
                        prop: "occupyQty",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "待上架数量",
                        prop: "onShelfQty",
                        width: 110,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "下架数量",
                        prop: "pickQty",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "盘点占用数量",
                        prop: "countOccupyQty",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "结余数量",
                        prop: "qty",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "差异数量",
                        prop: "diffQty",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "容器编码",
                        prop: "lpnCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "箱号",
                        prop: "boxCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    // {
                    //     label: "订单明细ID",
                    //     prop: "billDetailId",
                    //     width: 120,
                    //     sortable: true,
                    //     search: true
                    // },
                    {
                        label: "库存状态",
                        prop: "stockStatus",
                        width: 100,
                        sortable: true,
                        search: true,
                        type:'select',
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.stockStatus,
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        }
                    },
                    {
                        label: "上一次入库时间",
                        prop: "lastInTime",
                        width: 130,
                        sortable: true,
                        search: true,
                        type: 'date-picker'
                    },
                    {
                        label: "上一次出库时间",
                        prop: "lastOutTime",
                        width: 130,
                        sortable: true,
                        search: true,
                        type: 'date-picker'
                    },
                    // {
                    //     label: "批属性1",
                    //     prop: "skuLot1",
                    //     width: 100,
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性2",
                    //     prop: "skuLot2",
                    //     width: 100,
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性3",
                    //     prop: "skuLot3",
                    //     width: 100,
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性4",
                    //     prop: "skuLot4",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性5",
                    //     prop: "skuLot5",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性6",
                    //     prop: "skuLot6",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性7",
                    //     prop: "skuLot7",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性8",
                    //     prop: "skuLot8",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性9",
                    //     prop: "skuLot9",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性10",
                    //     prop: "skuLot10",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性11",
                    //     prop: "skuLot11",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性12",
                    //     prop: "skuLot12",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性13",
                    //     prop: "skuLot13",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性14",
                    //     prop: "skuLot14",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性15",
                    //     prop: "skuLot15",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性16",
                    //     prop: "skuLot16",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性17",
                    //     prop: "skuLot17",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性18",
                    //     prop: "skuLot18",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性19",
                    //     prop: "skuLot19",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性20",
                    //     prop: "skuLot20",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性21",
                    //     prop: "skuLot21",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性22",
                    //     prop: "skuLot22",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性23",
                    //     prop: "skuLot23",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性24",
                    //     prop: "skuLot24",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性25",
                    //     prop: "skuLot25",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性26",
                    //     prop: "skuLot26",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性27",
                    //     prop: "skuLot27",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性28",
                    //     prop: "skuLot28",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性29",
                    //     prop: "skuLot29",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "批属性30",
                    //     prop: "skuLot30",
                    //     sortable: true,
                    //     search: true
                    // },
                    // {
                    //     label: "货主ID",
                    //     prop: "woId",
                    //     sortable: true,
                    //     search: true
                    // },
                    {
                        label: "货主编码",
                        prop: "ownerCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "货主名称",
                        prop: "woId",
                        width: 100,
                        sortable: true,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId"
                        }
                    },
                ],
                group: []
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                view: this.vaildData(this.permission.stocksnapshoot_view, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
            });
            return ids.join(",");
        }
    },
    mounted() {
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
    methods: {
        rowSave(row, loading, done, type) {
            submit(row).then(() => {
                loading();
                this.$refs.curd.onLoad();
                this.$message({
                    type: "success",
                    message: "操作成功!"
                });
            }, error => {
                done();
                console.log(error);
            });
        },
        rowDel(row, index) {
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.id).then(() => {
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                });
            });
        },
        onMultiDel() {
            if (!this.selectionList || this.selectionList.length === 0) {
                this.$message.warning("请选择至少一条数据");
                return;
            }
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                    this.$refs.curd.toggleSelection();
                });
            });
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.id).then(res => {
                    this.form = res.data.data;
                }).finally(() => {
                    done();
                });
            }
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.curd.toggleSelection();
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(page.currentPage, page.pageSize, params).then(res => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        initialize() {
            initialize().then(() => {
                this.$refs.curd.onLoad();
                this.$message({
                    type: "success",
                    message: "操作成功!"
                });
            });
        },
        stockSnapshoot() {
            stockSnapshoot().then(() => {
                this.$refs.curd.onLoad();
                this.$message({
                    type: "success",
                    message: "操作成功!"
                });
            });
        },
    }
};
</script>

<style>
</style>
