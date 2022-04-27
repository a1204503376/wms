<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {stateService} from "@/api/wms/state/asnBillState";

export default {
    name: "NodesAsnBillState",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: Array
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
            this.dataSource = await stateService.getAsnBillState();
        }
    }
}
</script>

<style scoped>

</style>
