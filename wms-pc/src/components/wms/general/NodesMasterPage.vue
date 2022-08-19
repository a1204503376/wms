<template>
    <basic-container id="container" overflow-y:hidden style="position: relative;height: 100%;">
        <!-- 顶部搜索框布局 -->
        <div :class="expandMore ? 'top_search_expand': 'top_search_shrink'">
            <el-form ref="searchForm"
                     :class="expandMore ? 'top_expand_form_expand' : ''"
                     :inline="false"
                     label-position="right"
                     label-width="60"
                     size="mini"
                     @submit.native.prevent>

                <!-- 顶部搜索框布局:常用搜索框 -->
                <el-row v-if="showSearchForm" type="flex">
                    <el-col :xs="18" :sm="18" :md="19" :lg="20" :xl="21">
                        <slot name="searchFrom"></slot>
                    </el-col>

                    <el-col :xs="6" :sm="6" :md="5" :lg="4" :xl="3">
                        <div class="d-div-right">
                            <el-form-item>
                                <slot name="searchBtn"></slot>
                                <el-button native-type="submit" type="primary"
                                           @click="onSearch">
                                    查找
                                </el-button>
                                <el-button @click="onReset">重置</el-button>
                                <el-button v-if="showExpandBtn"
                                           :icon="expandMore ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" type="text"
                                           @click="expandMore = !expandMore">{{ expandMore ? '收起' : '展开' }}
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

    </basic-container>

</template>

<script>

export default {
    name: "NodesMasterPage",
    props: {
        showExpandBtn: {type: Boolean, required: false, default: () => true},
        showPage: {type: Boolean, required: false, default: () => true},
        showSearchForm: {type: Boolean, required: false, default: () => true}
    },
    data() {
        return {
            expandMore: false,
        }
    },
    created() {
        this.autoTableHeight();
        window.addEventListener('resize', this.autoTableHeight);
        window.addEventListener('keydown', this.handlerKeyCode, true)//开启监听键盘按下事件
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
            if (this.expandMore){
                this.expandMore = false;
            }
            this.$emit('search');
        },
        onReset() {
            this.$emit('reset');
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
    beforeDestroy() {
        window.removeEventListener('resize', this.autoTableHeight)
    }
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
    height: 30px;
    padding: 5px 5px 0 5px;
    //margin-bottom: 10px;
    overflow: hidden;
}

.top_search_expand {
    box-shadow: 0 2px 12px #0000001a;
    padding: 5px;
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
