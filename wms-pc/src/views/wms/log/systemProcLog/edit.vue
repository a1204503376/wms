<template>
    <el-dialog
        :title="title"
        :visible.sync="visible"
        :close-on-click-modal="false"
        @opened="dialogOpen"
        :before-close="dialogClose"
        v-dialogDrag="true"
        width="80%"
        top="3vh"
        id="my_dialogs"
        append-to-body
    >
        <el-container>
            <el-aside width="130px">
                <ul id="tabList">
                    <template v-if="data.serialList && data.serialList.length > 0">
                        <li :class="{active:index === 0}" @click="changeTab(0)">序列号</li>
                    </template>
                    <template v-if="data.serialLogList && data.serialLogList.length > 0">
                        <li :class="{active:index === 1}" @click="changeTab(1)">序列号日志</li>
                    </template>
                    <template v-if="data.lotList && data.lotList.length > 0">
                        <li :class="{active:index === 2}" @click="changeTab(2)">批次号</li>
                    </template>
                    <template v-if="data.lotLogList && data.lotLogList.length > 0">
                        <li :class="{active:index === 3}" @click="changeTab(3)">批次号日志</li>
                    </template>
                    <template v-if="data.stockOccupyList && data.stockOccupyList.length > 0">
                        <li :class="{active:index === 4}" @click="changeTab(4)">库存占用</li>
                    </template>
                    <template v-if="data.stockLogList && data.stockLogList.length > 0">
                        <li :class="{active:index === 5}" @click="changeTab(5)">库存日志</li>
                    </template>
                    <template v-if="data.truckSerialList && data.truckSerialList.length > 0">
                        <li :class="{active:index === 6}" @click="changeTab(6)">装车序列号</li>
                    </template>
                </ul>
            </el-aside>
            <el-main>
                    <div id="content" v-loading="loading">
                        <!-- 序列号 -->
                        <template v-if="data.serialList && data.serialList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">序列号</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.serialList"
                                                 :option="option.serialList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                        <!-- 序列号日志 -->
                        <template v-if="data.serialLogList && data.serialLogList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">序列号日志</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.serialLogList"
                                                 :option="option.serialLogList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                        <!-- 批次号 -->
                        <template v-if="data.lotList && data.lotList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">批次号</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.lotList"
                                                 :option="option.lotList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                        <!-- 批次号日志 -->
                        <template v-if="data.lotLogList && data.lotLogList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">批次号日志</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.lotLogList"
                                                 :option="option.lotLogList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                        <!-- 库存占用 -->
                        <template v-if="data.stockOccupyList && data.stockOccupyList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">库存占用</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.stockOccupyList"
                                                 :option="option.stockOccupyList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                        <!-- 库存日志 -->
                        <template v-if="data.stockLogList && data.stockLogList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">库存日志</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.stockLogList"
                                                 :option="option.stockLogList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                        <!-- 装车序列号 -->
                        <template v-if="data.truckSerialList && data.truckSerialList.length > 0">
                            <el-row :gutter="20" class="tabs-item">
                                <el-col :span="24">装车序列号</el-col>
                            </el-row>
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <child-table v-model="data.truckSerialList"
                                                 :option="option.truckSerialList"></child-table>
                                </el-col>
                            </el-row>
                        </template>
                    </div>
            </el-main>
        </el-container>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
import childTable from "@/components/nodes/childTable";
import {getParamValue} from "../../../../util/param";
import {getDetail as getSystemLog} from "@/api/wms/log/systemProcLog";

export default {
    name: "edit",
    components: {childTable},
    props: {
        visible: {type: Boolean, default: false},
        isView: {type: Boolean, default: false},
        dataSource: {type: String, default: ''}
    },
    data() {
        return {
            title: '系统日志-详细',
            loading: false,
            data: {
                serialList: [],
                serialLogList: [],
                lotList: [],
                lotLogList: [],
                stockOccupyList: [],
                stockLogList: [],
                truckSerialList: []
            },
            option: {
                serialList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: '库房',
                            prop: 'whName'
                        },
                        {
                            label: '序列号',
                            prop: 'serialNumber'
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode'
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName'
                        },
                        {
                            label: '批次号',
                            prop: 'lotNumber'
                        }
                    ]
                },
                serialLogList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: '库房',
                            prop: 'whName'
                        },
                        {
                            label: '序列号',
                            prop: 'serialNumber'
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode'
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName'
                        },
                        {
                            label: '批次号',
                            prop: 'lotNumber'
                        },
                        {
                            label: '处理类型',
                            prop: 'proTypeDesc'
                        },
                        {
                            label: '处理时间',
                            prop: 'procTime'
                        }
                    ]
                },
                lotList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: '库房',
                            prop: 'whName'
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode'
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName'
                        },
                        {
                            label: '批次号',
                            prop: 'lotNumber'
                        }
                    ]
                },
                lotLogList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: '库房',
                            prop: 'whName'
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode'
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName'
                        },
                        {
                            label: '批次号',
                            prop: 'lotNumber'
                        },
                        {
                            label: '处理类型',
                            prop: 'proTypeDesc'
                        },
                        {
                            label: '处理时间',
                            prop: 'procTime'
                        }
                    ]
                },
                stockOccupyList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: '事务ID',
                            prop: 'transId'
                        },
                        {
                            label: '占用类型',
                            prop: 'occupyTypeName'
                        },
                        {
                            label: '库房',
                            prop: 'whName'
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode'
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName'
                        },
                        {
                            label: '占用开始时间',
                            prop: 'occupyTime'
                        },
                        {
                            label: '占用数量',
                            prop: 'occupyQty'
                        },
                        {
                            label: '单据编码',
                            prop: 'soBillNo'
                        }
                    ]
                },
                stockLogList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: "所属库房",
                            prop: "whName",
                            width: 130,
                        },
                        {
                            label: "所属库区",
                            prop: "zoneName",
                            width: 150,
                        },
                        {
                            label: "库位编号",
                            prop: "locCode",
                            width: 150,
                        },
                        {
                            label: "容器编码",
                            prop: "lpnCode",
                            width: 120,
                        },
                        {
                            label: "货主",
                            prop: "ownerName",
                            width: 120,
                        },
                        {
                            label: "物品编码",
                            prop: "skuCode",
                            width: 100,
                        },
                        {
                            label: "物品名称",
                            prop: "skuName",
                            width: 200,
                        },
                        {
                            label: "计量单位",
                            prop: "wsuName",
                        },
                        {
                            label: "库存数量",
                            prop: "stockQty",
                        },
                        {
                            label: "下架数量",
                            prop: "pickQty",
                        },
                        {
                            label: "分配数量",
                            prop: "occupyQty",
                        },
                        {
                            label: "盘点占用数量",
                            prop: "countOccupyQty",
                            width: 120,
                        },
                        {
                            label: "批次号",
                            prop: "lotNumber",
                            width: 170,
                        },
                        {
                            label: "最近入库时间(库存数量增加时更新)",
                            prop: "lastInTime",
                            width: 250
                        },
                        {
                            label: "最近出库时间(下架数量增加时更新)",
                            prop: "lastOutTime",
                            width: 250
                        }
                    ]
                },
                truckSerialList: {
                    align: "center",
                    index: true,
                    indexLabel: "顺序",
                    select: true,
                    operationWidth: 120,
                    moveBtn: true,
                    menu: false,
                    column: [
                        {
                            label: '车次编号',
                            prop: 'truckCode'
                        },
                        {
                            label: '库房',
                            prop: 'whName'
                        },
                        {
                            label: '序列号',
                            prop: 'serialNumber'
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode'
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName'
                        },
                        {
                            label: '批次号',
                            prop: 'lotNumber'
                        }
                    ]
                }
            },
            index: 0,
            tabOffsetTop: [],//打开弹出层存数据
            ops: {
                vuescroll: {
                    mode: 'native',
                },
                scrollPanel: {
                    scrollingX: false,
                    scrollingY: true,
                    speed: 300,
                    maxHeight: 500
                },
                rail: {
                    background: 'red',
                    opacity: 0,
                    size: '6px',
                    specifyBorderRadius: false,
                    gutterOfEnds: null,
                    gutterOfSide: '2px',
                    keepShow: false,
                    border: '2px'
                },
                bar: {
                    background: 'rgba(144,146,152,0.3)',
                    onlyShowBarOnScroll: true,
                    keepShow: true
                }
            },
        }
    },
    created() {
        let lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            let column = {
                label: "批属性" + i,
                prop: 'skuLot' + i,
                width: 110
            };
            // 批属性
            this.option.lotList.column.push(Object.assign({}, column));
            // 批属性日志
            this.option.lotLogList.column.push(Object.assign({}, column));
            // 库存日志
            this.option.stockLogList.column.push(Object.assign({}, column));
        }
    },
    methods: {
        dialogOpen() {
            this.loading = true;
            let tabs = document.querySelectorAll('#content .tabs-item');
            this.tabOffsetTop = [];
            tabs.forEach(item => {
                this.tabOffsetTop.push(item.offsetTop);

            });
            getSystemLog(this.dataSource).then(res => {
                this.data = res.data.data;
                if (this.data.serialList && this.data.serialList.length > 0) {
                    this.index = 0;
                } else if (this.data.serialLogList && this.data.serialLogList.length > 0) {
                    this.index = 1;
                } else if (this.data.lotList && this.data.lotList.length > 0) {
                    this.index = 2;
                } else if (this.data.lotLogList && this.data.lotLogList.length > 0) {
                    this.index = 3;
                } else if (this.data.stockOccupyList && this.data.stockOccupyList.length > 0) {
                    this.index = 4;
                } else if (this.data.stockLogList && this.data.stockLogList.length > 0) {
                    this.index = 5;
                } else if (this.data.truckSerialList && this.data.truckSerialList.length > 0) {
                    this.index = 6;
                }
            }).finally(() => {
                this.loading = false;
            });
        }
        ,
        dialogClose() {
            let dialogResult = {
                visible: false
            };
            this.$emit("dialogResult", dialogResult);
        }
        ,
        changeTab(index) {
            this.isHandelScroll = true;
            this.index = index;
            this.$refs["vs"].scrollTo(
                {
                    y: this.tabOffsetTop[index] - 5
                },
                200,
            );
            setTimeout(() => {
                this.isHandelScroll = false;
            }, 200)
        }
        ,
        handleScroll(vertical, horizontal, nativeEvent) { //当内容滚动的回收触发
            if (this.isHandelScroll) {
                return;
            }
            let num = 500; //滚动div的高度
            let scrollHeight = nativeEvent.target.scrollHeight;
            let scrollTop = vertical.scrollTop;
            let countTabOffsetTop = this.tabOffsetTop.map(v => {
                return Math.abs(v - scrollTop);
            });
            let countTabOffsetTop2 = this.tabOffsetTop.map(v => {
                return (v - scrollTop);
            });
            let minNum = Math.min.apply(Math, countTabOffsetTop);
            let index = countTabOffsetTop.indexOf(minNum);
            if (countTabOffsetTop[index] < 50 && countTabOffsetTop2[index] >= 0) {
                this.index = index;
            }
            if (Math.abs(scrollTop - (scrollHeight - num)) <= 50) { //处理最后一个tab
                this.index = this.tabOffsetTop.length - 1;
            }
        }
        ,
    }
}
</script>

<style lang="scss" scoped>
.el-dialog__body {
    padding: 0px !important;
}

.el-main {
    padding: 0px !important;
}

// .el-row {
//     margin: 5px !important;
// }

#tabList {
    list-style: none;
    margin-top: 0;
    padding-top: 0;
    border-right: 1px solid #d2d6de;

    li {
        padding: 5px 20px 5px 20px;
        text-align-last: justify;
        cursor: pointer;

        &.active {
            text-align-last: justify;
            border-right: 2px solid #668eff;
        }
    }
}

#content {
    position: relative;
    padding-top: 10px;
    padding-right: 10px;
    font-size: 18px;
    min-height: 500px;
}
</style>
