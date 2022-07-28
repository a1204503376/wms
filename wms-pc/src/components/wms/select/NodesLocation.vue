<template>
    <el-select
        v-model="val"
        :default-first-option="true"
        :clearable="true"
        :multiple="multiple"
        :loading="loading"
        :remote-method="remoteMethod"
        filterable
        placeholder="请输入关键词"
        remote
        reserve-keyword
        :size="size"
        value-key="code"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.locCode"
            :label="item.locCode"
            :value="item.locId">
        </el-option>
    </el-select>
</template>

<script>
import debounce from "lodash/debounce";
import {getLocationSelectResponseTop10List} from "@/api/wms/basics/location";

export default {
    name: "NodesLocation",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
    },
    data() {
        return {
            options: [this.selectVal],
            val: this.selectVal,
            loading: false,
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    methods: {
        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let locationSelectQuery = {
                    key
                };
                let {data: {data}} = await getLocationSelectResponseTop10List(locationSelectQuery);
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
