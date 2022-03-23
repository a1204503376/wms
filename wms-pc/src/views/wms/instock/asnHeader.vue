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
            @menu-command="menuCommand"
        >
            <template slot="menuLeft">
                <span class="dropdownPanel">
                    <el-dropdown trigger="click" @command="handleCommand">
                        <el-button type="primary" size="mini">
                            <i class="el-icon-edit-outline"></i>
                            操作
                            <i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-if="permission.header_creatLpn" command="1">生成LPN</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_viewLpn" command="2">查看LPN</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_importSerial" command="4">导入序列号</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_cancel" command="5" divided>取消订单</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_import" command="6" icon="el-icon-upload2" divided>导入</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_export" command="7" icon="el-icon-download">导出</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_print" command="8" divided>打印箱贴</el-dropdown-item>
                            <el-dropdown-item v-if="permission.header_dataBack" command="9" divided>数据回传</el-dropdown-item>
                        </el-dropdown-menu>
                  </el-dropdown>
                </span>
                <el-button type="primary" icon="el-icon-check" size="mini"
                           style="margin-left:0px;margin-right:10px;"
                           v-if="permission.header_close"
                           @click="finishAsnBill">关闭单据
                </el-button>
            </template>
        </nodes-crud>
        <lpn-detail-dialog
            :isShowDialog="lpnDetailDialog.visible"
            :billIds="lpnDetailDialog.billIds"
            @child-cancel="hideLPN"
        ></lpn-detail-dialog>
        <!-- 序列号导入 -->
        <file-upload :visible="fileUploadSerial.visible"
                     template-url="/api/wms/instock/asnHeader/sn/export-template"
                     file-name="订单序列号"
                     @callback="callbackFileUploadSerial"></file-upload>
        <data-verify
            :visible="dataVerifySerial.visible"
            :dataSource="dataVerifySerial.dataSource"
            :uploadUrl="dataVerifySerial.uploadUrl"
            :dataVerifyUrl="dataVerifySerial.dataVerifyUrl"
            @callback="callbackDataVerifySerial"
        ></data-verify>
        <!-- 入库单导入 -->
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api//wms/instock/asnHeader/export-template"
            file-name="入库单"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/instock/asnHeader/import-data"
            dataVerifyUrl="/api/wms/instock/asnHeader/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
        <!-- 序列号 -->
        <serial :visible="serial.visible"
                :dataSource="serial.dataSource"
                @callback="callbackSerial"></serial>
        <!-- 到货登记 -->
        <register-dialog
            :visible="registerDialog.visible"
            :data-source="registerDialog.dataSource"
            :selectionList="selectionList"
            @callback="callbackRegisterDialog"
        ></register-dialog>
    </basic-container>
</template>
<script>
import {
    getPage,
    add,
    remove,
    getDetail,
    creatLpn,
    finishAsnBill,
    canEdit,
    cancel,
    exportFile,
    printBoxLabel,
} from "@/api/wms/instock/asnHeader";
import {getParamValue} from "@/util/param";
import {option} from "./asnHeader/option.js";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import serial from "./asnHeader/serial";
import registerDialog from "./asnHeader/registerDialog";
import lpnDetailDialog from "./asnHeader/lpnDetailDialog";
import {mapGetters} from "vuex";


export default {
    name: "instocks",
    components: {
        fileUpload,
        dataVerify,
        serial,
        registerDialog,
        lpnDetailDialog,
    },
    data() {
        return {
            loading: false,
            data: [],
            form: {},
            selectionList: [],
            serial: {
                visible: false,
                dataSource: ''
            },
            lpnDetailDialog: {
                billIds: "", //选中的订单Ids
                visible: false
            },
            registerDialog: {
                visible: false,
                dataSource: undefined
            },
            option: option,
            searchData: {},
            fileUpload: {
                visible: false
            },
            dataVerify: {
                visible: false,
                dataSource: {},
            },
            fileUploadSerial: {
                visible: false
            },
            dataVerifySerial: {
                visible: false,
                dataSource: {},
                dataVerifyUrl: "/api/wms/instock/asnHeader/sn/import-valid",
                uploadUrl: "/api/wms/instock/asnHeader/sn/import-data"
            },
            callbackDialog: {
                visible: false,
                ids: ''
            },
            barcodeDialog: {
                visible: false,
                data: {}
            }
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.header_add, false),
                view: this.vaildData(this.permission.header_view, false),
                delete: this.vaildData(this.permission.header_delete, false),
                edit: this.vaildData(this.permission.header_edit, false),
                导入: this.vaildData(this.permission.header_import, false),
                导出: this.vaildData(this.permission.header_export, false),
                到货登记: this.vaildData(this.permission.header_registerDialog, false),
                导入序列号: this.vaildData(this.permission.header_importSerial, false),
                取消订单: this.vaildData(this.permission.header_cancel, false),
                打印箱贴: this.vaildData(this.permission.header_print, false),
                数据回传: this.vaildData(this.permission.header_dataBack, false),
                关闭单据: this.vaildData(this.permission.header_close, false),
                生成LPN: this.vaildData(this.permission.header_creatLpn, false),
                查看LPN: this.vaildData(this.permission.header_viewLpn, false),
                复制: this.vaildData(this.permission.header_copy, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.asnBillId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        // 获取批属性数量
        let column = this.option.group[1].column[0];
        for (let i = 1; i <= getParamValue(this.$param.system.lotCount); i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 200,
                maxlength: 200,
                valueFormat: 'yyyyMMdd'
            };
            let filterList = column.children.filter(child => {
                return child.prop === skuLot.prop;
            });
            if (!filterList || filterList.length === 0) {
                column.children.push(skuLot);
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
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        rowSave(row, loading, done, type) {
            if (["edit", undefined].includes(type)) {
                if (row.asnDetailList) {
                    row.asnDetailList.forEach(detail => {
                        if (type === undefined) {
                            detail.asnDetailId = undefined;
                        }
                    });
                }
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
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.asnBillId).then(() => {
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
        finishAsnBill() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一个任务才能执行此操作！");
                return;
            }
            this.$confirm("确定将选择数据完成收货吗?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    return finishAsnBill(this.ids);
                })
                .then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "完成收货成功!"
                    });
                });
        },
        handleCommand(cmd) {
            let tag = parseInt(cmd);
            if (4 != tag && tag != 6 && tag != 7) {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一个任务才能执行此操作！");
                    return;
                }
            }
            switch (tag) {
                case 1:
                    this.$confirm("确定将选择数据生成LPN吗?", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    })
                        .then(() => {
                            this.loading = true;
                            return creatLpn(this.ids);
                        })
                        .then(() => {

                            this.$refs.table.onLoad();
                            this.$message({
                                type: "success",
                                message: "生成LPN成功!"
                            });
                        }).finally(() => {
                        this.loading = false;
                    });
                    break;
                case 2:
                    this.lpnDetailDialog.billIds = this.ids;
                    this.lpnDetailDialog.visible = true;
                    break;
                case 4:
                    this.fileUploadSerial.visible = true;
                    break;
                case 5:
                    this.$confirm("确定取消当前勾选入库单？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    })
                        .then(() => {
                            this.loading = true;
                            cancel(this.ids).then(res => {
                                this.$refs.table.onLoad();
                                this.$message.success('操作成功！');
                            }).finally(() => {
                                this.loading = false;
                            });
                        }).catch(() => {
                        this.loading = false;
                    });
                    break;

                case 6:
                    this.fileUpload.visible = true;
                    break;
                case 7:
                    this.loading = true;
                    exportFile(this.searchData)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res.data, "入库单.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
                case 8:
                    this.loading = true;
                    printBoxLabel(this.ids).then(res => {
                        this.$message.success('操作成功！');
                    }).finally(() => {
                        this.loading = false;
                    });
                    break;
                case 9:
                    this.callbackDialog.ids = this.ids;
                    this.callbackDialog.visible = true;
            }
        },
        hideLPN() {
            this.lpnDetailDialog.visible = false;
        },
        callbackRegisterDialog(data) {
            this.registerDialog.visible = data.visible;
            if (data.success) {
                this.$message.success('操作成功！');
                this.$refs.table.onLoad();
            }
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view", undefined].includes(type)) {
                if (type === "edit") {
                    if (this.form.asnBillId) {
                        canEdit(this.form.asnBillId)
                            .then(res => {
                                if (res.data.data) {
                                    this.getAsnHeaderDetail(done, type);
                                } else {
                                    this.$message.warning("当前入库单不允许编辑！");
                                    finish();
                                }
                            })
                            .catch(() => {
                                finish();
                            });
                    }
                } else {
                    this.getAsnHeaderDetail(done, type);
                }
            }
        },
        getAsnHeaderDetail(done, type) {
            if (!this.form.asnBillId) {
                return;
            }
            getDetail(this.form.asnBillId)
                .then(res => {
                    if (res.data.data.asnDetailList && res.data.data.asnDetailList.length > 0) {
                        // 替换所有物品显示数量
                        res.data.data.asnDetailList.forEach(detail => {
                            if (type === undefined) {
                                detail.scanQty = 0;
                                detail.scanQtyName= 0;
                            }
                        });
                    }
                    if (type === undefined) {
                        res.data.data.asnBillId = undefined;
                    }
                    this.form = res.data.data;
                }).finally(() => {
                    done();
                });
        },
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
        },
        // 入库单序列号文件上传回调
        callbackFileUploadSerial(res) {
            this.fileUploadSerial.visible = res.visible;
            if (res.result) {
                this.dataVerifySerial.dataSource = res.data;
                this.dataVerifySerial.visible = true;
            }
        },
        callbackDataVerifySerial(res) {
            this.dataVerifySerial.visible = res.visible;
            if (res.result) {
                this.$message.success("导入成功！");
            }
        },
        callbackSerial(res) {
            this.serial.visible = res.visible;
        },
        searchChange(data) {
            this.searchData = data;
        },
        callbackCallbackDialog(res) {
            this.callbackDialog.visible = res.visible;
            this.$refs.table.onLoad();
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:// 复制
                    this.$refs.table.onShowData(row, index, 'copy');
                    break;
                case 2:// 到货登记
                    if (row.asnBillState < 40) {
                        this.registerDialog.dataSource = Object.assign({}, row);
                        this.registerDialog.visible = true;

                    } else {
                        this.$message.warning("该单据不可登记！");
                        return;
                    }
                    break;
            }
        }
    }
};
</script>
<style lang="scss">

.dropdownPanel {
    margin-right: 10px;
}

.notice {
    height: 100%;
}
</style>
