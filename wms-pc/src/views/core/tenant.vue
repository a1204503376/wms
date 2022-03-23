<template>
    <basic-container>
        <nodes-crud
                ref="table"
                :option="option"
                :data="data"
                v-model="form"
                :table-loading="loading"
                :permission="permissionList"
                @on-load="onLoad"
                @on-del="onDel"
                @row-save="rowSave"
                :before-open="beforeOpen"
                @on-multi-del="onMultiDel"
                @selection-change="selectionChange"
        >
            <template slot="menuLeft">
                <el-button size="mini"
                           plain
                           icon="el-icon-setting"
                           v-if="permission.tenant_authorization"
                           @click="handleSetting">授权配置
                </el-button>
            </template>

            <template slot-scope="row"
                      slot="accountNumber">
                <el-tag>{{ row.accountNumber > 0 ? row.accountNumber : '不限制' }}</el-tag>
            </template>
            <template slot-scope="row"
                      slot="expireTime">
                <el-tag>{{ row.expireTime ? row.expireTime : '不限制' }}</el-tag>
            </template>
        </nodes-crud>
        <auth :visible="dialogAuth.visible"
              :ids="dialogAuth.ids"
              :data-source="dialogAuth.dataSource"
              @callback="callbackAuth"></auth>
    </basic-container>
</template>
<script>
    import {getList, add, remove, getDetail} from "@/api/core/tenant";
    import auth from "./tenant/auth";
    import {mapGetters} from "vuex";

    export default {
        name: "tenant",
        components: {auth},
        data() {
            return {
                titleText: "",
                dialogVisible: false,
                loading: false,
                isView: false,
                isShow: false,
                selectionList: [], //选中的数据
                data: [],
                form: {},
                dialogAuth: {
                    visible: false,
                    selectionList:[],
                    dataSource: {}
                },
                option: {
                    newBtn: true,
                    multiDelBtn: true,
                    viewBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    custom: false,
                    group: [
                        {
                            column: [
                                {
                                    label: "租户名称",
                                    prop: "tenantName",
                                    search: true,
                                    maxlength:45,
                                    rules: [
                                        {
                                            required: true,
                                            message: "租户名称不能为空",
                                            trigger: "blur", pattern: /\S/,
                                        }
                                    ]
                                },
                                {
                                    label: "联系人",
                                    prop: "linkman",
                                    search: true,
                                    maxlength:20,
                                    rules: [
                                        {required: true, message: "联系人不能为空", trigger: "blur", pattern: /\S/,}
                                    ]
                                },
                                {
                                    label: "联系电话",
                                    prop: "contactNumber",
                                    maxlength:20,
                                    rules: [
                                        {
                                            required: false,
                                            pattern: /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/,
                                            trigger: "blur",
                                            message: "请输入正确电话"
                                        }
                                    ]
                                },
                                {
                                    label: "联系地址",
                                    prop: "address",
                                    type: "textarea",
                                    maxlength:255,
                                    rules: [
                                        {
                                            required: true,
                                            message: "联系地址不能为空",
                                            trigger: "blur",
                                            pattern: /\S/,
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    column: [
                        {
                            label: "租户ID",
                            prop: "tenantId",
                            search: true,
                            view: true,
                            sortable: true,
                        },
                        {
                            label: "租户名称",
                            prop: "tenantName",
                            search: true,
                            sortable: true,
                        },
                        {
                            label: "联系人",
                            prop: "linkman",
                            search: true
                        },
                        {
                            label: "联系电话",
                            prop: "contactNumber"
                        },
                        {
                            label: "联系地址",
                            prop: "address"
                        },
                        {
                            label: '账号额度',
                            prop: 'accountNumber',
                            slot: true
                        },
                        {
                            label: '过期时间',
                            prop: 'expireTime',
                            width: 180,
                            slot: true,
                            type: 'date-picker'
                        },
                        {
                            label: '库房上限',
                            prop: 'whMax'
                        }
                    ]
                }
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.tenant_add, false),
                    view: this.vaildData(this.permission.tenant_view, false),
                    delete: this.vaildData(this.permission.tenant_delete, false),
                    edit: this.vaildData(this.permission.tenant_edit, false),
                    授权设置: this.vaildData(this.permission.tenant_authorization, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.id);
                });
                return ids.join(",");
            }
        },
        methods: {
            //默认渲染数据
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
            beforeOpen(done, type, finish) {
                //console.log(done,type,finish,'done')
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.id)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .catch(() => {
                        })
                        .finally(() => {
                            done();
                        });
                }
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.id).then(() => {
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
            //选中的数据
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            },
            handleSetting() {
                if (this.selectionList.length === 0) {
                    this.$message.warning("请选择至少一条数据");
                    return;
                }
                this.dialogAuth.ids = this.ids;

                if (this.selectionList.length === 1) {
                    getDetail(this.selectionList[0].id).then(res => {
                        this.dialogAuth.dataSource = res.data.data;
                    });
                } else {
                    this.dialogAuth.dataSource = {};
                    this.dialogAuth.dataSource.accountNumber = -1;
                    this.dialogAuth.dataSource.expires = '';
                    this.dialogAuth.dataSource.whMax = 1;
                }
                this.dialogAuth.visible = true;
            },
            callbackAuth(res) {
                this.dialogAuth.visible = res.visible;
                if (res.success) {
                    this.$refs.table.onLoad();
                }
            }
        }
    };
</script>
