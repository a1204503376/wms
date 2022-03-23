<template>
    <span>
        <editDialog ref="dialogEdit"
                    v-model="form"
                    title="分配方式"
                    :visible="visible"
                    :isEdit="false"
                    :isView="isView"
                    :group="group"
                    :dataSource="form"
                    width="40%"
                    :root="$parent"
                    :owner="owner"
                    @before-open="beforeOpen"
                    @callback="callback"
        ></editDialog>
        <pick-plan-dialog
            :dataSource="plan.dataSource"
            :isWellen="plan.isWellen"
            :visible="plan.visible"
            :wellenId="plan.wellenId"
            :targetLocId="plan.targetLocId"
            @callback="callbackPickPlan"
        >
        </pick-plan-dialog>
    </span>

</template>

<script>
import editDialog from "@/components/nodes/editDialog";
import {createPickPlanByWellen} from "@/api/wms/outstock/pickplan";
import {
    createPickPlan,
} from "@/api/wms/outstock/pickplan";
import pickPlanDialog from "../soHeader/pickPlanDialog";

export default {
    name: "planDialog",
    components: {
        editDialog,
        pickPlanDialog
    },
    props: {
        owner: {type: Object}
    },
    watch: {
        dataSource: {
            handler: function (newVal, oldVal) {
                this.form = Object.assign({}, newVal);
            },
            deep: true,
            immediate: true
        }
    },
    data() {
        return {
            form: {},
            isView: false,
            visible: false,
            params: {
                wellenId: undefined,
                locId: undefined,
                soBillIds: undefined,
            },
            plan: {
                visible: false,
                isWellen: false,
                wellenId: null,
                targetLocId: null,
            },
            group: [
                {
                    label: '',
                    column: [
                        {
                            prop: 'type',
                            label: '分配方式',
                            type: 'select',
                            span: 24,
                            dicData: [
                                {label: '先进先出', value: 0},
                                {label: '后进先出', value: 1},
                                {label: '批次范围', value: 2},
                                {label: '库区优先', value: 3},
                                {label: '指定批次', value: 4},
                                {label: '指定库位', value: 5},
                                {label: '指定库位&批次', value: 6}
                            ],
                            rules: [{required: true, message: "分配方式不能为空", trigger: "change"}],
                        },
                        {
                            prop: 'turnoverItem',
                            label: '周转类型',
                            type: 'select',
                            span: 24,
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.turnoverItem,
                            props: {
                                value: 'dictKey',
                                label: 'dictValue'
                            },
                            rules: [{required: true, message: "周转类型不能为空", trigger: "change"}],
                            hide: function (form, type) {
                                return form.type === 0 || form.type === 1;
                            }
                        },
                        {
                            prop: 'lotRange',
                            label: '批次范围',
                            type: 'date-range',
                            span: 24,
                            rules: [{required: true, message: "批次范围不能为空", trigger: "blur"}],
                            hide: function (form, type) {
                                return form.type === 2;
                            }
                        },
                        {
                            prop: 'zoneIds',
                            label: '库区',
                            type: 'select',
                            multiple: true,
                            props: {
                                label: 'zoneCode',
                                value: 'zoneId'
                            },
                            dicUrl: '/api/wms/warehouse/zone/list',
                            search: {
                                whId: 'whId'
                            },
                            span: 24,
                            rules: [{required: true, message: "库区不能为空", trigger: "change"}],
                            hide: function (form, type) {
                                return form.type === 3;
                            }
                        },
                        {
                            prop: 'lotNumbers',
                            label: '批次号',
                            type: 'input',
                            span: 24,
                            hide: function (form, type) {
                                return form.type === 4 || form.type === 6;
                            }
                        },
                        {
                            prop: 'locCodes',
                            label: '库位',
                            type: 'input',
                            span: 24,
                            hide: function (form, type) {
                                return form.type === 5 || form.type === 6;
                            }
                        },
                    ]
                },
            ],
        }
    },
    methods: {
        beforeOpen(done, type, finish) {
            this.form.type = 0;
            this.form.turnoverItem = 0;
            done();
        },
        callback(data) {
            if (data.success) {
                if (this.params.wellenId) {
                    let params = Object.assign(this.params, data.data);
                    createPickPlanByWellen(params).then(res => {
                        if (res.data.data.prompt) {
                            this.plan.dataSource = res.data.data;
                            this.plan.isWellen = true;
                            this.plan.visible = true;
                            this.plan.wellenId = params.wellenId;
                            this.plan.targetLocId = params.locId;
                        } else {
                            this.$emit('callback', {
                                success: true
                            });
                        }
                    }).finally(() => {
                        this.$refs.dialogEdit.loading.save = false;
                        this.$refs.dialogEdit.loading.content = false;
                    });
                } else {
                    let params = {
                        isWellen: false,
                        soBillIdList: this.params.soBillIds.split(","),
                        dto: data.data,
                    };
                    createPickPlan(params)
                        .then((res) => {
                            if (res.data.data.prompt) {
                                this.plan.dataSource = res.data.data;
                                this.plan.isWellen = false;
                                this.plan.visible = true;
                            } else {
                                this.$emit('callback', {
                                    success: true
                                });
                            }
                        }).finally(() => {
                            this.$refs.dialogEdit.loading.save = false;
                            this.$refs.dialogEdit.loading.content = false;
                        }
                    );
                 }
            } else {
                this.visible = data.visible;
                this.$emit('callback', {
                    success: false
                });
            }
        },
        show(wellenId, locId, soBillIds, whId) {
            this.params.wellenId = wellenId;
            this.params.locId = locId;
            this.params.soBillIds = soBillIds;
            this.params.whId = whId;
            this.visible = true;
        },
        callbackPickPlan(res) {
            this.plan.visible = res.visible;
            if (res.success) {
                this.visible = false;
                this.$emit('callback', {
                    success: res.success
                });
            } else {
                this.$emit('callback', {
                    success: false
                })
            }
        },
    }
}
</script>

<style scoped>

</style>
