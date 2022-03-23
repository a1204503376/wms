<template>
    <basic-container>
        <nodes-crud
            ref="table"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :permission="permissionList"
            :before-open="beforeOpen"
            @on-load="onLoad"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
        >
            <template slot="menuLeft" slot-scope="scope">
                <el-button v-if="permission.so_header_confirmDelivery" type="primary" size="mini" icon="el-icon-plus" @click="completeSo(scope.row)">确认发货
                </el-button>
                <!-- <el-button v-if="permission.so_header_printReceipt" type="primary" size="mini" icon="el-icon-printer">打印收货单</el-button>
                <el-button v-if="permission.so_header_printDeliver" type="primary" size="mini" icon="el-icon-printer">打印配送单</el-button> -->
            </template>
        </nodes-crud>
        <cancel-loading
            :isShowDialogs="cancelLoadingVisible"
            :truckId="truckId"
            @randomchange="loadChange"
            @randomSuccess="loadSuccess"
            @randomCancel="loadCancel"
        ></cancel-loading>
    </basic-container>
</template>

<script>
import {getPage, confirmSo, getDetail} from "@/api/wms/outstock/soTruckHeader";
import {group as group_1} from "./truckHeader/group_1";
import {group as group_2} from "./truckHeader/group_2";
import {getParamValue} from "@/util/param";
import  cancelLoading from "./truckHeader/cancelLoading"
import {mapGetters} from "vuex";

export default {
    name: "truckHeader",
    components: {cancelLoading},
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.so_header_add, false),
                view: this.vaildData(this.permission.so_header_view, false),
                delete: this.vaildData(this.permission.so_header_delete, false),
                edit: this.vaildData(this.permission.so_header_edit, false),
                确认发货: this.vaildData(this.permission.so_header_confirmDelivery, false),
                打印收货单: this.vaildData(this.permission.so_header_printReceipt, false),
                打印配送单: this.vaildData(this.permission.so_header_printDeliver, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.truckId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
        // 获取批属性数量
        let lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 120,
                search: true
            };
            this.option.group[1].column[0].children.push(skuLot);
        }
    },
    data() {
        return {
            cancelLoadingVisible:false,
            truckId: "",
            operation: false,
            operationButton: [],
            loading: false,
            data: [],
            selectionList: [],
            form: {},
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: true,
                custom: false,
                menuItem: [
                    {
                        label: "撤销装车",
                        command: 1,
                    },
                ],
                column: [
                    {
                        label: "车次编号",
                        prop: "truckCode",
                        width: 200,
                        view: true,
                        sortable: true,
                    },
                    {
                        label: "车次状态",
                        prop: "truckState",
                        width: 140,
                        type: "select",
                        sortable: true,
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.truckState,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        },
                    },
                    {
                        label: "车牌号",
                        prop: "plateNumber",
                        search: true,
                        searchLabelWidth: 50,
                        width: 120,
                        sortable: true,
                    },
                    {
                        label: "司机",
                        prop: "driverName",
                        search: true,
                        width: 140,
                        sortable: true,
                    },
                    {
                        label: "电话",
                        prop: "driverPhone",
                        width: 150
                    },
                    {
                        label: "运单编号",
                        prop: "truckHeaderWaybill",
                        width: 200
                    },
                    {
                        label: "装车时间",
                        prop: "truckDate",
                        width: 200,
                        type: 'date-picker'
                    },
                    {
                        label: "备注",
                        prop: "truckRemark",
                        width: 220
                    }
                ],
                group: []
            }
        }
    },
    methods: {
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                let data = res.data.data;
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
        // 确认发货
        completeSo() {
            if (this.selectionList.length === 0) {
                this.$message.warning("请选择一条数据");
                return;
            }
            confirmSo(this.ids).then(
                () => {
                    this.$refs.table.page.currentPage = 1;
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                }
            );
        },
        // 二级界面打开
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.truckId).then(res => {
                    this.form = res.data.data;
                }).finally(() => {
                    done();
                });
            }
        },
        //成功回调
        loadSuccess() {
            this.cancelLoadingVisible = false;
            this.$refs.table.onLoad();
        },
        //取消回调
        loadCancel() {
            this.cancelLoadingVisible = false;
        },
        loadChange(val) {
            this.cancelLoadingVisible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        // 操作按钮事件
        menuCommand(cmd, row) {
            switch (cmd) {
                case 1: // 撤销装车
                    this.cancelLoadingVisible = true;
                    this.truckId = row.truckId;
                    break;
            }
        },
    }
}
</script>
<style lang="scss">
</style>
