<template>
    <basic-container id="container" style="height: inherit; overflow-y: auto">
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
        <el-row>
            <el-col :lg="18" :md="16" :xl="21">
                <div class="d-div-left">
                    <slot name="batchBtn"></slot>
                </div>
            </el-col>
            <el-col :lg="6" :md="8" :xl="3">
                <div class="d-div-right">
                    <slot name="tableTool"></slot>
                </div>
            </el-col>
        </el-row>
        <el-row type="flex">
            <el-col :span="24">
                <slot name="table"></slot>
            </el-col>
        </el-row>
        <el-row justify="end" type="flex">
            <slot name="page"></slot>
        </el-row>
    </basic-container>
</template>

<script>

export default {
    name: "NodesMasterPage",
    props: {
        configure: {
            type: Object,
            required: false,
            default: () => {
                return {
                    showExpandBtn: true,
                    showPage: true
                }
            }
        }
    },
    data() {
        return {
            config: {
                showExpandBtn: this.configure.showExpandBtn,
                showPage: this.configure.showPage
            },
            expandMore: false
        }
    },
    created() {
        this.autoTableHeight();
        window.addEventListener('resize', this.autoTableHeight);
    },
    methods: {
        hideOnSinglePage() {
        },
        handleSizeChange() {
        },
        handleCurrentChange() {
        },
        autoTableHeight() {
            this.$nextTick(() => {
                let height;
                if (this.config.showPage) {
                    height = window.innerHeight - 100;
                } else {
                    height = window.innerHeight - 50;
                }
                document.getElementById('container').style.height = `${height}px`;
            })
        },
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
