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
            @search-change="searchChange"
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
                        <el-dropdown-item v-if="permission.skulotval_import" command="1" icon="el-icon-upload2">导入</el-dropdown-item>
                        <el-dropdown-item v-if="permission.skulotval_export" command="2" icon="el-icon-download">导出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/basedata/skulotval/export-template"
            file-name="批属性验证"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/basedata/skulotval/import-data"
            dataVerifyUrl="/api/wms/basedata/skulotval/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>

<script>
import {getPage, remove, getDetail, exportFile, add} from "@/api/wms/basedata/skulotval.js";
import {getParamValue} from "@/util/param";
import {getDictByCode} from "@/api/core/dict";
import {group as group_1} from "./skulotval/group_1.js";
import {group as group_2} from "./skulotval/group_2.js";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
    name: "skuLotVal",
    components: {
        fileUpload,
        dataVerify
    },
    data() {
        return {
            loading: false,
            data: [],
            selectionList: [],
            form: {},
            lotCount: 0,
            skuLotMaskSource: [], //生成掩码数据源
            skuLotEditTypeSource: [], //掩码生成规则数据源
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
                group: [],
                column: [
                    {
                        label: "验证名称",
                        prop: "skuLotValName",
                        search: true,
                        placeholder: '支持模糊查询',
                        view: true,
                        sortable: true
                    },
                    {
                        label: "验证说明",
                        prop: "skuLotRemark",
                        sortable: true
                    }
                ]

            },
            searchData: {},

        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.skulotval_add, false),
                view: this.vaildData(this.permission.skulotval_view, false),
                delete: this.vaildData(this.permission.skulotval_delete, false),
                edit: this.vaildData(this.permission.skulotval_edit, false),
                导入: this.vaildData(this.permission.skulotval_import, false),
                导出: this.vaildData(this.permission.skulotval_export, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.wslvId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
        // 设置批属性数量
        this.lotCount = getParamValue(this.$param.system.lotCount);
        getDictByCode(this.$dict.skuLotVal).then(res => {
            this.skuLotMaskSource = res.data.data;
        });
        getDictByCode(this.$dict.skuLotValRuler).then(res => {
            this.skuLotEditTypeSource = res.data.data;
        });
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
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
        //保存
        rowSave(row, loading, done, type) {
            if (row.skuLotList && row.skuLotList.length > 0) {
                row.skuLotList.forEach((element, item) => {
                    let skuLotNumber = item + 1;
                    row["skuLotMixMask" + skuLotNumber] = element.skuLotMixMask;
                    row["skuLotMask" + skuLotNumber] = element.skuLotMask;
                    row["skuLotEditType" + skuLotNumber] = element.skuLotEditType;
                    row["skuLen" + skuLotNumber] = element.skuLen;
                    row["skuLotMust" + skuLotNumber] = element.skuLotMust;
                    row["skuLotDisp" + skuLotNumber] = element.skuLotDisp;
                    row["skuLotMix" + skuLotNumber] = element.skuLotMix;
                });
            }
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
                getDetail(this.form.wslvId)
                    .then(res => {
                        this.form = res.data.data;
                        let skuLotList = [];
                        for (let i = 1; i <= this.lotCount; i++) {
                            let skuLotMask = this.skuLotMaskSource.filter(item => {
                                return item.dictKey == this.form["skuLotMask" + i];
                            });
                            let skuLotEditType = this.skuLotEditTypeSource.filter(item => {
                                return item.dictKey == this.form["skuLotEditType" + i];
                            });
                            let skuLot = {
                                skuLotValName: "批属性" + i,
                                skuLotMixMask: this.form["skuLotMixMask" + i],
                                skuLotMask: this.form["skuLotMask" + i],
                                skuLotMaskDesc:
                                    skuLotMask && skuLotMask.length > 0
                                        ? skuLotMask[0].dictValue
                                        : "",
                                skuLotEditType: this.form["skuLotEditType" + i],
                                skuLotEditDesc:
                                    skuLotEditType && skuLotEditType.length > 0
                                        ? skuLotEditType[0].dictValue
                                        : "",
                                skuLen: this.form["skuLen" + i],
                                skuLotMust: this.form["skuLotMust" + i],
                                skuLotDisp: this.form["skuLotDisp" + i],
                                skuLotMix: this.form["skuLotMix" + i]
                            };
                            skuLotList.push(skuLot);
                        }
                        if (skuLotList && skuLotList.length > 0) {
                            this.$set(this.form, "skuLotList", skuLotList);
                        }
                    }).finally(() => {
                    done();
                });
            } else {
                let skuLotList = [];
                for (let i = 1; i <= this.lotCount; i++) {
                    let skuLot = {
                        skuLotValName: "批属性" + i,
                        skuLotMixMask: "",
                        skuLotMask: "",
                        skuLotMaskDesc: "",
                        skuLotEditType: "",
                        skuLotEditDesc: "",
                        skuLen: "",
                        skuLotMust: 0,
                        skuLotDisp: 0,
                        skuLotMix: 1
                    };
                    skuLotList.push(skuLot);
                }
                if (skuLotList && skuLotList.length > 0)
                    this.$set(this.form, "skuLotList", skuLotList);
            }
            let children = this.option.group[1].column[0].children;
            if (children && children.length > 0) {
                children.forEach(element => {
                    if (element.prop === "skuLotMask")
                        element.dicData = this.skuLotMaskSource;
                    if (element.prop === "skuLotEditType")
                        element.dicData = this.skuLotEditTypeSource;
                });
            }
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.wslvId).then(() => {
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
        selectionChange(val) {
            this.selectionList = val;
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
                        fileDownload(res.data, "批属性验证.xlsx");
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

<style scoped>
</style>
