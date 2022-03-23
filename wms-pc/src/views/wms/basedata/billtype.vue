import fileDownload from "js-file-download";
<template>
    <basic-container>
        <nodes-crud
            ref="table"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :before-open="beforeOpen"
            @on-load="onLoad"
            @row-save="rowSave"
            @on-del="onDel"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @search-change="searchChange">

            <template slot="menuLeft">

            </template>
        </nodes-crud>

    </basic-container>
</template>

<script>
import {getPage, add, remove, getDetail} from "@/api/wms/basedata/billtype.js";
import {group as group_1} from "./billType/group_1";
import dataVerify from "@/components/nodes/dataVerify";
import LabelData from "./enterprise/labelData";
import BindLabelData from "./enterprise/bindLabelData";
import {mapGetters} from "vuex";

export default {
    name: "outstock",
    components: {
        BindLabelData,
        LabelData,
        dataVerify,
    },
    data() {
        return {
            loading: false,
            form: {},
            data: [],
            selectionList: [],
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                menuItem: [

                ],
                column: [
                    {
                        label: "单据类型编码",
                        prop: "billTypeCd",
                        placeholder: '支持模糊查询',
                        search: true,
                        view: true,
                        sortable: true
                    },
                    {
                        label: "单据类型名称",
                        prop: "billTypeName",
                        search: true,
                        sortable: true
                    },
                    // {
                    //     label: "单据类型种类",
                    //     prop: "billTypeKind",
                    //     sortable: true,
                    //     sortProp: 'ioType'
                    // },
                    {
                        label: "单据类型种类",
                        prop: "ioTypeDesc",
                        //hide: true,
                        search: true,
                        // type: "select",
                        // dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.ioType,
                        // props: {
                        //     label: "dictValue",
                        //     value: "dictKey"
                        // }
                    },
                    {
                        label: "是否启用",
                        prop: "status",
                        hide: true,
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.status,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "创建时间",
                        prop: "createTime",
                        search: true,
                        type: 'date',
                        sortable: true
                    },
                ],
                group: []
            },
            dataVerify: {
                visible: false,
                dataSource: {},
            },
            searchData: {},
            labelData: {
                visible: false
            },
            bindLabelData: {
                visible: false,
                dataId: null
            }
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.billTypeId);
            });
            return ids.join(",");
        },
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add:this.vaildData(this.permission.billtype_add,false),
                edit:this.vaildData(this.permission.billtype_edit,false),
                delete:this.vaildData(this.permission.billtype_delete,false),
                view:this.vaildData(this.permission.billtype_view,false),
            }
        },
    },
    mounted() {
        this.option.group.push(group_1);
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
                this.$refs.table.page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        rowSave(row, loading, done, type) {
            if(row.ioType==0){
                row.ioType='I';
            }else if (row.ioType==1){
                row.ioType='O';
            };
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
                remove(row.billTypeId).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                });
            });
        },
        onMultiDel() {
            //this.$message.warning("单据类型暂不允许删除，如不需要请修改为“未启用”状态！");
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
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.billTypeId)
                    .then(res => {
                        //从blade_dict中取出来的内容是数字类型，从wms_bill_type取出来的数据是字符串类型
                        //所以后台只能传过来字符串数据，现在手动转成数字类型以便绑定
                        if(res.data.data.ioType=="I"){
                            res.data.data.ioType=0;
                        }else if(res.data.data.ioType=="O"){
                            res.data.data.ioType=1;
                        }else if(res.data.data.ioType=="IC"){
                            res.data.data.ioType=2;
                        }else if(res.data.data.ioType=="OC"){
                            res.data.data.ioType=3;
                        }
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        selectionChange(val) {
            this.selectionList = val;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        dialogResult(result) {
            if (result.result) {
                this.$refs.table.onLoad();
            }
            this.dialogEdit.visible = result.visible;
        },
        searchChange(data) {
            this.searchData = data;
        },
        callbackLabelData(res) {
            this.labelData.visible = res.visible;
        },
        callbackBindLabelData(res) {
            this.bindLabelData.visible = res.visible;
        }
    }
};
</script>

<style scoped>
</style>
