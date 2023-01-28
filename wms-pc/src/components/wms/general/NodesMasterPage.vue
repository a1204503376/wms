<template>
    <basic-container id="container" overflow-y:hidden style="position: relative;height: 100%;">
        <!-- 顶部搜索框布局 -->
        <div :class="expandMore ? 'top_search_expand': 'top_search_shrink'">
            <el-form :class="expandMore ? 'top_expand_form_expand' : ''" :inline="false" @submit.native.prevent
                     label-position="right" label-width="60" ref="searchForm" size="mini">
                <el-row type="flex">
                    <el-form-item label="筛选方案" label-width="90px">
                    </el-form-item>
                    <el-form-item label="" style="margin-left: 10px" v-for="(item,index) in queryPlanList">
						<span v-if="item.isDefault == 1">
							<el-button @click="queryConditionEvaluation(item)">
								{{'(默认)' + item.name}}
							</el-button>
						</span>
                        <span v-else>
							<el-button @click="queryConditionEvaluation(item)">
								{{item.name}}
							</el-button>
						</span>

                    </el-form-item>
                    <el-col style="height: 0px">

                        <el-form-item style="margin-top: -2px;margin-right: 0px;float:right;z-index: 100;">
                            <el-button @click="openDialog">
                                保存方案
                            </el-button>
                            <el-dropdown>
                                <el-button icon="el-icon-s-tools">
                                    方案设置
                                </el-button>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item @click.native="updateDefaultQueryPlan()">设置默认方案</el-dropdown-item>
                                    <el-dropdown-item @click.native="cancelDefaultQueryPlan()">取消默认方案</el-dropdown-item>
                                    <el-dropdown-item @click.native="deleteQueryPlan()">删除方案</el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </el-form-item>
                    </el-col>
                </el-row>
                <!-- 顶部搜索框布局:常用搜索框 -->
                <el-row type="flex" v-if="showSearchForm">
                    <el-col :lg="20" :md="19" :sm="18" :xl="21" :xs="18">
                        <slot name="searchFrom"></slot>
                    </el-col>

                    <el-col :lg="4" :md="5" :sm="6" :xl="3" :xs="6">
                        <div class="d-div-right">
                            <el-form-item>
                                <slot name="searchBtn"></slot>
                                <el-button @click="onSearch" native-type="submit" type="primary">
                                    查找
                                </el-button>
                                <el-button @click="onReset">重置</el-button>
                                <el-button :icon="expandMore ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
                                           @click="expandMore = !expandMore" type="text" v-if="showExpandBtn">
                                    {{ expandMore ? '收起' : '展开' }}
                                </el-button>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>

                <!-- 顶部搜索框布局:非常用搜索框 -->
                <!--                <div v-if="expandMore">-->
                <!--                    <slot name="expandSearch"></slot>-->
                <!--                </div>-->
            </el-form>
        </div>

        <div :class="expandMore ? 'main_body_expand' : ''">
            <el-divider></el-divider>
            <!-- 中间操作按钮行布局 -->
            <el-row style="margin-bottom: 5px;" type="flex">
                <el-col :lg="18" :md="16" :xl="21" style="margin-bottom: 0;">
                    <div class="d-div-left">
                        <slot name="batchBtn"></slot>
                    </div>
                </el-col>
                <el-col :lg="6" :md="8" :xl="3" style="margin-bottom: 0;">
                    <div class="d-div-right">
                        <slot name="tableTool"></slot>
                    </div>
                </el-col>
            </el-row>

            <!-- 页面主体表格布局 -->
            <el-row style="margin-top: 0;margin-bottom: 0;" type="flex">
                <el-col :span="24">
                    <slot name="table"></slot>
                </el-col>
            </el-row>

            <!-- 页面底部分页栏布局 -->
            <el-row justify="end" style="margin-top: 0;margin-bottom: 0;" type="flex">
                <slot name="page"></slot>
            </el-row>
        </div>
        <div>
            <template>
                <el-dialog :visible.sync="dialogVisible" append-to-body title="保存方案" v-dialogDrag width="20%">
                    <el-form>
                        <el-form-item label="方案名称" label-width="110px" style="margin-top: 5px">
                            <el-input class="search-input" placeholder="请输入方案名称" style="width: 80%"
                                      v-model="form.params.name">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="初始化数据" label-width="110px">
                            <template>
                                <el-checkbox checked v-model="form.params.isInitDataState">是</el-checkbox>
                            </template>
                        </el-form-item>
                        <el-form-item label="是否默认方案" label-width="110px">
                            <template>
                                <el-checkbox v-model="form.params.isDefaultState">是</el-checkbox>
                            </template>
                        </el-form-item>
                    </el-form>
                    <span class="dialog-footer" slot="footer">
						<el-button @click="dialogVisible = false">取 消</el-button>
						<el-button @click="saveQueryPlan" type="primary">确 定</el-button>
					</span>
                </el-dialog>
            </template>
        </div>
    </basic-container>

</template>

<script>
    import {
        findAllQueryPlan,
        insertQueryPlan,
        deleteQueryPlan,
        updateDefaultQueryPlan,
        cancelDefaultQueryPlan
    } from '@/api/wms/queryPlan/queryPlan'
    import func from "../../../util/func";

    export default {
        name: "NodesMasterPage",
        props: {
            showExpandBtn: {
                type: Boolean,
                required: false,
                default: () => true
            },
            showPage: {
                type: Boolean,
                required: false,
                default: () => true
            },
            showSearchForm: {
                type: Boolean,
                required: false,
                default: () => true
            }
        },
        data() {
            return {
                expandMore: false,
                dialogVisible: false,
                form: {
                    params: {
                        name: '',
                        isDefault: 0,
                        isInitData: 1,
                        pageUrl: '',
                        queryData: '',
                        isDefaultState: false,
                        isInitDataState: true,
                    }
                },
                queryPlanList: [],
                selectedSchemeId: 0
        }
        },
        created() {
            console.log(this.$parent.form)
            this.getQueryPlanList();
            this.autoTableHeight();
            window.addEventListener('resize', this.autoTableHeight);
        },
        mounted() {
            window.getQueryPlanList = this.getQueryPlanList;
        },
        methods: {
            autoTableHeight() {
                this.$nextTick(() => {
                    let height;
                    if (this.showPage) {
                        height = window.innerHeight - 100;
                    } else {
                        height = window.innerHeight - 50;
                    }
                    document.getElementById('container').style.height = `${height}px`;
                    this.$emit("changeTableHeight", `${height - 180}`);
                })
            },
            onSearch() {
                if (this.expandMore) {
                    this.expandMore = false;
                }
                this.$emit('search');
            },
            onReset() {
                this.$emit('reset');
            },
            saveQueryPlan() {
                if (this.form.params.isInitDataState) {
                    this.form.params.isInitData = 1;
                } else {
                    this.form.params.isInitData = 0;
                }

                if (this.form.params.isDefaultState) {
                    this.form.params.isDefault = 1;
                } else {
                    this.form.params.isDefault = 0;
                }
                this.form.params.pageUrl = this.$parent.$route.path;
                this.form.params.queryData = JSON.stringify(this.$parent.form.params);
                insertQueryPlan(this.form.params).then(() => {
                    this.$message({
                        type: 'success',
                        message: '保存查询方案成功!'
                    });
                    this.getQueryPlanList();
                    this.form.params.name = '';
                    this.form.params.isDefault = 0;
                    this.form.params.isInitData = 1;
                    this.form.params.isDefaultState = false;
                    this.form.params.isInitDataState = true;
                    this.form.params.pageUrl = '';
                    this.form.params.queryData = '';
                    this.dialogVisible = false;
                })
            },
            deleteQueryPlan(){
                let params =  {
                    id : this.selectedSchemeId
                }
                deleteQueryPlan(params).then((data) => {
                    this.getQueryPlanList();
                })
            },
            updateDefaultQueryPlan(){
                let params =  {
                    id : this.selectedSchemeId,
                    pageUrl : this.$parent.$route.path
                }
                updateDefaultQueryPlan(params).then((data) => {
                    this.getQueryPlanList();
                })
            },
            cancelDefaultQueryPlan(){
                let params =  {
                    id : this.selectedSchemeId,
                    pageUrl : this.$parent.$route.path
                }
                cancelDefaultQueryPlan(params).then((data) => {
                    this.getQueryPlanList();
                })
            },
            queryConditionEvaluation(data) {
                this.selectedSchemeId = data.id;
                this.$emit('queryConditionEvaluation', data);
            },
            getQueryPlanList() {
                let params = {};
                params.pageUrl = this.$route.path;
                findAllQueryPlan(params).then(({
                                                   data: {
                                                       data
                                                   }
                                               }) => {
                    this.queryPlanList = data;
                    if (this.queryPlanList.length == 0) {

                    } else if (this.queryPlanList.length == 1) {
                        this.queryConditionEvaluation(this.queryPlanList[0]);
                    } else {
                        let index = this.queryPlanList.findIndex(item => item.isDefault == 1);
                        if (index > 0) {
                            this.queryConditionEvaluation(this.queryPlanList[index]);
                        }else {
                            this.queryConditionEvaluation(this.queryPlanList[0]);
                        }
                    }
                })

            },
            openDialog() {
                this.dialogVisible = true;
            },
            handlerKeyCode(e) {
                let key;
                if (event === undefined) {
                    key = e.keyCode;
                } else {
                    key = event.keyCode;
                }
                if (key === 13) {
                    this.onSearch();
                }
            }
        },
        activated() {
            window.addEventListener('keydown', this.handlerKeyCode, true) //开启监听键盘按下事件
        },
        deactivated() {
            window.removeEventListener('keydown', this.handlerKeyCode, true) //移除监听键盘按下事件
        },
        beforeDestroy() {
            window.removeEventListener('resize', this.autoTableHeight)
            window.removeEventListener('keydown', this.handlerKeyCode, true)
        },
    }
</script>

<style lang="scss">
    .el-dropdown-link {
        .el-icon--right {
            margin-left: 0;
        }
    }

    #nodes_form_container {
        padding-top: 1px;
        padding-left: 1px;
        padding-right: 1px;
        display: flex;
        height: inherit;
        background: #fff;
        flex-direction: column;
    }

    #nodes_form_container .stockcount {
        display: flex;
        flex-direction: row;
        align-items: center;
    }

    .myTable-box {
        position: relative;
    }

    .myTable-box .el-table {
        overflow: inherit;
    }

    .top_search_shrink {
        line-height: 30px;
        height: 60px;
        padding: 5px 5px 0 5px;
        //margin-bottom: 10px;
        overflow: hidden;
    }

    .top_search_expand {
        box-shadow: 0 2px 12px #0000001a;
        padding: 10px;
        display: flex;
        width: calc(100% - 63px);
        z-index: 100;
        position: absolute;
        background-color: #FFFFFF;
    }

    .top_expand_form_expand {
        width: 100%;
    }

    .main_body_expand {
        margin-top: 47px;
    }
</style>
