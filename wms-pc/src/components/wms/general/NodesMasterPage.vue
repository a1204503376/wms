<template>
    <basic-container id="container" style="height: inherit; overflow-y: auto">
        <el-form ref="searchForm"
                 :inline="true"
                 label-position="right"
                 label-width="60"
                 size="mini"
                 @submit.native.prevent>
            <el-row type="flex">
                <el-col :lg="18" :md="20" :xl="20">
                    <div class="d-div-left">
                        <slot name="searchFrom"></slot>
                    </div>
                </el-col>
                <el-col :lg="6" :md="4" :xl="4">
                    <div class="d-div-right">
                        <el-form-item>
                            <slot name="searchBtn"></slot>
                            <el-button v-if="permission.search"
                            native-type="submit"
                            type="primary"
                            @click="onSearch">
                            查找
                            </el-button>
                            <el-button @click="onReset">重置</el-button>
                            <el-button v-if="config.showExpandBtn"
                                       :icon="expandMore ?'el-icon-arrow-up':'el-icon-arrow-down'" type="text"
                                       @click="expandMore =!expandMore">{{ expandMore ? '收起' : '展开' }}
                            </el-button>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <div v-if="expandMore">
                <slot name="expandSearch"></slot>
            </div>
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
            <el-row style="margin-top: 0;margin-bottom: 0;" type="flex">
                <el-col :span="24">
                    <slot name="table"></slot>
                </el-col>
            </el-row>
            <el-row justify="end" style="margin-top: 0;margin-bottom: 0;" type="flex">
                <slot name="page"></slot>
            </el-row>
        </el-form>
    </basic-container>

</template>

<script>

export default {
    name: "NodesMasterPage",
    props: {
        showExpandBtn:{type:Boolean,required:false,default:()=>true},
        showPage:{type:Boolean,required:false,default:()=>true},
        permission:{type:Object,required:true}
    },
    data() {
        return {
            config: {
                showExpandBtn: this.showExpandBtn,
                showPage: this.showPage
            },
            expandMore: false
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
                if (this.config.showPage) {
                    console.log('innerHeight', window.innerHeight);
                    height = window.innerHeight - 100;
                } else {
                    console.log('notPage', window.innerHeight);
                    height = window.innerHeight - 50;
                }
                document.getElementById('container').style.height = `${height}px`;
            })
        },
        onSearch() {
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
    padding-top: 10px;
    padding-left: 10px;
    padding-right: 10px;
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


</style>
