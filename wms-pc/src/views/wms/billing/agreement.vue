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
                <el-button type="error" size="mini" v-if="permission.billing_agreement_termination">终止</el-button>
                <el-button type="success" size="mini" v-if="permission.billing_agreement_print">打印协议</el-button>
            </template>
            <template slot="menuRight">

            </template>
            <template slot="menu">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage, getDetail, submit, remove} from "@/api/wms/billing/agreement";
import {group as group_1} from "./agreement/group_1";
import {mapGetters} from "vuex";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            data: [],
            selectionList: [],
            option: {
                newBtn: true,
                multiDelBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                column: [
                    {
                        label: "协议号",
                        prop: "agreementNo",
                        sortable: true,
                        search: true,
                        view: true,
                        width: 140
                    },
                    {
                        label: "货主",
                        prop: "woId",
                        sortable: true,
                        sortProp: 'woId',
                        search: true,
                        width: 160,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId"
                        }
                    },
                    {
                        label: "生效日期",
                        prop: "effectiveDate",
                        sortable: true,
                        search: true,
                        width: 100,
                        type: 'date-picker'
                    },
                    {
                        label: "解约日期",
                        prop: "terminationDate",
                        sortable: true,
                        search: true,
                        width: 100,
                        type: 'date-picker'
                    },
                    {
                        label: "计费规则",
                        prop: "ruleHeaderId",
                        sortable: true,
                        type: 'select',
                        width: 120,
                        dicUrl:'/api/wms/billing/rule-header/list',
                        props:{
                            label: 'billingName',
                            value: 'id'
                        }
                    },
                    {
                        label: "执行状态",
                        prop: "executeState",
                        sortable: true,
                        sortProp: 'executeState',
                        width: 120,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.billingExecuteState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "协议状态",
                        prop: "agreementState",
                        sortable: true,
                        sortProp: 'agreementState',
                        search: true,
                        width: 120,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.billingAgreementState,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "自动续约状态",
                        prop: "isAuto",
                        sortable: true,
                        width: 140,
                        type: "select",
                        dicData:[
                            {
                                label:'是',
                                value:true
                            },
                            {
                                label:'否',
                                value:false
                            }
                        ]
                    },
                    {
                        label: "续约次数",
                        prop: "agreementCount",
                        sortable: true,
                        search: true,
                        width: 120
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
                add: this.vaildData(this.permission.billing_agreement_add, false),
                view: this.vaildData(this.permission.billing_agreement_view, false),
                delete: this.vaildData(this.permission.billing_agreement_delete, false),
                edit: this.vaildData(this.permission.billing_agreement_edit, false),
                终止: this.vaildData(this.permission.billing_agreement_termination, false),
                打印协议: this.vaildData(this.permission.billing_agreement_print, false),
            };
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.id);
            });
            return ids.join(",");
        }
    },
    mounted() {
        this.option.group.push(group_1);
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
            });
        }
    }
};
</script>

<style>
</style>
