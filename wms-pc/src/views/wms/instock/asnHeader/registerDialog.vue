<template>
    <editDialog ref="dialogEdit"
                title="到货登记"
                :visible="dialogEdit.visible"
                :isView="dialogEdit.isView"
                :isEdit="dialogEdit.isEdit"
                :group="dialogEdit.group"
                v-model="dialogEdit.dataSource"
                :data-source="dialogEdit.dataSource"
                :width="dialogEdit.width"
                :root="$parent"
                @callback="callback"
                @before-open="beforeOpen"
    ></editDialog>
</template>
<script>
    import editDialog from "@/components/nodes/editDialog";
    import {getList} from "@/api/wms/warehouse/platforminfo";
    import {add} from "@/api/wms/instock/register";

    export default {
        name: "registerDialog",
        components: {
            editDialog
        },
        props: {
            visible: {type: Boolean, default: false},
            dataSource: {
                type: Object, default: function () {
                    return null;
                }
            }
        },
        data() {
            return {
                dialogEdit: {
                    visible: false,
                    isView: false,
                    isEdit: false,
                    width: '40%',
                    dataSource: {},
                    group: [
                        {
                            column: [
                                {

                                    label: "库房",
                                    prop: "whId",
                                    type: "select",
                                    dicUrl: "/api/wms/warehouse/warehouse/list",
                                    props: {
                                        label: "whName",
                                        value: "whId"
                                    },
                                    rules: [{required: true, message: "库房不能为空", trigger: "change"}],
                                    disabled: true,
                                    span: 24,
                                },
                                {
                                    label: "送货牌ID",
                                    prop: "dcId",
                                    type: 'number',
                                    placeholder: '请输入数字',
                                    rules: [{required: true, message: "送货牌ID不能为空", trigger: "change"}],
                                    span: 24,
                                },
                                {
                                    label: "收货月台",
                                    prop: "spiId",
                                    type: "select",
                                    props: {
                                        value: 'spiId',
                                        label: 'platformName'
                                    },
                                    rules: [{required: true, message: "库房不能为空", trigger: "change"}],
                                    span: 24,
                                    change: function (val, obj, col, form) {
                                        if (obj) {
                                            form.platformCode = obj.platformCode;
                                            form.platformName = obj.platformName;
                                        } else {
                                            form.platformCode = undefined;
                                            form.platformName = undefined;
                                        }
                                    }
                                },
                                {
                                    label: "司机",
                                    prop: "driverName",
                                    span: 24,
                                },
                                {
                                    label: "电话",
                                    prop: "driverPhone",
                                    span: 24,
                                    rules: [
                                        {
                                            required: false,
                                            pattern: /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/,
                                            trigger: 'blur',
                                            message: '请输入正确电话'
                                        }
                                    ]
                                },
                                {
                                    label: "车牌",
                                    prop: "plateNumber",
                                    span: 24,
                                }
                            ]
                        }
                    ]
                }
            }
        },
        methods: {
            beforeOpen(done, type, finish) {
                getList({
                    whId: this.dialogEdit.dataSource.whId,
                    platformTypeStr: '1,3'
                }).then(res => {
                    this.$set(this.dialogEdit.group[0].column[2], 'dicData', res.data.data);
                }).finally(()=>{
                    done();
                });
            },
            callback(data) {
                if (data.success) {
                    // 保存到货登记
                    add(data.data).then(res => {
                        this.$emit('callback', data);
                    }).catch(()=>{
                        data.loading();
                    });
                } else {
                    this.$emit('callback', data);
                }
            }
        },
        watch: {
            visible: {
                handler: function (newValue) {
                    this.dialogEdit.visible = newValue;
                }
            },
            dataSource: function (newValue) {
                if (newValue) {
                    if (this.dialogEdit.dataSource.whId !== newValue.whId) {
                        getList({
                            whId: newValue.whId,
                            platformTypeDesc: '1,3'
                        }).then(res => {
                            this.$set(this.dialogEdit.group[0].column[2], 'dicData', res.data.data);
                        });
                        this.dialogEdit.dataSource.whId = newValue.whId;
                    }

                    this.dialogEdit.dataSource.asnBillId = newValue.asnBillId;
                }

            },
            deep: true,
            immediate: true
        }
    };
</script>
