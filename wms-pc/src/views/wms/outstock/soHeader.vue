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
                <el-dropdown trigger="click" @command="handleCommand">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.so_header_singleAllot" command="1">按单分配</el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_wellenAllot" command="2">波次分配</el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_detailAllot" command="21">明细分配</el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_cancelAllot" command="3">取消分配</el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_printPreview" command="40">打印预览</el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_canelOrder" command="5" divided>取消订单
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_import" command="6" icon="el-icon-upload2" divided>
                            导入
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_export" command="7" icon="el-icon-download"
                        >导出
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.so_header_completeOutstock" command="10">完成出库
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
        <pick-plan-dialog
            :visible="pickPlan.visible"
            :dataSource="pickPlan.dataSource"
            :isWellen="pickPlan.isWellen"
            @callback="callbackPickPlan"
        >
        </pick-plan-dialog>
        <so-detail-pick-plan
            :visible="soDetailPickPlan.visible"
            :data-source="soDetailPickPlan.dataSource"
            @callback="callbackSoDetailPickPlan"
        >
        </so-detail-pick-plan>
        <file-upload
            :visible="fileUpload.visible"
            template="出库单.xlsx"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            :uploadUrl="dataVerify.uploadUrl"
            :dataVerifyUrl="dataVerify.dataVerifyUrl"
            @callback="callbackDataVerify"
        ></data-verify>

        <strategy-config
            ref="planDialog"
            :owner="soHeader"
            @callback="callbackPlan"
        >
        </strategy-config>
    </basic-container>
</template>
<script>
import {
    getList,
    add,
    remove,
    getDetail,
    canEdit,
    cancel,
    exportFile,
    completed,
    getSoBillNo,
    completedOutstock,
} from "@/api/wms/outstock/soHeader";
import {
    createPickPlan,
    rollback,
    getDetailPickPlan,
} from "@/api/wms/outstock/pickplan";
import {getParamValue} from "../../../util/param";
import {group as group_1} from "./soHeader/group_1.js";
import {group as group_2} from "./soHeader/group_2.js";
import pickPlanDialog from "./soHeader/pickPlanDialog";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import SoDetailPickPlan from "./soHeader/soDetailPickPlan";
import strategyConfig from "./soHeader/strategy-config"
import {mapGetters} from "vuex";

export default {
    name: "soHeader",
    components: {
        SoDetailPickPlan,
        pickPlanDialog,
        fileUpload,
        dataVerify,
        strategyConfig
    },
    data() {
        return {
            loading: false,
            selectionList: [], //选中的数据
            data: [],
            form: {},
            templateList: [],
            pickPlan: {
                visible: false,
                isWellen: false,
            },
            plan: {
                visible: false,
                isWellen: false,
            },
            soDetailPickPlan: {
                visible: false,
                dataSource: {},
            },
            boxLabelHeader: {
                visible: false,
                dataSource: {},
            },
            serial: {
                visible: false,
                dataSource: "",
            },
            soHeader: {},
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                rowKey: "id",
                menuItem: [
                    {
                        label: '复制',
                        command: 1,
                        divided: true
                    }
                ],
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
                        width: 160,
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
                        search: true,
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
                        prop: "transportType",
                        label: "发货方式",
                        width: 150,
                        sortable: true,
                        sortProp: "transportCode",
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.transportType,
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
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.shipState,
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
                        label: "过账方式",
                        prop: "postState",
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.postState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "过账日期",
                        prop: "postTime",
                        width: 170,
                        search: true,
                        type: "date",
                    },
                    {
                        label: "过账人",
                        prop: "postUserCd",
                        width: 120,
                        search: true,
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
                group: [],
            },
            fileUpload: {
                visible: false,
            },
            fileUpload1: {
                visible: false,
            },
            dataVerify: {
                visible: false,
                dataSource: {},
                dataVerifyUrl: "/outstock/header/dataVerify",
                uploadUrl: "/outstock/header/upload/save",
            },
            dataVerify1: {
                visible: false,
                dataSource: {},
                dataVerifyUrl: "/outstock/header/dataVerifyPackage",
                uploadUrl: "/outstock/header/upload/savePackage",
            },
            callbackDialog: {
                visible: false,
                ids: "",
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.so_header_add, false),
                view: this.vaildData(this.permission.so_header_view, false),
                delete: this.vaildData(this.permission.so_header_delete, false),
                edit: this.vaildData(this.permission.so_header_edit, false),
                导入: this.vaildData(this.permission.so_header_import, false),
                导出: this.vaildData(this.permission.so_header_export, false),
                复制: this.vaildData(this.permission.so_header_copy, false),
                按单分配: this.vaildData(this.permission.so_header_singleAllot, false),
                波次分配: this.vaildData(this.permission.so_header_wellenAllot, false),
                明细分配: this.vaildData(this.permission.so_header_detailAllot, false),
                取消分配: this.vaildData(this.permission.so_header_cancelAllot, false),
                打印预览: this.vaildData(this.permission.so_header_printPreview, false),
                取消订单: this.vaildData(this.permission.so_header_canelOrder, false),
                完成出库: this.vaildData(this.permission.so_header_completeOutstock, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.soBillId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
        // 获取批属性数量
        let lotCount = getParamValue(this.$param.system.lotCount);
        let column = this.option.group[1].column[0];
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 200,
                maxlength: 200,
            };
            let filterList = column.children.filter((child) => {
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
            getList(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        rowSave(row, loading, done, type) {
            this.$set(row, "transportCode", "701");
            this.$set(row, "outstockType", "40");
            if (["edit", undefined].includes(type)) {
                if (row.detailList) {
                    row.detailList.forEach((detail) => {
                        if (type === undefined) {
                            detail.soDetailId = undefined;
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
            }).then(() => {
                remove(row.soBillId).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
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
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        handleCommand(cmd) {
            let tag = parseInt(cmd);
            let allocMode = getParamValue(this.$param.system.allocMode);
            if (tag != 6 && tag != 30 && tag != 7) {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一张单据才能执行此操作！");
                    return;
                }
            }
            this.loading = true;
            switch (tag) {
                case 20:
                    this.loading = true;
                    exportPackageFile(this.ids)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res, "包装明细.xlsx");
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
                    if (allocMode == 1) {
                        this.$refs.planDialog.show(undefined, undefined, this.ids);
                    } else {
                        let params = {
                            isWellen: false,
                            soBillIdList: this.ids.split(","),
                        };
                        createPickPlan(params)
                            .then((res) => {
                                if (res.data.data.prompt) {
                                    this.pickPlan.dataSource = res.data.data;
                                    this.pickPlan.isWellen = false;
                                    this.pickPlan.visible = true;
                                } else {
                                    this.$refs.table.onLoad();
                                    this.$message({type: "success", message: "操作成功!"});
                                }
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    }
                    // detail(1491597406016380930).then((res) => {
                    //     if (res.data.data.paramValue == 1) {
                    //         this.$refs.planDialog.show(undefined, undefined, this.ids);
                    //     } else {
                    //         let params = {
                    //             isWellen: false,
                    //             soBillIdList: this.ids.split(","),
                    //         };
                    //         createPickPlan(params)
                    //             .then((res) => {
                    //                 if (res.data.data.prompt) {
                    //                     this.pickPlan.dataSource = res.data.data;
                    //                     this.pickPlan.isWellen = false;
                    //                     this.pickPlan.visible = true;
                    //                 } else {
                    //                     this.$refs.table.onLoad();
                    //                     this.$message({type: "success", message: "操作成功!"});
                    //                 }
                    //             })
                    //             .finally(() => {
                    //                 this.loading = false;
                    //             });
                    //     }
                    // })
                    break;
                case 2:
                    if (allocMode == 1) {
                        this.$refs.planDialog.show(undefined, undefined, this.ids);
                    } else {
                        let params = {
                            isWellen: true,
                            soBillIdList: this.ids.split(","),
                        };
                        createPickPlan(params)
                            .then((res) => {
                                if (res.data.data.prompt) {
                                    this.pickPlan.dataSource = res.data.data;
                                    this.pickPlan.isWellen = true;
                                    this.pickPlan.visible = true;
                                } else {
                                    this.$refs.table.onLoad();
                                    this.$message({type: "success", message: "操作成功!"});
                                }
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    }
                    break;
                case 21:
                    if (allocMode == 1) {
                        this.$refs.planDialog.show(undefined, undefined, this.ids);
                    } else {
                        getDetailPickPlan(this.ids)
                            .then((res) => {
                                this.soDetailPickPlan.visible = true;
                                this.soDetailPickPlan.dataSource = {
                                    soDetailList: res.data.data,
                                };
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    }
                    break;
                case 3:
                    rollback(this.ids)
                        .then(() => {
                            this.$refs.table.onLoad();
                            this.$message({type: "success", message: "操作成功!"});
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
                case 40: // 打印预览
                    this.selectionList.forEach((soHeader) => {
                        if (!soHeader.reportFileName) {
                            this.$message.warning("企业未关联打印模板！");
                            this.loading = false;
                            return;
                        }
                        window.open(
                            process.env.VUE_APP_BASEURL +
                            "ureport/preview?_u=nodes-" +
                            soHeader.reportFileName +
                            "&soBillId=" +
                            soHeader.soBillId
                        );
                    });
                    this.loading = false;
                    break;
                case 5:
                    this.$confirm("确定取消当前勾选出库单？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            cancel(this.ids)
                                .then((res) => {
                                    debugger
                                    this.$refs.table.onLoad();
                                    this.$message.success("操作成功！");
                                })
                                .finally(() => {
                                    this.loading = false;
                                });
                        })
                        .catch(() => {
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
                            fileDownload(res.data, "出库单.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
                case 8: // 完成订单
                    this.$confirm("完成订单操作不可逆，是否确定完成当前勾选出库单？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            completed(this.ids)
                                .then((res) => {
                                    this.$refs.table.onLoad();
                                    this.$message.success("操作成功！");
                                })
                                .finally(() => {
                                    this.loading = false;
                                });
                        })
                        .catch(() => {
                            this.loading = false;
                        });
                    break;
                case 9:
                    this.callbackDialog.ids = this.ids;
                    this.callbackDialog.visible = true;
                    break;
                case 10:
                    this.$confirm("完成出库操作不可逆，是否确定当前勾选出库单已出库？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            completedOutstock(this.ids)
                                .then((res) => {
                                    this.$refs.table.onLoad();
                                    this.$message.success("操作成功！");
                                })
                                .finally(() => {
                                    this.loading = false;
                                });
                        })
                        .catch(() => {
                            this.loading = false;
                        });
                    break;
            }
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view", "copy"].includes(type)) {
                if (type === "edit") {
                    if (this.form.soBillId) {
                        canEdit(this.form.soBillId)
                            .then((res) => {
                                if (res.data.data) {
                                    this.getSoHeaderDetail(done, type);
                                } else {
                                    this.$message.warning("当前出库单不允许编辑！");
                                    finish();
                                }
                            })
                            .catch(() => {
                                finish();
                            });
                    }
                } else {
                    this.getSoHeaderDetail(done, type);
                }
            }
        },
        getSoHeaderDetail(done, type) {
            if (!this.form.soBillId) {
                return;
            }
            let soBillNo;
            if (type == 'new' || type == 'copy') {
                getSoBillNo().then(re => {
                    soBillNo = re.data.data;
                });
            }
            getDetail(this.form.soBillId)
                .then((res) => {
                    // this.$set(res.data, "soDetailList", res.data.soDetailIPage.records);
                    if (res.data.data.detailList && res.data.data.detailList.length > 0) {
                        // 替换所有物品显示数量
                        res.data.data.detailList.forEach((detail) => {
                            if (type == 'copy') {
                                detail.scanQty = 0;
                                detail.scanQtyName = 0;
                                detail.allow_replace = 0;
                            }
                        });
                    }
                    if (type == 'copy') {
                        res.data.data.soBillId = undefined;
                        res.data.data.soBillNo = soBillNo;
                    }
                    this.form = res.data.data;
                })
                .finally(() => {
                    done();
                });
        },
        callbackPickPlan(res) {
            this.pickPlan.visible = res.visible;
            if (res.success) {
                this.$message.success("操作成功！");
                this.$refs.table.onLoad();
            }
        },
        callbackPlan(res) {
            // this.plan.visible = res.visible;
            // if (res.success) {
            //     this.$message.success("操作成功！");
            //     this.$refs.table.onLoad();
            // }
            if (res.success) {
                this.$message.success('操作成功！');
                this.searchChange(this.searchData);
                this.$refs.table.onLoad(this.searchData);
            } else {
                this.loading = false;
            }
        },
        callbackSoDetailPickPlan(res) {
            this.soDetailPickPlan.visible = res.visible;
            if (!res.data || res.data.length === 0) {
                return;
            }
            this.loading = true;
            if (res.success) {
                let soBillIdList = [];
                let soDetailIdList = [];

                for (let i = 0; i < res.data.length; i++) {
                    let item = res.data[i];
                    soDetailIdList.push(item.soDetailId);
                    if (!soBillIdList.includes(item.soBillId)) {
                        soBillIdList.push(item.soBillId);
                    }
                }

                let params = {
                    isWellen: false,
                    soBillIdList: soBillIdList,
                    soDetailIdList: soDetailIdList,
                };
                createPickPlan(params)
                    .then((res) => {
                        if (res.data.data.prompt) {
                            this.pickPlan.dataSource = res.data.data;
                            this.pickPlan.isWellen = false;
                            this.pickPlan.visible = true;
                        } else {
                            this.$refs.table.onLoad();
                            this.$message({type: "success", message: "操作成功!"});
                        }
                    })
                    .finally(() => {
                        this.loading = false;
                    });
            }
        },
        callbackBoxLabelHeader(res) {
            this.boxLabelHeader.visible = res.visible;
        },
        callbackFileUpload(res) {
            this.fileUpload.visible = res.visible;
            if (res.result) {
                this.dataVerify.dataSource = res.data.data;
                this.dataVerify.visible = true;
            } else {
                this.loading = false;
            }
        },
        callbackDataVerify(res) {
            this.dataVerify.visible = res.visible;
            if (res.result) {
                this.$message.success("导入成功！");
            }
        },
        searchChange(data) {
            this.searchData = data;
        },
        callbackCallbackDialog(res) {
            this.callbackDialog.visible = res.visible;
            this.loading = false;
            this.$refs.table.onLoad();
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:

                    this.$refs.table.onShowData(row, index, 'copy');
                    break;
            }
        }
    },
};
</script>
<style lang="scss">
.dropdownPanel {
    margin-right: 10px;
}
</style>
