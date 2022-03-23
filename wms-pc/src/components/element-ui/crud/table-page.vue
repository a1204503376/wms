<template>
    <div v-if="pageFlag && crud.tableOption.page !== false"
         class="nodes-crud__pagination">
        <slot name="page"></slot>
        <el-pagination ref="pagination"
            :background="vaildData(defaultPage.background, config.pageBackground)"
                       :current-page.sync="defaultPage.currentPage"
                       :disabled="defaultPage.disabled"
                       :hide-on-single-page="vaildData(crud.tableOption.simplePage, config.simplePage)"
                       :layout="defaultPage.layout"
                       :page-size="defaultPage.pageSize"
                       :page-sizes="defaultPage.pageSizes"
                       :pager-count="defaultPage.pagerCount"
                       :total="defaultPage.total"
                       @size-change="sizeChange"
                       @prev-click="prevClick"
                       @next-click="nextClick"
                       @current-change="currentChange"
                       ></el-pagination>
    </div>

</template>

<script>
import config from "./config";

import memoryMixin from "@/components/core/mixin.js"

export default {
    mixins: [ memoryMixin ],
    name: "table-page",
    inject: ["crud"],
    props: {
        page: {
            type: Object,
            default: () => {
                return {}
            }
        }
    },
    mounted() {
        this.pageInit();
        this.crud.onLoad();
    },
    watch: {
        page: {
            handler() {
                this.pageInit();
            },
            deep: true,
        },
        pageFlag() {
            this.$nextTick(() => {
                this.crud.getTableHeight();
            })
        },
        //如果当前页面删除没数据了调用第一页
        'defaultPage.total': {
            handler() {
                if (this.defaultPage.total === (this.defaultPage.currentPage - 1) * this.defaultPage.pageSize && this.defaultPage.total != 0) {
                    this.defaultPage.currentPage = this.defaultPage.currentPage - 1;
                    this.crud.onLoad();

                    this.crud.$emit("current-change", this.defaultPage.currentPage);
                    this.updateValue();
                }
            },
            deep: true,
            immediate: true
        }
    },
    computed: {
        pageFlag() {
            return this.defaultPage.total != 0
        }
    },
    data() {
        return {
            config: config,
            defaultPage: {
                total: 0, // 总页数
                pagerCount: 7,//超过多少条隐藏
                currentPage: 1, // 当前页数
                pageSize: 20, // 每页显示多少条
                pageSizes: [20, 50, 100, 200, 500],
                layout: 'total, sizes, prev, pager, next, jumper',
                background: true // 背景颜色
            },
            memoryParamsKey: 'pageSize-params'
        }
    },
    methods: {
       pageInit() {
            this.defaultPage = Object.assign(this.defaultPage, this.page, {
                total: Number(this.page.total || this.defaultPage.total),
                pagerCount: Number(this.page.pagerCount || this.defaultPage.pagerCount),
                currentPage: Number(this.page.currentPage || this.defaultPage.currentPage),
                pageSize: Number(this.page.pageSize || this.defaultPage.pageSize)
            })
            this.updateValue();
        },
        getMemoryParams () {
            return {
                pageSize: this.defaultPage.pageSize,
                pageSizes:this.defaultPage.pageSizes,
            };
        },
        updateValue() {
            this.crud.$emit('update:page', this.defaultPage)
        },
        //下一页事件
        nextClick(val) {
            this.crud.$emit("next-click", val)
        },
        //上一页事件
        prevClick(val) {
            this.crud.$emit("prev-click", val)
        },
        // 页大小回调
        sizeChange(val) {
            this.defaultPage.currentPage = 1;
            this.defaultPage.pageSize = val;
            this.updateValue();
            this.crud.onLoad();
            this.crud.$emit("size-change", val);
        },
        // 页码回调
        currentChange(val) {
            this.updateValue();
            this.crud.onLoad();
            this.crud.$emit("current-change", val);
        }
    }
}
</script>

<style scoped>
.nodes-crud__pagination {
    position: relative;
    padding: 25px 0 20px 20px;
    text-align: right;
}
</style>
