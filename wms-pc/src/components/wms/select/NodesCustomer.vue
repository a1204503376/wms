<template>
    <el-select
        v-model="val"
        :collapse-tags="true"
        :default-first-option="true"
        :multiple="true"
        :loading="loading"
        :remote-method="remoteMethod"
        filterable
        placeholder="请输入客户编码或名称"
        remote
        reserve-keyword
        size="mini"
        value-key="code"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.code"
            :label="item.name"
            :value="item.id">
            <span style="float: left">{{ item.code }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.name }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getCustomerSelectResponseTop10List} from "@/api/wms/basics/customer";
import debounce from "lodash/debounce";

export default {
    name: "NodesCustomer",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String]
    },
    data() {
        return {
            options: [this.selectVal],
            val: this.selectVal,
            loading: false,
        }
    },
    methods: {
        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let customerSelectQuery = {
                    key
                };
                let {data: {data}} = await getCustomerSelectResponseTop10List(customerSelectQuery);
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
