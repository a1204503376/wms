<template>
    <basic-container>
        <nodes-crud ref="curd"
                    v-model="form"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    :before-open="beforeOpen"
                    @on-load="onLoad"
                    @row-save="rowSave"
                    @on-del="rowDel"
                    @on-multi-del="onMultiDel"
                    @selection-change="selectionChange">
            <template slot="menuLeft">

            </template>
            <template slot="menuRight">

            </template>
            <template slot="menu">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage, getDetail, submit, remove} from "@/api/wms/outstock/soInventory";
import {group as group_1} from "./soInventory/group_1";
import {group as group_2} from "./soInventory/group_2";
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
                        prop: "soBillNo",
                        label: "单据编码",
                        search: true,
                        view: true,
                        width: 180,
                        sortable: true,
                    },
                    {
                        prop: "orderNo",
                        label: "上位系统单号",
                        width: 180,
                        search: true,
                    },
                    {
                        prop: "cName",
                        label: "客户",
                        search: true,
                        width: 150,
                        sortable: true,
                    },
                    {
                        prop: "billTypeCd",
                        label: "单据类型",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/billtype/list?ioType=O",
                        props: {
                            label: "billTypeName",
                            value: "billTypeCd",
                        },
                    },
                    {
                        prop: "soBillState",
                        label: "单据状态",
                        // search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" +
                            this.$dict.soBillState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        prop: "transportCode",
                        label: "发货方式",
                        width: 150,
                        sortable: true,
                        sortProp: "transportCode",
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" +
                            this.$dict.soTransportCode,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "发运状态",
                        prop: "shipState",
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" +
                            this.$dict.shipState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "同步状态",
                        prop: "syncState",
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.syncState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        prop: "address",
                        label: "客户地址",
                        width: 150,
                        sortable: true,
                    },
                    {
                        prop: "phone",
                        label: "客户电话",
                        width: 150,
                    },
                    {
                        prop: "contact",
                        label: "联系人",
                        width: 150,
                    },
                    {
                        label: "所属货主",
                        prop: "woId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId",
                        },
                    },
                    {
                        label: "发货库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId",
                        },
                    },
                    {
                        label: "所属机构",
                        prop: "deptId",
                        width: 100,
                        sortable: true,
                        type: "select-tree",
                        dicUrl: "/api/blade-system/dept/tree",
                        props: {
                            label: "title",
                            value: "id",
                            children: "children"
                        },
                        clearable: true
                    },
                    {
                        prop: "expressName",
                        label: "物流名称",
                        search: true,
                        width: 150,
                        sortable: true,
                    },
                    {
                        prop: "expressAddress",
                        label: "物流地址",
                        width: 150,
                        sortable: true,
                    },
                    {
                        prop: "expressPhone",
                        label: "物流电话",
                        width: 150,
                    },
                    {
                        label: "单据下发时间",
                        prop: "createTime",
                        width: 130,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "单据完成时间",
                        prop: "finishDate",
                        width: 130,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "发货完成时间",
                        prop: "transportDate",
                        width: 130,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "单据创建人",
                        prop: "createUser",
                        width: 150,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "备注",
                        prop: "soBillRemark",
                        width: 150,
                    },
                ],
                group: []
            },
        };
    },
    computed: {
    },
    mounted() {
        const lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            group_2.column[0].children.push({
                prop: 'skuLot' + i,
                label: '批属性' + i,
                width: 180
            })
        }
        this.option.group.push(group_1);
        this.option.group.push(group_2);
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
        }
    }
};
</script>

<style>
</style>
