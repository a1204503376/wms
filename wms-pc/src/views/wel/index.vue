<template>
    <div class="index">
        <el-row class="main" type="flex">
            <el-col :span="5">
                <div class="main_div1">
                         <span class="icon">
                            <i class="iconfont icon-duijizhutu"/>
                         </span>
                    <span class="val">
                        {{ flowAnalysis.qcSkuQty }}
                        <label class="um">PCS</label>
                    </span>
                </div>
                <div class="main_lab">
                    <span>待检区物料数量</span>
                </div>
            </el-col>
            <el-col :span="5">
                <div class="main_div1">
                         <span class="icon">
                            <i class="iconfont icon-zhuzhuangtu"/>
                         </span>
                    <span class="val">
                        {{ flowAnalysis.stageSkuQty }}
                        <label class="um">PCS</label>
                    </span>
                </div>
                <div class="main_lab">
                    <span>入库暂存区数量</span>
                </div>
            </el-col>
            <el-col :span="3">
                <div class="main_div2">
                         <span class="icon">
                            <i class="iconfont icon-rili"/>
                         </span>
                    <span class="val">
                        {{ flowAnalysis.qcSkuStoreDay }}
                        <label class="um">天</label>
                    </span>
                </div>
                <div class="main_lab">
                    <span>待检区物料存放天数</span>
                </div>
            </el-col>
            <el-col :span="3">
                <div class="main_div2">
                         <span class="icon">
                            <i class="iconfont icon-rili1"/>
                         </span>
                    <span class="val">
                        {{ flowAnalysis.stageSkuStoreDay }}
                        <label class="um">天</label>
                    </span>
                </div>
                <div class="main_lab">
                    <span>入库暂存区物料存放天数</span>
                </div>
            </el-col>
            <el-col :span="5">
                <div class="main_div1">
                     <span class="icon">
                        <i class="iconfont icon-kucuntongji"/>
                     </span>
                    <span class="val">
                        {{ inventoryAnalysis.stockSkuCount }}
                         <label class="um">PCS</label>
                    </span>
                </div>
                <div class="main_lab">
                    <span>库存物品总数</span>
                </div>
            </el-col>
            <el-col :span="3">
                <div class="main_div2">
                         <span class="icon">
                            <i class="iconfont icon-shuliangtongji"/>
                         </span>
                    <span class="val">
                        {{ inventoryAnalysis.locOccupy }}
                        <span class="um">%</span>
                    </span>
                </div>
                <div class="main_lab">
                    <span>库位占用</span>
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
import "../../../public/cdn/iconfont/index/iconfont.css"

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
    created() {
    },
    mounted() {
        window.addEventListener('resize', this.autoEcharsWidth);
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
                let data = [];
                let value = [];
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
                    grid: {//直角坐标系内绘图网格
                        // show:true,//是否显示直角坐标系网格。[ default: false ]
                        // left:"20%",//grid 组件离容器左侧的距离。
                        // right:"30px",
                        // borderColor:"#c45455",//网格的边框颜色
                        bottom: "20%" //
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
                    grid: {//直角坐标系内绘图网格
                        // show:true,//是否显示直角坐标系网格。[ default: false ]
                        // left:"20%",//grid 组件离容器左侧的距离。
                        // right:"30px",
                        // borderColor:"#c45455",//网格的边框颜色
                        bottom: "20%" //
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
        },
        autoEcharsWidth() {
            this.barOutStockSku.resize();
            this.barInStockSku.resize();
        },
    }
};
</script>
<style lang="css">
* {
    margin: 0;
    padding: 0;
}

.main {
    background: #fff;
    min-height: 220px;
}

.main_div1 {
    border-right:1px solid #ccc;
    margin: 15% 0 0 10%;
}

.main_div2 {
    border-right:1px solid #ccc;
    margin: 26% 0 0 10%;
}

.main .val {
    display: block;
    white-space: nowrap;
    overflow: hidden;
    padding-left: 10px;
    line-height: 75px;
    font-size: 30px;
    font-weight: 600;
}

.main .um {
    margin-left: 5px;
    font-size: 15px;
}

.main_lab {
    overflow: hidden;
    text-align: center;
}

.icon {
    float: left;
    margin: 5% 0 0 0;
    display: block;
    background-color: #f3f6f8;
    width: 50px;
    height: 50px;
    border-radius: 150px;
    text-align: center;
}

.icon i {
    display: block;
    margin-top: 13%;
    font-size: 35px;
}

.echarts-box {
    background: #fff;
    margin-top: 5px;
    height: 400px;
}

</style>
