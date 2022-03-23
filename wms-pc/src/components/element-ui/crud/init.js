import slot from "../../core/slot";
import request from "@/router/axios";

export default function () {
    return {
        mixins: [slot],
        props: {
            option: {
                type: Object,
                required: true,
                default: () => {
                    return {};
                }
            }
        },
        watch: {
            option: {
                handler() {
                    this.init(false);
                },
                deep: true,
            }
        },
        data() {
            return {
                DIC: {},
                cascaderDIC: {},
                tableOption: {},
                isMobile: ''
            };
        },
        created() {
            this.init();
        },
        computed: {
            objectOption() {
                let obj = {};
                this.propOption.forEach(ele => obj[ele.prop] = ele);
                return obj;
            },
            resultOption() {
                return Object.assign(this.deepClone(this.tableOption), {
                    column: this.propOption
                })
            },
            rowKey() {
                return this.tableOption.rowKey || "id";
            },
            formRules() {
                let result = {};
                this.propOption.forEach(ele => {
                    if (ele.rules && ele.display !== false)
                        result[ele.prop] = ele.rules
                });
                return result;
            },
            isMediumSize() {
                return this.controlSize;
            },
            controlSize() {
                return this.tableOption.size || this.$AVUE.size || 'small';
            }
        },
        methods: {
            init(type) {
                this.tableOption = this.option;
                this.getIsMobile();
                this.columnInit && this.columnInit();
                // this.handleLocalDic();
                // if (type !== false) this.handleLoadDic()

                if (type !== false && this.option && this.option.column) {
                    this.option.column.forEach(col => {
                        if (!col.dicUrl && (!col.dicData || col.dicData.length == 0)) {
                            return;
                        }
                        if (!col.props) {
                            col.props = {
                                label: 'label',
                                value: 'value'
                            }
                        }
                        if (col.dicUrl) {
                            request({
                                url: col.dicUrl,
                                method: 'get'
                            }).then(res => {
                                this.$set(this.DIC, col.prop, res.data.data);
                            });
                        } else {
                            this.$set(this.DIC, col.prop, col.dicData);
                        }
                    })
                }
            },
            getIsMobile() {
                this.isMobile = window.document.body.clientWidth <= 768;
            },
        }
    };
}
