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
                @search-change="searchChange"
        >
            <template slot="menuLeft">
                <el-dropdown trigger="click" @command="handleCommand">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.location_printLabel" command="0" icon="el-icon-printer">打印标签</el-dropdown-item>
                        <el-dropdown-item v-if="permission.location_import" command="1" icon="el-icon-upload2" divided>导入</el-dropdown-item>
                        <el-dropdown-item v-if="permission.location_export" command="2" icon="el-icon-download">导出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/warehouse/location/export-template"
            file-name="库位"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/warehouse/location/import-data"
            dataVerifyUrl="/api/wms/warehouse/location/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>
<script>
    import {getPage, add, remove, getDetail, print, exportFile} from "@/api/wms/warehouse/location";
    import {getDetail as getParamDetail} from "@/api/core/param";
    import {getList as getZoneList} from "@/api/wms/warehouse/zone";
    import {group as group_1} from "./location/group_1";
    import {group as group_2} from "./location/group_2";
    import {group as group_3} from "./location/group_3";
    import fileDownload from "js-file-download";

    import fileUpload from "@/components/nodes/fileUpload";
    import dataVerify from "@/components/nodes/dataVerify";
    import {mapGetters} from "vuex";

    export default {
        name: "location",
        components: {
            fileUpload,
            dataVerify
        },
        data() {
            return {
                loading: false,
                selectionList: [], //选中的数据
                sptList: [], //模板数据
                form: {},
                data: [],
                fileUpload: {
                    visible: false
                },
                dataVerify: {
                    visible: false,
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
                    column: [
                        {
                            label: "库位编码",
                            prop: "locCode",
                            search: true,
                            view: true,
                            sortable: true,
                            placeholder: '支持模糊查询',
                        },
                        {
                            label: "所属库房",
                            prop: "whId",
                            search: true,
                            type: "select",
                            dicUrl: "/api/wms/warehouse/warehouse/list",
                            props: {label: "whName", value: "whId"},
                            cascaderItem: ['zoneId'],
                            change: function (val, obj, col) {
                                if (!val) {
                                    return;
                                }
                                getZoneList(val).then(res => {
                                    if (col && col.cascader) {
                                        col.cascader.forEach(item => {
                                            if (item.prop === 'zoneId') {
                                                item.dataSource = res.data.data;
                                            }
                                        });
                                    }
                                });
                            }
                        },
                        {
                            label: "所属库区",
                            prop: "zoneId",
                            search: true,
                            dataSource: [],
                            type: "select",
                            dicUrl: "/api/wms/warehouse/zone/list",
                            props: {
                                label: "zoneName",
                                value: "zoneId"
                            }
                        },
                        {
                            label: "应用类型",
                            prop: "locType",
                            search: true,
                            type: "select",
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.locType,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            }
                        },
                        {
                            label: "库位种类",
                            prop: "locCategory",
                            search: true,
                            type: "select",
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.locCategory,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            }
                        },
                        {
                            label: "库位容量",
                            prop: "capacity",
                        },
                        {
                            label: "库位处理",
                            prop: "locHandling",
                            type:'select',
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.locHandling,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            }
                        },
                        {
                            label: "路线顺序",
                            prop: "logicAllocation",
                            sortable: true,
                        },
                        {
                            label: "ABC分类",
                            prop: "abc",
                            type:'select',
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.abc,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            }
                        },
                    ],
                    group: []
                },
                searchData: {}
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.location_add, false),
                    view: this.vaildData(this.permission.location_view, false),
                    delete: this.vaildData(this.permission.location_delete, false),
                    edit: this.vaildData(this.permission.location_edit, false),
                    导入: this.vaildData(this.permission.location_import, false),
                    导出: this.vaildData(this.permission.location_export, false),
                    打印标签: this.vaildData(this.permission.location_printLabel, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.locId);
                });
                return ids.join(",");
            },
        },
        mounted() {
            this.option.group.push(group_1);
            this.option.group.push(group_2);
            this.option.group.push(group_3);
            // 获取批属性数量
            getParamDetail({paramKey: "account:lotCount"}).then(res => {
                for (let i = 1; i <= res.data.paramValue; i++) {
                    let skuLot = {
                        label: "混放批属性" + i,
                        prop: "locLotMix" + i,
                        hide: false,
                        width: 120,
                        type: "select",
                        dicData: [
                            {
                                label: "允许",
                                value: "2"
                            },
                            {
                                label: "不允许",
                                value: "1"
                            }
                        ]
                    };
                    this.option.group[2].column.push(skuLot);
                }
            });
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
                    var data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            },
            beforeOpen(done, type, finish) {
                if (type === 'edit' && this.form.locType === 40) {
                    this.$message.warning('虚拟库位不允许编辑！');
                    finish();
                }
                if (type === 'edit' && this.form.locFlag === 20) {
                    this.$message.warning('该库区使用状态为冻结！');
                    finish();
                }
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.locId)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .finally(() => {
                            done();
                        });
                }
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
                    remove(row.locId).then(() => {
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
            handlePrint() {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一条数据！");
                    return;
                }
                this.loading = true;
                print(this.ids).then(res => {
                    this.$message.success("操作成功！");
                }).finally(() => {
                    this.loading = false;
                });
            },
            handleCommand(cmd) {
                switch (parseInt(cmd)) {
                    case 0:
                        this.handlePrint();
                        break;
                    case 1:
                        this.fileUpload.visible = true;
                        break;
                    case 2:
                        this.loading = true;
                        exportFile(this.searchData).then(res => {
                            this.$message.success('操作成功，正在下载中...');
                            fileDownload(res.data, "库位.xlsx");
                        }).catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        }).finally(() => {
                            this.loading = false;
                        });
                        break;
                }
            },
            callbackFileUpload(res) {
                this.fileUpload.visible = res.visible;
                if (res.result) {
                    this.dataVerify.dataSource = res.data;
                    this.dataVerify.visible = true;
                }
            },
            callbackDataVerify(res) {
                this.dataVerify.visible = res.visible;
                if (res.result) {
                    this.$message.success("导入成功！");
                    this.$refs.table.onLoad();
                }
            },
            searchChange(data) {
                this.searchData = data;
            }
        }
    };
</script>
<style lang="scss">
</style>
