<template>
    <div class="nodes-crud__menu">
        <div class="nodes-crud__menu__left">
            <el-button
                v-if="this.crud.tableOption.newBtn"
                :icon="config.icons.addBtn"
                :size="config.controlSize"
                type="primary"
                @click="crud.rowAdd()"
            >新增
            </el-button>
            <el-button
                v-if="this.crud.tableOption.multiDelBtn"
                :icon="config.icons.delBtn"
                :size="config.controlSize"
                plain
                type="danger"
                @click="crud.onMultiDel()"
            >删除
            </el-button>
            <slot :size="config.controlSize"
                  name="menuLeft"></slot>
        </div>
        <div class="nodes-crud__menu__right">
            <slot :size="config.controlSize"
                  name="menuRight"></slot>
            <!--            <el-tooltip content="导出 Excel"-->
            <!--                        effect="dark"-->
            <!--                        placement="top">-->
            <!--                <el-button :icon="config.icons.excelBtn"-->
            <!--                           :size="config.controlSize"-->
            <!--                           circle-->
            <!--                           @click="onExportExcel"-->
            <!--                ></el-button>-->
            <!--            </el-tooltip>-->
            <!--            <el-tooltip content="打 印"-->
            <!--                        effect="dark"-->
            <!--                        placement="top">-->
            <!--                <el-button :icon="config.icons.printBtn"-->
            <!--                           :size="config.controlSize"-->
            <!--                           circle-->
            <!--                           @click="onPrint"-->
            <!--                ></el-button>-->
            <!--            </el-tooltip>-->
            <el-tooltip content="刷 新"
                        effect="dark"
                        placement="top">
                <el-button :icon="config.icons.refreshBtn"
                           :size="config.controlSize"
                           circle
                           @click="crud.onRefresh()"
                ></el-button>
            </el-tooltip>
            <el-tooltip content="显 隐"
                        effect="dark"
                        placement="top">
                <el-button :icon="config.icons.columnBtn"
                           :size="config.controlSize"
                           circle
                           @click="crud.onDialogColumnOpen()"></el-button>
            </el-tooltip>
<!--            <el-tooltip content="搜 索"-->
<!--                        effect="dark"-->
<!--                        placement="top">-->
<!--                <el-button :icon="config.icons.searchBtn"-->
<!--                           :size="config.controlSize"-->
<!--                           circle-->
<!--                           @click="crud.onSearch()"></el-button>-->
<!--            </el-tooltip>-->
        </div>
    </div>
</template>

<script>
import config from "./config";
import {vaildData, getAsVal} from "../../utils/util";

export default {
    name: "header-menu",
    inject: ["crud"],
    created() {
        this.initFun();
    },
    data() {
        return {
            config: config
        }
    },
    computed: {
        data() {
            if (this.crud.tableOption.selection) {
                return this.crud.tableSelect;
            } else {
                return this.crud.list;
            }
        }
    },
    methods: {
        initFun() {
            this.vaildData = vaildData;
            this.crud.rowExcel = this.onExportExcel;
            this.crud.rowPrint = this.onPrint;
        },
        onPrint() {
            this.$Print(this.crud.$refs.table);
        },
        onExportExcel() {
            if (this.validatenull(this.data)) {
                this.$message.warning("请勾选要导出的数据");
                return;
            }
            this.$Export.excel({
                title: this.crud.tableOption.title,
                columns: this.crud.columnOption,
                data: this.handleSum()
            });
            this.crud.setCurrentRow();
        },
        //计算统计
        handleSum() {
            const option = this.crud.tableOption;
            const columnOption = this.crud.propOption;
            let count = 0;
            let sumsList = [...this.crud.sumsList];
            let data = []
            this.data.forEach(ele => {
                let obj = this.deepClone(ele);
                columnOption.forEach(column => {
                    if (column.bind) {
                        obj[column.prop] = getAsVal(obj, column.bind);
                    }
                    if (!this.validatenull(obj['$' + column.prop])) {
                        obj[column.prop] = obj['$' + column.prop];
                    }
                })
                data.push(obj);
            })
            if (option.index) count++;
            if (option.selection) count++;
            if (option.expand) count++;
            sumsList.splice(0, count);
            sumsList.splice(sumsList.length - 1, 1);
            if (option.showSummary) {
                let sumsObj = {};
                sumsList.forEach((ele, index) => {
                    if ((columnOption[index] || {}).prop) {
                        sumsObj[columnOption[index].prop] = ele;
                    }
                });
                data.push(sumsObj);
            }
            return data;
        },
    }
}
</script>

<style scoped>
.nodes-crud__menu {
    display: -webkit-box;;
    -webkit-box-pack: justify;
    justify-content: space-between;
    position: relative;
    width: 100%;
    height: auto;
    overflow: hidden;
    margin-bottom: 5px;
    background-color: #fff;
}
</style>
