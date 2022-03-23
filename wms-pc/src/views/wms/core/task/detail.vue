<template>
    <el-dialog
        :title="title"
        width="80%"
        top="3vh"
        append-to-body
        v-dialogDrag="true"
        :close-on-click-modal="false"
        :visible.sync="visible"
        :before-close="dialogClose"
        @open="dialogOpen"
    >
        <div :loading="loading">
            <el-table :data="data.detail"
                      height=500
                      style="cursor:default;"
                      element-loading-text="数据正在加载中"
                      element-loading-spinner="el-icon-loading"
                      highlight-current-row
                      border
            >
                <!-- 收货任务表格 -->
                <template v-if="data.taskTypeCd===102">
                    <el-table-column prop="skuCode" label="物品编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuName" label="物品编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuSpec" label="规格" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="planQtyName" label="计划数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="scanQtyName" label="实收数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="surplusQtyName" label="剩余数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="detailStatusName" label="接收状态" show-overflow-tooltip></el-table-column>
                </template>
                <!-- 拣货任务表格 -->
                <template v-else-if="data.taskTypeCd===103">
                    <el-table-column prop="skuCode" label="物品编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuName" label="物品名称" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="repSkuCode" label="被替代物品编码" :width="120" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column prop="repSkuName" label="被替代物品名称" :width="120" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column prop="locCode" label="货位编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="lotNumber" label="批次号" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="lpnCode" label="容器" show-overflow-tooltip
                                     v-if="paramValue==0"></el-table-column>
                    <el-table-column prop="pickPlanQty" label="分配数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="pickRealQty" label="拣货数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="wsuName" label="单位" show-overflow-tooltip></el-table-column>
                    <el-table-column v-for="item in skuLotList" :prop="item.prop" :label="item.label"
                                     show-overflow-tooltip></el-table-column>
                </template>
                <!-- 盘点任务表格 -->
                <template v-else-if="data.taskTypeCd===104">
                    <el-table-column prop="locCode" label="库位编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="locStatusDesc" label="库位状态" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="userName" label="用户名称" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="procTime" label="盘点时间" show-overflow-tooltip></el-table-column>
                </template>
                <!-- 尾箱打包任务 -->
                <template v-else-if="data.taskTypeCd===106">
                    <el-table-column prop="whName" label="库房" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="locCode" label="库位编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuCode" label="物品编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuName" label="物品名称" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="stockQty" label=" 库存数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="pickQty" label="拣货数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="packQty" label="打包数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="packStateDesc" label="打包状态" show-overflow-tooltip></el-table-column>
                </template>
                <!-- 拼托任务 -->
                <template v-else-if="data.taskTypeCd===107">
                    <el-table-column prop="soBillNo" label="订单编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="whName" label="库房" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuCode" label="物品编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuName" label="物品名称" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="lotNumber" label="批次号" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="pickRealQty" label="数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="wsuName" label="单位" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="fillLpnStateDesc" label="状态" show-overflow-tooltip></el-table-column>
                </template>
                <!-- 补货任务 -->
                <template v-else-if="data.taskTypeCd===108">
                    <el-table-column prop="skuCode" label="物品编码" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="skuName" label="物品名称" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="fromWhName" label="库房" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="fromZoneName" label="从库区" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="fromLocName" label="从库位" show-overflow-tooltip></el-table-column>
                     <el-table-column prop="fromLpnCode" v-if="isLpn" label="从容器" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="toWhName" label="至库房" show-overflow-tooltip v-if="false"></el-table-column>
                    <el-table-column prop="toZoneName" label="至库区" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="toLocName" label="至库位" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="toLpnCode" v-if="isLpn"  label="至容器" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="relQty" label="补货数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="realQty" label="实际数量" show-overflow-tooltip></el-table-column>
                    <el-table-column prop="wsuName" label="单位" show-overflow-tooltip></el-table-column>
                </template>
            </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogClose">关 闭</el-button>
        </span>
    </el-dialog>
</template>

<script>
import {getDetail} from "@/api/wms/core/task";
import {getParamValue} from "@/util/param";

export default {
    name: "detail",
    props: {
        visible: {type: Boolean, default: false},
        taskId: {type: String, default: undefined}
    },
    data() {
        return {
            isLpn:false,
            title: "任务详情",
            loading: false,
            paramValue: window.localStorage.getItem('paramValue'),
            data: {
                taskTypeCd: '',
                detail: [],

            },
            skuLotList: []
        }
    },
    mounted() {
         let params = JSON.parse(window.localStorage.getItem('saber-param'));
        let lpnParam = params.content.filter(item => item.paramKey=='system::lpnEnable');  
        let flag = lpnParam[0].paramValue;
        if(flag=='1'){
            this.isLpn = false;
        }else{
            this.isLpn = true;
        }
        const lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            let obj = {
                prop: 'skuLot' + i,
                label: '批属性' + i
            }
            this.skuLotList.push(obj);
        }
    },
    methods: {
        dialogClose() {
            let dialogResult = {
                visible: false
            }
            this.$emit('dialogResult', dialogResult);
        },
        dialogOpen() {
            this.loading = true;
            getDetail(this.taskId).then(res => {
                if (res.data.data.detail instanceof Array) {
                    this.data = res.data.data;
                } else {
                    this.data = {
                        taskTypeCd: '',
                        detail: []
                    }
                }
            }).finally(() => {
                this.loading = false;
            });
        },
    }
}
</script>

<style scoped>

</style>
