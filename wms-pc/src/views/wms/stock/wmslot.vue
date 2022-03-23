<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :permission="permissionList"
            :table-loading="loading"
            :hide="false"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template slot="lotStatus" slot-scope="row">
                <span v-if="row.lotStatus == 0">
                    {{ row.lotStatusDesc }}
                </span>
                <span v-else style="color:red;">
                    {{ row.lotStatusDesc }}
                </span>
            </template>
            <template slot="menuLeft">
                <el-button type="primary" size="mini"
                           style="margin-left:0px;margin-right:10px;"
                           v-if="permission.wmslot_lock"
                           @click="lotLock">批次冻结
                </el-button>
                <el-button type="primary" size="mini"
                           style="margin-left:0px;margin-right:10px;"
                           v-if="permission.wmslot_unLock"
                           @click="lotUnlock">批次解冻
                </el-button>
            </template>
        </nodes-crud>

    </basic-container>
</template>

<script>
import {getPage} from "@/api/wms/stock/batch";
import {getParamValue} from "../../../util/param";
import {lotLock, lotunLock} from "@/api/wms/core/stock";
import {mapGetters} from "vuex";

export default {
    name: "wmslot",
    components: {},
    data() {
        return {
            value1: '0',
            isView: false,
            isShow: true,
            dialogs: false, //搜索界面的显隐
            loading: false, //加载中界面
            selectionList: [], //选中的数据
            query: {},
            data: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                column: [
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
                        label: "批次号",
                        prop: "lotNumber",
                        search: true,
                        width: 170,
                        sortable: true,
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        search: true,
                        width: 130,
                        sortable: true,
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        search: true,
                        width: 170,
                        sortable: true,
                    },
                    {
                        label: "批次状态",
                        prop: "lotStatus",
                        search: true,
                        width: 170,
                        sortable: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.lotStatus,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                ]
            }
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                批次冻结: this.vaildData(this.permission.wmslot_lock, false),
                批次解冻: this.vaildData(this.permission.wmslot_unLock, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.id);
            });
            return ids.join(",");
        },
        lotNumber() {
            let lotNumbers = [];
            this.selectionList.forEach(ele => {
                lotNumbers.push(ele.lotNumber);
            });
            return lotNumbers.join(",");
        }
    },
    created() {
        let lotCount = getParamValue(this.$param.system.lotCount);
        let column = this.option.column;
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "物品批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 140,
                sortable: true,
                search: true,
                placeholder: '支持模糊查询',
            };
            column.push(skuLot)
        }
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                console.log(res);
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        getRelenishment() {
            this.$refs.table.onLoad();
        },
        lotLock(){
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.$confirm("是否确认 冻结 当前勾选批次？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                console.log(this.lotNumber);
                lotLock(this.lotNumber).then(res => {
                    this.$message.success('操作成功');
                    this.$refs.table.onLoad();
                }).finally(() => {
                    this.loading = false;
                });
            }).catch(() => {
                this.loading = false;
            });
        },
        lotUnlock(){
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.$confirm("是否确认 解冻 当前勾选批次？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                lotunLock(this.lotNumber).then(res => {
                    this.$message.success('操作成功');
                    this.$refs.table.onLoad();
                }).finally(() => {
                    this.loading = false;
                });
            }).catch(() => {
                this.loading = false;
            });
        },
    },
};
</script>
<style lang="scss">
</style>
