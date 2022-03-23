<template>
    <basic-container>
        <nodes-crud
                ref="table"
                v-model="form"
                :option="option"
                :data="data"
                :table-loading="loading"
                :before-open="beforeOpen"
                :permission="permissionList"
                @on-load="onLoad"
                @row-save="rowSave"
                @on-del="onDel"
                @on-multi-del="onMultiDel"
                @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>

<script>
    import {
        getList,
        getDetail,
        add,
        remove,
    } from "@/api/wms/outstock/soRegister";
    import {getList as getPlatformList} from "@/api/wms/warehouse/platforminfo"
    import {mapGetters} from "vuex";

    export default {
        data() {
            return {
                form: {},
                query: {},
                loading: true,
                selectionList: [],
                option: {
                    newBtn: true,
                    multiDelBtn: true,
                    viewBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    custom: false,
                    column: [
                        {
                            label: "库房",
                            prop: "whId",
                            sortable: true,
                            search: true,
                            type: "select",
                            dicUrl: "/api/wms/warehouse/warehouse/list",
                            props: {
                                label: "whName",
                                value: "whId",
                            },
                        },
                        {
                            label: "装车牌",
                            prop: "loadId",
                            sortable: true,
                        },
                        {
                            label: "月台编号",
                            prop: "platformCode",
                            sortable: true,
                        },
                        {
                            label: "月台名称",
                            prop: "platformName",
                            sortable: true,
                        },
                        {
                            label: "司机名称",
                            prop: "driverName",
                            sortable: true,
                        },
                        {
                            label: "联系电话",
                            prop: "driverPhone"
                        },
                        {
                            label: "车牌号",
                            prop: "plateNumber",
                            sortable: true,
                        }
                    ],
                    group: [
                        {
                            column: [
                                {
                                    label: "库房",
                                    prop: "whId",
                                    type: "select",
                                    dicUrl: "/api/wms/warehouse/warehouse/list",
                                    props: {
                                        label: "whName",
                                        value: "whId"
                                    },
                                    cascaderItem: ['platformCode'],
                                    rules: [
                                        {
                                            required: true,
                                            message: "库房不能为空",
                                            trigger: "change"
                                        }
                                    ],
                                    change: (val, obj, col, data) => {
                                        let params = {
                                            whId: val,
                                            platformTypeStr: '2,3'
                                        }
                                        getPlatformList(params).then(res => {
                                            if (col.cascader) {
                                                col.cascader.forEach(item => {
                                                    if (item.prop === 'platformCode') {
                                                        item.dicData = res.data.data;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                },
                                {
                                    label: "装车牌",
                                    prop: "loadId",
                                    type:'number',
                                    maxlength: 20,
                                    rules: [
                                        {
                                            required: true,
                                            message: "请输入装车牌",
                                            trigger: "blur",
                                        }
                                    ]
                                },
                                {
                                    label: "发货月台",
                                    prop: "platformCode",
                                    type: "select",
                                    props: {
                                        label: "platformName",
                                        value: "platformCode"
                                    }
                                },
                                {
                                    label: "司机名称",
                                    prop: "driverName",
                                    maxlength: 50
                                },
                                {
                                    label: "联系电话",
                                    prop: "driverPhone",
                                    maxlength: 50
                                },
                                {
                                    label: "车牌号",
                                    prop: "plateNumber",
                                    maxlength: 50,
                                    rules: [
                                        {
                                            pattern: /[\w\u4e00-\u9fa5]/,
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                data: []
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.soregister_add, false),
                    view: this.vaildData(this.permission.soregister_view, false),
                    delete: this.vaildData(this.permission.soregister_delete, false),
                    edit: this.vaildData(this.permission.soregister_edit, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.trrId);
                });
                return ids.join(",");
            }
        },
        methods: {
            onLoad(page, params = {}) {
                this.loading = true;
                getList(
                    page.currentPage,
                    page.pageSize,
                    Object.assign(params, this.query)
                ).then(res => {
                    const data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            },
            rowSave(row, loading, done, type) {
                add(row).then(
                    () => {
                        loading();
                        this.$refs.table.onLoad();
                        this.$message.success("操作成功！");
                    },
                    error => {
                        done();
                    }
                );
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.trrId).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                    });
                });
            },
            onMultiDel() {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一条数据！");
                    return;
                }
                this.$confirm("确定将选择数据删除?", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(this.ids).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                        this.$refs.table.toggleSelection();
                    });
                });
            },
            beforeOpen(done, type) {
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.trrId)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .finally(() => {
                            done();
                        });
                }
            },
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            }
        }
    };
</script>

<style>
</style>
