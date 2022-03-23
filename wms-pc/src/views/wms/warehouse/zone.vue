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
                        <el-dropdown-item v-if="permission.zone_import" command="1" icon="el-icon-upload2">导入
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.zone_export" command="2" icon="el-icon-download">导出
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/warehouse/zone/export-template"
            file-name="库区"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/warehouse/zone/import-data"
            dataVerifyUrl="/api/wms/warehouse/zone/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>
<script>
import {getPage, add, remove, getDetail, exportFile} from "@/api/wms/warehouse/zone";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
    name: "zone",
    components: {
        fileUpload,
        dataVerify
    },
    data() {
        return {
            loading: false,
            selectionList: [], //选中的数据
            data: [],
            form: {},
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
                group: [
                    {
                        column: [
                            {
                                label: "库区编码",
                                prop: "zoneCode",
                                maxlength: 30,
                                rules: [
                                    {
                                        required: true,
                                        message: "库区编码不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    }
                                ],
                            },
                            {
                                label: "库区名称",
                                prop: "zoneName",
                                maxlength: 50,
                                rules: [
                                    {
                                        required: true,
                                        message: "库区名称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    }
                                ],
                            },
                            {
                                label: "所属库房",
                                prop: "whId",
                                type: "select",
                                dicUrl: "/api/wms/warehouse/warehouse/list",
                                props: {
                                    label: "whName",
                                    value: "whId"
                                },
                                rules: [
                                    {
                                        required: true,
                                        message: "所属库房不能为空",
                                        trigger: "blur"
                                    }
                                ]
                            },
                            {
                                label: "库区类型",
                                prop: "zoneType",
                                type: "select",
                                dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.zoneType,
                                props: {
                                    label: "dictValue",
                                    value: "dictKey"
                                },
                                rules: [
                                    {
                                        required: true,
                                        message: "库区类型不能为空",
                                        trigger: "blur"
                                    }
                                ],
                                show:['zoneTypeDesc']
                            },
                            // {
                            //     label: "是否启用",
                            //     prop: "isActive",
                            //     type: "radio",
                            //     dicData: [
                            //         {
                            //             value: "Y",
                            //             label: "是"
                            //         },
                            //         {
                            //             value: "N",
                            //             label: "否"
                            //         }
                            //     ],
                            //     default: "Y"
                            // }
                        ]
                    }
                ],
                column: [
                    {
                        label: "库区编码",
                        prop: "zoneCode",
                        search: true,
                        view: true,
                        sortable: true,
                        placeholder: '支持模糊查询',
                    },
                    {
                        label: "库区名称",
                        prop: "zoneName",
                        search: true,
                        sortable: true,
                        placeholder: '支持模糊查询',
                    },
                    // {
                    //     label: "是否启用",
                    //     prop: "isActiveName",
                    //     sortable: true,
                    //     sortProp: 'isActive'
                    // },
                    {
                        label: "所属库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        }
                    },
                    {
                        label: "库区类型",
                        prop: "zoneType",
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.zoneType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        },

                    }
                ]
            },
            searchData: {}
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.zone_add, false),
                view: this.vaildData(this.permission.zone_view, false),
                delete: this.vaildData(this.permission.zone_delete, false),
                edit: this.vaildData(this.permission.zone_edit, false),
                导入: this.vaildData(this.permission.zone_import, false),
                导出: this.vaildData(this.permission.zone_export, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.zoneId);
            });
            return ids.join(",");
        },
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
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.zoneId)
                    .then(res => {
                        this.form = res.data.data;
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
                remove(row.zoneId).then(() => {
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
        handleCommand(cmd) {
            switch (parseInt(cmd)) {
                case 1:
                    this.fileUpload.visible = true;
                    break;
                case 2:
                    this.loading = true;
                    exportFile(this.searchData).then(res => {
                        this.$message.success('操作成功，正在下载中...');
                        fileDownload(res.data, "库区.xlsx");
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
