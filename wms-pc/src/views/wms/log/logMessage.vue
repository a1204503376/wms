<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                    <el-row>
                        <template>
                            <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                                <el-tab-pane :label="item.lable" :name="item.name" v-for="(item, index) in tabList" :key="index">
                                    <el-table
                                        :data="table.data"
                                        border
                                        highlight-current-row
                                        size="mini"
                                        @sort-change="onSortChange"
                                        :row-class-name="tableRowClassName">

                                        <template v-for="(column,index) in table.columnList">
                                            <el-table-column
                                                v-if="!column.hide"
                                                :key="index"
                                                show-overflow-tooltip
                                                v-bind="column">
                                            </el-table-column>
                                        </template>
                                        <el-table-column
                                            fixed="right"
                                            label="操作"
                                            width="100">
                                            <template slot-scope="scope">
                                                <el-button v-if="scope.row.readed===0" @click="setRedaStatus(scope.row)" type="text" size="small" >设为已读</el-button>
                                                <el-button v-if="scope.row.readed===1" @click="setRedaStatus(scope.row)" type="text" size="small" >设为未读</el-button>
                                            </template>
                                        </el-table-column>

                                    </el-table>

                                </el-tab-pane>
                            </el-tabs>
                        </template>
                    </el-row>

            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >
                        关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>


<style>
.el-table .read-row {
    background: #DDDDDD;
}
</style>

<script>
import {editDetailMixin} from "@/mixins/editDetail";
// eslint-disable-next-line no-unused-vars
import {listMixin} from "@/mixins/list";
import {editLogMsgReaded, getLogMsgList} from "@/api/wms/log/log";

export default {
    name: "selectDeails",

    mixins: [editDetailMixin,listMixin],
    data() {
        return {
            num:2,
            that:this,
            //标签页list集合，根据这个集合循环出来Tab标签
            tabList:[
                {lable:'全部消息',name:'allMsg'},
                {lable:'已读消息',name:'readMsg'},
                {lable:'未读消息',name:'unread'}
            ],
            //tab标签默认打开第一个
            activeName:'allMsg',
            //Tab标签所有的数据来源
           table: {
                columnList: [
                    {
                        prop: 'whId',
                        label: '库房',
                        width: 120,
                        align: 'center'
                    },
                    {
                        prop: 'log',
                        label: '消息',
                        align: 'center'
                    },
                    {
                        prop: 'createTime',
                        label: '时间',
                        // left/center/right
                        align: 'center'
                    },
                ],
            },

        }
    },
    created() {
        this.getTableData(this.num);
        //初始化选择Tab行对象是allMsg日志行对象
    },
    methods: {
        tableRowClassName({row}) {
            if (row.readed === 1) {
                return 'read-row';
            } else  {
                return '';
            }

        },

        getTableData(num) {
            getLogMsgList(num)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj;
                });
        },
        getAllMsg() {
            this.num = 2;
            this.getTableData(this.num)
        },
        getReadMsg() {
            this.num = 1;
            this.getTableData(this.num)
        },
        getUnReadMsg() {
            this.num = 0;
            this.getTableData(this.num)
        },

        setRedaStatus(row){
            editLogMsgReaded(row.readed,row.id) .then(res => {
                this.$message.success('操作成功');
                this.getTableData(this.num);
            })
        },
        //点击Tab的时候进行判断，然后获取对应数据及行对象
        handleClick(tab) {
           if(tab.name == 'allMsg'){
               this.getAllMsg()
           }else if(tab.name == 'readMsg'){
             this.getReadMsg()
           }else{
               this.getUnReadMsg();
           }
        },


    }
}
</script>


