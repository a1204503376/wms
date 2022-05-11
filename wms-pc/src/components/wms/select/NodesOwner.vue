<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        value-key="woId"
        value-name="ownerCode"
        label-name="ownerName"
        :is-custom-template="true"
        :multiple="multiple"
       >

    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getOwnerSelectResponseList} from "@/api/wms/basics/owner";

export default {
    name: "NodesOwner",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        // 是否多选 true:多选 默认为单选
       multiple: {type: Boolean, required: false, default: false}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        val(newVal) {
            this.$emit('selectValChange', newVal);
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            const response = await getOwnerSelectResponseList();
            this.dataSource = response.data.data;
        },
        onChange(val) {
            console.log(val);
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
