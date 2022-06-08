<template>
    <el-select
        v-model="val"
        :multiple="true"
        :default-first-option="true"
        :loading="loading"
        :remote-method="remoteMethod"
        :collapse-tags="true"
        filterable
        placeholder="请输入物品编码或名称"
        remote
        reserve-keyword
        size="mini"
        style="width: 450px"
        value-key="skuCode"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.skuCode"
            :label="item.skuCode"
            :value="item.skuId">
            <span style="float: left">{{ item.skuCode }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.skuName }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getSkuSelectResponseTop10List} from "@/api/wms/basics/sku";
import debounce from "lodash/debounce";

export default {
    name: "NodesSku",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array],
    },
    data() {
        return {
            options: [],
            val: this.selectVal,
            loading: false,
        }
    },
    methods: {

        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let skuSelectQuery = {
                    key
                };
                let {data: {data}} = await getSkuSelectResponseTop10List(skuSelectQuery);
                this.options = data;
                this.loading = false;
            } else {
                this.options = [];
            }
        }, 500),
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>