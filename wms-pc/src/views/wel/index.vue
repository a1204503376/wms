<template>
    <div class="index">
        <el-row :gutter="10" class="detailed">
            <el-col :span="18">
                <div class="left_center">
                    <div class="header">
                        <p>周转分析</p>
                    </div>
                    <div class="main">
                        <ul class="main_ul">
                            <li>待检区物料数量</li>
                            <li>{{ flowAnalysis.qcSkuQty }}</li>
                            <span class="um" >PCS</span>
                        </ul>
                        <ul class="main_ul">
                            <li>入库暂存区数量</li>
                            <li>{{ flowAnalysis.stageSkuQty }}</li>
                            <span class="um" >PCS</span>
                        </ul>
                        <ul class="main_ul">
                            <li>待检区物料存放天数</li>
                            <li>{{ flowAnalysis.qcSkuStoreDay }}</li>
                            <span class="um" >天</span>
                        </ul>
                        <ul class="main_ul">
                            <li>入库暂存区物料存放天数</li>
                            <li>{{ flowAnalysis.stageSkuStoreDay }}</li>
                            <span class="um" >天</span>
                        </ul>
                    </div>
                </div>
            </el-col>
            <el-col :span="6">
                <div class="right_center">
                    <div class="header">
                        <p>库存分析</p>
                    </div>
                    <el-row>
                        <el-col :span="12">
                            <ul class="main_ul">
                                <li>库存物品总数</li>
                                <li>{{ inventoryAnalysis.stockSkuCount }}</li>
                                <span class="um" >PSC</span>
                            </ul>
                        </el-col>
                        <el-col :span="12">
                            <ul class="main_ul">
                                <li>库位占用</li>
                                <li>{{ inventoryAnalysis.locOccupy }}</li>
                                <span class="um" >%</span>
                            </ul>
                        </el-col>
                    </el-row>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="10" class="bar">
            <el-col :span="12">
                <div id="barInStockSku" class="echarts-box"></div>
            </el-col>
            <el-col :span="12">
                <div id="barOutStockSku" class="echarts-box"></div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {getInStockRate, getOutStockRate, getStockData} from "@/api/wms/index/index.js";
import LabelData from "@/views/wms/basedata/enterprise/labelData";

export default {
    name: "index",
    data() {
        return {
            flowAnalysis: {
                qcSkuQty: 0,
                stageSkuQty: 0,
                qcSkuStoreDay: 0,
                stageSkuStoreDay: 0,
            },
            inventoryAnalysis: {
                stockSkuCount: 0,
                locOccupy: 0.00,
            },
            barInStockSku: null,
            barOutStockSku: null,
        };
    },
    components: {LabelData},
    created() {
    },
    mounted() {
        this.barInStockSku = this.$echarts.init(document.getElementById('barInStockSku'));
        this.barOutStockSku = this.$echarts.init(document.getElementById('barOutStockSku'));
        this.drawLine();
    },
    deactivated() {
    },
    methods: {
        drawLine() {
            // 调用接口获取数据
            getStockData().then(res => {
                this.flowAnalysis = res.data.data;
                this.inventoryAnalysis = res.data.data;
            })

            // 柱状图：一周内发货频率最高的前十个SKU
            getOutStockRate().then(res => {
                let data = [];//['曲轴', '调速弹簧', '上壳体', '活塞销', '连杆总成', '中间轴', '挺柱', '进气门', '飞轮', '链条'];
                let value = [];//[1500, 1749, 2938, 4737, 5013, 5380, 7530, 7739, 7839, 8374];
                res.data.data.forEach(item => {
                    data.push(item.skuCode);
                    value.push(item.pickRealQty);
                });
                this.barOutStockSku.setOption({
                    title: {
                        top: 10,
                        text: '一周发货数量前10 物品',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} : {c}'
                    },
                    xAxis: {
                        type: 'category',
                        data: data,
                        axisLabel: {
                            interval: 0, //控制X轴刻度全部显示
                            rotate: 45 //倾斜角度
                        }
                    },
                    grid:{//直角坐标系内绘图网格
                        // show:true,//是否显示直角坐标系网格。[ default: false ]
                        // left:"20%",//grid 组件离容器左侧的距离。
                        // right:"30px",
                        // borderColor:"#c45455",//网格的边框颜色
                        bottom:"20%" //
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: value,
                        type: 'bar',
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        }
                    }]
                });
            });

            // 柱状图：一周内收货频率最高的前十个SKU
            getInStockRate().then(res => {
                let data = [];
                let value = [];
                res.data.data.forEach(item => {
                    data.push(item.skuCode);
                    value.push(item.qty);
                });
                this.barInStockSku.setOption({
                    title: {
                        top: 10,
                        text: '一周收货数量前10 物品',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} : {c}'
                    },
                    xAxis: {
                        type: 'category',
                        data: data,
                        axisLabel: {
                            interval: 0, //控制X轴刻度全部显示
                            rotate: 45 //倾斜角度
                        }
                    },
                    yAxis: {
                        type: 'value'
                    },
                    grid:{//直角坐标系内绘图网格
                        // show:true,//是否显示直角坐标系网格。[ default: false ]
                        // left:"20%",//grid 组件离容器左侧的距离。
                        // right:"30px",
                        // borderColor:"#c45455",//网格的边框颜色
                        bottom:"20%" //
                    },
                    series: [{
                        data: value,
                        type: 'bar',
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        }
                    }]
                });
            });
        }
    }
};
</script>
<style lang="css">
* {
    margin: 0;
    padding: 0;
}

.left_center, .right_center {
    min-height: 220px;
}

.left_center, .right_center, .left_security, .right_storage {
    background: #fff;
}

.left_center .header, .right_center .header, .left_security .title, .right_storage .title {
    font-size: 16px;
    height: 50px;
    line-height: 50px;
    margin-left: 15px;
}

.left_center .main_ul {
    width: 20%;
    float: left;
    margin-left: 5%;
    border-right: none !important;
}

.right_center .main_ul {
    width: 60%;
    float: left;
    margin-left: 20%;
    border-right: none !important;
}

.main_ul li {
    font-size: 14px;
    /*font-weight: 700;*/
    list-style: none;
    padding: 10px 0;
    line-height: 17px;
}

.main_ul li:last-of-type {
    font-size: 30px;
    display: inline;
    font-weight: 600;
    /*color: #1e9fff;*/
}

.main_ul .um{
    margin-left: 10px;
    font-size: 14px;
}


.echarts-box {
    background: #fff;
    margin-top: 5px;
    height: 400px;
}

</style>
