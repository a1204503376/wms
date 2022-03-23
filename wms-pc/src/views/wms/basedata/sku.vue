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
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
            @search-change="searchChange"
            :menuWidth="option.menuWidth"
            :before-open="beforeOpen"
        >
            <template slot="menuLeft">
                <el-dropdown trigger="click" @command="handleCommand">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.sku_import" command="1" icon="el-icon-upload2"
                        >导入
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.sku_export" command="2" icon="el-icon-download"
                        >导出
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
        <sku-outstock
            :visible="skuOutstock.visible"
            :dataSource="skuOutstock.dataSource"
            @callback="callbackSkuOutstock"
        ></sku-outstock>
        <instock
            :visible="instock.visible"
            :dataSource="instock.dataSource"
            @callback="callbackInstock"
        ></instock>

        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/basedata/sku/export-template"
            file-name="物品"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/basedata/sku/import-data"
            dataVerifyUrl="/api/wms/basedata/sku/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>

<script>
import {
    getPage,
    remove,
    getDetail,
    submit,
    exportFile,
    addsku,
    canEdit,
} from "@/api/wms/basedata/sku";
import {group as group_1} from "./sku/group_1.js";
import {group as group_2} from "./sku/group_2.js";
import {group as group_3} from "./sku/group_3.js";
import {group as group_4} from "./sku/group_4.js";
import skuOutstock from "./sku/skuOustock";
import instock from "./sku/skuInstock";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
    name: "sku",
    components: {
        skuOutstock: skuOutstock,
        instock,
        fileUpload,
        dataVerify,
    },
    data() {
        return {
            loading: false,
            data: [],
            selectionList: [], //选中的数据
            form: {},
            skuOutstock: {
                visible: false,
                dataSource: {
                    skuId: "",
                    woId: "",
                },
            },
            instock: {
                visible: false,
                dataSource: {
                    skuId: "",
                    woId: "",
                    //zoneId:''
                },
            },
            fileUpload: {
                visible: false,
            },
            dataVerify: {
                visible: false,
                dataSource: {},
            },
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                menuItem: [
                    {
                        label: "出库设置",
                        command: 1,
                        divided: true,
                    },
                    {
                        label: "入库设置",
                        command: 2,
                    },
                ],
                column: [
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        search: true,
                        view: true,
                        width: 160,
                        sortable: true,
                        placeholder: '支持模糊查询',
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        search: true,
                        width: 160,
                        sortable: true,
                        placeholder: '支持模糊查询',
                    },
                    {
                        prop: "wspName",
                        label: "包装",
                        search: true,
                        width: 140,
                        sortable: true,
                        sortProp: "wspId",
                        placeholder: '支持模糊查询',
                    },
                    {
                        prop: "skuTypeId",
                        label: "物品分类",
                        width: 140,
                        sortable: true,
                        type: "select-tree",
                        dicUrl: "/api/wms/basedata/skutype/tree",
                        props: {
                            label: "title",
                            value: "id",
                            children: "children"
                        }
                    },
                    {
                        prop: "isSn",
                        label: "SN管理",
                        width: 100,
                        sortable: true,
                        sortProp: "isSn",
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.isSn,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    // { prop: "skuStorageType", label: "存货类型" },
                    // { prop: "whName", label: "所属库房" },
                    {
                        prop: "woId",
                        label: "所属货主",
                        width: 160,
                        sortable: true,
                        sortProp: "woId",
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId"
                        }
                    },
                    {prop: "skuVolume", label: "体积"},
                    {prop: "skuNetWeight", label: "净重(g)"},
                    {prop: "skuGrossWeight", label: "毛重(g)"},
                    // {prop: "qualityType", label: "效期管理"},
                    // {
                    //   prop: "qualityDateTypeCd",
                    //   label: "保质期类型",
                    //   width: 160,
                    // },
                    {
                        prop: "qualityDateType",
                        label: "保质期类型",
                        hide: true,
                        width: 160,
                    },
                    {
                        prop: "qualityHours",
                        label: "保质期天数",
                        width: 160,
                    },
                ],
                group: [],
            },
            searchData: {},
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.sku_add, false),
                view: this.vaildData(this.permission.sku_view, false),
                delete: this.vaildData(this.permission.sku_delete, false),
                edit: this.vaildData(this.permission.sku_edit, false),
                导入: this.vaildData(this.permission.sku_import, false),
                导出: this.vaildData(this.permission.sku_export, false),
                入库设置: this.vaildData(this.permission.sku_instock, false),
                出库设置: this.vaildData(this.permission.sku_outstock, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.skuId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
        this.option.group.push(group_3);
        this.option.group.push(group_4);
    },
    methods: {
        //出库
        outibrary(row, index) {
            this.skuOutstock.dataSource.skuId = row.skuId;
            this.skuOutstock.dataSource.woId = row.woId;
            this.skuOutstock.visible = true;
        },
        //入库
        openInstock(row, index) {
            this.instock.dataSource.skuId = row.skuId;
            this.instock.dataSource.woId = row.woId;
            this.instock.visible = true;
        },
        //列表
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        //保存
        rowSave(row, loading, done, type) {
            addsku(row).then(
                () => {
                    loading();
                    this.$refs.table.onLoad();
                    this.$message.success("操作成功！");
                },
                (error) => {
                    done();
                }
            );
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    remove(row.skuId).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!",
                        });
                    });
                })
                .catch(() => {
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
                type: "warning",
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                    this.$refs.table.toggleSelection();
                });
            });
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                if (["edit"].includes(type)) {
                    canEdit(this.form.skuId)
                        .then(res => {
                            if (res.data.data) {
                                this.getSkuDetail(done, type);
                            } else {
                                this.$message.warning("当前物品不允许编辑！");
                                finish();
                            }
                        })
                        .catch(() => {
                            finish();
                        })
                } else {
                    this.getSkuDetail(done, type);
                }
            }
        },
        getSkuDetail(done, type) {
            getDetail(this.form.skuId)
                .then((res) => {
                    this.form = res.data.data;
                })
                .catch(() => {
                })
                .finally(() => {
                    done();
                });
        },
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        }
        ,
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        }
        ,
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.outibrary(row, index);
                    break;
                case 2:
                    this.openInstock(row, index);
                    break;
            }
        }
        ,
        callbackSkuOutstock(res) {
            this.skuOutstock.visible = res.visible;
        }
        ,
        callbackInstock(res) {
            this.instock.visible = res.visible;
        }
        ,
        handleCommand(cmd) {
            let tag = parseInt(cmd);

            if (tag != 1 && tag != 30 && tag != 2) {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一个任务才能执行此操作！");
                    return;
                }
            }
            switch (tag) {
                case 20:
                    this.loading = true;
                    exportSkuEnterpriseFile(this.ids)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res, "物品企业关联.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
                case 30:
                    this.fileUpload1.visible = true;
                    break;
                case 1:
                    this.fileUpload.visible = true;
                    break;
                case 2:
                    this.loading = true;
                    exportFile(this.searchData)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res.data, "物品.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
            }
        }
        ,
        callbackFileUpload(res) {
            this.fileUpload.visible = res.visible;
            if (res.result) {
                this.dataVerify.dataSource = res.data;
                this.dataVerify.visible = true;
            }
        }
        ,
        callbackDataVerify(res) {
            this.dataVerify.visible = res.visible;
            if (res.result) {
                this.$message.success("导入成功！");
                this.$refs.table.onLoad();
            }
        }
        ,
        searchChange(data) {
            this.searchData = data;
        }
        ,
    },
};
</script>
<style lang="scss">
</style>
